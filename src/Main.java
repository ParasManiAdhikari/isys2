import java.util.*;

import static java.lang.Boolean.*;

public class Main {
    static int numberOfCars = 0;
    static int sizeOfA3 = 3;

    public static void main(String[] args) {
        List<Lane> lanes = new ArrayList<>();
        lanes.add(new Lane("A1", 0.25));
        lanes.add(new Lane("A2", 0.25));
        lanes.add(new Lane("A3", 0.07));
        lanes.add(new Lane("B1", 0.05));
        lanes.add(new Lane("C1", 0.17));
        lanes.add(new Lane("C2", 0.20));
        lanes.add(new Lane("D1", 0.07));

        for (int i = 0; i < 60; i++) {
            System.out.println("---- SECOND " + i + " -------");

//            addNewCarRandom(lanes);  // lane direction

            // UPDATE TRAFFIC LIGHTS
            if(i <= 39){
                lightLanes(lanes, "A1", "A2");
                if(i <= 10) {
                    lightLanes(lanes, "A1", "A2", "A3");
                }
            } else if(i >= 14 && i <= 39){
                lightLanes(lanes, "C1", "C2", "A1", "A2");
            } else if (i >= 43 && i <= 55){
                lightLanes(lanes, "D1", "B1");
            } else lightLanes(lanes);

           printLanesAsTree(lanes);
           numberOfCars++;
//            removeCars(lanes);
        }

        System.out.println(numberOfCars);
//        printLanesAsTree(lanes);
    }

    private static void lightLanes(List<Lane> lanes, String... laneNames) {
        // turn off all lanes
        for(Lane lane : lanes){
            getLaneByName(lanes, lane.laneName).trafficLightIsGreen = FALSE;
        }

        // turn on the selected lanes
        if(laneNames.length >= 1){
            for(String laneName: laneNames){
                getLaneByName(lanes, laneName).trafficLightIsGreen = TRUE;
            }
        }
    }

    private static Lane getLaneByName(List<Lane> lanes, String laneName){
        for(Lane lane : lanes){
            if(lane.laneName.equals(laneName)){
                return lane;
            }
        }
        throw new IllegalArgumentException("Lane not found");
    }

    public static void addNewCarRandom(List<Lane> lanes) {
        Random random = new Random();
        double randomIndex = 1.6 * random.nextDouble();
        System.out.println("random: "+ randomIndex);
        int nextLane = laneGenerator(randomIndex);
        Lane randomLane = lanes.get(nextLane);
        randomLane. cars.add(new Car("Car" + numberOfCars++));
    }

    private static int laneGenerator(double randomIndex) {
      //  return 0;
       return randomIndex < 0.25 * 1.6 ? 0 : randomIndex < 0.50 * 1.6 ? 1 : randomIndex < 0.57 * 1.6 ? 2
                                                                                                    : randomIndex <  0.62 * 1.6 ? 3
                                                                                                    : randomIndex <  0.79 * 1.6 ? 4
                                                                                                    : randomIndex <  0.99   * 1.6 ? 5
                                                                                                    : 6;
    }

    // EXTRA
    public static void printLanesAsTree(List<Lane> lanes) {
        for (Lane lane : lanes) {
            System.out.print("Lane: " + lane.laneName + " ");
            if(lane.trafficLightIsGreen){
                System.out.println("GREEN");
            } else System.out.println("OFF");
//            printCarsAsTree(lane.getCars());
//            System.out.println();
        }
    }

    private static void printCarsAsTree(Queue<Car> cars) {
        for(Car car : cars){
            System.out.println("--" + car.carName);
        }
    }
}
