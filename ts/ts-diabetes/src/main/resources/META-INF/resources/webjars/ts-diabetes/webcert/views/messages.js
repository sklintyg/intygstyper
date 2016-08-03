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

/* jshint maxlen: false, unused: false */
var tsDiabetesMessages = {
    'sv': {
        'ts-diabetes.button.send': 'Skicka till Transportstyrelsen',
        'ts-diabetes.label.empty': '',
        'ts-diabetes.label.certtitle': 'Transportstyrelsens läkarintyg, diabetes',
        'ts-diabetes.label.send': 'Skicka intyg till Transportstyrelsen',
        'ts-diabetes.label.send.body': '',
        'ts-diabetes.label.intygavser': 'Intyget avser',
        'ts-diabetes.label.identitet': 'Identiteten är styrkt genom',
        'ts-diabetes.label.diabetes': '1. Allmänt',
        'ts-diabetes.label.hypoglykemier': '2. Hypoglykemier (lågt blodsocker)',
        'ts-diabetes.label.syn': '3. Synintyg',
        'ts-diabetes.label.bedomning': '4. Bedömning',
        'ts-diabetes.label.vardenhet': 'Vårdenhet',
        'ts-diabetes.label.makulera.confirmation': 'Transportstyrelsens läkarintyg, diabetes, utställd på ${namn} – ${personnummer} är återtaget. Mottagare av intyget är notifierade om detta.',
        'ts-diabetes.label.spara-utkast': 'Spara',
        'ts-diabetes.label.ta-bort-utkast': 'Ta bort utkast',
        'ts-diabetes.label.skriv-ut-utkast': 'Skriv ut',
        'ts-diabetes.label.status.sent': 'Intyget är signerat och har skickats till Transportstyrelsens system.',
        'ts-diabetes.label.status.recieved': 'Intyget är signerat och mottaget av Transportstyrelsens system.',
        'ts-diabetes.label.status.signed': 'Intyget är signerat och är nu tillgängligt för patienten i webbtjänsten Mina intyg, som nås via 1177.se. Om patienten inte har möjlighet att skicka intyget elektroniskt till Transportstyrelsen kan du skicka intyget direkt till Transportstyrelsen åt patienten. Observera att patientens samtycke då krävs.',
        'ts-diabetes.label.kopiera.text': 'Skapar en kopia av befintligt intyg. Det nya utkastet (kopian) kan ändras och signeras.',

        // Visa ts-diabetes
        'ts-diabetes.label.patient': 'Patientens adressuppgifter',

        'ts-diabetes.label.syn.binokulart': 'Binokulärt',

        'ts-diabetes.label.syn.hogeroga': '',
        'ts-diabetes.label.syn.vansteroga': '',
        'ts-diabetes.label.syn.utankorrektion': '',
        'ts-diabetes.label.syn.medkorrektion': '',
        'ts-diabetes.label.syn.kontaktlins': '',

        'ts-diabetes.label.diabetes.diabetestyp.diabetes_typ_1': 'Typ 1',
        'ts-diabetes.label.diabetes.diabetestyp.diabetes_typ_2': 'Typ 2',
        'ts-diabetes.label.kontakt-info': 'Namn och kontaktuppgifter till vårdenheten',

        'ts-diabetes.label.bedomning-info-alt-1': 'Patienten uppfyller kraven enligt Transportstyrelsens föreskrifter och allmänna råd om medicinska krav för innehav av körkort m.m. (TSFS 2010:125, senast ändrade genom TSFS 2013:2) för:',
        'ts-diabetes.label.bedomning.kan-inte-ta-stallning': 'Kan inte ta ställning',
        'ts-diabetes.label.bedomning-info-ej-angivet': 'Ej angivet',

        // Help texts

        'ts-diabetes.helptext.identitet-styrkt-genom': 'ID-kort = SIS-märkt ID-kort, svenskt nationellt ID-kort eller ID-kort utfärdat av Skatteverket.<br/> Företagskort eller tjänstekort = SIS-märkt företagskort eller tjänstekort.<br/> Försäkran enligt 18 kap. 4 § = Försäkran enligt 18 kap 4 § i Transportstyrelsens föreskrifter (TSFS 2010:125, senast ändrade genom TSFS 2013:2): Identiteten får fastställas genom att en förälder, annan vårdnadshavare, make, maka eller sambo, registrerad partner eller myndigt barn skriftligen försäkrar att lämnade uppgifter om sökandens identitet är riktiga. Den som lämnar en sådan försäkran ska vara närvarande vid identitetskontrollen och kunna styrka sin egen identitet.<br/> Pass = Svenskt EU-pass, annat EU-pass utfärdade från och med den 1 september 2006, pass utfärdat av Island, Liechtenstein, Norge eller Schweiz från och med den 1 september 200<br/>',

        'ts-diabetes.helptext.synfunktioner.synskarpa': 'Synskärpa (alla bokstäver ska kunna läsas på den rad som anger synskärpan. Är synskärpan sämre än 0,1 ska den anges som 0,0).<br/><br/> Uppgifterna om synskärpa med och utan korrektion kan grundas på aktuellt intyg av bl.a. legitimerad optiker eller den som är anställd hos optiker. Alternativt kan kopia av sådant intyg bifogas. Uppgifter från ögonbottenfoto kan också användas.',
        'ts-diabetes.helptext.synfunktioner.utan-korrektion': 'Uppgiften är obligatorisk',
        'ts-diabetes.helptext.synfunktioner.med-korrektion': 'Uppgiften är obligatorisk om föreskriven synskärpa endast uppnås med korrektion. Definition av föreskriven synskärpa finns i Transportstyrelsens föreskrifter.',

        // Validation messages
        'ts-diabetes.validation.patient.postadress.missing': 'Postadress saknas.',
        'ts-diabetes.validation.patient.postnummer.missing': 'Postnummer saknas.',
        'ts-diabetes.validation.patient.postort.missing': 'Postort saknas.',

        'ts-diabetes.validation.utlatande.missing': 'Utlatande saknas',

        'ts-diabetes.validation.intygavser.missing': 'Intyget avser körkortsbehörighet saknas.',
        'ts-diabetes.validation.intygavser.must-choose-one': 'Minst en körkortsbehörighet måste väljas.',

        // 'ts-diabetes.validation.identitet': 'Identiteten är styrkt genom', // Use ex ts-diabetes.label.identitet for validation summary headings
        'ts-diabetes.validation.identitet.missing': 'Ett alternativ måste anges.',

        'ts-diabetes.validation.diabetes.observationsperiod.missing': 'År då diabetesdiagnos ställdes måste anges.',
        'ts-diabetes.validation.diabetes.observationsperiod.incorrect-format': 'År måste anges enligt formatet ÅÅÅÅ. Det går inte att ange årtal som är senare än innevarande år eller tidigare än år 1900.',
        'ts-diabetes.validation.diabetes.missing': 'Diabetes saknas',
        'ts-diabetes.validation.diabetes.diabetestyp.missing': 'Diabetestyp måste anges.',
        'ts-diabetes.validation.diabetes.behandling.missing': 'Minst en behandling måste väljas.',
        'ts-diabetes.validation.diabetes.insulin.behandlingsperiod.missing' : 'År då behandling med insulin påbörjades måste anges.',
        'ts-diabetes.validation.diabetes.insulin.behandlingsperiod.incorrect-format' : 'År måste anges enligt formatet ÅÅÅÅ. Det går inte att ange årtal som är senare än innevarande år eller tidigare än år 1900.',

        'ts-diabetes.validation.hypoglykemier.missing': 'Hypoglykemier saknas',
        'ts-diabetes.validation.hypoglykemier.kunskap-om-atgarder.missing': 'a)	Ett alternativ måste anges.',
        'ts-diabetes.validation.hypoglykemier.tecken-nedsatt-hjarnfunktion.missing': 'b) Ett alternativ måste anges.',
        'ts-diabetes.validation.hypoglykemier.saknar-formaga-kanna-varningstecken.missing': 'c) Ett alternativ måste anges.',
        'ts-diabetes.validation.hypoglykemier.allvarlig-forekomst.missing': 'd) Ett alternativ måste anges.',
        'ts-diabetes.validation.hypoglykemier.allvarlig-forekomst.beskrivning.missing': 'd) Antal episoder måste anges.',
        'ts-diabetes.validation.hypoglykemier.allvarlig-forekomst-trafiken.missing': 'e) Ett alternativ måste anges.',
        'ts-diabetes.validation.hypoglykemier.allvarlig-forekomst-trafiken.beskrivning.missing': 'e) Uppgifter om episoder måste anges.',
        'ts-diabetes.validation.hypoglykemier.egenkontroll-blodsocker.missing': 'f) Ett alternativ måste anges.', // 2f
        'ts-diabetes.validation.hypoglykemier.allvarlig-forekomst-vaken-tid.missing': 'g) Ett alternativ måste anges.', // 2g
        'ts-diabetes.validation.hypoglykemier.allvarlig-forekomst-vaken-tid.observationstid.missing': 'Tidpunkt för allvarlig hypoglykemi under vaken tid saknas',
        'ts-diabetes.validation.hypoglykemier.allvarlig-forekomst-vaken-tid.observationstid.incorrect-date': 'Tidpunkt för allvarlig hypoglykemi under vaken tid måste anges som åååå-mm-dd, och får inte vara senare än dagens datum.',

        'ts-diabetes.validation.syn.missing': 'Synfunktioner saknas',
        'ts-diabetes.validation.syn.separat-ogonlakarintyg.missing': 'a) Ett alternativ måste anges.',
        'ts-diabetes.validation.syn.provning-utan-anmarkning.missing': 'b) Ett alternativ måste anges.',
        'ts-diabetes.validation.syn.hoger.missing': 'Synfunktioner relaterade till höger öga saknas',
        'ts-diabetes.validation.syn.hoger.utankorrektion.missing': 'c) Värden för synskärpa utan korrektion måste anges för: Höger öga', // combine these missing messages later
        'ts-diabetes.validation.syn.hoger.utankorrektion.out-of-bounds': 'Värdet för höger öga utan korrektion måste ligga i intervallet 0,0 till 2,0.',
        'ts-diabetes.validation.syn.hoger.medkorrektion.out-of-bounds': 'Värdet för höger öga med korrektion måste ligga i intervallet 0,0 till 2,0.',

        'ts-diabetes.validation.syn.vanster.missing': 'Synfunktioner relaterade till vänster öga saknas',
        'ts-diabetes.validation.syn.vanster.utankorrektion.missing': 'c) Värden för synskärpa utan korrektion måste anges för: Vänster öga', // combine these missing messages later
        'ts-diabetes.validation.syn.vanster.utankorrektion.out-of-bounds': 'Värdet för vänster öga utan korrektion måste ligga i intervallet 0,0 till 2,0.',
        'ts-diabetes.validation.syn.vanster.medkorrektion.out-of-bounds': 'Värdet för vänster öga med korrektion måste ligga i intervallet 0,0 till 2,0.',

        'ts-diabetes.validation.syn.binokulart.missing': 'Binokulära synfunktioner saknas',
        'ts-diabetes.validation.syn.binokulart.utankorrektion.missing': 'c)	Värden för synskärpa utan korrektion måste anges för: Binokulärt', // combine these missing messages later
        'ts-diabetes.validation.syn.binokulart.utankorrektion.out-of-bounds': 'Värdet för binokulär synskärpa utan korrektion måste ligga i intervallet 0,0 till 2,0.',
        'ts-diabetes.validation.syn.binokulart.medkorrektion.out-of-bounds': 'Värdet för binokulär synskärpa med korrektion måste ligga i intervallet 0,0 till 2,0.',

        'ts-diabetes.validation.syn.diplopi.missing': 'd) Ett alternativ måste anges.',

        'ts-diabetes.validation.bedomning.missing': 'Bedömning saknas',
        'ts-diabetes.validation.bedomning.must-choose-one': 'Minst en körkortsbehörighet, eller "Kan inte ta ställning", måste väljas.',
        'ts-diabetes.validation.bedomning.lamplighet-inneha-behorighet.missing': 'Ett alternativ måste anges för om patienten är lämplig för högre körkortsbehörighet (C1 eller högre).',

        'ts-diabetes.validation.vardenhet.postadress.missing': 'Postadressen för vårdenheten kunde inte hämtas från HSA, den måste anges manuellt.',
        'ts-diabetes.validation.vardenhet.postnummer.missing': 'Postnummer för vårdenheten kunde inte hämtas från HSA, den måste anges manuellt.',
        'ts-diabetes.validation.vardenhet.postnummer.incorrect-format': 'Postnummer måste anges i formatet XXX XX eller XXXXX (exempelvis 123 45)',
        'ts-diabetes.validation.vardenhet.postort.missing': 'Postort för vårdenheten kunde inte hämtas från HSA, den måste anges manuellt.',
        'ts-diabetes.validation.vardenhet.telefonnummer.missing': 'Telefonnummer till vårdenheten kunde inte hämtas från HSA, den måste anges manuellt.'
    },
    'en': {
        'view.label.pagetitle': 'Show Certificate'
    }
};
