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
package se.inera.certificate.modules.rli.model.converters;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.Partial;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import se.inera.certificate.integration.rest.dto.CertificateContentMeta;
import se.inera.certificate.integration.rest.dto.CertificateStatus;
import se.inera.certificate.model.HosPersonal;
import se.inera.certificate.model.PartialInterval;
import se.inera.certificate.modules.rli.model.codes.ArrangemangsKod;
import se.inera.certificate.modules.rli.model.codes.CodeConverter;
import se.inera.certificate.modules.rli.model.internal.Arrangemang;
import se.inera.certificate.modules.rli.model.internal.HoSPersonal;
import se.inera.certificate.modules.rli.model.internal.Patient;
import se.inera.certificate.modules.rli.model.internal.Rekommendation;
import se.inera.certificate.modules.rli.model.internal.Status;
import se.inera.certificate.modules.rli.model.internal.Undersokning;
import se.inera.certificate.modules.rli.model.internal.Utlatande;
import se.inera.certificate.modules.rli.model.internal.Vardenhet;
import se.inera.certificate.modules.rli.model.internal.Vardgivare;
import se.inera.certificate.modules.rli.rest.dto.CertificateContentHolder;

/**
 * Converter for converting the external format to the internal view format.
 * 
 * 
 * @author Niklas Pettersson, R2M
 * 
 */
public class ExternalToInternalConverter {

    private static final Logger LOG = LoggerFactory.getLogger(ExternalToInternalConverter.class);

    @Autowired
    private UndersokningPopulator undersokingPopulator;

    @Autowired
    private RekommendationPopulator rekommendationPopulator;

    public ExternalToInternalConverter() {

    }

    public Utlatande fromExternalToInternal(CertificateContentHolder certificateContentHolder) throws ConverterException {

        se.inera.certificate.modules.rli.model.external.Utlatande extUtlatande = certificateContentHolder
                .getCertificateContent();

        Utlatande intUtlatande = convertUtlatandeFromExternalToInternal(extUtlatande);

        decorateWithStatusInfo(intUtlatande, certificateContentHolder.getCertificateContentMeta());

        return intUtlatande;
    }

    private void decorateWithStatusInfo(Utlatande intUtlatande, CertificateContentMeta certificateContentMeta) {
        List<CertificateStatus> certStatuses = certificateContentMeta.getStatuses();

        List<Status> intStatuses = convertToIntStatuses(certStatuses);
        intUtlatande.setStatus(intStatuses);

    }

    Utlatande convertUtlatandeFromExternalToInternal(
            se.inera.certificate.modules.rli.model.external.Utlatande extUtlatande) throws ConverterException {

        LOG.debug("Converting Utlatande '{}' from external to internal", extUtlatande.getId());

        Utlatande intUtlatande = new Utlatande();

        intUtlatande.setUtlatandeid(InternalModelConverterUtils.getExtensionFromId(extUtlatande.getId()));

        intUtlatande.setTypAvUtlatande(InternalModelConverterUtils.getValueFromKod(extUtlatande.getTyp()));

        intUtlatande.setSigneringsdatum(extUtlatande.getSigneringsdatum());
        intUtlatande.setSkickatdatum(extUtlatande.getSkickatdatum());

        intUtlatande.setKommentarer(extUtlatande.getKommentarer());

        HoSPersonal intHoSPersonal = convertToIntHoSPersonal(extUtlatande.getSkapadAv());
        intUtlatande.setSkapadAv(intHoSPersonal);

        Patient intPatient = convertToIntPatient(extUtlatande.getPatient());
        intUtlatande.setPatient(intPatient);

        Arrangemang intArrangemang = convertToIntArrangemang(extUtlatande.getArrangemang());
        intUtlatande.setArrangemang(intArrangemang);

        populateUndersokingRekommendation(extUtlatande, intUtlatande);

        return intUtlatande;
    }

    private void populateUndersokingRekommendation(
            se.inera.certificate.modules.rli.model.external.Utlatande extUtlatande, Utlatande intUtlatande) {

        LOG.trace("Populating Utlatande with Undersokning and Rekommendation");

        Undersokning intUndersokning = undersokingPopulator.createAndPopulateUndersokning(extUtlatande);
        intUtlatande.setUndersokning(intUndersokning);

        Rekommendation intRekommendation = rekommendationPopulator.createAndPopulateRekommendation(extUtlatande);
        intUtlatande.setRekommendation(intRekommendation);
    }

    Vardenhet convertToIntVardenhet(se.inera.certificate.model.Vardenhet extVardenhet) throws ConverterException {

        LOG.trace("Converting vardenhet");

        if (extVardenhet == null) {
            throw new ConverterException("External Vardenhet is null, can not convert");
        }

        Vardenhet intVardenhet = new Vardenhet();

        intVardenhet.setEnhetsid(InternalModelConverterUtils.getExtensionFromId(extVardenhet.getId()));
        intVardenhet.setEnhetsnamn(extVardenhet.getNamn());
        intVardenhet.setPostadress(extVardenhet.getPostadress());
        intVardenhet.setPostnummer(extVardenhet.getPostnummer());
        intVardenhet.setPostort(extVardenhet.getPostort());
        intVardenhet.setTelefonnummer(extVardenhet.getTelefonnummer());
        intVardenhet.setEpost(extVardenhet.getEpost());

        Vardgivare intVardgivare = convertToIntVardgivare(extVardenhet.getVardgivare());
        intVardenhet.setVardgivare(intVardgivare);

        return intVardenhet;
    }

    Vardgivare convertToIntVardgivare(se.inera.certificate.model.Vardgivare extVardgivare) throws ConverterException {

        LOG.trace("Converting vardgivare");

        if (extVardgivare == null) {
            throw new ConverterException("External vardgivare is null, can not convert");
        }

        Vardgivare intVardgivare = new Vardgivare();

        intVardgivare.setVardgivarid(InternalModelConverterUtils.getExtensionFromId(extVardgivare.getId()));
        intVardgivare.setVardgivarnamn(extVardgivare.getNamn());

        return intVardgivare;
    }

    HoSPersonal convertToIntHoSPersonal(HosPersonal extHoSPersonal) throws ConverterException {

        LOG.trace("Converting HoSPersonal");

        if (extHoSPersonal == null) {
            throw new ConverterException("External HoSPersonal is null, can not convert");
        }

        HoSPersonal intHoSPersonal = new HoSPersonal();

        intHoSPersonal.setPersonid(InternalModelConverterUtils.getExtensionFromId(extHoSPersonal.getId()));
        intHoSPersonal.setFullstandigtNamn(extHoSPersonal.getNamn());
        intHoSPersonal.setBefattning(extHoSPersonal.getBefattning());

        Vardenhet intVardenhet = convertToIntVardenhet(extHoSPersonal.getVardenhet());
        intHoSPersonal.setVardenhet(intVardenhet);

        return intHoSPersonal;
    }

    List<Status> convertToIntStatuses(List<CertificateStatus> certStatuses) {

        List<Status> intStatuses = new ArrayList<Status>();

        if (certStatuses == null || certStatuses.isEmpty()) {
            LOG.trace("No statuses found to convert");
            return intStatuses;
        }

        LOG.trace("Converting {} statuses to internal", certStatuses.size());

        Status intStatus;

        for (CertificateStatus extStatus : certStatuses) {
            intStatus = new Status();
            intStatus.setType(extStatus.getType());
            intStatus.setTimestamp(extStatus.getTimestamp());
            intStatus.setTarget(extStatus.getTarget());
            intStatuses.add(intStatus);
        }

        return intStatuses;
    }

    Patient convertToIntPatient(se.inera.certificate.model.Patient extPatient) throws ConverterException {

        LOG.trace("Converting patient");

        if (extPatient == null) {
            throw new ConverterException("No Patient found to convert");
        }

        Patient intPatient = new Patient();

        intPatient.setPersonid(InternalModelConverterUtils.getExtensionFromId(extPatient.getId()));

        intPatient.setEfternamn(extPatient.getEfternamn());

        String forNamn = StringUtils.join(extPatient.getFornamn(), " ");
        intPatient.setFornamn(forNamn);

        String fullstandigtNamn = forNamn.concat(" ").concat(extPatient.getEfternamn());
        intPatient.setFullstandigtNamn(fullstandigtNamn);

        intPatient.setPostadress(extPatient.getPostadress());
        intPatient.setPostnummer(extPatient.getPostnummer());
        intPatient.setPostort(extPatient.getPostort());

        return intPatient;
    }

    Arrangemang convertToIntArrangemang(se.inera.certificate.modules.rli.model.external.Arrangemang extArr) throws ConverterException {

        LOG.trace("Converting arrangemang");

        if (extArr == null) {
            throw new ConverterException("No arrangemang found to convert");
        }

        Arrangemang intArr = new Arrangemang();

        intArr.setPlats(extArr.getPlats());

        ArrangemangsKod arrTyp = CodeConverter.fromCode(extArr.getArrangemangstyp(), ArrangemangsKod.class);
        intArr.setArrangemangstyp(arrTyp);

        intArr.setBokningsreferens(extArr.getBokningsreferens());

        Partial extBokningsDatum = extArr.getBokningsdatum();
        intArr.setBokningsdatum(PartialConverter.partialToString(extBokningsDatum));

        Partial extAvbestDatum = extArr.getAvbestallningsdatum();
        intArr.setAvbestallningsdatum(PartialConverter.partialToString(extAvbestDatum));

        PartialInterval arrangemangsTid = extArr.getArrangemangstid();

        if (arrangemangsTid != null) {
            intArr.setArrangemangsdatum(PartialConverter.partialToString(arrangemangsTid.getFrom()));
            String tom = (arrangemangsTid.getTom() != null) ? PartialConverter
                    .partialToString(arrangemangsTid.getTom()) : PartialConverter.partialToString(arrangemangsTid
                    .getFrom());
            intArr.setArrangemangslutdatum(tom);
        }

        return intArr;
    }

}
