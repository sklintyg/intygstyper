'use strict';
var fk7263Messages = {
    "sv" : {
        "error.generic" : "Kunde inte visa intyget",
        "info.loadingcertificate" : "Hämtar intyget..",
        "info.loading.existing.qa" : "Hämtar tidigare frågor och svar...",
        "view.message.certificateloading" : "Hämtar intyg...",

        // FK 7263
        "view.label.pagetitle.fk7263" : "Läkarintyg FK 7263",

        //Labels
        "view.label.field" : "Fält",
        "view.label.blank" : "- ej ifyllt",
        "view.label.nedsattning" : "Jag bedömer att arbetsförmåga är",
        "view.label.patientname" : "Patientens namn",
        "view.label.issued" : "Utfärdat",
        "view.label.civicnumber" : "Personnummer",
        "view.label.issuer" : "Utfärdare",
        "view.label.period" : "Period",
        "view.label.unit" : "Enhet",
        "view.label.smittskydd" : "Avstängning enligt smittskyddslagen på grund av smitta",
        "view.label.yes" : "Ja",
        "view.label.no" : "Nej",
        "view.label.partialyes" : "Ja, delvis",
        "view.label.diagnosis" : "Diagnos/diagnoser för sjukdom som orsakar nedsatt arbetsförmåga",
        "view.label.diagnosiscode" : "Diagnoskod enligt ICD-10 (huvuddiagnos): ",
        "view.label.progressofdisease" : "Aktuellt sjukdomförlopp",
        "view.label.disabilities" : "Funktionsnedsättning - observationer, undersökningsfynd och utredningsresultat",
        "view.label.basedon" : "Intyget baseras på",
        "view.label.comment" : "Kommentar:",
        "view.label.work" : "Arbete",
        "view.label.limitation" : "Aktivitetsbegränsning relaterat till diagnos och funktionsnedsättning",
        "view.label.recommendations" : "Rekommendationer",
        "view.label.plannedtreatment" : "Planerad eller pågående behandling eller åtgärd",
        "view.label.plannedtreatment.healthcare" : "Inom sjukvården:",
        "view.label.plannedtreatment.other" : "Annan åtgärd:",
        "view.label.workrehab" : "Är arbetslivsinriktad rehabilitering aktuell?",
        "view.label.unjudgeable" : "Går inte att bedöma",
        "view.label.patientworkcapacity" : "Patientens arbetsförmåga bedöms i förhållande till",
        "view.label.patientworkcapacity.currentwork" : "Nuvarande arbete: ",
        "view.label.patientworkcapacity.unemployed" : "Arbetslöshet - att utföra sådant arbete som är normalt förekommande på arbetsmarknaden",
        "view.label.patientworkcapacity.parentalleave" : "Föräldraledighet med föräldrapenning - att vårda sitt barn",
        "view.label.patientworkcapacityjudgement" : "Patientens arbetsförmåga bedöms nedsatt längre tid än den som det försäkringsmedicinska beslutsstödet anger, därför att:",
        "view.label.prognosis" : "Prognos - kommer patienten att få tillbaka sin arbetsförmåga i nuvarande arbete? (Gäller inte arbetslösa)",
        "view.label.othertransport" : "Kan resor till och från arbetet med annat färdsätt än normalt göra det möjligt för patienten att återgå i arbete?",
        "view.label.fkcontact" : "Kontakt önskas med Försäkringskassan",
        "view.label.workcodes" : "Förskrivarkod och arbetsplatskod",
        "view.label.recommendations.contact.jobcenter" : "Kontakt med Arbetsförmedlingen",
        "view.label.recommendations.contact.healthdepartment" : "Kontakt med företagshälsovården",
        "view.label.recommendations.contact.other" : "Övrigt: ",
        "view.label.reference.to.field13" : "Se under Övriga upplysningar och förtydliganden",
        "view.label.otherinformation" : "Övriga upplysningar och förtydliganden",

        "view.label.confirmedby" : "Ovanstående uppgifter och bedömningar har bekräftas",
        "view.label.date" : "Datum",
        "view.label.contactinfo" : "Namnförtydligande, mottagningens adress och telefon",

        "view.label.treatments" : "Åtgärder",
        "edit.label.recommendations" : "Rekommendationer till Försäkringskassan",
        "edit.label.fkcontact" : "Kontakt",
        "view.label.unitaddress" : "Vårdenhetens adress",

        "vardkontakt.undersokning" : "Min undersökning av patienten den ",
        "vardkontakt.telefonkontakt" : "Min telefonkontakt med patienten den ",
        "referens.journal" : "Journaluppgifter, den ",
        "referens.annat" : "Annat, den ",

        "nedsattningsgrad.helt_nedsatt" : "Helt nedsatt",
        "nedsattningsgrad.nedsatt_med_3_4" : "Nedsatt med 3/4",
        "nedsattningsgrad.nedsatt_med_1_2" : "Nedsatt med hälften",
        "nedsattningsgrad.nedsatt_med_1_4" : "Nedsatt med 1/4",
        
        //Help texts
        "fk7263.helptext.intyg-avser" : "Ange vad uppgifterna i intyget baseras på. Flera alternativ kan väljas.",
        "fk7263.helptext.intyg-avser.annat" : "Detta alternativ kan bl.a. användas för annan proffesions bedömning t.ex. kuratorsanteckningar, sjukgymnastanteckning eller teamkonferens.",

        "fk7263.helptext.diagnos" : "Ange vilken eller vilka sjukdomar som medför nedsättning av arbetsförmågan.",
        "fk7263.helptext.diagnos.ytterligare" : "Observera att det som skrivs in här kommer vid granskning och utskrift av intyget att flyttas till ett fritextfält (Diagnos) tillsammans med förtydligandet av diagnoser nedan.",
        "fk7263.helptext.diagnos.fortydligande" : "I intygsblanketten kommer allt som inte skrivs i huvuddiagnosens kodruta att hamna i fritext (fältet Diagnos). I fritextfältet kan du ytterligare kommentera eller förtydliga de angivna diagnoserna eller åtgärderna.",
        "fk7263.helptext.diagnos.samsjuklighet" : "Om du anser att Försäkringskassan särskilt bör uppmärksammas på att det rör sig om samsjuklighet, dvs. om patienten har flera sjukdomar eller symtom som förstärker och förvärrar patientens tillstånd, kan du ange detta med kryss. Om du sätter ett kryss kommer texten ”Samsjuklighet föreligger” att synas i fritextfältet för diagnos vid granskning och utskrift av intyget.",

        "fk7263.helptext.aktuellt-sjukdomsforlopp" : "Beskriv kortfattat sjukdomens utveckling, situation vid undersökningstillfället och förhållanden som påverkar sjukdomsutvecklingen.",
        
        
        // errors
        "error.could_not_load_cert" : "<strong>Ett tekniskt problem inträffade.</strong>Kunde inte hämta intyget.",
        "error.could_not_load_cert_not_auth" : "<strong>Kunde inte hämta intyget eftersom du saknar behörighet.",
        "error.could_not_load_cert_qa" : "<strong>Kunde inte hämta frågor och svar för intyget.",
        // fragaSvar errors
        "error.external_system_problem" : "<strong>Försäkringskassans system kan just nu inte ta emot informationen. Försök igen</strong>",
        "error.internal_problem" : "<strong>Ett tekniskt problem inträffade.</strong><br>Försök igen och kontakta supporten om problemet kvarstår.",
        "error.invalid_state" : "<strong>Funktionen är inte giltig</strong>Förmodligen har en annan användare ändrat informationen medan du arbetat på samma post. Ladda om informationen och försök igen",
        "error.unknown_internal_problem" : "<strong>Ett tekniskt problem inträffade</strong><br> Försök igen och kontakta supporten om problemet kvarstår.",
        "error.authorization_problem" : "<strong>Du har inte behörighet att utföra funktionen, kontakta supporten för mer information.</strong>",
        "error.data_not_found" : "<strong>Intyget kunde inte hittas i intygstjänsten.</strong><br> Intyget kunde inte hämtas.",
        "fragasvar.answer.is.sent" : "<strong>Svaret har skickats till Försäkringskassan</strong><br> Frågan är nu markerad som hanterad och visas nu under 'Hanterade frågor' längre ner på sidan.",
        "fragasvar.marked.as.ohanterad" : "<strong>Frågan är nu markerad som ohanterad</strong><br> Den visas nu under 'Ohanterade frågor' längre upp på sidan.",
        "fragasvar.marked.as.hanterad" : "<strong>Frågan är nu markerad som hanterad</strong><br> Den visas nu under 'Hanterade frågor' längre ner på sidan."

    },
    "en" : {
        "view.label.pagetitle" : "Show Certificate"
    }
};
