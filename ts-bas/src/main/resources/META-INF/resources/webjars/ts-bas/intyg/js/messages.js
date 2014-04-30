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
		            "ts-bas.label.syn.diplopi" : "Framkommer dubbelseende vid prövning av ögats rörlighet (prövningen ska göras i de åtta huvudmeridianerna)?",
		            "ts-bas.label.syn.8-dioptrier" : "Högsta styrka i något av glasen överskrider 8 dioptrier:",
		            "ts-bas.label.syn.nattblindhet" : "b) Framkommer anamnestiska uppgifter om begränsning av seendet vid nedsatt belysning?",
		            "ts-bas.label.syn.nystagmus" : "Förekommer nystagmus?",
		            "ts-bas.label.syn.progressivogonsjukdom" : " c) Har patienten någon progressiv ögonsjukdom?",
		            "ts-bas.label.syn.synfaltsdefekter" : "a) Finns tecken på synfältsdefekter vid undersökning enligt Donders konfrontationsmetod?",
		            
		            "ts-bas.label.syn.synskarpa" : "Värden för synskärpa",
		            "ts-bas.label.syn.hogeroga" : "",
		            "ts-bas.label.syn.vansteroga" : "",
		            "ts-bas.label.syn.utankorrektion" : "",
		            "ts-bas.label.syn.medkorrektion" : "",
		            "ts-bas.label.syn.kontaktlins" : "",

		            "ts-bas.label.horselbalans.balansrubbningar" : "Har patienten överraskande anfall av balansrubbningar eller yrsel?",
		            
		            "ts-bas.label.funktionsnedsattning.funktionsnedsattning" : "Har patienten någon sjukdom eller funktionsnedsättning som påverkar rörligheten och som medför att fordon inte kan köras på ett trafiksäkert sätt?",
		            "ts-bas.label.funktionsnedsattning.beskrivning" : "Typ av nedsättning eller sjukdom:",
		            
		            "ts-bas.label.hjartkarl.hjart-karlsjukdom" : "Föreligger hjärt- eller kärlsjukdom som kan medföra en påtaglig risk för att hjärnans funktioner akut försämras eller som i övrigt innebär en trafiksäkerhetsrisk?",
		            "ts-bas.label.hjartkarl.hjarnskada-efter-trauma" : "Finns tecken på hjärnskada eller trauma, stroke eller annan sjukdom i centrala nervsystemet?",
		            "ts-bas.label.hjartkarl.riskfaktorer-stroke" : "Föreligger viktiga riskfaktorer för stroke (tidigare stroke eller TIA, förhöjt blodtryck, förmaksflimmer eller kärlmissbildning)?",
		            "ts-bas.label.hjartkarl.riskfaktorer-stroke.beskrivning" : "Typ av sjukdom:",
		            
		            "ts-bas.label.diabetes.har-diabetes" : "Har patienten diabetes",
		            "ts-bas.label.diabetes.diabetestyp" : "Diabetestyp",
		            "ts-bas.label.diabetes.diabetestyp.diabetes_typ_1" : "Diabetes Typ1",
		            "ts-bas.label.diabetes.diabetestyp.diabetes_typ_2" : "Diabets Typ2",
		            "ts-bas.label.diabetes.behandling" : "Behandling:",
		            "ts-bas.label.diabetes.behandling.kost" : "Kost",
		            "ts-bas.label.diabetes.behandling.tabletter" : "Tabletter",
		            "ts-bas.label.diabetes.behandling.insulin" : "Insulin",

		            "ts-bas.label.neurologi.neurologisksjukdom" : "Finns tecken på neurologisk sjukdom?",

		            "ts-bas.label.medvetandestorning.medvetandestorning" : "Har eller har patienten haft epilepsi, epileptiskt anfall eller annan medvetandestörning?",
		            "ts-bas.label.medvetandestorning.beskrivning" : "Om ja. Information om när den inträffade och orsak:",

		            "ts-bas.label.njurar.nedsatt-njurfunktion" : "Föreligger allvarligt nedsatt njurfunktion som kan innebära en trafiksäkerhetsrisk?",

		            "ts-bas.label.kognitivt.sviktande-kognitiv-funktion" : "Finns tecken på sviktande kognitiv funktion?",

		            "ts-bas.label.somn-vakenhet.tecken-somnstorningar" : "Finns tecken på, eller anamnestiska uppgifter som talar för sömn- eller vakenhetsstörning?",

		            "ts-bas.label.narkotika-lakemedel.tecken-missbruk" : "a) Finns journaluppgifter, anamnestiska uppgifter, resultat av laboratorieprover eller andra tecken på missbruk eller beroende av alkohol, narkotika eller läkemedel?",
		            "ts-bas.label.narkotika-lakemedel.foremal-for-vardinsats" : "b) Har patienten vid något tillfälle varit föremål för vårdinsatser för missbruk eller beroende av alkohol, narkotika eller läkemedel?",
		            "ts-bas.label.narkotika-lakemedel.behov-provtagning" : "Behövs det provtagning avseende aktuellt bruk av akohol eller narkotika?",
		            "ts-bas.label.narkotika-lakemedel.lakarordinerat-lakemedelsbruk" : "c) Pågår regelbundet läkarordinerat bruk av läkemedel som kan innebära en trafiksäkerhetsrisk?",
		            "ts-bas.label.narkotika-lakemedel.beskrivning" : "Om JA på C. Information om läkemedel och ordinerad dos:",

		            "ts-bas.label.psykiskt.psykisksjukdom" : "Har eller har patienten haft psykisk sjukdom eller störning, till exempel schizofreni, annan psykos eller bipolär (manodepressiv) sjukdom?",

		            "ts-bas.label.utvecklingsstorning.psykisk-utvecklingsstorning" : "a) Har patienten någon psykisk utvecklingsstörning?",
		            "ts-bas.label.utvecklingsstorning.har-syndrom" : "b) Har patienten till exempel ADHD, ADD, DAMP, Aspergers syndrom eller Tourettes syndrom?",

		            "ts-bas.label.sjukhusvard.sjukhus-eller-lakarkontakt" : "Har patienten vårdats på sjukhus eller haft kontakt med läkare med anledning av fälten 1-13?",
		            "ts-bas.label.sjukhusvard.vardinrattning" : "Vårdinrättningens namn och klinik/er:",
		            "ts-bas.label.sjukhusvard.anledning" : "För vad?",
		            "ts-bas.label.sjukhusvard.tidpunkt" : "Om frågan besvarats med ja, när?",

		            "ts-bas.label.medicinering.stadigvarande-medicinering" : "Har patienten någon stadigvarande medicinering?",
		            "ts-bas.label.medicinering.beskrivning" : "Vilken eller vilka mediciner:",

		            "ts-bas.label.true" : "JA",
		            "ts-bas.label.false" : "NEJ",

		            "ts-bas.label.certtitle" : "Transportstyrelsens läkarintyg",
		            "ts-bas.label.pagetitle.step1" : "Skicka intyg steg 1/2 - Granska och skicka",
		            "ts-bas.label.pagetitle.step2" : "Skicka intyg steg 2/2 - Summering",
		            "ts-bas.confirm.label.summarypagedesc" : "Du har valt att skicka följande intyg: ",
		            "ts-bas.confirm.label.recipienttitle" : "Mottagare: ",
		            "ts-bas.confirm.label.verifytext" : "Kontrollera att uppgifterna ovan stämmer och tryck på Skicka.",
		            "ts-bas.sent.label.pageinformation" : "Intyget har nu skickats och nedan presenteras försändelsen.",

		            "ts-bas.typename" : " - Bas",
		            "ts-bas.label.pagetitle" : "Granska och skicka intyg",
		            "ts-bas.button.send" : "Skicka",
		            "ts-bas.common.cancel" : "Avbryt",

		            "ts-button.detail.send" : "Skicka intyg",
					"ts-bas.label.pagedescription" : "<p>Här kan du läsa ditt intyg och skicka det elektroniskt till olika Transportstyrelsen.</p> <p>För att skicka intyget till Transportstyrelsen så klickar du på Skicka intyg.</p> <p>Vill du spara eller skriva ut ditt intyg så väljer du Ladda ner / Skriv ut. Intyget öppnas eller laddas ner som en pdf-fil som du kan spara på din dator eller skriva ut</p>",
					"ts-bas.label.cert.inaccuracies" : "Om du upptäcker några felaktigheter så hör av dig enheten som utfärdade ditt intyg.",
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
			        
			        //Helptexts
			        
			        "ts-bas.helptext.synfunktioner.a-c" : "Då någon av a - c besvarats med JA krävs ett läkarintyg från ögonspecialist.",
			        "ts-bas.helptext.synfunktioner.8-dioptrier" : "Då högsta styrka i något av glasen överskrider 8 dioptrier besvarats med JA måste Intyg om korrektionsglasens styrka skickas in.",
			        "ts-bas.helptext.diabetes.behandling" : "Vid tablett- eller insulinbehandlad diabetes krävs det att ett läkarintyg gällande sjukdomen skickas in.",
			        "ts-bas.helptext.narkotika-lakemedel.provtagning" : "Om ja på ovanstående ska resultatet redovisas separat.",

			        "ts-bas.inbox.complementaryinfo" : "Intyget avser"

				},
				"en" : {
					"ts-bas.label.pagetitle" : "Show Certificate"
				}
			};
		});
