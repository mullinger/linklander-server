/**
 *
 */
package de.lander.persistence.commons;

/**
 * @author mvogel
 *
 */
public final class PersistenceUtils {


    public static final String IN_MEMORY_DB = "jdbc:neo4j:mem:landerDb";


    /**
     * pervent instantiation
     */
    private PersistenceUtils() {
        throw new AssertionError();
    }

}
