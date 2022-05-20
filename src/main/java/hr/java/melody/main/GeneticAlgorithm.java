package hr.java.melody.main;

import org.jfugue.player.Player;


import java.util.*;

public class GeneticAlgorithm {
    private ArrayList<Chromosome> population;
    private int START_SIZE;
    private int MAX_EPOCHS;
    private Random rand;
    private char[] melodyInput;
    private int kTurnir;
    private int k;
    private double MATING_PROBABILITY;
    private double MUTATION_RATE;
    private int MUTATION_RANGE;
    private int epoch;


    public GeneticAlgorithm(){
        population = new ArrayList<>();

        START_SIZE = 80;
        MAX_EPOCHS = 100000;
        rand = new Random();
        kTurnir = START_SIZE / 2;
        k = 45; // broj jedinki od kojih se bira najbolja u k - turniru
        MATING_PROBABILITY = 1.0;
        MUTATION_RATE = 0.9;
        MUTATION_RANGE = 10000;

    }

    public void pocetnaGeneracija(){

        for(int i = 0; i < START_SIZE; i++){
            char[] pocetniKromosom = new char[this.melodyInput.length];
            for(int j = 0;j <this.melodyInput.length;j++){
                pocetniKromosom[j] = (char) (rand.nextInt(7) + 97);
            }
            Chromosome c = new Chromosome(pocetniKromosom);
            population.add(c);
            population.get(i).computeConflicts(this.melodyInput);

        }

    }
    public boolean algorithm(){
        boolean done = false;
        int epoch = 0;

        pocetnaGeneracija();

        while(!done){
            for(int i = 0; i < population.size(); i++) {
                if((population.get(i).getConflicts() == 0)) {
                    System.out.println(i);
                    done = true;
                }
            }
            int min = 0;
            Chromosome chromosome = null;
            min = population.stream().mapToInt(v -> v.getConflicts()).min().getAsInt();
            for(int i = 0; i < population.size(); i++) {
               // System.out.println("aaa"+population.get(i).getConflicts());
                if(population.get(i).getConflicts() == min){
                    chromosome = population.get(i);
                }
            }
            System.out.println(chromosome.getConflicts());

            /*if(epoch % 100 == 0){
                Player player = new Player();
                System.out.println(chromosome.getNotes());
                String string = new String();
                for(int j = 0;j < chromosome.getNotes().length;j++){
                    string = string + chromosome.getNotes()[j] + ' ';
                }
                System.out.println("aaaaaaaaa:"+ string);
                player.play(string);
            }*/
            if(done){
                break;
            }

            if(epoch == MAX_EPOCHS) {
                done = true;
            }

            fitness();
            selection();

            for(int i = 0; i < population.size(); i++) {
                population.get(i).setSelected(false);
            }

            epoch++;
            System.out.println("Epoch: " + epoch);

        }
        if(epoch >= MAX_EPOCHS) {
            System.out.println("No solution found");
            done = false;
        } else {
            for(int i = 0; i < population.size(); i++) {
                if(population.get(i).getConflicts() == 0) {
                    System.out.println(i);
                    char[] array= population.get(i).getNotes();
                    System.out.println(array);
                    /*String stringg = new String();
                    for(int j = 0;j < array.length;j++){
                        stringg = stringg + array[j] + ' ';
                    }
                    Player player = new Player();
                    player.play(stringg);*/
                }
            }
        }
        System.out.println("done.");

        System.out.println("Completed " + epoch + " epochs.");
        this.epoch = epoch;
        return done;

    }
    public void setMelody(char[] melodyInput){
        this.melodyInput = melodyInput;

    }
    public void fitness(){

        int sum = 0;
        while(sum < kTurnir){

            int i = 0;
            int min = this.melodyInput.length + 1;
            int index = 0;

            while(i < k){

                int randomValue = rand.nextInt(population.size());
                if(population.get(randomValue).getConflicts() < min){
                    min = population.get(randomValue).getConflicts();
                    index = randomValue;

                }
                i++;

            }
            population.get(index).setSelected(true);
            sum++;
        }

    }
    public void selection(){
        int getRand = 0;
        int parentA = 0;
        int parentB = 0;
        int newIndex1 = 0;
        int newIndex2 = 0;
        Chromosome newChromo1 = null;
        Chromosome newChromo2 = null;
        List<Chromosome> help = new ArrayList<>();

        for(int i = 0; i < START_SIZE/2; i++) {
            parentA = chooseParent();

            // Test probability of mating.
            getRand = rand.nextInt(101);
            if(getRand <= MATING_PROBABILITY * 100) {
                parentB = chooseParent(parentA);

                //random note za djecu
                char[] pocetniKromosom1 = new char[this.melodyInput.length];
                char[] pocetniKromosom2 = new char[this.melodyInput.length];
                for(int j = 0;j <this.melodyInput.length;j++){
                    pocetniKromosom1[j] = (char) (rand.nextInt(7) + 97);
                }
                newChromo1 = new Chromosome(pocetniKromosom1);

                for(int j = 0;j <this.melodyInput.length;j++){
                    pocetniKromosom2[j] = (char) (rand.nextInt(7) + 97);
                }
                newChromo2 = new Chromosome(pocetniKromosom2);


                population.add(newChromo1);
                newIndex1 = population.size() - 1;
                population.add(newChromo2);
                newIndex2 = population.size() - 1;

                // twoPointCrossover
               twoPointCrossover(parentA, parentB, newIndex1, newIndex2);

                population.get(newIndex1).computeConflicts(this.melodyInput);
                population.get(newIndex2).computeConflicts(this.melodyInput);
                Chromosome dijete1 = null;
                Chromosome dijete2 = null;
                Chromosome rod1 = null;
                Chromosome rod2 = null;
                dijete1 = population.get(newIndex1);
                dijete2 = population.get(newIndex2);
                rod1 = population.get(parentA);
                rod2 = population.get(parentB);
                if(rod1.getConflicts() >= dijete1.getConflicts()){
                    help.add(dijete1);
                }else{
                    help.add(rod1);
                }
                if(rod2.getConflicts() >= dijete2.getConflicts()){
                    help.add(dijete2);
                }else{
                    help.add(rod2);
                }

            }
        }
        population.clear();
        population.addAll(help);

        //mutation
        Chromosome c;
        for(int i = 0; i < population.size(); i++){
            if(rand.nextInt(101) <= MUTATION_RATE * 100){
                c = population.get(i);
                //solved
                for(int j = rand.nextInt(MUTATION_RANGE); j < c.getNotes().length;
                    j = j + rand.nextInt(MUTATION_RANGE) + 1){
                    c.getNotes()[j] = (char) (rand.nextInt(7) + 97);
                }
                c.computeConflicts(this.melodyInput);
            }
        }
    }

    public int chooseParent() {
        int parent = 0;
        Chromosome thisChromo = null;
        boolean done = false;

        while(!done) {
            parent = rand.nextInt(population.size());
            thisChromo = population.get(parent);
            if(thisChromo.isSelected() == true) {

                done = true;
            }
        }
        return parent;
    }


    public int chooseParent(int parentA) {
        int parent = 0;
        Chromosome thisChromo = null;
        boolean done = false;

        while(!done) {

            parent = rand.nextInt(population.size());
            if(parent != parentA){

                thisChromo = population.get(parent);
                if(thisChromo.isSelected() == true){
                    done = true;
                }
            }
        }

        return parent;
    }
    public void twoPointCrossover(int chromA, int chromB, int child1, int child2) {
        int j = 0;
        char item1,item2;
        Chromosome parent1 = population.get(chromA);
        Chromosome parent2 = population.get(chromB);
        Chromosome newChromo1 = population.get(child1);
        Chromosome newChromo2 = population.get(child2);

        int len = parent1.getNotes().length;
        int crossPoint1 = rand.nextInt(len);
        int crossPoint2 = rand.nextInt(len);
        while(crossPoint2 == crossPoint1){
            crossPoint2 = rand.nextInt(this.melodyInput.length);
        }

        if(crossPoint2 < crossPoint1) {
            j = crossPoint1;
            crossPoint1 = crossPoint2;
            crossPoint2 = j;
        }

        // Copy Parent genes to offspring.
        for(int i = 0; i < this.melodyInput.length; i++) {
            newChromo1.getNotes()[i] = parent1.getNotes()[i];
            newChromo2.getNotes()[i] = parent2.getNotes()[i];

        }
        for(int i = crossPoint1; i <= crossPoint2; i++) {

            // Get the two items to swap.
            item1 = parent1.getNotes()[i];
            item2 = parent2.getNotes()[i];

            newChromo1.getNotes()[i] = item2;
            newChromo2.getNotes()[i] = item1;

        }
    }


    public int getEpoch() {
        return epoch;
    }

    public int getPopSize() {
        return population.size();
    }

}
