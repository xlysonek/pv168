package cz.muni.fi.pv168.hotelmanager.backend;

import java.time.LocalDate;
import java.util.List;

public interface RentManager {
    public void createRent(Rent rent);

    public Rent getRentById(Long id);

    public void updateRent(Rent rent);

    public void deleteRent(Rent rent);

    public List<Rent> getRentByGuest(Long guestId);

    public List<Rent> getRentByGuest(Long guestId, LocalDate startDate, LocalDate endDate);

    public List<Rent> getRentByRoom(Long roomId);

    public List<Rent> getRentByRoom(Long roomId, LocalDate startDate, LocalDate endDate);

    public List<Rent> getRentByDate(LocalDate startDate, LocalDate endDate);

    public List<Rent> getAllRents();
}
