package store.validator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import store.domain.promotion.PrimitivePromotionInfo;

public class PromotionValidatingParser implements FileValidatingParser<PrimitivePromotionInfo> {
	private static final String DELIMITER = ",";
	private static final int PROMOTION_COLUMN_SIZE = 5;
	private static final int HEADER_LINES_TO_SKIP = 1;

	private static final int PROMOTION_NAME_INDEX = 0;
	private static final int PROMOTION_BUY_INDEX = 1;
	private static final int PROMOTION_GET_INDEX = 2;
	private static final int PROMOTION_START_DATE_INDEX = 3;
	private static final int PROMOTION_END_DATE_INDEX = 4;

	private static final int MIN_VALID_NUMBER = 1;
	private static final int MAX_INTEGER_LENGTH = 10;

	private static final String DATE_FORMAT = "yyyy-MM-dd";
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);

	private PromotionValidatingParser() {
	}

	private static class Holder {
		private static final PromotionValidatingParser INSTANCE = new PromotionValidatingParser();
	}

	public static PromotionValidatingParser getInstance() {
		return Holder.INSTANCE;
	}

	@Override
	public void validateFirstLine(String firstLine) {
		String[] splitLine = firstLine.split(DELIMITER);
		validateColumnSize(splitLine);
	}

	@Override
	public List<PrimitivePromotionInfo> processLine(List<String> allLine) {
		return allLine.stream()
			.skip(HEADER_LINES_TO_SKIP)
			.map(this::convertPrimitiveProductInfo)
			.toList();
	}

	private PrimitivePromotionInfo convertPrimitiveProductInfo(String line) {
		validateReadLine(line);
		String[] splitLine = line.split(DELIMITER);
		return new PrimitivePromotionInfo(
			splitLine[PROMOTION_NAME_INDEX],
			Integer.parseInt(splitLine[PROMOTION_BUY_INDEX]),
			Integer.parseInt(splitLine[PROMOTION_GET_INDEX]),
			splitLine[PROMOTION_START_DATE_INDEX],
			splitLine[PROMOTION_END_DATE_INDEX]);
	}

	private void validateReadLine(String line) {
		String[] splitLine = line.split(DELIMITER);
		validateColumnSize(splitLine);
		validateBuy(splitLine);
		validateGet(splitLine);
		validateDate(splitLine);
	}

	private void validateColumnSize(String[] line) {
		if (line.length != PROMOTION_COLUMN_SIZE) {
			throw new IllegalArgumentException(FileValidationMessage.INVALID_PROMOTION_COLUMN_SIZE.getMessage());
		}
	}

	private void validateBuy(String[] splitLine) {
		validateBuyOverFlow(splitLine);
		validateBuyNumber(splitLine);
		validateBuyMinNumber(splitLine);
	}

	private void validateGet(String[] splitLine) {
		validateGetOverFlow(splitLine);
		validateGetNumber(splitLine);
		validateGetMinNumber(splitLine);
	}

	private void validateDate(String[] splitLine) {
		validateStartDate(splitLine);
		validateEndDate(splitLine);
		validateDateRange(splitLine);
	}

	private void validateBuyOverFlow(String[] line) {
		if (line[PROMOTION_BUY_INDEX].length() > MAX_INTEGER_LENGTH) {
			throw new IllegalArgumentException(FileValidationMessage.INVALID_PROMOTION_BUY_OVER_FLOW.getMessage());
		}
	}

	private void validateBuyNumber(String[] line) {
		try {
			Integer.parseInt(line[PROMOTION_BUY_INDEX]);
		} catch (NumberFormatException numberFormatException) {
			throw new IllegalArgumentException(FileValidationMessage.INVALID_PROMOTION_BUY_NUMBER.getMessage());
		}
	}

	private void validateBuyMinNumber(String[] line) {
		if (Integer.parseInt(line[PROMOTION_BUY_INDEX]) < MIN_VALID_NUMBER) {
			throw new IllegalArgumentException(
				FileValidationMessage.INVALID_PROMOTION_BUY_MIN_NUMBER.getMessage());
		}
	}

	private void validateGetOverFlow(String[] line) {
		if (line[PROMOTION_GET_INDEX].length() > MAX_INTEGER_LENGTH) {
			throw new IllegalArgumentException(FileValidationMessage.INVALID_PROMOTION_GET_OVER_FLOW.getMessage());
		}
	}

	private void validateGetNumber(String[] line) {
		try {
			Integer.parseInt(line[PROMOTION_GET_INDEX]);
		} catch (NumberFormatException numberFormatException) {
			throw new IllegalArgumentException(FileValidationMessage.INVALID_PROMOTION_GET_NUMBER.getMessage());
		}
	}

	private void validateGetMinNumber(String[] line) {
		if (Integer.parseInt(line[PROMOTION_GET_INDEX]) < MIN_VALID_NUMBER) {
			throw new IllegalArgumentException(
				FileValidationMessage.INVALID_PROMOTION_GET_MIN_NUMBER.getMessage());
		}
	}

	private void validateStartDate(String[] line) {
		try {
			LocalDate.parse(line[PROMOTION_START_DATE_INDEX], DATE_FORMATTER);
		} catch (DateTimeParseException dateTimeParseException) {
			throw new IllegalArgumentException(FileValidationMessage.INVALID_PROMOTION_START_DATE.getMessage());
		}
	}

	private void validateEndDate(String[] line) {
		try {
			LocalDate.parse(line[PROMOTION_END_DATE_INDEX], DATE_FORMATTER);
		} catch (DateTimeParseException dateTimeParseException) {
			throw new IllegalArgumentException(FileValidationMessage.INVALID_PROMOTION_END_DATE.getMessage());
		}
	}

	private void validateDateRange(String[] line) {
		LocalDate startDate = LocalDate.parse(line[PROMOTION_START_DATE_INDEX], DATE_FORMATTER);
		LocalDate endDate = LocalDate.parse(line[PROMOTION_END_DATE_INDEX], DATE_FORMATTER);

		if (startDate.isAfter(endDate)) {
			throw new IllegalArgumentException(FileValidationMessage.INVALID_PROMOTION_DATE_RANGE.getMessage());
		}
	}
}
