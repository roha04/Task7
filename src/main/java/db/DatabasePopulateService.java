package db;

import reader.SqlFileReader;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabasePopulateService {
    private static final String WORKER_QUERY="INSERT INTO worker (id, name, birthday, level, salary) VALUES (?, ?, ?, ?, ?)";
    private static final String CLIENT_QUERY="INSERT INTO client(id,name) VALUES(?,?)";
    private static final String PROJECT_QUERY="INSERT INTO project(id,client_id,start_date,finish_date) VALUES (?, ?, ?, ?)";
    private static final String PROJECT_WORKER_QUERY="INSERT INTO project_worker(project_id,worker_id) VALUES (?, ?)";
    public static void main(String[] args) {
        try {
            Connection connection = Database.getInstance().getConnection();


            insertWorker(connection,WORKER_QUERY);
            insertClient(connection,CLIENT_QUERY);
            insertProject(connection,PROJECT_QUERY);
            insertProjectWorker(connection,PROJECT_WORKER_QUERY);

            System.out.println("Database filled successfully.");
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static void insertWorker(Connection connection,String query) throws SQLException {
        try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
            int[] ids = {1, 2, 3, 4, 5,6,7,8,9,10};
            String[] names = {"Rostyslav Voloshchak", "Bill Gates", "John Cena", "Ilon Mask", "William Taylor",
            "Taylor Swift", "Johny Depp","Robert Martin","Alexander Garcia","Martin Odegaard"};
            String[] birthdays = {"2004-03-25", "1985-08-20", "1982-03-10", "1995-11-25", "1998-09-30",
            "1993-07-12","1987-12-05","1980-02-28","1991-04-18","1996-06-08"};
            String[] levels = {"Trainee", "Senior", "Middle", "Senior", "Trainee","Junior","Middle","Senior",
            "Junior","Trainee"};
            int[] salaries = {800, 7000, 3000, 6000, 900,1500,3500,7000,950,1350};
            for(int i=0;i<ids.length;i++){
                preparedStatement.setInt(1,ids[i]);
                preparedStatement.setString(2,names[i]);
                preparedStatement.setString(3,birthdays[i]);
                preparedStatement.setString(4,levels[i]);
                preparedStatement.setInt(5,salaries[i]);
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        }

    }
    private static void insertClient(Connection connection,String query) throws SQLException {
        try(PreparedStatement preparedStatement = connection.prepareStatement(query);){
            int ids[]={1,2,3,4,5};
            String names[]={"Amazon","Google","Microsoft","Apple","MSI"};
            for(int i=0;i<ids.length;i++){
                preparedStatement.setInt(1,ids[i]);
                preparedStatement.setString(2,names[i]);
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        }
    }
    private static void insertProject(Connection connection,String query) throws SQLException {
        try(PreparedStatement preparedStatement = connection.prepareStatement(query);){
            int ids[]={1,2,3,4,5,6,7,8,9,10};
            int client_ids[]={1,2,3,4,5,1,2,3,4,5};
            String start_dates[]={"2023-01-01", "2023-02-15", "2023-03-10", "2023-04-20", "2023-05-05", "2023-06-01",
            "2020-07-10", "2019-08-15", "2024-01-20", "2023-10-05"};
            String finish_dates[]={"2024-03-31", "2023-05-15", "2023-06-30", "2024-08-31", "2026-07-15", "2023-08-31",
            "2023-10-31", "2022-11-15", "2027-12-31", "2024-01-05"};
            for(int i=0;i< ids.length;i++){
                preparedStatement.setInt(1,ids[i]);
                preparedStatement.setInt(2,client_ids[i]);
                preparedStatement.setString(3,start_dates[i]);
                preparedStatement.setString(4,finish_dates[i]);
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        }
    }
    private static void insertProjectWorker(Connection connection,String query) throws SQLException{
        try(PreparedStatement preparedStatement=connection.prepareStatement(query)){
            int arraysSize=10;
            int project_ids[]={1,2,1,3,2,5,4,4,3,5};
            int worker_ids[]={3,1,6,7,9,6,4,5,2,10};
            for (int i=0;i<arraysSize;i++){
                preparedStatement.setInt(1,project_ids[i]);
                preparedStatement.setInt(2,worker_ids[i]);
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        }
    }
}

