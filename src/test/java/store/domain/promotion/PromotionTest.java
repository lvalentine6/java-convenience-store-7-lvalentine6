package store.domain.promotion;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import store.validator.promotion.PromotionValidateMessage;

class PromotionTest {
	@DisplayName("원시 프로모션을 받아 성공적으로 프로모션을 생성한다.")
	@Test
	void from() {
		//given
		PrimitivePromotionInfo primitivePromotionInfo = new PrimitivePromotionInfo(
			"탄산1+1", 1, 1, "2024-01-01", "2024-12-31");

		//when
		Promotion result = Promotion.from(primitivePromotionInfo);

		//then
		assertThat(result).extracting(Promotion::getName).isEqualTo("탄산1+1");
	}

	@DisplayName("프로모션의 이름이 비어있다면 IllegalArgumentException 예외가 발생한다.")
	@Test
	void fromEmptyName() {
		//given
		PrimitivePromotionInfo primitivePromotionInfo = new PrimitivePromotionInfo(
			"", 1, 1, "2024-01-01", "2024-12-31");

		//when & then
		assertThatThrownBy(() -> Promotion.from(primitivePromotionInfo))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(PromotionValidateMessage.INVALID_NAME.getMessage());
	}

	@DisplayName("프로모션의 구매수량과 증정수량이 0보다 크지 않다면 IllegalArgumentException 예외가 발생한다.")
	@Test
	void fromBuyAndGetMoreThenZero() {
		//given
		PrimitivePromotionInfo primitivePromotionInfo = new PrimitivePromotionInfo(
			"탄산1+1", 0, 0, "2024-01-01", "2024-12-31");

		//when & then
		assertThatThrownBy(() -> Promotion.from(primitivePromotionInfo))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(PromotionValidateMessage.INVALID_MIN_QUANTITY.getMessage());
	}

	@DisplayName("프로모션의 구매수량이 증정수량보다 작다면 IllegalArgumentException 예외가 발생한다.")
	@Test
	void fromBuyLessThenGet() {
		//given
		PrimitivePromotionInfo primitivePromotionInfo = new PrimitivePromotionInfo(
			"탄산1+1", 1, 2, "2024-01-01", "2024-12-31");

		//when & then
		assertThatThrownBy(() -> Promotion.from(primitivePromotionInfo))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(PromotionValidateMessage.INVALID_BUY_QUANTITY_UNDER_GET_QUANTITY.getMessage());
	}

	@DisplayName("프로모션의 시작날짜와 종료날짜의 형식이 맞지않으면 IllegalArgumentException 예외가 발생한다.")
	@Test
	void fromDateFormat() {
		//given
		PrimitivePromotionInfo primitivePromotionInfo = new PrimitivePromotionInfo(
			"탄산1+1", 1, 1, "20920-22-11", "2024-01-01");

		//when & then
		assertThatThrownBy(() -> Promotion.from(primitivePromotionInfo))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(PromotionValidateMessage.INVALID_DATE_FORMAT.getMessage());
	}

	@DisplayName("프로모션의 시작날짜와 종료날짜가 비어있다면 IllegalArgumentException 예외가 발생한다.")
	@Test
	void fromDateNull() {
		//given
		PrimitivePromotionInfo primitivePromotionInfo = new PrimitivePromotionInfo(
			"탄산1+1", 1, 1, null, null);

		//when & then
		assertThatThrownBy(() -> Promotion.from(primitivePromotionInfo))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(PromotionValidateMessage.INVALID_DATE.getMessage());
	}

	@DisplayName("프로모션의 시작날짜가 종료날짜 이전이라면 IllegalArgumentException 예외가 발생한다.")
	@Test
	void fromDateRange() {
		//given
		PrimitivePromotionInfo primitivePromotionInfo = new PrimitivePromotionInfo(
			"탄산1+1", 1, 1, "2024-12-01", "2024-01-01");

		//when & then
		assertThatThrownBy(() -> Promotion.from(primitivePromotionInfo))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(PromotionValidateMessage.INVALID_START_DATE_BEFORE_END_DATE.getMessage());
	}
}