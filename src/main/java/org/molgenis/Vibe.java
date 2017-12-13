package org.molgenis;

import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.ParseException;
import org.molgenis.data.Entity;
import org.molgenis.data.annotation.makervcf.structs.VcfEntity;
import org.molgenis.data.vcf.VcfRepository;
import org.molgenis.options_digestion.CommandLineOptionsParser;
import org.molgenis.options_digestion.OptionsParser;

import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.util.Iterator;

/**
 * The main application class.
 */
public class Vibe {
    /**
     * The main method for when used as a standalone application.
     * @param args {@link String}{@code []}
     */
    public static void main(String[] args) {
        Vibe app = new Vibe();

        try {
            CommandLineOptionsParser appOptions = new CommandLineOptionsParser(args);

            try {
                app.run(appOptions);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (MissingOptionException e) {
            System.err.println(e.getLocalizedMessage());
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (InvalidPathException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
