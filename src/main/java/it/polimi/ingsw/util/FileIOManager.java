package it.polimi.ingsw.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * Library class used for simple file IO operations in the resources' folder.
 */
public abstract class FileIOManager {
    /**
     * Writes data to a file at the specified path.
     * @param filePath path to file (excluding filename).
     * @param filename name of the file.
     * @param data data to write.
     * @throws IOException when unable to read from file.
     */
    public static void writeDataToFile(String filePath, String filename, String data) throws IOException {
        File directory = new File(filePath);
        if (!directory.exists()) {
            if (!directory.mkdir()) throw new RuntimeException("Unable to create dir");
        }

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(String.format("%s/%s", filePath, filename)));
        bufferedWriter.write(data);
        bufferedWriter.close();
    }

    /**
     * Reads data from a resource file. File must be a resource to be read.
     * @param resourcePath path to resource (including filename).
     * @return the data read from the file.
     */
    public static String readDataFromResource(String resourcePath) {
        InputStream inputStream = FileIOManager.class.getResourceAsStream(resourcePath);
        return getAllDataFromStream(inputStream);
    }

    /**
     * Reads data from a file at the specified path.
     * @param filePath path to file (including filename).
     * @return the data read from the file.
     * @throws FileNotFoundException when the file cannot be located.
     */
    public static String readDataFromFile(String filePath) throws FileNotFoundException {
        InputStream fileInputStream = new FileInputStream(filePath);
        return getAllDataFromStream(fileInputStream);
    }

    /**
     * Reads all data from the given stream.
     * @param inputStream stream to read from.
     * @return data read from stream.
     */
    private static String getAllDataFromStream(InputStream inputStream) {
        StringBuilder builder = new StringBuilder();
        try (
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader)
        ) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line);
            }
            return builder.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get a list of files contained in the directory at the specified path.
     * @param directoryPath path to directory.
     * @return the list of names of the files found in the directory (folders excluded).
     */
    public static List<String> getFilesInDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists() || !directory.isDirectory()) {
            throw new RuntimeException("Unable to read from directory");
        }

        File[] filesArr = directory.listFiles();

        return Arrays.stream(filesArr)
                .filter(File::isFile)
                .map(File::getName)
                .toList();
    }
}