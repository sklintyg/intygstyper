angular.module('fk7263').factory('fk7263.Domain.GrundDataModel',
    [ 'fk7263.Domain.SkapadAvModel',
        'fk7263.Domain.PatientModel',  function(skapadAvModel, patientModel) {
        'use strict';

        var _grundData;

        /**
         * Constructor, with class name
         */
        function GrundDataModel() {
            this.skapadAv = skapadAvModel;
            this.patient = patientModel;
        }

        

        GrundDataModel.prototype.update = function (grundData) {
            // refresh the model data
            if(grundData === undefined) return;
            this.skapadAv.update(grundData.skapadAv);
            this.patient.update(grundData.patient);
        }

        GrundDataModel.build = function() {
            return new GrundDataModel();
        };

        _grundData = GrundDataModel.build();

        /**
         * Return the constructor function GrundDataModel
         */
        return _grundData;

    }]);