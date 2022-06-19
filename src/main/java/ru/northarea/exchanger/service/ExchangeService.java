package ru.northarea.exchanger.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.northarea.exchanger.config.AppProperties;
import ru.northarea.exchanger.model.Currency;
import ru.northarea.exchanger.model.RequestDto;
import ru.northarea.exchanger.model.ResponseDto;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class ExchangeService {
    private static final int SCALE = 2;
    private static final RoundingMode roundingMode = RoundingMode.DOWN;
    private static final int SCALE_RATE = 5;

    private final AppProperties appProperties;
    private final CurrencyService currencyService;

    public ResponseDto convertAndSell(RequestDto requestDto) {
        final BigDecimal convertedAmount = currencyService.convert(requestDto.getAmount(), requestDto.getSell(), requestDto.getBuy());
        final var subtractedAmount = subtractMargin(convertedAmount);
        return fillResponseDto(subtractedAmount, requestDto.getAmount(), requestDto.getBuy());
    }

    public ResponseDto convertAndBuy(RequestDto requestDto) {
        final BigDecimal convertedAmount = currencyService.convert(requestDto.getAmount(), requestDto.getSell(), requestDto.getBuy());
        final var subtractedAmount = addMargin(convertedAmount);
        return fillResponseDto(subtractedAmount, requestDto.getAmount(), requestDto.getBuy());
    }

    private ResponseDto fillResponseDto(BigDecimal subtractedAmount, BigDecimal amount, Currency currency) {
        final var scaledAmount = subtractedAmount.setScale(SCALE, roundingMode);
        final var rate = amount.divide(scaledAmount, SCALE_RATE, roundingMode).stripTrailingZeros();
        return new ResponseDto().amount(scaledAmount).currency(currency).rate(rate);
    }

    private BigDecimal addMargin(BigDecimal value) {
        return value.add(value.multiply(appProperties.getMargin()));
    }

    private BigDecimal subtractMargin(BigDecimal value) {
        return value.subtract(value.multiply(appProperties.getMargin()));
    }
}
