package store.domain.product;

public record PromotionOrderStatus(
	PromotionOrderResult result,
	int displayQuantity,
	int promotionQuantity,
	int normalQuantity,
	int additionalQuantity,
	int unitPrice
) {
}
