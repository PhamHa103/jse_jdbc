package service;

import constant.Specialization;
import entity.Assignment;
import entity.Driver;
import entity.Route;

import java.sql.*;
import java.util.*;

import static service.ServiceRoute.*;
import static service.ServiceDriver.*;

public class ServiceAssignment {

    public static void printTableSumDistance() {
        System.out.println("Danh sách thống kê tổng khoảng cách chạy xe của mỗi tài xế: ");
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "sql01", "admin");
            String querySql = "SELECT DRIVER_ID,NAME,SUM " +
                    "FROM(" +
                    "SELECT*FROM" +
                    "(SELECT DRIVER_ID, SUM (NUMBER_OF_TURN*DISTANCE) AS SUM " +
                    "FROM(" +
                    "SELECT * FROM ASSIGNMENT_SHEET A " +
                    "JOIN ROUTE R ON A.ROUTE_ID = R.ID) " +
                    "GROUP BY DRIVER_ID) C " +
                    "JOIN DRIVER D ON C.DRIVER_ID = D.ID)";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(querySql);
            while (resultSet.next()) {
                int driverId = resultSet.getInt(1);
                String driverName = resultSet.getString(2);
                int sumDistance = resultSet.getInt(3);
                System.out.println("Lái xe tên " + driverName + " có ID = " + driverId + ", có tổng khoảng cách chạy xe trong ngày = " + sumDistance);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void printTableMangement() {
        if (isEmptyRoute()) {
            System.out.println("Hệ thống chưa có tuyến xe nào! Không thể tạo bảng phân công");
        } else if (isEmptyDriver()) {
            System.out.println("Hệ thống chưa có lái xe nào! Không thể tạo bảng phân công");
        } else {
            List<Assignment> assignments = getListAssignmentFromDB();
            if (assignments.size() == 0)
                System.out.println("Chưa có danh sách phân công trong hệ thống");
            else {
                System.out.println("--------------------------");
                System.out.println("Danh sách thông tin của hệ thống là: ");
                for (int i = 0; i < assignments.size(); i++) {
                    System.out.println("Lái xe " + assignments.get(i).getDriver().getName()
                            + " chạy tuyến xe có ID = " + assignments.get(i).getRoute().getId()
                            + ", dừng tại " + assignments.get(i).getNumberOfTurn() + " điểm.");
                }
            }
        }
    }

    private static List<Assignment> getListAssignmentFromDB() {
        List<Assignment> assignments = new ArrayList<>();
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "sql01", "admin");
            String querySql = "SELECT * FROM ASSIGNMENT_SHEET";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(querySql);
            while (resultSet.next()) {
                int driverId = resultSet.getInt(2);
                int routeId = resultSet.getInt(3);
                int numberOfTurn = resultSet.getInt(4);

                Driver driverFromDriverId = getDriverFromDriverId(driverId);
                Route routeFromRouteId = getRouteFromRouteId(routeId);
                Assignment assignment = new Assignment(driverFromDriverId, routeFromRouteId, numberOfTurn);
                assignments.add(assignment);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return assignments;
    }


    private static boolean IsExistedAssignment(int driverId, int routerId) {
        List<Integer> driverIds = new ArrayList<>();
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "sql01", "admin");
            String querySql = "SELECT DRIVER_ID FROM ASSIGNMENT_SHEET WHERE ROUTE_ID = " + routerId;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(querySql);
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                driverIds.add(id);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < driverIds.size(); i++) {
            if (driverIds.get(i) == driverId)
                return true;
        }
        return false;
    }


    public static void creatTableMangement() {
        if (isEmptyRoute())
            System.out.println("Chưa có tuyến xe nào trong hệ thống. Không thể lập bảng!");
        else if (isEmptyDriver())
            System.out.println("Chưa có lái xe nào trong hệ thống. KHông thể lập bảng!");
        else {
            System.out.print("Xin mời nhập số lượng lái xe muốn nhập danh sách: ");
            int number = new Scanner(System.in).nextInt();
            for (int i = 0; i < number; i++) {
                System.out.println("Xin mời nhập thông tin muốn tạo của lái xe thứ " + (i + 1));
                creatOneRecord();
            }
        }
    }

    private static void creatOneRecord() {
        List<Assignment> assignments = new ArrayList<>();
        do {
            // Tạo 1 lái xe từ ID nhập vào
            // 1.1  Nhập ID lái xe
            System.out.print("Xin mời nhập mã lái xe: ");
            int tempDriver = 0;
            do {
                do {
                    try {
                        tempDriver = new Scanner(System.in).nextInt();
                        break;
                    } catch (InputMismatchException ex) {
                        System.out.print("Bạn phải nhập kiểu số, xin mời bạn nhập lại: ");
                    }
                } while (true);
                if (driverIsExisted(tempDriver)) {
                    break;
                }
                System.out.print("ID lái xe không tồn tại trong hệ thống, xin mời bạn nhập lai: ");
            } while (true);

            // 1.3 Nhập tổng số tuyến chạy
            System.out.print("Xin mời nhập tổng số tuyến: ");
            int routeNumber;
            do {
                try {
                    routeNumber = new Scanner(System.in).nextInt();
                    break;
                } catch (InputMismatchException ex) {
                    System.out.print("Bạn phải nhập kiểu số, xin mời bạn nhập lại: ");
                }
            } while (true);

            int sumTurnOfOneRecord = 0;
            for (int j = 0; j < routeNumber; j++) {
                System.out.println("Xin mời nhập thông tin của tuyến thứ " + (j + 1));
                // Nhập thông tin tuyến
                System.out.print("Xin mời nhập ID tuyến xe :");
                int temp = 0;
                do {
                    do {
                        try {
                            temp = new Scanner(System.in).nextInt();
                            break;
                        } catch (InputMismatchException ex) {
                            System.out.print("Bạn phải nhập kiểu số, xin mời bạn nhập lại: ");
                        }
                    } while (true);

                    if (IsExistedAssignment(tempDriver, temp)) {
                        System.out.print("Lái xe đã được đăng ký tuyến này rồi nên không thể đăng ký, xin mời nhập lại: ");
                        continue;
                    }

                    if (routeIsExisted(temp))
                        break;
                    System.out.print("ID tuyến xe không tồn tại trong hệ thống, xin mời bạn nhập lại: ");
                } while (true);

                System.out.print("Xin mời nhập số lượt chạy xe của tuyến đó: ");
                int numberOfTurn;
                do {
                    try {
                        numberOfTurn = new Scanner(System.in).nextInt();
                        break;
                    } catch (InputMismatchException ex) {
                        System.out.print("Bạn phải nhập kiểu số, xin mời bạn nhập lại: ");
                    }
                } while (true);
                sumTurnOfOneRecord += numberOfTurn;

                //Lưu thông tin mỗi tuyến vào 1 giá trị assignment
                Assignment assignment = new Assignment(tempDriver, temp, numberOfTurn);
                assignments.add(assignment);
            }
            if (sumTurnOfOneRecord <= 15)
                break;
            assignments.clear();
            System.out.println("Tổng số lượt trong ngày của lái xe đã vượt quá 15, xin mời bạn nhập lại");
        } while (true);
        Connection connection = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "sql01", "admin");

            for (int i = 0; i < assignments.size(); i++) {

                String querySql = "INSERT INTO ASSIGNMENT_SHEET (ID, DRIVER_ID, ROUTE_ID,NUMBER_OF_TURN ) VALUES ({id},{driverId}, {routeId}, {numberOfTurn})";

                querySql = querySql.replace("{id}", assignments.get(i).getId() + "");
                querySql = querySql.replace("{driverId}", assignments.get(i).getDriverId() + "");
                querySql = querySql.replace("{routeId}", assignments.get(i).getRouterId() + "");
                querySql = querySql.replace("{numberOfTurn}", assignments.get(i).getNumberOfTurn() + "");

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
    }


    public static void sortByDriverName() {
        List<Assignment> Assignments = getListAssignmentFromDB();
        Comparator<Assignment> comparator = new Comparator<Assignment>() {
            @Override
            public int compare(Assignment o1, Assignment o2) {
                return o1.getDriver().getName().compareTo(o2.getDriver().getName());
            }
        };
        for (int i = 0; i < Assignments.size() - 1; i++) {
            for (int j = i + 1; j < Assignments.size(); j++) {
                if (comparator.compare(Assignments.get(i), Assignments.get(j)) > 0) {
                    Assignment temp = Assignments.get(i);
                    Assignments.set(i, Assignments.get(j));
                    Assignments.set(j, temp);
                }
            }
        }
        System.out.println("--------------------------");
        System.out.println("Danh sách thông tin của hệ thống là: ");
        for (int i = 0; i < Assignments.size(); i++) {
            System.out.println("Lái xe " + Assignments.get(i).getDriver().getName()
                    + " chạy tuyến xe có ID = " + Assignments.get(i).getRoute().getId()
                    + ", dừng tại " + Assignments.get(i).getNumberOfTurn() + " điểm.");
        }
    }

    public static void sortBySumTurn() {
        List<Assignment> Assignments = getListAssignmentFromDB();
        for (int i = 0; i < Assignments.size() - 1; i++) {
            for (int j = i + 1; j < Assignments.size(); j++) {
                if (Assignments.get(i).getNumberOfTurn() < Assignments.get(j).getNumberOfTurn()) {
                    Assignment temp = Assignments.get(i);
                    Assignments.set(i, Assignments.get(j));
                    Assignments.set(j, temp);
                }
            }
        }
        System.out.println("--------------------------");
        System.out.println("Danh sách thông tin của hệ thống là: ");
        for (int i = 0; i < Assignments.size(); i++) {
            System.out.println("Lái xe " + Assignments.get(i).getDriver().getName()
                    + " chạy tuyến xe có ID = " + Assignments.get(i).getRoute().getId()
                    + ", dừng tại " + Assignments.get(i).getNumberOfTurn() + " điểm.");
        }
    }

}
