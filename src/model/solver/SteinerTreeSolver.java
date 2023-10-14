package model.solver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SteinerTreeSolver {
    static {
        var path = Paths.get("resources/steiner_tree_jni.dll");
        var pathStr = path.toAbsolutePath().toString();
        System.out.println("trying to load "+pathStr);
        try{
            System.load(pathStr);
            System.out.println(
                "succeded beautifully as far as I can tell.");
        }catch(Exception e){
            System.out.println("failed terribly: "+e.getMessage());
        }
    }

    public native String solveString(String input);

    public String getSolutionToFile(String path){
        try{
            String content = Files.readString(Paths.get(path));
            return solveString(content);
        }catch(IOException e){
            System.out.println("Failed to read '"+path+"':"+e.getMessage());
            return "";
        }
    }
/*
    public static void main(String[] args) {

        try{
        Path dir = Paths.get("../public/problem");
        DirectoryStream<Path> stream = Files.newDirectoryStream(dir);
        SteinerTreeSolver myClass = new SteinerTreeSolver();
        Path destDir = dir.getParent().resolve("solutionJava");

        for (Path file : stream) {
            String content = Files.readString(file);
            String res = myClass.solveString(content);
            Path filename = destDir.resolve(file.getFileName().toString()+".out");
            Files.writeString(filename, res);
            System.out.println("wrote: "+filename.toString());
        }
    }catch(IOException e){
        System.out.println("oofy woofyy: "+e.getMessage());
    }
    
    }*/

}