/**
 *
 */
package de.lander.persistence.daos;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterable;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;

/**
 * Draft modus...
 *
 * @author mvogel
 *
 */
public class AdminDaoImpl {// implements AdminDao {

	/**
	 * Log4j2 Logger
	 */
	public static final transient Logger LOGGER = LogManager
			.getLogger(AdminDaoImpl.class);

	private String storeDir = "/home/mvogel/tmp/neo4jtestdb";
	private GraphDatabaseService graphDb;
	// = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder(storeDir)
	// .setConfig(GraphDatabaseSettings.nodestore_mapped_memory_size, "10M")
	// .setConfig(GraphDatabaseSettings.string_block_size, "60")
	// .setConfig(GraphDatabaseSettings.array_block_size,
	// "300").newGraphDatabase();

	private Label linkLabel = DynamicLabel.label("Link");
	private final String nameKey = "name";
	private final String urlKey = "url";

	/**
	 * Creates a new AdminDao
	 * 
	 * @param graphDb
	 *            the {@link GraphDatabaseService} to use
	 */
	public AdminDaoImpl(GraphDatabaseService graphDb) {
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
