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
            "ts-bas.label.certtitle" : "Läkarintyg Transportstyrelsen Bas",
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
            "ts-bas.label.somnvakenhet" : "10. Sömn- och vakenhetsstörningar",
            "ts-bas.label.narkotikalakemedel" : "11. Alkohol, narkotika och läkemedel",
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

            "ts-bas.helptext.lakaren-ska-uppmarksamma" : "Läkaren ska uppmärksamma Transportstyrelsens föreskrifter och allmänna råd om medicinska krav för innehav av körkort m.m. (TSFS 2010:125, senast ändrade genom TSFS 2013:2). Intyget skall utfärdas i enlighet med vad som sägs i 17 kap. och får inte vara äldre än två månader när det inkommer till Transportstyrelsen. Se: <a href='http://www.transportstyrelsen.se'>http://www.transportstyrelsen.se</a>. Därefter 'Väg' och 'Trafikmedicin'.",
            "ts-bas.helptext.intyg-avser" : "<span style='text-align:left'>C1 = medeltung lastbil,<br/> C1E = medeltung lastbil och ett eller flera släpfordon oavsett vikt,<br/> C = tung lastbil och enbart ett lätt släpfordon,<br/> CE = tung lastbil och ett eller flera släpfordon oavsett vikt,<br/> D1 = mellanstor buss,<br/> D1E = mellanstor buss och ett eller flera släpfordon oavsett vikt,<br/> D = buss och enbart ett lätt släpfordon,<br/> DE = buss och ett eller flera släpfordon oavsett vikt,<br/> E = tungt släpfordon,<br/> Taxi = taxiförarlegitimation</span>",
            "ts-bas.helptext.intyg-avser.grupp2-grupp3" : "Grupp II omfattar behörigheterna AM, A1, A2, A, B, BE, C1, C1E, C och CE.<br/> Grupp III omfattar behörigheterna AM, A1, A2, A, B, BE, C1, C1E, C, CE, D1, D1E, D och DE.'",

            "ts-bas.helptext.identitet-styrkt-genom.id-kort" : "ID-kort = SIS-märkt ID-kort, svenskt nationellt ID-kort eller ID-kort utfärdat av Skatteverket.",
            "ts-bas.helptext.identitet-styrkt-genom.foretag-tjanstekort" : "Företagskort eller tjänstekort = SIS-märkt företagskort eller tjänstekort.",
            "ts-bas.helptext.identitet-styrkt-genom.forsakran" : "Försäkran enligt 18 kap. 4 § = Försäkran enligt 18 kap 4 § i Transportstyrelsens föreskrifter (TSFS 2010:125, senast ändrade genom TSFS 2013:2): Identiteten får fastställas genom att en förälder, annan vårdnadshavare, make, maka eller sambo, registrerad partner eller myndigt barn skriftligen försäkrar att lämnade uppgifter om sökandens identitet är riktiga. Den som lämnar en sådan försäkran ska vara närvarande vid identitetskontrollen och kunna styrka sin egen identitet.",
            "ts-bas.helptext.identitet-styrkt-genom.pass" : "Pass = Svenskt EU-pass, annat EU-pass utfärdade från och med den 1 september 2006, pass utfärdat av Island, Liechtenstein, Norge eller Schweiz från och med den 1 september 200",

            "ts-bas.helptext.synfunktioner.om-nagon-av-a-c" : "Om någon av frågorna a-c besvaras med ja eller om det bedöms sannolikt att synfältsdefekter föreligger krävs läkarintyg av ögonspecialist.",
            "ts-bas.helptext.synfunktioner.synskarpa-kan-grundas-pa-tidigare" : "OBS! Uppgifterna om synskärpa och korrektion kan grundas på tidigare utförd undersökning av bland annat legitimerad optiker. Uppgifterna ska då ingå som underlag vid läkarens samlade bedömning.",
            "ts-bas.helptext.synfunktioner.info-8-dioptrier" : "Intyg om korrektionsglasens styrka måste skickas in",
            "ts-bas.helptext.synfunktioner.8-dioptrier-valt" : "Du har kryssat i frågan om 8 dioptrier – Glöm inte att skicka in Intyg som korrektionsglasens styrka.",
            "ts-bas.helptext.synfunktioner.utan-korrektion" : "Uppgiften är obligatorisk",
            "ts-bas.helptext.synfunktioner.med-korrektion" : "Uppgiften är obligatorisk om föreskriven synskärpa endast uppnås med korrektion. Definition av föreskriven synskärpa finns i Transportstyrelsens föreskrifter.",

            "ts-bas.helptext.diabetes.tabletter-eller-insulin" : "Har patienten tablett- eller insulinbehandlad diabetes krävs ett läkarintyg gällande sjukdomen",

            "ts-bas.helptext.alkohol-narkotika.provtagning" : "Om provtagning görs ska resultatet redovisas separat",

            "ts-bas.helptext.bedomning.info" : "Om någon av frågorna har besvarats med ja, ska de krav på ytterligare underlag som framgår av föreskrifterna beaktas.",

            // Validation messages starting
            "ts-bas.validation.utlatande.missing" : "Utlatande saknas",

            "ts-bas.validation.vardenhet.postadress.missing" : "Kunde inte hämta postadress för vårdenheten från HSA, måste ifyllas manuellt",
            "ts-bas.validation.vardenhet.postnummer.missing" : "Kunde inte hämta postnummer för vårdenheten från HSA, måste ifyllas manuellt",
            "ts-bas.validation.vardenhet.postnummer.incorrect-format" : "Postnummer måste anges i formatet XXX XX eller XXXXX (exempelvis 123 45)",
            "ts-bas.validation.vardenhet.postort.missing" : "Kunde inte hämta postort för vårdenheten från HSA, måste ifyllas manuellt",
            "ts-bas.validation.vardenhet.telefonnummer.missing" : "Kunde inte hämta telefonnummer för vårdenheten från HSA, måste ifyllas manuellt",

            "ts-bas.validation.sjukhusvard.missing" : "Objektet sjukhusvård saknas",
            "ts-bas.validation.sjukhusvard.sjukhusellerlakarkontakt.missing" : "Sjukhus eller läkarkontakt saknas",
            "ts-bas.validation.sjukhusvard.anledning.missing" : "Anledning till sjukhusvård saknas",
            "ts-bas.validation.sjukhusvard.tidpunkt.missing" : "Tidpunkt för sjukhusvård saknas",
            "ts-bas.validation.sjukhusvard.vardinrattning.missing" : "Vårdinrättningens namn och klinik saknas",

            "ts-bas.validation.bedomning.missing" : "Bedömning saknas",
            "ts-bas.validation.bedomning.must-choose-one" : "En körkortstyp eller Kan inte ta ställning måste väljas",

            "ts-bas.validation.diabetes.missing" : "Diabetes saknas",
            "ts-bas.validation.diabetes.hardiabetes.missing" : "Patienten har diabetes måste anges",
            "ts-bas.validation.diabetes.diabetestyp.missing" : "Diabetestyp måste anges",
            "ts-bas.validation.diabetes.diabetestyp.must-choose-one" : "Minst en behandling måste anges för diabetes typ 2",

            "ts-bas.validation.funktionsnedsattning.missing" : "Funktionsnedsättning saknas",
            "ts-bas.validation.funktionsnedsattning.funktionsnedsattning.missing" : "Funktionsnedsättning saknas",
            "ts-bas.validation.funktionsnedsattning.beskrivning.missing" : "Beskrivning av funktionsnedsättning saknas",
            "ts-bas.validation.funktionsnedsattning.otillrackligrorelseformaga.missing" : "För innehav av behörighet D1, D1E, D, DE eller Taxi måste rörelseförmaga att hjälpa passagerar anges",

            "ts-bas.validation.hjartkarl.missing" : "Hjärt- och kärlsjukdomar saknas",
            "ts-bas.validation.hjartkarl.hjartkarlsjukdom.missing" : "Hjärt- och kärlsjukdom som kan medföra en påtaglig risk för ... måste anges",
            "ts-bas.validation.hjartkarl.hjarnskadaeftertrauma.missing" : "Hjärnskada efter trauma måste anges",
            "ts-bas.validation.hjartkarl.riskfaktorerstroke.missing" : "Riskfaktorer för stroke saknas",
            "ts-bas.validation.hjartkarl.beskrivningriskfaktorer.missing" : "Beskrivning av riskfaktorer för stroke saknas",

            "ts-bas.validation.horselbalans.missing" : "Hörsel och balanssinne saknas",
            "ts-bas.validation.horselbalans.balansrubbningar.missing" : "Balansrubbningar saknas",
            "ts-bas.validation.horselbalans.uppfattasamtal4meter.missing" : "Svårt att uppfatta samtal på fyra meters avstånd måste anges för innehav av behörigheterna D1, D1E, D, DE eller Taxi",

            "ts-bas.validation.intygavser.missing" : "Intyget avser körkortsbehörighet saknas",
            "ts-bas.validation.intygavser.must-choose-one" : "Minst en körkortsbehörighet, eller Annat måste väljas",

            "ts-bas.validation.kognitivt.missing" : "Demens och kognitiva störningar saknas",
            "ts-bas.validation.kognitivt.sviktandekognitivfunktion.missing" : "Sviktande kognitiv funktion saknas",

            "ts-bas.validation.medicinering.missing" : "Övrig medicinering saknas",
            "ts-bas.validation.medicinering.stadigvarandemedicinering.missing" : "Stadigvarande medicinering saknas",
            "ts-bas.validation.medicinering.beskrivning.missing" : "Beskrivning av medicinering saknas",

            "ts-bas.validation.narkotikalakemedel.missing" : "Alkohol, narkotika och läkemedel saknas",
            "ts-bas.validation.narkotikalakemedel.teckenmissbruk.missing" : "Finns journaluppgifter, anamnestiska uppgifter, resultat av laboratorieprover eller andra tecken på missbruk saknas",
            "ts-bas.validation.narkotikalakemedel.vardinsats-bas.missing" : " Har patienten vid något tillfälle varit föremål för vårdinsatser för missbruk eller beroende av alkohol, narkotika eller läkemedel saknas",
            "ts-bas.validation.narkotikalakemedel.provtagning-behovs.missing" : "Om tecken på missbruk föreligger måste provtagning avseende aktuellt bruk av alkohol eller narkotika vara angett",
            "ts-bas.validation.narkotikalakemedel.lakarordineratlakemedelsbruk.missing" : "Läkarordinerat läkemedelsbruk saknas",
            "ts-bas.validation.narkotikalakemedel.lakemedelochdos.missing" : "Läkemedel och dos saknas",

            "ts-bas.validation.medvetandestorning.missing" : "Epilepsi, epileptiskt anfall och annan medvetandestörning saknas",
            "ts-bas.validation.medvetandestorning.medvetandestorning.missing" : "Epilepsi, epileptiskt anfall och annan medvetandestörning saknas",
            "ts-bas.validation.medvetandestorning.beskrivning.missing" : "Beskrivning av annan medvetandestörning saknas",

            "ts-bas.validation.syn.missing" : "Synfunktioner saknas",
            "ts-bas.validation.syn.progressiv-ogonsjukdom.missing" : "Har patienten någon progressiv ögonsjukdom saknas",
            "ts-bas.validation.syn.tecken-synfaltsdefekter.missing" : "Finns tecken på synfältsdefekter saknas",
            "ts-bas.validation.syn.nattblindhet.missing" : "Framkommer anamnestiska uppgifter om begränsning av seendet vid nedsatt belysning saknas",
            "ts-bas.validation.syn.diplopi.missing" : "Framkommer dubbelseende saknas",
            "ts-bas.validation.syn.nystagmus.missing" : "Förekommer nystagmus saknas",
            "ts-bas.validation.syn.hogeroga.missing" : "Synfunktioner relaterade till höger öga saknas",
            "ts-bas.validation.syn.hogeroga.utankorrektion.missing" : "Höger öga utan korrektion saknas",
            "ts-bas.validation.syn.hogeroga.utankorrektion.out-of-bounds" : "Korrektionsvärdet för höger öga utan korrektion måste ligga i intervallet 0.0 till 2.0",
            "ts-bas.validation.syn.hogeroga.medkorrektion.out-of-bounds" : "Korrektionsvärdet för höger öga med korrektion måste ligga i intervallet 0.0 till 2.0",

            "ts-bas.validation.syn.vansteroga.missing" : "Synfunktioner relaterade till vänster öga saknas",
            "ts-bas.validation.syn.vansteroga.utankorrektion.missing" : "Vänster öga utan korrektion saknas",
            "ts-bas.validation.syn.vansteroga.utankorrektion.out-of-bounds" : "Korrektionsvärdet för vänster öga utan korrektion måste ligga i intervallet 0.0 till 2.0",
            "ts-bas.validation.syn.vansteroga.medkorrektion.out-of-bounds" : "Korrektionsvärdet för vänster öga med korrektion måste ligga i intervallet 0.0 till 2.0",

            "ts-bas.validation.syn.binokulart.missing" : "Binokulära synfunktioner saknas",
            "ts-bas.validation.syn.binokulart.utankorrektion.missing" : "Binokulärt utan korrektion saknas",
            "ts-bas.validation.syn.binokulart.utankorrektion.out-of-bounds" : "Korrektionsvärdet för binokulärt utan korrektion måste ligga i intervallet 0.0 till 2.0",
            "ts-bas.validation.syn.binokulart.medkorrektion.out-of-bounds" : "Korrektionsvärdet för binokulärt med korrektion måste ligga i intervallet 0.0 till 2.0",

            "ts-bas.validation.utvecklingsstorning.missing" : "ADHD, autismspektrumtillstånd och likartade tillstånd samt psykisk utvecklingsstörning måste anges",
            "ts-bas.validation.utvecklingsstorning.harsyndrom.missing" : "Har patienten till exempel ADHD, ADD, DAMP, Aspergers syndrom eller Tourettes syndrom måste anges",
            "ts-bas.validation.utvecklingsstorning.psykiskutvecklingsstorning.missing" : "Har patienten någon psykisk utvecklingsstörning måste anges",

            "ts-bas.validation.psykiskt.missing" : "Psykiska sjukdomar och störningar saknas",
            "ts-bas.validation.psykiskt.psykisksjukdom.missing" : "Har eller har patienten haft psykisk sjukdom eller störning, till exempel schizofreni, annan psykos eller bipolär (manodepressiv) sjukdom måste anges",

            "ts-bas.validation.somnvakenhet.missing" : "Sömn- och vakenhetsstörningar saknas",
            "ts-bas.validation.somnvakenhet.teckensomnstorningar.missing" : "Har eller har patienten haft epilepsi, epileptiskt anfall eller annan medvetandestörningmåste anges",

            "ts-bas.validation.njurar.missing" : "Njursjukdomar saknas",
            "ts-bas.validation.njurar.nedsattnjurfunktion.missing" : "Föreligger allvarligt nedsatt njurfunktion som kan innebära en trafiksäkerhetsrisk måste anges",

            "ts-bas.validation.neurologi.missing" : "Neurologiska sjukdomar saknas",
            "ts-bas.validation.neurologi.neurologisksjukdom.missing" : "Finns tecken på neurologisk sjukdom måste anges",

            "ts-bas.validation.identitet.missing" : "Identitet styrkt saknas"

        },
        "en" : {
            "ts-bas.label.pagetitle" : "Show Certificate"
        }
    };
});
