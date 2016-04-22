/* jshint maxlen: false, unused: false */
var luae_fsMessages = {
    'sv': {
        'luae_fs.error.generic': 'Kunde inte visa intyget',
        'luae_fs.info.loadingcertificate': 'Hämtar intyget..',
        'luae_fs.message.certificateloading': 'Hämtar intyg...',

        'luae_fs.label.send': 'Skicka intyg till Försäkringskassan',
        'luae_fs.label.send.body': 'Upplys patienten om att även göra en ansökan om sjukpenning hos Försäkringskassan.',
        'luae_fs.button.send': 'Skicka till Försäkringskassan',

        // title för header
        'luae_fs.label.certtitle': 'Läkarutlåtande för aktivitetsersättning vid förlängd skolgång',

        //Labels
        'luae_fs.label.valjkodverk': 'Välj kodverk:',
        'luae_fs.label.spara-utkast': 'Spara',
        'luae_fs.label.ta-bort-utkast': 'Ta bort utkast',
        'luae_fs.label.skriv-ut-utkast': 'Skriv ut',

        'luae_fs.label.diagnoskod.icd': 'Diagnoskod enligt ICD-10 (huvuddiagnos): ',
        'luae_fs.label.diagnoskod.ksh': 'Diagnoskod enligt KSH97-P (Primärvård, huvuddiagnos): ',
        'luae_fs.label.diagnoskodverk.icd_10_se': 'ICD-10-SE',
        'luae_fs.label.diagnoskodverk.ksh_97_p': 'KSH97-P (Primärvård)',
        'luae_fs.label.vardenhet': 'Vårdenhetens adress',

        'luae_fs.label.makulera.confirmation': 'Läkarintyg utställd på ${namn} – ${personnummer} är återtaget. Mottagare av intyget är notifierade om detta.',
        'luae_fs.label.kopiera.text': 'Skapar en kopia av befintligt intyg. Det nya utkastet (kopian) kan ändras och signeras.',
        'luae_fs.label.status.sent': 'Intyget är signerat och har skickats till Försäkringskassans system.',
        'luae_fs.label.status.recieved': 'Intyget är signerat och mottaget av Försäkringskassans system.',
        'luae_fs.label.status.signed': 'Intyget är signerat. Intyget är nu tillgängligt för patienten i webbtjänsten Mina intyg, som nås via 1177.se. Intyget går även att nå via Försäkringskassans e-tjänst för ansökan om luae_fs. Om patienten inte har möjlighet att skicka intyget elektroniskt till Försäkringskassan kan du skicka intyget direkt till Försäkringskassan åt patienten. Observera att patientens samtycke då krävs.',

        'luae_fs.label.datum': 'Datum',
        'luae_fs.label.kontakt-info': 'Namn och kontaktuppgifter till vårdenheten',

        //Validation messages
        'luae_fs.validation.grund-for-mu.missing': 'Välj minst ett alternativ som uppgifterna i intyget baseras på.',
        'luae_fs.validation.grund-for-mu.undersokning.incorrect_format': 'Fel datumformat för undersökning av patient',
        'luae_fs.validation.grund-for-mu.anhorigsbeskrivning.incorrect_format': 'Fel datumformat för anhörigs beskrivning',
        'luae_fs.validation.grund-for-mu.journaluppgifter.incorrect_format': 'Fel datumformat för journaluppgifter',
        'luae_fs.validation.grund-for-mu.annat.incorrect_format': 'Fel datumformat för annan referens',
        'luae_fs.validation.grund-for-mu.annat.beskrivning.missing': 'Fritextfältet som hör till alternativet Annat måste anges.',
        'luae_fs.validation.grund-for-mu.kannedom.missing' : 'Fältet \'Jag har känt patienten sedan\' måste fyllas i.',
        'luae_fs.validation.grund-for-mu.kannedom.incorrect_format': 'Fel datumformat för kännedom om patienten',
        'luae_fs.validation.grund-for-mu.kannedom.after.undersokning' : 'Datum förkännedom om patienten får inte vara senare än datum för undersökning',
        'luae_fs.validation.grund-for-mu.kannedom.after.anhorigsbeskrivning' : 'Datum förkännedom om patienten får inte vara senare än datum för anhörigs beskrivning',

        'luae_fs.validation.underlagfinns.missing': 'Frågan \'Finns det andra medicinska utredningar eller underlag\' måste besvaras',
        'luae_fs.validation.underlagfinns.incorrect_combination' : 'Om frågan \'Finns det andra medicinska utredningar eller underlag\' besvarats med nej får underlag inte anges.',
        'luae_fs.validation.underlag.missing': 'Du måste ange ett underlag.',
        'luae_fs.validation.underlag.incorrect_format' : 'Fel typ av underlag',
        'luae_fs.validation.underlag.date.missing': 'Du måste ange datum för underlaget.',
        'luae_fs.validation.underlag.hamtas-fran.missing': 'Du måste ange var Försäkringskassan kan få information om utredningen.',
        'luae_fs.validation.underlag.date.incorrect_format': 'Fel datumformat för underlag',

        'luse.validation.underlag.max-extra-underlag': 'Du kan inte lägga till fler utredningar, max antal är 10st',

        'luae_fs.validation.diagnos.max-diagnoser': 'Du kan endast ange upp till tre st diagnoser.',

        'luae_fs.validation.diagnos0.missing': 'ICD-10 kod saknas på huvuddiagnosen.',
        'luae_fs.validation.diagnos0.invalid': 'ICD-10 kod på huvuddiagnosen är ej giltig',
        'luae_fs.validation.diagnos0.length-3': 'Diagnoskod på huvuddiagnosen ska anges med så många positioner som möjligt, men minst tre positioner.',
        'luae_fs.validation.diagnos0.psykisk.length-4': 'Diagnoskod på huvuddiagnosen ska anges med minst fyra positioner då en psykisk diagnos anges.',
        'luae_fs.validation.diagnos0.description.missing': 'Diagnostext saknas på huvuddiagnosen',
        'luae_fs.validation.diagnos1.missing': 'ICD-10 kod saknas på diagnosrad 2.',
        'luae_fs.validation.diagnos1.invalid': 'ICD-10 kod på diagnosrad 2 är ej giltig',
        'luae_fs.validation.diagnos1.length-3': 'Diagnoskod på diagnosrad 2 ska anges med så många positioner som möjligt, men minst tre positioner.',
        'luae_fs.validation.diagnos1.psykisk.length-4': 'Diagnoskod på diagnosrad 2 ska anges med minst fyra positioner då en psykisk diagnos anges.',
        'luae_fs.validation.diagnos1.description.missing': 'Diagnostext saknas på diagnosrad 2',
        'luae_fs.validation.diagnos2.missing': 'ICD-10 kod saknas på diagnosrad 3.',
        'luae_fs.validation.diagnos2.invalid': 'ICD-10 kod på diagnosrad 3 är ej giltig',
        'luae_fs.validation.diagnos2.length-3': 'Diagnoskod på diagnosrad 3 ska anges med så många positioner som möjligt, men minst tre positioner.',
        'luae_fs.validation.diagnos2.psykisk.length-4': 'Diagnoskod på diagnosrad 3 ska anges med minst fyra positioner då en psykisk diagnos anges.',
        'luae_fs.validation.diagnos2.description.missing': 'Diagnostext saknas på diagnosrad 3',
        'luae_fs.validation.diagnos.description.missing': 'Diagnostext saknas',

        'luae_fs.validation.funktionsnedsattning.debut.missing': 'Funktionsnedsättningens debut och utveckling måste fyllas i.',
        'luae_fs.validation.funktionsnedsattning.paverkan.missing': 'Funktionsnedsättningens påverkan måste fyllas i.',

        'luae_fs.validation.kontakt.invalid_combination': 'Anledning får inte fyllas i när man svarat nej på kontakt.',

        'luae_fs.validation.vardenhet.telefonnummer.missing': 'Telefonnummer för vårdenhet måste anges.',
        'luae_fs.validation.vardenhet.postadress.missing': 'Postadress för vårdenhet måste anges.',
        'luae_fs.validation.vardenhet.postnummer.missing': 'Postnummer för vårdenhet måste anges.',
        'luae_fs.validation.vardenhet.postnummer.incorrect-format': 'Postnummer har fel format.',
        'luae_fs.validation.vardenhet.postort.missing': 'Postort för vårdenhet måste anges.',

        // errors
        'luae_fs.error.could_not_load_cert': '<strong>Intyget gick inte att läsa in.</strong><br>Prova att ladda om sidan. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand Nationell kundservice på 0771-251010.',
        'luae_fs.error.could_not_load_cert_not_auth': '<strong>Kunde inte hämta intyget eftersom du saknar behörighet.</strong>',
        'luae_fs.error.could_not_load_cert_qa': '<strong>Frågor och svar för detta intyg gick inte att läsa in. Prova att ladda om sidan. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand Nationell kundservice på 0771-251010.</strong>',
        // fragaSvar errors
        'luae_fs.error.external_system_problem': '<strong>Meddelandet har inte skickats till Försäkringskassan då Webcert saknar kontakt med Försäkringskassans datasystem.</strong><br>Prova att skicka om meddelandet. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand Nationell kundservice på 0771-251010.',
        'luae_fs.error.internal_problem': '<strong>Ett tekniskt problem inträffade.</strong><br>Försök igen och kontakta supporten om problemet kvarstår.',
        'luae_fs.error.invalid_state': '<strong>Funktionen är inte giltig.</strong><br>Förmodligen har en annan användare ändrat informationen medan du arbetat på samma post. Ladda om informationen och försök igen',
        'luae_fs.error.unknown_internal_problem': '<strong>Ett tekniskt problem inträffade.</strong><br>Försök igen och kontakta supporten om problemet kvarstår.',
        'luae_fs.error.authorization_problem': '<strong>Du har inte behörighet att utföra funktionen, kontakta supporten för mer information.</strong>',
        'luae_fs.error.data_not_found': '<strong>Intyget kunde inte hittas i intygstjänsten.</strong><br>Intyget är borttaget eller så saknas behörighet.'

    },
    'en': {
        'luae_fs.label.pagetitle': 'Show Certificate'
    }
};
