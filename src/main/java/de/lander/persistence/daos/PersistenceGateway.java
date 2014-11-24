/**
 *
 */
package de.lander.persistence.daos;

import java.util.List;

import de.lander.persistence.entities.Link;

/**
 * The Persistence Layer / Gateway
 *
 * @author mvogel
 *
 */
public interface PersistenceGateway {

	/**
	 * Supported Link Types
	 */
	enum LinkProperty {
		NAME, URL
	}

	/**
	 * Supported Tag Types
	 */
	enum TagProperty {
		NAME
	}

	// /////////////
	// CRUD LINK
	// /////////////
	/**
	 * Adds a new link with the given properties<br>
	 * 
	 * @param name the name the link can be searched later (MANDATORY)
	 * @param url the url (MANDATORY)
	 * @param title the title of the weburl (Optional)
	 */
	void addLink(String name, String url, String title);

	/**
	 * Updates the given property of a link<br>
	 * Does nothing of the link was not found
	 * 
	 * @param property the {@link LinkProperty} to update (MANDATORY)
	 * @param propertyValue the value to search for (MANDATORY)
	 * @param newPropertyValue the value to set the found property of the link (MANDATORY)
	 */
	void updateLink(LinkProperty property, String propertyValue, String newPropertyValue);

	/**
	 * Retrieves a list of links matching the given property 
	 * 
	 * @param property the property of the link to search for (MANDATORY)
	 * @param propertyValue the value of the property (MANDATORY)
	 * @return a list of matched links which will can be empty but never <code>null</code>
	 */
	List<Link> getLink(LinkProperty property, String propertyValue);

	/**
	 * Deletes the links matching the given property<br>
	 * Note: the propertyString can also be a substring!
	 * 
	 * @param property the property of the link
	 * @param propertyValue the value of the property
	 */
	void deleteLink(LinkProperty property, String propertyValue);

	// /////////////
	// CRUD TAG
	// /////////////
	void addTag(String name, String description);

	void updateTag(TagProperty property, String propertyValue);

	void getTag(TagProperty property, String propertyValue);

	void deleteTag(TagProperty property, String propertyValue);

	// /////////////
	// RELATIONS
	// /////////////
	void addTagToLink(String linkName, String tagName);

	// /////////////
	// SEARCH
	// /////////////
	void search(String searchString);
}
