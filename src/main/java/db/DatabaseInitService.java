package db;

import reader.SqlFileReader;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitService {
    public static void main(String[] args) {
        try {

            Connection connection = Database.getInstance().getConnection();


            String sqlFilePath = "sql/init_db.sql";
            String sqlQuery = SqlFileReader.readSqlFile(sqlFilePath);


           createTables(connection, sqlQuery);

            System.out.println("Database initialized successfully.");
            connection.close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
    private static void createTables(Connection connection, String sqlQuery) throws SQLException {
        try (Statement statement = connection.createStatement()) {

            String[] queries = sqlQuery.split(";");


            for (String query : queries) {
                if (!query.trim().isEmpty()) {
                    statement.execute(query);
                }
            }
        }
    }

}
