package com.swam.catalog;

import java.util.Date;
import java.util.function.Supplier;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NameSourceConfiguration {

	// tag::supplyname[]
	@Bean
	public Supplier<String> supplyName() {
		return () -> "Test msg: " + new Date().toString();
	}
	// end::supplyname[]
}