/* jshint maxlen: false */
angular.module('luse').constant('luse.messages', {
    'sv': {
        'luse.recipient.label.pagetitle': 'Skicka intyg - steg 1 av 3',
        'luse.recipient.label.selectrecipents': 'Här ser du de mottagare som du kan skicka intyget till elektroniskt. Välj önskad mottagare och klicka sedan på knappen Nästa för att gå vidare till nästa steg.',
        'luse.recipient.label.recipientlisttitle': 'Myndigheter och försäkringsbolag',
        'luse.recipient.label.recipientlisttitle.others': 'Myndigheter och försäkringsbolag',
        'luse.recipient.label.missing.recipients-1': 'Om du saknar din mottagare i listan beror det troligen på att mottagaren inte kan ta emot elektroniska läkarintyg. Du kan då ',
        'luse.recipient.label.missing.recipients-2': ' och skicka det per post istället.',

        'luse.confirm.label.pagetitle': 'Skicka intyg - steg 2 av 3',
        'luse.confirm.label.certrowtitle': 'Intyg',
        'luse.confirm.label.recipienttitle': 'Mottagare',
        'luse.confirm.label.summarypagedesc': '<p><b>Du har valt att skicka det här intyget.</b></p>',
        'luse.confirm.label.verifytext': 'Kontrollera att uppgifterna ovan stämmer och tryck "Skicka"',
        'luse.confirm.message.alreadysent': '<strong>Observera!</strong> Intyget har redan skickats till: <br>',

        'luse.sent.label.pagetitle': 'Skicka intyg - steg 3 av 3',
        'luse.sent.label.pageinformation': 'Intyget har nu skickats och nedan presenteras försändelsen.',
        'luse.sent.label.result': 'Mottagare av intyget: <br>',
        'luse.sent.label.infoaboutrecipients': 'Observera att olika mottagare har olika tider när dom kan se det inskickade intyget.',
        'luse.sent.button.backtocertificate': 'Tillbaka till intyget',

        'luse.history.label.pagetitle': 'Intygets alla händelser',

        'luse.status.sent': 'Mottaget av',

        'luse.target.fk': 'Försäkringskassan',
        'luse.target.afa': 'AFA Försäkring',
        'luse.target.skandia': 'Skandia',

        'luse.inbox.complementaryinfo': 'Intygsperiod',

        'luse.button.send': 'Skicka',
        'luse.button.cancel': 'Avbryt',
        'luse.button.goback': 'Tillbaka',
        'luse.button.next': 'Nästa',
        'luse.button.previous': 'Föregående steg',
        'luse.button.sendtofk': 'Skicka till Försäkringskassan',
        'luse.button.send.certificate': 'Skicka',
        'luse.button.send.certificate.title': 'Skicka detta intyg till en eller flera mottagare.',

        // Labels
        'luse.label.latestevent': 'Senaste händelse',
        'luse.label.latestevent.noevents': 'Inga händelser',

        'luse.label.pagetitle': 'Granska och skicka intyg',
        'luse.label.pageingress': 'Här visas hela ditt läkarintyg. Från den här sidan kan du skicka intyget elektroniskt till Försäkringskassan.<br>',

        'luse.label.pagedescription.sendcertificate.header': '<h2>Skicka intyg</h2>',
        'luse.label.pagedescription.sendcertificate': 'Klicka på knappen Skicka för att skicka intyget elektroniskt till Försäkringskassan eller en annan mottagare.<br>',
        'luse.label.pagedescription.error.in.certificate.header': '<h2>Fel i intyget?</h2>',
        'luse.label.pagedescription.error.in.certificate': 'Om någon uppgift är fel i läkarintyget så ska du kontakta den som utfärdat ditt läkarintyg.',
        'luse.label.pagedescription.archive.header': '<h2>Arkivera</h2>',
        'luse.label.pagedescription.archive': 'För att arkivera intyget klickar du på symbolen Arkivera intyg <span class="glyphicon glyphicon-folder-close"></span>.',

        'luse.label.status.false': '<span class="glyphicon glyphicon-chevron-up"></span> Visa mindre',
        'luse.label.status.true': '<span class="glyphicon glyphicon-chevron-down"></span> Visa mer',

        'luse.label.showevents.false': '<span class="glyphicon glyphicon-chevron-up"></span> Visa färre händelser',
        'luse.label.showevents.true': '<span class="glyphicon glyphicon-chevron-down"></span> Visa alla händelser',

        'luse.label.patientname': 'Patientens namn',
        'luse.label.issued': 'Inkom till Mina intyg',
        'luse.label.civicnumber': 'Personnummer',
        'luse.label.issuer': 'Utfärdare',
        'luse.label.period': 'Period',
        'luse.label.unit': 'Enhet',
        'luse.label.yes': 'Ja',
        'luse.label.no': 'Nej',

        'luse.label.confirmedby': 'Ovanstående uppgifter och bedömningar bekräftas',
        'luse.label.date': 'Datum',
        'luse.label.contactinfo': 'Namn och kontaktuppgifter till vårdenheten',

        'luse.message.certificateloading': 'Hämtar intyg...',
        'luse.recipients.label.pagetitle': 'Skicka intyg - välj mottagare',

        'luse.fishbone.label.valj-mottagare': 'Välj mottagare',
        'luse.fishbone.label.granska-skicka': 'Granska och skicka',
        'luse.fishbone.label.bekraftelse': 'Bekräftelse',

        'luse.helptext.readpdfhelp': '<b>Läsa en pdf-fil</b><br/>PDF är ett filformat som används för att ett dokument ska se likadant ut i olika datorer. För att kunna öppna pdf-filer behöver du en pdf-läsare exempelvis. <a href="http://get.adobe.com/se/reader/" target="_blank">Adobe Reader</a>.',

        'luse.common.cancel': 'Avbryt',

        'luse.error.generic': 'Intyget kunde inte visas. Om felet kvarstår kan du kontakta <a href="/web/start/#/hjalp">support</a>. Om du inte kan komma åt intyget på Mina intyg just nu så kan du kontakta din läkare för att få en kopia.<br><br><a href="/web/start">Gå till Inkorgen och försök igen</a>',
        'luse.error.couldnotsend': 'Intyget kunde inte skickas till Försäkringskassan. Om felet kvarstår kan du kontakta <a href="/web/start/#/hjalp">support</a>.<br><br><a href="/web/start">Gå till Inkorgen och försök igen</a>',
        'luse.error.certnotfound': 'Intygen i Inkorgen kunde inte visas. Om felet kvarstår kan du kontakta <a href="/web/start/#/hjalp">support</a>. Om du inte kan komma åt intyget på Mina intyg just nu så kan du kontakta din läkare för att få en kopia.<br><br><a href="/web/start">Gå till Inkorgen och försök igen</a>',
        'luse.error.failedreceiverecipients': 'Kunde inte ladda mottagarlistan. Om felet kvarstår kan du kontakta <a href="/web/start/#/hjalp">support</a>.<br><br><a href="/web/start">Gå till Inkorgen och försök igen</a>'
    },
    'en': {
        'luse.label.pagetitle': 'Show Certificate'
    }
});
