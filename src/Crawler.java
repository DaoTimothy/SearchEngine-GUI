import java.util.*;
import java.io.*;
public class Crawler {
    private File directory;

    public Crawler(String directoryName) {
        directory = new File(directoryName);
    }

    public int crawl(String seed) {
        Queue<String> pageQueue = new LinkedList<>();
        HashMap<String, Integer> enqueuedPages = new HashMap<>();
        ArrayList<Page> readPages = new ArrayList<>();
        pageQueue.add(seed);
        while (pageQueue.size() > 0) {
            Page currentPage = new Page(pageQueue.poll());
            readPages.add(currentPage);
            currentPage.readContents();
            System.out.println("At page " + currentPage.getTitle());
            enqueuedPages.put(currentPage.getUrl(), 0);
            ArrayList<String> outgoingLinks = currentPage.getOutgoingLinks();
            for (String link : outgoingLinks) {
                if (!enqueuedPages.containsKey(link)) {
                    pageQueue.add(link);
                    enqueuedPages.put(link, 0);
                }
            }
        }
        for (Page p : readPages) {
            p.computeContents();
            p.saveContents(directory);
        }
        Page.saveIDF(directory);
        return Page.getTotalPages();
    }

    public void resetData() {
        if (directory.exists()) {
            deleteDirectory(directory);
        }
        directory.mkdir();
    }

    public void deleteDirectory(File file) {
        for (File subFile : file.listFiles()) {
            if (subFile.isDirectory()) {
                deleteDirectory(subFile);
            }
            subFile.delete();
        }
    }

    public File getDirectory() {
        return directory;
    }
    //for testing purposes, DELETE BEFORE SUBMITTING
    public static void main(String[] args) {
        Crawler c = new Crawler("PageResults");
        c.resetData();
        System.out.println(c.crawl("http://people.scs.carleton.ca/~davidmckenney/tinyfruits/N-0.html"));
    }
}
