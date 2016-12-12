/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

angular.module('ts-diabetes').service('ts-diabetes.UtkastController.ViewStateService',
    ['$log', 'ts-diabetes.Domain.IntygModel', 'common.UtkastViewStateService',
        function($log, IntygModel, CommonViewState) {
            'use strict';

            this.common = CommonViewState;

            this.intygModel = undefined;
            this.draftModel = undefined;

            this.clearModel = function(){
                this.intygModel = undefined;
                this.draftModel = undefined;
            };

            this.setDraftModel = function(draftModel){
                this.draftModel = draftModel;
                this.intygModel = draftModel.content;
            };

            this.reset = function() {
                this.korkortd = false;
                this.tomorrowDate = moment().format('YYYY-MM-DD');
                this.minDate = moment().subtract(1, 'y').format('YYYY-MM-DD');

                this.identitet = [
                    {label: 'ID-kort ¹', id: 'ID_KORT'},
                    {label: 'Företagskort eller tjänstekort ²', id: 'FORETAG_ELLER_TJANSTEKORT'},
                    {label: 'Svenskt körkort', id: 'KORKORT'},
                    {label: 'Personlig kännedom', id: 'PERS_KANNEDOM'},
                    {label: 'Försäkran enligt 18 kap. 4§ ³', id: 'FORSAKRAN_KAP18'},
                    {label: 'Pass ⁴', id: 'PASS'}
                ];
                this.behorighet = null;

                CommonViewState.reset();
                CommonViewState.intyg.type = 'ts-diabetes';
                this.setDraftModel(IntygModel._members.build());
                return this;
            };

            this.getValidationErrors = function(field) {
                if (!this.common.validation.messagesByField) {
                    return null;
                }
                return this.common.validation.messagesByField[field.toLowerCase()];
            };

            this.reset();

        }]);
