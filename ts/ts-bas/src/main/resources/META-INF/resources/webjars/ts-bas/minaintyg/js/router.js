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

/**
 * Created by stephenwhite on 05/03/15.
 */
angular.module('ts-bas').config(function($stateProvider) {
    'use strict';
    $stateProvider.
        state('ts-bas-view', {
            url :'/ts-bas/view/:certificateId',
            templateUrl: '/web/webjars/ts-bas/minaintyg/views/view-cert.html',
            controller: 'ts-bas.ViewCertCtrl',
            data:{title: 'Läkarintyg Transportstyrelsen Bas', keepInboxTabActive: true}
        }).
        state('ts-bas-recipients', {
            url : '/ts-bas/recipients',
            templateUrl: '/web/webjars/ts-bas/minaintyg/views/recipients.html',
            controller: 'common.SendCertWizardCtrl',
            data: { title: 'Skicka intyg till mottagare' }
        }).
        state('ts-bas-summary', {
            url :'/ts-bas/summary',
            templateUrl: '/web/webjars/ts-bas/minaintyg/views/send-summary.html',
            controller: 'common.SendCertWizardCtrl',
            data:{title: 'Kontrollera och skicka intyget'}
        }).
        state('ts-bas-sent', {
            url :'/ts-bas/sent',
            templateUrl: '/web/webjars/ts-bas/minaintyg/views/sent-cert.html',
            controller: 'common.SendCertWizardCtrl',
            data:{title: 'Intyget skickat till mottagare'}
        }).
        state('ts-bas-visafel', {
            url :'/ts-bas/visafel/:errorCode',
            templateUrl: '/web/webjars/ts-bas/minaintyg/views/error.html',
            controller: 'ts-bas.ErrorCtrl',
            data : { title: 'Fel', backLink: '/web/start' }
        });
});
