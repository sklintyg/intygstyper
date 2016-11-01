package se.inera.intyg.intygstyper.fkparent.pdf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import se.inera.intyg.common.support.common.enumerations.PartKod;
import se.inera.intyg.common.support.model.CertificateState;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.Status;

/**
 * Created by marced on 2016-10-25.
 */
public class FkBasePdfDefinitionBuilderTest {

    private FkBasePdfDefinitionBuilder builder = new FkBasePdfDefinitionBuilder();

    @Test
    public void testIsSentToFk() throws Exception {

        assertFalse(builder.isSentToFk(null));

        List<Status> statuses = new ArrayList<>();
        assertFalse(builder.isSentToFk(statuses));

        statuses.add(new Status(null, null, LocalDateTime.now()));
        assertFalse(builder.isSentToFk(statuses));

        statuses.add(new Status(CertificateState.SENT, null, LocalDateTime.now()));
        statuses.add(new Status(CertificateState.RECEIVED, null, LocalDateTime.now()));
        assertFalse(builder.isSentToFk(statuses));

        statuses.add(new Status(CertificateState.SENT, PartKod.FKASSA.getValue(), LocalDateTime.now()));
        assertTrue(builder.isSentToFk(statuses));

    }

    @Test
    public void testNullSafeString() throws Exception {
        InternalDate date = null;
        assertEquals("", builder.nullSafeString(date));

        date = new InternalDate();
        assertEquals(date.getDate(), builder.nullSafeString(date));

        String str = null;
        assertEquals("", builder.nullSafeString(str));

        str = "test";
        assertEquals(str, builder.nullSafeString(str));

    }

}
