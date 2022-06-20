package service;

import java.util.InputMismatchException;
import java.util.Scanner;

import static service.ServiceAssignment.*;
import static service.ServiceRoute.inputNewRouter;
import static service.ServiceRoute.printRoute;
import static service.ServiceDriver.inputNewDriver;
import static service.ServiceDriver.printDriver;

public class ServiceMenu {
    public static void showMenu() {
        boolean isExit = false;
        do {
            int functionalChoice = functionalChoice();
            switch (functionalChoice) {
                case 1:
                    inputNewRouter();
                    break;
                case 2:
                    printRoute();
                    break;
                case 3:
                    inputNewDriver();
                    break;
                case 4:
                    printDriver();
                    break;
                case 5:
                    creatTableMangement();
                    break;
                case 6:
                    printTableMangement();
                    break;
                case 7:
                    sortByDriverName();
                    break;
                case 8:
                    sortBySumTurn();
                    break;
                case 9:
                    printTableSumDistance();
                    break;
                case 10:
                    isExit = true;
                    System.out.println("Cảm ơn bạn đã sử dụng chương trình!");
                    break;
            }
        } while (!isExit);

    }

    private static int functionalChoice() {
        System.out.println("\n------PHẦN MỀM QUẢN LÝ PHÂN CÔNG LÁI XE BUÝT--------");
        System.out.println("1. Nhập tuyến xe mới");
        System.out.println("2. In ra danh sách tuyến xe trong hệ thống ");
        System.out.println("3. Nhập lái xe mới");
        System.out.println("4. In ra danh sách lái xe trong hệ thống ");
        System.out.println("5. Lập bảng phân công lái xe ");
        System.out.println("6. In bảng phân công lái xe ");
        System.out.println("7. Sắp xếp danh sách phân công lái xe theo họ tên lái xe");
        System.out.println("8. Sắp xếp danh sách phân công lái xe theo số lượng tuyến đảm nhận trong ngày(giảm dần)");
        System.out.println("9. Lập bảng kê tổng khoảng cách chạy xe trong ngày của mỗi lái xe");
        System.out.println("10. Kết thúc chương trình ");
        System.out.print("Xin mời chọn chức năng: ");
        int choice = 0;
        do {
            do {
                try{
                    choice = new Scanner(System.in).nextInt();
                    break;
                }catch (InputMismatchException ex){
                    System.out.print("Bạn phải nhập kiểu số, xin mời bạn nhập lại: ");
                }
            }while (true);
            if (choice >= 1 && choice <= 10)
                break;
            System.out.print("Bạn nhập sai rồi, xin mời nhập lại");
        } while (true);
        return choice;
    }
}
