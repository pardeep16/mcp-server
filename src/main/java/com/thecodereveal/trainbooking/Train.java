package com.thecodereveal.trainbooking;

import java.util.Map;

/** Sample data only: not connected to any real booking system. */
public record Train(String number, String name, String from, String to,
                    String departs, Map<String, Integer> seats) {
}
