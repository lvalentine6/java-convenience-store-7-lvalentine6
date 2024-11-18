package store.domain.order;

import store.domain.product.PromotionOrderStatus;

public class OrderLineItem {
	private static final int NO_NORMAL_QUANTITY = 0;
	private static final boolean PROMOTION_APPLIED = true;
	private static final boolean PROMOTION_NOT_APPLIED = false;

	private final String name;
	private final int totalQuantity;
	private final int unitPrice;
	private final int promotionQuantity;
	private final int normalQuantity;
	private final int freeQuantity;
	private final boolean isPromotionApplied;

	private OrderLineItem(String name, int totalQuantity, int unitPrice,
		int promotionQuantity, int normalQuantity, int freeQuantity,
		boolean isPromotionApplied) {
		this.name = name;
		this.totalQuantity = totalQuantity;
		this.unitPrice = unitPrice;
		this.promotionQuantity = promotionQuantity;
		this.normalQuantity = normalQuantity;
		this.freeQuantity = freeQuantity;
		this.isPromotionApplied = isPromotionApplied;
	}

	public static OrderLineItem of(String name, int totalQuantity, int unitPrice, int promotionQuantity,
		int normalQuantity, int freeQuantity, boolean isPromotionApplied) {
		return new OrderLineItem(name, totalQuantity, unitPrice, promotionQuantity, normalQuantity, freeQuantity,
			isPromotionApplied);
	}

	public static OrderLineItem ofPromotionOnly(String name, PromotionOrderStatus status) {
		return new OrderLineItem(
			name,
			status.promotionQuantity(),
			status.unitPrice(),
			status.promotionQuantity(),
			NO_NORMAL_QUANTITY,
			status.additionalQuantity(),
			PROMOTION_APPLIED
		);
	}

	public static OrderLineItem ofMixedQuantity(String name, PromotionOrderStatus status) {
		return new OrderLineItem(
			name,
			status.promotionQuantity() + status.normalQuantity(),
			status.unitPrice(),
			status.promotionQuantity(),
			status.normalQuantity(),
			status.additionalQuantity(),
			PROMOTION_APPLIED
		);
	}

	public static OrderLineItem ofAdditionalQuantity(OrderItem orderItem, PromotionOrderStatus status) {
		return new OrderLineItem(
			orderItem.name(),
			orderItem.quantity() + status.additionalQuantity(),
			status.unitPrice(),
			orderItem.quantity() + status.additionalQuantity(),
			NO_NORMAL_QUANTITY,
			status.additionalQuantity(),
			PROMOTION_APPLIED
		);
	}

	public static OrderLineItem ofNonPromotional(String name, PromotionOrderStatus status) {
		return new OrderLineItem(
			name,
			status.promotionQuantity(),
			status.unitPrice(),
			status.promotionQuantity(),
			NO_NORMAL_QUANTITY,
			NO_NORMAL_QUANTITY,
			PROMOTION_NOT_APPLIED
		);
	}

	public static OrderLineItem ofExactPromotion(String name, PromotionOrderStatus status) {
		return new OrderLineItem(
			name,
			status.promotionQuantity(),
			status.unitPrice(),
			status.promotionQuantity(),
			NO_NORMAL_QUANTITY,
			status.additionalQuantity(),
			PROMOTION_APPLIED
		);
	}

	public static OrderLineItem ofNormal(String name, int quantity, int unitPrice) {
		return new OrderLineItem(
			name,
			quantity,
			unitPrice,
			NO_NORMAL_QUANTITY,
			quantity,
			NO_NORMAL_QUANTITY,
			PROMOTION_NOT_APPLIED
		);
	}

	public String getName() {
		return name;
	}

	public int getTotalQuantity() {
		return totalQuantity;
	}

	public int getUnitPrice() {
		return unitPrice;
	}

	public int getPromotionQuantity() {
		return promotionQuantity;
	}

	public int getNormalQuantity() {
		return normalQuantity;
	}

	public int getFreeQuantity() {
		return freeQuantity;
	}

	public boolean isPromotionApplied() {
		return isPromotionApplied;
	}
}
