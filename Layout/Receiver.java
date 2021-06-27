import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
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

public class Receiver implements Runnable {
    
    Receiver() {
        new Thread(this).start();
    }
    
    public static void Receive() {
        try {
            while (true) {
                System.out.println("Getting MetaData");
                ByteBuffer bb = ByteBuffer.allocate(200 * 1024);
                Connection.sc.read(bb); // Reading Filename
                bb.flip();

                String[] unwrap = new String(bb.array()).trim().split(",");
                String file_name = new String("D:\\" + unwrap[0]); // Provide the location where to save the file.
                long file_size = Long.parseLong(unwrap[1]);

                Path file = Paths.get(file_name.trim());
                SeekableByteChannel sbc = Files.newByteChannel(file, StandardOpenOption.CREATE,
                                            StandardOpenOption.WRITE);
                bb.clear();

                LocalDateTime lt = LocalDateTime.now();
                String time[] = lt.toString().split("T");

                long sz = 0;
                System.out.println("Receiving File...");

                try{
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
                    Controller.ffrom.setText(Connection.Sender_Name);
                    Controller.ftime.setText("Date : " + time[0] + "\nTime : " + time[1].subSequence(0, time[1].lastIndexOf(".")));
                    Controller.bg.setStyle("-fx-background-color: linear-gradient(to right bottom, #1e81c0, #0099ce, #00b0d2, #00c6ce, #1fdac2);");
                    Controller.icon.setImage(new Image("file:../resources/done.png"));
                    Controller.label.setText("File Successfully Received");
                    Platform.runLater(() -> Controller.list.getChildren().add(0, nd));
                    
                    while (sz != file_size) {
                        Connection.sc.read(bb);
                        sz += bb.position();
                        bb.flip();
                        sbc.write(bb);
                        bb.clear();
                    }
                    System.out.println("File Received.");
                    Platform.runLater(() -> {
                        FadeTransition ft = new FadeTransition(Duration.millis(2000), pop);
                        ft.setFromValue(0.4);
                        ft.setToValue(1.0);
                        ft.setCycleCount(1);
                        ft.play();
                        
                        Popup pw = new Popup();
                        pw.setAnchorLocation(AnchorLocation.CONTENT_TOP_LEFT);
                        pw.setAutoHide(true);
                        pw.getContent().add(pop);
                        pw.show(MainApp.stage);
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void run() {
        try {
            Receive();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String File_Size(long size) {
        String str;
        long sz = size / 1024;
        if (sz < 1024)
            str = String.valueOf(sz) + " KB";
        else
            str = String.valueOf(sz/1024) + " MB";
        
        return str;
    }
}
