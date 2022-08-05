package com.pubsub.exactlyonce.demo;

import com.google.cloud.pubsub.v1.Subscriber;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(DemoApplication.class);

	private static final String PROJECT_ID = "FILL_ME";
	private static final String SUBSCRIPTION_NAME = "FILL_ME";
	private static final String SUBSCRIPTION = String.format("projects/%s/subscriptions/%s", PROJECT_ID, SUBSCRIPTION_NAME);

	final PubSubTemplate pubSubTemplate;


	public DemoApplication(PubSubTemplate pubSubTemplate) {
		this.pubSubTemplate = pubSubTemplate;
	}

	@Override
	public void run(String... args) throws Exception {
		final Subscriber subscriber = pubSubTemplate.getPubSubSubscriberTemplate().
				subscribe((args.length == 0) ?  SUBSCRIPTION : args[0], m -> {
					final String s = m.getPubsubMessage().getData().toStringUtf8();
					logger.info("sub1 = " + Thread.currentThread().getName() + " = " + s + " " + m.getPubsubMessage().getMessageId());
					try {
						logger.info("Processing in progress - going to sleep 5 minutes...");
						Thread.sleep(1000 * 60 * 5); //wait 5 minutes while ACK deadline is set to 10 minutes
						logger.info("Sleeping over");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				});
		subscriber.awaitRunning();


		System.out.println("Press Ctrl/Cmd+C to exit");
		for (; ; ) {
			Thread.sleep(Long.MAX_VALUE);
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
