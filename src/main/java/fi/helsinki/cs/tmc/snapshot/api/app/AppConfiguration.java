package fi.helsinki.cs.tmc.snapshot.api.app;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableCaching
@EnableWebMvc
public class AppConfiguration extends WebMvcConfigurerAdapter {

    @Override
    public void configureContentNegotiation(final ContentNegotiationConfigurer configurer) {

        configurer.favorPathExtension(false)
                  .defaultContentType(MediaType.APPLICATION_JSON);
    }

    @Bean
    public CacheManager cacheManager() {

        // configure and return an implementation of Spring's CacheManager SPI
        final SimpleCacheManager cacheManager = new SimpleCacheManager();
        final List<Cache> caches = new ArrayList<>();
        caches.add(new ConcurrentMapCache("EventList"));
        caches.add(new ConcurrentMapCache("RawSpywareData"));
        caches.add(new ConcurrentMapCache("tmcUsername"));
        cacheManager.setCaches(caches);
        return cacheManager;
    }
}
