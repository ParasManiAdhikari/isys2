class AStarNode implements Comparable<AStarNode> {
    City city;
    double gCost;
    double hCost;
    double totalCost;
    double remainingRange;

    public AStarNode(City city, double gCost, double hCost, double remainingRange) {
        this.city = city;
        this.gCost = gCost;
        this.hCost = hCost;
        this.remainingRange = remainingRange;
        this.totalCost = gCost + hCost;
    }

    @Override
    public int compareTo(AStarNode other) {  // -1 = this, 1 = other
////        System.out.println("\nPRIORITY : " + this.city.name + ": " + this.totalCost + " ? " + other.city.name + ": " + other.totalCost) ;
////        System.out.println("sdkljf: " + Integer.compare(this.totalCost, other.totalCost));
//        if(this.city.hasChargeStation && !other.city.hasChargeStation && hCost <= this.remainingRange){
//            return -1;
//        } else if(!this.city.hasChargeStation && other.city.hasChargeStation){
//            return 1;
//        } else{
            return Double.compare(this.totalCost, other.totalCost);

    }
}