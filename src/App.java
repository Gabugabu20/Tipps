
import javafx.application.Application;
import javafx.stage.Stage;
import model.Game;
import view.SteinerTree;

public class App extends Application {

    static Game game = new Game();

    public static void main(String[] args) throws Exception {
        game.instanceGame();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        SteinerTree steinerTree = new SteinerTree(game);
        steinerTree.start(primaryStage);
    }

}
