package it.polimi.ingsw.util;

import java.io.*;
import java.util.Scanner;

public abstract class FileIOManager {
    public static void writeToFile(String filepath, String data) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filepath));
        writer.write(data);
        writer.close();
    }

    public static void writeToFile(String filename, String data, FilePath path) throws IOException {
        String fullPath = String.format("%s/%s", path.getPath(), filename);
        writeToFile(fullPath, data);
    }

    public static String readFromFile(String filepath) throws FileNotFoundException {
        File file = new File(filepath);
        StringBuilder data = new StringBuilder();
        Scanner reader = new Scanner(file);

        while(reader.hasNextLine()) {
            data.append(reader.nextLine());
        }
        reader.close();

        return data.toString();
    }

    public static String readFromFile(String filename, FilePath path) throws FileNotFoundException {
        String fullPath = String.format("%s/%s", path.getPath(), filename);
        return readFromFile(fullPath);
    }
}
