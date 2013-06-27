'use strict';
var fk7263Messages = {
    "sv" : {
        "recipient.label.pagetitle" : "Skicka Intyget - välj mottagare",
        "recipient.label.recipientlisttitle" : "Myndigheter och försäkringsbolag",
        "recipient.label.recipientlisttitle.others" : "Myndigheter och försäkringsbolag",
        "recipient.label.downloadlink" : "Ladda ner / Skriv ut och skicka per post istället",

        "confirm.label.pagetitle" : "Skicka Intyget - Kontrollera och bekräfta",
        "confirm.label.certrowtitle" : "Intyg",
        "confirm.label.recipienttitle" : "Mottagare",
        "confirm.label.verifytext" : "Kontrollera att uppgifterna ovan stämmer och tryck \"Skicka\"",
        "confirm.message.alreadysent" : "<strong>Observera!</strong> Du har redan skickat detta intyg till denna mottagare.",

        "sent.label.pagetitle" : "Intyget skickades",
        "sent.label.result" : "Intyget har skickats till:",
        "sent.button.backtocertificate" : "Tillbaka till Intyget",
        "view.label.latestevent" : "Intygets senaste händelse",
        "view.label.latestevent.noevents" : "Inga händelser",
        "view.label.latestevent.showall" : "Visa alla händelser",
        "history.label.pagetitle" : "Intygets alla händelser",
        "fk7263.status.sent" : "Skickat till",
        "fk7263.target.fk" : "Försäkringskassan",

        "view.label.pagetitle" : "Granska och skicka",
        "view.label.pagedescription" : "Här kan du titta på ditt intyg och skicka det till Försäkringskassan. Om du upptäcker några felaktigheter så hör av dig enheten som utfärdade ditt intyg. Du kan även öppna intyget som en PDF och skriva ut och spara.",
        "view.label.nedsattning" : "Läkare bedömer att arbetsförmåga är (fält 8b)",
        "view.label.patientname" : "Patientens namn",
        "view.label.issued" : "Utfärdat",
        "view.label.civicnumber" : "Personnummer",
        "view.label.issuer" : "Utfärdare",
        "view.label.period" : "Period",
        "view.label.unit" : "Enhet",
        "view.label.smittskydd" : "Avstängning enligt smittskyddslagen på grund av smitta (fält 1)",
        "view.label.yes" : "Ja",
        "view.label.no" : "Nej",
        "view.label.partialyes" : "Ja, delvis",
        "view.label.diagnosis" : "Diagnos/diagnoser för sjukdom som orsakar nedsatt arbetsförmåga (fält 2)",
        "view.label.diagnosiscode" : "Diagnoskod",
        "view.label.progressofdesease" : "Aktuellt sjukdomförlopp (fält 3)",
        "view.label.disabilities" : "Funktionsnedsättning - observationer, undersökningsfynd och utredningsresultat (fält 4)",
        "view.label.basedon" : "Intyget baseras på (fält 4b)",
        "view.label.comment" : "Kommentar:",
        "view.label.limitation" : "Aktivitetsbegränsning relaterat till diagnos och funktionsnedsättning (fält 5)",
        "view.label.recommendations" : "Rekommendationer (fält 6a)",
        "view.label.plannedtreatment" : "Planerad eller pågående behandling eller åtgärd (fält 6b)",
        "view.label.plannedtreatment.healthcare" : "Inom sjukvården:",
        "view.label.plannedtreatment.other" : "Annan åtgärd:",
        "view.label.workrehab" : "Är arbetslivsinriktad rehabilitering aktuell? (fält 7)",
        "view.label.unjudgeable" : "Går inte att bedöma",
        "view.label.patientworkcapacity" : "Patientens arbetsförmåga bedöms i förhållande till (fält 8a)",
        "view.label.patientworkcapacity.currentwork" : "Nuvarande arbete: ",
        "view.label.patientworkcapacity.unemployed" : "Arbetslöshet - att utföra arbete på den reguljära arbetsmarknaden",
        "view.label.patientworkcapacity.parentalleave" : "föräldraledighet med föräldrapenning - att vårda sitt barn",
        "view.label.patientworkcapacityjudgement" : "Patientens arbetsförmåga bedöms nedsatt längre tid än den som det Försäkringsmedicinska beslutssötdet anger, därför att:  (fält 9)",
        "view.label.prognosis" : "Prognos - kommer patienten att få tillbaka sin arbetsförmåga i nuvarande arbete? (Gäller inte arbetslösa) (fält 10)",
        "view.label.othertransport" : "Kan resor till och från arbetet med annat färdsätt än normalt göra det möjligt för patienten att återgå i arbete? (fält 11)",
        "view.label.fkcontact" : "Kontakt önskas med Försäkringskassan (fält 12)",
        "view.label.workcodes" : "Förskrivarkod och arbetsplatskod (fält 17)",
        "view.label.recommendations.contact.jobcenter" : "Kontakt med Arbetsförmedlingen",
        "view.label.recommendations.contact.healthdepartment" : "Kontakt med företagshälsovården",

        "view.label.confirmedby" : "Ovanstående uppgifter och bedömningar har bekräftats av:",
        "view.label.date" : "Datum (fält 14)",
        "view.label.contactinfo" : "Namnförtydligande, mottagningens adress och telefon (fält 15)",

        "view.message.certificateloading" : "Hämtar intyg...",
        "recipients.label.pagetitle" : "Skicka intyg - välj mottagare",
        "button.sendtofk" : "Skicka",
        "button.downloadprint" : "Ladda ner / Skriv ut",
        "button.send" : "Skicka",
        "button.cancel" : "Avbryt",
        "button.goback" : "Tillbaka",

        "nedsattningsgrad.helt_nedsatt" : "Helt nedsatt",
        "nedsattningsgrad.nedsatt_med_3_4" : "Nedsatt med 3/4",
        "nedsattningsgrad.nedsatt_med_1_2" : "Nedsatt med hälften",
        "nedsattningsgrad.nedsatt_med_1_4" : "Nedsatt med 1/4",
        "vardkontakt.min_undersokning_av_patienten" : "Undersökning av patienten den %s",
        "vardkontakt.min_telefonkontakt_med_patienten" : "Telefonkontakt med patienten den %s",
        "referens.journaluppgifter" : "Journaluppgifter, den %s",
        "referens.annat" : "Annat, den %s",

        "error.generic" : "Kunde inte visa intyget",
        "error.couldnotsend" : "Kunde inte skicka intyget till Försäkringskassan. Vänligen försök igen senare.",
        "error.certnotfound" : "Intyget kunde inte laddas."
    },
    "en" : {
        "view.label.pagetitle" : "Show Certificate"
    }
};
