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
'use strict';
var tsDiabetesMessages = {
    "sv": {
      "ts.label.empty" : "",
      "ts.label.certtitle" : "Läkarintyg Transportstyrelsen Diabetes",
		"ts.label.intygavser" : "Intyget avser",
		"ts.label.identitet" : "Identiteten är styrkt genom",
		"ts.label.allmant" : "Allmänt",
		"ts.label.hypoglykemier" : "Hypoglykemier (lågt blodsocker)",
		"ts.label.synintyg" : "Synintyg",
		"ts.label.synfunktioner" : "1. Synfunktioner",
		"ts.label.diabetes" : "5. Diabetes",
		"ts.label.ovrigkommentar" : "16. Övrig kommentar",
		"ts.label.bedomning" : "Bedömning",
		"ts.label.unit" : "Vårdenhet",
		"ts.label.korkort.am" : "AM",
		"ts.label.korkort.a1" : "A1",
		"ts.label.korkort.a2" : "A2",
		"ts.label.korkort.a" : "A",
		"ts.label.korkort.b" : "B",
		"ts.label.korkort.be" : "BE",
		"ts.label.korkort.traktor" : "TRAKTOR",
		"ts.label.korkort.c1" : "C1",
		"ts.label.korkort.c1e" : "C1E",
		"ts.label.korkort.c" : "C",
		"ts.label.korkort.ce" : "CE",
		"ts.label.korkort.d1" : "D1",
		"ts.label.korkort.d1e" : "D1E",
		"ts.label.korkort.d" : "D",
		"ts.label.korkort.de" : "DE",
		"ts.label.korkort.taxi" : "Taxi",
		"ts.label.korkort.annat" : "Annat",

		"ts.validation.utlatande.missing" : "Utlatande saknas",
		
		"ts.validation.vardenhet.postadress.missing" : "Kunde inte hämta postadress för vårdenheten från HSA, måste ifyllas manuellt",
		"ts.validation.vardenhet.postnummer.missing" : "Kunde inte hämta postnummer för vårdenheten från HSA, måste ifyllas manuellt",
		"ts.validation.vardenhet.postnummer.incorrect-format" : "Postnummer måste anges i formatet XXX XX eller XXXXX (exempelvis 123 45)",
		"ts.validation.vardenhet.postort.missing" : "Kunde inte hämta postort för vårdenheten från HSA, måste ifyllas manuellt",
		"ts.validation.vardenhet.telefonnummer.missing" : "Kunde inte hämta telefonnummer för vårdenheten från HSA, måste ifyllas manuellt",

		"ts.validation.sjukhusvard.missing" : "Sjukhusvård saknas",
		"ts.validation.sjukhusvard.sjukhusellerlakarkontakt.missing" : "Sjukhus eller läkarkontakt saknas",
		"ts.validation.sjukhusvard.anledning.missing" : "För vad saknas",
		"ts.validation.sjukhusvard.tidpunkt.missing" : "Om frågan besvaras med ja, när saknas",
		"ts.validation.sjukhusvard.vardinrattning.missing" : "Vårdinrättningens namn och klinik saknas",

		"ts.validation.bedomning.missing" : "Bedömning saknas",
		"ts.validation.bedomning.must-choose-one" : "En körkortstyp eller Kan inte ta ställning måste väljas",

		"ts.validation.diabetes.missing" : "Diabetes saknas",
		"ts.validation.diabetes.diabetestyp.missing" : "Diabetestyp måste anges",

		"ts.validation.intygavser.missing" : "Intyget avser körkortsbehörighet saknas",
		"ts.validation.intygavser.must-choose-one" : "Minst en körkortsbehörighet, eller Annat måste väljas",

		"ts.validation.syn.missing" : "Synfunktioner saknas",
		"ts.validation.syn.separat-ogonlakarintyg.missing" : "Om ögonläkarintyg bifogas separat eller ej måste anges",
		"ts.validation.syn.hoger.missing" : "Synfunktioner relaterade till höger öga saknas",
		"ts.validation.syn.hoger.utankorrektion.missing" : "Höger öga utan korrektion saknas",
		"ts.validation.syn.hoger.utankorrektion.out-of-bounds" : "Korrektionsvärdet för höger öga utan korrektion måste ligga i intervallet 0.0 till 2.0",
		"ts.validation.syn.hoger.medkorrektion.out-of-bounds" : "Korrektionsvärdet för höger öga med korrektion måste ligga i intervallet 0.0 till 2.0",

		"ts.validation.syn.vanster.missing" : "Synfunktioner relaterade till vänster öga saknas",
		"ts.validation.syn.vanster.utankorrektion.missing" : "Vänster öga utan korrektion saknas",
		"ts.validation.syn.vanster.utankorrektion.out-of-bounds" : "Korrektionsvärdet för vänster öga utan korrektion måste ligga i intervallet 0.0 till 2.0",
		"ts.validation.syn.vanster.medkorrektion.out-of-bounds" : "Korrektionsvärdet för vänster öga med korrektion måste ligga i intervallet 0.0 till 2.0",

		"ts.validation.syn.binokulart.missing" : "Binokulära synfunktioner saknas",
		"ts.validation.syn.binokulart.utankorrektion.missing" : "Binokulärt utan korrektion saknas",
		"ts.validation.syn.binokulart.utankorrektion.out-of-bounds" : "Korrektionsvärdet för binokulärt utan korrektion måste ligga i intervallet 0.0 till 2.0",
		"ts.validation.syn.binokulart.medkorrektion.out-of-bounds" : "Korrektionsvärdet för binokulärt med korrektion måste ligga i intervallet 0.0 till 2.0",

		"ts.validation.identitet.missing" : "Identitet styrkt saknas",
		"ts.validation.vardkontakt.missing" : "Identitet styrkt saknas",
		
		"ts.validation.hypoglykemier.missing" : "Hypoglykemier saknas",
		"ts.validation.hypoglykemier.kunskap-om-atgarder.missing" : "Patienten har kunskap om åtgärder vid hypoglykemi saknas",
		"ts.validation.hypoglykemier.tecken-nedsatt-hjarnfunktion.missing" : "Förekommer hypoglykemier med tecken på nedsatt hjärnfunktion saknas",
		"ts.validation.hypoglykemier.allvarlig-forekomst.beskrivning.missing" : "Antal episoder med allvarlig hypoglykemi saknas",
		"ts.validation.hypoglykemier.allvarlig-forekomst-trafiken.beskrivning.missing" : "Information om episoder med allvarlig hypoglykemi i trafiken saknas",
		"ts.validation.hypoglykemier.allvarlig-forekomst-vaken-tid.missing" : "Allvarlig hypoglykemi under vaken tid saknas",
		"ts.validation.hypoglykemier.allvarlig-forekomst-vaken-tid.observationstid.missing" : "Tidpunkt för allvarlig hypoglykemi under vaken tid saknas",
		"ts.validation.hypoglykemier.allvarlig-forekomst-vaken-tid.observationstid.incorrect-date" : "Tidpunkt för allvarlig hypoglykemi under vaken tid måste anges som åååå-mm-dd",
		"ts.validation.hypoglykemier.egenkontroll-blodsocker.missing" : "Genomför patienten egenkontroll av blodsocker måste anges för högre körkortsbehörigheter"
		
    },
    "en": {
        "view.label.pagetitle": "Show Certificate"
    }
};
