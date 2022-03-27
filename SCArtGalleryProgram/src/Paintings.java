import java.math.BigDecimal;
import java.util.Objects;

public record Paintings(BigDecimal height, BigDecimal price, Artists artist) implements Comparable<Paintings> {

    final Paintings validate() {

        Objects.requireNonNull(height());
        if( height().compareTo(BigDecimal.ZERO) < 1) {                                                       // 1
            throw new IllegalStateException("The height of the painting must be a positive numeric value.");
        }
        Objects.requireNonNull(price());
        if( price().compareTo(BigDecimal.ZERO) < 1) {                                                        // 2
            throw new IllegalStateException("The price of the painting must be a positive numeric value.");
        }
        Objects.requireNonNull(artist());

        return this;
    }

    static final Paintings validate(Paintings painting) {
        Objects.requireNonNull(painting);

        return painting.validate();
    }

    @Override
    public int compareTo(Paintings o) { // Use Comparator object
        validate(o);

        // If the prices are equal, compare heights
        if (price().compareTo(o.price()) == 0) {
            return height().compareTo(o.height());
        } else {
            return price().compareTo(o.price());
        }
    }

    public String toSimpleString() {

        return artist().toString() + ":(Height: " + height() + ", Price: " + price() + ")";
    }

}

