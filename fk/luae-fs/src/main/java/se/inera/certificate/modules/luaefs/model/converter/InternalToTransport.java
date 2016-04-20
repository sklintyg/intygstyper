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

package se.inera.certificate.modules.luaefs.model.converter;

import se.inera.certificate.modules.luaefs.model.internal.LuaefsUtlatande;
import se.inera.intyg.common.support.common.enumerations.RelationKod;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v2.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.v2.MeddelandeReferens;

public final class InternalToTransport {

    private InternalToTransport() {
    }

    public static RegisterCertificateType convert(LuaefsUtlatande source) throws ConverterException {
        if (source == null) {
            throw new ConverterException("Source utlatande was null, cannot convert");
        }

        RegisterCertificateType luaefsType = new RegisterCertificateType();
        luaefsType.setIntyg(UtlatandeToIntyg.convert(source));
        decorateWithSvarPa(luaefsType, source);
        return luaefsType;
    }

    private static void decorateWithSvarPa(RegisterCertificateType destination, LuaefsUtlatande source) {
        if (source.getGrundData().getRelation() == null || !RelationKod.KOMPLT.equals(source.getGrundData().getRelation().getRelationKod())) {
            return;
        }

        MeddelandeReferens mr = new MeddelandeReferens();
        mr.setMeddelandeId(source.getGrundData().getRelation().getMeddelandeId());
        if (source.getGrundData().getRelation().getReferensId() != null) {
            mr.getReferensId().add(source.getGrundData().getRelation().getReferensId());
        }
        destination.setSvarPa(mr);
    }

}