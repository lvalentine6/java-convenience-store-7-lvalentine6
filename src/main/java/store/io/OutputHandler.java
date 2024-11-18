package store.io;

import java.util.List;

import store.domain.order.Order;
import store.domain.order.OrderLineItem;
import store.domain.product.Product;
import store.domain.promotion.Promotion;

public class OutputHandler {
	private static final String PROMPT_WELCOME = "\n안녕하세요. W편의점입니다.\n현재 보유하고 있는 상품입니다.\n";
	private static final String PROMPT_SELLING = "\n구매하실 상품명과 수량을 입력해 주세요.";
	private static final String PROMPT_SOLD_OUT = " 재고 없음";
	private static final String PROMPT_APPLY_PROMOTION_QUANTITY_PROMPT_FIRST = "\n현재 ";
	private static final String PROMPT_APPLY_PROMOTION_QUANTITY_PROMPT_SECOND = "은(는) ";
	private static final String PROMPT_APPLY_PROMOTION_QUANTITY_PROMPT_THIRD = "개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)";
	private static final String PROMPT_NON_DISCOUNT_FIRST = "\n현재 ";
	private static final String PROMPT_NON_DISCOUNT_PROMPT_SECOND = "개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)";
	private static final String PROMPT_EMPTY = "";
	private static final String PROMPT_BLANK = " ";
	private static final String PROMPT_MEMBERSHIP = "\n멤버십 할인을 받으시겠습니까? (Y/N)";
	private static final String PROMPT_RECEIPT_TOTAL_HEADER = "\n==============W 편의점================";
	private static final String PROMPT_RECEIPT_ORDER_TOP = "상품명\t\t수량\t금액";
	private static final String PROMPT_RECEIPT_ORDER_CONTENTS = "%s\t\t%d\t%,d%n";
	private static final String PROMPT_RECEIPT_FREE_HEADER = "=============증\t정===============";
	private static final String PROMPT_RECEIPT_FREE_CONTENTS = "%s\t\t%d%n";
	private static final String PROMPT_RECEIPT_AMOUNT_HEADER = "====================================";
	private static final String PROMPT_RECEIPT_AMOUNT_ORIGIN_TOTAL_AMOUNT = "총구매액\t\t%d\t%,d%n";
	private static final String PROMPT_RECEIPT_AMOUNT_PROMOTION_AMOUNT = "행사할인\t\t\t-%,d%n";
	private static final String PROMPT_RECEIPT_AMOUNT_MEMBERSHIP_AMOUNT = "멤버십할인\t\t\t-%,d%n";
	private static final String PROMPT_RECEIPT_AMOUNT_TOTAL_AMOUNT = "내실돈\t\t\t%,d%n";
	private static final String PROMPT_CONTINUE = "\n감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)";
	private static final String PRODUCT_FORMAT = "- %s %,d원%s%s";
	private static final String QUANTITY_FORMAT = " %d개";

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
		String output = String.format(PRODUCT_FORMAT,
			product.getName(),
			product.getPrice(),
			formatQuantity(product.getQuantity()),
			formatPromotion(product.getPromotion())
		);

		System.out.println(output);
	}

	private String formatQuantity(int quantity) {
		if (quantity == 0) {
			return PROMPT_SOLD_OUT;
		}
		return String.format(QUANTITY_FORMAT, quantity);
	}

	private String formatPromotion(Promotion promotion) {
		if (promotion == null) {
			return PROMPT_EMPTY;
		}
		return PROMPT_BLANK + promotion.getName();
	}

	public void showSellingPrompt() {
		System.out.println(PROMPT_SELLING);
	}

	public void showExceptionMessage(String message) {
		System.out.println("\n" + message);
	}

	public void showApplyPromotionPrompt(String name, int quantity) {
		String prompt =
			PROMPT_APPLY_PROMOTION_QUANTITY_PROMPT_FIRST
				+ name
				+ PROMPT_APPLY_PROMOTION_QUANTITY_PROMPT_SECOND
				+ quantity
				+ PROMPT_APPLY_PROMOTION_QUANTITY_PROMPT_THIRD;
		System.out.println(prompt);
	}

	public void showNonDisCountPrompt(String name, int quantity) {
		String prompt =
			PROMPT_NON_DISCOUNT_FIRST
				+ name
				+ PROMPT_BLANK
				+ quantity
				+ PROMPT_NON_DISCOUNT_PROMPT_SECOND;
		System.out.println(prompt);
	}

	public void showApplyMembershipPrompt() {
		System.out.println(PROMPT_MEMBERSHIP);
	}

	public void showReceipt(Order order) {
		printReceiptHeader();
		printOrderItems(order.getOrderLineItems());
		printPromotionItems(order.getPromotionItems());
		printAmountSummary(order);
	}

	private void printReceiptHeader() {
		System.out.println(PROMPT_RECEIPT_TOTAL_HEADER);
		System.out.println(PROMPT_RECEIPT_ORDER_TOP);
	}

	private void printOrderItems(List<OrderLineItem> orderItems) {
		orderItems.forEach(item ->
			System.out.printf(PROMPT_RECEIPT_ORDER_CONTENTS,
				item.getName(),
				item.getTotalQuantity(),
				item.getTotalQuantity() * item.getUnitPrice()));
	}

	private void printPromotionItems(List<OrderLineItem> promotionItems) {
		System.out.println(PROMPT_RECEIPT_FREE_HEADER);
		promotionItems.forEach(item ->
			System.out.printf(PROMPT_RECEIPT_FREE_CONTENTS,
				item.getName(),
				item.getFreeQuantity()));
	}

	private void printAmountSummary(Order order) {
		System.out.println(PROMPT_RECEIPT_AMOUNT_HEADER);
		printAmountDetails(order);
	}

	private void printAmountDetails(Order order) {
		System.out.printf(PROMPT_RECEIPT_AMOUNT_ORIGIN_TOTAL_AMOUNT,
			order.getTotalQuantity(),
			order.getTotalAmount());
		System.out.printf(PROMPT_RECEIPT_AMOUNT_PROMOTION_AMOUNT,
			order.getPromotionDiscount());
		System.out.printf(PROMPT_RECEIPT_AMOUNT_MEMBERSHIP_AMOUNT,
			order.getMembershipDiscount());
		System.out.printf(PROMPT_RECEIPT_AMOUNT_TOTAL_AMOUNT,
			order.getFinalAmount());
	}

	public void showContinuePrompt() {
		System.out.println(PROMPT_CONTINUE);
	}
}
