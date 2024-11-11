package store.validator.stock;

public enum StockValidateMessage {
	INVALID_DUPLICATE_PRODUCT("[ERROR] 중복된 상품은 등록할 수 없습니다."),
	INVALID_DUPLICATE_PROMOTIONS("[ERROR] 하나의 상품에는 하나의 프로모션만 가능합니다."),
	INVALID_ORDER_PRODUCT("[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요."),
	INVALID_ORDER_OUT_OF_STOCK("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요."),
	INVALID_ORDER_NOT_APPLY_PROMOTION("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.")
	;

	private final String message;

	StockValidateMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
