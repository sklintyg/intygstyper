package se.inera.certificate.model.converter.util;

import se.inera.certificate.model.common.codes.CodeConverter;
import se.inera.certificate.model.common.codes.SpecialitetKod;
import se.inera.certificate.model.common.external.HosPersonal;
import se.inera.certificate.model.common.internal.HoSPersonal;
import se.inera.certificate.model.common.internal.Patient;
import se.inera.certificate.model.common.internal.Vardenhet;
import se.inera.certificate.model.common.internal.Vardgivare;
import se.inera.certificate.model.util.Strings;

public class ExternalToInternalConverterUtil {

    private static Vardenhet convertToIntVardenhet(se.inera.certificate.model.Vardenhet extVardenhet)
            throws ConverterException {
        if (extVardenhet == null) {
            throw new ConverterException("External Vardenhet is null, can not convert");
        }

        Vardenhet intVardenhet = new Vardenhet();

        intVardenhet.setEnhetsid(InternalConverterUtil.getExtensionFromId(extVardenhet.getId()));
        intVardenhet.setEnhetsnamn(extVardenhet.getNamn());
        intVardenhet.setPostadress(extVardenhet.getPostadress());
        intVardenhet.setPostnummer(extVardenhet.getPostnummer());
        intVardenhet.setPostort(extVardenhet.getPostort());
        intVardenhet.setTelefonnummer(extVardenhet.getTelefonnummer());
        intVardenhet.setEpost(extVardenhet.getEpost());

        if (extVardenhet.getArbetsplatskod() != null) {
            intVardenhet.setArbetsplatsKod(extVardenhet.getArbetsplatskod().getExtension());
        }

        Vardgivare intVardgivare = convertToIntVardgivare(extVardenhet.getVardgivare());
        intVardenhet.setVardgivare(intVardgivare);

        return intVardenhet;
    }

    private static Vardgivare convertToIntVardgivare(se.inera.certificate.model.Vardgivare extVardgivare)
            throws ConverterException {
        if (extVardgivare == null) {
            throw new ConverterException("External vardgivare is null, can not convert");
        }

        Vardgivare intVardgivare = new Vardgivare();

        intVardgivare.setVardgivarid(InternalConverterUtil.getExtensionFromId(extVardgivare.getId()));
        intVardgivare.setVardgivarnamn(extVardgivare.getNamn());

        return intVardgivare;
    }

    public static HoSPersonal convertToIntHoSPersonal(HosPersonal extHoSPersonal) throws ConverterException {
        if (extHoSPersonal == null) {
            throw new ConverterException("External HoSPersonal is null, can not convert");
        }

        HoSPersonal intHoSPersonal = new HoSPersonal();

        intHoSPersonal.setPersonId(InternalConverterUtil.getExtensionFromId(extHoSPersonal.getId()));
        intHoSPersonal.setForskrivarKod(extHoSPersonal.getForskrivarkod());
        intHoSPersonal.setFullstandigtNamn(extHoSPersonal.getNamn());

        intHoSPersonal.getBefattningar().addAll(extHoSPersonal.getBefattningar());
        intHoSPersonal.getSpecialiteter().addAll(
                CodeConverter.convertKodToString(extHoSPersonal.getSpecialiteter(), SpecialitetKod.class));

        Vardenhet intVardenhet = convertToIntVardenhet(extHoSPersonal.getVardenhet());
        intHoSPersonal.setVardenhet(intVardenhet);

        return intHoSPersonal;
    }

    public static Patient convertToIntPatient(se.inera.certificate.model.Patient extPatient) throws ConverterException {
        if (extPatient == null) {
            throw new ConverterException("No Patient found to convert");
        }

        Patient intPatient = new Patient();

        intPatient.setPersonId(InternalConverterUtil.getExtensionFromId(extPatient.getId()));

        intPatient.setFornamn(Strings.join(" ", extPatient.getFornamn()));
        if (!extPatient.getMellannamn().isEmpty()) {
            intPatient.setMellannamn(Strings.join(" ", extPatient.getMellannamn()));
        }
        intPatient.setEfternamn(extPatient.getEfternamn());
        intPatient.setFullstandigtNamn(extPatient.getFullstandigtNamn());

        intPatient.setPostadress(extPatient.getPostadress());
        intPatient.setPostnummer(extPatient.getPostnummer());
        intPatient.setPostort(extPatient.getPostort());

        return intPatient;
    }
}
