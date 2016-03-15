package cz.muni.fi.pv168.hotelmanager.backend;

import java.time.LocalDate;
import java.util.List;

public interface RoomManager {
    public void createRoom(Room room);

    public Room getRoomById(Long id);

    public List<Room> getRoomByNumber(Long number);

    public List<Room> getAllRooms();

    public void updateRoom(Room room);

    public void deleteRoom(Room room);

    public List<Room> getFreeRooms(LocalDate startDate, LocalDate endDate);
}
