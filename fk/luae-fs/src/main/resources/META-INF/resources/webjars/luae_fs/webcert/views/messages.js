/* jshint maxlen: false, unused: false */
var luae_fsMessages = {
    'sv': {
        'luae_fs.error.generic': 'Kunde inte visa intyget',
        'luae_fs.info.loadingcertificate': 'Hämtar intyget..',
        'luae_fs.message.certificateloading': 'Hämtar intyg...',

        'luae_fs.label.send': 'Skicka intyg till Försäkringskassan',
        'luae_fs.label.send.body': 'Upplys patienten om att även göra en ansökan om sjukpenning hos Försäkringskassan.',
        'luae_fs.button.send': 'Skicka till Försäkringskassan',

        // Sjukpenning utökad
        'luae_fs.label.certtitle': 'Läkarutlåtande för aktivitetsersättning vid förlängd skolgång',

        // TODO: REMOVE THESE AND GENERATE from dynamic labels instead
        'luae_fs.label.grundformu': 'Utlåtandet är baserat på',
        'luae_fs.label.diagnos': 'Diagnos/diagnoser som orsakar nedsatt arbetsförmåga',
        'luae_fs.label.sysselsattning': 'I relation till vilken sysselsättning gör du den medicinska bedömningen?',
        'luae_fs.label.funktionsnedsattning': 'Funktionsnedsättning',
        'luae_fs.label.aktivitetsbegransning': 'Aktivitetsbegränsning',
        'luae_fs.label.bedomning': 'Bedömning',
        'luae_fs.label.atgarder': 'Åtgärder',
        'luae_fs.label.kontakt': 'Vill du att Försäkringskassan kontaktar dig?',

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
        'luae_fs.validation.grund-for-mu.telefonkontakt.incorrect_format': 'Fel datumformat för telefonkontakt',
        'luae_fs.validation.grund-for-mu.journaluppgifter.incorrect_format': 'Fel datumformat för journaluppgifter',
        'luae_fs.validation.grund-for-mu.annat.incorrect_format': 'Fel datumformat för annan referens',
        'luae_fs.validation.grund-for-mu.annat.beskrivning.missing': 'Fritextfältet som hör till alternativet Annat måste fyllas i.',

        'luae_fs.validation.sysselsattning.missing': 'Sysselsättning måste fyllas i.',
        'luae_fs.validation.sysselsattning.nuvarandearbete.missing': 'När nuvarande arbete är valt måste även beskrivningen av detta fyllas i.',
        'luae_fs.validation.sysselsattning.nuvarandearbete.invalid_combination': 'Beskrivningen får inte fyllas i när inte nuvarande arbete valts.', // Should never happen because GUI should block this combination
        'luae_fs.validation.sysselsattning.ampolitisktprogram.missing': 'Beskrivning för arbetsmarknadspolitiskt program måste fyllas i.',
        'luae_fs.validation.sysselsattning.ampolitisktprogram.invalid_combination': 'Beskrivning för arbetsmarknadspolitiskt program måste bara fyllas i om arbetsmarknadspolitiskt program valts.', // Should never happen because GUI should block this combination

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

        'luae_fs.validation.funktionsnedsattning.missing': 'Funktionsnedsättning måste fyllas i.',
        'luae_fs.validation.aktivitetsbegransning.missing': 'Aktivitetsbegränsning måste fyllas i.',

        'luae_fs.validation.bedomning.sjukskrivningar.missing': 'Minst en sjukskrivningsperiod måste anges.',
        'luae_fs.validation.bedomning.sjukskrivningar.sjukskrivningsgrad.missing': 'Tekniskt fel. Sjukskrivningsgrad saknas.', // Should never happen because GUI should block this combination
        'luae_fs.validation.bedomning.sjukskrivningar.period1.missing': 'Helt nedsatt: Tekniskt fel. Period saknas.', // Should never happen because GUI should block this combination
        'luae_fs.validation.bedomning.sjukskrivningar.period1.invalid_format': 'Helt nedsatt: Felaktigt datumformat.',
        'luae_fs.validation.bedomning.sjukskrivningar.period1.overlap': 'Helt nedsatt: Datumintervall överlappar.',
        'luae_fs.validation.bedomning.sjukskrivningar.period2.missing': '75% nedsatt: Tekniskt fel. Period saknas.', // Should never happen because GUI should block this combination
        'luae_fs.validation.bedomning.sjukskrivningar.period2.invalid_format': '75% nedsatt: Felaktigt datumformat.',
        'luae_fs.validation.bedomning.sjukskrivningar.period2.overlap': '75% nedsatt: Datumintervall överlappar.',
        'luae_fs.validation.bedomning.sjukskrivningar.period3.missing': '50% nedsatt: Tekniskt fel. Period saknas.', // Should never happen because GUI should block this combination
        'luae_fs.validation.bedomning.sjukskrivningar.period3.invalid_format': '50% nedsatt: Felaktigt datumformat.',
        'luae_fs.validation.bedomning.sjukskrivningar.period3.overlap': '50% nedsatt: Datumintervall överlappar.',
        'luae_fs.validation.bedomning.sjukskrivningar.period4.missing': '25% nedsatt: Period saknas.', // Should never happen because GUI should block this combination
        'luae_fs.validation.bedomning.sjukskrivningar.period4.invalid_format': '25% nedsatt: Felaktigt datumformat.',
        'luae_fs.validation.bedomning.sjukskrivningar.period4.overlap': '25% nedsatt: Datumintervall överlappar.',

        'luae_fs.validation.bedomning.sjukskrivningar.arbetstidsforlaggning.missing': 'Arbetstidsförläggning måste fyllas i om period 75%, 50% eller 25% har valts.',
        'luae_fs.validation.bedomning.sjukskrivningar.arbetstidsforlaggningmotivering.missing': 'Arbetstidsförläggning måste fyllas i om period 75%, 50% eller 25% har valts.',
        'luae_fs.validation.bedomning.sjukskrivningar.arbetstidsforlaggningmotivering.invalid_combination': 'Motivering till arbetstidsförläggning måste fyllas i om period 75%, 50% eller 25% har valts.', // Should never happen

        'luae_fs.validation.bedomning.fmb.empty' : 'Beskriv varför arbetsförmågan bedöms vara nedsatt längre tid än den som det försäkringsmedicinska beslutstödet anger kan inte fyllas i med endast blanksteg',
        'luae_fs.validation.bedomning.formagatrotsbegransning.empty' : ' Beskriv vad patienten kan göra trots sin sjukdom relaterat till sitt arbete kan inte fyllas i med endast blanksteg',

        'luae_fs.validation.bedomning.prognos.missing': 'Prognos måste fyllas i.',
        'luae_fs.validation.bedomning.prognos.fortydligande.missing': 'Förtydligande måste fyllas i om prognos oklar valts.',
        'luae_fs.validation.bedomning.prognos.fortydligande.invalid_combination': 'Förtydligande kan bara fyllas i om prognos oklar valts.', // Should never happen because GUI should block this combination

        'luae_fs.validation.atgarder.missing': 'Åtgärder måste väljas eller Inte aktuellt.',
        'luae_fs.validation.atgarder.inte_aktuellt_no_combine': 'Inte aktuellt kan inte kombineras med andra val.', // Should never happen because GUI should block this combination
        'luae_fs.validation.atgarder.invalid_combination': 'Beskrivning för arbetslivsinriktade åtgärder kan inte kombineras med åtgärd på detta sätt.', // Should never happen because GUI should block this combination
        'luae_fs.validation.atgarder.aktuelltbeskrivning.missing': 'Beskrivning av arbetslivsinriktade åtgärder måste fyllas i.',
        'luae_fs.validation.atgarder.too-many': 'För många val på åtgärder.', // Should never happen because GUI should block this combination
        'luae_fs.validation.atgarder.inte_aktuellt_missing_description' : 'Om Arbetslivsinriktade åtgärder inte är aktuellt måste fältet "Beskriv varför arbetslivsinriktade åtgärder just nu inte skulle underlätta återgång i arbete" fyllas i.',

        'luae_fs.validation.kontakt.invalid_combination': 'Anledning får inte fyllas i när man svarat nej på kontakt.',

        'luae_fs.validation.vardenhet.telefonnummer.missing': 'Telefonnummer för vårdenhet måste anges (Fält 15)',
        'luae_fs.validation.vardenhet.postadress.missing': 'Postadress för vårdenhet måste anges (Fält 15)',
        'luae_fs.validation.vardenhet.postnummer.missing': 'Postnummer för vårdenhet måste anges (Fält 15)',
        'luae_fs.validation.vardenhet.postnummer.incorrect-format': 'Postnummer har fel format (Fält 15)',
        'luae_fs.validation.vardenhet.postort.missing': 'Postort för vårdenhet måste anges (Fält 15)',

/*

         'luae_fs.validation.diagnosgrund.missing': 'Fältet \'När och var ställdes den/de diagnoser som orsakar nedsatt arbetsförmåga?\' måste fyllas i(Fält 3)',
         'luae_fs.validation.nybedomningdiagnosgrund.missing': 'Fältet \'Finns det skäl att göra en ny bedömning av diagnosen?\' måste fyllas i(Fält 3)',

         'luae_fs.validation.nedsattning.overlapping-date-interval': 'Två datuminterval med överlappande datum har angetts. (Fält 8b)',

         'luae_fs.validation.nedsattning.prognos.choose-one': 'Max ett alternativ kan väljas (Fält 10)',

         'luae_fs.validation.prognos.gar-ej-att-bedomma.beskrivning.missing': 'Fritextfältet som hör till alternativet Går ej att bedöma, förtydligande under \'Prognos - kommer patienten att få tillbaka sin arbetsförmåga i nuvarande arbete\' måste fyllas i. (Fält 10)',

         'luae_fs.validation.forandrat-ressatt.choose-one': 'Endast ett alternativ kan anges avseende förändrat ressätt. (Fält 11)',

 */
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
