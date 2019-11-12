package info.mta.bustime;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.time.format.DateTimeFormatter.ofPattern;

public class Bus {
    public String getVehicle() {
        return vehicle;
    }

    private String line;
    private String arrivalProximity;
    private String progress;
    private String arrivalTimestamp;
    private String terminalDepartureTimestamp;
    private String recordedAtTimestamp;
    private ZonedDateTime terminalDepartureDate;
    private ZonedDateTime arrivalDate;
    private ZonedDateTime recordedDate;
    private String vehicle;
    private boolean isOnLayover;
    private boolean isOnPreviousTrip;
    private boolean isOnTheWay = true;

    public void setTerminalDepartureTimestamp(String terminalDepartureTimestamp) {
        this.terminalDepartureDate = ZonedDateTime.parse(terminalDepartureTimestamp, DateTimeFormatter.ISO_DATE_TIME);
        this.terminalDepartureTimestamp = terminalDepartureDate.format(ofPattern("HH:mm:ss"));
    }

    public String getLine() {
        return line;
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

    public void setArrivalTimestamp(String arrivalTimestamp) {
        arrivalDate = ZonedDateTime.parse(arrivalTimestamp, DateTimeFormatter.ISO_DATE_TIME);
        this.arrivalTimestamp = arrivalDate.format(ofPattern("HH:mm:ss"));
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle.replaceAll(".*_", "");
    }


    public void setRecordedAtTimestamp(String recordedAtTimestamp) {
        recordedDate = ZonedDateTime.parse(recordedAtTimestamp, DateTimeFormatter.ISO_DATE_TIME);
        this.recordedAtTimestamp = recordedDate.format(ofPattern("HH:mm:ss"));
    }

    public void setProgressStatus(List<String> progressStatus) {
        isOnLayover = progressStatus.contains("layover");
        isOnPreviousTrip = !isOnLayover && progressStatus.contains("prevTrip");
        isOnTheWay = !isOnLayover && !isOnPreviousTrip;
    }

    public boolean isOnLayover() {
        return isOnLayover;
    }

    public boolean isOnPreviousTrip() {
        return isOnPreviousTrip;
    }

    public boolean isOnTheWay() {
        return isOnTheWay;
    }

    @Override
    public String toString() {
        return String.format("%7s, arrivalProximity=%15s, progress=%14s, arrivalTimestamp=%8s, terminalDepartureTimestamp=%8s, vehicle=%4s, recordedAtTimestamp=%8s, isOnLayover=%5s, isOnPreviousTrip=%5s, isOnTheWay=%5s;",
                line, arrivalProximity, progress, arrivalTimestamp, terminalDepartureTimestamp, vehicle, recordedAtTimestamp, isOnLayover, isOnPreviousTrip, isOnTheWay);
    }
}
