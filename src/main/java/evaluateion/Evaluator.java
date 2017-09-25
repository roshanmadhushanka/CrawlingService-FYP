package evaluateion;

import java.util.*;
import java.util.stream.IntStream;

/**
 * Created by roshanalwis on 9/2/17.
 */
public class Evaluator {
    private static ArrayList<String> getUnion(ArrayList<String> list1, ArrayList<String> list2) {
        /*
            Get union of two lists
         */

        ArrayList<String> list = new ArrayList<>();
        for(String str: list1) {
            if(!list.contains(str)) {
                list.add(str);
            }
        }

        for(String str: list2) {
            if(!list.contains(str)) {
                list.add(str);
            }
        }

        return list;
    }

    private static int count(ArrayList<String> list, String word) {
        /*
            Returns number of occurrences
         */

        int count = 0;
        for(String str: list) {
            if(str.equals(word)) {
                count++;
            }
        }

        return count;
    }

    private static int dotProduct(int[] arrayA, int[] arrayB){
        int result = 0;
        assert(arrayA.length == arrayB.length);

        for(int i=0; i<arrayA.length; i++){
            result += arrayA[i] * arrayB[i];
        }

        return result;
    }

    private static double modulus(int[] array){
        double result = 0;

        for(int i=0; i<array.length; i++){
            result += array[i] * array[i];
        }

        result = Math.sqrt(result);

        return result;
    }

    private static double cosineSimilarity(ArrayList<String> list1, ArrayList<String> list2) {
        double result;

        ArrayList<String> commonList = getUnion(list1, list2);
        int[] vector1 = new int[commonList.size()];
        int[] vector2 = new int[commonList.size()];

        for(int i=0; i<commonList.size(); i++) {
            vector1[i] = count(list1, commonList.get(i));
            vector2[i] = count(list2, commonList.get(i));
        }

        result = dotProduct(vector1, vector2) / (modulus(vector1) * modulus(vector2));

        return result;
    }

    public static void cosineSimilarityExt(ArrayList<ArrayList<String>> extList1, ArrayList<ArrayList<String>> extList2) {

        IntStream.range(0, extList1.size()).parallel().forEach((int i) ->{
            for(int j=0; j<extList2.size(); j++){
                cosineSimilarity(extList1.get(i), extList2.get(j));
            }
        });
    }

    public static void diff(HashMap<String, ArrayList<String>> version1,
                            HashMap<String, ArrayList<String>> version2) {

        Set<String> tagSet1 = version1.keySet();
        Set<String> tagSet2 = version1.keySet();

        ArrayList<String> commonTags = getUnion(new ArrayList<String>(tagSet1), new ArrayList<String>(tagSet2));
        for(String tag: commonTags) {
            // System.out.println(tag + ":" + cosineSimilarity(version1.get(tag), version2.get(tag)));
        }
    }

    public static void diffExt(HashMap<String, ArrayList<ArrayList<String>>> version1,
                            HashMap<String, ArrayList<ArrayList<String>>> version2) {
        Set<String> tagSet1 = version1.keySet();
        Set<String> tagSet2 = version2.keySet();

        ArrayList<String> commonTags = getUnion(new ArrayList<String>(tagSet1), new ArrayList<String>(tagSet2));

        long startTime = System.nanoTime();
        for(String tag: commonTags) {
            System.out.println(tag);
            cosineSimilarityExt(version1.get(tag), version2.get(tag));
        }
        long endTime = System.nanoTime();
        System.out.println("Took "+(endTime - startTime) + " ns");
    }
}
