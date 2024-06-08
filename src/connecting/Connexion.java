package connecting;

import java.sql.Connection;

public class Connexion {

    public static Connection getConnection(String base) throws Exception {
        try {
            Class.forName("org.postgresql.Driver");
            return java.sql.DriverManager.getConnection("jdbc:postgresql://localhost:5432/template", "postgres",
                    "root");
        } catch (Exception e) {
            throw e;
        }
    }
}
