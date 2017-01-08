package multithread;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Haochen on 2017/1/7.
 */
public class MultiThread {
    public static void main(String[] args) {
        MyThread thread = new MyThread();
        thread.start();
        String ins;
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.print(">>>>");
            ins = scanner.nextLine();
            switch (ins) {
                case "queue clear":
                    Common.queue.clear();
                    System.out.println("[done]: clear");
                    continue;
                case "thread start":
                    thread.setEnable(true);
                    System.out.println("[done]: enable = " + thread.isEnable());
                    continue;
                case "thread stop":
                    thread.setEnable(false);
                    System.out.println("[done]: enable = " + thread.isEnable());
                    continue;
                case "thread close":
                    thread.interrupt();
                    System.out.println("[done]: thread is close");
                    continue;
            }
            if (ins.startsWith("queue offer parallel ")) {
                String[] sub = ins.substring(21).replaceFirst("[\\s]*", "").split("[\\s]+");
                List<Thread> threads = new ArrayList<>();
                for (String s : sub) {
                    threads.add(new Thread(() -> {
                        synchronized (Common.queue) {
                            Common.queue.offer(Integer.parseInt(s));
                            Common.queue.notifyAll();
                        }
                    }));
                }
                for (Thread t : threads) {
                    t.start();
                }
                System.out.println("[done]: offer " + sub.length + " number(s)");
            } else if (ins.startsWith("queue offer ")) {
                String[] sub = ins.substring(12).replaceFirst("[\\s]*", "").split("[\\s]+");
                synchronized (Common.queue) {
                    for (String s : sub) {
                        Common.queue.offer(Integer.parseInt(s));
                        Common.queue.notifyAll();
                    }
                    System.out.println("[done]: offer " + sub.length + " number(s)");
                }
            } else if (ins.equals("queue print")) {
                System.out.println("[queue]: size = " + Common.queue.size());
                System.out.print("[data]: ");
                for (int i : Common.queue) {
                    System.out.print(i + " ");
                }
                System.out.println();
            } else {
                System.out.println("[error]");
            }
        } while (!ins.equals("exit"));
        System.out.println("[Exit]");
    }
}
