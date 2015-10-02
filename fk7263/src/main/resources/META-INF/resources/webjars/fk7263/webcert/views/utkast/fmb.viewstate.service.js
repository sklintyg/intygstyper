angular.module('fk7263').service('fk7263.fmb.ViewStateService',
        function() {
            'use strict';

            this.state = {
                formData: [],
                diagnosKod: undefined
            };

            this.reset = function() {
                this.state.formData = [];
                this.state.diagnosKod = undefined;
                return this;
            };

            var transformFormData = function transformFormData(formData) {
                var transformedFormData = {};
                formData.forms.forEach(function(item) {
                    transformedFormData[item.name] = item.content;
                });

                return transformedFormData;
            };

            this.setState = function(formData, diagnosKod){
                this.state.formData = transformFormData(formData);
                this.state.diagnosKod = diagnosKod;
            }

            this.reset();
        });
