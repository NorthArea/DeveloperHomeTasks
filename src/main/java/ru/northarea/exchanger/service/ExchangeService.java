package ru.northarea.exchanger.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.northarea.exchanger.config.AppProperties;
import ru.northarea.exchanger.exception.AppNotCorrectInitializedException;
import ru.northarea.exchanger.repository.DbConfigRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class ExchangeService {
    private static final long BASE_LINE_ID = 1L;

    private final DbConfigRepository dbConfigRepository;

    private final AppProperties appProperties;

    public BigDecimal convertEurToGbp(BigDecimal eur) {
        return eur.divide(getBaseLine(), RoundingMode.FLOOR);
    }

    public BigDecimal convertGbpToEur(BigDecimal gbp) {
        return gbp.multiply(getBaseLine());
    }

    private BigDecimal getBaseLine() {
        final var optional = dbConfigRepository.findById(BASE_LINE_ID);
        if(optional.isEmpty()) {
            throw new AppNotCorrectInitializedException("BaseLine value is missing");
        }
        return optional.get().getData();
    }

    public BigDecimal addMargin(BigDecimal value) {
        return value.add(value.multiply(appProperties.getMargin()));
    }

    public BigDecimal subtractMargin(BigDecimal value) {
        return value.subtract(value.multiply(appProperties.getMargin()));
    }
}
