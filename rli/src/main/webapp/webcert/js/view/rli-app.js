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

/* App Module */
/*
 * Cant seem to inject rootscope in .config, so for routing parameters, we use
 * the global JS config object for now
 */
var RLIApp = angular.module('RLIEditCertApp', [ 'controllers.rli.webcert', 'services.webcertService', 'modules.messages', 'directives.mi' ]).config(
        [ '$routeProvider', function($routeProvider) {
            $routeProvider.when('/edit/:certId', {
                templateUrl : MODULE_CONFIG.MODULE_CONTEXT_PATH + '/webcert/views/edit-cert.html',
                controller : 'EditCertCtrl'
            }).when('/new', {
                templateUrl : MODULE_CONFIG.MODULE_CONTEXT_PATH + '/webcert/views/new-cert.html',
                controller : 'NewCertCtrl'
            }).when('/list', {
            	templateUrl : MODULE_CONFIG.MODULE_CONTEXT_PATH + '/webcert/views/list-cert.html',
            	controller : 'ListCertCtrl'
            }).otherwise({
                redirectTo : '/list'
            });
        } ]);

RLIApp.run([ '$rootScope', 'messageService', function($rootScope, messageService) {
    $rootScope.lang = 'sv';
    $rootScope.DEFAULT_LANG = 'sv';
    $rootScope.MODULE_CONFIG = MODULE_CONFIG;
    messageService.addResources(commonMessageResources);
    messageService.addResources(rliMessages);
} ]);
