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
        Room room = new Room(Long.valueOf(42), 3, true, BigDecimal.valueOf(50));
        Guest guest = new Guest("a", "b", "c");
        try {
            Rent rent = new Rent(LocalDate.now(), LocalDate.now().plusDays(3), room, guest);
            fail("Rent constructor should throw exception because room and guest ids are null");
        }
        catch (Exception e) {
        }

        room.setID(Long.valueOf(1));
        guest.setID(Long.valueOf(2));
        Rent rent = new Rent(LocalDate.now(), LocalDate.now().plusDays(3), room, guest);
        manager.createRent(rent);
        Rent r = manager.getRentById(rent.getID());
        assertEquals(rent, r);
        assertNotSame(rent, r);
    }

    @Test
    public void updateRent()
    {
        Room room = new Room(Long.valueOf(42), 3, true, BigDecimal.valueOf(50));
        Guest guest = new Guest("a", "b", "c");
        room.setID(Long.valueOf(1));
        guest.setID(Long.valueOf(2));
        Rent rent = new Rent(LocalDate.now(), LocalDate.now().plusDays(3), room, guest);
        manager.createRent(rent);
        Rent r = manager.getRentById(rent.getID());
        assertEquals(rent, r);
        rent.setStartDate(LocalDate.now().plusDays(1));
        rent.setEndDate(LocalDate.now().plusDays(7));
        rent.setGuest(new Guest("d", "e", "f"));
        rent.setRoom(new Room(Long.valueOf(5), 2, false, BigDecimal.valueOf(10)));
        manager.updateRent(rent);
        r = manager.getRentById(rent.getID());
        assertEquals(rent, r);
        assertNotSame(rent, r);
        assertDeepEquals(rent, r);
    }

    @Test
    public void deleteRent()
    {
        Room room = new Room(Long.valueOf(42), 3, true, BigDecimal.valueOf(50));
        Guest guest = new Guest("a", "b", "c");
        room.setID(Long.valueOf(1));
        guest.setID(Long.valueOf(2));
        Rent rent = new Rent(LocalDate.now(), LocalDate.now().plusDays(3), room, guest);
        manager.createRent(rent);
        rent.setID(null);
        manager.createRent(rent);
        List<Rent> list = manager.getAllRents();
        assertEquals(list.size(), 2);
        manager.deleteRent(rent);
        list = manager.getAllRents();
        assertEquals(list.size(), 1);
    }

    private void assertDeepEquals(Rent expected, Rent real) {
        assertEquals("id value is not equal", expected.getID(), real.getID());
        assertEquals("start date is not equal", expected.getStartDate(), real.getStartDate());
        assertEquals("end date is not equal", expected.getEndDate(), real.getEndDate());
        assertEquals("room is not equal", expected.getRoom(), real.getRoom());
    }
}
