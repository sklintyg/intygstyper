angular.module('fk7263').factory('fk7263.Domain.IntygModel',
    ['fk7263.Domain.GrundDataModel', 'common.domain.DraftModel', function(grundData, draftModel) {
        'use strict';

        var _intygModel;

        /**
         * Constructor, with class name
         */
        function IntygModel() {

            // field 1
            this.avstangningSmittskydd;

            // field 2
            this.diagnosBeskrivning;
            this.diagnosBeskrivning1;
            this.diagnosBeskrivning2;
            this.diagnosBeskrivning3;
            this.diagnosKod;
            this.diagnosKod2;
            this.diagnosKod3;
            this.diagnosKodsystem1;
            this.diagnosKodsystem2;
            this.diagnosKodsystem3;
            this.samsjuklighet;

            // field 3
            this.sjukdomsforlopp;

            // field 4
            this.funktionsnedsattning;

            // field 4b
            this.annanReferens;
            this.annanReferensBeskrivning;
            this.journaluppgifter;
            this.telefonkontaktMedPatienten;
            this.undersokningAvPatienten;

            // field 5
            this.aktivitetsbegransning;

            // field 6a
            this.rekommendationKontaktArbetsformedlingen;
            this.rekommendationKontaktForetagshalsovarden;
            this.rekommendationOvrigt;
            this.rekommendationOvrigtCheck;

            // field 7
            this.rehab;

            // field 11
            this.ressattTillArbeteAktuellt;
            this.ressattTillArbeteEjAktuellt;

            // field 6b
            this.atgardInomSjukvarden;
            this.annanAtgard;

            // field 8a
            this.arbetsloshet;
            this.foraldrarledighet;
            this.nuvarandeArbete;
            this.nuvarandeArbetsuppgifter;

            // field 8b
            this.tjanstgoringstid;
            this.nedsattMed100;
            this.nedsattMed25;
            this.nedsattMed25Beskrivning;
            this.nedsattMed50;
            this.nedsattMed50Beskrivning;
            this.nedsattMed75;
            this.nedsattMed75Beskrivning;

            // field 9
            this.arbetsformagaPrognos;

            // field 10
            this.arbetsformagaPrognosGarInteAttBedomaBeskrivning;
            this.prognosBedomning;

            // field 12
            this.kontaktMedFk;

            // field 13
            this.kommentar;

            // field 15
            // vardenhets model

            this.grundData = grundData;

            this.forskrivarkodOchArbetsplatskod;
            this.namnfortydligandeOchAdress;
            this.rehabilitering;

            this.id;
        }

        // override the original update method
        IntygModel.prototype.update = function(content) {
            // refresh the model data

            if(content === undefined) return;
            this.aktivitetsbegransning=	content.aktivitetsbegransning;
            this.annanAtgard=	content.annanAtgard;
            this.annanReferens=	content.annanReferens;
            this.annanReferensBeskrivning=	content.annanReferensBeskrivning;
            this.arbetsformagaPrognos=	content.arbetsformagaPrognos;
            this.arbetsloshet=	content.arbetsloshet;
            this.atgardInomSjukvarden=	content.atgardInomSjukvarden;
            this.avstangningSmittskydd=	content.avstangningSmittskydd;
            this.diagnosBeskrivning=	content.diagnosBeskrivning;
            this.diagnosBeskrivning1=	content.diagnosBeskrivning1;
            this.diagnosBeskrivning2=	content.diagnosBeskrivning2;
            this.diagnosBeskrivning3=	content.diagnosBeskrivning3;
            this.diagnosKod=	content.diagnosKod;
            this.diagnosKod2=	content.diagnosKod2;
            this.diagnosKod3=	content.diagnosKod3;
            this.diagnosKodsystem1=	content.diagnosKodsystem1;
            this.diagnosKodsystem2=	content.diagnosKodsystem2;
            this.diagnosKodsystem3=	content.diagnosKodsystem3;
            this.forskrivarkodOchArbetsplatskod=	content.forskrivarkodOchArbetsplatskod;
            this.funktionsnedsattning=	content.funktionsnedsattning;
            this.foraldrarledighet=	content.foraldrarledighet;
            this.grundData.update(content.grundData);
            this.journaluppgifter=	content.journaluppgifter;
            this.kommentar=	content.kommentar;
            this.kontaktMedFk=	content.kontaktMedFk;
            this.namnfortydligandeOchAdress=	content.namnfortydligandeOchAdress;
            this.nedsattMed100=	content.nedsattMed100;
            this.nedsattMed25=	content.nedsattMed25;
            this.nedsattMed25Beskrivning=	content.nedsattMed25Beskrivning;
            this.nedsattMed50=	content.nedsattMed50;
            this.nedsattMed50Beskrivning=	content.nedsattMed50Beskrivning;
            this.nedsattMed75=	content.nedsattMed75;
            this.nedsattMed75Beskrivning=	content.nedsattMed75Beskrivning;
            this.nuvarandeArbete=	content.nuvarandeArbete;
            this.nuvarandeArbetsuppgifter=	content.nuvarandeArbetsuppgifter;
            this.prognosBedomning=	content.prognosBedomning;
            this.rehabilitering=	content.rehabilitering;
            this.rekommendationKontaktArbetsformedlingen=	content.rekommendationKontaktArbetsformedlingen;
            this.rekommendationKontaktForetagshalsovarden=	content.rekommendationKontaktForetagshalsovarden;
            this.rekommendationOvrigt=	content.rekommendationOvrigt;
            this.rekommendationOvrigtCheck=	content.rekommendationOvrigtCheck;
            this.ressattTillArbeteAktuellt=	content.ressattTillArbeteAktuellt;
            this.ressattTillArbeteEjAktuellt=	content.ressattTillArbeteEjAktuellt;
            this.samsjuklighet=	content.samsjuklighet;
            this.sjukdomsforlopp=	content.sjukdomsforlopp;
            this.telefonkontaktMedPatienten=	content.telefonkontaktMedPatienten;
            this.tjanstgoringstid=	content.tjanstgoringstid;
            this.undersokningAvPatienten=	content.undersokningAvPatienten;
            this.id= content.id;

            // maybe special cases
            this.arbetsformagaPrognosGarInteAttBedomaBeskrivning = content.arbetsformagaPrognosGarInteAttBedomaBeskrivning;
            this.rehab = content.rehab;
        }

        IntygModel.prototype.fromForm = function(){

        }

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