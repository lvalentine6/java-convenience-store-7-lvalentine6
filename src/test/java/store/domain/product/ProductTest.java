package store.domain.product;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import store.domain.promotion.PrimitivePromotionInfo;
import store.domain.promotion.Promotion;
import store.validator.ProductValidateMessage;

class ProductTest {
	@DisplayName("원시 상품과 프로모션을 받아 상품을 생성한다.")
	@Test
	void of() {
		//given
		PrimitiveProductInfo primitiveProductInfo = new PrimitiveProductInfo(
			"콜라", 1000, 10, "탄산1+1"
		);
		PrimitivePromotionInfo primitivePromotionInfo = new PrimitivePromotionInfo(
			"탄산1+1", 1, 1, "2024-01-01", "2024-12-31");
		Promotion promotion = Promotion.from(primitivePromotionInfo);

		//when
		Product result = Product.of(primitiveProductInfo, promotion);

		//then
		assertThat(result).extracting(Product::getName).isEqualTo("콜라");
	}

	@DisplayName("상품이 프로모션을 가지고 있는지 반환한다.")
	@Test
	void hasPromotion() {
		//given
		PrimitiveProductInfo primitiveProductInfo = new PrimitiveProductInfo(
			"콜라", 1000, 10, "탄산1+1"
		);
		PrimitivePromotionInfo primitivePromotionInfo = new PrimitivePromotionInfo(
			"탄산1+1", 1, 1, "2024-01-01", "2024-12-31");
		Promotion promotion = Promotion.from(primitivePromotionInfo);

		//when
		Product result = Product.of(primitiveProductInfo, promotion);

		//then
		assertThat(result).extracting(Product::hasPromotion).isEqualTo(true);
	}

	@DisplayName("상품의 이름이 비어있다면 IllegalArgumentException 예외가 발생한다.")
	@Test
	void ofEmptyName() {
		//given
		PrimitiveProductInfo primitiveProductInfo = new PrimitiveProductInfo(
			"null", 1000, 10, "탄산1+1"
		);
		PrimitivePromotionInfo primitivePromotionInfo = new PrimitivePromotionInfo(
			"탄산1+1", 1, 1, "2024-01-01", "2024-12-31");
		Promotion promotion = Promotion.from(primitivePromotionInfo);

		//when & then
		assertThatThrownBy(() -> Product.of(primitiveProductInfo, promotion))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(ProductValidateMessage.INVALID_NAME.getMessage());
	}

	@DisplayName("상품의 가격이 0 보다 크지 않다면 IllegalArgumentException 예외가 발생한다.")
	@Test
	void ofPriceMoreThanZero() {
		//given
		PrimitiveProductInfo primitiveProductInfo = new PrimitiveProductInfo(
			"콜라", 0, 10, "탄산1+1"
		);
		PrimitivePromotionInfo primitivePromotionInfo = new PrimitivePromotionInfo(
			"탄산1+1", 1, 1, "2024-01-01", "2024-12-31");
		Promotion promotion = Promotion.from(primitivePromotionInfo);

		//when & then
		assertThatThrownBy(() -> Product.of(primitiveProductInfo, promotion))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(ProductValidateMessage.INVALID_MIN_PRICE.getMessage());
	}

	@DisplayName("상픔의 수량이 0 보다 작다면 IllegalArgumentException 예외가 발생한다.")
	@Test
	void ofQuantityMoreThanZero() {
		//given
		PrimitiveProductInfo primitiveProductInfo = new PrimitiveProductInfo(
			"콜라", 1000, -1, "탄산1+1"
		);
		PrimitivePromotionInfo primitivePromotionInfo = new PrimitivePromotionInfo(
			"탄산1+1", 1, 1, "2024-01-01", "2024-12-31");
		Promotion promotion = Promotion.from(primitivePromotionInfo);

		//when & then
		assertThatThrownBy(() -> Product.of(primitiveProductInfo, promotion))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(ProductValidateMessage.INVALID_MIN_QUANTITY.getMessage());
	}
}