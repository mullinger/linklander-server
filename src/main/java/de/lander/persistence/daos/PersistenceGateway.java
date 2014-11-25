/**
 *
 */
package de.lander.persistence.daos;

import java.util.List;

import javax.servlet.jsp.tagext.TagSupport;

import de.lander.persistence.entities.Link;
import de.lander.persistence.entities.Tag;

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
	 * Initializes {@link Link#SCORE} and {@link Link#CLICK_COUNT} with 0
	 * 
	 * @param name
	 *            the name the link can be searched later (MANDATORY)
	 * @param url
	 *            the url (MANDATORY)
	 * @param title
	 *            the title of the weburl (Optional)
	 */
	void addLink(String name, String url, String title);

	/**
	 * Updates the given property of a link<br>
	 * Does nothing of the link was not found
	 * 
	 * @param property
	 *            the {@link LinkProperty} to update (MANDATORY)
	 * @param propertyValue
	 *            the value to search for (MANDATORY)
	 * @param newPropertyValue
	 *            the value to set the found property of the link (MANDATORY)
	 *            
	 * @throws {@link IllegalArgumentException} if no link with the property was found      
	 */
	void updateLink(LinkProperty property, String propertyValue,
			String newPropertyValue);

	/**
	 * Retrieves a list of links matching the given property
	 * 
	 * @param property
	 *            the property of the link to search for (MANDATORY)
	 * @param propertyValue
	 *            the value of the property (MANDATORY)
	 * @return a list of matched links which will can be empty but never
	 *         <code>null</code>
	 */
	List<Link> getLinks(LinkProperty property, String propertyValue);

	/**
	 * Deletes the links matching the given property<br>
	 * Note: the propertyString can also be a substring!
	 * 
	 * @param property
	 *            the property of the link (MANDATORY)
	 * @param propertyValue
	 *            the value of the property (MANDATORY)
	 */
	void deleteLink(LinkProperty property, String propertyValue);

	// /////////////
	// CRUD TAG
	// /////////////
	/**
	 * Adds a Tag with the given properties<br>
	 * Initializes {@link Tag#CLICK_COUNT} with 0
	 * 
	 * @param name the name (MANDATORY)
	 * @param description the description (MANDATORY with max 255 chars)
	 */
	void addTag(String name, String description);

	/**
	 * Updates the given property of a tag
	 * 
	 * @param property the property to update
	 * @param propertyValue the current property value
	 * @param newPropertyValue the new property value
	 * @throws {@link IllegalArgumentException} if no tag with the property was found
	 */
	void updateTag(TagProperty property, String propertyValue,
			String newPropertyValue);

	/**
	 * Retrieves {@link Tag}s with the given property 
	 * 
	 * @param property the property
	 * @param propertyValue the value of the property
	 * @return a list of {@link Tag}s
	 */
	List<Tag> getTags(TagProperty property, String propertyValue);

	/**
	 * Deletes all the tags with the given property
	 * 
	 * @param property the property
	 * @param propertyValue the value of the property
	 */
	void deleteTag(TagProperty property, String propertyValue);

	// /////////////
	// RELATIONS
	// /////////////
	/**
	 * Tags a link with the given linkName with a tag
	 * 
	 * @param linkName the name of the link to tag
	 * @param tagName the name of the tag to tag the link with
	 */
	void addTagToLink(String linkName, String tagName);

	// /////////////
	// SEARCH
	// /////////////
	void search(String searchString);

	// /////////////
	// MISC
	// /////////////

	// get all Links and Tags for displaying 
}
