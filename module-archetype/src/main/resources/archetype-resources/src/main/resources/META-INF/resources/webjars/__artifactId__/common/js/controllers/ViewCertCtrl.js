#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
define([
], function () {
    'use strict';

    return ['${symbol_dollar}scope', '${symbol_dollar}filter', '${symbol_dollar}location', '${artifactId}.certificateService', '${symbol_dollar}http', '${symbol_dollar}routeParams',
        function (${symbol_dollar}scope, ${symbol_dollar}filter, ${symbol_dollar}location, certService, http, ${symbol_dollar}routeParams) {

            ${symbol_dollar}scope.cert = {};
            ${symbol_dollar}scope.doneLoading = false;
            ${symbol_dollar}scope.shouldBeOpen = false;

            ${symbol_dollar}scope.open = function () {
                ${symbol_dollar}scope.shouldBeOpen = true;
            };

            ${symbol_dollar}scope.close = function () {
                ${symbol_dollar}scope.closeMsg = 'I was closed at: ' + new Date();
                ${symbol_dollar}scope.shouldBeOpen = false;
            };

            ${symbol_dollar}scope.opts = {
                backdropFade : true,
                dialogFade : true
            };

            console.log("Loading certificate " + ${symbol_dollar}routeParams.certificateId);

            // http.get('http://localhost:8088/m/rli/api/view/utlatande/' + ${symbol_dollar}scope.MODULE_CONFIG.CERT_ID_PARAMETER).then(function(res) {
            //   ${symbol_dollar}scope.cert = res.data;
            //   ${symbol_dollar}scope.doneLoading = true;
            // });

            certService.getCertificate(${symbol_dollar}routeParams.certificateId, function (result) {
                ${symbol_dollar}scope.doneLoading = true;
                if (result != null) {
                    ${symbol_dollar}scope.cert = result;
                } else {
                    // show error view
                    ${symbol_dollar}location.path("/fel");
                }
            });
        }];
});
