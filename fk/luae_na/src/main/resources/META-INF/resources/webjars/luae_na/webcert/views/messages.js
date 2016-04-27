/* jshint maxlen: false, unused: false */
var aktivitetsersattningNAMessages = {
    'sv': {
        'luae_na.error.generic': 'Kunde inte visa intyget',
        'luae_na.info.loadingcertificate': 'Hämtar intyget..',
        'luae_na.message.certificateloading': 'Hämtar intyg...',

        'luae_na.label.send': 'Skicka intyg till Försäkringskassan',
        'luae_na.label.send.body': 'Upplys patienten om att även göra en ansökan om luae_na hos Försäkringskassan.',
        'luae_na.button.send': 'Skicka till Försäkringskassan',

        // luae_na
        'luae_na.label.certtitle': 'Läkarutlåtande för aktivitetsersättning nedsatt arbetsförmåga',

        // TODO: REMOVE THESE AND GENERATE from dynamic labels instead
        'luae_na.label.grundformu': 'Utlåtandet är baserat på',
        'luae_na.label.underlag': 'Andra medicinska utredningar och underlag',
        'luae_na.label.sjukdomsforlopp': 'Sjukdomsförlopp',
        'luae_na.label.diagnos': 'Diagnos/diagnoser som orsakar nedsatt arbetsförmåga',
        'luae_na.label.funktionsnedsattning': 'Funktionsnedsättning',
        'luae_na.label.aktivitetsbegransning': 'Aktivitetsbegränsning',
        'luae_na.label.medicinskaforutsattningarforarbete': 'Medicinska förutsättningar för arbete',

        //Labels
        'luae_na.label.valj-version-icd-10': 'Välj version av ICD-10-SE:',
        'luae_na.label.spara-utkast': 'Spara',
        'luae_na.label.ta-bort-utkast': 'Ta bort utkast',
        'luae_na.label.skriv-ut-utkast': 'Skriv ut',

        'luae_na.label.diagnoskod.icd': 'Diagnoskod enligt ICD-10 (huvuddiagnos): ',
        'luae_na.label.diagnoskod.ksh': 'Diagnoskod enligt KSH97-P (Primärvård, huvuddiagnos): ',
        'luae_na.label.diagnoskodverk.fullstandig': 'Fullständig version',
        'luae_na.label.diagnoskodverk.primarvard': 'Primärvårdsversion',

        'luae_na.label.vardenhet': 'Vårdenhetens adress',

        'luae_na.label.status.sent': 'Intyget är signerat och har skickats till Försäkringskassans system.',
        'luae_na.label.status.recieved': 'Intyget är signerat och mottaget av Försäkringskassans system.',
        'luae_na.label.status.signed': 'Intyget är signerat. Intyget är nu tillgängligt för patienten i webbtjänsten Mina intyg, som nås via 1177.se. Intyget går även att nå via Försäkringskassans e-tjänst för ansökan om luae_na. Om patienten inte har möjlighet att skicka intyget elektroniskt till Försäkringskassan kan du skicka intyget direkt till Försäkringskassan åt patienten. Observera att patientens samtycke då krävs.',
        'luae_na.label.datum': 'Datum',
        'luae_na.label.kontakt-info': 'Namn och kontaktuppgifter till vårdenheten',
        'luae_na.label.makulera.confirmation': 'Läkarintyg utställd på ${namn} – ${personnummer} är återtaget. Mottagare av intyget är notifierade om detta.',
        'luae_na.label.kopiera.text': 'Skapar en kopia av befintligt intyg. Det nya utkastet (kopian) kan ändras och signeras.',

        //Validation messages
        'luae_na.validation.grund-for-mu.missing': 'Välj minst ett alternativ som uppgifterna i intyget baseras på. (Fält 1)',
        'luae_na.validation.grund-for-mu.incorrect_combination_annat_beskrivning' : 'Fritextfältet som hör till alternativet Annat under \'Uppgifterna i intyget baseras på\' (Fält 4) får endast fyllas i om \'Annan referens valts\' i Fält 1.',
        'luae_na.validation.grund-for-mu.undersokning.incorrect_format': 'Fel datumformat för undersökning av patient (Fält 1)',
        'luae_na.validation.grund-for-mu.journaluppgifter.incorrect_format': 'Fel datumformat för journaluppgifter (Fält 1)',
        'luae_na.validation.grund-for-mu.anhorigsbeskrivning.incorrect_format': 'Fel datumformat för anhörigs beskrivning (Fält 1)',
        'luae_na.validation.grund-for-mu.annat.incorrect_format': 'Fel datumformat för annan referens (Fält 1)',
        'luae_na.validation.grund-for-mu.annat.missing': 'Fritextfältet som hör till alternativet Annat under \'Uppgifterna i intyget baseras på\' måste fyllas i. (Fält 4)',

        'luae_na.validation.grund-for-mu.kannedom.missing': 'Fältet \'Jag har känt patienten sedan\' måste fyllas i. (Fält 1)',
        'luae_na.validation.grund-for-mu.kannedom.incorrect_format': 'Fel datumformat för \'Kännedom om patienten\'',
        'luae_na.validation.grund-for-mu.kannedom.after.undersokning': '\'Kännedom om patienten\' får inte vara senare än datum för \'Min undersökning av patienten\'',
        'luae_na.validation.grund-for-mu.kannedom.after.anhorigsbeskrivning': '\'Kännedom om patienten\' får inte vara senare än datum för \'Anhörigs beskrivning av patienten\'',

        'luae_na.validation.underlagfinns.missing': 'Frågan \'Finns det andra medicinska utredningar eller underlag\' måste besvaras',
        'luae_na.validation.underlagfinns.incorrect_combination' : 'Om frågan \'Finns det andra medicinska utredningar eller underlag\' besvarats med nej får underlag inte anges.',
        'luae_na.validation.underlag.missing': 'Du måste ange ett underlag.',
        'luae_na.validation.underlag.incorrect_format' : 'Fel typ av underlag',
        'luae_na.validation.underlag.date.missing': 'Du måste ange datum för underlaget.',
        'luae_na.validation.underlag.hamtas-fran.missing': 'Du måste ange var Försäkringskassan kan få information om utredningen.',
        'luae_na.validation.underlag.date.incorrect_format': 'Fel datumformat för underlag',

        'luae_na.validation.underlag.max-extra-underlag': 'Du kan inte lägga till fler utredningar, max antal är 10st',

        'luae_na.validation.sjukdomsforlopp.missing': '\'Beskrivning av sjukdomsförlopp\' måste anges',

        'luae_na.validation.diagnosgrund.missing': 'Fältet \'När och var ställdes den/de diagnoser som orsakar nedsatt arbetsförmåga?\' måste fyllas i(Fält 3)',
        'luae_na.validation.diagnos0.missing': 'ICD-10 kod saknas på huvuddiagnosen.',
        'luae_na.validation.diagnos0.invalid': 'ICD-10 kod på huvuddiagnosen är ej giltig',
        'luae_na.validation.diagnos0.length-3': 'Diagnoskod på huvuddiagnosen ska anges med så många positioner som möjligt, men minst tre positioner.',
        'luae_na.validation.diagnos0.psykisk.length-4': 'Diagnoskod på huvuddiagnosen ska anges med minst fyra positioner då en psykisk diagnos anges.',
        'luae_na.validation.diagnos0.description.missing': 'Diagnostext saknas på huvuddiagnosen',
        'luae_na.validation.diagnos1.missing': 'ICD-10 kod saknas på diagnosrad 2.',
        'luae_na.validation.diagnos1.invalid': 'ICD-10 kod på diagnosrad 2 är ej giltig',
        'luae_na.validation.diagnos1.length-3': 'Diagnoskod på diagnosrad 2 ska anges med så många positioner som möjligt, men minst tre positioner.',
        'luae_na.validation.diagnos1.psykisk.length-4': 'Diagnoskod på diagnosrad 2 ska anges med minst fyra positioner då en psykisk diagnos anges.',
        'luae_na.validation.diagnos1.description.missing': 'Diagnostext saknas på diagnosrad 2',
        'luae_na.validation.diagnos2.missing': 'ICD-10 kod saknas på diagnosrad 3.',
        'luae_na.validation.diagnos2.invalid': 'ICD-10 kod på diagnosrad 3 är ej giltig',
        'luae_na.validation.diagnos2.length-3': 'Diagnoskod på diagnosrad 3 ska anges med så många positioner som möjligt, men minst tre positioner.',
        'luae_na.validation.diagnos2.psykisk.length-4': 'Diagnoskod på diagnosrad 3 ska anges med minst fyra positioner då en psykisk diagnos anges.',
        'luae_na.validation.diagnos2.description.missing': 'Diagnostext saknas på diagnosrad 3',
        'luae_na.validation.nybedomningdiagnosgrund.missing': 'Fältet \'Finns det skäl att göra en ny bedömning av diagnosen?\' måste fyllas i(Fält 3)',

        'luae_na.validation.funktionsnedsattning.missing': 'Minst en funktionsnedsättning måste anges.',
        
        'luae_na.validation.forslagtillatgard.missing': 'Förslag till åtgärd ej giltig.',

        'luae_na.validation.aktivitetsbegransning.missing': 'Fältet med aktivitetsbegränsning måste fyllas i. (Fält 5)',

        'luae_na.validation.medicinskaforutsattningarforarbete.missing': 'Patientens medicinska förutsättningar för arbete måste anges.',

        'luae_na.validation.kontakt.incorrect_combination' : 'Anledning till kontakt kan endast fyllas i om \'kontakt med FK önskas\' är vald.',

        'luae_na.validation.vardenhet.telefonnummer.missing': 'Telefonnummer för vårdenhet måste anges (Fält 15)',
        'luae_na.validation.vardenhet.postadress.missing': 'Postadress för vårdenhet måste anges (Fält 15)',
        'luae_na.validation.vardenhet.postnummer.missing': 'Postnummer för vårdenhet måste anges (Fält 15)',
        'luae_na.validation.vardenhet.postnummer.incorrect-format': 'Postnummer har fel format (Fält 15)',
        'luae_na.validation.vardenhet.postort.missing': 'Postort för vårdenhet måste anges (Fält 15)',


        // errors
        'luae_na.error.could_not_load_cert': '<strong>Intyget gick inte att läsa in.</strong><br>Prova att ladda om sidan. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand Nationell kundservice på 0771-251010.',
        'luae_na.error.could_not_load_cert_not_auth': '<strong>Kunde inte hämta intyget eftersom du saknar behörighet.</strong>',
        'luae_na.error.could_not_load_cert_qa': '<strong>Frågor och svar för detta intyg gick inte att läsa in. Prova att ladda om sidan. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand Nationell kundservice på 0771-251010.</strong>',
        // fragaSvar errors
        'luae_na.error.external_system_problem': '<strong>Meddelandet har inte skickats till Försäkringskassan då Webcert saknar kontakt med Försäkringskassans datasystem.</strong><br>Prova att skicka om meddelandet. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand Nationell kundservice på 0771-251010.',
        'luae_na.error.internal_problem': '<strong>Ett tekniskt problem inträffade.</strong><br>Försök igen och kontakta supporten om problemet kvarstår.',
        'luae_na.error.invalid_state': '<strong>Funktionen är inte giltig.</strong><br>Förmodligen har en annan användare ändrat informationen medan du arbetat på samma post. Ladda om informationen och försök igen',
        'luae_na.error.unknown_internal_problem': '<strong>Ett tekniskt problem inträffade.</strong><br>Försök igen och kontakta supporten om problemet kvarstår.',
        'luae_na.error.authorization_problem': '<strong>Du har inte behörighet att utföra funktionen, kontakta supporten för mer information.</strong>',
        'luae_na.error.data_not_found': '<strong>Intyget kunde inte hittas i intygstjänsten.</strong><br>Intyget är borttaget eller så saknas behörighet.'

    },
    'en': {
        'luae_na.label.pagetitle': 'Show Certificate'
    }
};
