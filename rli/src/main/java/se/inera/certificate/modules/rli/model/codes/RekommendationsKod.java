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
package se.inera.certificate.modules.rli.model.codes;


public enum RekommendationsKod implements ICodeSystem {

    REK1("REK1", 
            "Patientens tillstånd innebär att resan innebär risk för men. Patienten rekommenderas därför att inte genomföra resan."), 
    REK2("REK2", 
            "Patientens tillstånd innebär inte någon ökad risk för men vid resa. Patienten rekommenderas därför inte att avstå från resan."),
    REK5("REK5", 
            "Patientens tillstånd föranleder speciell omvårdnad. Resenären rekommenderas därför att som närstående ställa in resan och ge denna omvårdnad."), 
    REK6("REK6",
            "Patientens tillstånd är allvarligt. Resenären rekommenderas därför som närstående till patienten att ej genomföra resan."), 
    REK7("REK7", 
            "Patientens tillstånd är inte så allvarligt att resenären behöver avstå resan. Resenären avrådes därför inte från resan.");

    private static String codeSystemName = "kv_rekommendation_intyg";

    private static String codeSystem = "5a931b99-bda8-4f1e-8a6d-6f8a3f40a459";

    private static String codeSystemVersion = null;

    private String code;

    private String description;

    private RekommendationsKod(String code, String desc) {
        this.code = code;
        this.description = desc;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public String getCodeSystem() {
        return codeSystem;
    }

    @Override
    public String getCodeSystemName() {
        return codeSystemName;
    }

    @Override
    public String getCodeSystemVersion() {
        return codeSystemVersion;
    }
}
