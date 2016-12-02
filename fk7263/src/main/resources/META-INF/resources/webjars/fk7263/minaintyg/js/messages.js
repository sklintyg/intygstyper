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
angular.module('fk7263').constant('fk7263.messages', {
    'sv': {
        // Labels
        'fk7263.label.pagetitle': 'Granska och skicka intyg',
        'fk7263.label.pageingress': 'Här visas hela ditt läkarintyg. Från den här sidan kan du skicka intyget elektroniskt till Försäkringskassan, du kan även skriva ut och spara intyget.<br>',
        'fk7263.label.pagedescription.sendcertificate.header': '<h2>Skicka intyg</h2>',
        'fk7263.label.pagedescription.sendcertificate': 'Klicka på knappen Skicka för att skicka intyget elektroniskt till Försäkringskassan eller en annan mottagare.<br>',
        'fk7263.label.pagedescription.certificate.to.employer.header': '<h2>Intyg till arbetsgivare</h2>',
        'fk7263.label.pagedescription.certificate.to.employer': 'Du har möjlighet att anpassa läkarintyget om du ska lämna läkarintyget till din arbetsgivare. Du anpassar intyget genom att välja om du vill visa alla uppgifter i intyget eller om du vill dölja vissa delar. Klicka på knappen Anpassa intyget för att välja vilken information du vill dela.',
        'fk7263.label.pagedescription.save.or.print.certificate.header': '<h2>Spara eller skriva ut</h2>',
        'fk7263.label.pagedescription.save.or.print.certificate': 'Klicka på knappen Spara som PDF för att spara eller skriva ut ditt intyg. Intyget öppnas eller laddas ner som en pdf-fil som du kan spara på din dator eller skriva ut.',
        'fk7263.label.pagedescription.sickness.benefit.header': '<h2>Vill du ansöka om sjukpenning?</h2>',
        'fk7263.label.pagedescription.sickness.benefit': 'Det gör du enklast på <a href="http://www.forsakringskassan.se/sjuk" target="_blank">www.forsakringskassan.se/sjuk</a>. Där kan du läsa mer om sjukpenning och hur du ansöker.<br>',
        'fk7263.label.pagedescription.error.in.certificate.header': '<h2>Fel i intyget?</h2>',
        'fk7263.label.pagedescription.error.in.certificate': 'Om någon uppgift är fel i läkarintyget så ska du kontakta den som utfärdat ditt läkarintyg.',
        'fk7263.label.pagedescription.archive.header': '<h2>Arkivera</h2>',
        'fk7263.label.pagedescription.archive': 'För att arkivera intyget klickar du på symbolen Arkivera intyg <span class="glyphicon glyphicon-folder-close"></span>.',

        'fk7263.label.status.false': '<span class="glyphicon glyphicon-chevron-up"></span> Visa mindre',
        'fk7263.label.status.true': '<span class="glyphicon glyphicon-chevron-down"></span> Visa mer',
        'fk7263.label.showevents.false': '<span class="glyphicon glyphicon-chevron-up"></span> Visa färre händelser',
        'fk7263.label.showevents.true': '<span class="glyphicon glyphicon-chevron-down"></span> Visa alla händelser',

        'fk7263.label.nedsattning': 'Jag bedömer att arbetsförmåga är (fält 8b)',
        'fk7263.label.nedsattning.anpassatintyg': 'Jag bedömer att arbetsförmåga är',
        'fk7263.label.patientname': 'Patientens namn',
        'fk7263.label.issued': 'Inkom till Mina intyg',
        'fk7263.label.civicnumber': 'Personnummer',
        'fk7263.label.issuer': 'Utfärdare',
        'fk7263.label.period': 'Period',
        'fk7263.label.unit': 'Enhet',
        'fk7263.label.smittskydd': 'Avstängning enligt smittskyddslagen på grund av smitta (fält 1)',
        'fk7263.label.smittskydd.anpassatintyg': 'Avstängning enligt smittskyddslagen på grund av smitta',
        'fk7263.label.yes': 'Ja',
        'fk7263.label.no': 'Nej',
        'fk7263.label.partialyes': 'Ja, delvis',
        'fk7263.label.diagnosis': 'Diagnos/diagnoser för sjukdom som orsakar nedsatt arbetsförmåga (fält 2)',
        'fk7263.label.diagnosis.anpassatintyg': 'Diagnos/diagnoser för sjukdom som orsakar nedsatt arbetsförmåga',
        'fk7263.label.diagnosiscode': 'Diagnoskod enligt ICD-10 (huvuddiagnos): ',
        'fk7263.label.ytterligarediagnoser': 'Ytterligare diagnoser',
        'fk7263.label.diagnosfortydligande': 'Förtydligande av diagnos/diagnoser',
        'fk7263.label.samsjuklighet': 'Samsjuklighet föreligger',
        'fk7263.label.progressofdesease': 'Aktuellt sjukdomförlopp (fält 3)',
        'fk7263.label.progressofdesease.anpassatintyg': 'Aktuellt sjukdomförlopp',
        'fk7263.label.disabilities': 'Funktionsnedsättning - observationer, undersökningsfynd och utredningsresultat (fält 4)',
        'fk7263.label.disabilities.anpassatintyg': 'Funktionsnedsättning - observationer, undersökningsfynd och utredningsresultat',
        'fk7263.label.basedon': 'Intyget baseras på (fält 4b)',
        'fk7263.label.basedon.anpassatintyg': 'Intyget baseras på',
        'fk7263.label.comment': 'Kommentar:',
        'fk7263.label.limitation': 'Aktivitetsbegränsning relaterat till diagnos och funktionsnedsättning (fält 5)',
        'fk7263.label.limitation.anpassatintyg': 'Aktivitetsbegränsning relaterat till diagnos och funktionsnedsättning',
        'fk7263.label.recommendations': 'Rekommendationer (fält 6a)',
        'fk7263.label.recommendations.anpassatintyg.1': 'Rekommendationer',
        'fk7263.label.recommendations.anpassatintyg.2': 'Rekommendationer - kontakt med företagshälsovård',
        'fk7263.label.plannedtreatment': 'Planerad eller pågående behandling eller åtgärd (fält 6b)',
        'fk7263.label.plannedtreatment.anpassatintyg': 'Planerad eller pågående behandling eller åtgärd',
        'fk7263.label.plannedtreatment.healthcare': 'Inom sjukvården:',
        'fk7263.label.plannedtreatment.other': 'Annan åtgärd:',
        'fk7263.label.workrehab': 'Är arbetslivsinriktad rehabilitering aktuell? (fält 7)',
        'fk7263.label.workrehab.anpassatintyg': 'Är arbetslivsinriktad rehabilitering aktuell?',
        'fk7263.label.unjudgeable': 'Går inte att bedöma',
        'fk7263.label.patientworkcapacity': 'Patientens arbetsförmåga bedöms i förhållande till (fält 8a)',
        'fk7263.label.patientworkcapacity.anpassatintyg.1': 'Patientens arbetsförmåga bedöms i förhållande till',
        'fk7263.label.patientworkcapacity.anpassatintyg.2': 'Patientens arbetsförmåga bedöms i förhållande till - nuvarande arbete',
        'fk7263.label.patientworkcapacity.currentwork': 'Nuvarande arbete: ',
        'fk7263.label.patientworkcapacity.unemployed': 'Arbetslöshet - att utföra sådant arbete som är normalt förekommande på arbetsmarknaden',
        'fk7263.label.patientworkcapacity.parentalleave': 'Föräldraledighet med föräldrapenning - att vårda sitt barn',
        'fk7263.label.patientworkcapacityjudgement': 'Patientens arbetsförmåga bedöms nedsatt längre tid än den som det försäkringsmedicinska beslutsstödet anger, därför att:  (fält 9)',
        'fk7263.label.patientworkcapacityjudgement.anpassatintyg': 'Patientens arbetsförmåga bedöms nedsatt längre tid än den som det försäkringsmedicinska beslutsstödet anger, därför att',
        'fk7263.label.prognosis': 'Prognos - kommer patienten att få tillbaka sin arbetsförmåga i nuvarande arbete? (Gäller inte arbetslösa) (fält 10)',
        'fk7263.label.prognosis.anpassatintyg': 'Prognos - kommer patienten att få tillbaka sin arbetsförmåga i nuvarande arbete? (Gäller inte arbetslösa)',
        'fk7263.label.othertransport': 'Kan resor till och från arbetet med annat färdsätt än normalt göra det möjligt för patienten att återgå i arbete? (fält 11)',
        'fk7263.label.othertransport.anpassatintyg': 'Kan resor till och från arbetet med annat färdsätt än normalt göra det möjligt för patienten att återgå i arbete?',
        'fk7263.label.fkcontact': 'Kontakt önskas med Försäkringskassan (fält 12)',
        'fk7263.label.fkcontact.anpassatintyg': 'Kontakt önskas med Försäkringskassan',
        'fk7263.label.workcodes': 'Förskrivarkod och arbetsplatskod (fält 17)',
        'fk7263.label.workcodes.anpassatintyg': 'Förskrivarkod och arbetsplatskod',
        'fk7263.label.recommendations.contact.jobcenter': 'Kontakt med Arbetsförmedlingen',
        'fk7263.label.recommendations.contact.healthdepartment': 'Kontakt med företagshälsovården',
        'fk7263.label.recommendations.contact.other': 'Övrigt: ',
        'fk7263.label.reference.to.field13': 'Se under Övriga upplysningar och förtydliganden (fält 13)',
        'fk7263.label.otherinformation': 'Övriga upplysningar och förtydliganden (fält 13)',
        'fk7263.label.otherinformation.anpassatintyg': 'Övriga upplysningar och förtydliganden',

        'fk7263.label.confirmedby': 'Ovanstående uppgifter och bedömningar bekräftas',
        'fk7263.label.date': 'Datum',
        'fk7263.label.contactinfo': 'Namn och kontaktuppgifter till vårdenheten',

        'fk7263.label.latestevent': 'Senaste händelse',
        'fk7263.label.latestevent.noevents': 'Inga händelser',

        'fk7263.button.sendtofk': 'Skicka till Försäkringskassan',
        'fk7263.button.send.certificate': 'Skicka',
        'fk7263.button.send.certificate.title': 'Skicka detta intyg till en eller flera mottagare.',
        'fk7263.button.customize.certificate': 'Anpassa intyget till arbetsgivare',
        'fk7263.button.customize.certificate.title': 'Anpassa detta intyg för att lämna till arbetsgivaren.',
        'fk7263.button.customize.certificate.change': 'Ändra val',
        'fk7263.button.send': 'Skicka',
        'fk7263.button.cancel': 'Avbryt',
        'fk7263.button.goback': 'Tillbaka',
        'fk7263.button.next': 'Nästa',
        'fk7263.button.previous': 'Föregående steg',

        'fk7263.fishbone.label.valj-mottagare': 'Välj mottagare',
        'fk7263.fishbone.label.granska-skicka': 'Granska och skicka',
        'fk7263.fishbone.label.bekraftelse': 'Bekräftelse',

        'fk7263.recipient.label.pagetitle': 'Skicka intyg - steg 1 av 3',
        'fk7263.recipient.label.selectrecipents': 'Här ser du de mottagare som du kan skicka intyget till elektroniskt. Välj önskad mottagare och klicka sedan på knappen Nästa för att gå vidare till nästa steg.',
        'fk7263.recipient.label.recipientlisttitle': 'Myndigheter och försäkringsbolag',
        'fk7263.recipient.label.recipientlisttitle.others': 'Myndigheter och försäkringsbolag',
        'fk7263.recipient.label.missing.recipients-1': 'Om du saknar din mottagare i listan beror det troligen på att mottagaren inte kan ta emot elektroniska läkarintyg. Du kan då ',
        'fk7263.recipient.label.missing.recipients-2': ' och skicka det per post istället.',

        'fk7263.recipients.label.pagetitle': 'Skicka intyg - välj mottagare',

        'fk7263.confirm.label.pagetitle': 'Skicka intyg - steg 2 av 3',
        'fk7263.confirm.label.certrowtitle': 'Intyg',
        'fk7263.confirm.label.recipienttitle': 'Mottagare',
        'fk7263.confirm.label.summarypagedesc': '<p><b>Du har valt att skicka det här intyget.</b></p>',
        'fk7263.confirm.label.verifytext': 'Kontrollera att uppgifterna ovan stämmer och tryck "Skicka"',
        'fk7263.confirm.message.alreadysent': '<strong>Observera!</strong> Intyget har redan skickats till: <br>',

        'fk7263.sent.label.pagetitle': 'Skicka intyg - steg 3 av 3',
        'fk7263.sent.label.pageinformation': 'Intyget har nu skickats och nedan presenteras försändelsen.',
        'fk7263.sent.label.result': 'Mottagare av intyget: <br>',
        'fk7263.sent.label.infoaboutrecipients': 'Observera att olika mottagare har olika tider när dom kan se det inskickade intyget.',
        'fk7263.sent.button.backtocertificate': 'Tillbaka till intyget',

        'fk7263.target.fk': 'Försäkringskassan',
        'fk7263.target.afa': 'AFA Försäkring',
        'fk7263.target.skandia': 'Skandia',

        'fk7263.nedsattningsgrad.helt_nedsatt': 'Helt nedsatt',
        'fk7263.nedsattningsgrad.nedsatt_med_3_4': 'Nedsatt med 3/4',
        'fk7263.nedsattningsgrad.nedsatt_med_1_2': 'Nedsatt med hälften',
        'fk7263.nedsattningsgrad.nedsatt_med_1_4': 'Nedsatt med 1/4',

        'fk7263.vardkontakt.5880005': 'Min undersökning av patienten den %0',
        'fk7263.vardkontakt.undersokning': 'Min undersökning av patienten den ',
        'fk7263.vardkontakt.185317003': 'Min telefonkontakt med patienten den %0',
        'fk7263.vardkontakt.telefonkontakt': 'Min telefonkontakt med patienten den ',

        'fk7263.referens.419891008': 'Journaluppgifter, den %0',
        'fk7263.referens.journal': 'Journaluppgifter, den ',
        'fk7263.referens.74964007': 'Annat, den %0',
        'fk7263.referens.annat': 'Annat, den ',

        // Anpassat intyg
        'fk7263.customize.step.1.pagetitle': 'Anpassa intygsinformation till din arbetsgivare - steg 1 av 2',
        'fk7263.customize.step.1.pageingress': '<p>Här kan du skapa ett anpassat intyg till din arbetsgivare genom att välja vilken information du vill ta med och inte. Viss information måste lämnas till arbetsgivaren och kan inte väljas bort. I de flesta fall är du inte skyldig att lämna mer information än den obligatoriska, men om ni har ett kollektivavtal kan det finnas andra regler för vilken information som måste tas med. Det finns ofta fördelar med att arbetsgivaren får ta del även av frivillig information, exempelvis hur sjukdomen begränsar din aktivitetsförmåga (fält 5). Det ger arbetsgivaren bättre möjlighet att anpassa din arbetsplats eller arbetssituation. Du kan alltid återvända till Mina intyg vid senare tillfälle för att skapa ett nytt anpassat intyg med mer information.</p><p>För att välja bort information bockar du ur alternativet <i>Inkludera i anpassat intyg</i> i det fält du inte vill ska synas.</p>',
        'fk7263.customize.step.1.fishbone': 'Anpassa intyg',
        'fk7263.customize.step.2.pagetitle': 'Anpassa intygsinformation till din arbetsgivare - steg 2 av 2',
        'fk7263.customize.step.2.pageingress': '<p>Om du vill spara ditt anpassade intyg som PDF klickar du på knappen Spara som PDF.</p><p>Om du märker att du har valt bort ett fält som du vill ha med, eller tvärt om, klickar du på Ändra val.</p>',
        'fk7263.customize.step.2.fishbone': 'Sammanfattning',
        'fk7263.customize.step.2.info.fk': 'Tänk på att det anpassade intyget <u>inte</u> ska skickas till Försäkringskassan.',
        'fk7263.customize.step.2.info.pdf': 'När du sett över dina val klickar du på knappen ',

        'fk7263.customize.title.field': 'Fält',

        'fk7263.customize.label.mandatory': 'Obligatoriskt fält',
        'fk7263.customize.label.optional': 'Inkludera i anpassat intyg',

        'fk7263.customize.message.limitation': 'Informationen i fält 5 är frivillig, men informationen kan underlätta arbetsgivarens möjlighet att göra arbetsanpassningar.',

        'fk7263.customize.summary.leave.header':'Vill du lämna anpassat intyg?',
        'fk7263.customize.summary.leave': 'Observera att ditt anpassade intyg inte sparas i Mina intyg efter att du navigerat till en annan sida. Se därför till att du har laddat ner det till din dator innan du lämnar sidan.',
        'fk7263.customize.summary.leave.yes': 'Ja, lämna anpassat intyg',
        'fk7263.customize.summary.leave.no': 'Nej, stanna kvar',

        // Misc
        'fk7263.common.cancel': 'Avbryt',
        'fk7263.helptext.readpdfhelp': '<b>Läsa en pdf-fil</b><br/>PDF är ett filformat som används för att ett dokument ska se likadant ut i olika datorer. För att kunna öppna pdf-filer behöver du en pdf-läsare exempelvis. <a href="http://get.adobe.com/se/reader/" target="_blank">Adobe Reader</a>.',
        'fk7263.history.label.pagetitle': 'Intygets alla händelser',
        'fk7263.inbox.complementaryinfo': 'Intygsperiod',
        'fk7263.message.certificateloading': 'Hämtar intyg...',
        'fk7263.status.sent': 'Mottaget av',

        // Errors
        'fk7263.error.generic': 'Intyget kunde inte visas. Om felet kvarstår kan du kontakta <a href="/web/start/#/hjalp">support</a>. Om du inte kan komma åt intyget på Mina intyg just nu så kan du kontakta din läkare för att få en kopia.<br><br><a href="/web/start">Gå till Inkorgen och försök igen</a>',
        'fk7263.error.couldnotsend': 'Intyget kunde inte skickas till Försäkringskassan. Om felet kvarstår kan du kontakta <a href="/web/start/#/hjalp">support</a>.<br><br><a href="/web/start">Gå till Inkorgen och försök igen</a>',
        'fk7263.error.certnotfound': 'Intygen i Inkorgen kunde inte visas. Om felet kvarstår kan du kontakta <a href="/web/start/#/hjalp">support</a>. Om du inte kan komma åt intyget på Mina intyg just nu så kan du kontakta din läkare för att få en kopia.<br><br><a href="/web/start">Gå till Inkorgen och försök igen</a>',
        'fk7263.error.failedreceiverecipients': 'Kunde inte ladda mottagarlistan. Om felet kvarstår kan du kontakta <a href="/web/start/#/hjalp">support</a>.<br><br><a href="/web/start">Gå till Inkorgen och försök igen</a>'
    },
    'en': {
        'fk7263.label.pagetitle': 'Show Certificate'
    }
});
