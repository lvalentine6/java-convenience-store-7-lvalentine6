package store.domain.order;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import store.domain.membershop.Membership;

class OrderTest {
	@DisplayName("주문 항목 목록, 맴버십, 맴버십 적용여부를 받아 주문을 생성한다.")
	@Test
	void of() {
		//given
		List<OrderLineItem> orderLineItems = List.of(
			OrderLineItem.of("콜라", 10, 1000, 3, 0, 1, true),
			OrderLineItem.of("사이다", 5, 1000, 0, 5, 0, false)
		);
		Membership membership = Membership.create();
		String hasMembership = "Y";

		//when
		Order order = Order.of(orderLineItems, membership, hasMembership);

		//then
		assertThat(order.getOrderLineItems()).isEqualTo(orderLineItems);
	}

	@DisplayName("주문 항목 목록을 반환한다.")
	@Test
	void getOrderLineItems() {
		//given
		List<OrderLineItem> orderLineItems = List.of(
			OrderLineItem.of("콜라", 10, 1000, 3, 0, 1, true),
			OrderLineItem.of("사이다", 5, 1000, 0, 5, 0, false)
		);
		Membership membership = Membership.create();
		Order order = Order.of(orderLineItems, membership, "Y");

		//when
		List<OrderLineItem> result = order.getOrderLineItems();

		//then
		assertThat(result).isEqualTo(orderLineItems);
	}

	@DisplayName("프로모션이 적용된 상품 목록을 반환한다.")
	@Test
	void getPromotionItems() {
		//given
		List<OrderLineItem> orderLineItems = List.of(
			OrderLineItem.of("콜라", 10, 1000, 3, 0, 1, true),
			OrderLineItem.of("사이다", 5, 1000, 0, 5, 0, false)
		);
		Membership membership = Membership.create();
		Order order = Order.of(orderLineItems, membership, "Y");

		//when
		List<OrderLineItem> promotionItems = order.getPromotionItems();

		//then
		assertThat(promotionItems).hasSize(1);
		assertThat(promotionItems).extracting(OrderLineItem::getName).containsExactly("콜라");
	}

	@DisplayName("총 주문 금액을 계산한다.")
	@Test
	void getTotalAmount() {
		//given
		List<OrderLineItem> orderLineItems = List.of(
			OrderLineItem.of("콜라", 10, 1000, 3, 0, 1, true),
			OrderLineItem.of("사이다", 5, 1000, 0, 5, 0, false)
		);
		Membership membership = Membership.create();
		Order order = Order.of(orderLineItems, membership, "Y");

		//when
		int totalAmount = order.getTotalAmount();

		//then
		assertThat(totalAmount).isEqualTo(15000);
	}

	@DisplayName("프로모션 할인 금액을 계산한다.")
	@Test
	void getPromotionDiscount() {
		//given
		List<OrderLineItem> orderLineItems = List.of(
			OrderLineItem.of("콜라", 10, 1000, 3, 0, 1, true),
			OrderLineItem.of("사이다", 5, 1000, 0, 5, 0, false)
		);
		Membership membership = Membership.create();
		Order order = Order.of(orderLineItems, membership, "Y");

		//when
		int promotionDiscount = order.getPromotionDiscount();

		//then
		assertThat(promotionDiscount).isEqualTo(1000);
	}

	@DisplayName("멤버십 할인 금액을 계산한다.")
	@Test
	void getMembershipDiscount() {
		//given
		List<OrderLineItem> orderLineItems = List.of(
			OrderLineItem.of("콜라", 10, 1000, 3, 0, 1, true),
			OrderLineItem.of("사이다", 5, 1000, 0, 5, 0, false)
		);
		Membership membership = Membership.create();
		Order orderWithMembership = Order.of(orderLineItems, membership, "Y");
		Order orderWithoutMembership = Order.of(orderLineItems, membership, "N");

		//when
		int discountWithMembership = orderWithMembership.getMembershipDiscount();
		int discountWithoutMembership = orderWithoutMembership.getMembershipDiscount();

		//then
		int normalAmount = 5000;
		assertThat(discountWithMembership).isEqualTo(membership.calculateDiscountPrice(normalAmount));
		assertThat(discountWithoutMembership).isEqualTo(0);
	}

	@DisplayName("최종 결제 금액을 계산한다.")
	@Test
	void getFinalAmount() {
		//given
		List<OrderLineItem> orderLineItems = List.of(
			OrderLineItem.of("콜라", 10, 1000, 3, 0, 1, true),
			OrderLineItem.of("사이다", 5, 1000, 0, 5, 0, false)
		);
		Membership membership = Membership.create();
		Order order = Order.of(orderLineItems, membership, "Y");

		//when
		int finalAmount = order.getFinalAmount();

		//then
		int expectedAmount = 15000 - 1000 - order.getMembershipDiscount();
		assertThat(finalAmount).isEqualTo(expectedAmount);
	}

	@DisplayName("총 주문 수량을 계산한다.")
	@Test
	void getTotalQuantity() {
		//given
		List<OrderLineItem> orderLineItems = List.of(
			OrderLineItem.of("콜라", 10, 1000, 3, 0, 1, true),
			OrderLineItem.of("사이다", 5, 1000, 0, 5, 0, false)
		);
		Membership membership = Membership.create();
		Order order = Order.of(orderLineItems, membership, "Y");

		//when
		int totalQuantity = order.getTotalQuantity();

		//then
		assertThat(totalQuantity).isEqualTo(15);
	}
}