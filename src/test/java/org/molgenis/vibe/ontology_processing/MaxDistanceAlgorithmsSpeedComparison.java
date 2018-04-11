package org.molgenis.vibe.ontology_processing;

import com.google.common.base.Stopwatch;
import org.apache.jena.ontology.OntModel;
import org.molgenis.vibe.TestData;
import org.molgenis.vibe.formats.Phenotype;
import org.molgenis.vibe.io.OntologyModelFilesReader;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class MaxDistanceAlgorithmsSpeedComparison {
    private OntModel model;
    private static final int testRepeats = 1;
    private static final Set<Phenotype> startPhenotypes = new HashSet<>(Arrays.asList(
            new Phenotype("hp:0001377")
    ));

    @BeforeClass(groups = {"benchmarking"})
    public void beforeClass() {
        OntologyModelFilesReader reader = new OntologyModelFilesReader(TestData.ONTOLOGY_FILE.getFiles()[0]);
        model = reader.getModel();
    }

    @Test(groups = {"benchmarking"})
    public void compareDistance00() {
        System.out.println("# Distance 0");
        runTestWithDistance(0);
    }

    @Test(groups = {"benchmarking"})
    public void compareDistance03() {
        System.out.println("# Distance 3");
        runTestWithDistance(3);
    }

    @Test(groups = {"benchmarking"})
    public void compareDistance05() {
        System.out.println("# Distance 5");
        runTestWithDistance(5);
    }

    @Test(groups = {"benchmarking"})
    public void compareDistance10() {
        System.out.println("# Distance 10");
        runTestWithDistance(8);
    }

    public void runTestWithDistance(int maxDistance) {
        printResults(
                runRetriever(new MaxDistanceRetriever(model, startPhenotypes, maxDistance)),
                runRetriever(new MaxDistanceRetriever2(model, startPhenotypes, maxDistance))
        );
    }

    private String[] runRetriever(PhenotypesRetriever retriever) {
        String[] times = new String[testRepeats];
        for(int i = 0; i<testRepeats;i++) {
            Stopwatch timer = Stopwatch.createStarted();
            retriever.run();
            times[i] = timer.stop().toString();
        }
        return times;
    }

    private void printResults(String[] times1, String[] times2) {
        System.out.println("MaxDistanceRetriever: " + Arrays.stream(times1).map(String::toString).collect(Collectors.joining(", ")));
        System.out.println("MaxDistanceRetriever2: " + Arrays.stream(times2).map(String::toString).collect(Collectors.joining(", ")));
    }

}
