package com.thecodereveal.trainbooking;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Plain unit tests for the two MCP tools (no Spring context needed). */
class TrainToolsTest {

    private final TrainTools tools = new TrainTools();

    @Test
    void searchTrainsFindsTrainsBetweenStations() {
        var trains = tools.searchTrains("ndls", "BCT");
        assertEquals(2, trains.size());
        assertTrue(trains.stream().anyMatch(t -> t.number().equals("12777")));
    }

    @Test
    void checkSeatsReturnsSeatsLeft() {
        assertEquals("12 seats left in 3A on Sunrise Express", tools.checkSeats("12777", "3a"));
    }

    @Test
    void checkSeatsHandlesWaitlistAndUnknowns() {
        assertTrue(tools.checkSeats("12777", "SL").contains("waitlist"));
        assertEquals("Sunrise Express has no 1A class.", tools.checkSeats("12777", "1A"));
        assertEquals("Train 99999 not found.", tools.checkSeats("99999", "3A"));
    }
}
