package store.io;

import java.util.List;

import store.domain.order.Order;
import store.domain.order.OrderItem;
import store.domain.product.PrimitiveProductInfo;
import store.domain.product.Product;
import store.domain.promotion.PrimitivePromotionInfo;
import store.validator.input.InputValidatingParser;

public class IoHandler {
	private final InputHandler inputHandler;
	private final OutputHandler outputHandler;
	private final ResourceHandler resourceHandler;
	private final InputValidatingParser inputValidatingParser;

	private IoHandler(InputHandler inputHandler, OutputHandler outputHandler, ResourceHandler resourceHandler,
		InputValidatingParser inputValidatingParser) {
		this.inputHandler = inputHandler;
		this.outputHandler = outputHandler;
		this.resourceHandler = resourceHandler;
		this.inputValidatingParser = inputValidatingParser;
	}

	private static class Holder {
		private static final IoHandler INSTANCE = new IoHandler(
			InputHandler.getInstance(),
			OutputHandler.getInstance(),
			ResourceHandler.getInstance(),
			InputValidatingParser.getInstance());
	}

	public static IoHandler getInstance() {
		return Holder.INSTANCE;
	}

	public List<PrimitiveProductInfo> getPrimitiveProductInfos(String filePathName) {
		return resourceHandler.readProductFrom(filePathName);
	}

	public List<PrimitivePromotionInfo> getPrimitivePromotionInfos(String filePathName) {
		return resourceHandler.readPromotionFrom(filePathName);
	}

	public void showSellingProducts(List<Product> products) {
		outputHandler.showWelcomeMessage();
		products.forEach(outputHandler::showProduct);
	}

	public List<OrderItem> getOrderItems() {
		while (true) {
			try {
				outputHandler.showSellingPrompt();
				return inputValidatingParser.validatedOrderItems(inputHandler.readLine());
			} catch (IllegalArgumentException invalidInput) {
				outputHandler.showExceptionMessage(invalidInput.getMessage());
			}
		}
	}

	public void showExceptionMessage(String message) {
		outputHandler.showExceptionMessage(message);
	}

	public String getApplyPromotion(String name, int quantity) {
		while (true) {
			try {
				outputHandler.showApplyPromotionPrompt(name, quantity);
				return inputValidatingParser.validateAdditionalAnswer(inputHandler.readLine());
			} catch (IllegalArgumentException invalidInput) {
				outputHandler.showExceptionMessage(invalidInput.getMessage());
			}
		}
	}

	public String getNonDisCount(String name, int quantity) {
		while (true) {
			try {
				outputHandler.showNonDisCountPrompt(name, quantity);
				return inputValidatingParser.validateAdditionalAnswer(inputHandler.readLine());
			} catch (IllegalArgumentException invalidInput) {
				outputHandler.showExceptionMessage(invalidInput.getMessage());
			}
		}
	}

	public String getApplyMembership() {
		while (true) {
			try {
				outputHandler.showApplyMembershipPrompt();
				return inputValidatingParser.validateAdditionalAnswer(inputHandler.readLine());
			} catch (IllegalArgumentException invalidInput) {
				outputHandler.showExceptionMessage(invalidInput.getMessage());
			}
		}
	}

	public void showReceipt(Order order) {
		outputHandler.showReceipt(order);
	}

	public String getContinue() {
		while (true) {
			try {
				outputHandler.showContinuePrompt();
				return inputValidatingParser.validateAdditionalAnswer(inputHandler.readLine());
			} catch (IllegalArgumentException invalidInput) {
				outputHandler.showExceptionMessage(invalidInput.getMessage());
			}
		}
	}
}
