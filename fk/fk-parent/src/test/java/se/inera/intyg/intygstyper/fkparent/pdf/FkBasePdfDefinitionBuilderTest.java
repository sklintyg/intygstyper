/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
