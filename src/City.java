public class City {
    String name;
    double heuristicValue;
    boolean hasChargeStation;

    public City(String name, double heuristicValue, boolean hasChargeStation) {
        this.name = name;
        this.heuristicValue = heuristicValue;
        this.hasChargeStation = hasChargeStation;
    }
}
