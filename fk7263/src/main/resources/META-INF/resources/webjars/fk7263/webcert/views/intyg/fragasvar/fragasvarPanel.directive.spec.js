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

describe('qaPanel', function() {
    'use strict';

    var element;
    var $httpBackend;
    var $scope;
    var $rootScope;
    var fragaSvarCommonService;
    var fragaSvarService;
    var IntygService;
    var deferred;

    // Load the webcert module and mock away everything that is not necessary.
    beforeEach(angular.mock.module('fk7263', function($provide) {
        $provide.value('common.dialogService', {});
        fragaSvarCommonService = jasmine.createSpyObj('common.fragaSvarCommonService', [ 'isUnhandled', 'fromFk', 'setVidareBefordradState' ]);
        $provide.value('common.fragaSvarCommonService', fragaSvarCommonService);
        $provide.value('common.IntygService', { isSentToTarget: function() {} });
        $provide.value('common.UserModel', {});
        $provide.value('common.FocusElementService', {});
        $provide.value('common.statService', {});
        $provide.value('common.ObjectHelper', jasmine.createSpyObj('common.ObjectHelper',
            [ 'isDefined']));
        $provide.value('common.IntygCopyRequestModel', jasmine.createSpyObj('common.IntygCopyRequestModel',
            [ 'build']));

        fragaSvarService = jasmine.createSpyObj('fk7263.fragaSvarProxy',
            [ 'getQAForCertificate', 'closeAsHandled', 'closeAllAsHandled', 'saveNewQuestion', 'saveAnswer']);
        $provide.value('fk7263.fragaSvarProxy', fragaSvarService);
    }));

    beforeEach(angular.mock.module('htmlTemplates'));

    beforeEach(angular.mock.inject(['$controller', '$compile', '$rootScope', '$q', '$httpBackend', 'common.IntygService',
        function($controller, $compile, _$rootScope_, _$q_, _$httpBackend_, _IntygService_) {
            $rootScope = _$rootScope_;
            $scope = $rootScope.$new();

            $httpBackend = _$httpBackend_;
            IntygService = _IntygService_;
            $scope.qa = { status: 'CLOSED'};
            $scope.certProperties = {};
            
            element = angular.element('<div qa-panel' +
                ' panel-id="handled" type="handled" qa="qa" qa-list="qaList" cert="cert" cert-properties="certProperties"></div>');
            element = $compile(element)($scope);
            $scope.$digest();
            $scope = element.isolateScope();
        }]));

    describe('#send answer', function() {
        it('should sendAnswer when "svara" is clicked', function() {

            var fragaSvar = {
                internReferens: 'intyg-1',
                svarsText: 'Att svara eller inte svara. Det är frågan.'
            };

            $scope.sendAnswer(fragaSvar);

            expect(fragaSvarService.saveAnswer).toHaveBeenCalled();
        });

    });

    describe('#dismissProxy', function() {
        it('should dismiss a message for a question', function() {

            var qaAnswered = {status: 'ANSWERED', proxyMessage: 'Error'};
            $scope.qaList = [qaAnswered];
            expect($scope.qaList.length).toBe(1);
            $scope.dismissProxy(qaAnswered);
            expect($scope.qaList.length).toBe(0);
        });
    });

    describe('#vidarebefordra', function() {
        it('should setVidarebefordradState when forward state is changed with onVidarebefordrad', function() {

            var question = {
                chosenTopic: {
                    value: 'KONTAKT'
                },
                frageText: 'Att fråga eller inte fråga. Det är frågan.'
            };

            $scope.onVidareBefordradChange(question);

            expect(fragaSvarCommonService.setVidareBefordradState).toHaveBeenCalled();
        });
    });


    describe('#hasUnhandledQas', function() {
        it('has no UnhandledQas', function() {

            $scope.qaList = [];
            expect($scope.hasUnhandledQas()).toBeFalsy();

        });

        it('has UnhandledQas', function() {
            // ----- arrange
            // in arrange we setup our spies with expected return values
            var qaAnswered = {status: 'ANSWERED'};
            $scope.qaList = [qaAnswered];
            fragaSvarCommonService.isUnhandled.and.returnValue(true);
            fragaSvarCommonService.fromFk.and.returnValue(true);

            // ----- act
            var hasUnhandled = $scope.hasUnhandledQas();

            // ----- assert
            expect(fragaSvarCommonService.isUnhandled).toHaveBeenCalledWith(qaAnswered);
            expect(fragaSvarCommonService.fromFk).toHaveBeenCalledWith(qaAnswered);


            expect(hasUnhandled).toBeTruthy();

        });

    });

    describe('#updateAnsweredAsHandled', function() {
        it('has no UnhandledQas so shouldnt update qas', function() {
            // ----- arrange
            var qaList = [];

            // ----- act
            $scope.updateAnsweredAsHandled(deferred, qaList);

            // ----- assert
            expect(fragaSvarService.closeAllAsHandled).not.toHaveBeenCalled();
        });

        it('has UnhandledQas so should update qas', function() {
            // ----- arrange
            var qaAnswered = {};
            var qaList = [qaAnswered];
            fragaSvarCommonService.isUnhandled.and.returnValue(true);
            fragaSvarCommonService.fromFk.and.returnValue(true);

            // ----- act
            $scope.updateAnsweredAsHandled(deferred, qaList);

            // ----- assert
            expect(fragaSvarService.closeAllAsHandled).toHaveBeenCalled();
        });

    });
});
