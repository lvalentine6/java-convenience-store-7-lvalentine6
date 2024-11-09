package store.validator.file;

import java.util.List;

import store.domain.product.PrimitiveProductInfo;

public class ProductFileValidatingParser implements FileValidatingParser<PrimitiveProductInfo> {
	private static final String DELIMITER = ",";
	private static final int PRODUCT_COLUMN_SIZE = 4;
	private static final int HEADER_LINES_TO_SKIP = 1;

	private static final int PRODUCT_NAME_INDEX = 0;
	private static final int PRODUCT_PRICE_INDEX = 1;
	private static final int PRODUCT_QUANTITY_INDEX = 2;
	private static final int PRODUCT_PROMOTION_INDEX = 3;

	private static final int MIN_VALID_PRICE_NUMBER = 1;
	private static final int MIN_VALID_QUANTITY_NUMBER = 0;
	private static final int MAX_INTEGER_LENGTH = 10;

	private ProductFileValidatingParser() {
	}

	private static class Holder {
		private static final ProductFileValidatingParser INSTANCE = new ProductFileValidatingParser();

	}

	public static ProductFileValidatingParser getInstance() {
		return Holder.INSTANCE;
	}

	@Override
	public void validateFirstLine(String firstLine) {
		String[] splitLine = firstLine.split(DELIMITER);
		validateColumnSize(splitLine);
	}

	@Override
	public List<PrimitiveProductInfo> processLine(List<String> allLine) {
		return allLine.stream()
			.skip(HEADER_LINES_TO_SKIP)
			.map(this::convertPrimitiveProductInfo)
			.toList();
	}

	private PrimitiveProductInfo convertPrimitiveProductInfo(String line) {
		validateReadLine(line);
		String[] splitLine = line.split(DELIMITER);
		return new PrimitiveProductInfo(
			splitLine[PRODUCT_NAME_INDEX],
			Integer.parseInt(splitLine[PRODUCT_PRICE_INDEX]),
			Integer.parseInt(splitLine[PRODUCT_QUANTITY_INDEX]),
			splitLine[PRODUCT_PROMOTION_INDEX]);
	}

	private void validateReadLine(String line) {
		String[] splitLine = line.split(DELIMITER);
		validateColumnSize(splitLine);
		validatePrice(splitLine);
		validateQuantity(splitLine);
	}

	private void validatePrice(String[] splitLine) {
		validatePriceOverFlow(splitLine);
		validatePriceNumber(splitLine);
		validatePriceMinNumber(splitLine);
	}

	private void validateQuantity(String[] splitLine) {
		validateQuantityOverFlow(splitLine);
		validateQuantityNumber(splitLine);
		validateQuantityMinNumber(splitLine);
	}

	private void validateColumnSize(String[] line) {
		if (line.length != PRODUCT_COLUMN_SIZE) {
			throw new IllegalArgumentException(FileValidationMessage.INVALID_PRODUCT_COLUMN_SIZE.getMessage());
		}
	}

	private void validatePriceOverFlow(String[] line) {
		if (line[PRODUCT_PRICE_INDEX].length() > MAX_INTEGER_LENGTH) {
			throw new IllegalArgumentException(FileValidationMessage.INVALID_PRODUCT_PRICE_OVER_FLOW.getMessage());
		}
	}

	private void validatePriceNumber(String[] line) {
		try {
			Integer.parseInt(line[PRODUCT_PRICE_INDEX]);
		} catch (NumberFormatException numberFormatException) {
			throw new IllegalArgumentException(FileValidationMessage.INVALID_PRODUCT_PRICE_NUMBER.getMessage());
		}
	}

	private void validatePriceMinNumber(String[] line) {
		if (Integer.parseInt(line[PRODUCT_PRICE_INDEX]) < MIN_VALID_PRICE_NUMBER) {
			throw new IllegalArgumentException(
				FileValidationMessage.INVALID_PRODUCT_PRICE_MIN_NUMBER.getMessage());
		}
	}

	private void validateQuantityOverFlow(String[] line) {
		if (line[PRODUCT_QUANTITY_INDEX].length() > MAX_INTEGER_LENGTH) {
			throw new IllegalArgumentException(FileValidationMessage.INVALID_PRODUCT_QUANTITY_OVER_FLOW.getMessage());
		}
	}

	private void validateQuantityNumber(String[] line) {
		try {
			Integer.parseInt(line[PRODUCT_QUANTITY_INDEX]);
		} catch (NumberFormatException numberFormatException) {
			throw new IllegalArgumentException(FileValidationMessage.INVALID_PRODUCT_QUANTITY_NUMBER.getMessage());
		}
	}

	private void validateQuantityMinNumber(String[] line) {
		if (Integer.parseInt(line[PRODUCT_QUANTITY_INDEX]) < MIN_VALID_QUANTITY_NUMBER) {
			throw new IllegalArgumentException(
				FileValidationMessage.INVALID_PRODUCT_QUANTITY_MIN_NUMBER.getMessage());
		}
	}
}
