package fi.helsinki.cs.tmc.snapshot.api.util;

import java.lang.reflect.Constructor;

import org.junit.Test;

import static org.junit.Assert.fail;

public class RequestHandlerTest {

    /*
     * Make sure constructor is private
     */
    @Test(expected = IllegalAccessException.class)
    public void shouldHavePrivateConstructor() throws InstantiationException, IllegalAccessException {

        RequestHandler.class.newInstance();
        fail("Should have private constructor");
    }

    /*
     * Make sure cobertura knows we visited the private constructor...
     */
    @Test
    public void coverageForPrivateConstructor() throws Exception {

        final Constructor<?>[] constructor = RequestHandler.class.getDeclaredConstructors();
        constructor[0].setAccessible(true);
        constructor[0].newInstance((Object[]) null);
    }
}
