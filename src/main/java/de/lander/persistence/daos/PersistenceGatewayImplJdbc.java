/**
 *
 */
package de.lander.persistence.daos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import de.lander.persistence.commons.PersistenceUtils;

/**
 * @author mvogel
 *
 */
public class PersistenceGatewayImplJdbc {
    static {
        try {
            Class.forName("org.neo4j.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void addLink(final String name, final String url) throws SQLException {
        Connection con = DriverManager.getConnection(PersistenceUtils.IN_MEMORY_DB);
        try (Statement stmt = con.createStatement()) {
            // Filling
            String insertSql = "CREATE (Hugo:Person {name:'Hugo Weaving', born:1960})";
            stmt.executeUpdate(insertSql);
        }
    }
}
