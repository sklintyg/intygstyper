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
angular.module('controllers.ts-diabetes.ViewCertCtrl', []);
angular.module('controllers.ts-diabetes.ViewCertCtrl').controller('ViewCertCtrl',
    [ '$scope', '$filter', '$location', 'certService', '$log',
        function ViewCertCtrl($scope, $filter, $location, certService, $log) {
            $scope.cert = {};
            $scope.doneLoading = false;
            $scope.shouldBeOpen = false;

            $scope.open = function() {
                $scope.shouldBeOpen = true;
            };

            $scope.close = function() {
                $scope.closeMsg = 'I was closed at: ' + new Date();
                $scope.shouldBeOpen = false;
            };

            $scope.opts = {
                backdropFade: true,
                dialogFade: true
            };

            $log.debug('Loading certificate ' + $scope.MODULE_CONFIG.CERT_ID_PARAMETER);

            certService.getCertificate($scope.MODULE_CONFIG.CERT_ID_PARAMETER, function(result) {
                $scope.doneLoading = true;
                if (result !== null) {
                    $scope.cert = result;
                } else {
                    // show error view
                    $location.path('/fel');
                }
            });
        }
    ]
);
