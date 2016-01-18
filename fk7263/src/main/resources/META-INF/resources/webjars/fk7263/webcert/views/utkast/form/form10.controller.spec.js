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

describe('fk7263.EditCertCtrl.Form10Ctrl', function() {
    'use strict';

    var $scope;
    var $rootScope;
    var $log;
    var model;
    var viewState;

    // Load the webcert module and mock away everything that is not necessary.

    beforeEach(angular.mock.module('common', 'fk7263', function(/*$provide*/) {

    }));

    // Get references to the object we want to test from the context.

    beforeEach(angular.mock.inject([
        '$controller',
        '$rootScope',
        '$httpBackend',
        '$log',
        'fk7263.Domain.IntygModel',
        'fk7263.EditCertCtrl.ViewStateService',
        function( $controller, _$rootScope_, _$httpBackend_, _$log_, _model_, _viewState_) {
            $rootScope = _$rootScope_;
            $scope = $rootScope.$new();
            $log = _$log_;
            var IntygModel = _model_;
            model = new IntygModel();
            viewState = _viewState_;
            viewState.intygModel = model;

            $controller('fk7263.EditCert.Form10Ctrl' , { $scope: $scope, $log : $log, model : model, viewState : viewState });

        }]));

    describe('#change in sysselsattning to Arbetsloshet should trigger an update in the attic', function() {
        beforeEach(function(){

            model.prognosBedomning = 'prognosBedomning';
            model.arbetsformagaPrognosGarInteAttBedomaBeskrivning = 'arbetsformagaPrognosGarInteAttBedomaBeskrivning';

        });

        it('can restore from the attic', function(){
            // ----- arrange
            viewState.common.doneLoading = true;

            // ----- act
            $scope.$apply(); // register false on avstangningSmittskydd


            // set sysselsattning to Arbetsloshet only
            viewState.sysselsattningValue = [false, true, false];
            $scope.$apply();

            // ----- assert
            expect(model.prognosBedomning).toBe(undefined);
            expect(model.arbetsformagaPrognosGarInteAttBedomaBeskrivning).toBe(undefined);

            // ----- act
            viewState.sysselsattningValue = [true, false, false]; // this should trigger the watch event
            $scope.$apply();

            // ----- assert
            // expects
            // restore from attic
            model.prognosBedomning = 'prognosBedomning';
            model.arbetsformagaPrognosGarInteAttBedomaBeskrivning = 'arbetsformagaPrognosGarInteAttBedomaBeskrivning';
        });

    });
});
