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
        loadImage(inFile);
        skeletonAry = new int [numRows+2][numCols+2];
    }

    public void loadImage (Scanner in) {
        for (int i = 0; i < numRows; i++) // Frame array
            for (int j = 0; j < numCols; j++)
                zeroFramedArray[i+1][j+1] = in.nextInt();
    }

    private void convertToBinary (int [] [] ary) {
        for (int i = 1; i <= numRows; i++) { // Print array
            for (int j = 1; j <= numCols; j++) {
                if (zeroFramedArray[i][j] >= 1)
                    zeroFramedArray[i][j] = 1;
                else zeroFramedArray[i][j] = 0;
            }
        }
    }
    private void ary2File (int [] [] ary, PrintWriter file) {
        if (ary == zeroFramedArray)
            convertToBinary(zeroFramedArray);
        for (int i = 1; i <= numRows; i++) { // Print array
            for (int j = 1; j <= numCols; j++) {
                file.print(ary[i][j] + " ");
            } file.println();
        }
    }

    public void compute8Distance () {
        firstPass_8Distance();
        prettyPrint (zeroFramedArray, outFile1, "Compute Distance Pass 1:");
        secondPass_8Distance();
        prettyPrint(zeroFramedArray,outFile1,"Compute Distance Pass 2:");
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
                }
                int newValue = zeroFramedArray[i][j];
                if (newValue < newMinVal) newMinVal = newValue;
                if (newValue > newMaxVal) newMaxVal = newValue;
            }
        }
    }

    public void computeLocalMaxima () {
        for (int i = 1; i <= numRows; i++)
            for (int j = 1; j <= numCols; j++)
                if (zeroFramedArray[i][j] > 0 && isMaximum(i,j))
                    skeletonAry[i][j] = zeroFramedArray[i][j];
        prettyPrint(skeletonAry,outFile1,"Local Maxima: ");
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

    public void prettyPrint (int [] [] ary, PrintWriter outFile, String message) {
        outFile.println("-----------------------------------------------------------------------------------------------------------------------");
        outFile.println(message + "\n");
        for (int i = 1; i <= numRows; i++) {
            for (int j = 1; j <= numCols; j++) {
                if (ary[i][j] > 0)
                    outFile.print(ary[i][j] + ((ary[i][j] < 10) ? "  " : " "));
                else outFile.print("   ");
            } outFile.println("\n");
        }
    }

    public void output2File (PrintWriter outFile, int [] [] ary) {
        outFile.println(numRows + " " + numCols + " "
                + (ary == zeroFramedArray ? minVal : newMinVal)
                + " " + (ary == zeroFramedArray ? maxVal : newMaxVal));
        ary2File (ary,outFile);
    }
    
    public void skeletonExpansion () {
        firstPass_Expansion();
        prettyPrint(zeroFramedArray,outFile2,"Expansion Pass 1:");
        secondPass_Expansion();
        prettyPrint(zeroFramedArray,outFile2,"Expansion Pass 2:");
    }

    private void firstPass_Expansion () {
        for (int i = 1; i <= numRows; i++) {
            for (int j = 1; j <= numCols; j++) {
                if (zeroFramedArray[i][j] == 0) {
                    int a = zeroFramedArray[i-1][j];
                    int b = zeroFramedArray[i][j-1];
                    int c = zeroFramedArray[i][j+1];
                    int d = zeroFramedArray[i+1][j];
                    int maxN = Math.max(a,Math.max(b,Math.max(c,d)));
                    if (maxN > 0)
                        zeroFramedArray[i][j] = maxN-1;
                }
            }
        }
    }

    private void secondPass_Expansion () {
        for (int i = numRows; i > 0; i--) {
            for (int j = numCols; j > 0; j--) {
                int pIJ = zeroFramedArray[i][j];
                int a = zeroFramedArray[i-1][j];
                int b = zeroFramedArray[i][j-1];
                int c = zeroFramedArray[i][j+1];
                int d = zeroFramedArray[i+1][j];
                int maxN = Math.max(pIJ,Math.max(a,Math.max(b,Math.max(c,d))));
                if (maxN > pIJ)
                    zeroFramedArray[i][j] = maxN-1;
                int newValue = zeroFramedArray[i][j];
                if (newValue < newMinVal) newMinVal = newValue;
                if (newValue > newMaxVal) newMaxVal = newValue;
            }
        }
    }

    public static void main (String [] args) {
        if (args.length != 3) {
            System.out.println("ERROR: Illegal arguments");
            System.exit(1);
        }
        try {
            //Compression
            Scanner inFile1 = new Scanner(new FileReader(args[0]));
            PrintWriter outFile1 = new PrintWriter(new BufferedWriter(new FileWriter(args[1])), true);
            PrintWriter outFile2 = new PrintWriter(new BufferedWriter(new FileWriter(args[2])), true);
            DistanceSkeletonExpansion DSE = new DistanceSkeletonExpansion(inFile1,outFile1,outFile2);
            DSE.compute8Distance();
            String skeletonFileName, expansionFileNm;
            if (args[0].indexOf('.') > 0) {
                skeletonFileName = args[0].substring(0, args[0].lastIndexOf('.'))
                        + "_skeleton.txt";
                expansionFileNm = args[0].substring(0, args[0].lastIndexOf('.'))
                        + "_expansion.txt";
            }
            else {
                skeletonFileName = args[0] + "_skeleton.txt";
                expansionFileNm = args[0] + "_expansion.txt";
            }
            PrintWriter skeletonFile = new PrintWriter(new BufferedWriter(new FileWriter(skeletonFileName)), true);
            DSE.computeLocalMaxima();
            DSE.output2File(skeletonFile, DSE.skeletonAry);
            outFile1.close();
            skeletonFile.close();
            //Expansion
            Scanner skeletonInputFile = new Scanner(new FileReader(skeletonFileName));
            for (int i = 1; i <= 4; i++) skeletonInputFile.nextInt();
            DSE.loadImage(skeletonInputFile);
            PrintWriter expansionFile = new PrintWriter(new BufferedWriter(new FileWriter(expansionFileNm)), true);
            DSE.skeletonExpansion();
            DSE.output2File(expansionFile, DSE.zeroFramedArray);
            inFile1.close();
            outFile2.close();
            skeletonInputFile.close();
            expansionFile.close();

        } catch (FileNotFoundException e) {
            System.out.println("One or more input files not found.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}