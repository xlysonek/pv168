package cz.muni.fi.pv168.hotelmanager.backend;


import java.math.BigDecimal;

/**
 *
 * @author Matlafous
 */
public class Room {
    private Long id;
    private Long number;
    private int capacity;
    private boolean service;
    private BigDecimal price;

    /** Constructor for room */
    public Room() {}

    /**
     * Constructor for room
     * @param number
     * @param capacity
     * @param service
     * @param price
     */
    public Room(Long number, int capacity, boolean service, BigDecimal price) {
        this.number = number;     
        this.capacity = capacity;
        this.service = service;
        this.price = price;
    }

    /**
     * Setter for room ID
     */
    public void setID(long id)
    {
        this.id = id;
    }

    /**
     * Setter for room number
     * @param number
     */
    public void setNumber(long number){
        this.number = number;
    }

    /**
     * Setter for room capacity
     * @param capacity
     */
    public void setCapacity(int capacity){
        this.capacity = capacity;
    }

    /**
     * Setter for room service
     * @param service
     */
    public void setService(boolean service){
        this.service = service;
    }

    /**
     * Setter for room price
     * @param price
     */
    public void setPrice(BigDecimal price){
        this.price = price;
    }

    /**
     * Getter for room ID
     * @return
     */
    public Long getID(){
        return this.id;
    }

    /**
     * Getter for room number
     * @return
     */
    public long getNumber(){
        return this.number;
    }

    /**
     * Getter for room capacity
     * @return
     */
    public int getCapacity(){
        return this.capacity;
    }

    /**
     * Getter for room service
     * @return
     */
    public boolean getService(){
        return this.service;
    }

    /**
     * Getter for room price
     * @return
     */
    public BigDecimal getPrice(){
        return this.price;
    }

    /**
     * Equals method for room entity
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj){
        if (!(obj instanceof Room) || (this == null)){
            return false;
        }else {
            Room room = (Room) obj;
            return this.id.equals(room.id);
        }
    }

    /**
     * Generated hashCode
     * @return
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + this.id.hashCode();
        return hash;
    }

    /**
     * Method that returns String with all the information about a specific entity
     * @return
     */
    @Override
    public String toString(){
        return "Room ID:" + this.id + "\nRoom number:" + this.number + "\nRoom capacity"
                + this.capacity + "\n Room service:" + this.service + "\n Room price"
                + this.price;
    }
}