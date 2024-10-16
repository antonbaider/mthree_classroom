package com.mthree.IO;

import static com.mthree.IO.IOClassImpl.*;

public interface IOClass {
    static void applicationStart() {
        boolean exit = false;
        System.out.println("WELCOME TO STUDENT-DATA APPLICATION");
        System.out.println("===================================");
        do {
            IOClassImpl.printMainMenu();

            switch (scanner.nextLine()) {
                case "1":
                    createStudent();
                    break;

                case "2":
                    readAll();
                    break;

                case "3":
                    readById();
                    break;

                case "4":
                    updateStudent();
                    break;

                case "5":
                    deleteById();
                    break;

                case "6":
                    System.out.println("Exit");
                    System.out.println("============================");
                    exit = true;
                    break;

                default:
                    System.out.println("Invalid option");
                    System.out.println("============================");
                    break;
            }
            if (!exit) {
                System.out.println("Do you want to continue? (y/n)");
                String answer = scanner.nextLine();
                if (!answer.equalsIgnoreCase("y")) {
                    exit = true;
                }
            }

        } while (!exit);
    }
}
