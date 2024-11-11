package store.domain.stock;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import store.domain.order.OrderItem;
import store.domain.order.OrderLineItem;
import store.domain.product.PrimitiveProductInfo;
import store.domain.product.Product;
import store.domain.product.PromotionOrderResult;
import store.domain.product.PromotionOrderStatus;
import store.domain.promotion.Promotion;
import store.validator.stock.StockValidateMessage;

public class Stock {
	private final List<Product> products;

	private Stock(List<Product> products) {
		validateDuplicateProduct(products);
		validateDuplicatePromotion(products);
		this.products = addOutOfStockVersions(products);
	}

	public static Stock from(List<Product> products) {
		return new Stock(products);
	}

	private void validateDuplicateProduct(List<Product> products) {
		Map<String, Long> productCount = products.stream()
			.filter(product -> !product.hasPromotion())
			.collect(Collectors.groupingBy(Product::getName, Collectors.counting()));

		if (productCount.values().stream().anyMatch(count -> count > 1)) {
			throw new IllegalArgumentException(StockValidateMessage.INVALID_DUPLICATE_PRODUCT.getMessage());
		}
	}

	private void validateDuplicatePromotion(List<Product> products) {
		Map<String, Long> promotionCount = products.stream()
			.filter(Product::hasPromotion)
			.collect(Collectors.groupingBy(Product::getName, Collectors.counting()));

		if (promotionCount.values().stream().anyMatch(count -> count > 1)) {
			throw new IllegalArgumentException(StockValidateMessage.INVALID_DUPLICATE_PROMOTIONS.getMessage());
		}
	}

	private List<Product> addOutOfStockVersions(List<Product> products) {
		Set<String> hasNormalVersions = findProductsWithNormalVersion(products);
		List<Product> result = new ArrayList<>();
		products.forEach(p -> {
			result.add(p);
			if (p.hasPromotion() && !hasNormalVersions.contains(p.getName())) {
				result.add(createOutOfStockVersion(p));
			}
		});
		return result;
	}

	private Set<String> findProductsWithNormalVersion(List<Product> products) {
		return products.stream()
			.filter(p -> !p.hasPromotion())
			.map(Product::getName)
			.collect(Collectors.toSet());
	}

	private Product createOutOfStockVersion(Product product) {
		return Product.of(
			new PrimitiveProductInfo(
				product.getName(),
				product.getPrice(),
				0,
				null),
			null
		);
	}

	public List<Product> getProducts() {
		return products;
	}

	public void validateOrderItems(List<OrderItem> orderItems) {
		List<String> orderNames = orderItems.stream()
			.map(OrderItem::name)
			.toList();

		validateNames(orderNames);

		for (OrderItem orderItem : orderItems) {
			validateOrderItem(orderItem);
		}
	}

	public boolean isPromotionAndValidPromotion(OrderItem orderItem) {
		Product findProduct = findPromotionProductByName(orderItem.name());
		if (findProduct == null) {
			return false;
		}
		Promotion promotion = findProduct.getPromotion();
		return findProduct.getQuantity() > 0 && promotion.isValidPromotion();
	}

	public PromotionOrderStatus getPromotionOrderStatus(OrderItem orderItem) {
		Product findProduct = findPromotionProductByName(orderItem.name());
		if (findProduct.getQuantity() > 0 && findProduct.getQuantity() < orderItem.quantity()) {
			return calculateRemainQuantity(findProduct, orderItem);
		}
		return calculateFreeQuantity(findProduct, orderItem);
	}

	public void deductStocks(List<OrderLineItem> orderLineItems) {
		orderLineItems.forEach(this::deductStockForOrder);
	}

	private void deductStockForOrder(OrderLineItem orderLineItem) {
		String productName = orderLineItem.getName();
		Product promotionProduct = findPromotionProductByName(productName);

		if (promotionProduct != null && orderLineItem.isPromotionApplied()) {
			int promotionQuantity = orderLineItem.getPromotionQuantity();
			promotionProduct.deductQuantity(promotionQuantity);
		}

		if (orderLineItem.getNormalQuantity() > 0) {
			Product normalProduct = findNormalProductByName(productName);
			normalProduct.deductQuantity(orderLineItem.getNormalQuantity());
		}
	}

	private PromotionOrderStatus calculateRemainQuantity(Product findProduct, OrderItem orderItem) {
		Promotion promotion = findProduct.getPromotion();
		int stock = findProduct.getQuantity();
		int orderQuantity = orderItem.quantity();
		int normalQuantity = orderQuantity - stock;
		int freeQuantity = stock / (promotion.getBuy() + 1);
		int displayQuantity = normalQuantity + (stock % (promotion.getBuy() + 1));

		return new PromotionOrderStatus(
			PromotionOrderResult.INSUFFICIENT_STOCK,
			displayQuantity,
			stock,
			normalQuantity,
			freeQuantity,
			findProduct.getPrice()
		);
	}

	private PromotionOrderStatus calculateFreeQuantity(Product findProduct, OrderItem orderItem) {
		Promotion promotion = findProduct.getPromotion();
		if (orderItem.quantity() < findProduct.getQuantity() && orderItem.quantity() % promotion.getBuy() == 0) {
			return new PromotionOrderStatus(PromotionOrderResult.BELOW_QUANTITY, -1,
				orderItem.quantity(), -1,
				(orderItem.quantity() / promotion.getBuy()) * promotion.getGet(), findProduct.getPrice());
		}
		return new PromotionOrderStatus(PromotionOrderResult.EXACT_QUANTITY, -1, orderItem.quantity(),
			-1, (orderItem.quantity() / promotion.getBuy()) * promotion.getGet(), findProduct.getPrice());
	}

	public OrderLineItem getNormalProductInfo(OrderItem orderItem) {
		Product findProduct = findNormalProductByName(orderItem.name());
		return OrderLineItem.ofNormal(
			findProduct.getName(),
			orderItem.quantity(),
			findProduct.getPrice()
		);
	}

	private void validateNames(List<String> orderNames) {
		Set<String> productsNames = products.stream()
			.map(Product::getName)
			.collect(Collectors.toSet());

		if (!productsNames.containsAll(orderNames)) {
			throw new IllegalStateException(StockValidateMessage.INVALID_ORDER_PRODUCT.getMessage());
		}
	}

	private void validateOrderItem(OrderItem orderItem) {
		Product promotionProduct = findPromotionProductByName(orderItem.name());
		Product normalProduct = findNormalProductByName(orderItem.name());

		if (promotionProduct != null) {
			validatePromotionOrder(promotionProduct, normalProduct, orderItem.quantity());
			return;
		}

		validateNormalOrderStock(normalProduct, orderItem.quantity());
	}

	private Product findPromotionProductByName(String name) {
		return products.stream()
			.filter(p -> p.getName().equals(name))
			.filter(Product::hasPromotion)
			.findFirst()
			.orElse(null);
	}

	private Product findNormalProductByName(String name) {
		return products.stream()
			.filter(p -> p.getName().equals(name))
			.filter(p -> !p.hasPromotion())
			.findFirst()
			.orElse(null);
	}

	private void validatePromotionOrder(Product promotionProduct, Product normalProduct, int quantity) {
		if (!validatePromotionValidity(promotionProduct)) {
			validateNormalOrderStock(normalProduct, quantity);
			return;
		}
		int remainQuantity = validatePromotionStock(promotionProduct, quantity);
		if (remainQuantity > 0) {
			validateNormalOrderStock(normalProduct, remainQuantity);
		}
	}

	private boolean validatePromotionValidity(Product promotionProduct) {
		Promotion promotion = promotionProduct.getPromotion();
		return promotion.isValidPromotion();
	}

	private int validatePromotionStock(Product promotionProduct, int quantity) {
		if (promotionProduct.getQuantity() < quantity) {
			return quantity - promotionProduct.getQuantity();
		}
		return 0;
	}

	private void validateNormalOrderStock(Product normalProduct, int orderQuantity) {
		if (normalProduct.getQuantity() < orderQuantity) {
			throw new IllegalStateException(StockValidateMessage.INVALID_ORDER_OUT_OF_STOCK.getMessage());
		}
	}
}
