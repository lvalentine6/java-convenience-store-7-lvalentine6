package store.io;

import store.domain.product.Product;
import store.domain.promotion.Promotion;

public class OutputHandler {
	private static final String PROMPT_WELCOME = "안녕하세요. W편의점입니다.\n현재 보유하고 있는 상품입니다.\n";

	private OutputHandler() {
	}

	private static class Holder {
		private static final OutputHandler INSTANCE = new OutputHandler();
	}

	public static OutputHandler getInstance() {
		return Holder.INSTANCE;
	}

	public void showWelcomeMessage() {
		System.out.println(PROMPT_WELCOME);
	}

	public void showProduct(Product product) {
		String output = String.format("- %s %,d원%s%s",
			product.getName(),
			product.getPrice(),
			formatQuantity(product.getQuantity()),
			formatPromotion(product.getPromotion())
		);

		System.out.println(output);
	}

	private String formatQuantity(int quantity) {
		if (quantity == 0) {
			return " 재고 없음";
		}
		return String.format(" %d개", quantity);
	}

	private String formatPromotion(Promotion promotion) {
		if (promotion == null) {
			return "";
		}
		return " " + promotion.getName();
	}

}
