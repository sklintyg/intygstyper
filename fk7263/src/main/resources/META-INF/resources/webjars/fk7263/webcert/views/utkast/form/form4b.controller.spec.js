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

describe('fk7263.EditCertCtrl.Form4bCtrl', function() {
    'use strict';

    // Load the webcert module and mock away everything that is not necessary.

    beforeEach(angular.mock.module('common', 'fk7263', function(/*$provide*/) {

        // the below are now included in common
        //$provide.value('fk7263.domain.DraftModel', __draftModel__);
        //$provide.value('fk7263.domain.IntygModel', {});
        //$provide.value('fk7263.domain.PatientModel', {});
        //$provide.value('fk7263.domain.SkapadAvModel', {});
        //$provide.value('fk7263.domain.VardenhetModel', {});

    }));


    // Get references to the object we want to test from the context.

    var $scope;
    var $rootScope;
    var $log;
    var model;
    var viewState;

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

            $controller('fk7263.EditCert.Form4bCtrl' , { $scope: $scope, $log : $log, model : model, viewState : viewState });

        }]));


    describe('#change in avstangningSmittskydd should trigger an update in the attic', function() {
        beforeEach(function(){

            model.annanReferens = 'annanReferens';
            model.annanReferensBeskrivning = 'annanReferensBeskrivning';
            model.journaluppgifter = 'journaluppgifter';
            model.telefonkontaktMedPatienten = 'telefonkontaktMedPatienten';
            model.undersokningAvPatienten = 'undersokningAvPatienten';

            $scope.form4b = {
                undersokningAvPatientenDate: {$parsers:[]},
                telefonkontaktMedPatienten: {$parsers:[]},
                journaluppgifter: {$parsers:[]},
                annanReferens: {$parsers:[]}
            };
        });

        it('can restore from the attic', function(){
            // ----- arrange
            viewState.common.doneLoading = true;

            // ----- act
            $scope.$apply(); // register false on avstangningSmittskydd


            viewState.avstangningSmittskyddValue = true;
            $scope.$apply();

            // ----- assert
            expect(model.annanReferens).toBe(undefined);
            expect(model.annanReferensBeskrivning).toBe(undefined);
            expect(model.journaluppgifter).toBe(undefined);
            expect(model.telefonkontaktMedPatienten).toBe(undefined);
            expect(model.undersokningAvPatienten).toBe(undefined);

            // ----- act
            viewState.avstangningSmittskyddValue = false;  // this should trigger the watch event
            $scope.$apply();

            // ----- assert
            // expects
            // restore from attic
            model.annanReferens = 'annanReferens';
            model.annanReferensBeskrivning = 'annanReferensBeskrivning';
            model.journaluppgifter = 'journaluppgifter';
            model.telefonkontaktMedPatienten = 'telefonkontaktMedPatienten';
            model.undersokningAvPatienten = 'undersokningAvPatienten';

        });

    });


    var tpl, inputElement;

    describe('#manual change in date picker field should trigger value formatting', function() {

        // Set up a form element

        beforeEach(angular.mock.inject(['$rootScope', '$compile',
            function($rootScope, $compile) {

                tpl = angular.element(
                    '<div ng-form="testForm">' +
                    '<span wc-date-picker-field target-model="model" dom-id="test" override-render="true" add-date-parser="loose"></span>' +
                    '</div>'
                );

                $scope = $rootScope.$new();
                $scope.model = null;
                $compile(tpl)($scope);
                $scope.$digest();

                // Find the input control:
                inputElement = tpl.find('input');
            }
        ]));

        it('should allow 2016-09-12', function() {
            _applyValue('2016-09-12');
            _validateOutcome('2016-09-12');
        });

        it('should allow 2016-0912', function() {
            _applyValue('2016-0912');
            _validateOutcome('2016-09-12');
        });

        it('should allow 201609-12', function() {
            _applyValue('201609-12');
            _validateOutcome('2016-09-12');
        });

        it('should allow 2016/09/12', function() {
            _applyValue('2016/09/12');
            _validateOutcome('2016-09-12');
        });

        it('should allow 2016/0912', function() {
            _applyValue('2016/0912');
            _validateOutcome('2016-09-12');
        });

        it('should allow 201609/12', function() {
            _applyValue('201609/12');
            _validateOutcome('2016-09-12');
        });

        it('should allow 20160912', function() {
            _applyValue('20160912');
            _validateOutcome('2016-09-12');
        });

    });


    function _applyValue(value) {
        angular.element(inputElement).val(value).trigger('input');
        $scope.$apply();
    }

    function _validateOutcome(value) {
        expect($scope.testForm.test.$viewValue).toBe(value);
        expect($scope.testForm.test.$modelValue).toBe(value);
    }


});
