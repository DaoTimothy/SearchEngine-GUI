import java.util.ArrayList;
import java.io.*;
public class Result extends Page implements SearchResult {
    private double score;
    private ArrayList<Double> vector;

    public Result(String title, double pageRank) {
        super(title, pageRank);
        vector = new ArrayList<>();
    }

    //Used to create placeholder result objects
    public Result(double score) {
        super("", 0.0);
        this.score = score;
    }

    public void appendVector(double tfidf) {
        vector.add(tfidf);
    }

    public void computeScore(Boolean boost, ArrayList<Double> queryVector) {
        score = computeCosineSimilarity(queryVector);
        if (boost) {
            score *= getPageRank();
        }
    }
    private double computeCosineSimilarity(ArrayList<Double> queryVector) {
        double numerator = dotProduct(queryVector, vector);
        double queryEuclideanNorm = euclideanNorm(queryVector);
        double documentEuclideanNorm = euclideanNorm(vector);

        if (documentEuclideanNorm == 0.0 || queryEuclideanNorm == 0.0) {
            return 0;
        }
        return (double) numerator / (queryEuclideanNorm * documentEuclideanNorm);
    }

    private double euclideanNorm(ArrayList<Double> vector) {
        double sum = 0;
        for (Double num : vector) {
            sum += Math.pow(num, 2);
        }
        return Math.sqrt(sum);
    }

    private double dotProduct(ArrayList<Double> a, ArrayList<Double> b) {
        double sum = 0;
        for (int i = 0; i < a.size(); i++) {
            sum += a.get(i) * b.get(i);
        }
        return sum;
    }

    @Override
    public String getTitle() {
        return super.getTitle();
    }

    @Override
    public double getScore() {
        return score;
    }

    public String toString() {
        return getTitle() + ": " + score;
    }
}
