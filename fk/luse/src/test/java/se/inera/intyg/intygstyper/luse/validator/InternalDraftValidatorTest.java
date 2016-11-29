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
package se.inera.intyg.intygstyper.luse.validator;

import org.junit.Test;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.intygstyper.fkparent.model.internal.Underlag;
import se.inera.intyg.intygstyper.luse.model.internal.LuseUtlatande;
import se.inera.intyg.intygstyper.luse.utils.ScenarioNotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by eriklupander on 2016-11-29.
 */
public class InternalDraftValidatorTest {

    private InternalDraftValidatorImpl testee = new InternalDraftValidatorImpl();

    @Test
    public void testUnderlagWithEmptyStringOnFranVardgivareHamtasTriggersError() throws ScenarioNotFoundException {
        LuseUtlatande utlatande = LuseUtlatande.builder()
                .setId("123")
                .setTextVersion("1")
                .setGrundData(buildGrundData())
                .setUnderlagFinns(true)
                .setUnderlag(buildUnderlagList()).build();

        List<ValidationMessage> validationMessages = new ArrayList<>();
        testee.validateUnderlag(utlatande, validationMessages);
        assertEquals(1, validationMessages.size());
        assertEquals("luse.validation.underlag.hamtas-fran.missing", validationMessages.get(0).getMessage());
    }

    private GrundData buildGrundData() {
        GrundData grundData = new GrundData();

        return grundData;
    }

    private List<Underlag> buildUnderlagList() {
        return Arrays.asList(buildUnderlag());
    }

    private Underlag buildUnderlag() {
        Underlag underlag = Underlag.create(Underlag.UnderlagsTyp.NEUROPSYKIATRISKT_UTLATANDE, new InternalDate(LocalDate.now()), "");
        return underlag;
    }


}
