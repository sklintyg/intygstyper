describe('qaPanel', function() {
    'use strict';

    var element;
    var $httpBackend;
    var $scope;
    var $rootScope;
    var fragaSvarCommonService;
    var fragaSvarService;
    var ManageCertView;
    var deferred;

    // Load the webcert module and mock away everything that is not necessary.
    beforeEach(angular.mock.module('fk7263', function($provide) {
        $provide.value('common.dialogService', {});
        fragaSvarCommonService = jasmine.createSpyObj('common.fragaSvarCommonService', [ 'isUnhandled', 'fromFk', 'setVidareBefordradState' ]);
        $provide.value('common.fragaSvarCommonService', fragaSvarCommonService);
        $provide.value('common.ManageCertView', { isSentToTarget: function() {} });
        $provide.value('common.User', {});
        $provide.value('common.statService', {});

        fragaSvarService = jasmine.createSpyObj('fk7263.fragaSvarService',
            [ 'getQAForCertificate', 'closeAsHandled', 'closeAllAsHandled', 'saveNewQuestion', 'saveAnswer']);
        $provide.value('fk7263.fragaSvarService', fragaSvarService);
    }));

    beforeEach(angular.mock.module('htmlTemplates'));

    beforeEach(angular.mock.inject(['$controller', '$compile', '$rootScope', '$q', '$httpBackend', 'common.ManageCertView',
        function($controller, $compile, _$rootScope_, _$q_, _$httpBackend_, _ManageCertView_) {
            $rootScope = _$rootScope_;
            $scope = $rootScope.$new();

            $httpBackend = _$httpBackend_;
            ManageCertView = _ManageCertView_;

            element = angular.element('<div qa-panel panel-id="handled" type="handled" qa="qa" qa-list="qaList" cert="cert" cert-properties="certProperties"></div>');
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