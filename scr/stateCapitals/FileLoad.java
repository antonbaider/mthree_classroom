package stateCapitals;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class FileLoad {
    static Map<String, String> fileInit() {
        Path path = Paths.get("scr/stateCapitals/StateCapitals.txt");

        Map<String, String> listName = new HashMap<>();

        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line;
            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty()) continue;

                String[] tokens = line.split("::");
                String state = tokens[0];
                String capital = tokens[1];
                listName.put(state, capital);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listName;
    }

    static List<String> getInfo(Map<String, String> listName) {
        AtomicInteger count = new AtomicInteger();
        listName.forEach((k, v) -> count.getAndIncrement());
        System.out.println(count.get() + " STATES & CAPITALS ARE LOADED. \n=====================");
        System.out.println("HERE ARE THE STATES: \n");
        listName.forEach((k, v) -> System.out.print(v + ", "));

        List<String> states = new ArrayList<>(listName.keySet());
        return states;
    }
}
