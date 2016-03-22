package cz.muni.fi.pv168.hotelmanager.backend;

import java.util.List;

public interface GuestManager {
	public void createGuest(Guest guest);

	public Guest getGuestByID(Long id);

	public List<Guest> getAllGuests();

	public void updateGuest(Guest guest);

	public void deleteGuest(Guest guest);
}
