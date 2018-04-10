/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package birds;

/**
 *
 * @author MHK
 */
public class OrderedDictionary implements OrderedDictionaryADT{
    
    /**
     * Instance variables declared
     */
    BinaryTreeNode root;
    int size;
       
    /**
    * Default constructor
    */
    public OrderedDictionary(){
        root = null;
        size = 0;
    }
    
    
    public OrderedDictionary(BirdRecord birdData){
        
        root = new BinaryTreeNode(birdData);
        size = 1;
    }
    
     /* Returns the Record object with key k, or It throws a DictionaryException
       says: "There is no record matches the given key", if such a record
       is not in the dictionary. 

       @param k
       @return BirdRecord
       @throws DictionaryException
     */
    public BirdRecord find(DataKey k) throws DictionaryException{
        BinaryTreeNode node = root;
        boolean found = false;
        while (node!=null & !found){
            int compResult = node.getKey().compareTo(k);
            if (compResult==0){// node found
                return node.record;
            }
            else if (compResult<0){ //implies that current node's key is smaller than k, therefore go to right
                node = node.rightChildNode;
            }
            else{// i.e. compResult>0
                node = node.leftChildNode;
            }
        }
        if (!found){
            throw new DictionaryException("There is no record matches the given key");
        }
        return null;
    }

    /* Inserts r into the ordered dictionary. It throws a DictionaryException 
       if a record with the same key as r is already in the dictionary.  

       @param r
       @throws DictionaryException
     */
    public void insert(BirdRecord r) throws DictionaryException{
        //BinaryTreeNode node = root;
        BinaryTreeNode newNode = new BinaryTreeNode(r); //will make new leafnodes null therefore tree requirement fulfilled 
        if (root == null){ // case 1: tree is empty
            root = newNode;
        }
        else{ // case 2: tree is not empty
        BinaryTreeNode trackNode = root;
        while(trackNode!=null){
           
            int compResult = trackNode.getRecord().getDataKey().compareTo(r.getDataKey());
            if (compResult==0){
                throw new DictionaryException("A record with the same key as the one provided is already in the dictionary");
            }
            else if (compResult<0){
                if(trackNode.rightChildNode==null){
                    trackNode.rightChildNode=newNode;
                    newNode.parentNode = trackNode;
                    size++;
                    break;
                }
                else{
                    trackNode = trackNode.rightChildNode;
                }
            }
            else{
                if(trackNode.leftChildNode==null){
                    trackNode.leftChildNode=newNode;
                    newNode.parentNode = trackNode;
                    size++;
                    break;
                }
                else{
                    trackNode = trackNode.leftChildNode;
                }
            }
            
        }
        }
    }

    /*  Removes the record with Key k from the dictionary. It throws a 
        DictionaryException says: "No such record key exists", if the record
        is not in the dictionary. 
             
       @param k
       @throws DictionaryException
     */
    public void remove(DataKey key) throws DictionaryException{
        BinaryTreeNode nodeToBeRemoved = findNode(key); //source of exception if not found

        if (nodeToBeRemoved.leftChildNode == null && nodeToBeRemoved.rightChildNode == null) {
            //No children
            RemoveChildlessNode(nodeToBeRemoved);

        } else if (nodeToBeRemoved.leftChildNode == null) {
            //The node to be removed has only a right child
            BinaryTreeNode replacementNode = nodeToBeRemoved.rightChildNode;
            RemoveOneChildNode(nodeToBeRemoved, replacementNode);


        } else if (nodeToBeRemoved.rightChildNode == null) {
            //The node to be removed has only a left child
            BinaryTreeNode replacementNode = nodeToBeRemoved.leftChildNode;
            RemoveOneChildNode(nodeToBeRemoved, replacementNode);
        } else { //replace nodeToBeRemoved with a node that has both left and right children
            //The node has both left and right child
            //Go to the right child then keep going left
            //Take its entry and move it to the node to be deleted
            BinaryTreeNode node = nodeToBeRemoved.rightChildNode;
            while (node.leftChildNode != null) {
                node = node.leftChildNode;
            }
            nodeToBeRemoved.record = node.getRecord();

            //smallest node acessed therefore, check if that node has right child to place as 
            if (node.rightChildNode == null) {
                RemoveChildlessNode(node);
            } else {
                BinaryTreeNode replacementNode = node.rightChildNode;
                RemoveOneChildNode(node, replacementNode);
            }

        }

    }
    
    private BinaryTreeNode findNode(DataKey k) throws DictionaryException{
        BinaryTreeNode node = root;
        boolean found = false;
        while (node!=null & !found){
            int compResult = node.getKey().compareTo(k);
            if (compResult==0){// node found
                return node;
            }
            else if (compResult<0){ //implies that current node's key is smaller than k, therefore go to right
                node = node.rightChildNode;
            }
            else{// i.e. compResult>0
                node = node.leftChildNode;
            }
        }
        if (!found){
            throw new DictionaryException("No such record key exists");
        }
        return null;
    }

    private void RemoveChildlessNode(BinaryTreeNode nodeToBeRemoved) {
        if (nodeToBeRemoved.parentNode.leftChildNode == nodeToBeRemoved) {
            nodeToBeRemoved.parentNode.leftChildNode = null;
        } else if (nodeToBeRemoved.parentNode.rightChildNode == nodeToBeRemoved) {
            nodeToBeRemoved.parentNode.rightChildNode = null;
        }
        nodeToBeRemoved.parentNode = null;
        size--;
    }

    private void RemoveOneChildNode(BinaryTreeNode nodeToBeRemoved, BinaryTreeNode replacementNode) {
        replacementNode.parentNode = nodeToBeRemoved.parentNode;

        //Fix the child reference of the parent
        if (nodeToBeRemoved.parentNode.leftChildNode == nodeToBeRemoved) {
            //node to be removed is a leftchild
            nodeToBeRemoved.parentNode.leftChildNode = replacementNode;
        } else if (nodeToBeRemoved.parentNode.rightChildNode == nodeToBeRemoved) {
            //node to be removed is a right child
            nodeToBeRemoved.parentNode.rightChildNode = replacementNode;
        }
        nodeToBeRemoved.parentNode = null;
        size--;

    }

    /* Returns the successor of k (the record from the ordered dictionary 
       with smallest key larger than k); It throws a DictionaryException says:
       "There is no successor for the given record key", if the given key has 
       no successor. The given key DOES NOT need to be in the dictionary. 
         
       @param k
       @return BirdRecord
       @throws DictionaryException
     */
    public BirdRecord successor(DataKey k) throws DictionaryException{
        BinaryTreeNode successor;
        BinaryTreeNode nodeFindSuccFor = null;
        boolean notFound = false;
        try{
            nodeFindSuccFor = findNode(k);
        }
        catch(DictionaryException e){
            notFound = true;
        }
        if(notFound){
            return findClosestSuccIter(k,"successor").record; //throws exception if no node is larger than k.
        }
        else{
            BinaryTreeNode firstRight;
            firstRight = nodeFindSuccFor.rightChildNode;

            if(firstRight==null){

                successor = nodeFindSuccFor; 

                while(successor.parentNode!=null){ //to test each parent node, since no node child will be larger than nodeFindSuccFor
                    successor = successor.parentNode;
                    if (nodeFindSuccFor.record.getDataKey().compareTo(successor.record.getDataKey())<0){ //if parent is larger
                       return successor.record;
                    }

                }

                throw new DictionaryException("There is no successor for the given record key"); //Will occur only if no successor found. 

            }
            else if (firstRight.leftChildNode == null){
                successor = firstRight;
                return successor.record;
            }
            else if (firstRight.leftChildNode!=null){
                successor = firstRight.leftChildNode;
                while(successor.leftChildNode!=null){
                    successor = successor.leftChildNode;
                }  
                return successor.record;
            }
        }
        return null;
        
    }
    
    
    private BinaryTreeNode findClosestSuccIter(DataKey k, String closestType) throws DictionaryException{
    
        BinaryTreeNode closestNode = root;
        BinaryTreeNode trackNode = root;
        boolean found = false;
        
        while(!found){
            int comparison1 = trackNode.getKey().compareTo(k);
            if(comparison1<0){
                trackNode = trackNode.rightChildNode;
                if(trackNode.getKey().compareTo(k)>0){
                    return trackNode;
                }
            }
            else if(comparison1>0){
                trackNode = trackNode.leftChildNode;
                if(trackNode.getKey().compareTo(k)<0){
                    return trackNode.parentNode;
                }
                
            }
        }
        return trackNode;
        
        
    }
    
    
    private BinaryTreeNode findClosestPredIter(DataKey k, String closestType) throws DictionaryException{
    
        BinaryTreeNode closestNode = root;
        BinaryTreeNode trackNode = root;
        boolean found = false;
        
        while(!found){
            int comparison1 = trackNode.getKey().compareTo(k);
            if(comparison1<0){
                trackNode = trackNode.rightChildNode;
                if(trackNode.getKey().compareTo(k)>0){
                    return trackNode.parentNode;
                }
            }
            else if(comparison1>0){
                trackNode = trackNode.leftChildNode;
                if(trackNode.getKey().compareTo(k)<0){
                    return trackNode;
                }
                
            }
        }
        return trackNode;
        
        
    }
    
    /**
     * 
     * @param k is the DataKey whose successor is to be found
     * @param closestType is String value denoting whether a successor or predecessor is required
     * @return a BinaryTreeNode that is closest successor or predecessor to k. If none found, will return null
     * @throws DictionaryException if closestType is not valid. 
     */
    
    private BinaryTreeNode findClosestNode(DataKey k,String closestType) throws DictionaryException{
        
        BinaryTreeNode node = root;
        BinaryTreeNode closestNode = root;
        
        findClosestNodeAux(node, closestNode, k, closestType);
            
        return closestNode;
    }
    /**
     * Auxiliary method for tail recursive calls - comparisons made on the way down the recursive chain - to find closest node to DataKey k.  
     * 
     * @param node is the BinaryTreeNode being visited in that call of this method
     * @param closestNode is the BinaryTreeNode that is updated with the closest node to the node with DataKey k
     * @param k the DataKey in the node for which the closest node is being searched
     * @param closestType is a String value denoting whether successor or predecessor is being searched
     */
    private void findClosestNodeAux(BinaryTreeNode node, BinaryTreeNode closestNode, DataKey k, String closestType) throws DictionaryException{
        DataKey closestData = closestNode.getKey();
        DataKey nodeData = node.getKey();
        int comparison1 = nodeData.compareTo(k);
        int comparison2 = nodeData.compareTo(closestData);
        
        if(closestType.equals("successor")){
            if(comparison1>0 & comparison2<0){
                closestNode = node;
            }
            if(node.leftChildNode!=null){
                findClosestNodeAux(node.leftChildNode, closestNode, k, closestType);
            }
            if(node.rightChildNode!=null){
                findClosestNodeAux(node.rightChildNode, closestNode, k, closestType);
            }
        }
        else if(closestType.equals("predecessor")){
            if(comparison1<0 & comparison2>0){
                closestNode = node;
            }
            if(node.leftChildNode!=null){
                findClosestNodeAux(node.leftChildNode, closestNode, k, closestType);
            }
            if(node.rightChildNode!=null){
                findClosestNodeAux(node.rightChildNode, closestNode, k, closestType);
            }
        }
        else {throw new DictionaryException("wrong type of closest node specified - can only be 'successor' or 'predecessor'.");}
        
        // account for when closestNode is the root but there exists no successor - ie closest node does not change
        if(closestType.equals("successor") & closestNode.getKey().compareTo(k)<0){
            closestNode = null;
        }
        else if(closestType.equals("predecessor") & closestNode.getKey().compareTo(k)>0){
            closestNode = null;
        }

    }
    
    /* Returns the predecessor of k (the record from the ordered dictionary 
       with largest key smaller than k; It throws a DictionaryException says:
       "There is no predecessor for the given record key", if the given key has 
       no predecessor. The given key DOES NOT need to be in the dictionary.  
     
       @param k
       @return BirdRecord
       @throws DictionaryException
     */
    public BirdRecord predecessor(DataKey k) throws DictionaryException{
        
        BinaryTreeNode predecessor;
        BinaryTreeNode nodeFindPredFor = null;
        boolean notFound = false;
        
        try{
            nodeFindPredFor = findNode(k);
        }
        catch(DictionaryException e){
            notFound = true;
        }
        if(notFound){
            return findClosestPredIter(k,"predecessor").record; //throws exception if no node is larger than k.
        }
        else{
        
            BinaryTreeNode firstLeft = nodeFindPredFor.leftChildNode;

            if(firstLeft==null){

                predecessor = nodeFindPredFor; 

                while(predecessor.parentNode!=null){ //to test each parent node, since no node child will be larger than nodeFindSuccFor
                    predecessor = predecessor.parentNode;
                    if (nodeFindPredFor.record.getDataKey().compareTo(predecessor.record.getDataKey())>0){ //if parent is larger
                       return predecessor.record;
                    }

                }

                throw new DictionaryException("There is no successor for the given record key"); //Will occur only if no successor found. 

            }
            else if (firstLeft.rightChildNode == null){
                predecessor = firstLeft;
                return predecessor.record;
            }
            else if (firstLeft.rightChildNode!=null){
                predecessor = firstLeft.rightChildNode;
                while(predecessor.rightChildNode!=null){
                    predecessor = predecessor.rightChildNode;
                }  
                return predecessor.record;
            }
        }
        return null;
    }

    /* Returns the record with smallest key in the ordered dictionary. 
       It throws a DictionaryException says:"Dictionary is empty", if the 
       dictionary is empty.   

       @return BirdRecord
       @throws DictionaryException
     */
    public BirdRecord smallest() throws DictionaryException{
        BinaryTreeNode smallest = root;
        if(smallest==null){
            throw new DictionaryException("Dictionary is empty");
        }
        //if root is not null, following code portion will be proceeded to. 
        while(smallest.leftChildNode!=null){ 
            smallest = smallest.leftChildNode;
        }
        //following code will be executed regardless of if root is only node or not. 
        return smallest.record;
        
    }

    /* Returns the record with largest key in the ordered dictionary. 
       It throws a DictionaryException says:"Dictionary is empty", if the 
       dictionary is empty.  
       @return BirdRecord
       @throws DictionaryException
     */
    public BirdRecord largest() throws DictionaryException{
        
        BinaryTreeNode largest = root;
        if(largest==null){
            throw new DictionaryException("Dictionary is empty");
        }
        //if root is not null, following code portion will be proceeded to. 
        while(largest.rightChildNode!=null){ 
            largest = largest.rightChildNode;
        }
        //following code will be executed regardless of if root is only node or not. 
        return largest.record;
    }

    /* Returns true if the dictionary is empty, and true otherwise. 

       @return boolean
     */
    public boolean isEmpty(){
        return root==null;
    }
    
    private class BinaryTreeNode{
        
        /**
         * Declaring instance variables
         */
        BirdRecord record; 
        BinaryTreeNode leftChildNode;
        BinaryTreeNode rightChildNode;
        BinaryTreeNode parentNode;
        
        /**
         * Default constructor
         */
        public BinaryTreeNode(){
            record = null;
            leftChildNode = null; 
            rightChildNode = null;
            parentNode = null;
        }
        
        /**
         * Overloaded constructor to be used for dictionary instantiation
         * @param record representing record that would be passed to dictionary constructor
         */
        public BinaryTreeNode(BirdRecord record){
            this.record = record;
            leftChildNode = null; 
            rightChildNode = null;
            parentNode = null;
        }
        
        /**
         * Overloaded constructor with proper instantiation
         */
        
        public BinaryTreeNode(BirdRecord record, BinaryTreeNode leftChildNode, BinaryTreeNode rightChildNode, BinaryTreeNode parentNode){
            this.record = record;
            this.leftChildNode = leftChildNode;
            this.rightChildNode = rightChildNode;
            this.parentNode = parentNode;
        }
        
        /**
         * Method for accessing BirdRecord
        */
        public BirdRecord getRecord(){
            return this.record;
        }
        /**
         * Method for retrieving data key directly 
         */
        public DataKey getKey(){
            return this.record.getDataKey();
        }
        
    }
}
