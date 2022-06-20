package service;

import entity.Route;

import java.sql.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class ServiceRoute {

    public static List<Route> ROUTES = new ArrayList<>();


    public static void printRoute() {
        if (isEmptyRoute()) {
            System.out.println("Hệ thống hiện không có tuyến xe nào!");
            return;
        } else {
            List<Route> routes = getListRouteFromDB();
            System.out.println("Danh sách tuyến xe của hệ thống ");
            for (int i = 0; i < routes.size(); i++) {
                System.out.println(routes.get(i));
            }
        }
    }

    public static void inputNewRouter() {
        System.out.print("Xin mời nhập số lượng tuyến xe mới muốn thêm: ");
        int routeNumber;
        do {
            try{
                routeNumber = new Scanner(System.in).nextInt();
                break;
            }catch (InputMismatchException ex){
                System.out.print("Bạn phải nhập kiểu số, xin mời bạn nhập lại: ");
            }
        }while (true);
        for (int i = 0; i < routeNumber; i++) {
            System.out.println("Xin mời nhập thông tin tuyến xe thứ " + (i + 1));
            Route route = new Route();
            route.inputInfor();
            ROUTES.add(route);
        }
        Connection connection = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "sql01", "admin");

            for (int i = 0; i < ROUTES.size(); i++) {
                Route route = ROUTES.get(i);
                String querySql = "INSERT INTO ROUTE (ID, DISTANCE, NUMBER_OF_STOP ) VALUES ({id},{distance}, {numberOfStop})";

                querySql = querySql.replace("{id}", route.getId() + "");
                querySql = querySql.replace("{distance}", route.getDistance() + "");
                querySql = querySql.replace("{numberOfStop}", route.getNumberOfStop() + "");

                Statement statement = connection.createStatement();
                int affectedRows = statement.executeUpdate(querySql);
                System.out.println(affectedRows);
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
        ROUTES.clear();
    }


    public static boolean isEmptyRoute() {
        List<Route> routes = getListRouteFromDB();
        if (routes.size() == 0)
                return true;
            else
                return false;
    }

    public static boolean routeIsExisted(int input) {
        List<Route> routes = getListRouteFromDB();
        for (int i = 0; i < routes.size(); i++) {
            if (routes.get(i).getId() == input)
                return true;
        }
        return false;
    }


    public static Route getRouteFromRouteId(int routeId) {
        Route route = null;
        Connection connection = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "sql01", "admin");
            String querySql = "SELECT * FROM ROUTE WHERE ID = " + routeId;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(querySql);
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                Double distance = resultSet.getDouble(2);
                int numberOfStop = resultSet.getInt(3);
                route = new Route(id, distance, numberOfStop);
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
        return route;
    }

    public static List<Route> getListRouteFromDB(){
        List<Route> routes = new ArrayList<>();
        Connection connection = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "sql01", "admin");
            String querySql = "SELECT * FROM ROUTE";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(querySql);
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                Double distance = resultSet.getDouble(2);
                int numberOfStop = resultSet.getInt(3);
                Route route = new Route(id, distance, numberOfStop);
                routes.add(route);
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
        return routes;
    }
}
