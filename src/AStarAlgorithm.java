import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
public class AStarAlgorithm {
    public static List<City> cities = readCities();
    public static List<Connection> connections = readConnections();
    public static void main(String[] args) {
        List<String> path = aStarSearch(getCityByName("A"), getCityByName("B"), 410);
        System.out.println(path);
    }

    private static City getCityByName(String cityName) {
        for (City city : cities) {
            if (city.name.equals(cityName)) {
                return city;
            }
        }
        return null; // City not found
    }

    public static List<String> aStarSearch(City start, City goal, int maxRange) {
        PriorityQueue<AStarNode> openList = new PriorityQueue<>();
        List<City> closedList = new ArrayList<>();

        Map<City, City> parentMap = new HashMap<>();

        AStarNode startNode = new AStarNode(start, 0, start.heuristicValue);
        openList.add(startNode);

        while (!openList.isEmpty()) {
            System.out.println("OPEN LIST: " + "{" + printList2(openList) + "}");
            System.out.println("-------------------------\n");
            AStarNode currentNode = openList.poll();                                                                    // Choose the lowest cost node for expansion
            City currentCity = currentNode.city;

            if (currentCity.equals(goal)) {
                System.out.println("TOTAL COST " + currentNode.gCost);
                System.out.println("B REACHED");
                return reconstructPath(parentMap, start, goal);
            }
            closedList.add(currentCity);

            System.out.println(currentCity.name + " : Chosen from Open List | " + "GCOST : " + currentNode.gCost);
            System.out.println("CLOSED LIST: " + "{ " + printList(closedList) + " }");
            System.out.println("OPEN LIST: " + "{" + printList2(openList) + "}");
            System.out.println("------------Possible EXPANSIONS-------------");

            for (Connection connection : connections) {
                if (connection.city1.equals(currentCity) && !closedList.contains(connection.city2)) {
                    System.out.print("* Current -> Neighbour:  " +  connection.city1.name + " -> " + connection.city2.name + " | ");
                    int tentativeGCost = currentNode.gCost + connection.distance;

                    if (tentativeGCost > maxRange && !currentCity.hasChargeStation) {
                        System.out.print(tentativeGCost +  " > " +  maxRange);
                        continue; // Skip this connection if charging is needed and no charging station is available
                    }

                    AStarNode neighbor = new AStarNode(connection.city2, tentativeGCost, connection.city2.heuristicValue);

                    if (!openList.contains(neighbor) || tentativeGCost < neighbor.gCost) {
                        System.out.print(tentativeGCost);
                        parentMap.put(connection.city2, currentCity);

                        openList.add(neighbor);
                        System.out.print(" | " + neighbor.city.name + " -> OpenList *");
                    }
                    System.out.println();
                } else if(connection.city1.equals(currentCity) && closedList.contains(connection.city2)){
                    System.out.println("* Neighbour "+  connection.city2.name + " already explored. *");
                }
            }
            printParentMap(parentMap);
        }

        return new ArrayList<>();                                                                                       // EMPTY
    }

    private static String printList(List<City> closedList) {
        String list = "";
        for(City city: closedList){
            list += " + " + city.name;
        }
        return list;
    }

    private static String printList2(PriorityQueue<AStarNode> closedList) {
        String list = "";
        for(AStarNode node: closedList){
            list += " + " + node.city.name;
        }
        return list;
    }
    public static void printParentMap(Map<City, City> parentMap) {
        System.out.println("Parent Map:");
        for (Map.Entry<City, City> entry : parentMap.entrySet()) {
            City childCity = entry.getKey();
            City parentCity = entry.getValue();
            System.out.println("Child: " + childCity.name + ", Parent: " + parentCity.name);
        }
    }

    private static int heuristicValue(City currentCity, City goalCity) {
        return Math.abs(currentCity.heuristicValue - goalCity.heuristicValue);
    }

    // Reconstruct the path from start to goal using the parent map
    private static List<String> reconstructPath(Map<City, City> parentMap, City start, City goal) {
        List<String> path = new ArrayList<>();
        City current = goal;
        while (current != null) {
            path.add(current.name);
            current = parentMap.get(current);
        }
        Collections.reverse(path);
        return path;
    }

    private static List<City> readCities() {
        List<City> cities = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\paras\\IdeaProjects\\isys2\\src\\resources\\testcases_Teilaufgabe_2\\t1_cities.txt"))) {
            String line;
            br.readLine(); // Skip the header line
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                String name = parts[0];
                int heuristicValue = Integer.parseInt(parts[1]);
                boolean hasChargeStation = Boolean.parseBoolean(parts[2]);
                cities.add(new City(name, heuristicValue, hasChargeStation));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return cities;
    }

    private static List<Connection> readConnections() {
        List<Connection> connections = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\paras\\IdeaProjects\\isys2\\src\\resources\\testcases_Teilaufgabe_2\\t1_connections.txt"))) {
            String line;
            br.readLine(); // Skip the header line
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                City city1 = getCityByName(parts[0]);
                City city2 = getCityByName(parts[1]);
                int distance = Integer.parseInt(parts[2]);
                connections.add(new Connection(city1, city2, distance));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return connections;
    }
}
