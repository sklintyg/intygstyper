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

package se.inera.intyg.intygstyper.ts_parent.model.converter;

import java.util.stream.Collectors;

import org.joda.time.LocalDateTime;

import se.inera.intyg.common.support.common.enumerations.BefattningKod;
import se.inera.intyg.common.support.model.common.internal.*;
import se.inera.intyg.common.support.modules.support.api.dto.Personnummer;
import se.inera.intygstjanster.ts.services.v1.SkapadAv;

public final class TransportToInternalUtil {

    private TransportToInternalUtil() {
    }

    public static GrundData buildGrundData(se.inera.intygstjanster.ts.services.v1.GrundData source) {
        GrundData grundData = new GrundData();
        grundData.setPatient(convertPatient(source.getPatient()));
        grundData.setSigneringsdatum(LocalDateTime.parse(source.getSigneringsTidstampel()));
        grundData.setSkapadAv(convertHoSPersonal(source.getSkapadAv()));
        return grundData;
    }

    private static HoSPersonal convertHoSPersonal(SkapadAv source) {
        HoSPersonal hosPersonal = new HoSPersonal();
        hosPersonal.setFullstandigtNamn(source.getFullstandigtNamn());
        hosPersonal.setPersonId(source.getPersonId().getExtension());
        hosPersonal.setVardenhet(convertVardenhet(source.getVardenhet()));

        // try to convert befattning from description, otherwise use it as a code
        hosPersonal.getBefattningar().addAll(source.getBefattningar().stream()
                .map(description -> BefattningKod.getCodeFromDescription(description).orElse(description))
                .collect(Collectors.toList()));
        hosPersonal.getSpecialiteter().addAll(source.getSpecialiteter());
        return hosPersonal;
    }

    private static Vardenhet convertVardenhet(se.inera.intygstjanster.ts.services.v1.Vardenhet source) {
        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setEnhetsid(source.getEnhetsId().getExtension());
        vardenhet.setEnhetsnamn(source.getEnhetsnamn());
        vardenhet.setPostadress(source.getPostadress());
        vardenhet.setPostnummer(source.getPostnummer());
        vardenhet.setPostort(source.getPostort());
        vardenhet.setTelefonnummer(source.getTelefonnummer());
        vardenhet.setVardgivare(convertVardgivare(source.getVardgivare()));
        return vardenhet;
    }

    private static Vardgivare convertVardgivare(se.inera.intygstjanster.ts.services.v1.Vardgivare source) {
        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setVardgivarid(source.getVardgivarid().getExtension());
        vardgivare.setVardgivarnamn(source.getVardgivarnamn());
        return vardgivare;
    }

    private static Patient convertPatient(se.inera.intygstjanster.ts.services.v1.Patient source) {
        Patient patient = new Patient();
        patient.setEfternamn(source.getEfternamn());
        patient.setFornamn(source.getFornamn());
        patient.setFullstandigtNamn(source.getFullstandigtNamn());
        patient.setPersonId(new Personnummer(source.getPersonId().getExtension()));
        patient.setPostadress(source.getPostadress());
        patient.setPostnummer(source.getPostnummer());
        patient.setPostort(source.getPostort());
        return patient;
    }
}
