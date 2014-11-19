/**
 *
 */
package de.lander.persistence.daos;

import java.util.Map;

/**
 * Represents use-cases for an administrator
 *
 * @author mvogel
 *
 */
public interface PersistenceGateway {

    void addLink(String url);

    void addTag(String tag);

    void updateTag(String tag, Map<String, String> attributes);

    void createCategory(String category);

    void updateCategory(String category, Map<String, String> attributes);
}
