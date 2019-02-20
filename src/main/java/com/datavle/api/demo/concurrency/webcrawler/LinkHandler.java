package com.datavle.api.demo.concurrency.webcrawler;

/**
 * User: jeyanthan
 * Date: 2019-02-18
 * Time: 10:42
 * Add description here
 */
public interface LinkHandler {

    /**
     * Places the link in the queue
     * @param link
     * @throws Exception
     */
    void queueLink(String link) throws Exception;

    /**
     * Returns the number of visited links
     * @return
     */
    int size();

    /**
     * Checks if the link was already visited
     * @param link
     * @return
     */
    boolean visited(String link);

    /**
     * Adds a link as visited
     * @param link
     */
    void addVisited(String link);

}
