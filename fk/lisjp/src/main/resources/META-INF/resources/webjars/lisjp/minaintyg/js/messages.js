/* jshint maxlen: false */
angular.module('lisjp').constant('lisjp.messages', {
    'sv': {
        'lisjp.recipient.label.pagetitle': 'Skicka intyg - steg 1 av 3',
        'lisjp.recipient.label.selectrecipents': 'Här ser du de mottagare som du kan skicka intyget till elektroniskt. Välj önskad mottagare och klicka sedan på knappen Nästa för att gå vidare till nästa steg.',
        'lisjp.recipient.label.recipientlisttitle': 'Myndigheter och försäkringsbolag',
        'lisjp.recipient.label.recipientlisttitle.others': 'Myndigheter och försäkringsbolag',
        'lisjp.recipient.label.missing.recipients-1': 'Om du saknar din mottagare i listan beror det troligen på att mottagaren inte kan ta emot elektroniska läkarintyg. Du kan då ',
        'lisjp.recipient.label.missing.recipients-2': ' och skicka det per post istället.',

        'lisjp.confirm.label.pagetitle': 'Skicka intyg - steg 2 av 3',
        'lisjp.confirm.label.certrowtitle': 'Intyg',
        'lisjp.confirm.label.recipienttitle': 'Mottagare',
        'lisjp.confirm.label.summarypagedesc': '<p><b>Du har valt att skicka det här intyget.</b></p>',
        'lisjp.confirm.label.verifytext': 'Kontrollera att uppgifterna ovan stämmer och tryck "Skicka"',
        'lisjp.confirm.message.alreadysent': '<strong>Observera!</strong> Intyget har redan skickats till: <br>',

        'lisjp.sent.label.pagetitle': 'Skicka intyg - steg 3 av 3',
        'lisjp.sent.label.pageinformation': 'Intyget har nu skickats och nedan presenteras försändelsen.',
        'lisjp.sent.label.result': 'Mottagare av intyget: <br>',
        'lisjp.sent.label.infoaboutrecipients': 'Observera att olika mottagare har olika tider när dom kan se det inskickade intyget.',
        'lisjp.sent.button.backtocertificate': 'Tillbaka till intyget',
        'lisjp.label.latestevent': 'Senaste händelse',
        'lisjp.label.latestevent.noevents': 'Inga händelser',
        'lisjp.history.label.pagetitle': 'Intygets alla händelser',
        'lisjp.status.sent': 'Mottaget av',
        'lisjp.target.fk': 'Försäkringskassan',
        'lisjp.target.afa': 'AFA Försäkring',
        'lisjp.target.skandia': 'Skandia',

        'lisjp.inbox.complementaryinfo': 'Intygsperiod',

        'lisjp.button.sendtofk': 'Skicka till Försäkringskassan',
        'lisjp.button.send.certificate': 'Skicka',
        'lisjp.button.send.certificate.title': 'Skicka detta intyg till en eller flera mottagare.',

        // Labels
        'lisjp.label.pagetitle': 'Granska och skicka intyg',
        'lisjp.label.pageingress': 'Här visas hela ditt läkarintyg. Från den här sidan kan du skicka intyget elektroniskt till Försäkringskassan, du kan även skriva ut och spara intyget.<br>',
        'lisjp.label.pagedescription.sendcertificate.header': '<h2>Skicka intyg</h2>',
        'lisjp.label.pagedescription.sendcertificate': 'Klicka på knappen Skicka för att skicka intyget elektroniskt till Försäkringskassan eller en annan mottagare.<br>',
        'lisjp.label.status.false': '<span class="glyphicon glyphicon-chevron-up"></span> Visa mindre',
        'lisjp.label.status.true': '<span class="glyphicon glyphicon-chevron-down"></span> Visa mer',
        'lisjp.label.showevents.false': '<span class="glyphicon glyphicon-chevron-up"></span> Visa färre händelser',
        'lisjp.label.showevents.true': '<span class="glyphicon glyphicon-chevron-down"></span> Visa alla händelser',

        'lisjp.label.pagedescription.certificate.to.employer.header': '<h2>Intyg till arbetsgivare</h2>',
        'lisjp.label.pagedescription.certificate.to.employer': 'Du har möjlighet att anpassa läkarintyget om du ska lämna läkarintyget till din arbetsgivare. Du anpassar intyget genom att välja om du vill visa alla uppgifter i intyget eller om du vill dölja vissa delar. Klicka på knappen Anpassa intyget för att välja vilken information du vill dela.',
        'lisjp.label.pagedescription.save.or.print.certificate.header': '<h2>Spara eller skriva ut</h2>',
        'lisjp.label.pagedescription.save.or.print.certificate': 'Klicka på knappen Spara som PDF för att spara eller skriva ut ditt intyg. Intyget öppnas eller laddas ner som en pdf-fil som du kan spara på din dator eller skriva ut.',
        'lisjp.label.pagedescription.sickness.benefit.header': '<h2>Vill du ansöka om sjukpenning-utokad?</h2>',
        'lisjp.label.pagedescription.sickness.benefit': 'Det gör du enklast på <a href="http://www.forsakringskassan.se/sjuk" target="_blank">www.forsakringskassan.se/sjuk</a>. Där kan du läsa mer om sjukpenning-utokad och hur du ansöker.<br>',
        'lisjp.label.pagedescription.error.in.certificate.header': '<h2>Fel i intyget?</h2>',
        'lisjp.label.pagedescription.error.in.certificate': 'Om någon uppgift är fel i läkarintyget så ska du kontakta den som utfärdat ditt läkarintyg.',
        'lisjp.label.pagedescription.archive.header': '<h2>Arkivera</h2>',
        'lisjp.label.pagedescription.archive': 'För att arkivera intyget klickar du på symbolen Arkivera intyg <span class="glyphicon glyphicon-folder-close"></span>.',

        'lisjp.helptext.readpdfhelp': '<b>Läsa en pdf-fil</b><br/>PDF är ett filformat som används för att ett dokument ska se likadant ut i olika datorer. För att kunna öppna pdf-filer behöver du en pdf-läsare exempelvis. <a href="http://get.adobe.com/se/reader/" target="_blank">Adobe Reader</a>.',
        'lisjp.label.patientname': 'Patientens namn',
        'lisjp.label.issued': 'Inkom till Mina intyg',
        'lisjp.label.civicnumber': 'Personnummer',
        'lisjp.label.issuer': 'Utfärdare',
        'lisjp.label.period': 'Period',
        'lisjp.label.unit': 'Enhet',
        'lisjp.label.yes': 'Ja',
        'lisjp.label.no': 'Nej',

        'lisjp.label.confirmedby': 'Ovanstående uppgifter och bedömningar bekräftas',
        'lisjp.label.date': 'Datum',
        'lisjp.label.contactinfo': 'Namn och kontaktuppgifter till vårdenheten',

        'lisjp.message.certificateloading': 'Hämtar intyg...',
        'lisjp.recipients.label.pagetitle': 'Skicka intyg - välj mottagare',
        'lisjp.button.send': 'Skicka',
        'lisjp.button.cancel': 'Avbryt',
        'lisjp.button.goback': 'Tillbaka',
        'lisjp.button.next': 'Nästa',
        'lisjp.button.previous': 'Föregående steg',

        'lisjp.fishbone.label.valj-mottagare': 'Välj mottagare',
        'lisjp.fishbone.label.granska-skicka': 'Granska och skicka',
        'lisjp.fishbone.label.bekraftelse': 'Bekräftelse',

        'lisjp.vardkontakt.5880005': 'Min undersökning av patienten den %0',
        'lisjp.vardkontakt.undersokning': 'Min undersökning av patienten den ',
        'lisjp.vardkontakt.185317003': 'Min telefonkontakt med patienten den %0',
        'lisjp.vardkontakt.telefonkontakt': 'Min telefonkontakt med patienten den ',
        'lisjp.referens.419891008': 'Journaluppgifter, den %0',
        'lisjp.referens.journal': 'Journaluppgifter, den ',
        'lisjp.referens.kannedomompatient': 'Kännedom om patient, den ',
        'lisjp.referens.74964007': 'Annat, den %0',
        'lisjp.referens.annat': 'Annat, den ',
        'lisjp.common.cancel': 'Avbryt',

        'lisjp.error.generic': 'Intyget kunde inte visas. Om felet kvarstår kan du kontakta <a href="/web/start/#/hjalp">support</a>. Om du inte kan komma åt intyget på Mina intyg just nu så kan du kontakta din läkare för att få en kopia.<br><br><a href="/web/start">Gå till Inkorgen och försök igen</a>',
        'lisjp.error.couldnotsend': 'Intyget kunde inte skickas till Försäkringskassan. Om felet kvarstår kan du kontakta <a href="/web/start/#/hjalp">support</a>.<br><br><a href="/web/start">Gå till Inkorgen och försök igen</a>',
        'lisjp.error.certnotfound': 'Intygen i Inkorgen kunde inte visas. Om felet kvarstår kan du kontakta <a href="/web/start/#/hjalp">support</a>. Om du inte kan komma åt intyget på Mina intyg just nu så kan du kontakta din läkare för att få en kopia.<br><br><a href="/web/start">Gå till Inkorgen och försök igen</a>',
        'lisjp.error.failedreceiverecipients': 'Kunde inte ladda mottagarlistan. Om felet kvarstår kan du kontakta <a href="/web/start/#/hjalp">support</a>.<br><br><a href="/web/start">Gå till Inkorgen och försök igen</a>'
    },
    'en': {
        'lisjp.label.pagetitle': 'Show Certificate'
    }
});
