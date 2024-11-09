package store.domain.promotion;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

import store.validator.PromotionValidateMessage;

public class Promotion {
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);

	private final String name;
	private final int buy;
	private final int get;
	private final LocalDate startDate;
	private final LocalDate endDate;

	private Promotion(String name, int buy, int get, String startDate, String endDate) {
		validateName(name);
		validateQuantities(buy, get);
		validateDates(startDate, endDate);
		this.name = name;
		this.buy = buy;
		this.get = get;
		this.startDate = LocalDate.parse(startDate, DATE_FORMATTER);
		this.endDate = LocalDate.parse(endDate, DATE_FORMATTER);
	}

	public static Promotion from(PrimitivePromotionInfo info) {
		return new Promotion(
			info.name(),
			info.buy(),
			info.get(),
			info.startDate(),
			info.endDate()
		);
	}

	private void validateName(String name) {
		if (name == null || name.isBlank() || name.equals("null")) {
			throw new IllegalArgumentException(PromotionValidateMessage.INVALID_NAME.getMessage());
		}
	}

	private void validateQuantities(int buy, int get) {
		if (buy <= 0 || get <= 0) {
			throw new IllegalArgumentException(PromotionValidateMessage.INVALID_MIN_QUANTITY.getMessage());
		}
		if (buy < get) {
			throw new IllegalArgumentException(
				PromotionValidateMessage.INVALID_BUY_QUANTITY_UNDER_GET_QUANTITY.getMessage());
		}
	}

	private void validateDates(String startDate, String endDate) {
		validateNull(startDate, endDate);
		validateFormat(startDate, endDate);
		validateRange(startDate, endDate);
	}

	private void validateNull(String startDate, String endDate) {
		if (startDate == null || endDate == null || startDate.isBlank() || endDate.isBlank() || startDate.equals("null")
			|| endDate.equals("null")) {
			throw new IllegalArgumentException(PromotionValidateMessage.INVALID_DATE.getMessage());
		}
	}

	private void validateFormat(String startDate, String endDate) {
		try {
			LocalDate.parse(startDate, DATE_FORMATTER);
			LocalDate.parse(endDate, DATE_FORMATTER);
		} catch (DateTimeParseException e) {
			throw new IllegalArgumentException(PromotionValidateMessage.INVALID_DATE_FORMAT.getMessage());
		}
	}

	private void validateRange(String startDate, String endDate) {
		if (LocalDate.parse(startDate, DATE_FORMATTER).isAfter(LocalDate.parse(endDate, DATE_FORMATTER))) {
			throw new IllegalArgumentException(
				PromotionValidateMessage.INVALID_START_DATE_BEFORE_END_DATE.getMessage());
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Promotion promotion = (Promotion)o;
		return buy == promotion.buy && get == promotion.get && Objects.equals(name, promotion.name)
			&& Objects.equals(startDate, promotion.startDate) && Objects.equals(endDate,
			promotion.endDate);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, buy, get, startDate, endDate);
	}

	public String getName() {
		return name;
	}
}
