package ru.northarea.exchanger;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.northarea.exchanger.model.Currency;
import ru.northarea.exchanger.model.ResponseDto;
import ru.northarea.exchanger.repository.DbConfigRepository;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DeveloperHomeCaseTests {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private DbConfigRepository dbConfigRepository;

	@Test
	void firstCase() throws Exception {
		final var dto = new ResponseDto().amount(BigDecimal.valueOf(100.00)).currency(Currency.EUR);
		final var requestAsByte = objectMapper.writeValueAsBytes(dto);

		final var responseAsString = mockMvc
				.perform(post(AppConstants.EXCHANGE_URL + AppConstants.SELL_URL)
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestAsByte))
				.andDo(print()).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		final var response = objectMapper.readValue(responseAsString, ResponseDto.class);

		assertEquals(BigDecimal.valueOf(79.18), response.getAmount());
		assertEquals(Currency.GBP, response.getCurrency());
	}

	@Test
	void secondCase() throws Exception {
		final var dto = new ResponseDto().amount(BigDecimal.valueOf(100.00)).currency(Currency.GBP);
		final var requestAsByte = objectMapper.writeValueAsBytes(dto);

		final var responseAsString = mockMvc
				.perform(post(AppConstants.EXCHANGE_URL + AppConstants.BUY_URL)
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestAsByte))
				.andDo(print()).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		final var response = objectMapper.readValue(responseAsString, ResponseDto.class);

		assertEquals(BigDecimal.valueOf(126.22), response.getAmount());
		assertEquals(Currency.EUR, response.getCurrency());
	}
}
