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

package se.inera.intyg.intygstyper.luae_na.utils;

/**
 * Thrown when an expected scenario wasn't found.
 */
public class ScenarioNotFoundException extends Exception {

    private static final long serialVersionUID = 2092187161098644931L;

    public ScenarioNotFoundException(String scenario, String model) {
        super(String.format("Could not find %s model scenario %s", model, scenario));
    }

    public ScenarioNotFoundException(String scenario, String model, Throwable cause) {
        super(String.format("Could not find %s model scenario %s", model, scenario), cause);
    }
}
