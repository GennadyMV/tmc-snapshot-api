package fi.helsinki.cs.tmc.snapshot.api.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
@ComponentScan(basePackages = { "fi.helsinki.cs.tmc.snapshot.api" })
public final class App {

    public static void main(final String[] args) throws Exception {

        SpringApplication.run(App.class, args);
    }
}
