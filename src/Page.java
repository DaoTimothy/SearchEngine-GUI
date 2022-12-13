import java.net.MalformedURLException;
import java.util.*;
import java.io.*;
public class Page {
    private static HashMap<String, ArrayList<String>> allIncomingLinks = new HashMap<>();
    private static HashMap<String, Integer> allWordsFreq = new HashMap<>();
    private static HashMap<String, Double> idfHashMap = new HashMap<>();
    private static HashMap<Integer, String> idMap = new HashMap<>();
    private static ArrayList<ArrayList<Double>> pageRankVector = new ArrayList<>();
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

    public Page(String url) {
        this.url = url;
        title = "";
        wordFreq = new HashMap<>();
        wordCount = 0;
        outgoingLinks = new ArrayList<>();
        incomingLinks = new ArrayList<>();
        tfHashMap = new HashMap<>();
        tfidfHashMap = new HashMap<>();
        pageRank = 0;
    }

    public Page(String title, Double pageRank) {
        this.title = title;
        this.pageRank = pageRank;
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
        String[] wordsList = rawText.replace("\n", " ").strip().split(" ");

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
            idfHashMap.put(word, Math.log((double)totalPages / (1+allWordsFreq.get(word)))/Math.log(2));
        }

        for (String word : tfHashMap.keySet()) {
            tfidfHashMap.put(word, Math.log(tfHashMap.get(word)+1)/Math.log(2) * idfHashMap.get(word));
        }
    }

    public static void computePageRanks() {
        createIdMap();
        calculateFinalVector();
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

    public static void saveIDFandPageRank(File directory) {
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

        try{
            for(int i=0;i<pageRankVector.get(0).size();i++){
                double rank= pageRankVector.get(0).get(i);
                String url= idMap.get(i).replace(":", "{").replace("/", "}");
                //System.out.println(directory.getPath() + File.separator + url+ File.separator +"pagerank.txt");
                PrintWriter outy = new PrintWriter(new FileWriter(directory.toString() + File.separator + url+ File.separator +"pagerank"));
                outy.print(rank);
                outy.close();

            }
        }
        catch (IOException e) {

        }
    }

    public static void createIdMap(){
        HashMap<Integer,String> map = new HashMap<Integer,String>();
        int count = 0;

        for(String key : allIncomingLinks.keySet()){
            map.put(count,key);
            count++;
        }
        idMap = map;
    }

    private static ArrayList<ArrayList<Double>>createMatrix(){
        ArrayList<ArrayList<Double>> matrix = new ArrayList<ArrayList<Double>>();
        double alpha = 0.1;
        int yesCount = 0;
        int notCount = 0;
        for(Integer key: idMap.keySet()){
            ArrayList<Double> row = new ArrayList<Double>();
            //System.out.println(idmap.get(key));
            String sidepage = idMap.get(key);
            for(int toprow = 0; toprow < totalPages; toprow++){
                String toppage = idMap.get(toprow);
                if(allIncomingLinks.get(toppage).contains(sidepage)){
                    row.add(1.0);
                    yesCount++;
                    notCount = 0;
                }
                else{
                    row.add(0.0);
                    notCount++;
                    if(notCount == totalPages){
                        row = new ArrayList<Double>();
                        for(int i = 0; i < totalPages; i++){
                            row.add(1.0/(double)totalPages);
                        }
                    }
                }
            }
            for(int i = 0; i < totalPages; i++){
                if(row.get(i) == 1.0){
                    row.set(i, 1.0/(double)yesCount);
                }
                row.set(i, ((double)row.get(i)*(1.0-alpha)) + (alpha/(double)totalPages));
            }
            yesCount = 0;
            notCount = 0;
            matrix.add(row);
        }
        return matrix;
    }

    private static void calculateFinalVector(){
        double threshhold = 0.0001;
        double dist = 1;
        pageRankVector = new ArrayList<ArrayList<Double>> ();
        ArrayList<ArrayList<Double>> matrix = new ArrayList<ArrayList<Double>>(createMatrix());
        ArrayList<ArrayList<Double>> oldVector= new ArrayList<ArrayList<Double>>();
        ArrayList<Double> temp = new ArrayList<Double>();
        for(int i=0;i<matrix.size();i++){
            temp.add(i,0.0);
        }
        oldVector.add(new ArrayList<>(temp));
        oldVector.get(0).set(1,1.0);
        pageRankVector.add(new ArrayList<>(dotProduct(oldVector)));
        // System.out.println(oldVector);
        // System.out.println(newVector);
        while(dist>threshhold){
            pageRankVector.set(0,new ArrayList<Double>(dotProduct(pageRankVector)));

            dist = euclidean_dist(pageRankVector,oldVector);
            oldVector = new ArrayList<ArrayList<Double>>(pageRankVector);
        }
    }
    private static double euclidean_dist(ArrayList<ArrayList<Double>> a, ArrayList<ArrayList<Double>> b){
        double sum = 0;
        for(int i = 0; i < a.get(0).size(); i++){
            sum += Math.pow((a.get(0).get(i)-b.get(0).get(i)), 2.0);
        }
        return (double) Math.sqrt(sum);
    }
    private static ArrayList<Double> dotProduct(ArrayList<ArrayList<Double>> vector){
        ArrayList<Double> newVector= new ArrayList<Double> ();
        ArrayList<ArrayList<Double>> matrix = createMatrix();
        double sum;
        int index;
        //System.out.println(vector);
        for(int column = 0; column<vector.get(0).size();column++){
            sum = 0.0;
            index=0;

            for(ArrayList<Double>row:matrix){
                sum+=(row.get(column))*(vector.get(0).get(index));

                //System.out.println(vector.get(0));
                //System.out.println(sum);
                index++;
            }
            //System.out.println(sum);
            newVector.add(sum);
        }

        return newVector;
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
    public double getPageRank() {  return pageRank; }

    public String toString(){
        return "Page: " + url;
    }


    public static ArrayList<ArrayList<Double>> getPageRankVector() {
        return pageRankVector;
    }
}
