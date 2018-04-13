package org.molgenis.vibe.ontology_processing;

import org.apache.commons.lang3.StringUtils;
import org.apache.jena.ext.com.google.common.base.Stopwatch;
import org.apache.jena.ontology.OntModel;
import org.molgenis.vibe.TestData;
import org.molgenis.vibe.formats.Phenotype;
import org.molgenis.vibe.io.OntologyModelFilesReader;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MaxDistanceAlgorithmsSpeedComparison {
    private static final String SYS_OUT_FORMAT = "%12s|%10s|%10s|%10s|%10s%n";
//    private static final String SYS_OUT_FORMAT = "%s & %s & %s & %s & %s%n";
    private static final int SPACER_REPEAT = 56;
    private OntModel model;
    private static final int TEST_REPEATS = 3;

    private static final Set<Phenotype> startPhenotypes1 = new HashSet<>(Arrays.asList(
            new Phenotype("hp:0002996")
    ));

    private static final Set<Phenotype> startPhenotypes2 = new HashSet<>(Arrays.asList(
            new Phenotype("hp:0000001")
    ));

    @BeforeClass(groups = {"benchmarking"})
    public void beforeClass() {
        OntologyModelFilesReader reader = new OntologyModelFilesReader(TestData.ONTOLOGY_FILE.getFiles()[0]);
        model = reader.getModel();
    }

    @Test(groups = {"benchmarking"})
    public void benchmarkMaxDistance1() {
        printHeader();
        for(int i : new int[]{0,2,5,6,7,8,9}) {
            printResults(i, runRetriever(new MaxDistanceRetriever(model, startPhenotypes1, i)));
        }
        printFooter();
    }

    @Test(groups = {"benchmarking"})
    public void benchmarkMaxDistance2() {
        printHeader();
        for(int i : new int[]{0,2,5,8,9,10,20,50}) {
            printResults(i, runRetriever(new MaxDistanceRetriever2(model, startPhenotypes1, i)));
        }
        printFooter();
    }

    @Test(groups = {"benchmarking"})
    public void benchmarkChildren() {
        printHeader();
        for(int i : new int[]{0,2,5}) { // 2 is furthest child distance available
            printResults(i, runRetriever(new ChildrenRetriever(model, startPhenotypes1, i)));
        }
        printFooter();
    }

    @Test(groups = {"benchmarking"})
    public void benchmarkChildrenFromRoot() {
        printHeader();
        for(int i : new int[]{0,2,5,10,20,50}) {
            printResults(i, runRetriever(new ChildrenRetriever(model, startPhenotypes2, i)));
        }
        printFooter();
    }

    private BenchmarkOutput runRetriever(PhenotypesRetriever retriever) {
        String[] times = new String[TEST_REPEATS];
        for(int i = 0; i<TEST_REPEATS;i++) {
            Stopwatch timer = Stopwatch.createStarted();
            retriever.run();
            times[i] = timer.stop().toString();
        }

        return new BenchmarkOutput(retriever.getPhenotypeNetworkCollection().getPhenotypes().size(), times);
    }

    private void printResults(int maxDistance, BenchmarkOutput benchmarkerOutput) {
        System.out.format(SYS_OUT_FORMAT, maxDistance, benchmarkerOutput.getOutputSize(), benchmarkerOutput.getTimes()[0],
                benchmarkerOutput.getTimes()[1], benchmarkerOutput.getTimes()[2]);
//        System.out.println("maxDistance=" + maxDistance + ", size=" + benchmarkerOutput.getOutputSize() + ", times=" +
//                Arrays.stream(benchmarkerOutput.getTimes()).map(String::toString).collect(Collectors.joining(", ")));
    }

    private void printHeader() {
        System.out.format(StringUtils.repeat("-", SPACER_REPEAT) + "\n");
        System.out.format(SYS_OUT_FORMAT, "maxDistance", "size", "time1", "time2", "time3");
        System.out.format(StringUtils.repeat("-", SPACER_REPEAT) + "\n");
    }

    private void printFooter() {
        System.out.format(StringUtils.repeat("-", SPACER_REPEAT) + "\n");
    }

    private class BenchmarkOutput {
        private int outputSize;
        private String[] times;

        public int getOutputSize() {
            return outputSize;
        }

        public String[] getTimes() {
            return times;
        }

        public BenchmarkOutput(int outputSize, String[] times) {
            this.outputSize = outputSize;
            this.times = times;
        }
    }
}
