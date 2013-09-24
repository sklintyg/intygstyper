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
angular.module('controllers.rli.webcert', []);
angular.module('controllers.rli.webcert').controller('NewCertCtrl', [ '$scope', '$filter', '$location', '$rootScope', 'webcertService', '$http' , function NewCertCtrl($scope, $filter, $location, $rootScope, webcertService, $http) {
    $scope.cert = {};

    webcertService.createDraft('RLI', function(result) {
    		$scope.cert = result;
    		$scope.cert.skapadAv = {
					vardenhet : {
    						enhetsnamn		: "Test",
    						postadress		: "Vårdenhetsvägen 13",
    						postort			: "Hägersten",
    						postnummer		: "12637",
    						telefonnummer	: "08-1337",
    						epost 			: "test@vardenheten.se"    						
					}
    		};
	});
//    $scope.cert.skapadAv.vardenhet.enhetsnamn = "Testvårdenhet";
//    $scope.cert.skapadAv.vardenhet.postadress = "Vårdenhetsvägen 13";
//    $scope.cert.skapadAv.vardenhet.postort = "Hägersten";
//    $scope.cert.skapadAv.vardenhet.postnummer = "12637";
//    $scope.cert.skapadAv.vardenhet.telefonnummer = "08-1337";
//    $scope.cert.skapadAv.vardenhet.epost = "test@vardenheten.se";
   
    // TODO: Remove when editModelFactory implementation is finished
    
    
    $scope.proceedToCert = function() {
	   webcertService.saveDraft($scope.cert.utlatandeid, angular.toJson($scope.cert), function(){
		   console.log("Call completed"); 		 
 	   });	
    	window.location = "#/edit/" + $scope.cert.utlatandeid;
    };
    
}]);


angular.module('controllers.rli.webcert').controller('ListCertCtrl', [ '$scope', '$filter', '$location', '$rootScope', 'webcertService', '$routeParams', function ListCertCtrl($scope, $filter, $location, $rootScope, webcertService, $routeParams) {
	$scope.getDraftList = {}
	
	webcertService.getDraftList(function(result) {
			$scope.draftList = result;
		});
	
}]);

angular.module('controllers.rli.webcert').controller('EditCertCtrl', [ '$scope', '$filter', '$location', '$rootScope', 'webcertService', '$routeParams', function EditCertCtrl($scope, $filter, $location, $rootScope, webcertService, $routeParams) {
    $scope.cert = {};
    
    webcertService.getDraft($routeParams.certId, function(result){
    	$scope.cert = result;
    });

    console.log("The controller at least got this far..");
    
    
   $scope.saveCert = function () {
	   console.log("Making save call to REST");
	   webcertService.saveDraft($scope.cert.utlatandeid, angular.toJson($scope.cert), function(){
		   console.log("Call completed");
		   window.location = "#/list";
		   
	   });
	   
    }
} ]);

