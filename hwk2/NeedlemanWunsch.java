import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * @author: Infant Derrick Gnana Susairaj
 * @version: 1.0
 * @apiNote: Discussed with Kailash Kalyanasundaram
 * @see <a href=
 *      "https://en.wikipedia.org/wiki/Needleman%E2%80%93Wunsch_algorithm">Needleman–Wunsch
 *      algorithm</a>
 */
public class NeedlemanWunsch {
    public static void main(String[] args) throws FileNotFoundException {
        /*
         * We set up for file reading here and get the paths and the arguments from the
         * terminal arguments
         */
        String seq1Path = args[0];
        String seq2Path = args[1];
        String out = args[2];
        int fixedGapPenalty = -2, mismatchPenalty = -1, match = 1, startEndPenalty = fixedGapPenalty;
        // to retrieve the parameters
        switch (args.length) {
            case 4 -> fixedGapPenalty = Integer.parseInt(args[3]);
            case 5 -> mismatchPenalty = Integer.parseInt(args[4]);
            case 6 -> match = Integer.parseInt(args[5]);
            case 7 -> startEndPenalty = Integer.parseInt(args[6]);
        }
        if (args.length < 7)
            startEndPenalty = fixedGapPenalty;
        File seq1File = new File(seq1Path);
        File seq2File = new File(seq2Path);
        Scanner seq1Scanner = new Scanner(seq1File);
        Scanner seq2Scanner = new Scanner(seq2File);
        String seq1Header = seq1Scanner.nextLine().trim();
        String seq2Header = seq2Scanner.nextLine().trim();
        String seq1 = seq1Scanner.nextLine().trim();
        String seq2 = seq2Scanner.nextLine().trim();
        seq1Scanner.close();
        seq2Scanner.close();
        // this finish reading the fna files.
        // we do a similarity matrix to see the difference in similarity between the two
        // files
        int[][] similarity = new int[seq1.length()][seq2.length()];
        for (int i = 0; i < similarity.length; i++)
            for (int j = 0; j < similarity[i].length; j++)
                similarity[i][j] = seq1.charAt(i) == seq2.charAt(j) ? match : mismatchPenalty; // assign match or
                                                                                               // mismatchpenality based
                                                                                               // on matches
        // this is the DP grid we will be using to solve
        int[][] F = new int[seq1.length() + 1][seq2.length() + 1];
        for (int i = 0; i < seq1.length() + 1; i++)
            F[i][0] = startEndPenalty * i;
        for (int i = 0; i < seq2.length() + 1; i++)
            F[0][i] = startEndPenalty * i;
        // this is for the startend penalty creation
        // this is filling the the full martrix so that we can traverse to allign it
        for (int i = 1; i < seq1.length() + 1; i++)
            for (int j = 1; j < seq2.length() + 1; j++)
                F[i][j] = Math.max(Math.max(F[i - 1][j - 1] + similarity[i - 1][j - 1],
                        F[i - 1][j] + ((i != 1 && j != 1 && i != seq1.length() && j != seq2.length()) ? fixedGapPenalty
                                : startEndPenalty)),
                        F[i][j - 1] + ((i != 1 && j != 1 && i != seq1.length() && j != seq2.length()) ? fixedGapPenalty
                                : startEndPenalty)); // distingusing between max of diagonal, left, or up based on start
                                                     // or fixed
        String alignedSequence1 = "";
        String alignedSequence2 = "";
        int score = 0, count = 0;
        for (int i = seq1.length(), j = seq2.length(); i > 0 || j > 0;) {
            int gapPenalty = ((i == seq1.length() || j == seq2.length() || i == 0 || j == 0) ? startEndPenalty
                    : fixedGapPenalty); // this is for gapPenalty based on where we are
            if (i > 0 && j > 0 && F[i][j] == F[i - 1][j - 1] + similarity[i - 1][j - 1]) { // diagonal matches
                score += seq1.charAt(i - 1) == seq2.charAt(j - 1) ? match : mismatchPenalty;
                count += seq1.charAt(i - 1) == seq2.charAt(j - 1) ? 0 : 1;
                alignedSequence1 = seq1.charAt(i - 1) + alignedSequence1;
                alignedSequence2 = seq2.charAt(j - 1) + alignedSequence2;
                i--;
                j--;
            } else if (i > 0 && F[i][j] == F[i - 1][j] + fixedGapPenalty) { // up matches
                alignedSequence2 = "_" + alignedSequence2;
                alignedSequence1 = seq1.charAt(i - 1) + alignedSequence1;
                i--;
                score += gapPenalty;
                count += gapPenalty != 0 ? 1 : 0;
            } else { // left matches
                alignedSequence1 = "_" + alignedSequence1;
                alignedSequence2 = seq2.charAt(j - 1) + alignedSequence2;
                j--;
                score += gapPenalty;
                count += gapPenalty != 0 ? 1 : 0;
            }
        }
        // this is to build a visualization of what is going on.
        StringBuilder visualization = new StringBuilder();
        for (int i = 0; i < alignedSequence1.length(); i++) {
            if (alignedSequence1.charAt(i) == alignedSequence2.charAt(i)) // if match
                visualization.append('|');
            else if (alignedSequence1.charAt(i) == '_' || alignedSequence2.charAt(i) == '_') // if gap
                visualization.append(' ');
            else // if mismatch
                visualization.append('x');
        }
        // write to file
        try {
            FileWriter wr = new FileWriter(out);
            wr.write("" + score + "\n");
            wr.write(seq1Header + "\n");
            wr.write(alignedSequence1 + "\n");
            wr.write(visualization.toString() + "\n");
            wr.write(alignedSequence2 + "\n");
            wr.write(seq2Header + "\n");
            wr.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        System.out.println("Mismatches: " + count);
    }
}