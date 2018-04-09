package birds;

/**
 * This class represents a bird record in the database. Each record consists of two
 * parts: a DataKey and the data associated with the DataKey.
 */
public class BirdRecord {
    
    /**
     * Declaring instance variables
     */
    private DataKey key;
    private String about;
    private String sound;
    private String image;
    

    // default constructor
    public BirdRecord() {
        
        key = null;
        about = "";
        sound = "";
        image = "";
        
    }
    
    // Other constructors
    
    public BirdRecord(DataKey key, String about, String sound, String image){
        this.key = key;
        this.about = about;
        this.sound = sound;
        this.image = image;
    }
 
    /**
    * The following are public methods to access the instance variables
    */
    
    public DataKey getDataKey(){
        return key;
    }
    public String getAbout(){
        return about;
    }
    public String getSound(){
        return sound;
    }
    public String getImage(){
            return image;
    }

}
