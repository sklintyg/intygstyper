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
import se.inera.certificate.modules.rli.model.codes.ArrangemangsTyp;
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
public class ExternalToInternalConverterImpl implements ExternalToInternalConverter {

    private static final Logger LOG = LoggerFactory.getLogger(ExternalToInternalConverterImpl.class);

    @Autowired
    private UndersokningPopulator undersokingPopulator;

    @Autowired
    private RekommendationPopulator rekommendationPopulator;

    public ExternalToInternalConverterImpl() {

    }

    @Override
    public Utlatande fromExternalToInternal(CertificateContentHolder certificateContentHolder) {

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
            se.inera.certificate.modules.rli.model.external.Utlatande extUtlatande) {

        LOG.debug("Starting conversion of Utlatande from external to internal");

        Utlatande intUtlatande = new Utlatande();

        intUtlatande.setUtlatandeId(InternalModelConverterUtils.getValueFromId(extUtlatande.getId()));
        intUtlatande.setTypAvUtlatande(InternalModelConverterUtils.getValueFromKod(extUtlatande.getTyp()));

        intUtlatande.setSigneringsDatum(extUtlatande.getSigneringsdatum());
        intUtlatande.setSkickatDatum(extUtlatande.getSkickatdatum());

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

        LOG.debug("Populating Utlatande with Undersokning and Rekommendation");
        
        Undersokning intUndersokning = undersokingPopulator.createAndPopulateUndersokning(extUtlatande);
        intUtlatande.setUndersokning(intUndersokning);

        Rekommendation intRekommendation = rekommendationPopulator.createAndPopulateRekommendation(extUtlatande);
        intUtlatande.setRekommendation(intRekommendation);
    }

    Vardenhet convertToIntVardenhet(se.inera.certificate.model.Vardenhet extVardenhet) {

        LOG.debug("Converting vardenhet");

        if (extVardenhet == null) {
            LOG.debug("External Vardenhet is null, can not convert");
            return null;
        }

        Vardenhet intVardenhet = new Vardenhet();

        intVardenhet.setEnhetsId(InternalModelConverterUtils.getValueFromId(extVardenhet.getId()));
        intVardenhet.setEnhetsnamn(extVardenhet.getNamn());
        intVardenhet.setPostadress(extVardenhet.getPostadress());
        intVardenhet.setPostnummer(extVardenhet.getPostnummer());
        intVardenhet.setPostort(extVardenhet.getPostort());
        intVardenhet.setTelefonnummer(extVardenhet.getTelefonnummer());
        intVardenhet.setePost(extVardenhet.getEpost());

        Vardgivare intVardgivare = convertToIntVardgivare(extVardenhet.getVardgivare());
        intVardenhet.setVardgivare(intVardgivare);

        return intVardenhet;
    }

    Vardgivare convertToIntVardgivare(se.inera.certificate.model.Vardgivare extVardgivare) {

        LOG.debug("Converting vardgivare");

        if (extVardgivare == null) {
            LOG.debug("External vardgivare is null, can not convert");
            return null;
        }

        Vardgivare intVardgivare = new Vardgivare();

        intVardgivare.setVardgivarId(InternalModelConverterUtils.getValueFromId(extVardgivare.getId()));
        intVardgivare.setVardgivarnamn(extVardgivare.getNamn());

        return intVardgivare;
    }

    HoSPersonal convertToIntHoSPersonal(HosPersonal extHoSPersonal) {

        LOG.debug("Converting HoSPersonal");

        if (extHoSPersonal == null) {
            LOG.debug("External HoSPersonal is null, can not convert");
            return null;
        }

        HoSPersonal intHoSPersonal = new HoSPersonal();

        intHoSPersonal.setPersonId(InternalModelConverterUtils.getValueFromId(extHoSPersonal.getId()));
        intHoSPersonal.setFullstandigtnamn(extHoSPersonal.getNamn());
        // intHoSPersonal.setBefattning(befattning);

        Vardenhet intVardenhet = convertToIntVardenhet(extHoSPersonal.getVardenhet());
        intHoSPersonal.setVardenhet(intVardenhet);

        return intHoSPersonal;
    }

    List<Status> convertToIntStatuses(List<CertificateStatus> certStatuses) {

        List<Status> intStatuses = new ArrayList<Status>();

        if (certStatuses == null || certStatuses.isEmpty()) {
            LOG.debug("No statuses found to convert");
            return intStatuses;
        }

        LOG.debug("Converting {} statuses to internal", certStatuses.size());

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

    Patient convertToIntPatient(se.inera.certificate.model.Patient extPatient) {

        LOG.debug("Converting patient");

        if (extPatient == null) {
            LOG.debug("No Patient found to convert");
            return null;
        }

        Patient intPatient = new Patient();

        intPatient.setPersonId(InternalModelConverterUtils.getValueFromId(extPatient.getId()));

        String efterNamn = StringUtils.join(extPatient.getEfternamns(), " ");
        intPatient.setEfternamn(efterNamn);

        String forNamn = StringUtils.join(extPatient.getFornamns(), " ");
        intPatient.setFornamn(forNamn);

        String fullstandigtNamn = forNamn.concat(" ").concat(efterNamn);
        intPatient.setFullstandigtnamn(fullstandigtNamn);
        
        intPatient.setPostadress(extPatient.getPostadress());
        intPatient.setPostnummer(extPatient.getPostnummer());
        intPatient.setPostort(extPatient.getPostort());

        return intPatient;
    }

    Arrangemang convertToIntArrangemang(se.inera.certificate.modules.rli.model.external.Arrangemang extArr) {

        LOG.debug("Converting arrangemang");

        if (extArr == null) {
            LOG.debug("- No arrangemang found to convert");
            return null;
        }

        Arrangemang intArr = new Arrangemang();

        intArr.setPlats(extArr.getPlats());

        String arrTypCode = InternalModelConverterUtils.getValueFromKod(extArr.getArrangemangstyp());
        ArrangemangsTyp arrTyp = ArrangemangsTyp.getFromCode(arrTypCode);
        intArr.setArrangemangstyp(arrTyp);

        intArr.setBokningsreferens(extArr.getBokningsreferens());

        Partial extBokningsDatum = extArr.getBokningsdatum();
        intArr.setBokningsdatum(PartialConverter.partialToString(extBokningsDatum));

        Partial extAvbestDatum = extArr.getAvbestallningsdatum();
        intArr.setAvbestallningsdatum(PartialConverter.partialToString(extAvbestDatum));

        PartialInterval arrangemangsTid = extArr.getArrangemangstid();

        if (arrangemangsTid != null) {
            intArr.setArrangemangdatum(PartialConverter.partialToString(arrangemangsTid.getFrom()));
        }

        return intArr;
    }

}
