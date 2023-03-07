package model;

import java.util.ArrayList;

public class Edge {
    private int id;
    private boolean selected;
    private boolean solution;
    private Node node1;
    private Node node2;

    private int value;

    private ArrayList<Section> sections = new ArrayList<>();

    public Edge(int id, Node node1, Node node2, int value) {
        this.id = id;
        this.node1 = node1;
        this.node2 = node2;
        this.value = value;
    }

    public Edge(boolean selected, boolean solution, Node node1, Node node2) {
        this.selected = selected;
        this.solution = solution;
        this.node1 = node1;
        this.node2 = node2;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSolution() {
        return solution;
    }

    public void setSolution(boolean solution) {
        this.solution = solution;
    }

    public Node getNode1() {
        return node1;
    }

    public void setNode1(Node node1) {
        this.node1 = node1;
    }

    public Node getNode2() {
        return node2;
    }

    public void setNode2(Node node2) {
        this.node2 = node2;
    }

    public ArrayList<Section> getSections() {
        return sections;
    }

    public void addSection(Section section) {
        this.sections.add(section);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "ID: " + id + "\nNode1 : " + node1.toString() + "\nNode2: " + node2.toString() + "\nValue: " + value;
    }

}
