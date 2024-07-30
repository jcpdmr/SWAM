package com.swam.catalog;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@SpringBootApplication
@Controller
public class CatalogApplication {

	public static void main(String[] args) {
        SpringApplication.run(CatalogApplication.class, args);
    }

    @Autowired
	private StreamBridge streamBridge;

	@GetMapping(value = "/send")
    @ResponseBody
	public String producer() {
		System.out.println("Sending ");
		streamBridge.send("test-exchange", "test msg" + new Date().toString());
        return "inviato su rabbit";
	}
}


