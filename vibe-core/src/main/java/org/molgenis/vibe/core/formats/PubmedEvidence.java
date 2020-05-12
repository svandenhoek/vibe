package org.molgenis.vibe.core.formats;

import java.net.URI;
import java.util.Comparator;

import static java.util.Objects.requireNonNull;

public class PubmedEvidence extends Evidence {
    private static final String ID_PREFIX = "pmid:";
    private static final String ID_REGEX = "^(pmid|PMID):([0-9]+)$";
    private static final int REGEX_ID_GROUP = 2;
    private static final String URI_PREFIX = "http://identifiers.org/pubmed/";

    private int year;
    private int idInt;

    /**
     * Sorts {@link PubmedEvidence} first on {@link #getReleaseYear()} (most recent first) and then by ID
     * (as {@code int}, lowest to highest).
     */
    public static final Comparator<PubmedEvidence> releaseYearComparator = new Comparator<PubmedEvidence>() {
        @Override
        public int compare(PubmedEvidence o1, PubmedEvidence o2) {
            int diff =  o2.year - o1.year;
            if (diff == 0) {
                diff = o1.idInt - o2.idInt;
            }
            return diff;
        }
    };

    public PubmedEvidence(String id, int year) {
        super(id);
        this.idInt = Integer.parseInt(getId());
        this.year = requireNonNull(year);
    }

    public PubmedEvidence(URI uri, int year) {
        super(uri);
        this.idInt = Integer.parseInt(getId());
        this.year = requireNonNull(year);
    }

    @Override
    protected String getIdPrefix() {
        return ID_PREFIX;
    }

    @Override
    protected String getIdRegex() {
        return ID_REGEX;
    }

    @Override
    protected int getRegexIdGroup() {
        return REGEX_ID_GROUP;
    }

    @Override
    protected String getUriPrefix() {
        return URI_PREFIX;
    }

    public int getReleaseYear() {
        return year;
    }

    @Override
    public int compareTo(Entity o) {
        if (o instanceof PubmedEvidence) {
            PubmedEvidence oPubmedEvidence = (PubmedEvidence) o;
            return this.idInt - oPubmedEvidence.idInt;
        } else {
            return super.compareTo(o);
        }
    }

    @Override
    public boolean allFieldsEquals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PubmedEvidence that = (PubmedEvidence) o;
        return super.allFieldsEquals(o) &&
                year == that.year &&
                idInt == that.idInt;
    }

    @Override
    public String toString() {
        return "PubmedEvidence{" +
                "year=" + year +
                ' ' + super.toString() +
                '}';
    }
}
