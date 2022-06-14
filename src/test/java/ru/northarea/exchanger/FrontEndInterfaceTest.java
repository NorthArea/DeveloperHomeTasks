package ru.northarea.exchanger;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.northarea.exchanger.model.Currency;
import ru.northarea.exchanger.model.ResponseDto;
import ru.northarea.exchanger.repository.DbConfigRepository;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class FrontEndInterfaceTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DbConfigRepository dbConfigRepository;

    @Test
    void baseLineIsEmpty() throws Exception {
        final var dto = new ResponseDto().amount(BigDecimal.valueOf(100.00)).currency(Currency.GBP);
        final var requestAsByte = objectMapper.writeValueAsBytes(dto);

        mockMvc.perform(post(AppConstants.EXCHANGE_URL + AppConstants.BUY_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestAsByte))
                .andDo(print()).andExpect(status().is5xxServerError());
    }

    @Test
    void validation() throws Exception {
        dbConfigRepository.deleteAll();
        final var dto = new ResponseDto().amount(BigDecimal.valueOf(100.00));
        final var requestAsByte = objectMapper.writeValueAsBytes(dto);

        mockMvc.perform(post(AppConstants.EXCHANGE_URL + AppConstants.BUY_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestAsByte))
                .andDo(print()).andExpect(status().isBadRequest());
    }
}
