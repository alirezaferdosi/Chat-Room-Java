package com.ServerController;

import org.eclipse.persistence.internal.oxm.record.json.JsonParserReader;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.nio.charset.StandardCharsets;


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
            String str = "",line;
            System.out.println("____________");

            while(((line = bufferedReader.readLine()) != null)){
                str += line + "\n";
            }
            bufferedReader.close();
            return str;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "";
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
