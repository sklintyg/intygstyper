define([ 'angular' ], function(angular) {
    'use strict';

    return ['$scope', '$log', '$location', '$anchorScroll', '$routeParams', 'CertificateService', 'ManageCertView',
        function($scope, $log, $location, $anchorScroll, $routeParams, CertificateService, ManageCertView) {
            $scope.cert = {};

            $scope.messages = [];
            $scope.isComplete = false;

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
            // TODO: Hide the form until the draft has been loaded.
            CertificateService.getDraft($routeParams.certificateId, function(data) {
                $scope.cert = data.content;
            }, function() {
                // TODO: Show error message.
            });

            /**
             * Action to save the certificate draft to the server.
             */
            $scope.save = function() {
                CertificateService.saveDraft($routeParams.certificateId, $scope.cert, function(data) {

                    $scope.certForm.$setPristine();

                    $scope.validationMessagesGrouped = {};
                    $scope.validationMessages = [];

                    if (data.status === 'COMPLETE') {
                        $scope.isComplete = true;
                    } else {
                        $scope.isComplete = false;
                        $scope.validationMessages = data.messages;

                        angular.forEach(data.messages, function(message) {
                            var field = message.field;
                            var parts = field.split('.');
                            var section;
                            if (parts.length > 0) {
                                section = parts[0].toLowerCase();

                                if ($scope.validationMessagesGrouped[section]) {
                                    $scope.validationMessagesGrouped[section].push(message);
                                } else {
                                    $scope.validationMessagesGrouped[section] = [ message ];
                                }
                            }
                        });
                    }
                }, function() {
                    // TODO: Show error message.
                });
            };

            /**
             * Action to discard the certificate draft and return to WebCert again.
             */
            $scope.discard = function() {
                ManageCertView.discard($scope);
            };
        }
    ];
});
