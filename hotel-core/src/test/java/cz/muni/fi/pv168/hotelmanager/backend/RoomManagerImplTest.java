package cz.muni.fi.pv168.hotelmanager.backend;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Matlafous
 */
public class RoomManagerImplTest extends TestsCommon {
	private DataSource source;
    private RoomManagerImpl manager;
    private final LocalDate NOW = LocalDate.now();
    private final double delta = 0.00001;
    
    
    @Before
    public void setUp() throws SQLException {
    	source = getDataSource();
    	createTables(source);
        manager = new RoomManagerImpl(source);
    }
    
    @After
    public void tearDown() throws DatabaseException {
    	dropTables(source);
    }

    @Test
    public void testCreateRoom(){
        Room room = newRoom(12l, 5, true, BigDecimal.valueOf(5000));
        manager.createRoom(room);

        Long roomId = room.getID();

        assertNotNull("created room has null id", roomId);

        Room result = manager.getRoomByID(roomId);

        assertThat("loaded room differs from the saved one", result, is(equalTo(room)));
        assertThat("loaded room is the same instance", result, is(not(sameInstance(room))));
        assertDeepEquals(room, result);

    }
    
    @Test
    public void testGetRoomByNumber() {
    	Room room = new Room(Long.valueOf(42), 3, false, BigDecimal.valueOf(100));
    	manager.createRoom(room);
    	List<Room> res = manager.getRoomByNumber(room.getNumber());
    	assertEquals(res.size(), 1);
    	assertDeepEquals(room, res.get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNull(){
        manager.createRoom(null);
    }

    @Test
    public void createRoomWithWrongValues(){
        Room room = newRoom(12l, -1, true, BigDecimal.valueOf(5000));

        try{
            manager.createRoom(room);
            fail("negative capacity not possible");
        }catch(IllegalArgumentException iae){
            //OK
        }
        room = newRoom(-1l,5,true, BigDecimal.valueOf(5000));
        try{
            manager.createRoom(room);
            fail("negative room number not possible");
        }catch(IllegalArgumentException iae){
            //OK
        }

        room = newRoom(1l,0,true, BigDecimal.valueOf(5000));
        try{
            manager.createRoom(room);
            fail("zero capacity not possible");
        }catch(IllegalArgumentException iae){
            //OK
        }

        room = newRoom(12l,5,true, BigDecimal.valueOf(-5000));
        try{
            manager.createRoom(room);
            fail("negative price not possible");
        }catch(IllegalArgumentException iae){
            //OK
        }

    }


    @Test
    public void getAllRoomsTest(){
        assertTrue(manager.getAllRooms().isEmpty());

        Room room1 = newRoom(21l, 5, true, BigDecimal.valueOf(1000));
        Room room2 = newRoom(10l, 2, false, BigDecimal.valueOf(800));

        manager.createRoom(room1);
        manager.createRoom(room2);

        List<Room> expected = Arrays.asList(room1, room2);
        List<Room> reality = manager.getAllRooms();

        assertEquals("saved and retrieved room list differs", expected, reality);
        assertDeepEquals(expected, reality);
    }

    @Test
    public void updateRoom(){
        Room room = newRoom(21l, 5, true, BigDecimal.valueOf(100));
        Room room2 = newRoom(20l, 4, false, BigDecimal.valueOf(90));

        manager.createRoom(room);
        manager.createRoom(room2);

        Long roomID = room.getID();
        room.setCapacity(666);
        manager.updateRoom(room);
        room = manager.getRoomByID(roomID);

        assertEquals("Room capacity was not changed.", room.getCapacity(), 666l);
        assertEquals("Room number was changed while changing capacity", room.getNumber(), 21l);
        assertEquals("Service was changed while changing capacity", room.getService(), true);
        assertTrue("Room price was changed while changing capacity", room.getPrice().compareTo(BigDecimal.valueOf(100)) == 0);

        room.setNumber(6l);
        manager.updateRoom(room);
        room = manager.getRoomByID(roomID);

        assertEquals("Room capacity was changed while changing room number", room.getCapacity(), 666l);
        assertEquals("Room number was not changed.", room.getNumber(), 6l);
        assertEquals("Service was changed while changing room number", room.getService(), true);
        assertTrue("Room price was changed while changing capacity", room.getPrice().compareTo(BigDecimal.valueOf(100)) == 0);

        room.setService(false);
        manager.updateRoom(room);
        room = manager.getRoomByID(roomID);

        assertEquals("Room capacity was changed while changing service.", room.getCapacity(), 666l);
        assertEquals("Room number was changed while changing service", room.getNumber(), 6l);
        assertEquals("Service was not changed.", room.getService(), false);
        assertTrue("Room price was changed while changing capacity", room.getPrice().compareTo(BigDecimal.valueOf(100)) == 0);

        room.setPrice(BigDecimal.ZERO);
        manager.updateRoom(room);
        room = manager.getRoomByID(roomID);

        assertEquals("Room capacity was changed while changing price.", room.getCapacity(), 666l);
        assertEquals("Room number was changed while changing price", room.getNumber(), 6l);
        assertEquals("Service was changed while changing price", room.getService(), false);
        assertTrue("Room price was changed while changing capacity", room.getPrice().compareTo(BigDecimal.ZERO) == 0);

        assertDeepEquals(room2, manager.getRoomByID(room2.getID()));
    }

    @Test
    public void deleteRoom(){
        Room room1 = newRoom(21l, 5, true, BigDecimal.valueOf(1000));
        Room room2 = newRoom(22l, 4, false, BigDecimal.valueOf(90));

        manager.createRoom(room1);
        manager.createRoom(room2);

        assertNotNull(manager.getRoomByID(room1.getID()));
        assertNotNull(manager.getRoomByID(room2.getID()));

        manager.deleteRoom(room1);

        assertNull(manager.getRoomByID(room1.getID()));
        assertNotNull(manager.getRoomByID(room2.getID()));

    }
    
    @Test
    public void getFreeRooms() {
    	Room room1 = newRoom(21l, 5, true, BigDecimal.valueOf(1000));
        Room room2 = newRoom(22l, 4, false, BigDecimal.valueOf(90));
        manager.createRoom(room1);
        manager.createRoom(room2);

        Guest guest1 = new Guest("a", "b", "c");
        Guest guest2 = new Guest("d", "e", "f");
        GuestManager guestManager = new GuestManagerImpl(source);
        guestManager.createGuest(guest1);
        guestManager.createGuest(guest2);
        
        Rent rent1 = new Rent(NOW, NOW.plusDays(2), room1, guest1);
        Rent rent2 = new Rent(NOW.plusDays(4), NOW.plusDays(7), room2, guest2);
        RentManager rentManager = new RentManagerImpl(source);
        rentManager.createRent(rent1);
        rentManager.createRent(rent2);
        
        List<Room> res = manager.getFreeRooms(NOW, NOW.plusDays(3));
        assertEquals(res.size(), 1);
        assertDeepEquals(room2, res.get(0));
    }

    private static Room newRoom(Long number, int capacity, boolean service, BigDecimal price){
        Room room = new Room();
        room.setNumber(number);
        room.setCapacity(capacity);
        room.setPrice(price);
        room.setService(service);
        return room;
    }

    private void assertDeepEquals(Room expected, Room result) {
        assertEquals("id value differs",expected.getID(), result.getID());
        assertEquals("capacity value differs",expected.getCapacity(), result.getCapacity());
        assertTrue("price value differs",expected.getPrice().compareTo(result.getPrice()) == 0);
        assertEquals("service value differs",expected.getService(), result.getService());
        assertEquals("room number value differs",expected.getNumber(), result.getNumber());
    }

    private void assertDeepEquals(List<Room> expectedList, List<Room> realList){
        for (int i = 0; i < expectedList.size(); i++){
            Room expected = expectedList.get(i);
            Room reality = realList.get(i);
            assertDeepEquals(expected, reality);
        }
    }
}
