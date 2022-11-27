public class Result extends Page implements SearchResult{
    double cosineSimilarity;

    public Result() {
        //cosine similarity
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
