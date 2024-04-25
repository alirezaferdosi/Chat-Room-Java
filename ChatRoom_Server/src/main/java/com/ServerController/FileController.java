package com.ServerController;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;


public class FileController {
    private String GroupPath = "src/Group/";
    private String ChatPath = "src/Chat/";
    private FileWriter fileWriter;
    private File file;
    private Scanner scanner;
    private BufferedReader bufferedReader;


    public void WriteToGroup(String message, String name){
        String filename = GroupPath + name + ".txt";
        if(Files.exists(Paths.get(filename))){
            try {
                fileWriter = new FileWriter(filename);
                fileWriter.append(message);
                fileWriter.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            try {
                fileWriter = new FileWriter(GroupPath + filename);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fileWriter.write(message);
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public String ReadFromGroup(String name){
        String filename = GroupPath + name + ".txt";
        file = new File(filename);

        try {
            bufferedReader = new BufferedReader(new FileReader(filename));
            String str = "";
            String line;

            while(((line = bufferedReader.readLine()) != null)){
                str += line + "\n";
            }
            bufferedReader.close();
            return str;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
