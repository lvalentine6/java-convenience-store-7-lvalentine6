package store.validator.stock;

public enum StockValidateMessage {
	INVALID_DUPLICATE_PROMOTIONS("[ERROR] 하나의 상품에는 하나의 프로모션만 가능합니다."),
	;

	private final String message;

	StockValidateMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
