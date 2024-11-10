package store.domain.product;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import store.domain.order.OrderItem;
import store.domain.promotion.PrimitivePromotionInfo;
import store.domain.promotion.Promotion;
import store.validator.stock.StockValidateMessage;

class StockTest {
	@DisplayName("중복된 상품이 있다면 IllegalArgumentException 예외가 발생한다.")
	@Test
	void validateDuplicateProduct() {
		//given
		List<Product> products = List.of(
			Product.of(new PrimitiveProductInfo("콜라", 1000, 10, ""), null),
			Product.of(new PrimitiveProductInfo("콜라", 1000, 10, ""), null)
		);

		//when & then
		assertThatThrownBy(() -> Stock.from(products))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(StockValidateMessage.INVALID_DUPLICATE_PRODUCT.getMessage());
	}

	@DisplayName("하나의 상품에 두개 이상의 프로모션이 있다면 IllegalArgumentException 예외가 발생한다.")
	@Test
	void validateDuplicatePromotion() {
		//given
		Promotion promotion1 = Promotion.from(
			new PrimitivePromotionInfo("탄산1+1", 1, 1, "2024-01-01", "2024-12-31")
		);

		Promotion promotion2 = Promotion.from(
			new PrimitivePromotionInfo("탄산2+1", 2, 1, "2024-01-01", "2024-12-31")
		);

		List<Product> products = List.of(
			Product.of(new PrimitiveProductInfo("콜라", 1000, 10, "탄산1+1"), promotion1),
			Product.of(new PrimitiveProductInfo("콜라", 1000, 10, "탄산2+1"), promotion2)
		);

		//when & then
		assertThatThrownBy(() -> Stock.from(products))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(StockValidateMessage.INVALID_DUPLICATE_PROMOTIONS.getMessage());
	}

	@DisplayName("각 상품의 재고를 고려하여 주문 가능 여부를 확인한다.")
	@MethodSource("validStockProvider")
	@ParameterizedTest(name = "{0}")
	void validateOrderItems(String description, List<Product> inputProducts, List<OrderItem> inputOrderItems) {
		//given
		Stock stock = Stock.from(inputProducts);

		//when & then
		stock.validateOrderItems(inputOrderItems);
	}

	@DisplayName("주문이 가능하지 않으면 IllegalStateException 예외가 발생한다.")
	@MethodSource("invalidStockProvider")
	@ParameterizedTest(name = "{0}")
	void validateInvalidOrderItems(String description, List<Product> inputProducts, List<OrderItem> inputOrderItems) {
		//given
		Stock stock = Stock.from(inputProducts);

		//when & then
		assertThatThrownBy(() -> stock.validateOrderItems(inputOrderItems))
			.isInstanceOf(IllegalStateException.class);
	}

	static Stream<Arguments> validStockProvider() {
		return Stream.of(
			Arguments.of(
				"프로모션이 없고 일반 재고가 있는 주문",
				createValidNormalOrderProducts(),
				List.of(new OrderItem("콜라", 10))
			),
			Arguments.of(
				"프로모션이 있고 프로모션이 유효하며 프로모션 재고가 충분한 주문",
				createValidPromotionProducts(),
				List.of(new OrderItem("콜라", 10))
			),
			Arguments.of(
				"프로모션이 있고 프로모션이 유효하지만 프로모션 재고가 부족하고 일반 재고가 있는 주문",
				createValidPromotionOutOfPromotionStockButNormalStockProducts(),
				List.of(new OrderItem("콜라", 10))
			),
			Arguments.of(
				"프로모션이 있지만 유효하지 않고 일반 재고가 있는 주문",
				createValidInvalidPromotionButNormalStockProducts(),
				List.of(new OrderItem("콜라", 5))
			)
		);
	}

	static Stream<Arguments> invalidStockProvider() {
		return Stream.of(
			Arguments.of(
				"없는 상품을 주문",
				createValidPromotionOutOfPromotionOutOfNormalStockProducts(),
				List.of(new OrderItem("커피", 3))
			),
			Arguments.of(
				"프로모션이 있고 프로모션이 유효하지만 프로모션 재고가 부족하고 일반 재고도 부족한 주문",
				createValidPromotionOutOfPromotionOutOfNormalStockProducts(),
				List.of(new OrderItem("콜라", 3))
			),
			Arguments.of(
				"프로모션이 있지만 유효하지 않고 일반 재고도 부족한 주문",
				createValidInvalidPromotionOutOfNormalStockProducts(),
				List.of(new OrderItem("콜라", 6))
			),
			Arguments.of(
				"프로모션이 없고 일반 재고가 부족한 주문",
				createValidOutOfNormalPromotionStockProducts(),
				List.of(new OrderItem("콜라", 2))
			)
		);
	}

	// 프로모션이 없고 일반 재고가 있는 주문
	private static List<Product> createValidNormalOrderProducts() {
		return List.of(
			Product.of(
				"콜라", 1000, 10,
				null
			),
			Product.of(
				"사이다", 1000, 5,
				null
			)
		);
	}

	// 프로모션이 있고 프로모션이 유효하며 프로모션 재고가 충분한 주문
	private static List<Product> createValidPromotionProducts() {
		return List.of(
			Product.of(
				"콜라", 1000, 10,
				Promotion.from(
					new PrimitivePromotionInfo("탄산1+1", 1, 1, "2024-01-01", "2024-12-31"))
			),
			Product.of(
				"콜라", 1000, 5,
				null
			)
		);
	}

	// 프로모션이 있고 프로모션이 유효하지만 프로모션 재고가 부족하고 일반 재고가 있는 주문
	private static List<Product> createValidPromotionOutOfPromotionStockButNormalStockProducts() {
		return List.of(
			Product.of(
				"콜라", 1000, 1,
				Promotion.from(
					new PrimitivePromotionInfo("탄산1+1", 1, 1, "2024-01-01", "2024-12-31"))
			),
			Product.of(
				"콜라", 1000, 9,
				null
			)
		);
	}

	// 프로모션이 있지만 유효하지 않고 일반 재고가 있는 주문
	private static List<Product> createValidInvalidPromotionButNormalStockProducts() {
		return List.of(
			Product.of(
				"콜라", 1000, 1,
				Promotion.from(
					new PrimitivePromotionInfo("탄산1+1", 1, 1, "2023-01-01", "2023-12-31"))
			),
			Product.of(
				"콜라", 1000, 5,
				null
			)
		);
	}

	// 프로모션이 있고 프로모션이 유효하지만 프로모션 재고가 부족하고 일반 재고도 부족한 주문
	private static List<Product> createValidPromotionOutOfPromotionOutOfNormalStockProducts() {
		return List.of(
			Product.of(
				"콜라", 1000, 1,
				Promotion.from(
					new PrimitivePromotionInfo("탄산1+1", 1, 1, "2024-01-01", "2024-12-31"))
			),
			Product.of(
				"콜라", 1000, 1,
				null
			)
		);
	}

	// 프로모션이 있지만 유효하지 않고 일반 재고도 부족한 주문
	private static List<Product> createValidInvalidPromotionOutOfNormalStockProducts() {
		return List.of(
			Product.of(
				"콜라", 1000, 1,
				Promotion.from(
					new PrimitivePromotionInfo("탄산1+1", 1, 1, "2023-01-01", "2023-12-31"))
			),
			Product.of(
				"콜라", 1000, 5,
				null
			)
		);
	}

	// 프로모션이 없고 일반 재고가 부족한 주문
	private static List<Product> createValidOutOfNormalPromotionStockProducts() {
		return List.of(
			Product.of(
				"콜라", 1000, 1,
				null
			),
			Product.of(
				"사이다", 1000, 5,
				null
			)
		);
	}
}