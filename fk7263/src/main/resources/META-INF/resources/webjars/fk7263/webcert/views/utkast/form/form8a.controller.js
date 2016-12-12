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

angular.module('fk7263').controller('fk7263.EditCert.Form8aCtrl',
    ['$scope', '$log', 'fk7263.EditCertCtrl.ViewStateService', 'common.ObjectHelper', 'common.UtkastValidationService',
        function($scope, $log, viewState, ObjectHelper, UtkastValidationService) {
            'use strict';

            var model = viewState.intygModel;
            $scope.model = model;
            $scope.viewState = viewState;

            $scope.onSysselsattningChange = function(){

                var nuvarandeArbeteChanged = false;
                if(ObjectHelper.isDefined(model.nuvarandeArbete) && model.nuvarandeArbete !== viewState.sysselsattningValue[0]){
                    nuvarandeArbeteChanged = true;
                }

                viewState.sysselsattningValue = [model.nuvarandeArbete, model.arbetsloshet, model.foraldrarledighet];
                
                // only do this once the page is loaded and changes come from the gui!
                if(viewState.common.doneLoading && nuvarandeArbeteChanged) {
                    var modelSubProperty = model.properties.form8a.nuvarandeArbetsuppgifter;
                    if (model.nuvarandeArbete === false) {
                        model.updateToAttic(modelSubProperty);
                        model.clear(modelSubProperty);
                    } else if(model.isInAttic(modelSubProperty)){
                        model.restoreFromAttic(modelSubProperty);
                    }
                }
            };


            function syncAttic(valueHidden){
                // only do this once the page is loaded and changes come from the gui!
                if(viewState.common.doneLoading) {
                    if (valueHidden === true) {
                        model.updateToAttic(model.properties.form8a);
                        model.clear(model.properties.form8a);
                    } else if(model.isInAttic(model.properties.form8a)){
                        model.restoreFromAttic(model.properties.form8a);
                    }
                }
            }

            $scope.$watch('viewState.avstangningSmittskyddValue', function(newVal, oldVal) {
                if(newVal === oldVal){
                    return;
                }
                syncAttic(newVal);
            });

            $scope.validate = function() {
                UtkastValidationService.validate(model);
            };
        }]);
