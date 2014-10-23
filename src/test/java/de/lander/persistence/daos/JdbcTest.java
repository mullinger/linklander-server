/**
 *
 */
package de.lander.persistence.daos;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.lander.persistence.commons.PersistenceUtils;

/**
 * Test for a jdbc connection with adding and querying on in memory db<br>
 *
 * NOTE: use jdbc:neo4j://localhost:7474/ for local DB
 * with this query MATCH (nineties:Movie) WHERE nineties.released > 1990 AND nineties.released < 2000 RETURN nineties.title, nineties.released
 *
 * @author mvogel
 *
 */
public class JdbcTest {

    private Connection con;
    private Statement stmt;

    @Before
    public void setUp() throws ClassNotFoundException, SQLException {
        // Register driver
        Class.forName("org.neo4j.jdbc.Driver");
        // Connect
        con = DriverManager.getConnection(PersistenceUtils.IN_MEMORY_DB);
        stmt = con.createStatement();
    }

    @Test
    public void fillingAndQueryingMemoryDb() throws SQLException {

        // == prepare ==
        String insertSql =
                "CREATE (Hugo:Person {name:'Hugo Weaving', born:1960}) "
                        + "CREATE (AndyW:Person {name:'Andy Wachowski', born:1967}) "
                        + "CREATE (LanaW:Person {name:'Lana Wachowski', born:1965})";
        stmt.executeUpdate(insertSql);

        // == go query ==
        ResultSet rs = stmt.executeQuery("MATCH (persons:Person) WHERE persons.born > 1963 RETURN persons.name");

        // == verify ==
        int counter = 0;
        while (rs.next()) {
            System.out.println(rs.getString("persons.name"));
            counter++;

        }

        assertEquals(2, counter);
    }

    @After
    public void teardown() throws SQLException {
        stmt.close();
        con.close();
    }
}
