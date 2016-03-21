package cz.muni.fi.pv168.hotelmanager.backend;


import java.math.BigDecimal;

public class Room {
    public Room(Long number, Long capacity, boolean service, BigDecimal price) {
        this.number = number;
        this.capacity = capacity;
        this.service = service;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Long getCapacity() {
        return capacity;
    }

    public void setCapacity(Long capacity) {
        this.capacity = capacity;
    }

    public boolean isService() {
        return service;
    }

    public void setService(boolean service) {
        this.service = service;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    private Long id;
    private Long number;
    private Long capacity;
    private boolean service;
    private BigDecimal price;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Room room = (Room) o;

        if (service != room.service) return false;
        if (capacity != null ? !capacity.equals(room.capacity) : room.capacity != null) return false;
        if (id != null ? !id.equals(room.id) : room.id != null) return false;
        if (number != null ? !number.equals(room.number) : room.number != null) return false;
        if (price != null ? !price.equals(room.price) : room.price != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (number != null ? number.hashCode() : 0);
        result = 31 * result + (capacity != null ? capacity.hashCode() : 0);
        result = 31 * result + (service ? 1 : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        return result;
    }
}
