package it.polimi.ingsw.util;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * Library class used for simple file IO operations in the resources' folder.
 */
public abstract class FileIOManager {
    /**
     * Writes a string of data to the specified file.
     * <p> The string replaces the contents of the file.
     * @param filepath absolute or relative path to file.
     * @param data data to write in the file.
     * @throws IOException  when the file cannot be written or the path does not exist.
     */
    public static void writeToFile(String filepath, String filename, String data) throws IOException {
        URL url = FileIOManager.class.getResource(filepath);
        BufferedWriter writer = new BufferedWriter(new FileWriter(url.getPath() + "/" + filename));
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
        writeToFile(path.getPath(), filename, data);
    }

    /**
     * Reads from the specified file and returns all of its contents as a string.
     * @param filepath relative path to resource file.
     * @return a string containing all the contents read from the file.
     * @throws RuntimeException when the file does not exist or something went wrong
     * while reading.
     */
    public static String readFromFile(String filepath) throws RuntimeException {
        StringBuilder data = new StringBuilder();
        InputStream inputStream = FileIOManager.class.getResourceAsStream(filepath);
        try (
                InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                BufferedReader reader = new BufferedReader(streamReader)
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                data.append(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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
     * @param directoryPath relative path to directory in resources.
     * @return returns the list of names of the files contained in the directory
     * (null if directory is empty).
     */
    public static List<String> getFilesInDirectory(String directoryPath) {
        List<String> files;
        String resourcePath = FileIOManager.class.getResource(directoryPath).getPath();
        File directory = new File(resourcePath);

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
