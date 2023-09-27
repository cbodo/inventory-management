package com.cbodo.inventorymanagement.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Product class models a product.
 * Contains associated parts.
 *
 * @author Craig Bodo
 */
public class Product {

    /**
     * List of associated parts product object contains
     */
    private final ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    /**
     * id of product
     */
    private int id;
    /**
     * Product name
     */
    private String name;
    /**
     * Product price
     */
    private double price;
    /**
     * Product inventory level
     */
    private int stock;
    /**
     * Minimum level of inventory for product
     */
    private int min;
    /**
     * Maximum level of inventory for product
     */
    private int max;

    /**
     * Product class constructor.
     * @param id Product id.
     * @param name Product name.
     * @param price Product price.
     * @param stock Product stock.
     * @param min Product stock minimum.
     * @param max Product stock maximum.
     */
    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * @return Product id.
     */
    public int getId() {
        return id;
    }

    /**
     * @param id Product id to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return Product name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name Product name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Product price.
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price Product price to set.
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @return Product stock.
     */
    public int getStock() {
        return stock;
    }

    /**
     * @param stock Product stock to set.
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * @return Product minimum level.
     */
    public int getMin() {
        return min;
    }

    /**
     * @param min Product minimum level to set.
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * @return Product maximum level.
     */
    public int getMax() {
        return max;
    }

    /**
     * @param max Product maximum level to set.
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * Adds part to associated parts list.
     * @param part Part to add.
     */
    public void addAssociatedPart(Part part) {
        this.associatedParts.add(part);
    }

    /**
     * Deletes part from associated parts list.
     * @param selectedAssociatedPart Part to delete.
     * @return Boolean indicating if part was deleted.
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart) {
        for (Part part : getAllAssociatedParts()) {
            if (part.getId() == selectedAssociatedPart.getId()) {
                return getAllAssociatedParts().remove(part);
            }
        }
        return false;
    }

    /**
     * @return List of all associated parts.
     */
    public ObservableList<Part> getAllAssociatedParts() {
        return this.associatedParts;
    }
}
