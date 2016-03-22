package cz.muni.fi.pv168.hotelmanager.backend;

import java.time.LocalDate;
import java.util.List;

public interface RentManager {
	/**
	 * Creates new Rent in database
	 * @param rent
	 */
	public void createRent(Rent rent);

	/**
	 * Gets information about rent with given id
	 * @param id
	 * @return
	 */
	public Rent getRentById(Long id);

	/**
	 * Updates information of given rent
	 * @param rent
	 */
	public void updateRent(Rent rent);

	/**
	 * Deletes rent from database
	 * @param rent
	 */
	public void deleteRent(Rent rent);

	/**
	 * Gets all rents of guest with given ID (anytime in existence of Hotel)
	 * @param guestId
	 * @return
	 */
	public List<Rent> getRentByGuest(Long guestId);

	/**
	 * Gets all rents of guest with given ID and with date range overlapping in any way
	 * with the date range [startDate, endDate]
	 * @param guestId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<Rent> getRentByGuest(Long guestId, LocalDate startDate, LocalDate endDate);

	/**
	 * Get all rents with room of given id
	 * @param roomId
	 * @return
	 */
	public List<Rent> getRentByRoom(Long roomId);

	/**
	 * Get all rents with room of given id and with date range overlapping in any way
	 * with the date range [startDate, endDate]
	 * @param roomId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<Rent> getRentByRoom(Long roomId, LocalDate startDate, LocalDate endDate);

	/**
	 * Gets all rents existing in given date interval
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<Rent> getRentByDate(LocalDate startDate, LocalDate endDate);

	/**
	 * Gets all rents in database
	 * @return
	 */
	public List<Rent> getAllRents();
        
        /**  
        * Gets all currently empty rooms
        * @param startDate
         * @param endDate
        * @return
        */

        public List<Room> getFreeRooms(LocalDate startDate, LocalDate endDate); // p?esunuto z room kv?li snazší manipulovatelnosti
}
