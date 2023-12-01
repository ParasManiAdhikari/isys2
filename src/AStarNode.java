class AStarNode implements Comparable<AStarNode> {
    City city;
    int gCost;
    int hCost;
    int totalCost;
    int remainingRange;

    public AStarNode(City city, int gCost, int hCost, int remainingRange) {
        this.city = city;
        this.gCost = gCost;
        this.hCost = hCost;
        this.remainingRange = remainingRange;
        this.totalCost = gCost + hCost;
    }

    @Override
    public int compareTo(AStarNode other) {  // -1 = this, 1 = other
//        System.out.println("\nPRIORITY : " + this.city.name + ": " + this.totalCost + " ? " + other.city.name + ": " + other.totalCost) ;
//        System.out.println("sdkljf: " + Integer.compare(this.totalCost, other.totalCost));
        if(this.city.hasChargeStation && !other.city.hasChargeStation){
            return -1;
        } else if(!this.city.hasChargeStation && other.city.hasChargeStation){
            return 1;
        } else{
            return Integer.compare(this.totalCost, other.totalCost);
        }
    }
}