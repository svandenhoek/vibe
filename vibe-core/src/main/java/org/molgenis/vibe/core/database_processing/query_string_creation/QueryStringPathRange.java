package org.molgenis.vibe.core.database_processing.query_string_creation;

import org.apache.jena.query.Syntax;

/**
 * A {@link String} defining the path which should be used in the RDF graph.
 * @see <a href="https://jena.apache.org/documentation/query/property_paths.html">https://jena.apache.org/documentation/query/property_paths.html</a>
 * @see <a href="https://www.w3.org/2009/sparql/docs/property-paths/Overview.xml#path-language">https://www.w3.org/2009/sparql/docs/property-paths/Overview.xml#path-language</a>
 */
public class QueryStringPathRange {
    // Some simplified syntaxes that are available. See also links mentioned in the JavaDoc of this class.
    private static final String ZERO_OR_MORE = "*";
    private static final String ONE_OR_MORE = "+";
    private static final String ZERO_OR_ONE = "?";

    /**
     * The {@link String} defining the path.
     */
    private String rangeString;

    /**
     * Apache Jena Syntax needed for it.
     */
    private Syntax syntax;

    public String getRangeString() {
        return rangeString;
    }

    public Syntax getSyntax() {
        return syntax;
    }

    /**
     * @param value must be >= 0
     */
    public QueryStringPathRange(int value) {
        rangeString = "{" + value + "}";
        syntax = Syntax.syntaxARQ;
    }

    /**
     * @param value must be >= 0
     * @param isStart whether value indicates the start value of the range (if false, indicates end value)
     */
    public QueryStringPathRange(int value, boolean isStart) {
        throwExceptionIfNegative(value);

        if(isStart) {
            switch (value) {
                case 0:
                    rangeString = ZERO_OR_MORE;
                    syntax = Syntax.syntaxSPARQL_11;
                    break;
                case 1:
                    rangeString = ONE_OR_MORE;
                    syntax = Syntax.syntaxSPARQL_11;
                    break;
                default:
                    rangeString = "{" + value + ",}";
                    syntax = Syntax.syntaxARQ;
            }
        } else {
            switch (value) {
                case 0:
                    rangeString =  "{" + value + "}";
                    syntax = Syntax.syntaxARQ;
                    break;
                case 1:
                    rangeString = ZERO_OR_ONE;
                    syntax = Syntax.syntaxSPARQL_11;
                    break;
                default:
                    rangeString =  "{," + value + "}";
                    syntax = Syntax.syntaxARQ;
            }
        }
    }

    /**
     *
     * @param start must be >= 0
     * @param end must be >= 0
     */
    public QueryStringPathRange(int start, int end) {
        throwExceptionIfNegative(start);
        throwExceptionIfNegative(end);

        if(start > end) {
            throw new IllegalArgumentException("start > end");
        } else if(start == end) {
            rangeString = "{" + start + "}";
            syntax = Syntax.syntaxARQ;
        } else if(start == 0 && end == 1) {
            rangeString = ZERO_OR_ONE;
            syntax = Syntax.syntaxSPARQL_11;
        } else {
            rangeString = "{" + start + "," + end + "}";
            syntax = Syntax.syntaxARQ;
        }
    }

    private void throwExceptionIfNegative(int value) {
        if(value < 0) {
            throw new IllegalArgumentException("a negative argument was found while not allowed");
        }
    }

    @Override
    public String toString() {
        return getRangeString();
    }
}
