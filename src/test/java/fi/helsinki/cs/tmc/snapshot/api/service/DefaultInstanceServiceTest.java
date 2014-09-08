package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.app.App;
import fi.helsinki.cs.tmc.snapshot.api.exception.NotFoundException;
import fi.helsinki.cs.tmc.snapshot.api.model.Instance;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = App.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class DefaultInstanceServiceTest {

    @Autowired
    private InstanceService instanceService;

    @Test
    public void shouldFindAllInstances() {

        final List<Instance> instances = new ArrayList(instanceService.findAll());

        assertEquals(2, instances.size());

        assertEquals("hy", instances.get(0).getId());
        assertEquals("mooc", instances.get(1).getId());
    }

    @Test
    public void shouldFindInstance() {

        assertNotNull(instanceService.find("hy"));
        assertNotNull(instanceService.find("mooc"));
    }

    @Test(expected = NotFoundException.class)
    public void shouldReturnNullOnNonExistentInstance() {

        instanceService.find("404");
    }
}
