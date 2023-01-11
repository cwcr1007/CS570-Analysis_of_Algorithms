import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;

public class basic {
    public static final int DELTA = 30;
    public static String s1;
    public static String s2;
    public static int[][] opt;
    public static int cost;
    public static String[] basic;
    public static double totalTime;
    public static double totalMemory;
    public static final int[][] alphas =
            new int[][] {{0, 110, 48, 94}, {110, 0, 118, 48}, {48, 118, 0, 110}, {94, 48, 110, 0}};

    public static void main(String[] args) {
        ArrayList<String> arr = new ArrayList<>();

        try {
            File myObj = new File(args[0]);
            Scanner myReader = new Scanner(myObj);
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
//            System.out.println(s1);
//            System.out.println(s2);

            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        double startTime = getTimeInMilliseconds();
        System.gc();
        double beforeUsedMem=getMemoryInKB();

        basic = baseMethod(s1, s2);

        double afterUsedMem = getMemoryInKB();
        double endTime = getTimeInMilliseconds();
        totalMemory = afterUsedMem-beforeUsedMem;
        totalTime = endTime - startTime;

//        System.out.println(basic[0]);
//        System.out.println(basic[1]);
//        System.out.println("totalUsage: " + totalMemory);
//        System.out.println("totalTime: " + totalTime);

        try {
            FileWriter fw = new FileWriter(args[1]);

            fw.write(String.valueOf(cost));
            fw.write(System.lineSeparator());
            fw.write(basic[0]);
            fw.write(System.lineSeparator());
            fw.write(basic[1]);
            fw.write(System.lineSeparator());
            fw.write(String.valueOf(totalTime));
            fw.write(System.lineSeparator());
            fw.write(String.valueOf(totalMemory));

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

        cost = opt[m][n];
        return getString(s1, s2);
    }

    private static String[] getString(String s1, String s2) {
        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();

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
        String idx = "ACGT";
        return alphas[idx.indexOf(c1)][idx.indexOf(c2)];

    }
}
