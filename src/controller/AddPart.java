/**
 *
 * @author Tory Fitzgerald, id: 000559078
 */

package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Inventory;
import model.InHouse;
import model.Outsourced;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddPart implements Initializable {
    public RadioButton inHouseRadio;
    public ToggleGroup Parts;
    public RadioButton outsourcedRadio;
    public Label machineIDCompanyLabel;
    public Button saveButton;
    public Button cancelButton;
    public TextField nameTF;
    public TextField stockTF;
    public TextField priceTF;
    public TextField minTF;
    public TextField machineIDCompanyNameTF;
    public TextField maxTF;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    /**
     * Generates an error message when a user enters a non-integer in an attribute field which requires an integer.
     *
     * @param item - the name of the attribute that is not an integer
     */
    private void intAlert (String item) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(item + " must be an integer.");
        alert.showAndWait();
    }

    /**
     * Closes the Add Part form and returns to the main menu.
     *
     * @param actionEvent - the cancel button is clicked
     */
    public void onCancel(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Main Form");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Changes the last form field label to Machine ID.
     *
     * @param actionEvent - the In House radio button is selected
     */
    public void onInHouseRadio(ActionEvent actionEvent) {
        machineIDCompanyLabel.setText("Machine ID");
    }

    /**
     * Changes the last form field label to Company Name.
     *
     * @param actionEvent - the Outsourced radio button is selected
     */
    public void onOutsourcedRadio(ActionEvent actionEvent) {
        machineIDCompanyLabel.setText("Company");
    }

    /**
     * Creates a new part and adds it to the inventory. First, each attribute field is checked to ensure that the
     * user-entered data is correctly formatted. If an attribute is not of the correct type, if min is not less than
     * max, or if inventory is not between min and max, an error message is generated and the user is returned to the
     * form. If the data is correct, a new InHouse or Outsourced part is created, based on the selected radio button,
     * added to inventory, and the user is returned to the main menu.
     *
     * @param actionEvent - the save button is clicked
     */
    public void onSave(ActionEvent actionEvent) throws IOException {
        int autoID = Inventory.getAutoID();
        String name = nameTF.getText();
        String invStr = stockTF.getText();
        String priceStr = priceTF.getText();
        String minStr = minTF.getText();
        String maxStr = maxTF.getText();
        String machineIDCoNameStr = machineIDCompanyNameTF.getText();

        int stock = -1;
        double price = -1;
        int min = -1;
        int max = -1;
        int machineID = -1;

        boolean valid = true;

        try {
            stock = Integer.parseInt(invStr);
        } catch (NumberFormatException e) {
            intAlert("Inventory");
            valid = false;
        }
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Price must be a number.");
            alert.showAndWait();
            valid = false;
        }
        try {
            min = Integer.parseInt(minStr);
        } catch (NumberFormatException e) {
            intAlert("Min");
            valid = false;
        }
        try {
            max = Integer.parseInt(maxStr);
        } catch (NumberFormatException e) {
            intAlert("Min");
            valid = false;
        }

        if (min > max) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Min must be less than max.");
            alert.showAndWait();
            valid = false;
        }
        if (stock > max || stock < min) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Inventory must be between min and max.");
            alert.showAndWait();
            valid = false;
        }

        if (inHouseRadio.isSelected()) {
            try {
                machineID = Integer.parseInt(machineIDCoNameStr);
            }
            catch (NumberFormatException e) {
                intAlert("Machine ID");
                valid = false;
            }

            if (valid) {
                InHouse part = new InHouse(autoID, name, price, stock, min, max, machineID);
                Inventory.addPart(part);

                Parent root = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
                Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setTitle("Main Form");
                stage.setScene(scene);
                stage.show();
            }
        }
        else if (outsourcedRadio.isSelected()) {
            if (valid) {
                Outsourced part = new Outsourced(autoID, name, price, stock, min, max, machineIDCoNameStr);
                Inventory.addPart(part);

                Parent root = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
                Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setTitle("Main Form");
                stage.setScene(scene);
                stage.show();
            }
        }
    }
}
