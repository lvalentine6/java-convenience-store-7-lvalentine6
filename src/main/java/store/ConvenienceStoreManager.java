package store;

import java.util.List;

import store.domain.membershop.Membership;
import store.domain.order.Order;
import store.domain.order.OrderItem;
import store.domain.order.OrderLineItem;
import store.domain.product.Product;
import store.domain.product.PromotionOrderResult;
import store.domain.product.PromotionOrderStatus;
import store.domain.stock.Stock;
import store.domain.promotion.Promotions;
import store.io.IoHandler;

public class ConvenienceStoreManager {
	private static final String PRODUCT_FILE_PATH_NAME = "/products.md";
	private static final String PROMOTION_FILE_PATH_NAME = "/promotions.md";

	private final IoHandler ioHandler;
	private final Promotions promotions;
	private final Stock stock;
	private final Membership membership;

	private ConvenienceStoreManager(IoHandler ioHandler, Promotions promotions, Stock stock, Membership membership) {
		this.ioHandler = ioHandler;
		this.promotions = promotions;
		this.stock = stock;
		this.membership = membership;
	}

	public static ConvenienceStoreManager from(IoHandler ioHandler) {
		Promotions promotions = Promotions.from(ioHandler.getPrimitivePromotionInfos(PROMOTION_FILE_PATH_NAME));
		List<Product> products = ioHandler.getPrimitiveProductInfos(PRODUCT_FILE_PATH_NAME).stream()
			.map(info -> Product.of(info, promotions.findByName(info.promotion())))
			.toList();

		Stock stock = Stock.from(products);
		Membership membership = Membership.create();
		return new ConvenienceStoreManager(ioHandler, promotions, stock, membership);
	}

	public void run() {
		while (true) {
			ioHandler.showSellingProducts(stock.getProducts());
			Order order = createOrder();
			ioHandler.showReceipt(order);
			stock.deductStocks(order.getOrderLineItems());
			if (getContinueAnswer().equals("N")) {
				return;
			}
		}
	}

	private Order createOrder() {
		List<OrderItem> validOrderItems = getOrderItems();
		List<OrderLineItem> orderLineItems = getOrderLineItems(validOrderItems);
		String hasMembership = getApplyMembershipAnswer();
		return Order.of(orderLineItems, membership, hasMembership);
	}

	private List<OrderItem> getOrderItems() {
		while (true) {
			try {
				List<OrderItem> orderItems = ioHandler.getOrderItems();
				stock.validateOrderItems(orderItems);
				return orderItems;
			} catch (IllegalStateException invalidOrderItems) {
				ioHandler.showExceptionMessage(invalidOrderItems.getMessage());
			}
		}
	}

	private List<OrderLineItem> getOrderLineItems(List<OrderItem> validOrderItems) {
		return validOrderItems.stream()
			.map(this::createOrderLineItem)
			.toList();
	}

	private OrderLineItem createOrderLineItem(OrderItem orderItem) {
		if (stock.isPromotionAndValidPromotion(orderItem)) {
			return getPromotionProductInfo(orderItem);
		}
		return stock.getNormalProductInfo(orderItem);
	}

	private OrderLineItem getPromotionProductInfo(OrderItem orderItem) {
		PromotionOrderStatus promotionOrderStatus = stock.getPromotionOrderStatus(orderItem);

		if (promotionOrderStatus.result() == PromotionOrderResult.INSUFFICIENT_STOCK) {
			return handleInsufficientStock(orderItem, promotionOrderStatus);
		}

		if (promotionOrderStatus.result() == PromotionOrderResult.BELOW_QUANTITY) {
			return handleBelowQuantity(orderItem, promotionOrderStatus);
		}

		return handleExactQuantity(orderItem, promotionOrderStatus);
	}

	private OrderLineItem handleInsufficientStock(OrderItem orderItem, PromotionOrderStatus status) {
		String answer = getInsufficientStockAnswer(orderItem.name(), status.displayQuantity());
		if (answer.equals("N")) {
			return OrderLineItem.ofPromotionOnly(orderItem.name(), status);
		}
		return OrderLineItem.ofMixedQuantity(orderItem.name(), status);
	}

	private String getInsufficientStockAnswer(String name, int quantity) {
		while (true) {
			try {
				return ioHandler.getNonDisCount(name, quantity);
			} catch (IllegalArgumentException invalidInput) {
				ioHandler.showExceptionMessage(invalidInput.getMessage());
			}
		}
	}

	private OrderLineItem handleBelowQuantity(OrderItem orderItem, PromotionOrderStatus status) {
		String answer = getBlowStockAnswer(orderItem.name(), status.additionalQuantity());

		if (answer.equals("Y")) {
			return OrderLineItem.ofAdditionalQuantity(orderItem, status);
		}
		return OrderLineItem.ofNonPromotional(orderItem.name(), status);
	}

	private String getBlowStockAnswer(String name, int quantity) {
		while (true) {
			try {
				return ioHandler.getApplyPromotion(name, quantity);
			} catch (IllegalArgumentException invalidInput) {
				ioHandler.showExceptionMessage(invalidInput.getMessage());
			}
		}
	}

	private OrderLineItem handleExactQuantity(OrderItem orderItem, PromotionOrderStatus status) {
		return OrderLineItem.ofExactPromotion(orderItem.name(), status);
	}

	private String getApplyMembershipAnswer() {
		while (true) {
			try {
				return ioHandler.getApplyMembership();
			} catch (IllegalArgumentException invalidInput) {
				ioHandler.showExceptionMessage(invalidInput.getMessage());
			}
		}
	}

	private String getContinueAnswer() {
		while (true) {
			try {
				return ioHandler.getContinue();
			} catch (IllegalArgumentException invalidInput) {
				ioHandler.showExceptionMessage(invalidInput.getMessage());
			}
		}
	}
}
