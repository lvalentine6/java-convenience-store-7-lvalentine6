package store.domain.promotion;

import java.util.List;

public class Promotions {
	private final List<Promotion> promotions;

	private Promotions(List<PrimitivePromotionInfo> infos) {
		this.promotions = create(infos);
	}

	public static Promotions from(List<PrimitivePromotionInfo> infos) {
		return new Promotions(infos);
	}

	private List<Promotion> create(List<PrimitivePromotionInfo> infos) {
		return infos.stream()
			.map(Promotion::from)
			.toList();
	}

	public Promotion findByName(String name) {
		return promotions.stream()
			.filter(p -> p.getName().equals(name))
			.findFirst()
			.orElse(null);
	}
}
