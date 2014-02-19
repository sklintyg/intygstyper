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
var tsBasMessages = {
	"sv" : {
		"ts.label.certtitle" : "Läkarintyg Transportstyrelsen Bas",
		"ts.label.intygavser" : "Intyget avser",
		"ts.label.identitet" : "Identiteten är styrkt genom",
		"ts.label.synfunktioner" : "1. Synfunktioner",
		"ts.label.horselbalans" : "2. Hörsel och balanssinne",
		"ts.label.funktionsnedsattning" : "3. Rörelseorganens funktioner",
		"ts.label.hjartkarl" : "4. Hjärt- och kärlsjukdomar",
		"ts.label.diabetes" : "5. Diabetes",
		"ts.label.neurologi" : "6. Neurologiska sjukdomar",
		"ts.label.medvetandestorning" : "7. Epilepsi, epileptiskt anfall och annan medvetandestörning",
		"ts.label.njurar" : "8. Njursjukdomar",
		"ts.label.kognitivt" : "9. Demens och andra kognitiva störningar",
		"ts.label.somnvakenhet" : "10. Sömn- och vakenhetsstörningar",
		"ts.label.narkotikalakemedel" : "11. Alkohol, narkotika och läkemedel",
		"ts.label.psykiskt" : "12. Psykiska sjukdomar och störningar",
		"ts.label.utvecklingsstorning" : "13. ADHD, autismspektrumtillstånd och likartade tillstånd samt psykisk utvecklingsstörning",
		"ts.label.sjukhusvard" : "14. Sjukhusvård",
		"ts.label.medicinering" : "15. Övrig medicinering",
		"ts.label.ovrigkommentar" : "16. Övrig kommentar",
		"ts.label.bedomning" : "Bedömning",
		"ts.label.unit" : "Vårdenhet",

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

		"ts.validation.sjukhusvard.missing" : "Sjukhusvård saknas",
		"ts.validation.sjukhusvard.sjukhusellerlakarkontakt.missing" : "Sjukhus eller läkarkontakt saknas",
		"ts.validation.sjukhusvard.anledning.missing" : "För vad saknas",
		"ts.validation.sjukhusvard.tidpunkt.missing" : "Om frågan besvaras med ja, när saknas",
		"ts.validation.sjukhusvard.vardinrattning.missing" : "Vårdinrättningens namn och klinik saknas",

		"ts.validation.bedomning.missing" : "Bedömning saknas",
		"ts.validation.bedomning.must-choose-one" : "En körkortstyp eller Kan inte ta ställning måste väljas",

		"ts.validation.diabetes.missing" : "Diabetes saknas",
		"ts.validation.diabetes.hardiabetes.missing" : "Patienten har diabetes måste anges",
		"ts.validation.diabetes.diabetestyp.missing" : "Diabetestyp måste anges",
		"ts.validation.diabetes.diabetestyp.must-choose-one" : "Minst en behandling måste anges för diabetes typ 2",

		"ts.validation.funktionsnedsattning.missing" : "Funktionsnedsättning saknas",
		"ts.validation.funktionsnedsattning.funktionsnedsattning.missing" : "Funktionsnedsättning saknas",
		"ts.validation.funktionsnedsattning.beskrivning.missing" : "Beskrivning av funktionsnedsättning saknas",

		"ts.validation.hjartkarl.missing" : "Hjärt- och kärlsjukdomar saknas",
		"ts.validation.hjartkarl.riskfaktorerstroke.missing" : "Riskfaktorer för stroke saknas",
		"ts.validation.hjartkarl.beskrivningriskfaktorer.missing" : "Beskrivning av riskfaktorer för stroke saknas",

		"ts.validation.horselbalans.missing" : "Hörsel och balanssinne saknas",
		"ts.validation.horselbalans.balansrubbningar.missing" : "Balansrubbningar saknas",

		"ts.validation.intygavser.missing" : "Intyget avser körkortsbehörighet saknas",
		"ts.validation.intygavser.must-choose-one" : "Minst en körkortsbehörighet, eller Annat måste väljas",

		"ts.validation.kognitivt.missing" : "Demens och kognitiva störningar saknas",
		"ts.validation.kognitivt.sviktandekognitivfunktion.missing" : "Sviktande kognitiv funktion saknas",

		"ts.validation.medicinering.missing" : "Övrig medicinering saknas",
		"ts.validation.medicinering.stadigvarandemedicinering.missing" : "Stadigvarande medicinering saknas",
		"ts.validation.medicinering.beskrivning.missing" : "Beskrivning av medicinering saknas",

		"ts.validation.narkotikalakemedel.missing" : "Alkohol, narkotika och läkemedel saknas",
		"ts.validation.narkotikalakemedel.lakarordineratlakemedelsbruk.missing" : "Läkarordinerat läkemedelsbruk saknas",
		"ts.validation.narkotikalakemedel.lakemedelochdos.missing" : "Läkemedel och dos saknas",

		"ts.validation.medvetandestorning.missing" : "Epilepsi, epileptiskt anfall och annan medvetandestörning saknas",
		"ts.validation.medvetandestorning.medvetandestorning.missing" : "Epilepsi, epileptiskt anfall och annan medvetandestörning saknas",
		"ts.validation.medvetandestorning.beskrivning.missing" : "Beskrivning av annan medvetandestörning saknas",

		"ts.validation.syn.missing" : "Synfunktioner saknas",
		"ts.validation.syn.hogeroga.missing" : "Synfunktioner relaterade till höger öga saknas",
		"ts.validation.syn.hogeroga.utankorrektion.missing" : "Höger öga utan korrektion saknas",
		"ts.validation.syn.hogeroga.utankorrektion.out-of-bounds" : "Korrektionsvärdet för höger öga utan korrektion måste ligga i intervallet 0.0 till 2.0",
		"ts.validation.syn.hogeroga.medkorrektion.out-of-bounds" : "Korrektionsvärdet för höger öga med korrektion måste ligga i intervallet 0.0 till 2.0",

		"ts.validation.syn.vansteroga.missing" : "Synfunktioner relaterade till vänster öga saknas",
		"ts.validation.syn.vansteroga.utankorrektion.missing" : "Vänster öga utan korrektion saknas",
		"ts.validation.syn.vansteroga.utankorrektion.out-of-bounds" : "Korrektionsvärdet för vänster öga utan korrektion måste ligga i intervallet 0.0 till 2.0",
		"ts.validation.syn.vansteroga.medkorrektion.out-of-bounds" : "Korrektionsvärdet för vänster öga med korrektion måste ligga i intervallet 0.0 till 2.0",

		"ts.validation.syn.binokulart.missing" : "Binokulära synfunktioner saknas",
		"ts.validation.syn.binokulart.utankorrektion.missing" : "Binokulärt utan korrektion saknas",
		"ts.validation.syn.binokulart.utankorrektion.out-of-bounds" : "Korrektionsvärdet för binokulärt utan korrektion måste ligga i intervallet 0.0 till 2.0",
		"ts.validation.syn.binokulart.medkorrektion.out-of-bounds" : "Korrektionsvärdet för binokulärt med korrektion måste ligga i intervallet 0.0 till 2.0",

		"ts.validation.utvecklingsstorning.missing" : "ADHD, autismspektrumtillstånd och likartade tillstånd samt psykisk utvecklingsstörning måste anges",
		"ts.validation.utvecklingsstorning.harsyndrom.missing" : "Har patienten till exempel ADHD, ADD, DAMP, Aspergers syndrom eller Tourettes syndrom måste anges",
		"ts.validation.utvecklingsstorning.psykiskutvecklingsstorning.missing" : "Har patienten någon psykisk utvecklingsstörning måste anges",

		"ts.validation.psykiskt.missing" : "Psykiska sjukdomar och störningar saknas",
		"ts.validation.psykiskt.psykisksjukdom.missing" : "Har eller har patienten haft psykisk sjukdom eller störning, till exempel schizofreni, annan psykos eller bipolär (manodepressiv) sjukdom måste anges",

		"ts.validation.somnvakenhet.missing" : "Sömn- och vakenhetsstörningar saknas",
		"ts.validation.somnvakenhet.teckensomnstorningar.missing" : "Har eller har patienten haft epilepsi, epileptiskt anfall eller annan medvetandestörningmåste anges",

		"ts.validation.njurar.missing" : "Njursjukdomar saknas",
		"ts.validation.njurar.nedsattnjurfunktion.missing" : "Föreligger allvarligt nedsatt njurfunktion som kan innebära en trafiksäkerhetsrisk måste anges",

		"ts.validation.neurologi.missing" : "Neurologiska sjukdomar saknas",
		"ts.validation.neurologi.neurologisksjukdom.missing" : "Finns tecken på neurologisk sjukdom måste anges",

		"ts.validation.identitet.missing" : "Identitet styrkt saknas",

	},
	"en" : {
		"view.label.pagetitle" : "Show Certificate"
	}
};
