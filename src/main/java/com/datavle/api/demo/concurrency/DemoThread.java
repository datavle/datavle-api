package com.datavle.api.demo.concurrency;

import java.util.ArrayList;
import java.util.List;

public class DemoThread extends Thread{

    private boolean isSuspendFlag;

    public DemoThread(String threadName, ThreadGroup threadGroup){
        super(threadGroup, threadName);
        System.out.println("Thread created :" + this);
        isSuspendFlag = false;
    }

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            System.out.println(getName() + " processing " + i);

            try {
                Thread.sleep(1000);
                synchronized (this){
                    while (isSuspendFlag){
                        wait();
                    }

                }

            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println(" exception in " + getName());
            }

        }

        System.out.println(" Exiting " + getName());

    }

    public synchronized void mySuspend(){
        isSuspendFlag = true;
    }

    public synchronized void myResume() {
        isSuspendFlag = false;
        notify();
    }

}
