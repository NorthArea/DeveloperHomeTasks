package ru.northarea.exchanger.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.northarea.exchanger.exception.AppNotCorrectInitializedException;
import ru.northarea.exchanger.model.Currency;
import ru.northarea.exchanger.repository.DbConfigRepository;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CurrencyService {
    private final DbConfigRepository dbConfigRepository;

    public BigDecimal convert(BigDecimal amount, Currency from, Currency to) {
        return amount.multiply(getBaseLine(from.toString() + "_" + to.toString()));
    }

    private BigDecimal getBaseLine(String name) {
        final var optional = dbConfigRepository.findByName(name);
        if(optional.isEmpty()) {
            throw new AppNotCorrectInitializedException("BaseLine value is missing");
        }
        return optional.get().getData();
    }
}
