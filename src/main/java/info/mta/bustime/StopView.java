package info.mta.bustime;

import java.io.IOException;

public class StopView {
    public static void main(String[] args) throws IOException, InterruptedException {
        String stopId = "307020";
        String rawStopData = new QueryStopMonitoringData(stopId).queryStopData();
        Stop stop = new Stop(rawStopData);
        System.out.println(stop);
        Thread.sleep(1000);
        System.out.println(stop);
    }
}
