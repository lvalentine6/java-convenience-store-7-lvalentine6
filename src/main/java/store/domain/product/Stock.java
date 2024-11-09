package store.domain.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import store.validator.stock.StockValidateMessage;

public class Stock {
	private final List<Product> products;

	private Stock(List<Product> products) {
		validateDuplicateProduct(products);
		validateDuplicatePromotion(products);
		this.products = addOutOfStockVersions(products);
	}

	public static Stock from(List<Product> products) {
		return new Stock(products);
	}

	private void validateDuplicateProduct(List<Product> products) {
		Map<String, Long> productCount = products.stream()
			.filter(product -> !product.hasPromotion())
			.collect(Collectors.groupingBy(Product::getName, Collectors.counting()));

		if (productCount.values().stream().anyMatch(count -> count > 1)) {
			throw new IllegalArgumentException(StockValidateMessage.INVALID_DUPLICATE_PRODUCT.getMessage());
		}
	}

	private void validateDuplicatePromotion(List<Product> products) {
		Map<String, Long> promotionCount = products.stream()
			.filter(Product::hasPromotion)
			.collect(Collectors.groupingBy(Product::getName, Collectors.counting()));

		if (promotionCount.values().stream().anyMatch(count -> count > 1)) {
			throw new IllegalArgumentException(StockValidateMessage.INVALID_DUPLICATE_PROMOTIONS.getMessage());
		}
	}

	private List<Product> addOutOfStockVersions(List<Product> products) {
		Set<String> hasNormalVersions = findProductsWithNormalVersion(products);
		List<Product> result = new ArrayList<>();
		products.forEach(p -> {
			result.add(p);
			if (p.hasPromotion() && !hasNormalVersions.contains(p.getName())) {
				result.add(createOutOfStockVersion(p));
			}
		});
		return result;
	}

	private Set<String> findProductsWithNormalVersion(List<Product> products) {
		return products.stream()
			.filter(p -> !p.hasPromotion())
			.map(Product::getName)
			.collect(Collectors.toSet());
	}

	private Product createOutOfStockVersion(Product product) {
		return Product.of(
			new PrimitiveProductInfo(
				product.getName(),
				product.getPrice(),
				0,
				null),
			null
		);
	}

	public List<Product> getProducts() {
		return products;
	}
}
