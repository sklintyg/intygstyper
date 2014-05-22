define([
    'angular',
    'webjars/common/webcert/js/services/CertificateService',
    'webjars/common/webcert/js/services/ManageCertView',
    'webjars/common/webcert/js/services/User'
], function(angular, CertificateService, ManageCertView, User) {
    'use strict';

    var moduleName = 'ts-diabetes.EditCertCtrl';

    angular.module(moduleName, [ CertificateService, ManageCertView, User ]).
        controller(moduleName, [ '$anchorScroll', '$location', '$log', '$scope',
            CertificateService, ManageCertView, User,
            function($anchorScroll, $location, $log, $scope, CertificateService, ManageCertView, User) {
                $scope.cert = {};

                $scope.messages = [];
                $scope.isComplete = false;
                $scope.isSigned = false;
                $scope.user = User;

                // init state
                $scope.widgetState = {
                    doneLoading: false,
                    hasError: false,
                    showComplete: false,
                    collapsedHeader: false
                };

                $scope.toggleHeader = function() {
                    $scope.widgetState.collapsedHeader = !$scope.widgetState.collapsedHeader;
                };

                $scope.toggleShowComplete = function() {
                    $scope.widgetState.showComplete = !$scope.widgetState.showComplete;
                    if ($scope.widgetState.showComplete) {

                        var old = $location.hash();
                        $location.hash('top');
                        $anchorScroll();
                        // reset to old to keep any additional routing logic from kicking in
                        $location.hash(old);
                    }
                };

                $scope.form = {
                    'identity': {
                        'ID-kort': 'ID_KORT',
                        'Företagskort eller tjänstekort': 'FORETAG_ELLER_TJANSTEKORT',
                        'Körkort': 'KORKORT',
                        'Personlig kännedom': 'PERS_KANNEDOM',
                        'Försäkran enligt 18 kap. 4§': 'FORSAKRAN_KAP18',
                        'Pass': 'PASS'
                    },
                    'korkorttypselected': false,
                    'behorighet': true
                };

                $scope.testerror = false;

                $scope.$watch('cert.intygAvser.korkortstyp', function(newValue) {
                    if (!$scope.cert || !$scope.cert.intygAvser || !$scope.cert.intygAvser.korkortstyp) {
                        return;
                    }
                    $scope.form.korkortd = false;
                    for (var i = 4; i < $scope.cert.intygAvser.korkortstyp.length; i++) {
                        if (newValue[i].selected) {
                            $scope.form.korkortd = true;
                            break;
                        }
                    }
                }, true);

                $scope.$watch('form.behorighet', function(newValue) {
                    if (!$scope.cert || !$scope.cert.bedomning) {
                        return;
                    }
                    $scope.cert.bedomning.kanInteTaStallning = !newValue;
                }, true);

                $scope.cert = {};

                // Get the certificate draft from the server.
                $scope.cert = {};
                ManageCertView.load($scope);

                /**
                 * Action to save the certificate draft to the server.
                 */
                $scope.save = function() {
                    ManageCertView.save($scope);
                };

                /**
                 * Action to discard the certificate draft and return to WebCert again.
                 */
                $scope.discard = function() {
                    ManageCertView.discard($scope);
                };

                /**
                 * Action to sign the certificate draft and return to Webcert again.
                 */
                $scope.sign = function() {
                    ManageCertView.signera($scope, 'ts-diabetes');
                };
            }
        ]);

    return moduleName;
});
