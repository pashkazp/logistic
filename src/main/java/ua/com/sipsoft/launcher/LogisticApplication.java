package ua.com.sipsoft.launcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import com.vaadin.flow.spring.annotation.EnableVaadin;

import lombok.extern.slf4j.Slf4j;

/**
 * The Class LogisticApplication.
 */
@SpringBootApplication
@Configuration
@EnableAutoConfiguration
@EnableWebSecurity
@EnableJpaRepositories({ "ua.com.sipsoft" })
@EntityScan({ "ua.com.sipsoft.model.entity" })
@EnableVaadin({ "ua.com.sipsoft" })
@ComponentScan({ "ua.com.sipsoft" })
@Slf4j
public class LogisticApplication extends SpringBootServletInitializer {

    /**
     * Instantiates a new logistic application.
     */
    public LogisticApplication() {
	super();
	// for disable Error Page Filter in log file
	log.info("Switch off Error Page Filter");
	setRegisterErrorPageFilter(false);
    }

    /**
     * Configure to Create a deployable War File
     *
     * @param application the application
     * @return the spring application builder
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
	return application.sources(LogisticApplication.class);
    }

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
	log.info("Start logistic application");
	SpringApplication.run(LogisticApplication.class, args);
    }

}
