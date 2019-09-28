import java.io.*;
import java.util.Scanner;

public class DistanceSkeletonExpansion {

    private int numRows, numCols, minVal, maxVal, newMinVal, newMaxVal;
    private int [] [] zeroFramedArray;
    private int [] [] skeletonAry;
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
        newMinVal = 999;
        newMaxVal = -999;
        zeroFramedArray = new int [numRows+2][numCols+2];
        for (int i = 0; i < numRows; i++) // Frame array
            for (int j = 0; j < numCols; j++)
                zeroFramedArray[i+1][j+1] = inFile.nextInt();
        skeletonAry = new int [numRows+2][numCols+2];
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
        prettyPrint(zeroFramedArray,outFile1,2);
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
        for (int i = numRows; i > 0; i--) {
            for (int j = numCols; j > 0; j--) {
                if (zeroFramedArray[i][j] > 0) {
                    int e = zeroFramedArray[i][j+1];
                    int f = zeroFramedArray[i+1][j-1];
                    int g = zeroFramedArray[i+1][j];
                    int h = zeroFramedArray[i+1][j+1];
                    int x = zeroFramedArray[i][j];
                    zeroFramedArray[i][j] = Math.min(x,(Math.min(e, (Math.min(f, Math.min(g,h))))+1));
                    int newValue = zeroFramedArray[i][j];
                    if (newValue < newMinVal) newMinVal = newValue;
                    if (newValue > newMaxVal) newMaxVal = newValue;
                }
            }
        }
    }

    public void computeLocalMaxima () {
        for (int i = 1; i <= numRows; i++)
            for (int j = 1; j <= numCols; j++)
                if (zeroFramedArray[i][j] > 0 && isMaximum(i,j))
                    skeletonAry[i][j] = zeroFramedArray[i][j];
        prettyPrint(skeletonAry,outFile1,0);
    }

    boolean isMaximum (int i, int j) {
        for (int row = i - 1; row <= i + 1; row++) {
            for (int col = j - 1; col <= j + 1; col++) {
                if ((row == i || col == j) && !(row==i && col==j)) {
                    if (zeroFramedArray[row][col] > zeroFramedArray[i][j])
                        return false;
                }
            }
        }
        return true;
    }

    public void prettyPrint (int [] [] ary, PrintWriter outFile, int passNum) {
        outFile.println("-----------------------------------------------------------------------------------------------------------------------");
        if (passNum == 0) outFile.println("Local Maxima:\n");
        else outFile.println("EXPANSION PASS " + passNum + ":\n");
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
            DistanceSkeletonExpansion DSE = new DistanceSkeletonExpansion(inFile1,outFile1,outFile2);
            DSE.compute8Distance();
            String skeletonFileName;
            if (args[0].indexOf('.') > 0)
                skeletonFileName = args[0].substring(0,args[0].lastIndexOf('.'))
                        + "_skeleton.txt";
            else skeletonFileName = args[0] + "_skeleton.txt";
            PrintWriter skeletonFile = new PrintWriter(new BufferedWriter(new FileWriter(skeletonFileName)), true);
            DSE.computeLocalMaxima();
            inFile1.close();
            outFile1.close();
            outFile2.close();
            skeletonFile.close();
        } catch (FileNotFoundException e) {
            System.out.println("One or more input files not found.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


