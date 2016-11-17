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

package se.inera.intyg.intygstyper.fkparent.model.converter;

import java.util.List;

import se.inera.intyg.common.support.model.common.internal.Utlatande;

public interface SvarIdHelper<T extends Utlatande> {

    /**
     * Get specific jsonProperties in order according to which grund for MU
     * exists in certificate. Positon 0 is base case.
     *
     * @param utlatande the relevant certificate
     * @return a (ordered) list of the jsonProperties matching grund for MU in the supplied certificate, starting with
     *         the jsonProperty for the question.
     */
    List<String> calculateFrageIdHandleForGrundForMU(T utlatande);
}
