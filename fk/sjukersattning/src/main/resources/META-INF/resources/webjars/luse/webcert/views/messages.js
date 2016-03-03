/* jshint maxlen: false, unused: false */
var sjukersattningMessages = {
    'sv': {
        'luse.error.generic': 'Kunde inte visa intyget',
        'luse.info.loadingcertificate': 'Hämtar intyget..',
        'luse.message.certificateloading': 'Hämtar intyg...',

        'luse.label.send': 'Skicka intyg till Försäkringskassan',
        'luse.label.send.body': 'Upplys patienten om att även göra en ansökan om sjukersattning hos Försäkringskassan.',
        'luse.button.send': 'Skicka till Försäkringskassan',

        // Sjukersattning
        'luse.label.certtitle': 'Läkarutlåtande för sjukersättning',

        // TODO: REMOVE THESE AND GENERATE from dynamic labels instead
        'luse.label.grundformu': 'Utlåtandet är baserat på',
        'luse.label.underlag': 'Andra medicinska utredningar och underlag',
        'luse.label.sjukdomsforlopp': 'Sjukdomsförlopp',
        'luse.label.diagnos': 'Diagnos/diagnoser som orsakar nedsatt arbetsförmåga',
        'luse.label.funktionsnedsattning': 'Funktionsnedsättning',
        'luse.label.aktivitetsbegransning': 'Aktivitetsbegränsning',
        'luse.label.medicinskaforutsattningarforarbete': 'Medicinska förutsättningar för arbete',

        //Labels
        'sjukersattning.label.valjkodverk': 'Välj kodverk:',
        'sjukersattning.label.spara-utkast': 'Spara',
        'sjukersattning.label.ta-bort-utkast': 'Ta bort utkast',
        'luse.label.skriv-ut-utkast': 'Skriv ut',

        'sjukersattning.label.diagnoskod.icd': 'Diagnoskod enligt ICD-10 (huvuddiagnos): ',
        'sjukersattning.label.diagnoskod.ksh': 'Diagnoskod enligt KSH97-P (Primärvård, huvuddiagnos): ',
        'sjukersattning.label.diagnoskodverk.icd_10_se': 'ICD-10-SE',
        'sjukersattning.label.diagnoskodverk.ksh_97_p': 'KSH97-P (Primärvård)',

        'luse.label.vardenhet': 'Vårdenhetens adress',

        'luse.label.status.sent': 'Intyget är signerat och har skickats till Försäkringskassans system.',
        'luse.label.status.recieved': 'Intyget är signerat och mottaget av Försäkringskassans system.',
        'luse.label.status.signed': 'Intyget är signerat. Intyget är nu tillgängligt för patienten i webbtjänsten Mina intyg, som nås via 1177.se. Intyget går även att nå via Försäkringskassans e-tjänst för ansökan om sjukersattning. Om patienten inte har möjlighet att skicka intyget elektroniskt till Försäkringskassan kan du skicka intyget direkt till Försäkringskassan åt patienten. Observera att patientens samtycke då krävs.',
        'luse.label.datum': 'Datum',
        'luse.label.kontakt-info': 'Namn och kontaktuppgifter till vårdenheten',
        'luse.label.makulera.confirmation': 'Läkarintyg utställd på ${namn} – ${personnummer} är återtaget. Mottagare av intyget är notifierade om detta.',

        //Validation messages
        'luse.validation.grund-for-mu.missing': 'Välj minst ett alternativ som uppgifterna i intyget baseras på. (Fält 1)',
        'luse.validation.grund-for-mu.incorrect_combination_annat_beskrivning' : 'Fritextfältet som hör till alternativet Annat under \'Uppgifterna i intyget baseras på\' (Fält 4) får endast fyllas i om \'Annan referens valts\' i Fält 1.',  
        'luse.validation.grund-for-mu.undersokning.incorrect_format': 'Fel datumformat för undersökning av patient (Fält 1)',
        'luse.validation.grund-for-mu.journaluppgifter.incorrect_format': 'Fel datumformat för journaluppgifter (Fält 1)',
        'luse.validation.grund-for-mu.anhorigsbeskrivning.incorrect_format': 'Fel datumformat för anhörigs beskrivning (Fält 1)',
        'luse.validation.grund-for-mu.annat.incorrect_format': 'Fel datumformat för annan referens (Fält 1)',
        'luse.validation.grund-for-mu.annat.missing': 'Fritextfältet som hör till alternativet Annat under \'Uppgifterna i intyget baseras på\' måste fyllas i. (Fält 4)',

        'luse.validation.grund-for-mu.kannedom.missing': 'Fältet \'Jag har känt patienten sedan\' måste fyllas i. (Fält 1)',
        'luse.validation.grund-for-mu.kannedom.incorrect_format': 'Fel datumformat för \'Kännedom om patienten\'',
        'luse.validation.grund-for-mu.kannedom.after.undersokning': '\'Kännedom om patienten\' får inte vara senare än datum för \'Min undersökning av patienten\'',
        'luse.validation.grund-for-mu.kannedom.after.anhorigsbeskrivning': '\'Kännedom om patienten\' får inte vara senare än datum för \'Anhörigs beskrivning av patienten\'',

        'luse.validation.underlagfinns.missing': 'Frågan \'Finns det andra medicinska utredningar eller underlag\' måste besvaras',
        'luse.validation.underlagfinns.incorrect_combination' : 'Om frågan \'Finns det andra medicinska utredningar eller underlag\' besvarats med nej får underlag inte anges.',
        'luse.validation.underlag.missing': 'Du måste ange ett underlag.',
        'luse.validation.underlag.date.missing': 'Du måste ange datum för underlaget.',
        'luse.validation.underlag.hamtas-fran.missing': 'Du måste ange var Försäkringskassan kan få information om utredningen.',

        'luse.validation.underlag.max-extra-underlag': 'Du kan inte lägga till fler utredningar, max antal är 10st',

        'luse.validation.sjukdomsforlopp.missing': '\'Beskrivning av sjukdomsförlopp\' måste anges',

        'luse.validation.diagnosgrund.missing': 'Fältet \'När och var ställdes den/de diagnoser som orsakar nedsatt arbetsförmåga?\' måste fyllas i(Fält 3)',
        'luse.validation.diagnos0.missing': 'ICD-10 kod saknas på huvuddiagnosen.',
        'luse.validation.diagnos0.invalid': 'ICD-10 kod på huvuddiagnosen är ej giltig',
        'luse.validation.diagnos0.length-3': 'Diagnoskod på huvuddiagnosen ska anges med så många positioner som möjligt, men minst tre positioner.',
        'luse.validation.diagnos0.psykisk.length-4': 'Diagnoskod på huvuddiagnosen ska anges med minst fyra positioner då en psykisk diagnos anges.',
        'luse.validation.diagnos0.description.missing': 'Diagnostext saknas på huvuddiagnosen',
        'luse.validation.diagnos1.missing': 'ICD-10 kod saknas på diagnosrad 2.',
        'luse.validation.diagnos1.invalid': 'ICD-10 kod på diagnosrad 2 är ej giltig',
        'luse.validation.diagnos1.length-3': 'Diagnoskod på diagnosrad 2 ska anges med så många positioner som möjligt, men minst tre positioner.',
        'luse.validation.diagnos1.psykisk.length-4': 'Diagnoskod på diagnosrad 2 ska anges med minst fyra positioner då en psykisk diagnos anges.',
        'luse.validation.diagnos1.description.missing': 'Diagnostext saknas på diagnosrad 2',
        'luse.validation.diagnos2.missing': 'ICD-10 kod saknas på diagnosrad 3.',
        'luse.validation.diagnos2.invalid': 'ICD-10 kod på diagnosrad 3 är ej giltig',
        'luse.validation.diagnos2.length-3': 'Diagnoskod på diagnosrad 3 ska anges med så många positioner som möjligt, men minst tre positioner.',
        'luse.validation.diagnos2.psykisk.length-4': 'Diagnoskod på diagnosrad 3 ska anges med minst fyra positioner då en psykisk diagnos anges.',
        'luse.validation.diagnos2.description.missing': 'Diagnostext saknas på diagnosrad 3',
        'luse.validation.nybedomningdiagnosgrund.missing': 'Fältet \'Finns det skäl att göra en ny bedömning av diagnosen?\' måste fyllas i(Fält 3)',

        'luse.validation.funktionsnedsattning.missing': 'Minst en funktionsnedsättning måste anges.',

        'luse.validation.aktivitetsbegransning.missing': 'Fältet med aktivitetsbegränsning måste fyllas i. (Fält 5)',

        'luse.validation.medicinskaforutsattningarforarbete.missing': 'Patientens medicinska förutsättningar för arbete måste anges.',

        'luse.validation.kontakt.incorrect_combination' : 'Anledning till kontakt kan endast fyllas i om \'kontakt med FK önskas\' är vald.',

        'luse.validation.vardenhet.telefonnummer.missing': 'Telefonnummer för vårdenhet måste anges (Fält 15)',
        'luse.validation.vardenhet.postadress.missing': 'Postadress för vårdenhet måste anges (Fält 15)',
        'luse.validation.vardenhet.postnummer.missing': 'Postnummer för vårdenhet måste anges (Fält 15)',
        'luse.validation.vardenhet.postnummer.incorrect-format': 'Postnummer har fel format (Fält 15)',
        'luse.validation.vardenhet.postort.missing': 'Postort för vårdenhet måste anges (Fält 15)',


        // errors
        'sjukersattning.error.could_not_load_cert': '<strong>Intyget gick inte att läsa in.</strong><br>Prova att ladda om sidan. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand Nationell kundservice på 0771-251010.',
        'sjukersattning.error.could_not_load_cert_not_auth': '<strong>Kunde inte hämta intyget eftersom du saknar behörighet.</strong>',
        'sjukersattning.error.could_not_load_cert_qa': '<strong>Frågor och svar för detta intyg gick inte att läsa in. Prova att ladda om sidan. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand Nationell kundservice på 0771-251010.</strong>',
        // fragaSvar errors
        'sjukersattning.error.external_system_problem': '<strong>Meddelandet har inte skickats till Försäkringskassan då Webcert saknar kontakt med Försäkringskassans datasystem.</strong><br>Prova att skicka om meddelandet. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand Nationell kundservice på 0771-251010.',
        'sjukersattning.error.internal_problem': '<strong>Ett tekniskt problem inträffade.</strong><br>Försök igen och kontakta supporten om problemet kvarstår.',
        'sjukersattning.error.invalid_state': '<strong>Funktionen är inte giltig.</strong><br>Förmodligen har en annan användare ändrat informationen medan du arbetat på samma post. Ladda om informationen och försök igen',
        'sjukersattning.error.unknown_internal_problem': '<strong>Ett tekniskt problem inträffade.</strong><br>Försök igen och kontakta supporten om problemet kvarstår.',
        'sjukersattning.error.authorization_problem': '<strong>Du har inte behörighet att utföra funktionen, kontakta supporten för mer information.</strong>',
        'sjukersattning.error.data_not_found': '<strong>Intyget kunde inte hittas i intygstjänsten.</strong><br>Intyget är borttaget eller så saknas behörighet.'

    },
    'en': {
        'sjukersattning.label.pagetitle': 'Show Certificate'
    }
};
