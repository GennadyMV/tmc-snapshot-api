package fi.helsinki.cs.tmc.snapshot.api.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

@Service
public class CacheHelper {

    private static final Logger LOG = LoggerFactory.getLogger(CacheHelper.class);

    @CachePut(value = "TmcUsername", key = "#p0.concat('-').concat(#p1)")
    public String cacheUsername(final String instance, final Long id, final String username) {

        LOG.info("Caching username {} for id {} of instance {}...", username, id, instance);

        return username;
    }
}
