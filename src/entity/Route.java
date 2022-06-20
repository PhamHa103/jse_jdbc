package entity;

import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Route implements inputable {

    public static int AUTO_ID = getIdFromDB();


    private int id;
    private double distance;
    private int numberOfStop;


    public Route(int id, double distance, int numberOfStop) {
        this.id = id;
        this.distance = distance;
        this.numberOfStop = numberOfStop;
    }

    public Route() {
        this.id = ++AUTO_ID;
    }

    public int getId() {
        return id;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getNumberOfStop() {
        return numberOfStop;
    }

    public void setNumberOfStop(int numberOfStop) {
        this.numberOfStop = numberOfStop;
    }

    @Override
    public void inputInfor() {
        System.out.println("Nhập khoảng cách: ");
        do {
            try{
                this.distance = new Scanner(System.in).nextDouble();
                break;
            }catch (InputMismatchException ex){
                System.out.print("Bạn phải nhập kiểu số, xin mời bạn nhập lại: ");
            }
        }while(true);

        System.out.println("Nhập số điểm dừng: ");
        do {
            try{
                this.numberOfStop = new Scanner(System.in).nextInt();
                break;
            }catch (InputMismatchException ex){
                System.out.print("Bạn phải nhập kiểu số, xin mời bạn nhập lại: ");
            }
        }while(true);
    }

    @Override
    public String toString() {
        return "Tuyến xe với " +
                "id = " + id +
                ", khoảng cách " + distance +
                ", dừng xe tại " + numberOfStop +" điểm." ;
    }

    private static int getIdFromDB() {
        int id = 99;
        Connection connection = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "sql01", "admin");
            String querySql = "SELECT MAX(ID) FROM ROUTE ";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(querySql);
            while (resultSet.next()) {
                int temp = resultSet.getInt(1);
                if (temp>99)
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

}
