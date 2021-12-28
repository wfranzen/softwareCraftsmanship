package edu.cwru.wcf13.gis;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * A Coordinate represents a pair x,y of horizontal and vertical values denoting the location of a landmark.
 *
 * @author Will Franzen
 * @version Programming Assignment 13
 */
public record Coordinate(BigDecimal x, BigDecimal y) implements Comparable<Coordinate> {

    public static final Coordinate ORIGIN = new Coordinate(BigDecimal.ZERO, BigDecimal.ZERO);

    /**
     * Throws a NullPointerException if either x or y are null. Otherwise, returns the calling Coordinate
     *
     * @return this Coordinate
     */
    public final Coordinate validate() {
        Objects.requireNonNull(x());
        Objects.requireNonNull(y());
        return this;
    }

    /**
     * Throws a NullPointerException if the Coordinate is null
     *
     * @param coordinate coordinate to be validated
     * @return this Coordinate
     */
    public static final Coordinate validate(Coordinate coordinate) {

        Objects.requireNonNull(coordinate);
        coordinate.validate();
        return coordinate;
    }

    public String toSimpleString() {

        return "(" + x.toString() + "," + y.toString() + ")";
    }

    @Override
    public int compareTo(Coordinate o) {

        Coordinate.validate(o);
        int compX = x().compareTo(o.x());
        int compY = y().compareTo(o.y());

        if(compX < 1) {
            // If the x location of this is not greater than o, compare y values to determine comparable value
            // ... this coordinate is lesser if its x is smaller or y is smaller when x values are equal
            if(compY < 0)
                return compY;
            else if(compY < 1)
                return compY;
        }
        return 1;

    }

}

// OLD COMPARETO METHOD - x=-1 and y=0 RETURNED 0
//if(x.compareTo(o.x) < 1) {
//
//    if(y.compareTo(o.y) < 0)
//        return -1;
//    else if(y.compareTo(o.y) < 1)
//        return 0;
//}
//return 1;
