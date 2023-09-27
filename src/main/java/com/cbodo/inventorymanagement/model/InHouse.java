package com.cbodo.inventorymanagement.model;

/**
 * InHouse class models an in-house Part.
 *
 * @author Craig Bodo
 */
public class InHouse extends Part {

    /**
     * Part Machine ID
     */
    private int machineId;

    /**
     * InHouse Class constructor.
     * @param id Part id.
     * @param name Part name.
     * @param price Part price.
     * @param stock Part inventory level.
     * @param min Part inventory minimum.
     * @param max Part inventory maximum.
     * @param machineId Part machine id.
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /**
     * @param machineId Part's machine id to set.
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }

    /**
     * @return Parts's machine id.
     */
    public int getMachineId() {
        return machineId;
    }

}
