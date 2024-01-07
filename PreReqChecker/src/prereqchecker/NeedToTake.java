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
 * NeedToTakeInputFile name is passed through the command line as args[1]
 * Read from NeedToTakeInputFile with the format:
 * 1. One line, containing a course ID
 * 2. c (int): Number of courses
 * 3. c lines, each with one course ID
 * 
 * Step 3:
 * NeedToTakeOutputFile name is passed through the command line as args[2]
 * Output to NeedToTakeOutputFile with the format:
 * 1. Some number of lines, each with one course ID
 */
public class NeedToTake {
    public static void main(String[] args) {

        if ( args.length < 3 ) {
            StdOut.println("Execute: java NeedToTake <adjacency list INput file> <need to take INput file> <need to take OUTput file>");
            return;
        }

	// WRITE YOUR CODE HERE
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

        String target = StdIn.readString();
        int taken = StdIn.readInt();
        ArrayList<String> del = null;
        for(int i=0; i<taken; i++){
            String course = StdIn.readString();
            g.DFS(course);
            del = g.getCheck();
            for(int j=0; j<del.size(); j++){
                if(copy.contains(del.get(j)))
                    copy.remove(del.get(j));
            }
        }

        Object[] prereqs = g.getList(target);
        ArrayList<String> del2 = new ArrayList<String>();
        for(int i=0; i<prereqs.length; i++){
            g.DFS(prereqs[i].toString());
            del2 = g.getCheck();
            for(int j=0; j<del2.size(); j++){
                if(copy.contains(del2.get(j))){
                    copy.remove(del2.get(j));
                    StdOut.println(del2.get(j));
                }
            }
        }

    }
}
