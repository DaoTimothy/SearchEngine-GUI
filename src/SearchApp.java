import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class SearchApp extends Application {
    Project model;

    public SearchApp() {
        model = new Project();
        model.initialize();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Pane aPane = new Pane();

        // Create the view
        SearchAppView view = new SearchAppView();
        //DVDCollectionAppView2  view = new DVDCollectionAppView2();
        aPane.getChildren().add(view);

        primaryStage.setTitle("Search");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(aPane));
        primaryStage.show();
        view.update(model, "", false, 5);
    }
}
