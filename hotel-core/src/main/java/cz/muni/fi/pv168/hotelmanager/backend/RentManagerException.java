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
public class RentManagerException extends DatabaseException{
    public RentManagerException(String message){
        super(message);
    }
    
    public RentManagerException(Throwable cause){
        super(cause);
    }
    
    public RentManagerException(String message, Throwable cause){
        super(message, cause);
    }
}
