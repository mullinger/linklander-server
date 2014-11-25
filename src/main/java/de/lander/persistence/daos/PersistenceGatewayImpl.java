/**
 *
 */
package de.lander.persistence.daos;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.cypher.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterable;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.impl.util.StringLogger;

import scala.collection.Iterator;
import de.lander.persistence.entities.Link;
import de.lander.persistence.entities.Tag;

/**
 * Draft modus...
 *
 * @author mvogel
 *
 */
public class PersistenceGatewayImpl implements PersistenceGateway {

    /**
     * Log4j2 Logger
     */
    public static final transient Logger LOGGER = LogManager.getLogger(PersistenceGatewayImpl.class);

    private String storeDir = "/home/mvogel/tmp/neo4jtestdb";
    private GraphDatabaseService graphDb;
    private ExecutionEngine cypher;

    // = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder(storeDir)
    // .setConfig(GraphDatabaseSettings.nodestore_mapped_memory_size, "10M")
    // .setConfig(GraphDatabaseSettings.string_block_size, "60")
    // .setConfig(GraphDatabaseSettings.array_block_size,
    // "300").newGraphDatabase();


    /**
     * Creates a new AdminDao
     *
     * @param graphDb
     *            the {@link GraphDatabaseService} to use
     */
    public PersistenceGatewayImpl(final GraphDatabaseService graphDb) {
        this.graphDb = graphDb;
        cypher = new ExecutionEngine(graphDb, StringLogger.DEV_NULL);
    }

    /**
     * @see de.lander.persistence.daos.PersistenceGateway#addLink(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void addLink(final String name, final String url, final String title) {
    	Validate.notBlank(name, "the name of the link is blank");
    	Validate.notBlank(url, "the url of the link is blank");
    	Validate.notNull(title, "the title of the link is null");
    	
        Node node;
        try (Transaction tx = graphDb.beginTx()) {
            node = graphDb.createNode();
            node.addLabel(Link.LABEL);
            node.setProperty(Link.NAME, name);
            node.setProperty(Link.URL, url);
            node.setProperty(Link.TITLE, title);
            node.setProperty(Link.CLICK_COUNT, 0);
            node.setProperty(Link.SCORE, 0);
            tx.success();
        }
    }

    /**
     * @see de.lander.persistence.daos.PersistenceGateway#updateLink(de.lander.persistence.daos.PersistenceGateway.LinkProperty, java.lang.String, java.lang.String)
     */
    @Override
    public void updateLink(final LinkProperty property, final String propertyValue, final String newPropertyValue) {
    	Validate.notNull(property);
    	Validate.notBlank(propertyValue);
    	Validate.notBlank(newPropertyValue);

        Node linkToUpdate;
        try (Transaction tx = graphDb.beginTx()) {
            linkToUpdate = retrieveLinkByExactProperty(property, propertyValue);
            if (linkToUpdate != null) {
                switch (property) {
                    case NAME:
                        linkToUpdate.setProperty(Link.NAME, newPropertyValue);
                        break;
                    case URL:
                        linkToUpdate.setProperty(Link.URL, newPropertyValue);
                        break;
                    default:
                        throw new IllegalArgumentException("property={" + property + "} is not supported");
                }

                tx.success();
            } else {
                throw new IllegalArgumentException("no node was found for property={" + property + "} and value={"
                        + propertyValue + "}");
            }
        }
    }

    /**
     * Retrieves a link by matching the property exactly
     *
     * @param property the property
     * @param propertyValue the value of the property
     * @return the {@link Node} or <code>null</code> if no node was found
     */
    private Node retrieveLinkByExactProperty(final LinkProperty property, final String propertyValue) {

        ResourceIterable<Node> links = null;
        switch (property) {
            case NAME:
                links = graphDb.findNodesByLabelAndProperty(Link.LABEL, Link.NAME, propertyValue);
                break;
            case URL:
                links = graphDb.findNodesByLabelAndProperty(Link.LABEL, Link.URL, propertyValue);
                break;
            default:
                throw new IllegalArgumentException("property={" + property + "} is not supported");
        }

        ResourceIterator<Node> iterator = links.iterator();
        if (iterator.hasNext()) {
            // TODO mvogel: only first node will returned
            // there should only be one node with this searchable property!
            return iterator.next();
        } else {
            return null;
        }
    }

    /**
     * @see de.lander.persistence.daos.PersistenceGateway#getLink(de.lander.persistence.daos.PersistenceGateway.LinkProperty, java.lang.String)
     */
    @Override
    public List<Link> getLinks(final LinkProperty property, final String propertyValue) {
    	Validate.notNull(property);
    	Validate.notBlank(propertyValue);
    	
        List<Link> retrievedLinks = new ArrayList<>();

        String sql =
                new StringBuilder().append("MATCH (link:").append(Link.LABEL).append(") WHERE link.{property}  =~ '.*")
                        .append(propertyValue).append(".*'").append(" RETURN link").toString();

        ExecutionResult execute = null;
        try (Transaction tx = graphDb.beginTx()) {
            switch (property) {
                case NAME:
                    execute = cypher.execute(sql.replace("{property}", Link.NAME));
                    break;
                case URL:
                    execute = cypher.execute(sql.replace("{property}", Link.URL));
                    break;
                default:
                    throw new IllegalArgumentException("property '" + property.name() + "' is not supported");
            }

            Iterator<Node> links = execute.columnAs("link"); // from return statement
            while (links.hasNext()) {
                Node link = links.next();
                String name = String.valueOf(link.getProperty(Link.NAME));
                String title = String.valueOf(link.getProperty(Link.TITLE));
                String url = String.valueOf(link.getProperty(Link.URL));
                int clicks = Integer.valueOf(String.valueOf(link.getProperty(Link.CLICK_COUNT)));
                double score = Double.valueOf(String.valueOf(link.getProperty(Link.SCORE)));

                retrievedLinks.add(new Link(name, title, url, clicks, score));
            }
        }

        return retrievedLinks;
    }

    /**
     * @see de.lander.persistence.daos.PersistenceGateway#deleteLink(de.lander.persistence.daos.PersistenceGateway.LinkProperty, java.lang.String)
     */
    @Override
    public void deleteLink(final LinkProperty property, final String propertyValue) {
    	Validate.notNull(property);
    	Validate.notBlank(propertyValue);
    	
    	String sql =
                new StringBuilder().append("MATCH (link:").append(Link.LABEL).append(")" )
                		.append("WHERE link.{property}  =~ '.*")
                        .append(propertyValue).append(".*'")
                        .append(" DELETE link").toString();
    	

    	ExecutionResult execute = null;
        try (Transaction tx = graphDb.beginTx()) {
            switch (property) {
                case NAME:
				execute = cypher.execute(sql.replace("{property}", Link.NAME));
                    break;
                case URL:
                	execute = cypher.execute(sql.replace("{property}", Link.URL));
                    break;
                default:
                    throw new IllegalArgumentException("property '" + property.name() + "' is not supported");
            }
            
            tx.success();
    	}
    }

    /**
     * @see de.lander.persistence.daos.PersistenceGateway#addTag(java.lang.String, java.lang.String)
     */
    @Override
    public void addTag(final String name, final String description) {
    	Validate.notBlank(name, "the name of the tag is blank");
    	Validate.notBlank(description, "the description of the tag is blank");
    	Validate.isTrue(description.length() <= 255, "the description is longer than 255 chars");
    	
        Node node;
        try (Transaction tx = graphDb.beginTx()) {
            node = graphDb.createNode();
            node.addLabel(Tag.LABEL);
            node.setProperty(Tag.NAME, name);
            node.setProperty(Tag.DESCRIPTION, description);
            node.setProperty(Tag.CLICK_COUNT, 0);
            tx.success();
        }
    }

    /**
     * @see de.lander.persistence.daos.PersistenceGateway#updateTag(de.lander.persistence.daos.PersistenceGateway.TagProperty, java.lang.String)
     */
    @Override
    public void updateTag(final TagProperty property, final String propertyValue, final String newPropertyValue) {
    	Validate.notNull(property);
    	Validate.notBlank(propertyValue);
    	Validate.notBlank(newPropertyValue);

        Node tagToUpdate;
        try (Transaction tx = graphDb.beginTx()) {
            tagToUpdate = retrieveTagByExactProperty(property, propertyValue);
            if (tagToUpdate != null) {
                switch (property) {
                    case NAME:
                        tagToUpdate.setProperty(Tag.NAME, newPropertyValue);
                        break;
                    default:
                        throw new IllegalArgumentException("property={" + property + "} is not supported");
                }

                tx.success();
            } else {
                throw new IllegalArgumentException("no node was found for property={" + property + "} and value={"
                        + propertyValue + "}");
            }
        }
    }
    
    /**
     * Retrieves a tag by matching the property exactly
     *
     * @param property the property
     * @param propertyValue the value of the property
     * @return the {@link Node} or <code>null</code> if no node was found
     */
    private Node retrieveTagByExactProperty(final TagProperty property, final String propertyValue) {

        ResourceIterable<Node> links = null;
        switch (property) {
            case NAME:
                links = graphDb.findNodesByLabelAndProperty(Tag.LABEL, Tag.NAME, propertyValue);
                break;
            default:
                throw new IllegalArgumentException("property={" + property + "} is not supported");
        }

        ResourceIterator<Node> iterator = links.iterator();
        if (iterator.hasNext()) {
            // TODO mvogel: only first node will returned
            // there should only be one node with this searchable property!
            return iterator.next();
        } else {
            return null;
        }
    }

    /**
     * @see de.lander.persistence.daos.PersistenceGateway#getTag(de.lander.persistence.daos.PersistenceGateway.TagProperty, java.lang.String)
     */
    @Override
    public List<Tag> getTags(final TagProperty property, final String propertyValue) {
    	Validate.notNull(property);
    	Validate.notBlank(propertyValue);
    	
        List<Tag> retrievedTags = new ArrayList<>();

        String sql =
                new StringBuilder().append("MATCH (tag:").append(Tag.LABEL).append(") WHERE tag.{property}  =~ '.*")
                        .append(propertyValue).append(".*'").append(" RETURN tag").toString();

        ExecutionResult execute = null;
        try (Transaction tx = graphDb.beginTx()) {
            switch (property) {
                case NAME:
                    execute = cypher.execute(sql.replace("{property}", Tag.NAME));
                    break;
                default:
                    throw new IllegalArgumentException("property '" + property.name() + "' is not supported");
            }

            Iterator<Node> links = execute.columnAs("tag"); // from return statement
            while (links.hasNext()) {
                Node link = links.next();
                String name = String.valueOf(link.getProperty(Tag.NAME));
                String description = String.valueOf(link.getProperty(Tag.DESCRIPTION));
                int clicks = Integer.valueOf(String.valueOf(link.getProperty(Link.CLICK_COUNT)));

                retrievedTags.add(new Tag(name, description, clicks));
            }
        }

        return retrievedTags;
    }

    /**
     * @see de.lander.persistence.daos.PersistenceGateway#deleteTag(de.lander.persistence.daos.PersistenceGateway.TagProperty, java.lang.String)
     */
    @Override
    public void deleteTag(final TagProperty property, final String propertyValue) {
    	Validate.notNull(property);
    	Validate.notBlank(propertyValue);
    	
        String sql =
                new StringBuilder().append("MATCH (tag:").append(Tag.LABEL).append(") WHERE tag.{property}  =~ '.*")
                        .append(propertyValue).append(".*'").append(" DELETE tag").toString();

        ExecutionResult execute = null;
        try (Transaction tx = graphDb.beginTx()) {
            switch (property) {
                case NAME:
                    execute = cypher.execute(sql.replace("{property}", Tag.NAME));
                    break;
                default:
                    throw new IllegalArgumentException("property '" + property.name() + "' is not supported");
            }
            
            tx.success();
        }

    }

    /**
     * @see de.lander.persistence.daos.PersistenceGateway#addTagToLink(java.lang.String, java.lang.String)
     */
    @Override
    public void addTagToLink(final String linkName, final String tagName) {
        // TODO Auto-generated method stub

    }

    /**
     * @see de.lander.persistence.daos.PersistenceGateway#search(java.lang.String)
     */
    @Override
    public void search(final String searchString) {
        // TODO Auto-generated method stub

    }

    //
    // /**
    // * Shutdown hook for the graphDb
    // *
    // * @param graphDb the db to securely shutdown
    // */
    // private static void registerShutdownHook(final GraphDatabaseService graphDb) {
    // // Registers a shutdown hook for the Neo4j instance so that it
    // // shuts down nicely when the VM exits (even if you "Ctrl-C" the
    // // running application).
    // Runtime.getRuntime().addShutdownHook(new Thread() {
    // @Override
    // public void run() {
    // graphDb.shutdown();
    // }
    // });
    // }

}
