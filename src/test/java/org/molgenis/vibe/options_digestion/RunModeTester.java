package org.molgenis.vibe.options_digestion;

import org.testng.Assert;
import org.testng.annotations.Test;

public class RunModeTester {

    @Test
    public void setRunModeWithKnownInt() {
        Assert.assertEquals(RunMode.retrieve(1), RunMode.GET_GENES_WITH_SINGLE_HPO);
    }

    @Test
    public void setRunModeWithUnknownInt() {
        Assert.assertEquals(RunMode.retrieve(-1), RunMode.NONE);
    }

    @Test
    public void setRunModeWithKnownString() {
        Assert.assertEquals(RunMode.retrieve("1"), RunMode.GET_GENES_WITH_SINGLE_HPO);
    }

    @Test
    public void setRunModeWithUnknownString() {
        Assert.assertEquals(RunMode.retrieve("-1"), RunMode.NONE);
    }

    @Test(expectedExceptions = NumberFormatException.class)
    public void setRunModeWithInvalidString() {
        RunMode.retrieve("kaas");
    }
}
