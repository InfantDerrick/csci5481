import java.util.*;
import java.io.*;

public class count_acids {
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
                System.out.println(s);
                for (int i = 0; i < s.length() - 2; i += 3) {
                    String three = s.substring(i, i + 3);
                    switch (three) {
                        case "TTT":
                        case "TTC":
                            map.put("Phe", map.getOrDefault("Phe", 0) + 1);
                            break;
                        case "TTA":
                        case "CTT":
                        case "TTG":
                        case "CTC":
                        case "CTA":
                        case "CTG":
                            map.put("Leu", map.getOrDefault("Leu", 0) + 1);
                            break;
                        case "ATT":
                        case "ATC":
                        case "ATA":
                            map.put("Ile", map.getOrDefault("Ile", 0) + 1);
                            break;
                        case "ATG":
                            map.put("Met", map.getOrDefault("Met", 0) + 1);
                            break;
                        case "GTT":
                        case "GTC":
                        case "GTA":
                        case "GTG":
                            map.put("Val", map.getOrDefault("Val", 0) + 1);
                            break;
                        case "TCT":
                        case "TCC":
                        case "TCA":
                        case "TCG":
                        case "AGT":
                        case "AGC":
                            map.put("Ser", map.getOrDefault("Ser", 0) + 1);
                            break;
                        case "CCT":
                        case "CCC":
                        case "CCA":
                        case "CCG":
                            map.put("Pro", map.getOrDefault("Pro", 0) + 1);
                            break;
                        case "ACT":
                        case "ACC":
                        case "ACA":
                        case "ACG":
                            map.put("Thr", map.getOrDefault("Thr", 0) + 1);
                            break;
                        case "GCT":
                        case "GCC":
                        case "GCA":
                        case "GCG":
                            map.put("Ala", map.getOrDefault("Ala", 0) + 1);
                            break;
                        case "TAT":
                        case "TAC":
                            map.put("Tyr", map.getOrDefault("Tyr", 0) + 1);
                            break;
                        case "TAA":
                        case "TGA":
                        case "TAG":
                            map.put("Stp", map.getOrDefault("Stp", 0) + 1);
                            break;
                        case "CAT":
                        case "CAC":
                            map.put("His", map.getOrDefault("His", 0) + 1);
                            break;
                        case "CAA":
                        case "CAG":
                            map.put("Gin", map.getOrDefault("Gin", 0) + 1);
                            break;
                        case "AAT":
                        case "AAC":
                            map.put("Asn", map.getOrDefault("Asn", 0) + 1);
                            break;
                        case "AAA":
                        case "AAG":
                            map.put("Lys", map.getOrDefault("Lys", 0) + 1);
                            break;
                        case "GAT":
                        case "GAC":
                            map.put("Asp", map.getOrDefault("Asp", 0) + 1);
                            break;
                        case "GAA":
                        case "GAG":
                            map.put("Glu", map.getOrDefault("Glu", 0) + 1);
                            break;
                        case "TGT":
                        case "TGC":
                            map.put("Cys", map.getOrDefault("Cys", 0) + 1);
                            break;
                        case "TGG":
                            map.put("Trp", map.getOrDefault("Trp", 0) + 1);
                            break;
                        case "CGT":
                        case "CGC":
                        case "CGA":
                        case "CGG":
                        case "AGA":
                        case "AGG":
                            map.put("Arg", map.getOrDefault("Arg", 0) + 1);
                            break;
                        case "GGT":
                        case "GGC":
                        case "GGA":
                        case "GGG":
                            map.put("Gly", map.getOrDefault("Gly", 0) + 1);
                            break;
                    }
                }
                if (sc.hasNextLine())
                    sc.nextLine();
            }
            // write to a csv file
            String eol = System.getProperty("line.separator");
            try (Writer writer = new FileWriter(args[1])) {
                writer.append("Amino Acid").append(',').append("Amino Acid Count").append(eol);
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