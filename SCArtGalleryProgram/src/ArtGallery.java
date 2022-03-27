
import java.util.*;

public final class ArtGallery {

    private final List<Paintings> tempDalis = new LinkedList<>();
    private final List<Paintings> tempPicassos = new LinkedList<>();
    private final SortedMap<Paintings, Paintings> galleryPairs = new TreeMap<>();
    private final Paintings[] dalis;
    private final Paintings[] picassos;

    public ArtGallery(Collection<Paintings> allPaintings) {
        Objects.requireNonNull(allPaintings);

        allPaintings.forEach(paintings -> {
            Paintings.validate(paintings);

            if( paintings.artist() == Artists.DALI) {
                tempDalis.add(paintings);
            }
            if( paintings.artist() == Artists.PICASSO) {
                tempPicassos.add(paintings);
            }
        });

        if( tempPicassos.size() < 1 || tempDalis.size() < 1) {
            throw new IllegalStateException("There are not enough Dalis or Picassos to make a non-empty gallery");
        } else {
            Collections.sort(tempDalis);
            dalis = tempDalis.toArray(new Paintings[0]);
            Collections.sort(tempPicassos);
            picassos = tempPicassos.toArray(new Paintings[0]);
        }
    }

    SortedMap<Paintings, Paintings> createGalleryPairs() {

        int picassoIndex = 0;
        int daliIndex = 0;

        while(isNotEnd(picassoIndex, daliIndex)) {

            checkForPairs(picassoIndex, daliIndex);
            picassoIndex++;
            daliIndex++;
        }

        return galleryPairs;
    }

    void checkForPairs(int pIndex, int dIndex) {

        if (isPicassoShorter(pIndex, dIndex)) {
            galleryPairs.put(dalis[dIndex], picassos[pIndex]);

        } else if (isPicassoShorter(pIndex, dIndex + 1)) {
            galleryPairs.put(dalis[dIndex + 1], picassos[pIndex]);

        } else if (isPicassoShorter(pIndex + 1, dIndex)) {
            galleryPairs.put(dalis[dIndex], picassos[pIndex + 1]);
        }

    }

    Boolean isPicassoShorter(int pIndex, int dIndex) {

        return isNotEnd(pIndex, dIndex) && // It cannot be the end of either array AND...
                picassos[pIndex].height().compareTo( // The indexed picasso must be shorter than the indexed dali
                        dalis[dIndex].height()
                ) < 0;
    }

    Boolean isNotEnd(int pIndex, int dIndex) {

        return pIndex < picassos.length &&
                dIndex < dalis.length;
    }

    public String toString() {

        StringBuilder galleryString = new StringBuilder();

        for (Paintings painting : galleryPairs.keySet()) {
            galleryString.append(painting.toSimpleString())
                    .append(", ").append(galleryPairs.get(painting).toSimpleString())
                    .append("\n");
        }

        return galleryString.toString();
    }

}
