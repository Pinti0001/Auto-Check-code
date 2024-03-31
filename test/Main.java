import java.io.*;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        if (args.length < 2 || !"-i".equals(args[0])) {
            System.out.println("Usage: java Main -i <file_name.java> && [--auto-fix]");
            return;
        }

        String fileName = args[1];
        boolean autoFix = args.length == 3 && "--auto-fix".equals(args[2]);

        try {
            List<Integer> errors = errorList(fileName);
            if (!errors.isEmpty()) {
                System.out.println("Errors found in " + fileName + ":");
                for (int error : errors) {
                    System.out.println("Error: Incorrect format in line " + error + "\n If you want to fix this error then give command : java Main -i "+fileName+" --auto-fix");
                    if (autoFix) {
                        Mainror(fileName, error);
                        System.out.println("Auto-fixed the error in line " + error);
                    }
                }
            } else {
                System.out.println("No errors found in " + fileName);
            }
        } catch (IOException e) {
            System.err.println("Error reading : " + e.getMessage());
        }
    }

    private static List<Integer> errorList(String filePath) throws IOException {
        List<Integer> errors = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNumber = 0;
            boolean insideIf = false;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();
                if (line.startsWith("if") && line.endsWith("{")) {
                    insideIf = true;
                    continue;
                } else if (insideIf && line.equals("} else {")) {
                    insideIf = false;
                } else if (insideIf && !line.equals("}") && !line.equals("\n else {")) {
                    errors.add(lineNumber);
//                    break;
                }
            }
        }
        return errors;
    }


    private static void Mainror(String filePath, int errorLine) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int currentLine = 0;
            boolean insideIf = false;
            while ((line = reader.readLine()) != null) {
                currentLine++;
                line = line.trim();
                if (currentLine == errorLine) {
                    if (line.endsWith("{")) {
                        insideIf = true;
                    } else if (line.equals("} else {")) {
                        insideIf = false;
                        lines.add("} else {");
                    } else if (insideIf && line.equals("}")) {
                        lines.add(line); // Add the ending bracket
                        insideIf = false;
                    } else {
                        lines.add(line);
                    }
                } else {
                    lines.add(line);
                }
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        }
    }
}
