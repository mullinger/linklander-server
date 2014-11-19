/**
 *
 */
package de.lander.persistence.daos;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.ResourceIterable;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;

/**
 * Draft modus...
 *
 * @author mvogel
 *
 */
public class PersistenceGatewayImpl {// implements AdminDao {

	/**
	 * Log4j2 Logger
	 */
	public static final transient Logger LOGGER = LogManager
			.getLogger(PersistenceGatewayImpl.class);

	private String storeDir = "/home/mvogel/tmp/neo4jtestdb";
	private GraphDatabaseService graphDb;
	// = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder(storeDir)
	// .setConfig(GraphDatabaseSettings.nodestore_mapped_memory_size, "10M")
	// .setConfig(GraphDatabaseSettings.string_block_size, "60")
	// .setConfig(GraphDatabaseSettings.array_block_size,
	// "300").newGraphDatabase();

	private Label linkLabel = DynamicLabel.label("Link");
	private Label TagLabel = DynamicLabel.label("Tag");
	private final String nameKey = "name";
	private final String urlKey = "url";

	/**
	 * Creates a new AdminDao
	 *
	 * @param graphDb
	 *            the {@link GraphDatabaseService} to use
	 */
	public PersistenceGatewayImpl(final GraphDatabaseService graphDb) {
		this.graphDb = graphDb;
	}

	/**
	 * Adds a link to the database
	 *
	 * @param name
	 *            the name of the link
	 * @param url
	 *            the url -> the link
	 */
	public void addLink(final String name, final String url) {
		Node linkNodeToPersist;
		try (Transaction tx = graphDb.beginTx()) {
			linkNodeToPersist = graphDb.createNode();
			linkNodeToPersist.setProperty(nameKey, name);
			linkNodeToPersist.setProperty(urlKey, url);
			linkNodeToPersist.addLabel(linkLabel);
			tx.success();
		}
	}

	/**
     * Adds a tag
     *
     * @param tag the tag to add
     */
    public void addTag(final String tag) {
        Node tagNode;
        try (Transaction tx = graphDb.beginTx()) {
            tagNode = graphDb.createNode();
            tagNode.setProperty("name", tag);
            tagNode.addLabel(TagLabel);
            tx.success();
        }
    }

	/**
	 * Reads the given link from the database
	 *
	 * @param name
	 *            the name of the link
	 * @return the url of the link
	 */
	public String readLink(final String name) {
		Label label = linkLabel;
		Object value = name;

		ResourceIterable<Node> foundLinks;
		StringBuilder sb = new StringBuilder();
		try (Transaction tx = graphDb.beginTx()) {
			foundLinks = graphDb.findNodesByLabelAndProperty(label, nameKey,
					value);

			if (foundLinks != null) {
				// TODO use Stringjoining and java8 features
				ResourceIterator<Node> it = foundLinks.iterator();
				Node node;
				while (it.hasNext()) {
					node = it.next();
					LOGGER.debug("Found node: name={} url={}", new Object[] { node.getProperty(nameKey), node.getProperty(urlKey) });
					sb.append(node.getProperty(urlKey));
					if (it.hasNext()) {
						sb.append(" ");
					}
				}
			}
		}
		return sb.toString();
	}

    /**
     * Retrieves a given Node for a Link from the database<br>
     *
     * @param url the url or part of the url to search for
     * @return the {@link Node}
     */
    public Node findLinkNode(final String url) {
        String key = "url";
        Object value = url;

        ResourceIterable<Node> foundLinks;
        StringBuilder sb = new StringBuilder();
        try (Transaction tx = graphDb.beginTx()) {
            foundLinks = graphDb.findNodesByLabelAndProperty(linkLabel, key, value);

            if (foundLinks != null) {
                // TODO use Stringjoining collector and java8 features
                ResourceIterator<Node> it = foundLinks.iterator();
                Node node;
                while (it.hasNext()) {
                    return  it.next();
                }
            }
        }
        return null;
    }

    /**
     * Retrieves a given tag from the database<br>
     * Returns an empty String if the tag was not found
     *
     * @param tagName the name of the tag
     * @return the name of the tag
     */
    public String findTag(final String tagName) {
        String key = "name";
        Object value = tagName;

        StringBuilder sb = new StringBuilder();
        try (Transaction tx = graphDb.beginTx()) {
            graphDb.findNodesByLabelAndProperty(TagLabel, key, value).forEach(s -> sb.append(s.getProperty(key)));
        }
        return sb.toString();
    }

    /**
     * Retrieves a given tag {@link Node} from the database<br>
     *
     * @param tagName the name of the tag
     * @return the {@link Node}
     */
    public Node findTagNode(final String tagName) {
        String key = "name";
        Object value = tagName;

        List<Node> nodes = new ArrayList<>();
        try (Transaction tx = graphDb.beginTx()) {
            graphDb.findNodesByLabelAndProperty(TagLabel, key, value).forEach(s -> nodes.add(s));
        }
        return nodes.get(0);
    }

    /**
     * Tags the given url with the tag
     *
     * @param tag the tag
     * @param url the url to tag
     * @throws IllegalArgumentException if the url was not found
     */
    public void tagLink(final String tag, final String url) {
        Node foundLink = findLinkNode(url);
        if (foundLink == null) {
            throw new IllegalArgumentException("url={" + url + "} was not found");
        }

        Node foundTag = findTagNode(tag);
        if (foundTag == null) {
            throw new IllegalArgumentException("tag={" + tag + "} was not found");
        }

        try (Transaction tx = graphDb.beginTx()) {
            RelationshipType type = DynamicRelationshipType.withName("TAGGED");
            foundLink.createRelationshipTo(foundLink, type);
        }
    }

    /**
     * TODO
     *
     * @param tagName the name of the tag
     * @return
     */
    public List<String> findLinksByTag(final String tagName) {
        String key = "name";
        Object value = tagName;

        ResourceIterable<Node> foundTags;
        StringBuilder sb = new StringBuilder();
        try (Transaction tx = graphDb.beginTx()) {
            foundTags = graphDb.findNodesByLabelAndProperty(linkLabel, key, value);
            RelationshipType type = DynamicRelationshipType.withName("TAGGED");
            foundTags.forEach(t -> t.getRelationships(type)); // TODO map?
        }
        return null;
    }

    /**
     * Shutdown hook for the graphDb
     *
     * @param graphDb the db to securely shutdown
     */
	private static void registerShutdownHook(final GraphDatabaseService graphDb) {
		// Registers a shutdown hook for the Neo4j instance so that it
		// shuts down nicely when the VM exits (even if you "Ctrl-C" the
		// running application).
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				graphDb.shutdown();
			}
		});
	}

}
