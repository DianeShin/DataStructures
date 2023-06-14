import java.util.*;

public class Station implements Comparable<Station>{
    public String id;
    public String name;
    public String line;
    public List<Edge> adjacentEdges = new ArrayList<>();
    public long time;
    public Station prev;
    public long transferTime = 5; // default transfer time

    public Station(String id, String name, String line){
        this.id = id;
        this.name = name;
        this.line = line;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Station other = (Station) obj;

        if (this.id.equals(other.id)) {
            return true;
        }
        else return false;
    }
    
    @Override
    public int compareTo(Station other) {
        // Compare the distance of this station with the distance of the other station
        return Long.compare(this.time, other.time);
    }
}