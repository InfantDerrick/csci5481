import java.util.*;
import java.io.*;

/**
 * @author Infant Derrick Gnana Susairaj
 * @implNote computations and storing for NeighborJoining
 */
public class NeighborJoining {
    public static void main(String[] args) {
        File f = new File(args[0]);
        try {
            GeneticDistance gb = new GeneticDistance(new Scanner(f));
            NeighborJoining nn = new NeighborJoining(gb, gb.getSeqs().size() * 2);
            nn.writeEdgesToFile();
            nn.writeTreeToFile();
        } catch (FileNotFoundException fe) {
            System.out.println(fe.toString());
        }
    }

    /**
     * @implNote Helper Pair class
     */
    public class Pair {
        int node;
        double edge;

        /**
         * Construct a pair based on node and edge
         * 
         * @param node child node
         * @param edge distance from parent to child
         */
        public Pair(int node, double edge) {
            this.node = node;
            this.edge = edge;
        }

        @Override
        public String toString() {
            return ("" + node + "->" + edge);
        }
    }

    private GeneticDistance gb;
    private Map<String, Integer> mapOfIds;
    private Map<Integer, Set<Pair>> map;
    private int root;
    private int seq;

    /**
     * given a genetic distance and seq count we compute nn
     * 
     * @param gb  genetic distance
     * @param seq
     */
    public NeighborJoining(GeneticDistance gb, int seq) {
        this.gb = gb;
        mapOfIds = new LinkedHashMap<>();
        map = new LinkedHashMap<>();
        for (int i = 0; i < gb.getSeqs().size(); i++)
            mapOfIds.put(gb.getSeqs().get(i)[0], i + 1);
        List<String> headers = new LinkedList<>();
        for (String[] x : gb.getSeqs())
            headers.add(x[0]);
        this.seq = seq;
        nn(headers, gb.getDistances(), seq);
    }

    /**
     * do neighborjoining
     * 
     * @param headers
     * @param dists
     * @param seq
     */
    private void nn(List<String> headers, List<List<Double>> dists, int seq) {
        // at the very end we want to get the last bit of the tree in
        if (dists.size() <= 2) {
            int key = mapOfIds.get(headers.get(0)) == null ? Integer.parseInt(headers.get(0))
                    : mapOfIds.get(headers.get(0));
            Set<Pair> s = map.getOrDefault(key, new LinkedHashSet<>());
            s.add(new Pair(mapOfIds.get(headers.get(1)) == null ? Integer.parseInt(headers.get(1))
                    : mapOfIds.get(headers.get(1)),
                    dists.get(0).get(1)));
            root = mapOfIds.get(headers.get(0)) == null ? Integer.parseInt(headers.get(0))
                    : mapOfIds.get(headers.get(0));
            return;
        }
        // q matrix computation
        double[] qinfo = getQ(dists);
        // get edge distances
        double[] edges = edge(dists, (int) qinfo[0], (int) qinfo[1]);
        // adjacency list calculations
        Set<Pair> s = map.getOrDefault(seq, new LinkedHashSet<>());
        s.add(new Pair(
                mapOfIds.get(headers.get((int) qinfo[0])) == null ? Integer.parseInt(headers.get((int) qinfo[0]))
                        : mapOfIds.get(headers.get((int) qinfo[0])),
                edges[0]));
        s.add(new Pair(
                mapOfIds.get(headers.get((int) qinfo[1])) == null ? Integer.parseInt(headers.get((int) qinfo[1]))
                        : mapOfIds.get(headers.get((int) qinfo[1])),
                edges[1]));
        map.put(seq, s);
        // removal of old nodes
        headers.remove((int) qinfo[0]);
        headers.remove((int) qinfo[0] > (int) qinfo[1] ? (int) qinfo[1] : (int) qinfo[1] - 1);
        headers.add(seq + "");
        // recurse to keep going
        nn(headers, newDist(dists, (int) qinfo[0], (int) qinfo[1]), --seq);
    }

    /**
     * calculate Q Matrix
     * 
     * @param dists
     * @return
     */
    private double[] getQ(List<List<Double>> dists) {
        double[][] q = new double[dists.size()][dists.get(0).size()];
        int mini = 0, minj = 0;
        double min = Double.MAX_VALUE;
        for (int i = 0; i < dists.size(); i++) {
            for (int j = 0; j < dists.get(i).size(); j++) {
                if (i != j) {
                    q[i][j] = (dists.size() - 2) * dists.get(i).get(j) - listSum(dists.get(i)) - listSum(dists.get(j));
                    if (q[i][j] < min) {
                        mini = i;
                        minj = j;
                        min = q[i][j];
                    }
                }
            }
        }
        return new double[] { mini, minj };
    }

    /**
     * calculate edges
     * 
     * @param dists
     * @param i
     * @param j
     * @return
     */
    private double[] edge(List<List<Double>> dists, int i, int j) {
        double ei = (1 / 2.0 * dists.get(i).get(j))
                + (1 / (2.0 * (dists.size() - 2))) * (listSum(dists.get(i)) - listSum(dists.get(j)));
        return new double[] { ei, dists.get(i).get(j) - ei };
    }

    private double listSum(List<Double> l) {
        double sum = 0;
        for (double x : l)
            sum += x;
        return sum;
    }

    /**
     * calculate new distances matrix
     * 
     * @param dists
     * @param mini
     * @param minj
     * @return
     */
    private List<List<Double>> newDist(List<List<Double>> dists, int mini, int minj) {
        double[][] temp = new double[dists.size() + 1][dists.get(0).size() + 1];
        for (int i = 0; i < dists.size(); i++)
            for (int j = 0; j < dists.size(); j++)
                temp[i][j] = dists.get(i).get(j);
        for (int i = 0; i < dists.size(); i++) {
            temp[dists.size()][i] = 0.5 * (dists.get(mini).get(i) + dists.get(minj).get(i) - dists.get(mini).get(minj));
            temp[i][dists.size()] = temp[dists.size()][i];
        }
        double[][] ans = new double[dists.size() - 1][dists.get(0).size() - 1];
        int x = 0, y = 0;
        for (int i = 0; i < dists.size() + 1; i++) {
            if (!(i == mini || i == minj)) {
                y = 0;
                for (int j = 0; j < dists.size() + 1; j++)
                    if (!(j == mini || j == minj))
                        ans[x][y++] = temp[i][j];
                x++;
            }
        }
        List<List<Double>> toRtn = new LinkedList<>();

        for (int i = 0; i < ans.length; i++) {
            List<Double> l = new LinkedList<>();
            for (int j = 0; j < ans[i].length; j++)
                l.add(ans[i][j]);
            toRtn.add(l);
        }
        return toRtn;
    }

    /**
     * run a preorder for the edges
     * 
     * @param root
     * @param l
     * @return
     */
    private List<double[]> preorder(int root, List<double[]> l) {
        if (!(map.get(root) == null || map.get(root).isEmpty())) {
            for (Pair p : map.get(root)) {
                l.add(new double[] { root, p.node, p.edge });
                preorder(p.node, l);
            }
        }
        return l;
    }

    public void writeEdgesToFile() {
        try {
            FileWriter w = new FileWriter("edges.txt");
            for (double[] x : preorder(root, new LinkedList<>()))
                w.write(new StringBuilder().append((int) x[0]).append('\t').append((int) x[1]).append('\t').append(x[2])
                        .append('\n').toString());
            w.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     * run a post order for newick
     * 
     * @param root
     * @return
     */
    private String postorder(int root) {
        if (map.get(root) == null)
            if (root >= 1 && root <= seq / 2 + 1)
                return gb.getSeqs().get(root - 1)[0];
        List<String> l = new LinkedList<>();
        for (Pair p : map.get(root))
            l.add(new StringBuilder().append(postorder(p.node)).append(':').append(p.edge).toString());
        return new StringBuilder().append('(').append(l.toString().substring(1, l.toString().length() - 1))
                .append(')').toString();

    }

    public void writeTreeToFile() {
        try {
            FileWriter w = new FileWriter("tree.tre");
            w.write(postorder(root).replaceAll("\\s", "") + ';');
            w.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public GeneticDistance getGb() {
        return gb;
    }

    public Map<Integer, Set<Pair>> getMap() {
        return map;
    }

    public int getRoot() {
        return root;
    }
}
