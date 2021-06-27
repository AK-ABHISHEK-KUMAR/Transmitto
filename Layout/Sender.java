import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.time.LocalDateTime;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.PopupWindow.AnchorLocation;
import javafx.util.Duration;

public class Sender {
    
    public static void Send(Path file) throws IOException {
        LocalDateTime lt = LocalDateTime.now();
        String time[] = lt.toString().split("T");
        String file_name = file.getFileName().toString().trim();
        String file_size = String.valueOf(Files.size(file));
        String wraper = file_name + "," + file_size;

        try {
            FileChannel fc = (FileChannel) Files.newByteChannel(file, StandardOpenOption.READ);
            ByteBuffer bb = ByteBuffer.allocate(200 * 1024);
            Connection.sc.write(ByteBuffer.wrap(wraper.getBytes()));
            System.out.println("Sending File...");

            try {
                // Parent nd = FXMLLoader.load(Controller.class.getResource("S_R_Layout.fxml"));
                Parent nd, pop;
                FXMLLoader fl = new FXMLLoader(Controller.class.getResource("S_R_Layout.fxml"));
                FXMLLoader flpop = new FXMLLoader(Controller.class.getResource("PopMessage.fxml"));
                nd = fl.load();
                pop = flpop.load();
                Controller.fname = (Label) nd.lookup("#fname");
                Controller.fsize = (Label) nd.lookup("#fsize");
                Controller.ffrom = (Label) nd.lookup("#ffrom");
                Controller.ftime = (Label) nd.lookup("#ftime");
                Controller.bg = (AnchorPane) nd.lookup("#bg");
                Controller.list = (VBox) Controller.root.lookup("#list");
                Controller.icon = (ImageView) pop.lookup("#icon");
                Controller.label = (Label) pop.lookup("#label");

                Controller.fname.setText("File_Name : " + file_name);
                Controller.fsize.setText(File_Size(file_size));
                Controller.ffrom.setText(Connection.Receiver_Name);
                Controller.ftime.setText("Date : " + time[0] + "\nTime : " + time[1].subSequence(0, time[1].lastIndexOf(".")));
                Controller.bg.setStyle("-fx-background-color: linear-gradient(to right bottom, #1ec041, #23c754, #29cd66, #32d477, #3bda87);");
                Controller.icon.setImage(new Image("file:../resources/done.png"));
                Controller.label.setText("File Successfully Sent");
                Platform.runLater(() -> Controller.list.getChildren().add(0, nd));
                
                while (fc.read(bb) != -1) {
                    bb.flip();
                    Connection.sc.write(bb);
                    bb.clear();
                }
                System.out.println("File Sent.");
                Platform.runLater(() -> {
                    FadeTransition ft = new FadeTransition(Duration.millis(2000), pop);
                    ft.setFromValue(0.4);
                    ft.setToValue(1.0);
                    ft.setCycleCount(1);
                    ft.play();
                    
                    Popup pw = new Popup();
                    pw.setAnchorLocation(AnchorLocation.CONTENT_TOP_RIGHT);
                    pw.setAutoHide(true);
                    pw.getContent().add(pop);
                    pw.show(MainApp.stage);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace(new PrintStream(System.out));
        }
    }
    
    public static String File_Size(String size) {
        long s = Long.parseLong(size);
        String str;
        long sz = s / 1024;
        if (sz < 1024)
            str = String.valueOf(sz) + " KB";
        else
            str = String.valueOf(sz/1024) + " MB";
        
        return str;
    }
}
