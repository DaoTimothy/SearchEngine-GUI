import java.net.MalformedURLException;
import java.util.*;
import java.io.*;
public class Page {
    private static HashMap<String, ArrayList<String>> allIncomingLinks = new HashMap<>();
    private static HashMap<String, Integer> allWordsFreq = new HashMap<>();
    private static HashMap<String, Double> idfHashMap = new HashMap<>();
    private static ArrayList<Double> pageRanks = new ArrayList<>();
    private static int totalPages = 0;
    private String url;
    private String title;
    private HashMap<String, Integer> wordFreq;
    private int wordCount;
    private ArrayList<String> outgoingLinks;
    private ArrayList<String> incomingLinks;
    private HashMap<String, Double> tfHashMap;
    private HashMap<String, Double> tfidfHashMap;
    private double pageRank;

    public Page() {

    }
    public Page(String url) {
        this.url = url;
        title = "";
        wordFreq = new HashMap<>();
        wordCount = 0;
        outgoingLinks = new ArrayList<>();
        incomingLinks = new ArrayList<>();
        tfHashMap = new HashMap<>();
        tfidfHashMap = new HashMap<>();
        pageRank = 0.0;
    }

    public Page(Page page) {
        title = page.title;
    }

    public void readContents() {
        String[] contents = new String[0];
        try {
            contents = WebRequester.readURL(url).split("<");
        }catch(MalformedURLException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        }
        String rawText = "";
        for (String element : contents) {
            if(element.length() == 0) {
                continue;
            } else if ((element.length() > 6) && (element.substring(0, 6).equals("title>") || element.substring(0, 6).equals("title "))) {
                title = element.substring(element.indexOf(">")+1, element.length());
            } else if ((element.length() > 2) && (element.substring(0, 2).equals("p ") || element.substring(0, 2).equals("p>"))) {
                rawText += element.substring(element.indexOf(">")+1, element.length()) + " ";
            } else if (element.contains("href=\"")) {
                String link = "";
                for (int letter = element.indexOf("href=\"")+6; letter < element.length(); letter++) {
                    if ((element.substring(letter, letter+1).equals("\""))) {
                        break;
                    }
                    link += element.substring(letter, letter+1);
                }
                outgoingLinks.add(buildLink(link));
            }
        }
        String[] wordsList = rawText.replace("\n", " ").split(" ");
        for (String word : wordsList) {
            wordCount++;
            if (!wordFreq.containsKey(word)) {
                wordFreq.put(word, 1);
            } else {
                wordFreq.put(word, wordFreq.get(word)+1);
            }
        }

        for (String word : wordFreq.keySet()) {
            if (!allWordsFreq.containsKey(word)) {
                allWordsFreq.put(word, 1);
            } else {
                allWordsFreq.put(word, allWordsFreq.get(word)+1);
            }
            tfHashMap.put(word, (double)wordFreq.get(word)/wordCount);
        }

        for(String link : outgoingLinks) {
            if (!allIncomingLinks.containsKey(link)) {
                ArrayList<String> temp = new ArrayList<>();
                temp.add(url);
                allIncomingLinks.put(link, temp);
            } else {
                ArrayList<String> temp = allIncomingLinks.get(link);
                temp.add(url);
                allIncomingLinks.put(link, temp);
            }
        }
        totalPages++;
    }

    public String buildLink(String link) {
        if (link.substring(0, 1).equals(".")) {
            return url.substring(0, url.lastIndexOf("/")) + link.substring(1);
        }
        return link;
    }

    public void computeContents() {
        incomingLinks = allIncomingLinks.get(url);
        for (String word : allWordsFreq.keySet()) {
            idfHashMap.put(word, (double)totalPages / (1+allWordsFreq.get(word)));
        }

        for (String word : tfHashMap.keySet()) {
            tfidfHashMap.put(word, Math.log(tfHashMap.get(word)+1)/Math.log(2) * idfHashMap.get(word));
        }

    }

    public void saveContents(File directory) {
        String cleanUrl = url.replace(":", "{").replace("/", "}");
        File thisPage = new File(directory.toString() + File.separator + cleanUrl);
        thisPage.mkdir();
        File tfFolder = new File(thisPage.toString() + File.separator + "tf");
        tfFolder.mkdir();
        File tfidfFolder = new File(thisPage.toString() + File.separator + "tfidf");
        tfidfFolder.mkdir();
        File outFolder = new File(thisPage.toString() + File.separator + "out");
        outFolder.mkdir();
        File inFolder = new File(thisPage.toString() + File.separator + "in");
        inFolder.mkdir();
        File titleFile = new File(thisPage.toString() + File.separator + "title");
        File pageRankFile = new File(thisPage.toString() + File.separator + "pagerank");
        try {
            FileWriter writer = new FileWriter(titleFile);
            writer.write(title);
            writer.close();
            writer = new FileWriter(pageRankFile);
            writer.write(String.valueOf(pageRank));
            writer.close();
        } catch (IOException e) {

        }
        for (String word : tfHashMap.keySet()) {
            File thisWord = new File(tfFolder.toString() + File.separator + word);
            try {
                FileWriter writer = new FileWriter(thisWord.toString());
                writer.write(tfHashMap.get(word).toString());
                writer.close();
            } catch (IOException e) {

            }
        }
        for (String word : tfidfHashMap.keySet()) {
            File thisWord = new File(tfidfFolder.toString() + File.separator + word);
            try {
                FileWriter writer = new FileWriter(thisWord.toString());
                writer.write(tfidfHashMap.get(word).toString());
                writer.close();
            } catch (IOException e) {

            }
        }

        for (String link : outgoingLinks) {
            String temp = link.replace(":", "{").replace("/", "}");
            File thisLink = new File(outFolder.toString() + File.separator + temp);
            try {
                FileWriter writer = new FileWriter(thisLink.toString());
                writer.write("");
            } catch (IOException e) {

            }
        }

        for (String link : incomingLinks) {
            String temp = link.replace(":", "{").replace("/", "}");
            File thisLink = new File(inFolder.toString() + File.separator + temp);
            try {
                FileWriter writer = new FileWriter(thisLink.toString());
                writer.write("");
            } catch (IOException e) {

            }
        }
    }

    public static void saveIDF(File directory) {
        File idfFolder = new File(directory.toString() + File.separator + "idf folder");
        idfFolder.mkdir();
        for (String word : idfHashMap.keySet()) {
            File thisWord = new File(idfFolder.toString() + File.separator + word);
            try {
                FileWriter writer = new FileWriter(thisWord);
                writer.write(idfHashMap.get(word).toString());
                writer.close();
            } catch (IOException e) {

            }
        }
    }

    public ArrayList<String> getOutgoingLinks() {
        return outgoingLinks;
    }

    public String getTitle() {
        return title;
    }

    public static int getTotalPages() {
        return totalPages;
    }

    public String getUrl() {
        return url;
    }

    public String toString(){
        return url;
    }
}
