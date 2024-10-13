package stateCapitals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class StateCapitalsApp {
    public static void main(String[] args) {

        Map<String, Capital> listCapitals = new HashMap<>();

        File file = new File("scr/stateCapitals/MoreStateCapitals.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while (br.ready()) {
                String line = br.readLine();
                String[] tokens = line.split("::");
                String state = tokens[0];
                String capital = tokens[1];
                int population = Integer.parseInt(tokens[2]);
                double square = Double.parseDouble(tokens[3]);

                Capital capitalList = new Capital(capital, population, square);
                listCapitals.put(state, capitalList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        long count = listCapitals.entrySet().size();
        System.out.println(count + "STATE/CAPITAL PAIRS LOADED.\n==========================");

        for (String key : listCapitals.keySet()) {
            listCapitals.get(key);
            System.out.println(key + " - " + listCapitals.get(key));
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the lower limit for capital city population:");
        int lowerLimit = scanner.nextInt();

        System.out.println("LISTING CAPITALS WITH POPULATIONS GREATER THAN " + lowerLimit);
        for (String key : listCapitals.keySet()) {
            Capital result = listCapitals.get(key);
            if (result.getPopulation() >= lowerLimit) {
                System.out.println(key + " - " + listCapitals.get(key));
            }
        }

        System.out.println("Please enter the upper limit for capital city sq mileage:");
        int upperLimit = scanner.nextInt();
        scanner.close();

        System.out.println("LISTING CAPITALS WITH AREAS LESS THAN " + upperLimit);
        for (String key : listCapitals.keySet()) {
            Capital result = listCapitals.get(key);
            if (result.getMileage() <= upperLimit) {
                System.out.println(key + " - " + listCapitals.get(key));
            }
        }
    }
}
