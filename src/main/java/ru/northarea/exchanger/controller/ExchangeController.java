package ru.northarea.exchanger.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.northarea.exchanger.model.RequestDto;
import ru.northarea.exchanger.model.ResponseDto;
import ru.northarea.exchanger.service.ExchangeService;

@RestController
@RequiredArgsConstructor
public class ExchangeController implements ExchangeApi {
    private final ExchangeService exchangeService;

    @Override
    public ResponseEntity<ResponseDto> sell(RequestDto requestDto) {
        return ResponseEntity.ok(exchangeService.convertAndSell(requestDto));
    }

    @Override
    public ResponseEntity<ResponseDto> buy(RequestDto requestDto) {
        return ResponseEntity.ok(exchangeService.convertAndBuy(requestDto));
    }
}
