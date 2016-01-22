angular.module('sjukersattning').controller('sjukersattning.EditCert.FormlyCtrl',
    ['$scope', 'sjukersattning.EditCertCtrl.ViewStateService',
        function FormlyCtrl($scope, ViewStateService) {
            'use strict';

            $scope.model = ViewStateService.intygModel;

            $scope.options = {
                formState:{}
            };

            $scope.formFields = [{
                wrapper: 'wc-field',
                templateOptions: { category: 1 },
                fieldGroup: [
                    { key: 'undersokningAvPatienten',        type: 'date', templateOptions: { label: 'KV_FKMU_0001.1' } },
                    { key: 'journaluppgifter',               type: 'date', templateOptions: { label: 'KV_FKMU_0001.3' } },
                    { key: 'anhorigsBeskrivningAvPatienten', type: 'date', templateOptions: { label: 'KV_FKMU_0001.4' } },
                    { key: 'annatGrundForMU',                type: 'date', templateOptions: { label: 'KV_FKMU_0001.5' } },
                    { key: 'annatGrundForMUBeskrivning',     type: 'single-text', className: 'dfr_1_3', hideExpression: '!model.annatGrundForMU', templateOptions: { label: 'DFR_1.3' } },
                    { key: 'kannedomOmPatient',              type: 'date', templateOptions: { label: 'DFR_2.1' } }
                ]
            },{
                wrapper: 'wc-field',
                templateOptions: { category: 2 },
                fieldGroup: [
                    { key: 'underlagFinns',   type: 'boolean', templateOptions: { label: 'DFR_3.1' } },
                    { key: 'underlag',        type: 'underlag', hideExpression: '!model.underlagFinns', templateOptions: { underlagsTyper: [1,2,3,4,5,6,7,9,10,11] } }
                ]
            },{
                wrapper: 'wc-field',
                templateOptions: { category: 3 },
                fieldGroup: [
                    { key: 'sjukdomsforlopp', type: 'multi-text', templateOptions: { label: 'DFR_5.1' } }
                ]
            },{
                wrapper: 'wc-field',
                templateOptions: { category: 4 },
                fieldGroup: [
                    { key: 'diagnoser',               type: 'diagnos' },
                    { key: 'diagnosgrund',            type: 'multi-text', templateOptions: { label: 'DFR_7.1' } },
                    { key: 'nyBedomningDiagnosgrund', type: 'boolean', templateOptions: { label: 'DFR_7.2' } }
                ]
            },{
                wrapper: 'wc-field',
                templateOptions: { category: 5 },
                fieldGroup: [
                    { key: 'funktionsnedsattningIntellektuell',      type: 'multi-text', templateOptions: { label: 'DFR_8.1' } },
                    { key: 'funktionsnedsattningKommunikation',      type: 'multi-text', templateOptions: { label: 'DFR_9.1' } },
                    { key: 'funktionsnedsattningKoncentration',      type: 'multi-text', templateOptions: { label: 'DFR_10.1' } },
                    { key: 'funktionsnedsattningPsykisk',            type: 'multi-text', templateOptions: { label: 'DFR_11.1' } },
                    { key: 'funktionsnedsattningSynHorselTal',       type: 'multi-text', templateOptions: { label: 'DFR_12.1' } },
                    { key: 'funktionsnedsattningBalansKoordination', type: 'multi-text', templateOptions: { label: 'DFR_13.1' } },
                    { key: 'funktionsnedsattningAnnan',              type: 'multi-text', templateOptions: { label: 'DFR_14.1' } }
                ]
            },{
                wrapper: 'wc-field',
                templateOptions: { category: 6 },
                fieldGroup: [{
                    key: 'aktivitetsbegransning',     type: 'multi-text', templateOptions: { label: 'DFR_17.1' }
                }]
            },{
                wrapper: 'wc-field',
                templateOptions: { category: 7 },
                fieldGroup: [
                    { key: 'avslutadBehandling',      type: 'multi-text', templateOptions: { label: 'DFR_18.1' } },
                    { key: 'pagaendeBehandling',      type: 'multi-text', templateOptions: { label: 'DFR_19.1' } },
                    { key: 'planeradBehandling',      type: 'multi-text', templateOptions: { label: 'DFR_20.1' } },
                    { key: 'substansintag',           type: 'multi-text', templateOptions: { label: 'DFR_21.1' } }
                ]
            },{
                wrapper: 'wc-field',
                templateOptions: { category: 8 },
                fieldGroup: [
                    { key: 'medicinskaForutsattningarForArbete', type: 'multi-text', templateOptions: { label: 'DFR_22.1' } },
                    { key: 'aktivitetsFormaga',                  type: 'multi-text', templateOptions: { label: 'DFR_23.1' } }
                ]
            },{
                wrapper: 'wc-field',
                templateOptions: { category: 9 },
                fieldGroup: [
                    { key: 'ovrigt',                  type: 'multi-text', templateOptions: { label: 'DFR_25.1' } }
                ]
            },{
                wrapper: 'wc-field',
                templateOptions: { category: 10 },
                fieldGroup: [
                    { key: 'kontaktMedFk', type: 'boolean', templateOptions: { label: 'DFR_26.1' } },
                    { key: 'anledningTillKontakt', type: 'multi-text', hideExpression: '!model.kontaktMedFk', templateOptions: { label: 'DFR_26.2' } }
                ]
            }];

        }
    ]);