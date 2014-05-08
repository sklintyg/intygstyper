define([ 'angular' ], function(angular) {
    'use strict';

    return ['$scope', '$log', '$location', '$anchorScroll', '$routeParams', 'CertificateService', 'ManageCertView', 'User',
        function($scope, $log, $location, $anchorScroll, $routeParams, CertificateService, ManageCertView, User) {
            $scope.cert = {};

            $scope.messages = [];
            $scope.isComplete = false;
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
                    //reset to old to keep any additional routing logic from kicking in
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
                'korkortd': false,
                'behorighet': true
            };

            $scope.testerror = false;

            // Input limit handling
            $scope.inputLimits = {
                'funktionsnedsattning': 180,
                'beskrivningRiskfaktorer': 180,
                'medvetandestorning': 180,
                'lakemedelOchDos': 180,
                'medicinering': 180,
                'kommentar': 500,
                'lakareSpecialKompetens': 270,
                'sjukhusvardtidpunkt': 49,
                'sjukhusvardvardinrattning': 45,
                'sjukhusvardanledning': 63
            };

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

            $scope.cert = {};

            // Get the certificate draft from the server.
            // TODO: Hide the form until the draft has been loaded.
            CertificateService.getDraft($routeParams.certificateId, function(data) {
                $scope.cert = data.content;
                $scope.isComplete = data.status === 'DRAFT_COMPLETE';
            }, function() {
                // TODO: Show error message.
            });

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
                var bodyText = 'Är du säker på att du vill signera utkastet?';
                wcDialogService.showDialog($scope, {
                    dialogId: 'confirm-sign',
                    titleId: 'label.confirmsign',
                    bodyText: bodyText,

                    button1click: function() {
                        $log.debug('sign draft ');
                        $scope.dialog.acceptprogressdone = false;
                        certificateService.signDraft($routeParams.certificateId, function(data) {
                            $log.debug(data);
                            $scope.dialog.acceptprogressdone = true;
                        }, function(data) {
                            $log.debug(data);
                            $scope.dialog.acceptprogressdone = true;
                        });
                    },
                    button1text: 'common.sign',
                    button2text: 'common.cancel'
                });



            };
        }
    ];
});
