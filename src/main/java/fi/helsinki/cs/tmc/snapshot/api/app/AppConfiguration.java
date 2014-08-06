package fi.helsinki.cs.tmc.snapshot.api.app;

import net.sf.ehcache.Cache;
import net.sf.ehcache.config.CacheConfiguration;

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

    private Cache buildSpywareCache() {

        final CacheConfiguration configuration = new CacheConfiguration();
        configuration.setName("spyware");

        // 50% of max heap size
        final Long maxMemory = Runtime.getRuntime().maxMemory();
        final Long cacheMemory = (long) (maxMemory * 0.5);
        configuration.setMaxBytesLocalHeap(cacheMemory);

        return new Cache(configuration);
    }

    @Bean
    public CacheManager cacheManager() {

        // Ehcache Manager
        final net.sf.ehcache.CacheManager cacheManager = net.sf.ehcache.CacheManager.newInstance();

        // Caches
        cacheManager.addCache(buildSpywareCache());

        return new EhCacheCacheManager(cacheManager);
    }

    @Override
    public void configureContentNegotiation(final ContentNegotiationConfigurer configurer) {

        configurer.favorPathExtension(false)
                  .defaultContentType(MediaType.APPLICATION_JSON);
    }
}
