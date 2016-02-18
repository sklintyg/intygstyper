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
angular.module('ts-diabetes').config(function($stateProvider) {
    'use strict';

    var commonPath = '/web/webjars/common/webcert/';
    var intygsTypPath = '/web/webjars/ts-diabetes/webcert/';

    $stateProvider.
        state('ts-diabetes-edit', {
            data: { defaultActive : 'index' },
            url: '/ts-diabetes/edit/:certificateId',
            views : {
                'content@': {
                    templateUrl: intygsTypPath + 'views/utkast/utkast.html',
                    controller: 'ts-diabetes.UtkastController'
                },
                'wcHeader@ts-diabetes-edit' : {
                    templateUrl: commonPath + 'gui/headers/wcHeader.partial.html',
                    controller: 'common.wcHeaderController'
                },
                'header@ts-diabetes-edit': {
                    templateUrl: commonPath + 'utkast/utkast-header/utkastHeader.html',
                    controller: 'common.UtkastHeader'
                },

                'patient@ts-diabetes-edit' : {
                    templateUrl: intygsTypPath + 'views/utkast/form/patient.html'
                },
                'intention@ts-diabetes-edit' : {
                    templateUrl: intygsTypPath + 'views/utkast/form/intention.html',
                    controller: 'ts-diabetes.Utkast.IntentionController'
                },
                'identity@ts-diabetes-edit' : {
                    templateUrl: intygsTypPath + 'views/utkast/form/identity.html'
                },
                'alert@ts-diabetes-edit' : {
                    templateUrl: intygsTypPath + 'views/utkast/form/alert.html'
                },

                'form1@ts-diabetes-edit' : {
                    templateUrl: intygsTypPath + 'views/utkast/form/form1.html',
                    controller: 'ts-diabetes.Utkast.Form1Controller'
                },
                'form2@ts-diabetes-edit' : {
                    templateUrl: intygsTypPath + 'views/utkast/form/form2.html',
                    controller: 'ts-diabetes.Utkast.Form2Controller'
                },
                'form3@ts-diabetes-edit' : {
                    templateUrl: intygsTypPath + 'views/utkast/form/form3.html',
                    controller: 'ts-diabetes.Utkast.Form3Controller'
                },
                'form4@ts-diabetes-edit' : {
                    templateUrl: intygsTypPath + 'views/utkast/form/form4.html',
                    controller: 'ts-diabetes.Utkast.Form4Controller'
                },
                'form5@ts-diabetes-edit' : {
                    templateUrl: intygsTypPath + 'views/utkast/form/form5.html'
                },
                'form6@ts-diabetes-edit' : {
                    templateUrl: intygsTypPath + 'views/utkast/form/form6.html'
                },
                'form7@ts-diabetes-edit' : {
                    templateUrl: intygsTypPath + 'views/utkast/form/form7.html'
                },

                'messages@ts-diabetes-edit' : {
                    templateUrl: intygsTypPath + 'views/utkast/form/messages.html',
                    controller: 'ts-diabetes.Utkast.MessagesController'
                },

                'footer@ts-diabetes-edit' : {
                    templateUrl: commonPath + 'utkast/utkast-footer/utkastFooter.html',
                    controller: 'common.UtkastFooter'
                }
            }
        }).
        state('webcert.intyg.ts.diabetes', {
            data: { defaultActive: 'index', intygType: 'ts-diabetes' },
            url: '/intyg/ts-diabetes/:certificateId?:patientId&:hospName&:signed',
            views: {
                'intyg@webcert.intyg.ts': {
                    templateUrl: intygsTypPath + 'views/intyg/intyg.html',
                    controller: 'ts-diabetes.IntygController'
                },
                'header@webcert.intyg.ts.diabetes': {
                    templateUrl: commonPath + 'intyg/intyg-header/intyg-header.html',
                    controller: 'common.IntygHeader'
                }
            }
        });
});
