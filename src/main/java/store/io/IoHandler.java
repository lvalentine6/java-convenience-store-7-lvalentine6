package store.io;

import java.util.List;

import store.domain.product.PrimitiveProductInfo;
import store.domain.product.Product;
import store.domain.promotion.PrimitivePromotionInfo;

public class IoHandler {
	private final InputHandler inputHandler;
	private final OutputHandler outputHandler;
	private final ResourceHandler resourceHandler;

	private IoHandler(InputHandler inputHandler, OutputHandler outputHandler, ResourceHandler resourceHandler) {
		this.inputHandler = inputHandler;
		this.outputHandler = outputHandler;
		this.resourceHandler = resourceHandler;
	}

	private static class Holder {
		private static final IoHandler INSTANCE = new IoHandler(
			InputHandler.getInstance(),
			OutputHandler.getInstance(),
			ResourceHandler.getInstance());
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
}
