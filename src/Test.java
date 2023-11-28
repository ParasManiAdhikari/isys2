//import java.text.DecimalFormat;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//
//public class Test {
//    public static void main(String[] args) {
//        // Alg 1: Try lanes 1 by 1
//        List<Lane> lanes = new ArrayList<>();
//        lanes.add(new Lane("A1", 0.25, 100));
//        lanes.add(new Lane("A2", 0.25, 100));
//        lanes.add(new Lane("A3", 0.07, 3));
//        lanes.add(new Lane("B1", 0.05, 100));
//        lanes.add(new Lane("C1", 0.17, 100));
//        lanes.add(new Lane("C2", 0.20, 100));
//        lanes.add(new Lane("D1", 0.07, 100));
//
//        int[] laneCounts = new int[lanes.size()];
//        int numSimulations = 7;
//        Random random = new Random();
//
//        // choose 100 lanes
//        for (int i = 0; i < numSimulations; i++) {
//            double randomValue = random.nextDouble();
//            System.out.println("-------" + new DecimalFormat("#.##").format(randomValue) + "-------");
//            double cumulativeProbability = 0;
//
//            for (int j = 0; j < lanes.size(); j++) {
//                cumulativeProbability += lanes.get(j).probability;
//                System.out.println(lanes.get(j).laneName + " " + cumulativeProbability);
//                if (randomValue <= cumulativeProbability) {
//                    System.out.println(lanes.get(j).laneName + " **CHOSEN");
//                    laneCounts[j]++;
//                    break;
//                }
//            }
//        }
//
//        // show how many times a lane was chosen
//        for (int i = 0; i < lanes.size(); i++) {
//            System.out.println(lanes.get(i).laneName + ": " + laneCounts[i] + " times");
//        }
//
//
//        // Alg 2: Try Random Lanes
//        /*List<Lane> lanes = new ArrayList<>();
//        lanes.add(new Lane("A1", 0.25, 100));
//        lanes.add(new Lane("A2", 0.25, 100));
//        lanes.add(new Lane("A3", 0.07, 3));
//        lanes.add(new Lane("B1", 0.05, 100));
//        lanes.add(new Lane("C1", 0.17, 100));
//        lanes.add(new Lane("C2", 0.20, 100));
//        lanes.add(new Lane("D1", 0.07, 100));
//
//        int[] laneCounts = new int[lanes.size()];
//        int numSimulations = 7;
//
//        Random random = new Random();
//        for (int i = 0; i < numSimulations; i++) {
//            double randomValue = random.nextDouble();
//            System.out.println("-------" + new DecimalFormat("#.##").format(randomValue) + "-------");
//            double cumulativeProbability = 0;
//
//            for (int j = 0; j < lanes.size(); j++) {
//                int randomIndex = random.nextInt(lanes.size());
//                Lane randomLane = lanes.get(randomIndex);
//                System.out.print(randomLane.laneName);
//                cumulativeProbability += randomLane.probability;
//                System.out.println(" " + cumulativeProbability);
//                if (randomValue <= cumulativeProbability) {
//                    System.out.println(randomLane.laneName + "  **CHOSEN");
//                    laneCounts[randomIndex]++;
//                    break;
//                }
//            }
//        }
//
//        for (int i = 0; i < lanes.size(); i++) {
//            System.out.println(lanes.get(i).laneName + ": " + laneCounts[i] + " times");
//        }*/
//    }
//}
