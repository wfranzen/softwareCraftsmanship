package edu.cwru.wcf13.gis;

import org.junit.Test;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.sql.Array;
import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;

class BiDimensionalMapTest {

    // Coordinate Class Objects
    private final Coordinate coordTest = new Coordinate(new BigDecimal("1"), BigDecimal.ZERO);
    private final Coordinate coordTest2 = new Coordinate(new BigDecimal("3"), new BigDecimal("4"));
    private final Coordinate coordTest3 = new Coordinate(new BigDecimal("3"), new BigDecimal("4"));
    private final Coordinate nullCoordTest = new Coordinate(new BigDecimal("1"), null);

    // Interest Point Class Objects
    private final Rectangle rect1 = new Rectangle(coordTest, coordTest2);
    private final Rectangle rect2 = new Rectangle(coordTest2, coordTest3);
    private final Rectangle rect3 = new Rectangle(coordTest, coordTest3);

    private final InterestPoint<Object> workingIP = new InterestPoint<>(coordTest, rect1);
    private final InterestPoint<Object> workingIP2 = new InterestPoint<>(coordTest2, rect2);
    private final InterestPoint<Object> workingIP3 = new InterestPoint<>(coordTest3, rect3);

    // BiDimensionalMap Class Objects
    private final BiDimensionalMap outsideTest = new BiDimensionalMap<>(new HashSet<>(), new HashSet<>());
    private final BiDimensionalMap.Updater testUpdater = outsideTest.getUpdater();

    private final BiDimensionalMap<Object>.Updater testmap = new BiDimensionalMap<>(new HashSet<>(), new HashSet<>()).getUpdater();

    // Rectangle Class Objects
    private final Coordinate bottomLeftTest = new Coordinate(new BigDecimal("1"), new BigDecimal("0"));
    private final Coordinate topRightTest = new Coordinate(new BigDecimal("3"), new BigDecimal("2"));
    private final Rectangle testRect = new Rectangle(bottomLeftTest, topRightTest);

    // InterestPoints Class Objects
    private final InterestPoints.Builder builderTest = new InterestPoints.Builder();
    private final InterestPoints intsPointsTest = builderTest.build();

    @Test
    void testGet() {

        Collection firstCol = new HashSet<>();
            firstCol.add(workingIP.marker());

            testUpdater.setCoordinate(workingIP.coordinate());
            testUpdater.addValue(workingIP.marker());
            testUpdater.set();
        assertEquals(firstCol, outsideTest.get(coordTest));




        Collection secondCol = new HashSet<>();
            secondCol.add(workingIP2.marker());

        Collection thirdCol = new HashSet<>();
            thirdCol.add(workingIP3.marker());

            testUpdater.setCoordinate(coordTest2);
            testUpdater.setValues(secondCol);
            testUpdater.set();

            testUpdater.setCoordinate(coordTest3);
            testUpdater.setValues(thirdCol);
            testUpdater.add();

        Collection expectionCol = new HashSet<>();
            expectionCol.add(workingIP2.marker());
            expectionCol.add(workingIP3.marker());

        assertEquals(expectionCol, outsideTest.get(coordTest2));

    }

    @Test
    void updaterSetCoordinate() {

        assertEquals(testmap, testmap.setCoordinate(coordTest));
        assertThrows(NullPointerException.class, () -> {
            testmap.setCoordinate(nullCoordTest);
        });
    }

    @Test
    void updaterSetCollectionFactory() {

        Supplier<Collection<Object>> CFTest = HashSet::new;
        Supplier<Collection<Object>> CFTestNull = null;

        assertEquals(testmap, testmap.setCollectionFactory(CFTest));
        assertThrows(NullPointerException.class, () -> {
            testmap.setCollectionFactory(CFTestNull);
        });
    }

    @Test
    void updaterSetValues() {

        Collection<Object> valuesTest = new ArrayList<Object>();
            valuesTest.add(4);
            valuesTest.add(2);
        Collection<Object> nullValuesTest = null;

        ArrayList<Integer> numList = new ArrayList<>( Arrays.asList(4, 2));

        assertEquals(numList, valuesTest);
        assertEquals(testmap, testmap.setValues(valuesTest));
        assertThrows(NullPointerException.class, () -> {
            testmap.setValues(nullValuesTest);
        });
    }

    @Test
    void updaterAddValues() {

        Collection<Object> nullValuesTest = null;
        assertThrows(NullPointerException.class, () -> {
            testmap.addValue(nullValuesTest);
        });

        Collection<Object> valuesTest = new ArrayList<Object>();
            valuesTest.add(1);
            valuesTest.add(null);

        assertEquals(testmap, testmap.addValue(valuesTest));
    }

    @Test
    void updaterSet() {

        //Returns null from the previously empty map
        Collection<Object> testColl = new ArrayList<>();
            testColl.add(workingIP);
            testColl.add(workingIP2);
        testmap.setValues(testColl);
        assertNull(testmap.set());

        //Returns the previous collection of points
        Collection<Object> newTestColl = new ArrayList<>();
            newTestColl.add(new InterestPoint<Object>(new Coordinate(BigDecimal.ZERO, BigDecimal.ONE), "FIREDEPO"));
            newTestColl.add(new InterestPoint<Object>(new Coordinate(BigDecimal.ONE, BigDecimal.ONE), "UNIVERSITY"));
            newTestColl.add(new InterestPoint<Object>(new Coordinate(BigDecimal.TEN, BigDecimal.ZERO), "CITY"));
        testmap.setValues(newTestColl);
        assertEquals(testColl, testmap.set());
    }

    @Test
    void updaterAdd() {

        Collection<Object> testColl = new ArrayList<>();
            testColl.add(workingIP);
            testColl.add(workingIP2);
        testmap.setValues(testColl);

        //Should return true the first time as the previous map had no values (null)
        assertTrue(testmap.add());
        //Should then return false the second time as the values are identical and therefore nothing is modified
        assertFalse(testmap.add());
    }

    @Test
    void testXandYSet() {


        testUpdater.setCoordinate(workingIP.coordinate());
        testUpdater.addValue(workingIP);
        assertTrue(testUpdater.add());
        testUpdater.setCoordinate(workingIP2.coordinate());
        testUpdater.setValues(Collections.singleton(workingIP2));
        assertTrue(testUpdater.add());
        testUpdater.setCoordinate(workingIP3.coordinate());
        testUpdater.setValues(Collections.singleton(workingIP3));
        assertTrue(testUpdater.add());

        Set<BigDecimal> xCoordSet = new TreeSet<>();
            xCoordSet.add(coordTest.x());
            xCoordSet.add(coordTest2.x());

        assertEquals(xCoordSet, outsideTest.xSet());

        Set<BigDecimal> yCoordSet = new TreeSet<>();
            yCoordSet.add(coordTest.y());
        Set<BigDecimal> yCoordSet2 = new TreeSet<>();
            yCoordSet2.add(coordTest2.y());
            yCoordSet2.add(coordTest3.y());

        assertEquals(yCoordSet, outsideTest.ySet(coordTest.x()));
        assertEquals(yCoordSet2, outsideTest.ySet(coordTest2.x()));
    }

    @Test
    void testCollectionList() {

        Collection<Object> numList = new ArrayList<>( Arrays.asList(coordTest, coordTest2));
        testUpdater.setValues(numList);
        testUpdater.set();

        List<Collection> listOfNumList = new ArrayList<>();
        listOfNumList.add(numList);

        assertEquals(listOfNumList, outsideTest.collectionList());
    }

    @Test
    void testToString() {

        Collection<Object> numList = new ArrayList<>( Arrays.asList(coordTest, coordTest2));
        testUpdater.setValues(numList);
        testUpdater.set();
        Collection<Object> numList2 = new ArrayList<>( Arrays.asList(coordTest2, coordTest));
        testUpdater.setValues(numList2);
        testUpdater.add();

        System.out.println(outsideTest.toString());
    }

    @Test
    void testCollectionSize() {
        testUpdater.setCoordinate(workingIP.coordinate());
        testUpdater.addValue(workingIP);
        assertTrue(testUpdater.add());
        testUpdater.setCoordinate(workingIP2.coordinate());
        testUpdater.setValues(Collections.singleton(workingIP2));
        assertTrue(testUpdater.add());
        testUpdater.setCoordinate(workingIP3.coordinate());
        testUpdater.setValues(Collections.singleton(workingIP3));
        assertTrue(testUpdater.add());

        assertEquals(3, outsideTest.collectionSize());

        // Test collectionSize with a Predicate
        // Predicate testPredicate = new ;
        assertEquals(3, outsideTest.collectionSize());

    }

    @Test
    void testSlice() {

        testUpdater.setCoordinate(workingIP.coordinate());
        testUpdater.addValue(workingIP);
            assertTrue(testUpdater.add());
        testUpdater.setCoordinate(workingIP2.coordinate());
        testUpdater.setValues(Collections.singleton(workingIP2));
            assertTrue(testUpdater.add());
        testUpdater.setCoordinate(workingIP3.coordinate());
        testUpdater.setValues(Collections.singleton(workingIP3));
            assertTrue(testUpdater.add());


        BiDimensionalMap expectedPoints = new BiDimensionalMap<>(new HashSet<>(), new HashSet<>());
        BiDimensionalMap<Object>.Updater expectedPointsUpdater = expectedPoints.getUpdater();
            expectedPointsUpdater.setCoordinate(workingIP.coordinate());
            expectedPointsUpdater.addValue(workingIP);
            expectedPointsUpdater.add();
        Rectangle.validate(testRect);

//        BiDimensionalMap emptyMap = new BiDimensionalMap<>();
//        BiDimensionalMap<Object>.Updater emptyMapUpdater = emptyMap.getUpdater();
//        emptyMapUpdater.add();
        BiDimensionalMap slicedMap = outsideTest.slice(testRect);
        assertEquals(expectedPoints.collectionList(), slicedMap.collectionList());

    }

}