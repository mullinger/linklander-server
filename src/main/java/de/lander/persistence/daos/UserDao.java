/**
 *
 */
package de.lander.persistence.daos;

import java.math.BigInteger;
import java.util.List;

/**
 * Represents the the queries/actions of a user
 *
 * @author mvogel
 *
 */
public interface UserDao {

    // Vervollständigung
    List<String> linkProposal(String substring);

    void updateScore(String link, BigInteger newScore);
}
