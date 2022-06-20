package entity;

import constant.Specialization;

import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Driver extends Person {

    public static int AUTO_ID = getIdFromDB();

    private Specialization specialization;

    public Driver() {
        super.id = ++AUTO_ID;
    }

    public Driver(int id, String name, String address, String phone, Specialization specialization) {
        super.id = id;
        super.name = name;
        super.address = address;
        super.phone = phone;
        this.specialization = specialization;
    }

    private static int getIdFromDB() {
        int id = 9999;
        Connection connection = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "sql01", "admin");
            String querySql = "SELECT MAX(ID) FROM DRIVER ";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(querySql);
            while (resultSet.next()) {
                int temp = resultSet.getInt(1);
                if (temp > 9999)
                    id = temp;
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
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

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }

    @Override
    public String toString() {
        return "Lái xe có " +
                "id=" + id +
                ", tên là '" + name + '\'' +
                ", địa chỉ '" + address + '\'' +
                ", SĐT '" + phone + '\'' +
                ", bằng lái xe " + specialization.value +
                '.';
    }

    @Override
    public void inputInfor() {
        super.inputInfor();
        inputDriverSpecialization();
    }

    private void inputDriverSpecialization() {
        System.out.println("Trình độ của lái xe mới");
        System.out.println("1. Loại A");
        System.out.println("2. Loại B");
        System.out.println("3. Loại C");
        System.out.println("4. Loại D");
        System.out.println("5. Loại E");
        System.out.println("6. Loại F");
        System.out.print("Xin mời chọn: ");
        int number = 0;
        do {
            try {
                number = new Scanner(System.in).nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Bạn phải nhập số nguyên, xin mời nhập lại");
            }
            if (number >= 1 && number <= 6)
                break;
            System.out.print("Bạn phải nhập số nguyên từ 1 đến 6, xin mời nhập lại: ");
        } while (true);
        switch (number) {
            case 1:
                this.specialization = Specialization.LOAI_A;
                break;
            case 2:
                this.specialization = Specialization.LOAI_B;
                break;
            case 3:
                this.specialization = Specialization.LOAI_C;
                break;
            case 4:
                this.specialization = Specialization.LOAI_D;
                break;
            case 5:
                this.specialization = Specialization.LOAI_E;
                break;
            case 6:
                this.specialization = Specialization.LOAI_F;
                break;
        }
    }
}
