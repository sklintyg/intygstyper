#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
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

/* Controllers */
angular.module('controllers.rli.ViewCertCtrl', []);
angular.module('controllers.rli.ViewCertCtrl').controller('ViewCertCtrl', [ '${symbol_dollar}scope', '${symbol_dollar}filter', '${symbol_dollar}location', 'certService', '${symbol_dollar}http', function ViewCertCtrl(${symbol_dollar}scope, ${symbol_dollar}filter, ${symbol_dollar}location, certService, http) {
    ${symbol_dollar}scope.cert = {};
    ${symbol_dollar}scope.doneLoading = false;
    ${symbol_dollar}scope.shouldBeOpen = false;

    ${symbol_dollar}scope.open = function() {
        ${symbol_dollar}scope.shouldBeOpen = true;
    };

    ${symbol_dollar}scope.close = function() {
        ${symbol_dollar}scope.closeMsg = 'I was closed at: ' + new Date();
        ${symbol_dollar}scope.shouldBeOpen = false;
    };

    ${symbol_dollar}scope.opts = {
        backdropFade : true,
        dialogFade : true
    };

    console.log("Loading certificate " + ${symbol_dollar}scope.MODULE_CONFIG.CERT_ID_PARAMETER);
    
//    http.get('http://localhost:8088/m/rli/api/view/utlatande/' + ${symbol_dollar}scope.MODULE_CONFIG.CERT_ID_PARAMETER).then(function(res) {
//		${symbol_dollar}scope.cert = res.data;
//		${symbol_dollar}scope.doneLoading = true;
//	});
    
    certService.getCertificate(${symbol_dollar}scope.MODULE_CONFIG.CERT_ID_PARAMETER, function(result) {
        ${symbol_dollar}scope.doneLoading = true;
        if (result != null) {
            ${symbol_dollar}scope.cert = result;
        } else {
            // show error view
            ${symbol_dollar}location.path("/fel");
        }
    });
} ]);
