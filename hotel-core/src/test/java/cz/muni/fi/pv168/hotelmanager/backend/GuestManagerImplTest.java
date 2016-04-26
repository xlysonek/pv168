package cz.muni.fi.pv168.hotelmanager.backend;

import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GuestManagerImplTest extends TestsCommon {
    private GuestManager manager;
    private DataSource source;

    @Before
    public void setUp() throws SQLException
    {
        source = getDataSource();
        createTables(source);
        manager = new GuestManagerImpl(source);
    }
    
    @After
    public void tearDown() throws GuestManagerException {
    	dropTables(source);
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
        manager.getGuestByID(null);
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
    
    @Test
    public void getAllGuests()
    {
    	Guest g1 = new Guest("a", "b", "c");
    	Guest g2 = new Guest("d", "e", "f");
    	manager.createGuest(g1);
    	manager.createGuest(g2);
    	List<Guest> res = manager.getAllGuests();
    	assertEquals(res.size(), 2);
    	assertDeepEquals(g1, res.get(0));
    	assertDeepEquals(g2, res.get(1));
    	assertNotSame(g1, res.get(0));
    	assertNotSame(g2, res.get(1));
    }

    private void assertDeepEquals(Guest expected, Guest real)
    {
        assertEquals(expected.getID(), real.getID());
    }
}
