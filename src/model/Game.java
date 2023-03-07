package model;

import java.util.ArrayList;

public class Game {
    private ArrayList<Node> nodes = new ArrayList<>();
    private ArrayList<Edge> edges = new ArrayList<>();

    private ArrayList<Edge> solution = new ArrayList<>();

    public void instanceGame() {
        instanceNodes();
        instanceEdges();
        setSections();
        setSolution();
    }

    private void instanceNodes() {
        int[][] nodesCoordinates = {
                { 100, 150 },
                { 300, 150 },
                { 50, 300 },
                { 100, 450 },
                { 155, 300 },
                { 250, 475 },
                { 350, 250 },
                { 550, 50 },
                { 575, 200 },
                { 500, 500 },
                { 400, 550 },
                { 300, 375 },
                { 500, 300 }
        };
        for (int i = 1; i < 14; i++) {
            nodes.add(new Node(i, nodesCoordinates[i - 1][0], nodesCoordinates[i - 1][1]));
        }
    }

    private void instanceEdges() {
        String[] edgesRead = new TextFileReader().loadTextFile("edges");
        for (int i = 0; i < edgesRead.length; i++) {
            String edge = edgesRead[i];
            int node1 = Integer.parseInt(edge.substring(0, edge.indexOf(",")));
            int node2 = Integer.parseInt(edge.substring(edge.indexOf(",") + 2, edge.indexOf(";")));
            int value = Integer.parseInt(edge.substring(edge.indexOf(";") + 1, edge.length()));
            edges.add(new Edge(i + 1, nodes.get(node1 - 1), nodes.get(node2 - 1), value));
        }

        // for (Edge edge : edges) {
        // System.out.println(edge.toString());
        // }
    }

    private void setSections() {
        String[] sections = new TextFileReader().loadTextFile("sections");
        for (int i = 0; i < sections.length; i++) {
            String section = sections[i];
            int id = Integer.parseInt(section.substring(0, section.indexOf(":"))) - 1;
            String section1 = section.substring(section.indexOf(":") + 1, section.indexOf(","));
            String section2 = section.substring(section.indexOf(",") + 1, section.length());
            edges.get(id).addSection(Section.getSectionByName(section1));
            edges.get(id).addSection(Section.getSectionByName(section2));
        }
        // for (Edge edge : edges) {
        // if (edge.getSections().contains(Section.LINKS)) {
        // System.out.println(edge.toString());
        // }
        // }
    }

    private void setSolution() {
        solution.add(edges.get(1));
        solution.add(edges.get(6));
        solution.add(edges.get(9));
        solution.add(edges.get(14));
        solution.add(edges.get(16));
        solution.add(edges.get(17));
        solution.add(edges.get(20));
        solution.add(edges.get(18));
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    public ArrayList<Edge> getSolution() {
        return solution;
    }

}
