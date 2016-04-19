/* jshint maxlen: false, unused: false */
var luaefsMessages = {
    'sv': {
        'luaefs.error.generic': 'Kunde inte visa intyget',
        'luaefs.info.loadingcertificate': 'Hämtar intyget..',
        'luaefs.message.certificateloading': 'Hämtar intyg...',

        'luaefs.label.send': 'Skicka intyg till Försäkringskassan',
        'luaefs.label.send.body': 'Upplys patienten om att även göra en ansökan om sjukpenning hos Försäkringskassan.',
        'luaefs.button.send': 'Skicka till Försäkringskassan',

        // Sjukpenning utökad
        'luaefs.label.certtitle': 'Läkarutlåtande för aktivitetsersättning vid förlängd skolgång',

        // TODO: REMOVE THESE AND GENERATE from dynamic labels instead
        'luaefs.label.grundformu': 'Utlåtandet är baserat på',
        'luaefs.label.diagnos': 'Diagnos/diagnoser som orsakar nedsatt arbetsförmåga',
        'luaefs.label.sysselsattning': 'I relation till vilken sysselsättning gör du den medicinska bedömningen?',
        'luaefs.label.funktionsnedsattning': 'Funktionsnedsättning',
        'luaefs.label.aktivitetsbegransning': 'Aktivitetsbegränsning',
        'luaefs.label.bedomning': 'Bedömning',
        'luaefs.label.atgarder': 'Åtgärder',
        'luaefs.label.kontakt': 'Vill du att Försäkringskassan kontaktar dig?',

        //Labels
        'luaefs.label.valjkodverk': 'Välj kodverk:',
        'luaefs.label.spara-utkast': 'Spara',
        'luaefs.label.ta-bort-utkast': 'Ta bort utkast',
        'luaefs.label.skriv-ut-utkast': 'Skriv ut',

        'luaefs.label.diagnoskod.icd': 'Diagnoskod enligt ICD-10 (huvuddiagnos): ',
        'luaefs.label.diagnoskod.ksh': 'Diagnoskod enligt KSH97-P (Primärvård, huvuddiagnos): ',
        'luaefs.label.diagnoskodverk.icd_10_se': 'ICD-10-SE',
        'luaefs.label.diagnoskodverk.ksh_97_p': 'KSH97-P (Primärvård)',
        'luaefs.label.vardenhet': 'Vårdenhetens adress',

        'luaefs.label.makulera.confirmation': 'Läkarintyg utställd på ${namn} – ${personnummer} är återtaget. Mottagare av intyget är notifierade om detta.',
        'luaefs.label.kopiera.text': 'Skapar en kopia av befintligt intyg. Det nya utkastet (kopian) kan ändras och signeras.',
        'luaefs.label.status.sent': 'Intyget är signerat och har skickats till Försäkringskassans system.',
        'luaefs.label.status.recieved': 'Intyget är signerat och mottaget av Försäkringskassans system.',
        'luaefs.label.status.signed': 'Intyget är signerat. Intyget är nu tillgängligt för patienten i webbtjänsten Mina intyg, som nås via 1177.se. Intyget går även att nå via Försäkringskassans e-tjänst för ansökan om luaefs. Om patienten inte har möjlighet att skicka intyget elektroniskt till Försäkringskassan kan du skicka intyget direkt till Försäkringskassan åt patienten. Observera att patientens samtycke då krävs.',

        'luaefs.label.datum': 'Datum',
        'luaefs.label.kontakt-info': 'Namn och kontaktuppgifter till vårdenheten',

        //Validation messages
        'luaefs.validation.grund-for-mu.missing': 'Välj minst ett alternativ som uppgifterna i intyget baseras på.',
        'luaefs.validation.grund-for-mu.undersokning.incorrect_format': 'Fel datumformat för undersökning av patient',
        'luaefs.validation.grund-for-mu.telefonkontakt.incorrect_format': 'Fel datumformat för telefonkontakt',
        'luaefs.validation.grund-for-mu.journaluppgifter.incorrect_format': 'Fel datumformat för journaluppgifter',
        'luaefs.validation.grund-for-mu.annat.incorrect_format': 'Fel datumformat för annan referens',
        'luaefs.validation.grund-for-mu.annat.beskrivning.missing': 'Fritextfältet som hör till alternativet Annat måste fyllas i.',

        'luaefs.validation.sysselsattning.missing': 'Sysselsättning måste fyllas i.',
        'luaefs.validation.sysselsattning.nuvarandearbete.missing': 'När nuvarande arbete är valt måste även beskrivningen av detta fyllas i.',
        'luaefs.validation.sysselsattning.nuvarandearbete.invalid_combination': 'Beskrivningen får inte fyllas i när inte nuvarande arbete valts.', // Should never happen because GUI should block this combination
        'luaefs.validation.sysselsattning.ampolitisktprogram.missing': 'Beskrivning för arbetsmarknadspolitiskt program måste fyllas i.',
        'luaefs.validation.sysselsattning.ampolitisktprogram.invalid_combination': 'Beskrivning för arbetsmarknadspolitiskt program måste bara fyllas i om arbetsmarknadspolitiskt program valts.', // Should never happen because GUI should block this combination

        'luaefs.validation.diagnos0.missing': 'ICD-10 kod saknas på huvuddiagnosen.',
        'luaefs.validation.diagnos0.invalid': 'ICD-10 kod på huvuddiagnosen är ej giltig',
        'luaefs.validation.diagnos0.length-3': 'Diagnoskod på huvuddiagnosen ska anges med så många positioner som möjligt, men minst tre positioner.',
        'luaefs.validation.diagnos0.psykisk.length-4': 'Diagnoskod på huvuddiagnosen ska anges med minst fyra positioner då en psykisk diagnos anges.',
        'luaefs.validation.diagnos0.description.missing': 'Diagnostext saknas på huvuddiagnosen',
        'luaefs.validation.diagnos1.missing': 'ICD-10 kod saknas på diagnosrad 2.',
        'luaefs.validation.diagnos1.invalid': 'ICD-10 kod på diagnosrad 2 är ej giltig',
        'luaefs.validation.diagnos1.length-3': 'Diagnoskod på diagnosrad 2 ska anges med så många positioner som möjligt, men minst tre positioner.',
        'luaefs.validation.diagnos1.psykisk.length-4': 'Diagnoskod på diagnosrad 2 ska anges med minst fyra positioner då en psykisk diagnos anges.',
        'luaefs.validation.diagnos1.description.missing': 'Diagnostext saknas på diagnosrad 2',
        'luaefs.validation.diagnos2.missing': 'ICD-10 kod saknas på diagnosrad 3.',
        'luaefs.validation.diagnos2.invalid': 'ICD-10 kod på diagnosrad 3 är ej giltig',
        'luaefs.validation.diagnos2.length-3': 'Diagnoskod på diagnosrad 3 ska anges med så många positioner som möjligt, men minst tre positioner.',
        'luaefs.validation.diagnos2.psykisk.length-4': 'Diagnoskod på diagnosrad 3 ska anges med minst fyra positioner då en psykisk diagnos anges.',
        'luaefs.validation.diagnos2.description.missing': 'Diagnostext saknas på diagnosrad 3',
        'luaefs.validation.diagnos.description.missing': 'Diagnostext saknas',

        'luaefs.validation.funktionsnedsattning.missing': 'Funktionsnedsättning måste fyllas i.',
        'luaefs.validation.aktivitetsbegransning.missing': 'Aktivitetsbegränsning måste fyllas i.',

        'luaefs.validation.bedomning.sjukskrivningar.missing': 'Minst en sjukskrivningsperiod måste anges.',
        'luaefs.validation.bedomning.sjukskrivningar.sjukskrivningsgrad.missing': 'Tekniskt fel. Sjukskrivningsgrad saknas.', // Should never happen because GUI should block this combination
        'luaefs.validation.bedomning.sjukskrivningar.period1.missing': 'Helt nedsatt: Tekniskt fel. Period saknas.', // Should never happen because GUI should block this combination
        'luaefs.validation.bedomning.sjukskrivningar.period1.invalid_format': 'Helt nedsatt: Felaktigt datumformat.',
        'luaefs.validation.bedomning.sjukskrivningar.period1.overlap': 'Helt nedsatt: Datumintervall överlappar.',
        'luaefs.validation.bedomning.sjukskrivningar.period2.missing': '75% nedsatt: Tekniskt fel. Period saknas.', // Should never happen because GUI should block this combination
        'luaefs.validation.bedomning.sjukskrivningar.period2.invalid_format': '75% nedsatt: Felaktigt datumformat.',
        'luaefs.validation.bedomning.sjukskrivningar.period2.overlap': '75% nedsatt: Datumintervall överlappar.',
        'luaefs.validation.bedomning.sjukskrivningar.period3.missing': '50% nedsatt: Tekniskt fel. Period saknas.', // Should never happen because GUI should block this combination
        'luaefs.validation.bedomning.sjukskrivningar.period3.invalid_format': '50% nedsatt: Felaktigt datumformat.',
        'luaefs.validation.bedomning.sjukskrivningar.period3.overlap': '50% nedsatt: Datumintervall överlappar.',
        'luaefs.validation.bedomning.sjukskrivningar.period4.missing': '25% nedsatt: Period saknas.', // Should never happen because GUI should block this combination
        'luaefs.validation.bedomning.sjukskrivningar.period4.invalid_format': '25% nedsatt: Felaktigt datumformat.',
        'luaefs.validation.bedomning.sjukskrivningar.period4.overlap': '25% nedsatt: Datumintervall överlappar.',

        'luaefs.validation.bedomning.sjukskrivningar.arbetstidsforlaggning.missing': 'Arbetstidsförläggning måste fyllas i om period 75%, 50% eller 25% har valts.',
        'luaefs.validation.bedomning.sjukskrivningar.arbetstidsforlaggningmotivering.missing': 'Arbetstidsförläggning måste fyllas i om period 75%, 50% eller 25% har valts.',
        'luaefs.validation.bedomning.sjukskrivningar.arbetstidsforlaggningmotivering.invalid_combination': 'Motivering till arbetstidsförläggning måste fyllas i om period 75%, 50% eller 25% har valts.', // Should never happen

        'luaefs.validation.bedomning.fmb.empty' : 'Beskriv varför arbetsförmågan bedöms vara nedsatt längre tid än den som det försäkringsmedicinska beslutstödet anger kan inte fyllas i med endast blanksteg',
        'luaefs.validation.bedomning.formagatrotsbegransning.empty' : ' Beskriv vad patienten kan göra trots sin sjukdom relaterat till sitt arbete kan inte fyllas i med endast blanksteg',

        'luaefs.validation.bedomning.prognos.missing': 'Prognos måste fyllas i.',
        'luaefs.validation.bedomning.prognos.fortydligande.missing': 'Förtydligande måste fyllas i om prognos oklar valts.',
        'luaefs.validation.bedomning.prognos.fortydligande.invalid_combination': 'Förtydligande kan bara fyllas i om prognos oklar valts.', // Should never happen because GUI should block this combination

        'luaefs.validation.atgarder.missing': 'Åtgärder måste väljas eller Inte aktuellt.',
        'luaefs.validation.atgarder.inte_aktuellt_no_combine': 'Inte aktuellt kan inte kombineras med andra val.', // Should never happen because GUI should block this combination
        'luaefs.validation.atgarder.invalid_combination': 'Beskrivning för arbetslivsinriktade åtgärder kan inte kombineras med åtgärd på detta sätt.', // Should never happen because GUI should block this combination
        'luaefs.validation.atgarder.aktuelltbeskrivning.missing': 'Beskrivning av arbetslivsinriktade åtgärder måste fyllas i.',
        'luaefs.validation.atgarder.too-many': 'För många val på åtgärder.', // Should never happen because GUI should block this combination
        'luaefs.validation.atgarder.inte_aktuellt_missing_description' : 'Om Arbetslivsinriktade åtgärder inte är aktuellt måste fältet "Beskriv varför arbetslivsinriktade åtgärder just nu inte skulle underlätta återgång i arbete" fyllas i.',

        'luaefs.validation.kontakt.invalid_combination': 'Anledning får inte fyllas i när man svarat nej på kontakt.',

        'luaefs.validation.vardenhet.telefonnummer.missing': 'Telefonnummer för vårdenhet måste anges (Fält 15)',
        'luaefs.validation.vardenhet.postadress.missing': 'Postadress för vårdenhet måste anges (Fält 15)',
        'luaefs.validation.vardenhet.postnummer.missing': 'Postnummer för vårdenhet måste anges (Fält 15)',
        'luaefs.validation.vardenhet.postnummer.incorrect-format': 'Postnummer har fel format (Fält 15)',
        'luaefs.validation.vardenhet.postort.missing': 'Postort för vårdenhet måste anges (Fält 15)',

/*

         'luaefs.validation.diagnosgrund.missing': 'Fältet \'När och var ställdes den/de diagnoser som orsakar nedsatt arbetsförmåga?\' måste fyllas i(Fält 3)',
         'luaefs.validation.nybedomningdiagnosgrund.missing': 'Fältet \'Finns det skäl att göra en ny bedömning av diagnosen?\' måste fyllas i(Fält 3)',

         'luaefs.validation.nedsattning.overlapping-date-interval': 'Två datuminterval med överlappande datum har angetts. (Fält 8b)',

         'luaefs.validation.nedsattning.prognos.choose-one': 'Max ett alternativ kan väljas (Fält 10)',

         'luaefs.validation.prognos.gar-ej-att-bedomma.beskrivning.missing': 'Fritextfältet som hör till alternativet Går ej att bedöma, förtydligande under \'Prognos - kommer patienten att få tillbaka sin arbetsförmåga i nuvarande arbete\' måste fyllas i. (Fält 10)',

         'luaefs.validation.forandrat-ressatt.choose-one': 'Endast ett alternativ kan anges avseende förändrat ressätt. (Fält 11)',

 */
        // errors
        'luaefs.error.could_not_load_cert': '<strong>Intyget gick inte att läsa in.</strong><br>Prova att ladda om sidan. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand Nationell kundservice på 0771-251010.',
        'luaefs.error.could_not_load_cert_not_auth': '<strong>Kunde inte hämta intyget eftersom du saknar behörighet.</strong>',
        'luaefs.error.could_not_load_cert_qa': '<strong>Frågor och svar för detta intyg gick inte att läsa in. Prova att ladda om sidan. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand Nationell kundservice på 0771-251010.</strong>',
        // fragaSvar errors
        'luaefs.error.external_system_problem': '<strong>Meddelandet har inte skickats till Försäkringskassan då Webcert saknar kontakt med Försäkringskassans datasystem.</strong><br>Prova att skicka om meddelandet. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand Nationell kundservice på 0771-251010.',
        'luaefs.error.internal_problem': '<strong>Ett tekniskt problem inträffade.</strong><br>Försök igen och kontakta supporten om problemet kvarstår.',
        'luaefs.error.invalid_state': '<strong>Funktionen är inte giltig.</strong><br>Förmodligen har en annan användare ändrat informationen medan du arbetat på samma post. Ladda om informationen och försök igen',
        'luaefs.error.unknown_internal_problem': '<strong>Ett tekniskt problem inträffade.</strong><br>Försök igen och kontakta supporten om problemet kvarstår.',
        'luaefs.error.authorization_problem': '<strong>Du har inte behörighet att utföra funktionen, kontakta supporten för mer information.</strong>',
        'luaefs.error.data_not_found': '<strong>Intyget kunde inte hittas i intygstjänsten.</strong><br>Intyget är borttaget eller så saknas behörighet.'

    },
    'en': {
        'luaefs.label.pagetitle': 'Show Certificate'
    }
};
