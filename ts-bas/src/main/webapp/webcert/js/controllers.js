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
'use strict';

/* Controllers */
angular.module('wc.ts-bas.controllers', []);

angular.module('wc.ts-bas.controllers').controller('EditCertCtrl', [ '$scope', '$filter', '$location', '$rootScope', '$routeParams', function EditCertCtrl($scope, $filter, $location, $rootScope, $routeParams) {
  $scope.cert = {};
  $scope.doneLoading = true;
  $scope.displayLoader = false;

  $scope.form = {
    "identity" : {
      "ID-kort" : "ID_KORT",
      "Företagskort eller tjänstekort" : "FORETAG_ELLER_TJANSTEKORT",
      "Körkort" : "KORKORT",
      "Personlig kännedom" : "PERS_KANNEDOM",
      "Försäkran enligt 18 kap. 4§" : "FORSAKRAN_KAP18",
      "Pass" : "PASS"
    },
    "korkortd" : false,
    "behorighet" : true
  };

  $scope.$watch('cert.intygAvser.korkortstyp', function(newValue, oldValue){
    $scope.form.korkortd = false;
    for(var i = 4; i<$scope.cert.intygAvser.korkortstyp.length; i++){
      if(newValue[i].selected){
        $scope.form.korkortd = true;
        break;
      }
    }

    angular.forEach($scope.cert.intygAvser.korkortstyp, function(value, key){

    })
  }, true);

  var dummycert =
  {
    "utlatandeid": "987654321",
    "typAvUtlatande": "TSTRK1007 (U06, V06)",
    "signeringsdatum": "2013-08-12T15:57:00.000",
    "kommentar" : "Här kommer en övrig kommentar",
    "skapadAv": {
      "personid": "SE0000000000-1333",
      "fullstandigtNamn": "Doktor Thompson",
      "specialiteter" : ["SPECIALITET"],
      "vardenhet": {
        "enhetsid": "SE0000000000-1337",
        "enhetsnamn": "Vårdenhet Väst",
        "postadress": "Enhetsvägen 12",
        "postnummer": "54321",
        "postort": "Tumba",
        "telefonnummer": "08-1337",
        "vardgivare": {
          "vardgivarid": "SE0000000000-HAHAHHSAA",
          "vardgivarnamn": "Vårdgivarnamn"
        }
      },
      "befattningar" : []
    },
    "patient": {
      "personid": "19121212-1212",
      "fullstandigtNamn": "Herr Dundersjuk",
      "fornamn": "Herr",
      "efternamn": "Dundersjuk",
      "postadress": "Testvägen 12",
      "postnummer": "123456",
      "postort": "Testort"
    },
    "vardkontakt" : {
      "typ" : "5880005",
      "idkontroll" : "PASS"
    },
    "intygAvser" : {
      "korkortstyp" : [
        {"type": "C1", "selected": false},
        {"type": "C1E", "selected": false},
        {"type": "C", "selected": true},
        {"type": "CE", "selected": false},
        {"type": "D1", "selected": false},
        {"type": "D1E", "selected": false},
        {"type": "D", "selected": false},
        {"type": "DE", "selected": false},
        {"type": "TAXI", "selected": false}
      ]
    },
    "syn" : {
      "synfaltsdefekter" :  true,
      "nattblindhet" : true,
      "progressivOgonsjukdom" : true,
      "diplopi" : true,
      "nystagmus" : true,
      "hogerOga" : {
        "utanKorrektion" : 0.0,
        "medKorrektion" : 0.0,
        "kontaktlins" : true
      },

      "vansterOga" : {
        "utanKorrektion" : 0.0,
        "medKorrektion" : 0.0,
        "kontaktlins" : true
      },

      "binokulart" : {
        "utanKorrektion" : 0.0,
        "medKorrektion" : 0.0
      },
      "korrektionsglasensStyrka" : true
    },

    "horselBalans" : {
      "balansrubbningar" : true,
      "svartUppfattaSamtal4Meter" : true
    },

    "funktionsnedsattning" : {
      "funktionsnedsattning" : true,
      "otillrackligRorelseformaga" : true,
      "beskrivning" : "Spik i foten"

    },

    "hjartKarl" : {
      "hjartKarlSjukdom" : true,
      "hjarnskadaEfterTrauma" : true,
      "riskfaktorerStroke" : true,
      "beskrivningRiskfaktorer" : "Förkärlek för Elivsmackor"
    },

    "diabetes" : {
      "harDiabetes" : true,
      "diabetesTyp" : "DIABETES_TYP_1",
      "kost" : true
    },

    "neurologi" : {
      "neurologiskSjukdom" : true
    },

    "medvetandestorning" : {
      "medvetandestorning" : true,
      "beskrivning" : "Beskrivning"
    },

    "njurar" : {
      "nedsattNjurfunktion" : true
    },

    "kognitivt" : {
      "sviktandeKognitivFunktion" : true
    },

    "somnVakenhet" : {
      "teckenSomnstorningar" : true
    },

    "narkotikaLakemedel" : {
      "teckenMissbruk" : true,
      "foremalForVardinsats" : true,
      "lakarordineratLakemedelsbruk" : true,
      "lakemedelOchDos" : "Läkemedel och dos",
      "provtagningBehovs" : true
    },

    "psykiskt" : {
      "psykiskSjukdom" : true
    },

    "utvecklingsstorning" : {
      "psykiskUtvecklingsstorning" : true,
      "harSyndrom" : true
    },

    "sjukhusvard" : {
      "sjukhusEllerLakarkontakt" : true,
      "tidpunkt" : "20 Januari",
      "vardinrattning" : "Vårdcentralen",
      "anledning" : "Akut lungsot"
    },

    "medicinering" : {
      "stadigvarandeMedicinering" : true,
      "beskrivning" : "Alvedon"
    },

    "bedomning" : {
      "korkortstyp" : [
        {"type": "C1", "selected": false},
        {"type": "C1E", "selected": false},
        {"type": "C", "selected": true},
        {"type": "CE", "selected": false},
        {"type": "D1", "selected": false},
        {"type": "D1E", "selected": false},
        {"type": "D", "selected": false},
        {"type": "DE", "selected": false},
        {"type": "TAXI", "selected": false},
        {"type": "ANNAT", "selected": false}
      ],
      "lakareSpecialKompetens" : "Spektralanalys"
    }

  }

  $scope.cert = dummycert;

}]);

