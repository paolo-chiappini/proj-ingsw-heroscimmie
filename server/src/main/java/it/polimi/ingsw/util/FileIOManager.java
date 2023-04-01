package it.polimi.ingsw.util;

import java.io.*;
import java.util.Scanner;

public abstract class FileIOManager {
    /**
     * Writes a string of data on the specified file.
     * <p> The string replaces the contents of the file.
     * @param filepath full or relative path to file.
     * @param data data to write in the file.
     * @throws IOException  when the file cannot be written or the path does not exist.
     */
    public static void writeToFile(String filepath, String data) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filepath));
        writer.write(data);
        writer.close();
    }

    /**
     * Writes a string of data on the specified file.
     * <p> The string replaces the contents of the file.
     * <p> Allows to choose a predefined path in a range of paths.
     * @param filename name of the file to write to.
     * @param data data to write in the file.
     * @param path one of the default paths where the file is stored.
     * @throws IOException when the file cannot be written or the path does not exist.
     */
    public static void writeToFile(String filename, String data, FilePath path) throws IOException {
        String fullPath = String.format("%s/%s", path.getPath(), filename);
        writeToFile(fullPath, data);
    }

    /**
     * Reads from the specified file and returns all of its contents as a string.
     * @param filepath full or relative path to file.
     * @return a string containing all the contents read from the file.
     * @throws FileNotFoundException when the file does not exist.
     */
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

    /**
     * Reads from the specified file and returns all of its contents as a string.
     * <p> Allows to choose a predefined path in a range of paths.
     * @param filename name of the file to read from.
     * @param path one of the default paths where the file is stored.
     * @return a string containing all the contents read from the file.
     * @throws FileNotFoundException when the file does not exist.
     */
    public static String readFromFile(String filename, FilePath path) throws FileNotFoundException {
        String fullPath = String.format("%s/%s", path.getPath(), filename);
        return readFromFile(fullPath);
    }
}
