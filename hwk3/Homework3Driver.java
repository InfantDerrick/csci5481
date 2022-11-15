import java.io.*;
import java.util.*;

public class Homework3Driver {
    public static void main(String[] args) {
        File f = new File(args[0]);
        try {
            GeneticDistance gd = new GeneticDistance(new Scanner(f));
            gd.writeToFile();
            // System.out.println((gd.getSeqs().size() - 1) * 2);
            NeighborJoining nn = new NeighborJoining(gd, 120);
            nn.writeEdgesToFile();
            nn.writeTreeToFile();
            Bootstrap bs = new Bootstrap(nn);
            bs.writeTreeToFile();
        } catch (FileNotFoundException fe) {
            System.out.println(fe.toString());
        }
    }
}
