/* jshint maxlen: false, unused: false */
var sjukpenningUtokadMessages = {
    'sv': {
        'lisu.error.generic': 'Kunde inte visa intyget',
        'lisu.info.loadingcertificate': 'Hämtar intyget..',
        'lisu.message.certificateloading': 'Hämtar intyg...',

        'lisu.label.send': 'Skicka intyg till Försäkringskassan',
        'lisu.label.send.body': 'Upplys patienten om att även göra en ansökan om sjukpenning hos Försäkringskassan.',
        'lisu.button.send': 'Skicka till Försäkringskassan',

        // Sjukpenning utökad
        'lisu.label.certtitle': 'Läkarintyg för sjukpenning utökat',

        // TODO: REMOVE THESE AND GENERATE from dynamic labels instead
        'lisu.label.grundformu': 'Utlåtandet är baserat på',
        'lisu.label.diagnos': 'Diagnos/diagnoser som orsakar nedsatt arbetsförmåga',
        'lisu.label.sysselsattning': 'I relation till vilken sysselsättning gör du den medicinska bedömningen?',
        'lisu.label.funktionsnedsattning': 'Funktionsnedsättning',
        'lisu.label.aktivitetsbegransning': 'Aktivitetsbegränsning',
        'lisu.label.bedomning': 'Bedömning',
        'lisu.label.atgarder': 'Åtgärder',
        'lisu.label.kontakt': 'Vill du att Försäkringskassan kontaktar dig?',

        //Labels
        'lisu.label.valjkodverk': 'Välj kodverk:',
        'lisu.label.spara-utkast': 'Spara',
        'lisu.label.ta-bort-utkast': 'Ta bort utkast',
        'lisu.label.skriv-ut-utkast': 'Skriv ut',

        'lisu.label.diagnoskod.icd': 'Diagnoskod enligt ICD-10 (huvuddiagnos): ',
        'lisu.label.diagnoskod.ksh': 'Diagnoskod enligt KSH97-P (Primärvård, huvuddiagnos): ',
        'lisu.label.diagnoskodverk.icd_10_se': 'ICD-10-SE',
        'lisu.label.diagnoskodverk.ksh_97_p': 'KSH97-P (Primärvård)',
        'lisu.label.vardenhet': 'Vårdenhetens adress',

        'lisu.label.makulera.confirmation': 'Läkarintyg utställd på ${namn} – ${personnummer} är återtaget. Mottagare av intyget är notifierade om detta.',
        'lisu.label.status.sent': 'Intyget är signerat och har skickats till Försäkringskassans system.',
        'lisu.label.status.recieved': 'Intyget är signerat och mottaget av Försäkringskassans system.',
        'lisu.label.status.signed': 'Intyget är signerat. Intyget är nu tillgängligt för patienten i webbtjänsten Mina intyg, som nås via 1177.se. Intyget går även att nå via Försäkringskassans e-tjänst för ansökan om lisu. Om patienten inte har möjlighet att skicka intyget elektroniskt till Försäkringskassan kan du skicka intyget direkt till Försäkringskassan åt patienten. Observera att patientens samtycke då krävs.',

        'lisu.label.datum': 'Datum',
        'lisu.label.kontakt-info': 'Namn och kontaktuppgifter till vårdenheten',

        //Validation messages
        'lisu.validation.grund-for-mu.missing': 'Välj minst ett alternativ som uppgifterna i intyget baseras på.',
        'lisu.validation.grund-for-mu.undersokning.incorrect_format': 'Fel datumformat för undersökning av patient',
        'lisu.validation.grund-for-mu.telefonkontakt.incorrect_format': 'Fel datumformat för telefonkontakt',
        'lisu.validation.grund-for-mu.journaluppgifter.incorrect_format': 'Fel datumformat för journaluppgifter',
        'lisu.validation.grund-for-mu.annat.incorrect_format': 'Fel datumformat för annan referens',
        'lisu.validation.grund-for-mu.annat.beskrivning.missing': 'Fritextfältet som hör till alternativet Annat måste fyllas i.',

        'lisu.validation.sysselsattning.missing': 'Sysselsättning måste fyllas i.',
        'lisu.validation.sysselsattning.nuvarandearbete.missing': 'När nuvarande arbete är valt måste även beskrivningen av detta fyllas i.',
        'lisu.validation.sysselsattning.nuvarandearbete.invalid_combination': 'Beskrivningen får inte fyllas i när inte nuvarande arbete valts.', // Should never happen because GUI should block this combination
        'lisu.validation.sysselsattning.ampolitisktprogram.missing': 'Beskrivning för arbetsmarknadspolitiskt program måste fyllas i.',
        'lisu.validation.sysselsattning.ampolitisktprogram.invalid_combination': 'Beskrivning för arbetsmarknadspolitiskt program måste bara fyllas i om arbetsmarknadspolitiskt program valts.', // Should never happen because GUI should block this combination

        'lisu.validation.diagnos.missing': 'ICD-10 kod saknas på huvuddiagnosen.',
        'lisu.validation.diagnos.invalid': 'ICD-10 kod på huvuddiagnosen är ej giltig',
        'lisu.validation.diagnos.length-3': 'Diagnoskod ska anges med så många positioner som möjligt, men minst tre positioner.',
        'lisu.validation.diagnos.psykisk.length-4': 'Diagnoskod ska anges med minst fyra positioner då en psykisk diagnos anges.',
        'lisu.validation.diagnos2.invalid': 'ICD-10 kod på ytterligare diagnoser rad 1, är ej giltig.',
        'lisu.validation.diagnos3.invalid': 'ICD-10 kod på ytterligare diagnoser rad 2, är ej giltig.',
        'lisu.validation.diagnos.description.missing': 'Diagnostext saknas',

        'lisu.validation.funktionsnedsattning.missing': 'Funktionsnedsättning måste fyllas i.',
        'lisu.validation.aktivitetsbegransning.missing': 'Aktivitetsbegränsning måste fyllas i.',

        'lisu.validation.bedomning.sjukskrivningar.missing': 'Minst en sjukskrivningsperiod måste anges.',
        'lisu.validation.bedomning.sjukskrivningar.sjukskrivningsgrad.missing': 'Tekniskt fel. Sjukskrivningsgrad saknas.', // Should never happen because GUI should block this combination
        'lisu.validation.bedomning.sjukskrivningar.period1.missing': 'Helt nedsatt: Tekniskt fel. Period saknas.', // Should never happen because GUI should block this combination
        'lisu.validation.bedomning.sjukskrivningar.period1.invalid_format': 'Helt nedsatt: Felaktigt datumformat.',
        'lisu.validation.bedomning.sjukskrivningar.period1.overlap': 'Helt nedsatt: Datumintervall överlappar.',
        'lisu.validation.bedomning.sjukskrivningar.period2.missing': '75% nedsatt: Tekniskt fel. Period saknas.', // Should never happen because GUI should block this combination
        'lisu.validation.bedomning.sjukskrivningar.period2.invalid_format': '75% nedsatt: Felaktigt datumformat.',
        'lisu.validation.bedomning.sjukskrivningar.period2.overlap': '75% nedsatt: Datumintervall överlappar.',
        'lisu.validation.bedomning.sjukskrivningar.period3.missing': '50% nedsatt: Tekniskt fel. Period saknas.', // Should never happen because GUI should block this combination
        'lisu.validation.bedomning.sjukskrivningar.period3.invalid_format': '50% nedsatt: Felaktigt datumformat.',
        'lisu.validation.bedomning.sjukskrivningar.period3.overlap': '50% nedsatt: Datumintervall överlappar.',
        'lisu.validation.bedomning.sjukskrivningar.period4.missing': '25% nedsatt: Period saknas.', // Should never happen because GUI should block this combination
        'lisu.validation.bedomning.sjukskrivningar.period4.invalid_format': '25% nedsatt: Felaktigt datumformat.',
        'lisu.validation.bedomning.sjukskrivningar.period4.overlap': '25% nedsatt: Datumintervall överlappar.',

        'lisu.validation.bedomning.sjukskrivningar.arbetstidsforlaggning.missing': 'Arbetstidsförläggning måste fyllas i om period 75%, 50% eller 25% har valts.',
        'lisu.validation.bedomning.sjukskrivningar.arbetstidsforlaggningmotivering.missing': 'Arbetstidsförläggning måste fyllas i om period 75%, 50% eller 25% har valts.',
        'lisu.validation.bedomning.sjukskrivningar.arbetstidsforlaggningmotivering.invalid_combination': 'Motivering till arbetstidsförläggning måste fyllas i om period 75%, 50% eller 25% har valts.', // Should never happen

        'lisu.validation.bedomning.prognos.missing': 'Prognos måste fyllas i.',
        'lisu.validation.bedomning.prognos.fortydligande.missing': 'Förtydligande måste fyllas i om prognos oklar valts.',
        'lisu.validation.bedomning.prognos.fortydligande.invalid_combination': 'Förtydligande kan bara fyllas i om prognos oklar valts.', // Should never happen because GUI should block this combination

        'lisu.validation.atgarder.missing': 'Åtgärder måste väljas eller Inte aktuellt.',
        'lisu.validation.atgarder.inte_aktuellt_no_combine': 'Inte aktuellt kan inte kombineras med andra val.', // Should never happen because GUI should block this combination
        'lisu.validation.atgarder.invalid_combination': 'Beskrivning för arbetslivsinriktade åtgärder kan inte kombineras med åtgärd på detta sätt.', // Should never happen because GUI should block this combination
        'lisu.validation.atgarder.aktuelltbeskrivning.missing': 'Beskrivning av arbetslivsinriktade åtgärder måste fyllas i.',
        'lisu.validation.atgarder.too-many': 'För många val på åtgärder.', // Should never happen because GUI should block this combination

        'lisu.validation.kontakt.invalid_combination': 'Anledning får inte fyllas i när man svarat nej på kontakt.',

        'lisu.validation.vardenhet.telefonnummer.missing': 'Telefonnummer för vårdenhet måste anges (Fält 15)',
        'lisu.validation.vardenhet.postadress.missing': 'Postadress för vårdenhet måste anges (Fält 15)',
        'lisu.validation.vardenhet.postnummer.missing': 'Postnummer för vårdenhet måste anges (Fält 15)',
        'lisu.validation.vardenhet.postnummer.incorrect-format': 'Postnummer har fel format (Fält 15)',
        'lisu.validation.vardenhet.postort.missing': 'Postort för vårdenhet måste anges (Fält 15)',

/*

         'lisu.validation.diagnosgrund.missing': 'Fältet \'När och var ställdes den/de diagnoser som orsakar nedsatt arbetsförmåga?\' måste fyllas i(Fält 3)',
         'lisu.validation.nybedomningdiagnosgrund.missing': 'Fältet \'Finns det skäl att göra en ny bedömning av diagnosen?\' måste fyllas i(Fält 3)',

         'lisu.validation.nedsattning.overlapping-date-interval': 'Två datuminterval med överlappande datum har angetts. (Fält 8b)',

         'lisu.validation.nedsattning.prognos.choose-one': 'Max ett alternativ kan väljas (Fält 10)',

         'lisu.validation.prognos.gar-ej-att-bedomma.beskrivning.missing': 'Fritextfältet som hör till alternativet Går ej att bedöma, förtydligande under \'Prognos - kommer patienten att få tillbaka sin arbetsförmåga i nuvarande arbete\' måste fyllas i. (Fält 10)',

         'lisu.validation.forandrat-ressatt.choose-one': 'Endast ett alternativ kan anges avseende förändrat ressätt. (Fält 11)',

 */
        // errors
        'lisu.error.could_not_load_cert': '<strong>Intyget gick inte att läsa in.</strong><br>Prova att ladda om sidan. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand Nationell kundservice på 0771-251010.',
        'lisu.error.could_not_load_cert_not_auth': '<strong>Kunde inte hämta intyget eftersom du saknar behörighet.</strong>',
        'lisu.error.could_not_load_cert_qa': '<strong>Frågor och svar för detta intyg gick inte att läsa in. Prova att ladda om sidan. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand Nationell kundservice på 0771-251010.</strong>',
        // fragaSvar errors
        'lisu.error.external_system_problem': '<strong>Meddelandet har inte skickats till Försäkringskassan då Webcert saknar kontakt med Försäkringskassans datasystem.</strong><br>Prova att skicka om meddelandet. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand Nationell kundservice på 0771-251010.',
        'lisu.error.internal_problem': '<strong>Ett tekniskt problem inträffade.</strong><br>Försök igen och kontakta supporten om problemet kvarstår.',
        'lisu.error.invalid_state': '<strong>Funktionen är inte giltig.</strong><br>Förmodligen har en annan användare ändrat informationen medan du arbetat på samma post. Ladda om informationen och försök igen',
        'lisu.error.unknown_internal_problem': '<strong>Ett tekniskt problem inträffade.</strong><br>Försök igen och kontakta supporten om problemet kvarstår.',
        'lisu.error.authorization_problem': '<strong>Du har inte behörighet att utföra funktionen, kontakta supporten för mer information.</strong>',
        'lisu.error.data_not_found': '<strong>Intyget kunde inte hittas i intygstjänsten.</strong><br>Intyget är borttaget eller så saknas behörighet.'

    },
    'en': {
        'lisu.label.pagetitle': 'Show Certificate'
    }
};
