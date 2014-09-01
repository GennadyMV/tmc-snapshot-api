package fi.helsinki.cs.tmc.snapshot.api.util;

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
}
