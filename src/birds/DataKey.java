package birds;

public class DataKey {
        
    private String birdName;
    private int birdSize;
    
    // default constructor
    public DataKey() {
        birdName = "";
        birdSize = 1; //assume small size
    }
    
    // other constructors
    
    //no possibility of any size other than 1, 2, 3 from drop down menu - therefore no exception can be raised. 
    public DataKey(String name, int size){
        birdName = name;
        birdSize = size;
    }
    
    public String getBirdName(){
        return birdName;
    }
    
    public int getBirdSize(){
        return birdSize;
    }
    
    /**
     * Returns 0 if this DataKey is equal to k, returns -1 if this DataKey is smaller
     * than k, and it returns 1 otherwise. 
     */
    public int compareTo(DataKey k) {
        
        int comparisonResult = this.birdName.compareTo(k.getBirdName());
        
        if(k.getBirdSize()==this.birdSize){
            if(comparisonResult==0){
                return 0;
            }
            if(comparisonResult<0){
                return -1;
            }
        }
        else if(this.birdSize<k.getBirdSize()){
            return -1;
        }
        else if(this.birdSize>k.getBirdSize()){
            return 1;
        }
        
        return 0;
    
    }
}
