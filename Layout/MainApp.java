import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author Bittu
 */
public class MainApp extends Application {
    
    static Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Start.fxml"));

        Scene scene = new Scene(root);
        
        stage = primaryStage;
        stage.setScene(scene);
        stage.setTitle("Transmitto");
        stage.setResizable(false);
        stage.getIcons().add(new Image("file:../resources/transmitto.png"));
        // stage.fireEvent(new WindowEvent(MainApp.stage, WindowEvent.WINDOW_CLOSE_REQUEST));
        stage.setOnCloseRequest((e) -> stage.close());
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
