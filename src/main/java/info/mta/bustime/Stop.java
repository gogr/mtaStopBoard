package info.mta.bustime;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static info.mta.bustime.TimeUtils.getTimeDiff;
import static java.time.format.DateTimeFormatter.ofPattern;

public class Stop {
    private String stopId;
    private String stopName;
    private ZonedDateTime responseDate;
    private Map<String, Bus> upcomingBuses = new HashMap<>();

    public Stop(String rawStopData) throws JsonProcessingException {
        processJson(rawStopData);
    }

    private void processJson(String rawStopData) throws JsonProcessingException {
        JsonNode mapper = new ObjectMapper().readTree(rawStopData).get("Siri").get("ServiceDelivery");
        String responseTimestamp = mapper.get("ResponseTimestamp").asText();
        this.responseDate = ZonedDateTime.parse(responseTimestamp, DateTimeFormatter.ISO_DATE_TIME);
        JsonNode busesNode = mapper.get("StopMonitoringDelivery").findValue("MonitoredStopVisit");
        for (JsonNode busNode : busesNode) {
            Bus bus = new Bus();
            bus.setRecordedAtTimestamp(busNode.findValue("RecordedAtTime").asText());
            JsonNode vehicleJourney = busNode.findValue("MonitoredVehicleJourney");
            String vehicle = vehicleJourney.findValue("VehicleRef").asText();
            bus.setVehicle(vehicle);
            bus.setLine(vehicleJourney.get("PublishedLineName").iterator().next().asText());
            bus.setArrivalProximity(vehicleJourney.findValue("ArrivalProximityText").asText());
            bus.setProgress(vehicleJourney.findValue("ProgressRate").asText());
            if (vehicleJourney.findValue("ExpectedArrivalTime") != null) {
                bus.setArrivalTime(vehicleJourney.findValue("ExpectedArrivalTime").asText());
            }
            if (vehicleJourney.findValue("ProgressStatus") != null) {
                bus.setProgressStatus(vehicleJourney.get("ProgressStatus").toString());
            }
            if (vehicleJourney.findValue("OriginAimedDepartureTime") != null) {
                bus.setTerminalDepartureTimestamp(vehicleJourney.findValue("OriginAimedDepartureTime").asText());
            }
            upcomingBuses.put(bus.getVehicle(), bus);
        }
    }

    @Override
    public String toString() {
        String stop = String.format("Retrieved at %8s(%2s ago). Upcoming %2d buses",
                responseDate.format(ofPattern("HH:mm:ss")), getTimeDiff(responseDate), upcomingBuses.size());
        String buses = upcomingBuses.values().stream().map(Bus::toString).collect(Collectors.joining("\n"));
        return String.format("%s\n%s", stop, buses);
    }
}
