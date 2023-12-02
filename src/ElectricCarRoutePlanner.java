import java.util.*;

public class ElectricCarRoutePlanner {
    // City class representing a location with its properties
    public class City {
        private String name;
        private double heuristic; // Heuristic value for A* algorithm
        private boolean hasChargingStation;

        public City(String name, double heuristic, boolean hasChargingStation) {
            this.name = name;
            this.heuristic = heuristic;
            this.hasChargingStation = hasChargingStation;
        }

        public String getName() {
            return name;
        }

        public double getHeuristic() {
            return heuristic;
        }

        public boolean hasChargingStation() {
            return hasChargingStation;
        }
    }

    // Connection class representing a road between two cities
    public class Connection {
        private String source;
        private String destination;
        private int distance;

        public Connection(String source, String destination, int distance) {
            this.source = source;
            this.destination = destination;
            this.distance = distance;
        }

        public String getSource() {
            return source;
        }

        public String getDestination() {
            return destination;
        }

        public int getDistance() {
            return distance;
        }
    }

    // State class representing the current state in the search
    public class State implements Comparable<State> {
        private String city;
        private int range; // Remaining range of the electric car
        private int cost;  // Accumulated cost so far
        private State previousState; // Previous state in the search

        public State(String city, int range, int cost, State previousState) {
            this.city = city;
            this.range = range;
            this.cost = cost;
            this.previousState = previousState;
        }

        public String getCity() {
            return city;
        }

        public int getRange() {
            return range;
        }

        public int getCost() {
            return cost;
        }

        public State getPreviousState() {
            return previousState;
        }

        @Override
        public int compareTo(State other) {
            return 1;
            // Implement compareTo for priority queue in A* algorithm
//            return Integer.compare(this.cost + this.heuristic, other.cost + other.heuristic);
        }
    }

    // Define data structures to represent the graph, cities, connections, etc.
    // You may need classes like City, Connection, State, etc.

    // A* algorithm implementation
    public List<String> aStarSearch(String startCity, String goalCity, int initialRange, int chargingCost) {
        // Initialize priority queue for open states
        PriorityQueue<State> openSet = new PriorityQueue<>();

        // Create a map to store the best known cost to reach each state
        Map<State, Integer> costSoFar = new HashMap<>();

        // Create the initial state and add it to the open set
        State startState = new State(startCity, initialRange, 0, null);
        openSet.add(startState);
        costSoFar.put(startState, 0);

        while (!openSet.isEmpty()) {
            // Get the state with the lowest cost from the open set
            State currentState = openSet.poll();

            // Check if the goal is reached
            if (currentState.getCity().equals(goalCity)) {
                // Reconstruct the path from the goal state to the start state
                return reconstructPath(currentState);
            }

            // Expand the current state by considering all possible actions (connections)
            for (Connection connection : getConnections(currentState.getCity())) {
                // Calculate the new range after traveling the current connection
                int newRange = currentState.getRange() - connection.getDistance();

                // Check if charging is needed
                if (newRange < 0) {
                    int chargingNeeded = Math.abs(newRange);
                    int newCost = currentState.getCost() + chargingNeeded * chargingCost;

                    // Create a new state after charging
                    State nextState = new State(connection.getDestination(), initialRange, newCost, currentState);

                    // Check if the new state is better than the previously known state
                    if (!costSoFar.containsKey(nextState) || newCost < costSoFar.get(nextState)) {
                        costSoFar.put(nextState, newCost);
                        openSet.add(nextState);
                    }
                } else {
                    // Create a new state without charging
                    State nextState = new State(connection.getDestination(), newRange, currentState.getCost() + connection.getDistance(), currentState);

                    // Check if the new state is better than the previously known state
                    if (!costSoFar.containsKey(nextState) || nextState.getCost() < costSoFar.get(nextState)) {
                        costSoFar.put(nextState, nextState.getCost());
                        openSet.add(nextState);
                    }
                }
            }
        }

        // No path found
        return null;
    }

    // Helper method to reconstruct the path from the goal state to the start state
    private static List<String> reconstructPath(State goalState) {
        List<String> path = new ArrayList<>();
        State currentState = goalState;

        while (currentState != null) {
            path.add(currentState.getCity());
            currentState = currentState.getPreviousState();
        }

        Collections.reverse(path);
        return path;
    }

    // Helper method to get connections from a city
    private static List<Connection> getConnections(String city) {
        // Implement logic to get connections from the graph
        // You may need to represent the graph and connections appropriately
        return null;
    }

    public static void main(String[] args) {
        // Test your implementation with the provided test cases
        // Call aStarSearch method for each test case and print the result
    }
}
