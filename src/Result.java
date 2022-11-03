public class Result implements SearchResult{
    String title;
    double score;

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public double getScore() {
        return score;
    }
}
