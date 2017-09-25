package parser;

import java.util.Random;

/**
 * Created by roshanalwis on 9/23/17.
 */
public class LoopTest {
    public static int[] randArray(int size){
        int[] array = new int[size];
        Random rand = new Random();

        for(int i=0; i<size; i++){
            array[i] = rand.nextInt(50) + 1;
        }

        return array;
    }

    public static void forLoop(int[] array){
        int total = 0;

        long startTime = System.nanoTime();
        for(int i=0; i<array.length; i++){
            total += array[i];
        }
        long endTime = System.nanoTime();
        System.out.println("Took "+(endTime - startTime) + " ns");
    }

    public static void forEachLoop(int[] array){
        int total = 0;

        long startTime = System.nanoTime();
        for (int a: array) {
            total += a;
        }

        long endTime = System.nanoTime();
        System.out.println("Took "+(endTime - startTime) + " ns");
    }


    public static void main(String[] args) {
        int[] array = randArray(10000);
        forLoop(array);
        forEachLoop(array);
    }
}
