/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pv168.hotelmanager.backend;

/**
 *
 * @author Matlafous
 */
public class RoomNotFoundException extends RuntimeException{
    
    public RoomNotFoundException(String message){
        super(message);
    }
}
