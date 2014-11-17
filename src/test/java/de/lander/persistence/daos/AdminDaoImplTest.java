/**
 *
 */
package de.lander.persistence.daos;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
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

    // TODO tests for relations
}
