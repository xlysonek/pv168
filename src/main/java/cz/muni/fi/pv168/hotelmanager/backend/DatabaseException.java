package cz.muni.fi.pv168.hotelmanager.backend;

public class DatabaseException extends RuntimeException {
	public DatabaseException(String msg) {
		super(msg);
	}
	
	public DatabaseException(Throwable cause) {
		super(cause);
	}
	
	public DatabaseException(String msg, Throwable cause) {
		super(msg,cause);
	}
}
