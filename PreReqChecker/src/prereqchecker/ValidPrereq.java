package prereqchecker;
import java.util.*;

/**
 * Steps to implement this class main method:
 * 
 * Step 1:
 * AdjListInputFile name is passed through the command line as args[0]
 * Read from AdjListInputFile with the format:
 * 1. a (int): number of courses in the graph
 * 2. a lines, each with 1 course ID
 * 3. b (int): number of edges in the graph
 * 4. b lines, each with a source ID
 * 
 * Step 2:
 * ValidPreReqInputFile name is passed through the command line as args[1]
 * Read from ValidPreReqInputFile with the format:
 * 1. 1 line containing the proposed advanced course
 * 2. 1 line containing the proposed prereq to the advanced course
 * 
 * Step 3:
 * ValidPreReqOutputFile name is passed through the command line as args[2]
 * Output to ValidPreReqOutputFile with the format:
 * 1. 1 line, containing either the word "YES" or "NO"
 */
public class ValidPrereq {
    public static void main(String[] args) {

        if ( args.length < 3 ) {
            StdOut.println("Execute: java -cp bin prereqchecker.ValidPrereq <adjacency list INput file> <valid prereq INput file> <valid prereq OUTput file>");
            return;
        }
	// WRITE YOUR CODE HERE
        /*StdIn.setFile(args[0]);
        StdOut.setFile(args[2]);
      
        int numCourses = StdIn.readInt();
        Node[] list = new Node[numCourses];
        for(int i=0; i<numCourses; i++){
            String course = StdIn.readString();
            Node add = new Node(course, null);
            list[i] = add;
        }
        int edges = StdIn.readInt();
        int index = -1;
        for(int i=0; i<edges; i++){
            String course = StdIn.readString();
            for(int j=0; j<list.length; j++){
                if(list[j].getData().equals(course)){
                    index = j;
                }
            }
            Node front = list[index].getNext();
            Node add = new Node(StdIn.readString(), front);
            list[index].setNext(add);
        }
       

        StdIn.setFile(args[1]);
        String course1 = StdIn.readString();
        String course2 = StdIn.readString();
        for(int i=0; i<list.length; i++){
            if(course2.equals(list[i].getData())){
                Node ptr = list[i];
                while(ptr!=null){
                    if(ptr.getData().equals(course1)){
                        StdOut.print("NO");
                        return;
                    }
                    ptr = ptr.getNext();
                }
            }
        }
        
        StdOut.print("YES");*/

        StdIn.setFile(args[0]);
        int numCourses = StdIn.readInt();
        String[] list = new String[numCourses];
        ArrayList<String> copy = new ArrayList<String>();
        for(int i=0; i<numCourses; i++){
            String course = StdIn.readString();
            list[i] = course;
            copy.add(i,course);
        }
        Graph g = new Graph(list);
        int edges = StdIn.readInt();
        for(int i=0; i<edges; i++){
            g.addEdge(StdIn.readString(), StdIn.readString());
        }
        StdIn.setFile(args[1]);
        StdOut.setFile(args[2]);

        String course1 = StdIn.readString();
        String course2 = StdIn.readString();

        g.DFS(course2);
        ArrayList<String> allPre = g.getCheck();
        for(int i=0; i<allPre.size(); i++){
            if(allPre.get(i).equals(course1)){
                StdOut.print("NO");
                return;
            }
        }

        StdOut.print("YES");
    }
}
