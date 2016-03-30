package cz.muni.fi.pv168.hotelmanager.backend;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import javax.sql.DataSource;

public class RentManagerImplTest extends TestsCommon {
    private RentManager rentManager;
    private GuestManager guestManager;
    private RoomManager roomManager;
    private DataSource source;
    private final LocalDate NOW = LocalDate.now();

    @Before
    public void setUp() throws SQLException
    {
    	source = getDataSource();
    	createTables(source);
        rentManager = new RentManagerImpl(source);
        guestManager = new GuestManagerImpl(source);
        roomManager = new RoomManagerImpl(source);
    }
    
    @After
    public void tearDown()
    {
    	dropTables(source);
    }

    @Test
    public void createRent()
    {
        Room room = new Room(Long.valueOf(42), 3, true, BigDecimal.valueOf(50));
        Guest guest = new Guest("a", "b", "c");
        try {
            Rent rent = new Rent(NOW, NOW.plusDays(3), room, guest);
            rentManager.createRent(rent);
            fail("createRent should throw exception because room and guest ids are null");
        }
        catch (IllegalArgumentException e) {
        }

        roomManager.createRoom(room);
        guestManager.createGuest(guest);
        Rent rent = new Rent(NOW, NOW.plusDays(3), room, guest);
        rentManager.createRent(rent);
        Rent r = rentManager.getRentById(rent.getID());
        assertEquals(rent, r);
        assertNotSame(rent, r);
    }

    @Test
    public void updateRent()
    {
        Room room = new Room(Long.valueOf(42), 3, true, BigDecimal.valueOf(50));
        Guest guest = new Guest("a", "b", "c");
        roomManager.createRoom(room);
        guestManager.createGuest(guest);
        Rent rent = new Rent(NOW, NOW.plusDays(3), room, guest);
        rentManager.createRent(rent);
        Rent r = rentManager.getRentById(rent.getID());
        
        assertEquals(rent, r);
        
        rent.setStartDate(NOW.plusDays(1));
        rent.setEndDate(NOW.plusDays(7));
        guest = new Guest("d", "e", "f");
        guestManager.createGuest(guest);
        rent.setGuest(guest);
        room = new Room(Long.valueOf(5), 2, false, BigDecimal.valueOf(10));
        roomManager.createRoom(room);
        rent.setRoom(room);
        
        rentManager.updateRent(rent);
        r = rentManager.getRentById(rent.getID());
        
        assertEquals(rent, r);
        assertNotSame(rent, r);
        assertDeepEquals(rent, r);
    }

    @Test
    public void deleteRent()
    {
        Room room = new Room(Long.valueOf(42), 3, true, BigDecimal.valueOf(50));
        Guest guest = new Guest("a", "b", "c");
        roomManager.createRoom(room);
        guestManager.createGuest(guest);
        Rent rent = new Rent(NOW, NOW.plusDays(3), room, guest);
        rentManager.createRent(rent);
        rent.setID(null);
        rentManager.createRent(rent);
        List<Rent> list = rentManager.getAllRents();
        assertEquals(list.size(), 2);
        rentManager.deleteRent(rent);
        list = rentManager.getAllRents();
        assertEquals(list.size(), 1);
    }
    
    @Test
    public void getAllRents()
    {
    	Room room1 = new Room(Long.valueOf(1), 2, false, BigDecimal.valueOf(100));
    	roomManager.createRoom(room1);
    	Guest guest1 = new Guest("a", "b", "c");
    	guestManager.createGuest(guest1);
    	Rent r1 = new Rent(NOW, NOW.plusDays(1), room1, guest1);
    	rentManager.createRent(r1);
    	
    	Room room2 = new Room(Long.valueOf(3), 4, true, BigDecimal.valueOf(1000));
    	roomManager.createRoom(room2);
    	Guest guest2 = new Guest("d", "e", "f");
    	guestManager.createGuest(guest2);
    	Rent r2 = new Rent(NOW, NOW.plusDays(2), room2, guest2);
    	rentManager.createRent(r2);
    	
    	List<Rent> res = rentManager.getAllRents();
    	assertEquals(res.size(), 2);
    	assertDeepEquals(r1, res.get(0));
    	assertDeepEquals(r2, res.get(1));
    }
    
    @Test
    public void getRentByGuest()
    {
    	Room room1 = new Room(Long.valueOf(1), 2, false, BigDecimal.valueOf(100));
    	roomManager.createRoom(room1);
    	Guest guest1 = new Guest("a", "b", "c");
    	guestManager.createGuest(guest1);
    	Rent r1 = new Rent(NOW, NOW.plusDays(1), room1, guest1);
    	rentManager.createRent(r1);
    	
    	Room room2 = new Room(Long.valueOf(3), 4, true, BigDecimal.valueOf(1000));
    	roomManager.createRoom(room2);
    	Guest guest2 = new Guest("d", "e", "f");
    	guestManager.createGuest(guest2);
    	Rent r2 = new Rent(NOW, NOW.plusDays(2), room2, guest2);
    	rentManager.createRent(r2);
    	
    	List<Rent> res = rentManager.getRentByGuest(guest1.getID());
    	assertEquals(res.size(), 1);
    	assertEquals(guest1, res.get(0).getGuest());
    }
    
    @Test
    public void getRentByRoom()
    {
    	Room room1 = new Room(Long.valueOf(1), 2, false, BigDecimal.valueOf(100));
    	roomManager.createRoom(room1);
    	Guest guest1 = new Guest("a", "b", "c");
    	guestManager.createGuest(guest1);
    	Rent r1 = new Rent(NOW, NOW.plusDays(1), room1, guest1);
    	rentManager.createRent(r1);
    	
    	Room room2 = new Room(Long.valueOf(3), 4, true, BigDecimal.valueOf(1000));
    	roomManager.createRoom(room2);
    	Guest guest2 = new Guest("d", "e", "f");
    	guestManager.createGuest(guest2);
    	Rent r2 = new Rent(NOW, NOW.plusDays(2), room2, guest2);
    	rentManager.createRent(r2);
    	
    	List<Rent> res = rentManager.getRentByRoom(room1.getID());
    	assertEquals(res.size(), 1);
    	assertEquals(room1, res.get(0).getRoom());
    }
    
    @Test
    public void getRentByDate()
    {
    	Room room1 = new Room(Long.valueOf(1), 2, false, BigDecimal.valueOf(100));
    	roomManager.createRoom(room1);
    	Guest guest1 = new Guest("a", "b", "c");
    	guestManager.createGuest(guest1);
    	Rent r1 = new Rent(NOW, NOW.plusDays(3), room1, guest1);
    	rentManager.createRent(r1);
    	
    	Room room2 = new Room(Long.valueOf(3), 4, true, BigDecimal.valueOf(1000));
    	roomManager.createRoom(room2);
    	Guest guest2 = new Guest("d", "e", "f");
    	guestManager.createGuest(guest2);
    	Rent r2 = new Rent(NOW.minusDays(3), NOW.plusDays(5), room2, guest2);
    	rentManager.createRent(r2);

    	Room room3 = new Room(Long.valueOf(4), 5, true, BigDecimal.valueOf(10020));
    	roomManager.createRoom(room3);
    	Guest guest3 = new Guest("g", "h", "i");
    	guestManager.createGuest(guest3);
    	Rent r3 = new Rent(NOW.minusDays(10), NOW.minusDays(7), room3, guest3);
    	rentManager.createRent(r3);
    	
    	List<Rent> res = rentManager.getRentByDate(NOW.minusDays(2), NOW.plusDays(4));
    	assertEquals(res.size(), 2);
    	assertEquals(r1, res.get(0));
    	assertEquals(r2, res.get(1));
    }

    private void assertDeepEquals(Rent expected, Rent real) {
        assertEquals("id value is not equal", expected.getID(), real.getID());
        assertEquals("start date is not equal", expected.getStartDate(), real.getStartDate());
        assertEquals("end date is not equal", expected.getEndDate(), real.getEndDate());
        assertEquals("room is not equal", expected.getRoom(), real.getRoom());
    }
}
