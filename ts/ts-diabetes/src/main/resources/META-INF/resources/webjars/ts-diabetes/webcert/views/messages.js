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

/* jshint maxlen: false */
angular.module('ts-diabetes').constant('ts-diabetes.messages', {
    'sv': {
        'ts-diabetes.form.postadress': 'Postadress',
        'ts-diabetes.form.postnummer': 'Postnummer',
        'ts-diabetes.form.postort': 'Postort',

        'ts-diabetes.label.empty': '',
        'ts-diabetes.label.patientadress': 'Patientens adressuppgifter',
        'ts-diabetes.label.intygavser': 'Intyget avser',
        'ts-diabetes.label.identitet': 'Identiteten är styrkt genom',
        'ts-diabetes.label.diabetes': '1. Allmänt',
        'ts-diabetes.label.hypoglykemier': '2. Hypoglykemier (lågt blodsocker)',
        'ts-diabetes.label.syn': '3. Synintyg',
        'ts-diabetes.label.bedomning': '4. Bedömning',
        'ts-diabetes.label.vardenhet': 'Vårdenhet',

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
        'ts-diabetes.validation.hypoglykemier.missing': 'Hypoglykemier saknas',
        'ts-diabetes.validation.hypoglykemier.allvarlig-forekomst-vaken-tid.observationstid.incorrect-date': 'Tidpunkt för allvarlig hypoglykemi under vaken tid måste anges som åååå-mm-dd, och får inte vara tidigare än ett år tillbaka eller senare än dagens datum.',
        'ts-diabetes.validation.diabetes.missing': 'Diabetes saknas',
        'ts-diabetes.validation.diabetes.observationsperiod.incorrect-format': 'År måste anges enligt formatet ÅÅÅÅ. Det går inte att ange årtal som är senare än innevarande år eller tidigare än år 1900.',
        'ts-diabetes.validation.diabetes.behandling.missing': 'Minst en behandling måste väljas.',
        'ts-diabetes.validation.diabetes.insulin.behandlingsperiod.missing' : 'År då behandling med insulin påbörjades måste anges.',
        'ts-diabetes.validation.diabetes.insulin.behandlingsperiod.incorrect-format' : 'År måste anges enligt formatet ÅÅÅÅ. Det går inte att ange årtal som är senare än innevarande år eller tidigare än år 1900.',
        'ts-diabetes.validation.syn.out-of-bounds': 'Måste ligga i intervallet 0,0 till 2,0.'
    },
    'en': {
        'view.label.pagetitle': 'Show Certificate'
    }
});
