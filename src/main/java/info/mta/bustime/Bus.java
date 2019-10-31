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
    private String terminalDepartureTime;
    private String vehicle;

    public String getTerminalDepartureTime() {
        return terminalDepartureTime;
    }

    public void setTerminalDepartureTime(String terminalDepartureTime) {
        this.terminalDepartureTime = terminalDepartureTime;
    }

    public String getLine() {
        return line;
    }

    public String getArrivalProximity() {
        return arrivalProximity;
    }

    public String getProgress() {
        return progress;
    }

    public String getArrivalTime() {
        return arrivalTime;
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
        this.arrivalTime = arrivalTime;
        ZonedDateTime arrivalDate = ZonedDateTime.parse(arrivalTime, DateTimeFormatter.ISO_DATE_TIME);
        ZonedDateTime now = ZonedDateTime.now();
        String arrivesIn;
        if (ChronoUnit.SECONDS.between(now, arrivalDate) < 60) {
            arrivesIn = ChronoUnit.SECONDS.between(now, arrivalDate) + " seconds";
        } else if (ChronoUnit.MINUTES.between(now, arrivalDate) < 60) {
            arrivesIn = ChronoUnit.MINUTES.between(now, arrivalDate) + " minutes";
        } else {
            arrivesIn = ChronoUnit.HOURS.between(now, arrivalDate) + " hours";
        }
        this.arrivalTime = String.format("%s(%10s)", arrivalDate.format(ofPattern("HH:mm:ss")), arrivesIn);
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle.replaceAll(".*_", "");
    }

    @Override
    public String toString() {
        return String.format("\n\tBus{'%7s', progress='%20s', arrival='%s', terminalDeparture='%s', arrivalProximity='%s', vehicle='%s'}",
                line,
                progress,
                arrivalTime == null ? "Undefined" : arrivalTime,
                terminalDepartureTime == null ? "Undefined" : terminalDepartureTime,
                arrivalProximity,
                vehicle);
    }
}
