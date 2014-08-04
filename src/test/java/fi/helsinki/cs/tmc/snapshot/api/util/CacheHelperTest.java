package fi.helsinki.cs.tmc.snapshot.api.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CacheHelperTest {

    @Test
    public void cacheUsernameReturnsCorrectUsername() {

        final CacheHelper cacheHelper = new CacheHelper();

        assertEquals("username", cacheHelper.cacheUsername("hy", 1L, "username"));
        assertEquals("anotherUser", cacheHelper.cacheUsername("hy", 1L, "anotherUser"));
    }

}
