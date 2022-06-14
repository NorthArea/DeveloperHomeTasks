package ru.northarea.exchanger.model;

import lombok.Getter;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;

@Getter
public class DbConfig {
    @Id
    private long id;
    private String name;
    private BigDecimal data;
}
