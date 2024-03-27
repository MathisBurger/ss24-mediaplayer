package studiplayer.cert;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Test;

import studiplayer.audio.SortCriterion;

public class SortCriterionTest {

    @SuppressWarnings("rawtypes")
    private Class clazz = SortCriterion.class;

    @Test
    public void testEntries() {
        Object[] consts = clazz.getEnumConstants();
        Assert.assertNotNull("Kein Enum", consts);
        Assert.assertTrue("Falsche Anzahl Enum-Entries", consts.length == 5);
    }
}
