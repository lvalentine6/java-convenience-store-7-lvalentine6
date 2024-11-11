package store.validator.input;

public enum InputValidationMessage {
	INVALID_ORDER_LIST_FORMAT("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요."),
	INVALID_ORDER_FORMAT("[ERROR] 상품명, 수량의 형식이 잘못되었습니다."),
	INVALID_ORDER_OVER_FLOW("[ERROR] 구매 수량의 최대값을 초과했습니다."),
	INVALID_ORDER_MIN_QUANTITY("[ERROR] 구매 수량은 0보다 커야 합니다."),
	INVALID_ORDER_NUMBER("[ERROR] 구매 수량은 숫자여야 합니다."),
	INVALID_ORDER_DUPLICATION("[ERROR] 중복된 구매 물품이 있습니다."),
	INVALID_ETC_FORMAT("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요."),
	;

	private final String message;

	InputValidationMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
