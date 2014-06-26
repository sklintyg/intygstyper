define([
    'angular',
    'webjars/common/webcert/js/services/CertificateService',
    'webjars/common/webcert/js/services/ManageCertView',
    'webjars/common/webcert/js/services/User'
], function(angular, CertificateService, ManageCertView, User) {
    'use strict';

    var moduleName = 'ts-diabetes.EditCertCtrl';

    angular.module(moduleName, [ CertificateService, ManageCertView, User ]).
        controller(moduleName, [ '$anchorScroll', '$location', '$log', '$scope', '$window',
            CertificateService, ManageCertView, User,
            function($anchorScroll, $location, $log, $scope, $window, CertificateService, ManageCertView, User) {
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
                    'identity': [
                        {label: 'ID-kort *', id: 'ID_KORT'},
                        {label: 'Företagskort eller tjänstekort **', id: 'FORETAG_ELLER_TJANSTEKORT'},
                        {label: 'Svenskt körkort', id: 'KORKORT'},
                        {label: 'Personlig kännedom', id: 'PERS_KANNEDOM'},
                        {label: 'Försäkran enligt 18 kap. 4§ ***', id: 'FORSAKRAN_KAP18'},
                        {label: 'Pass ****', id: 'PASS'}
                    ],
                    'korkorttypselected': false,
                    'behorighet': true
                };

                $scope.testerror = false;

                $scope.$watch('cert.intygAvser.korkortstyp', function(newValue) {
                    if (!$scope.cert || !$scope.cert.intygAvser || !$scope.cert.intygAvser.korkortstyp) {
                        return;
                    }

                    // C1,C1E,C, CE,D1,D1E, D, DE och taxiförarlegitimation should activate 2g and 2f = korkortd = true
                    var targetTypes = ['C1', 'C1E', 'C', 'CE', 'D1', 'D1E', 'D', 'DE', 'TAXI'];
                    $scope.form.korkortd = false;
                    for (var i = 0; i < $scope.cert.intygAvser.korkortstyp.length; i++) {
                        for (var j = 0; j < targetTypes.length; j++) {
                            if (newValue[i].type === targetTypes[j] && newValue[i].selected) {
                                $scope.form.korkortd = true;
                                break;
                            }
                        }
                    }
                }, true);

                $scope.$watch('form.behorighet', function(newValue) {
                    if (!$scope.cert || !$scope.cert.bedomning) {
                        return;
                    }
                    $scope.cert.bedomning.kanInteTaStallning = !newValue;
                }, true);

                //Make a printable list of Befattningar (which as of yet consists of un-readable codes...)
                $scope.befattningar = '';
                $scope.$watch('user.userContext.befattningar', function (befattningar) {
                    if (befattningar === undefined) {
                        return;
                    }
                    $scope.befattningar = befattningar;
//                    var result = '';
//                    for (var i = 0; i < befattningar.length; i++) {
//                        if (i < befattningar.length-1) {
//                            result += befattningar[i] + (', ');
//                        } else {
//                            result += befattningar[i];
//                        }
//                    }
//                    $scope.befattningar = result;
                }, true);

                //Make a printable list of Specialiteter
                $scope.specialiteter = '';
                $scope.$watch('user.userContext.specialiseringar', function (specialiteter) {
                    if (specialiteter === undefined) {
                        return;
                    }
                    var result = '';
                    for (var i = 0; i < specialiteter.length; i++) {
                        if (i < specialiteter.length-1) {
                            result += specialiteter[i] + (', ');
                        } else {
                            result += specialiteter[i];
                        }
                    }
                    $scope.specialiteter = result;
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

                /**
                 * Print draft
                 */
                $scope.print = function() {
                    $window.print();
                };
            }
        ]);

    return moduleName;
});
