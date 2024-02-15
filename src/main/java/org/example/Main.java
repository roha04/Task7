package org.example;

import db.Database;
import db.DatabaseQueryService;
import db.dao.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws SQLException, IOException {
        //Connection connection = Database.getInstance().getConnection();
        //connection.close();
        //To check work of the following methods you need to run one of it,others must be commented.
        //If you dont, it will occur exception: The object is already closed
        List<LongestProject> longestProjects = new DatabaseQueryService().findLongestProjectPrepared();
        //List<MaxSalaryWorker> maxSalaryWorkers = new DatabaseQueryService().findMaxSalaryWorkerPrepared();
        //List<YoungestEldestWorker> youngestEldestWorkers = new DatabaseQueryService().findYoungestEldestPrepared();
        //List<ProjectPrice> projectPrices = new DatabaseQueryService().calculateProjectPricePrepared();
        //List<MaxProjectCountClient> projectCountClients = new DatabaseQueryService().findMaxProjectsClientPrepared();

    }
}