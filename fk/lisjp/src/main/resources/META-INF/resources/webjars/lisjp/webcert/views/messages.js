/* jshint maxlen: false */

angular.module('lisjp').constant('lisjp.messages', {
    'sv': {
        //Validation messages
        'lisjp.validation.sysselsattning.ampolitisktprogram.invalid_combination': 'Beskrivning för arbetsmarknadspolitiskt program måste bara fyllas i om arbetsmarknadspolitiskt program valts.', // Should never happen because GUI should block this combination
        'lisjp.validation.sysselsattning.nuvarandearbete.invalid_combination': 'Beskrivningen får inte fyllas i när inte nuvarande arbete valts.', // Should never happen because GUI should block this combination
        'lisjp.validation.sysselsattning.too-many': 'sysselsättning.toomany',  // Should never happen because GUI should block this combination
        'lisjp.validation.bedomning.fmb.empty' : 'Beskriv varför arbetsförmågan bedöms vara nedsatt längre tid än den som det försäkringsmedicinska beslutstödet anger kan inte fyllas i med endast blanksteg',
        'lisjp.validation.bedomning.prognos.dagartillarbete.invalid_combination': 'Dagar till arbete kan bara fyllas i om prognosen "kan återgå helt i nuvarande sysselsättning efter x antal dagar valts"', // Should never happen because GUI should block this combination
        'lisjp.validation.bedomning.sjukskrivningar.missing': 'Minst en sjukskrivningsperiod måste anges.',
        'lisjp.validation.bedomning.sjukskrivningar.arbetstidsforlaggning.missing': 'Arbetstidsförläggning måste fyllas i om period 75%, 50% eller 25% har valts.',
        'lisjp.validation.bedomning.sjukskrivningar.arbetstidsforlaggningmotivering.missing': 'Arbetstidsförläggning måste fyllas i om period 75%, 50% eller 25% har valts.',
        'lisjp.validation.bedomning.sjukskrivningar.arbetstidsforlaggningmotivering.incorrect': '',
        'lisjp.validation.bedomning.sjukskrivningar.arbetstidsforlaggningmotivering.invalid_combination': 'Motivering till arbetstidsförläggning måste fyllas i om period 75%, 50% eller 25% har valts.', // Should never happen
        'lisjp.validation.bedomning.sjukskrivningar.sjukskrivningsgrad.missing': 'Tekniskt fel. Sjukskrivningsgrad saknas.', // Should never happen because GUI should block this combination
        'lisjp.validation.bedomning.sjukskrivningar.periodhelt_nedsatt.missing': 'Helt nedsatt: Tekniskt fel. Period saknas.', // Should never happen because GUI should block this combination
        'lisjp.validation.bedomning.sjukskrivningar.periodhelt_nedsatt.invalid_format': 'Helt nedsatt: Felaktigt datumformat.',
        'lisjp.validation.bedomning.sjukskrivningar.periodhelt_nedsatt.overlap': 'Helt nedsatt: Datumintervall överlappar.',
        'lisjp.validation.bedomning.sjukskrivningar.periodtre_fjardedel.missing': '75% nedsatt: Tekniskt fel. Period saknas.', // Should never happen because GUI should block this combination
        'lisjp.validation.bedomning.sjukskrivningar.periodtre_fjardedel.invalid_format': '75% nedsatt: Felaktigt datumformat.',
        'lisjp.validation.bedomning.sjukskrivningar.periodtre_fjardedel.overlap': '75% nedsatt: Datumintervall överlappar.',
        'lisjp.validation.bedomning.sjukskrivningar.periodhalften.missing': '50% nedsatt: Tekniskt fel. Period saknas.', // Should never happen because GUI should block this combination
        'lisjp.validation.bedomning.sjukskrivningar.periodhalften.invalid_format': '50% nedsatt: Felaktigt datumformat.',
        'lisjp.validation.bedomning.sjukskrivningar.periodhalften.overlap': '50% nedsatt: Datumintervall överlappar.',
        'lisjp.validation.bedomning.sjukskrivningar.perioden_fjardedel.missing': '25% nedsatt: Period saknas.', // Should never happen because GUI should block this combination
        'lisjp.validation.bedomning.sjukskrivningar.perioden_fjardedel.invalid_format': '25% nedsatt: Felaktigt datumformat.',
        'lisjp.validation.bedomning.sjukskrivningar.perioden_fjardedel.overlap': '25% nedsatt: Datumintervall överlappar.',
        'lisjp.validation.bedomning.sjukskrivningar.tidigtstartdatum': 'Det startdatum du angett är mer än <strong>en vecka före dagens datum</strong>. Du bör kontrollera att tidsperioderna är korrekta.',
        'lisjp.validation.atgarder.missing': 'Åtgärder måste väljas eller Inte aktuellt.',
        'lisjp.validation.atgarder.inte_aktuellt_no_combine': 'Inte aktuellt kan inte kombineras med andra val.', // Should never happen because GUI should block this combination
        'lisjp.validation.atgarder.invalid_combination': 'Beskrivning för arbetslivsinriktade åtgärder kan inte kombineras med åtgärd på detta sätt.', // Should never happen because GUI should block this combination
        'lisjp.validation.atgarder.too-many': 'För många val på åtgärder.', // Should never happen because GUI should block this combination

        'lisjp.label.sjukskrivningar.tidigtstartdatum.motivering': 'Ange orsak för att starta perioden mer än 7 dagar bakåt i tiden.',
        'lisjp.label.sjukskrivningar.tidigtstartdatum.motivering.help': 'Observera att detta inte är en fråga från Försäkringskassan. Information om varför sjukskrivningen startar mer än en vecka före dagens datum kan vara till hjälp för Försäkringskassan i deras handläggning.',
        'lisjp.label.sjukskrivningar.tidigtstartdatum.motivering.info': 'Informationen överförs till fältet \'{0}\' vid signering.'
    },
    'en': {
        'lisjp.label.pagetitle': 'Show Certificate'
    }
});
