package com.vgtu.PRIf18_4.NormanBuiko.AccountingApp;

import java.io.*;

public class FileDriver<T> {

    public void exportFile(T obj, String path){
        try{
            var streamOut = new FileOutputStream(path);
            var objStreamOut = new ObjectOutputStream(streamOut);
            objStreamOut.writeObject(obj);
        }catch (IOException e){
            System.out.println("failed to export: " + e.getMessage());
        }
    }

    public T importFile(String path){
        try{
            var streamIn = new FileInputStream(path);
            var objStreamIn = new ObjectInputStream(streamIn);
            return (T) objStreamIn.readObject();
        }catch (Exception e){
            System.out.println("failed to import: " + e.getMessage());
        }

        return null;
    }
}
