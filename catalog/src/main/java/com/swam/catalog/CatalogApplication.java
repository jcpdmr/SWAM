package com.swam.catalog;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@SpringBootApplication
public class CatalogApplication {

	public static void main(String[] args) {
        SpringApplication.run(CatalogApplication.class, args);
    }

        @Autowired
    private MessageSender messageSender;

    // curl -X POST -H "Content-Type: text/plain" -d "test123" http://localhost:8080/send
    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestBody String message) {
        messageSender.sendMessage(message);
        return ResponseEntity.ok("Message sent: " + message);
    }

    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
      return String.format("Hello %s!", name);
    }
}


