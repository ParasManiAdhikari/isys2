class AStarNode implements Comparable<AStarNode> {
    String city;
    int gCost;
    int hCost;
    int fCost;

    public AStarNode(String city, int gCost, int hCost) {
        this.city = city;
        this.gCost = gCost;
        this.hCost = hCost;
        this.fCost = gCost + hCost;
    }

    @Override
    public int compareTo(AStarNode other) {
        return Integer.compare(this.fCost, other.fCost);
    }
}