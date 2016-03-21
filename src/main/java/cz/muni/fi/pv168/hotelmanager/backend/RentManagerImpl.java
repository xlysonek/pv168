package cz.muni.fi.pv168.hotelmanager.backend;

import java.time.LocalDate;
import java.util.List;

public class RentManagerImpl implements RentManager {
    @Override
    public void createRent(Rent rent) {

    }

    @Override
    public Rent getRentById(Long id) {
        return null;
    }

    @Override
    public void updateRent(Rent rent) {

    }

    @Override
    public void deleteRent(Rent rent) {

    }

    @Override
    public List<Rent> getRentByGuest(Long guestId) {
        return null;
    }

    @Override
    public List<Rent> getRentByGuest(Long guestId, LocalDate startDate, LocalDate endDate) {
        return null;
    }

    @Override
    public List<Rent> getRentByRoom(Long roomId) {
        return null;
    }

    @Override
    public List<Rent> getRentByRoom(Long roomId, LocalDate startDate, LocalDate endDate) {
        return null;
    }

    @Override
    public List<Rent> getRentByDate(LocalDate startDate, LocalDate endDate) {
        return null;
    }

    @Override
    public List<Rent> getAllRents() {
        return null;
    }
}
