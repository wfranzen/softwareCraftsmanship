package edu.cwru.wcf13.gis;

import org.junit.*;
import static org.junit.Assert.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class RectilinearRegionTest {

    private final Coordinate coordTest = new Coordinate(BigDecimal.ZERO, BigDecimal.ONE);
    private final Coordinate coordTest2 = new Coordinate(new BigDecimal("1"), new BigDecimal("2"));
    private final Coordinate coordTest3 = new Coordinate(BigDecimal.ONE, BigDecimal.ZERO);
    private final Coordinate coordTest4 = new Coordinate(new BigDecimal("2"), new BigDecimal("1"));
    private final Coordinate coordTest5 = new Coordinate(new BigDecimal("2"), new BigDecimal("0"));
    private final Coordinate coordTest6 = new Coordinate(new BigDecimal("4"), new BigDecimal("5"));
    private final Rectangle rectTest = new Rectangle(coordTest, coordTest2);
    private final Rectangle rectTest2 = new Rectangle(coordTest3, coordTest4);
    private final Rectangle rectTest3 = new Rectangle(coordTest5, coordTest6);
    private final Set<Rectangle> rectSet = new HashSet<>();

    private final RectilinearRegion regionTest = RectilinearRegion.of(getRectSet());
    private Set<Rectangle> getRectSet() {
        rectSet.add(rectTest);
        rectSet.add(rectTest2);
        rectSet.add(rectTest3);
        return rectSet;
    }

    @Test
    public void connectionPossibleCase1() {
        // Region is not connected and there are connections left
        boolean regionConnected = false;
        int numConnections = 1;

        assertTrue(regionTest.connectionPossible(regionConnected, numConnections));

    }

    @Test
    public void connectionPossibleCase2() {
        // Region is not connected and no connections left to check
        boolean regionConnected = false;
        int numConnections = 0;

        assertFalse(regionTest.connectionPossible(regionConnected, numConnections));

    }

    @Test
    public void connectionPossibleCase3() {
        // Region is connected, number of connections will not matter due to AND statement
        boolean regionConnected = true;
        int numConnections = 0;

        assertFalse(regionTest.connectionPossible(regionConnected, numConnections));
    }

    @Test
    public void stressTestingLow() {

        Set<Rectangle> stressRects = new HashSet<>();
        int numRects = 10;

        for( int i = 0; i < numRects; i++) {

            stressRects.add( rectGenerator() );
        }

        RectilinearRegion stressRegion = RectilinearRegion.of( stressRects );

        assertFalse(stressRegion.isOverlapping());
    }

    @Test
    public void stressTestingHigh() {

        Set<Rectangle> stressRects = new HashSet<>();
        int numRects = 100;

        for( int i = 0; i < numRects; i++) {

            stressRects.add( rectGenerator() );
        }

        RectilinearRegion stressRegion = RectilinearRegion.of( stressRects );

        stressRegion.isOverlapping();
    }

    private Rectangle rectGenerator() {

        double spread = Math.floor( Math.random() * 1000);
        double xMax = spread;
        double yMax = spread;


        Coordinate xCoord = new Coordinate(BigDecimal.valueOf((int)( Math.random()*(xMax-(xMax-10)+1)+(xMax-10))),
                BigDecimal.valueOf((int)(Math.random()*(yMax-(yMax-10)+1)+(yMax-10))));

        Coordinate yCoord = new Coordinate(BigDecimal.valueOf((int)( Math.random()*(xMax+10-(xMax)+1)+(xMax))),
                BigDecimal.valueOf((int)(Math.random()*(yMax+10-(yMax)+1)+(yMax))));

        return new Rectangle(xCoord, yCoord);
    }

}

    // LEGACY TESTING


    /*
    @Test
    public void getRectangles() {

    }


    @Test
    public void isOverlapping() {

        assertFalse(regionTest.isOverlapping());
    }

    @Test
    public void of() {
    }

    @Test
    public void rectangleMap_And_intPointsCount() {

        BiDimensionalMap<Rectangle> testingMap = regionTest.getRectMap();
        System.out.println(testingMap);

        final InterestPoints.Builder builderTest = new InterestPoints.Builder();
        final InterestPoints intsPointsClass = builderTest.build();

        long numCount = intsPointsClass.count(regionTest, rectTest3);
        System.out.println(numCount);

        //        for( Object coord : testingMap.coordinateSet()) {
//            System.out.println(coord);
//        }
//        for( Object thing : testingMap.collectionList()) {
//            System.out.println(thing);
//        }

    }

    @Test
    public void getCoord() {

        Function<Rectangle, BigDecimal> left = Rectangle::left;
        Function<Rectangle, BigDecimal> right = Rectangle::right;
        Function<Rectangle, BigDecimal> bottom = Rectangle::bottom;
        Function<Rectangle, BigDecimal> top = Rectangle::top;

        Set<BigDecimal> yRegionSet = regionTest.coordSet(bottom, top);
        Set<BigDecimal> expectedYSet = new HashSet<>();
            expectedYSet.add(BigDecimal.ZERO);
            expectedYSet.add(BigDecimal.ONE);
            expectedYSet.add(new BigDecimal("2"));
            expectedYSet.add(new BigDecimal("3"));
            expectedYSet.add(new BigDecimal("4"));
        assertEquals(expectedYSet, yRegionSet);

        Set<BigDecimal> xRegionSet = regionTest.coordSet(left, right);
        Set<BigDecimal> expectedXSet = new HashSet<>();
            expectedXSet.add(BigDecimal.ZERO);
            expectedXSet.add(BigDecimal.ONE);
            expectedXSet.add(new BigDecimal("2"));
            expectedXSet.add(new BigDecimal("3"));
        assertEquals(expectedXSet, xRegionSet);
    }
}
*/


/*
// OVERLAPPING RECTANGLES
private final Coordinate coordOLTest = new Coordinate(BigDecimal.ONE, BigDecimal.ZERO);
private final Coordinate coordOLTest2 = new Coordinate(new BigDecimal("2"), new BigDecimal("3"));
private final Coordinate coordOLTest3 = new Coordinate(BigDecimal.TEN, BigDecimal.TEN); // ZERO, ONE
private final Coordinate coordOLTest4 = new Coordinate(new BigDecimal("11"), new BigDecimal("11")); // THREE, TWO
private final Rectangle rectOLTest = new Rectangle(coordOLTest, coordOLTest2);
private final Rectangle rectOLTest2 = new Rectangle(coordOLTest3, coordOLTest4);
private final Set<Rectangle> rectOLSet = new HashSet<>();

private final RectilinearRegion regionOLTest = RectilinearRegion.of(getOLRectSet());

private Set<Rectangle> getOLRectSet() {
        rectOLSet.add(rectOLTest);
        rectOLSet.add(rectOLTest2);
        return rectOLSet;*/
