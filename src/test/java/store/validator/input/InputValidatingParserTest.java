package store.validator.input;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import store.domain.order.OrderItem;

class InputValidatingParserTest {
	@DisplayName("주문 상품 목록을 성공적으로 파싱하여 반환한다.")
	@Test
	void validatedOrderItems() {
		//given
		InputValidatingParser inputValidatingParser = InputValidatingParser.getInstance();
		String input = "[콜라-2],[사이다-1]";
		List<OrderItem> expect = List.of(
			new OrderItem("콜라", 2),
			new OrderItem("사이다", 1)
		);

		//when
		List<OrderItem> result = inputValidatingParser.validatedOrderItems(input);

		//then
		assertThat(result).containsExactlyElementsOf(expect);
	}

	@DisplayName("주문 상품의 이름과 수량이 '[](대괄호)로 묶여있지 않으면 예외가 발생한다.")
	@Test
	void validateStartEndFormat() {
		//given
		InputValidatingParser inputValidatingParser = InputValidatingParser.getInstance();
		String input = "콜라-2],[사이다-1]";

		//when & then
		assertThatThrownBy(() -> inputValidatingParser.validatedOrderItems(input))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(InputValidationMessage.INVALID_ORDER_LIST_FORMAT.getMessage());
	}

	@DisplayName("주문 상품의 이름과 수량이 '-(하이픈)'으로 나눌 수 없으면 예외가 발생한다.")
	@Test
	void validateContentsFormat() {
		//given
		InputValidatingParser inputValidatingParser = InputValidatingParser.getInstance();
		String input = "[콜라,2],[사이다-1]";

		//when & then
		assertThatThrownBy(() -> inputValidatingParser.validatedOrderItems(input))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(InputValidationMessage.INVALID_ORDER_LIST_FORMAT.getMessage());
	}

	@DisplayName("주문 수량이 숫자가 아니면 예외가 발생한다.")
	@Test
	void validateNumber() {
		//given
		InputValidatingParser inputValidatingParser = InputValidatingParser.getInstance();
		String input = "[콜라-abc],[사이다-1]";

		//when & then
		assertThatThrownBy(() -> inputValidatingParser.validatedOrderItems(input))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(InputValidationMessage.INVALID_ORDER_LIST_FORMAT.getMessage());
	}

	@DisplayName("주문 수량이 최소 수량보다 작으면 예외가 발생한다.")
	@Test
	void validateMinQuantity() {
		//given
		InputValidatingParser inputValidatingParser = InputValidatingParser.getInstance();
		String input = "[콜라-0],[사이다-1]";

		//when & then
		assertThatThrownBy(() -> inputValidatingParser.validatedOrderItems(input))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(InputValidationMessage.INVALID_ORDER_LIST_FORMAT.getMessage());
	}

	@DisplayName("주문한 상품이 중복되면 예외가 발생한다.")
	@Test
	void validateDuplication() {
		//given
		InputValidatingParser inputValidatingParser = InputValidatingParser.getInstance();
		String input = "[콜라-2],[콜라-1]";

		//when & then
		assertThatThrownBy(() -> inputValidatingParser.validatedOrderItems(input))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(InputValidationMessage.INVALID_ORDER_LIST_FORMAT.getMessage());
	}

	@DisplayName("추가 주문 답변이 'Y' 또는 'N'이면 성공적으로 반환한다.")
	@Test
	void validateAdditionalAnswer() {
		//given
		InputValidatingParser inputValidatingParser = InputValidatingParser.getInstance();
		String input = "Y";

		//when
		String result = inputValidatingParser.validateAdditionalAnswer(input);

		//then
		assertThat(result).isEqualTo(input);
	}

	@DisplayName("추가 주문 답변이 'Y' 또는 'N'이 아니면 예외가 발생한다.")
	@Test
	void validateAdditionalAnswerFormat() {
		//given
		InputValidatingParser inputValidatingParser = InputValidatingParser.getInstance();
		String input = "A";

		//when & then
		assertThatThrownBy(() -> inputValidatingParser.validateAdditionalAnswer(input))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(InputValidationMessage.INVALID_ETC_FORMAT.getMessage());
	}

	@DisplayName("주문 수량이 최대 범위를 넘어서면 예외가 발생한다")
	@Test
	void validateOverFlow() {
		//given
		InputValidatingParser inputValidatingParser = InputValidatingParser.getInstance();
		String input = "[콜라-12345678901]";  // 11자리 숫자

		//when & then
		assertThatThrownBy(() -> inputValidatingParser.validatedOrderItems(input))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(InputValidationMessage.INVALID_ORDER_LIST_FORMAT.getMessage());
	}
}