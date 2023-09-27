package com.cbodo.inventorymanagement.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Inventory class models inventory of Parts and Products
 *
 * @author Craig Bodo
 */
public class Inventory {

    /**
     * List of all parts in inventory
     */
    private static final ObservableList<Part> allParts = FXCollections.observableArrayList();
    /**
     * List of all products in inventory
     */
    private static final ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * Adds new part to inventory.
     * @param newPart Part to add.
     */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }
    /**
     * Adds new product to inventory.
     * @param newProduct Product to add.
     */
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    /**
     * Finds part(s) in inventory matching part id.
     * @param partId ID of part to find.
     * @return Boolean indicating if input was found.
     */
    public static Part lookupPart(int partId) {
        for (Part part : getAllParts()) {
            if (part.getId() == partId) {
                return part;
            }
        }
        return null;
    }

    /**
     * Finds product(s) in inventory matching product id.
     * @param productId ID of product to find.
     * @return Boolean indicating if input was found.
     */
    public static Product lookupProduct(int productId) {
        for (Product product : getAllProducts()) {
            if (product.getId() == productId) {
                return product;
            }
        }
        return null;
    }

    /**
     * Finds part(s) in inventory matching part name.
     * @param partName Part name to find.
     * @return Boolean indicating if input was found.
     */
    public static ObservableList<Part> lookupPart(String partName) {
        ObservableList<Part> allFilteredParts = FXCollections.observableArrayList();

        for (Part part : getAllParts()) {
            if (part.getName().toLowerCase().contains(partName.toLowerCase())) {
                allFilteredParts.add(part);
            }
        }
        if (allFilteredParts.isEmpty()) {
            return null;
        }

        return allFilteredParts;
    }

    /**
     * Finds product(s) in inventory matching product name.
     * @param productName Product name to find.
     * @return Boolean indicating if input was found.
     */
    public static ObservableList<Product> lookupProduct(String productName) {
        ObservableList<Product> allFilteredProducts = FXCollections.observableArrayList();

        for (Product product : getAllProducts()) {
            if (product.getName().toLowerCase().contains(productName.toLowerCase())) {
                allFilteredProducts.add(product);
            }
        }
        if (allFilteredProducts.isEmpty()) {
            return null;
        }

        return allFilteredProducts;
    }

    /**
     * Updates modified part in inventory.
     * @param index Index of selected part.
     * @param selectedPart The modified part.
     */
    public static void updatePart(int index, Part selectedPart) {
        getAllParts().set(index, selectedPart);
    }

    /**
     * Updates modified product in inventory.
     * @param index Index of selected product.
     * @param selectedProduct The modified product.
     */
    public static void updateProduct(int index, Product selectedProduct) {
        getAllProducts().set(index, selectedProduct);
    }

    /**
     * Deletes selected part from inventory.
     * @param selectedPart Part to delete.
     * @return Boolean indicating if part was deleted.
     */
    public static boolean deletePart(Part selectedPart) {
        for (Part part : getAllParts()) {
            if (part.getId() == selectedPart.getId()) {
                return getAllParts().remove(part);
            }
        }
        return false;
    }

    /**
     * Deletes selected product from inventory.
     * @param selectedProduct Product to delete.
     * @return Boolean indicating if part was deleted.
     */
    public static boolean deleteProduct(Product selectedProduct) {
        for (Product product : getAllProducts()) {
            if (product.getId() == selectedProduct.getId()) {
                return getAllProducts().remove(product);
            }
        }
        return false;
    }

    /**
     * @return All parts in inventory.
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * @return All products in inventory.
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }

}
