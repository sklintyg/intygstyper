/*
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
define([
], function () {
    'use strict';

    return {
        "sv" : {
        	"view.label.certtitle": "Läkarintyg vid avbeställd resa",
        	
        	//Labels
        	"rli.label.empty" : "",
          

            // Help texts

            // Validation messages
            "rli.validation.utlatande.missing" : "Utlatande saknas",

            "rli.validation.vardenhet.postadress.missing" : "Kunde inte hämta postadress för vårdenheten från HSA, måste ifyllas manuellt",
            "rli.validation.vardenhet.postnummer.missing" : "Kunde inte hämta postnummer för vårdenheten från HSA, måste ifyllas manuellt",
            "rli.validation.vardenhet.postnummer.incorrect-format" : "Postnummer måste anges i formatet XXX XX eller XXXXX (exempelvis 123 45)",
            "rli.validation.vardenhet.postort.missing" : "Kunde inte hämta postort för vårdenheten från HSA, måste ifyllas manuellt",
            "rli.validation.vardenhet.telefonnummer.missing" : "Kunde inte hämta telefonnummer för vårdenheten från HSA, måste ifyllas manuellt"
        },
        "en" : {
            "view.label.pagetitle" : "Show Certificate"
        }
    };
});
