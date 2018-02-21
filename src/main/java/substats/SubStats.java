/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package substats;



import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javax.swing.SwingUtilities;


/**
 *
 * @author Keith
 */
public class SubStats extends Application {
    
    public static final Logger LOGGER = Logger.getLogger(SubStats.class.getName());
    
    DataGatherer dataGatherer;
    WebSocketClientEndpoint controlSocket;
    
    @Override
    public void init() {
        //Set up data query connection
        URI frequencyLocation;
        try {
            frequencyLocation = new URI("http://localhost:4567/frequencies");
        } catch (URISyntaxException ex) {
            LOGGER.log(Level.SEVERE, "Error parsing frequency URI", ex);
            return;
        }
        dataGatherer = new DataGatherer(frequencyLocation);
        dataGatherer.setupClient();
        
        //Set up websocket for control
        URI controlUri;
        try {
            controlUri = new URI("ws://localhost:4567/controls");
        } catch (URISyntaxException ex) {
            LOGGER.log(Level.SEVERE, "Error parsing control URI", ex);
            return;
        }
        controlSocket = new WebSocketClientEndpoint(controlUri);
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception{
        //Would be used if we did FXML UI config
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        
        //Set up UI in code
        GridPane root = new GridPane();
        root.setHgap(10);
        root.setVgap(10);
        
        //Visualizer
        SwingNode swingNode = new SwingNode();
        FrequencyDisplaySwingComponent vis = new FrequencyDisplaySwingComponent();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                vis.initialize();
                swingNode.setContent(vis.getComponent());
            }
        });
        root.add(swingNode, 0, 1);
        
        //name label
        Label categoryLabel = new Label("Platform Name:");
        root.add(categoryLabel, 0, 0);
        
        //name field
        TextField nameTextField = new TextField();
        root.add(nameTextField, 1, 0);
        
        //Query Button
        Button queryButton = new Button("Query");
        queryButton.setOnAction((event) -> {
            String name = nameTextField.getText();
            controlSocket.sendMessage("Querying for: " + name);
            PlatformData data = dataGatherer.getPlatformData(name);
            vis.clearAllBands();
            for (Integer b : data.getFrequencies()) {
                vis.addTargetBand(new FrequencyBand(b, 3, 100));
            }
        });
        root.add(queryButton, 2, 0);
        
        primaryStage.setTitle("SubStats");
        primaryStage.setScene(new Scene(root, 2100, 800));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
    
    
    //////////////
    
    /*
    public void launch() {
        //Set up GUI
        frame = new JFrame("SubStats");
        frame.setPreferredSize(new Dimension(800, 600));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        
        //Frequency Visual Display
        vis = new FrequencyDisplaySwingComponent();
        frame.add(vis.getComponent(), BorderLayout.CENTER);
        
        //Query Panel
        JLabel name = new JTextField();
        JTextField name = new JTextField();
        
    }


    public static void main(String[] args) {
        SubStats app = new SubStats();
        app.launch();
    }
*/
}
