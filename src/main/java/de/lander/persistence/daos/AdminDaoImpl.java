/**
 *
 */
package de.lander.persistence.daos;

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
 *
 *
 * @author mvogel
 *
 */
public class AdminDaoImpl {// implements AdminDao {

    private String storeDir = "/home/mvogel/tmp/neo4jtestdb";
    private GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder(storeDir)
            .setConfig(GraphDatabaseSettings.nodestore_mapped_memory_size, "10M")
            .setConfig(GraphDatabaseSettings.string_block_size, "60")
            .setConfig(GraphDatabaseSettings.array_block_size, "300").newGraphDatabase();

    private Label testLabel = DynamicLabel.label("myLabel");

    /**
     * Adds a link to the database
     *
     * @param name the name of the link
     * @param url the url -> the link
     */
    public void addLink(final String name, final String url) {
        Node firstNode;
        try (Transaction tx = graphDb.beginTx()) {
            firstNode = graphDb.createNode();
            firstNode.setProperty("name", name);
            firstNode.setProperty("url", url);
            firstNode.addLabel(testLabel);
            tx.success();
        }
    }

    // TODO
    public String readLink(final String name) {
        Label label = testLabel; // TODO what is this exactly?
        String key = "url";
        Object value = name;

        ResourceIterable<Node> foundLinks;
        StringBuilder sb = new StringBuilder();
        try (Transaction tx = graphDb.beginTx()) {
            foundLinks = graphDb.findNodesByLabelAndProperty(label, key, value);

            if (foundLinks != null) {
                // TODO use Stringjoining and java8 features
                ResourceIterator<Node> it = foundLinks.iterator();
                Node node;
                while (it.hasNext()) {
                    node = it.next();
                    sb.append(node.getProperty(key));
                    sb.append(" ");
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
