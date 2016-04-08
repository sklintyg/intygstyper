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

describe('FragaSvarHelper', function() {
    'use strict';

    var fragaSvarHelper;

    beforeEach(module('fk7263', function($provide) {
        var fragaSvarCommonService = jasmine.createSpyObj('common.fragaSvarCommonService', [ 'isUnhandled', 'fromFk', 'setVidareBefordradState' ]);
        $provide.value('common.fragaSvarCommonService', fragaSvarCommonService);
        $provide.value('common.statService', {});
        $provide.value('common.ObjectHelper', jasmine.createSpyObj('common.ObjectHelper',
            [ 'isDefined']));
    }));

    beforeEach(angular.mock.inject(['fk7263.QACtrl.Helper',
        function(_fragaSvarHelper_) {
            fragaSvarHelper = _fragaSvarHelper_;
        }]));

    describe('#filterKompletteringar', function() {
        it('should filter out kompletteringar and show kompletteringar NOT handled', function() {

            var qaList = [
                {
                    amne: 'KOMPLETTERING_AV_LAKARINTYG',
                    status: 'PENDING_INTERNAL_ACTION',
                    internReferens: '14'
                },
                {
                    amne: 'OVRIGT',
                    status: 'CLOSED',
                    internReferens: '15'
                }
            ];

            var widgetState = {
                showAllKompletteringarHandled: false
            };

            var certProperties = {
                meddelandeId: '14'
            };

            var resultList = fragaSvarHelper.filterKompletteringar(qaList, widgetState, certProperties);

            expect(resultList.length).toBe(1);
            expect(widgetState.showAllKompletteringarHandled).toBe(false);
        });

        it('should filter out kompletteringar and show kompletteringar ARE handled', function() {

            var qaList = [
                {
                    amne: 'KOMPLETTERING_AV_LAKARINTYG',
                    status: 'CLOSED',
                    internReferens: '14'
                },
                {
                    amne: 'KOMPLETTERING_AV_LAKARINTYG',
                    status: 'CLOSED',
                    internReferens: '16'
                },
                {
                    amne: 'OVRIGT',
                    status: 'CLOSED',
                    internReferens: '15'
                }
            ];

            var widgetState = {
                showAllKompletteringarHandled: false
            };

            // Meddelande to fetch
            var certProperties = {
                meddelandeId: '14'
            };

            var resultList = fragaSvarHelper.filterKompletteringar(qaList, widgetState, certProperties);

            // Should still be 1 since its matched on ID
            expect(resultList.length).toBe(1);
            expect(widgetState.showAllKompletteringarHandled).toBe(true);
        });
    });

});
