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
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Part;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ModifyPart implements Initializable {
    public RadioButton inHouseRadio;
    public ToggleGroup modPartsType;
    public  RadioButton outsourcedRadio;
    public Label machineIDCompanyNameLabel;
    public Button saveButton;
    public Button cancelButton;
    public  TextField autoIDTF;
    public  TextField nameTF;
    public  TextField stockTF;
    public  TextField priceTF;
    public  TextField minTF;
    public  TextField machineIDCompanyNameTF;
    public  TextField maxTF;
    private Part selectedPart;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { }

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
     * Loads the current part attributes into the Modify Part form.
     *
     * @param part - the user-selected part to be modified
     */
    public void loadPart(Part part) {
        selectedPart = part;

        nameTF.setText(selectedPart.getName());
        autoIDTF.setText(Integer.toString(selectedPart.getId()));
        stockTF.setText(Integer.toString(selectedPart.getStock()));
        minTF.setText(Integer.toString(selectedPart.getMin()));
        maxTF.setText(Integer.toString(selectedPart.getMax()));
        priceTF.setText(Double.toString(selectedPart.getPrice()));

        if (selectedPart instanceof Outsourced) {
            outsourcedRadio.setSelected(true);
            machineIDCompanyNameLabel.setText("Company");
            machineIDCompanyNameTF.setText(((Outsourced) selectedPart).getCompanyName());
        }
        if (selectedPart instanceof InHouse)
            machineIDCompanyNameTF.setText(Integer.toString(((InHouse) selectedPart).getMachineID()));

    }

    /**
     * Closes the Modify Part form and returns to the main menu.
     *
     * @param actionEvent - the cancel button is clicked
     */
    public void onCancel(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
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
        machineIDCompanyNameLabel.setText("Machine ID");
    }

    /**
     * Changes the last form field label to Company Name.
     *
     * @param actionEvent - the Outsourced radio button is selected
     */
    public void onOutsourcedRadio(ActionEvent actionEvent) {
        machineIDCompanyNameLabel.setText("Company");
    }

    /**
     * Saves the updated data for the modified part. First, each attribute field is checked to ensure that the
     * user-entered data is correctly formatted. If an attribute is not of the correct type, if min is not less than
     * max, or if inventory is not between min and max, an error message is generated and the user is returned to the
     * form. If the data is correct, the selected part is replaced with the updated information, including changing the
     * part type to InHouse or Outsourced based on the selected radio button, and the user is returned to the main menu.
     *
     * @param actionEvent - the save button is clicked
     */
    public void onSave(ActionEvent actionEvent) throws IOException {
        String name = nameTF.getText();
        String invStr = stockTF.getText();
        String priceStr = priceTF.getText();
        String minStr = minTF.getText();
        String maxStr = maxTF.getText();
        String machineIDCoNameStr = machineIDCompanyNameTF.getText();

        int index = Inventory.getPartIndex(selectedPart);
        int autoID = selectedPart.getId();
        int stock = -1;
        double price = -1;
        int min = -1;
        int max = -1;
        int machineID = -1;
        String company = "";

        boolean valid = true;

        try {
            stock = Integer.parseInt(invStr);
            selectedPart.setStock(stock);
        }
        catch (NumberFormatException e) {
            intAlert("Inventory");
            valid = false;
        }
        try {
            price = Double.parseDouble(priceStr);
            selectedPart.setPrice(price);
        }
        catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Price must be a number.");
            alert.showAndWait();
            valid = false;
        }
        try {
            min = Integer.parseInt(minStr);
            selectedPart.setMin(min);
        }
        catch (NumberFormatException e) {
            intAlert("Min");
            valid = false;
        }
        try {
            max = Integer.parseInt(maxStr);
            selectedPart.setMax(max);
        }
        catch (NumberFormatException e) {
            intAlert("Max");
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
        }
        else if (outsourcedRadio.isSelected()) {
            company = machineIDCoNameStr;
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

        if (valid) {
            if (inHouseRadio.isSelected()) {
                InHouse part = new InHouse(autoID, name, price, stock, min, max, machineID);
                Inventory.updatePart(index, part);
            } else if (outsourcedRadio.isSelected()) {
                Outsourced part = new Outsourced(autoID, name, price, stock, min, max, company);
                Inventory.updatePart(index, part);
            }

            Parent root = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("Main Form");
            stage.setScene(scene);
            stage.show();
        }
    }
}
