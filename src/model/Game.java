package model;

import java.util.ArrayList;

public class Game {
    private ArrayList<Node> nodes = new ArrayList<>();
    private ArrayList<Edge> edges = new ArrayList<>();

    private ArrayList<Integer> nodesToConnect = new ArrayList<>();
    private ArrayList<Edge> solution = new ArrayList<>();

    private ArrayList<String[]> levels = new ArrayList<>();

    public void instanceGame() {
        instanceNodes();
        instanceEdges();
        instanceLevels();
    }

    private void instanceNodes() {
        int[][] nodesCoordinates = {
                { 190, 160 },
                { 70, 240 },
                { 140, 320 },
                { 70, 470 },
                { 70, 750 },
                { 140, 580 },
                { 330, 320 },
                { 280, 450 },
                { 260, 560 },
                { 430, 520 },
                { 290, 700 },
                { 490, 190 },
                { 520, 280 },
                { 430, 370 },
                { 410, 665 },
                { 500, 580 },
                { 620, 345 },
                { 690, 450 },
                { 670, 600 },
                { 600, 750 },
                { 980, 160 },
                { 860, 220 },
                { 760, 275 },
                { 1125, 390 },
                { 980, 365 },
                { 1030, 535 },
                { 900, 585 },
                { 835, 645 },
                { 1120, 730 },
        };
        for (int i = 1; i < nodesCoordinates.length + 1; i++) {
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

        // print all edges
        // for (Edge edge : edges) {
        // System.out.println(edge.toString());
        // }
    }

    private void instanceLevels() {
        final int NUMBEROFLEVELS = 1;
        for (int i = 0; i < NUMBEROFLEVELS; i++) {
            levels.add(new TextFileReader().loadTextFile(String.format("level%d", i + 1)));
        }

        for (String[] level : levels) {
            boolean nodesIndezes = true;
            for (int i = 0; i < level.length; i++) {
                String line = level[i];

                if (!line.equals("-")) {
                    if (nodesIndezes) {
                        nodesToConnect.add(Integer.parseInt(line));
                    } else {
                        solution.add(edges.get(Integer.parseInt(line)));
                    }
                } else {
                    nodesIndezes = false;
                }
            }
        }
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

    public ArrayList<Integer> getNodesToConnect() {
        return nodesToConnect;
    }

    public ArrayList<String[]> getLevels() {
        return levels;
    }

}
