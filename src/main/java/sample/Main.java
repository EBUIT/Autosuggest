package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.mockserver.integration.ClientAndProxy;
import org.mockserver.integration.ClientAndServer;

public class Main extends Application {

    private ClientAndProxy proxy;
    private ClientAndServer mockServer;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 1024, 768));
        primaryStage.show();
    }


    public void startProxy() {
        //mockServer = startClientAndServer(1080);
        //proxy = startClientAndProxy(1090);
    }

    public void stopProxy() {
        proxy.stop();
        mockServer.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
