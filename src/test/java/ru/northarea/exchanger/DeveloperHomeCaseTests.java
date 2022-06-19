package ru.northarea.exchanger;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.northarea.exchanger.model.Currency;
import ru.northarea.exchanger.model.RequestDto;
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
	void firstCaseSellEURBuyGBP() throws Exception {
		final var dto = new RequestDto().amount(BigDecimal.valueOf(100.00)).sell(Currency.EUR).buy(Currency.GBP);
		final var requestAsByte = objectMapper.writeValueAsBytes(dto);

		final String responseAsString = requestToWebServer(AppConstants.SELL_URL, requestAsByte);

		final var response = objectMapper.readValue(responseAsString, ResponseDto.class);

		assertEquals(new BigDecimal("79.18"), response.getAmount());
		assertEquals(new BigDecimal("1.26294"), response.getRate());
		assertEquals(Currency.GBP, response.getCurrency());
	}

	@Test
	void firstCaseGBPToEUR() throws Exception {
		final var dto = new RequestDto().amount(BigDecimal.valueOf(100.00)).sell(Currency.GBP).buy(Currency.EUR);
		final var requestAsByte = objectMapper.writeValueAsBytes(dto);

		final String responseAsString = requestToWebServer(AppConstants.SELL_URL, requestAsByte);

		final var response = objectMapper.readValue(responseAsString, ResponseDto.class);

		assertEquals(new BigDecimal("121.27"), response.getAmount());
		assertEquals(new BigDecimal("0.8246"), response.getRate());
		assertEquals(Currency.EUR, response.getCurrency());
	}

	@Test
	void secondCaseEURToGBP() throws Exception {
		final var dto = new RequestDto().amount(BigDecimal.valueOf(100.00)).sell(Currency.EUR).buy(Currency.GBP);
		final var requestAsByte = objectMapper.writeValueAsBytes(dto);

		final String responseAsString = requestToWebServer(AppConstants.BUY_URL, requestAsByte);

		final var response = objectMapper.readValue(responseAsString, ResponseDto.class);

		assertEquals(new BigDecimal("82.41"), response.getAmount());
		assertEquals(new BigDecimal("1.21344"), response.getRate());
		assertEquals(Currency.GBP, response.getCurrency());
	}

	@Test
	void secondCaseGBPToEUR() throws Exception {
		final var dto = new RequestDto().amount(BigDecimal.valueOf(100.00)).sell(Currency.GBP).buy(Currency.EUR);
		final var requestAsByte = objectMapper.writeValueAsBytes(dto);

		final String responseAsString = requestToWebServer(AppConstants.BUY_URL, requestAsByte);

		final var response = objectMapper.readValue(responseAsString, ResponseDto.class);

		assertEquals(new BigDecimal("126.22"), response.getAmount());
		assertEquals(new BigDecimal("0.79226"), response.getRate());
		assertEquals(Currency.EUR, response.getCurrency());
	}

	private String requestToWebServer(String sellUrl, byte[] requestAsByte) throws Exception {
		return mockMvc.perform(post(AppConstants.EXCHANGE_URL + sellUrl)
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestAsByte))
				.andDo(print()).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
	}
}
