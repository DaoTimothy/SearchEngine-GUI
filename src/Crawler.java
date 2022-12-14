import java.util.*;
import java.io.*;
public class Crawler extends FileSystem {


    public Crawler(String directoryName) {
        super(new File(directoryName));
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
            p.saveContents(getDirectory());
        }
        Page.computePageRanks();
        Page.saveIDFandPageRank(getDirectory());
        return Page.getTotalPages();
    }

    public void resetData() {
        if (getDirectory().exists()) {
            deleteDirectory(getDirectory());
        }
        getDirectory().mkdir();
    }

    public void deleteDirectory(File file) {
        for (File subFile : file.listFiles()) {
            if (subFile.isDirectory()) {
                deleteDirectory(subFile);
            }
            subFile.delete();
        }
    }
}
