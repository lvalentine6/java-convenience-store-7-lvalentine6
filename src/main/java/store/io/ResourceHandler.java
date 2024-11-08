package store.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import store.domain.product.PrimitiveProductInfo;
import store.domain.promotion.PrimitivePromotionInfo;
import store.validator.FileValidatingParser;
import store.validator.FileValidationMessage;
import store.validator.ProductValidatingParser;
import store.validator.PromotionValidatingParser;

public class ResourceHandler {
	private final FileValidatingParser<PrimitiveProductInfo> productValidatingParser;
	private final FileValidatingParser<PrimitivePromotionInfo> promotionValidatingParser;

	private ResourceHandler() {
		this.productValidatingParser = ProductValidatingParser.getInstance();
		this.promotionValidatingParser = PromotionValidatingParser.getInstance();
	}

	private static class Holder {
		private static final ResourceHandler INSTANCE = new ResourceHandler();
	}

	public static ResourceHandler getInstance() {
		return Holder.INSTANCE;
	}

	public List<PrimitiveProductInfo> readProductFrom(String filePathAndName) {
		return readFrom(filePathAndName, productValidatingParser);
	}

	public List<PrimitivePromotionInfo> readPromotionFrom(String filePathAndName) {
		return readFrom(filePathAndName, promotionValidatingParser);
	}

	private <T> List<T> readFrom(String filePathAndName, FileValidatingParser<T> parser) {
		InputStream stream = ResourceHandler.class.getResourceAsStream(filePathAndName);
		validateStream(stream);
		return getValidatedLines(stream, parser);
	}

	private void validateStream(InputStream stream) {
		if (stream == null) {
			throw new IllegalArgumentException(FileValidationMessage.INVALID_NOT_FOUND.getMessage());
		}
	}

	private <T> List<T> getValidatedLines(InputStream stream, FileValidatingParser<T> parser) {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
			List<String> allLine = reader.lines().toList();
			validateLines(allLine);
			parser.validateFirstLine(allLine.getFirst());
			return parser.processLine(allLine);
		} catch (IOException ioException) {
			throw new IllegalArgumentException(FileValidationMessage.INVALID_UNREADABLE.getMessage());
		}
	}

	private void validateLines(List<String> lines) {
		if (lines.isEmpty()) {
			throw new IllegalArgumentException(FileValidationMessage.INVALID_EMPTY.getMessage());
		}
	}
}
