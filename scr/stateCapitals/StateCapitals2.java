package stateCapitals;

import java.util.*;

public class StateCapitals2 {
    public static void main(String[] args) {
        GameInit.gameStart();
    }

    record gameConfig(int play, List<String> answered) {
    }

}
