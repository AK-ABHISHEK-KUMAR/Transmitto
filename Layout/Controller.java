import java.awt.HeadlessException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.WindowEvent;
import javafx.stage.FileChooser;
/**
 * FXML Controller class
 *
 * @author Bittu
 */
public class Controller implements Initializable {

    @FXML
    public static AnchorPane bg;

    @FXML
    public static VBox list;
    
    @FXML
    public static Label fname, fsize, ffrom, ftime;

    @FXML
    public static ImageView icon;

    @FXML
    public static Label label;
    
    @FXML
    private TextField text_ip;
    
    @FXML
    private Label connect_status;
    
    @FXML
    private TextField file_path;
    
    @FXML
    private TextField time;

    @FXML
    private Button close;
    
    // @FXML
    // private ListView<Pane> lv;

    public static Path path;
    static String ip;
    static Parent root = null;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    void close(ActionEvent event) {
        close.getScene().getWindow().fireEvent(new WindowEvent(MainApp.stage, WindowEvent.WINDOW_CLOSE_REQUEST));
        // System.exit(0);
    }
    
    @FXML
    void Receiver_btn(ActionEvent event) throws IOException, InterruptedException {
        Platform.runLater(() -> {
            if (Connection.Request_Accept()) {
                try {
                    if (MakeWindow("MainWindow.fxml"))
                        new Receiver();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    void Sender_btn(ActionEvent event) throws IOException {
        MakeWindow("Connection.fxml");
    }

    @FXML
    void ip_connect(ActionEvent event) throws InterruptedException, IOException {
        ip = text_ip.getText();
        if (Connection.Request_Send(ip)) {
            connect_status.setText("Connected!");
            connect_status.setTextFill(Color.GREEN);
            if (MakeWindow("MainWindow.fxml")) 
                new Receiver();
        } else {
            connect_status.setText("Not Connected!");
            connect_status.setTextFill(Color.RED);
        }
    }

    @FXML
    void browse_file(ActionEvent event) throws HeadlessException, IOException {
        new Thread(new Runnable(){
            @Override
            public void run() {
                // JFileChooser file_choose = new JFileChooser();
                // file_choose.showOpenDialog(null);
                // path = file_choose.getSelectedFile().toPath();
                // file_path.setText(path.getFileName().toString());
                Platform.runLater(() -> {
                    FileChooser fChooser = new FileChooser();
                    path = fChooser.showOpenDialog(MainApp.stage).toPath();
                    file_path.setText(path.getFileName().toString());
                });
                
            }
        }).start();
    }

    @FXML
    void Schedule_file(ActionEvent event) {
        new ScheduleSent(path, Long.parseLong(time.getText()));
        file_path.setText("");
        time.setText("");
    }

    @FXML
    void Send_file(ActionEvent event) throws Exception {
        Sender.Send(path);
        file_path.setText("");
        time.setText("");
    }


    Boolean MakeWindow(String name) throws IOException {
        root = FXMLLoader.load(getClass().getResource(name));
        Scene scene = new Scene(root);
        MainApp.stage.close();
        MainApp.stage.setScene(scene);
        MainApp.stage.setResizable(false);
        MainApp.stage.setTitle("Transmitto");
        MainApp.stage.getIcons().add(new Image("file:../resources/transmitto.png"));
        // MainApp.stage.fireEvent(new WindowEvent(MainApp.stage, WindowEvent.WINDOW_CLOSE_REQUEST));
        MainApp.stage.setOnCloseRequest((e) -> MainApp.stage.close());
        MainApp.stage.show();
        return MainApp.stage.isShowing();
    }
}
