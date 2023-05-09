package it.polimi.ingsw.util;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Library class used for simple file IO operations.
 */
public abstract class FileIOManager {
    /**
     * Writes a string of data to the specified file.
     * <p> The string replaces the contents of the file.
     * @param filepath absolute or relative path to file.
     * @param data data to write in the file.
     * @throws IOException  when the file cannot be written or the path does not exist.
     */
    public static void writeToFile(String filepath, String data) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filepath));
        writer.write(data);
        writer.close();
    }

    /**
     * Writes a string of data to the specified file.
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
     * @param filepath absolute or relative path to file.
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

    /**
     * Reads the names of the files in the specified directory.
     * @param directoryPath absolute path to directory.
     * @return returns the list of names of the files contained in the directory
     * (null if directory is empty).
     */
    public static List<String> getFilesInDirectory(String directoryPath) {
        List<String> files;
        File directory = new File(directoryPath);

        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("Given path is not a directory");
        }

        File[] filesArr = directory.listFiles();
        if (filesArr == null) return null;
        files = Arrays.stream(filesArr).map(File::getName).toList();
        return files;
    }

    /**
     * Reads the names of the files in the specified directory.
     * @param path default path to directory.
     * @return returns the list of names of the files contained in the directory
     * (null if directory is empty).
     */
    public static List<String> getFilesInDirectory(FilePath path) {
        return getFilesInDirectory(path.getPath());
    }
}
