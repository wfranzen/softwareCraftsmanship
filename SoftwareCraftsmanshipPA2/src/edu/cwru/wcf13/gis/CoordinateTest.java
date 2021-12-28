package edu.cwru.wcf13.gis;

import org.junit.Test;
import static org.junit.Assert.*;

import java.math.BigDecimal;

class CoordinateTest {

    private final Coordinate coordTest = new Coordinate(new BigDecimal("1.0"), BigDecimal.ZERO);
    private final Coordinate nullCoordinate = null;

    private final Coordinate biggerCoord = new Coordinate(new BigDecimal("2.0"), new BigDecimal("3.0"));
    private final Coordinate smallerCoord = new Coordinate(new BigDecimal("1.0"), new BigDecimal("3.0"));


    @Test
    void validate() {

        assertEquals(coordTest, coordTest);
        assertThrows(NullPointerException.class, nullCoordinate::validate);

    }

    @Test
    void testValidate() {

        assertEquals(coordTest, coordTest);
        assertThrows(NullPointerException.class, () -> {
            Coordinate.validate(nullCoordinate);
        });
    }

    @Test
    void toSimpleString() {

        String coordXString = coordTest.x().toString();
        String coordYString = coordTest.y().toString();
        assertEquals("(x,y) of this coordinate is: (" + coordXString + "," + coordYString + ").", coordTest.toSimpleString());
    }

    @Test
    void compareTo() {

        int compareCoords = smallerCoord.compareTo(biggerCoord);
        assertEquals(0, compareCoords);
    }

    @Test
    void x() {
        assertEquals(new BigDecimal("1.0"), coordTest.x());
    }

    @Test
    void y() {
        assertEquals(new BigDecimal("1.0"), coordTest.y());
    }
}