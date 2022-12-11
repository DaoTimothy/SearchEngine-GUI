import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Search extends SearchData{
    public Search(String path) {
        super(path);
    }

    public ArrayList<SearchResult> search(String phrase, boolean boost, int X) {
        int totalWords = 0;
        HashMap<String, Integer> wordFreq = new HashMap<>();
        String[] words = phrase.split(" ");
        for (String word : words) {
            totalWords++;
            if (!wordFreq.containsKey(word)) {
                wordFreq.put(word, 1);
            } else {
                wordFreq.put(word, wordFreq.get(word)+1);
            }
        }
        HashMap<String, Double> tfHashMap = new HashMap<>();
        HashMap<String, Double> tfidfHashmap = new HashMap<>();
        for (String word : wordFreq.keySet()) {
            tfHashMap.put(word, (double)wordFreq.get(word)/totalWords);
            tfidfHashmap.put(word, Math.log(tfHashMap.get(word)+1)/Math.log(2) * getIDF(word));
        }
        ArrayList<String> wordOrder = new ArrayList<>();
        ArrayList<Double> queryVector = new ArrayList<>();
        for (String word : tfidfHashmap.keySet()) {
            wordOrder.add(word);
            queryVector.add(tfidfHashmap.get(word));
        }

        File[] allPages = getDirectory().listFiles();

        ArrayList<SearchResult> results = new ArrayList<>();
        for (int i = 0; i < X; i++) {
            Result blank = new Result(-1);
            results.add((SearchResult)blank);
        }
        for (File fileUrl : allPages) {
            if(fileUrl.toString().contains(" ")) {
                continue;
            }
            File titleFile = new File(fileUrl.toString() + File.separator + "title");
            String title = "";
            try {
                Scanner s = new Scanner(titleFile);
                title = s.nextLine();
            } catch (IOException e) {

            }
            String url = fileUrl.toString().replace(getDirectory().toString(), "").replace("{",":").replace("}","/");
            Result pageResult = new Result(title, getPageRank(url));

            for (String word : wordOrder) {
                pageResult.appendVector(getTFIDF(url, word));
            }
            pageResult.computeScore(boost, queryVector);
            for (int i = 0; i < results.size(); i++) {
                if (round(pageResult.getScore()) > round(results.get(i).getScore())) {
                    results.add(i, (SearchResult) pageResult);
                    results.remove(X);
                    break;
                } else if (round(pageResult.getScore()) == round(results.get(i).getScore()) && pageResult.getTitle().compareTo(results.get(i).getTitle()) <= 0) {
                    results.add(i, (SearchResult) pageResult);
                    results.remove(X);
                    break;
                }
            }
        }
        return results;
    }

    public double round(Double d) {
        return Math.round(d*1000.0)/1000.0;
    }
}
