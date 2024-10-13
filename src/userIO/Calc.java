package userIO;

import java.util.ArrayList;
import java.util.List;

public class Calc {
    public static void main(String[] args) {
        List<Runnable> tasks = getRunnable();
        threadsRun(tasks);
    }

    private static void threadsRun(List<Runnable> tasks) {
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < tasks.size(); i++) {
            final int index = i;
            Thread thread = new Thread(() -> {
                synchronized (tasks) {
                    tasks.get(index).run();
                }
            });
            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static List<Runnable> getRunnable() {
        UserIOImpl userIO = new UserIOImpl();
        List<Runnable> tasks = new ArrayList<>();

        tasks.add(() -> userIO.readString("Please enter a string: "));
        tasks.add(() -> userIO.readInt("Please enter an integer:"));
        tasks.add(() -> userIO.readInt("Please enter an integer between 0 and 100: ", 0, 100));
        tasks.add(() -> userIO.readDouble("Please enter a double: "));
        tasks.add(() -> userIO.readDouble("Please enter a double between 100 and 200: ", 100.0, 200.0));
        tasks.add(() -> userIO.readFloat("Please enter a float: "));
        tasks.add(() -> userIO.readFloat("Please enter a float between 200 and 300: ", 200f, 300f));
        tasks.add(() -> userIO.readLong("Please enter a long: "));
        tasks.add(() -> userIO.readLong("Please enter a long between 300 and 400: ", 300L, 400L));

        return tasks;
    }
}
