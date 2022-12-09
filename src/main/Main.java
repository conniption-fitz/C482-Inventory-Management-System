

package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Tory Fitzgerald, id: 000559078
 * Javadocs location: /Fitzgerald_V_C482/javadoc
 *
 * FUTURE ENHANCEMENT
 * Currently, the Inventory lists of parts and products is unsorted. Because of this, each item has to be checked when
 * searching by name or ID. As inventory grows, this will be very slow. The process could be sped up by sorting the
 * objects as they are added to the list. Since each item has a unique ID, a hash table could also be used. By using
 * chaining, parts and products could be organized by type, allowing for quicker searching.
 */
public class Main extends Application {
    /**
     * Loads the Main Menu.
     *
     * @param stage - the Main Form
     */
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
        stage.setTitle("Main Form");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}
