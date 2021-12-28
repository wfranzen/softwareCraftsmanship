package edu.cwru.wcf13.gis;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * An interest point is a landmark whose information is recorded in the GIS
 *
 * @author Will Franzen
 * @version Programming Assignment 13
 * @param <M> denotes general characteristics of a point of interest
 */
public record InterestPoint<M>(Coordinate coordinate, M marker) {

    /**
     * Throws a NullPointerException if either the marker or coordinate is null.
     * Otherwise, returns the calling InterestPoint
     *
     * @return this InterestPoint
     */
    public final InterestPoint<M> validate() {

        Objects.requireNonNull(marker());
        Coordinate.validate(coordinate());
        return this;
    }

    /**
     * Throws a NullPointerException if the interestPoint is null
     * Otherwise, returns the calling InterestPoint
     *
     * @param interestPoint interestPoint to be validated
     * @return this InterestPoint
     */
    public static final InterestPoint validate(InterestPoint interestPoint) {

        Objects.requireNonNull(interestPoint);
        return interestPoint.validate();
    }

    /**
     * Returns whether the coordinate has a marker that equals the argument.
     *
     * @param marker marker to be checked against coordinate's
     * @return if the marker at the coordinate equaled that of the argument
     */
    public boolean hasMarker(M marker) {

        Objects.requireNonNull(marker());
        return marker().equals(marker);
    }

    public String toString() {

        //Return compact and informative string of POI
        return ("Interest Point at " + coordinate().toSimpleString() +  " has the marker: " + marker().toString());
    }

    /**
     * Validates and returns the x location of the calling InterestPoint
     *
     * @return x location of this InterestPoint
     */
    public final BigDecimal x() {

        return InterestPoint
                .validate(this)
                .coordinate()
                .x();
    }

    /**
     * Validates and returns the y location of the calling InterestPoint
     *
     * @return y location of this InterestPoint
     */
    public final BigDecimal y() {

        return InterestPoint
                .validate(this)
                .coordinate()
                .y();
    }


}
