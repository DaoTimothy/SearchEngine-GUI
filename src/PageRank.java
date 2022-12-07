import java.util.*;
import java.lang.Math;
import java.io.*;
public  class PageRank{
    private static HashMap<String, ArrayList<String>>incominglinks=Page.getIncomingLinksDict();

    

    public static HashMap<Integer, String> idmap(){
        HashMap<Integer,String> map= new HashMap<Integer,String>();
        int count = 0;

        for(String key : incominglinks.keySet()){
            map.put(count,key);
            
            count++;
        }
        return map;
    }

    private static ArrayList<ArrayList<Double>>createMatrix(){
        ArrayList<ArrayList<Double>> matrix = new ArrayList<ArrayList<Double>>();
        HashMap<Integer, String> idmap = idmap();
        int totalpages = idmap.size();
        double alpha =0.1;
        int yesCount=0;
        int notCount=0;

        for(Integer key: idmap.keySet()){
            ArrayList<Double> row = new  ArrayList<Double>();
            //System.out.println(idmap.get(key));
            String sidepage = idmap.get(key);

            for(int toprow=0; toprow<totalpages;toprow++){
                String toppage= idmap.get(toprow);

                if(incominglinks.get(toppage).contains(sidepage)){
                    row.add((double)1);
                    yesCount++;
                    notCount=0;
                   
                }
                else{
                    notCount++;
                    row.add((double)0);
                   
                    if(notCount==totalpages){
                        row.clear();
                        for(int i=0;i<totalpages;i++){
                            row.add((double)1/totalpages);
                        }
                    }
                }

            }
            for(int i=0;i<totalpages;i++){
                if(row.get(i)==1){
                    row.set(i, (double)1/yesCount);
                }
                row.set(i, (double)row.get(i)*(1-alpha)+alpha/totalpages);
            }
            yesCount = 0;
            notCount = 0;
            matrix.add(row);
        }




        return matrix;
    }

    private static ArrayList<ArrayList<Double>> finalVector(){
        double threshhold= 0.001;
        double dist =1;
        ArrayList<ArrayList<Double>> newVector= new ArrayList<ArrayList<Double>> ();
        ArrayList<ArrayList<Double>> matrix = createMatrix();
        ArrayList<ArrayList<Double>> oldVector= new ArrayList<ArrayList<Double>>();
        ArrayList<Double> temp= new ArrayList<Double>();
        for(int i=0;i<matrix.size();i++){
            temp.add(i,(double)(0));
        }
        oldVector.add(temp);
        oldVector.get(0).set(0,(double)1);
        newVector.add(dotProduct(oldVector));
        // System.out.println(oldVector);
        // System.out.println(newVector);
        while(dist>threshhold){
            newVector.set(0,dotProduct(newVector));
            
            dist= euclidean_dist(newVector,oldVector);
            oldVector=newVector;
        }
        return newVector;
        

    }
    private static double euclidean_dist(ArrayList<ArrayList<Double>> a, ArrayList<ArrayList<Double>> b){
        double sum=0;
        for(int i=0; i<a.get(0).size();i++){
            sum+=Math.pow((a.get(0).get(i)-b.get(0).get(i)),2);
        }
        return Math.sqrt(sum);
    }
    private static ArrayList<Double> dotProduct(ArrayList<ArrayList<Double>> vector){
        ArrayList<Double> newVector= new ArrayList<Double> ();
        ArrayList<ArrayList<Double>> matrix = createMatrix();
        double sum;
        int index;
        //System.out.println(vector);
        for(int column =0; column<vector.get(0).size();column++){
            sum=0;
            index=0;
            
            for(ArrayList<Double>row:matrix){
                sum+=(row.get(column))*(vector.get(0).get(index));
              
                //System.out.println(vector.get(0));
                //System.out.println(sum);
                index++;
            }
            //System.out.println(sum);
            newVector.add((double)sum);
        }
        
        return newVector;
    }
    private static void saveRank(){
        try{
            for(int i=0;i<finalVector().get(0).size();i++){
                double rank= finalVector().get(0).get(i);
                String url= idmap().get(i).replace(":", "{").replace("/", "}");
                System.out.println(i);
                //System.out.println(directory.getPath() + File.separator + url+ File.separator +"pagerank.txt");
                PrintWriter outy = new PrintWriter(new FileWriter("PageResults" + File.separator + url+ File.separator +"pagerank.txt"));
                outy.print(rank);
                outy.close();
    
            }
        }
        catch (IOException e) {
            System.out.println("Oh fuck");
        }
        
    }

    public static ArrayList<ArrayList<Double>>getPageRank(){
        saveRank();
        return finalVector();
    }
}