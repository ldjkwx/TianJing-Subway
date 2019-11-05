import java.util.*;

public class Map{
    private Vector<String> listSubwayInfo = new Vector<>();
    private int nMaxDistance = 999999; // 访问不到的设置为无穷大
    private static HashMap<String, Station> mapNametoStation = new HashMap<>();
    private static HashMap<Integer, Station> mapStationIdtoStation = new HashMap<>();

    private static HashMap<Integer, String> stationInfo = new HashMap<>();
    private static HashMap<String,String> finalMap = new HashMap<>();
    //地铁中转站的信息
    private static HashMap<String, Integer> mapTransferStationNametoDistance = new HashMap<>();
    private static HashMap<Integer, Integer> circleInfo = new HashMap<>();
    private static HashMap<String, Integer> NtostationInfo = new HashMap<>();
    // ------------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------
    // 最优路径规划
    int getLineNumber(int nStationId) {
        return nStationId / 1000;
    }

    int getLineNumber(String strLine) {
        return Integer.parseInt(strLine);
    }

    int getLineNumber(Station S1, Station S2) {
        for (int nS1Id: S1.setStationId) {
            int nS1LineNum = getLineNumber(nS1Id);

            for (int nS2Id: S2.setStationId) {
                int nS2LineNumber = getLineNumber(nS2Id);

                if (nS1LineNum == nS2LineNumber) {
                    return nS1LineNum;
                }
            }
        }
        return -1;
    }

    int getStationsDistance(Station S1, Station S2) {
        int nMinDistance = nMaxDistance;
        Set<Integer> S1Ids = S1.setStationId;
        Set<Integer> S2Ids = S2.setStationId;
        for (int id1: S1Ids) {
            for (int id2: S2Ids) {
                int nDistance = Math.abs(id1-id2);
                if(nDistance<1000 && id1<0 &&id2<0)
                {
                    int lineNum = getLineNumber(-id1);
                    int lineLength = circleInfo.get(lineNum);
                    nDistance = Math.min(Math.abs(id1-id2), lineLength-Math.abs(id1-id2));
                }
                if (nDistance < nMinDistance) {
                    nMinDistance = nDistance;
                }
            }
        }

        return nMinDistance;
    }

    Path Dijkstra(String strStartStationName, String strEndStationName) {
        // todo: 进行一些合法性检查
        Station stationStart = mapNametoStation.get(strStartStationName);
        Station stationEnd = mapNametoStation.get(strEndStationName);
        if(stationStart==null || stationEnd==null)
        {
            System.out.println("起始站或终点输入不正确，请检查输入数据！");
            return null;
        }
        mapTransferStationNametoDistance.put(strEndStationName, nMaxDistance);
        mapTransferStationNametoDistance.put(strStartStationName, nMaxDistance);
        Path pathStart = new Path();
        pathStart.nFDistance = 0;
        pathStart.stationLastStationInPath = stationStart;
        Path Dijkstra = new Path();
        Dijkstra.nFDistance = nMaxDistance;

        Stack<Path> stackAllPaths = new Stack<>();
        stackAllPaths.push(pathStart);

        Set<String> TStationNameSet = new TreeSet<>();
        for(String strname: mapTransferStationNametoDistance.keySet()) {
            TStationNameSet.add(strname);
        }
        for(String strname: mapTransferStationNametoDistance.keySet()) {
            finalMap.put(strname,"null");
        }
        while (!stackAllPaths.empty()) {
            Path pathCurrent = stackAllPaths.pop();
            if (pathCurrent.nFDistance > Dijkstra.nFDistance) {
                continue;
            }
            int nBDistance = getStationsDistance(pathCurrent.stationLastStationInPath, stationEnd);
            if (nBDistance == 0) {      // 到达终止节点
                if (pathCurrent.nFDistance < Dijkstra.nFDistance) {
                    Dijkstra = pathCurrent;
                }
                continue;
            }
            int minDistance = 1000000;
            String nextStation = null;
            TStationNameSet.remove(pathCurrent.stationLastStationInPath.stationName);
            for (String strTStationName: mapTransferStationNametoDistance.keySet()) {
                Station stationTransfer = mapNametoStation.get(strTStationName);
                int nDistanceDelta = getStationsDistance(pathCurrent.stationLastStationInPath, stationTransfer);
                int nTStationDistance = pathCurrent.nFDistance + nDistanceDelta;
                if (nTStationDistance >= mapTransferStationNametoDistance.get(strTStationName)) {
                    continue;
                }
                finalMap.put(strTStationName,pathCurrent.stationLastStationInPath.stationName);
                mapTransferStationNametoDistance.put(strTStationName, nTStationDistance);
            }
            for(String strTStationName: mapTransferStationNametoDistance.keySet()) {
                int Distance = mapTransferStationNametoDistance.get(strTStationName);
                if(Distance<minDistance&& TStationNameSet.contains(strTStationName)) {
                    minDistance = Distance;
                    nextStation = strTStationName;
                }
            }
            Station stationTransfer = mapNametoStation.get(nextStation);
            Path pathNew = new Path();
            pathNew.nFDistance = minDistance;
            pathNew.stationLastStationInPath = stationTransfer;
            stackAllPaths.push(pathNew);
        }
        System.out.println(finalMap);
        return Dijkstra;
    }


}
