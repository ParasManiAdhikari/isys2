import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Lane {
    String laneName;
    double probability;
    Queue<Car> cars  = new LinkedList<>();
    Boolean trafficLightIsGreen = Boolean.FALSE;

    // Constructor
    public Lane(String laneName, double probability) {
        this.laneName = laneName;
        this.probability = probability;
    }
}
