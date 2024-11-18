package store.domain.membershop;

public class Membership {
	private static final int DISCOUNT_RATE = 30;
	private static final int MAXIMUM_DISCOUNT_AMOUNT = 8_000;

	private Membership() {
	}

	public static Membership create() {
		return new Membership();
	}

	public int calculateDiscountPrice(int price) {
		int discountPrice = (int)(price * DISCOUNT_RATE / 100.0);
		return Math.min(discountPrice, MAXIMUM_DISCOUNT_AMOUNT);
	}
}
