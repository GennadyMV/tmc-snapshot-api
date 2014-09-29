package fi.helsinki.cs.tmc.snapshot.api.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan(basePackages = { "fi.helsinki.cs.tmc.snapshot.api" })
@EnableJpaRepositories("fi.helsinki.cs.tmc.snapshot.api.repository")
@EntityScan("fi.helsinki.cs.tmc.snapshot.api.model")
@EnableAutoConfiguration
public final class App {

    public static void main(final String[] args) throws Exception {

        SpringApplication.run(App.class, args);
    }
}
