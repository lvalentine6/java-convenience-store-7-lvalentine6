package store.validator.product;

public enum ProductValidateMessage {
	INVALID_NAME("[ERROR] 상품 이름은 비어있을수 없습니다."),
	INVALID_MIN_PRICE("[ERROR] 상품 가격은 0보다 커야 합니다."),
	INVALID_MIN_QUANTITY("[ERROR] 상품 수량은 0과 같거나 커야 합니다."),
	;

	private final String message;

	ProductValidateMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
