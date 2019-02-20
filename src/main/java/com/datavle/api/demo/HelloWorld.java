package com.datavle.api.demo;

import com.datavle.api.demo.concurrency.DemoThread;
import com.datavle.api.demo.concurrency.webcrawler.WebCrawler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorld {

    @RequestMapping("/")
    public String index(){

        return "Hello Datavle v1";

    }

    @RequestMapping("/demo/webcrawler/1")
    public String startCrawling(@RequestParam String url){
        WebCrawler webCrawler = new WebCrawler(url, 64);

        System.out.println(" url in rest controller " + url);

        try {
            webCrawler.startCrawling();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "web crawler started for url " + url + " with 64 threads";
    }

    @RequestMapping("/demo/threads")
    public String runThreadDemo(){
        ThreadGroup tg1 = new ThreadGroup("Group 1");
        ThreadGroup tg2 = new ThreadGroup("Group 2");

        DemoThread demoThread1 = new DemoThread("Thread 1", tg1);
        DemoThread demoThread2 = new DemoThread("Thread 2", tg1);
        DemoThread demoThread3 = new DemoThread("Thread 3", tg2);
        DemoThread demoThread4 = new DemoThread("Thread 4", tg2);

        demoThread1.start();
        demoThread2.start();
        demoThread3.start();
        demoThread4.start();

        //print the thread groups
        tg1.list();
        tg2.list();
        System.out.println(" towards the end of thread group lists");

        Thread[] threadgp1 = new Thread[tg1.activeCount()];

        tg1.enumerate(threadgp1);

        for(Thread t: threadgp1){
            System.out.println(" Suspending group 1 thread " + t);
            DemoThread thread = (DemoThread)t;
            thread.mySuspend();
        }

        try{
            Thread.sleep(4000);
        } catch (InterruptedException e){
            System.out.println(" main thread interrupted ");
        }


        System.out.println(" resuming group 1 threads");

        for(Thread t: threadgp1){
            System.out.println(" resuming group 1 thread " + t);
            DemoThread thread = (DemoThread)t;
            thread.myResume();
        }

        try {
            System.out.println(" waiting for sub threads to finish ");
            demoThread1.join();
            demoThread2.join();
            demoThread3.join();
            demoThread4.join();
        } catch (InterruptedException e) {
            System.out.println(" main thread interrupted ");
        }

        System.out.println(" main thread exiting");

        return " thread demo completed";

    }


}
