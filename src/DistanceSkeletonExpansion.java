import java.io.*;
import java.util.Scanner;

public class DistanceSkeletonExpansion {
    int numRows, numCols, minVal, maxVal, newMinVal, newMaxVal;
    int [] [] zeroFramedArray;
    int [] [] skeletonAry;
    Scanner inFile;
    PrintWriter outFile1, outFile2;
    public DistanceSkeletonExpansion (Scanner in, PrintWriter out1, PrintWriter out2) {
        inFile = in;
        outFile1 = out1;
        outFile2 = out2;
        numRows = inFile.nextInt();
        numCols = inFile.nextInt();
        minVal = inFile.nextInt();
        maxVal = inFile.nextInt();
        zeroFramedArray = new int [numRows+2][numCols+2];
        for (int i = 0; i < numRows; i++) // Frame array
            for (int j = 0; j < numCols; j++)
                zeroFramedArray[i+1][j+1] = inFile.nextInt();
    }

    private void printAry (int [] [] ary) {
        for (int i = 0; i < numRows+2; i++) { // Print array
            for (int j = 0; j < numCols + 2; j++) {
                System.out.print(ary[i][j] + " ");
            } System.out.println();
        }
    }

    public void compute8Distance () {
        firstPass_8Distance();
        prettyPrint (zeroFramedArray, outFile1, 1);
        secondPass_8Distance();
    }

    private void firstPass_8Distance () {
        for (int i = 1; i <= numRows; i++) {
            for (int j = 1; j <= numCols; j++) {
                if (zeroFramedArray[i][j] > 0) {
                    int a = zeroFramedArray[i-1][j-1];
                    int b = zeroFramedArray[i-1][j];
                    int c = zeroFramedArray[i-1][j+1];
                    int d = zeroFramedArray[i][j-1];
                    zeroFramedArray[i][j] = (Math.min(a, (Math.min(b, Math.min(c,d)))) + 1);
                }
            }
        }
    }

    private void secondPass_8Distance () {

    }

    public void prettyPrint (int [] [] ary, PrintWriter outFile, int passNum) {
        outFile.println("----------------------------------------------------------------------------------------");
        outFile.println("PASS " + passNum + ":\n");
        for (int i = 1; i <= numRows; i++) {
            for (int j = 1; j <= numCols; j++) {
                if (ary[i][j] > 0)
                    outFile.print(ary[i][j] + ((ary[i][j] < 10) ? "  " : " "));
                else outFile.print("   ");
            } outFile.println("\n");
        }
    }

    public static void main (String [] args) {
        if (args.length != 3) {
            System.out.println("ERROR: Illegal arguments");
            System.exit(1);
        }
        try {
            Scanner inFile1 = new Scanner(new FileReader(args[0]));
            PrintWriter outFile1 = new PrintWriter(new BufferedWriter(new FileWriter(args[1])), true);
            PrintWriter outFile2 = new PrintWriter(new BufferedWriter(new FileWriter(args[2])), true);
            DistanceSkeletonExpansion skeleton = new DistanceSkeletonExpansion(inFile1,outFile1,outFile2);
            skeleton.compute8Distance();
            inFile1.close();
            outFile1.close();
            outFile2.close();
        } catch (FileNotFoundException e) {
            System.out.println("One or more input files not found.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


