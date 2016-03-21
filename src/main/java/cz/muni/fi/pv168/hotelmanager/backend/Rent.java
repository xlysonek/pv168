package cz.muni.fi.pv168.hotelmanager.backend;

import java.time.LocalDate;

public class Rent {
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private Room room;
    private Guest guest;

    /**
     * Getter for rent id
     * @return
     */
    public Long getID() {
        return id;
    }

    /**
     * Setter for rent id
     * @paarm id
     */
    public void setID(Long id) {
        this.id = id;
    }

    /**
     * Getter for date of beginning of accommodation
     * @return
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Setter for date of beginning of accommodation
     * @param startDate
     */
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    /**
     * Getter for date of end of accommodation
     * @return
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Setter for date of end of accommodation
     * @param endDate
     */
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    /**
     * Getter for room
     * @return
     */
    public Room getRoom() {
        return room;
    }

    /**
     * Setter for room
     * @param room
     */
    public void setRoom(Room room) {
        this.room = room;
    }

    /**
     * Getter for guest
     * @return
     */
    public Guest getGuest() {
        return guest;
    }

    /**
     * Setter for guest
     * @param guest
     */
    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public Rent(LocalDate startDate, LocalDate endDate, Room room, Guest guest)
    {
        this.startDate = startDate;
        this.endDate = endDate;
        this.room = room;
        this.guest = guest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rent rent = (Rent) o;

        if (endDate != null ? !endDate.equals(rent.endDate) : rent.endDate != null) return false;
        if (guest != null ? !guest.equals(rent.guest) : rent.guest != null) return false;
        if (id != null ? !id.equals(rent.id) : rent.id != null) return false;
        if (room != null ? !room.equals(rent.room) : rent.room != null) return false;
        if (startDate != null ? !startDate.equals(rent.startDate) : rent.startDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        result = 31 * result + (room != null ? room.hashCode() : 0);
        result = 31 * result + (guest != null ? guest.hashCode() : 0);
        return result;
    }
}
