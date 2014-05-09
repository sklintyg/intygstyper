define([
    'angular',
    'angularMocks',
    'common/js/webcert/CertificateService',
    'common/js/webcert/ManageCertView'
], function(angular, mocks, CertificateServiceModule, ManageCertViewModule) {
    'use strict';

    describe('ManageCertView', function() {

        var ManageCertView;
        var $httpBackend;
        var $timeout;
        var wcDialogService;

        beforeEach(mocks.module(ManageCertViewModule, CertificateServiceModule, function($provide) {
            $provide.value('$route', jasmine.createSpyObj('$route', [ 'refresh' ]));
            $provide.value('$routeParams', {});
            $provide.value('wcDialogService', jasmine.createSpyObj('wcDialogService', [ 'showDialog' ]));
            $provide.value('statService', jasmine.createSpyObj('statService', [ 'refreshStat' ]));
        }));

        beforeEach(mocks.inject(function(_ManageCertView_, _$httpBackend_, _$timeout_, _wcDialogService_) {
            ManageCertView = _ManageCertView_;
            $httpBackend = _$httpBackend_;
            $timeout = _$timeout_;
            wcDialogService = _wcDialogService_;
        }));

        describe('#confirmSign', function() {

            it('should call onSuccess if the server accepts request to sign without delay', function() {

                var intygId = 123, biljettId = 12345;
                var $scope = { dialog: {} };
                var onSuccess = jasmine.createSpy('onSuccess');

                $httpBackend.expectPOST('/moduleapi/intyg/signera/' + intygId).
                    respond(200, { id: biljettId, status: 'BEARBETAR' });
                $httpBackend.expectGET('/moduleapi/intyg/signera/status/' + biljettId).
                    respond(200, { id: biljettId, status: 'SIGNERAD' });

                ManageCertView.__test__.confirmSign(intygId, $scope, onSuccess);
                $httpBackend.flush();

                expect(onSuccess).toHaveBeenCalled();
                expect($scope.dialog.acceptprogressdone).toBeTruthy();
                expect($scope.dialog.showerror).toBeFalsy();
            });

            it('should call onSuccess if the server accepts request to sign with delay', function() {

                var intygId = 123, biljettId = 12345;
                var $scope = { dialog: {} };
                var onSuccess = jasmine.createSpy('onSuccess');

                $httpBackend.expectPOST('/moduleapi/intyg/signera/' + intygId).
                    respond(200, { id: biljettId, status: 'BEARBETAR' });
                $httpBackend.expectGET('/moduleapi/intyg/signera/status/' + biljettId).
                    respond(200, { id: biljettId, status: 'BEARBETAR' });

                ManageCertView.__test__.confirmSign(intygId, $scope, onSuccess);
                $httpBackend.flush();

                $httpBackend.expectGET('/moduleapi/intyg/signera/status/' + biljettId).
                    respond(200, { id: biljettId, status: 'SIGNERAD' });
                $timeout.flush();
                $httpBackend.flush();

                expect(onSuccess).toHaveBeenCalled();
                expect($scope.dialog.acceptprogressdone).toBeTruthy();
                expect($scope.dialog.showerror).toBeFalsy();
            });

            it('should call show an error if the server refuses the request to sign', function() {

                var intygId = 123;
                var $scope = { dialog: {} };
                var onSuccess = jasmine.createSpy('onSuccess');

                $httpBackend.expectPOST('/moduleapi/intyg/signera/' + intygId).
                    respond(500, { errorCode: 'DATA_NOT_FOUND' });

                ManageCertView.__test__.confirmSign(intygId, $scope, onSuccess);
                $httpBackend.flush();

                expect(onSuccess).not.toHaveBeenCalled();
                expect($scope.dialog.acceptprogressdone).toBeTruthy();
                expect($scope.dialog.showerror).toBeTruthy();
            });

            it('should show an error if the server returns an unknown status', function() {

                var intygId = 123, biljettId = 12345;
                var $scope = { dialog: {} };
                var onSuccess = jasmine.createSpy('onSuccess');

                $httpBackend.expectPOST('/moduleapi/intyg/signera/' + intygId).
                    respond(200, { id: biljettId, status: 'ERROR' });
                $httpBackend.expectGET('/moduleapi/intyg/signera/status/' + biljettId).
                    respond(200, { id: biljettId, status: 'ERROR' });

                ManageCertView.__test__.confirmSign(intygId, $scope, onSuccess);
                $httpBackend.flush();

                expect(onSuccess).not.toHaveBeenCalled();
                expect($scope.dialog.acceptprogressdone).toBeTruthy();
                expect($scope.dialog.showerror).toBeTruthy();
            });
        });

        describe('#sign', function() {

            it('should open dialog', function() {

                var $scope = {};
                ManageCertView.sign($scope);

                expect(wcDialogService.showDialog).toHaveBeenCalledWith($scope, jasmine.any(Object));
            });
        });
    });
});
