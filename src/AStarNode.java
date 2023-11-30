class AStarNode implements Comparable<AStarNode> {
    City city;
    int gCost;
    int hCost;
    int totalCost;

    public AStarNode(City city, int gCost, int hCost) {
        this.city = city;
        this.gCost = gCost;
        this.hCost = hCost;
        this.totalCost = gCost + hCost;
    }

    @Override
    public int compareTo(AStarNode other) {
        return Integer.compare(this.totalCost, other.totalCost);
    }
}