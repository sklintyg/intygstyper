/*
 * Copyright (C) 2015 Inera AB (http://www.inera.se)
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

package se.inera.intyg.intygstyper.ts_diabetes.model.converter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.LocalDateTime;

import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.model.common.internal.Vardgivare;
import se.inera.intyg.common.support.modules.support.api.dto.Personnummer;
import se.inera.intyg.common.util.integration.schema.adapter.InternalDateAdapter;
import se.inera.intyg.intygstyper.ts_diabetes.model.codes.IdKontrollKod;
import se.inera.intyg.intygstyper.ts_diabetes.model.codes.UtlatandeKod;
import se.inera.intyg.intygstyper.ts_diabetes.model.internal.Bedomning;
import se.inera.intyg.intygstyper.ts_diabetes.model.internal.BedomningKorkortstyp;
import se.inera.intyg.intygstyper.ts_diabetes.model.internal.Diabetes;
import se.inera.intyg.intygstyper.ts_diabetes.model.internal.Hypoglykemier;
import se.inera.intyg.intygstyper.ts_diabetes.model.internal.IntygAvser;
import se.inera.intyg.intygstyper.ts_diabetes.model.internal.IntygAvserKategori;
import se.inera.intyg.intygstyper.ts_diabetes.model.internal.Syn;
import se.inera.intyg.intygstyper.ts_diabetes.model.internal.Synskarpevarden;
import se.inera.intyg.intygstyper.ts_diabetes.model.internal.Utlatande;
import se.inera.intyg.intygstyper.ts_diabetes.model.internal.Vardkontakt;
import se.inera.intygstjanster.ts.services.v1.BedomningTypDiabetes;
import se.inera.intygstjanster.ts.services.v1.DiabetesTypVarden;
import se.inera.intygstjanster.ts.services.v1.IdentitetStyrkt;
import se.inera.intygstjanster.ts.services.v1.IntygsAvserTypDiabetes;
import se.inera.intygstjanster.ts.services.v1.KorkortsbehorighetTsDiabetes;
import se.inera.intygstjanster.ts.services.v1.SkapadAv;
import se.inera.intygstjanster.ts.services.v1.SynfunktionDiabetes;
import se.inera.intygstjanster.ts.services.v1.TSDiabetesIntyg;

public final class TransportToInternalConverter {
    private static final String VARDKONTAKT_TYP = "5880005";

    public static final Map<DiabetesTypVarden, String> TYP_VARDEN_MAP;

    static {
        Map<DiabetesTypVarden, String> tempMap = new HashMap<>();
        tempMap.put(DiabetesTypVarden.TYP_2, "DIABETES_TYP_2");
        tempMap.put(DiabetesTypVarden.TYP_1, "DIABETES_TYP_1");

        TYP_VARDEN_MAP = Collections.unmodifiableMap(tempMap);
    }

    private TransportToInternalConverter() {
    }

    public static Utlatande convert(TSDiabetesIntyg transport) {
        Utlatande result = new Utlatande();

        result.setId(transport.getIntygsId());
        result.setTyp(UtlatandeKod.getCurrentVersion().getCode());
        result.setGrundData(readGrundData(transport.getGrundData()));
        readDiabetes(result.getDiabetes(), transport.getDiabetes());
        readHypoglykemier(result.getHypoglykemier(), transport.getHypoglykemier());
        result.getSyn().setSeparatOgonlakarintyg(transport.isSeparatOgonLakarintygKommerSkickas());
        readSyn(result.getSyn(), transport.getSynfunktion());
        readBedomning(result.getBedomning(), transport.getBedomning());
        readIntygAvser(result.getIntygAvser(), transport.getIntygAvser());
        result.setVardkontakt(readVardkontakt(transport.getIdentitetStyrkt()));
        result.setKommentar(transport.getOvrigKommentar());
        return result;
    }

    private static Vardkontakt readVardkontakt(IdentitetStyrkt identitetStyrkt) {
        Vardkontakt result = new Vardkontakt();
        result.setTyp(VARDKONTAKT_TYP);
        result.setIdkontroll(IdKontrollKod.fromCode(identitetStyrkt.getIdkontroll().value()).name());
        return result;
    }

    private static void readIntygAvser(IntygAvser intygAvser, IntygsAvserTypDiabetes intygAvser2) {
        for (KorkortsbehorighetTsDiabetes kbh : intygAvser2.getKorkortstyp()) {
            intygAvser.getKorkortstyp().add(IntygAvserKategori.valueOf(kbh.name().replaceAll("_", "")));
        }
    }

    private static void readBedomning(Bedomning bedomning, BedomningTypDiabetes bedomning2) {
        bedomning.setKanInteTaStallning(bedomning2.isKanInteTaStallning());
        bedomning.setKommentarer(bedomning2.getOvrigKommentar());
        bedomning.setLakareSpecialKompetens(bedomning2.getBehovAvLakareSpecialistKompetens());
        bedomning.setLamplighetInnehaBehorighet(bedomning2.isLamplighetInnehaBehorighetSpecial());

        for (KorkortsbehorighetTsDiabetes kbh : bedomning2.getKorkortstyp()) {
            bedomning.getKorkortstyp().add(BedomningKorkortstyp.valueOf(kbh.toString().replaceAll("_", "")));
        }
    }

    private static void readSyn(Syn syn, SynfunktionDiabetes synfunktion) {

        if (synfunktion != null) {
            //syn.setSeparatOgonlakarintyg(synfunktion.isFinnsSeparatOgonlakarintyg());
            syn.setBinokulart(readBinokulart(synfunktion));
            syn.setDiplopi(synfunktion.isHarDiplopi());
            syn.setHoger(readHoger(synfunktion));
            syn.setVanster(readVanster(synfunktion));
            syn.setSynfaltsprovning(synfunktion.isFinnsSynfaltsprovning());
            syn.setProvningOgatsRorlighet(synfunktion.isFinnsProvningOgatsRorlighet());
            syn.setSynfaltsprovningUtanAnmarkning(synfunktion.isSynfaltsprovningUtanAnmarkning());
        }
    }

    private static Synskarpevarden readVanster(SynfunktionDiabetes synfunktion) {
        Synskarpevarden result = new Synskarpevarden();
        result.setMedKorrektion(synfunktion.getSynskarpaMedKorrektion().getVansterOga());
        result.setUtanKorrektion(synfunktion.getSynskarpaUtanKorrektion().getVansterOga());
        return result;
    }

    private static Synskarpevarden readHoger(SynfunktionDiabetes synfunktion) {
        Synskarpevarden result = new Synskarpevarden();
        result.setMedKorrektion(synfunktion.getSynskarpaMedKorrektion().getHogerOga());
        result.setUtanKorrektion(synfunktion.getSynskarpaUtanKorrektion().getHogerOga());
        return result;
    }

    private static Synskarpevarden readBinokulart(SynfunktionDiabetes synfunktion) {
        Synskarpevarden result = new Synskarpevarden();
        result.setMedKorrektion(synfunktion.getSynskarpaMedKorrektion().getBinokulart());
        result.setUtanKorrektion(synfunktion.getSynskarpaUtanKorrektion().getBinokulart());
        return result;
    }

    private static void readHypoglykemier(Hypoglykemier hypoglykemier, se.inera.intygstjanster.ts.services.v1.Hypoglykemier source) {
        hypoglykemier.setAllvarligForekomst(source.isHarAllvarligForekomst());
        hypoglykemier.setAllvarligForekomstBeskrivning(source.getAllvarligForekomstBeskrivning());
        hypoglykemier.setAllvarligForekomstTrafikBeskrivning(source.getAllvarligForekomstTrafikBeskrivning());
        hypoglykemier.setAllvarligForekomstTrafiken(source.isHarAllvarligForekomstTrafiken());
        if (source.isHarAllvarligForekomstVakenTid() != null) {
            hypoglykemier.setAllvarligForekomstVakenTid(source.isHarAllvarligForekomstVakenTid());
            if (source.isHarAllvarligForekomstVakenTid() && InternalDateAdapter.parseInternalDate(source.getAllvarligForekomstVakenTidAr()) != null) {
                hypoglykemier.setAllvarligForekomstVakenTidObservationstid(InternalDateAdapter.parseInternalDate(source.getAllvarligForekomstVakenTidAr()));
            }
        }
        hypoglykemier.setEgenkontrollBlodsocker(source.isGenomforEgenkontrollBlodsocker());
        hypoglykemier.setKunskapOmAtgarder(source.isHarKunskapOmAtgarder());
        hypoglykemier.setSaknarFormagaKannaVarningstecken(source.isSaknarFormagaKannaVarningstecken());
        hypoglykemier.setTeckenNedsattHjarnfunktion(source.isHarTeckenNedsattHjarnfunktion());
    }

    private static void readDiabetes(Diabetes diabetes, se.inera.intygstjanster.ts.services.v1.Diabetes diabetes2) {
        diabetes.setAnnanBehandlingBeskrivning(diabetes2.getAnnanBehandlingBeskrivning());
        diabetes.setDiabetestyp(TYP_VARDEN_MAP.get(diabetes2.getDiabetesTyp().get(0)));
        diabetes.setEndastKost(diabetes2.isHarBehandlingKost());
        diabetes.setInsulin(diabetes2.isHarBehandlingInsulin());
        diabetes.setInsulinBehandlingsperiod(diabetes2.getInsulinBehandlingSedanAr());
        diabetes.setObservationsperiod(diabetes2.getDebutArDiabetes());
        diabetes.setTabletter(diabetes2.isHarBehandlingTabletter());
    }

    private static GrundData readGrundData(se.inera.intygstjanster.ts.services.v1.GrundData grundData) {
        GrundData result = new GrundData();
        result.setPatient(readPatient(grundData.getPatient()));
        result.setSigneringsdatum(grundData.getSigneringsTidstampel() != null ? LocalDateTime.parse(grundData.getSigneringsTidstampel()) : null);
        result.setSkapadAv(readSkapadAv(grundData.getSkapadAv()));
        return result;
    }

    private static HoSPersonal readSkapadAv(SkapadAv skapadAv) {
        HoSPersonal result = new HoSPersonal();
        //result.setForskrivarKod(skapadAv.get);
        result.setFullstandigtNamn(skapadAv.getFullstandigtNamn());
        result.setPersonId(skapadAv.getPersonId().getExtension());
        result.setVardenhet(readVardenhet(skapadAv.getVardenhet()));
        return result;
    }

    private static Vardenhet readVardenhet(se.inera.intygstjanster.ts.services.v1.Vardenhet vardenhet) {
        Vardenhet result = new Vardenhet();
        //result.setArbetsplatsKod(vardenhet.get);
        result.setEnhetsid(vardenhet.getEnhetsId().getExtension());
        result.setEnhetsnamn(vardenhet.getEnhetsnamn());
        //result.setEpost(epost);
        result.setPostadress(vardenhet.getPostadress());
        result.setPostnummer(vardenhet.getPostnummer());
        result.setPostort(vardenhet.getPostort());
        result.setTelefonnummer(vardenhet.getTelefonnummer());
        result.setVardgivare(readVardgivare(vardenhet.getVardgivare()));
        return result;
    }

    private static Vardgivare readVardgivare(se.inera.intygstjanster.ts.services.v1.Vardgivare vardgivare) {
        Vardgivare result = new Vardgivare();
        result.setVardgivarid(vardgivare.getVardgivarid().getExtension());
        result.setVardgivarnamn(vardgivare.getVardgivarnamn());
        return result;
    }

    private static Patient readPatient(se.inera.intygstjanster.ts.services.v1.Patient patient) {
        Patient result = new Patient();
        result.setEfternamn(patient.getEfternamn());
        result.setFornamn(patient.getFornamn());
        result.setFullstandigtNamn(patient.getFullstandigtNamn());
        result.setPersonId(new Personnummer(patient.getPersonId().getExtension()));
        result.setPostadress(patient.getPostadress());
        result.setPostnummer(patient.getPostnummer());
        result.setPostort(patient.getPostort());
        return result;
    }
}
