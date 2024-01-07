package prereqchecker;
//import java.util.*;
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
 * AdjListOutputFile name is passed through the command line as args[1]
 * Output to AdjListOutputFile with the format:
 * 1. c lines, each starting with a different course ID, then 
 *    listing all of that course's prerequisites (space separated)
 */
public class AdjList {
    public static void main(String[] args) {

        if ( args.length < 2 ) {
            StdOut.println("Execute: java -cp bin prereqchecker.AdjList <adjacency list INput file> <adjacency list OUTput file>");
            return;
        }

	// WRITE YOUR CODE HERE

        StdIn.setFile(args[0]);
        StdOut.setFile(args[1]);
        int numCourses = StdIn.readInt();
        String[] list = new String[numCourses];
        for(int i=0; i<numCourses; i++){
            String course = StdIn.readString();
            list[i] = course;
        }
        Graph g = new Graph(list);
        int edges = StdIn.readInt();
        for(int i=0; i<edges; i++){
            g.addEdge(StdIn.readString(), StdIn.readString());
        }

        g.print(args[1]);

        

       /* StdIn.setFile(args[0]);
        StdOut.setFile(args[1]);
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
        for(int i=0; i<numCourses; i++){
            Node ptr = list[i];
            while(ptr!=null){
                StdOut.print(ptr.getData() + " ");
                ptr = ptr.getNext();
            }
            StdOut.println();
        }*/
        //Node [] list = createAdjList()

    }

   
}
