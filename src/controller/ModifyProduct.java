/**
 *
 * @author Tory Fitzgerald, id: 000559078
 */

package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ModifyProduct implements Initializable {

    public Button cancelButton;
    public Button saveButton;
    public Button addAssocPartButton;
    public Button removeAssocPartButton;
    public TableView<Part> allPartsTable;
    public TableColumn<Part, Integer> allPartIDCol;
    public TableColumn<Part, String> allPartsNameCol;
    public TableColumn<Part, Integer> allPartsInvCol;
    public TableColumn<Part, Double> allPartsCostCol;
    public TableView<Part> assocPartsTable;
    public TableColumn<Part, Integer> assocPartsIDCol;
    public TableColumn<Part, String> assocPartsNameCol;
    public TableColumn<Part, Integer> assocPartsInvCol;
    public TableColumn<Part, Double> assocPartsCostCol;
    public TextField searchPartsTF;
    public TextField prodIDTF;
    public TextField prodNameTF;
    public TextField prodInvTF;
    public TextField prodCostTF;
    public TextField prodMaxTF;
    public TextField prodMinTF;
    private Product selectedProduct;
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    /**
     * Initializes the Modify Product form and sets up the Parts table.
     *
     * @param url, resourceBundle - the modify product form
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        allPartsTable.setItems(Inventory.getAllParts());

        allPartIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        allPartsNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        allPartsInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        allPartsCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));

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
     * Loads the current product attributes into the Modify Product form. If the product has associated parts, they are
     * generated in the Associated Parts table.
     *
     * @param product - the user-selected product to be modified
     */
    public void loadProduct(Product product) {
        selectedProduct = product;

        prodNameTF.setText(selectedProduct.getName());
        prodIDTF.setText(Integer.toString(selectedProduct.getID()));
        prodInvTF.setText(Integer.toString(selectedProduct.getStock()));
        prodMinTF.setText(Integer.toString(selectedProduct.getMin()));
        prodMaxTF.setText(Integer.toString(selectedProduct.getMax()));
        prodCostTF.setText(Double.toString(selectedProduct.getPrice()));

        if (selectedProduct.getAssociatedParts() != null)
            associatedParts = selectedProduct.getAssociatedParts();

        assocPartsTable.setItems(associatedParts);

        assocPartsIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        assocPartsNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        assocPartsInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        assocPartsCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * Adds a selected part from the All Parts table to the Associated Parts. The associated part is attached to the
     * product that is being modified. If the user does not select a part to associate, an error message is generated.
     *
     * @param actionEvent - the add button is clicked
     */
    public void onAddAssocPart(ActionEvent actionEvent) {
        Part part = allPartsTable.getSelectionModel().getSelectedItem();

        if (part != null) {
            selectedProduct.addAssociatedPart(part);
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Select a part to associate.");
            alert.showAndWait();
        }
    }

    /**
     * Closes the Modify Product form and returns to the main menu.
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
     * Removes a selected part from the Associated Parts table and disassociates it with the product being modified.
     * Generates a message which confirms that the associated part has been removed.
     *
     * @param actionEvent - the remove button is clicked
     */
    public void onRemoveAssocPart(ActionEvent actionEvent) {
        Part current = assocPartsTable.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to remove " + current.getName()
                + "?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Remove Associated Part?");

        Optional<ButtonType> response = alert.showAndWait();

        if (response.get() == ButtonType.YES) {
            if (selectedProduct.deleteAssociatedPart(current)) {
                associatedParts.remove(current);
                Alert success = new Alert(Alert.AlertType.INFORMATION, "Associated part removed.");
                success.showAndWait();
            } else if (!selectedProduct.deleteAssociatedPart(current)) {
                Alert fail = new Alert(Alert.AlertType.ERROR, "Unable to remove associated part.");
                fail.showAndWait();
            }
        }
    }

    /**
     * Saves the updated data for the modified product. First, each attribute field is checked to ensure that the
     * user-entered data is correctly formatted. If an attribute is not of the correct type, if min is not less than
     * max, or if inventory is not between min and max, an error message is generated and the user is returned to the
     * form. If the data is correct, the selected product is replaced with the updated information, including any
     * associated parts, and the user is returned to the main screen.
     *
     * @param actionEvent - the save button is clicked
     */
    public void onSave(ActionEvent actionEvent) throws IOException {
        Product product;

        String name = prodNameTF.getText();
        String invStr = prodInvTF.getText();
        String priceStr = prodCostTF.getText();
        String minStr = prodMinTF.getText();
        String maxStr = prodMaxTF.getText();

        int index = Inventory.getProductIndex(selectedProduct);
        int autoID = selectedProduct.getID();
        int stock = -1;
        double price = -1;
        int min = -1;
        int max = -1;

        boolean valid = true;

        try {
            stock = Integer.parseInt(invStr);
        }
        catch (NumberFormatException e) {
            intAlert("Inventory");
            valid = false;
        }
        try {
            price = Double.parseDouble(priceStr);
        }
        catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Price must be a number.");
            alert.showAndWait();
            valid = false;
        }
        try {
            min = Integer.parseInt(minStr);
        }
        catch (NumberFormatException e) {
            intAlert("Min");
            valid = false;
        }
        try {
            max = Integer.parseInt(maxStr);
        }
        catch (NumberFormatException e) {
            intAlert("Max");
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

        if (valid) {
            product = new Product(autoID, name, price, stock, min, max);

            for (Part part : associatedParts)
                product.addAssociatedPart(part);

            Inventory.updateProduct(index, product);

            Parent root = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("Main Form");
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * Searches for a part in the All Parts table. First the function checks to see if the user-entered query is an
     * integer. If it is, the function searches by Part ID and displays the matching part in the table. If the search
     * query is not an integer, the function searches by Part Name and displays all parts containing the query. If no
     * matching parts are found, a message appears indicating that there are no matching parts.
     *
     * @param actionEvent - the user enters a query into the search bar and presses enter
     */
    public void onSearchParts(ActionEvent actionEvent) {
        String query = searchPartsTF.getText();
        ObservableList<Part> parts = FXCollections.observableArrayList();

        try {
            int queryInt = Integer.parseInt(query);
            Part id = Inventory.lookupPart(queryInt);
            if (id != null)
                parts.add(id);
        } catch (NumberFormatException e) {
            parts = Inventory.lookupPart(query);
        }
        if (parts.size() == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "No items found.");
            alert.showAndWait();
        }

        allPartsTable.setItems(parts);
    }
}
