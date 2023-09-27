package com.cbodo.inventorymanagement.model;

/**
 * Outsourced class models an outsourced Part.
 *
 * @author Craig Bodo
 */
public class Outsourced extends Part {

    /**
     * Part's company name
     */
    private String companyName;

    /**
     * Outsourced class constructor.
     * @param id Part's id.
     * @param name Part's name.
     * @param price Part's price.
     * @param stock Part's inventory level.
     * @param min Part's inventory minimum.
     * @param max Part's inventory maximum.
     * @param companyName Part's company name.
     */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     * @return Part's company name.
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * @param companyName Part's company name to set.
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
