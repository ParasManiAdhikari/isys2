import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class AStarAlgorithm {
    public static List<City> cities = new ArrayList<>();
    public static List<BigCity> bigCities = new ArrayList<>();
    public static List<Connection> connections = new ArrayList<>();
    public static List<TestCase> testCases = readBigTests("src/resources/testcases_Teilaufgabe_3/testcases_bigGraph.txt");

    public static double STARTCOST = 0;
    public static double CHARGECOST = 10;
    public static double BigTestRange = 200;

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
//            List<String> path = aStarSearch(getCityByName(testCase.start), getCityByName(testCase.goal), 200);
//            System.out.println(path);
//            System.out.println("-------");
//        }
        // SINGLE TEST CASE - BIG CITY
//        TestCase testCase = new TestCase("Bohmte", "Kappeln");
//        bigCities = readBigCities("src/resources/testcases_Teilaufgabe_3/bigGraph_cities.txt");
//        cities = convertBigCities(bigCities, testCase.goal);
//        connections = readConnections("src/resources/testcases_Teilaufgabe_3/bigGraph_connections.txt");
//        List<String> path = aStarSearch(getCityByName(testCase.start), getCityByName(testCase.goal), 200);
//        System.out.println("-------");
    }

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

    private static City getCityByName(String cityName) {
        for (City city : cities) {
            if (city.name.equals(cityName)) {
                return city;
            }
        }
        return null;
    }

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
                return new ArrayList<>();
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

    private static AStarNode openListContains(City neighbor, PriorityQueue<AStarNode> openList) {
        for (AStarNode node : openList) {
            if (node.city == neighbor) {
                return node;
            }
        }
        return null;
    }

    private static double getDistance(City parentCity, City currentCity) {
        for (Connection connection : connections) {
            if ((connection.city1.equals(parentCity) && connection.city2.equals(currentCity)) ||
                    (connection.city1.equals(currentCity) && connection.city2.equals(parentCity))) {
                return connection.distance;
            }
        }
        return -1;
    }

    private static String printList(List<City> closedList) {
        String list = "";
        for (City city : closedList) {
            list += " + " + city.name;
        }
        return list;
    }

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