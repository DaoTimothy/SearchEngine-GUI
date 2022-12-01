import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public abstract class SearchData {
    private File directory;

    protected SearchData(String path) {
        directory = new File(path);
    }

    public List<String> getOutgoingLinks(String url) {
        String cleanUrl = url.replace(":", "{").replace("/", "}");
        File outFolder = new File(directory + File.separator + cleanUrl + File.separator + "out");
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
        File inFolder = new File(directory + File.separator + cleanUrl + File.separator + "in");
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
        File pageRankFile = new File(directory.toString() + File.separator + cleanUrl + File.separator + "pagerank");
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
        File idfFile = new File(directory.toString() + File.separator + "idf folder" + File.separator + word);
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
        File tfFile = new File(directory.toString() + File.separator + cleanUrl + File.separator + "tf" + File.separator + word);
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
        File tfidfFile = new File(directory.toString() + File.separator + cleanUrl + File.separator + "tfidf" + File.separator + word);
        try {
            Scanner scan = new Scanner(tfidfFile);
            return Double.parseDouble(scan.nextLine());
        } catch (IOException e){
        }
        return 0.0;
    }

    public File getDirectory() {
        return directory;
    }
}
