package cz.muni.fi.pv168.hotelmanager.backend;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class RentManagerImplTest {
    RentManager manager;

    @Before
    public void setUp()
    {
        manager = new RentManagerImpl();
    }

    @Test
    public void createRent()
    {
        Room room = new Room(Long.valueOf(42), Long.valueOf(3), true, BigDecimal.valueOf(50));
        Guest guest = new Guest("a", "b", "c");
        try {
            Rent rent = new Rent(LocalDate.now(), LocalDate.now().plusDays(3), room, guest);
            fail("Rent constructor should throw exception because room and guest ids are null");
        }
        catch (Exception e) {
        }

        room.setId(Long.valueOf(1));
        guest.setId(Long.valueOf(2));
        Rent rent = new Rent(LocalDate.now(), LocalDate.now().plusDays(3), room, guest);
        manager.createRent(rent);
        Rent r = manager.getRentById(rent.getId());
        assertEquals(rent, r);
    }

    @Test
    public void updateRent()
    {
        Room room = new Room(Long.valueOf(42), Long.valueOf(3), true, BigDecimal.valueOf(50));
        Guest guest = new Guest("a", "b", "c");
        room.setId(Long.valueOf(1));
        guest.setId(Long.valueOf(2));
        Rent rent = new Rent(LocalDate.now(), LocalDate.now().plusDays(3), room, guest);
        manager.createRent(rent);
        Rent r = manager.getRentById(rent.getId());
        assertEquals(rent, r);
        rent.setStartDate(LocalDate.now().plusDays(1));
        rent.setEndDate(LocalDate.now().plusDays(7));
        rent.setGuest(new Guest("d", "e", "f"));
        rent.setRoom(new Room(Long.valueOf(5), Long.valueOf(2), false, BigDecimal.valueOf(10)));
        manager.updateRent(rent);
        r = manager.getRentById(rent.getId());
        assertEquals(rent, r);
    }

    @Test
    public void deleteRent()
    {
        Room room = new Room(Long.valueOf(42), Long.valueOf(3), true, BigDecimal.valueOf(50));
        Guest guest = new Guest("a", "b", "c");
        room.setId(Long.valueOf(1));
        guest.setId(Long.valueOf(2));
        Rent rent = new Rent(LocalDate.now(), LocalDate.now().plusDays(3), room, guest);
        manager.createRent(rent);
        rent.setId(null);
        manager.createRent(rent);
        List<Rent> list = manager.getAllRents();
        assertEquals(list.size(), 2);
        manager.deleteRent(rent);
        list = manager.getAllRents();
        assertEquals(list.size(), 1);
    }
}
