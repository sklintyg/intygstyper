/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/* jshint maxlen: false */
angular.module('fk7263').constant('fk7263.messages', {
    'sv': {
        // fragaSvar errors
        'fk7263.error.external_system_problem': '<strong>Meddelandet har inte skickats till Försäkringskassan då Webcert saknar kontakt med Försäkringskassans datasystem.</strong><br>Prova att skicka om meddelandet. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand Nationell kundservice på 0771-251010.',
        'fk7263.error.internal_problem': '<strong>Ett tekniskt problem inträffade.</strong><br>Försök igen och kontakta supporten om problemet kvarstår.',
        'fk7263.error.invalid_state': '<strong>Funktionen är inte giltig.</strong><br>Förmodligen har en annan användare ändrat informationen medan du arbetat på samma post. Ladda om informationen och försök igen',
        'fk7263.error.unknown_internal_problem': '<strong>Ett tekniskt problem inträffade.</strong><br>Försök igen och kontakta supporten om problemet kvarstår.',
        'fk7263.error.authorization_problem': '<strong>Du har inte behörighet att utföra funktionen, kontakta supporten för mer information.</strong>',
        'fk7263.error.data_not_found': '<strong>Intyget kunde inte hittas i intygstjänsten.</strong><br>Intyget är borttaget eller så saknas behörighet.',
        'fk7263.error.komplettera-no-intyg': '<strong>Kan inte svara med nytt intyg</strong><br>Intyget kunde inte laddas och är därför inte tillgängligt att användas som grund för komplettering.',

        // fragasvar actions response
        'fk7263.fragasvar.answer.is.sent': '<strong>Svaret har skickats till Försäkringskassan.</strong><br> Frågan är nu markerad som hanterad och visas nu under \'Hanterade frågor\' längre ner på sidan.',
        'fk7263.fragasvar.marked.as.hanterad': '<strong>Frågan-svaret är markerad som hanterad.</strong><br> Frågan-svaret visas under rubriken \'hanterade frågor och svar\' nedan.',
        'fk7263.fragasvar.marked.as.ohanterad': '<strong>Frågan-svaret är markerad som ej hanterad.</strong><br> Frågan-svaret visas under rubriken \'Ej hanterade frågor och svar\' ovan.',

        //Labels
        'fk7263.label.field': 'Fält',
        'fk7263.label.nedsattning': 'Jag bedömer att arbetsförmåga är',
        'fk7263.label.patientnamn': 'Patientens namn',
        'fk7263.label.utfardat': 'Utfärdat',
        'fk7263.label.personnummer': 'Personnummer',
        'fk7263.label.utfardare': 'Utfärdare',
        'fk7263.label.period': 'Period',
        'fk7263.label.enhet': 'Enhet',
        'fk7263.label.smittskydd': 'Avstängning enligt smittskyddslagen på grund av smitta',
        'fk7263.label.yes': 'Ja',
        'fk7263.label.no': 'Nej',
        'fk7263.label.delvis': 'Ja, delvis',
        'fk7263.label.diagnos': 'Diagnos/diagnoser för sjukdom som orsakar nedsatt arbetsförmåga',
        'fk7263.label.diagnoskod.icd': 'Diagnoskod enligt ICD-10 (huvuddiagnos): ',
        'fk7263.label.diagnoskod.ksh': 'Diagnoskod enligt KSH97-P (Primärvård, huvuddiagnos): ',
        'fk7263.label.diagnoskodverk.icd_10_se': 'ICD-10-SE',
        'fk7263.label.diagnoskodverk.ksh_97_p': 'KSH97-P (Primärvård)',
        'fk7263.label.ytterligarediagnoser': 'Ytterligare diagnoser',
        'fk7263.label.diagnosfortydligande': 'Förtydligande av diagnos/diagnoser',
        'fk7263.label.diagnoses.more_results': 'Det finns fler träffar än vad som kan visas i listan, förfina sökningen.',
        'fk7263.label.samsjuklighet': 'Samsjuklighet föreligger',
        'fk7263.label.aktuellt-sjukdomsforlopp': 'Aktuellt sjukdomsförlopp',
        'fk7263.label.funktionsnedsattning': 'Funktionsnedsättning - observationer, undersökningsfynd och utredningsresultat',
        'fk7263.label.intygbaseratpa': 'Intyget baseras på',
        'fk7263.label.kommentar': 'Kommentar:',
        'fk7263.label.sysselsattning': 'Arbete',
        'fk7263.label.aktivitetsbegransning': 'Aktivitetsbegränsning relaterat till diagnos och funktionsnedsättning',
        'fk7263.label.rekommendationer': 'Rekommendationer',
        'fk7263.label.planerad-atgard': 'Planerad eller pågående behandling eller åtgärd',
        'fk7263.label.planerad-atgard.sjukvarden': 'Inom sjukvården:',
        'fk7263.label.planerad-atgard.annat': 'Annan åtgärd:',
        'fk7263.label.rehabilitering-aktuell': 'Är arbetslivsinriktad rehabilitering aktuell?',
        'fk7263.label.gar-ej-att-bedomma': 'Går inte att bedöma',
        'fk7263.label.arbetsformaga': 'Patientens arbetsförmåga bedöms i förhållande till',
        'fk7263.label.arbetsformaga.nuvarande-arbete': 'Nuvarande arbete: ',
        'fk7263.label.arbetsformaga.arbetslos': 'Arbetslöshet - att utföra sådant arbete som är normalt förekommande på arbetsmarknaden',
        'fk7263.label.arbetsformaga.foraldrarledighet': 'Föräldraledighet med föräldrapenning - att vårda sitt barn',
        'fk7263.label.arbetsformaga.bedomning': 'Patientens arbetsförmåga bedöms nedsatt längre tid än den som det försäkringsmedicinska beslutsstödet anger, därför att:',
        'fk7263.label.prognos': 'Prognos - kommer patienten att få tillbaka sin arbetsförmåga i nuvarande arbete? (Gäller inte arbetslösa)',
        'fk7263.label.resor-till-arbetet': 'Kan resor till och från arbetet med annat färdsätt än normalt göra det möjligt för patienten att återgå i arbete?',
        'fk7263.label.kontakt-med-fk': 'Kontakt önskas med Försäkringskassan',
        'fk7263.label.arbetsplatskod': 'Förskrivarkod och arbetsplatskod',
        'fk7263.label.rekommendationer.kontakt.arbetsformedlingen': 'Kontakt med Arbetsförmedlingen',
        'fk7263.label.rekommendationer.kontakt.foretagshalsovarden': 'Kontakt med företagshälsovården',
        'fk7263.label.rekommendationer.kontakt.ovrigt': 'Övrigt: ',
        'fk7263.label.se-ovrigt': 'Se under Övriga upplysningar och förtydliganden',
        'fk7263.label.ovrigt': 'Övriga upplysningar och förtydliganden',
        'fk7263.label.datum': 'Datum',
        'fk7263.label.kontakt-info': 'Namn och kontaktuppgifter till vårdenheten',
        'fk7263.label.atgarder': 'Åtgärder',
        'fk7263.label.rekommendationer.fk': 'Rekommendationer till Försäkringskassan',
        'fk7263.label.fk-kontakt': 'Kontakt',
        'fk7263.label.vardenhet': 'Vårdenhetens kontaktuppgifter',
        'fk7263.label.makulera.confirmation': 'Läkarintyg FK 7263 utställd på ${namn} – ${personnummer} är återtaget. Mottagare av intyget är notifierade om detta.',
        'fk7263.label.valjkodverk': 'Välj kodverk:',
        'fk7263.label.spara-utkast': 'Spara',
        'fk7263.label.ta-bort-utkast': 'Ta bort utkast',
        'fk7263.label.skriv-ut-utkast': 'Skriv ut',
        'fk7263.label.status.sent': 'Intyget är signerat och har skickats till Försäkringskassans system.<br><br>Intyget är nu tillgängligt för patienten i Mina intyg, som nås via 1177.se.',
        'fk7263.label.status.recieved': 'Intyget är signerat, skickat och mottaget av Försäkringskassans system.<br><br>Intyget är nu tillgängligt för patienten i Mina intyg, som nås via 1177.se.',
        'fk7263.label.status.signed': 'Intyget är signerat. <br><br>Intyget är tillgängligt för patienten i Mina intyg, som nås via 1177.se. Intyget går även att nå via Försäkringskassans e-tjänst för ansökan om sjukpenning. Om patienten inte har möjlighet att skicka intyget elektroniskt till Försäkringskassan kan du skicka intyget direkt till Försäkringskassan åt patienten.',
        'fk7263.label.kopiera.text': 'Skapar en kopia av befintligt intyg. Det nya utkastet (kopian) kan ändras och signeras.',
        'fk7263.label.fornya.text': 'Förnya ett befintligt intyg. Det förnyade utkastet kan ändras och signeras.',
        'fk7263.label.transfertoovrigt': 'Informationen överförs till "Övriga upplysningar" vid signering. Tecken kvar påverkas av antalet tecken tillförda i samtliga fält.',
        'fk7263.vardkontakt.undersokning': 'Min undersökning av patienten den ',
        'fk7263.vardkontakt.telefonkontakt': 'Min telefonkontakt med patienten den ',
        'fk7263.referens.journal': 'Journaluppgifter, den ',
        'fk7263.referens.annat': 'Annat, den ',
        'fk7263.nedsattningsgrad.helt_nedsatt': 'Helt nedsatt',
        'fk7263.nedsattningsgrad.nedsatt_med_3_4': 'Nedsatt med 3/4',
        'fk7263.nedsattningsgrad.nedsatt_med_1_2': 'Nedsatt med hälften',
        'fk7263.nedsattningsgrad.nedsatt_med_1_4': 'Nedsatt med 1/4',

        //Help texts
        'fk7263.helptext.smittskydd': 'Fylls i om patienten måste avstå från sitt arbete på grund av:<ul><li>Intygsskrivande läkares beslut enligt smittskyddslagen.</li><li>Läkarundersökning eller hälsokontroll som syftar till att klarlägga om hon eller han är smittad av en allmänfarlig sjukdom eller om personen har en sjukdom, en smitta, ett sår eller annan skada som gör att hon eller han inte får hantera livsmedel.</li>',
        'fk7263.helptext.sista-giltighets-datum': 'Datum "från och med" baseras på förra intygets sista giltighetsdag ({{lastEffectiveDate}}).',
        'fk7263.helptext.intyg-baserat-pa': 'Ange vad uppgifterna i intyget baseras på. Flera alternativ kan väljas.',
        'fk7263.helptext.intyg-baserat-pa.annat': 'Detta alternativ kan användas för annan professions bedömning, till exempel för kuratorsanteckning, sjukgymnastanteckning eller teamkonferens.',
        'fk7263.helptext.diagnos': 'Ange vilken eller vilka sjukdomar som medför nedsättning av arbetsförmågan.',
        'fk7263.helptext.diagnos.ytterligare': 'Observera att det som skrivs in här kommer vid granskning och utskrift av intyget att flyttas till ett fritextfält (Diagnos) tillsammans med förtydligandet av diagnoser nedan.',
        'fk7263.helptext.diagnos.fortydligande': 'I intygsblanketten kommer allt som inte skrivs i huvuddiagnosens kodruta att hamna i fritext (fältet Diagnos). I fritextfältet kan du ytterligare kommentera eller förtydliga de angivna diagnoserna eller åtgärderna.',
        'fk7263.helptext.diagnos.samsjuklighet': 'Om du anser att Försäkringskassan särskilt bör uppmärksammas på att det rör sig om samsjuklighet, dvs. om patienten har flera sjukdomar eller symtom som förstärker och förvärrar patientens tillstånd, kan du ange det här. Om du anger att samsjuklighet föreligger kommer texten "Samsjuklighet föreligger" att synas i fritextfältet för diagnos vid granskning och utskrift av intyget.',
        'fk7263.helptext.aktuellt-sjukdomsforlopp': 'Beskriv kortfattat sjukdomens utveckling, situation vid undersökningstillfället och förhållanden som påverkar sjukdomsutvecklingen.',
        'fk7263.helptext.funktionsnedsattning': 'Beskriv den angivna sjukdomens påverkan på patientens fysiska respektive psykiska funktioner och hur denna påverkan observerats vid din och andra professioners undersökning och medicinska utredning.',
        'fk7263.helptext.aktivitetsbegransning': '<p>Ge en tydlig beskrivning av hur sjukdomen begränsar patientens förmåga, liksom av hur omfattande konsekvenserna är.</p> Denna information är den mest centrala för bedömning av rätten till sjukpenning. Använd enkel svenska som är begriplig för en person utan medicinsk utbildning.',
        'fk7263.helptext.sysselsattning': 'Ange vad du bedömt nedsättningen av arbetsförmågan mot: patientens nuvarande arbete, arbetslöshet eller föräldraledighet. Läkarintyget måste alltid innehålla information om det. Om patienten till exempel både arbetar och är föräldraledig, kan du förtydliga det under Övriga upplysningar.',
        'fk7263.helptext.sysselsattning.nuvarande': 'Om patienten har ett arbete, ska nedsättningen av arbetsförmåga relateras till de arbetsuppgifter patienten har.',
        'fk7263.helptext.sysselsattning.arbetsloshet': 'Är patienten arbetslös ska bedömningen relateras till förmågan att klara ett arbete på den reguljära arbetsmarknaden. Det är viktigt att en arbetslös patient inte sjukskrivs i förhållande till det arbete han eller hon hade tidigare. Observera att arbetslösa också kan sjukskrivas på deltid.',
        'fk7263.helptext.sysselsattning.foraldrarledighet': 'Är patienten föräldraledig ska bedömningen relateras till förmågan att ta hand om barnet.',
        'fk7263.helptext.arbetsformaga': 'Ange hur mycket patientens arbetsförmåga bedöms vara nedsatt och för hur lång tid. För att patienten ska få rätt till sjukpenning ska aktivitetsbegränsningen påverka arbetsförmågan med minst 25 procent. Gradvis ökad arbetsförmåga kan anges på samma intyg.',
        'fk7263.helptext.arbetsformaga.faktisk-tjanstgoringstid': 'Ange hur många timmar patienten arbetar i snitt per vecka. Observera att denna uppgift endast är ett stöd för att tydliggöra hur många timmar per vecka patienten bedöms kunna arbeta när en viss arbetsförmågenedsättning har angivits. Uppgiften lagras inte som en del av intyget då Försäkringskassan inhämtar informationen från annat håll.',
        'fk7263.helptext.arbetsformaga.bedoms-langre': 'Om sjukskrivningen omfattar längre tid än den som rekommenderas i det försäkringsmedicinska beslutsstödets specifika rekommendationer, motivera varför. Det gäller också om patienten vid intygsskrivandet redan varit sjukskriven längre tid än beslutsstödets rekommendationer.',
        'fk7263.helptext.arbetsformaga.prognos': 'Ange hur du bedömer patientens möjligheter att återgå till sina nuvarande arbetsuppgifter i samma omfattning som före sjukskrivningen. Prognosen avser inte möjligheter till omplacering eller förändrade arbetsuppgifter. För en föräldraledig patient avser prognosen möjligheterna att återfå förmågan att ta hand om barnet.',
        'fk7263.helptext.atgarder': 'Ange om patienten är under behandling eller väntar på åtgärder inom vården (till exempel operationer eller behandlingar) eller någon annanstans. Angivna åtgärder ska vara väsentliga för att patienten ska återfå arbetsförmågan.',
        'fk7263.helptext.rekommendationer': 'Här kan du ge Försäkringskassan underlag för bedömning av patientens behov av samordnade åtgärder för rehabilitering. Dina rekommendationer riktar sig också till patienten själv och till patientens arbetsgivare. Försäkringskassan har ett samordningsansvar för rehabiliteringen. Försäkringskassan kan bland annat ta initiativ till möten med patienten och olika aktörer som kan bidra till att patienten snabbare kan återgå i arbete.',
        'fk7263.helptext.rekommendationer.resor': 'Ange om resor till och från arbetet med annat färdsätt, till exempel taxi eller automatväxlad bil, kan göra att patienten kan arbeta trots sjukdomen. Patienten kan få ersättning för annat färdsätt i tre månader, och i undantagsfall längre.',
        'fk7263.helptext.rekommendationer.arbetsformedlingen': 'Kontakt med Arbetsförmedlingen rekommenderas om du bedömer att patienten behöver hjälp via Arbetsförmedlingen för att han eller hon ska kunna återgå i arbete.',
        'fk7263.helptext.rekommendationer.foretagshalsovarden': 'Patientens arbetsgivare kan ha ett avtal om företagshälsovård. Försäkringskassan har ansvar för samordning av aktiviteter för patientens rehabilitering. Kontakt med företagshälsovården rekommenderas om: <ul><li>patienten riskerar en längre sjukskrivningsperiod (överstigande 45 dagar) eller</li><li>patientens arbetsförmåga i relation till nuvarande arbetsuppgifter behöver bedömas närmare eller</li><li>det finns skäl att anta att patientens återgång till nuvarande arbete inte är möjligt eller endast möjligt vid förändring/anpassning av dennes arbetsuppgifter</li></ul>',
        'fk7263.helptext.rekommendationer.ovrigt': 'Ange om du bedömer att det finns ytterligare någon insats som skulle kunna bidra till att patienten snabbare kan återgå i arbete.',
        'fk7263.helptext.rekommendationer.arbetslivsinriktad-rehabilitering': 'Ange om du bedömer att patienten skulle ha förutsättningar att återvinna arbetsförmågan snabbare med arbetslivsinriktad rehabilitering.',
        'fk7263.helptext.kontakt': 'Ange om du vill att Försäkringskassans handläggare tar kontakt med dig för att diskutera patientens sjukskrivning. Observera att denna ruta i första hand ska användas om intyget ska skrivas ut på papper. Överförs intyget elektroniskt används hellre funktionen för elektronisk ärendekommunikation (efter det att intyget skickats in).',
        'fk7263.helptext.ovrigt': 'Ange här sådan ytterligare information som du bedömer att Försäkringskassan skulle ha nytta av. Observera att vid granskning och utskrift av intyget kommer text som skrivits in i vissa av fritextfälten på andra ställen i intyget också att flyttas hit.',
        'fk7263.helptext.samtycke': 'Läkarintyget får bara skickas till Försäkringskassan om patienten givit sitt samtycke till det. Genom att kryssa i denna ruta intygar du att du fått patientens samtycke till att skicka intyget elektroniskt till Försäkringskassan. ',
        'fk7263.helptext.adress': 'Vårdenhetens kontaktuppgifter inhämtas från HSA. Om vårdenhetens kontaktuppgifter inte stämmer kontakta HSA för att ändra dem.',

        //Validation messages
        'fk7263.validation.forandrat-ressatt.choose-one': 'Endast ett alternativ kan anges avseende förändrat ressätt. (Fält 11)',
        'fk7263.validation.nedsattning.tjanstgoringstid': 'För att kunna beräkna arbetstid måste \'Faktisk tjänstgöringstid\' vara ett nummer (Fält 8a)',
        'fk7263.validation.nedsattning.choose-at-least-one': 'Välj minst ett alternativ för arbetsförmåga. (Fält 8b)',
        'fk7263.validation.nedsattning.nedsattmed100.incorrect-format' : 'Datum för nedsatt med 100% har angetts med felaktigt format (Fält 8b)',
        'fk7263.validation.nedsattning.nedsattmed75.incorrect-format' : 'Datum för nedsatt med 75% har angetts med felaktigt format (Fält 8b)',
        'fk7263.validation.nedsattning.nedsattmed50.incorrect-format' : 'Datum för nedsatt med 50% har angetts med felaktigt format (Fält 8b)',
        'fk7263.validation.nedsattning.nedsattmed25.incorrect-format' : 'Datum för nedsatt med 25% har angetts med felaktigt format (Fält 8b)',
        'fk7263.validation.nedsattning.overlapping-date-interval': 'Två datumintervall med överlappande datum har angetts. (Fält 8b)',
        'fk7263.validation.diagnos.missing': 'ICD-10 kod saknas på huvuddiagnosen. (Fält 2)',
        'fk7263.validation.rekommendationer.ovriga': 'Fritextfältet som hör till alternativet Övrigt måste fyllas i. (Fält 6a)'
    },
    'en': {
        'fk7263.label.pagetitle': 'Show Certificate'
    }
});
