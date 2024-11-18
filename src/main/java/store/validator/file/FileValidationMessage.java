package store.validator.file;

public enum FileValidationMessage {
	INVALID_NOT_FOUND("[ERROR] 파일을 찾을수 없습니다."),
	INVALID_UNREADABLE("[ERROR] 파일을 읽을수 없습니다."),
	INVALID_EMPTY("[ERROR] 빈 파일입니다."),
	INVALID_PRODUCT_COLUMN_SIZE("[ERROR] 파일의 상품 형식이 올바르지 않습니다."),
	INVALID_PRODUCT_PRICE_NUMBER("[ERROR] 파일의 상품 가격은 숫자여야 합니다."),
	INVALID_PRODUCT_PRICE_MIN_NUMBER("[ERROR] 파일의 상품 가격은 0보다 커야 합니다."),
	INVALID_PRODUCT_PRICE_OVER_FLOW("[ERROR] 파일의 상품 가격이 최대값을 초과했습니다."),
	INVALID_PRODUCT_QUANTITY_NUMBER("[ERROR] 파일의 상품 수량은 숫자여야 합니다."),
	INVALID_PRODUCT_QUANTITY_MIN_NUMBER("[ERROR] 파일의 상품 수량은 0보다 같거나 커야 합니다."),
	INVALID_PRODUCT_QUANTITY_OVER_FLOW("[ERROR] 파일의 상품 수량이 최대값을 초과했습니다."),
	INVALID_PROMOTION_COLUMN_SIZE("[ERROR] 파일의 프로모션 형식이 올바르지 않습니다."),
	INVALID_PROMOTION_BUY_NUMBER("[ERROR] 파일의 프로모션 제공 기준 수량은 숫자여야 합니다."),
	INVALID_PROMOTION_BUY_MIN_NUMBER("[ERROR] 파일의 프로모션 제공 기준 수량의 숫자는 0보다 커야 합니다."),
	INVALID_PROMOTION_BUY_OVER_FLOW("[ERROR] 파일의 프로모션 제공 기준 수량이 최대값을 초과했습니다."),
	INVALID_PROMOTION_GET_NUMBER("[ERROR] 파일의 프로모션 제공 개수는 숫자여야 합니다."),
	INVALID_PROMOTION_GET_MIN_NUMBER("[ERROR] 파일의 프로모션 제공 개수는 0보다 커야 합니다."),
	INVALID_PROMOTION_GET_OVER_FLOW("[ERROR] 파일의 프로모션 제공 개수가 최대값을 초과했습니다."),
	INVALID_PROMOTION_START_DATE("[ERROR] 파일의 프로모션 시작 날짜가 올바르지 않습니다."),
	INVALID_PROMOTION_END_DATE("[ERROR] 파일의 프로모션 끝 날짜가 올바르지 않습니다."),
	INVALID_PROMOTION_DATE_RANGE("[ERROR] 파일의 프로모션 시작 날짜가 종료 날짜보다 늦습니다."),
	;

	private final String message;

	FileValidationMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
