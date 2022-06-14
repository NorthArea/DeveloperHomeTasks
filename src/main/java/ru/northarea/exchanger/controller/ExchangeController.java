package ru.northarea.exchanger.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.northarea.exchanger.model.Currency;
import ru.northarea.exchanger.model.RequestDto;
import ru.northarea.exchanger.model.ResponseDto;
import ru.northarea.exchanger.service.ExchangeService;

import java.math.RoundingMode;

@RestController
@RequiredArgsConstructor
public class ExchangeController implements ExchangeApi {
    private final static int SCALE = 2;
    private final static RoundingMode roundingMode = RoundingMode.DOWN;

    private final ExchangeService exchangeService;

    @Override
    public ResponseEntity<ResponseDto> sell(RequestDto requestDto) {
        final var converted = exchangeService.convertEurToGbp(requestDto.getAmount());
        final var amount = exchangeService.subtractMargin(converted);
        final var scaled = amount.setScale(SCALE, roundingMode);
        return ResponseEntity.ok(new ResponseDto().amount(scaled).currency(Currency.GBP));
    }

    @Override
    public ResponseEntity<ResponseDto> buy(RequestDto requestDto) {
        final var converted = exchangeService.convertGbpToEur(requestDto.getAmount());
        final var amount = exchangeService.addMargin(converted);
        final var scaled = amount.setScale(SCALE, roundingMode);
        return ResponseEntity.ok(new ResponseDto().amount(scaled).currency(Currency.EUR));
    }
}
