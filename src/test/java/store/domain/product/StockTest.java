package store.domain.product;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import store.domain.promotion.PrimitivePromotionInfo;
import store.domain.promotion.Promotion;
import store.validator.StockValidateMessage;

class StockTest {
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
}