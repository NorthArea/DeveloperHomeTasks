package ru.northarea.exchanger.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Validated
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    @NotNull
    BigDecimal margin;
}
