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

package se.inera.intyg.intygstyper.luse.rest;

import java.util.List;
import java.util.stream.Collectors;

import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.api.dto.PdfResponse;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.intygstyper.fkparent.model.internal.Diagnos;
import se.inera.intyg.intygstyper.fkparent.rest.FkParentModuleApi;
import se.inera.intyg.intygstyper.luse.model.converter.InternalToTransport;
import se.inera.intyg.intygstyper.luse.model.converter.TransportToInternal;
import se.inera.intyg.intygstyper.luse.model.converter.UtlatandeToIntyg;
import se.inera.intyg.intygstyper.luse.model.internal.LuseUtlatande;
import se.inera.intyg.intygstyper.luse.support.LuseEntryPoint;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v2.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.v2.Intyg;

public class LuseModuleApi extends FkParentModuleApi<LuseUtlatande> {
    public LuseModuleApi() {
        super(LuseUtlatande.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PdfResponse pdf(String internalModel, List<Status> statuses, ApplicationOrigin applicationOrigin) throws ModuleException {
        // TODO
        return null;
    }

    @Override
    public PdfResponse pdfEmployer(String internalModel, List<Status> statuses, ApplicationOrigin applicationOrigin, List<String> optionalFields)
            throws ModuleException {
        // TODO
        return null;
    }

    @Override
    protected String getSchematronFileName() {
        return LuseEntryPoint.SCHEMATRON_FILE;
    }

    @Override
    protected RegisterCertificateType internalToTransport(LuseUtlatande utlatande) throws ConverterException {
        return InternalToTransport.convert(utlatande);
    }

    @Override
    protected LuseUtlatande transportToInternal(Intyg intyg) throws ConverterException {
        return TransportToInternal.convert(intyg);
    }

    @Override
    protected Intyg utlatandeToIntyg(LuseUtlatande utlatande) throws ConverterException {
        return UtlatandeToIntyg.convert(utlatande);
    }

    @Override
    protected void decorateDiagnoserWithDescriptions(LuseUtlatande utlatande) {
        List<Diagnos> decoratedDiagnoser = utlatande.getDiagnoser().stream()
                .map(diagnos -> Diagnos.create(diagnos.getDiagnosKod(), diagnos.getDiagnosKodSystem(), diagnos.getDiagnosBeskrivning(),
                        moduleService.getDescriptionFromDiagnosKod(diagnos.getDiagnosKod(), diagnos.getDiagnosKodSystem())))
                .collect(Collectors.toList());
        utlatande = utlatande.toBuilder().setDiagnoser(decoratedDiagnoser).build();
    }

}
