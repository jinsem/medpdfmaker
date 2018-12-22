package com.jsoft.medpdfmaker;

import com.jsoft.medpdfmaker.parser.impl.*;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@ComponentScan(basePackages = {"com.jsoft.*"})
@PropertySource("classpath:med-pdf-maker.properties")
@PropertySource(value = "${app.properties.ext}", ignoreResourceNotFound = true)
public class AppConfiguration {

    private Environment environment;

    @Autowired
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public AppProperties appProperties() {
        return new AppProperties(environment);
    }

    @Bean
    public AppParametersParser appParametersParser() {
        return new AppParametersParser();
    }

    @Bean
    public BooleanValueExtractor booleanValueExtractor() {
        return new BooleanValueExtractor();
    }

    @Bean
    public LocalDateValueExtractor localDateValueExtractor() {
        return new LocalDateValueExtractor();
    }

    @Bean
    public LocalTimeValueExtractor localTimeValueExtractor() {
        return new LocalTimeValueExtractor();
    }

    @Bean
    public IntegerValueExtractor integerValueExtractor() {
        return new IntegerValueExtractor(new DataFormatter());
    }

    @Bean
    public DecimalMoneyValueExtractor decimalMoneyValueExtractor() {
        return new DecimalMoneyValueExtractor(new DataFormatter());
    }

    @Bean
    public StringValueExtractor stringValueExtractor() {
        return new StringValueExtractor(new DataFormatter());
    }
}
