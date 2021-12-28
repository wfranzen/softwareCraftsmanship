package edu.cwru.wcf13.gis;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class RectangleTest {

    private final Coordinate coordTest = new Coordinate(new BigDecimal("1"), BigDecimal.ZERO);
    private final Coordinate coordTest2 = new Coordinate(new BigDecimal("3"), new BigDecimal("2"));
    private final Coordinate nullCoord = new Coordinate(null, BigDecimal.TEN);

    private final Rectangle testRect = new Rectangle(coordTest, coordTest2);
    private final Rectangle nullRect = new Rectangle(coordTest, nullCoord);

    @Test
    public void isTouchingCaseCorner() {
        final Coordinate rect1BL = new Coordinate(new BigDecimal("3"), new BigDecimal("5"));
        final Coordinate rect1TR = new Coordinate(new BigDecimal("5"), new BigDecimal("8"));
        final Coordinate rect2BL = new Coordinate(new BigDecimal("1"), new BigDecimal("0"));
        final Coordinate rect2TR = new Coordinate(new BigDecimal("3"), new BigDecimal("5"));

        final Rectangle firstRect = new Rectangle(rect1BL, rect1TR);
        final Rectangle secondRect = new Rectangle(rect2BL, rect2TR);

        assertTrue( firstRect.isTouching(secondRect) );
    }

    @Test
    public void isTouchingCaseBLBelow() {
        final Coordinate rect1BL = new Coordinate(new BigDecimal("1"), new BigDecimal("0"));
        final Coordinate rect1TR = new Coordinate(new BigDecimal("3"), new BigDecimal("6"));
        final Coordinate rect2BL = new Coordinate(new BigDecimal("3"), new BigDecimal("5"));
        final Coordinate rect2TR = new Coordinate(new BigDecimal("5"), new BigDecimal("8"));

        final Rectangle firstRect = new Rectangle(rect1BL, rect1TR);
        final Rectangle secondRect = new Rectangle(rect2BL, rect2TR);

        assertTrue( firstRect.isTouching(secondRect) );
    }

    @Test
    public void isTouchingCaseNotTouching() {
        final Coordinate rect1BL = new Coordinate(new BigDecimal("1"), new BigDecimal("0"));
        final Coordinate rect1TR = new Coordinate(new BigDecimal("5"), new BigDecimal("4"));
        final Coordinate rect2BL = new Coordinate(new BigDecimal("3"), new BigDecimal("5"));
        final Coordinate rect2TR = new Coordinate(new BigDecimal("5"), new BigDecimal("8"));

        final Rectangle firstRect = new Rectangle(rect1BL, rect1TR);
        final Rectangle secondRect = new Rectangle(rect2BL, rect2TR);



        assertFalse( firstRect.isTouching(secondRect) );
    }


    // LEGACY TESTS
    @Test
    public void validate() {

        assertEquals(testRect, testRect);
        assertThrows(NullPointerException.class, () -> {
            nullRect.validate();
        });
    }

    @Test
    public void testValidate() {

        assertEquals(testRect, Rectangle.validate(testRect));
        assertThrows(NullPointerException.class, () -> {
            Rectangle.validate(nullRect);
        });
    }

    @Test
    public void left() {

        assertEquals(new BigDecimal("1"), testRect.left());

//        assertThrows(NullPointerException.class, () -> {
//            nullRect.left();
//        });
    }

    @Test
    public void right() {

        assertEquals(new BigDecimal("3"), testRect.right());
    }

    @Test
    public void bottom() {

        assertEquals(new BigDecimal("0"), testRect.bottom());
    }

    @Test
    public void top() {

        assertEquals(new BigDecimal("2"), testRect.top());
    }

    @Test
    public void bottomLeft() {
    }

    @Test
    public void topRight() {
    }
}