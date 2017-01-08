package multithread;

/**
 * Created by Haochen on 2017/1/7.
 */
public class MyThread extends Thread {
    private boolean enable;

    public void setEnable(boolean enable) {
        this.enable = enable;
        if (enable) {
            synchronized (Common.queue) {
                Common.queue.notifyAll();
            }
        }
    }

    public boolean isEnable() {
        return enable;
    }

    @Override
    public void run() {
        synchronized (Common.queue) {
            try {
                while (true) {
                    while (!enable || Common.queue.isEmpty()) {
                        Common.queue.wait();
                    }
                    int head = Common.queue.poll();
                    System.out.println("[get a number]: " + head);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
