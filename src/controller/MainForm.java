/**
 *
 * @author Tory Fitzgerald, id: 000559078
 */

package controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import model.Inventory;
import model.Part;
import model.Product;



public class MainForm implements Initializable {
    public Button exitButton;
    public Button addProductButton;
    public Button modProductButton;
    public Button deleteProductButton;
    public TextField searchPartsTF;
    public Button addPartButton;
    public Button modPartButton;
    public Button deletePartButton;
    public TableView<Part> partsMenu;
    public TableColumn<Part, Integer> partIDCol;
    public TableColumn<Part, String> partNameCol;
    public TableColumn<Part, Integer> partsInvCol;
    public TableColumn<Part, Double> partsCostCol;
    public TableView<Product> productMenu;
    public TableColumn<Product, Integer> prodIDCol;
    public TableColumn<Product, String> prodNameCol;
    public TableColumn<Product, Integer> prodInvCol;
    public TableColumn<Product, Double> prodCostCol;
    public TextField searchProductsTF;

    Alert delSuccess = new Alert(Alert.AlertType.INFORMATION, "Item successfully deleted.");
    Alert delFail = new Alert(Alert.AlertType.ERROR, "Unable to delete item.");
    Alert noSelection = new Alert(Alert.AlertType.ERROR, "Select an item.");
    Alert noSearch = new Alert(Alert.AlertType.INFORMATION, "No items found.");

    /**
     * Initializes the Main Menu form and sets up the Parts and Products tables.
     *
     * @param url, resourceBundle - the main menu form
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        partsMenu.setItems(Inventory.getAllParts());

        partIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partsInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partsCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        productMenu.setItems(Inventory.getAllProducts());

        prodIDCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        prodNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        prodInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        prodCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    public boolean confirmDel(String item) {
        boolean delete = false;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete " + item + "?",
                ButtonType.YES, ButtonType.NO);
        alert.setTitle("Delete Item?");


        Optional<ButtonType> response = alert.showAndWait();

        if (response.get() == ButtonType.YES) {
            delete = true;
        }

        return delete;
    }

    /**
     * Opens the Add Part menu.
     *
     * @param actionEvent - the add part button is clicked
     */
    public void onAddPart(ActionEvent actionEvent) throws IOException {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/AddPart.fxml"));
            Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("Add Part");
            stage.setScene(scene);
            stage.show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens the Add Product menu.
     *
     * @param actionEvent - the add product button is clicked
     */
    public void onAddProduct(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/AddProduct.fxml"));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Add Product");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Deletes a part from inventory. First, a confirmation pop-up asks if the user wants to delete the selected part.
     * If the user clicks OK, the part is deleted from inventory. If the user does not select a part, or if the part
     * can't be deleted for some other reason, a descriptive error message is generated.
     *
     * @param actionEvent - the delete part button is clicked
     */
    public void onDeletePart(ActionEvent actionEvent) {
        Part selectedPart = partsMenu.getSelectionModel().getSelectedItem();

        if (selectedPart != null) {
            if (confirmDel(selectedPart.getName())) {
                if (Inventory.deletePart(selectedPart))
                    delSuccess.show();
                else if (!Inventory.deletePart(selectedPart))
                    delFail.show();
            }
        }
        else
            noSelection.showAndWait();
    }

    /**
     * Deletes a product from inventory. First, a confirmation pop-up asks if the user wants to delete the selected
     * product. If the user clicks OK, the function checks to see if there are any parts associated with the product. If
     * there are associated parts, if the user does not select a product, or if the product can't be deleted for any
     * other reason, a descriptive error message is generated.
     *
     * @param actionEvent - the delete product button is clicked
     */
    public void onDeleteProduct(ActionEvent actionEvent) {
        Product selectedProduct = productMenu.getSelectionModel().getSelectedItem();

        if (selectedProduct != null) {
            if (selectedProduct.getAssociatedParts().size() != 0) {
                Alert assocParts = new Alert(Alert.AlertType.ERROR, "Can't delete a product with associated parts.");
                assocParts.showAndWait();
            } else if (confirmDel(selectedProduct.getName())) {
                if (Inventory.deleteProduct(selectedProduct))
                    delSuccess.show();
                else if (!Inventory.deleteProduct(selectedProduct))
                    delFail.show();
            }
        }
        else
            noSelection.showAndWait();
    }

    /**
     * Exits the program.
     *
     * @param actionEvent - the exit button is clicked
     */
    public void onExit(ActionEvent actionEvent) {
        Platform.exit();
    }

    /**
     * Opens the Modify Part menu and sends the data for the user-selected part to the Modify Part controller. If the
     * user does not select a part, an error message is generated.
     *
     * @param actionEvent - the modify part button is clicked
     */
    public void onModPart(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/ModifyPart.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        ModifyPart controller = loader.getController();

        try {
            Part selectedPart = partsMenu.getSelectionModel().getSelectedItem();
            controller.loadPart(selectedPart);

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("Modify Part");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            noSelection.showAndWait();
        }
    }

    /**
     * Opens the Modify Product menu and sends the data for the user-selected part to the Modify Product controller. If
     * the user does not select a product, an error message is generated.
     *
     * @param actionEvent - the modify part button is clicked
     */
    public void onModProduct(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/ModifyProduct.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        ModifyProduct controller = loader.getController();

        try {
            Product selectedProduct = productMenu.getSelectionModel().getSelectedItem();
            controller.loadProduct(selectedProduct);

            Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("Modify Product");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            noSelection.showAndWait();
        }
    }

    /**
     * Searches for a part in the parts table. First the function checks to see if the user-entered query is an integer.
     * If it is, the function searches by Part ID and displays the matching part in the table. If the search query is
     * not an integer, the function searches by Part Name and displays all parts containing the query. If no matching
     * parts are found, a message appears indicating that there are no matching parts.
     *
     * @param actionEvent - the user enters a query into the search parts bar and presses enter
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
        if (parts.size() == 0)
            noSearch.showAndWait();

        partsMenu.setItems(parts);
    }

    /**
     * Searches for a product in the products table. First the function checks to see if the user-entered query is an
     * integer. If it is, the function searches by Product ID and displays the matching product in the table. If the
     * search query is not an integer, the function searches by Product Name and displays all products containing the
     * query. If no matching products are found, a message appears indicating that there are no matching products.
     *
     * @param actionEvent - the user enters a query into the search parts bar and presses enter
     */
    public void onSearchProducts(ActionEvent actionEvent) {
        String query = searchProductsTF.getText();
        ObservableList<Product> products = Inventory.lookupProduct(query);

        try {
            int queryInt = Integer.parseInt(query);
            Product id = Inventory.lookupProduct(queryInt);
            if (id != null)
                products.add(id);
        } catch (NumberFormatException e) {
            products = Inventory.lookupProduct(query);
        }
        if (products.size() == 0)
            noSearch.showAndWait();

        productMenu.setItems(products);
    }
}
