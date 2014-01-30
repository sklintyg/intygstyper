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
angular.module('wc.ts-bas.controllers').controller('NewCertCtrl', [ '$scope', '$filter', '$location', '$rootScope', '$routeParams' , function NewCertCtrl($scope, $filter, $location, $rootScope, $routeParams) {
    $scope.cert = {};
    $scope.doneLoading = true;
    
    /*
	 * Injects complicationType into the $rootScope (this is used to determine
	 * visibility for certain div's in other controllers templates)
	 */
    $scope.changeCompType = function() {
    	$rootScope.complicationType = $scope.complicationType;
    };

}]);

angular.module('wc.ts-bas.controllers').controller('EditCertCtrl', [ '$scope', '$filter', '$location', '$rootScope', '$routeParams', function EditCertCtrl($scope, $filter, $location, $rootScope, $routeParams) {
    $scope.cert = {};
    $scope.doneLoading = true;
    $scope.displayLoader = false;
}]);

