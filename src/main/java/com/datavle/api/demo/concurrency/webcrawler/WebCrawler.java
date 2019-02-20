package com.datavle.api.demo.concurrency.webcrawler;

import com.kizna.html.HTMLImageScanner;
import com.kizna.html.HTMLLinkScanner;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * User: jeyanthan
 * Date: 2019-02-18
 * Time: 10:50
 * Add description here
 */
public class WebCrawler implements LinkHandler {

    private final Collection<String> visitedLinks = Collections.synchronizedSet(new HashSet<>());
    private String url;
    private ExecutorService executorService;

    public WebCrawler(String startUrl, int maxThreads) {
        url = startUrl;
        executorService = Executors.newFixedThreadPool(maxThreads);
    }

    @Override
    public void queueLink(String link) throws Exception {
        startNewThread(link);
    }

    @Override
    public int size() {
        return visitedLinks.size();
    }

    @Override
    public boolean visited(String link) {
        return visitedLinks.contains(link);
    }

    @Override
    public void addVisited(String link) {
        visitedLinks.add(link);

    }

    private void startNewThread(String link) throws Exception{
        executorService.submit(new LinkFinder(link, this), this);

    }

    public void startCrawling() throws Exception{
        startNewThread(url);
    }

}
