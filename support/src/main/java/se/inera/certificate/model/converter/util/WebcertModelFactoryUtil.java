package se.inera.certificate.model.converter.util;

import se.inera.certificate.model.common.internal.HoSPersonal;
import se.inera.certificate.model.common.internal.Patient;
import se.inera.certificate.model.common.internal.Vardenhet;
import se.inera.certificate.model.common.internal.Vardgivare;

public class WebcertModelFactoryUtil {

    public static Patient convertPatientToEdit(se.inera.certificate.modules.support.api.dto.Patient patientInfo) {
        Patient patient = new Patient();
        patient.setFornamn(patientInfo.getFornamn());
        patient.setMellannamn(patientInfo.getMellannamn());
        patient.setEfternamn(patientInfo.getEfternamn());
        patient.setFullstandigtNamn(patientInfo.getFullstandigtNamn());
        patient.setPersonId(patientInfo.getPersonnummer());
        patient.setPostadress(patientInfo.getPostadress());
        patient.setPostnummer(patientInfo.getPostnummer());
        patient.setPostort(patientInfo.getPostort());

        return patient;
    }

    public static HoSPersonal convertHosPersonalToEdit(se.inera.certificate.modules.support.api.dto.HoSPersonal hosPers) {
        HoSPersonal hosPersonal = new HoSPersonal();

        hosPersonal.setPersonId(hosPers.getHsaId());
        hosPersonal.setForskrivarKod(hosPers.getForskrivarkod());
        hosPersonal.setFullstandigtNamn(hosPers.getNamn());

        if (hosPers.getBefattning() != null) {
            hosPersonal.getBefattningar().add(hosPers.getBefattning());

        }

        if (hosPers.getVardenhet() != null) {
            Vardenhet vardenhet = convertVardenhetToEdit(hosPers.getVardenhet());
            hosPersonal.setVardenhet(vardenhet);
        }

        return hosPersonal;
    }

    private static Vardenhet convertVardenhetToEdit(se.inera.certificate.modules.support.api.dto.Vardenhet vardenhetDto) {

        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setEnhetsid(vardenhetDto.getHsaId());
        vardenhet.setEnhetsnamn(vardenhetDto.getNamn());
        vardenhet.setVardgivare(convertVardgivareToEdit(vardenhetDto.getVardgivare()));

        vardenhet.setPostadress(vardenhetDto.getPostadress());
        vardenhet.setPostort(vardenhetDto.getPostort());
        vardenhet.setPostnummer(vardenhetDto.getPostnummer());
        vardenhet.setTelefonnummer(vardenhetDto.getTelefonnummer());
        vardenhet.setEpost(vardenhetDto.getEpost());
        vardenhet.setArbetsplatsKod(vardenhetDto.getArbetsplatskod());

        return vardenhet;
    }

    private static Vardgivare convertVardgivareToEdit(se.inera.certificate.modules.support.api.dto.Vardgivare vardgivareDto) {

        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setVardgivarid(vardgivareDto.getHsaId());
        vardgivare.setVardgivarnamn(vardgivareDto.getNamn());

        return vardgivare;
    }
}
