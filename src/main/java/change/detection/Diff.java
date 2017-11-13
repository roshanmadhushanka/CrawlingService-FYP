package change.detection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by eranga on 10/27/17.
 */
public class Diff
{
    public String[] diff(String X, String Y){
        /*
            Returns length of Longest Common Sub-sequence for X[0..m-1], Y[0..n-1]
        */
        int m = X.length();
        int n = Y.length();
        int[][] L = new int[m+1][n+1];

        // Following steps build L[m+1][n+1] in bottom up fashion. Note
        // that L[i][j] contains length of LCS of X[0..i-1] and Y[0..j-1]
        for (int i=0; i<=m; i++)
        {
            for (int j=0; j<=n; j++)
            {
                if (i == 0 || j == 0)
                    L[i][j] = 0;
                else if (X.charAt(i-1) == Y.charAt(j-1))
                    L[i][j] = L[i-1][j-1] + 1;
                else
                    L[i][j] = Math.max(L[i-1][j], L[i][j-1]);
            }
        }

        // Following code is used to print LCS
        int index = L[m][n];
        int temp = index;

        // Create a character array to store the lcs string
        char[] lcs = new char[index+1];
        lcs[index] = '\0'; // Set the terminating character
        // Create two strings to contain differences
        List<String> x = new ArrayList();
        List<String> y = new ArrayList();
        // Start from the right-most-bottom-most corner and
        // one by one store characters in lcs[]
        int i = m, j = n;
        while (i > 0 && j > 0)
        {
            // If current character in X[] and Y are same, then
            // current character is part of LCS
            if (X.charAt(i-1) == Y.charAt(j-1))
            {
                // Put current character in result
                lcs[index-1] = X.charAt(i-1);
                x.add(String.valueOf(X.charAt(i-1)));
                y.add(String.valueOf(X.charAt(i-1)));

                // reduce values of i, j and index
                i--;
                j--;
                index--;
            }

            // If not same, then find the larger of two and
            // go in the direction of larger value
            else if (L[i-1][j] > L[i][j-1]) {
                x.addAll(new ArrayList(Arrays.asList("</mark>", String.valueOf(X.charAt(i - 1))
                        ,"<mark>")));
                i--;
            }else {
                y.addAll(new ArrayList(Arrays.asList("</mark>",  String.valueOf(Y.charAt(j - 1))
                        ,"<mark>")));
                j--;
            }
        }

        Collections.reverse(x);
        Collections.reverse(y);
        return new String[]{String.join("", x), String.join("", y)};
    }
}
