package store.validator;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import store.domain.promotion.PrimitivePromotionInfo;

class PromotionValidatingParserTest {
	@DisplayName("상품의 첫번째 줄을 ','(콤마)로 나눈 길이와 열의 길이가 같지 않으면 예외가 발생한다.")
	@Test
	void validateFirstLine() {
		//given
		PromotionValidatingParser promotionValidatingParser = PromotionValidatingParser.getInstance();
		String firstLine = "name,buy,get,start_date";

		//when & then
		assertThatThrownBy(() -> promotionValidatingParser.validateFirstLine(firstLine))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(FileValidationMessage.INVALID_PROMOTION_COLUMN_SIZE.getMessage());
	}

	@DisplayName("프로모션 파일 내용을 읽어 원시 프로모션 목록을 반환한다.")
	@Test
	void processLine() {
		//given
		PromotionValidatingParser promotionValidatingParser = PromotionValidatingParser.getInstance();
		List<String> allLine = List.of("name,buy,get,start_date",
			"탄산2+1,2,1,2024-01-01,2024-12-31",
			"MD추천상품,1,1,2024-01-01,2024-12-31",
			"반짝할인,1,1,2024-11-01,2024-11-30"
		);
		List<PrimitivePromotionInfo> expect = List.of(
			new PrimitivePromotionInfo("탄산2+1", 2, 1, "2024-01-01", "2024-12-31"),
			new PrimitivePromotionInfo("MD추천상품", 1, 1, "2024-01-01", "2024-12-31"),
			new PrimitivePromotionInfo("반짝할인", 1, 1, "2024-11-01", "2024-11-30")
		);

		//when
		List<PrimitivePromotionInfo> result = promotionValidatingParser.processLine(allLine);

		//then
		assertThat(result).containsExactlyElementsOf(expect);
	}

	@DisplayName("프로모션을 ','(콤마)로 나눈 길이와 열의 길이가 같지 않으면 IllegalArgumentException 예외가 발생한다.")
	@Test
	void processLineNotSize() {
		//given
		PromotionValidatingParser promotionValidatingParser = PromotionValidatingParser.getInstance();
		List<String> allLine = List.of("name,buy,get,start_date",
			"탄산2+1,2,1,2024-01-01,2024-12-31",
			"MD추천상품,1,2024-01-01,2024-12-31",
			"반짝할인,1,1,2024-11-01,2024-11-30"
		);

		//when & then
		assertThatThrownBy(() -> promotionValidatingParser.processLine(allLine))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(FileValidationMessage.INVALID_PROMOTION_COLUMN_SIZE.getMessage());
	}

	@DisplayName("프로모션 제공 기준 수량이 숫자가 아니면 IllegalArgumentException 예외가 발생한다.")
	@Test
	void processLineBuyNotNumber() {
		//given
		PromotionValidatingParser promotionValidatingParser = PromotionValidatingParser.getInstance();
		List<String> allLine = List.of("name,buy,get,start_date",
			"탄산2+1,2,1,2024-01-01,2024-12-31",
			"MD추천상품,abcd,1,2024-01-01,2024-12-31",
			"반짝할인,1,1,2024-11-01,2024-11-30"
		);

		//when & then
		assertThatThrownBy(() -> promotionValidatingParser.processLine(allLine))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(FileValidationMessage.INVALID_PROMOTION_BUY_NUMBER.getMessage());
	}

	@DisplayName("프로모션 제공 기준 수량이 최대 범위를 넘어서면 IllegalArgumentException 예외가 발생한다.")
	@Test
	void processLineBuyOverFlow() {
		//given
		PromotionValidatingParser promotionValidatingParser = PromotionValidatingParser.getInstance();
		List<String> allLine = List.of("name,buy,get,start_date",
			"탄산2+1,2,1,2024-01-01,2024-12-31",
			"MD추천상품,10000000000,1,2024-01-01,2024-12-31",
			"반짝할인,1,1,2024-11-01,2024-11-30"
		);

		//when & then
		assertThatThrownBy(() -> promotionValidatingParser.processLine(allLine))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(FileValidationMessage.INVALID_PROMOTION_BUY_OVER_FLOW.getMessage());
	}

	@DisplayName("프로모션 제공 수량이 숫자가 아니면 IllegalArgumentException 예외가 발생한다.")
	@Test
	void processLineGetNotNumber() {
		//given
		PromotionValidatingParser promotionValidatingParser = PromotionValidatingParser.getInstance();
		List<String> allLine = List.of("name,buy,get,start_date",
			"탄산2+1,2,1,2024-01-01,2024-12-31",
			"MD추천상품,1,abcd,2024-01-01,2024-12-31",
			"반짝할인,1,1,2024-11-01,2024-11-30"
		);

		//when & then
		assertThatThrownBy(() -> promotionValidatingParser.processLine(allLine))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(FileValidationMessage.INVALID_PROMOTION_GET_NUMBER.getMessage());
	}

	@DisplayName("프로모션 제공 수량이 최대 범위를 넘어서면 IllegalArgumentException 예외가 발생한다.")
	@Test
	void processLineGetOverFlow() {
		//given
		PromotionValidatingParser promotionValidatingParser = PromotionValidatingParser.getInstance();
		List<String> allLine = List.of("name,buy,get,start_date",
			"탄산2+1,2,1,2024-01-01,2024-12-31",
			"MD추천상품,1,10000000000,2024-01-01,2024-12-31",
			"반짝할인,1,1,2024-11-01,2024-11-30"
		);

		//when & then
		assertThatThrownBy(() -> promotionValidatingParser.processLine(allLine))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(FileValidationMessage.INVALID_PROMOTION_GET_OVER_FLOW.getMessage());
	}

	@DisplayName("프로모션 시작 날짜의 형식이 유효하지 않으면 IllegalArgumentException 예외가 발생한다.")
	@Test
	void processLineNotStartDateFormat() {
		//given
		PromotionValidatingParser promotionValidatingParser = PromotionValidatingParser.getInstance();
		List<String> allLine = List.of("name,buy,get,start_date",
			"탄산2+1,2,1,2024-01-01,2024-12-31",
			"MD추천상품,1,1,2024-010-01,2024-12-31",
			"반짝할인,1,1,2024-11-01,2024-11-30"
		);

		//when & then
		assertThatThrownBy(() -> promotionValidatingParser.processLine(allLine))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(FileValidationMessage.INVALID_PROMOTION_START_DATE.getMessage());
	}

	@DisplayName("프로모션 종료 날짜의 형식이 유효하지 않으면 IllegalArgumentException 예외가 발생한다.")
	@Test
	void processLineNotEndDateFormat() {
		//given
		PromotionValidatingParser promotionValidatingParser = PromotionValidatingParser.getInstance();
		List<String> allLine = List.of("name,buy,get,start_date",
			"탄산2+1,2,1,2024-01-01,2024-12-31",
			"MD추천상품,1,1,2024-01-01,2024-12-311",
			"반짝할인,1,1,2024-11-01,2024-11-30"
		);

		//when & then
		assertThatThrownBy(() -> promotionValidatingParser.processLine(allLine))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(FileValidationMessage.INVALID_PROMOTION_END_DATE.getMessage());
	}

	@DisplayName("프로모션 시작 날짜가 종료 날짜 이후이면 IllegalArgumentException 예외가 발생한다.")
	@Test
	void processLineStatDateIsRaterEndDate() {
		//given
		PromotionValidatingParser promotionValidatingParser = PromotionValidatingParser.getInstance();
		List<String> allLine = List.of("name,buy,get,start_date",
			"탄산2+1,2,1,2024-01-01,2024-12-31",
			"MD추천상품,1,1,2024-12-01,2024-01-01",
			"반짝할인,1,1,2024-11-01,2024-11-30"
		);

		//when & then
		assertThatThrownBy(() -> promotionValidatingParser.processLine(allLine))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(FileValidationMessage.INVALID_PROMOTION_DATE_RANGE.getMessage());
	}
}