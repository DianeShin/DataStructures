import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class SubwayMap{
    // save all stations in terms of id
    private List<Station> stationList = new ArrayList<>();
    // station id to index
    private HashMap<String, Integer> stationIDHashMap = new HashMap<>();
    // station name to index
    private HashMap<String, List<Integer>> stationNameHashMap = new HashMap<>();
    // total station id cnt
    private int stationCnt = 0;

    // construct map from input
    public void constructMap(String fileName){
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;

            // mode 0, 1, 2 for 3 types of input
            int mode = 0;

            while ((line = br.readLine()) != null) {
                // change mode if we encounter an empty line
                if (line.isEmpty()){
                    mode++; continue; 
                } 

                // parse input
                String[] words = line.split(" ");

                // save station info
                if (mode == 0){
                    // save in arraylist
                    Station newStation = new Station(words[0], words[1], words[2]);
                    stationList.add(newStation);    

                    // save in id hashmap
                    stationIDHashMap.put(words[0], stationCnt);
                    
                    // resolve index list
                    List<Integer> indexList;
                    if (stationNameHashMap.get(words[1]) == null){ // station never introduced
                        indexList = new ArrayList<>();
                    }
                    
                    else{ // same name station already in hash map
                        indexList = stationNameHashMap.get(words[1]);
                    }
                        
                    // add new station into indexList
                    indexList.add(stationCnt);

                    // insert indexList
                    stationNameHashMap.put(words[1], indexList);     

                    // increment station count
                    stationCnt++;
                }
                // save edge info
                else if (mode == 1){
                    // get station
                    int srcIndex = stationIDHashMap.get(words[0]);
                    Station srcStation = stationList.get(srcIndex);
                    int destIndex = stationIDHashMap.get(words[1]);
                    Station destStation = stationList.get(destIndex);

                    // add edge to src station
                    long time = Long.parseLong(words[2]);
                    srcStation.adjacentEdges.add(new Edge(destStation, time));
                }
                // save transfer time
                else {
                    List<Integer> srcIndexList = stationNameHashMap.get(words[0]);
                    Long time = Long.parseLong(words[1]);
                    for (int srcIndex : srcIndexList){
                        Station srcStation = stationList.get(srcIndex);
                        srcStation.transferTime = time;                        
                    }

                }
            }
        } catch (IOException e) {
            System.out.println("ERR: " + e.toString());
        }

        // create edge among transfer stations
        for (String srcName : stationNameHashMap.keySet()){
            // get src alias
            List<Integer> srcIndexList = stationNameHashMap.get(srcName);

            // if list length is 1, this station is not a transfer station.
            if (srcIndexList.size() == 1) continue;
            // if not, create edges for each combination.
            else{
                for (int item1 : srcIndexList){
                    for (int item2 : srcIndexList){
                        Station item1Station = stationList.get(item1);
                        Station item2Station = stationList.get(item2);
                        if (item1 == item2) continue; // no edge if same station id
                        else{ // yes edge if diff station id(transfer)
                            Edge newEdge = new Edge(item2Station, item2Station.transferTime);
                            item1Station.adjacentEdges.add(newEdge);
                        }
                    }
                }
            }
        }
    }

    // reset map for new calculation
    public void resetMap(){
        for (Station station : stationList){
            station.prev = null;
        }
    }

    // perform dijkstra's algorithm
    public void calculate(String src){
        // get all srcStations(considering we might start at a transfer station)
        List<Integer> srcStationIndexList = stationNameHashMap.get(src);

        // initiate values, 0 for srcStation, 100M for anywhere else
        PriorityQueue<Station> priorityQueue = new PriorityQueue<>();
        for (int index = 0; index < stationList.size(); index++){
            if (srcStationIndexList.contains(index)){
                stationList.get(index).time = 0;
            }
            else{
                stationList.get(index).time = 1000000000L;              
            }

            priorityQueue.add(stationList.get(index));  
        }

        // choose closest station, update distance if possible
        while (!priorityQueue.isEmpty()){
            Station closest = priorityQueue.poll();

            for (Edge edge : closest.adjacentEdges){
                long newDistance = closest.time + edge.weight;
                if (newDistance < edge.dest.time) {
                    priorityQueue.remove(edge.dest);
                    edge.dest.time = newDistance;
                    edge.dest.prev = closest;
                    priorityQueue.add(edge.dest);
                }
            }
            
        }
    }      
    
    // print result on cmd
    public void printResult(String src, String dest){
        // get Station for src/dest
        Station srcStation = stationList.get(stationNameHashMap.get(src).get(0));
        Station destStation = stationList.get(stationNameHashMap.get(dest).get(0));

        // find the least time dest(considering transfer stations are considered as multiple dest)
        for (int stationIndexIter : stationNameHashMap.get(dest)){            
            Station station = stationList.get(stationIndexIter);
            if (station.time < destStation.time) destStation = station;
        }

        // declare result list
        List<Station> result = new LinkedList<>();
        // declare cursor
        Station currStation = destStation;
        
        // add station into list
        while (true){
            // add station to start of list(dest -> src)
            result.add(0, currStation);
            // break if srcStation reached
            if (currStation.name.equals(srcStation.name)) break;
            // move cursor to prevStation
            currStation = currStation.prev;
        }

        // print final result
        for (int index = 0; index < result.size()-1; index++){
            Station thisStation = result.get(index);
            // transfer station
            if (thisStation.name.equals(result.get(index+1).name)){
                System.out.print("[" + thisStation.name + "] ");
                index++;
            }
            // normal station
            else{
                System.out.print(thisStation.name + " ");
            }
        }
        // end station
        System.out.print(result.get(result.size()-1).name);
        System.out.println();

        // print time
        System.out.println(destStation.time);

    }
    
}