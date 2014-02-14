'use strict';

/* Controllers */
var controllers = angular.module('wc.ts-bas.controllers', []);

controllers.controller('EditCertCtrl', [ '$scope', 'certificateService',
    function ($scope, certificateService) {
        $scope.cert = {};
        $scope.doneLoading = true;
        $scope.displayLoader = false;

        $scope.form = {
            'identity' : {
                'ID-kort' : 'ID_KORT',
                'Företagskort eller tjänstekort' : 'FORETAG_ELLER_TJANSTEKORT',
                'Körkort' : 'KORKORT',
                'Personlig kännedom' : 'PERS_KANNEDOM',
                'Försäkran enligt 18 kap. 4§' : 'FORSAKRAN_KAP18',
                'Pass' : 'PASS'
            },
            'korkortd' : false,
            'behorighet' : true
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

        $scope.$watch('cert.intygAvser.korkortstyp', function(newValue, oldValue){
          if(!$scope.cert || !$scope.cert.intygAvser || !$scope.cert.intygAvser.korkortstyp) return;
          $scope.form.korkortd = false;
          for(var i = 4; i < $scope.cert.intygAvser.korkortstyp.length; i++){
            if(newValue[i].selected){
              $scope.form.korkortd = true;
              break;
            }
          }
        });

        var dummycert = {
            "utlatandeid" : "987654321",
            "typAvUtlatande" : "TSTRK1007 (U06, V06)",
            "signeringsdatum" : "2013-08-12T15:57:00.000",
            "kommentar" : "Här kommer en övrig kommentar",
            "skapadAv" : {
                "personid" : "SE0000000000-1333",
                "fullstandigtNamn" : "Doktor Thompson",
                "specialiteter" : ["SPECIALITET"],
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
                },
                "befattningar" : []
            },
            "patient" : {
                "personid" : "19121212-1212",
                "fullstandigtNamn" : "Herr Dundersjuk",
                "fornamn" : "Herr",
                "efternamn" : "Dundersjuk",
                "postadress" : "Testvägen 12",
                "postnummer" : "123456",
                "postort" : "Testort"
            },
            "vardkontakt" : {
                "typ" : "5880005",
                "idkontroll" : "PASS"
            },
            "intygAvser" : {
                "korkortstyp" : [
                    {"type" : "C1", "selected" : false},
                    {"type" : "C1E", "selected" : false},
                    {"type" : "C", "selected" : true},
                    {"type" : "CE", "selected" : false},
                    {"type" : "D1", "selected" : false},
                    {"type" : "D1E", "selected" : false},
                    {"type" : "D", "selected" : false},
                    {"type" : "DE", "selected" : false},
                    {"type" : "TAXI", "selected" : false}
                ]
            },
            "syn" : {
                "synfaltsdefekter" : true,
                "nattblindhet" : true,
                "progressivOgonsjukdom" : true,
                "diplopi" : true,
                "nystagmus" : true,
                "hogerOga" : {
                    "utanKorrektion" : 0.0,
                    "medKorrektion" : 0.0,
                    "kontaktlins" : true
                },

                "vansterOga" : {
                    "utanKorrektion" : 0.0,
                    "medKorrektion" : 0.0,
                    "kontaktlins" : true
                },

                "binokulart" : {
                    "utanKorrektion" : 0.0,
                    "medKorrektion" : 0.0
                },
                "korrektionsglasensStyrka" : true
            },

            "horselBalans" : {
                "balansrubbningar" : true,
                "svartUppfattaSamtal4Meter" : true
            },

            "funktionsnedsattning" : {
                "funktionsnedsattning" : true,
                "otillrackligRorelseformaga" : true,
                "beskrivning" : "Spik i foten"

            },

            "hjartKarl" : {
                "hjartKarlSjukdom" : true,
                "hjarnskadaEfterTrauma" : true,
                "riskfaktorerStroke" : true,
                "beskrivningRiskfaktorer" : "Förkärlek för Elivsmackor"
            },
            "diabetes" : {
                "harDiabetes" : true,
                "diabetesTyp" : "DIABETES_TYP_1",
                "kost" : true
            },

            "neurologi" : {
                "neurologiskSjukdom" : true
            },

            "medvetandestorning" : {
                "medvetandestorning" : true,
                "beskrivning" : "Beskrivning"
            },

            "njurar" : {
                "nedsattNjurfunktion" : true
            },

            "kognitivt" : {
                "sviktandeKognitivFunktion" : true
            },

            "somnVakenhet" : {
                "teckenSomnstorningar" : true
            },

            "narkotikaLakemedel" : {
                "teckenMissbruk" : true,
                "foremalForVardinsats" : true,
                "lakarordineratLakemedelsbruk" : true,
                "lakemedelOchDos" : "Läkemedel och dos",
                "provtagningBehovs" : true
            },

            "psykiskt" : {
                "psykiskSjukdom" : true
            },

            "utvecklingsstorning" : {
                "psykiskUtvecklingsstorning" : true,
                "harSyndrom" : true
            },

            "sjukhusvard" : {
                "sjukhusEllerLakarkontakt" : true,
                "tidpunkt" : "20 Januari",
                "vardinrattning" : "Vårdcentralen",
                "anledning" : "Akut lungsot"
            },

            "medicinering" : {
                "stadigvarandeMedicinering" : true,
                "beskrivning" : "Alvedon"
            },

            "bedomning" : {
                "korkortstyp" : [
                    {"type" : "C1", "selected" : false},
                    {"type" : "C1E", "selected" : false},
                    {"type" : "C", "selected" : true},
                    {"type" : "CE", "selected" : false},
                    {"type" : "D1", "selected" : false},
                    {"type" : "D1E", "selected" : false},
                    {"type" : "D", "selected" : false},
                    {"type" : "DE", "selected" : false},
                    {"type" : "TAXI", "selected" : false},
                    {"type" : "ANNAT", "selected" : false}
                ],
                "lakareSpecialKompetens" : "Spektralanalys"
            }
        };

        $scope.cert = {};

        // Get the certificate draft from the server.
        // TODO: Hide the form until the draft has been loaded.
        certificateService.getDraft($scope.MODULE_CONFIG.CERT_ID_PARAMETER,
            function (data) {
                $scope.cert = data.content;
                // TODO: set the cert from the data.
            }, function (errorData) {
                // TODO: Show error message.
        });

        /**
         * Action to save the certificate draft to the server.
         */
        $scope.save = function () {
            certificateService.saveDraft($scope.MODULE_CONFIG.CERT_ID_PARAMETER, $scope.cert,
                function (data) {
                    // TODO: Convert validation messages.
                },
                function (errorData) {
                    // TODO: Show error message.
                });
        };

        /**
         * Action to discard the certificate draft and return to WebCert again.
         */
        $scope.discard = function () {
            certificateService.discardDraft($scope.MODULE_CONFIG.CERT_ID_PARAMETER,
                function (data) {
                    // TODO: Redirect back to start page.
                },
                function (errorData) {
                    // TODO: Show error message.
                });
        };
    }]);
