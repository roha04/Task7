package db;

import db.dao.*;
import reader.SqlFileReader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseQueryService {

    private static final String PROJECT_PRICE_QUERY="sql/print_project_prices.sql";
    private static final String LONGEST_PROJECT_QUERY="SELECT id, count_day FROM (\n" +
            " SELECT project.id, (EXTRACT(DAY FROM finish_date - start_date)) as count_day FROM project)\n" +
            "WHERE count_day = ?";
    private static final String LONGEST_PROJECT_CONDITION="(SELECT MAX(count_day) FROM ("+
            "SELECT project.id, (EXTRACT(DAY FROM finish_date - start_date)) as count_day FROM project));";
    private static final String MAX_SALARY_WORKER_QUERY="SELECT name, salary FROM worker"+" WHERE salary = ?";
    private static final String MAX_SALARY_WORKER_CONDITION="(SELECT MAX(salary) FROM worker);";
    private static final String YOUNGEST_ELDEST_WORKER_QUERY =
            "SELECT 'YOUNGEST' AS TYPE, name, birthday " +
                    "FROM worker " +
                    "WHERE birthday = ? " +
                    "UNION ALL " +
                    "SELECT 'ELDEST' AS TYPE, name, birthday " +
                    "FROM worker " +
                    "WHERE birthday = ?";

    private static final String YOUNGEST_BIRTHDAY_CONDITION = "(SELECT MAX(birthday) FROM worker)";
    private static final String ELDEST_BIRTHDAY_CONDITION = "(SELECT MIN(birthday) FROM worker)";
    private static final String MAX_PROJECTS_CLIENT_QUERY="SELECT  client.name, COUNT(project.id) AS project_count\n" +
            "FROM client\n" +
            "JOIN project ON client.id = project.client_id\n" +
            "GROUP BY client.id, client.name\n" +
            "HAVING COUNT(project.id) = ?";
    private static final String Max_PROJECT_CLIENT_CONDITION="(\n" +
            "    SELECT MAX(project_count) \n" +
            "    FROM (\n" +
            "        SELECT COUNT(*) AS project_count\n" +
            "        FROM project\n" +
            "        GROUP BY client_id\n" +
            "    ) AS subquery\n" +
            ");";
    private static String sqlQuery="";

    public  List<LongestProject> findLongestProjectPrepared() throws SQLException {
        ArrayList<LongestProject> result = new ArrayList<>();

        sqlQuery = LONGEST_PROJECT_QUERY;

        try (Connection connection = Database.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);) {
            int maxCountDay = getMaxCountDay(connection);

            preparedStatement.setInt(1, maxCountDay);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    LongestProject longestProject = new LongestProject();
                    longestProject.setId(resultSet.getInt("id"));
                    longestProject.setCountDay(resultSet.getInt("count_day"));
                    result.add(longestProject);
                }
            }
        }

        return result;

    }

    private int getMaxCountDay(Connection connection) throws SQLException {
        sqlQuery = LONGEST_PROJECT_CONDITION;
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sqlQuery)) {
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }

        return -1;
    }
    public List<MaxSalaryWorker>findMaxSalaryWorkerPrepared() throws SQLException{
        ArrayList<MaxSalaryWorker> result= new ArrayList<>();
        sqlQuery=MAX_SALARY_WORKER_QUERY;
        try (Connection connection=Database.getInstance().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);){
            int maxSalary  = getMaxSalary(connection);
            preparedStatement.setInt(1,maxSalary);
            try (ResultSet resultSet = preparedStatement.executeQuery()){
                while(resultSet.next()){
                    MaxSalaryWorker maxSalaryWorker = new MaxSalaryWorker();
                    maxSalaryWorker.setName(resultSet.getString("name"));
                    maxSalaryWorker.setSalary(resultSet.getInt("salary"));
                    result.add(maxSalaryWorker);
                }
            }
        }
        return result;
    }
    private int getMaxSalary(Connection connection) throws SQLException{
        sqlQuery=MAX_SALARY_WORKER_CONDITION;
        try(Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlQuery);) {
            if(resultSet.next()){
                return resultSet.getInt(1);
            }
            return -1;

        }


    }
    public List<YoungestEldestWorker> findYoungestEldestPrepared() throws SQLException {
        ArrayList<YoungestEldestWorker> result= new ArrayList<>();
        sqlQuery=YOUNGEST_ELDEST_WORKER_QUERY;
        try (Connection connection = Database.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
             PreparedStatement maxBirthdayStatement = connection.prepareStatement(YOUNGEST_BIRTHDAY_CONDITION);
             PreparedStatement minBirthdayStatement = connection.prepareStatement(ELDEST_BIRTHDAY_CONDITION);) {

            preparedStatement.setString(1, YOUNGEST_BIRTHDAY_CONDITION);
            preparedStatement.setString(2, ELDEST_BIRTHDAY_CONDITION);

            Date maxBirthday;
            try (ResultSet resultSet = maxBirthdayStatement.executeQuery()) {
                resultSet.next();
                maxBirthday = resultSet.getDate(1);
            }

            Date minBirthday;
            try (ResultSet resultSet = minBirthdayStatement.executeQuery()) {
                resultSet.next();
                minBirthday = resultSet.getDate(1);
            }

            preparedStatement.setDate(1, maxBirthday);
            preparedStatement.setDate(2, minBirthday);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    YoungestEldestWorker youngestEldestWorker = new YoungestEldestWorker();
                    youngestEldestWorker.setType(Type.valueOf(resultSet.getString("TYPE")));
                    youngestEldestWorker.setName(resultSet.getString("name"));
                    youngestEldestWorker.setBirthday(resultSet.getDate("birthday"));
                    result.add(youngestEldestWorker);
                }
            }
        }

        return result;

    }
    public List<ProjectPrice> calculateProjectPricePrepared() throws SQLException{
        List<ProjectPrice> result = new ArrayList<>();
        try {
            sqlQuery = SqlFileReader.readSqlFile(PROJECT_PRICE_QUERY);
        } catch (IOException e) {
            e.printStackTrace();
            return result;
        }


        try (Connection connection = Database.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                ProjectPrice projectPrice = new ProjectPrice();
                projectPrice.setClientName(resultSet.getString("client_name"));
                projectPrice.setProjectPrice(resultSet.getInt("project_price"));
                result.add(projectPrice);
            }
        }

        return result;
    }
    public List<MaxProjectCountClient> findMaxProjectsClientPrepared() throws IOException,SQLException{
        ArrayList<MaxProjectCountClient> result = new ArrayList<>();

        sqlQuery=MAX_PROJECTS_CLIENT_QUERY;
        try (Connection connection = Database.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
             PreparedStatement subQueryStatement = connection.prepareStatement(Max_PROJECT_CLIENT_CONDITION)) {

            try (ResultSet subQueryResultSet = subQueryStatement.executeQuery()) {
                int maxProjectCount = 0;
                if (subQueryResultSet.next()) {
                    maxProjectCount = subQueryResultSet.getInt(1);
                }

                preparedStatement.setInt(1, maxProjectCount);
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    MaxProjectCountClient client = new MaxProjectCountClient();
                    client.setName(resultSet.getString("name"));
                    client.setProjectCount(resultSet.getInt("project_count"));
                    result.add(client);
                }
            }
        }

        return result;
    }

}


