package hr.java.melody.main;

public class Chromosome implements Comparable<Chromosome> {
    private char[] notes; // geni
    private int conflicts;
    private boolean selected;

    public Chromosome(char[] notes){
        this.notes = notes;
        this.conflicts = 0;
        this.selected = false;
    }

    public char[] getNotes() {
        return notes;
    }
    public void setNote(int index,char position){
        this.notes[index] = position;
    }
    public void computeConflicts(char[] melodyInput){
        int conflicts = 0;
        for(int i = 0; i < melodyInput.length; i++){
            if(this.notes[i] != melodyInput[i]){
                conflicts++;
            }
        }
        this.conflicts = conflicts;
    }

    public int getConflicts() {
        return conflicts;
    }

    @Override
    public int compareTo(Chromosome o) {
        int c= 0;
        for(int i = 0; i < o.getNotes().length; i++){
            if(this.notes[i] != o.getNotes()[i]){
                c++;
            }
        }
        return c;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    public boolean isSelected() {
        return selected;
    }
}
