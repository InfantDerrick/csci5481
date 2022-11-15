import java.util.*;
import java.io.*;

public class count_codons {
    public static void main(String[] args) {
        // get the path to the codons from args
        File codons = new File(args[0]);
        Scanner sc;
        // this will be the map of codons to their count
        Map<String, Integer> map = new TreeMap<>();
        try {
            // going through the file
            sc = new Scanner(codons);
            sc.nextLine();
            // getting 3 chars at a time to set as a codon and its count
            while (sc.hasNextLine()) {
                String s = sc.nextLine();
                for (int i = 0; i < s.length() - 2; i += 3) {
                    String three = s.substring(i, i + 3);
                    map.put(three, map.getOrDefault(three, 0) + 1);
                }
                if (sc.hasNextLine())
                    sc.nextLine();
            }
            // write to a csv file
            String eol = System.getProperty("line.separator");
            try (Writer writer = new FileWriter(args[1])) {
                writer.append("Codon").append(',').append("Codon Count").append(eol);
                for (Map.Entry<String, Integer> entry : map.entrySet()) {
                    writer.append(entry.getKey())
                            .append(',')
                            .append(entry.getValue() + "")
                            .append(eol);
                }
            } catch (IOException ex) {
                ex.printStackTrace(System.err);
            }
        } catch (FileNotFoundException e) {
            System.out.print(e);
        }
    }
}