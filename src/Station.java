import java.util.Set;
import java.util.TreeSet;
// 站点，保存站点名称和ID
class Station {
    String stationName;
    Set<Integer> setStationId;

    Station() {
        stationName = "";
        setStationId = new TreeSet<>();
    }
}
