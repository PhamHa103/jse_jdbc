package service;

import constant.Specialization;
import entity.Driver;
import entity.Route;

import java.sql.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class ServiceDriver {

    public static List<Driver> DRIVERS = new ArrayList<>();


    public static void printDriver() {
        if (isEmptyDriver()) {
            System.out.println("Hệ thống hiện không có lái xe nào!");
            return;
        } else {
            List<Driver> drivers = getListDriverFromDB();
            System.out.println("Danh sách lái xe của hệ thống: ");
            for (int i = 0; i < drivers.size(); i++) {
                System.out.println(drivers.get(i));
            }
        }
    }

    public static void inputNewDriver() {
        System.out.print("Xin mời nhập số lượng lái xe mới muốn thêm: ");
        int driverNumber;
        do {
            try{
                driverNumber = new Scanner(System.in).nextInt();
                break;
            }catch (InputMismatchException ex){
                System.out.print("Bạn phải nhập kiểu số, xin mời bạn nhập lại: ");
            }
        }while (true);
        for (int i = 0; i < driverNumber; i++) {
            System.out.println("Xin mời nhập thông tin lái xe thứ " + (i + 1));
            Driver driver = new Driver();
            driver.inputInfor();
            DRIVERS.add(driver);
        }
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "sql01", "admin");

            for (int i = 0; i < DRIVERS.size(); i++) {
                Driver driver = DRIVERS.get(i);
                String querySql = "INSERT INTO DRIVER (ID, NAME, ADDRESS, PHONE, SPECIALIZATION ) VALUES ({id},'{name}', '{address}', '{phone}', '{specialization}')";

                querySql = querySql.replace("{id}", driver.getId() + "");
                querySql = querySql.replace("{name}", driver.getName() + "");
                querySql = querySql.replace("{address}", driver.getAddress() + "");
                querySql = querySql.replace("{phone}", driver.getPhone() + "");
                querySql = querySql.replace("{specialization}", driver.getSpecialization().value + "");

                Statement statement = connection.createStatement();
                int affectedRows = statement.executeUpdate(querySql);
                System.out.println(affectedRows);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        DRIVERS.clear();
    }


    public static boolean isEmptyDriver() {
        List<Driver> drivers = getListDriverFromDB();
        if (drivers.size() == 0)
            return true;
        else
            return false;
    }

    public static boolean driverIsExisted(int input) {
        List<Driver> drivers = getListDriverFromDB();
        for (int i = 0; i < drivers.size(); i++) {
            if (drivers.get(i).getId() == input)
                return true;
        }
        return false;
    }


    public static Driver getDriverFromDriverId(int driverId) {
        Driver driver = null;
        Connection connection = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "sql01", "admin");
            String querySql = "SELECT * FROM DRIVER WHERE ID = " + driverId;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(querySql);
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String address = resultSet.getString(3);
                String phone = resultSet.getString(4);
                String temp = resultSet.getString(5);
                Specialization specialization = null;
                switch (temp) {
                    case "LOAI_A":
                        specialization = Specialization.LOAI_A;
                        break;
                    case "LOAI_B":
                        specialization = Specialization.LOAI_B;
                        break;
                    case "LOAI_C":
                        specialization = Specialization.LOAI_C;
                        break;
                    case "LOAI_D":
                        specialization = Specialization.LOAI_D;
                        break;
                    case "LOAI_E":
                        specialization = Specialization.LOAI_E;
                        break;
                    case "LOAI_F":
                        specialization = Specialization.LOAI_F;
                        break;
                }
                driver = new Driver(id, name, address, phone, specialization);
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
        return driver;
    }

    public static List<Driver> getListDriverFromDB(){
        List<Driver> drivers = new ArrayList<>();
        Connection connection = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "sql01", "admin");
            String querySql = "SELECT * FROM DRIVER";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(querySql);
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String address = resultSet.getString(3);
                String phone = resultSet.getString(4);
                String temp =  resultSet.getString(5);
                Specialization specialization = null;
                switch (temp) {
                    case "Loại A":
                        specialization = Specialization.LOAI_A;
                        break;
                    case "Loại B":
                        specialization = Specialization.LOAI_B;
                        break;
                    case "Loại C":
                        specialization = Specialization.LOAI_C;
                        break;
                    case "Loại D":
                        specialization = Specialization.LOAI_D;
                        break;
                    case "Loại E":
                        specialization = Specialization.LOAI_E;
                        break;
                    case "Loại F":
                        specialization = Specialization.LOAI_F;
                        break;
                }
                Driver driver = new Driver(id, name, address, phone, specialization);
                drivers.add(driver);
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
        return drivers;
    }
}
