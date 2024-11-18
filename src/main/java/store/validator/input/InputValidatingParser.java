package store.validator.input;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import store.domain.order.OrderItem;

public class InputValidatingParser {
	private static final String PRODUCT_DELIMITER = ",";
	private static final String NAME_QUANTITY_DELIMITER = "-";
	private static final int SUBSTRING_START_INDEX = 1;
	private static final int SUBSTRING_END_INDEX = 1;
	private static final String START_CHARACTER = "[";
	private static final String END_CHARACTER = "]";
	private static final int SPLIT_SIZE = 2;
	private static final int NAME_INDEX = 0;
	private static final int QUANTITY_INDEX = 1;
	private static final int MAX_LENGTH = 10;
	private static final int MIN_QUANTITY = 1;

	private InputValidatingParser() {
	}

	private static class Holder {
		private static final InputValidatingParser INSTANCE = new InputValidatingParser();
	}

	public static InputValidatingParser getInstance() {
		return Holder.INSTANCE;
	}

	public List<OrderItem> validatedOrderItems(String input) {
		List<String> splitInput = splitInput(input);
		List<OrderItem> orderItems = parseOrderItems(splitInput);
		validateDuplication(orderItems);
		return orderItems;
	}

	public String validateAdditionalAnswer(String input) {
		if (!(input.equals("Y") || input.equals("N"))) {
			throw new IllegalArgumentException(InputValidationMessage.INVALID_ETC_FORMAT.getMessage());
		}
		return input;
	}

	private List<String> splitInput(String input) {
		return Arrays.stream(input.split(PRODUCT_DELIMITER))
			.toList();
	}

	private List<OrderItem> parseOrderItems(List<String> splitInput) {
		return splitInput.stream()
			.map(this::parseOrderItem)
			.toList();
	}

	private OrderItem parseOrderItem(String splitInput) {
		validateStartEndFormat(splitInput);
		String contents = splitInput.substring(SUBSTRING_START_INDEX, splitInput.length() - SUBSTRING_END_INDEX);
		String[] contentsSplit = contents.split(NAME_QUANTITY_DELIMITER);

		validateContentsFormat(contentsSplit);
		validateQuantity(contentsSplit[QUANTITY_INDEX]);
		return new OrderItem(contentsSplit[NAME_INDEX], Integer.parseInt(contentsSplit[QUANTITY_INDEX]));
	}

	private void validateStartEndFormat(String splitInput) {
		if (!(splitInput.startsWith(START_CHARACTER) && splitInput.endsWith(END_CHARACTER))) {
			throw new IllegalArgumentException(InputValidationMessage.INVALID_ORDER_LIST_FORMAT.getMessage());
		}
	}

	private void validateContentsFormat(String[] contentsSplit) {
		if (contentsSplit.length != SPLIT_SIZE) {
			throw new IllegalArgumentException(InputValidationMessage.INVALID_ORDER_LIST_FORMAT.getMessage());
		}
	}

	private void validateQuantity(String splitInput) {
		validateOverFlow(splitInput);
		validateNumber(splitInput);
		validateMinQuantity(splitInput);
	}

	private void validateOverFlow(String input) {
		if (input.length() > MAX_LENGTH) {
			throw new IllegalArgumentException(InputValidationMessage.INVALID_ORDER_LIST_FORMAT.getMessage());
		}
		validateNumber(input);
	}

	private void validateNumber(String input) {
		try {
			Integer.parseInt(input);
		} catch (NumberFormatException numberFormatException) {
			throw new IllegalArgumentException(InputValidationMessage.INVALID_ORDER_LIST_FORMAT.getMessage());
		}
	}

	private void validateMinQuantity(String input) {
		if (Integer.parseInt(input) < MIN_QUANTITY) {
			throw new IllegalArgumentException(InputValidationMessage.INVALID_ORDER_LIST_FORMAT.getMessage());
		}
	}

	private void validateDuplication(List<OrderItem> orderItems) {
		List<String> orderNames = orderItems.stream()
			.map(OrderItem::name)
			.toList();
		Set<String> duplicateNames = new HashSet<>(orderNames);

		if (orderNames.size() != duplicateNames.size()) {
			throw new IllegalArgumentException(InputValidationMessage.INVALID_ORDER_LIST_FORMAT.getMessage());
		}
	}
}
