define([], function() {
    'use strict';

    return {
        'sv': {
            'error.generic': 'Kunde inte visa intyget',
            'info.loadingcertificate': 'Hämtar intyget..',
            'info.loading.existing.qa': 'Hämtar tidigare frågor och svar...',
            'fk7263.message.certificateloading': 'Hämtar intyg...',

            // FK 7263
            'fk7263.label.pagetitle.fk7263': 'Läkarintyg FK 7263',

            //Labels
            'fk7263.label.field': 'Fält',
            'fk7263.label.blank': '- ej ifyllt',
            'fk7263.label.nedsattning': 'Jag bedömer att arbetsförmåga är',
            'fk7263.label.patientname': 'Patientens namn',
            'fk7263.label.issued': 'Utfärdat',
            'fk7263.label.civicnumber': 'Personnummer',
            'fk7263.label.issuer': 'Utfärdare',
            'fk7263.label.period': 'Period',
            'fk7263.label.unit': 'Enhet',
            'fk7263.label.smittskydd': 'Avstängning enligt smittskyddslagen på grund av smitta',
            'fk7263.label.yes': 'Ja',
            'fk7263.label.no': 'Nej',
            'fk7263.label.partialyes': 'Ja, delvis',
            'fk7263.label.diagnosis': 'Diagnos/diagnoser för sjukdom som orsakar nedsatt arbetsförmåga',
            'fk7263.label.diagnosiscode': 'Diagnoskod enligt ICD-10 (huvuddiagnos): ',
            'fk7263.label.progressofdisease': 'Aktuellt sjukdomförlopp',
            'fk7263.label.disabilities': 'Funktionsnedsättning - observationer, undersökningsfynd och utredningsresultat',
            'fk7263.label.basedon': 'Intyget baseras på',
            'fk7263.label.comment': 'Kommentar:',
            'fk7263.label.work': 'Arbete',
            'fk7263.label.limitation': 'Aktivitetsbegränsning relaterat till diagnos och funktionsnedsättning',
            'fk7263.label.recommendations': 'Rekommendationer',
            'fk7263.label.plannedtreatment': 'Planerad eller pågående behandling eller åtgärd',
            'fk7263.label.plannedtreatment.healthcare': 'Inom sjukvården:',
            'fk7263.label.plannedtreatment.other': 'Annan åtgärd:',
            'fk7263.label.workrehab': 'Är arbetslivsinriktad rehabilitering aktuell?',
            'fk7263.label.unjudgeable': 'Går inte att bedöma',
            'fk7263.label.patientworkcapacity': 'Patientens arbetsförmåga bedöms i förhållande till',
            'fk7263.label.patientworkcapacity.currentwork': 'Nuvarande arbete: ',
            'fk7263.label.patientworkcapacity.unemployed': 'Arbetslöshet - att utföra sådant arbete som är normalt förekommande på arbetsmarknaden',
            'fk7263.label.patientworkcapacity.parentalleave': 'Föräldraledighet med föräldrapenning - att vårda sitt barn',
            'fk7263.label.patientworkcapacityjudgement': 'Patientens arbetsförmåga bedöms nedsatt längre tid än den som det försäkringsmedicinska beslutsstödet anger, därför att:',
            'fk7263.label.prognosis': 'Prognos - kommer patienten att få tillbaka sin arbetsförmåga i nuvarande arbete? (Gäller inte arbetslösa)',
            'fk7263.label.othertransport': 'Kan resor till och från arbetet med annat färdsätt än normalt göra det möjligt för patienten att återgå i arbete?',
            'fk7263.label.fkcontact': 'Kontakt önskas med Försäkringskassan',
            'fk7263.label.workcodes': 'Förskrivarkod och arbetsplatskod',
            'fk7263.label.recommendations.contact.jobcenter': 'Kontakt med Arbetsförmedlingen',
            'fk7263.label.recommendations.contact.healthdepartment': 'Kontakt med företagshälsovården',
            'fk7263.label.recommendations.contact.other': 'Övrigt: ',
            'fk7263.label.reference.to.field13': 'Se under Övriga upplysningar och förtydliganden',
            'fk7263.label.otherinformation': 'Övriga upplysningar och förtydliganden',

            'fk7263.label.confirmedby': 'Ovanstående uppgifter och bedömningar har bekräftas',
            'fk7263.label.date': 'Datum',
            'fk7263.label.contactinfo': 'Namnförtydligande, mottagningens adress och telefon',

            'fk7263.label.treatments': 'Åtgärder',
            'edit.label.recommendations': 'Rekommendationer till Försäkringskassan',
            'edit.label.fkcontact': 'Kontakt',
            'fk7263.label.unitaddress': 'Vårdenhetens adress',

            'vardkontakt.undersokning': 'Min undersökning av patienten den ',
            'vardkontakt.telefonkontakt': 'Min telefonkontakt med patienten den ',
            'referens.journal': 'Journaluppgifter, den ',
            'referens.annat': 'Annat, den ',

            'nedsattningsgrad.helt_nedsatt': 'Helt nedsatt',
            'nedsattningsgrad.nedsatt_med_3_4': 'Nedsatt med 3/4',
            'nedsattningsgrad.nedsatt_med_1_2': 'Nedsatt med hälften',
            'nedsattningsgrad.nedsatt_med_1_4': 'Nedsatt med 1/4',

            //Help texts
            'fk7263.helptext.intyg-baserat-pa': 'Ange vad uppgifterna i intyget baseras på. Flera alternativ kan väljas.',
            'fk7263.helptext.intyg-baserat-pa.annat': 'Detta alternativ kan bl.a. användas för annan proffesions bedömning t.ex. kuratorsanteckningar, sjukgymnastanteckning eller teamkonferens.',

            'fk7263.helptext.diagnos': 'Ange vilken eller vilka sjukdomar som medför nedsättning av arbetsförmågan.',
            'fk7263.helptext.diagnos.ytterligare': 'Observera att det som skrivs in här kommer vid granskning och utskrift av intyget att flyttas till ett fritextfält (Diagnos) tillsammans med förtydligandet av diagnoser nedan.',
            'fk7263.helptext.diagnos.fortydligande': 'I intygsblanketten kommer allt som inte skrivs i huvuddiagnosens kodruta att hamna i fritext (fältet Diagnos). I fritextfältet kan du ytterligare kommentera eller förtydliga de angivna diagnoserna eller åtgärderna.',
            'fk7263.helptext.diagnos.samsjuklighet': 'Om du anser att Försäkringskassan särskilt bör uppmärksammas på att det rör sig om samsjuklighet, dvs. om patienten har flera sjukdomar eller symtom som förstärker och förvärrar patientens tillstånd, kan du ange detta med kryss. Om du sätter ett kryss kommer texten "Samsjuklighet föreligger" att synas i fritextfältet för diagnos vid granskning och utskrift av intyget.',

            'fk7263.helptext.aktuellt-sjukdomsforlopp': 'Beskriv kortfattat sjukdomens utveckling, situation vid undersökningstillfället och förhållanden som påverkar sjukdomsutvecklingen.',

            'fk7263.helptext.funktionsnedsattning': 'Beskriv den angivna sjukdomens påverkan på patientens fysiska respektive psykiska funkitoner och hur denna påverkan observerats vid din och andra proffesioners undersökning och medicinska utredning.',

            'fk7263.helptext.aktivitetsbegransning': 'Ge en tydlig beskrivning av hur sjukdomen begränsar patientens förmåga, liksom av hur omfattande konsekvenserna är. Denna information är den mest centrala för bedömningen av rätten till sjukpenning. Använd enkel svenska som är begriplig för en person utan medicinsk utbildning.',

            'fk7263.helptext.sysselsattning': 'Ange vad du bedömt nedsättningen av arbetsförmågan mot patientens nuvarande arbete, arbetslöshet eller föräldraledighet. Läkarintyget måste alltid innehålla information om detta. Om patienten t.ex. både arbetar och är föräldraledig, kan du förtydliga detta under Övriga upplysningar.',
            'fk7263.helptext.sysselsattning.nuvarande': 'Om patienten har ett arbete, ska nedsättningen av arbetsförmåga relateras till de arbetsuppgifter patienten har.',
            'fk7263.helptext.sysselsattning.arbetsloshet': 'Är patienten arbetslös ska bedömningen relateras till förmågan att klara ett arbete på den reguljära arbetsmarknaden.',
            'fk7263.helptext.sysselsattning.foraldrarledighet': 'Är patienten föräldraledig ska bedömningen relateras till förmågan att ta hand om barnet.',

            'fk7263.helptext.arbetsformaga': 'Ange hur mycket patientens arbetsförmåga bedöms vara nedsatt och för hur lång tid. För att patienten ska få rätt till sjukpenning ska aktivitetsbegränsningen påverka arbetsförmågan med minst 25 %. Gradvis ökad arbetsförmåga kan anges på samma intyg. ',
            'fk7263.helptext.arbetsformaga.bedoms-langre': 'Om sjukskrivningen omfattar längre tid än den som rekommenderas i det försäkringsmedicinska beslutsstödets specifika rekommendationer, motivera varför. Detta gäller också om patienten vid intygsskrivandet redan varit sjukskriven längre tid än beslutsstödets rekommendationer.',
            'fk7263.helptext.arbetsformaga.prognos': 'Ange hur du bedömer patientens möjligheter att återgå till sina nuvarande arbetsuppgifter i samma omfattning som före sjukskrivningen. Prognosen avser inte möjligheter till omplacering eller förändrade arbetsuppgifter.',

            'fk7263.helptext.atgarder': 'Ange om patienten är under behandling eller väntar på åtgärder inom vården (t.ex. operationer eller behandlingar) eller någon annanstans. Angivna åtgärder ska vara väsentliga för att patienten ska återfå arbetsförmågan.',

            'fk7263.helptext.rekommendationer': 'Här kan du ge Försäkringskassan underlag för bedömning av patientens behov av samordade åtgärder för rehabilitering. Dina rekommendationer riktar sig också till patienten själv och till patientens arbetsgivare. Försäkringskassan har ett samordningsansvar för rehabiliteringen. Försäkringskassan kan bland annat ta initiativ till möten med patienten och olika aktörer som kan bidra till att patienten snabbare kan återgå i arbete.',
            'fk7263.helptext.rekommendationer.resor': 'Ange om resor till och från arbetet med annat färdsätt, till exempel taxi eller automatväxlad bil, kan göra att patienten kan arbeta trots sjukdomen. Patienten kan få ersättning för annat färdsätt i tre månader, och i undantagsfall längre.',
            'fk7263.helptext.rekommendationer.arbetsformedlingen': 'Kontakt med Arbetsförmedlingen rekommenderas om du bedömer att patienten behöver hjälp via Arbetsförmedlingen för att han eller hon ska kunna återgå i arbete.',
            'fk7263.helptext.rekommendationer.foretagshalsovarden': 'Patientens arbetsgivare kan ha ett avtal om företagshälsovård. Försäkringskassan har ansvar för samordning av aktiviteter för patientens rehabilitering.',
            'fk7263.helptext.rekommendationer.arbetslivsinriktad-rehabilitering': 'Ange om du bedömer att patienten skulle ha förutsättningar att återvinna arbetsförmågan snabbare med arbetslivsinriktad rehabilitering.',

            'fk7263.helptext.kontakt': 'Ange om du vill att Försäkringskassans handläggare tar kontakt med dig för att diskutera patientens sjukskrivning. Observera att denna ruta i första hand ska användas om intyget ska skrivas ut på papper. Överförs intyget elektroniskt används hellre funktionen för elektronisk ärendekommunikation (efter det att intyget skickats in).',

            'fk7263.helptext.ovrigt': 'Ange här sådan ytterligare information som du bedömer att Försäkringskassan skulle ha nytta av. Observera att vid granskning och utskrift av intyget kommer text som skrivits in i vissa av fritextfälten på andra ställen i intyget också att flyttas hit. ',

            'fk7263.helptext.samtycke': 'Läkarintyget får bara skickas till Försäkringskassan om patienten givit sitt samtycke till det. Genom att kryssa i denna ruta intygar du att du fått patientens samtycke till att skicka intyget elektroniskt till Försäkringskassan. ',

            //Validation messages
            'fk7263.validation.diagnos.missing': 'ICD-10 kod saknas på huvuddiagnosen. (Fält 2)',

            'fk7263.validation.intyg-baserat-pa.missing': 'Välj minst ett alternativ som uppgifterna i intyget baseras på. (Fält 4b)',
            'fk7263.validation.intyg-baserat-pa.annat.beskrivning.missing': 'Fritextfältet som hör till alternativet Annat under \'Uppgifterna i intyget baseras på\' måste fyllas i. (Fält 4)',

            'fk7263.validation.funktionsnedsattning.missing': 'Fältet med funktionsnedsättning måste fyllas i. (Fält 4)',

            'fk7263.validation.sysselsattning.missing': '	Aktuella arbetsuppgifter som hör till alternativet Nuvarande arbete under \'Arbete\' måste fyllas i. (Fält 8a)',

            'fk7263.validation.arbetsformaga.choose-at-least-one': 'Välj minst ett alternativ för arbetsförmåga. (Fält 8b)',
            'fk7263.validation.arbetsformaga.incorrect-date-interval': 'Ett datuminterval har angetts på ett felaktigt sätt (startdatum efter slutdatum). (Fält 8b)',
            'fk7263.validation.arbetsformaga.overlapping-date-interval': 'Två datuminterval med överlappande datum har angetts. (Fält 8b)',

            'fk7263.validation.prognos.gar-ej-att-bedomma.beskrivning.missing': 'Fritextfältet som hör till alternativet Går ej att bedöma, förtydligande under \'Prognos - kommer patienten att få tillbaka sin arbetsförmåga i nuvarande arbete\' måste fyllas i. (Fält 10)',

            // errors
            'error.could_not_load_cert': '<strong>Ett tekniskt problem inträffade.</strong>Kunde inte hämta intyget.',
            'error.could_not_load_cert_not_auth': '<strong>Kunde inte hämta intyget eftersom du saknar behörighet.',
            'error.could_not_load_cert_qa': '<strong>Kunde inte hämta frågor och svar för intyget.',
            // fragaSvar errors
            'error.external_system_problem': '<strong>Försäkringskassans system kan just nu inte ta emot informationen. Försök igen</strong>',
            'error.internal_problem': '<strong>Ett tekniskt problem inträffade.</strong><br>Försök igen och kontakta supporten om problemet kvarstår.',
            'error.invalid_state': '<strong>Funktionen är inte giltig</strong>Förmodligen har en annan användare ändrat informationen medan du arbetat på samma post. Ladda om informationen och försök igen',
            'error.unknown_internal_problem': '<strong>Ett tekniskt problem inträffade</strong><br> Försök igen och kontakta supporten om problemet kvarstår.',
            'error.authorization_problem': '<strong>Du har inte behörighet att utföra funktionen, kontakta supporten för mer information.</strong>',
            'error.data_not_found': '<strong>Intyget kunde inte hittas i intygstjänsten.</strong><br>Intyget är borttaget eller så saknas behörighet.',
            'fragasvar.answer.is.sent': '<strong>Svaret har skickats till Försäkringskassan</strong><br> Frågan är nu markerad som hanterad och visas nu under \'Hanterade frågor\' längre ner på sidan.',
            'fragasvar.marked.as.ohanterad': '<strong>Frågan är nu markerad som ohanterad</strong><br> Den visas nu under \'Ohanterade frågor\' längre upp på sidan.',
            'fragasvar.marked.as.hanterad': '<strong>Frågan är nu markerad som hanterad</strong><br> Den visas nu under \'Hanterade frågor\' längre ner på sidan.'

        },
        'en': {
            'fk7263.label.pagetitle': 'Show Certificate'
        }
    };
});
