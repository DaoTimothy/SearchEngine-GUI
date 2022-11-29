import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Project implements ProjectTester{
    Crawler c;
    /*
    This method must delete any existing data that has been stored from any previous crawl.
    This method should also perform any other initialization needed by your system.
    This method will be always called before executing the crawl for a new dataset
     */
    public void initialize() {
        c = new Crawler("PageResults");
        c.resetData();
    }

    /*
    This method performs a crawl starting at the given seed URL.
    It should visit each page it can find once.
    It should not stop until it has visited all reachable pages.
    All data required for later search queries should be saved in files once this completes.
     */
    public void crawl(String seedURL) {
        c.crawl(seedURL);
    }

    /*
    Returns a list of the outgoing links of the page with the given URL.
    That is, the URLs that the page with the given URL links to.
    If no page with the given URL exists, returns null.
     */
    public List<String> getOutgoingLinks(String url) {
        String cleanUrl = url.replace(":", "{").replace("/", "}");
        File outFolder = new File(c.getDirectory() + File.separator + cleanUrl + File.separator + "out");
        File[] fileArray = outFolder.listFiles();
        ArrayList<String> result = new ArrayList<>();
        for (File file : fileArray) {
            result.add(file.toString().replace("{", ":").replace("}", "/"));
        }
        return result;
    }

    /*
    Returns a list of the incoming links for the page with the given URL.
    That is, the URLs that link to the page with the given URL
    If no page with the given URL exists, returns null.
     */
    public List<String> getIncomingLinks(String url) {
        String cleanUrl = url.replace(":", "{").replace("/", "}");
        File inFolder = new File(c.getDirectory() + File.separator + cleanUrl + File.separator + "in");
        File[] fileArray = inFolder.listFiles();
        ArrayList<String> result = new ArrayList<>();
        for (File file : fileArray) {
            result.add(file.toString().replace("{", ":").replace("}", "/"));
        }
        return result;
    }

    /*
    Returns the PageRank value for the page with the given URL.
    If no page with the given URL exists, returns -1.
     */
    public double getPageRank(String url) {
        String cleanUrl = url.replace(":", "{").replace("/", "}");
        File pageRankFile = new File(c.getDirectory().toString() + File.separator + cleanUrl + File.separator + "pagerank");
        try {
            Scanner scan = new Scanner(pageRankFile);
            return Double.parseDouble(scan.nextLine());
        } catch (IOException e){
        }
        return 0.0;
    }

    /*
    Returns the IDF value for the given word.
    A word that did not show up during the crawl should have an IDF of 0.
     */
    public double getIDF(String word) {
        File idfFile = new File(c.getDirectory().toString() + File.separator + "idf folder" + File.separator + word);
        try {
            Scanner scan = new Scanner(idfFile);
            return Double.parseDouble(scan.nextLine());
        } catch (IOException e){
        }
        return 0.0;
    }

    /*
    Returns the term frequency of the given word within the page with the given URL.
    If the word did not appear on the given page, the TF should be 0.
     */
    public double getTF(String url, String word) {
        String cleanUrl = url.replace(":", "{").replace("/", "}");
        File tfFile = new File(c.getDirectory().toString() + File.separator + cleanUrl + File.separator + "tf" + File.separator + word);
        try {
            Scanner scan = new Scanner(tfFile);
            return Double.parseDouble(scan.nextLine());
        } catch (IOException e){
        }
        return 0.0;
    }

    /*
    Returns the TF-IDF value of the given word within the page with the given URL.
     */
    public double getTFIDF(String url, String word) {
        String cleanUrl = url.replace(":", "{").replace("/", "}");
        File tfidfFile = new File(c.getDirectory().toString() + File.separator + cleanUrl + File.separator + "tfidf" + File.separator + word);
        try {
            Scanner scan = new Scanner(tfidfFile);
            return Double.parseDouble(scan.nextLine());
        } catch (IOException e){
        }
        return 0.0;
    }

    /*
    Performs a search using the given query.
    If boost is true, the search score for a page should be boosted by the page's PageRank value.
    If boost is false, the search score for a page will be only based on cosine similarity.
    This method must return a list of objects that implement the SearchResult interface.
    The list should return the top X search results for the given query/boost values.
    Results should be sorted from highest score to lowest.
    If two search results have the same score when rounded to 3 decimal places,
    the scores for those two results should be considered identical and their
    lexicographical ordering (this is what Java's String compareTo() method uses)
    should be used to determine which goes before the other.
    A copy of this interface is included on the project's BrightSpace page.
     */
    public List<SearchResult> search(String query, boolean boost, int X) {
        return null;
    }
}
