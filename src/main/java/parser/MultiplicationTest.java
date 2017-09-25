package parser;

import java.util.Random;

/**
 * Created by roshanalwis on 9/23/17.
 */
public class MultiplicationTest {
    public static int[] randArray(int size){
        int[] array = new int[size];
        Random rand = new Random();

        for(int i=0; i<size; i++){
            array[i] = rand.nextInt(50) + 1;
        }

        return array;
    }

    public static void usingPower(int[] array){
        int result = 0;

        long startTime = System.nanoTime();
        for(int i=0; i<array.length; i++){
            result += Math.pow(array[i], 2);
        }
        long endTime = System.nanoTime();
        System.out.println("Took "+(endTime - startTime) + " ns");
    }

    public static void justMultiplication(int[] array){
        int result = 0;

        long startTime = System.nanoTime();
        for(int i=0; i<array.length; i++){
            result += array[i] * array[i];
        }
        long endTime = System.nanoTime();
        System.out.println("Took "+(endTime - startTime) + " ns");
    }

    public static void main(String[] args) {
        int[] array = randArray(1000);
        usingPower(array);
        justMultiplication(array);
    }
}
