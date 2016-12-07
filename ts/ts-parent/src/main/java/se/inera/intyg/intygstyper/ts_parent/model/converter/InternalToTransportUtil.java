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

import static se.inera.intyg.intygstyper.ts_parent.codes.RespConstants.BEFATTNINGSKOD_LAKARE_EJ_LEG_AT;

import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import com.google.common.base.Joiner;

import se.inera.intyg.common.support.Constants;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.services.BefattningService;
import se.inera.intyg.intygstyper.ts_parent.codes.DiabetesKod;
import se.inera.intygstjanster.ts.services.types.v1.II;
import se.inera.intygstjanster.ts.services.v1.*;

public final class InternalToTransportUtil {

    private static final DateTimeFormatter SIGNERINGS_TIDSTAMPEL_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    public static final String DELIMITER_REGEXP = "\\.";

    private InternalToTransportUtil() {
    }

    public static GrundData buildGrundData(se.inera.intyg.common.support.model.common.internal.GrundData source) {
        GrundData grundData = new GrundData();
        grundData.setPatient(buildPatient(source.getPatient()));
        grundData.setSigneringsTidstampel(source.getSigneringsdatum().format(SIGNERINGS_TIDSTAMPEL_FORMAT));
        grundData.setSkapadAv(buildSkapadAv(source.getSkapadAv()));
        return grundData;
    }

    public static DiabetesTypVarden convertDiabetesTyp(DiabetesKod kod) {
        switch (kod) {
        case DIABETES_TYP_1:
            return DiabetesTypVarden.TYP_1;
        case DIABETES_TYP_2:
            return DiabetesTypVarden.TYP_2;
        default:
            throw new IllegalArgumentException(kod.name());
        }
    }

    public static Optional<String> getVersion(Utlatande source) {
        if (StringUtils.isBlank(source.getTextVersion())) {
            return Optional.empty();
        }
        return Optional.of(source.getTextVersion());
    }

    private static Patient buildPatient(se.inera.intyg.common.support.model.common.internal.Patient source) {
        Patient patient = new Patient();
        patient.setEfternamn(source.getEfternamn());
        patient.setFornamn(source.getFornamn());
        patient.setFullstandigtNamn(Joiner.on(' ').skipNulls().join(source.getFornamn(), source.getEfternamn()));
        patient.setPersonId(buildII(source.getPersonId().isSamordningsNummer() ? Constants.SAMORDNING_ID_OID : Constants.PERSON_ID_OID,
                source.getPersonId().getPersonnummer()));
        patient.setPostadress(source.getPostadress());
        patient.setPostnummer(source.getPostnummer());
        patient.setPostort(source.getPostort());
        return patient;
    }

    private static SkapadAv buildSkapadAv(HoSPersonal source) {
        SkapadAv skapadAv = new SkapadAv();
        skapadAv.setAtLakare(source.getBefattningar().contains(BEFATTNINGSKOD_LAKARE_EJ_LEG_AT));
        skapadAv.setFullstandigtNamn(source.getFullstandigtNamn());
        skapadAv.setPersonId(buildII(Constants.HSA_ID_OID, source.getPersonId()));
        skapadAv.setVardenhet(buildVardenhet(source.getVardenhet()));

        // try to convert befattning to klartext
        if (!CollectionUtils.isEmpty(source.getBefattningar())) {
            skapadAv.getBefattningar().addAll(source.getBefattningar().stream()
                    .map(code -> BefattningService.getDescriptionFromCode(code).orElse(code))
                    .collect(Collectors.toList()));
        }
        if (!CollectionUtils.isEmpty(source.getSpecialiteter())) {
            skapadAv.getSpecialiteter().addAll(source.getSpecialiteter());
        }

        return skapadAv;
    }

    private static Vardenhet buildVardenhet(se.inera.intyg.common.support.model.common.internal.Vardenhet source) {
        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setEnhetsId(buildII(Constants.HSA_ID_OID, source.getEnhetsid()));
        vardenhet.setEnhetsnamn(source.getEnhetsnamn());
        vardenhet.setPostadress(source.getPostadress());
        vardenhet.setPostnummer(source.getPostnummer());
        vardenhet.setPostort(source.getPostort());
        vardenhet.setTelefonnummer(source.getTelefonnummer());
        vardenhet.setVardgivare(buildVardgivare(source.getVardgivare()));
        return vardenhet;
    }

    private static Vardgivare buildVardgivare(se.inera.intyg.common.support.model.common.internal.Vardgivare source) {
        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setVardgivarid(buildII(Constants.HSA_ID_OID, source.getVardgivarid()));
        vardgivare.setVardgivarnamn(source.getVardgivarnamn());
        return vardgivare;
    }

    private static II buildII(String root, String extension) {
        II ii = new II();
        ii.setExtension(extension);
        ii.setRoot(root);
        return ii;
    }
}
