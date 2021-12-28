package edu.cwru.wcf13.gis;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A record class representing a rectangle defined between two given Coordinates:
 * ... a bottom-left vertex and a top-right vertex
 *
 * @author Will Franzen
 * @version Programming Assignment 13
 */

public record Rectangle(Coordinate bottomLeft, Coordinate topRight) {

    /**
     * Validate a Rectangle object is non-null and validate the contained Coordinates as well
     *
     * @return the validated rectangle
     * @see #validate(Rectangle)
     */
    public final Rectangle validate() {

        Coordinate.validate(bottomLeft());
        Coordinate.validate(topRight());

        // If 0 then the rectangle is a line as X. or Y.compareTo = 0, and if 1 then bottomLeft's X or Y is greater than topRight's
        if (bottomLeft().compareTo(topRight()) > -1)
            throw new IllegalStateException("The bottom left of the rectangle exceeds the bounds of the top right.");

        return this;
    }

    /**
     * Validate a Rectangle object is non-null and validate the contained Coordinates as well
     *
     * @param rectangle a Rectangle to be validated
     * @return the validated rectangle
     * @see #validate()
     */
    public static final Rectangle validate(Rectangle rectangle) {

        Objects.requireNonNull(rectangle);
        return rectangle.validate();
    }


    public final BigDecimal left() {

        return bottomLeft().x();
    }

    public final BigDecimal right() {

        return topRight().x();
    }

    public final BigDecimal bottom() {

        return bottomLeft().y();
    }

    public final BigDecimal top() {

        return topRight().y();
    }

    public final String toString() {
        // Returns the string: "The bottom left coordinate is (x1,y1), and the top right coordinate is (x2, y2)"
        return "RECTANGLE: "
                + bottomLeft().toSimpleString()
                + " to "
                + topRight().toSimpleString();
    }

    /**
     * Create and return a set of Bigdecimal coordinates between a given lower and upper bound
     *
     * @param lower the lower bound of the range
     * @param upper the upper bound of the range
     * @return a set of BigDecimals incremented by one
     * @see RectilinearRegion#coordSet(Function, Function)
     */
    Set<BigDecimal> rectBounds(BigDecimal lower, BigDecimal upper) {

        Objects.requireNonNull(lower);
        Objects.requireNonNull(upper);

        // Potentially traverse through the values without having to add BigDecimail.ONE ?
        return Stream.iterate(lower,
                        i -> i.compareTo(upper) < 0, // Top and Right sides are exclusive so stop when iterator equal to upper bound
                        i -> i.add(BigDecimal.ONE))
                .collect(Collectors.toSet());
    }

    /**
     * This method is used to reduce complexity in the isConnected algorithm while check to see if two rectangles are
     * connected.
     *
     * @param rectTwo rectangle to compare
     * @return <Code>True</Code> if this.bottomLeft <= rectTwo.topRight and this.topRight >= rectTwo.bottomLeft
     * @see RectilinearRegion#isConnected()
     */
    Boolean isTouching(Rectangle rectTwo) {

        validate(this);
        validate(rectTwo);

        if( this.bottomLeft.compareTo( rectTwo.topRight ) == 0) {

            return this.topRight().compareTo( rectTwo.bottomLeft() ) > 0;
        } else if( this.bottomLeft.compareTo( rectTwo.topRight ) < 0 ) {

            // If this.bottomLeft is below than rectTwo.topRight then we need to confirm this.topRight borders rectTwo
            return this.right().compareTo(rectTwo.left()) == 0
                    || this.top().compareTo(rectTwo.bottom()) == 0;
        }

        // If this.bottomRight > rectTwo.topRight, it is not possible for the rectangles to be touching
        return false;
    }

}
