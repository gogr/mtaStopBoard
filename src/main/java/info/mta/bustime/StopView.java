package info.mta.bustime;

import java.io.IOException;

public class StopView {
    public static void main(String[] args) throws IOException {
        String stopId = "307020";
        String rawStopData = new QueryStopMonitoringData(stopId).queryStopData();
        Stop stop = new Stop(rawStopData);
        System.out.println(stop);
        System.out.println(stop.getStopMetaData());
    }
}
