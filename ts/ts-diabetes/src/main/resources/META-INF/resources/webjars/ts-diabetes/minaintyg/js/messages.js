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
        // Labels
        'ts-diabetes.label.intygavser': 'Avser lämplighet för körkort',
        'ts-diabetes.label.identitet': 'Identiteten är styrkt genom',
        'ts-diabetes.label.allmant': '1. Allmänt',
        'ts-diabetes.label.hypoglykemier': '2. Hypoglykemier (lågt blodsocker)',
        'ts-diabetes.label.syn': '3. Synintyg',
        'ts-diabetes.label.bedomning': '4. Bedömning',
        'ts-diabetes.label.ovrigkommentar': 'Övriga upplysningar och kommentarer',
        'ts-diabetes.label.unit': 'Vårdenhet',

        'ts-diabetes.label.syn.binokulart': 'Binokulärt',
        'ts-diabetes.label.syn.hogeroga': 'Höger öga',
        'ts-diabetes.label.syn.vansteroga': 'Vänster öga',
        'ts-diabetes.label.syn.utankorrektion': 'Utan korrektion',
        'ts-diabetes.label.syn.medkorrektion': 'Med korrektion',
        'ts-diabetes.label.syn.kontaktlins': 'Kontaktlinser',
        'ts-diabetes.label.syn.korrektionsglasens-styrka': 'Korrektionsglasens styrka',

        'ts-diabetes.label.diabetes.diabetestyp.diabetes_typ_1': 'Typ 1',
        'ts-diabetes.label.diabetes.diabetestyp.diabetes_typ_2': 'Typ 2',

        'ts-diabetes.label.true': 'JA',
        'ts-diabetes.label.false': 'NEJ',

        'ts-diabetes.label.pagetitle': 'Granska och skicka intyg',
        'ts-diabetes.label.pagetitle.step1': 'Skicka intyg - steg 1 av 2',
        'ts-diabetes.label.pagetitle.step2': 'Skicka intyg - steg 2 av 2',

        'ts-diabetes.label.pageingress': 'Här visas hela ditt läkarintyg. Från den här sidan kan du skicka intyget elektroniskt till Transportstyrelsen, du kan även skriva ut eller spara intyget.<br>',
        'ts-diabetes.label.pagedescription.sendcertificate.header': '<h2>Skicka intyg</h2>',
        'ts-diabetes.label.pagedescription.sendcertificate': 'Klicka på knappen Skicka för att skicka intyget elektroniskt till Transportstyrelsen.',
        'ts-diabetes.label.pagedescription.save.or.print.certificate.header': '<h2>Spara eller skriva ut</h2>',
        'ts-diabetes.label.pagedescription.save.or.print.certificate': 'Klicka på knappen Spara som PDF för att spara eller skriva ut ditt intyg. Intyget öppnas eller laddas ner som en pdf-fil som du kan spara på din dator eller skriva ut.',
        'ts-diabetes.label.pagedescription.error.in.certificate.header': '<h2>Fel i intyget?</h2>',
        'ts-diabetes.label.pagedescription.error.in.certificate': 'Om någon uppgift är fel i läkarintyget så ska du kontakta den som utfärdat ditt läkarintyg.',
        'ts-diabetes.label.pagedescription.archive.header': '<h2>Arkivera</h2>',
        'ts-diabetes.label.pagedescription.archive': 'För att arkivera intyget klickar du på symbolen Arkivera intyg <span class="glyphicon glyphicon-folder-close"></span>.',

        'ts-diabetes.label.status.false': '<span class="glyphicon glyphicon-chevron-up"></span> Visa mindre',
        'ts-diabetes.label.status.true': '<span class="glyphicon glyphicon-chevron-down"></span> Visa mer',
        'ts-diabetes.label.showevents.false': '<span class="glyphicon glyphicon-chevron-up"></span> Visa färre händelser',
        'ts-diabetes.label.showevents.true': '<span class="glyphicon glyphicon-chevron-down"></span> Visa alla händelser',
        'ts-diabetes.label.patientname': 'Patientens namn:',
        'ts-diabetes.label.issued': 'Inkom till Mina intyg',
        'ts-diabetes.label.civicnumber': 'Personnr:',
        'ts-diabetes.label.issuer': 'Utfärdare:',
        'ts-diabetes.label.period': 'Period:',
        'ts-diabetes.label.errorpagetitle': 'Ett problem har uppstått',

        'ts-diabetes.label.latestevent': 'Senaste händelse',
        'ts-diabetes.label.latestevent.noevents': 'Inga händelser',
        'ts-diabetes.label.latestevent.showall': 'Visa alla händelser',

        'ts-diabetes.label.confirmedby': 'Ovanstående uppgifter och bedömningar bekräftas',
        'ts-diabetes.label.date': 'Datum',
        'ts-diabetes.label.contactinfo': 'Namn och kontaktuppgifter till vårdenheten',
        'ts-diabetes.label.ej-angivet': 'Ej ifyllt',

        'ts-diabetes.label.bedomning.kan-inte-ta-stallning': 'Kan inte ta ställning',

        'ts-diabetes.confirm.label.pagetitle': 'Skicka intyg - steg 2 av 3',
        'ts-diabetes.confirm.label.summarypagedesc': 'Du har valt att skicka följande intyg: ',
        'ts-diabetes.confirm.label.recipienttitle': 'Mottagare: ',
        'ts-diabetes.confirm.label.verifytext': 'Kontrollera att uppgifterna ovan stämmer och tryck på "Skicka".',
        'ts-diabetes.confirm.message.alreadysent': '<strong>Observera!</strong> Intyget har redan skickats till: <br>',

        'ts-diabetes.detail.send': 'Skicka',
        'ts-diabetes.message.certificateloading': 'Hämtar intyg...',

        'ts-diabetes.fishbone.label.granska-skicka': 'Granska och skicka',
        'ts-diabetes.fishbone.label.bekraftelse': 'Bekräftelse',

        'ts-diabetes.button.send': 'Skicka',
        'ts-diabetes.button.cancel': 'Avbryt',
        'ts-diabetes.button.goback': 'Tillbaka',
        'ts-diabetes.button.send.certificate.title': 'Skicka detta intyg till Transportstyrelsen.',

        'ts-diabetes.sent.label.result': 'Mottagare av intyget: ',
        'ts-diabetes.sent.label.pagetitle': 'Skicka intyg - steg 3 av 3',
        'ts-diabetes.sent.button.backtocertificate': 'Tillbaka till intyget',
        'ts-diabetes.sent.label.pageinformation': 'Intyget har nu skickats och nedan presenteras försändelsen.',

        'ts-diabetes.error.generic': 'Kunde inte visa intyget',
        'ts-diabetes.error.certnotfound': 'Intygen i Inkorgen kunde inte visas. Om felet kvarstår kan du kontakta <a href="/web/start/#/hjalp">support</a>. Om du inte kan komma åt intyget på Mina intyg just nu så kan du kontakta din läkare för att få en kopia.<br><br><a href="/web/start">Gå till Inkorgen och försök igen</a>',
        'ts-diabetes.history.label.pagetitle': 'Intygets alla händelser',
        'ts-diabetes.status.sent': 'Mottaget av',
        'ts-diabetes.inbox.complementaryinfo': 'Avser behörighet',

        'ts-diabetes.target.mi': 'Försäkringsbolaget',
        'ts-diabetes.target.ts': 'Transportstyrelsen',

        // Helptexts
        'ts-diabetes.helptext.readpdfhelp': '<b>Läsa en pdf-fil</b><br/>PDF är ett filformat som används för att ett dokument ska se likadant ut i olika datorer. För att kunna öppna pdf-filer behöver du en pdf-läsare exempelvis. <a href="http://get.adobe.com/se/reader/" target="_blank">Adobe Reader</a>.'

    },
    'en': {
        'ts-diabetes.label.pagetitle': 'Show Certificate'
    }
});
