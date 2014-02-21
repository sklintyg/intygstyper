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
var controllers = angular.module('wc.ts-diabetes.controllers', []);
controllers.controller('NewCertCtrl', [ '$scope', '$filter', '$location', '$rootScope', 'webcertService', '$routeParams' , function NewCertCtrl($scope, $filter, $location, $rootScope, webcertService, $routeParams) {
    $scope.cert = {};
    $scope.doneLoading = false;
   
    /*
	 * Calls method getDraft in webcertService to get a draft corresponding to
	 * the id provided in $routeParams.certId
	 */
    webcertService.getDraft($routeParams.certId, function(result){
    	if ($routeParams.certId == null || $routeParams.certId == 'undefined') {
    		console.log("$routeParams.certId was null or undefined");
    		$location.path = "/list";
    	}
    	$scope.cert = result;
 
    });
    
    $scope.doneLoading = true;
    
    /*
	 * Injects complicationType into the $rootScope (this is used to determine
	 * visibility for certain div's in other controllers templates)
	 */
    $scope.changeCompType = function() {
    	$rootScope.complicationType = $scope.complicationType;
    };
    
    /*
	 * proceedToCert saves the current draft and redirects to /edit/utlatandeid
	 * for current cert.
	 */
    $scope.proceedToCert = function() {
    	webcertService.saveDraft($scope.cert.utlatandeid, angular.toJson($scope.cert), function(){
    		console.log("Call completed"); 	
 	   	});	
        $location.path("/edit/" + $scope.cert.utlatandeid);
    };
    
}]);


controllers.controller('ListCertCtrl', [ '$scope', '$filter', '$location', '$rootScope', 'webcertService', '$routeParams', function ListCertCtrl($scope, $filter, $location, $rootScope, webcertService, $routeParams) {
	$scope.initial_params = {};
	$scope.doneLoading = true;
	
	/* Different types of certificates */
	$scope.cert_types = [
	                    {type : 'RLI'},
	                    {type : 'FK7263'}
                    ];
	/* Initiate cert_type to RLI */
	$scope.cert_type = $scope.cert_types[0];
	
	/*
	 * Set initial parameters (can be changed in corresponding textarea in
	 * template)
	 */
	$scope.initial_params.data = {
			skapadAv:{
				id:{
					"root":"1.2.752.129.2.1.4.1",
					"extension":"19101010-1010"
				},
				namn:"Doktor Alban",
				vardenhet:{
					id:{
						"root":"1.2.752.129.2.1.4.1",
						"extension":"vardenhet_test"
					},
					namn		:"Testenheten",
					postadress	:"Teststigen 12",
					postnummer	:"12345",
					postort		:"Tolvberga",
					telefonnummer:"012-345678",
					epost		:"ingen@alls.se",
					vardgivare :{
						id :{
							root:"1.2.752.129.2.1.4.1",
							extension:"19101010-1010"
						},
						namn:"vårdgivarnamn"
					}
				}
			},
			patientInfo:{
				id:{
					root:"1.2.752.129.2.1.3.1",
					extension:"19121212-1212"
				},
				fornamn:[
					"johnny"
				],
				efternamn:"appleseed",
				postadress:"Testvägen 12",
				postnummer:"1337",
				postort:"Huddinge"
			}
		};

	/*
	 * Creates a new draft using the information in $scope.certificateContent by
	 * calling createDraft in webcertService, then redirects to /new/utlatandeid
	 * for current cert
	 */
    $scope.createCert = function() {
    	$scope.cert = {};
    	$scope.certificateContent = {
        		certificateType : $scope.cert_type.type,
        		skapadAv : eval($scope.initial_params.data.skapadAv),
        		patientInfo : eval($scope.initial_params.data.patientInfo)
        };
    	
    	webcertService.createDraft($scope.certificateContent, function(result) {
    		$scope.cert = result;
    		console.log("Got: " + $scope.cert.utlatandeid);
    		$location.path("/new/" + $scope.cert.utlatandeid);
    	});
    	
    };
	
	/*
	 * Get a list of drafts currently in storage
	 */
	$scope.getList = function(){
		webcertService.getDraftList(function(result) {
			$scope.draftList = result;
		});
		$scope.doneLoading = true;
	};
	
	/*
	 * Delete the draft corresponding to certId and update list
	 */
	$scope.deleteDraft = function(certId) {
		$scope.doneLoading = false;
		webcertService.deleteDraft(certId, function(){
			$scope.getList();
		});		
		
	};
	
}]);

controllers.controller('EditCertCtrl', [ '$scope', '$filter', '$location', '$rootScope', 'webcertService', '$routeParams', function EditCertCtrl($scope, $filter, $location, $rootScope, webcertService, $routeParams) {
    $scope.cert = {
      "utlatandeid": "987654321",
      "typAvUtlatande": "TSTRK1031 (U06, V02)",
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
        "korkortstyp" : ["C"]
      },
      "diabetes" : {
        "diabetestyp" : "E11"
      },
      "hypoglykemier" : {
        "kunskapOmAtgarder" : false,
        "teckenNedsattHjarnfunktion" : false
      },

      "bedomning" : {
        "korkortstyp" : ["C"]
      }

    };
    $scope.doneLoading = false;
    $scope.displayLoader = false;

    /*
	 * Gets cert draft using certId specified in $routeParams, redirect to /list
	 * if no corresponding draft is found
	 */
/*    webcertService.getDraft($routeParams.certId, function(result){
         if (result != null) {
             $scope.cert = result;
         } else {
            $location.path("/list");
         }
         $scope.doneLoading = true;
    });
  */
    /*
	 * Delete current draft and redirect to /list
	 */
    $scope.deleteDraft = function(){
    	webcertService.deleteDraft($scope.cert.utlatandeid, function(){
    		console.log("Deleted current draft");
            $location.path("/list");
    	});
    };
    
    /*
	 * Saves the draft by making a saveDraft call to webcertService
	 */
    $scope.saveCert = function () {
    	$scope.displayLoader = true;
    	console.log("Making save call to REST");
    	webcertService.saveDraft($scope.cert.utlatandeid, angular.toJson($scope.cert), function(){
    		console.log("Call completed");
            $location.path("/list");
    	});
	};
}]);

