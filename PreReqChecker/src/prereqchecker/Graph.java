package prereqchecker;
import java.util.LinkedList;
//import java.io.*;
import java.util.*;
//import java.util.LinkedHashMap;

public class Graph {
 /*   private int vertices;
    private LinkedList <String> adjList[];

    public Graph(int vertices){
        this.vertices = vertices;
        adjList = new LinkedList[vertices];
        for(int i=0; i<vertices; i++){
            adjList[i] = new LinkedList<>();
        }
    }

    public void setValue(int index, String s){
       // adjList[index].add(s);
    }

    public void addEdge(int to, String from){
        adjList[to].add(from);
    }*/

    private Map<String, LinkedList<String>> adjList;
    private ArrayList<String> check;

    public Graph(String[] list){
        adjList = new HashMap<String, LinkedList<String>>();
        for(int i=0; i<list.length; i++){
            adjList.put(list[i], new LinkedList<String>());
        }
        check = new ArrayList<String>();
    }

    public Graph(){

    }

    public void addEdge(String a, String b){
        if(adjList.get(a) == null){
            adjList.put(a, new LinkedList<String>());
        }
        adjList.get(a).add(b);
    }

    public void print(String file){
        StdOut.setFile(file);
        for(String x: adjList.keySet()){
            String key = x.toString();
            LinkedList l = adjList.get(x);
            Object[] a = l.toArray();
            StdOut.print(key + " ");
            for (Object element : a)
                StdOut.print(element + " ");
            StdOut.println();
        }
    }  
    
    public Object[] getList(String key){
        Object[] a = adjList.get(key).toArray();
        return a;
    }

    public boolean hasEdge(String a, String b){
        Object[] arr = adjList.get(a).toArray();
        for(int i=0; i<arr.length; i++){
            if(arr[i].equals(b)) //do i need the toString
                return true;
        }
        return false;
    }

    public void DFS(String startNode) {
        Set<String> visited = new HashSet<>();
        DFSHelper(startNode, visited);
    }

    private void DFSHelper(String currentNode, Set<String> visited) {
        if (!visited.contains(currentNode)) {
            //System.out.print(currentNode + " ");
            if(!check.contains(currentNode))
                check.add(currentNode);
            visited.add(currentNode);

            LinkedList<String> neighbors = adjList.get(currentNode);
            if (neighbors != null) {
                for (String neighbor : neighbors) {
                    DFSHelper(neighbor, visited);
                }
            }
        }
    }

    public ArrayList<String> getCheck(){
        return check;
    }
   
}
