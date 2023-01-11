import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException; // Import this class to handle errors
import java.util.ArrayList;

public class efficient {
    public static final int DELTA = 30;
    public static String s1;
    public static String s2;
    public static int[][] opt;
    public static int efficientCost;
    public static final int[][] alphas =
            new int[][] {{0, 110, 48, 94}, {110, 0, 118, 48}, {48, 118, 0, 110}, {94, 48, 110, 0}};
    public static int[] prev;
    public static int[] update;
//    public static String xleft;
//    public static String xright;
    public static int[] leftCol;
    public static int[] rightCol;
//    public static String[] leftArr;
//    public static String[] rightArr;
    public static StringBuilder sb1;
    public static StringBuilder sb2;
    public static String[] efficient;
    public static String idx;

    public static void init() {
        efficientCost = Integer.MIN_VALUE;
    }

    public static void main(String[] args) {
        ArrayList<String> arr = new ArrayList<>();

        try {
            File myObj = new File(args[0]);
            Scanner myReader = new Scanner(myObj);
            // Scanner input = new Scanner(new File(args[0]));
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                arr.add(data);

            }

            StringBuilder sb = new StringBuilder(arr.get(0));
            for (int i = 1; i < arr.size(); i++) {
                if (isNumeric(arr.get(i))) {
                    int num = Integer.parseInt(arr.get(i));
                    sb = sb.insert(num + 1, sb.toString());
                } else {
                    s1 = sb.toString();
                    sb = new StringBuilder(arr.get(i));
                }
            }
            s2 = sb.toString();
//            System.out.println(s1.length()+s2.length());

            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        double startTime = getTimeInMilliseconds();
        System.gc();
        double beforeUsedMem=getMemoryInKB();

        efficient = methodEfficient(s1, s2);

        double afterUsedMem = getMemoryInKB();
        double endTime = getTimeInMilliseconds();
        double totalUsage = afterUsedMem-beforeUsedMem;
        double totalTime = endTime - startTime;

//        System.out.println(efficientCost);
//        System.out.println(efficient[0]);
//        System.out.println(efficient[1]);
//        System.out.println("totalUsage: " + totalUsage);
//        System.out.println("totalTime: " + totalTime);

        try {
            FileWriter fw = new FileWriter(args[1]);

            fw.write(String.valueOf(efficientCost));
            fw.write(System.lineSeparator());
            fw.write(efficient[0]);
            fw.write(System.lineSeparator());
            fw.write(efficient[1]);
            fw.write(System.lineSeparator());
            fw.write(String.valueOf(totalTime));
            fw.write(System.lineSeparator());
            fw.write(String.valueOf(totalUsage));

            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static double getMemoryInKB() {
        double total = Runtime.getRuntime().totalMemory();
        return (total - Runtime.getRuntime().freeMemory()) / 10e3;
    }

    private static double getTimeInMilliseconds() {
        return System.nanoTime() / 10e6;
    }

    private static String[] baseMethod(String s1, String s2) {
        int m = s1.length();
        int n = s2.length();

        // initialize
        opt = new int[m + 1][n + 1];
        for (int i = 0; i <= m; i++)
            opt[i][0] = i * DELTA;
        for (int j = 0; j <= n; j++)
            opt[0][j] = j * DELTA;

        // recursive formula
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                opt[i][j] = Math.min(getAlpha(s1.charAt(i - 1), s2.charAt(j - 1)) + opt[i - 1][j - 1],
                        Math.min(DELTA + opt[i - 1][j], DELTA + opt[i][j - 1]));
            }
        }

        return getString(s1, s2);
    }

    private static String[] getString(String s1, String s2) {
        sb1 = new StringBuilder();
        sb2 = new StringBuilder();

        int row = s1.length();
        int col = s2.length();

        while (row != 0 || col != 0) {
            if (col > 0 && row > 0 && getAlpha(s1.charAt(row - 1), s2.charAt(col - 1)) + opt[row-1][col-1] == opt[row][col] ) {
                sb1.append(s1.charAt(row - 1));
                sb2.append(s2.charAt(col - 1));
                row--;
                col--;
            }

            else if (col > 0 && opt[row][col] == opt[row][col - 1] + DELTA) {
                sb1.append('_');
                sb2.append(s2.charAt(col - 1));
                col--;
            }

            else if (row > 0 && opt[row][col] == opt[row - 1][col] + DELTA) {
                sb2.append('_');
                sb1.append(s1.charAt(row - 1));
                row--;
            }
        }
        return new String[] {sb1.reverse().toString(), sb2.reverse().toString()};
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            int d = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    private static int getAlpha(char c1, char c2) {
        idx = "ACGT";
        return alphas[idx.indexOf(c1)][idx.indexOf(c2)];

    }


    private static int[] method2DP(String x, String y) {
        prev = new int[y.length() + 1];
        for(int i = 0; i < prev.length; i++)
            prev[i] = i * DELTA;

        for(int j = 1; j < x.length() + 1; j++) {
            update = new int[y.length() + 1];
            update[0] = j * DELTA;

            for(int i = 1; i < y.length() + 1; i++)
                update[i] = Math.min(getAlpha(x.charAt(j - 1), y.charAt(i - 1)) + prev[i - 1],
                        Math.min(DELTA + update[i - 1], DELTA + prev[i]));

            for(int i=0; i < y.length() + 1; i++)
                prev[i] = update[i];
        }

        return prev;
    }

    private static String[] methodEfficient(String s1, String s2) {
        if(s1.length() <= 2 || s2.length() <= 2) {
            return baseMethod(s1, s2);
        }

        int xmid = (s2.length()-1) / 2;
        String xleft = s2.substring(0, xmid+1);
        String xright = s2.substring(xmid+1);

        leftCol = method2DP(xleft, s1);
        rightCol = method2DP(new StringBuilder(xright).reverse().toString(), new StringBuilder(s1).reverse().toString());

        int min = Integer.MAX_VALUE;
        int ymid = Integer.MAX_VALUE;

        for (int i = 0; i < s1.length()+1; i++) {
            if (leftCol[i] + rightCol[s1.length() - i] <= min) {
                min = leftCol[i] + rightCol[s1.length() - i];
                ymid = i;
            }
        }

        if (efficientCost < min) {
            efficientCost = min;
        }

        String[] leftArr = methodEfficient(s1.substring(0, ymid), xleft);
        String[] rightArr = methodEfficient(s1.substring(ymid), xright);

        return new String[]{leftArr[0] + rightArr[0], leftArr[1] + rightArr[1]};
    }
}
