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

angular.module('lisjp').config(function($stateProvider) {
    'use strict';

    var commonPath = '/web/webjars/common/webcert/';
    var intygsTypPath = '/web/webjars/lisjp/webcert/';

    $stateProvider.
        state('lisjp-edit', {
            data: { defaultActive : 'index', intygType: 'lisjp' },
            url : '/lisjp/edit/:certificateId?:patientId&:hospName&:fornamn&:efternamn&:mellannamn&:postadress&:postnummer&:postort&:sjf',
            views : {
                'content@' : {
                    templateUrl: intygsTypPath + 'views/utkast/utkast.html',
                    controller: 'lisjp.EditCertCtrl'
                },

                'wcHeader@lisjp-edit' : {
                    templateUrl: commonPath + 'gui/headers/wcHeader.partial.html',
                    controller: 'common.wcHeaderController'
                },

                'header@lisjp-edit' : {
                    templateUrl: commonPath + 'utkast/utkast-header/utkastHeader.html',
                    controller: 'common.UtkastHeader'
                },

                'footer@lisjp-edit' : {
                    templateUrl: commonPath + 'utkast/utkast-footer/utkastFooter.html',
                    controller: 'common.UtkastFooter'
                },

                'formly@lisjp-edit' : {
                    templateUrl: intygsTypPath + 'views/utkast/formly.html',
                    controller: 'lisjp.EditCert.FormlyCtrl'
                },

                'fragasvar@lisjp-edit' : {
                    templateUrl: commonPath + 'fk/arenden/arendeListUtkast.html',
                    controller: 'common.ArendeListCtrl'
                }
            }
        }).
        state('webcert.intyg.fk.lisjp', {
            data: { defaultActive : 'index', intygType: 'lisjp' },
            url:'/intyg/lisjp/:certificateId?:patientId&:hospName&:fornamn&:efternamn&:mellannamn&:postadress&:postnummer&:postort&:signed&:sjf',
            views: {
                'intyg@webcert.intyg.fk' : {
                    templateUrl: intygsTypPath + 'views/intyg/intyg.html',
                    controller: 'lisjp.ViewCertCtrl'
                },
                'fragasvar@webcert.intyg.fk' : {
                    templateUrl: commonPath + 'fk/arenden/arendeList.html',
                    controller: 'common.ArendeListCtrl'
                },
                'header@webcert.intyg.fk.lisjp' : {
                    templateUrl: commonPath + 'intyg/intyg-header/intyg-header.html',
                    controller: 'common.IntygHeader'
                }
            }
        }).
        state('webcert.fragasvar.lisjp', {
            data: { defaultActive : 'unhandled-qa', intygType: 'lisjp'  },
            url: '/fragasvar/lisjp/:certificateId',
            views: {
                'intyg@webcert.fragasvar' : {
                    templateUrl: intygsTypPath + 'views/intyg/intyg.html',
                    controller: 'lisjp.ViewCertCtrl'
                },
                'fragasvar@webcert.fragasvar' : {
                    templateUrl: commonPath + 'fk/arenden/arendeList.html',
                    controller: 'common.ArendeListCtrl'
                },
                'header@webcert.fragasvar.lisjp' : {
                    templateUrl: commonPath + 'intyg/intyg-header/intyg-header.html',
                    controller: 'common.IntygHeader'
                }
            }
        });
});