package info.mta.bustime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
import java.util.stream.Collectors;

public class QueryStopMonitoringData {
    private static final String BASE_URL = "http://bustime.mta.info/api/siri/stop-monitoring.json";
    private static final String API_KEY = "9492a8b9-5212-4ab4-85c5-c957703ae065";
    private static final String API_VERSION = "2";
    private String URL;

    QueryStopMonitoringData(String stopId) {
        URL = BASE_URL + "?key=" + API_KEY + "&MonitoringRef=" + stopId + "&version=" + API_VERSION;
    }

    public String queryStopData() throws IOException {
        int timeout = 60000;
        int responseCode;
        BufferedReader reader;
        HttpURLConnection connection = null;
        try {
            java.net.URL pageSpeedOnlineUrl = new URL(URL);
            connection = (HttpURLConnection) pageSpeedOnlineUrl.openConnection();
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.connect();
            responseCode = connection.getResponseCode();
            if (200 <= responseCode && responseCode <= 299) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }
            return reader.lines().collect(Collectors.joining());
        } finally {
            Objects.requireNonNull(connection).disconnect();
        }
    }
}
