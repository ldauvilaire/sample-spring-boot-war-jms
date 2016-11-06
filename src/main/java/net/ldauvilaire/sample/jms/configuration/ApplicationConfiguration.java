package net.ldauvilaire.sample.jms.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jms.annotation.EnableJms;

@Configuration
@EnableJms
@ComponentScan(basePackages = { "net.ldauvilaire.sample" })
@PropertySource("classpath:application.properties")
public class ApplicationConfiguration {
}
