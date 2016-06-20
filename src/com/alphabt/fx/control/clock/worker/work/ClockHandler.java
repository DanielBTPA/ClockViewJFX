package com.alphabt.fx.control.clock.worker.work;

import com.alphabt.fx.control.clock.util.Executable;
import javafx.application.Platform;

import java.util.*;

/**
 * Created by danielbt on 25/05/16.
 */
public class ClockHandler {

    private static ThreadOperation threadOperation;

    protected ClockHandler() {
        if (threadOperation == null) {
            threadOperation = new ThreadOperation("Clock Operation");
            threadOperation.setDaemon(true);
            threadOperation.start();
        }
    }

    protected void post(Executable run) {
        threadOperation.post(run);
    }

    protected void remove(Executable removeRun) {
        threadOperation.remove(removeRun);
    }

    private static class ThreadOperation extends Thread {

        private List<Executable> listTask;
        private Deque<Executable> queue;
        private boolean remove;

        static final long TIME_SLEEP = 1000 / 55;

        ThreadOperation(String name) {
            super(name);

            listTask = new ArrayList<>();
            queue = new ArrayDeque<>();
        }


        @Override
        public void run() {
            while (!threadOperation.isInterrupted()) {
                long startMills = System.currentTimeMillis();

                Executable run = null;

                if (!queue.isEmpty()) {
                    run = queue.pollFirst();
                }

                if (run != null && !listTask.contains(run) && !remove) {
                    listTask.add(run);
                } else if (remove) {
                    listTask.remove(run);
                    remove = false;
                }

                Collections.shuffle(listTask);

                listTask.forEach(Executable::execute);

                long endMills = System.currentTimeMillis();

                long totalMills = TIME_SLEEP - (endMills - startMills);

                if (totalMills > 0) {
                    sleepThread(totalMills);
                }
            }
        }

        synchronized void post(Executable run) {
            queue.add(run);
        }

        synchronized void remove(Executable run) {
            queue.add(run);
            remove = true;
        }

        void sleepThread(long mills) {
            try {
                sleep(mills);
            } catch (Exception e) {
            }
        }
    }
}