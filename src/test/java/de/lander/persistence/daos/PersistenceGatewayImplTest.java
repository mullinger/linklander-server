/**
 *
 */
package de.lander.persistence.daos;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.neo4j.test.TestGraphDatabaseFactory;

import de.lander.persistence.daos.PersistenceGateway.LinkProperty;
import de.lander.persistence.entities.Link;

/**
 * Tests for {@link PersistenceGateway} with Neo4J Properties
 *
 * @author mvogel
 *
 */
public class PersistenceGatewayImplTest {

    private PersistenceGatewayImpl classUnderTest;

    @Before
    public void setUp() {
		classUnderTest = new PersistenceGatewayImpl(new TestGraphDatabaseFactory().newImpermanentDatabase());
    }

    /**
     * @author mvogel
     */
    @Test
    public void shouldPersistAndReadLinkByName() throws Exception {
        // == prepare ==
        String name = "Neo4j-Tutorial";
        String url = "http://neo4j.com/docs/stable/tutorials-java-embedded-hello-world.html";
        String title = "Neo4j-Tutorial-Title";

        // == go ==
        classUnderTest.addLink(name, url, title);
        List<Link> resultLinks = classUnderTest.getLink(LinkProperty.NAME, name);

        // == verify ==
        assertThat(resultLinks.size(), is(1));
        Link resultLink = resultLinks.get(0);
        assertThat(resultLink.getUrl(), is(url));
        assertThat(resultLink.getTitle(), is(title));
        assertThat(resultLink.getClicks(), is(0));
        assertThat(resultLink.getScore(), is(0.0));
    }

    /**
     * @author mvogel
     */
    @Test
    public void shouldPersistAndReadLinkByUrl() throws Exception {
        // == prepare ==
        String name = "Neo4j-Tutorial";
        String url = "http://neo4j.com/docs/stable/tutorials-java-embedded-hello-world.html";
        String title = "Neo4j-Tutorial-Title";

        // == go ==
        classUnderTest.addLink(name, url, title);
        List<Link> resultLinks = classUnderTest.getLink(LinkProperty.URL, url);

        // == verify ==
        assertThat(resultLinks.size(), is(1));
        Link resultLink = resultLinks.get(0);
        assertThat(resultLink.getUrl(), is(url));
        assertThat(resultLink.getTitle(), is(title));
        assertThat(resultLink.getClicks(), is(0));
        assertThat(resultLink.getScore(), is(0.0));
    }

    // /**
    // * @author mvogel
    // */
    // @Test
    // public void shouldPersistAndReadTagToImpermanentEmbeddedDb() throws Exception {
    // // == prepare ==
    // String tag = "myTag";
    //
    // // == go ==
    // classUnderTest.addTag(tag);
    // String readTag = classUnderTest.findTag(tag);
    //
    // // == verify ==
    // assertEquals(tag, readTag);
    // }
    //
    //
    // /**
    // * @author mvogel
    // */
    // @Test
    // @Ignore("fixme")
    // public void shouldFindLinkToByTag() throws Exception {
    // // == prepare ==
    // String name = "myLink";
    // String url = "http://neo4j.com/docs/stable/tutorials-java-embedded-hello-world.html";
    // String tag = "neo4jTut";
    //
    // // == go ==
    // classUnderTest.addLink(name, url);
    // classUnderTest.addTag(tag);
    // classUnderTest.tagLink(tag, url);
    // List<String> foundLinks = classUnderTest.findLinksByTag(url);
    //
    // // == verify ==
    // assertNotNull(foundLinks);
    // assertEquals(1, foundLinks.size());
    // assertEquals(url, foundLinks.get(0));
    // }

    // TODO tests for relations
}
