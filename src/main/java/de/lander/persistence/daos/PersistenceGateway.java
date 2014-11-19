/**
 *
 */
package de.lander.persistence.daos;


/**
 * TODO
 *
 * @author mvogel
 *
 */
public interface PersistenceGateway {

    enum LinkProperty{
        NAME, URL
    }

    enum TagProperty{
        NAME
    }

    // CRUD LINK
    void addLink(String name, String url, String title);

    void updateLink(LinkProperty property, String propertyToUpdate);

    void getLink(LinkProperty property, String propertyString);

    void deleteLink(LinkProperty property, String propertyString);

    // CRUD TAG
    void addTag(String name, String description);

    void updateTag(TagProperty property, String propertyString);

    void getTag(TagProperty property, String propertyString);

    void deleteTag(TagProperty property, String propertyString);

    // Combined
    void addTagToLink(String linkName, String tagName);

    // Search
    void search(String searchString);
}
