package com.tasalparslan.techconfscheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.tasalparslan.techconfscheduler.model.Talk;
import com.tasalparslan.techconfscheduler.repository.TalkRepository;

@SpringBootApplication
public class TechConfSchedulerApplication {

	private static final Logger log = LoggerFactory.getLogger(TechConfSchedulerApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(TechConfSchedulerApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(TalkRepository repository) {
		return (args) -> {
			// save a couple of customers
			repository.save(new Talk("Architecting Your Codebase", 60));
			repository.save(new Talk("Overdoing it in Python", 45));
			repository.save(new Talk("Flavors of Concurrency in Java", 30));
			repository.save(new Talk("Ruby Errors from Mismatched Gem Versions", 45));
			repository.save(new Talk("JUnit 5 - Shaping the Future of Testing on the JVM", 45));
			repository.save(new Talk("Cloud Native Java lightning", 5));
			repository.save(new Talk("Communicating Over Distance", 60));
			repository.save(new Talk("AWS Technical Essentials", 45));
			repository.save(new Talk("Continuous Delivery", 30));
			repository.save(new Talk("Monitoring Reactive Applications", 30));
			repository.save(new Talk("Pair Programming vs Noise", 45));
			repository.save(new Talk("Rails Magic", 60));
			repository.save(new Talk("Microservices \"Just Right\"", 60));
			repository.save(new Talk("Clojure Ate Scala (on my project)", 45));
			repository.save(new Talk("Perfect Scalability", 30));
			repository.save(new Talk("Apache Spark", 30));
			repository.save(new Talk("Async Testing on JVM", 60));
			repository.save(new Talk("A World Without HackerNews", 30));
			repository.save(new Talk("User Interface CSS in Apps", 30));

//			repository.save(new Talk("aaUser Interface CSS in Apps", 60));
//			repository.save(new Talk("bbUser Interface CSS in Apps", 60));
//			repository.save(new Talk("bbUser Interface CSS in Apps", 60));
//			
//			repository.save(new Talk("ccUser Interface CSS in Apps", 60));
//			repository.save(new Talk("ddUser Interface CSS in Apps", 60));
			// repository.save(new Talk("eeUser Interface CSS in Apps", 60));
			// repository.save(new Talk("ffUser Interface CSS in Apps", 60));
			
			// fetch all talks
			log.info("Talks found with findAll():");
			log.info("-------------------------------");
			for (Talk talk : repository.findAll()) {
				log.info(talk.toString());
			}
			log.info("");

		};
	}

}
