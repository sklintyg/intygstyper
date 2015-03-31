angular.module('fk7263').factory('fk7263.Domain.IntygModel',
    ['fk7263.Domain.GrundDataModel', 'common.domain.DraftModel', function(grundData, draftModel) {
        'use strict';

        var _intygModel, attic;

        /**
         * Constructor, with class name
         */
        function IntygModel(attic) {

            if (attic) {
                this.attic = attic;
            }

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

        IntygModel.prototype.prepare = function(nedsatt) {
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
            if (this.attic) {
                if (this.attic.diagnosKod || this.attic.diagnosKod2 || this.attic.diagnosKod3) {
                    return true;
                }
            }
            return false;
        };

        IntygModel.prototype.atticUpdateForm2 = function() {
            if (this.attic) {
                this.attic.diagnosBeskrivning = this.diagnosBeskrivning;
                this.attic.diagnosBeskrivning1 = this.diagnosBeskrivning1;
                this.attic.diagnosBeskrivning2 = this.diagnosBeskrivning2;
                this.attic.diagnosBeskrivning3 = this.diagnosBeskrivning3;
                this.attic.diagnosKod = this.diagnosKod;
                this.attic.diagnosKod2 = this.diagnosKod2;
                this.attic.diagnosKod3 = this.diagnosKod3;
                this.attic.diagnosKodsystem1 = this.diagnosKodsystem1;
                this.attic.diagnosKodsystem2 = this.diagnosKodsystem2;
                this.attic.diagnosKodsystem3 = this.diagnosKodsystem3;
                this.attic.samsjuklighet = this.samsjuklighet;
            }
        };

        IntygModel.prototype.atticRestoreForm2 = function() {
            if (this.attic) {
                this.diagnosBeskrivning = this.attic.diagnosBeskrivning;
                this.diagnosBeskrivning1 = this.attic.diagnosBeskrivning1;
                this.diagnosBeskrivning2 = this.attic.diagnosBeskrivning2;
                this.diagnosBeskrivning3 = this.attic.diagnosBeskrivning3;
                this.diagnosKod = this.attic.diagnosKod;
                this.diagnosKod2 = this.attic.diagnosKod2;
                this.diagnosKod3 = this.attic.diagnosKod3;
                this.diagnosKodsystem1 = this.attic.diagnosKodsystem1;
                this.diagnosKodsystem2 = this.attic.diagnosKodsystem2;
                this.diagnosKodsystem3 = this.attic.diagnosKodsystem3;
                this.samsjuklighet = this.attic.samsjuklighet;
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
            this.samsjuklighet = undefined;
        };

        // form3
        IntygModel.prototype.atticHasForm3 = function() {
            if (this.attic) {
                if (this.attic.sjukdomsforlopp) {
                    return true;
                }
            }
            return false;
        };

        IntygModel.prototype.atticUpdateForm3 = function() {
            if (this.attic) {
                this.attic.sjukdomsforlopp = this.sjukdomsforlopp;
            }
        };

        IntygModel.prototype.atticRestoreForm3 = function() {
            if (this.attic) {
                this.sjukdomsforlopp = this.attic.sjukdomsforlopp;
            }
        };

        IntygModel.prototype.clearForm3 = function() {
            this.sjukdomsforlopp = undefined;
        };

        // form4
        IntygModel.prototype.atticHasForm4 = function() {
            if (this.attic) {
                if (this.attic.funktionsnedsattning) {
                    return true;
                }
            }
            return false;
        };

        IntygModel.prototype.atticUpdateForm4 = function() {
            if (this.attic) {
                this.attic.funktionsnedsattning = this.funktionsnedsattning;
            }
        };

        IntygModel.prototype.atticRestoreForm4 = function() {
            if (this.attic) {
                this.funktionsnedsattning = this.attic.funktionsnedsattning;
            }
        };

        IntygModel.prototype.clearForm4 = function() {
            this.funktionsnedsattning = undefined;
        };

        // form4b
        IntygModel.prototype.atticHasForm4b = function() {
            if (this.attic) {
                if (this.attic.annanReferens || this.attic.journaluppgifter || this.attic.journaluppgifter || this.attic.undersokningAvPatienten) {
                    return true;
                }
            }
            return false;
        };

        IntygModel.prototype.atticUpdateForm4b = function() {
            if (this.attic) {
                 this.attic.annanReferens = this.annanReferens;
                 this.attic.annanReferensBeskrivning = this.annanReferensBeskrivning;
                 this.attic.journaluppgifter = this.journaluppgifter;
                 this.attic.telefonkontaktMedPatienten = this.telefonkontaktMedPatienten;
                 this.attic.undersokningAvPatienten = this.undersokningAvPatienten;
            }
        };

        IntygModel.prototype.atticRestoreForm4b = function() {
            if (this.attic) {
                this.annanReferens = this.attic.annanReferens;
                this.annanReferensBeskrivning = this.attic.annanReferensBeskrivning;
                this.journaluppgifter = this.attic.journaluppgifter;
                this.telefonkontaktMedPatienten = this.attic.telefonkontaktMedPatienten;
                this.undersokningAvPatienten = this.attic.undersokningAvPatienten;
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
            if (this.attic) {
                if ( this.attic.aktivitetsbegransning ) {
                    return true;
                }
            }
            return false;
        };

        IntygModel.prototype.atticUpdateForm5 = function() {
            if (this.attic) {
                this.attic.aktivitetsbegransning = this.aktivitetsbegransning;
            }
        };

        IntygModel.prototype.atticRestoreForm5 = function() {
            if (this.attic) {
                this.aktivitetsbegransning = this.attic.aktivitetsbegransning;
            }
        };

        IntygModel.prototype.clearForm5 = function() {
            this.aktivitetsbegransning = undefined;
        };

        // form6a
        IntygModel.prototype.atticHasForm6a = function() {
            if (this.attic) {
                if ( this.attic.rekommendationKontaktArbetsformedlingen ||
                    this.attic.rekommendationKontaktForetagshalsovarden ||
                    this.attic.rekommendationOvrigt ||
                    this.attic.rekommendationOvrigtCheck) {
                    return true;
                }
            }
            return false;
        };

        IntygModel.prototype.atticUpdateForm6a = function() {
            if (this.attic) {
                this.attic.rekommendationKontaktArbetsformedlingen = this.rekommendationKontaktArbetsformedlingen;
                this.attic.rekommendationKontaktForetagshalsovarden = this.rekommendationKontaktForetagshalsovarden;
                this.attic.rekommendationOvrigt = this.rekommendationOvrigt;
                this.attic.rekommendationOvrigtCheck = this.rekommendationOvrigtCheck;
            }
        };

        IntygModel.prototype.atticRestoreForm6a = function() {
            if (this.attic) {
                this.rekommendationKontaktArbetsformedlingen = this.attic.rekommendationKontaktArbetsformedlingen;
                this.rekommendationKontaktForetagshalsovarden = this.attic.rekommendationKontaktForetagshalsovarden;
                this.rekommendationOvrigt = this.attic.rekommendationOvrigt;
                this.rekommendationOvrigtCheck = this.attic.rekommendationOvrigtCheck;
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
            if (this.attic) {
                if ( this.attic.atgardInomSjukvarden || this.attic.annanAtgard ) {
                    return true;
                }
            }
            return false;
        };

        IntygModel.prototype.atticUpdateForm6b = function() {
            if (this.attic) {
                this.attic.atgardInomSjukvarden = this.atgardInomSjukvarden;
                this.attic.annanAtgard = this.annanAtgard;
            }
        };

        IntygModel.prototype.atticRestoreForm6b = function() {
            if (this.attic) {
                this.atgardInomSjukvarden = this.attic.atgardInomSjukvarden;
                this.annanAtgard = this.attic.annanAtgard;
            }
        };

        IntygModel.prototype.clearForm6b = function() {
            this.atgardInomSjukvarden = undefined;
            this.annanAtgard = undefined;
        };

        // form7
        IntygModel.prototype.atticHasForm7 = function() {
            if (this.attic) {
                if ( this.attic.ressattTillArbeteAktuellt || this.attic.ressattTillArbeteEjAktuellt ) {
                    return true;
                }
            }
            return false;
        };

        IntygModel.prototype.atticUpdateForm7 = function() {
            if (this.attic) {
                this.attic.ressattTillArbeteAktuellt = this.ressattTillArbeteAktuellt;
                this.attic.ressattTillArbeteEjAktuellt = this.ressattTillArbeteEjAktuellt;
            }
        };

        IntygModel.prototype.atticRestoreForm7 = function() {
            if (this.attic) {
                this.ressattTillArbeteAktuellt = this.attic.ressattTillArbeteAktuellt;
                this.ressattTillArbeteEjAktuellt = this.attic.ressattTillArbeteEjAktuellt;
            }
        };

        IntygModel.prototype.clearForm7 = function() {
            this.ressattTillArbeteAktuellt = undefined;
            this.ressattTillArbeteEjAktuellt = undefined;
        };

        // form8a
        IntygModel.prototype.atticHasForm8a = function() {
            if (this.attic) {
                if ( this.attic.arbetsloshet || this.attic.foraldrarledighet || this.attic.nuvarandeArbete || this.attic.nuvarandeArbetsuppgifter) {
                    return true;
                }
            }
            return false;
        };

        IntygModel.prototype.atticUpdateForm8a = function() {
            if (this.attic) {
                 this.attic.arbetsloshet = this.arbetsloshet;
                 this.attic.foraldrarledighet = this.foraldrarledighet;
                 this.attic.nuvarandeArbete = this.nuvarandeArbete;
                 this.attic.nuvarandeArbetsuppgifter = this.nuvarandeArbetsuppgifter;
            }
        };

        IntygModel.prototype.atticRestoreForm8a = function() {
            if (this.attic) {
                this.arbetsloshet = this.attic.arbetsloshet;
                this.foraldrarledighet = this.attic.foraldrarledighet;
                this.nuvarandeArbete = this.attic.nuvarandeArbete;
                this.nuvarandeArbetsuppgifter = this.attic.nuvarandeArbetsuppgifter;
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
            if (this.attic) {
                if (this.attic.prognosBedomning) {
                    return true;
                }
            }
            return false;
        };

        IntygModel.prototype.atticUpdateForm10 = function() {
            if (this.attic) {
                this.attic.prognosBedomning = this.prognosBedomning;
                this.attic.arbetsformagaPrognosGarInteAttBedomaBeskrivning =
                    this.arbetsformagaPrognosGarInteAttBedomaBeskrivning;
            }
        };

        IntygModel.prototype.atticRestoreForm10 = function() {
            if (this.attic) {
                this.prognosBedomning = this.attic.prognosBedomning;
                this.arbetsformagaPrognosGarInteAttBedomaBeskrivning =
                    this.attic.arbetsformagaPrognosGarInteAttBedomaBeskrivning;
            }
        };

        IntygModel.prototype.clearForm10 = function() {
            this.prognosBedomning = undefined;
            this.arbetsformagaPrognosGarInteAttBedomaBeskrivning = undefined;
        };

        // form11
        IntygModel.prototype.atticHasForm11 = function() {
            if (this.attic) {
                if ( this.attic.rehab ) {
                    return true;
                }
            }
            return false;
        };

        IntygModel.prototype.atticUpdateForm11 = function() {
            if (this.attic) {
                this.attic.rehab = this.rehab;
            }
        };

        IntygModel.prototype.atticRestoreForm11 = function() {
            if (this.attic) {
                this.rehab = this.attic.rehab;
            }
        };

        IntygModel.prototype.clearForm11 = function() {
            this.rehab = undefined;
        };


        IntygModel.build = function() {
            return new IntygModel(new IntygModel());
        };

        _intygModel = IntygModel.build();

        draftModel.content = _intygModel;

        /**
         * Return the constructor function IntygModel
         */
        return _intygModel;

    }]);