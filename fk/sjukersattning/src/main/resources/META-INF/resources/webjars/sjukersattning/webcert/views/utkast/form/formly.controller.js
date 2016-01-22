angular.module('sjukersattning').controller('sjukersattning.EditCert.FormlyCtrl',
    ['$scope', 'sjukersattning.EditCertCtrl.ViewStateService',
        function FormlyCtrl($scope, ViewStateService) {
            'use strict';

            $scope.vm = {};
            var vm = $scope.vm; // vm stands for "View Model" --> see https://github.com/johnpapa/angular-styleguide#controlleras-with-vm
            vm.model = ViewStateService.intygModel;

            vm.options = {
                formState:{}
            };

            vm.userFields = [{
                wrapper: 'wc-field',
                templateOptions: { category: 1 },
                fieldGroup: [{
                    key: 'undersokningAvPatienten',
                    type: 'date',
                    templateOptions: {
                        label: 'KV_FKMU_0001.1'
                    }
                },{
                    key: 'journaluppgifter',
                    type: 'date',
                    templateOptions: {
                        label: 'KV_FKMU_0001.3'
                    }
                },{
                    key: 'anhorigsBeskrivningAvPatienten',
                    type: 'date',
                    templateOptions: {
                        label: 'KV_FKMU_0001.4'
                    }
                },{
                    key: 'annatGrundForMU',
                    type: 'date',
                    templateOptions: {
                        label: 'KV_FKMU_0001.5'
                    }
                },{
                    className: 'dfr_1_3',
                    key: 'annatGrundForMUBeskrivning',
                    type: 'single-text',
                    templateOptions: {
                        label: 'DFR_1.3.RBK'
                    },
                    hideExpression: '!model.annatGrundForMU'
                }]
            },{
                wrapper: 'wc-field',
                templateOptions: { category: 2 },
                fieldGroup: []
            },{
                wrapper: 'wc-field',
                templateOptions: { category: 3 },
                fieldGroup: []
            },{
                wrapper: 'wc-field',
                templateOptions: { category: 4 },
                fieldGroup: [{
                    key: 'diagnoser',
                    type: 'diagnos'
                }]
            }];

            vm.onSubmit = onSubmit;


            function onSubmit() {
                console.log('form submitted:', vm.user);
            }
        }
    ]);