'use strict';

/* Controllers */
angular.module('wc.fk7263.controllers', []);

/*
 * EditCertCtrl - Controller for logic related to editing a certificate
 */
angular.module('wc.fk7263.controllers').controller('EditCertCtrl', [ '$scope', '$filter', '$location', '$rootScope', '$timeout', function EditCertCtrl($scope, $filter, $location, $rootScope, $timeout) {

    // init state
    $scope.widgetState = {
        doneLoading : false,
        hasError : false,
    }
    
    $scope.diagnoseState = {
        basedOnCheck : [false,false,false,false],
        dpState0 : { open : false },
        dpState1 : { open : false },
        dpState2 : { open : false },
        dpState3 : { open : false }
    }
    
    $scope.inputLimits = {
    		diagnosBeskrivning : 100, 
    		sjukdomsforlopp : 270, 
    		funktionsnedsattning : 560, 
    		aktivitetsbegransning : 1100, 
    		nuvarandeArbetsuppgifter : 120,
    		arbetsformagaPrognos : 600,
    		atgardInomSjukvarden : 66,
    		annanAtgard : 66,
    		ovrigt : 420 // 420 = combined field 13 (and dependencies) limit
    }; 
    
    $scope.widgetState.doneLoading = true;
    $scope.today = new Date();
    
    $scope.helpTooltip = "Ange vad uppgifterna i intyget baseras på. Flera alternativ kan väljas.<br><br> <a href='#'>Öppna hjälpen</a>";
    
        
    $scope.autoEnterDate = function(index) {
    	if($scope.widgetState.basedOnCheck[index]){
    		if(index == 0 && $scope.cert.undersokningAvPatienten == ""){ $scope.cert.undersokningAvPatienten = $scope.today;}
    		else if(index == 1 && $scope.cert.telefonkontaktMedPatienten == ""){	$scope.cert.telefonkontaktMedPatienten = $scope.today;}
    		else if(index == 2 && $scope.cert.journaluppgifter == ""){$scope.cert.journaluppgifter = $scope.today;}
    		else if(index == 3 && $scope.cert.annanReferens == ""){$scope.cert.annanReferens = $scope.today;}
    	}
    	else {
    		if(index == 0) { $scope.cert.undersokningAvPatienten = ""; }
    		else if(index == 1) {	$scope.cert.telefonkontaktMedPatienten = ""; }
    		else if(index == 2) { $scope.cert.journaluppgifter = ""; }
    		else if(index == 3) { $scope.cert.annanReferens = ""; }
  		}
    }
        
    $scope.updateCheck = function(index) {
    	$scope.widgetState.basedOnCheck[index]=true;
    }
    
    $scope.toggleDatePickerInstance = function(instance) {
      $timeout(function() {
          	instance.open = !instance.open;
      });
    }

    $scope.diagnose_codes = [
      {value:"J44.0", label:"J44.0 Kroniskt obstruktiv lungsjukdom med akut nedre luftvägsinfektion"},
      {value:"K92.2", label:"K92.2 Gastrointestinal blödning, ospecificerad"}
    ];

    $scope.diagnoses = [
      {value:"Kroniskt obstruktiv lungsjukdom med akut nedre luftvägsinfektion", label:"J44.0 Kroniskt obstruktiv lungsjukdom med akut nedre luftvägsinfektion"},
      {value:"Gastrointestinal blödning, ospecificerad", label:"K92.2 Gastrointestinal blödning, ospecificerad"}
    ];
    
    $scope.cert = {
    		otherData : {
    			samsjuklighet : false
    		},
    		workTime : '',
      	"id":"intyg-1","giltighet":{"from":"2011-01-26","tom":"2011-05-31"},"skickatDatum":"2011-03-23T09:29:15.000","patientNamn":"Test Testorsson stubbe",
  			"patientPersonnummer":"19121212-1212","avstangningSmittskydd":false,"diagnosKod":"S47","diagnosBeskrivning":"Medicinskttillstånd: Klämskada på överarm",
  			"sjukdomsforlopp":"Bedömttillstånd: Patienten klämde höger överarm vid olycka i hemmet. Problemen har pågått en längre tid.",
  			"funktionsnedsattning":"Funktionstillstånd-Kroppsfunktion: Kraftigt nedsatt rörlighet i överarmen pga skadan. Böj- och sträckförmågan är mycket dålig. Smärtar vid rörelse vilket ger att patienten inte kan använda armen särkilt mycket.",
  			"undersokningAvPatienten":"","telefonkontaktMedPatienten":"","journaluppgifter":"","annanReferens":"",
  			"aktivitetsbegransning":"Funktionstillstånd-Aktivitet: Patienten bör/kan inte använda armen förrän skadan läkt. Skadan förvärras vid för tidigt påtvingad belastning. Patienten kan inte lyfta armen utan den ska hållas riktad nedåt och i fast läge så mycket som möjligt under tiden för läkning.",
  			"rekommendationKontaktArbetsformedlingen":true,"rekommendationKontaktForetagshalsovarden":true,"rekommendationOvrigt":"När skadan förbättrats rekommenderas muskeluppbyggande sjukgymnastik","atgardInomSjukvarden":"Utreds om operation är nödvändig",
  			"annanAtgard":"Patienten ansvarar för att armen hålls i stillhet","rehabiliteringAktuell":false,"rehabiliteringEjAktuell":false,"rehabiliteringGarInteAttBedoma":true,
  			"nuvarandeArbetsuppgifter":"Dirigent. Dirigerar en större orkester på deltid","arbetsloshet":true,"foraldrarledighet":true,
  			"nedsattMed25":{"start":"2011-04-01","end":"2011-05-31"},"nedsattMed50":{"start":"2011-03-07","end":"2011-03-31"},"nedsattMed75":{"start":"2011-02-14","end":"2011-03-06"},"nedsattMed100":{"start":"2011-01-26","end":"2011-02-13"},
  			"arbetsformagaPrognos":"Arbetsförmåga: Skadan har förvärrats vid varje tillfälle patienten använt armen. Måste hållas i total stillhet tills läkningsprocessen kommit en bit på väg. Eventuellt kan utredning visa att operation är nödvändig för att läka skadan.",
  			"arbetsformataPrognosJa":false,"arbetsformataPrognosJaDelvis":false,"arbetsformataPrognosNej":false,"arbetsformataPrognosGarInteAttBedoma":true,"ressattTillArbeteAktuellt":false,"ressattTillArbeteEjAktuellt":true,"kontaktMedFk":true,
  			"kommentar":"Prognosen för patienten är god. Han kommer att kunna återgå till sitt arbete efter genomförd behandling.",
  			"signeringsdatum":"2011-01-26T00:00:00.000",
  			"vardperson":{"hsaId":"Personal HSA-ID","namn":"En Läkare","enhetsId":"centrum-vast","arbetsplatsKod":"123456789011","enhetsnamn":"Centrum väst","postadress":"Lasarettsvägen 13","postnummer":"85150","postort":"Sundsvall","telefonnummer":"060-1818000","epost":"kirmott@vardenhet.se","vardgivarId":"VardgivarId","vardgivarnamn":"Landstinget Norrland"},
  			"forskrivarkodOchArbetsplatskod":"123456789011","namnfortydligandeOchAdress":"En Läkare\nCentrum väst\nLasarettsvägen 13\n85150 Sundsvall\n060-1818000"
      };

      /*$scope.cert = {"id":"intyg-1","giltighet":{"from":"2011-01-26","tom":"2011-05-31"},"skickatDatum":"2011-03-23T09:29:15.000","patientNamn":"Test Testorsson stubbe",
      		"patientPersonnummer":"19121212-1212","avstangningSmittskydd":false,"diagnosKod":"S47","diagnosBeskrivning":"Medicinskttillstånd: Klämskada på överarm",
      		"sjukdomsforlopp":"Bedömttillstånd: Patienten klämde höger överarm vid olycka i hemmet. Problemen har pågått en längre tid.",
      		"funktionsnedsattning":"Funktionstillstånd-Kroppsfunktion: Kraftigt nedsatt rörlighet i överarmen pga skadan. Böj- och sträckförmågan är mycket dålig. Smärtar vid rörelse vilket ger att patienten inte kan använda armen särkilt mycket.",
      		"undersokningAvPatienten":"2011-01-26","telefonkontaktMedPatienten":"2011-01-12","journaluppgifter":"2010-01-14","annanReferens":"2010-01-24",
      		"aktivitetsbegransning":"Funktionstillstånd-Aktivitet: Patienten bör/kan inte använda armen förrän skadan läkt. Skadan förvärras vid för tidigt påtvingad belastning. Patienten kan inte lyfta armen utan den ska hållas riktad nedåt och i fast läge så mycket som möjligt under tiden för läkning.",
      		"rekommendationKontaktArbetsformedlingen":true,"rekommendationKontaktForetagshalsovarden":true,"rekommendationOvrigt":"När skadan förbättrats rekommenderas muskeluppbyggande sjukgymnastik","atgardInomSjukvarden":"Utreds om operation är nödvändig",
      		"annanAtgard":"Patienten ansvarar för att armen hålls i stillhet","rehabiliteringAktuell":false,"rehabiliteringEjAktuell":false,"rehabiliteringGarInteAttBedoma":true,
      		"nuvarandeArbetsuppgifter":"Dirigent. Dirigerar en större orkester på deltid","arbetsloshet":true,"foraldrarledighet":true,
      		"nedsattMed25":{"start":"2011-04-01","end":"2011-05-31"},"nedsattMed50":{"start":"2011-03-07","end":"2011-03-31"},"nedsattMed75":{"start":"2011-02-14","end":"2011-03-06"},"nedsattMed100":{"start":"2011-01-26","end":"2011-02-13"},
      		"arbetsformagaPrognos":"Arbetsförmåga: Skadan har förvärrats vid varje tillfälle patienten använt armen. Måste hållas i total stillhet tills läkningsprocessen kommit en bit på väg. Eventuellt kan utredning visa att operation är nödvändig för att läka skadan.",
      		"arbetsformataPrognosJa":false,"arbetsformataPrognosJaDelvis":false,"arbetsformataPrognosNej":false,"arbetsformataPrognosGarInteAttBedoma":true,"ressattTillArbeteAktuellt":false,"ressattTillArbeteEjAktuellt":true,"kontaktMedFk":true,
      		"kommentar":"Prognosen för patienten är god. Han kommer att kunna återgå till sitt arbete efter genomförd behandling.",
      		"signeringsdatum":"2011-01-26T00:00:00.000",
      		"vardperson":{"hsaId":"Personal HSA-ID","namn":"En Läkare","enhetsId":"centrum-vast","arbetsplatsKod":"123456789011","enhetsnamn":"Centrum väst","postadress":"Lasarettsvägen 13","postnummer":"85150","postort":"Sundsvall","telefonnummer":"060-1818000","epost":"kirmott@vardenhet.se","vardgivarId":"VardgivarId","vardgivarnamn":"Landstinget Norrland"},
      		"forskrivarkodOchArbetsplatskod":"123456789011","namnfortydligandeOchAdress":"En Läkare\nCentrum väst\nLasarettsvägen 13\n85150 Sundsvall\n060-1818000"
      };*/
    
  /*J44.0	Kroniskt obstruktiv lungsjukdom med akut nedre luftvägsinfektion 
    K92.2	Gastrointestinal blödning, ospecificerad 
    R06.0	Dyspné 
    G47.3	Sömnapnésyndrom
    E11.9	Diabetes mellitus typ 2 utan (uppgift om) komplikationer
    T81.4	Infektion efter kirurgiska och medicinska ingrepp som ej klassificeras annorstädes 
    E10.9	Diabetes mellitus typ 1 utan (uppgift om) komplikationer
    J35.0	Kronisk tonsillit 
    M54.5	Lumbago 
    Z03.8F	Observation/utredning för misstänkt diabetes
    N81.1	Cystocele hos kvinna 
    M48.5	Kotkompression som ej klassificeras annorstädes 
    T84.0	Mekanisk komplikation av inre ledprotes
    T84.0B	Mekanisk komplikation av inre ledprotes i axelled
    T84.0C	Mekanisk komplikation av inre ledprotes i armbåge
    T84.0D	Mekanisk komplikation av inre ledprotes i handled/hand
    T84.0F	Mekanisk komplikation av inre ledprotes i höftled
    T84.0G	Mekanisk komplikation av inre ledprotes i knäled
    T84.0H	Mekanisk komplikation av inre ledprotes i fotled/fot
    T84.0X	Mekanisk komplikation av inre ledprotes med annan eller icke specificerad lokalisation
    M54.4	Lumbago med ischias 
    T81.0	Blödning och hematom som komplikation till kirurgiska och medicinska ingrepp som ej klassificeras annorstädes 
    R41.0	Desorientering, ospecificerad 
    F20.0	Paranoid schizofreni 
    N83.2	Andra och icke specificerade ovarialcystor 
    S30.0	Kontusion på nedre delen av ryggen och bäckenet
    I95.1	Ortostatisk hypotoni 
    J18.0	Bronkopneumoni, ospecificerad 
    K61.0	Analabscess 
    C90.0	Multipelt myelom
    C18.7	Malign tumör i sigmoideum 
    K92.1	Melena 
    N81.2	Inkomplett uterovaginal prolaps 
    K57.2	Divertikel i tjocktarmen med perforation och abscess 
    N81.6	Rektocele 
    K62.5	Blödning i anus och rektum 
    N30.0	Akut cystit 
    I47.2C	Kammartakykardi, torsades des pointes
    T78.4	Allergi, ospecificerad 
    M35.3	Polymyalgia rheumatica
    K56.6	Annan och icke specificerad obstruktion av tarmen 
    M24.3	Patologisk luxation och subluxation i led som ej klassificeras annorstädes 
    R06.5	Munandning och snarkning 
    E10.5	Diabetes mellitus typ 1 med perifera kärlkomplikationer
    E10.5A	Diabetes mellitus typ 1 med perifer angiopati (utan gangrän)
    E10.5B	Diabetes mellitus typ 1 med perifer angiopati med gangrän
*/
        

} ]);

/*
 * ViewCertCtrl - Controller for logic related to viewing a certificate
 */
angular.module('wc.fk7263.controllers').controller('ViewCertCtrl', [ '$scope', '$log', '$timeout', 'viewCertificateService', function CreateCertCtrl($scope, $log, $timeout, viewCertificateService) {

    // init state
    $scope.widgetState = {
        doneLoading : false,
        activeErrorMessageKey : null
    }
    $scope.cert = {};
    $scope.cert.filledAlways = true;

    // Load certificate json
    viewCertificateService.getCertificate($scope.MODULE_CONFIG.CERT_ID_PARAMETER, function(result) {
        $log.debug("Got getCertificate data:" + result);
        $scope.widgetState.doneLoading = true;
        if (result != null) {
            $scope.cert = result;
        }
    }, function(errorData) {
        // show error view
        $scope.widgetState.doneLoading = true;
        $scope.widgetState.activeErrorMessageKey = "error.could_not_load_cert";
    });
} ]);
