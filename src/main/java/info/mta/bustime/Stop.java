package info.mta.bustime;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static java.time.format.DateTimeFormatter.ofPattern;

public class Stop {
    private ZonedDateTime responseDate;

    private Map<String, Bus> upcomingVehicles = new HashMap<>();

    public Stop(String rawStopData) throws JsonProcessingException {
        processJson(rawStopData);
    }

    private void processJson(String rawStopData) throws JsonProcessingException {
        JsonNode mapper = new ObjectMapper().readTree(rawStopData).get("Siri").get("ServiceDelivery");
        String responseTimestamp = mapper.get("ResponseTimestamp").asText();
        responseDate = ZonedDateTime.parse(responseTimestamp, DateTimeFormatter.ISO_DATE_TIME);
        JsonNode busesNode = mapper.get("StopMonitoringDelivery").findValue("MonitoredStopVisit");
        for (JsonNode busNode : busesNode) {
            JsonNode vehicleJourney = busNode.findValue("MonitoredVehicleJourney");
            Bus bus = new Bus();
            String vehicle = vehicleJourney.findValue("VehicleRef").asText();
            bus.setVehicle(vehicle);
            bus.setLine(vehicleJourney.get("PublishedLineName").iterator().next().asText());
            bus.setArrivalProximity(vehicleJourney.findValue("ArrivalProximityText").asText());
            bus.setProgress(vehicleJourney.findValue("ProgressRate").asText());
            if (vehicleJourney.findValue("ExpectedArrivalTime") != null) {
                bus.setArrivalTime(vehicleJourney.findValue("ExpectedArrivalTime").asText());
            }
            if (vehicleJourney.findValue("OriginAimedDepartureTime") != null) {
                bus.setTerminalDepartureTime(vehicleJourney.findValue("OriginAimedDepartureTime").asText());
            }
            upcomingVehicles.put(bus.getVehicle(), bus);
        }
    }

    @Override
    public String toString() {

        return "Stop{responseDate=" + responseDate.format(ofPattern("HH:mm:ss")) +
                ", upcomingVehicles=" + upcomingVehicles.values() +
                '}';
    }
}
