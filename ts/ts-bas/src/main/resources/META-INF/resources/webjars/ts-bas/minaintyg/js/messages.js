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
angular.module('ts-bas').constant('ts-bas.messages', {
    'sv': {
        // Labels
        'ts-bas.label.intygavser': 'Avser behörighet för körkort',
        'ts-bas.label.identitet': 'Identiteten är styrkt genom',
        'ts-bas.label.unit': 'Vårdenhet',

        'ts-bas.label.syn.binokulart': 'Binokulärt',
        'ts-bas.label.syn.hogeroga': 'Höger öga',
        'ts-bas.label.syn.vansteroga': 'Vänster öga',
        'ts-bas.label.syn.utankorrektion': 'Utan korrektion',
        'ts-bas.label.syn.medkorrektion': 'Med korrektion',
        'ts-bas.label.syn.kontaktlins': 'Kontaktlinser',
        'ts-bas.label.syn.korrektionsglasens-styrka': 'Korrektionsglasens styrka',

        'ts-bas.label.diabetes.diabetestyp.diabetes_typ_1': 'Typ 1',
        'ts-bas.label.diabetes.diabetestyp.diabetes_typ_2': 'Typ 2',

        'ts-bas.label.true': 'JA',
        'ts-bas.label.false': 'NEJ',

        'ts-bas.label.pagetitle': 'Granska och skicka intyg',
        'ts-bas.label.pagetitle.step1': 'Skicka intyg - steg 1 av 2',
        'ts-bas.label.pagetitle.step2': 'Skicka intyg - steg 2 av 2',

        'ts-bas.label.pageingress': 'Här visas hela ditt läkarintyg. Från den här sidan kan du skicka intyget elektroniskt till Transportstyrelsen, du kan även skriva ut eller spara intyget.<br>',
        'ts-bas.label.pagedescription.sendcertificate.header': '<h2>Skicka intyg</h2>',
        'ts-bas.label.pagedescription.sendcertificate': 'Klicka på knappen Skicka för att skicka intyget elektroniskt till Transportstyrelsen.',
        'ts-bas.label.pagedescription.save.or.print.certificate.header': '<h2>Spara eller skriva ut</h2>',
        'ts-bas.label.pagedescription.save.or.print.certificate': 'Klicka på knappen Spara som PDF för att spara eller skriva ut ditt intyg. Intyget öppnas eller laddas ner som en pdf-fil som du kan spara på din dator eller skriva ut.',
        'ts-bas.label.pagedescription.error.in.certificate.header': '<h2>Fel i intyget?</h2>',
        'ts-bas.label.pagedescription.error.in.certificate': 'Om någon uppgift är fel i läkarintyget så ska du kontakta den som utfärdat ditt läkarintyg.',
        'ts-bas.label.pagedescription.archive.header': '<h2>Arkivera</h2>',
        'ts-bas.label.pagedescription.archive': 'För att arkivera intyget klickar du på symbolen Arkivera intyg <span class="glyphicon glyphicon-folder-close"></span>.',

        'ts-bas.label.status.false': '<span class="glyphicon glyphicon-chevron-up"></span> Visa mindre',
        'ts-bas.label.status.true': '<span class="glyphicon glyphicon-chevron-down"></span> Visa mer',
        'ts-bas.label.showevents.false': '<span class="glyphicon glyphicon-chevron-up"></span> Visa färre händelser',
        'ts-bas.label.showevents.true': '<span class="glyphicon glyphicon-chevron-down"></span> Visa alla händelser',
        'ts-bas.label.patientname': 'Patientens namn:',
        'ts-bas.label.issued': 'Inkom till Mina intyg',
        'ts-bas.label.civicnumber': 'Personnr:',
        'ts-bas.label.issuer': 'Utfärdare:',
        'ts-bas.label.period': 'Period:',
        'ts-bas.label.errorpagetitle': 'Ett problem har uppstått',

        'ts-bas.label.confirmedby': 'Ovanstående uppgifter och bedömningar bekräftas',
        'ts-bas.label.date': 'Datum',
        'ts-bas.label.contactinfo': 'Namn och kontaktuppgifter till vårdenheten',

        'ts-bas.label.bedomning-info-ej-angivet': 'Ej ifyllt',

        'ts-bas.label.latestevent': 'Senaste händelse',
        'ts-bas.label.latestevent.noevents': 'Inga händelser',
        'ts-bas.label.latestevent.showall': 'Visa alla händelser',

        'ts-bas.confirm.label.summarypagedesc': 'Du har valt att skicka följande intyg: ',
        'ts-bas.confirm.label.recipienttitle': 'Mottagare: ',
        'ts-bas.confirm.label.verifytext': 'Kontrollera att uppgifterna ovan stämmer och tryck på Skicka.',
        'ts-bas.confirm.message.alreadysent': '<strong>Observera!</strong> Intyget har redan skickats till: <br>',

        'ts-bas.button.send': 'Skicka',
        'ts-bas.button.send.certificate.title': 'Skicka detta intyg till Transportstyrelsen.',
        'ts-bas.button.sendtofk': 'Skicka',
        'ts-bas.button.goback': 'Tillbaka',
        'ts-bas.button.cancel': 'Avbryt',

        'ts-bas.fishbone.label.granska-skicka': 'Granska och skicka',
        'ts-bas.fishbone.label.bekraftelse': 'Bekräftelse',

        'ts-bas.target.mi': 'Försäkringsbolaget',
        'ts-bas.target.ts': 'Transportstyrelsen',

        'ts-bas.sent.label.pageinformation': 'Intyget har nu skickats och nedan presenteras försändelsen.',
        'ts-bas.sent.label.result': 'Mottagare av intyget: ',
        'ts-bas.sent.button.backtocertificate': 'Tillbaka till intyget',

        'ts-button.detail.send': 'Skicka',
        'ts-bas.message.certifits-basloading': 'Hämtar intyg...',
        'ts-bas.error.generic': 'Kunde inte visa intyget',
        'ts-bas.error.certnotfound': 'Intygen i Inkorgen kunde inte visas. Om felet kvarstår kan du kontakta <a href="/web/start/#/hjalp">support</a>. Om du inte kan komma åt intyget på Mina intyg just nu så kan du kontakta din läkare för att få en kopia.<br><br><a href="/web/start">Gå till Inkorgen och försök igen</a>',
        'ts-bas.history.label.pagetitle': 'Intygets alla händelser',
        'ts-bas.status.sent': 'Mottaget av',
        'ts-bas.inbox.complementaryinfo': 'Avser behörighet',

        //Helptexts
        'ts-bas.helptext.readpdfhelp': '<b>Läsa en pdf-fil</b><br/>PDF är ett filformat som används för att ett dokument ska se likadant ut i olika datorer. För att kunna öppna pdf-filer behöver du en pdf-läsare exempelvis. <a href="http://get.adobe.com/se/reader/" target="_blank">Adobe Reader</a>.'

    },
    'en': {
        'ts-bas.label.pagetitle': 'Show Certificate'
    }
});
