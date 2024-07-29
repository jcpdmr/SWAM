package com.swam.operation;

import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SinkConfig {

    @Bean
	public Consumer<String> nameSink() {
		return msg -> {
			System.out.println(msg);
		};
	}
}