/**
 * Copyright (C) 2013 Inera AB (http://www.inera.se)
 *
 * This file is part of Inera Certificate Modules (http://code.google.com/p/inera-certificate-modules).
 *
 * Inera Certificate Modules is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Inera Certificate Modules is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.certificate.modules.ts_diabetes.model.codes;

import se.inera.certificate.model.Kod;

public enum RekommendationsKod implements CodeSystem {

    /** Patienten uppfyller kraven för (REK8). */
    PATIENT_UPPFYLLER_KRAV_FOR("REK8", "Patienten uppfyller kraven för"),

    /** Patienten bör före ärendets avgörande undersökas av läkare med specialistkompetens i (REK9). */
    PATIENT_BOR_UNDESOKAS_AV_SPECIALIST("REK9",
            "Patienten bör före ärendets avgörande undersökas av läkare med specialistkompetens i"),
    /**
     * Lämplighet att inneha behörighet med hänsyn till de körningar och arbetsformer som är aktuella vid sådant innehav
     * (REK10).
     */
    LAMPLIGHET_INNEHA_BEHORIGHET_TILL_KORNINGAR_OCH_ARBETSFORMER("REK10",
            "Lämplighet att inneha behörighet med hänsyn till de körningar och arbetsformer som är aktuella vid sådant innehav");

    private static String codeSystemName = "kv_rekommendation_intyg";

    private static String codeSystem = "5a931b99-bda8-4f1e-8a6d-6f8a3f40a459";

    private static String codeSystemVersion = null;

    private String code;

    private String description;

    private RekommendationsKod(String code, String desc) {
        this.code = code;
        this.description = desc;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCode() {
        return this.code;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {
        return this.description;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCodeSystem() {
        return codeSystem;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCodeSystemName() {
        return codeSystemName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCodeSystemVersion() {
        return codeSystemVersion;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean matches(Kod kod) {
        return CodeConverter.matches(this, kod);
    }
}
