package com.swam.catalog;

import java.util.List;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.swam.multimodule.commons.EchoTest;

@SpringBootApplication()
@ComponentScan(basePackages = { "com.swam.multimodule" })
public class CatalogApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(CatalogApplication.class, args);
	}

	@Autowired
	private PersonRepository personRepository;

	@Override
	public void run(String... args) throws Exception {
		Person pippo = new Person("pippo", 10);

		System.out.println("saving person");
		personRepository.save(pippo);

		List<Person> personList = personRepository.findByName("pippo");
		System.out.println("person saved: ");
		personList.forEach(person -> System.out.println(person));

		personRepository.deleteById(personList.get(0).getId());

		personList = personRepository.findByName("pippo");
		System.out.println("person found after deleting: ");
		personList.forEach(person -> System.out.println(person));

		EchoTest.testCommons();
		EchoTest.testCommons();

	}

	@RabbitListener(queues = "catalog_in")
	public void requestHandler(String msg) {
		System.out.println("Messaggio ricevuto dall'handler: " + msg);
	}
}
