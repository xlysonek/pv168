package cz.muni.fi.pv168.hotelmanager.backend;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;


public class RoomManagerImpl implements RoomManager {

    private final DataSource dataSource;
    private final String dateSubquery = "NOT ((startDate < ? AND endDate < ?) OR (startDate > ? AND endDate > ?))";

    public RoomManagerImpl(DataSource dataSource){
        this.dataSource = dataSource;
    }

    @Override
    public void createRoom(Room room) {
        validate(room);
        if (room.getID() != null){
            throw new IllegalArgumentException("Room id has already been used.");
        }

        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO room (number, capacity, service, price) VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(1, room.getNumber());
            statement.setInt(2, room.getCapacity());
            statement.setBoolean(3, room.getService());
            statement.setBigDecimal(4, room.getPrice());

            int count = statement.executeUpdate();
            if (count != 1){
                throw new RoomManagerException("");
            }

            ResultSet key = statement.getGeneratedKeys();
            room.setID(getKey(key, room));

        }catch (SQLException ex){
            throw new RoomManagerException("Room insertion was interupted by an error." + room + ex, ex);
        }
    }

    @Override
    public Room getRoomByID(Long id) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT id, number, capacity, service, price FROM room WHERE id = ?")){

            statement.setLong(1, id);
            ResultSet table = statement.executeQuery();

            if (table.next()){
                Room room = resultSetToRoom(table);

                if (table.next()){
                    throw new RoomManagerException("Internal error:" +
                            "More entities with the same id found" + id +
                            File.separator + " Entities found:" + room + "," + resultSetToRoom(table));
                }

                return room;
            }else{
                return null;
            }


        } catch (SQLException ex) {
            throw new RoomManagerException("There has been an error while retrieving "
                    + "entity with given ID" + id, ex);
        }
    }

    @Override
    public void updateRoom(Room room){
        validate(room);

        if (room.getID() == null){
            throw new IllegalArgumentException("updated data are invalid. Room id doesnt exist.");
        }
        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("UPDATE room SET"
                        + " number = ?, capacity = ?, service = ?, price = ? WHERE id = ?")){

            statement.setLong(1, room.getNumber());
            statement.setInt(2, room.getCapacity());
            statement.setBoolean(3, room.getService());
            statement.setBigDecimal(4, room.getPrice());
            statement.setLong(5, room.getID());

            int count = statement.executeUpdate();
            if (count == 0){
                throw new RoomManagerException("Room" + room + "not found in database.");
            } else if (count != 1){
                throw new RoomManagerException("Invalid updated rows number " + count);
            }


        } catch (SQLException ex) {
            throw new RoomManagerException("There has been an error while updating a room "+ room, ex);
        }

    }

    @Override
    public void deleteRoom(Room room) {
        if (room == null){
            throw new IllegalArgumentException("Room doesnt exist.");
        }
        if (room.getID() == null){
            throw new IllegalArgumentException("Room id hasnt been initialized.");
        }
        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "DELETE FROM room WHERE id = ?")){

            statement.setLong(1, room.getID());

            int count = statement.executeUpdate();
            if (count == 0){
                throw new RoomManagerException("Room doesnt exist in database" + room);
            } else if (count != 1){
                throw new RoomManagerException("Invalid deleted number of rows" + count);
            }

        } catch (SQLException ex) {
            throw new RoomManagerException("There has been an error while deleting a room" + room, ex);
        }

    }

    @Override
    public List<Room> getRoomByNumber(Long number) {
        try( Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT id, number, capacity, service, price FROM room WHERE number = ?")){

        	statement.setLong(1, number);
            ResultSet table = statement.executeQuery();

            List<Room> result = new ArrayList<>();
            while(table.next()){
                result.add(resultSetToRoom(table));
            }
            return result;

        }catch (SQLException ex) {
            throw new RoomManagerException(" There has been an error while getting all rooms"
                    + " with given number");
        }
    }

    @Override
    public List<Room> getAllRooms() {
        try( Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT id, number, capacity, service, price FROM room")){

            ResultSet table = statement.executeQuery();

            List<Room> result = new ArrayList<>();
            while(table.next()){
                result.add(resultSetToRoom(table));
            }
            return result;

        }catch (SQLException ex) {
            throw new RoomManagerException(" There has been an error while getting all rooms");
        }
    }

    @Override
    public List<Room> getFreeRooms(LocalDate startDate, LocalDate endDate) {
            if (startDate == null){
                throw new IllegalArgumentException("startDate is null");
            }
            if (endDate == null){
                throw new IllegalArgumentException("endDate is null");
            }
            if (startDate.isAfter(endDate)){
                throw new IllegalArgumentException("startDate and endDate differs by negative value.");
            }

            try (Connection connection = dataSource.getConnection();
                    PreparedStatement statement = connection.prepareStatement(
                            "SELECT id, number, capacity, service, price FROM room "
                          + "WHERE id NOT IN (SELECT roomID FROM rent WHERE "
                          + dateSubquery + ")")) {

            	statement.setDate(1, java.sql.Date.valueOf(startDate));
                statement.setDate(2, java.sql.Date.valueOf(endDate));
            	statement.setDate(3, java.sql.Date.valueOf(startDate));
                statement.setDate(4, java.sql.Date.valueOf(endDate));

                ResultSet resultSet = statement.executeQuery();
                List<Room> result = new ArrayList<>();

                while(resultSet.next()){
                    result.add(resultSetToRoom(resultSet));
                }
                return result;
            } catch (SQLException ex) {
            throw new DatabaseException("Error when retrieving list of rents by date: " + ex, ex);
        }
    }

    private void validate(Room room) throws IllegalArgumentException {
        if (room == null) {
            throw new IllegalArgumentException("Room wasnt initialized.");
        }
        if (room.getCapacity() <= 0){
            throw new IllegalArgumentException("Room capacity cannot have zero or negative value.");
        }
        if (room.getNumber() < 0){
            throw new IllegalArgumentException("Room number has to be higher than zero.");
        }
        if (room.getPrice().signum() < 0){
            throw new IllegalArgumentException("Room price cannot be a negative number");
        }

    }

    private long getKey(ResultSet key, Room room) throws SQLException {
        if (key.next()){
            if (key.getMetaData().getColumnCount() != 1){
                throw new RoomManagerException("Internal error: Generated key" +
                        " retrieving failed when trying to insert room" + room +
                        " - wrong key fields count: " + key.getMetaData().getColumnCount());
            }
            Long result = key.getLong(1);
            if (key.next()){
                throw new RoomManagerException("Internal error: Generated key" +
                        "retrieving failed when trying to insert room " + room +
                        " - more keys found");
            }
            return result;
        }else{
           throw new RoomManagerException("Internal error: Generated key " +
                   "retrieving failed when trying to insert room" + room +
                   " - no key");
        }
    }

    public Room resultSetToRoom(ResultSet table) throws SQLException {
        Room room = new Room();
        room.setID(table.getLong("id"));
        room.setNumber(table.getLong("number"));
        room.setCapacity(table.getInt("capacity"));
        room.setService(table.getBoolean("service"));
        room.setPrice(table.getBigDecimal("price"));
        return room;
    }

}
