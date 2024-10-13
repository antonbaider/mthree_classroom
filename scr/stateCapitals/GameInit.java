package stateCapitals;

import java.util.*;

public class GameInit {
    static int gameInit(int play, List<String> states, Random r, List<String> answered, Scanner sc, Map<String, String> listName, int wins) {
        while (play > 0) {
            String randomState = states.get(r.nextInt(states.size()));

            if (answered.contains(randomState)) continue;

            System.out.println("\nWHAT IS THE CAPITAL OF " + "'" + randomState + "'?");
            String answer = sc.nextLine();

            if (answer.equalsIgnoreCase(listName.get(randomState))) {
                System.out.println("NICE WORK! " + answer.toUpperCase() + " IS CORRECT!");
                wins++;
            } else {
                System.out.println("SORRY, THE CAPITAL OF " + randomState + " IS " + listName.get(randomState) + ".");
                wins--;
                if (wins < 0) wins = 0;
            }
            play--;
            answered.add(randomState);
        }
        return wins;
    }

    static StateCapitals2.gameConfig getGameConfig(Scanner sc) {
        resultScore("READY TO TEST YOUR KNOWLEDGE?\nHow many do you want to play?");

        int play = 0;

        while (play <= 0) {
            if (sc.hasNextInt()) {
                play = sc.nextInt();
                if (play <= 0) {
                    resultScore("Please enter a positive number greater than 0.");
                }
            } else {
                resultScore("Invalid input. Please enter a valid number.");

            }
            sc.nextLine();
        }

        List<String> answered = new ArrayList<>();
        StateCapitals2.gameConfig result = new StateCapitals2.gameConfig(play, answered);
        return result;
    }

    static void gameStart() {
        Random r = new Random();
        Scanner sc = new Scanner(System.in);
        Map<String, String> listName = FileLoad.fileInit();

        List<String> states = FileLoad.getInfo(listName);

        StateCapitals2.gameConfig result = getGameConfig(sc);
        int wins = 0;

        wins = gameInit(result.play(), states, r, result.answered(), sc, listName, wins);

        resultScore("==========================\nScore is: \n" + wins + " WINS");
    }

    static void resultScore(String wins) {
        System.out.println(wins);
    }
}
