package com.datavle.api.demo.concurrency.webcrawler;

import com.datavle.htmlparser.*;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * User: jeyanthan
 * Date: 2019-02-18
 * Time: 11:53
 * Add description here
 */
public class LinkFinder implements Runnable {

    private String url;
    private LinkHandler linkHandler;
    private static final long t0 = System.nanoTime();

    public LinkFinder(String url, LinkHandler linkHandler) {
        this.url = url;
        this.linkHandler = linkHandler;
    }

    @Override
    public void run() {
        getSimpleLinks(url);
    }

    private void getSimpleLinks(String url) {
        if (!linkHandler.visited(url)) {
            try {

                List<String> links = parse("-l");
                System.out.println(" visited url " + url);

                linkHandler.addVisited(url);

                if (linkHandler.size() == 100) {
                    System.out.println("time to visit 100 links" + (System.nanoTime() - t0));
                }

                for (String link : links) {
                    //System.out.println(" link queued " + link);
                    linkHandler.queueLink(link);
                }

            } catch (Exception e) {
                //Ignore errors for now
                e.printStackTrace();
            }
        }

    }

    /**
     * Parse the given resource, using the filter provided
     */
    public List<String> parse(String filter) {
        new HTMLLinkScanner("-l");
        //new HTMLImageScanner("-i");
        HTMLNode node;
        HTMLParser htmlParser = new HTMLParser(url);
        List<String> links = new ArrayList<>();

        //System.out.println("I am here in parse " + url);

        for (Enumeration e = htmlParser.elements(); e.hasMoreElements(); ) {
            node = (HTMLNode) e.nextElement();

            //System.out.println(" before node null check ");

            System.out.println(" I am outside ");
            //node.print();

            if (node != null) {
                //node.print();
                if (filter == null)
                    node.print();
                else {
                    // There is a filter. Find if the associated filter of this node
                    // matches the specified filter
                    //System.out.println("I am here in parse just before filter ");
                    //node.print();
                    if (!(node instanceof HTMLTag)) continue;
                    HTMLTag tag = (HTMLTag) node;
                    tag.setThisScanner(new HTMLLinkScanner());
                    HTMLTagScanner scanner = tag.getThisScanner();
                    System.out.println(" scanner " + scanner.getFilter());
                    if (scanner == null) continue;
                    String tagFilter = scanner.getFilter();
                    if (tagFilter == null) continue;
                    if (tagFilter.equals(filter)) {
                        System.out.println("I am inside the link filter " + ((HTMLTag) node).getText());
                        HTMLLinkNode linkNode = null;
                        if (scanner.evaluate(tag.getText())) {
                            System.out.println(" yes linknode " + linkNode);
                            //linkNode = (HTMLLinkNode) scanner.scan(tag, url, htmlParser.getReader());
                        }

                        try {
                            if (linkNode != null && !linkNode.getLink().isEmpty() && !linkHandler.visited(linkNode.getLink())) {
                                System.out.println("finallyyyy " + linkNode.getLink());
                                links.add(linkNode.getLink());
                            }
                        } catch (Exception e1) {
                            e1.printStackTrace();
                            System.out.println("Exception while queuing " + linkNode.getLink());
                        }
                    }

                }
            } else System.out.println("Node is null");
        }

        return links;

    }

}