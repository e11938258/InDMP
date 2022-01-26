package at.tuwien.indmp;

import java.util.TimeZone;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class Application {

    @Value("${application.timezone}")
    private String timeZone;

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        log.info("The application has started successfully!");
    }

    @PostConstruct
    void started() {
        // Set JVM timezone as UTC
        TimeZone.setDefault(TimeZone.getTimeZone(timeZone));
    }
}
