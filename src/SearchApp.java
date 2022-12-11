import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class SearchApp extends Application {
    Search model;

    public SearchApp() {
        model = new Search("PageResults");
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Pane aPane = new Pane();

        // Create the view
        SearchAppView view = new SearchAppView();
        aPane.getChildren().add(view);

        primaryStage.setTitle("Search");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(aPane));
        primaryStage.show();

        String query = "";
        Boolean boost = false;

        view.getSearchBox().textProperty().addListener((observable -> {
            view.update(model, view.getSearchBox().getText(), view.getPageRankToggle().isSelected(), 10);
        }));
        view.getPageRankToggle().selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                view.update(model, view.getSearchBox().getText(), view.getPageRankToggle().isSelected(), 10);
            }
        });

        view.update(model, query, boost, 10);



    }
}
