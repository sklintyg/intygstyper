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
define([
        'angular',
        'services',
        'rli/intyg/js/controllers',
        'rli/intyg/js/messages',
        'common/js/minaintyg/CertificateService',
        'common/js/filters'
    ], function(angular, miServices, controllers, messages, miCertificateService, filters) {
        'use strict';

        var moduleName = 'rli';

        var module = angular
            .module(moduleName, [miServices, controllers, miCertificateService, filters]);

        module.config(['$routeProvider', function($routeProvider) {
            $routeProvider.
                when('/rli/view/:certificateId', {
                    templateUrl: '/web/webjars/rli/intyg/views/view-cert.html',
                    controller: 'rli.ViewCertCtrl',
                    title: 'Läkarintyg Transportstyrelsen Bas'
                }).
                when('/rli/recipients', {
                    templateUrl: '/web/webjars/rli/intyg/views/recipients.html',
                    controller: 'rli.SendCertWizardCtrl'
                }).
                when('/rli/summary', {
                    templateUrl: '/web/webjars/rli/intyg/views/send-summary.html',
                    controller: 'rli.SendCertWizardCtrl',
                    title: 'Kontrollera och skicka intyget'
                }).
                when('/rli/sent', {
                    templateUrl: '/web/webjars/rli/intyg/views/sent-cert.html',
                    controller: 'rli.SendCertWizardCtrl',
                    title: 'Intyget skickat till mottagare'
                });
        }]);
        // Inject language resources
        // TODO: This only works since we always load webcert before the module, when the messageService
        // is moved to a commons project, make sure this is loaded for this module as well.
        module.run(['messageService',
            function(messageService) {
                messageService.addResources(messages);
            }
        ]);

        return moduleName;
    });



