package ru.northarea.exchanger.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.northarea.exchanger.config.AppProperties;
import ru.northarea.exchanger.model.RequestDto;
import ru.northarea.exchanger.model.ResponseDto;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class ExchangeService {
    private final static int SCALE = 2;
    private final static RoundingMode roundingMode = RoundingMode.DOWN;

    private final AppProperties appProperties;
    private final CurrencyService currencyService;

    public ResponseDto sellConvert(RequestDto requestDto) {
        final var converted = currencyService.convert(requestDto.getAmount(), requestDto.getFrom(), requestDto.getTo());
        final var amount = subtractMargin(converted);
        final var scaled = amount.setScale(SCALE, roundingMode);
        return new ResponseDto().amount(scaled).currency(requestDto.getTo());
    }

    public ResponseDto buyConvert(RequestDto requestDto) {
        final var converted = currencyService.convert(requestDto.getAmount(), requestDto.getFrom(), requestDto.getTo());
        final var amount = addMargin(converted);
        final var scaled = amount.setScale(SCALE, roundingMode);
        return new ResponseDto().amount(scaled).currency(requestDto.getTo());
    }

    private BigDecimal addMargin(BigDecimal value) {
        return value.add(value.multiply(appProperties.getMargin()));
    }

    private BigDecimal subtractMargin(BigDecimal value) {
        return value.subtract(value.multiply(appProperties.getMargin()));
    }
}
