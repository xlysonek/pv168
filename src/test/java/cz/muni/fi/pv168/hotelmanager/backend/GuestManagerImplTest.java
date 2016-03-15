package cz.muni.fi.pv168.hotelmanager.backend;

import cz.muni.fi.pv168.hotelmanager.backend.Guest;
import cz.muni.fi.pv168.hotelmanager.backend.GuestManager;
import cz.muni.fi.pv168.hotelmanager.backend.GuestManagerImpl;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

public class GuestManagerImplTest {
    private GuestManager manager;

    @Before
    public void setUp()
    {
        manager = new GuestManagerImpl();
    }

    @Test
    public void createGuest()
    {
        Guest guest = new Guest("John Smith", "777", "Brno");
        manager.createGuest(guest);
        assertNotNull(guest.getId());

        Guest g = manager.getGuestById(guest.getId());
        assertEquals(guest, g);
    }

    @Test(expected = Exception.class)
    public void getGuestById()
    {
        manager.getGuestById(new Long(0));
    }

    @Test
    public void deleteGuest()
    {
        Guest guest = new Guest("John Smith", "777", "Brno");
        assertNull(guest.getId());
        manager.createGuest(guest);
        List<Guest> list = manager.getAllGuests();
        assertNotNull(list);
        assertEquals(list.size(), 1);
        manager.deleteGuest(guest);
        list = manager.getAllGuests();
        assertNotNull(list);
        assertEquals(list.size(), 0);
    }

    @Test
    public void updateGuest()
    {
        Guest guest = new Guest("bla", "ble", "bli");
        manager.createGuest(guest);
        guest = manager.getGuestById(guest.getId());
        assertEquals(guest.getName(), "bla");
        assertEquals(guest.getPhone(), "ble");
        assertEquals(guest.getAddress(), "bli");
        guest.setName("a");
        guest.setPhone("b");
        guest.setAddress("c");
        manager.updateGuest(guest);
        guest = manager.getGuestById(guest.getId());
        assertEquals(guest.getName(), "a");
        assertEquals(guest.getPhone(), "b");
        assertEquals(guest.getAddress(), "c");
    }
}
