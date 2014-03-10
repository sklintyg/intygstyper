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

        "ts.helptext.lakaren-ska-uppmarksamma" : "Läkaren ska uppmärksamma Transportstyrelsens föreskrifter och allmänna råd om medicinska krav för innehav av körkort m.m. (TSFS 2010:125, senast ändrade genom TSFS 2013:2). Intyget skall utfärdas i enlighet med vad som sägs i 17 kap. och får inte vara äldre än två månader när det inkommer till Transportstyrelsen. Se: <a href='http://www.transportstyrelsen.se'>http://www.transportstyrelsen.se</a>. Därefter 'Väg' och 'Trafikmedicin'.",
        "ts.helptext.intyg-avser" : "<span style='text-align:left'>C1 = medeltung lastbil,<br/> C1E = medeltung lastbil och ett eller flera släpfordon oavsett vikt,<br/> C = tung lastbil och enbart ett lätt släpfordon,<br/> CE = tung lastbil och ett eller flera släpfordon oavsett vikt,<br/> D1 = mellanstor buss,<br/> D1E = mellanstor buss och ett eller flera släpfordon oavsett vikt,<br/> D = buss och enbart ett lätt släpfordon,<br/> DE = buss och ett eller flera släpfordon oavsett vikt,<br/> E = tungt släpfordon,<br/> Taxi = taxiförarlegitimation</span>",
        "ts.helptext.intyg-avser.grupp2-grupp3" : "Grupp II omfattar behörigheterna AM, A1, A2, A, B, BE, C1, C1E, C och CE.<br/> Grupp III omfattar behörigheterna AM, A1, A2, A, B, BE, C1, C1E, C, CE, D1, D1E, D och DE.'",

        "ts.helptext.identitet-styrkt-genom.id-kort" : "ID-kort = SIS-märkt ID-kort, svenskt nationellt ID-kort eller ID-kort utfärdat av Skatteverket.",
        "ts.helptext.identitet-styrkt-genom.foretag-tjanstekort" : "Företagskort eller tjänstekort = SIS-märkt företagskort eller tjänstekort.",
        "ts.helptext.identitet-styrkt-genom.forsakran" : "Försäkran enligt 18 kap. 4 § = Försäkran enligt 18 kap 4 § i Transportstyrelsens föreskrifter (TSFS 2010:125, senast ändrade genom TSFS 2013:2): Identiteten får fastställas genom att en förälder, annan vårdnadshavare, make, maka eller sambo, registrerad partner eller myndigt barn skriftligen försäkrar att lämnade uppgifter om sökandens identitet är riktiga. Den som lämnar en sådan försäkran ska vara närvarande vid identitetskontrollen och kunna styrka sin egen identitet.",
        "ts.helptext.identitet-styrkt-genom.pass" : "Pass = Svenskt EU-pass, annat EU-pass utfärdade från och med den 1 september 2006, pass utfärdat av Island, Liechtenstein, Norge eller Schweiz från och med den 1 september 200",

        "ts.helptext.synfunktioner.om-nagon-av-a-c" : "Om någon av frågorna a-c besvaras med ja eller om det bedöms sannolikt att synfältsdefekter föreligger krävs läkarintyg av ögonspecialist.",
        "ts.helptext.synfunktioner.synskarpa-kan-grundas-pa-tidigare" : "OBS! Uppgifterna om synskärpa och korrektion kan grundas på tidigare utförd undersökning av bland annat legitimerad optiker. Uppgifterna ska då ingå som underlag vid läkarens samlade bedömning.",
        "ts.helptext.synfunktioner.info-8-dioptrier" : "Intyg om korrektionsglasens styrka måste skickas in",
        "ts.helptext.synfunktioner.8-dioptrier-valt" : "Du har kryssat i frågan om 8 dioptrier – Glöm inte att skicka in Intyg som korrektionsglasens styrka.",
        "ts.helptext.synfunktioner.utan-korrektion" : "Uppgiften är obligatorisk",
        "ts.helptext.synfunktioner.med-korrektion" : "Uppgiften är obligatorisk om föreskriven synskärpa endast uppnås med korrektion. Definition av föreskriven synskärpa finns i Transportstyrelsens föreskrifter.",

        "ts.helptext.diabetes.tabletter-eller-insulin" : "Har patienten tablett- eller insulinbehandlad diabetes krävs ett läkarintyg gällande sjukdomen",

        "ts.helptext.alkohol-narkotika.provtagning" : "Om provtagning görs ska resultatet redovisas separat",

        "ts.helptext.bedomning.info" : "Om någon av frågorna har besvarats med ja, ska de krav på ytterligare underlag som framgår av föreskrifterna beaktas.",

        // Validation messages starting
        "ts.validation.utlatande.missing" : "Utlatande saknas",

        "ts.validation.vardenhet.postadress.missing" : "Kunde inte hämta postadress för vårdenheten från HSA, måste ifyllas manuellt",
        "ts.validation.vardenhet.postnummer.missing" : "Kunde inte hämta postnummer för vårdenheten från HSA, måste ifyllas manuellt",
        "ts.validation.vardenhet.postnummer.incorrect-format" : "Postnummer måste anges i formatet XXX XX eller XXXXX (exempelvis 123 45)",
        "ts.validation.vardenhet.postort.missing" : "Kunde inte hämta postort för vårdenheten från HSA, måste ifyllas manuellt",
        "ts.validation.vardenhet.telefonnummer.missing" : "Kunde inte hämta telefonnummer för vårdenheten från HSA, måste ifyllas manuellt",

        "ts.validation.sjukhusvard.missing" : "Objektet sjukhusvård saknas",
        "ts.validation.sjukhusvard.sjukhusellerlakarkontakt.missing" : "Sjukhus eller läkarkontakt saknas",
        "ts.validation.sjukhusvard.anledning.missing" : "Anledning till sjukhusvård saknas",
        "ts.validation.sjukhusvard.tidpunkt.missing" : "Tidpunkt för sjukhusvård saknas",
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
        "ts.validation.funktionsnedsattning.otillrackligrorelseformaga.missing" : "För innehav av behörighet D1, D1E, D, DE eller Taxi måste rörelseförmaga att hjälpa passagerar anges",

        "ts.validation.hjartkarl.missing" : "Hjärt- och kärlsjukdomar saknas",
        "ts.validation.hjartkarl.hjartkarlsjukdom.missing" : "Hjärt- och kärlsjukdom som kan medföra en påtaglig risk för ... måste anges",
        "ts.validation.hjartkarl.hjarnskadaeftertrauma.missing" : "Hjärnskada efter trauma måste anges",
        "ts.validation.hjartkarl.riskfaktorerstroke.missing" : "Riskfaktorer för stroke saknas",
        "ts.validation.hjartkarl.beskrivningriskfaktorer.missing" : "Beskrivning av riskfaktorer för stroke saknas",

        "ts.validation.horselbalans.missing" : "Hörsel och balanssinne saknas",
        "ts.validation.horselbalans.balansrubbningar.missing" : "Balansrubbningar saknas",
        "ts.validation.horselbalans.uppfattasamtal4meter.missing" : "Svårt att uppfatta samtal på fyra meters avstånd måste anges för innehav av behörigheterna D1, D1E, D, DE eller Taxi",

        "ts.validation.intygavser.missing" : "Intyget avser körkortsbehörighet saknas",
        "ts.validation.intygavser.must-choose-one" : "Minst en körkortsbehörighet, eller Annat måste väljas",

        "ts.validation.kognitivt.missing" : "Demens och kognitiva störningar saknas",
        "ts.validation.kognitivt.sviktandekognitivfunktion.missing" : "Sviktande kognitiv funktion saknas",

        "ts.validation.medicinering.missing" : "Övrig medicinering saknas",
        "ts.validation.medicinering.stadigvarandemedicinering.missing" : "Stadigvarande medicinering saknas",
        "ts.validation.medicinering.beskrivning.missing" : "Beskrivning av medicinering saknas",

        "ts.validation.narkotikalakemedel.missing" : "Alkohol, narkotika och läkemedel saknas",
        "ts.validation.narkotikalakemedel.teckenmissbruk.missing" : "Finns journaluppgifter, anamnestiska uppgifter, resultat av laboratorieprover eller andra tecken på missbruk saknas",
        "ts.validation.narkotikalakemedel.vardinsats.missing" : " Har patienten vid något tillfälle varit föremål för vårdinsatser för missbruk eller beroende av alkohol, narkotika eller läkemedel saknas",
        "ts.validation.narkotikalakemedel.provtagning-behovs.missing" : "Om tecken på missbruk föreligger måste provtagning avseende aktuellt bruk av alkohol eller narkotika vara angett",
        "ts.validation.narkotikalakemedel.lakarordineratlakemedelsbruk.missing" : "Läkarordinerat läkemedelsbruk saknas",
        "ts.validation.narkotikalakemedel.lakemedelochdos.missing" : "Läkemedel och dos saknas",

        "ts.validation.medvetandestorning.missing" : "Epilepsi, epileptiskt anfall och annan medvetandestörning saknas",
        "ts.validation.medvetandestorning.medvetandestorning.missing" : "Epilepsi, epileptiskt anfall och annan medvetandestörning saknas",
        "ts.validation.medvetandestorning.beskrivning.missing" : "Beskrivning av annan medvetandestörning saknas",

        "ts.validation.syn.missing" : "Synfunktioner saknas",
        "ts.validation.syn.progressiv-ogonsjukdom.missing" : "Har patienten någon progressiv ögonsjukdom saknas",
        "ts.validation.syn.tecken-synfaltsdefekter.missing" : "Finns tecken på synfältsdefekter saknas",
        "ts.validation.syn.nattblindhet.missing" : "Framkommer anamnestiska uppgifter om begränsning av seendet vid nedsatt belysning saknas",
        "ts.validation.syn.diplopi.missing" : "Framkommer dubbelseende saknas",
        "ts.validation.syn.nystagmus.missing" : "Förekommer nystagmus saknas",
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

        "ts.validation.identitet.missing" : "Identitet styrkt saknas"

    },
    "en" : {
        "view.label.pagetitle" : "Show Certificate"
    }
};
