angular.module('fk7263').factory('fk7263.Domain.IntygModel',
    ['fk7263.Domain.GrundDataModel', 'common.domain.DraftModel', function(grundData, draftModel) {
        'use strict';

        // private
        var _intygModel, _attic;


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

            if (content !== undefined) {
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

        IntygModel.prototype.getNedsatt = function(nedsatt) {
            if (!nedsatt || (!nedsatt.from && !nedsatt.tom)) {
                return undefined;
            } else {
                return nedsatt;
            }
        };

        IntygModel.prototype.prepare = function() {
            if (this.aktivitetsbegransning && this.aktivitetsbegransning.length === 0) {
                this.aktivitetsbegransning = undefined;
            }
            if (this.samsjuklighet && this.samsjuklighet.length === 0) {
                this.samsjuklighet = undefined;
            }
            if (this.funktionsnedsattning && this.funktionsnedsattning.length === 0) {
                this.funktionsnedsattning = undefined;
            }
        };

        // attic functions
        // form 2
        IntygModel.prototype.atticHasForm2 = function() {
            if (_attic) {
                if (_attic.diagnosKod || _attic.diagnosKod2 || _attic.diagnosKod3) {
                    return true;
                }
            }
            return false;
        };

        IntygModel.prototype.atticUpdateForm2 = function() {
            if (_attic) {
                _attic.diagnosBeskrivning = this.diagnosBeskrivning;
                _attic.diagnosBeskrivning1 = this.diagnosBeskrivning1;
                _attic.diagnosBeskrivning2 = this.diagnosBeskrivning2;
                _attic.diagnosBeskrivning3 = this.diagnosBeskrivning3;
                _attic.diagnosKod = this.diagnosKod;
                _attic.diagnosKod2 = this.diagnosKod2;
                _attic.diagnosKod3 = this.diagnosKod3;
                _attic.diagnosKodsystem1 = this.diagnosKodsystem1;
                _attic.diagnosKodsystem2 = this.diagnosKodsystem2;
                _attic.diagnosKodsystem3 = this.diagnosKodsystem3;
                _attic.samsjuklighet = this.samsjuklighet;
            }
        };

        IntygModel.prototype.atticRestoreForm2 = function() {
            if (_attic) {
                this.diagnosBeskrivning = _attic.diagnosBeskrivning;
                this.diagnosBeskrivning1 = _attic.diagnosBeskrivning1;
                this.diagnosBeskrivning2 = _attic.diagnosBeskrivning2;
                this.diagnosBeskrivning3 = _attic.diagnosBeskrivning3;
                this.diagnosKod = _attic.diagnosKod;
                this.diagnosKod2 = _attic.diagnosKod2;
                this.diagnosKod3 = _attic.diagnosKod3;
                this.diagnosKodsystem1 = _attic.diagnosKodsystem1;
                this.diagnosKodsystem2 = _attic.diagnosKodsystem2;
                this.diagnosKodsystem3 = _attic.diagnosKodsystem3;
                this.samsjuklighet = _attic.samsjuklighet;
            }
        };

        IntygModel.prototype.clearForm2 = function() {
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
            this.samsjuklighet = false;
        };

        // form3
        IntygModel.prototype.atticHasForm3 = function() {
            if (_attic) {
                if (_attic.sjukdomsforlopp) {
                    return true;
                }
            }
            return false;
        };

        IntygModel.prototype.atticUpdateForm3 = function() {
            if (_attic) {
                _attic.sjukdomsforlopp = this.sjukdomsforlopp;
            }
        };

        IntygModel.prototype.atticRestoreForm3 = function() {
            if (_attic) {
                this.sjukdomsforlopp = _attic.sjukdomsforlopp;
            }
        };

        IntygModel.prototype.clearForm3 = function() {
            this.sjukdomsforlopp = undefined;
        };

        // form4
        IntygModel.prototype.atticHasForm4 = function() {
            if (_attic) {
                if (_attic.funktionsnedsattning) {
                    return true;
                }
            }
            return false;
        };

        IntygModel.prototype.atticUpdateForm4 = function() {
            if (_attic) {
                _attic.funktionsnedsattning = this.funktionsnedsattning;
            }
        };

        IntygModel.prototype.atticRestoreForm4 = function() {
            if (_attic) {
                this.funktionsnedsattning = _attic.funktionsnedsattning;
            }
        };

        IntygModel.prototype.clearForm4 = function() {
            this.funktionsnedsattning = undefined;
        };

        // form4b
        IntygModel.prototype.atticHasForm4b = function() {
            if (_attic) {
                if (_attic.annanReferens || _attic.journaluppgifter || _attic.journaluppgifter || _attic.undersokningAvPatienten) {
                    return true;
                }
            }
            return false;
        };

        IntygModel.prototype.atticUpdateForm4b = function() {
            if (_attic) {
                 _attic.annanReferens = this.annanReferens;
                 _attic.annanReferensBeskrivning = this.annanReferensBeskrivning;
                 _attic.journaluppgifter = this.journaluppgifter;
                 _attic.telefonkontaktMedPatienten = this.telefonkontaktMedPatienten;
                 _attic.undersokningAvPatienten = this.undersokningAvPatienten;
            }
        };

        IntygModel.prototype.atticRestoreForm4b = function() {
            if (_attic) {
                this.annanReferens = _attic.annanReferens;
                this.annanReferensBeskrivning = _attic.annanReferensBeskrivning;
                this.journaluppgifter = _attic.journaluppgifter;
                this.telefonkontaktMedPatienten = _attic.telefonkontaktMedPatienten;
                this.undersokningAvPatienten = _attic.undersokningAvPatienten;
            }
        };

        IntygModel.prototype.clearForm4b = function() {
            this.annanReferens = undefined;
            this.annanReferensBeskrivning = undefined;
            this.journaluppgifter = undefined;
            this.telefonkontaktMedPatienten = undefined;
            this.undersokningAvPatienten = undefined;
        };

        // form5
        IntygModel.prototype.atticHasForm5 = function() {
            if (_attic) {
                if ( _attic.aktivitetsbegransning ) {
                    return true;
                }
            }
            return false;
        };

        IntygModel.prototype.atticUpdateForm5 = function() {
            if (_attic) {
                _attic.aktivitetsbegransning = this.aktivitetsbegransning;
            }
        };

        IntygModel.prototype.atticRestoreForm5 = function() {
            if (_attic) {
                this.aktivitetsbegransning = _attic.aktivitetsbegransning;
            }
        };

        IntygModel.prototype.clearForm5 = function() {
            this.aktivitetsbegransning = undefined;
        };

        // form6a711
        IntygModel.prototype.atticUpdateForm6a711 = function() {
            if (_attic) {
                this.atticUpdateForm6a();
                this.atticUpdateForm7();
                this.atticUpdateForm11();
            }
        };

        IntygModel.prototype.atticRestoreForm6a711 = function() {
            if (_attic) {
                this.atticRestoreForm6a();
                this.atticRestoreForm7();
                this.atticRestoreForm11();
            }
        };

        IntygModel.prototype.clearForm6a711 = function() {
            this.clearForm6a();
            this.clearForm7();
            this.clearForm11();
        };

        // form6a
        IntygModel.prototype.atticHasForm6a = function() {
            if (_attic) {
                if ( _attic.rekommendationKontaktArbetsformedlingen ||
                    _attic.rekommendationKontaktForetagshalsovarden ||
                    _attic.rekommendationOvrigt ||
                    _attic.rekommendationOvrigtCheck) {
                    return true;
                }
            }
            return false;
        };

        IntygModel.prototype.atticUpdateForm6a = function() {
            if (_attic) {
                _attic.rekommendationKontaktArbetsformedlingen = this.rekommendationKontaktArbetsformedlingen;
                _attic.rekommendationKontaktForetagshalsovarden = this.rekommendationKontaktForetagshalsovarden;
                _attic.rekommendationOvrigt = this.rekommendationOvrigt;
                _attic.rekommendationOvrigtCheck = this.rekommendationOvrigtCheck;
            }
        };

        IntygModel.prototype.atticRestoreForm6a = function() {
            if (_attic) {
                this.rekommendationKontaktArbetsformedlingen = _attic.rekommendationKontaktArbetsformedlingen;
                this.rekommendationKontaktForetagshalsovarden = _attic.rekommendationKontaktForetagshalsovarden;
                this.rekommendationOvrigt = _attic.rekommendationOvrigt;
                this.rekommendationOvrigtCheck = _attic.rekommendationOvrigtCheck;
            }
        };

        IntygModel.prototype.clearForm6a = function() {
            this.rekommendationKontaktArbetsformedlingen = undefined;
            this.rekommendationKontaktForetagshalsovarden = undefined;
            this.rekommendationOvrigt = undefined;
            this.rekommendationOvrigtCheck = undefined;
        };

        // form6b
        IntygModel.prototype.atticHasForm6b = function() {
            if (_attic) {
                if ( _attic.atgardInomSjukvarden || _attic.annanAtgard ) {
                    return true;
                }
            }
            return false;
        };

        IntygModel.prototype.atticUpdateForm6b = function() {
            if (_attic) {
                _attic.atgardInomSjukvarden = this.atgardInomSjukvarden;
                _attic.annanAtgard = this.annanAtgard;
            }
        };

        IntygModel.prototype.atticRestoreForm6b = function() {
            if (_attic) {
                this.atgardInomSjukvarden = _attic.atgardInomSjukvarden;
                this.annanAtgard = _attic.annanAtgard;
            }
        };

        IntygModel.prototype.clearForm6b = function() {
            this.atgardInomSjukvarden = undefined;
            this.annanAtgard = undefined;
        };

        // form7
        IntygModel.prototype.atticHasForm7 = function() {
            if (_attic) {
                if ( _attic.ressattTillArbeteAktuellt || _attic.ressattTillArbeteEjAktuellt ) {
                    return true;
                }
            }
            return false;
        };

        IntygModel.prototype.atticUpdateForm7 = function() {
            if (_attic) {
                _attic.ressattTillArbeteAktuellt = this.ressattTillArbeteAktuellt;
                _attic.ressattTillArbeteEjAktuellt = this.ressattTillArbeteEjAktuellt;
            }
        };

        IntygModel.prototype.atticRestoreForm7 = function() {
            if (_attic) {
                this.ressattTillArbeteAktuellt = _attic.ressattTillArbeteAktuellt;
                this.ressattTillArbeteEjAktuellt = _attic.ressattTillArbeteEjAktuellt;
            }
        };

        IntygModel.prototype.clearForm7 = function() {
            this.ressattTillArbeteAktuellt = undefined;
            this.ressattTillArbeteEjAktuellt = undefined;
        };

        // form8a
        IntygModel.prototype.atticHasForm8a = function() {
            if (_attic) {
                if ( _attic.arbetsloshet || _attic.foraldrarledighet || _attic.nuvarandeArbete || _attic.nuvarandeArbetsuppgifter) {
                    return true;
                }
            }
            return false;
        };

        IntygModel.prototype.atticUpdateForm8a = function() {
            if (_attic) {
                 _attic.arbetsloshet = this.arbetsloshet;
                 _attic.foraldrarledighet = this.foraldrarledighet;
                 _attic.nuvarandeArbete = this.nuvarandeArbete;
                 _attic.nuvarandeArbetsuppgifter = this.nuvarandeArbetsuppgifter;
            }
        };

        IntygModel.prototype.atticRestoreForm8a = function() {
            if (_attic) {
                this.arbetsloshet = _attic.arbetsloshet;
                this.foraldrarledighet = _attic.foraldrarledighet;
                this.nuvarandeArbete = _attic.nuvarandeArbete;
                this.nuvarandeArbetsuppgifter = _attic.nuvarandeArbetsuppgifter;
            }
        };

        IntygModel.prototype.clearForm8a = function() {
            this.arbetsloshet = undefined;
            this.foraldrarledighet = undefined;
            this.nuvarandeArbete = undefined;
            this.nuvarandeArbetsuppgifter = undefined;
        };

        // form10
        IntygModel.prototype.atticHasForm10 = function() {
            if (_attic) {
                if (_attic.prognosBedomning) {
                    return true;
                }
            }
            return false;
        };

        IntygModel.prototype.atticUpdateForm10 = function() {
            if (_attic) {
                _attic.prognosBedomning = this.prognosBedomning;
                _attic.arbetsformagaPrognosGarInteAttBedomaBeskrivning =
                    this.arbetsformagaPrognosGarInteAttBedomaBeskrivning;
            }
        };

        IntygModel.prototype.atticRestoreForm10 = function() {
            if (_attic) {
                this.prognosBedomning = _attic.prognosBedomning;
                this.arbetsformagaPrognosGarInteAttBedomaBeskrivning =
                    _attic.arbetsformagaPrognosGarInteAttBedomaBeskrivning;
            }
        };

        IntygModel.prototype.clearForm10 = function() {
            this.prognosBedomning = undefined;
            this.arbetsformagaPrognosGarInteAttBedomaBeskrivning = undefined;
        };

        // form11
        IntygModel.prototype.atticHasForm11 = function() {
            if (_attic) {
                if ( _attic.rehab ) {
                    return true;
                }
            }
            return false;
        };

        IntygModel.prototype.atticUpdateForm11 = function() {
            if (_attic) {
                _attic.rehab = this.rehab;
            }
        };

        IntygModel.prototype.atticRestoreForm11 = function() {
            if (_attic) {
                this.rehab = _attic.rehab;
            }
        };

        IntygModel.prototype.clearForm11 = function() {
            this.rehab = undefined;
        };

        IntygModel.prototype.attic = function(){
            return _attic;
        };

        _intygModel = new IntygModel();
        _attic = new IntygModel();

        draftModel.content = _intygModel;

        /**
         * Return the constructor function IntygModel
         */
        return _intygModel;

    }]);