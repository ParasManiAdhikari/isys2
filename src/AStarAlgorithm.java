import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
public class AStarAlgorithm {
    public static List<City> cities = readCities();
    public static List<Connection> connections = readConnections();
    public static void main(String[] args) {
        List<String> optimalRoute = aStarSearch(cities, connections, getCityByName("A"), getCityByName("B"), 410); // 410 for test1
//        aStarAlgorithm(cities, connections);
        int index = 2;
        System.out.println(connections.get(index).city1.name + ", " + connections.get(index).city2.name);
    }

    public static List<String> aStarSearch(List<City> cities, List<Connection> connections, City start, City goal, int maxRange) {
        PriorityQueue<AStarNode> openList = new PriorityQueue<>();
        Set<City> closedList = new HashSet<>();

        Map<City, City> parentMap = new HashMap<>();

        AStarNode startNode = new AStarNode(start, 0, heuristicValue(start, goal));
        openList.add(startNode);

        while (!openList.isEmpty()) {
            AStarNode currentNode = openList.poll();
            City currentCity = currentNode.city;

            if (currentCity.equals(goal)) {
                return reconstructPath(parentMap, start, goal);
            }

            closedList.add(currentCity);

            for (Connection connection : connections) {
                if (connection.city1.equals(currentCity) && !closedList.contains(connection.city2)) {
                    int tentativeGCost = currentNode.gCost + connection.distance;

                    if (tentativeGCost > maxRange && !currentCity.hasChargeStation) {
                        continue; // Skip this connection if charging is needed and no charging station is available
                    }

                    AStarNode neighbor = new AStarNode(connection.city2, tentativeGCost, heuristicValue(connection.city2, goal));

                    if (!openList.contains(neighbor) || tentativeGCost < neighbor.gCost) {
                        parentMap.put(connection.city2, currentCity);

                        openList.add(neighbor);
                    }
                }
            }
        }

        return new ArrayList<>();
    }

    private static int heuristicValue(City currentCity, City goalCity) {
        return Math.abs(currentCity.heuristicValue - goalCity.heuristicValue);
    }

    // Helper method to reconstruct the path from start to goal using the parent map
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
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
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
                City city1 = new City(parts[0], getHeuristicValueByName(parts[0]), getladeStatiionByName(parts[0]));
                City city2 = new City(parts[1], getHeuristicValueByName(parts[1]), getladeStatiionByName(parts[1]));
                int distance = Integer.parseInt(parts[2]);
                connections.add(new Connection(city1, city2, distance));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return connections;
    }

    private static City getCityByName(String cityName) {
        for (int i = 0; i < cities.size(); i++) {
            if (cities.get(i).name.equals(cityName)) {
                return cities.get(i);
            }
        }
        return null; // City not found
    }

    private static boolean getladeStatiionByName(String cityName) {
        for (int i = 0; i < cities.size(); i++) {
            if (cities.get(i).name.equals(cityName)) {
                return cities.get(i).hasChargeStation;
            }
        }
        return false; // City not found
    }

    private static int getHeuristicValueByName(String cityName) {
        for (int i = 0; i < cities.size(); i++) {
            if (cities.get(i).name.equals(cityName)) {
                return cities.get(i).heuristicValue;
            }
        }
        return 0; // City not found
    }
}
