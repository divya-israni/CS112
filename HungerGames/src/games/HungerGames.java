package games;

import java.util.ArrayList;

/**
 * This class contains methods to represent the Hunger Games using BSTs.
 * Moves people from input files to districts, eliminates people from the game,
 * and determines a possible winner.
 * 
 * @author Pranay Roni
 * @author Maksims Kurjanovics Kravcenko
 * @author Kal Pandit
 */
public class HungerGames {

    private ArrayList<District> districts;  // all districts in Panem.
    private TreeNode            game;       // root of the BST. The BST contains districts that are still in the game.

    /**
     * ***** DO NOT REMOVE OR UPDATE this method *********
     * Default constructor, initializes a list of districts.
     */
    public HungerGames() {
        districts = new ArrayList<>();
        game = null;
        StdRandom.setSeed(2023);
    }

    /**
     * ***** DO NOT REMOVE OR UPDATE this method *********
     * Sets up Panem, the universe in which the Hunger Games takes place.
     * Reads districts and people from the input file.
     * 
     * @param filename will be provided by client to read from using StdIn
     */
    public void setupPanem(String filename) { 
        StdIn.setFile(filename);  // open the file - happens only once here
        setupDistricts(filename); 
        setupPeople(filename);
    }

    /**
     * Reads the following from input file:
     * - Number of districts
     * - District ID's (insert in order of insertion)
     * Insert districts into the districts ArrayList in order of appearance.
     * 
     * @param filename will be provided by client to read from using StdIn
     */
    public void setupDistricts (String filename) {

        //no empty files
        int numDis = StdIn.readInt();
        for(int i=0; i<numDis; i++){
            District addDistrict = new District(StdIn.readInt());
            districts.add(addDistrict);
        }
    }

    /**
     * Reads the following from input file (continues to read from the SAME input file as setupDistricts()):
     * Number of people
     * Space-separated: first name, last name, birth month (1-12), age, district id, effectiveness
     * Districts will be initialized to the instance variable districts
     * 
     * Persons will be added to corresponding district in districts defined by districtID
     * 
     * @param filename will be provided by client to read from using StdIn
     */
    public void setupPeople (String filename) {
        
        //no empty files
        int numPeople = StdIn.readInt();
        for(int i=0; i<numPeople; i++){
            String firstName = StdIn.readString();
            String lastName = StdIn.readString();
            int birthMonth = StdIn.readInt();
            int age = StdIn.readInt();
            boolean tessera = false;
            if(age>=12 && age<18){
                tessera = true;
            }
            int districtID = StdIn.readInt();
            int effectiveness = StdIn.readInt();
            Person addPerson = new Person(birthMonth, firstName, lastName, age, districtID, effectiveness);
            addPerson.setTessera(tessera);
            District addToDis = null;
            for(int j=0; j<districts.size(); j++){
                if(districts.get(j).getDistrictID() == districtID){
                    addToDis = districts.get(j);
                }
            }
            if(birthMonth%2==0){
                addToDis.addEvenPerson(addPerson);
            }
            else{
                addToDis.addOddPerson(addPerson);
            }
        }
    }

    /**
     * Adds a district to the game BST.
     * If the district is already added, do nothing
     * 
     * @param root        the TreeNode root which we access all the added districts
     * @param newDistrict the district we wish to add
     */
    public void addDistrictToGame(TreeNode root, District newDistrict) {

        if(root!=null){

            if(root.getDistrict().getDistrictID() > newDistrict.getDistrictID()){
                if(root.getLeft() == null){
                    TreeNode newNode = new TreeNode(newDistrict, null, null);
                    root.setLeft(newNode);
                    int districtIndex = 0;
                    for(int i=0; i<districts.size(); i++){
                        if(districts.get(i).equals(newDistrict)){
                            districtIndex = i;
                        }
                    }
                    districts.remove(districtIndex);
                }
                else{
                    addDistrictToGame(root.getLeft(), newDistrict); //this might need an else
                }
            }
            if(root.getDistrict().getDistrictID() < newDistrict.getDistrictID()){
                if(root.getRight() == null){
                    TreeNode newNode = new TreeNode(newDistrict, null, null);
                    root.setRight(newNode);
                    int districtIndex = 0;
                    for(int i=0; i<districts.size(); i++){
                        if(districts.get(i).equals(newDistrict)){
                            districtIndex = i;
                        }
                    }
                    districts.remove(districtIndex);
                }
                else{
                    addDistrictToGame(root.getRight(), newDistrict);
                }
            }

        }

        if(root==null){
            TreeNode newNode = new TreeNode(newDistrict, null, null);
            root = newNode;
            game = newNode;
            int districtIndex = 0;
            for(int i=0; i<districts.size(); i++){
                if(districts.get(i).equals(newDistrict)){
                    districtIndex = i;
                }
            }
            districts.remove(districtIndex);
        }
    }

    /**
     * Searches for a district inside of the BST given the district id.
     * 
     * @param id the district to search
     * @return the district if found, null if not found
     */
    public District findDistrict(int id) {
        return findDistrictHelper(id, game);
    }

    private District findDistrictHelper(int id, TreeNode root){
        if(root==null){
            return null;
        }
        if(id == root.getDistrict().getDistrictID()){
            return root.getDistrict();
        }
        if(id < root.getDistrict().getDistrictID()){
            return findDistrictHelper(id, root.getLeft());
        }
        if(id > root.getDistrict().getDistrictID()){
            return findDistrictHelper(id, root.getRight());
        }

        return null;
    }

    /**
     * Selects two duelers from the tree, following these rules:
     * - One odd person and one even person should be in the pair.
     * - Dueler with Tessera (age 12-18, use tessera instance variable) must be
     * retrieved first.
     * - Find the first odd person and even person (separately) with Tessera if they
     * exist.
     * - If you can't find a person, use StdRandom.uniform(x) where x is the respective 
     * population size to obtain a dueler.
     * - Add odd person dueler to person1 of new DuelerPair and even person dueler to
     * person2.
     * - People from the same district cannot fight against each other.
     * 
     * @return the pair of dueler retrieved from this method.
     */
    public DuelPair selectDuelers() {
        Person person1 = null;
        Person person2 = null;
        DuelPair dp = new DuelPair(person1, person2);

        //searching odd population w tesera
        ArrayList<TreeNode> order = new ArrayList<TreeNode>();
        preOrder(game, order);

        int bstSize = order.size();
        int count1 = 0;
        while(person1==null && count1!=bstSize){
            int size = order.get(count1).getDistrict().getOddPopulation().size();
            for(int i=0; i<size; i++){
                if(person1==null){
                    if(order.get(count1).getDistrict().getOddPopulation().get(i).getTessera()){
                        person1 = order.get(count1).getDistrict().getOddPopulation().get(i);
                    }
                }
            }
            count1++;
        }

        //searching even population with tesera 
        int count2 = 0;
        while(person2==null && count2!=bstSize){
            int size = order.get(count2).getDistrict().getEvenPopulation().size();
            for(int i=0; i<size; i++){
                if(person2==null){
                    if(order.get(count2).getDistrict().getEvenPopulation().get(i).getTessera()){
                        if(person1!=null){
                            if(order.get(count2).getDistrict().getDistrictID()!=person1.getDistrictID()){
                                person2 = order.get(count2).getDistrict().getEvenPopulation().get(i);
                            }
                        }
                        else{
                            person2 = order.get(count2).getDistrict().getEvenPopulation().get(i);
                        }
                    }
                }
            }
            count2++;
        }

        //odd pop random
        int count3 = 0 ;
        while(person1==null && count3!=bstSize){
            int size = order.get(count3).getDistrict().getOddPopulation().size();
            int indexPerson = StdRandom.uniform(size);
            if(person2!=null){
                if(order.get(count3).getDistrict().getDistrictID() != person2.getDistrictID()){
                    person1 = order.get(count3).getDistrict().getOddPopulation().get(indexPerson);
                }
            }
            else{
                person1 = order.get(count3).getDistrict().getOddPopulation().get(indexPerson);
            }
            count3++;
        }

        //even pop random
        int count4 = 0;
        while(person2==null && count4!=bstSize){
            int size = order.get(count4).getDistrict().getEvenPopulation().size();
            int indexPerson = StdRandom.uniform(size);
            if(person1!=null){
                if(order.get(count4).getDistrict().getDistrictID() != person1.getDistrictID()){
                    person2 = order.get(count4).getDistrict().getEvenPopulation().get(indexPerson);
                }
            }
            else{
                person2 = order.get(count4).getDistrict().getEvenPopulation().get(indexPerson);
            }
            count4++;
        }
        dp.setPerson1(person1);
        dp.setPerson2(person2);

        //removing people from district
        if(person1!=null){
            for(int i=0; i<order.size(); i++){
                if(person1.getDistrictID() == order.get(i).getDistrict().getDistrictID()){
                    order.get(i).getDistrict().getOddPopulation().remove(person1);
                }
            }
        }
        if(person2!=null){
            for(int i=0; i<order.size(); i++){
                if(person2.getDistrictID() == order.get(i).getDistrict().getDistrictID()){
                    order.get(i).getDistrict().getEvenPopulation().remove(person2);
                }
            }
        }

        return dp;
    }


    private void preOrder(TreeNode root, ArrayList<TreeNode> districtOrder){
        if(root==null){
            return;
        }
        districtOrder.add(root); 
        preOrder(root.getLeft(), districtOrder);
        preOrder(root.getRight(), districtOrder);
    }



    /**
     * Deletes a district from the BST when they are eliminated from the game.
     * Districts are identified by id's.
     * If district does not exist, do nothing.
     * 
     * This is similar to the BST delete we have seen in class.
     * 
     * @param id the ID of the district to eliminate
     */
    public void eliminateDistrict(int id) {
        game = delete(game, id);
    }

    private TreeNode delete(TreeNode node, int id){
        if(node == null){
            return null;
        }
        else if(node.getDistrict().getDistrictID() > id){
            node.setLeft(delete(node.getLeft(), id));
        }
        else if(node.getDistrict().getDistrictID() < id){
            node.setRight(delete(node.getRight(), id));
        }
        else{
            if(node.getLeft()==null){ 
                return node.getRight();
            }
            if(node.getRight()==null){
                return node.getLeft();
            }
           
            TreeNode temp = node;
            node = min(temp.getRight());
            node.setRight(deleteMin(temp.getRight()));
            node.setLeft(temp.getLeft());
        }
        return node;
    }

    private TreeNode min (TreeNode node){
        if(node.getLeft() == null){
            return node;
        }
        else{
            return min(node.getLeft());
        }
    }

    private TreeNode deleteMin(TreeNode node){
        if(node.getLeft()==null){
            return node.getRight();
        }
        node.setLeft(deleteMin(node.getLeft()));
        return node;
    }

    /**
     * Eliminates a dueler from a pair of duelers.
     * - Both duelers in the DuelPair argument given will duel
     * - Winner gets returned to their District
     * - Eliminate a District if it only contains a odd person population or even
     * person population
     * 
     * @param pair of persons to fight each other.
     */
    public void eliminateDueler(DuelPair pair) {
        ArrayList<TreeNode> order = new ArrayList<TreeNode>();
        preOrder(game, order);
        if(pair.getPerson1()==null && pair.getPerson2()!=null){
            for(int i=0; i<order.size(); i++){
                if(order.get(i).getDistrict().getDistrictID() == pair.getPerson2().getDistrictID()){
                    order.get(i).getDistrict().getEvenPopulation().add(pair.getPerson2());
                }
            }
            return;
        }
        if(pair.getPerson1()!=null && pair.getPerson2()==null){
            for(int i=0; i<order.size(); i++){
                if(order.get(i).getDistrict().getDistrictID() == pair.getPerson1().getDistrictID()){
                    order.get(i).getDistrict().getOddPopulation().add(pair.getPerson1());
                }
            }
            return;
        }
        if(pair.getPerson1()==null && pair.getPerson2()==null){
            for(int i=0; i<order.size(); i++){
                if(order.get(i).getDistrict().getDistrictID() == pair.getPerson2().getDistrictID()){
                    order.get(i).getDistrict().getEvenPopulation().add(pair.getPerson2());
                }
            }
            for(int i=0; i<order.size(); i++){
                if(order.get(i).getDistrict().getDistrictID() == pair.getPerson1().getDistrictID()){
                    order.get(i).getDistrict().getOddPopulation().add(pair.getPerson1());
                }
            }
            return;
        }

        //if DP complete
        Person winner = pair.getPerson1().duel(pair.getPerson2());
        District person1Dis = null;
        District person2Dis = null;
        if(pair.getPerson1() == winner){
            for(int i=0; i<order.size(); i++){
                if(order.get(i).getDistrict().getDistrictID() == pair.getPerson1().getDistrictID()){
                    order.get(i).getDistrict().getOddPopulation().add(pair.getPerson1());
                }
            }
        }
        else{
            for(int i=0; i<order.size(); i++){
                if(order.get(i).getDistrict().getDistrictID() == pair.getPerson2().getDistrictID()){
                    order.get(i).getDistrict().getEvenPopulation().add(pair.getPerson2());
                }
            }
        }
        for(int i=0; i<order.size(); i++){
            if(order.get(i).getDistrict().getDistrictID() == pair.getPerson1().getDistrictID()){
                person1Dis = order.get(i).getDistrict();
            }
        }
        for(int i=0; i<order.size(); i++){
            if(order.get(i).getDistrict().getDistrictID() == pair.getPerson2().getDistrictID()){
                person2Dis = order.get(i).getDistrict();
            }
        }

        if(person1Dis.getOddPopulation().size() == 0 || person1Dis.getEvenPopulation().size() == 0){
            eliminateDistrict(person1Dis.getDistrictID());
        }
        if(person2Dis.getOddPopulation().size() == 0 || person2Dis.getEvenPopulation().size()==0){
            eliminateDistrict(person2Dis.getDistrictID());
        }
        
    }

    /**
     * ***** DO NOT REMOVE OR UPDATE this method *********
     * 
     * Obtains the list of districts for the Driver.
     * 
     * @return the ArrayList of districts for selection
     */
    public ArrayList<District> getDistricts() {
        return this.districts;
    }

    /**
     * ***** DO NOT REMOVE OR UPDATE this method *********
     * 
     * Returns the root of the BST
     */
    public TreeNode getRoot() {
        return game;
    }
}
