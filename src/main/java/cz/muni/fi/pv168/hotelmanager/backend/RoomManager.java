package cz.muni.fi.pv168.hotelmanager.backend;

import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Matlafous
 */
public interface RoomManager {

    /**
     * Creates a new room in database
     * @param room
     */

    public void createRoom(Room room);

    /**
     * Gets a room by id
     * @param id
     * @return
     */

    public Room getRoomByID(Long id);

    /**
     * Gets list of rooms with given number
     * @param number
     * @return
     */

    public List<Room> getRoomByNumber(Long number);

    /**
     * Gets list of all rooms
     * @return
     */

    public List<Room> getAllRooms();

    /**
     * Updates room informations
     * @param room
     */

    public void updateRoom(Room room);

    /**
     * Deletes a room from a database
     * @param room
     */

    public void deleteRoom(Room room);
    
    /**  
     * Gets all currently empty rooms
     * @param startDate
     * @param endDate
     * @return
     */
	public List<Room> getFreeRooms(LocalDate startDate, LocalDate endDate);
}
