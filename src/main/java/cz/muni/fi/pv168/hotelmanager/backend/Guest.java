package cz.muni.fi.pv168.hotelmanager.backend;

public class Guest {
	private Long id;
	private String name;
	private String phone;
	private String address;

	public Long getID() {
		return id;
	}

	public void setID(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Guest() {
	}
	
	public Guest(String name, String phone, String address) {
		this.name = name;
		this.phone = phone;
		this.address = address;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Guest guest = (Guest) o;

		if (address != null ? !address.equals(guest.address) : guest.address != null) return false;
		if (id != null ? !id.equals(guest.id) : guest.id != null) return false;
		if (name != null ? !name.equals(guest.name) : guest.name != null) return false;
		if (phone != null ? !phone.equals(guest.phone) : guest.phone != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (phone != null ? phone.hashCode() : 0);
		result = 31 * result + (address != null ? address.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Guest{" +
		       "\n  id=" + id +
		       "\n  name=" + name +
		       "\n  phone=" + phone +
		       "\n  address=" + address +
		       "\n}";
	}
}
