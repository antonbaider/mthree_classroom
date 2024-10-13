package com.sg.classroster.ui;

import java.util.InputMismatchException;
import java.util.Scanner;

public class UserIOConsoleImpl implements UserIO {

        Scanner scanner = new Scanner(System.in);

        private <T extends Number> T readNumber(String prompt, T min, T max, Class<T> type) {
            Number input = null;

            while (true) {
                try {
                    System.out.println(prompt);

                    if (type == Integer.class) {
                        input = scanner.nextInt();
                        scanner.nextLine();
                    } else if (type == Double.class) {
                        input = scanner.nextDouble();
                        scanner.nextLine();
                    } else if (type == Float.class) {
                        input = scanner.nextFloat();
                        scanner.nextLine();
                    } else if (type == Long.class) {
                        input = scanner.nextLong();
                        scanner.nextLine();
                    } else {
                        throw new IllegalArgumentException("Unsupported number type");
                    }

                    if (input.doubleValue() >= min.doubleValue() && input.doubleValue() <= max.doubleValue()) {
                        System.out.println("You entered: " + input);
                        return type.cast(input);
                    } else {
                        System.out.println("Please enter a number between " + min + " and " + max + ".");
                    }

                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                    scanner.nextLine();
                }
            }
        }

        /**
         * Print a given String to the console.
         * The String value displayed should be passed in as a parameter.
         *
         * @param message
         */
        @Override
        public void print(String message) {
            System.out.println(message);
        }

        /**
         * Display a given message String to prompt the user to enter a String,
         * then read in the user response as a String and return that value.
         * The prompt message should be passed in as a parameter and
         * the String value read in should be the return value of the method.
         *
         * @param prompt
         * @return String
         */
        @Override
        public String readString(String prompt) {
            System.out.println(prompt);
            return scanner.nextLine();
        }

        /**
         * Display a given message String to prompt the user to enter an integer,
         * then read in the user response and return that integer value.
         * The prompt message value should be passed in as a parameter and the value that
         * is read in should be the return of the method.
         *
         * @param prompt
         * @return int
         */
        @Override
        public int readInt(String prompt) {
            return readNumber(prompt, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.class);
        }

        /**
         * Display a prompt to the user to enter an integer between a specified min and max range,
         * and read in an integer. If the user's number does not fall within the range,
         * keep prompting the user for new input until it does.
         * he prompt message and the min and max values should be passed in as parameters.
         * The value read in from the console should be the return of the method.
         *
         * @param prompt
         * @param min
         * @param max
         * @return int
         */
        @Override
        public int readInt(String prompt, int min, int max) {
            return readNumber(prompt, min, max, Integer.class);
        }

        /**
         * Display a given message String to prompt the user to enter a double,
         * then read in the user response and return that double value.
         * The prompt message value should be passed in as a parameter and
         * the value that is read in should be the return of the method.
         *
         * @param prompt
         * @return
         */
        @Override
        public double readDouble(String prompt) {
            return readNumber(prompt, Double.MIN_VALUE, Double.MAX_VALUE, Double.class);
        }

        /**
         * Display a prompt to the user to enter a double between a specified
         * min and max range, and read in a double. If the user's number does not fall
         * within the range, keep prompting the user for new input until it does.
         * The prompt message and min and max values should be passed in as parameters.
         * The value read in from the console should be the return of the method.
         *
         * @param prompt
         * @param min
         * @param max
         * @return
         */
        @Override
        public double readDouble(String prompt, double min, double max) {
            return readNumber(prompt, min, max, Double.class);
        }

        /**
         * Display a given message String to prompt the user to enter a float and then read
         * in the user response and return that float value. The prompt message value should
         * be passed in as a parameter and the value that is read in should be the return of the method.
         *
         * @param prompt
         * @return
         */
        @Override
        public float readFloat(String prompt) {
            return readNumber(prompt, Float.MIN_VALUE, Float.MAX_VALUE, Float.class);
        }

        /**
         * Display a prompt to the user to enter a float between a specified min and max range,
         * and read in a float. If the user's number does not fall within the range,
         * keep prompting the user for new input until it does. The prompt message and min and max
         * values should be passed in as parameters. The value read in from the console should be the return of the method.
         *
         * @param prompt
         * @param min
         * @param max
         * @return
         */
        @Override
        public float readFloat(String prompt, float min, float max) {
            return readNumber(prompt, min, max, Float.class);
        }

        /**
         * Display a given message String to prompt the user to enter a long,
         * then read in the user response and return that long value.
         * The prompt message value should be passed in as a parameter and the value
         * that is read in should be the return of the method.
         *
         * @param prompt
         * @return
         */
        @Override
        public long readLong(String prompt) {
            return readNumber(prompt, Long.MIN_VALUE, Long.MAX_VALUE, Long.class);
        }

        /**
         * Display a prompt to the user to enter a long between a specified min and max range,
         * and read in a long. If the user's number does not fall within the range, keep prompting
         * the user for new input until it does. The prompt message and min and max values should be
         * passed in as parameters. The value read in from the console should be the return of the method.
         *
         * @param prompt
         * @param min
         * @param max
         * @return
         */
        @Override
        public long readLong(String prompt, long min, long max) {
            return readNumber(prompt, min, max, Long.class);
        }
    }
