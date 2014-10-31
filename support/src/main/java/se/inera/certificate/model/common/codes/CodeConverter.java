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

import org.apache.commons.lang3.StringUtils;

import se.inera.certificate.model.Kod;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Util for converting an enum implementing CodeSystem to a Kod object.
 *
 * @author nikpet
 */
public final class CodeConverter {

    private CodeConverter() {

    }

    public static Kod toKod(CodeSystem codeEnum) {

        if (codeEnum == null) {
            return null;
        }

        if (StringUtils.isBlank(codeEnum.getCode())) {
            throw new IllegalArgumentException("The provided enum does not contain any code values");
        }

        Kod kod = new Kod(codeEnum.getCode());

        if (StringUtils.isNotBlank(codeEnum.getCodeSystemName())) {
            kod.setCodeSystemName(codeEnum.getCodeSystemName());
        }

        if (StringUtils.isNotBlank(codeEnum.getCodeSystem())) {
            kod.setCodeSystem(codeEnum.getCodeSystem());
        }

        if (StringUtils.isNotBlank(codeEnum.getCodeSystemVersion())) {
            kod.setCodeSystemVersion(codeEnum.getCodeSystemVersion());
        }

        return kod;
    }

    /**
     * Converts from {@link Kod} to a specified enum representing that kod.
     *
     * @param kod
     *            The Kod to convert to an enum.
     * @param type
     *            The enum class to convert to.
     * @return An enum instance of <code>null</code> if the kod was <code>null</code>.
     * @throws RuntimeException
     *             if no enum constant was defined for the specified kod.
     */
    public static <T extends CodeSystem> T fromCode(Kod kod, Class<T> type) {
        if (kod == null) {
            return null;
        }

        for (T enumConstant : type.getEnumConstants()) {
            if (matches(enumConstant, kod)) {
                return enumConstant;
            }
        }

        throw new RuntimeException(String.format("Found no valid enum for code '%s' of type '%s'", kod.getCode(),
                type.getSimpleName()));
    }

    /**
     * Checks if a specified code enum matches a specified {@link Kod}.
     *
     * @param codeEnum
     *            The code enum to match.
     * @param kod
     *            The kod to match.
     * @return <code>true</code> if the code enum and kod matches, <code>false</code> otherwise.
     */
    public static boolean matches(CodeSystem codeEnum, Kod kod) {
        if (codeEnum == null) {
            return kod == null;
        }
        if (kod == null) {
            return false;
        }

        if (codeEnum.getCode() != null ? !codeEnum.getCode().equals(kod.getCode()) : kod.getCode() != null) {
            return false;
        }
        if (codeEnum.getCodeSystem() != null ? !codeEnum.getCodeSystem().equals(kod.getCodeSystem()) : kod
                .getCodeSystem() != null) {
            return false;
        }
        if (codeEnum.getCodeSystemName() != null ? !codeEnum.getCodeSystemName().equals(kod.getCodeSystemName()) : kod
                .getCodeSystemName() != null) {
            return false;
        }
        if (codeEnum.getCodeSystemVersion() != null ? !codeEnum.getCodeSystemVersion().equals(
                kod.getCodeSystemVersion()) : kod.getCodeSystemVersion() != null) {
            return false;
        }

        return true;
    }

    /**
     * Given a Kod and its CodeSystem, returns the corresponding internal enum constant name for that code from the
     * CodeSystem representing it.
     *
     * @param kod
     *            {@link Kod}
     * @return a String with the Enum constant
     */
    public static String getInternalNameFromKod(Kod kod, Class<? extends CodeSystem> type) {
        return fromCode(kod, type).toString();
    }

    public static Collection<String> convertKodToString(List<Kod> koder, Class<? extends CodeSystem> type) {
        List<String> intKoder = new ArrayList<>();
        for (Kod kod : koder) {
            intKoder.add(CodeConverter.fromCode(kod, type).toString());
        }
        return intKoder;
    }
}
