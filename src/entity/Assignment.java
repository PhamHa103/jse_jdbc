package entity;

import java.sql.*;
import java.util.List;

public class Assignment  {
    public static int AUTO_ID = getIdFromDB();

    private int id;
    private int driverId;
    private int routerId;
    private Driver driver;
    private Route route;
    private int numberOfTurn;

    public Assignment(int driverId, int routerId, int numberOfTurn) {
        this.id = ++AUTO_ID;
        this.driverId = driverId;
        this.routerId = routerId;
        this.numberOfTurn = numberOfTurn;
    }

    public Assignment(Driver driver, Route route, int numberOfTurn) {
        this.id = ++AUTO_ID;
        this.driver = driver;
        this.route = route;
        this.numberOfTurn = numberOfTurn;
    }

    private static int getIdFromDB() {
        int id = 0;
        Connection connection = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "sql01", "admin");
            String querySql = "SELECT MAX(ID) FROM ASSIGNMENT_SHEET ";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(querySql);
            while (resultSet.next()) {
                int temp = resultSet.getInt(1);
                if (temp>0)
                    id = temp;
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return id;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public int getRouterId() {
        return routerId;
    }

    public void setRouterId(int routerId) {
        this.routerId = routerId;
    }

    public int getId() {
        return id;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public int getNumberOfTurn() {
        return numberOfTurn;
    }

    public void setNumberOfTurn(int numberOfTurn) {
        this.numberOfTurn = numberOfTurn;
    }

}
