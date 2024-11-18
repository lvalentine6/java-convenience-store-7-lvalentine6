package store.domain.membershop;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MembershipTest {
	@DisplayName("프로모션 미적용 금액을 받아 예상 멤버십 할인 금액을 계산한다.")
	@Test
	void calculateDiscountPrice() {
		//given
		Membership membership = Membership.create();
		int inputNormalPrice = 10000;
		int expect = 3000;

		//when
		int result = membership.calculateDiscountPrice(inputNormalPrice);

		//then
		assertThat(result).isEqualTo(expect);
	}

	@DisplayName("예상 멤버십 할인 금액이 한도를 초과한다면 최대 할인 금액으로 적용한다.")
	@Test
	void calculateDiscountPriceOverLimit() {
		//given
		Membership membership = Membership.create();
		int inputNormalPrice = 100000;
		int expect = 8000;

		//when
		int result = membership.calculateDiscountPrice(inputNormalPrice);

		//then
		assertThat(result).isEqualTo(expect);
	}
}