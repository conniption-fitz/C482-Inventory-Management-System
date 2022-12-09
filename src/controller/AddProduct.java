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

public class AddProduct implements Initializable {
    public Button cancelButton;
    public Button saveButton;
    public Button addAssocPartButton;
    public Button removeAssocPartButton;
    public TextField searchPartsTF;
    public TableView<Part> allPartsTable;
    public TableColumn<Part, Integer> allPartsIDCol;
    public TableColumn<Part, String> allPartsNameCol;
    public TableColumn<Part, Integer> allPartsInvCol;
    public TableColumn<Part, Double> allPartsCostCol;
    public TableView<Part> assocPartsTable;
    public TableColumn<Part, Integer> assocPartsIDCol;
    public TableColumn<Part, String> assocPartsNameCol;
    public TableColumn<Part, Integer> assocPartsInvCol;
    public TableColumn<Part, Double> assocPartsCostCol;
    public TextField autoIDTF;
    public TextField nameTF;
    public TextField stockTF;
    public TextField priceTF;
    public TextField maxTF;
    public TextField minTF;
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    private Product product;

    /**
     * Initializes the Add Product form and sets up the All Parts and Associated Parts tables.
     *
     * @param url, resourceBundle - the add product form
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        allPartsTable.setItems(Inventory.getAllParts());

        allPartsIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        allPartsNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        allPartsInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        allPartsCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        assocPartsTable.setItems(associatedParts);

        assocPartsIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        assocPartsNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        assocPartsInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        assocPartsCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        product = new Product(Inventory.getAutoID(), "New Product", -1, -1, -1, -1);
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
     * Adds a selected part from the All Parts table to the Associated Parts. The associated part is attached to the
     * product that is being modified. If the user does not select a part to associate, an error message is generated.
     *
     * @param actionEvent - the add button is clicked
     */
    public void onAddAssocPart(ActionEvent actionEvent) {
        Part part = allPartsTable.getSelectionModel().getSelectedItem();

        if (part != null) {
            associatedParts.add(part);
            product.addAssociatedPart(part);
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Select a part to associate.");
            alert.showAndWait();
        }
    }

    /**
     * Closes the Add Product form and returns to the main menu.
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
     * Generates a message asking the user to confirm removing an associated part. If the user clicks YES, the part is
     * removed from the Associated Parts table and disassociated from the product being added, and generates a
     * message which confirms the successful removal.
     *
     * @param actionEvent - the remove button is clicked
     */
    public void onRemoveAssocPart(ActionEvent actionEvent) {
        Part current = assocPartsTable.getSelectionModel().getSelectedItem();

        if (current == null) {
            Alert remAlert = new Alert(Alert.AlertType.ERROR, "Select a part to remove.");
            remAlert.showAndWait();
        } else {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to remove " + current.getName()
                    + "?", ButtonType.YES, ButtonType.NO);
            alert.setTitle("Remove Associated Part?");

            Optional<ButtonType> response = alert.showAndWait();

            if (response.get() == ButtonType.YES) {
                if (product.deleteAssociatedPart(current)) {
                    associatedParts.remove(current);
                    Alert success = new Alert(Alert.AlertType.INFORMATION, "Associated part removed.");
                    success.showAndWait();
                } else if (!product.deleteAssociatedPart(current)) {
                    Alert fail = new Alert(Alert.AlertType.ERROR, "Unable to remove associated part.");
                    fail.showAndWait();
                }

            }
        }
    }

    /**
     * Creates a new product and saves in inventory. First, each attribute field is checked to ensure that the
     * user-entered data is correctly formatted. If an attribute is not of the correct type, if min is not less than
     * max, or if inventory is not between min and max, an error message is generated and the user is returned to the
     * form. If the data is correct, the new product is added to the inventory, and the user is returned to the main
     * menu.
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
            product.setName(name);
            product.setPrice(price);
            product.setStock(stock);
            product.setMin(min);
            product.setMax(max);

            for (Part part : associatedParts)
                product.addAssociatedPart(part);

            Inventory.addProduct(product);

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
