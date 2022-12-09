/**
 *
 * @author Tory Fitzgerald, id: 000559078
 */

package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory {

    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    private static int autoID = 0;

    /**
     * Adds a Part object to the inventory list allParts.
     *
     * @param newPart the part to add to the list of all parts
     */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    /**
     * Adds a Product object to the inventory list allProducts.
     *
     * @param newProduct the product to add to the list of all products
     */
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    /**
     * Returns a part when the user searches by a part ID.
     *
     * RUNTIME ERROR
     *
     * The function was originally written to use the ObservableList get(int i) function. When a user entered an
     * integer, the function would use the getter to retrieve the part at that index, rather than the part with the
     * matching ID. When the user entered an integer that was larger than the size of the ObservableList, the function
     * threw an IndexOutOfBounds exception.
     *
     * The corrected function iterates through the list of all parts until it finds the part with the matching part ID.
     * If the part is found, it returns the part, otherwise it returns null.
     *
     * @param partID the partID to lookup
     * @return the part with matching partID
     */
    public static Part lookupPart(int partID) {
        for (Part part : allParts) {
            if (part.getId() == partID)
                return part;
        }
        return null;
    }

    /**
     * Returns a product when the user searches by a product ID.
     *
     * @param productID the productID to lookup
     * @return the product with matching productID
     */
    public static Product lookupProduct (int productID) {
        for (Product product : allProducts) {
            if (product.getID() == productID)
                return product;
        }
        return null;
    }

    /**
     * Returns a list of parts that contain some or all of a part name searched for by the user.
     *
     * @param partName user-entered String to search
     * @return the list of parts containing the user-entered String
     */
    public static ObservableList<Part> lookupPart(String partName) {
        ObservableList<Part> partsSubset = FXCollections.observableArrayList();

        for (Part search : allParts) {
            if (search.getName().toLowerCase().contains(partName.toLowerCase())) {
                partsSubset.add(search);
            }
        }
        return partsSubset;
    }

    /**
     * Returns a list of products that contain some or all of a product name searched for by the user.
     *
     * @param productName user-entered String to search
     * @return the list of products containing the user-entered String
     */
    public static ObservableList<Product> lookupProduct(String productName) {
        ObservableList<Product> productSubset = FXCollections.observableArrayList();

        for (Product search : allProducts) {
            if (search.getName().toLowerCase().contains(productName.toLowerCase())) {
                productSubset.add(search);
            }
        }
        return productSubset;
    }

    /**
     * Updates a part at the selected index.
     *
     * @param index the index of the part to be updated
     * @param selectedPart the updated part
     */
    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }

    /**
     * Updates a product at the selected index.
     *
     * @param index the index of the part to be updated
     * @param newProduct the updated product
     */
    public static void updateProduct(int index, Product newProduct) {
        allProducts.set(index, newProduct);
    }

    /**
     * Deletes a part from the inventory.
     *
     * @param selectedPart the part to be deleted
     * @return boolean representing whether the deletion was successful
     */
    public static boolean deletePart (Part selectedPart) {
        return allParts.remove(selectedPart);
    }

    /**
     * Deletes a product from the inventory.
     *
     * @param selectedProduct the product to be deleted
     * @return boolean representing whether the deletion was successful
     */
    public static boolean deleteProduct (Product selectedProduct) {
        return allProducts.remove(selectedProduct);
    }

    /**
     * Returns the full inventory of parts.
     *
     * @return the list of all parts
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * Returns the full inventory of products.
     *
     * @return the list of all products
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }

    /**
     * Generates a unique ID for a Part or Project.
     *
     * @return the ID
     */
    public static int getAutoID() {
        autoID = autoID + 1;
        return autoID;
    }

    /**
     * Returns the index of a selected Part.
     *
     * @param part the selected part
     * @return the index of the part
     */
    public static int getPartIndex(Part part) {
        return allParts.indexOf(part);
    }

    /**
     * Returns the index of a selected Product.
     *
     * @param product the selected part
     * @return the index of the part
     */
    public static int getProductIndex(Product product) {
        return allProducts.indexOf(product);
    }
}
