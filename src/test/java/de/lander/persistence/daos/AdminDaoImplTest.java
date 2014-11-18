/**
 *
 */
package de.lander.persistence.daos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.neo4j.test.TestGraphDatabaseFactory;

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
		classUnderTest = new AdminDaoImpl(new TestGraphDatabaseFactory().newImpermanentDatabase());
    }

    /**
     * @author mvogel
     */
    @Test
    public void shouldPersistAndReadLinkToEmbeddedDb() throws Exception {
        // == prepare ==
        String name = "Neo4j-Tutorial";
        String url = "http://neo4j.com/docs/stable/tutorials-java-embedded-hello-world.html";

        // == go ==
        classUnderTest.addLink(name, url);
        String readUrlFromDb = classUnderTest.readLink(name);

        // == verify ==
        assertEquals(url, readUrlFromDb);
    }

    /**
     * @author mvogel
     */
    @Test
    public void shouldPersistAndReadTagToImpermanentEmbeddedDb() throws Exception {
        // == prepare ==
        String tag = "myTag";

        // == go ==
        classUnderTest.addTag(tag);
        String readTag = classUnderTest.findTag(tag);

        // == verify ==
        assertEquals(tag, readTag);
    }


    /**
     * @author mvogel
     */
    @Test
    @Ignore("fixme")
    public void shouldFindLinkToByTag() throws Exception {
        // == prepare ==
        String name = "myLink";
        String url = "http://neo4j.com/docs/stable/tutorials-java-embedded-hello-world.html";
        String tag = "neo4jTut";

        // == go ==
        classUnderTest.addLink(name, url);
        classUnderTest.addTag(tag);
        classUnderTest.tagLink(tag, url);
        List<String> foundLinks = classUnderTest.findLinksByTag(url);

        // == verify ==
        assertNotNull(foundLinks);
        assertEquals(1, foundLinks.size());
        assertEquals(url, foundLinks.get(0));
    }

    // TODO tests for relations
}
