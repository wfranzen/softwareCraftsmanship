package edu.cwru.wcf13.gis;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * A class representing a two-dimensional mapping with collections of some object T mapped to Coordinate key pairings
 *
 * @author Will Franzen
 * @version Programming Assignment 13
 */

public final class BiDimensionalMap<T> {

    /**
     * A sorted map representation of BiDimensionalMap used to manipulate and alter the points contained within
     */
    private final SortedMap<BigDecimal, SortedMap<BigDecimal, Collection<T>>> points = new TreeMap<>();

    /**
     * Constructs a BiDimensionalMap object from non-null collections of x and y BigDecimal values which are
     * individually paired together as Coordinate objects
     *
     * @param xCoord a collection of x coordinates present in the map
     * @param yCoord a collection of y coordinates present in the map
     */
    BiDimensionalMap(Collection<BigDecimal> xCoord, Collection<BigDecimal> yCoord) {

        Objects.requireNonNull(xCoord);
        Objects.requireNonNull(yCoord);

        Updater constructorUpdater = this.getUpdater();

        for (BigDecimal x : xCoord) {
            Objects.requireNonNull(x);

            for (BigDecimal y : yCoord) {
                Objects.requireNonNull(y);

                constructorUpdater.setCoordinate(new Coordinate(x, y));
                constructorUpdater.add();
            }
        }
    }

    /**
     * Adds an object of type T to all valid Coordinates in <code>points</code>
     *
     * @param value a desired object of type T to be added to <code>points</code>
     */
    public final void addEverywhere(T value) {

        Objects.requireNonNull(value);
        Updater addEvery = this.getUpdater();
        addEvery.addValue(value);

        for (Coordinate coord : coordinateSet()) {
            // Update the accessed coordinate in points for all Coordinates and add the desired value to it
            addEvery.setCoordinate(coord);
            addEvery.add();
        }
    }

    /**
     * Return the collection of objects located at an x,y coordinate pair inside <code>points</code>.
     * Additionally, validates the existence of y mapped to x
     *
     * @param x specified x coordinate in the map
     * @param y specified y coordinate in the map
     * @return validated collection of objects at coordinate pair
     * @see #get(Coordinate)
     */
    public final Collection<T> get(BigDecimal x, BigDecimal y) {

        Objects.requireNonNull(x);
        Objects.requireNonNull(y);

        if (Objects.nonNull(points.get(x))) {
            return points.get(x).get(y);
        }

        return null;
    }

    /**
     * Return the collection of objects located at the given coordinate inside <code>points</code>
     *
     * @param coordinate a specified Coordinate in the map
     * @return validated collection of objects at coordinate pair
     * @see #get(BigDecimal, BigDecimal)
     */
    public final Collection<T> get(Coordinate coordinate) {

        Coordinate.validate(coordinate);
        return get(coordinate.x(), coordinate.y());
    }

    /**
     * Returns all x values contained within <code>points</code>
     *
     * @return set of x values
     */
    public final Set<BigDecimal> xSet() {

        return points.keySet();
    }

    /**
     * Returns all y values with non-null x keys contained within <code>points</code>
     *
     * @return set of y values if x non-null
     *         empty sorted map otherwise
     */
    public final Set<BigDecimal> ySet(BigDecimal x) {

        Objects.requireNonNull(x);
        return points.getOrDefault(x, Collections.emptySortedMap()).keySet();
    }

    /**
     * Generates and returns a list of Coordinates for all x and y mappings within <code>points</code>
     *
     * @return list of Coordinates in <code>points</code>
     */
    public final List<Coordinate> coordinateSet() {

        List<Coordinate> coordSet = new LinkedList<>();

        xSet().forEach(xCoord -> ySet(xCoord).stream()
                        .map(yCoord -> new Coordinate(xCoord, yCoord))
                        .forEach(coordSet::add)
        );

        return coordSet;
    }

    /**
     * Parses all Coordinates located in <code>points</code> and returns a list of all objects mapped to the coordinates
     *
     * @return list of Interest Points in <code>points</code>
     */
    public final List<Collection<T>> collectionList() {

        return coordinateSet().stream()
                .map(coords -> get(coords))
                .collect(Collectors.toList());

    }

    /**
     * Returns the total number of objects of every collection contained in <code>points</code>
     *
     * @return total sum of all collection's sizes in <code>points</code>
     * @see #collectionSize(Predicate)
     */
    public final long collectionSize() {

        return collectionList().stream()
                .mapToLong(Collection::size)
                .sum();
    }

    /**
     * Returns the total number of objects contained in all collections within <code>points</code> according
     * to some defined predicate
     *
     * @param filter
     * @return total sum of all collection's sizes in <code>points</code>
     * @see #collectionSize()
     */
    public final long collectionSize(Predicate<? super T> filter) {

        Objects.requireNonNull(filter);

        return collectionList().stream()
                .flatMap(Collection::stream)
                .filter(filter)
                .count();
    }

    public final String toString() {

        StringBuilder mapRep = new StringBuilder();
        for (Coordinate coordString : coordinateSet()) {
                mapRep.append("Marker(s): ").append(get(coordString)).append(" at ").append(coordString).append("\n");
        }


        return mapRep.toString();
    }

    /**
     * Returns a new two-dimensional map containing only the points in the given valid rectangle.
     * In the slice, coordinates along the bottom and left borders are included, but coordinates along the
     * top and right border are not.
     *
     * @param rectangle the rectangular area used to slice the map
     * @return a new BiDimensionalMap only containing points from the given Rectangle
     */
    public final BiDimensionalMap<T> slice(Rectangle rectangle) {

        Rectangle.validate(rectangle);

        BiDimensionalMap<T> rectBounds = new BiDimensionalMap<>(new HashSet<>(), new HashSet<>());
        Updater rectUpdater = rectBounds.getUpdater();

        for (Coordinate coordinate : coordinateSet()) {

            // If the currently accessed Coordinate is not within the Rectangle's area check the next one
            // ... otherwise, add to the new BiDimensionalMap to be returned
            if (coordinate.compareTo(rectangle.bottomLeft()) < 0)
                continue;
            if (coordinate.compareTo(rectangle.topRight()) > -1)
                continue;
            rectUpdater.setCoordinate(coordinate);
            rectUpdater.setValues(get(coordinate));
            rectUpdater.add();
        }
        return rectBounds;

    }

    /**
     * Returns a builder object for manipulation of the BiDimensionalMap
     *
     * @return builder object for BiDimensionalMap
     */
    public final Updater getUpdater() {

        return new Updater();
    }

    /**
     * An inner, Builder class for BiDimensionalMap to allow for variability
     */
    public final class Updater {


        private BigDecimal x = new BigDecimal("0"); // Default x coordinate value
        private BigDecimal y = new BigDecimal("0"); // Default y coordinate value

        // Supplies the initial instance of the collection stored at the (x, y) coordinates
        private Supplier<Collection<T>> collectionFactory = HashSet::new;
        private Collection<T> values = collectionFactory.get();

        /**
         * Setter method to update the current x location
         *
         * @param x value of <code>x</code> coordinate to be updated to
         * @return this Updater
         */
        public final Updater setX(BigDecimal x) {

            this.x = x;
            return this;
        }

        /**
         * Setter method to update the current y location
         *
         * @param y value of <code>y</code> coordinate to be updated to
         * @return this Updater
         */
        public final Updater setY(BigDecimal y) {

            this.y = y;
            return this;
        }

        /**
         * Setter method for the <code>collectionFactory</code>> Supplier object
         *
         * @param collectionFactory instance of collection at x,y coordinates to be updated to
         * @return this Updater
         */
        public final Updater setCollectionFactory(Supplier<Collection<T>> collectionFactory) {

            Objects.requireNonNull(collectionFactory);
            this.collectionFactory = collectionFactory;
            return this;
        }

        /**
         * Setter method for the <code>values</code> collections of objects
         *
         * @param values collection of values to later be used in Updater usage
         * @return this Updater
         */
        public final Updater setValues(Collection<T> values) {

            Objects.requireNonNull(values);
            this.values = values;
            return this;
        }

        /**
         * Sets the x and y values to those of a valid Coordinate
         *
         * @param coordinate coordinate containing the x and y values to be updated
         * @return this Updater
         */
        public final Updater setCoordinate(Coordinate coordinate) {

            Coordinate.validate(coordinate);

            this.x = coordinate.x();
            this.y = coordinate.y();
            return this;
        }

        /**
         * Adds a single value to the Updater’s values and returns this Updater
         *
         * @param value Object to be added to values for manipulation
         * @return this Updater
         */
        public final Updater addValue(T value) {

            Objects.requireNonNull(value);
            this.values.add(value);
            return this;
        }

        /**
         * Replaces information at the x,y location with <code>values</code>
         *
         * @return previous collection at x,y location if any
         *         null, otherwise
         */
        public final Collection<T> set() {

            this.xNullCheck();
            // Sets the previously specified coordinates to the values collection
            // Additionally, returns the old values if any (or null)
            return points.get(x).put(y, this.values);
        }

        /**
         * Adds all <code>values</code> to x,y location of BiDimensionalMap. If there is no interest point at that
         * location of BiDimensionalMap, a new <code>collectionFactory</code> is created to allow value copying
         *
         * @return whether the previously associated interest points at the location changed
         */
        public final boolean add() {

            this.xNullCheck();
            // Adds the values collection to the previously specified coordinates
            // If the values are modified points in the process, return true. Otherwise, return false
            if (points.get(x).get(y) == null) {
                points.get(x).put(y, collectionFactory.get());
            }
            return points.get(x).get(y).addAll(this.values);
        }

        /**
         * If the specified x in map is null, create a new tree map at that location. Otherwise, if the specified y
         * in map is null, create a new collection
         */
        void xNullCheck() {

            points.computeIfAbsent(x, x -> new TreeMap<>());
        }

        /**
         * Replaces information at the x,y location with a single value
         *
         * @param value value to replace information at x,y location
         */
        void setSingleValue(T value) {
            Collection<T> soleValue = new HashSet<>();
            soleValue.add(value);

            this.values = soleValue;
        }
    } // Updater


}