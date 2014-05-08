/*
 * Copyright (C) 2013 Inera AB (http://www.inera.se)
 *
 * This file is part of Inera Certificate Modules (http://code.google.com/p/inera-certificate-modules).
 *
 * Inera Certificate Modules is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Inera Certificate Modules is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
define(
        [],
        function() {
            'use strict';

            return {
                'sv': {
                    'ts-diabetes.label.empty': '',
                    'ts-diabetes.label.certtitle': 'Läkarintyg Transportstyrelsen Diabetes',
                    'ts-diabetes.label.intygavser': 'Intyget avser',
                    'ts-diabetes.label.identitet': 'Identiteten är styrkt genom',
                    'ts-diabetes.label.allmant': '1. Allmänt',
                    'ts-diabetes.label.hypoglykemier': '2. Hypoglykemier (lågt blodsocker)',
                    'ts-diabetes.label.synintyg': '3. Synintyg',
                    'ts-diabetes.label.bedomning': '4. Bedömning',
                    'ts-diabetes.label.unit': 'Vårdenhet',
                    'ts-diabetes.label.korkort.am': 'AM',
                    'ts-diabetes.label.korkort.a1': 'A1',
                    'ts-diabetes.label.korkort.a2': 'A2',
                    'ts-diabetes.label.korkort.a': 'A',
                    'ts-diabetes.label.korkort.b': 'B',
                    'ts-diabetes.label.korkort.be': 'BE',
                    'ts-diabetes.label.korkort.traktor': 'Traktor',
                    'ts-diabetes.label.korkort.c1': 'C1',
                    'ts-diabetes.label.korkort.c1e': 'C1E',
                    'ts-diabetes.label.korkort.c': 'C',
                    'ts-diabetes.label.korkort.ce': 'CE',
                    'ts-diabetes.label.korkort.d1': 'D1',
                    'ts-diabetes.label.korkort.d1e': 'D1E',
                    'ts-diabetes.label.korkort.d': 'D',
                    'ts-diabetes.label.korkort.de': 'DE',
                    'ts-diabetes.label.korkort.taxi': 'Taxi',
                    'ts-diabetes.label.korkort.annat': 'Annat',

                    // Help texts
                    'ts-diabetes.helptext.lakaren-ska-uppmarksamma': 'Läkaren ska uppmärksamma Transportstyrelsens föreskrifter och allmänna råd om medicinska krav för innehav av körkort m.m. (TSFS 2010:125, senast ändrade genom TSFS 2013:2). Intyget skall utfärdas i enlighet med vad som sägs i 17 kap. och får inte vara äldre än två månader när det inkommer till Transportstyrelsen. Se: <a href="http://www.transportstyrelsen.se">http://www.transportstyrelsen.se</a>. Därefter "Väg" och "Trafikmedicin".',
                    'ts-diabetes.helptext.intyget-avser': 'AM = moped klass I, <br/> A1 = lätt motorcykel, <br/>A2 = mellanstor motorcykel, <br/> A = motorcykel, <br/>B = personbil och lätt lastbil, <br/>BE = personbil, lätt lastbil och ett eller flera släpfordon, <br/>C1 = medeltung lastbil och enbart ett lättsläpfordon, <br/>C1E = medeltung lastbil och ett eller flera släpfordon oavsett vikt, <br/>C = tung lastbil och enbart ett lätt släpfordon, <br/>CE = tung lastbil och ett eller flera släpfordon oavsett vikt, <br/>D1 = mellanstor buss, <br/>D1E = mellanstorbuss och ett eller flera släpfordon oavsett vikt, <br/>D = buss, <br/>DE = buss och enbart ett lätt släpfordon, <br/>E = tungt släpfordon, <br/>Taxi = taxiförarlegitimation <br/>',

                    'ts-diabetes.helptext.identitet-styrkt-genom': 'ID-kort = SIS-märkt ID-kort, svenskt nationellt ID-kort eller ID-kort utfärdat av Skatteverket.<br/> Företagskort eller tjänstekort = SIS-märkt företagskort eller tjänstekort.<br/> Försäkran enligt 18 kap. 4 § = Försäkran enligt 18 kap 4 § i Transportstyrelsens föreskrifter (TSFS 2010:125, senast ändrade genom TSFS 2013:2): Identiteten får fastställas genom att en förälder, annan vårdnadshavare, make, maka eller sambo, registrerad partner eller myndigt barn skriftligen försäkrar att lämnade uppgifter om sökandens identitet är riktiga. Den som lämnar en sådan försäkran ska vara närvarande vid identitetskontrollen och kunna styrka sin egen identitet.<br/> Pass = Svenskt EU-pass, annat EU-pass utfärdade från och med den 1 september 2006, pass utfärdat av Island, Liechtenstein, Norge eller Schweiz från och med den 1 september 200<br/>',

                    'ts-diabetes.helptext.synfunktioner.utan-korrektion': 'Uppgiften är obligatorisk',
                    'ts-diabetes.helptext.synfunktioner.med-korrektion': 'Uppgiften är obligatorisk om föreskriven synskärpa endast uppnås med korrektion. Definition av föreskriven synskärpa finns i Transportstyrelsens föreskrifter.',

                    // Validation messages
                    'ts-diabetes.validation.utlatande.missing': 'Utlatande saknas',

                    'ts-diabetes.validation.vardenhet.postadress.missing': 'Kunde inte hämta postadress för vårdenheten från HSA, måste ifyllas manuellt',
                    'ts-diabetes.validation.vardenhet.postnummer.missing': 'Kunde inte hämta postnummer för vårdenheten från HSA, måste ifyllas manuellt',
                    'ts-diabetes.validation.vardenhet.postnummer.incorrect-format': 'Postnummer måste anges i formatet XXX XX eller XXXXX (exempelvis 123 45)',
                    'ts-diabetes.validation.vardenhet.postort.missing': 'Kunde inte hämta postort för vårdenheten från HSA, måste ifyllas manuellt',
                    'ts-diabetes.validation.vardenhet.telefonnummer.missing': 'Kunde inte hämta telefonnummer för vårdenheten från HSA, måste ifyllas manuellt',

                    'ts-diabetes.validation.sjukhusvard.missing': 'Objektet sjukhusvård saknas',
                    'ts-diabetes.validation.sjukhusvard.sjukhusellerlakarkontakt.missing': 'Sjukhus eller läkarkontakt saknas',
                    'ts-diabetes.validation.sjukhusvard.anledning.missing': 'Anledning till sjukhusvård saknas',
                    'ts-diabetes.validation.sjukhusvard.tidpunkt.missing': 'Tidpunkt för sjukhusvård saknas',
                    'ts-diabetes.validation.sjukhusvard.vardinrattning.missing': 'Vårdinrättningens namn och klinik saknas',

                    'ts-diabetes.validation.bedomning.missing': 'Bedömning saknas',
                    'ts-diabetes.validation.bedomning.must-choose-one': 'En körkortstyp eller Kan inte ta ställning måste väljas',
                    'ts-diabetes.validation.bedomning.lamplighet-inneha-behorighet.missing': 'Ställning behöver tas till om patienten är lämpad för högre körkortsbehörighet',

                    'ts-diabetes.validation.diabetes.missing': 'Diabetes saknas',
                    'ts-diabetes.validation.diabetes.diabetestyp.missing': 'Diabetestyp måste anges',
                    'ts-diabetes.validation.diabetes.observationsperiod.missing': 'År då diabetesdiagnos ställdes måste anges',
                    'ts-diabetes.validation.diabetes.behandling.missing': 'Minst en behandling måste väljas',

                    'ts-diabetes.validation.intygavser.missing': 'Intyget avser körkortsbehörighet saknas',
                    'ts-diabetes.validation.intygavser.must-choose-one': 'Minst en körkortsbehörighet, eller Annat måste väljas',

                    'ts-diabetes.validation.syn.missing': 'Synfunktioner saknas',
                    'ts-diabetes.validation.syn.diplopi.missing': 'Förekommer dubbelseende saknas',
                    'ts-diabetes.validation.syn.provning-utan-anmarkning.missing': 'Måste anges om synfältsprövning enligt Donders konfrontationsmetod är utan anmärkning',
                    'ts-diabetes.validation.syn.separat-ogonlakarintyg.missing': 'Om ögonläkarintyg bifogas separat eller ej måste anges',
                    'ts-diabetes.validation.syn.hoger.missing': 'Synfunktioner relaterade till höger öga saknas',
                    'ts-diabetes.validation.syn.hoger.utankorrektion.missing': 'Höger öga utan korrektion saknas',
                    'ts-diabetes.validation.syn.hoger.utankorrektion.out-of-bounds': 'Korrektionsvärdet för höger öga utan korrektion måste ligga i intervallet 0.0 till 2.0',
                    'ts-diabetes.validation.syn.hoger.medkorrektion.out-of-bounds': 'Korrektionsvärdet för höger öga med korrektion måste ligga i intervallet 0.0 till 2.0',

                    'ts-diabetes.validation.syn.vanster.missing': 'Synfunktioner relaterade till vänster öga saknas',
                    'ts-diabetes.validation.syn.vanster.utankorrektion.missing': 'Vänster öga utan korrektion saknas',
                    'ts-diabetes.validation.syn.vanster.utankorrektion.out-of-bounds': 'Korrektionsvärdet för vänster öga utan korrektion måste ligga i intervallet 0.0 till 2.0',
                    'ts-diabetes.validation.syn.vanster.medkorrektion.out-of-bounds': 'Korrektionsvärdet för vänster öga med korrektion måste ligga i intervallet 0.0 till 2.0',

                    'ts-diabetes.validation.syn.binokulart.missing': 'Binokulära synfunktioner saknas',
                    'ts-diabetes.validation.syn.binokulart.utankorrektion.missing': 'Binokulärt utan korrektion saknas',
                    'ts-diabetes.validation.syn.binokulart.utankorrektion.out-of-bounds': 'Korrektionsvärdet för binokulärt utan korrektion måste ligga i intervallet 0.0 till 2.0',
                    'ts-diabetes.validation.syn.binokulart.medkorrektion.out-of-bounds': 'Korrektionsvärdet för binokulärt med korrektion måste ligga i intervallet 0.0 till 2.0',

                    'ts-diabetes.validation.identitet.missing': 'Identitet styrkt saknas',
                    'ts-diabetes.validation.vardkontakt.missing': 'Identitet styrkt saknas',

                    'ts-diabetes.validation.hypoglykemier.missing': 'Hypoglykemier saknas',
                    'ts-diabetes.validation.hypoglykemier.kunskap-om-atgarder.missing': 'Patienten har kunskap om åtgärder vid hypoglykemi saknas',
                    'ts-diabetes.validation.hypoglykemier.tecken-nedsatt-hjarnfunktion.missing': 'Förekommer hypoglykemier med tecken på nedsatt hjärnfunktion saknas',
                    'ts-diabetes.validation.hypoglykemier.saknar-formaga-kanna-varningstecken.missing': 'Saknar patienten förmåga att känna varningstecken på hypoglykemi saknas',
                    'ts-diabetes.validation.hypoglykemier.allvarlig-forekomst-trafiken.missing': 'Har patienten haft allvarlig hypoglykemi i trafiken under det senaste året saknas',
                    'ts-diabetes.validation.hypoglykemier.allvarlig-forekomst.missing': 'Har patienten haft allvarlig hypoglykemi saknas',
                    'ts-diabetes.validation.hypoglykemier.allvarlig-forekomst.beskrivning.missing': 'Antal episoder med allvarlig hypoglykemi saknas',
                    'ts-diabetes.validation.hypoglykemier.allvarlig-forekomst-trafiken.beskrivning.missing': 'Information om episoder med allvarlig hypoglykemi i trafiken saknas',
                    'ts-diabetes.validation.hypoglykemier.allvarlig-forekomst-vaken-tid.missing': 'Allvarlig hypoglykemi under vaken tid saknas',
                    'ts-diabetes.validation.hypoglykemier.allvarlig-forekomst-vaken-tid.observationstid.missing': 'Tidpunkt för allvarlig hypoglykemi under vaken tid saknas',
                    'ts-diabetes.validation.hypoglykemier.allvarlig-forekomst-vaken-tid.observationstid.incorrect-date': 'Tidpunkt för allvarlig hypoglykemi under vaken tid måste anges som åååå-mm-dd',
                    'ts-diabetes.validation.hypoglykemier.egenkontroll-blodsocker.missing': 'Genomför patienten egenkontroll av blodsocker måste anges för högre körkortsbehörigheter'

                },
                'en': {
                    'view.label.pagetitle': 'Show Certificate'
                }
            };
        });
