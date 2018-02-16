package org.molgenis.vibe.rdf_processing.query_string_creation;

import org.apache.jena.query.Syntax;

public class QueryStringPathRange {

    private String rangeString;

    private Syntax syntax;

    public String getRangeString() {
        return rangeString;
    }

    public Syntax getSyntax() {
        return syntax;
    }

    public QueryStringPathRange(Predefined predefined) {
        syntax = Syntax.syntaxSPARQL_11;
        this.rangeString = predefined.getRangeString();
    }

    public QueryStringPathRange(int value) {
        syntax = Syntax.syntaxARQ;
        this.rangeString = "{" + value + "}";
    }

    public QueryStringPathRange(int value, boolean isStart) {
        syntax = Syntax.syntaxARQ;
        if(isStart) {
            this.rangeString = "{" + Integer.toString(value) + ",}";
        } else {
            this.rangeString =  "{," + Integer.toString(value) + "}";
        }
        simplifySyntaxIfPossible();
    }

    public QueryStringPathRange(int start, int end) {
        syntax = Syntax.syntaxARQ;
        if(start == end) {
            this.rangeString = "{" + start + "}";
        } else if (start > end) {
          throw new IllegalArgumentException("start > end");
        } else {
            this.rangeString = "{" + Integer.toString(start) + "," + Integer.toString(end) + "}";
        }
        simplifySyntaxIfPossible();
    }

    private void simplifySyntaxIfPossible() {
        switch(rangeString) {
            case "{0,}":
                syntax = Syntax.syntaxSPARQL_11;
                rangeString = Predefined.ZERO_OR_MORE.getRangeString();
                break;
            case "{,0}":
                rangeString = "{0}";
                break;
            case "{1,}":
                syntax = Syntax.syntaxSPARQL_11;
                rangeString = Predefined.ONE_OR_MORE.getRangeString();
                break;
            case "{,1}":
                syntax = Syntax.syntaxSPARQL_11;
                rangeString = Predefined.ZERO_OR_ONE.getRangeString();
                break;
            case "{0,1}":
                syntax = Syntax.syntaxSPARQL_11;
                rangeString = Predefined.ZERO_OR_ONE.getRangeString();
                break;
        }
    }

    @Override
    public String toString() {
        return getRangeString();
    }

    /**
     * @see <a href="https://jena.apache.org/documentation/query/property_paths.html">https://jena.apache.org/documentation/query/property_paths.html</a>
     * @see <a href="https://www.w3.org/TR/sparql11-query/#pp-language">https://www.w3.org/TR/sparql11-query/#pp-language</a>
     */
    public enum Predefined {
        ZERO_OR_MORE("*"),
        ONE_OR_MORE("+"),
        ZERO_OR_ONE("?");

        private String rangeString;

        public String getRangeString() {
            return rangeString;
        }

        Predefined(String rangeString) {
            this.rangeString = rangeString;
        }

        @Override
        public String toString() {
            return getRangeString();
        }
    }
}
