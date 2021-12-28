package edu.cwru.wcf13.gis;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A class representing an area defined by the contained rectangles' areas.
 * This region does not allow for area overlap between the rectangles.
 *
 * @author Will Franzen
 * @version Programming Assignment 13
 */

public final class RectilinearRegion {

    private final Set<Rectangle> rectangles;

    /**
     * Constructor for the RectilinearRegion class, called privately through the static <code>of()</code> method
     *
     * @param rectangles set of rectangles to be added to a RectilinearRegion
     * @see #of(Set)
     */
    private RectilinearRegion(Set<Rectangle> rectangles) {

        this.rectangles = rectangles.stream()
                .map(rectangle -> Rectangle.validate(rectangle))
                .collect(Collectors.toSet());

    }

    /**
     * Getter method for the set of Rectangles contained within the region
     *
     * @return set of Rectangles
     */
    public final Set<Rectangle> getRectangles() {
        return this.rectangles;
    }


    /**
     * Method to initialize a BiDimensionalMap with Rectangle objects inserted into the map at each of their respective
     * area overlaps.
     *
     * @return a new BiDimensionalMap with Rectangle objects mapped to their respective Coordinates on the map
     * @see #coordSet(Function, Function)
     * @see Rectangle
     * @see BiDimensionalMap
     */
    private final BiDimensionalMap<Rectangle> rectangleMap() {

        // Creates a set of all rectangles' values between the bounds left/bottom inclusive, right/top exclusive
        BiDimensionalMap<Rectangle> grid = new BiDimensionalMap<>(
                coordSet(Rectangle::left, Rectangle::right),
                coordSet(Rectangle::bottom, Rectangle::top)
        );

        // Builder object for the BiDimensionalMap
        BiDimensionalMap<Rectangle>.Updater gridUpdater = grid.getUpdater();

        for (Rectangle rect : getRectangles()) {
            // Add every rectangle within the RectilinearRegion as points on the BiDimensionalMap

            BiDimensionalMap<Rectangle> tempGrid = grid.slice(rect);
            tempGrid.addEverywhere(rect);

            // Helper method to add the rectangles as points on the map
            fillRectMap(gridUpdater, rect, tempGrid);

        }
        return grid;
    }

    /**
     * Checks to see if <code>rectangleMap()</code>'s output has overlapping Rectangles
     *
     * @return <code>True</code> if overlapping Rectangles are found
     *         <code>False</code> otherwise
     * @see #of(Set)
     */
    public boolean isOverlapping() {

        boolean overlapping = false;

        for (Coordinate coord : rectangleMap().coordinateSet()) {
            if (rectangleMap().get(coord).size() > 1) {
                // If two Rectangles exist at one Coordinate on the map then overlap has occurred
                overlapping = true;
                break;
            }
        }

        return overlapping;
    }

    /**
     * Static method used to create a RectilinearRegion. The set of rectangles used to create the region must be
     * non-null as well as every rectangle being non-overlapping.
     *
     * @param rectangles set of rectangles used to initialize RectilinearRegion
     * @return a new, non-overlapping RectilinearRegion
     * @exception IllegalArgumentException
     */
    public static final RectilinearRegion of(Set<Rectangle> rectangles) {


        Objects.requireNonNull(rectangles);
        rectangles.forEach(rect -> Rectangle.validate(rect));

        RectilinearRegion newRegion = new RectilinearRegion(rectangles);

        if (newRegion.isOverlapping()) {
            throw new IllegalArgumentException("The given rectilinear region is overlapping.");
        }
        return newRegion;
    }

    /**
     * Getter method for the BiDimensionalMap created by <code>rectangleMap()</code>
     * @return output from the rectangleMap method
     */
    BiDimensionalMap<Rectangle> getRectMap() {
        return rectangleMap();
    }

    /**
     * Returns a set containing all values between the specified lower and upper bounds for a rectangle's sides
     *
     * @param lower function used to calculate lower bound of a rectangle's coordinate
     * @param upper function used to calculate upper bound of a rectangle's coordinate
     * @return a set of all values between a rectangle's upper and lower bound
     * @see #rectangleMap()
     */
    Set<BigDecimal> coordSet(Function<Rectangle, BigDecimal> lower, Function<Rectangle, BigDecimal> upper) {

        return getRectangles().stream()
                .flatMap(rect -> rect.rectBounds(lower.apply(rect), upper.apply(rect)).stream())
                .collect(Collectors.toSet());
    }

    /**
     * Method for filling the BiDimensionalMap with Rectangles as needed in <code>rectangleMap()</code>
     *
     * @param gridUpdater builder for the BiDimensionalGrid
     * @param currentRect rectangle whose points will be added to the BiDimensionalMap
     * @param tempGrid BiDimensionalMap to be added to
     * @see #rectangleMap()
     */
    void fillRectMap(BiDimensionalMap<Rectangle>.Updater gridUpdater, Rectangle currentRect, BiDimensionalMap<Rectangle> tempGrid) {

        for (BigDecimal x : currentRect.rectBounds(currentRect.left(), currentRect.right())) { // Turn into a helper method
            for (BigDecimal y : currentRect.rectBounds(currentRect.bottom(), currentRect.top())) {

                gridUpdater.setCoordinate(new Coordinate(x, y));
                gridUpdater.setValues(tempGrid.get(x, y));
                gridUpdater.add();

            }
        }
    }

    /**
     * Checks a RectilinearRegion for connectivity, returning a Boolean value indicating the presence of connectivity.
     * A region is connected if all contained Rectangle objects directly or transitively share borders with one another
     * without overlapping. However, overlapping is checked during RectilinearRegion initialization and is unnecessary.
     *
     * @return <code>True</code> if the region is connected
     *         <code>False</code> otherwise, or if there are less than two Rectangle
     * @see #connectionPossible(boolean, int)
     */
    Boolean isConnected() {

        if (getRectangles().size() < 2) {
            // Connection cannot occur with less than two rectangles, return immediately in this case

            return false;
        }

        boolean isRegionConnected = false;
        NavigableSet<Rectangle> nonConnectedRects = new TreeSet<>(getRectangles());
        Queue<Rectangle> uncheckedConnections = new ArrayDeque<>(Collections.singleton(nonConnectedRects.pollFirst()));

        while( connectionPossible(isRegionConnected, uncheckedConnections.size()) ) {
            // Continue to check for connections while it is still possible for the region to be connected

            nonConnectedRects.stream().forEach(toBeChecked -> {
                // Parse all rectangles without a connection in an attempt to find one
                if(uncheckedConnections.peek().isTouching( toBeChecked )) {
                    // Move toBeChecked from nonConnectedRects to uncheckedConnections if a connection is found
                    uncheckedConnections.add( toBeChecked );
                    nonConnectedRects.remove( toBeChecked );
                }
            });
            uncheckedConnections.poll();

            if( nonConnectedRects.isEmpty() ) {
                isRegionConnected = true;
            }

        } // while

        return isRegionConnected;
    }

    /**
     * Returns a Boolean value indicating if there are remaining rectangles still to be checked for connections.
     * If all possible rectangles have been checked, then region connectivity should be determined.
     *
     * @param isRegionConnected      status of the region's connectivity
     * @param numPossibleConnections number of connected rectangles
     * @return <code>True</code> if there are still unconnected rectangles left
     * <code>False</code> otherwise, or if all rectangles have been checked
     * @see #isConnected()
     */
    Boolean connectionPossible(boolean isRegionConnected, int numPossibleConnections) {

        // If already connected or no rectangles left to check for connections, return false to end isConnected algo
        return !(isRegionConnected || numPossibleConnections == 0);
    }





}