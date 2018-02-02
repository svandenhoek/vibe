package org.molgenis.vibe.rdf_querying;

public class SparqlRange {

    private String rangeString;

    SparqlRange(int value) {
        this.rangeString = "{" + value + "}";
    }

    SparqlRange(int value, boolean isStart) {
        if(isStart) {
            this.rangeString = "{" + Integer.toString(value) + ",}";
        } else {
            this.rangeString =  "{," + Integer.toString(value) + "}";
        }
        convertRangeStringToDefaultCharacterIfPossible();
    }

    SparqlRange(int start, int end) {
        if(start == end) {
            this.rangeString = "{" + start + "}";
        } else if (start > end) {
          throw new IllegalArgumentException("start > end");
        } else {
            this.rangeString = "{" + Integer.toString(start) + "," + Integer.toString(end) + "}";
        }
        convertRangeStringToDefaultCharacterIfPossible();
    }

    private void convertRangeStringToDefaultCharacterIfPossible() {
        switch(rangeString) {
            case "{0,}":
                rangeString = "*";
                break;
            case "{,0}":
                rangeString = "{0}";
                break;
            case "{1,}":
                rangeString = "+";
                break;
            case "{,1}":
                rangeString = "?";
                break;
            case "{0,1}":
                rangeString = "?";
        }
    }

    @Override
    public String toString() {
        return rangeString;
    }
}
