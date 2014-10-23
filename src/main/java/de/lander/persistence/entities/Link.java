/**
 *
 */
package de.lander.persistence.entities;

/**
 * Holds attributes for a link
 *
 * @author mvogel
 *
 */
public class Link {

    private Link() {
        throw new AssertionError();
    }

    public static final String NAME = "name";
    public static final String URL = "url";
    public static final String CLICK_COUNT = "clicks";
    public static final String SCORE = "score";
}
