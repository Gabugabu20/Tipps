package model;
public enum Section {
    OBEN("OBEN"), UNTEN("UNTEN"), LINKS("LINKS"), RECHTS("RECHTS");

    private String name;

    private Section(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
