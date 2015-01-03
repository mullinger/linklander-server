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

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.test.TestGraphDatabaseFactory;

import de.lander.persistence.daos.PersistenceGateway.DeletionMode;
import de.lander.persistence.daos.PersistenceGateway.LinkProperty;
import de.lander.persistence.daos.PersistenceGateway.TagProperty;
import de.lander.persistence.entities.Link;
import de.lander.persistence.entities.Relationships;
import de.lander.persistence.entities.Tag;

/**
 * Tests for {@link PersistenceGateway} with Neo4J Properties
 *
 * @author mvogel
 *
 */
@RunWith(Arquillian.class)
public class PersistenceGatewayImplTest {

	// TODO mvogel: default constructor is need for this annotation
	// @Inject
	private PersistenceGatewayImpl classUnderTest;

	@Deployment
	public static JavaArchive createDeployment() {
		return ShrinkWrap.create(JavaArchive.class)
				.addClass(Relationships.class)
				.addClass(PersistenceGateway.class)
				.addClass(PersistenceGatewayImpl.class)
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
	}

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
		List<Link> resultLinks = classUnderTest.searchLinks(LinkProperty.NAME,
				name);

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
		List<Link> resultLinks = classUnderTest.searchLinks(LinkProperty.NAME,
				name);

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
		List<Link> resultLinks = classUnderTest.searchLinks(LinkProperty.URL,
				url);

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
		List<Link> resultLinks = classUnderTest.searchLinks(LinkProperty.URL,
				"neo4j.co");

		// == verify ==
		assertThat(resultLinks.size(), is(3));
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
		assertThat(classUnderTest.searchLinks(LinkProperty.NAME, oldName)
				.size(), is(1));

		String newName = "Neo4j-New-Tutorial-111";

		// == go ==
		classUnderTest.updateLink(LinkProperty.NAME, oldName, newName);

		// == verify ==
		assertThat(classUnderTest.searchLinks(LinkProperty.NAME, oldName)
				.size(), is(0));
		List<Link> resultLinks = classUnderTest.searchLinks(LinkProperty.NAME,
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
			assertTrue(e.getMessage().contains("no link node was found for"));
			throw e;
		}
	}

	/**
	 * @author mvogel
	 */
	@Test
	public void shouldOnlyOneLinkByExactName() throws Exception {
		// == prepare ==
		classUnderTest
				.addLink(
						"Neo4j-Tutorial-Web",
						"http://neo4j.com/docs/stable/tutorials-java-embedded-hello-world.html",
						"Neo4j-Tutorial-Title");
		classUnderTest.addLink("Neo4j-Tutorial",
				"http://neo4j.com/docs/stable/tutorials-web.html",
				"Neo4j-Tutorial-Web-Title");
		classUnderTest.addLink("Linux-Magazin", "http://linux-magazin.com",
				"my linux magazin");
		assertEquals(2,
				classUnderTest.searchLinks(LinkProperty.NAME, "Tutorial")
						.size());

		// == go ==
		classUnderTest.deleteLink(LinkProperty.NAME, "Neo4j-Tutorial",
				DeletionMode.EXACT);

		// == verify ==
		assertEquals(1,
				classUnderTest.searchLinks(LinkProperty.NAME, "Tutorial")
						.size());
		assertEquals(1, classUnderTest
				.searchLinks(LinkProperty.NAME, "Magazin").size());
	}

	/**
	 * @author mvogel
	 */
	@Test
	public void shouldDeleteMultipleLinksByName() throws Exception {
		// == prepare ==
		classUnderTest
				.addLink(
						"Neo4j-Tutorial-Web",
						"http://neo4j.com/docs/stable/tutorials-java-embedded-hello-world.html",
						"Neo4j-Tutorial-Title");
		classUnderTest.addLink("Neo4j-Tutorial",
				"http://neo4j.com/docs/stable/tutorials-web.html",
				"Neo4j-Tutorial-Web-Title");
		classUnderTest.addLink("Linux-Magazin", "http://linux-magazin.com",
				"my linux magazin");
		assertEquals(2,
				classUnderTest.searchLinks(LinkProperty.NAME, "Tutorial")
						.size());

		// == go ==
		classUnderTest.deleteLink(LinkProperty.NAME, "Tutorial",
				DeletionMode.SOFT);

		// == verify ==
		assertEquals(0,
				classUnderTest.searchLinks(LinkProperty.NAME, "Tutorial")
						.size());
		assertEquals(1, classUnderTest
				.searchLinks(LinkProperty.NAME, "Magazin").size());
	}

	/**
	 * @author mvogel
	 */
	@Test
	public void shouldDeleteMultipleLinksByUrl() throws Exception {
		// == prepare ==
		classUnderTest
				.addLink(
						"Neo4j-Tutorial-Web",
						"http://neo4j.com/docs/stable/tutorials-java-embedded-hello-world.html",
						"Neo4j-Tutorial-Title");
		classUnderTest.addLink("Neo4j-Tutorial",
				"http://neo4j.com/docs/stable/tutorials-web.html",
				"Neo4j-Tutorial-Web-Title");
		classUnderTest.addLink("Linux-Magazin", "http://linux-magazin.com",
				"my linux magazin");
		assertEquals(2,
				classUnderTest.searchLinks(LinkProperty.NAME, "Tutorial")
						.size());

		// == go ==
		classUnderTest.deleteLink(LinkProperty.URL, "neo4j", DeletionMode.SOFT);

		// == verify ==
		assertEquals(0,
				classUnderTest.searchLinks(LinkProperty.NAME, "Tutorial")
						.size());
		assertEquals(1, classUnderTest
				.searchLinks(LinkProperty.NAME, "Magazin").size());
	}

	/**
	 * @author mvogel
	 */
	@Test
	public void shouldOnlyOneLinkByExactUrl() throws Exception {
		// == prepare ==
		classUnderTest
				.addLink(
						"Neo4j-Tutorial-Web",
						"http://neo4j.com/docs/stable/tutorials-java-embedded-hello-world.html",
						"Neo4j-Tutorial-Title");
		classUnderTest.addLink("Neo4j-Tutorial",
				"http://neo4j.com/docs/stable/tutorials-web.html",
				"Neo4j-Tutorial-Web-Title");
		classUnderTest.addLink("Linux-Magazin", "http://linux-magazin.com",
				"my linux magazin");
		assertEquals(2,
				classUnderTest.searchLinks(LinkProperty.NAME, "Tutorial")
						.size());

		// == go ==
		classUnderTest
				.deleteLink(
						LinkProperty.URL,
						"http://neo4j.com/docs/stable/tutorials-java-embedded-hello-world.html",
						DeletionMode.EXACT);

		// == verify ==
		assertEquals(1,
				classUnderTest.searchLinks(LinkProperty.NAME, "Tutorial")
						.size());
		assertEquals(1, classUnderTest
				.searchLinks(LinkProperty.NAME, "Magazin").size());
	}

	/**
	 * @author mvogel
	 */
	@Test
	public void shouldNotDeleteLinkBecausePropertyvalueWasNotFound()
			throws Exception {
		// == prepare ==
		classUnderTest
				.addLink(
						"Neo4j-Tutorial-Web",
						"http://neo4j.com/docs/stable/tutorials-java-embedded-hello-world.html",
						"Neo4j-Tutorial-Title");
		classUnderTest.addLink("Neo4j-Tutorial",
				"http://neo4j.com/docs/stable/tutorials-web.html",
				"Neo4j-Tutorial-Web-Title");
		classUnderTest.addLink("Linux-Magazin", "http://linux-magazin.com",
				"my linux magazin");
		assertEquals(2,
				classUnderTest.searchLinks(LinkProperty.NAME, "Tutorial")
						.size());

		// == go ==
		classUnderTest.deleteLink(LinkProperty.URL, "i do not exist url",
				DeletionMode.EXACT);

		// == verify ==
		assertEquals(2,
				classUnderTest.searchLinks(LinkProperty.NAME, "Tutorial")
						.size());
		assertEquals(1, classUnderTest
				.searchLinks(LinkProperty.NAME, "Magazin").size());
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
		List<Tag> resultTags = classUnderTest.searchTags(TagProperty.NAME,
				"Prod");

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
		assertThat(classUnderTest.searchTags(TagProperty.NAME, oldName).size(),
				is(1));

		String newName = "Development";

		// == go ==
		classUnderTest.updateTag(TagProperty.NAME, oldName, newName);

		// == verify ==
		assertThat(classUnderTest.searchTags(TagProperty.NAME, oldName).size(),
				is(0));
		List<Tag> resultTags = classUnderTest.searchTags(TagProperty.NAME,
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
		assertEquals(2, classUnderTest.searchTags(TagProperty.NAME, "Tag")
				.size());

		// == go ==
		classUnderTest.deleteTag(TagProperty.NAME, "Tag", DeletionMode.SOFT);

		// == verify ==
		assertEquals(0, classUnderTest.searchTags(TagProperty.NAME, "Tag")
				.size());
		assertEquals(1, classUnderTest.searchTags(TagProperty.NAME, "My")
				.size());
	}

	/**
	 * @author mvogel
	 */
	@Test
	public void shouldDeleteExactlyOneTagByName() throws Exception {
		// == prepare ==
		classUnderTest.addTag("Tag1", "description1");
		classUnderTest.addTag("Tag2", "description2");
		classUnderTest.addTag("Myblabla", "description3");
		assertEquals(2, classUnderTest.searchTags(TagProperty.NAME, "Tag")
				.size());

		// == go ==
		classUnderTest.deleteTag(TagProperty.NAME, "Tag1", DeletionMode.EXACT);

		// == verify ==
		assertEquals(1, classUnderTest.searchTags(TagProperty.NAME, "Tag")
				.size());
		assertEquals(1, classUnderTest.searchTags(TagProperty.NAME, "My")
				.size());
	}

	/**
	 * @author mvogel
	 */
	@Test
	public void shouldTagALink() throws Exception {
		// == prepare ==
		classUnderTest.addTag("Tag1", "description1");
		classUnderTest.addLink("MyLink", "http://link.com", "MyLink-Title");

		// == go ==
		classUnderTest.addTagToLink("MyLink", "Tag1");
		List<Tag> tagsForLink = classUnderTest.getTagsForLink("MyLink");

		// == verify ==
		assertEquals(1, tagsForLink.size());
		assertEquals("Tag1", tagsForLink.get(0).getName());
	}

	/**
	 * @author mvogel
	 */
	@Test(expected = IllegalArgumentException.class)
	public void shouldFailToAddLinkBecauseItAlreadyExists() throws Exception {
		// == prepare ==
		classUnderTest.addLink("MyLink", "http://link.com", "MyLink-Title");

		// == go ==
		try {
			classUnderTest.addLink("MyLink", "http://link.com", "MyLink-Title");
			fail();
		} catch (Exception e) {
			// == verify ==
			assertTrue(e.getMessage().contains("Error on creating link"));
			throw e;
		}
	}

	/**
	 * @author mvogel
	 */
	@Test(expected = IllegalArgumentException.class)
	public void shouldFailToAddTagBecauseItAlreadyExists() throws Exception {
		// == prepare ==
		classUnderTest.addTag("Tag1", "description1");

		// == go ==
		try {
			classUnderTest.addTag("Tag1", "description1");
			fail();
		} catch (Exception e) {
			// == verify ==
			assertTrue(e.getMessage().contains("Error on creating tag"));
			throw e;
		}
	}

	@Test
	public void shouldIncrementTheClickCountOfALink() throws Exception {
		// == prepare ==
		String name = "Neo4j-Tutorial";
		String url = "http://neo4j.com/docs/stable/tutorials-java-embedded-hello-world.html";
		String title = "Neo4j-Tutorial-Title";
		classUnderTest.addLink(name, url, title);
		assertThat(classUnderTest.searchLinks(LinkProperty.NAME, name).get(0)
				.getClicks(), is(0));

		// == go ==
		classUnderTest.updateLink(LinkProperty.CLICK_COUNT, name, name);

		// == verify ==
		List<Link> resultLinks = classUnderTest.searchLinks(LinkProperty.NAME,
				name);
		assertThat(resultLinks.size(), is(1));
		Link resultLink = resultLinks.get(0);
		assertThat(resultLink.getName(), is(name));
		assertThat(resultLink.getUrl(), is(url));
		assertThat(resultLink.getTitle(), is(title));
		assertThat(resultLink.getClicks(), is(1));
		assertThat(resultLink.getScore(), is(0.0));
	}

	@Test
	public void shouldIncrementTheClickCountOfATag() throws Exception {
		// == prepare ==
		String name = "Tag1";
		String description = "description112sdsdds q2slkdsld";
		classUnderTest.addTag(name, description);
		assertThat(classUnderTest.searchTags(TagProperty.NAME, name).get(0)
				.getClicks(), is(0));

		// == go ==
		classUnderTest.incrementTagClick(name);

		// == verify ==
		List<Tag> resultLinks = classUnderTest.searchTags(TagProperty.NAME,
				name);
		assertThat(resultLinks.size(), is(1));
		Tag resultLink = resultLinks.get(0);
		assertThat(resultLink.getName(), is(name));
		assertThat(resultLink.getDescription(), is(description));
		assertThat(resultLink.getClicks(), is(1));
	}

	@Test
	public void shouldSetNewScoreForLink() {
		// == prepare ==
		String linkName = "Neo4j-Tutorial";
		String url = "http://neo4j.com/docs/stable/tutorials-java-embedded-hello-world.html";
		String title = "Neo4j-Tutorial-Title";
		classUnderTest.addLink(linkName, url, title);

		// == go ==
		double newScore = 9.8;
		classUnderTest.updateLinkScore(linkName, newScore);

		// == verify ==
		List<Link> resultLinks = classUnderTest.searchLinks(LinkProperty.NAME,
				linkName);
		assertThat(resultLinks.size(), is(1));
		Link resultLink = resultLinks.get(0);
		assertThat(resultLink.getUrl(), is(url));
		assertThat(resultLink.getTitle(), is(title));
		assertThat(resultLink.getClicks(), is(0));
		assertThat(resultLink.getScore(), is(9.8));
	}
}
