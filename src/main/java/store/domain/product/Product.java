package store.domain.product;

import store.domain.promotion.Promotion;
import store.validator.product.ProductValidateMessage;

public class Product {
	private final String name;
	private final int price;
	private final int quantity;
	private final Promotion promotion;

	private Product(String name, int price, int quantity, Promotion promotion) {
		validateName(name);
		validatePrice(price);
		validateQuantity(quantity);
		this.name = name;
		this.price = price;
		this.quantity = quantity;
		this.promotion = promotion;
	}

	public static Product of(String name, int price, int quantity, Promotion promotion) {
		return new Product(name, price, quantity, promotion);
	}

	public static Product of(PrimitiveProductInfo info, Promotion promotion) {
		return new Product(
			info.name(),
			info.price(),
			info.quantity(),
			promotion
		);
	}

	private void validateName(String name) {
		if (name == null || name.isBlank() || name.equals("null")) {
			throw new IllegalArgumentException(ProductValidateMessage.INVALID_NAME.getMessage());
		}
	}

	private void validatePrice(int price) {
		if (price <= 0) {
			throw new IllegalArgumentException(ProductValidateMessage.INVALID_MIN_PRICE.getMessage());
		}
	}

	private void validateQuantity(int quantity) {
		if (quantity < 0) {
			throw new IllegalArgumentException(ProductValidateMessage.INVALID_MIN_QUANTITY.getMessage());
		}
	}

	public String getName() {
		return name;
	}

	public int getPrice() {
		return price;
	}

	public int getQuantity() {
		return quantity;
	}

	public Promotion getPromotion() {
		return promotion;
	}

	public boolean hasPromotion() {
		return promotion != null;
	}
}
