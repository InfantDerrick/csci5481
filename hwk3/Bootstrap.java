import java.util.*;
import java.io.*;

/**
 * @author Infant Derrick Gnana Susairaj
 * @implNote Bootstrap functions
 */
public class Bootstrap {
    private NeighborJoining nn;
    private double[] vals;
    List<Integer> ids;

    /**
     * given an nn we can compute the bootstrap values
     * 
     * @param nn
     */
    public Bootstrap(NeighborJoining nn) {
        this.nn = nn;
        ids = dfs(nn.getRoot(), new LinkedList<>(), nn);
        double[] vals = new double[ids.size()];
        Map<Integer, List<Integer>> partitions = partition(nn.getRoot(), nn);
        for (int i = 0; i < 100; i++) {
            List<String[]> bs = new LinkedList<>();
            List<Integer> idxs = new LinkedList<>();
            int N = nn.getGb().getSeqs().get(0)[1].length();
            List<String[]> org = nn.getGb().getSeqs();
            for (int j = 0; j < N; j++)
                idxs.add(new Random().nextInt(N - 1));
            for (String[] seq : org) {
                StringBuilder sb = new StringBuilder();
                for (int idx : idxs)
                    sb.append(seq[1].charAt(idx));
                bs.add(new String[] { seq[0], sb.toString() });
            }
            GeneticDistance bsgb = new GeneticDistance(bs);
            NeighborJoining bsnn = new NeighborJoining(bsgb, 120);
            Map<Integer, List<Integer>> bspartitions = partition(bsnn.getRoot(), bsnn);
            for (int j = 0; j < ids.size(); j++)
                if (partitions.containsKey(ids.get(j)) && bspartitions.containsKey(ids.get(j))
                        && partitions.get(ids.get(j)).equals(bspartitions.get(ids.get(j))))
                    vals[j]++;

        }
        for (int i = 0; i < vals.length; i++)
            vals[i] = vals[i] / 100.0;
        this.vals = vals;
    }

    /**
     * write to bootstrap.txt
     */
    public void writeTreeToFile() {
        try {
            FileWriter w = new FileWriter("bootstrap.txt");
            for (int i = 0; i < vals.length; i++)
                w.write(new StringBuilder().append(ids.get(i)).append('\t').append(vals[i]).append('\n')
                        .toString());
            w.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /*
     * dfs to go through the tree
     */
    private List<Integer> dfs(int root, List<Integer> l, NeighborJoining nn) {
        if (!(nn.getMap().get(root) == null || nn.getMap().get(root).isEmpty())) {
            l.add(root);
            for (NeighborJoining.Pair p : nn.getMap().get(root))
                dfs(p.node, l, nn);
        }
        return l;
    }

    /*
     * partition calculation
     */
    private Map<Integer, List<Integer>> partition(int root, NeighborJoining nn) {
        Queue<Integer> q = new ArrayDeque<>();
        q.add(root);
        Map<Integer, List<Integer>> p = new LinkedHashMap<>();
        while (!q.isEmpty()) {
            int f = q.poll();
            if (nn.getMap().containsKey(f) && !nn.getMap().get(f).isEmpty()) {
                p.put(f, dfs(f, new LinkedList<>(), nn));
                for (NeighborJoining.Pair pair : nn.getMap().get(f))
                    q.add(pair.node);
            }
        }
        return p;
    }
}
