/**
 *
 */
package de.lander.persistence.daos;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.neo4j.test.TestGraphDatabaseFactory;

import de.lander.persistence.daos.PersistenceGateway.LinkProperty;
import de.lander.persistence.daos.PersistenceGateway.TagProperty;
import de.lander.persistence.entities.Link;
import de.lander.persistence.entities.Tag;

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
				.getLinks(LinkProperty.NAME, name);

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
				.getLinks(LinkProperty.NAME, name);

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
		List<Link> resultLinks = classUnderTest.getLinks(LinkProperty.URL, url);

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
		List<Link> resultLinks = classUnderTest.getLinks(LinkProperty.URL,
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
		assertThat(classUnderTest.getLinks(LinkProperty.NAME,
				oldName).size(), is(1));

		String newName = "Neo4j-New-Tutorial-111";

		// == go ==
		classUnderTest.updateLink(LinkProperty.NAME, oldName, newName);

		// == verify ==
		assertThat(classUnderTest.getLinks(LinkProperty.NAME,
				oldName).size(), is(0));
		List<Link> resultLinks = classUnderTest.getLinks(LinkProperty.NAME,
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

	/**
	 * @author mvogel
	 */
	@Test
	public void shouldDeleteMultipleLinksByName() throws Exception {
		// == prepare ==
		classUnderTest.addLink("Neo4j-Tutorial-Web", "http://neo4j.com/docs/stable/tutorials-java-embedded-hello-world.html", "Neo4j-Tutorial-Title");
		classUnderTest.addLink("Neo4j-Tutorial", "http://neo4j.com/docs/stable/tutorials-web.html", "Neo4j-Tutorial-Web-Title");
		classUnderTest.addLink("Linux-Magazin", "http://linux-magazin.com", "my linux magazin");
		assertEquals(2, classUnderTest.getLinks(LinkProperty.NAME, "Tutorial").size());
		
		// == go ==
		classUnderTest.deleteLink(LinkProperty.NAME, "Tutorial");

		// == verify ==
		assertEquals(0, classUnderTest.getLinks(LinkProperty.NAME, "Tutorial").size());
		assertEquals(1, classUnderTest.getLinks(LinkProperty.NAME, "Magazin").size());
	}
	
	/**
	 * @author mvogel
	 */
	@Test
	public void shouldDeleteMultipleLinksByUrl() throws Exception {
		// == prepare ==
		classUnderTest.addLink("Neo4j-Tutorial-Web", "http://neo4j.com/docs/stable/tutorials-java-embedded-hello-world.html", "Neo4j-Tutorial-Title");
		classUnderTest.addLink("Neo4j-Tutorial", "http://neo4j.com/docs/stable/tutorials-web.html", "Neo4j-Tutorial-Web-Title");
		classUnderTest.addLink("Linux-Magazin", "http://linux-magazin.com", "my linux magazin");
		assertEquals(2, classUnderTest.getLinks(LinkProperty.NAME, "Tutorial").size());
		
		// == go ==
		classUnderTest.deleteLink(LinkProperty.URL, "neo4j");

		// == verify ==
		assertEquals(0, classUnderTest.getLinks(LinkProperty.NAME, "Tutorial").size());
		assertEquals(1, classUnderTest.getLinks(LinkProperty.NAME, "Magazin").size());
	}
	
	/**
	 * @author mvogel
	 */
	@Test
	public void shouldPersistAndReadTagBySubstringName() throws Exception {
		// == prepare ==
		String name = "Production";
		String description = "Production-Stage-Tag";

		// == go ==
		classUnderTest.addTag(name, description);
		List<Tag> resultTags = classUnderTest
				.getTags(TagProperty.NAME, "Prod");

		// == verify ==
		assertThat(resultTags.size(), is(1));
		Tag resultTag = resultTags.get(0);
		assertThat(resultTag.getDescription(), is(description));
		assertThat(resultTag.getClicks(), is(0));
	}
	
	/**
	 * @author mvogel
	 */
	@Test
	public void shouldUpdateTheNameOfTheTag() throws Exception {
		// == prepare ==
		String oldName = "Production";
		String description = "Production-Stage-Description";
		classUnderTest.addTag(oldName, description);
		assertThat(classUnderTest.getTags(TagProperty.NAME,
				oldName).size(), is(1));

		String newName = "Development";

		// == go ==
		classUnderTest.updateTag(TagProperty.NAME, oldName, newName);

		// == verify ==
		assertThat(classUnderTest.getTags(TagProperty.NAME,
				oldName).size(), is(0));
		List<Tag> resultTags = classUnderTest.getTags(TagProperty.NAME,
				newName);
		assertThat(resultTags.size(), is(1));
		Tag resultTag = resultTags.get(0);
		assertThat(resultTag.getName(), is(newName));
		assertThat(resultTag.getDescription(), is(description));
		assertThat(resultTag.getClicks(), is(0));
	}
	
	/**
	 * @author mvogel
	 */
	@Test
	public void shouldDeleteMultipleTagsByName() throws Exception {
		// == prepare ==
		classUnderTest.addTag("Tag1", "description1");
		classUnderTest.addTag("Tag2", "description2");
		classUnderTest.addTag("Myblabla", "description3");
		assertEquals(2, classUnderTest.getTags(TagProperty.NAME, "Tag").size());
		
		// == go ==
		classUnderTest.deleteTag(TagProperty.NAME, "Tag");
		
		// == verify ==
		assertEquals(0, classUnderTest.getTags(TagProperty.NAME, "Tag").size());
		assertEquals(1, classUnderTest.getTags(TagProperty.NAME, "My").size());
	}
}
