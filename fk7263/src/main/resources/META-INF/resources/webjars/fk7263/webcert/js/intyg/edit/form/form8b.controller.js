angular.module('fk7263').controller('fk7263.EditCert.Form8bCtrl',
    ['$log', 'fk7263.Domain.IntygModel', '$scope', 'fk7263.EditCertCtrl.ViewStateService',
        'fk7263.EditCertCtrl.DateRangeGroupsService',
        function($log, model, $scope, viewState, DateRangeGroupsService) {
            'use strict';
            // private vars
            var _dateRangeGroups;

            // scope
            $scope.model = model;
            $scope.viewState = viewState;

            // 8b. Arbetsförmåga date management
            $scope.field8b = {
                nedsattMed25 : null,
                nedsattMed50 : null,
                nedsattMed75 : null,
                nedsattMed100 : null,
                onChangeWorkStateCheck : function(nedsattModelName) {
                    if(_dateRangeGroups){
                        _dateRangeGroups.onChangeWorkStateCheck(nedsattModelName);
                    }

                    if (!$scope.field8b[nedsattModelName].workState) {
                        this[nedsattModelName + 'Beskrivning'] = null;
                    }

                }
            };

            $scope.datesOutOfRange = false;
            $scope.datesPeriodTooLong = false;
            $scope.totalCertDays = false;


            $scope.$watch('viewState.common.doneLoading', function(newVal) {
                if (newVal) {
                    registerDateParsersFor8b($scope);
                    _dateRangeGroups.validateDatesWithCert($scope.cert);
                }
            });

            function registerDateParsersFor8b(_$scope) {
                if(_dateRangeGroups === undefined){
                    _dateRangeGroups = DateRangeGroupsService.build(_$scope);
                }
                _dateRangeGroups.validateDatesWithCert(_$scope.cert);
            }
        }]);