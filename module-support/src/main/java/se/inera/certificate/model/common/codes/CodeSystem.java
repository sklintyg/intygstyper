/**
 * Copyright (C) 2013 Inera AB (http://www.inera.se)
 *
 * This file is part of Inera Certificate Modules (http://code.google.com/p/inera-certificate-modules).
 *
 * Inera Certificate Modules is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Inera Certificate Modules is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.certificate.model.common.codes;

import se.inera.certificate.model.Kod;

/**
 * The base of all codes used by this module.
 */
public interface CodeSystem {

    /**
     * The code value of a specific code.
     *
     * @return The code value.
     */
    String getCode();

    /**
     * A textual description of a specific code.
     *
     * @return The textual description of the code.
     */
    String getDescription();

    /**
     * The id of the code system that this code is defined in.
     *
     * @return The code system.
     */
    String getCodeSystem();

    /**
     * The name of the code system that this code is defined in.
     *
     * @return The code system name.
     */
    String getCodeSystemName();

    /**
     * The version of the code system that this code is defined in.
     *
     * @return The code system version.
     */
    String getCodeSystemVersion();

    /**
     * Checks if a specific code system enum matches a specified {@link Kod}.
     *
     * @param kod The kod to match
     * @return <code>true</code> if this enum matches the kod, <code>false</code> otherwise.
     */
    boolean matches(Kod kod);
}
