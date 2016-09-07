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
angular.module('ts-bas').constant('ts-bas.messages', {
    'sv': {
        'ts-bas.button.send': 'Skicka till Transportstyrelsen',
        'ts-bas.label.certtitle': 'Transportstyrelsens läkarintyg',
        'ts-bas.label.patientadress': 'Patientens adressuppgifter',
        'ts-bas.label.intygavser': 'Intyget avser',
        'ts-bas.label.identitet': 'Identiteten är styrkt genom',
        'ts-bas.label.syn': '1. Synfunktioner',
        'ts-bas.label.horselbalans': '2. Hörsel och balanssinne',
        'ts-bas.label.funktionsnedsattning': '3. Rörelseorganens funktioner',
        'ts-bas.label.hjartkarl': '4. Hjärt- och kärlsjukdomar',
        'ts-bas.label.diabetes': '5. Diabetes',
        'ts-bas.label.neurologi': '6. Neurologiska sjukdomar',
        'ts-bas.label.medvetandestorning': '7. Epilepsi, epileptiskt anfall och annan medvetandestörning',
        'ts-bas.label.njurar': '8. Njursjukdomar',
        'ts-bas.label.kognitivt': '9. Demens och andra kognitiva störningar',
        'ts-bas.label.somnvakenhet': '10. Sömn- och vakenhetsstörningar',
        'ts-bas.label.narkotikalakemedel': '11. Alkohol, narkotika och läkemedel',
        'ts-bas.label.psykiskt': '12. Psykiska sjukdomar och störningar',
        'ts-bas.label.utvecklingsstorning': '13. ADHD, autismspektrumtillstånd och likartade tillstånd samt psykisk utvecklingsstörning',
        'ts-bas.label.sjukhusvard': '14. Sjukhusvård',
        'ts-bas.label.medicinering': '15. Övrig medicinering',
        'ts-bas.label.ovrigkommentar': '16. Övrig kommentar',
        'ts-bas.label.bedomning': 'Bedömning',
        'ts-bas.label.vardenhet': 'Vårdenhet',
        'ts-bas.label.send': 'Skicka intyg till Transportstyrelsen',
        'ts-bas.label.send.body': '',
        'ts-bas.label.makulera.confirmation': 'Transportstyrelsens läkarintyg utställd på ${namn} – ${personnummer} är återtaget. Mottagare av intyget är notifierade om detta.',

        'ts-bas.label.status.sent': 'Intyget är signerat och har skickats till Transportstyrelsens system.',
        'ts-bas.label.status.recieved': 'Intyget är signerat och mottaget av Transportstyrelsens system.',
        'ts-bas.label.status.signed': 'Intyget är signerat och är nu tillgängligt för patienten i webbtjänsten Mina intyg, som nås via 1177.se. Om patienten inte har möjlighet att skicka intyget elektroniskt till Transportstyrelsen kan du skicka intyget direkt till Transportstyrelsen åt patienten. Observera att patientens samtycke då krävs.',
        'ts-bas.label.kopiera.text': 'Skapar en kopia av befintligt intyg. Det nya utkastet (kopian) kan ändras och signeras.',

        'ts-bas.helptext.intyg-avser': '<span style="text-align:left">C1 = medeltung lastbil och enbart ett lätt släpfordon<br/>C1E = medeltung lastbil och ett eller flera släpfordon oavsett vikt<br/>C = tung lastbil och enbart ett lätt släpfordon<br/>CE = tung lastbil och ett eller flera släpfordon oavsett vikt<br/>D1 = mellanstor buss och enbart ett lätt släpfordon<br/>D1E = mellanstor buss och ett eller flera släpfordon oavsett vikt<br/>D = buss och enbart ett lätt släpfordon<br/>DE = buss och ett eller flera släpfordon oavsett vikt<br/>Taxi = taxiförarlegitimation<br/>Annat = (AM,A1,A2,A,B,BE eller Traktor)</span>',

        'ts-bas.helptext.identitet': 'Identitet styrkt genom någon av nedanstående',

        'ts-bas.helptext.synfunktioner.info-8-dioptrier': 'Intyg om korrektionsglasens styrka måste skickas in.',
        'ts-bas.helptext.synfunktioner.8-dioptrier-valt': 'Du har kryssat i frågan om 8 dioptrier – Glöm inte att skicka in intyg om korrektionsglasens styrka.',
        'ts-bas.helptext.synfunktioner.utan-korrektion': 'Uppgiften är obligatorisk',
        'ts-bas.helptext.synfunktioner.med-korrektion': 'Uppgiften är obligatorisk om föreskriven synskärpa endast uppnås med korrektion. Definition av föreskriven synskärpa finns i Transportstyrelsens föreskrifter.',

        'ts-bas.helptext.bedomning.info': 'Om någon av frågorna har besvarats med ja, ska de krav på ytterligare underlag som framgår av föreskrifterna beaktas.',

        'ts-bas.helptext.diabetes.behandling': 'Vid tablett- eller insulinbehandlad diabetes krävs det att ett läkarintyg gällande sjukdomen skickas in.',
        'ts-bas.helptext.narkotika-lakemedel.provtagning': 'Om ja på ovanstående ska resultatet redovisas separat.',
        'ts-bas.helptext.bedomning': '<span style="text-align:left">C1 - medeltung lastbil och enbart ett lätt släpfordon<br/>C1E - medeltung lastbil och ett eller flera släpfordon oavsett vikt<br/>C - tung lastbil och enbart ett lätt släpfordon<br/>CE - tung lastbil och ett eller flera släpfordon oavsett vikt<br/>D1 - mellanstor buss och enbart ett lätt släpfordon<br/>D1E - mellanstor buss och ett eller flera släpfordon oavsett vikt<br/>D - buss och enbart ett lätt släpfordon<br/>DE - buss och ett eller flera släpfordon oavsett vikt<br/>Taxi = taxiförarlegitimation<br/>Taxi = taxiförarlegitimation<br/>Annat = AM,A1,A2,A,B,BE eller traktor<br/>',

        'ts-bas.label.spara-utkast': 'Spara',
        'ts-bas.label.ta-bort-utkast': 'Ta bort utkast',
        'ts-bas.label.skriv-ut-utkast': 'Skriv ut',
        'ts-bas.label.visa-kompletteras': 'Visa vad som behöver kompletteras',
        'ts-bas.label.dolj-kompletteras': 'Dölj vad som behöver kompletteras',

        // Labels for showing signed intyg
        'ts-bas.label.syn.binokulart': 'Binokulärt',
        'ts-bas.label.syn.vanster-oga': 'Vänster öga',
        'ts-bas.label.syn.hoger-oga': 'Höger öga',
        'ts-bas.label.syn.kontaktlinster': 'Kontaktlinser',
        'ts-bas.label.syn.utan-korrektion': 'Utan korrektion',
        'ts-bas.label.syn.med-korrektion': 'Med korrektion',
        'ts-bas.label.syn.korrektionsglasens-styrka': 'Korrektionsglasens styrka',

        'ts-bas.label.diabetes.diabetestyp.diabetes_typ_1': 'Typ 1',
        'ts-bas.label.diabetes.diabetestyp.diabetes_typ_2': 'Typ 2',

        'ts-bas.label.kontakt-info': 'Namn och kontaktuppgifter till vårdenheten',

        'ts-bas.label.bedomning-info-undersokas-med-specialkompetens': 'Patienten bör före ärendets avgörande undersökas av läkare med specialistkompetens i',
        'ts-bas.label.bedomning-info-ej-angivet': 'Ej angivet',
        'ts-bas.label.nagon-av-foljande-behorigheter': 'Någon av följande behörigheter',

        'ts-bas.label.kommentar-relevant-trafiksakerhet': 'Övriga kommentarer som är relevant ur trafiksäkerhetssynpunkt.',

        // Validation messages starting
        'ts-bas.validation.utlatande.missing': 'Utlatande saknas',

        'ts-bas.validation.patient.postadress.missing': 'Postadress saknas.',
        'ts-bas.validation.patient.postnummer.missing': 'Postnummer saknas.',
        'ts-bas.validation.patient.postort.missing': 'Postort saknas.',

        'ts-bas.validation.intygavser.missing': 'Intyget avser körkortsbehörighet saknas',
        'ts-bas.validation.intygavser.must-choose-one': 'Minst en körkortsbehörighet, eller "Annat", måste väljas.',

        'ts-bas.validation.identitet.missing': 'Identitet styrkt saknas',

        'ts-bas.validation.syn.missing': 'Synfunktioner saknas',
        'ts-bas.validation.syn.tecken-synfaltsdefekter.missing': 'a) Ett alternativ måste anges.',
        'ts-bas.validation.syn.nattblindhet.missing': 'b) Ett alternativ måste anges.',
        'ts-bas.validation.syn.progressiv-ogonsjukdom.missing': 'c) Ett alternativ måste anges.',
        'ts-bas.validation.syn.diplopi.missing': 'd) Ett alternativ måste anges.',
        'ts-bas.validation.syn.nystagmus.missing': 'e) Ett alternativ måste anges.',
        'ts-bas.validation.syn.hogeroga.missing': 'Synfunktioner relaterade till höger öga saknas',
        'ts-bas.validation.syn.hogeroga.utankorrektion.missing': 'f) Värden för synskärpa utan korrektion måste anges för: Höger öga.',
        'ts-bas.validation.syn.hogeroga.utankorrektion.out-of-bounds': 'Värdet för vänster öga utan korrektion måste ligga i intervallet 0,0 till 2,0.',
        'ts-bas.validation.syn.hogeroga.medkorrektion.out-of-bounds': 'Värdet för höger öga med korrektion måste ligga i intervallet 0,0 till 2,0.',

        'ts-bas.validation.syn.vansteroga.missing': 'Synfunktioner relaterade till vänster öga saknas',
        'ts-bas.validation.syn.vansteroga.utankorrektion.missing': 'f) Värden för synskärpa utan korrektion måste anges för: Vänster öga.',
        'ts-bas.validation.syn.vansteroga.utankorrektion.out-of-bounds': 'Värdet för vänster öga utan korrektion måste ligga i intervallet 0,0 till 2,0.',
        'ts-bas.validation.syn.vansteroga.medkorrektion.out-of-bounds': 'Värdet för vänster öga med korrektion måste ligga i intervallet 0,0 till 2,0.',

        'ts-bas.validation.syn.binokulart.missing': 'Binokulära synfunktioner saknas',
        'ts-bas.validation.syn.binokulart.utankorrektion.missing': 'f) Värden för synskärpa utan korrektion måste anges för: Binokulärt.',
        'ts-bas.validation.syn.binokulart.utankorrektion.out-of-bounds': 'Värdet för binokulär synskärpa utan korrektion måste ligga i intervallet 0,0 till 2,0.',
        'ts-bas.validation.syn.binokulart.medkorrektion.out-of-bounds': 'Värdet för binokulär synskärpa med korrektion måste ligga i intervallet 0,0 till 2,0.',

        'ts-bas.validation.horselbalans.missing': 'Hörsel och balanssinne saknas',
        'ts-bas.validation.horselbalans.balansrubbningar.missing': 'a) Ett alternativ måste anges.',
        'ts-bas.validation.horselbalans.uppfattasamtal4meter.missing': 'b) Ett alternativ måste anges.',

        'ts-bas.validation.funktionsnedsattning.missing': 'Funktionsnedsättning saknas',
        'ts-bas.validation.funktionsnedsattning.funktionsnedsattning.missing': 'a) Ett alternativ måste anges.',
        'ts-bas.validation.funktionsnedsattning.beskrivning.missing': 'Beskrivning av funktionsnedsättning saknas.',
        'ts-bas.validation.funktionsnedsattning.otillrackligrorelseformaga.missing': 'b) Ett alternativ måste anges.',

        'ts-bas.validation.hjartkarl.missing': 'Hjärt- och kärlsjukdomar saknas',
        'ts-bas.validation.hjartkarl.hjartkarlsjukdom.missing': 'a) Ett alternativ måste anges.',
        'ts-bas.validation.hjartkarl.hjarnskadaeftertrauma.missing': 'b) Ett alternativ måste anges.',
        'ts-bas.validation.hjartkarl.riskfaktorerstroke.missing': 'c) Ett alternativ måste anges.',
        'ts-bas.validation.hjartkarl.beskrivningriskfaktorer.missing': 'Beskrivning av riskfaktorer för stroke saknas.',

        'ts-bas.validation.diabetes.missing': 'Diabetes saknas',
        'ts-bas.validation.diabetes.hardiabetes.missing': 'Ett alternativ måste anges.',
        'ts-bas.validation.diabetes.diabetestyp.missing': 'Ett alternativ måste anges.',
        'ts-bas.validation.diabetes.diabetestyp.must-choose-one': 'Minst en behandlingstyp måste anges.',

        'ts-bas.validation.neurologi.missing': 'Neurologiska sjukdomar saknas',
        'ts-bas.validation.neurologi.neurologisksjukdom.missing': 'Ett alternativ måste anges.',

        'ts-bas.validation.medvetandestorning.missing': 'Ett alternativ måste anges.',
        'ts-bas.validation.medvetandestorning.medvetandestorning.missing': 'Ett alternativ måste anges.',

        'ts-bas.validation.njurar.missing': 'Njursjukdomar saknas',
        'ts-bas.validation.njurar.nedsattnjurfunktion.missing': 'Ett alternativ måste anges.',

        'ts-bas.validation.kognitivt.missing': 'Demens och kognitiva störningar saknas',
        'ts-bas.validation.kognitivt.sviktandekognitivfunktion.missing': 'Ett alternativ måste anges.',

        'ts-bas.validation.somnvakenhet.missing': 'Sömn- och vakenhetsstörningar saknas',
        'ts-bas.validation.somnvakenhet.teckensomnstorningar.missing': 'Ett alternativ måste anges.',

        'ts-bas.validation.narkotikalakemedel.missing': 'Alkohol, narkotika och läkemedel saknas',
        'ts-bas.validation.narkotikalakemedel.teckenmissbruk.missing': 'a) Ett alternativ måste anges.',
        'ts-bas.validation.narkotikalakemedel.vardinsats-bas.missing': ' b)	Ett alternativ måste anges.',
        'ts-bas.validation.narkotikalakemedel.provtagning-behovs.missing': 'Om 11a) eller 11b) besvarats med Ja så måste ett alternativ anges för följdfrågan.',
        'ts-bas.validation.narkotikalakemedel.lakarordineratlakemedelsbruk.missing': 'c) Ett alternativ måste anges.',
        'ts-bas.validation.narkotikalakemedel.lakemedelochdos.missing': 'Läkemedel och dos måste anges.',

        'ts-bas.validation.psykiskt.missing': 'Psykiska sjukdomar och störningar saknas',
        'ts-bas.validation.psykiskt.psykisksjukdom.missing': 'Ett alternativ måste anges.',

        'ts-bas.validation.utvecklingsstorning.missing': 'ADHD, autismspektrumtillstånd och likartade tillstånd samt psykisk utvecklingsstörning måste anges',
        'ts-bas.validation.utvecklingsstorning.harsyndrom.missing': 'a) Ett alternativ måste anges.',
        'ts-bas.validation.utvecklingsstorning.psykiskutvecklingsstorning.missing': 'b) Ett alternativ måste anges.',

        'ts-bas.validation.sjukhusvard.missing': 'Objektet sjukhusvård saknas',
        'ts-bas.validation.sjukhusvard.sjukhusellerlakarkontakt.missing': 'Ett alternativ måste anges.',
        'ts-bas.validation.sjukhusvard.anledning.missing': 'Anledning till sjukhusvård måste anges.',
        'ts-bas.validation.sjukhusvard.tidpunkt.missing': 'Angivelse av tidpunkt måste anges.',
        'ts-bas.validation.sjukhusvard.vardinrattning.missing': 'Vårdinrättningens namn och klinik måste anges.',

        'ts-bas.validation.medicinering.missing': 'Övrig medicinering saknas',
        'ts-bas.validation.medicinering.stadigvarandemedicinering.missing': 'Ett alternativ måste anges.',
        'ts-bas.validation.medicinering.beskrivning.missing': 'Beskrivning av medicinering saknas.',

        'ts-bas.validation.bedomning.missing': 'Bedömning saknas',
        'ts-bas.validation.bedomning.must-choose-one': 'Minst en behörighet, "Annat" eller "Kan inte ta ställning" måste anges.',

        'ts-bas.validation.vardenhet.postadress.missing': 'Postadressen för vårdenheten kunde inte hämtas från HSA, den måste anges manuellt.',
        'ts-bas.validation.vardenhet.postnummer.missing': 'Postnummer för vårdenheten kunde inte hämtas från HSA, den måste anges manuellt.',
        'ts-bas.validation.vardenhet.postnummer.incorrect-format': 'Postnummer måste anges i formatet XXX XX eller XXXXX (exempelvis 123 45)',
        'ts-bas.validation.vardenhet.postort.missing': 'Postost för vårdenheten kunde inte hämtas från HSA, den måste anges manuellt.',
        'ts-bas.validation.vardenhet.telefonnummer.missing': 'Telefonnummer till vårdenheten kunde inte hämtas från HSA, den måste anges manuellt.',

        'ts-bas.form.postadress': 'Postadress',
        'ts-bas.form.postnummer': 'Postnummer',
        'ts-bas.form.postort': 'Postort',
        'ts-bas.form.telefonnummer': 'Telefonnummer',
        'ts-bas.form.epost': 'Epost',

        'ts-bas.label.specialkompetens': 'Specialistkompetens',
        'ts-bas.label.befattningar': 'Befattningar',
        'ts-bas.label.signera': 'Signera'
    },
    'en': {
        'ts-bas.label.pagetitle': 'Show Certificate'
    }
});
