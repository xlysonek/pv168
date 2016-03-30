package cz.muni.fi.pv168.hotelmanager.backend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.sql.DataSource;
public class RentManagerImpl implements RentManager {
	
        private final DataSource dataSource;
        
        public RentManagerImpl(DataSource dataSource){
            this.dataSource = dataSource;
        }
    
        @Override
	public void createRent(Rent rent) {
            validate(rent);
            if (rent.getID() != null){
                throw new IllegalArgumentException("Rent id was already set.");
            }
            
            try( Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement(""
                         + "INSERT INTO RENT (startDate, endDate, room, guest) VALUES (?,?,?,?)", 
                         Statement.RETURN_GENERATED_KEYS)){
                statement.setDate(1, (java.sql.Date) 
                        Date.from(rent.getStartDate().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
                statement.setDate(2, (java.sql.Date) 
                        Date.from(rent.getEndDate().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
                statement.setObject(3, rent.getRoom());
                statement.setObject(4, rent.getGuest());
                
                int addedRows = statement.executeUpdate();
                if (addedRows != 1){
                    throw new DatabaseException("Internal error: More rows (" + 
                            addedRows + ") inserted while trying to create new rent" + rent);
                }
                
                ResultSet key = statement.getGeneratedKeys();
                rent.setID(getKey(key, rent));
            } catch (SQLException ex) {
                throw new DatabaseException("Error when creating rent" + rent, ex);
            }
            
	}

	@Override
	public Rent getRentById(Long id) {
            try (Connection connection = dataSource.getConnection();
                    PreparedStatement statement = connection.prepareStatement(
                            "SELECT id, startDate, endDate, room, guest FROM rent WHERE id = ?")){
                
                statement.setLong(1, id);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()){
                    Rent rent = resultSetToRent(resultSet);
                    
                    if (resultSet.next()){
                        throw new DatabaseException("Internal Error: More entities with"
                                + "the same id: " + id + " found " + rent + ", " + resultSetToRent(resultSet));
                    }
                    return rent;
                }else{
                    return null;
                }
                
                
            } catch (SQLException ex) {
                throw new DatabaseException("Error when retrieving rent with the id " + id, ex);
            }
            
	}

	@Override
	public void updateRent(Rent rent) {
            validate(rent);
            if (rent.getID() == null){
                throw new IllegalArgumentException("rent id wasnt set");
            }
            
            try (Connection connection = dataSource.getConnection();
                    PreparedStatement statement = connection.prepareStatement("UPDATE Rent SET "
                            + "startDate = ?, endDate = ?, room = ?, guest = ? WHERE id = ?")){
                
                statement.setDate(1, (java.sql.Date) 
                        Date.from(rent.getStartDate().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
                statement.setDate(2, (java.sql.Date) 
                        Date.from(rent.getEndDate().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
                statement.setObject(3, rent.getRoom());
                statement.setObject(4, rent.getGuest());
                statement.setLong(5, rent.getID());
                
                int count = statement.executeUpdate();
                if (count == 0){
                    throw new DatabaseException("Rent " + rent + " not found.");
                }else if (count != 1){
                    throw new DatabaseException("Invalid updated rows count detected "
                            + "(only one should have been updated)");
                }
                
            } catch (SQLException ex) {
                throw new DatabaseException("Error when updating rent" + rent, ex);
            }
            
	}

	@Override
	public void deleteRent(Rent rent) {
                if (rent == null){
                    throw new IllegalArgumentException("rent doesnt exist.");
                }
                if (rent.getID() == null){
                    throw new IllegalArgumentException("rent id hasnt been set.");
                }
                try (Connection connection = dataSource.getConnection();
                        PreparedStatement statement = connection.prepareStatement(
                                "DELETE FROM rent WHERE id = ?")){
                    statement.setLong(1, rent.getID());
                    
                    int count = statement.executeUpdate();
                    if (count == 0){
                        throw new DatabaseException("Rent " + rent + "was not found.");
                    }else if (count != 1){
                        throw new DatabaseException("Invalid deleted rows in database "
                                + "(only one should be updated)");
                    }
                    
                }catch (SQLException ex){
                    throw new DatabaseException("Error when deleting rent" + rent, ex);
                }
	}

	@Override
	public List<Rent> getRentByGuest(Long guestId) { 
		if (guestId == null){
                    throw new IllegalArgumentException("Guest id hasnt been initialized");
                }
                
                try (Connection connection = dataSource.getConnection();
                        PreparedStatement statement = connection.prepareStatement(
                                "SELECT id, startDate, endDate, room, guest FROM rent WHERE guest.id = ?")){ 
                    
                    statement.setLong(1, guestId);
                    
                    ResultSet resultSet = statement.executeQuery();
                    List<Rent> result = new ArrayList<>();
                    
                    while(resultSet.next()){
                        result.add(resultSetToRent(resultSet));
                    }
                    return result;
                } catch (SQLException ex) {
                throw new DatabaseException("Error when retrieving list of rents by guest id", ex);
            }
	}

	@Override
	public List<Rent> getRentByGuest(Long guestId, LocalDate startDate, LocalDate endDate) {
		if (guestId == null){
                    throw new IllegalArgumentException("Guest id hasnt been initialized");
                }
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
                                "SELECT id, startDate, endDate, room, guest FROM rent "
                                        + "WHERE guest.id = ? AND startDate = ? AND endDate = ?")){ //????
                    
                    statement.setLong(1, guestId);
                    statement.setDate(2, (java.sql.Date) 
                        Date.from(startDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
                    statement.setDate(3, (java.sql.Date) 
                        Date.from(endDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
                    
                    ResultSet resultSet = statement.executeQuery();
                    List<Rent> result = new ArrayList<>();
                    
                    while(resultSet.next()){
                        result.add(resultSetToRent(resultSet));
                    }
                    return result;
                } catch (SQLException ex) {
                throw new DatabaseException("Error when retrieving list of rents by guest id" + 
                        guestId + " and date " + startDate + " - " + endDate, ex);
            }
	}

	@Override
	public List<Rent> getRentByRoom(Long roomId) {
		if (roomId == null){
                    throw new IllegalArgumentException("Room id hasnt been initialized");
                }
                
                try (Connection connection = dataSource.getConnection();
                        PreparedStatement statement = connection.prepareStatement(
                                "SELECT id, startDate, endDate, room, guest FROM rent WHERE room.id = ?")){ 
                    
                    statement.setLong(1, roomId);
                    
                    ResultSet resultSet = statement.executeQuery();
                    List<Rent> result = new ArrayList<>();
                    
                    while(resultSet.next()){
                        result.add(resultSetToRent(resultSet));
                    }
                    return result;
                } catch (SQLException ex) {
                throw new DatabaseException("Error when retrieving list of rents by room id" + 
                        roomId, ex);
            }
	}

	@Override
	public List<Rent> getRentByRoom(Long roomId, LocalDate startDate, LocalDate endDate) {
		if (roomId == null){
                    throw new IllegalArgumentException("Room id hasnt been initialized");
                }
                if (startDate == null){
                    throw new IllegalArgumentException("startDate is null.");
                }
                if (endDate == null){
                    throw new IllegalArgumentException("endDate is null");
                }
                if (startDate.isAfter(endDate)){
                    throw new IllegalArgumentException("startDate differs from endDate by negative value.");
                }
                
                try (Connection connection = dataSource.getConnection();
                        PreparedStatement statement = connection.prepareStatement(
                                "SELECT id, startDate, endDate, room, guest FROM rent "
                                        + "WHERE room.id = ? AND startDate = ? AND endDate = ?")){ 
                    
                    statement.setLong(1, roomId);
                    statement.setDate(2, (java.sql.Date) 
                            Date.from(startDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
                    statement.setDate(3, (java.sql.Date) 
                            Date.from(endDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
                    
                    ResultSet resultSet = statement.executeQuery();
                    List<Rent> result = new ArrayList<>();
                    
                    while(resultSet.next()){
                        result.add(resultSetToRent(resultSet));
                    }
                    return result;
                } catch (SQLException ex) {
                throw new DatabaseException("Error when retrieving list of rents by room id" + 
                        roomId + " and date " + startDate + " - " + endDate, ex);
            }
	}

	@Override
	public List<Rent> getRentByDate(LocalDate startDate, LocalDate endDate) { 
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
                                "SELECT id, startDate, endDate, room, guest FROM rent WHERE startDate = ?, endDate = ?")){ 
                    
                    statement.setDate(1, (java.sql.Date) 
                        Date.from(startDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
                    statement.setDate(2, (java.sql.Date) 
                        Date.from(endDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
                    
                    ResultSet resultSet = statement.executeQuery();
                    List<Rent> result = new ArrayList<>();
                    
                    while(resultSet.next()){
                        result.add(resultSetToRent(resultSet));
                    }
                    return result;
                } catch (SQLException ex) {
                throw new DatabaseException("Error when retrieving list of rents by date", ex);
            }
	}

	@Override
	public List<Rent> getAllRents() {
		try (Connection connection = dataSource.getConnection();
                        PreparedStatement statement = connection.prepareStatement(
                                "SELECT id, startDate, endDate, room, guest FROM rent")){ 
                    
                    ResultSet resultSet = statement.executeQuery();
                    List<Rent> result = new ArrayList<>();
                    
                    while(resultSet.next()){
                        result.add(resultSetToRent(resultSet));
                    }
                    return result;
                } catch (SQLException ex) {
                throw new DatabaseException("Error when retrieving list of all rents", ex);
            }
	}

        // addiional methods
        public void validate(Rent rent) throws IllegalArgumentException{
            if (rent == null){
                throw new IllegalArgumentException("Rent is null");
            }
            if (rent.getGuest() == null){
                throw new IllegalArgumentException("There is no guest to asign rent to.");
            }
            if (rent.getRoom() == null){
                throw new IllegalArgumentException("There is no room to be asigned by rent.");
            }
            if (rent.getEndDate().isBefore(rent.getStartDate())){
                throw new IllegalArgumentException("Rent ends before it begins.");
            }
            
        }
        
        public Long getKey(ResultSet key, Rent rent) throws SQLException, DatabaseException{
            if (key.next()){
                if (key.getMetaData().getColumnCount() != 1){
                    throw new DatabaseException("Internal Error: Generated key"
                    + " retrieve failed when trying to insert rent " + rent 
                            + " wrong key fields count" + key.getMetaData().getColumnCount());
                }
                Long result = key.getLong(1);
                if (key.next()){
                    throw new DatabaseException("Internal Error: Generated key"
                    + "retrival failed when trying to insert rent" + rent 
                            + " more keys found");
                }
                return result;
            }else {
                throw new DatabaseException("Internal Error: Generated key" 
                        + "retrieval failed when trying to insert rent" + rent
                        + " no key found.");
            }
        }

    private Rent resultSetToRent(ResultSet resultSet) throws SQLException {
        Rent rent = new Rent();
        rent.setID(resultSet.getLong("id"));
        rent.setStartDate((LocalDate) resultSet.getObject("startDate"));
        rent.setEndDate((LocalDate) resultSet.getObject("endDate"));
        rent.setRoom((Room) resultSet.getObject("room"));   //??
        rent.setGuest((Guest) resultSet.getObject("guest"));
        return rent;
    }

}
