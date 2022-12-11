import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;

public class SearchAppView extends Pane {
    private ListView<String> resultTitles;
    private ListView<Double> resultScores;
    private TextField searchBox;
    private RadioButton pageRankToggle;

    public RadioButton getPageRankToggle() {
        return pageRankToggle;
    }

    public TextField getSearchBox() {
        return searchBox;
    }

    public SearchAppView() {
        Label searchLabel = new Label("Search");
        Label pageRankLabel = new Label("Include PageRank");
        Label titleLabel = new Label("Result Titles");
        Label scoreLabel = new Label("Result Scores");

        searchBox = new TextField("");
        searchBox.setPromptText("Enter your query");
        searchBox.relocate(10, 30);
        searchBox.setPrefSize(410, 25);
        searchLabel.relocate(10,10);

        pageRankToggle = new RadioButton();
        pageRankToggle.relocate(10, 55);
        pageRankToggle.setPrefSize(50, 25);
        pageRankLabel.relocate(40, 55);
        pageRankLabel.setPrefSize(100, 25);

        resultTitles = new ListView<String>();
        resultTitles.relocate(10, 110);
        resultTitles.setPrefSize(200,235);
        titleLabel.relocate(10, 90);

        resultScores = new ListView<Double>();
        resultScores.relocate(220, 110);
        resultScores.setPrefSize(200,235);
        scoreLabel.relocate(220,90);


        // Add all the components to the Pane
        getChildren().addAll(searchBox, searchLabel, pageRankToggle, pageRankLabel, resultTitles, titleLabel, resultScores, scoreLabel);

        setPrefSize(430, 355);
    }

    public void update(Search model, String query, boolean boost, int n) {
        List<SearchResult> results = model.search(query, boost, n);
        resultTitles.getItems().clear();
        resultScores.getItems().clear();
        for (int i = 0; i < results.size(); i++) {
            resultTitles.getItems().add(results.get(i).getTitle());
            resultScores.getItems().add(results.get(i).getScore());
        }
    }
}
