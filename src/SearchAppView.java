import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.control.ListView;

import java.util.ArrayList;
import java.util.List;

public class SearchAppView extends Pane {
    private ListView<String> resultTitles;
    private ListView<Double> resultScores;

    public SearchAppView() {
        Label label1 = new Label("Title");
        label1.relocate(10, 10);
        Label label2 = new Label("Score");
        label2.relocate(220, 10);

        resultTitles = new ListView<String>();
        resultTitles.relocate(10, 40);
        resultTitles.setPrefSize(200,150);

        resultScores = new ListView<Double>();
        resultScores.relocate(220, 40);
        resultScores.setPrefSize(60,150);


        // Add all the components to the Pane
        getChildren().addAll(resultScores, resultTitles);

        setPrefSize(360, 240);
    }

    public void update(Project model, String query, boolean boost, int n) {
        List<SearchResult> results = model.search(query, boost, n);
        resultTitles.getItems().clear();
        resultScores.getItems().clear();
        for (int i = 0; i < results.size(); i++) {
            resultTitles.getItems().add(results.get(i).getTitle());
            resultScores.getItems().add(results.get(i).getScore());
        }
    }
}
