package info.mta.bustime;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import static java.time.format.DateTimeFormatter.ofPattern;

public class Bus {
    private String line;
    private String arrivalProximity;
    private String progress;
    private String arrivalTime;
    private String terminalDepartureTimestamp;
    private String vehicle;
    private String recordedAtTimestamp;
    private String progressStatus;

    public void setTerminalDepartureTimestamp(String terminalDepartureTimestamp) {
        this.terminalDepartureTimestamp = terminalDepartureTimestamp;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public void setArrivalProximity(String arrivalProximity) {
        this.arrivalProximity = arrivalProximity;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public void setArrivalTime(String arrivalTime) {
        ZonedDateTime arrivalDate = ZonedDateTime.parse(arrivalTime, DateTimeFormatter.ISO_DATE_TIME);
        this.arrivalTime = arrivalDate.format(ofPattern("HH:mm:ss"));
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle.replaceAll(".*_", "");
    }


    public void setRecordedAtTimestamp(String recordedAtTimestamp) {
        ZonedDateTime recordedDate = ZonedDateTime.parse(recordedAtTimestamp, DateTimeFormatter.ISO_DATE_TIME);
        this.recordedAtTimestamp = recordedDate.format(ofPattern("HH:mm:ss"));
    }

    public void setProgressStatus(String progressStatus) {
        this.progressStatus = progressStatus;
    }

    @Override
    public String toString() {
        return String.format("%7s: arrival='%8s', arrivalProximity='%16s', vehicle='%4s', progress='%14s';",
                line, arrivalTime, arrivalProximity, vehicle, progress);
    }
}
