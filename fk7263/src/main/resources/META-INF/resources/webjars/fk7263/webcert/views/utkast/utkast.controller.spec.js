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

describe('UtkastController', function() {
    'use strict';

    var $httpBackend;
    var $scope;
    var $q;
    var $rootScope;
    var $controller;

    beforeEach(angular.mock.module('common', function(/*$provide*/) {

    }));

    // Load the webcert module and mock away everything that is not necessary.
    beforeEach(angular.mock.module('fk7263', function($provide) {
        $provide.value('common.UtkastProxy', {});
        $provide.value('common.utkastNotifyService', {});
    }));

    // Get references to the object we want to test from the context.

    beforeEach(angular.mock.inject(['$controller', '$rootScope', '$q', '$httpBackend',
        function( _$controller_, _$rootScope_, _$q_, _$httpBackend_) {

            $controller = _$controller_;
            $rootScope = _$rootScope_;
            $scope = $rootScope.$new();

            $q = _$q_;
            $httpBackend = _$httpBackend_;

            // arrange
            spyOn($scope, '$broadcast');
        }]));

    describe('#testEvents', function() {
        it('on some event', function(){

            //$controller('fk7263.EditCertCtrl', { $scope: $scope, viewState:viewState });
            // ----- arrange
            // spies, mocks

            // kick off the window change event
            //$rootScope.$broadcast('fk7263.ViewCertCtrl.load');

            // ------ act
            // promises are resolved/dispatched only on next $digest cycle
            $rootScope.$apply();

            // ------ assert
            // dialog should be opened
            // expects

            expect(true).toBe(true);
        });

    });

    describe('#testScopeFunctions', function() {
        it('some scope function', function(){
            // ----- arrange
            // in arrange we setup our spies with expected return values


            // ----- act
            // call the function

            // ----- assert
            // expects

            expect(true).toBe(true);
        });

    });

});
