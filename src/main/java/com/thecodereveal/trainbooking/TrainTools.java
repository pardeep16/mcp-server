package com.thecodereveal.trainbooking;

import java.util.List;
import java.util.Map;

import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;

@Component
public class TrainTools {

    // Sample in-memory data for the demo
    private final List<Train> trains = List.of(
            new Train("12777", "Sunrise Express", "NDLS", "BCT", "06:00",
                    Map.of("SL", 0, "3A", 12, "2A", 3)),
            new Train("22222", "CodeReveal Rajdhani", "NDLS", "BCT", "16:55",
                    Map.of("3A", 0, "2A", 5, "1A", 1)),
            new Train("12345", "Demo Mail", "NDLS", "HWH", "16:50",
                    Map.of("SL", 41, "3A", 7)));

    @McpTool(name = "search_trains",
             description = "Find trains between two station codes",
             annotations = @McpTool.McpAnnotations(readOnlyHint = true))
    public List<Train> searchTrains(
            @McpToolParam(description = "Source station code, e.g. NDLS") String from,
            @McpToolParam(description = "Destination station code, e.g. BCT") String to) {
        return trains.stream()
                .filter(t -> t.from().equalsIgnoreCase(from) && t.to().equalsIgnoreCase(to))
                .toList();
    }

    @McpTool(name = "check_seats",
             description = "Check how many seats are left for a train and class",
             annotations = @McpTool.McpAnnotations(readOnlyHint = true))
    public String checkSeats(
            @McpToolParam(description = "Train number, e.g. 12777") String trainNo,
            @McpToolParam(description = "Class code: SL, 3A, 2A or 1A") String travelClass) {
        return trains.stream()
                .filter(t -> t.number().equals(trainNo))
                .findFirst()
                .map(t -> {
                    String cls = travelClass.toUpperCase();
                    Integer left = t.seats().get(cls);
                    if (left == null) return t.name() + " has no " + cls + " class.";
                    return left > 0
                            ? left + " seats left in " + cls + " on " + t.name()
                            : "No seats left in " + cls + " on " + t.name() + " (waitlist)";
                })
                .orElse("Train " + trainNo + " not found.");
    }
}
