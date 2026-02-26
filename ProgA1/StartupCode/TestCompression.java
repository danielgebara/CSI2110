import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.stream.Stream;

/**
 * Class TestCompression to Test Huffman
 * @author Lucia Moura
 * updated by Bo Sun
 */
public class TestCompression {

    // Base folder for test files
    private static final Path TEST_DIR = Paths.get("testfiles");

    /**
     * If the given path is absolute OR already contains a directory component,
     * keep it as-is. Otherwise, assume it is inside testfiles/.
     */
    private static String resolveInTestDir(String filename) {
        Path p = Paths.get(filename);
        if (p.isAbsolute() || p.getParent() != null) {
            return filename; // already has a directory or is absolute
        }
        return TEST_DIR.resolve(filename).toString();
    }

    /**
     * Helper method to take inputs from command line
     *
     * @param args contains commands in 3 types of format:
     *             E inputfile outputfile
     *             D inputfile outputfile
     *             T testfile
     */
    private static void testInput(String[] args) throws IOException, ClassNotFoundException {
        Huffman myHuff = new Huffman();

        if (args.length == 0) {
            System.out.println("Empty command.");
            return;
        }

        String mode = args[0].trim();

        switch (mode) {
            case "E":
            case "e": {
                if (args.length < 3) {
                    System.out.println("Usage: TestCompression E <inputfile> <outputfile>");
                    return;
                }
                String inFile = resolveInTestDir(args[1]);
                String outFile = resolveInTestDir(args[2]);
                myHuff.encode(inFile, outFile);
                break;
            }

            case "D":
            case "d": {
                if (args.length < 3) {
                    System.out.println("Usage: TestCompression D <inputfile> <outputfile>");
                    return;
                }
                String inFile = resolveInTestDir(args[1]);
                String outFile = resolveInTestDir(args[2]);
                myHuff.decode(inFile, outFile);
                break;
            }

            case "T":
            case "t": {
                if (args.length < 2) {
                    System.out.println("Usage: TestCompression T <testfile_with_commands>");
                    return;
                }
                String testFile = resolveInTestDir(args[1]);
                Path path = Paths.get(testFile);

                if (!Files.exists(path)) {
                    System.out.println("Cannot find test file: " + path);
                    return;
                }

                try (Stream<String> lines = Files.lines(path)) {
                    lines.forEach(rawLine -> {
                        String line = rawLine.trim();

                        // skip blanks and comments (supports lines starting with # or //)
                        if (line.isEmpty() || line.startsWith("#") || line.startsWith("//")) return;

                        // split by any whitespace (space/tabs/multiple spaces)
                        String[] parts = line.split("\\s+");

                        try {
                            testInput(parts);
                        } catch (Exception e) {
                            System.out.println("Error running command: \"" + line + "\"");
                            System.out.println("  -> " + e.getClass().getSimpleName() + ": " + e.getMessage());
                        }
                    });
                }

                System.out.println("Test file completed: " + path);
                break;
            }

            default:
                System.out.println("Error: first argument must be E, D or T.");
                System.out.println("Usage: TestCompression E/D <inputfile> <outputfile>");
                System.out.println("   or: TestCompression T <testfile_with_commands>");
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Huffman myHuff = new Huffman();

        // Ensure testfiles directory exists (optional but helpful)
        if (!Files.exists(TEST_DIR)) {
            // You can comment this out if you don't want auto-create
            Files.createDirectories(TEST_DIR);
        }

        if (args.length == 0) { // hardcoded and interactive inputs

            // Hardcoded test: assumes genes.txt is inside testfiles/
            myHuff.encode(TEST_DIR.resolve("genes.txt").toString(), TEST_DIR.resolve("genes.huf").toString());
            myHuff.decode(TEST_DIR.resolve("genes.huf").toString(), TEST_DIR.resolve("genesRecovered.txt").toString());

            // Interactive part typed as input
            Scanner input = new Scanner(System.in);
            while (true) {
                System.out.println("\nCommand Formats:");
                System.out.println("E <inputfile> <outputfile>");
                System.out.println("D <inputfile> <outputfile>");
                System.out.println("T <testfile_with_commands>");
                System.out.println("or type Q to quit\n");
                System.out.print("Enter command > ");

                String command = input.nextLine().trim();
                if (command.equalsIgnoreCase("Q")) break;
                if (command.isEmpty()) continue;

                testInput(command.split("\\s+"));
            }
            System.out.println("Ended program.");
        } else {
            // command line inputs
            testInput(args);
        }
    }
}