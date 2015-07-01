package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import org.mockserver.integration.ClientAndServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import sample.combobox.KeyValueTextFlow;
import sample.mockserver.Expectation;
import sample.mockserver.MockDatas;

import java.util.List;

public class Main extends Application {
    private final static Logger LOG = LoggerFactory.getLogger(Main.class);
    public static ClassPathXmlApplicationContext applicationContext;
    private ClientAndServer mockServer;

    @Override
    public void start(Stage primaryStage) throws Exception {
        LOG.info("Spring context");
        applicationContext = new ClassPathXmlApplicationContext("/META-INF/autosuggest.xml");

        // test textflow
        LOG.info(" test TextFlow -- ");
        List<KeyValueTextFlow> list = MockDatas.loadLocationTextFlow();
        for (KeyValueTextFlow k : list) {
            TextFlow tf = k.getValue();
            List<Text> listinside = (List) tf.getChildren();
            for (Text t : listinside) {
                LOG.info(t.getText());
            }
        }

        LOG.info("Start Mock Server");
//        startProxy();

        LOG.info("Get Expectations");
//        Expectation.start(Expectation.SCENARIO.MOCK_1COL);

        LOG.info("Fx loading");
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");

        Scene scene = new Scene(root, 1024, 768);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() {
        stopProxy();
    }

    public void startProxy() {
        mockServer = ClientAndServer.startClientAndServer(8080);
    }

    public void stopProxy() {
        mockServer.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
