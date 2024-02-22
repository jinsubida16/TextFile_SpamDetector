import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SpamDetector {
    public static void main(String[] args) {
        Path currentDirectory = Paths.get("");
        System.out.println(currentDirectory.toAbsolutePath());

        int totalWords = 0;
        List<String> uniqueWords = new ArrayList<>();
        List<Integer> wordCounts = new ArrayList<>();

        boolean txtFilesFound = false; // Flag to track if any .txt files were found

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(currentDirectory, "*.txt")) {
            for (Path filePath : directoryStream) {
                txtFilesFound = true; // Set the flag to true as .txt file(s) were found
                List<String> lines = Files.readAllLines(filePath);
                for (String line : lines) {
                    String[] colStr = line.split(" ");
                    for (String s : colStr) {
                        String cleanedWord = s.replaceAll("[^a-zA-Z]", "").toLowerCase();

                        if (!cleanedWord.isEmpty()) {
                            totalWords++;
                            int index = uniqueWords.indexOf(cleanedWord);
                            if (index == -1) {
                                uniqueWords.add(cleanedWord);
                                wordCounts.add(1);
                            } else {
                                int count = wordCounts.get(index);
                                wordCounts.set(index, count + 1);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        if (!txtFilesFound) {
            System.out.println("No .txt files found in the directory.");
        } else if (totalWords == 0) {
            System.out.println("No words found in the .txt file(s).");
        } else {
            System.out.println("Dictionary: " + uniqueWords.size());
            System.out.println("Total number of words: " + totalWords);

            for (int i = 0; i < uniqueWords.size(); i++) {
                System.out.println(uniqueWords.get(i) + ": " + wordCounts.get(i));
            }
        }
    }
}