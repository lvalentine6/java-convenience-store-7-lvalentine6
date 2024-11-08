package store.validator;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import store.domain.product.PrimitiveProductInfo;

class ProductValidatingParserTest {
	@DisplayName("상품의 첫번째 줄을 ','(콤마)로 나눈 길이와 열의 길이가 같지 않으면 IllegalArgumentException 예외가 발생한다.")
	@Test
	void validateFirstLine() {
		//given
		ProductValidatingParser productValidatingParser = ProductValidatingParser.getInstance();
		String firstLine = "name,price,quantity";

		//when & then
		assertThatThrownBy(() -> productValidatingParser.validateFirstLine(firstLine))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(FileValidationMessage.INVALID_PRODUCT_COLUMN_SIZE.getMessage());

	}

	@DisplayName("상품 파일 내용을 읽어 원시 상품 목록을 반환한다.")
	@Test
	void processLine() {
		//given
		ProductValidatingParser productValidatingParser = ProductValidatingParser.getInstance();
		List<String> allLine = List.of("name,price,quantity,promotion",
			"콜라,1000,10,탄산2+1",
			"콜라,1000,10,null",
			"사이다,1000,8,탄산2+1",
			"사이다,1000,7,null"
		);
		List<PrimitiveProductInfo> expect = List.of(
			new PrimitiveProductInfo("콜라", 1000, 10, "탄산2+1"),
			new PrimitiveProductInfo("콜라", 1000, 10, "null"),
			new PrimitiveProductInfo("사이다", 1000, 8, "탄산2+1"),
			new PrimitiveProductInfo("사이다", 1000, 7, "null")
		);

		//when
		List<PrimitiveProductInfo> result = productValidatingParser.processLine(allLine);

		//then
		assertThat(result).containsExactlyElementsOf(expect);
	}

	@DisplayName("상품을 ','(콤마)로 나눈 길이와 열의 길이가 같지 않으면 IllegalArgumentException 예외가 발생한다.")
	@Test
	void processLineNotSize() {
		//given
		ProductValidatingParser productValidatingParser = ProductValidatingParser.getInstance();
		List<String> allLine = List.of("name,price,quantity,promotion",
			"콜라,1000,10",
			"콜라,1000,10,null",
			"사이다,1000,8,탄산2+1",
			"사이다,1000,7,null"
		);

		//when & then
		assertThatThrownBy(() -> productValidatingParser.processLine(allLine))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(FileValidationMessage.INVALID_PRODUCT_COLUMN_SIZE.getMessage());
	}

	@DisplayName("상품의 가격이 숫자가 아니면 IllegalArgumentException 예외가 발생한다.")
	@Test
	void processLinePriceNotNumber() {
		//given
		ProductValidatingParser productValidatingParser = ProductValidatingParser.getInstance();
		List<String> allLine = List.of("name,price,quantity,promotion",
			"콜라,1000,10,탄산2+1",
			"콜라,abcd,10,null",
			"사이다,1000,8,탄산2+1",
			"사이다,1000,7,null"
		);

		//when & then
		assertThatThrownBy(() -> productValidatingParser.processLine(allLine))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(FileValidationMessage.INVALID_PRODUCT_PRICE_NUMBER.getMessage());
	}

	@DisplayName("상품의 가격이 최대 범위를 넘어서면 IllegalArgumentException 예외가 발생한다.")
	@Test
	void processLinePriceOverFlow() {
		//given
		ProductValidatingParser productValidatingParser = ProductValidatingParser.getInstance();
		List<String> allLine = List.of("name,price,quantity,promotion",
			"콜라,1000,10,탄산2+1",
			"콜라,10000000000,10,null",
			"사이다,1000,8,탄산2+1",
			"사이다,1000,7,null"
		);

		//when & then
		assertThatThrownBy(() -> productValidatingParser.processLine(allLine))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(FileValidationMessage.INVALID_PRODUCT_PRICE_OVER_FLOW.getMessage());
	}

	@DisplayName("상품의 가격이 0과 같거나 작으면 IllegalArgumentException 예외가 발생한다.")
	@Test
	void processLinePriceUnderMinNumber() {
		//given
		ProductValidatingParser productValidatingParser = ProductValidatingParser.getInstance();
		List<String> allLine = List.of("name,price,quantity,promotion",
			"콜라,1000,10,탄산2+1",
			"콜라,0,10,null",
			"사이다,1000,8,탄산2+1",
			"사이다,1000,7,null"
		);

		//when & then
		assertThatThrownBy(() -> productValidatingParser.processLine(allLine))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(FileValidationMessage.INVALID_PRODUCT_PRICE_MIN_NUMBER.getMessage());
	}

	@DisplayName("상품의 수량이 숫자가 아니면 IllegalArgumentException 예외가 발생한다.")
	@Test
	void processLineQuantityNotNumber() {
		//given
		ProductValidatingParser productValidatingParser = ProductValidatingParser.getInstance();
		List<String> allLine = List.of("name,price,quantity,promotion",
			"콜라,1000,10,탄산2+1",
			"콜라,1000,abcd,null",
			"사이다,1000,8,탄산2+1",
			"사이다,1000,7,null"
		);

		//when & then
		assertThatThrownBy(() -> productValidatingParser.processLine(allLine))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(FileValidationMessage.INVALID_PRODUCT_QUANTITY_NUMBER.getMessage());
	}

	@DisplayName("상품의 수량이 최대 범위를 넘어서면 IllegalArgumentException 예외가 발생한다.")
	@Test
	void processLineQuantityOverFlow() {
		//given
		ProductValidatingParser productValidatingParser = ProductValidatingParser.getInstance();
		List<String> allLine = List.of("name,price,quantity,promotion",
			"콜라,1000,10,탄산2+1",
			"콜라,1000,10,null",
			"사이다,1000,80000000000,탄산2+1",
			"사이다,1000,7,null"
		);

		//when & then
		assertThatThrownBy(() -> productValidatingParser.processLine(allLine))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(FileValidationMessage.INVALID_PRODUCT_QUANTITY_OVER_FLOW.getMessage());
	}

	@DisplayName("상품의 수량이 0보다 작으면 IllegalArgumentException 예외가 발생한다.")
	@Test
	void processLineQuantityUnderMinNumber() {
		//given
		ProductValidatingParser productValidatingParser = ProductValidatingParser.getInstance();
		List<String> allLine = List.of("name,price,quantity,promotion",
			"콜라,1000,10,탄산2+1",
			"콜라,1000,10,null",
			"사이다,1000,-1,탄산2+1",
			"사이다,1000,7,null"
		);

		//when & then
		assertThatThrownBy(() -> productValidatingParser.processLine(allLine))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(FileValidationMessage.INVALID_PRODUCT_QUANTITY_MIN_NUMBER.getMessage());
	}
}