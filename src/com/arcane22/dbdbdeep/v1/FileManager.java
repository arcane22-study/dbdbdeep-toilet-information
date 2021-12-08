package com.arcane22.dbdbdeep.v1;

import java.io.*;

public class FileManager {

    // Private LazyHolder class for singleton pattern
    private static class LazyHolder {
        private static final FileManager instance = new FileManager();
    }


    // Private instance variables
    private BufferedReader bufferedReader;


    // Constructor
    public FileManager() {
    }


    /*
     * Return instance of FileManager (singleton pattern)
     * @return {FileManager} instance of FileManager
     */
    public static FileManager getInstance(){
        return LazyHolder.instance;
    }


    /*
     * Read File from path
     * @param {String} path, path of file to read (ex. "./data/dataFile.csv")
     * @return {BufferedReader} BufferedReader that include the FileInputStream
     */
    public BufferedReader readFile(String path) {

        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path)), "euc-kr"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return bufferedReader;
    }


    /**
     * Write File
     * @param path {String} path, path of file to read (ex. "./data/dataFile.csv")
     * @param values {String...} values, values to write file
     * @return {void}
     */
    public void writeFile(String path, String... values) {

        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(path)), "euc-kr"));
            for(String str:values) {
                bufferedWriter.write(str + "\n");
            }
            bufferedWriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
