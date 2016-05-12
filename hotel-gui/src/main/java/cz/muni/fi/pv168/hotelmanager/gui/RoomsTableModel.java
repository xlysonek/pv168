/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pv168.hotelmanager.gui;

import cz.muni.fi.pv168.hotelmanager.backend.Room;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author xlysonek
 */
public class RoomsTableModel extends AbstractTableModel {
    private List<Room> rooms = new ArrayList<>();

    public Room getRow(int row) {
        return rooms.get(row);
    }

    @Override
    public int getRowCount() {
        return rooms.size();
    }

    @Override
    public int getColumnCount() {
        return 4; // number, service, capacity, price
    }

    @Override
    public Object getValueAt(int row, int col) {
        Room r = rooms.get(row);
        switch (col) {
            case 0:
                return r.getNumber();
            case 1:
                return r.getService();
            case 2:
                return r.getCapacity();
            case 3:
                return r.getPrice();
            default:
                throw new IllegalArgumentException("Bad guest column index");
        }
    }

    @Override
    public String getColumnName(int col) {
        switch (col) {
            case 0:
                return "Number";
            case 1:
                return "Service";
            case 2:
                return "Capacity";
            case 3:
                return "Price";
            default:
                throw new IllegalArgumentException("Bad guest column index");
        }
    }

    public void addRoom(Room r) {
        rooms.add(r);
        int lastRow = rooms.size() - 1;
        fireTableRowsInserted(lastRow, lastRow);
    }

    public void editRoom(Room r, int row) {
        Room old = rooms.get(row);
        if (old.getID() != r.getID()) {
            throw new IllegalArgumentException("Old guest ID doesn't match new ID");
        }
        old.setNumber(r.getNumber());
        old.setService(r.getService());
        old.setCapacity(r.getCapacity());
        old.setPrice(r.getPrice());
        fireTableRowsUpdated(row, row);
    }

    public void deleteRoom(int row) {
        rooms.remove(row);
        fireTableRowsDeleted(row, row);
    }

    public void deleteAll() {
        int size = rooms.size();
        if (size > 0) {
            rooms.clear();
            fireTableRowsDeleted(0, size - 1);
        }
    }
}
