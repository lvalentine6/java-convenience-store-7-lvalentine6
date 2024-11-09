package store.domain.promotion;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PromotionsTest {
	@DisplayName("원시 프로모션 목록을 받아 프로모션 목록을 성공적으로 생성한다.")
	@Test
	void from() {
		//given
		List<PrimitivePromotionInfo> infos = List.of(
			new PrimitivePromotionInfo("탄산1+1", 1, 1, "2024-01-01", "2024-12-31"),
			new PrimitivePromotionInfo("과자2+1", 2, 1, "2024-11-01", "2024-12-31"),
			new PrimitivePromotionInfo("MD추천1+1", 1, 1, "2024-05-01", "2024-12-31")
		);

		//when
		Promotions result = Promotions.from(infos);

		//then
		assertThat(result).isNotNull();
	}

	@DisplayName("원시 프로모션 목록을 받아 프로모션 목록을 성공적으로 생성한다.")
	@Test
	void findByName() {
		//given
		List<PrimitivePromotionInfo> infos = List.of(
			new PrimitivePromotionInfo("탄산1+1", 1, 1, "2024-01-01", "2024-12-31"),
			new PrimitivePromotionInfo("과자2+1", 2, 1, "2024-11-01", "2024-12-31"),
			new PrimitivePromotionInfo("MD추천1+1", 1, 1, "2024-05-01", "2024-12-31")
		);
		Promotion expect = Promotion.from(
			new PrimitivePromotionInfo("탄산1+1", 1, 1, "2024-01-01", "2024-12-31")
		);
		Promotions promotions = Promotions.from(infos);

		//when
		Promotion result = promotions.findByName("탄산1+1");

		//then
		assertThat(result).isEqualTo(expect);
	}

	@DisplayName("상품의 프로모션 이름에 해당되는것이 없다면 null을 반환한다.")
	@Test
	void findByNameNull() {
		//given
		List<PrimitivePromotionInfo> infos = List.of(
			new PrimitivePromotionInfo("탄산1+1", 1, 1, "2024-01-01", "2024-12-31"),
			new PrimitivePromotionInfo("과자2+1", 2, 1, "2024-11-01", "2024-12-31"),
			new PrimitivePromotionInfo("MD추천1+1", 1, 1, "2024-05-01", "2024-12-31")
		);
		Promotions promotions = Promotions.from(infos);

		//when
		Promotion result = promotions.findByName("탄산1+2");

		//then
		assertThat(result).isNull();
	}
}