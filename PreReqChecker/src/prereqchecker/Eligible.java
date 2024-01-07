package prereqchecker;

import java.util.*;

/**
 * 
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
 * EligibleInputFile name is passed through the command line as args[1]
 * Read from EligibleInputFile with the format:
 * 1. c (int): Number of courses
 * 2. c lines, each with 1 course ID
 * 
 * Step 3:
 * EligibleOutputFile name is passed through the command line as args[2]
 * Output to EligibleOutputFile with the format:
 * 1. Some number of lines, each with one course ID
 */
public class Eligible {
    public static void main(String[] args) {

        if ( args.length < 3 ) {
            StdOut.println("Execute: java -cp bin prereqchecker.Eligible <adjacency list INput file> <eligible INput file> <eligible OUTput file>");
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

       // ArrayList<String> end = new ArrayList<String>();
        for(int i=0; i<copy.size(); i++){
            Object[] prereqs = g.getList(copy.get(i));
            int count = 0;
            for(int j=0; j<del.size(); j++){
                if(g.hasEdge(copy.get(i), del.get(j))){
                    count++;
                }
            }
            if(count==prereqs.length){
                //end.add(copy.get(i));
                StdOut.println(copy.get(i));
            }
        }


       /* for(int i=0; i<copy.size(); i++){
            Object[] prereqs = g.getList(copy.get(i));
            int count = 0;
            for(int j=0; j<prereqs.length; j++){
                //count = 0;
                for(int k=0; k<del.size(); k++){
                    boolean check = false;
                    if(prereqs[j].toString().equals(del.get(k))){ //fix while thing only can count ocne
                        count++;
                        check = true;
                    }
                    if(check)
                        break;
                }
            }
            if(count==prereqs.length){
                StdOut.println(copy.get(i));
            }
        }

        for(int i=0; i<copy.size(); i++){
            StdOut.println(copy.get(i));
        }*/


        
        /*int numCourses = StdIn.readInt();
        Node[] list = new Node[numCourses];
        ArrayList<String> courseList = new ArrayList<String>(numCourses);
        for(int i=0; i<numCourses; i++){
            String course = StdIn.readString();
            Node add = new Node(course, null);
            list[i] = add;
            courseList.add(course);
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
        StdOut.setFile(args[2]);
        int taken = StdIn.readInt();
        for(int i=0; i<taken; i++){
            String course = StdIn.readString();
            finalList(courseList, course, list);
            courseList = getFinalList(courseList);

        }
        for(int i=0; i<courseList.size(); i++){
            StdOut.println(courseList.get(i));
        }*/
    }

    public static void finalList(ArrayList<String> courseList, String course, Node[] list){
        Node ptr = null;
        for(int i=0; i<list.length; i++){
            if(list[i].getData().equals(course)){
                ptr = list[i];
            }
        }
        if(ptr==null){
            return;
        }
        /*while(ptr!=null){
            String copy = ptr.getData();
            if(courseList.contains(copy))
                courseList.remove(copy);
            ptr = ptr.getNext();
            if(ptr==null)
                return;
            course = ptr.getData();
            finalList(courseList, course, list);
        }*/
        while(ptr!=null){
            if(courseList.contains(ptr.getData()))
                courseList.remove(ptr.getData());
            ptr = ptr.getNext();
            if(ptr!=null)
                finalList(courseList, ptr.getData(), list);
        }

        
    }
    public static ArrayList<String> getFinalList(ArrayList<String> courseList){
        return courseList;
    }
}
