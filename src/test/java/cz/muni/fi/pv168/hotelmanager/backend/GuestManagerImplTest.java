package cz.muni.fi.pv168.hotelmanager.backend;

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
        assertNotNull(guest.getID());

        Guest g = manager.getGuestByID(guest.getID());
        assertEquals(guest, g);
        assertNotSame(guest, g);
        assertDeepEquals(guest, g);
    }

    @Test(expected = Exception.class)
    public void getGuestById()
    {
        manager.getGuestByID(new Long(0));
    }

    @Test
    public void deleteGuest()
    {
        Guest guest = new Guest("John Smith", "777", "Brno");
        assertNull(guest.getID());
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
        guest = manager.getGuestByID(guest.getID());
        assertEquals(guest.getName(), "bla");
        assertEquals(guest.getPhone(), "ble");
        assertEquals(guest.getAddress(), "bli");
        guest.setName("a");
        guest.setPhone("b");
        guest.setAddress("c");
        manager.updateGuest(guest);
        guest = manager.getGuestByID(guest.getID());
        assertEquals(guest.getName(), "a");
        assertEquals(guest.getPhone(), "b");
        assertEquals(guest.getAddress(), "c");
    }

    private void assertDeepEquals(Guest expected, Guest real)
    {
        assertEquals(expected.getID(), real.getID());
    }
}
