package hr.java.melody.main;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Unesite note za melodiju:");
        String melody = sc.nextLine();

        char[] melodyInput = melody.toCharArray();

        GeneticAlgorithm g = new GeneticAlgorithm();
        g.setMelody(melodyInput);


        long startTime,endTime,totalTime;
        startTime = System.nanoTime();
        if(g.algorithm()){
            endTime = System.nanoTime();
            totalTime = endTime - startTime;
            System.out.println("Runtime in nanoseconds: " + totalTime /1000000000 + "D" + totalTime +
                    "a"+ totalTime/1000000);
            System.out.println(("Found at epoch: "+g.getEpoch()));
            System.out.println(("Population size" + g.getPopSize()));
            System.out.println(melodyInput);
            System.out.println(melodyInput.length);

        }else{
            System.out.println("Fail.");
        }

    }
}
