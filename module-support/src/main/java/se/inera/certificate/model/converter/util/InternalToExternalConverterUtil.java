package se.inera.certificate.model.converter.util;

import se.inera.certificate.model.*;
import se.inera.certificate.model.common.codes.CodeConverter;
import se.inera.certificate.model.common.codes.CodeSystem;
import se.inera.certificate.model.common.codes.HSpersonalKod;
import se.inera.certificate.model.common.codes.SpecialitetKod;
import se.inera.certificate.model.common.external.HosPersonal;
import se.inera.certificate.model.common.external.Vardenhet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class InternalToExternalConverterUtil {

    private static final String ARBETSPLATSKOD_ROOT = "1.2.752.29.4.71";

    /**
     * Convert from internal to external Patient.
     *
     * @param source
     *            {@link se.inera.certificate.model.common.internal.Patient}
     * @return external {@link se.inera.certificate.model.Patient}
     */
    public static Patient convertToExtPatient(se.inera.certificate.model.common.internal.Patient source) {
        Patient patient = new Patient();
        if (patient.getFornamn() != null) {
            patient.getFornamn().add(source.getFornamn());
        }
        if (source.getMellannamn() != null) {
            patient.getMellannamn().add(source.getMellannamn());
        }
        patient.setEfternamn(source.getEfternamn());
        patient.setId(InternalConverterUtil.createPersonId(source.getPersonId()));
        patient.setPostadress(source.getPostadress());
        patient.setPostnummer(source.getPostnummer());
        patient.setPostort(source.getPostort());

        return patient;
    }

    /**
     * Convert from internal to external HosPersonal.
     *
     * @param source
     *            internal {@link se.inera.certificate.model.common.internal.HoSPersonal}
     * @return external {@link HosPersonal}
     */
    public static HosPersonal convertToExtHosPersonal(se.inera.certificate.model.common.internal.HoSPersonal source) {
        HosPersonal hosPersonal = new HosPersonal();
        hosPersonal.setId(new Id(HSpersonalKod.HSA_ID.getCodeSystem(), source.getPersonId()));
        hosPersonal.setForskrivarkod(source.getForskrivarKod());
        hosPersonal.setNamn(source.getFullstandigtNamn());
        hosPersonal.setVardenhet(convertToExtVardenhet(source.getVardenhet()));
        hosPersonal.getBefattningar().addAll(source.getBefattningar());
        hosPersonal.getSpecialiteter().addAll(convertStringToCode(SpecialitetKod.class, source.getSpecialiteter()));

        return hosPersonal;
    }

    /**
     * Convert a String-representation (i.e the name of the enum constant representing that particular Kod) to a Kod
     * object.
     *
     * @param type
     *            the code enum (must extend {@link CodeSystem})
     * @param strings
     *            a list of Strings with the names of enum constants to convert
     * @return a list of {@link se.inera.certificate.model.Kod}
     */
    private static <E extends CodeSystem> Collection<Kod> convertStringToCode(Class<E> type, List<String> strings) {
        List<Kod> koder = new ArrayList<>();
        for (E enumValue : type.getEnumConstants()) {
            if (strings.contains(enumValue.toString())) {
                koder.add(CodeConverter.toKod(enumValue));
            }
        }

        return koder;
    }

    /**
     * Convert from internal to external Vardenhet.
     *
     * @param source
     *            {@link se.inera.certificate.model.common.internal.Vardenhet}
     * @return external {@link Vardenhet}
     */
    private static Vardenhet convertToExtVardenhet(se.inera.certificate.model.common.internal.Vardenhet source) {
        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setId(new Id(HSpersonalKod.HSA_ID.getCodeSystem(), source.getEnhetsid()));
        if (source.getArbetsplatsKod() != null) {
            vardenhet.setArbetsplatskod(new Id(ARBETSPLATSKOD_ROOT, source.getArbetsplatsKod()));
        }
        vardenhet.setNamn(source.getEnhetsnamn());
        vardenhet.setPostadress(source.getPostadress());
        vardenhet.setPostnummer(source.getPostnummer());
        vardenhet.setPostort(source.getPostort());
        vardenhet.setTelefonnummer(source.getTelefonnummer());
        vardenhet.setEpost(source.getEpost());
        vardenhet.setVardgivare(convertToExtVardgivare(source.getVardgivare()));
        return vardenhet;
    }

    /**
     * Convert from internal to external Vardenhet.
     *
     * @param source
     *            {@link se.inera.certificate.model.common.internal.Vardgivare}
     * @return external {@link se.inera.certificate.model.Vardgivare}
     */
    private static Vardgivare convertToExtVardgivare(se.inera.certificate.model.common.internal.Vardgivare source) {
        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setId(new Id(HSpersonalKod.HSA_ID.getCodeSystem(), source.getVardgivarid()));
        vardgivare.setNamn(source.getVardgivarnamn());
        return vardgivare;
    }
}
