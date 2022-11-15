import java.util.*;
import java.io.*;

/**
 * @author Infant Derrick Gnana Susairaj
 * @implNote This class is used to store and compute information about the
 *           genetic
 *           distances
 */

public class GeneticDistance {
    // sequences where we have [[seq head, seq]...]
    private List<String[]> seqs;
    // 2D Matrix of our distances
    private List<List<Double>> distances;

    /**
     * Constructor given a scanner
     * 
     * @param sc the scanner
     */
    public GeneticDistance(Scanner sc) {
        seqs = new LinkedList<>();
        while (sc.hasNextLine())
            seqs.add(new String[] { sc.nextLine().substring(1), sc.nextLine() });
        computeDistances();
    }

    /**
     * 
     * @param seqs given a sequences list as cunstructed above
     */
    public GeneticDistance(List<String[]> seqs) {
        this.seqs = seqs;
        computeDistances();
    }

    /**
     * comppute the distances between the sequences to get the 2D array
     */
    private void computeDistances() {
        distances = new LinkedList<>();
        for (int i = 0; i < seqs.size(); i++) {
            List<Double> temp = new LinkedList<>();
            for (int j = 0; j < seqs.size(); j++) {
                int dis = 0;
                for (int k = 0; k < seqs.get(i)[1].length(); k++) // going through each and every conbination
                    if (seqs.get(i)[1].charAt(k) != seqs.get(j)[1].charAt(k))
                        dis++;
                // scoring based on distance
                double score = (double) dis / seqs.get(i)[1].length();
                temp.add(score);
            }
            distances.add(temp);
        }
    }

    public void printDistances() {
        System.out.println(distances);
    }

    /**
     * write to file
     */
    public void writeToFile() {
        try {
            FileWriter w = new FileWriter("genetic-distances.txt");
            w.write("\t\t");
            for (int i = 0; i < seqs.size(); i++)
                w.write(seqs.get(i)[0] + '\t');
            for (int i = 0; i < distances.size(); i++) {
                w.write('\n');
                w.write(seqs.get(i)[0] + '\t');
                for (int j = 0; j < seqs.size(); j++)
                    w.write(String.format("%.5f", distances.get(i).get(j)) + '\t');
            }
            w.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public List<List<Double>> getDistances() {
        return distances;
    }

    public List<String[]> getSeqs() {
        return seqs;
    }
}