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

  var dummycert =
  {
    "utlatandeid": "987654321",
      "typAvUtlatande": "TSTRK1007 (U06, V06)",
      "signeringsdatum": "2013-08-12T15:57:00.000",
      "skapadAv": {
    "personid": "SE0000000000-1333",
        "fullstandigtNamn": "Doktor Thompson",
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
    }
  },
    "patient": {
    "personid": "19121212-1212",
        "fullstandigtNamn": "Johnny Appleseed",
        "fornamn": "Johnny",
        "efternamn": "Appleseed",
        "postadress": "Testvägen 12",
        "postnummer": "123456",
        "postort": "Testort"
  },
    "vardkontakt" : {
    "typ" : "5880005",
        "idkontroll" : "IDK6"
  },
    "intygAvser" : {
    "korkortstyp" : {"C":true}
  },
    "syn" : {
    "synfaltsdefekter" :  false,
        "nattblindhet" : false,
        "progressivOgonsjukdom" : false,
        "diplopi" : false,
        "nystagmus" : false,
        "hogerOga" : {
      "utanKorrektion" : 0.0,
          "kontaktlins" : false
    },

    "vansterOga" : {
      "utanKorrektion" : 0.0,
          "kontaktlins" : false
    },

    "binokulart" : {
      "utanKorrektion" : 0.0
    }
  },

    "horselBalans" : {
    "balansrubbningar" : false
  },

    "funktionsnedsattning" : {
    "funktionsnedsattning" : false
  },

    "hjartKarl" : {
    "hjartKarlSjukdom" : false,
        "hjarnskadaEfterTrauma" : false,
        "riskfaktorerStroke" : false
  },

    "diabetes" : {
    "harDiabetes" : false
  },

    "neurologi" : {
    "neurologiskSjukdom" : false
  },

    "medvetandestorning" : {
    "medvetandestorning" : false
  },

    "njurar" : {
    "nedsattNjurfunktion" : false
  },

    "kognitivt" : {
    "sviktandeKognitivFunktion" : false
  },

    "somnVakenhet" : {
    "teckenSomnstorningar" : false
  },

    "narkotikaLakemedel" : {
    "teckenMissbruk" : false,
        "foremalForVardinsats" : false,
        "lakarordineratLakemedelsbruk" : false
  },

    "psykiskt" : {
    "psykiskSjukdom" : false
  },

    "utvecklingsstorning" : {
    "psykiskUtvecklingsstorning" : false,
        "harSyndrom" : false
  },

    "sjukhusvard" : {
    "sjukhusEllerLakarkontakt" : false
  },

    "medicinering" : {
    "stadigvarandeMedicinering" : false
  },

    "bedomning" : {
    "korkortstyp" : ["C"]
  }

  }

  $scope.cert = dummycert;

}]);

