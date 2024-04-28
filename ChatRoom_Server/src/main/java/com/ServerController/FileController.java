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
    private BufferedWriter bufferedWriter;


    public void WriteToGroup(String message, String name){
        String filename = GroupPath + name + ".txt";
        try{
            if(Files.exists(Paths.get(filename))){
                fileWriter = new FileWriter(filename,true);
                bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.append(message + "\n");
                bufferedWriter.close();
            }else{
                fileWriter = new FileWriter(filename);
                bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(message + "\n");
                bufferedReader.close();
            }
        }catch(IOException e){
            e.printStackTrace();
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

    public void WriteToChat(String message,String name1, String name2){
        String filename1 = ChatPath + name1 + name2 + ".txt";
        String filename2 = ChatPath + name2 + name1 + ".txt";
        try{
            if(Files.exists(Paths.get(filename1))){
                fileWriter = new FileWriter(filename1,true);
                bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.append(message + "\n");
                bufferedWriter.close();
            }else if(Files.exists(Paths.get(filename2))){
                fileWriter = new FileWriter(filename2,true);
                bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.append(message + "\n");
                bufferedWriter.close();
            }else{
                fileWriter = new FileWriter(filename1);
                bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(message + "\n");
                bufferedReader.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public String ReadfromChat(String name1, String name2) {
        String filename1 = ChatPath + name1 + name2 + ".txt";
        String filename2 = ChatPath + name2 + name1 + ".txt";

        try {
            if (Files.exists(Paths.get(filename1))) {
                file = new File(filename1);
                bufferedReader = new BufferedReader(new FileReader(filename1));
                String str = "";
                String line;

                while (((line = bufferedReader.readLine()) != null)) {
                    str += line + "\n";
                }
                bufferedReader.close();
                return str;
            }
            else if(Files.exists(Paths.get(filename2))){
                file = new File(filename2);
                bufferedReader = new BufferedReader(new FileReader(filename2));
                String str = "";
                String line;

                while (((line = bufferedReader.readLine()) != null)) {
                    str += line + "\n";
                }
                bufferedReader.close();
                return str;
            }
            else{
                return "";
            }

        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }

    }
}
