package fi.helsinki.cs.tmc.snapshot.api.app;

import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.config.CacheConfiguration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableCaching
@EnableWebMvc
public class AppConfiguration extends WebMvcConfigurerAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(AppConfiguration.class);

    @Value("${spyware.cacheSize}")
    private long cacheMemory;

    private Cache buildSpywareCache() {

        final CacheConfiguration configuration = new CacheConfiguration();
        configuration.setName("spyware");

        configuration.setMaxBytesLocalHeap(cacheMemory);

        LOG.info("Configured Spyware data cache with max size of {} bytes.", cacheMemory);

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

        configurer.defaultContentType(MediaType.APPLICATION_JSON);
    }

    @Override
    public void configureMessageConverters(final List<HttpMessageConverter<?>> messageConverters) {

        // Jackson
        final MappingJackson2HttpMessageConverter jacksonConverter = new MappingJackson2HttpMessageConverter();

        // Configure ObjectMapper
        //jacksonConverter.getObjectMapper().configure(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS, false);

        messageConverters.add(jacksonConverter);
        messageConverters.add(new ByteArrayHttpMessageConverter());
        messageConverters.add(new StringHttpMessageConverter());
        messageConverters.add(new PlainErrorMessageConverter());
    }
}
