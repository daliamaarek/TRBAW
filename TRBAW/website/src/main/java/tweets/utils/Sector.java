package tweets.utils;

/**
 * Created by daliamaarek on 29/05/16.
 */
public class Sector {

    private String sector; //name of sector (used in the js chart)
    private int size; //no of tweets in this sector

    public Sector(String sector, int size) {
        this.sector = sector;
        this.size = size;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
