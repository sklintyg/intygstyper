/* jshint maxlen: false, unused: false */
var sjukersattningMessages = {
    'sv': {
        'sjukpenning-utokad.error.generic': 'Kunde inte visa intyget',
        'sjukpenning-utokad.info.loadingcertificate': 'Hämtar intyget..',
        'sjukpenning-utokad.info.loading.existing.qa': 'Hämtar tidigare frågor och svar...',
        'sjukpenning-utokad.message.certificateloading': 'Hämtar intyg...',

        'sjukpenning-utokad.label.send': 'Skicka intyg till Försäkringskassan',
        'sjukpenning-utokad.label.send.body': 'Upplys patienten om att även göra en ansökan om sjukpenning-utokad hos Försäkringskassan.',
        'sjukpenning-utokad.button.send': 'Skicka till Försäkringskassan',

        // Sjukersattning
        'lisu.label.certtitle': 'Läkarutlåtande för sjukersättning',

        //Labels
        'sjukpenning-utokad.label.valjkodverk': 'Välj kodverk:',
        'sjukpenning-utokad.label.spara-utkast': 'Spara',
        'sjukpenning-utokad.label.ta-bort-utkast': 'Ta bort utkast',
        'lisu.label.skriv-ut-utkast': 'Skriv ut',

        'sjukpenning-utokad.label.diagnoskod.icd': 'Diagnoskod enligt ICD-10 (huvuddiagnos): ',
        'sjukpenning-utokad.label.diagnoskod.ksh': 'Diagnoskod enligt KSH97-P (Primärvård, huvuddiagnos): ',
        'sjukpenning-utokad.label.diagnoskodverk.icd_10_se': 'ICD-10-SE',
        'sjukpenning-utokad.label.diagnoskodverk.ksh_97_p': 'KSH97-P (Primärvård)',
        'sjukpenning-utokad.label.vardenhet': 'Vårdenhetens adress',

        /*
         'sjukpenning-utokad.label.common': 'Ej angivet',
         'sjukpenning-utokad.label.field': 'Fält',
         'sjukpenning-utokad.label.blank': '- ej ifyllt',
         'sjukpenning-utokad.label.nedsattning': 'Din bedömning av patientens behov av sjukskrivning (Arbete)',
         'sjukpenning-utokad.label.patientnamn': 'Patientens namn',
         'sjukpenning-utokad.label.utfardat': 'Utfärdat',
         'sjukpenning-utokad.label.personnummer': 'Personnummer',
         'sjukpenning-utokad.label.utfardare': 'Utfärdare',
         'sjukpenning-utokad.label.period': 'Period',
         'sjukpenning-utokad.label.enhet': 'Enhet',
         'sjukpenning-utokad.label.smittskydd': 'Avstängning enligt smittskyddslagen på grund av smitta',
         'sjukpenning-utokad.label.yes': 'Ja',
         'sjukpenning-utokad.label.no': 'Nej',
         'sjukpenning-utokad.label.anledningtillkontakt': 'Anledning till kontakt:',
         'sjukpenning-utokad.label.delvis': 'Ja, delvis',
         'sjukpenning-utokad.label.diagnos': 'Diagnos/Diagnoser som orsakar nedsatt arbetsförmåga',
         'sjukpenning-utokad.label.ytterligarediagnoser': 'Ytterligare diagnos',
         'sjukpenning-utokad.label.ytterligarebehandling': 'Ytterligare behandling',
         'sjukpenning-utokad.label.diagnosfortydligande': 'Förtydligande av diagnos/diagnoser',
         'sjukpenning-utokad.label.diagnoses.more_results': 'Det finns fler träffar än vad som kan visas i listan, förfina sökningen.',
         'sjukpenning-utokad.label.samsjuklighet': 'Samsjuklighet föreligger',
         'sjukpenning-utokad.label.aktuellt-sjukdomsforlopp': 'Aktuellt sjukdomsförlopp',
         'sjukpenning-utokad.label.funktionsnedsattning': 'Beskriv patientens funktionsnedsättning inom de områden som är relevanta',
         'sjukpenning-utokad.label.funktionsnedsattningar': 'Beskriv patientens funktionsnedsättningar inom de områden som är relevanta',
         'sjukpenning-utokad.label.intygbaseratpa': 'Intyget baseras på',
         'sjukpenning-utokad.label.kommentar': 'Övrigt',
         'sjukpenning-utokad.label.sysselsattning': 'I relation till vilken sysselsättning gör du den medicinska bedömningen?',
         'sjukpenning-utokad.label.sysselsattning.arbete': 'Nuvarande arbete',
         'sjukpenning-utokad.label.sysselsattning.uppgifter': 'Ange yrke och nuvarande arbetsuppgifter',
         'sjukpenning-utokad.label.sysselsattning.yrke': 'Ange yrke',
         'sjukpenning-utokad.label.sysselsattning.arbetslos': 'Arbetssökande - att utföra sådant arbete som är normalt förekommande på arbetsmarknaden',
         'sjukpenning-utokad.label.sysselsattning.foraldraledighet': 'Föräldraledighet för vård av barn',
         'sjukpenning-utokad.label.sysselsattning.studier': 'Studier',
         'sjukpenning-utokad.label.sysselsattning.program': 'Deltar i arbetsmarknadspolitiskt program',
         'sjukpenning-utokad.label.aktivitetsbegransning': 'Vad har patienten svårt att göra på grund av den eller de funktionsnedsättningar som beskrivs ovan?',
         'sjukpenning-utokad.label.diagnostisering': 'När och var ställdes den/de diagnoser som orsakar nedsatt arbetsförmåga?',
         'sjukpenning-utokad.label.diagnostisering.nybedomning': 'Finns det skäl att göra en ny bedömning av diagnosen?',
         'sjukpenning-utokad.label.diagnostisering.nybedomning.help' :'<p>Möjliga skäl kan vara:</p><p>-att det var länge sedan utredningen gjordes.</p><p>-att ny bedömning har rekommenderats i tidigare utlåtande eller</p><p>-att den kliniska bilden avviker från vad som kan förväntas utifrån ställds diagnos.  </p></p>',
         'sjukpenning-utokad.label.behandling.avslutad': 'Avslutade medicinska behandlingar/åtgärder. Ange också under vilka perioder de pågick och vilka resultat de gav.',
         'sjukpenning-utokad.label.behandling.pagaende': 'Pågående medicinska behandlingar/åtgärder och ansvarig vårdenhet.',
         'sjukpenning-utokad.label.behandling.planerad': 'Planerade medicinska behandlingar/åtgärder. Ange om möjligt tidplan och ansvarig enhet.',
         'sjukpenning-utokad.label.rekommendationer': 'Rekommendationer',
         'sjukpenning-utokad.label.planerad-atgard': 'Medicinsk behandling',
         'sjukpenning-utokad.label.planerad-atgard.sjukvarden': 'Pågående behandling',
         'sjukpenning-utokad.label.planerad-atgard.annat': 'Planerad behandling:',
         'sjukpenning-utokad.label.planerad-atgard.avslutadbehandling': 'Avslutad behandling',
         'sjukpenning-utokad.label.planerad-atgard.pagaendebehandling': 'Pågående behandling',
         'sjukpenning-utokad.label.planerad-atgard.planeradbehandling': 'Planerad behandling',
         'sjukpenning-utokad.label.rehabilitering-aktuell': 'Är arbetslivsinriktad rehabilitering aktuell?',
         'sjukpenning-utokad.label.gar-ej-att-bedomma': 'Går inte att bedöma',
         'sjukpenning-utokad.label.arbetsformaga': 'Patientens arbetsförmåga bedöms i förhållande till',
         'sjukpenning-utokad.label.arbetsformaga.nuvarande-arbete': 'Nuvarande arbete: ',
         'sjukpenning-utokad.label.arbetsformaga.arbetslos': 'Arbetslöshet - att utföra sådant arbete som är normalt förekommande på arbetsmarknaden',
         'sjukpenning-utokad.label.arbetsformaga.foraldraledighet': 'Föräldraledighet med föräldrapenning - att vårda sitt barn',
         'sjukpenning-utokad.label.arbetsformaga.bedomning': 'Patientens arbetsförmåga bedöms nedsatt längre tid än den som det försäkringsmedicinska beslutsstödet anger, därför att:',
         'sjukpenning-utokad.label.resor-till-arbetet': 'Resor till och från arbetet med annat färdmedel än normalt kan göra det möjligt för patienten att återgå till arbete under sjukskrivningsperioden.',
         'sjukpenning-utokad.label.rekommendation-overskrider-beslutsstod': 'Min rekommendation av sjukskrivningens längd överskrider Socialstyrelsens försäkringsmedicinska beslutsstöd.',
         'sjukpenning-utokad.label.kan-gora': 'Beskriv vad patienten kan göra trots sin sjukdom relaterat till sitt arbete.',
         'sjukpenning-utokad.label.kan-gora.view': 'Vad patienten kan göra trots sin sjukdom relaterat till sitt arbete:',
         'sjukpenning-utokad.label.prognos': 'Uppskattning av prognos för när patienten bedöms kunna återgå till aktuell sysselsättning helt eller delvis.',
         'sjukpenning-utokad.label.kontakt-med-fk': 'Kontakt önskas med Försäkringskassan',
         'sjukpenning-utokad.label.kontaktmedfk.anledningtillkontakt': 'Anledning till att du vill ha kontakt med FK?',
         'sjukpenning-utokad.label.arbetsplatskod': 'Förskrivarkod och arbetsplatskod',
         'sjukpenning-utokad.label.rekommendationer.kontakt.arbetsformedlingen': 'Kontakt med Arbetsförmedlingen',
         'sjukpenning-utokad.label.rekommendationer.kontakt.foretagshalsovarden': 'Kontakt med företagshälsovården',
         'sjukpenning-utokad.label.rekommendationer.kontakt.ovrigt': 'Övrigt: ',
         'sjukpenning-utokad.label.se-ovrigt': 'Övrigt',
         'sjukpenning-utokad.label.ovrigt': 'Övrigt',
         'sjukpenning-utokad.label.datum': 'Datum',
         'sjukpenning-utokad.label.kontakt-info': 'Namn och kontaktuppgifter till vårdenheten',
         'sjukpenning-utokad.label.atgarder': 'Medicinsk behandling',
         'sjukpenning-utokad.label.rekommendationer.fk': 'Rekommendationer till Försäkringskassan',
         'sjukpenning-utokad.label.fk-kontakt': 'Vill du att Försäkringskassan kontaktar dig?',
         'sjukpenning-utokad.label.makulera.confirmation': 'Läkarintyg utställd på ${namn} – ${personnummer} är återtaget. Mottagare av intyget är notifierade om detta.',
         'sjukpenning-utokad.label.status.sent': 'Intyget är signerat och har skickats till Försäkringskassans system.',
         'sjukpenning-utokad.label.status.recieved': 'Intyget är signerat och mottaget av Försäkringskassans system.',
         'sjukpenning-utokad.label.status.signed': 'Intyget är signerat. Intyget är nu tillgängligt för patienten i webbtjänsten Mina intyg, som nås via 1177.se. Intyget går även att nå via Försäkringskassans e-tjänst för ansökan om sjukpenning-utokad. Om patienten inte har möjlighet att skicka intyget elektroniskt till Försäkringskassan kan du skicka intyget direkt till Försäkringskassan åt patienten. Observera att patientens samtycke då krävs.',
         'sjukpenning-utokad.label.kopiera.text': 'Kopiera intyg innebär att en kopia skapas av det befintliga intyget och med samma information. I de fall patienten har ändrat namn eller adress så uppdateras den informationen. Uppgifterna i intygsutkastet går att ändra innan det signeras. Kopiera intyg kan användas exempelvis vid förlängning av en sjukskrivning.',
         'sjukpenning-utokad.label.transfertoovrigt': 'Informationen överförs till "Övriga upplysningar" vid signering. Tecken kvar påverkas av antalet tecken tillförda i samtliga fält.',
         'sjukpenning-utokad.label.atgard.inte-aktuellt': 'Inte aktuellt',
         'sjukpenning-utokad.label.atgard.arbetstraning': 'Arbetsträning',
         'sjukpenning-utokad.label.atgard.arbetsanpassning': 'Arbetsanpassning',
         'sjukpenning-utokad.label.atgard.soka-nytt-arbete': 'Söka nytt arbete',
         'sjukpenning-utokad.label.atgard.besok-pa-arbete': 'Besök på arbetsplatsen',
         'sjukpenning-utokad.label.atgard.ergonomi': 'Ergonomisk bedömning',
         'sjukpenning-utokad.label.atgard.hjalpmedel': 'Hjälpmedel',
         'sjukpenning-utokad.label.atgard.konflikthantering': 'Konflikthantering',
         'sjukpenning-utokad.label.atgard.omfordelning': 'Omfördelning av arbetsuppgifter',
         'sjukpenning-utokad.label.atgard.foretagshalsovard': 'Kontakt med företagshälsovård',
         'sjukpenning-utokad.label.atgard.ovrigt': 'Övrigt',
         'sjukpenning-utokad.label.atgard': 'Förslag till åtgärder som skulle kunna underlätta återgång till arbete',
         'sjukpenning-utokad.label.intyg.nedsattningar1': 'Kommunikation och social interaktion',
         'sjukpenning-utokad.label.intyg.nedsattningar2': 'Uppmärksamhet och koncentration',
         'sjukpenning-utokad.label.intyg.nedsattningar3': 'Annan psykisk funktion',
         'sjukpenning-utokad.label.intyg.nedsattningar4': 'Syn, hörsel och tal',
         'sjukpenning-utokad.label.intyg.nedsattningar5': 'Balans, koordination och motorik',
         'sjukpenning-utokad.label.intyg.nedsattningar6': 'Annan kroppslig funktion',
         'sjukpenning-utokad.label.intyg.nedsattningar7': 'Okänd',

         'sjukpenning-utokad.label.medicinskaforutsattningar': 'Medicinska förutsättningar för arbete',
         'sjukpenning-utokad.label.medicinskaforutsattningar.bedomning': 'Hur bedömer du patientens medicinska förutsättningar för arbete, helt eller delvis, kan utvecklas över tid?',
         'sjukpenning-utokad.label.medicinskaforutsattningar.mojligheter': 'Beskriv vad patienten kan göra trots sin sjukdom eller sina begränsningar',

         'sjukpenning-utokad.label.grund-for-mu-date.undersokningavpatienten': 'Min undersökning av patienten',
         'sjukpenning-utokad.label.grund-for-mu-date.journaluppgifter': 'Journaluppgifter från den',
         'sjukpenning-utokad.label.grund-for-mu-date.anhorigsbeskrivningavpatienten': 'Anhörigs beskrivning av patienten',
         'sjukpenning-utokad.label.grund-for-mu-date.annatgrundformu': 'Annat',
         'sjukpenning-utokad.label.grund-for-mu-date.kannedomompatient': 'Jag har känt patienten sedan den',

         'sjukpenning-utokad.vardkontakt.undersokning': 'Min undersökning av patienten den ',
         'sjukpenning-utokad.referens.journal': 'Journaluppgifter, den ',
         'sjukpenning-utokad.referens.anhorigbeskrivningavpatienten': 'Anhörig beskrivning av patiend, den ',
         'sjukpenning-utokad.referens.telefonkontaktmedpatienten': 'Telefonkontakt med patienten, den',
         'sjukpenning-utokad.referens.kannedomompatient': 'Jag har känt patien, från den ',
         'sjukpenning-utokad.referens.underlag0': 'Neuropsykiatriskt utlåtande, från den ',
         'sjukpenning-utokad.referens.underlag1': 'Underlag från habiliteringen, från den ',
         'sjukpenning-utokad.referens.underlag2': 'Underlag från arbetsterapeut, från den ',
         'sjukpenning-utokad.referens.underlag3': 'Underlag från fysioterapeut, från den ',
         'sjukpenning-utokad.referens.underlag4': 'Underlag från logoped, från den ',
         'sjukpenning-utokad.referens.underlag5': 'Underlag från psykolog, från den ',
         'sjukpenning-utokad.referens.underlag6': 'Underlag från företagshälsovård, från den ',
         'sjukpenning-utokad.referens.underlag7': 'Utredning av annan specialistklinik, från den ',
         'sjukpenning-utokad.referens.underlag8': 'Utredning från vårdinrättning utomlands, från den ',
         'sjukpenning-utokad.referens.underlag9': 'Övrigt, från den ',
         'sjukpenning-utokad.referens.annat': 'Annat, den ',

         'sjukpenning-utokad.nedsattningsgrad.helt_nedsatt': 'Helt nedsatt',
         'sjukpenning-utokad.nedsattningsgrad.nedsatt_med_3_4': 'Nedsatt med 3/4',
         'sjukpenning-utokad.nedsattningsgrad.nedsatt_med_1_2': 'Nedsatt med hälften',
         'sjukpenning-utokad.nedsattningsgrad.nedsatt_med_1_4': 'Nedsatt med 1/4',

         //Help texts
         'sjukpenning-utokad.helptext.smittskydd': 'Fylls i om patienten måste avstå från sitt arbete på grund av:<ul><li>Intygsskrivande läkares beslut enligt smittskyddslagen.</li><li>Läkarundersökning eller hälsokontroll som syftar till att klarlägga om hon eller han är smittad av en allmänfarlig sjukdom eller om personen har en sjukdom, en smitta, ett sår eller annan skada som gör att hon eller han inte får hantera livsmedel.</li>',

         'sjukpenning-utokad.helptext.intyg-baserat-pa': 'Motivera under rubriken "övrigt" i de fall någon undersökning av patienten inte varit aktuell.',
         'sjukpenning-utokad.helptext.intyg-baserat-pa.annat': 'Detta alternativ kan användas för annan professions bedömning, till exempel för kuratorsanteckning, sjukgymnastanteckning eller teamkonferens.',

         'sjukpenning-utokad.helptext.diagnos': 'Ange vilken eller vilka sjukdomar som medför nedsättning av arbetsförmågan.',
         'sjukpenning-utokad.helptext.diagnos.ytterligare': 'Observera att det som skrivs in här kommer vid granskning och utskrift av intyget att flyttas till ett fritextfält (Diagnos) tillsammans med förtydligandet av diagnoser nedan.',
         'sjukpenning-utokad.helptext.diagnos.fortydligande': 'I intygsblanketten kommer allt som inte skrivs i huvuddiagnosens kodruta att hamna i fritext (fältet Diagnos). I fritextfältet kan du ytterligare kommentera eller förtydliga de angivna diagnoserna eller åtgärderna.',
         'sjukpenning-utokad.helptext.diagnos.samsjuklighet': 'Om du anser att Försäkringskassan särskilt bör uppmärksammas på att det rör sig om samsjuklighet, dvs. om patienten har flera sjukdomar eller symtom som förstärker och förvärrar patientens tillstånd, kan du ange det här. Om du anger att samsjuklighet föreligger kommer texten "Samsjuklighet föreligger" att synas i fritextfältet för diagnos vid granskning och utskrift av intyget.',

         'sjukpenning-utokad.helptext.aktuellt-sjukdomsforlopp': 'Beskriv kortfattat sjukdomens utveckling, situation vid undersökningstillfället och förhållanden som påverkar sjukdomsutvecklingen.',

         'sjukpenning-utokad.helptext.funktionsnedsattning': 'Beskriv patientens funktionsnedsättning inom de områden som är relevanta.',

         'sjukpenning-utokad.helptext.aktivitetsbegransningOld': '<p>Ge en tydlig beskrivning av hur sjukdomen begränsar patientens förmåga, liksom av hur omfattande konsekvenserna är.</p> Denna information är den mest centrala för bedömning av rätten till sjukpenning-utokad. Använd enkel svenska som är begriplig för en person utan medicinsk utbildning.',
         'sjukpenning-utokad.helptext.aktivitetsbegransning': 'Vad har patienten svårt att göra på grund av den eller de funktionsnedsättningar som beskrivs ovan',
         'sjukpenning-utokad.helptext.diagnosticering': '<p>Ge en tydlig beskrivning av hur sjukdomen begränsar patientens förmåga, liksom av hur omfattande konsekvenserna är.</p> Denna information är den mest centrala för bedömning av rätten till sjukpenning-utokad. Använd enkel svenska som är begriplig för en person utan medicinsk utbildning.',

         'sjukpenning-utokad.helptext.sysselsattning': 'Ange vad du bedömt nedsättningen av arbetsförmågan mot: patientens nuvarande arbete, arbetslöshet eller föräldraledighet. Läkarintyget måste alltid innehålla information om det. Om patienten till exempel både arbetar och är föräldraledig, kan du förtydliga det under Övriga upplysningar.',
         'sjukpenning-utokad.helptext.sysselsattning.nuvarande': 'Om patienten har ett arbete, ska nedsättningen av arbetsförmåga relateras till de arbetsuppgifter patienten har.',
         'sjukpenning-utokad.helptext.sysselsattning.arbetsloshet': 'Är patienten arbetslös ska bedömningen relateras till förmågan att klara ett arbete på den reguljära arbetsmarknaden. Det är viktigt att en arbetslös patient inte sjukskrivs i förhållande till det arbete han eller hon hade tidigare. Observera att arbetslösa också kan sjukskrivas på deltid.',
         'sjukpenning-utokad.helptext.sysselsattning.foraldraledighet': 'Är patienten föräldraledig ska bedömningen relateras till förmågan att ta hand om barnet.',

         'sjukpenning-utokad.helptext.arbetsformaga': 'Ange hur mycket patientens arbetsförmåga bedöms vara nedsatt och för hur lång tid. För att patienten ska få rätt till sjukpenning-utokad ska aktivitetsbegränsningen påverka arbetsförmågan med minst 25 procent. Gradvis ökad arbetsförmåga kan anges på samma intyg.',
         'sjukpenning-utokad.helptext.arbetsformaga.faktisk-tjanstgoringstid': 'Ange hur många timmar patienten arbetar i snitt per vecka. Observera att denna uppgift endast är ett stöd för att tydliggöra hur många timmar per vecka patienten bedöms kunna arbeta när en viss arbetsförmågenedsättning har angivits. Uppgiften lagras inte som en del av intyget då Försäkringskassan inhämtar informationen från annat håll.',
         'sjukpenning-utokad.helptext.arbetsformaga.bedoms-langre': 'Om sjukskrivningen omfattar längre tid än den som rekommenderas i det försäkringsmedicinska beslutsstödets specifika rekommendationer, motivera varför. Det gäller också om patienten vid intygsskrivandet redan varit sjukskriven längre tid än beslutsstödets rekommendationer.',
         'sjukpenning-utokad.helptext.arbetsformaga.prognos': 'Ange hur du bedömer patientens möjligheter att återgå till sina nuvarande arbetsuppgifter i samma omfattning som före sjukskrivningen. Prognosen avser inte möjligheter till omplacering eller förändrade arbetsuppgifter. För en föräldraledig patient avser prognosen möjligheterna att återfå förmågan att ta hand om barnet.',

         'sjukpenning-utokad.helptext.atgarder': 'Ange om patienten är under behandling eller väntar på åtgärder inom vården (till exempel operationer eller behandlingar) eller någon annanstans. Angivna åtgärder ska vara väsentliga för att patienten ska återfå arbetsförmågan.',

         'sjukpenning-utokad.helptext.rekommendationer': 'Här kan du ge Försäkringskassan underlag för bedömning av patientens behov av samordade åtgärder för rehabilitering. Dina rekommendationer riktar sig också till patienten själv och till patientens arbetsgivare. Försäkringskassan har ett samordningsansvar för rehabiliteringen. Försäkringskassan kan bland annat ta initiativ till möten med patienten och olika aktörer som kan bidra till att patienten snabbare kan återgå i arbete.',
         'sjukpenning-utokad.helptext.rekommendationer.resor': 'Ange om resor till och från arbetet med annat färdsätt, till exempel taxi eller automatväxlad bil, kan göra att patienten kan arbeta trots sjukdomen. Patienten kan få ersättning för annat färdsätt i tre månader, och i undantagsfall längre.',
         'sjukpenning-utokad.helptext.rekommendationer.arbetsformedlingen': 'Kontakt med Arbetsförmedlingen rekommenderas om du bedömer att patienten behöver hjälp via Arbetsförmedlingen för att han eller hon ska kunna återgå i arbete.',
         'sjukpenning-utokad.helptext.rekommendationer.foretagshalsovarden': 'Patientens arbetsgivare kan ha ett avtal om företagshälsovård. Försäkringskassan har ansvar för samordning av aktiviteter för patientens rehabilitering. Kontakt med företagshälsovården rekommenderas om: <ul><li>patienten riskerar en längre sjukskrivningsperiod (överstigande 45 dagar) eller</li><li>patientens arbetsförmåga i relation till nuvarande arbetsuppgifter behöver bedömas närmare eller</li><li>det finns skäl att anta att patientens återgång till nuvarande arbete inte är möjligt eller endast möjligt vid förändring/anpassning av dennes arbetsuppgifter</li></ul>',
         'sjukpenning-utokad.helptext.rekommendationer.ovrigt': 'Ange om du bedömer att det finns ytterligare någon insats som skulle kunna bidra till att patienten snabbare kan återgå i arbete.',
         'sjukpenning-utokad.helptext.rekommendationer.arbetslivsinriktad-rehabilitering': 'Ange om du bedömer att patienten skulle ha förutsättningar att återvinna arbetsförmågan snabbare med arbetslivsinriktad rehabilitering.',

         'sjukpenning-utokad.helptext.kontakt': 'Ange om du vill att Försäkringskassans handläggare tar kontakt med dig för att diskutera patientens sjukskrivning. Observera att denna ruta i första hand ska användas om intyget ska skrivas ut på papper. Överförs intyget elektroniskt används hellre funktionen för elektronisk ärendekommunikation (efter det att intyget skickats in).',

         'sjukpenning-utokad.helptext.ovrigt': 'Ange här sådan ytterligare information som du bedömer att Försäkringskassan skulle ha nytta av. Observera att vid granskning och utskrift av intyget kommer text som skrivits in i vissa av fritextfälten på andra ställen i intyget också att flyttas hit.',

         'sjukpenning-utokad.helptext.samtycke': 'Läkarintyget får bara skickas till Försäkringskassan om patienten givit sitt samtycke till det. Genom att kryssa i denna ruta intygar du att du fått patientens samtycke till att skicka intyget elektroniskt till Försäkringskassan. ',
         'sjukpenning-utokad.helptext.adress': 'Vårdenhetens kontaktuppgifter inhämtas från HSA. Om vårdenhetens kontaktuppgifter inte stämmer kontakta HSA för att ändra dem.',
         */
        //Validation messages

        'lisu.validation.underlagfinns.missing': 'Frågan \'Finns det andra medicinska utredningar eller underlag\' måste besvaras',
        'lisu.validation.underlag.max-extra-underlag': 'Du kan inte lägga till fler utredningar, max antal är 10st',
        'lisu.validation.diagnos.missing': 'ICD-10 kod saknas på huvuddiagnosen. (Fält 2)',
        'lisu.validation.diagnos.invalid': 'ICD-10 kod på huvuddiagnosen är ej giltig',
        'lisu.validation.diagnos.length-3': 'Diagnoskod ska anges med så många positioner som möjligt, men minst tre positioner.',
        'lisu.validation.diagnos.psykisk.length-4': 'Diagnoskod ska anges med minst fyra positioner då en psykisk diagnos anges.',
        'lisu.validation.diagnos2.invalid': 'ICD-10 kod på ytterligare diagnoser rad 1, är ej giltig. (Fält 2)',
        'lisu.validation.diagnos3.invalid': 'ICD-10 kod på ytterligare diagnoser rad 2, är ej giltig. (Fält 2)',

        'lisu.validation.rekommendationer.ovriga': 'Fritextfältet som hör till alternativet Övrigt måste fyllas i. (Fält 6a)',

        'lisu.validation.grund-for-mu.undersokning.incorrect_format': 'Fel datumformat för undersökning av patient (Fält 1)',

        'lisu.validation.grund-for-mu.annan.incorrect_format': 'Fel datumformat för annan referens (Fält 1)',
        'lisu.validation.grund-for-mu.journaluppgifter.incorrect_format': 'Fel datumformat för journaluppgifter (Fält 1)',

        'lisu.validation.grund-for-mu.missing': 'Välj minst ett alternativ som uppgifterna i intyget baseras på. (Fält 1)',
        'lisu.validation.grund-for-mu.kannedom.missing': 'Fältet \'Jag har känt patienten sedan\' måste fyllas i. (Fält 1)',
        'lisu.validation.grund-for-mu.kannedom.incorrect_format': 'Fel datumformat för \'Kännedom om patienten\'',
        'lisu.validation.grund-for-mu.kannedom.after.undersokning': '\'Kännedom om patienten\' får inte vara senare än datum för \'Min undersökning av patienten\'',
        'lisu.validation.grund-for-mu.kannedom.after.anhorigsbeskrivning': '\'Kännedom om patienten\' får inte vara senare än datum för \'Anhörigs beskrivning av patienten\'',
        'lisu.validation.grund-for-mu.annat.beskrivning.missing': 'Fritextfältet som hör till alternativet Annat under \'Uppgifterna i intyget baseras på\' måste fyllas i. (Fält 4)',

        'lisu.validation.sjukdomsforlopp.missing': '\'Beskrivning av sjukdomsförlopp\' måste anges',

        'lisu.validation.diagnosgrund.missing': 'Fältet \'När och var ställdes den/de diagnoser som orsakar nedsatt arbetsförmåga?\' måste fyllas i(Fält 3)',
        'lisu.validation.nybedomningdiagnosgrund.missing': 'Fältet \'Finns det skäl att göra en ny bedömning av diagnosen?\' måste fyllas i(Fält 3)',

        'lisu.validation.funktionsnedsattning.missing': 'Minst en funktionsnedsättning måste anges.',

        'lisu.validation.aktivitetsbegransning.missing': 'Fältet med aktivitetsbegränsning måste fyllas i. (Fält 5)',

        'lisu.validation.sysselsattning.missing': 'Välj minst ett alternativ som arbetsförmågan bedöms i förhållande till. (Fält 8a)',
        'lisu.validation.sysselsattning.arbetsuppgifter.missing': 'Aktuella arbetsuppgifter som hör till alternativet Nuvarande arbete under \'Arbete\' måste fyllas i. (Fält 8a)',

        'lisu.validation.nedsattning.tjanstgoringstid': 'För att kunna beräkna arbetstid måste \'Faktisk tjänstgöringstid\' vara ett nummer (Fält 8a)',
        'lisu.validation.nedsattning.choose-at-least-one': 'Välj minst ett alternativ för arbetsförmåga. (Fält 8b)',
        'lisu.validation.nedsattning.incorrect-date-interval': 'Ett datuminterval har angetts på ett felaktigt sätt (startdatum efter slutdatum). (Fält 8b)',

        'lisu.validation.nedsattning.nedsattmed100.incorrect-format': 'Datum för nedsatt med 100% har angetts med felaktigt format (Fält 8b)',
        'lisu.validation.nedsattning.nedsattmed75.incorrect-format': 'Datum för nedsatt med 75% har angetts med felaktigt format (Fält 8b)',
        'lisu.validation.nedsattning.nedsattmed50.incorrect-format': 'Datum för nedsatt med 50% har angetts med felaktigt format (Fält 8b)',
        'lisu.validation.nedsattning.nedsattmed25.incorrect-format': 'Datum för nedsatt med 25% har angetts med felaktigt format (Fält 8b)',

        'lisu.validation.nedsattning.overlapping-date-interval': 'Två datuminterval med överlappande datum har angetts. (Fält 8b)',

        'lisu.validation.nedsattning.prognos.choose-one': 'Max ett alternativ kan väljas (Fält 10)',

        'lisu.validation.prognos.gar-ej-att-bedomma.beskrivning.missing': 'Fritextfältet som hör till alternativet Går ej att bedöma, förtydligande under \'Prognos - kommer patienten att få tillbaka sin arbetsförmåga i nuvarande arbete\' måste fyllas i. (Fält 10)',

        'lisu.validation.forandrat-ressatt.choose-one': 'Endast ett alternativ kan anges avseende förändrat ressätt. (Fält 11)',

        'lisu.validation.vardenhet.telefonnummer.missing': 'Telefonnummer för vårdenhet måste anges (Fält 15)',
        'lisu.validation.vardenhet.postadress.missing': 'Postadress för vårdenhet måste anges (Fält 15)',
        'lisu.validation.vardenhet.postnummer.missing': 'Postnummer för vårdenhet måste anges (Fält 15)',
        'lisu.validation.vardenhet.postnummer.incorrect-format': 'Postnummer har fel format (Fält 15)',
        'lisu.validation.vardenhet.postort.missing': 'Postort för vårdenhet måste anges (Fält 15)',


        // errors
        'sjukpenning-utokad.error.could_not_load_cert': '<strong>Intyget gick inte att läsa in.</strong><br>Prova att ladda om sidan. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand Nationell kundservice på 0771-251010.',
        'sjukpenning-utokad.error.could_not_load_cert_not_auth': '<strong>Kunde inte hämta intyget eftersom du saknar behörighet.</strong>',
        'sjukpenning-utokad.error.could_not_load_cert_qa': '<strong>Frågor och svar för detta intyg gick inte att läsa in. Prova att ladda om sidan. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand Nationell kundservice på 0771-251010.</strong>',
        // fragaSvar errors
        'sjukpenning-utokad.error.external_system_problem': '<strong>Meddelandet har inte skickats till Försäkringskassan då Webcert saknar kontakt med Försäkringskassans datasystem.</strong><br>Prova att skicka om meddelandet. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand Nationell kundservice på 0771-251010.',
        'sjukpenning-utokad.error.internal_problem': '<strong>Ett tekniskt problem inträffade.</strong><br>Försök igen och kontakta supporten om problemet kvarstår.',
        'sjukpenning-utokad.error.invalid_state': '<strong>Funktionen är inte giltig.</strong><br>Förmodligen har en annan användare ändrat informationen medan du arbetat på samma post. Ladda om informationen och försök igen',
        'sjukpenning-utokad.error.unknown_internal_problem': '<strong>Ett tekniskt problem inträffade.</strong><br>Försök igen och kontakta supporten om problemet kvarstår.',
        'sjukpenning-utokad.error.authorization_problem': '<strong>Du har inte behörighet att utföra funktionen, kontakta supporten för mer information.</strong>',
        'sjukpenning-utokad.error.data_not_found': '<strong>Intyget kunde inte hittas i intygstjänsten.</strong><br>Intyget är borttaget eller så saknas behörighet.',
        'sjukpenning-utokad.fragasvar.answer.is.sent': '<strong>Svaret har skickats till Försäkringskassan.</strong><br> Frågan är nu markerad som hanterad och visas nu under \'Hanterade frågor\' längre ner på sidan.',
        'sjukpenning-utokad.fragasvar.marked.as.hanterad': '<strong>Frågan-svaret är markerad som hanterad.</strong><br> Frågan-svaret visas under rubriken \'hanterade frågor och svar\' nedan.',
        'sjukpenning-utokad.fragasvar.marked.as.ohanterad': '<strong>Frågan-svaret är markerad som ej hanterad.</strong><br> Frågan-svaret visas under rubriken \'Ej hanterade frågor och svar\' ovan.',

        'sjukpenning-utokad.fragasvar.label.ovanstaende-har-bekraftats': '<strong>Ovanstående har bekräftats</strong>'

    },
    'en': {
        'sjukpenning-utokad.label.pagetitle': 'Show Certificate'
    }
};