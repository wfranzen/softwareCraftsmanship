package edu.cwru.wcf13.gis;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.*;

class InterestPointsTest {

    private final Coordinate coordTest = new Coordinate(new BigDecimal("1"), BigDecimal.ZERO);
    private final Coordinate coordTest2 = new Coordinate(new BigDecimal("3"), new BigDecimal("2"));
    private final InterestPoint<Object> workingIP = new InterestPoint<>(coordTest, "COORDTEST1");
    private final InterestPoint<Object> workingIP2 = new InterestPoint<>(coordTest2, "COORDTEST2");
    private final InterestPoint<Object> nullIP = new InterestPoint<>(coordTest, null);


    private final InterestPoints.Builder builderTest = new InterestPoints.Builder();
    private final InterestPoints intsPointsClass = builderTest.build();


    @Test
    void get() {

        builderTest.add(workingIP);
        builderTest.add(workingIP2);
        Collection<InterestPoint> coordCollection = new HashSet<>();
            coordCollection.add(workingIP);

        assertEquals(coordCollection, intsPointsClass.get(coordTest));
    }

    @Test
    void interestPoints() {

        builderTest.add(workingIP);
        builderTest.add(workingIP2);


    }

    @Test
    void testToString() {

        builderTest.add(workingIP);
        builderTest.add(workingIP2);



        String test = intsPointsClass.toString();
        System.out.println(test);
    }

    @Test
    void builderAdd() {

        assertTrue(builderTest.add(workingIP));
        assertTrue(builderTest.add(workingIP2));
        assertFalse(builderTest.add(workingIP));

        assertThrows(NullPointerException.class, () -> {
            builderTest.add(nullIP);
        });

    }
}