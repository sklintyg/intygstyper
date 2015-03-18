angular.module('fk7263').factory('fk7263.Domain.PatientModel',
    [function() {
        'use strict';

        var _patientModel;
        /**
         * Constructor, with class name
         */
        function PatientModel() {
            this.personId;
            this.fullstandigtNamn;
            this.fornamn;
            this.efternamn;
            this.postadress;
            this.postnummer;
            this.postort;
            this.samordningsNummer;
        }


        PatientModel.prototype.update = function(patient) {
            // refresh the model data
            if(patient === undefined) return;
            this.personId = patient.personId;
            this.fullstandigtNamn = patient.fullstandigtNamn;
            this.fornamn = patient.fornamn;
            this.efternamn = patient.efternamn;
            this.postadress = patient.postadress;
            this.postnummer = patient.postnummer;
            this.postort = patient.postort;
            this.samordningsNummer = patient.samordningsNummer;
        }

        PatientModel.build = function() {
            return new PatientModel();
        };

        _patientModel = PatientModel.build();
        /**
         * Return the constructor function PatientModel
         */
        return _patientModel;

    }]);