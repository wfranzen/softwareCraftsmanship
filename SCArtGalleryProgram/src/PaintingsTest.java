import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class PaintingsTest {


    @Test(expected = IllegalStateException.class)
    public void validateCaseOne() { // Test Case (1) for validate()
        // Test Case (1) in test documentation
        // Error should be thrown when height <= 0
        Paintings validateHeight = new Paintings(BigDecimal.ZERO, BigDecimal.ONE, Artists.DALI);
        validateHeight.validate();
    }

    @Test(expected = IllegalStateException.class)
    public void validateCaseTwo() { // Test Case (2) for validate()
        // Error should be thrown when price <= 0
        Paintings validatePrice = new Paintings(BigDecimal.ONE, BigDecimal.ZERO, Artists.DALI);
        validatePrice.validate();
    }

    @Test
    public void validateCaseThree() { // Test Case (3) for validate()
        // Painting calling the method should be returned, throwing no errors
        Paintings validateNominal = new Paintings(BigDecimal.ONE, BigDecimal.ONE, Artists.DALI);
        assertEquals(validateNominal, validateNominal.validate());
    }

    @Test
    public void staticValidate() {
        // Static validate method requiring a Paintings argument, passing to the non-static validate method
        Paintings validateNominal = new Paintings(BigDecimal.ONE, BigDecimal.ONE, Artists.DALI);
        assertEquals(validateNominal.validate(), Paintings.validate(validateNominal));
    }

    @Test
    public void compareToCaseOne() { // Test Case (1) for compareTo()
        // When prices of paintings are equal, return the comparison of heights

        int expectedHeight = (BigDecimal.TEN).compareTo(BigDecimal.ONE);
        Paintings biggerPicture = new Paintings(BigDecimal.TEN, BigDecimal.TEN, Artists.DALI);
        Paintings smallerPicture = new Paintings(BigDecimal.ONE, BigDecimal.TEN, Artists.DALI);

        assertEquals(expectedHeight, biggerPicture.compareTo(smallerPicture));
    }

    @Test
    public void compareToCaseTwo() { // Test Case (2) for compareTo()
        // When prices of paintings are not equal, return that comparison

        int expectedPrice = (BigDecimal.TEN).compareTo(BigDecimal.ONE);
        Paintings picture1 = new Paintings(BigDecimal.TEN, BigDecimal.TEN, Artists.DALI);
        Paintings picture2 = new Paintings(BigDecimal.TEN, BigDecimal.ONE, Artists.DALI);

        assertEquals(expectedPrice, picture1.compareTo(picture2));

    }

    @Test
    public void toSimpleString() { // Basic toString method

        Paintings stringTest = new Paintings(BigDecimal.TEN, BigDecimal.ONE, Artists.DALI);
        assertEquals("DALI:(Height: 10, Price: 1)", stringTest.toSimpleString());
    }

    @Test
    public void height() { // Record getter method

        Paintings heightTest = new Paintings(BigDecimal.TEN, BigDecimal.ONE, Artists.DALI);
        assertEquals(BigDecimal.TEN, heightTest.height());
    }

    @Test
    public void price() { // Record getter method

        Paintings priceTest = new Paintings(BigDecimal.TEN, BigDecimal.ONE, Artists.DALI);
        assertEquals(BigDecimal.ONE, priceTest.price());
    }

    @Test
    public void artist() { // Record getter method

        Paintings artistTest = new Paintings(BigDecimal.TEN, BigDecimal.ONE, Artists.DALI);
        assertEquals(Artists.DALI, artistTest.artist());
    }
}