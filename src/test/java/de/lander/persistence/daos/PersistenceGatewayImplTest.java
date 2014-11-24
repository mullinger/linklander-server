/**
 *
 */
package de.lander.persistence.daos;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
		classUnderTest = new PersistenceGatewayImpl(
				new TestGraphDatabaseFactory().newImpermanentDatabase());
	}

	/**
	 * @author mvogel
	 */
	@Test
	public void shouldPersistAndReadLinkByFullName() throws Exception {
		// == prepare ==
		String name = "Neo4j-Tutorial";
		String url = "http://neo4j.com/docs/stable/tutorials-java-embedded-hello-world.html";
		String title = "Neo4j-Tutorial-Title";

		// == go ==
		classUnderTest.addLink(name, url, title);
		List<Link> resultLinks = classUnderTest
				.getLink(LinkProperty.NAME, name);

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
	public void shouldPersistAndReadLinkBySubstringName() throws Exception {
		// == prepare ==
		String name = "Neo4j-Tuto";
		String url = "http://neo4j.com/docs/stable/tutorials-java-embedded-hello-world.html";
		String title = "Neo4j-Tutorial-Title";

		// == go ==
		classUnderTest.addLink(name, url, title);
		List<Link> resultLinks = classUnderTest
				.getLink(LinkProperty.NAME, name);

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
	public void shouldPersistAndReadLinkByFullUrl() throws Exception {
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

	/**
	 * @author mvogel
	 */
	@Test
	public void shouldPersistAndReadLinkBySubstringUrl() throws Exception {
		// == prepare ==
		String name = "Neo4j-Tutorial1";
		String url = "http://neo4j.com/docs/stable/tutorials-java-embedded-hello-world.html";
		String title = "Neo4j-Tutorial-Embedded";

		classUnderTest.addLink(name, url, title);
		classUnderTest.addLink("Neo4j-Tutorial2",
				"http://neo4j.com/docs/stable/complete.html", "Neo4j");
		classUnderTest.addLink("Neo4j-Tutorial3",
				"http://neo4j.com/docs/stable/half.html", "Neo4j");
		classUnderTest.addLink("Linux", "http://linux.com", "Linux-Stuff");
		classUnderTest.addLink("Apple-Repair", "http://applerepair.com",
				"Apple-Repair");
		classUnderTest.addLink("Apple-Talk", "http://appletalk.com",
				"Apple-Talk");

		// == go ==
		List<Link> resultLinks = classUnderTest.getLink(LinkProperty.URL,
				"neo4j.co");

		// == verify ==
		assertThat(resultLinks.size(), is(3));
		resultLinks.forEach(System.out::println);
	}

	/**
	 * @author mvogel
	 */
	@Test
	public void shouldUpdateTheNameOfTheLink() throws Exception {
		// == prepare ==
		String oldName = "Neo4j-Tutorial";
		String url = "http://neo4j.com/docs/stable/tutorials-java-embedded-hello-world.html";
		String title = "Neo4j-Tutorial-Title";
		classUnderTest.addLink(oldName, url, title);

		String newName = "Neo4j-New-Tutorial-111";

		// == go ==
		classUnderTest.updateLink(LinkProperty.NAME, oldName, newName);

		// == verify ==
		List<Link> noResultLinks = classUnderTest.getLink(LinkProperty.NAME,
				oldName);
		assertThat(noResultLinks.size(), is(0));
		List<Link> resultLinks = classUnderTest.getLink(LinkProperty.NAME,
				newName);
		assertThat(resultLinks.size(), is(1));
		Link resultLink = resultLinks.get(0);
		assertThat(resultLink.getName(), is(newName));
		assertThat(resultLink.getUrl(), is(url));
		assertThat(resultLink.getTitle(), is(title));
		assertThat(resultLink.getClicks(), is(0));
		assertThat(resultLink.getScore(), is(0.0));
	}

	/**
	 * @author mvogel
	 */
	@Test(expected = IllegalArgumentException.class)
	public void shouldFailToUpdateTheNameOfTheLinkBecauseItDoesNotExist()
			throws Exception {
		// == prepare ==
		String oldName = "Neo4j-Tutorial";
		String url = "http://neo4j.com/docs/stable/tutorials-java-embedded-hello-world.html";
		String title = "Neo4j-Tutorial-Title";
		classUnderTest.addLink(oldName, url, title);

		String newName = "Neo4j-New-Tutorial-111";

		// == go ==
		try {
			classUnderTest.updateLink(LinkProperty.NAME, "i do not exist",
					newName);
			fail();
		} catch (Exception e) {
			// == verify ==
			assertTrue(e.getMessage().contains("no node was found for"));
			throw e;
		}
	}
}
