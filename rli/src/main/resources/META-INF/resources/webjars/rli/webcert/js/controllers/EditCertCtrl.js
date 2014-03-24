define([
], function () {
    'use strict';

	return [ '$scope', '$location', '$anchorScroll', 'rli.certificateService', '$routeParams',
			function($scope, $location, $anchorScroll, certificateService, $routeParams) {
				$scope.cert = {};

				$scope.messages = [];
				$scope.isComplete = false;

				// init state
				$scope.widgetState = {
					doneLoading : false,
					hasError : false,
					showComplete : false,
					collapsedHeader : false
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
					'identity' : {
						'ID-kort' : 'ID_KORT',
						'Företagskort eller tjänstekort' : 'FORETAG_ELLER_TJANSTEKORT',
						'Körkort' : 'KORKORT',
						'Personlig kännedom' : 'PERS_KANNEDOM',
						'Försäkran enligt 18 kap. 4§' : 'FORSAKRAN_KAP18',
						'Pass' : 'PASS'
					},
					'korkorttypselected' : false,
					'behorighet' : true
				};

				$scope.testerror = false;

				$scope.$watch('cert.intygAvser.korkortstyp', function(newValue, oldValue) {
					if (!$scope.cert || !$scope.cert.intygAvser || !$scope.cert.intygAvser.korkortstyp)
						return;
					$scope.form.korkortd = false;
					for ( var i = 4; i < $scope.cert.intygAvser.korkortstyp.length; i++) {
						if (newValue[i].selected) {
							$scope.form.korkortd = true;
							break;
						}
					}
				}, true);

				$scope.$watch('form.behorighet', function(newValue, oldValue) {
					if (!$scope.cert || !$scope.cert.bedomning)
						return;
					$scope.cert.bedomning.kanInteTaStallning = !newValue;
				}, true);

				var dummycert = {
					"utlatandeid" : "987654321",
					"typAvUtlatande" : "TSTRK1031 (U06, V02)",
					"signeringsdatum" : "2013-08-12T15:57:00.000",
					"skapadAv" : {
						"personid" : "SE0000000000-1333",
						"fullstandigtNamn" : "Doktor Thompson",
						"vardenhet" : {
							"enhetsid" : "SE0000000000-1337",
							"enhetsnamn" : "Vårdenhet Väst",
							"postadress" : "Enhetsvägen 12",
							"postnummer" : "54321",
							"postort" : "Tumba",
							"telefonnummer" : "08-1337",
							"vardgivare" : {
								"vardgivarid" : "SE0000000000-HAHAHHSAA",
								"vardgivarnamn" : "Vårdgivarnamn"
							}
						}
					},
					"patient" : {
						"personid" : "19121212-1212",
						"fullstandigtNamn" : "Johnny Appleseed",
						"fornamn" : "Johnny",
						"efternamn" : "Appleseed",
						"postadress" : "Testvägen 12",
						"postnummer" : "123456",
						"postort" : "Testort"
					},
					"vardkontakt" : {
						"typ" : "5880005",
						"idkontroll" : "PASS"
					},
					"intygAvser" : {
						"korkortstyp" : [ {
							"type" : "C1",
							"selected" : false
						}, {
							"type" : "C1E",
							"selected" : false
						}, {
							"type" : "C",
							"selected" : true
						}, {
							"type" : "CE",
							"selected" : false
						}, {
							"type" : "D1",
							"selected" : false
						}, {
							"type" : "D1E",
							"selected" : false
						}, {
							"type" : "D",
							"selected" : false
						}, {
							"type" : "DE",
							"selected" : false
						}, {
							"type" : "TAXI",
							"selected" : false
						} ]
					},
					"diabetes" : {
						"diabetestyp" : "DIABETES_TYP_2",
						"observationsperiod" : "2012",
						"endastKost" : true,
						"tabletter" : true,
						"insulin" : true,
						"insulinBehandlingsperiod" : "2012",
						"annanBehandling" : true,
						"annanBehandlingBeskrivning" : "Hypnos"
					},
					"hypoglykemier" : {
						"kunskapOmAtgarder" : true,
						"teckenNedsattHjarnfunktion" : true,
						"saknarFormagaKannaVarningstecken" : true,
						"allvarligForekomst" : true,
						"allvarligForekomstBeskrivning" : "Beskrivning",
						"allvarligForekomstTrafiken" : true,
						"allvarligForekomstTrafikBeskrivning" : "Beskrivning",
						"allvarligForekomstVakenTid" : true,
						"allvarligForekomstVakenTidObservationstid" : "2012-12-12",
						"egenkontrollBlodsocker" : true
					},
					"syn" : {
						"separatOgonlakarintyg" : true,
						"synfaltsprovningUtanAnmarkning" : false,
						"hoger" : {
							"utanKorrektion" : 0.1,
							"medKorrektion" : 0.1
						},
						"vanster" : {
							"utanKorrektion" : 0.1,
							"medKorrektion" : 0.1
						},
						"binokulart" : {
							"utanKorrektion" : 0.1,
							"medKorrektion" : 0.1
						},
						"diplopi" : true,
						"synfaltsprovning" : true,
						"provningOgatsRorlighet" : true
					},
					"bedomning" : {
						"korkortstyp" : [ {
							"type" : "C1",
							"selected" : false
						}, {
							"type" : "C1E",
							"selected" : false
						}, {
							"type" : "C",
							"selected" : true
						}, {
							"type" : "CE",
							"selected" : false
						}, {
							"type" : "D1",
							"selected" : false
						}, {
							"type" : "D1E",
							"selected" : false
						}, {
							"type" : "D",
							"selected" : false
						}, {
							"type" : "DE",
							"selected" : false
						}, {
							"type" : "TAXI",
							"selected" : false
						} ],
						"lakareSpecialKompetens" : "Kronologisk bastuberedning",
						"lamplighetInnehaBehorighet" : true
					}
				};

				$scope.cert = {};

				// Get the certificate draft from the server.
				// TODO: Hide the form until the draft has been loaded.
				certificateService.getDraft($routeParams.certificateId, function(data) {
					$scope.cert = data.content;
				}, function(errorData) {
					// TODO: Show error message.
				});

				/**
				 * Action to save the certificate draft to the server.
				 */
				$scope.save = function() {
					certificateService.saveDraft($routeParams.certificateId, $scope.cert, function(data) {

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
								var parts = field.split(".");
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
					}, function(errorData) {
						// TODO: Show error message.
					});
				};

				/**
				 * Action to discard the certificate draft and return to WebCert again.
				 */
				$scope.discard = function() {
					certificateService.discardDraft($routeParams.certificateId, function(data) {
						// TODO: Redirect back to start page.
					}, function(errorData) {
						// TODO: Show error message.
					});
				};
			} ];
});
