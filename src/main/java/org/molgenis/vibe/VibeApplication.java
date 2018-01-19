package org.molgenis.vibe;

import org.molgenis.data.Entity;
import org.molgenis.data.annotation.makervcf.structs.VcfEntity;
import org.molgenis.data.vcf.VcfRepository;
import org.molgenis.vibe.options_digestion.CommandLineOptionsParser;
import org.molgenis.vibe.options_digestion.OptionsParser;
import org.molgenis.vibe.options_digestion.RunMode;

import java.util.Iterator;

/**
 * The main application class.
 */
public class VibeApplication {
    /**
     * The main method for when used as a standalone application.
     * @param args {@link String}{@code []}
     */
    public static void main(String[] args) {
        VibeApplication app = new VibeApplication();

        try {
            CommandLineOptionsParser appOptions = new CommandLineOptionsParser(args);
            // If RunMode is NONE, shows help message and quits application.
            if(appOptions.getRunMode() == RunMode.NONE) {
                CommandLineOptionsParser.printHelpMessage();
            } else { // Any other RunMode will continue application.
                try {
                    app.run(appOptions);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
            CommandLineOptionsParser.printHelpMessage();
        }
    }

    /**
     * The actual processing parts of the application.
     * @param appOptions {@link OptionsParser}
     * @throws Exception if {@link VcfEntity#VcfEntity(Entity)} fails
     */
    public void run(OptionsParser appOptions) throws Exception {
        // Reads in rVCF for further usage.
        VcfRepository vcf = new VcfRepository(appOptions.getRvcfData().toFile(), "rvcf");

        // Example of iterating through VcfRepository:
        Iterator<Entity> vcfIterator = vcf.iterator();
        while(vcfIterator.hasNext())
        {
            VcfEntity record = new VcfEntity(vcfIterator.next());
        }
    }
}
