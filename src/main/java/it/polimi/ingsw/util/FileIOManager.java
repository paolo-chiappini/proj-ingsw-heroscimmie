package it.polimi.ingsw.util;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Library class used for simple file IO operations in the resources' folder.
 */
public abstract class FileIOManager {
    public static void writeDataToFile(String filePath, String filename, String data) throws IOException {
        File directory = new File(filePath);
        if (!directory.exists()) {
            if (!directory.mkdir()) throw new RuntimeException("Unable to create dir");
        }

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(String.format("%s/%s", filePath, filename)));
        bufferedWriter.write(data);
        bufferedWriter.close();
    }

    public static String readDataFromResource(String resourcePath) {
        InputStream inputStream = FileIOManager.class.getResourceAsStream(resourcePath);
        return getAllDataFromStream(inputStream);
    }

    public static String readDataFromFile(String filePath) throws FileNotFoundException {
        InputStream fileInputStream = new FileInputStream(filePath);
        return getAllDataFromStream(fileInputStream);
    }

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