package cz.muni.fi.pv168.hotelmanager.backend;

import java.time.LocalDate;

public class Rent {
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Guest getGuest() {
        return guest;
    }

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

    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private Room room;
    private Guest guest;

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
