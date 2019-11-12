package info.mta.bustime;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static info.mta.bustime.TimeUtils.getTimeDiff;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.stream.Collectors.groupingBy;

public class Stop {
    private String stopId;
    private String stopName;
    private ZonedDateTime responseDate;
    private List<Bus> buses = new ArrayList<>();

    public Stop(String rawStopData) throws JsonProcessingException {
        processJson(rawStopData);
    }

    private void processJson(String rawStopData) throws JsonProcessingException {
        JsonNode mapper = new ObjectMapper().readTree(rawStopData).get("Siri").get("ServiceDelivery");
        String responseTimestamp = mapper.get("ResponseTimestamp").asText();
        this.responseDate = ZonedDateTime.parse(responseTimestamp, DateTimeFormatter.ISO_DATE_TIME);
        JsonNode busesNode = mapper.get("StopMonitoringDelivery").findValue("MonitoredStopVisit");
        Map<String, Bus> upcomingBuses = new HashMap<>();
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
                bus.setArrivalTimestamp(vehicleJourney.findValue("ExpectedArrivalTime").asText());
            }
            if (vehicleJourney.findValue("ProgressStatus") != null) {
                String progresses = vehicleJourney.get("ProgressStatus").iterator().next().asText();
                List<String> progressesList = Arrays.stream(progresses.split(",")).collect(Collectors.toList());
                bus.setProgressStatus(progressesList);
            }
            if (vehicleJourney.findValue("OriginAimedDepartureTime") != null) {
                bus.setTerminalDepartureTimestamp(vehicleJourney.findValue("OriginAimedDepartureTime").asText());
            }
            upcomingBuses.put(bus.getVehicle(), bus);
        }
        buses.addAll(upcomingBuses.values());
    }

    public String getStopMetaData() {
        StringBuilder metaData = new StringBuilder();
        String generalInfo = String.format("Retrieved at %8s(%2s ago).",
                responseDate.format(ofPattern("HH:mm:ss")), getTimeDiff(responseDate));
        metaData.append(generalInfo);
        List<Bus> layover = getBusesOnLayover();
        List<Bus> onLastTrip = getBusesFinishingLastTrip();
        List<Bus> onTheWay = getBusesOnTheWay();
        List<Bus> onTheWayFiltered = getBuses("BM3");
        List<Bus> onTheWayFiltered_0 = getBuses("B44");
        List<Bus> onTheWayFiltered_1 = getBuses("B44-SBS");
        Map<String, Integer> buses = this.buses
                .stream()
                .map(Bus::getLine)
                .collect(groupingBy(String::valueOf))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, busesAmount -> busesAmount.getValue().size()));
        String busesInfo = String.format("%2d Upcoming buses:", this.buses.size());
        return metaData.toString();
    }


    public List<Bus> getBuses(String ... busLane) {
        List lanes = Arrays.asList(busLane);
        return buses.stream().filter(bus -> lanes.contains(bus.getLine())).collect(Collectors.toList());
    }


    public List<Bus> getBusesOnTheWay() {
        return buses.stream().filter(Bus::isOnTheWay).collect(Collectors.toList());
    }

    public List<Bus> getBusesFinishingLastTrip() {
        return buses.stream().filter(Bus::isOnPreviousTrip).collect(Collectors.toList());
    }

    public List<Bus> getBusesOnLayover() {
        return buses.stream().filter(Bus::isOnLayover).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        String stop = getStopMetaData();
        String buses = this.buses.stream().map(Bus::toString).collect(Collectors.joining("\n"));
        return String.format("%s\n%s", stop, buses);
    }
}
