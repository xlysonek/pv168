package cz.muni.fi.pv168.hotelmanager.backend;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.sql.DataSource;

public class RentManagerImplTest extends TestsCommon {
    private RentManager manager;
    private DataSource source;
    private final LocalDate NOW = LocalDate.now();

    @Before
    public void setUp() throws SQLException
    {
    	source = getDataSource();
    	createTables(source);
        manager = new RentManagerImpl(source);
    }

    @Test
    public void createRent()
    {
        Room room = new Room(Long.valueOf(42), 3, true, BigDecimal.valueOf(50));
        Guest guest = new Guest("a", "b", "c");
        try {
            Rent rent = new Rent(NOW, NOW.plusDays(3), room, guest);
            fail("Rent constructor should throw exception because room and guest ids are null");
        }
        catch (Exception e) {
        }

        room.setID(Long.valueOf(1));
        guest.setID(Long.valueOf(2));
        Rent rent = new Rent(NOW, NOW.plusDays(3), room, guest);
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
        Rent rent = new Rent(NOW, NOW.plusDays(3), room, guest);
        manager.createRent(rent);
        Rent r = manager.getRentById(rent.getID());
        assertEquals(rent, r);
        rent.setStartDate(NOW.plusDays(1));
        rent.setEndDate(NOW.plusDays(7));
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
        Rent rent = new Rent(NOW, NOW.plusDays(3), room, guest);
        manager.createRent(rent);
        rent.setID(null);
        manager.createRent(rent);
        List<Rent> list = manager.getAllRents();
        assertEquals(list.size(), 2);
        manager.deleteRent(rent);
        list = manager.getAllRents();
        assertEquals(list.size(), 1);
    }
    
    @Test
    public void getAllRents()
    {
    	Rent r1 = new Rent(NOW, NOW.plusDays(1),
				new Room(Long.valueOf(1), 2, false, BigDecimal.valueOf(100)),
				new Guest("a", "b", "c"));
    	Rent r2 = new Rent(NOW, NOW.plusDays(2),
				new Room(Long.valueOf(3), 4, true, BigDecimal.valueOf(1000)),
				new Guest("d", "e", "f"));
    	manager.createRent(r1);
    	manager.createRent(r2);
    	List<Rent> res = manager.getAllRents();
    	assertEquals(res.size(), 2);
    	assertDeepEquals(r1, res.get(0));
    	assertDeepEquals(r2, res.get(1));
    }
    
    @Test
    public void getRentByGuest()
    {
    	Guest guest = new Guest("a", "b", "c");
    	Rent r1 = new Rent(NOW, NOW.plusDays(2), new Room(Long.valueOf(0), 1, false, BigDecimal.ONE.valueOf(3)), guest);
    	Rent r2 = new Rent(NOW, NOW.plusDays(2),
				new Room(Long.valueOf(3), 4, true, BigDecimal.valueOf(1000)),
				new Guest("d", "e", "f"));
    	manager.createRent(r1);
    	manager.createRent(r2);
    	List<Rent> res = manager.getRentByGuest(guest.getID());
    	assertEquals(res.size(), 1);
    	assertEquals(guest, res.get(0).getGuest());
    }
    
    @Test
    public void getRentByRoom()
    {
    	
    }

    private void assertDeepEquals(Rent expected, Rent real) {
        assertEquals("id value is not equal", expected.getID(), real.getID());
        assertEquals("start date is not equal", expected.getStartDate(), real.getStartDate());
        assertEquals("end date is not equal", expected.getEndDate(), real.getEndDate());
        assertEquals("room is not equal", expected.getRoom(), real.getRoom());
    }
}
