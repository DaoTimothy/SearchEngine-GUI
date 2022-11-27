import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;
public class Page {
    private static HashMap<String, ArrayList<String>> allIncomingLinks;
    private static HashMap<String, Integer> allWords;
    private static ArrayList<Double> pageRanks;
    private static int totalPages = 0;
    private String url;
    private String title;
    private HashMap<String, Integer> wordFreq;
    private int wordCount;
    private ArrayList<String> outgoingLinks;
    private ArrayList<String> incomingLinks;
    private HashMap<String, Double> tfHashMap;
    private double pageRank;

    public Page(String url) {
        this.url = url;
        totalPages++;
    }

    public void readAndSave() {
        readContents();
        saveContents();
    }
    private void readContents() {
        String[] contents;
        try {
            contents = WebRequester.readURL("http://people.scs.carleton.ca/~davidmckenney/fruits/N-0.html").split(">");
        }catch(MalformedURLException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        }
        String rawText = "";
        for (String element : contents) {
            if(element.length() == 0) {
                continue;
            } else if (element.substring(0, 6).equals("title>") || element.substring(0, 6).equals("title ")) {
                title = element.substring(element.indexOf(">")+1, element.length());
            } else if (element.substring(0, 2).equals("p ") || element.substring(0, 2).equals("p>")) {
                rawText += element.substring(element.indexOf(">")+1, element.length()) + " ";
            } else if (element.contains("href=\"")) {
                String link = "";
                for (int letter = element.indexOf("href\""); letter < element.length(); letter++) {
                    if ((element.substring(letter, letter+1).equals("\""))) {
                        break;
                    }
                    link += element.substring(letter, letter+1);
                }
                outgoingLinks.add(link);
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
            if (!allWords.containsKey(word)) {
                allWords.put(word, 1);
            } else {
                allWords.put(word, allWords.get(word)+1);
            }
            tfHashMap.put(word, (double)wordFreq.get(word)/wordCount);
        }
        


    }

    private void saveContents() {

    }

}
