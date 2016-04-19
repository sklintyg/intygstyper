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

angular.module('luaefs').config(function($stateProvider) {
    'use strict';

    var commonPath = '/web/webjars/common/webcert/';
    var intygsTypPath = '/web/webjars/luaefs/webcert/';

    $stateProvider.
        state('luaefs-edit', {
            data: { defaultActive : 'index', intygType: 'luaefs' },
            url : '/luaefs/edit/:certificateId?:patientId&:hospName',
            views : {
                'content@' : {
                    templateUrl: intygsTypPath + 'views/utkast/utkast.html',
                    controller: 'luaefs.EditCertCtrl'
                },

                'wcHeader@luaefs-edit' : {
                    templateUrl: commonPath + 'gui/headers/wcHeader.partial.html',
                    controller: 'common.wcHeaderController'
                },

                'header@luaefs-edit' : {
                    templateUrl: commonPath + 'utkast/utkast-header/utkastHeader.html',
                    controller: 'common.UtkastHeader'
                },

                'footer@luaefs-edit' : {
                    templateUrl: commonPath + 'utkast/utkast-footer/utkastFooter.html',
                    controller: 'common.UtkastFooter'
                },

                'formly@luaefs-edit' : {
                    templateUrl: intygsTypPath + 'views/utkast/formly.html',
                    controller: 'luaefs.EditCert.FormlyCtrl'
                }
            }
        }).
        state('webcert.intyg.fk.luaefs', {
            data: { defaultActive : 'index', intygType: 'luaefs' },
            url:'/intyg/luaefs/:certificateId?:patientId&:hospName&:signed',
            views: {
                'intyg@webcert.intyg.fk' : {
                    templateUrl: intygsTypPath + 'views/intyg/intyg.html',
                    controller: 'luaefs.ViewCertCtrl'
                },
                'fragaSvar@webcert.intyg.fk' : {
                    templateUrl: commonPath + 'intyg/fk/arenden/arenden.html',
                    controller: 'common.QACtrl'
                },
                'header@webcert.intyg.fk.luaefs' : {
                    templateUrl: commonPath + 'intyg/intyg-header/intyg-header.html',
                    controller: 'common.IntygHeader'
                }
            }
        }).
        state('webcert.arenden.luaefs', {
            data: { defaultActive : 'unhandled-qa', intygType: 'luaefs'  },
            url: '/arenden/luaefs/:certificateId',
            views: {
                'intyg@webcert.fragasvar' : {
                    templateUrl: intygsTypPath + 'views/intyg/intyg.html',
                    controller: 'luaefs.ViewCertCtrl'
                },
                'fragasvar@webcert.fragasvar' : {
                    templateUrl: commonPath + 'intyg/fk/arenden/arenden.html',
                    controller: 'common.QACtrl'
                },
                'header@webcert.fragasvar.luaefs' : {
                    templateUrl: commonPath + 'intyg/intyg-header/intyg-header.html',
                    controller: 'common.IntygHeader'
                }
            }
        });
});