package fi.helsinki.cs.tmc.snapshot.api.app;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
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

        // Ehcache
        final net.sf.ehcache.CacheManager cacheManager = net.sf.ehcache.CacheManager.newInstance();

        cacheManager.addCache("TmcUsername");
        cacheManager.addCache("RawSpywareData");
        cacheManager.addCache("Snapshots");

        return new EhCacheCacheManager(cacheManager);
    }
}
