/* jshint maxlen: false */
angular.module('luae_na').constant('luae_na.messages', {
    'sv': {
        'luae_na.recipient.label.pagetitle': 'Skicka intyg - steg 1 av 3',
        'luae_na.recipient.label.selectrecipents': 'Här ser du de mottagare som du kan skicka intyget till elektroniskt. Välj önskad mottagare och klicka sedan på knappen Nästa för att gå vidare till nästa steg.',
        'luae_na.recipient.label.recipientlisttitle': 'Myndigheter och försäkringsbolag',
        'luae_na.recipient.label.recipientlisttitle.others': 'Myndigheter och försäkringsbolag',
        'luae_na.recipient.label.missing.recipients-1': 'Om du saknar din mottagare i listan beror det troligen på att mottagaren inte kan ta emot elektroniska läkarintyg. Du kan då ',
        'luae_na.recipient.label.missing.recipients-2': ' och skicka det per post istället.',

        'luae_na.confirm.label.pagetitle': 'Skicka intyg - steg 2 av 3',
        'luae_na.confirm.label.certrowtitle': 'Intyg',
        'luae_na.confirm.label.recipienttitle': 'Mottagare',
        'luae_na.confirm.label.summarypagedesc': '<p><b>Du har valt att skicka det här intyget.</b></p>',
        'luae_na.confirm.label.verifytext': 'Kontrollera att uppgifterna ovan stämmer och tryck "Skicka"',
        'luae_na.confirm.message.alreadysent': '<strong>Observera!</strong> Intyget har redan skickats till: <br>',

        'luae_na.sent.label.pagetitle': 'Skicka intyg - steg 3 av 3',
        'luae_na.sent.label.pageinformation': 'Intyget har nu skickats och nedan presenteras försändelsen.',
        'luae_na.sent.label.result': 'Mottagare av intyget: <br>',
        'luae_na.sent.label.infoaboutrecipients': 'Observera att olika mottagare har olika tider när dom kan se det inskickade intyget.',
        'luae_na.sent.button.backtocertificate': 'Tillbaka till intyget',
        'luae_na.label.latestevent': 'Senaste händelse',
        'luae_na.label.latestevent.noevents': 'Inga händelser',
        'luae_na.history.label.pagetitle': 'Intygets alla händelser',
        'luae_na.status.sent': 'Mottaget av',
        'luae_na.target.fk': 'Försäkringskassan',
        'luae_na.target.afa': 'AFA Försäkring',
        'luae_na.target.skandia': 'Skandia',

        'luae_na.inbox.complementaryinfo': 'Intygsperiod',

        'luae_na.button.sendtofk': 'Skicka till Försäkringskassan',
        'luae_na.button.send.certificate': 'Skicka',
        'luae_na.button.send.certificate.title': 'Skicka detta intyg till en eller flera mottagare.',

        // Labels
        'luae_na.label.pagetitle': 'Granska och skicka intyg',
        'luae_na.label.pageingress': 'Här visas hela ditt läkarintyg. Från den här sidan kan du skicka intyget elektroniskt till Försäkringskassan, du kan även skriva ut och spara intyget.<br>',
        'luae_na.label.pagedescription.sendcertificate.header': '<h2>Skicka intyg</h2>',
        'luae_na.label.pagedescription.sendcertificate': 'Klicka på knappen Skicka för att skicka intyget elektroniskt till Försäkringskassan eller en annan mottagare.<br>',
        'luae_na.label.status.false': '<span class="glyphicon glyphicon-chevron-up"></span> Visa mindre',
        'luae_na.label.status.true': '<span class="glyphicon glyphicon-chevron-down"></span> Visa mer',
        'luae_na.label.showevents.false': '<span class="glyphicon glyphicon-chevron-up"></span> Visa färre händelser',
        'luae_na.label.showevents.true': '<span class="glyphicon glyphicon-chevron-down"></span> Visa alla händelser',

        'luae_na.label.pagedescription.certificate.to.employer.header': '<h2>Intyg till arbetsgivare</h2>',
        'luae_na.label.pagedescription.certificate.to.employer': 'Du har möjlighet att anpassa läkarintyget om du ska lämna läkarintyget till din arbetsgivare. Du anpassar intyget genom att välja om du vill visa alla uppgifter i intyget eller om du vill dölja vissa delar. Klicka på knappen Anpassa intyget för att välja vilken information du vill dela.',
        'luae_na.label.pagedescription.save.or.print.certificate.header': '<h2>Spara eller skriva ut</h2>',
        'luae_na.label.pagedescription.save.or.print.certificate': 'Klicka på knappen Spara som PDF för att spara eller skriva ut ditt intyg. Intyget öppnas eller laddas ner som en pdf-fil som du kan spara på din dator eller skriva ut.',
        'luae_na.label.pagedescription.sickness.benefit.header': '<h2>Vill du ansöka om luae_na?</h2>',
        'luae_na.label.pagedescription.sickness.benefit': 'Det gör du enklast på <a href="http://www.forsakringskassan.se/sjuk" target="_blank">www.forsakringskassan.se/sjuk</a>. Där kan du läsa mer om luae_na och hur du ansöker.<br>',
        'luae_na.label.pagedescription.error.in.certificate.header': '<h2>Fel i intyget?</h2>',
        'luae_na.label.pagedescription.error.in.certificate': 'Om någon uppgift är fel i läkarintyget så ska du kontakta den som utfärdat ditt läkarintyg.',
        'luae_na.label.pagedescription.archive.header': '<h2>Arkivera</h2>',
        'luae_na.label.pagedescription.archive': 'För att arkivera intyget klickar du på symbolen Arkivera intyg <span class="glyphicon glyphicon-folder-close"></span>.',

        'luae_na.helptext.readpdfhelp': '<b>Läsa en pdf-fil</b><br/>PDF är ett filformat som används för att ett dokument ska se likadant ut i olika datorer. För att kunna öppna pdf-filer behöver du en pdf-läsare exempelvis. <a href="http://get.adobe.com/se/reader/" target="_blank">Adobe Reader</a>.',
        'luae_na.label.patientname': 'Patientens namn',
        'luae_na.label.issued': 'Inkom till Mina intyg',
        'luae_na.label.civicnumber': 'Personnummer',
        'luae_na.label.issuer': 'Utfärdare',
        'luae_na.label.period': 'Period',
        'luae_na.label.unit': 'Enhet',
        'luae_na.label.yes': 'Ja',
        'luae_na.label.no': 'Nej',

        'luae_na.label.confirmedby': 'Ovanstående uppgifter och bedömningar bekräftas',
        'luae_na.label.date': 'Datum',
        'luae_na.label.contactinfo': 'Namn och kontaktuppgifter till vårdenheten',

        'luae_na.message.certificateloading': 'Hämtar intyg...',
        'luae_na.recipients.label.pagetitle': 'Skicka intyg - välj mottagare',
        'luae_na.button.send': 'Skicka',
        'luae_na.button.cancel': 'Avbryt',
        'luae_na.button.goback': 'Tillbaka',
        'luae_na.button.next': 'Nästa',
        'luae_na.button.previous': 'Föregående steg',

        'luae_na.fishbone.label.valj-mottagare': 'Välj mottagare',
        'luae_na.fishbone.label.granska-skicka': 'Granska och skicka',
        'luae_na.fishbone.label.bekraftelse': 'Bekräftelse',

        'luae_na.error.generic': 'Intyget kunde inte visas. Om felet kvarstår kan du kontakta <a href="/web/start/#/hjalp">support</a>. Om du inte kan komma åt intyget på Mina intyg just nu så kan du kontakta din läkare för att få en kopia.<br><br><a href="/web/start">Gå till Inkorgen och försök igen</a>',
        'luae_na.error.couldnotsend': 'Intyget kunde inte skickas till Försäkringskassan. Om felet kvarstår kan du kontakta <a href="/web/start/#/hjalp">support</a>.<br><br><a href="/web/start">Gå till Inkorgen och försök igen</a>',
        'luae_na.error.certnotfound': 'Intygen i Inkorgen kunde inte visas. Om felet kvarstår kan du kontakta <a href="/web/start/#/hjalp">support</a>. Om du inte kan komma åt intyget på Mina intyg just nu så kan du kontakta din läkare för att få en kopia.<br><br><a href="/web/start">Gå till Inkorgen och försök igen</a>',
        'luae_na.error.failedreceiverecipients': 'Kunde inte ladda mottagarlistan. Om felet kvarstår kan du kontakta <a href="/web/start/#/hjalp">support</a>.<br><br><a href="/web/start">Gå till Inkorgen och försök igen</a>'
    },
    'en': {
        'luae_na.label.pagetitle': 'Show Certificate'
    }
});
