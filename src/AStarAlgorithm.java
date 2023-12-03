import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * This class implements the A* algorithm for pathfinding in a graph of cities and connections.
 * It includes methods to read input files, perform A* search, and handle big graphs with special considerations.
 */
public class AStarAlgorithm {

    /**
     * List of normal cities in the graph.
     */
    public static List<City> cities = new ArrayList<>();

    /**
     * List of big cities in the graph.
     */
    public static List<BigCity> bigCities = new ArrayList<>();

    /**
     * List of connections between cities in the graph.
     */
    public static List<Connection> connections = new ArrayList<>();

    /**
     * List of test cases for big graphs.
     */
    public static List<TestCase> testCases = readBigTests("src/resources/testcases_Teilaufgabe_3/testcases_bigGraph.txt");

    /**
     * Cost of starting the journey.
     */
    public static double STARTCOST = 0;

    /**
     * Cost of charging during the journey.
     */
    public static double CHARGECOST = 10;

    /**
     * Maximum range for big tests.
     */
    public static double BigTestRange = 200;

    /**
     * The main method of the program. It demonstrates the A* algorithm on various test cases.
     *
     * @param args The command-line arguments (not used in this program).
     */
    public static void main(String[] args) {
        // AUFGABE 2
        int[] ranges = {410, 500, 30, 40, 30, 410, 410};
        for(int i = 1; i <= 7; i++){
            System.out.println("TESTCASE " +  i);
            cities = readCities("src/resources/testcases_Teilaufgabe_2/t" + i + "_cities.txt");
            connections = readConnections("src/resources/testcases_Teilaufgabe_2/t" + i + "_connections.txt");
            List<String> path = aStarSearch(getCityByName("A"), getCityByName("B"), ranges[i-1]);
            System.out.println(path);
            System.out.println("-----------");
        }
        // AUFGABE 3
//        for(TestCase testCase : testCases){
//            bigCities = readBigCities("src/resources/testcases_Teilaufgabe_3/bigGraph_cities.txt");
//            cities = convertBigCities(bigCities, testCase.goal);
//            connections = readConnections("src/resources/testcases_Teilaufgabe_3/bigGraph_connections.txt");
//            List<String> path = aStarSearch(getCityByName(testCase.start), getCityByName(testCase.goal), BigTestRange);
//            System.out.println(path);
//            System.out.println("-------");
//        }
        // SINGLE TEST CASE - BIG CITY
//        TestCase testCase = new TestCase("Bohmte", "Kappeln");
//        bigCities = readBigCities("src/resources/testcases_Teilaufgabe_3/bigGraph_cities.txt");
//        cities = convertBigCities(bigCities, testCase.goal);
//        connections = readConnections("src/resources/testcases_Teilaufgabe_3/bigGraph_connections.txt");
//        List<String> path = aStarSearch(getCityByName(testCase.start), getCityByName(testCase.goal), BigTestRange);
//        System.out.println("-------");
    }

    /**
     * Converts a list of BigCities to normal Cities using heuristic calculation.
     *
     * @param bigCities The list of BigCities to be converted.
     * @param goal      The goal city used for heuristic calculation.
     * @return A list of normal Cities with updated heuristic values.
     */
    private static List<City> convertBigCities(List<BigCity> bigCities, String goal) {
        List<City> cities = new ArrayList<>();
        BigCity goalCity = null;
        for (BigCity bigCity : bigCities) {
            if (bigCity.name.equals(goal)) {
                goalCity = bigCity;
            }
        }
        for (BigCity bigCity : bigCities) {
            double heuristic = haversine_distance(bigCity.latitude, bigCity.longitude, goalCity.latitude, goalCity.longitude);
            cities.add(new City(bigCity.name, heuristic, bigCity.hasChargeStation));
        }
        return cities;
    }

    /**
     * Reads test cases from a file containing start and goal cities.
     *
     * @param path The path to the file containing test cases.
     * @return A list of test cases.
     */
    private static List<TestCase> readBigTests(String path) {
        List<TestCase> tests = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                String start = parts[0];
                String goal = parts[1];
                tests.add(new TestCase(start, goal));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return tests;
    }

    /**
     * Process cities.txt file and store the cities as City Instance.
     *
     * @param path The path to the file containing city information.
     * @return A list of City instances.
     */
    private static List<City> readCities(String path) {
        List<City> cities = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                String name = parts[0];
                double heuristicValue = Integer.parseInt(parts[1]);
                boolean hasChargeStation = Boolean.parseBoolean(parts[2]);
                cities.add(new City(name, heuristicValue, hasChargeStation));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return cities;
    }

    /**
     * Process bigGraph_cities.txt file and store the cities as BigCity Instance.
     *
     * @param path The path to the file containing big city information.
     * @return A list of BigCity instances.
     */
    private static List<BigCity> readBigCities(String path) {
        List<BigCity> cities = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                String name = parts[0];
                double latitude = Double.parseDouble(parts[1]);
                double longitude = Double.parseDouble(parts[2]);
                boolean hasChargeStation = Boolean.parseBoolean(parts[3]);
                cities.add(new BigCity(name, latitude, longitude, hasChargeStation));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return cities;
    }

    /**
     * Process connections.txt file and store the connections as Connection Instance.
     *
     * @param path The path to the file containing connection information.
     * @return A list of Connection instances.
     */
    private static List<Connection> readConnections(String path) {
        List<Connection> connections = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                City city1 = getCityByName(parts[0]);
                City city2 = getCityByName(parts[1]);
                double distance = Integer.parseInt(parts[2]);
                connections.add(new Connection(city1, city2, distance));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return connections;
    }

    /**
     * Retrieves a City instance by its name.
     *
     * @param cityName The name of the city to retrieve.
     * @return The City instance with the specified name, or null if not found.
     */
    private static City getCityByName(String cityName) {
        for (City city : cities) {
            if (city.name.equals(cityName)) {
                return city;
            }
        }
        return null;
    }

    /**
     * Performs A* search algorithm to find the shortest path between two cities.
     *
     * @param start     The starting city.
     * @param goal      The goal city.
     * @param maxRange  The maximum range a vehicle can travel without recharging.
     * @return A list of city names representing the shortest path, or an empty list if no path exists.
     */
    public static List<String> aStarSearch(City start, City goal, double maxRange) {
        PriorityQueue<AStarNode> openList = new PriorityQueue<>();
        List<City> closedList = new ArrayList<>();
        Map<City, City> parentMap = new HashMap<>();

        AStarNode startNode = new AStarNode(start, STARTCOST, start.heuristicValue, maxRange);
        openList.add(startNode);

        while (!openList.isEmpty()) {
            AStarNode currentNode = openList.poll();
            City currentCity = currentNode.city;

            if (currentCity.equals(goal)) {
                System.out.println("TOTAL COST " + currentNode.gCost);
//                return new ArrayList<>();
                return reconstructPath(parentMap, start, goal);
            }
            closedList.add(currentCity);
            boolean chargingNeeded = currentNode.remainingRange <= currentNode.gCost;

            boolean hasForwardConnection = false;

            for (Connection connection : connections) {
                if ((connection.city1.equals(currentCity) || connection.city2.equals(currentCity))) {
                    City neighbour = (connection.city1 == currentCity) ? connection.city2 : connection.city1;
                    if (closedList.contains(neighbour)) {
                        continue;
                    }
                    hasForwardConnection = true;
                    double costFromStart = currentNode.gCost + connection.distance;

                    if (currentNode.remainingRange < connection.distance && chargingNeeded) {
                        if (currentCity.hasChargeStation) {
                            currentNode.remainingRange = maxRange;
                            costFromStart += CHARGECOST;
                        } else {
                            continue;
                        }
                    }

                    AStarNode neighbor = new AStarNode(neighbour, costFromStart, neighbour.heuristicValue, currentNode.remainingRange - connection.distance);
                    AStarNode openListContains = openListContains(neighbour, openList);
                    if (openListContains == null) {
                        parentMap.put(neighbour, currentCity);
                        openList.add(neighbor);
                    }
                }
            }

            if (!hasForwardConnection) {
                City parentCity = parentMap.get(currentCity);
                double distance = getDistance(parentCity, currentCity);
                if (parentCity != null && currentCity.hasChargeStation && chargingNeeded) {
                    double costFromStart = currentNode.gCost + distance + CHARGECOST;
                    double newRemainingRange = maxRange - distance;
                    closedList = new ArrayList<>();
                    closedList.add(currentCity);
                    openList = new PriorityQueue<>();
                    openList.add(new AStarNode(parentCity, costFromStart, parentCity.heuristicValue, newRemainingRange));
                }
            }
        }
        System.out.println("Es existiert keine Lösung");
        return new ArrayList<>();
    }

    /**
     * Checks if the PriorityQueue contains a node with the specified city.
     *
     * @param neighbor The city to check for in the PriorityQueue.
     * @param openList The PriorityQueue of AStarNodes.
     * @return The AStarNode containing the specified city if found, or null otherwise.
     */
    private static AStarNode openListContains(City neighbor, PriorityQueue<AStarNode> openList) {
        for (AStarNode node : openList) {
            if (node.city == neighbor) {
                return node;
            }
        }
        return null;
    }

    /**
     * Retrieves the distance between two cities from the list of connections.
     *
     * @param parentCity The first city.
     * @param currentCity The second city.
     * @return The distance between the two cities, or -1 if no connection is found.
     */
    private static double getDistance(City parentCity, City currentCity) {
        for (Connection connection : connections) {
            if ((connection.city1.equals(parentCity) && connection.city2.equals(currentCity)) ||
                    (connection.city1.equals(currentCity) && connection.city2.equals(parentCity))) {
                return connection.distance;
            }
        }
        return -1;
    }

    /**
     * Prints a list of cities for debugging purposes.
     *
     * @param closedList The list of cities to print.
     * @return A string representation of the list of cities.
     */
    private static String printList(List<City> closedList) {
        String list = "";
        for (City city : closedList) {
            list += " + " + city.name;
        }
        return list;
    }

    /**
     * Reconstructs the path from start to goal using the parent map.
     *
     * @param parentMap The map containing parent relationships.
     * @param start     The starting city.
     * @param goal      The goal city.
     * @return A list of city names representing the reconstructed path.
     */
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

    /**
     * Calculates the haversine distance between two geographic coordinates.
     *
     * @param lat1 The latitude of the first point.
     * @param lon1 The longitude of the first point.
     * @param lat2 The latitude of the second point.
     * @param lon2 The longitude of the second point.
     * @return The haversine distance between the two points.
     */
    public static double haversine_distance(double lat1, double lon1, double lat2, double lon2) {
        final double d = 12742;
        double sinHalfDeltaLat = Math.sin(Math.toRadians(lat2 - lat1) / 2);
        double sinHalfDeltaLon = Math.sin(Math.toRadians(lon2 - lon1) / 2);
        double latARadians = Math.toRadians(lat1);
        double latBRadians = Math.toRadians(lat2);
        double a = sinHalfDeltaLat * sinHalfDeltaLat
                + Math.cos(latARadians) * Math.cos(latBRadians) * sinHalfDeltaLon * sinHalfDeltaLon;
        return d * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }
}