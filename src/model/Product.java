/**
 *
 * @author Tory Fitzgerald, id: 000559078
 */

package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Product {

    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    int id;
    String name;
    double price;
    int stock;
    int min;
    int max;

    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * @param id the id
     */
    public void setID(int id) {
        this.id = id;
    }

    /**
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param stock the stock
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * @param price the price
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @param min the min
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * @param max the max
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * @return the id
     */
    public int getID() {
        return id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the price
     * */
    public double getPrice() {
        return price;
    }

    /**
     * @return the stock
     */
    public int getStock() {
        return stock;
    }

    /**
     * @return the min
     */
    public int getMin() {
        return min;
    }

    /**
     * @return the max
     */
    public int getMax() {
        return max;
    }

    /**
     * Adds an associated part to a list of parts associated with a Product.
     *
     * @param part the associated part to be added
     */
    public void addAssociatedPart(Part part) {
        associatedParts.add(part);
    }

    /**
     * Deletes an associated part from a list of parts associated with a Product.
     *
     * @param selectedAssociatedPart the associated part to be deleted
     * @return boolean reflecting whether deletion was successful
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart) {
        return associatedParts.remove(selectedAssociatedPart);
    }

    /**
     * Returns the list of all parts associated with a product.
     *
     * @return the list of all associated parts
     */
    public ObservableList<Part> getAssociatedParts(){
        return associatedParts;
    }
}
