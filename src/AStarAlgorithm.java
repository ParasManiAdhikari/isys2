import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
public class AStarAlgorithm {

    public static List<String> aStarSearch(List<City> cities, List<Connection> connections, String start, String goal, int maxRange) {
        // Implement A* algorithm here

        return new ArrayList<>(); // Replace with the actual result
    }

    public static void main(String[] args) {
        List<City> cities = readCities();
        List<Connection> connections = readConnections();

//        aStarAlgorithm(cities, connections);

    }

//    private static int findDistance(City city1, City city2) {
//        return city1.heuristicVal +
//    }

    private static List<Connection> readConnections() {
        List<Connection> connections = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\paras\\IdeaProjects\\isys2\\src\\resources\\testcases_Teilaufgabe_2\\t1_connections.txt"))) {
            String line;
            br.readLine(); // Skip the header line
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                String city1 = parts[0];
                String city2 = parts[1];
                int distance = Integer.parseInt(parts[2]);
                connections.add(new Connection(city1, city2, distance));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return connections;
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
}
