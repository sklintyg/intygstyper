/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

angular.module('fk7263', [ 'ui.bootstrap', 'ngCookies', 'ui.router', 'ngSanitize', 'common' ]);

angular.module('fk7263').config(function($stateProvider) {
    'use strict';

    $stateProvider.
        state('fk7263-view', {
            url :'/fk7263/view/:certificateId',
            templateUrl: '/web/webjars/fk7263/minaintyg/views/view-cert.html',
            controller: 'fk7263.ViewCertCtrl',
            data : { title: 'Läkarintyg FK7263', keepInboxTabActive: true}
        }).
        state('fk7263-customize', {
            url :'/fk7263/customize/:certificateId',
            templateUrl: '/web/webjars/fk7263/minaintyg/views/customize-cert.html',
            controller: 'fk7263.CustomizeCertCtrl',
            data : { title: 'Anpassa intyget till arbetsgivare', keepInboxTabActive: true}
        }).
        state('fk7263-customize-summary', {
            url :'/fk7263/customize/:certificateId/summary',
            templateUrl: '/web/webjars/fk7263/minaintyg/views/customize-cert-summary.html',
            controller: 'fk7263.CustomizeCertSummaryCtrl',
            data : { title: 'Summering anpassa intyget till arbetsgivare', keepInboxTabActive: true}
        }).
        state('fk7263-recipients', {
            url : '/fk7263/recipients',
            templateUrl: '/web/webjars/fk7263/minaintyg/views/recipients.html',
            controller: 'common.SendCertWizardCtrl',
            data : { title: 'Skicka intyg till mottagare' }
        }).
        state('fk7263-statushistory', {
            url : '/fk7263/statushistory',
            templateUrl: '/web/webjars/fk7263/minaintyg/views/status-history.html',
            controller: 'fk7263.ViewCertCtrl',
            data : { title: 'Alla intygets händelser' }
        }).
        state('fk7263-summary', {
            url : '/fk7263/summary',
            templateUrl: '/web/webjars/fk7263/minaintyg/views/send-summary.html',
            controller: 'common.SendCertWizardCtrl',
            data : { title: 'Kontrollera och skicka intyget' }
        }).
        state('fk7263-sent', {
            url : '/fk7263/sent',
            templateUrl: '/web/webjars/fk7263/minaintyg/views/sent-cert.html',
            controller: 'common.SendCertWizardCtrl',
            data : { title: 'Intyget har skickats' }
        }).
        state('fk7263-fel', {
            url : '/fk7263/fel/:errorCode',
            templateUrl: '/web/webjars/fk7263/minaintyg/views/error.html',
            controller: 'fk7263.ErrorCtrl',
            data : { title: 'Fel' }
        }).
        state('fk7263-visafel', {
            url :'/fk7263/visafel/:errorCode',
            templateUrl: '/web/webjars/fk7263/minaintyg/views/error.html',
            controller: 'fk7263.ErrorCtrl',
            data : { title: 'Fel',
                    backLink: '/web/start' }
        });
});

// Inject language resources
angular.module('fk7263').run(['common.messageService', 'fk7263.messages',
    function(messageService, fk7263Messages) {
        'use strict';

        messageService.addResources(fk7263Messages);
    }]);
