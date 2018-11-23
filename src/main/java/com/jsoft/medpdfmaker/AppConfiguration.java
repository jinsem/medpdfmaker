package com.jsoft.medpdfmaker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@ComponentScan(basePackages = {"com.jsoft.*"})
@PropertySource("${properties.dir:classpath}:med-pdf-maker.properties")
public class AppConfiguration {

}
