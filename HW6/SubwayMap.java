import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class SubwayMap{
    private List<Station> stationList = new ArrayList<>();
    // station id to index
    private HashMap<String, Integer> stationIDHashMap = new HashMap<>();
    // station name to index
    private HashMap<String, List<Integer>> stationNameHashMap = new HashMap<>();

    private int stationCnt = 0;

    public void constructMap(String fileName){
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            // mode 0, 1, 2 for 3 types of input
            int mode = 0;

            while ((line = br.readLine()) != null) {
                if (line.isEmpty()){
                    mode++; continue; 
                } 

                String[] words = line.split(" ");

                // save station info
                if (mode == 0){
                    // save in arraylist
                    Station newStation = new Station(words[0], words[1], words[2]);
                    stationList.add(newStation);    

                    // save in id hashmap
                    stationIDHashMap.put(words[0], stationCnt);
                    
                    // save in name hashmap
                    if (stationNameHashMap.get(words[1]) == null){
                        List<Integer> indexList = new ArrayList<>();
                        indexList.add(stationCnt);
                        stationNameHashMap.put(words[1], indexList); 
                    }
                    else{
                        List<Integer> indexList = stationNameHashMap.get(words[1]);
                        indexList.add(stationCnt);
                        stationNameHashMap.put(words[1], indexList); 
                    }
                         
                    
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
                        srcStation.isTransfer = true;
                        srcStation.transferTime = time;                        
                    }

                }
            }
        } catch (IOException e) {
            System.out.println("ERR: " + e.toString());
        }

        // add edge as 
        for (String srcName : stationNameHashMap.keySet()){
            // get src alias
            List<Integer> srcIndexList = stationNameHashMap.get(srcName);

            // if list length is 1, this station is not a transfer station.
            if (srcIndexList.size() == 1) continue;
            // if not, create edges for each combination.
            else{
                //System.out.println(srcName);
                for (int item1 : srcIndexList){
                    for (int item2 : srcIndexList){
                        if (item1 == item2) continue;
                        else{
                            //System.out.println(stationList.get(item1).name + " " + stationList.get(item2).name);
                            Edge newEdge = new Edge(stationList.get(item2), stationList.get(item2).transferTime);
                            stationList.get(item1).adjacentEdges.add(newEdge);
                        }
                    }
                }
            }
        }
    }
        // TODO : NEED A FIX
    public void calculate(String src){
        Station srcStation = stationList.get(stationNameHashMap.get(src).get(0));

        PriorityQueue<Station> priorityQueue = new PriorityQueue<>();

        for (Station stationIter : stationList){
            if (srcStation == stationIter){
                stationIter.time = 0;
                priorityQueue.add(stationIter);
            }
            else{
                stationIter.time = 1000000000L;
                priorityQueue.add(stationIter);                    
            }
        }

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
    
    public List<Station> printResult(String src, String dest){
        Station srcStation = stationList.get(stationNameHashMap.get(src).get(0));
        Station destStation = stationList.get(stationNameHashMap.get(dest).get(0));

        // find the least time dest(considering transfer stations are considered as multiple dest)
        for (int stationIndexIter : stationNameHashMap.get(dest)){            
            Station station = stationList.get(stationIndexIter);
            if (station.time < destStation.time) destStation = station;
        }

        List<Station> result = new LinkedList<>();
        Station currStation = destStation;

        while (true){
            //System.out.println(currStation.name);
            try {
                // Pause the program for 5 seconds
                Thread.sleep(50);
            } catch (InterruptedException e) {
                // Handle any exceptions
                
                e.printStackTrace();
            }

            result.add(0, currStation);

            if (currStation == srcStation) break;
            currStation = currStation.prev;
        }

        for (int index = 0; index < result.size()-1; index++){
            if (result.get(index).name.equals(result.get(index+1).name)){
                System.out.print("[" + result.get(index).name + "] ");
                index++;
            }
            else{
                System.out.print(result.get(index).name + " ");
            }
        }
        System.out.print(result.get(result.size()-1).name + " ");

        System.out.println();
        System.out.println(destStation.time);

        return result;
    }
    
}