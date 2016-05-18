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

angular.module('lisu').config(function($stateProvider) {
    'use strict';

    var commonPath = '/web/webjars/common/webcert/';
    var intygsTypPath = '/web/webjars/lisu/webcert/';

    $stateProvider.
        state('lisu-edit', {
            data: { defaultActive : 'index', intygType: 'lisu' },
            url : '/lisu/edit/:certificateId?:patientId&:hospName',
            views : {
                'content@' : {
                    templateUrl: intygsTypPath + 'views/utkast/utkast.html',
                    controller: 'sjukpenning-utokad.EditCertCtrl'
                },

                'wcHeader@lisu-edit' : {
                    templateUrl: commonPath + 'gui/headers/wcHeader.partial.html',
                    controller: 'common.wcHeaderController'
                },

                'header@lisu-edit' : {
                    templateUrl: commonPath + 'utkast/utkast-header/utkastHeader.html',
                    controller: 'common.UtkastHeader'
                },

                'footer@lisu-edit' : {
                    templateUrl: commonPath + 'utkast/utkast-footer/utkastFooter.html',
                    controller: 'common.UtkastFooter'
                },

                'formly@lisu-edit' : {
                    templateUrl: intygsTypPath + 'views/utkast/formly.html',
                    controller: 'sjukpenning-utokad.EditCert.FormlyCtrl'
                }
            }
        }).
        state('webcert.intyg.fk.lisu', {
            data: { defaultActive : 'index', intygType: 'lisu' },
            url:'/intyg/lisu/:certificateId?:patientId&:hospName&:signed',
            views: {
                'intyg@webcert.intyg.fk' : {
                    templateUrl: intygsTypPath + 'views/intyg/intyg.html',
                    controller: 'lisu.ViewCertCtrl'
                },
                'fragaSvar@webcert.intyg.fk' : {
                    templateUrl: commonPath + 'intyg/fk/arenden/arenden.html',
                    controller: 'common.QACtrl'
                },
                'header@webcert.intyg.fk.lisu' : {
                    templateUrl: commonPath + 'intyg/intyg-header/intyg-header.html',
                    controller: 'common.IntygHeader'
                }
            }
        }).
        state('webcert.fragasvar.lisu', {
            data: { defaultActive : 'unhandled-qa', intygType: 'lisu'  },
            url: '/fragasvar/lisu/:certificateId',
            views: {
                'intyg@webcert.fragasvar' : {
                    templateUrl: intygsTypPath + 'views/intyg/intyg.html',
                    controller: 'lisu.ViewCertCtrl'
                },
                'fragasvar@webcert.fragasvar' : {
                    templateUrl: commonPath + 'intyg/fk/arenden/arenden.html',
                    controller: 'common.ArendeCtrl'
                },
                'header@webcert.fragasvar.lisu' : {
                    templateUrl: commonPath + 'intyg/intyg-header/intyg-header.html',
                    controller: 'common.IntygHeader'
                }
            }
        });
});