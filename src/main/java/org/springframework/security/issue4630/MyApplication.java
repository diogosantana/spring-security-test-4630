package org.springframework.security.issue4630;

import java.util.Locale;
import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;

@SpringBootApplication
@Configuration
public class MyApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyApplicationDev.class, args);
	}
	
	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:org/springframework/security/messages");
		return messageSource;
	}

	@Bean
	public LocaleResolver localeResolver() {
	    return new FixedLocaleResolver(new Locale("pt", "BR"), TimeZone.getDefault());
	}
}
