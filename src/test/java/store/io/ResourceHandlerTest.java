package store.io;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import store.domain.product.PrimitiveProductInfo;
import store.domain.promotion.PrimitivePromotionInfo;
import store.validator.FileValidationMessage;

class ResourceHandlerTest {
	@DisplayName("상품 파일을 읽어 원시 상품 정보를 반환한다.")
	@Test
	void readProductFrom() {
		//given
		ResourceHandler resourceHandler = ResourceHandler.getInstance();
		List<PrimitiveProductInfo> expect = List.of(
			new PrimitiveProductInfo("콜라", 1000, 10, "탄산2+1"),
			new PrimitiveProductInfo("콜라", 1000, 10, "null"),
			new PrimitiveProductInfo("사이다", 1000, 8, "탄산2+1"),
			new PrimitiveProductInfo("사이다", 1000, 7, "null")
		);

		//when
		List<PrimitiveProductInfo> result = resourceHandler.readProductFrom("/validProducts.md");

		//then
		assertThat(result).containsExactlyElementsOf(expect);
	}

	@DisplayName("상품 파일이 없다면 IllegalArgumentException 예외가 발생한다.")
	@Test
	void readProductFromNotExist() {
		//given
		ResourceHandler resourceHandler = ResourceHandler.getInstance();

		//when & then
		assertThatThrownBy(() -> resourceHandler.readProductFrom("/products123.md"))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(FileValidationMessage.INVALID_NOT_FOUND.getMessage());
	}

	@DisplayName("빈 상품 파일이라면 IllegalArgumentException 예외가 발생한다.")
	@Test
	void readProductFromEmpty() {
		//given
		ResourceHandler resourceHandler = ResourceHandler.getInstance();

		//when & then
		assertThatThrownBy(() -> resourceHandler.readProductFrom("/emptyProducts.md"))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(FileValidationMessage.INVALID_EMPTY.getMessage());
	}

	@DisplayName("프로모션 파일을 읽어 원시 프로모션 목록을 반환한다.")
	@Test
	void readPromotionFrom() {
		//given
		ResourceHandler resourceHandler = ResourceHandler.getInstance();
		List<PrimitivePromotionInfo> expect = List.of(
			new PrimitivePromotionInfo("탄산2+1", 2, 1, "2024-01-01", "2024-12-31")
		);

		//when
		List<PrimitivePromotionInfo> result = resourceHandler.readPromotionFrom("/validPromotions.md");

		//then
		assertThat(result).containsExactlyElementsOf(expect);
	}

	@DisplayName("프로모션 파일이 없다면 IllegalArgumentException 예외가 발생한다.")
	@Test
	void readPromotionFromNotExist() {
		//given
		ResourceHandler resourceHandler = ResourceHandler.getInstance();

		//when & then
		assertThatThrownBy(() -> resourceHandler.readPromotionFrom("/promotions123.md"))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(FileValidationMessage.INVALID_NOT_FOUND.getMessage());
	}

	@DisplayName("빈 프로모션 파일이라면 IllegalArgumentException 예외가 발생한다.")
	@Test
	void readPromotionFromEmpty() {
		//given
		ResourceHandler resourceHandler = ResourceHandler.getInstance();

		//when & then
		assertThatThrownBy(() -> resourceHandler.readPromotionFrom("/emptyProducts.md"))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(FileValidationMessage.INVALID_EMPTY.getMessage());
	}
}