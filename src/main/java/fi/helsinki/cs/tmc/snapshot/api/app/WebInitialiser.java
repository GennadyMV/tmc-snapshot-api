package fi.helsinki.cs.tmc.snapshot.api.app;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

public final class WebInitialiser extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(final SpringApplicationBuilder application) {

        return application.sources(App.class);
    }
}
