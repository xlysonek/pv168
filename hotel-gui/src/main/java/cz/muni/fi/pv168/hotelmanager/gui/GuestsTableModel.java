package cz.muni.fi.pv168.hotelmanager.gui;

import cz.muni.fi.pv168.hotelmanager.backend.Guest;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class GuestsTableModel extends AbstractTableModel {
    private List<Guest> guests = new ArrayList<>();

    public Guest getRow(int row) {
        return guests.get(row);
    }

    @Override
    public int getRowCount() {
        return guests.size();
    }

    @Override
    public int getColumnCount() {
        return 3; // name, address, phone
    }

    @Override
    public Object getValueAt(int row, int col) {
        Guest g = guests.get(row);
        switch (col) {
            case 0:
                return g.getName();
            case 1:
                return g.getAddress();
            case 2:
                return g.getPhone();
            default:
                throw new IllegalArgumentException("Bad guest column index");
        }
    }

    @Override
    public String getColumnName(int col) {
        switch (col) {
            case 0:
                return "Name";
            case 1:
                return "Address";
            case 2:
                return "Phone";
            default:
                throw new IllegalArgumentException("Bad guest column index");
        }
    }

    public void addGuest(Guest g) {
        guests.add(g);
        int lastRow = guests.size() - 1;
        fireTableRowsInserted(lastRow, lastRow);
    }

    public void editGuest(Guest g, int row) {
        Guest old = guests.get(row);
        if (old.getID() != g.getID()) {
            throw new IllegalArgumentException("Old guest ID doesn't match new ID");
        }
        old.setName(g.getName());
        old.setAddress(g.getAddress());
        old.setPhone(g.getPhone());

        fireTableRowsUpdated(row, row);
    }

    public void deleteGuest(int row) {
        guests.remove(row);
        fireTableRowsDeleted(row, row);
    }

    public void deleteAll() {
        int size = guests.size();
        if (size > 0) {
            guests.clear();
            fireTableRowsDeleted(0, size - 1);
        }
    }
}
