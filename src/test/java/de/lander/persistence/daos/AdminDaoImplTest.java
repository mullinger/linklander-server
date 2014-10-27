/**
 *
 */
package de.lander.persistence.daos;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Tests for {@link AdminDao} with Neo4J Properties
 *
 * @author mvogel
 *
 */
public class AdminDaoImplTest {

    private AdminDaoImpl classUnderTest;

    @Before
    public void setUp() {
        classUnderTest = new AdminDaoImpl();
    }

    /**
     * @author mvogel
     */
    @Test
    @Ignore("mvogel: runs red when started more than once because the data is persisted on hard disc already. Think about cleaning nodes after having run this test...")
    public void shouldPersistAndReadLinkToEmbeddedDb() throws Exception {
        // == prepare ==
        String name = "myLink";
        String url = "http://neo4j.com/docs/stable/tutorials-java-embedded-hello-world.html";

        // == go ==
        classUnderTest.addLink(name, url);
        String readLink = classUnderTest.readLink(url);

        // == verify ==
        assertEquals(url, readLink);
    }

    // TODO tests for relations
}
