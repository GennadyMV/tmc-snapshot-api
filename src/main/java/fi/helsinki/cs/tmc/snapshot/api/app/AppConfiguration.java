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

    @Override
    public void configureContentNegotiation(final ContentNegotiationConfigurer configurer) {

        configurer.favorPathExtension(false)
                  .defaultContentType(MediaType.APPLICATION_JSON);
    }

    @Bean
    public CacheManager cacheManager() {

        // Caches
        final Cache tmcUsername = new Cache(new CacheConfiguration("TmcUsername", 5000));

        final CacheConfiguration spywareConfig = new CacheConfiguration();
        spywareConfig.setName("RawSpywareData");

        // 50% of max heap size
        final Long maxMemory = Runtime.getRuntime().maxMemory();
        final Long cacheMemory = (long) (maxMemory.doubleValue() * 0.5);
        spywareConfig.setMaxBytesLocalHeap(cacheMemory);

        final Cache rawSpywareData = new Cache(spywareConfig);

        // Ehcache Manager
        final net.sf.ehcache.CacheManager cacheManager = net.sf.ehcache.CacheManager.newInstance();

        cacheManager.addCache(tmcUsername);
        cacheManager.addCache(rawSpywareData);

        return new EhCacheCacheManager(cacheManager);
    }
}
