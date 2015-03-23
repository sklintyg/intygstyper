angular.module('fk7263').factory('fk7263.Domain.IntygModel',
    ['fk7263.Domain.GrundDataModel', 'common.domain.DraftModel', function(grundData, draftModel) {
        'use strict';

        var _intygModel;

        /**
         * Constructor, with class name
         */
        function IntygModel() {

            // field 1
            this.avstangningSmittskydd = undefined;

            // field 2
            this.diagnosBeskrivning = undefined;
            this.diagnosBeskrivning1 = undefined;
            this.diagnosBeskrivning2 = undefined;
            this.diagnosBeskrivning3 = undefined;
            this.diagnosKod = undefined;
            this.diagnosKod2 = undefined;
            this.diagnosKod3 = undefined;
            this.diagnosKodsystem1 = undefined;
            this.diagnosKodsystem2 = undefined;
            this.diagnosKodsystem3 = undefined;
            this.samsjuklighet = undefined;

            // field 3
            this.sjukdomsforlopp = undefined;

            // field 4
            this.funktionsnedsattning = undefined;

            // field 4b
            this.annanReferens = undefined;
            this.annanReferensBeskrivning = undefined;
            this.journaluppgifter = undefined;
            this.telefonkontaktMedPatienten = undefined;
            this.undersokningAvPatienten = undefined;

            // field 5
            this.aktivitetsbegransning = undefined;

            // field 6a
            this.rekommendationKontaktArbetsformedlingen = undefined;
            this.rekommendationKontaktForetagshalsovarden = undefined;
            this.rekommendationOvrigt = undefined;
            this.rekommendationOvrigtCheck = undefined;

            // field 7
            this.rehab = undefined;

            // field 11
            this.ressattTillArbeteAktuellt = undefined;
            this.ressattTillArbeteEjAktuellt = undefined;

            // field 6b
            this.atgardInomSjukvarden = undefined;
            this.annanAtgard = undefined;

            // field 8a
            this.arbetsloshet = undefined;
            this.foraldrarledighet = undefined;
            this.nuvarandeArbete = undefined;
            this.nuvarandeArbetsuppgifter = undefined;

            // field 8b
            this.tjanstgoringstid = undefined;
            this.nedsattMed100 = undefined;
            this.nedsattMed25 = undefined;
            this.nedsattMed25Beskrivning = undefined;
            this.nedsattMed50 = undefined;
            this.nedsattMed50Beskrivning = undefined;
            this.nedsattMed75 = undefined;
            this.nedsattMed75Beskrivning = undefined;

            // field 9
            this.arbetsformagaPrognos = undefined;

            // field 10
            this.arbetsformagaPrognosGarInteAttBedomaBeskrivning = undefined;
            this.prognosBedomning = undefined;

            // field 12
            this.kontaktMedFk = undefined;

            // field 13
            this.kommentar = undefined;

            // field 15
            // vardenhets model

            this.grundData = grundData;

            this.forskrivarkodOchArbetsplatskod = undefined;
            this.namnfortydligandeOchAdress = undefined;
            this.rehabilitering = undefined;

            this.id = undefined;
        }

        // override the original update method
        IntygModel.prototype.update = function(content) {
            // refresh the model data

            if(content !== undefined) {
                this.aktivitetsbegransning = content.aktivitetsbegransning;
                this.annanAtgard = content.annanAtgard;
                this.annanReferens = content.annanReferens;
                this.annanReferensBeskrivning = content.annanReferensBeskrivning;
                this.arbetsformagaPrognos = content.arbetsformagaPrognos;
                this.arbetsloshet = content.arbetsloshet;
                this.atgardInomSjukvarden = content.atgardInomSjukvarden;
                this.avstangningSmittskydd = content.avstangningSmittskydd;
                this.diagnosBeskrivning = content.diagnosBeskrivning;
                this.diagnosBeskrivning1 = content.diagnosBeskrivning1;
                this.diagnosBeskrivning2 = content.diagnosBeskrivning2;
                this.diagnosBeskrivning3 = content.diagnosBeskrivning3;
                this.diagnosKod = content.diagnosKod;
                this.diagnosKod2 = content.diagnosKod2;
                this.diagnosKod3 = content.diagnosKod3;
                this.diagnosKodsystem1 = content.diagnosKodsystem1;
                this.diagnosKodsystem2 = content.diagnosKodsystem2;
                this.diagnosKodsystem3 = content.diagnosKodsystem3;
                this.forskrivarkodOchArbetsplatskod = content.forskrivarkodOchArbetsplatskod;
                this.funktionsnedsattning = content.funktionsnedsattning;
                this.foraldrarledighet = content.foraldrarledighet;
                this.grundData.update(content.grundData);
                this.journaluppgifter = content.journaluppgifter;
                this.kommentar = content.kommentar;
                this.kontaktMedFk = content.kontaktMedFk;
                this.namnfortydligandeOchAdress = content.namnfortydligandeOchAdress;
                this.nedsattMed100 = this.getNedsatt(content.nedsattMed100);
                this.nedsattMed25 = this.getNedsatt(content.nedsattMed25);
                this.nedsattMed25Beskrivning = content.nedsattMed25Beskrivning;
                this.nedsattMed50 = this.getNedsatt(content.nedsattMed50);
                this.nedsattMed50Beskrivning = content.nedsattMed50Beskrivning;
                this.nedsattMed75 = this.getNedsatt(content.nedsattMed75);
                this.nedsattMed75Beskrivning = content.nedsattMed75Beskrivning;
                this.nuvarandeArbete = content.nuvarandeArbete;
                this.nuvarandeArbetsuppgifter = content.nuvarandeArbetsuppgifter;
                this.prognosBedomning = content.prognosBedomning;
                this.rehabilitering = content.rehabilitering;
                this.rekommendationKontaktArbetsformedlingen = content.rekommendationKontaktArbetsformedlingen;
                this.rekommendationKontaktForetagshalsovarden = content.rekommendationKontaktForetagshalsovarden;
                this.rekommendationOvrigt = content.rekommendationOvrigt;
                this.rekommendationOvrigtCheck = content.rekommendationOvrigtCheck;
                this.ressattTillArbeteAktuellt = content.ressattTillArbeteAktuellt;
                this.ressattTillArbeteEjAktuellt = content.ressattTillArbeteEjAktuellt;
                this.samsjuklighet = content.samsjuklighet;
                this.sjukdomsforlopp = content.sjukdomsforlopp;
                this.telefonkontaktMedPatienten = content.telefonkontaktMedPatienten;
                this.tjanstgoringstid = content.tjanstgoringstid;
                this.undersokningAvPatienten = content.undersokningAvPatienten;
                this.id = content.id;

                // maybe special cases
                this.arbetsformagaPrognosGarInteAttBedomaBeskrivning =
                    content.arbetsformagaPrognosGarInteAttBedomaBeskrivning;
                this.rehab = content.rehab;
            }
        };

        IntygModel.prototype.getNedsatt = function(nedsatt){
            if(!nedsatt || (!nedsatt.from && !nedsatt.tom)){
                return undefined;
            } else {
                return nedsatt;
            }
        };

        IntygModel.prototype.prepare = function(nedsatt){
            if(this.aktivitetsbegransning && this.aktivitetsbegransning.length === 0){
                this.aktivitetsbegransning = undefined;
            }
            if(this.samsjuklighet && this.samsjuklighet.length === 0){
                this.samsjuklighet = undefined;
            }
            if(this.funktionsnedsattning && this.funktionsnedsattning.length === 0){
                this.funktionsnedsattning = undefined;
            }
        };

        IntygModel.build = function() {
            return new IntygModel();
        };

        _intygModel = IntygModel.build();

        draftModel.content = _intygModel;

        /**
         * Return the constructor function IntygModel
         */
        return _intygModel;

    }]);