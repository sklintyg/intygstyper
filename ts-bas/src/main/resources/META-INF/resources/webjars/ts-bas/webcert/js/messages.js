/* jshint maxlen: false, unused: false */
var tsBasMessages = {
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
        'ts-bas.label.unit': 'Vårdenhet',
        'ts-bas.label.send': 'Skicka intyg till Transportstyrelsen',
        'ts-bas.label.send.body': '',
        'ts-bas.label.makulera.confirmation': 'Transportstyrelsens läkarintyg utställd på ${namn} – ${personnummer} är återtaget. Mottagare av intyget är notifierade om detta.',

        'ts-bas.label.status.sent': 'Intyget är signerat och mottaget av Transportstyrelsens system.',
        'ts-bas.label.status.signed': 'Intyget är signerat och är nu tillgängligt för patienten i webbtjänsten Mina intyg, som nås via 1177.se. Om patienten inte har möjlighet att skicka intyget elektroniskt till Transportstyrelsen kan du skicka intyget direkt till Transportstyrelsen åt patienten. Observera att patientens samtycke då krävs.',
        'ts-bas.label.kopiera.text': 'Kopiera intyg innebär att en kopia skapas av det befintliga intyget och med samma information. I de fall patienten har ändrat namn eller adress så uppdateras den informationen. Uppgifterna i intygsutkastet går att ändra innan det signeras.',

        // Identitet styrkt genom
        'ts-bas.label.identitet.id_kort': 'ID-kort',
        'ts-bas.label.identitet.foretag_eller_tjanstekort': 'Företagskort eller tjänstekort.',
        'ts-bas.label.identitet.korkort': 'Svenskt körkort',
        'ts-bas.label.identitet.pers_kannedom': 'Personlig kännedom',
        'ts-bas.label.identitet.forsakran_kap18': 'Försäkran enligt 18 kap 4 §',
        'ts-bas.label.identitet.pass': 'Pass',
        'ts-bas.helptext.identitet-styrkt-genom.id-kort': '* SIS-märkt ID-kort, svenskt nationellt ID-kort eller ID-kort utfärdat av Skatteverket.',
        'ts-bas.helptext.identitet-styrkt-genom.foretags-tjanstekort': '** SIS-märkt företagskort eller tjänstekort.',
        'ts-bas.helptext.identitet-styrkt-genom.forsakran-enl18kap4': '*** Försäkran enligt 18 kap 4 § i Transportstyrelsens föreskrifter (TSFS 2010:125, senast ändrade genom TSFS 2013:2): Identiteten får fastställas genom att en förälder, annan vårdnadshavare, make, maka eller sambo, registrerad partner eller myndigt barn skriftligen försäkrar att lämnade uppgifter om sökandens identitet är riktiga. Den som lämnar en sådan försäkran ska vara närvarande vid identitetskontrollen och kunna styrka sin egen identitet.',
        'ts-bas.helptext.identitet-styrkt-genom.pass': '**** Svenskt EU-pass, annat EU-pass utfärdade från och med den 1 september 2006, pass utfärdat av Island, Liechtenstein, Norge eller Schweiz från och med den 1 september 2006.',

        //Körkortsbehörigheter
        'ts-bas.label.korkort.c1': 'C1',
        'ts-bas.label.korkort.c1e': 'C1E',
        'ts-bas.label.korkort.c': 'C',
        'ts-bas.label.korkort.ce': 'CE',
        'ts-bas.label.korkort.d1': 'D1',
        'ts-bas.label.korkort.d1e': 'D1E',
        'ts-bas.label.korkort.d': 'D',
        'ts-bas.label.korkort.de': 'DE',
        'ts-bas.label.korkort.taxi': 'Taxi',
        'ts-bas.label.korkort.annat': 'Annat',

        'ts-bas.helptext.lakaren-ska-uppmarksamma': 'Läkaren ska uppmärksamma Transportstyrelsens föreskrifter och allmänna råd om medicinska krav för innehav av körkort m.m. (TSFS 2010:125, senast ändrade genom TSFS 2013:2). Intyget ska vara utfärdat tidigast 2 månader före att ansökan kom in till Transportstyrelsen och i enlighet med vad som sägs i 17 kapitlet.<br/>Se: <a href="http://www.transportstyrelsen.se" target="_blank">http://www.transportstyrelsen.se</a>.',

        'ts-bas.helptext.intyg-avser': '<span style="text-align:left">C1 = medeltung lastbil och enbart ett lätt släpfordon<br/>C1E = medeltung lastbil och ett eller flera släpfordon oavsett vikt<br/>C = tung lastbil och enbart ett lätt släpfordon<br/>CE = tung lastbil och ett eller flera släpfordon oavsett vikt<br/>D1 = mellanstor buss och enbart ett lätt släpfordon<br/>D1E = mellanstor buss och ett eller flera släpfordon oavsett vikt<br/>D = buss och enbart ett lätt släpfordon<br/>DE = buss och ett eller flera släpfordon oavsett vikt<br/>Taxi = taxiförarlegitimation<br/>Annat = (AM,A1,A2,A,B,BE eller Traktor)</span>',
        'ts-bas.helptext.intyg-avser.grupp2-grupp3': 'Grupp II omfattar behörigheterna AM, A1, A2, A, B, BE, C1, C1E, C och CE.<br/> Grupp III omfattar behörigheterna AM, A1, A2, A, B, BE, C1, C1E, C, CE, D1, D1E, D och DE.',

        'ts-bas.helptext.identitet': 'Identitet styrkt genom någon av nedanstående',

        'ts-bas.helptext.synfunktioner.om-nagon-av-a-c': 'Om någon av frågorna a-c besvaras med ja eller om det bedöms sannolikt att synfältsdefekter föreligger krävs läkarintyg av ögonspecialist.',
        'ts-bas.helptext.synfunktioner.synskarpa': 'Alla bokstäver ska kunna läsas på den rad som anger synskärpa. Är synskärpan sämre än 0,1 ska den anges som 0,0<br/><br/> OBS! Uppgifterna om synskärpa och korrektion kan grundas på tidigare utförd undersökning av bland annat legitimerad optiker. Uppgifterna ska då ingå som underlag vid läkarens samlade bedömning.',
        'ts-bas.helptext.synfunktioner.info-8-dioptrier': 'Intyg om korrektionsglasens styrka måste skickas in.',
        'ts-bas.helptext.synfunktioner.8-dioptrier-valt': 'Du har kryssat i frågan om 8 dioptrier – Glöm inte att skicka in intyg om korrektionsglasens styrka.',
        'ts-bas.helptext.synfunktioner.utan-korrektion': 'Uppgiften är obligatorisk',
        'ts-bas.helptext.synfunktioner.med-korrektion': 'Uppgiften är obligatorisk om föreskriven synskärpa endast uppnås med korrektion. Definition av föreskriven synskärpa finns i Transportstyrelsens föreskrifter.',

        'ts-bas.helptext.diabetes.tabletter-eller-insulin': 'Har patienten tablett- eller insulinbehandlad diabetes krävs ett läkarintyg gällande sjukdomen.',

        'ts-bas.helptext.alkohol-narkotika.provtagning': 'Om provtagning görs ska resultatet redovisas separat.',

        'ts-bas.helptext.bedomning.info': 'Om någon av frågorna har besvarats med ja, ska de krav på ytterligare underlag som framgår av föreskrifterna beaktas.',

        'ts-bas.helptext.synfunktioner.a-c': 'Då någon av a - c besvarats med JA krävs ett läkarintyg från ögonspecialist.',
        'ts-bas.helptext.synfunktioner.8-dioptrier': 'Då högsta styrka i något av glasen överskrider 8 dioptrier besvarats med JA måste Intyg om korrektionsglasens styrka skickas in.',
        'ts-bas.helptext.diabetes.behandling': 'Vid tablett- eller insulinbehandlad diabetes krävs det att ett läkarintyg gällande sjukdomen skickas in.',
        'ts-bas.helptext.narkotika-lakemedel.provtagning': 'Om ja på ovanstående ska resultatet redovisas separat.',
        'ts-bas.helptext.bedomning': '<span style="text-align:left">C1 - medeltung lastbil och enbart ett lätt släpfordon<br/>C1E - medeltung lastbil och ett eller flera släpfordon oavsett vikt<br/>C - tung lastbil och enbart ett lätt släpfordon<br/>CE - tung lastbil och ett eller flera släpfordon oavsett vikt<br/>D1 - mellanstor buss och enbart ett lätt släpfordon<br/>D1E - mellanstor buss och ett eller flera släpfordon oavsett vikt<br/>D - buss och enbart ett lätt släpfordon<br/>DE - buss och ett eller flera släpfordon oavsett vikt<br/>Taxi = taxiförarlegitimation<br/>Taxi = taxiförarlegitimation<br/>Annat = AM,A1,A2,A,B,BE eller traktor<br/>',

        'ts-bas.label.spara-utkast': 'Spara',
        'ts-bas.label.ta-bort-utkast': 'Ta bort utkast',
        'ts-bas.label.skriv-ut-utkast': 'Skriv ut',
        'ts-bas.label.visa-kompletteras': 'Visa vad som behöver kompletteras',
        'ts-bas.label.dolj-kompletteras': 'Dölj vad som behöver kompletteras',

        'ts-bas.text.information-om-intyg': 'Detta intyg ska användas vid förlängd giltighet av högre behörighet, ansökan om körkortstillstånd för grupp II och III och vid ansökan om taxiförarlegitimation.',
        'ts-bas.text.information-om-begart-lakarintyg': 'Intyget kan också användas när Transportstyrelsen i andra fall begärt ett läkarintyg. Markera då endast i rutan Annat nedan.',

        // Labels for showing signed intyg
        'ts-bas.label.syn.binokulart': 'Binokulärt',
        'ts-bas.label.syn.vanster-oga': 'Vänster öga',
        'ts-bas.label.syn.hoger-oga': 'Höger öga',
        'ts-bas.label.syn.kontaktlinster': 'Kontaktlinser',
        'ts-bas.label.syn.utan-korrektion': 'Utan korrektion',
        'ts-bas.label.syn.med-korrektion': 'Med korrektion',
        'ts-bas.label.syn.8-dioptrier': 'Högsta styrka i något av glasen överskrider plus 8 dioptrier:',
        'ts-bas.label.syn.korrektionsglasens-styrka': 'Korrektionsglasens styrka',
        'ts-bas.label.syn.synfaltsdefekter': 'a) Finns tecken på synfältsdefekter vid undersökning enligt Donders konfrontationsmetod?',
        'ts-bas.label.syn.nattblindhet': 'b) Framkommer anamnestiska uppgifter om begränsning av seendet vid nedsatt belysning?',
        'ts-bas.label.syn.progressivogonsjukdom': ' c) Har patienten någon progressiv ögonsjukdom?',
        'ts-bas.label.syn.diplopi': 'd) Framkommer dubbelseende vid prövning av ögats rörlighet (prövningen ska göras i de åtta huvudmeridianerna)?',
        'ts-bas.label.syn.nystagmus': 'e) Förekommer nystagmus?',
        'ts-bas.label.syn.synskarpa': 'f) Värden för synskärpa',



        'ts-bas.label.horselbalans.balansrubbningar': 'a) Har patienten överraskande anfall av balansrubbningar eller yrsel?',
        'ts-bas.label.horselbalans.uppfatta-vanlig-samtalsstamma': 'b) Har patienten svårt att uppfatta vanlig samtalsstämma på fyra meters avstånd (hörapparat får användas)?',
        'ts-bas.label.funktionsnedsattning.funktionsnedsattning': 'a) Har patienten någon sjukdom eller funktionsnedsättning som påverkar rörligheten och som medför att fordon inte kan köras på ett trafiksäkert sätt?',
        'ts-bas.label.funktionsnedsattning.beskrivning': 'Ange vilken typ av nedsättning eller sjukdom:',
        'ts-bas.label.view.funktionsnedsattning.beskrivning': 'Typ av nedsättning eller sjukdom:',
        'ts-bas.label.funktionsnedsattning.rorelsformaga': 'b) Är rörelseförmågan otillräcklig för att kunna hjälpa passagerare in och ut ur fordonet samt med bilbälte?',

        'ts-bas.label.hjartkarl.hjart-karlsjukdom': 'a) Föreligger hjärt- eller kärlsjukdom som kan medföra en påtaglig risk för att hjärnans funktioner akut försämras eller som i övrigt innebär en trafiksäkerhetsrisk?',
        'ts-bas.label.hjartkarl.hjarnskada-efter-trauma': 'b) Finns tecken på hjärnskada efter trauma, stroke eller annan sjukdom i centrala nervsystemet?',
        'ts-bas.label.hjartkarl.riskfaktorer-stroke': 'c) Föreligger viktiga riskfaktorer för stroke (tidigare stroke eller TIA, förhöjt blodtryck, förmaksflimmer eller kärlmissbildning)?',
        'ts-bas.label.hjartkarl.riskfaktorer-stroke.beskrivning': 'Typ av sjukdom:',

        'ts-bas.label.diabetes.har-diabetes': 'Har patienten diabetes?',
        'ts-bas.label.diabetes.diabetestyp': 'Vilken typ?',
        'ts-bas.label.diabetes.diabetestyp.diabetes_typ_1': 'Typ 1',
        'ts-bas.label.diabetes.diabetestyp.diabetes_typ_2': 'Typ 2',
        'ts-bas.label.diabetes.behandling': 'Ange behandling:',
        'ts-bas.label.diabetes.view.behandling': 'Behandling:',
        'ts-bas.label.diabetes.behandling.kost': 'Kost',
        'ts-bas.label.diabetes.behandling.tabletter': 'Tabletter',
        'ts-bas.label.diabetes.behandling.insulin': 'Insulin',

        'ts-bas.label.neurologi.neurologisksjukdom': 'Finns tecken på neurologisk sjukdom?',

        'ts-bas.label.medvetandestorning.medvetandestorning': 'Har eller har patienten haft epilepsi, epileptiskt anfall eller annan medvetandestörning?',
        'ts-bas.label.medvetandestorning.medvetandestorning-ange-datum-orsak': 'Om det är frågan om annan medvetandestörning, ange när den inträffade och orsak:',
        'ts-bas.label.medvetandestorning.beskrivning': 'Om ja. Information om när den inträffade och orsak:',

        'ts-bas.label.njurar.nedsatt-njurfunktion': 'Föreligger allvarligt nedsatt njurfunktion som kan innebära en trafiksäkerhetsrisk?',

        'ts-bas.label.kognitivt.sviktande-kognitiv-funktion': 'Finns tecken på sviktande kognitiv funktion?',

        'ts-bas.label.somn-vakenhet.tecken-somnstorningar': 'Finns tecken på, eller anamnestiska uppgifter som talar för sömn- eller vakenhetsstörning?',

        'ts-bas.label.narkotika-lakemedel.tecken-missbruk': 'a) Finns journaluppgifter, anamnestiska uppgifter, resultat av laboratorieprover eller andra tecken på missbruk eller beroende av alkohol, narkotika eller läkemedel?',
        'ts-bas.label.narkotika-lakemedel.foremal-for-vardinsats': 'b) Har patienten vid något tillfälle varit föremål för vårdinsatser för missbruk eller beroende av alkohol, narkotika eller läkemedel?',
        'ts-bas.label.narkotika-lakemedel.behov-provtagning': 'Om någon av frågorna 11a) eller 11b) besvaras med ja, behövs det provtagning avseende aktuellt bruk av alkohol eller narkotika?',
        'ts-bas.label.narkotika-lakemedel.lakarordinerat-lakemedelsbruk': 'c) Pågår regelbundet läkarordinerat bruk av läkemedel som kan innebära en trafiksäkerhetsrisk?',
        'ts-bas.label.narkotika-lakemedel.ange-lakemedel-dos': 'Ange läkemedel och ordinerad dos:',
        'ts-bas.label.narkotika-lakemedel.beskrivning': 'Om JA på C. Information om läkemedel och ordinerad dos:',

        'ts-bas.label.psykiskt.psykisksjukdom': 'Har eller har patienten haft psykisk sjukdom eller störning, till exempel schizofreni, annan psykos eller bipolär (manodepressiv) sjukdom?',

        'ts-bas.label.utvecklingsstorning.psykisk-utvecklingsstorning': 'a) Har patienten någon psykisk utvecklingsstörning?',
        'ts-bas.label.utvecklingsstorning.har-syndrom': 'b) Har patienten till exempel ADHD, ADD, DAMP, Aspergers syndrom eller Tourettes syndrom?',

        'ts-bas.label.sjukhusvard.sjukhus-eller-lakarkontakt': 'Har patienten vårdats på sjukhus eller haft kontakt med läkare med anledning av fälten 1-13?',
        'ts-bas.label.sjukhusvard.vardinrattning': 'Vårdinrättningens namn och klinik/er:',
        'ts-bas.label.sjukhusvard.anledning': 'För vad?',
        'ts-bas.label.sjukhusvard.tidpunkt': 'Om frågan besvarats med ja, när?',

        'ts-bas.label.medicinering.stadigvarande-medicinering': 'Har patienten någon stadigvarande medicinering?',
        'ts-bas.label.medicinering.beskrivning': 'Vilken eller vilka mediciner?',

        'ts-bas.label.kontakt-info': 'Namnförtydligande, mottagningens adress och telefon',

        'ts-bas.label.bedomning-info-alt-1': 'Patienten uppfyller kraven enligt Transportstyrelsens föreskrifter och allmänna råd om medicinska krav för innehav av körkort m.m. (TSFS 2010:125, senast ändrade genom TSFS 2013:2) för:',
        'ts-bas.label.bedomning.kan-inte-ta-stallning': 'Kan inte ta ställning',
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
        'ts-bas.validation.narkotikalakemedel.provtagning-behovs.missing': 'Om tecken på missbruk föreligger måste ett alternativ anges för följdfrågan.',
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

        'ts-bas.validation.vardenhet.postadress.missing': 'Kunde inte hämta postadress för vårdenheten från HSA, måste ifyllas manuellt',
        'ts-bas.validation.vardenhet.postnummer.missing': 'Kunde inte hämta postnummer för vårdenheten från HSA, måste ifyllas manuellt',
        'ts-bas.validation.vardenhet.postnummer.incorrect-format': 'Postnummer måste anges i formatet XXX XX eller XXXXX (exempelvis 123 45)',
        'ts-bas.validation.vardenhet.postort.missing': 'Kunde inte hämta postort för vårdenheten från HSA, måste ifyllas manuellt',
        'ts-bas.validation.vardenhet.telefonnummer.missing': 'Kunde inte hämta telefonnummer för vårdenheten från HSA, måste ifyllas manuellt',

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
};
