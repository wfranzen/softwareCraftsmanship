package edu.cwru.wcf13.gis;

import java.util.*;
import java.util.function.Predicate;

/**
 * A collection of interest points described in the form of a BiDimensionalMap
 *
 * @author Will Franzen
 * @version Programming Assignment 13
 * @param <M> denotes general characteristics of a point of interest
 */

public final class InterestPoints<M> {

    /**
     * A BiDimensionalMap containing all points of interest to be manipulated
     *
     * @see BiDimensionalMap
     */
    private final BiDimensionalMap<InterestPoint> points;

    /**
     * A private constructor to be accessed by the inner Builder class
     *
     * @param builder Builder object used to manipulate this
     */
    private InterestPoints(Builder builder) {

        this.points = builder.points;
    }

    /**
     * Returns the interest points at the given valid coordinate
     *
     * @param coordinate location at which interest points are to be accessed
     * @return collection of interest points at the given coordinate
     */
    public final Collection<InterestPoint> get(Coordinate coordinate) {

        Coordinate.validate(coordinate);
        return points.get(coordinate);
    }

    /**
     * Returns the list of interest points ordered by their coordinates
     *
     * @return list of interest points ordered by their coordinates
     */
    public final List<Collection<InterestPoint>> interestPoints() {

        return points.coordinateSet().stream()
                .map( points::get )
                .toList();
    }

    public final String toString() {
        // Delegates the toString method to BiDimensionalMap
        return points.toString();
    }

    /**
     * Returns the count of interest points met by a given predicate contained within the given region
     *
     * @param region region in which the desired interest points must be contained
     * @param marker predicate describing the value of the desired predicate
     * @return number of interest points meeting the given predicate within the region
     */
    public final long count(RectilinearRegion region, M marker) {

        Objects.requireNonNull(marker);
        Objects.requireNonNull(region);

        return region
                .getRectMap()
                .collectionSize(
                        Predicate.isEqual(marker)
                );

        // Use the marker as a predicate and check the values of region against interestPoints ?
        /*return points.collectionSize(
                Predicate.isEqual(region.getRectMap().collectionList().stream()
                        .map(HashSet::new)
                        .collect(Collectors.toSet())));*/

    }


    public static class Builder {

        private final BiDimensionalMap<InterestPoint> points = new BiDimensionalMap<>(new HashSet<>(), new HashSet<>());

        /**
         * Adds a valid interest point to the map
         *
         * @param interestPoint interest point to be added to the map
         * @return whether the previously associated interest point at the location changed
         */
        public final boolean add(InterestPoint interestPoint) {

            InterestPoint.validate(interestPoint);
            return points
                    .getUpdater()
                    .setCoordinate(interestPoint.coordinate())
                    .addValue(interestPoint)
                    .add();
        }

        /**
         * Returns a new InterestPoints object
         *
         * @return InterestPoints object
         */
        public final InterestPoints build() {

            return new InterestPoints(this);
        }

    } // End of Builder()

} // End of InterestPoints()
