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
define(
		[],
		function() {
			'use strict';

			return {
				"sv" : {
		            "ts-bas.label.intygavser" : "Intyget avser",
		            "ts-bas.label.identitet" : "Identiteten är styrkt genom",
		            "ts-bas.label.synfunktioner" : "1. Synfunktioner",
		            "ts-bas.label.horselbalans" : "2. Hörsel och balanssinne",
		            "ts-bas.label.funktionsnedsattning" : "3. Rörelseorganens funktioner",
		            "ts-bas.label.hjartkarl" : "4. Hjärt- och kärlsjukdomar",
		            "ts-bas.label.diabetes" : "5. Diabetes",
		            "ts-bas.label.neurologi" : "6. Neurologiska sjukdomar",
		            "ts-bas.label.medvetandestorning" : "7. Epilepsi, epileptiskt anfall och annan medvetandestörning",
		            "ts-bas.label.njurar" : "8. Njursjukdomar",
		            "ts-bas.label.kognitivt" : "9. Demens och andra kognitiva störningar",
		            "ts-bas.label.somn-vakenhet" : "10. Sömn- och vakenhetsstörningar",
		            "ts-bas.label.narkotika-lakemedel" : "11. Alkohol, narkotika och läkemedel",
		            "ts-bas.label.psykiskt" : "12. Psykiska sjukdomar och störningar",
		            "ts-bas.label.utvecklingsstorning" : "13. ADHD, autismspektrumtillstånd och likartade tillstånd samt psykisk utvecklingsstörning",
		            "ts-bas.label.sjukhusvard" : "14. Sjukhusvård",
		            "ts-bas.label.medicinering" : "15. Övrig medicinering",
		            "ts-bas.label.ovrigkommentar" : "16. Övrig kommentar",
		            "ts-bas.label.bedomning" : "Bedömning",
		            "ts-bas.label.unit" : "Vårdenhet",

		            "ts-bas.label.korkort.c1" : "C1",
		            "ts-bas.label.korkort.c1e" : "C1E",
		            "ts-bas.label.korkort.c" : "C",
		            "ts-bas.label.korkort.ce" : "CE",
		            "ts-bas.label.korkort.d1" : "D1",
		            "ts-bas.label.korkort.d1e" : "D1E",
		            "ts-bas.label.korkort.d" : "D",
		            "ts-bas.label.korkort.de" : "DE",
		            "ts-bas.label.korkort.taxi" : "Taxi",
		            "ts-bas.label.korkort.annat" : "Annat",
		            
		            "ts-bas.label.syn.binokulart" : "Binokulärt",
		            "ts-bas.label.syn.diplopi" : "Diplopi",
		            "ts-bas.label.syn.hogeroga" : "",
		            "ts-bas.label.syn.nattblindhet" : "Nattblindhet",
		            "ts-bas.label.syn.nystagmus" : "Nystagmus",
		            "ts-bas.label.syn.progressivogonsjukdom" : "Progressiv ögonsjukdom",
		            "ts-bas.label.syn.synfaltsdefekter" : "Synfältsdefekter",
		            "ts-bas.label.syn.vansteroga" : "",
		            "ts-bas.label.syn.utankorrektion" : "",
		            "ts-bas.label.syn.medkorrektion" : "",
		            "ts-bas.label.syn.kontaktlins" : "",

		            "ts-bas.label.horselbalans.balansrubbningar" : "Balansrubbningar",
		            
		            "ts-bas.label.funktionsnedsattning.funktionsnedsattning" : "Funktionsnedsättning",
		            
		            "ts-bas.label.hjartkarl.hjart-karlsjukdom" : "Hjärt- och kärlsjukdomar",
		            "ts-bas.label.hjartkarl.hjarnskada-efter-trauma" : "Förekommer hjärnskada efter trauma",
		            "ts-bas.label.hjartkarl.riskfaktorer-stroke" : "Förekommer ökade riskfaktorer för stroke",
		            
		            "ts-bas.label.diabetes.har-diabetes" : "Har patienten diabetes",
		            "ts-bas.label.diabetes.diabetestyp" : "Diabetestyp",
		            "ts-bas.label.diabetes.diabetestyp.diabetes_typ_1" : "Diabetes Typ1",
		            "ts-bas.label.diabetes.diabetestyp.diabetes_typ_2" : "Diabets Typ2",
		            "ts-bas.label.diabetes.behandling" : "Typ av behandling(ar)",
		            "ts-bas.label.diabetes.behandling.kost" : "Kost",
		            "ts-bas.label.diabetes.behandling.tabletter" : "Tabletter",
		            "ts-bas.label.diabetes.behandling.insulin" : "Insulin",
		            
		            
		            "ts-bas.label.neurologi.neurologisksjukdom" : "Neurologisk sjukdom",
		            
		            "ts-bas.label.medvetandestorning.medvetandestorning" : "Medvetandestörning",
		            
		            "ts-bas.label.njurar.nedsatt-njurfunktion" : "Nedsatt njurfunktion",
		   
		            "ts-bas.label.kognitivt.sviktande-kognitiv-funktion" : "Sviktande kognitiv funktion",
		            
		            "ts-bas.label.somn-vakenhet.tecken-somnstorningar" : "Tecken på sömnstörningar",
		            
		            "ts-bas.label.narkotika-lakemedel.tecken-missbruk" : "Finns tecken på missbruk",
		            "ts-bas.label.narkotika-lakemedel.foremal-for-vardinsats" : "Har patienten varit föremål för vårdinsats",
		            "ts-bas.label.narkotika-lakemedel.lakarordinerat-lakemedelsbruk" : "Förekommer läkarordinerat läkemedelsebruk",
		            
		            "ts-bas.label.psykiskt.psykisksjukdom" : "Psykisk sjukdom",
		            
		            "ts-bas.label.utvecklingsstorning.psykisk-utvecklingsstorning" : "Psykisk utvecklingsstörning",
		            "ts-bas.label.utvecklingsstorning.har-syndrom" : "Syndrom av olika slag",
		            
		            "ts-bas.label.sjukhusvard.sjukhus-eller-lakarkontakt" : "Har patienten vårdats på sjukhus eller haft övrig kontakt med läkare",
		            
		            "ts-bas.label.medicinering.stadigvarande-medicinering" : "Förekommer stadigvarande medicinering",
		            
		            "ts-bas.label.true" : "JA",
		            "ts-bas.label.false" : "NEJ",
		            		            
		            "ts-bas.label.certtitle" : "Läkarintyg Transportstyrelsen Bas",
					"ts-bas.label.pagetitle" : "Granska och skicka",
					"ts-bas.label.pagedescription" : "Här kan du titta på ditt intyg och skicka det. Om du upptäcker några felaktigheter så hör av dig enheten som utfärdade ditt intyg. Du kan även öppna intyget som en PDF och skriva ut och spara.",
					"ts-bas.label.patientname" : "Patientens namn:",
					"ts-bas.label.issued" : "Utfärdat:",
					"ts-bas.label.civicnumber" : "Personnr:",
					"ts-bas.label.issuer" : "Utfärdare:",
					"ts-bas.label.period" : "Period:",
					"ts-bas.label.errorpagetitle" : "Ett problem har uppstått",
					"ts-bas.message.certifits-basloading" : "Hämtar intyg...",
					"button.sendtofk" : "Skicka",
					"button.downloadprint" : "Ladda ner / Skriv ut",
					"button.send" : "Skicka",
					"button.cancel" : "Avbryt",
					"button.goback" : "Tillbaka",
					"error.generic" : "Kunde inte visa intyget",
					"ts-bas.label.latestevent" : "Senaste händelsen",
					"ts-bas.label.latestevent.noevents" : "Inga händelser",
					"ts-bas.label.latestevent.showall" : "Visa alla händelser",
					"history.label.pagetitle" : "Intygets alla händelser",
					"ts-bas.status.sent" : "Skickat till",
					"ts-bas.target.mi" : "Försäkringsbolaget",
						
					"ts-bas.label.confirmedby" : "Ovanstående uppgifter och bedömningar har bekräftas",
			        "ts-bas.label.date" : "Datum",
			        "ts-bas.label.contactinfo" : "Namnförtydligande, mottagningens adress och telefon",
			        
			        "ts-bas.label.bedomning-info-alt-1":"Patienten uppfyller kraven enligt Transportstyrelsens föreskrifter och allmänna råd om medicinska krav för innehav av körkort m.m. (TSFS 2010:125, senast ändrade genom TSFS 2013:2) för:",
			        "ts-bas.label.bedomning.kan-inte-ta-stallning":"Kan inte ta ställning",
			        "ts-bas.label.bedomning-info-undersokas-med-specialkompetens":"Patienten bör före ärendets avgörande undersökas av läkare med specialistkompetens i:",
			        "ts-bas.label.bedomning-info-ej-angivet":"Ej angivet",

				},
				"en" : {
					"ts-bas.label.pagetitle" : "Show Certificate"
				}
			};
		});
