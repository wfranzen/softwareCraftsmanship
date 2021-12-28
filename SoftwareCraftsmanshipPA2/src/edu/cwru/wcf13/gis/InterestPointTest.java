package edu.cwru.wcf13.gis;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

class InterestPointTest {

    private final Coordinate coordTest = new Coordinate(new BigDecimal("1.0"), BigDecimal.ZERO);
    private final String markerTest = "GYM";

    private final InterestPoint<Object> nullIP = new InterestPoint<>(null, null);
    private final InterestPoint<Object> workingIP = new InterestPoint<>(coordTest, markerTest);


    @Test
    void validate() {

        assertEquals(workingIP, workingIP.validate());
        assertThrows(NullPointerException.class, () -> {
            nullIP.validate();
        });
    }

    @Test
    void testValidate() {

        assertEquals(workingIP, InterestPoint.validate(workingIP));
        assertThrows(NullPointerException.class, () -> {
            InterestPoint.validate(nullIP);
        });
    }

    @Test
    void hasMarker() {

        assertTrue(workingIP.hasMarker(workingIP.marker()));
        assertFalse(workingIP.hasMarker("TEST"));
        assertThrows(NullPointerException.class, () -> {
            nullIP.hasMarker(markerTest);
        });
    }

    @Test
    void testToString() {

        String workingIPMarker = workingIP.marker().toString();
        String stringIPCoord = workingIP.coordinate().toSimpleString();
        String expectedIPString = "The marker, " + workingIPMarker + ", has the coordinates of: " + stringIPCoord;

        assertEquals(expectedIPString, workingIP.toString());
        assertThrows(NullPointerException.class, () -> {
            nullIP.toString();
        });

    }

    @Test
    void coordinate() {

    }

    @Test
    void marker() {
    }
}