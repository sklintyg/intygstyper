angular.module('fk7263').factory('fk7263.Domain.IntygModel',
    ['common.DateUtilsService', 'common.UtilsService', '$log', function( dateUtils, utils, $log) {
        'use strict';

        /**
         * Constructor, with class name
         */
        function IntygModel() {
            // ---------------------------------------------------------
            // 1. Smittskydd. Vid sparning: ta bort data på alla fält före 8b som döljs när smittskydd är icheckat.
            this.avstangningSmittskydd
            // ---------------------------------------------------------

            // ---------------------------------------------------------
            // 2. Diagnos
            this.diagnosKodverk1;
            this.diagnosKodverk2;
            this.diagnosKodverk3;

            this.diagnosKodsystem1;
            this.diagnosKodsystem2;
            this.diagnosKodsystem3;

            this.diagnosKod;
            this.diagnosKod2;
            this.diagnosKod3;

            this.diagnosBeskrivning1;
            this.diagnosBeskrivning2;
            this.diagnosBeskrivning3;
            this.diagnosBeskrivning;
            this.samsjuklighet = false;
            // ---------------------------------------------------------

            // ---------------------------------------------------------
            // 3,4,5
            this.sjukdomsforlopp;
            this.funktionsnedsattning;
            this.aktivitetsbegransning;
            // ---------------------------------------------------------

            // ---------------------------------------------------------
            // 4b. Baserat på
            this.undersokningAvPatienten;
            this.telefonkontaktMedPatienten;
            this.journaluppgifter;
            this.annanReferens;
            this.annanReferensBeskrivning;
            this.basedOnState.check.undersokningAvPatienten = false;
            this.basedOnState.check.telefonkontaktMedPatienten = false;
            this.basedOnState.check.journaluppgifter = false;
            this.basedOnState.check.annanReferens = false;
            // Fält 4b. datum
            this.undersokningAvPatienten;
            this.telefonkontaktMedPatienten;
            this.journaluppgifter;
            this.annanReferens;
            // Fält 4b. AnnanReferensBeskrivning
            // view state
            this.basedOnState.check.annanReferens
            this.annanReferensBeskrivning
            // view state
            //this.form.ovrigt.annanReferensBeskrivning;
            // ---------------------------------------------------------

            // Fält 6a.
            // view state
            // ---------------------------------------------------------
            this.rekommendationOvrigtCheck;
            this.rekommendationOvrigt;
            // ---------------------------------------------------------

            // ---------------------------------------------------------
            // 6a, 7, 11
            this.rekommendationKontaktArbetsformedlingen = false;
            this.rekommendationKontaktForetagshalsovarden = false;
            this.rekommendationOvrigtCheck = false;
            this.rekommendationOvrigt;
            // ---------------------------------------------------------

            // ---------------------------------------------------------
            // 6b åtgärder
            this.atgardInomSjukvarden;
            this.annanAtgard;
            // view state
            //this.form.rehab;
            // ---------------------------------------------------------


            // ---------------------------------------------------------
            // 7. Rehab radio conversions
            this.rehabilitering;
            // view state checkboxes
            //'JA' = 'rehabiliteringAktuell';
            //'NEJ' = 'rehabiliteringEjAktuell';
            //'GAREJ' = 'rehabiliteringGarInteAttBedoma';
            // ---------------------------------------------------------


            // ---------------------------------------------------------
            // 8a
            this.nuvarandeArbete = false;
            this.nuvarandeArbetsuppgifter;
            this.arbetsloshet = false;
            this.foraldrarledighet = false;
            // ---------------------------------------------------------

            // ---------------------------------------------------------
            // 8b.
            this.nedsattMed25.from;
            this.nedsattMed25.tom;
            this.nedsattMed25Beskrivning;
            this.nedsattMed50.from;
            this.nedsattMed50.tom;
            this.nedsattMed50Beskrivning;
            this.nedsattMed75.from;
            this.nedsattMed75.tom;
            this.nedsattMed75Beskrivning;
            this.nedsattMed100.from;
            this.nedsattMed100.tom;
            this.nedsattMed100Beskrivning;
            // ---------------------------------------------------------

            // ---------------------------------------------------------
            // 10. Går ej att bedöma and update backend model when view changes.
            this.arbetsformagaPrognosGarInteAttBedomaBeskrivning = null;
            this.prognosBedomning = null;
            // view state for arbetsformagaPrognosGarInteAttBedomaBeskrivning, 
            // value can be a radio choice or text from the beskrivning
            // view value = model value
            //this.form.ovrigt.arbetsformagaPrognosGarInteAttBedomaBeskrivning;
            //'YES' = 'arbetsformagaPrognosJa';
            //'PARTLY' = 'arbetsformagaPrognosJaDelvis';
            //'NO' = 'arbetsformagaPrognosNej';
            //'UNKNOWN' = 'arbetsformagaPrognosGarInteAttBedoma';
            // ---------------------------------------------------------

            // ---------------------------------------------------------
            // 11. Ressätt till arbete
            // view state = 'JA', 'NEJ' we should just use the model values!!
            this.ressattTillArbeteAktuellt = false;
            this.ressattTillArbeteEjAktuellt = false;
            /*if (this.form.ressattTillArbeteAktuellt === 'JA') {
                this.ressattTillArbeteAktuellt = true;
                this.ressattTillArbeteEjAktuellt = false;
            } else {
                this.ressattTillArbeteAktuellt = false;
                this.ressattTillArbeteEjAktuellt = true;
            }*/
            // ---------------------------------------------------------

        }

        

        IntygModel.prototype.refresh = function (cert) {
            // refresh the model data
        }

        IntygModel.build = function() {
            return new IntygModel();
        };

        /**
         * Return the constructor function IntygModel
         */
        return IntygModel;

    }]);