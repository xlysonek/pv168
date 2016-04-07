package cz.muni.fi.pv168.hotelmanager.backend;

public class GuestManagerException extends DatabaseException {
	private static final long serialVersionUID = 1L;

	public GuestManagerException(String msg) {
		super(msg);
	}

	public GuestManagerException(Throwable cause) {
		super(cause);
	}

	public GuestManagerException(String msg, Throwable cause) {
		super(msg,cause);
	}
}
