package cz.muni.fi.pv168.hotelmanager.backend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

public class GuestManagerImpl implements GuestManager {
	private final DataSource dataSource;

	public GuestManagerImpl(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public void createGuest(Guest guest) {
		validate(guest);
		if (guest.getID() != null) {
			throw new IllegalArgumentException("Guest id is not null");
		}
		try (Connection conn = dataSource.getConnection();
		     PreparedStatement st = conn.prepareStatement(
		            "INSERT INTO guest (name, phone, address) VALUES (?,?,?)",
		            Statement.RETURN_GENERATED_KEYS)
		    ) {
			st.setString(1, guest.getName());
			st.setString(2, guest.getPhone());
			st.setString(3, guest.getAddress());

			int n = st.executeUpdate();
			if (n != 1) {
				throw new GuestManagerException("Wrong number of added rows (" + n
				                            + ") for guest: " + guest);
			}

			ResultSet keys = st.getGeneratedKeys();
			keys.next();
			guest.setID(keys.getLong(1));
		}
		catch (SQLException e) {
			throw new GuestManagerException("Failed to insert guest into database", e);
		}
	}

	@Override
	public Guest getGuestByID(Long id) {
		if (id == null) {
			throw new IllegalArgumentException("id is null");
		}
		try (Connection conn = dataSource.getConnection();
		     PreparedStatement st = conn.prepareStatement(
		            "SELECT * FROM guest WHERE id=?")
		    ) {
			st.setLong(1, id);
			ResultSet set = st.executeQuery();
			if (set.next()) {
				Guest guest = resultSetToGuest(set);
				if (set.next()) {
					throw new GuestManagerException("More than one guest with id " + id + " found");
				}
				return guest;
			} else {
				return null;
			}
		}
		catch (SQLException e) {
			throw new GuestManagerException("Failed to get guest with id " + id, e);
		}
	}

	private Guest resultSetToGuest(ResultSet set) throws SQLException {
		Guest guest = new Guest();
		guest.setID(set.getLong("id"));
		guest.setName(set.getString("name"));
		guest.setPhone(set.getString("phone"));
		guest.setAddress(set.getString("address"));
		return guest;
	}

	@Override
	public List<Guest> getAllGuests() {
		try (Connection conn = dataSource.getConnection();
		     PreparedStatement st = conn.prepareStatement("SELECT * FROM guest")
		    ) {
			ResultSet set = st.executeQuery();
			List<Guest> ret = new ArrayList<>();
			while (set.next()) {
				ret.add(resultSetToGuest(set));
			}
			return ret;
		}
		catch (SQLException e) {
			throw new GuestManagerException("Failed to get all guests", e);
		}
	}

	@Override
	public void updateGuest(Guest guest) {
		validate(guest);
		if (guest.getID() == null) {
			throw new IllegalArgumentException("Guest id is null");
		}
		try (Connection conn = dataSource.getConnection();
		     PreparedStatement st = conn.prepareStatement(
		            "UPDATE guest SET name=?, phone=?, address=? WHERE id=?")
		    ) {
			st.setString(1, guest.getName());
			st.setString(2, guest.getPhone());
			st.setString(3, guest.getAddress());
			st.setLong(4, guest.getID());

			int n = st.executeUpdate();
			if (n == 0) {
				throw new GuestManagerException("Guest with id " + guest.getID() + " was not found in database");
			}
			else if (n != 1) {
				throw new GuestManagerException("Wrong number of updated rows: " + n);
			}
		}
		catch (SQLException e) {
			throw new GuestManagerException("Failed to update guest: " + guest, e);
		}
	}

	@Override
	public void deleteGuest(Guest guest) {
		validate(guest);
		if (guest.getID() == null) {
			throw new IllegalArgumentException("Guest id is null");
		}
		try (Connection conn = dataSource.getConnection();
		     PreparedStatement st = conn.prepareStatement(
		            "DELETE FROM guest WHERE id=?")
		    ) {
			st.setLong(1, guest.getID());
			int n = st.executeUpdate();
			if (n == 0) {
				throw new GuestManagerException("Guest with id " + guest.getID() + " was not found in database");
			}
			else if (n != 1) {
				throw new GuestManagerException("Oops, more than one row deleted. Guest id: " + guest.getID());
			}
		}
		catch (SQLException e) {
			throw new GuestManagerException("Failed to delete guest with id " + guest.getID(), e);
		}
	}

	private void validate(Guest guest) {
		if (guest == null) {
			throw new IllegalArgumentException("Guest is null");
		}
	}
}
