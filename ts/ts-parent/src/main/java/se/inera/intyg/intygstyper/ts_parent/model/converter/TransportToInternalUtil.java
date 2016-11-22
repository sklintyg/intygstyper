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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;

import se.inera.intyg.common.support.model.common.internal.*;
import se.inera.intyg.common.support.modules.support.api.dto.Personnummer;
import se.inera.intyg.common.support.services.BefattningService;
import se.inera.intyg.intygstyper.ts_parent.codes.DiabetesKod;
import se.inera.intygstjanster.ts.services.v1.DiabetesTypVarden;
import se.inera.intygstjanster.ts.services.v1.SkapadAv;

public final class TransportToInternalUtil {

    private static final String DELIMITER = ".";
    private static final DateTimeFormatter SIGNERINGS_TIDSTAMPEL_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private TransportToInternalUtil() {
    }

    public static GrundData buildGrundData(se.inera.intygstjanster.ts.services.v1.GrundData source) {
        GrundData grundData = new GrundData();
        grundData.setPatient(convertPatient(source.getPatient()));
        grundData
                .setSigneringsdatum(LocalDateTime.parse(source.getSigneringsTidstampel(), SIGNERINGS_TIDSTAMPEL_FORMAT));
        grundData.setSkapadAv(convertHoSPersonal(source.getSkapadAv()));
        return grundData;
    }

    public static DiabetesKod convertDiabetesTyp(DiabetesTypVarden kod) {
        switch (kod) {
        case TYP_1:
            return DiabetesKod.DIABETES_TYP_1;
        case TYP_2:
            return DiabetesKod.DIABETES_TYP_2;
        default:
            throw new IllegalArgumentException(kod.name());
        }
    }

    public static String getTextVersion(String version, String utgava) {
        return String.valueOf(Integer.parseInt(version)) + DELIMITER + String.valueOf(Integer.parseInt(utgava));
    }

    private static HoSPersonal convertHoSPersonal(SkapadAv source) {
        HoSPersonal hosPersonal = new HoSPersonal();
        hosPersonal.setFullstandigtNamn(source.getFullstandigtNamn());
        hosPersonal.setPersonId(source.getPersonId().getExtension());
        hosPersonal.setVardenhet(convertVardenhet(source.getVardenhet()));

        // try to convert befattning from description, otherwise use it as a code
        if (!CollectionUtils.isEmpty(source.getBefattningar())) {
            hosPersonal.getBefattningar().addAll(source.getBefattningar().stream()
                    .map(description -> BefattningService.getCodeFromDescription(description).orElse(description))
                    .collect(Collectors.toList()));
        }
        if (!CollectionUtils.isEmpty(source.getSpecialiteter())) {
            hosPersonal.getSpecialiteter().addAll(source.getSpecialiteter());
        }
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
