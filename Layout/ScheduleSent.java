import java.util.*;
import java.io.IOException;
import java.nio.file.*;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ScheduleSent implements Runnable {
    
    static long t;
    static Path file;
    static String filename;
    static Timer timer;
    // static Sending send = new Sending();

    ScheduleSent(Path path, long time) {
        file = path;
        filename = path.getFileName().toString().trim();
        t = time * 60 * 1000;
        new Thread(this).start();
    }

    public static void Send() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(filename);
            alert.setGraphic(new ImageView(new Image("file:../resources/schedule.png")));
            alert.setContentText("Schedule to be Send in " + String.valueOf(Math.round(t / 6e4)) + " Minute.");
            alert.showAndWait().ifPresent(e -> {
                if (e == ButtonType.OK) {
                    timer = new Timer();
                    timer.purge();
                    timer.schedule(new Sending(), t);
                }
            });
        });
    }
    @Override
    public void run() {
        Send();
    }
}

class Sending extends TimerTask{
    @Override
    public void run() {
        try {
            Sender.Send(ScheduleSent.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}