/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pv168.hotelmanager.gui;

import cz.muni.fi.pv168.hotelmanager.backend.Rent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author xlysonek
 */
public class RentsTableModel extends AbstractTableModel {
    private List<Rent> rents = new ArrayList<>();

    public Rent getRow(int row) {
        return rents.get(row);
    }

    @Override
    public int getRowCount() {
        return rents.size();
    }

    @Override
    public int getColumnCount() {
        return 4; // room, guest, start date, end date
    }

    @Override
    public Object getValueAt(int row, int col) {
        Rent r = rents.get(row);
        switch (col) {
            case 0:
                return r.getRoom().getNumber();
            case 1:
                return r.getGuest().getName();
            case 2:
                return r.getStartDate().toString();
            case 3:
                return r.getEndDate().toString();
            default:
                throw new IllegalArgumentException("Bad guest column index");
        }
    }

    @Override
    public String getColumnName(int col) {
        switch (col) {
            case 0:
                return "Room number";
            case 1:
                return "Guest name";
            case 2:
                return "Start date";
            case 3:
                return "End date";
            default:
                throw new IllegalArgumentException("Bad guest column index");
        }
    }

    public void addRent(Rent r) {
        rents.add(r);
        int lastRow = rents.size() - 1;
        fireTableRowsInserted(lastRow, lastRow);
    }

    public void editRent(Rent r, int row) {
        Rent old = rents.get(row);
        if (old.getID() != r.getID()) {
            throw new IllegalArgumentException("Old guest ID doesn't match new ID");
        }
        old.setRoom(r.getRoom());
        old.setGuest(r.getGuest());
        old.setStartDate(r.getStartDate());
        old.setEndDate(r.getEndDate());
        fireTableRowsUpdated(row, row);
    }

    public void deleteRent(int row) {
        rents.remove(row);
        fireTableRowsDeleted(row, row);
    }

    public void deleteAll() {
        int size = rents.size();
        if (size > 0) {
            rents.clear();
            fireTableRowsDeleted(0, size - 1);
        }
    }
}
