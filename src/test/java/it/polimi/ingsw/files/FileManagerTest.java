package it.polimi.ingsw.files;
import it.polimi.ingsw.util.FileIOManager;
import it.polimi.ingsw.util.FilePath;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests on File Manager")
public class FileManagerTest {

    @Nested
    @DisplayName("When writing and reading a file")
    class WritingReadingTests {
        @Test
        @DisplayName("File should be created at the specified relative path")
        void createFileWithRelativePath() {
            String filename = "test2.txt";
            try {
                FileIOManager.writeToFile(filename, "", FilePath.TEST);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            String resourcePath = this.getClass().getResource(FilePath.TEST.getPath()).getPath();
            File f = new File(resourcePath + "/" + filename);

            assertAll(
                    () -> assertTrue(f.exists()),
                    () -> assertTrue(f.isFile()),
                    () -> assertTrue(f.delete())
            );
        }

        @Test
        @DisplayName("Read data should be correct")
        void readFromFile() {
            String filename = "permanent.txt";
            String expectedData = "This is a permanent file for testing purposes.";

            String data;
            try {
                data = FileIOManager.readFromFile(filename, FilePath.TEST);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

            assertEquals(expectedData, data);
        }

        @Test
        @DisplayName("Trying to read non existing file should throw an exception")
        void readNonExistingFile() {
            String filename = "ghost.txt";
            assertThrows(RuntimeException.class, () -> FileIOManager.readFromFile(filename, FilePath.TEST));
        }

        @Test
        @DisplayName("File writing on existing file should overwrite data")
        void overwriteFile() {
            String filename = "test3.txt";
            String data1 = "This is a test";
            String data2 = "This is some new data";

            String dataAfterFirstWrite;
            String dataAfterSecondWrite;
            try {
                FileIOManager.writeToFile(filename, data1, FilePath.TEST);
                dataAfterFirstWrite = FileIOManager.readFromFile(filename, FilePath.TEST);
                FileIOManager.writeToFile(filename, data2, FilePath.TEST);
                dataAfterSecondWrite = FileIOManager.readFromFile(filename, FilePath.TEST);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            String resourcePath = this.getClass().getResource(FilePath.TEST.getPath()).getPath();
            File f = new File(resourcePath + "/" + filename);

            assertAll(
                    () -> assertTrue(f.exists()),
                    () -> assertTrue(f.isFile()),
                    () -> assertEquals(data1, dataAfterFirstWrite),
                    () -> assertEquals(data2, dataAfterSecondWrite),
                    () -> assertTrue(f.delete())
            );
        }
    }

    @Test
    @DisplayName("Reading contents of a directory")
    public void readDirectoryContents() {
        List<String> expectedFiles = List.of("permanent.txt", "testfile.txt");
        List<String> filesInDirectory = FileIOManager.getFilesInDirectory(FilePath.TEST);

        assertIterableEquals(expectedFiles, filesInDirectory);
    }
}
