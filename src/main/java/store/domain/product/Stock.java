package store.domain.product;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import store.validator.stock.StockValidateMessage;

public class Stock {
	private final List<Product> products;

	private Stock(List<Product> products) {
		validateDuplicatePromotion(products);
		this.products = products;
	}

	public static Stock from(List<Product> products) {
		return new Stock(products);
	}

	private void validateDuplicatePromotion(List<Product> products) {
		Map<String, Long> promotionCount = products.stream()
			.filter(Product::hasPromotion)
			.collect(Collectors.groupingBy(Product::getName, Collectors.counting()));
		if (promotionCount.values().stream().anyMatch(count -> count > 1)) {
			throw new IllegalArgumentException(StockValidateMessage.INVALID_DUPLICATE_PROMOTIONS.getMessage());
		}
	}
}
