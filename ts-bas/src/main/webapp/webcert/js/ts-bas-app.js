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

/*
 * App Module
 */
/*
 * Cant seem to inject rootscope in .config, so for routing parameters, we use
 * the global JS config object for now
 */
angular.module('TSBASViewCertApp', [ 'ui.bootstrap', 'wc.ts-bas.controllers', 'wc.ts-bas.directives', 'wc.ts-bas.services', 'modules.messages', 'wc.common',
        'wc.utils' ]);
angular.module('TSBASViewCertApp').config([ '$routeProvider', '$httpProvider', 'http403ResponseInterceptorProvider', function($routeProvider, $httpProvider, http403ResponseInterceptorProvider) {
    $routeProvider.when('/edit', {
        templateUrl : MODULE_CONFIG.MODULE_CONTEXT_PATH + '/webcert/views/edit-cert.html',
        controller : 'EditCertCtrl'
    }).when('/view', {
        templateUrl : MODULE_CONFIG.MODULE_CONTEXT_PATH + '/webcert/views/view-cert.html',
        controller : 'ViewCertCtrl'
    }).otherwise({
        redirectTo : '/view'
    });
} ]);

angular.module('TSBASViewCertApp').run([ '$rootScope', 'messageService', function($rootScope, messageService) {
    $rootScope.lang = 'sv';
    $rootScope.DEFAULT_LANG = 'sv';
    $rootScope.MODULE_CONFIG = MODULE_CONFIG;
    // Add WC user context info
    $rootScope.WC_CONTEXT = WC_CONTEXT;
    messageService.addResources(commonMessageResources);
    messageService.addResources(tsBasMessages);
} ]);
