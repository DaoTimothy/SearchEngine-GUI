import java.util.ArrayList;
public class Result extends Page implements SearchResult {
    double cosineSimilarity;
    ArrayList<Double> vector;
    public Result(Page page) {
        super(page);

    }

    @Override
    public String getTitle() {
        return super.getTitle();
    }

    @Override
    public double getScore() {
        return cosineSimilarity;
    }
}
