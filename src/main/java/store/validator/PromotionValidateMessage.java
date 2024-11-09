package store.validator;

public enum PromotionValidateMessage {
	INVALID_NAME("[ERROR] 프로모션 이름은 비어있을수 없습니다."),
	INVALID_MIN_QUANTITY("[ERROR] 구매 수량과 증정 수량은 0보다 커야 합니다."),
	INVALID_BUY_QUANTITY_UNDER_GET_QUANTITY("[ERROR] 구매 수량은 증정 수량보다 커야 합니다."),
	INVALID_DATE("[ERROR] 프로모션 기간은 비어있을수 없습니다."),
	INVALID_DATE_FORMAT("[ERROR] 날짜 형식이 잘못되었습니다."),
	INVALID_START_DATE_BEFORE_END_DATE("[ERROR] 프로모션 시작일은 종료일보다 이전이여야 합니다."),
	;

	private final String message;

	PromotionValidateMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
