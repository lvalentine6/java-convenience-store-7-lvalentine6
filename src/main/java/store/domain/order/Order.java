package store.domain.order;

import java.util.List;

import store.domain.membershop.Membership;

public class Order {
	private final List<OrderLineItem> orderLineItems;
	private final Membership membership;
	private final String hasMembership;

	private Order(List<OrderLineItem> orderLineItems, Membership membership, String hasMembership) {
		this.orderLineItems = orderLineItems;
		this.membership = membership;
		this.hasMembership = hasMembership;
	}

	public static Order of(List<OrderLineItem> orderLineItems, Membership membership, String hasMembership) {
		return new Order(orderLineItems, membership, hasMembership);
	}

	public List<OrderLineItem> getOrderLineItems() {
		return orderLineItems;
	}

	public List<OrderLineItem> getPromotionItems() {
		return orderLineItems.stream()
			.filter(item -> item.isPromotionApplied() && item.getFreeQuantity() > 0)
			.toList();
	}

	public int getTotalAmount() {
		return orderLineItems.stream()
			.mapToInt(item -> item.getTotalQuantity() * item.getUnitPrice())
			.sum();
	}

	public int getPromotionDiscount() {
		return orderLineItems.stream()
			.filter(OrderLineItem::isPromotionApplied)
			.mapToInt(item -> item.getFreeQuantity() * item.getUnitPrice())
			.sum();
	}

	public int getMembershipDiscount() {
		if (!hasMembership.equals("Y")) {
			return 0;
		}
		int normalAmount = getNormalAmount();
		return membership.calculateDiscountPrice(normalAmount);
	}

	public int getFinalAmount() {
		return getTotalAmount() - getPromotionDiscount() - getMembershipDiscount();
	}

	public int getTotalQuantity() {
		return orderLineItems.stream()
			.mapToInt(OrderLineItem::getTotalQuantity)
			.sum();
	}

	private int getNormalAmount() {
		return orderLineItems.stream()
			.filter(item -> !item.isPromotionApplied())
			.mapToInt(item -> item.getNormalQuantity() * item.getUnitPrice())
			.sum();
	}
}
