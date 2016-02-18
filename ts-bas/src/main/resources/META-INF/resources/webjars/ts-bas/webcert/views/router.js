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

    var commonPath = '/web/webjars/common/webcert/';
    var intygsTypPath = '/web/webjars/ts-bas/webcert/';

    $stateProvider.
        state('ts-bas-edit', {
            data: { defaultActive : 'index' },
            url : '/ts-bas/edit/:certificateId',
            views : {
                'content@': {
                    templateUrl: intygsTypPath + 'views/utkast/utkast.html',
                    controller: 'ts-bas.UtkastController'
                },
                'wcHeader@ts-bas-edit' : {
                    templateUrl: commonPath + 'gui/headers/wcHeader.partial.html',
                    controller: 'common.wcHeaderController'
                },
                'header@ts-bas-edit': {
                    templateUrl: commonPath + 'utkast/utkast-header/utkastHeader.html',
                    controller: 'common.UtkastHeader'
                },

                'patient@ts-bas-edit' : {
                    templateUrl: intygsTypPath + 'views/utkast/form/patient.html'
                },
                'intention@ts-bas-edit' : {
                    templateUrl: intygsTypPath + 'views/utkast/form/intention.html',
                    controller: 'ts-bas.Utkast.IntentionController'
                },
                'identity@ts-bas-edit' : {
                    templateUrl: intygsTypPath + 'views/utkast/form/identity.html'
                },
                'alert@ts-bas-edit' : {
                    templateUrl: intygsTypPath + 'views/utkast/form/alert.html'
                },

                'form1@ts-bas-edit' : {
                    templateUrl: intygsTypPath + 'views/utkast/form/form1.html',
                    controller: 'ts-bas.Utkast.Form1Controller'
                },
                'form2@ts-bas-edit' : {
                    templateUrl: intygsTypPath + 'views/utkast/form/form2.html'
                },
                'form3@ts-bas-edit' : {
                    templateUrl: intygsTypPath + 'views/utkast/form/form3.html',
                    controller: 'ts-bas.Utkast.Form3Controller'
                },
                'form4@ts-bas-edit' : {
                    templateUrl: intygsTypPath + 'views/utkast/form/form4.html',
                    controller: 'ts-bas.Utkast.Form4Controller'
                },
                'form5@ts-bas-edit' : {
                    templateUrl: intygsTypPath + 'views/utkast/form/form5.html',
                    controller: 'ts-bas.Utkast.Form5Controller'
                },
                'form6@ts-bas-edit' : {
                    templateUrl: intygsTypPath + 'views/utkast/form/form6.html'
                },
                'form7@ts-bas-edit' : {
                    templateUrl: intygsTypPath + 'views/utkast/form/form7.html',
                    controller: 'ts-bas.Utkast.Form7Controller'
                },
                'form8@ts-bas-edit' : {
                    templateUrl: intygsTypPath + 'views/utkast/form/form8.html'
                },
                'form9@ts-bas-edit' : {
                    templateUrl: intygsTypPath + 'views/utkast/form/form9.html'
                },

                'form10@ts-bas-edit' : {
                    templateUrl: intygsTypPath + 'views/utkast/form/form10.html'
                },
                'form11@ts-bas-edit' : {
                    templateUrl: intygsTypPath + 'views/utkast/form/form11.html',
                    controller: 'ts-bas.Utkast.Form11Controller'
                },
                'form12@ts-bas-edit' : {
                    templateUrl: intygsTypPath + 'views/utkast/form/form12.html'
                },
                'form13@ts-bas-edit' : {
                    templateUrl: intygsTypPath + 'views/utkast/form/form13.html'
                },
                'form14@ts-bas-edit' : {
                    templateUrl: intygsTypPath + 'views/utkast/form/form14.html',
                    controller: 'ts-bas.Utkast.Form14Controller'
                },
                'form15@ts-bas-edit' : {
                    templateUrl: intygsTypPath + 'views/utkast/form/form15.html',
                    controller: 'ts-bas.Utkast.Form15Controller'
                },
                'form16@ts-bas-edit' : {
                    templateUrl: intygsTypPath + 'views/utkast/form/form16.html'
                },
                'form17@ts-bas-edit' : {
                    templateUrl: intygsTypPath + 'views/utkast/form/form17.html',
                    controller: 'ts-bas.Utkast.Form17Controller'
                },
                'form18@ts-bas-edit' : {
                    templateUrl: intygsTypPath + 'views/utkast/form/form18.html'
                },
                'form19@ts-bas-edit' : {
                    templateUrl: intygsTypPath + 'views/utkast/form/form19.html',
                    controller: 'ts-bas.Utkast.Form19Controller'
                },

                'footer@ts-bas-edit' : {
                    templateUrl: commonPath + 'utkast/utkast-footer/utkastFooter.html',
                    controller: 'common.UtkastFooter'
                }

            }
        }).
        state('webcert.intyg.ts.bas', {
            data: { defaultActive: 'index', intygType: 'ts-bas' },
            url: '/intyg/ts-bas/:certificateId?:patientId&:hospName&:signed',
            views: {
                'intyg@webcert.intyg.ts': {
                    templateUrl: intygsTypPath + 'views/intyg/intyg.html',
                    controller: 'ts-bas.IntygController'
                },
                'header@webcert.intyg.ts.bas': {
                    templateUrl: commonPath + 'intyg/intyg-header/intyg-header.html',
                    controller: 'common.IntygHeader'
                }
            }
        });
});
