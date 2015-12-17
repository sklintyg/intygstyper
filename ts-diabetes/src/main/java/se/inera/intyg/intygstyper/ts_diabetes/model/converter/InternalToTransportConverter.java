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

import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.intygstyper.ts_diabetes.model.codes.IdKontrollKod;
import se.inera.intyg.intygstyper.ts_diabetes.model.codes.UtlatandeKod;
import se.inera.intyg.intygstyper.ts_diabetes.model.internal.Bedomning;
import se.inera.intyg.intygstyper.ts_diabetes.model.internal.BedomningKorkortstyp;
import se.inera.intyg.intygstyper.ts_diabetes.model.internal.IntygAvser;
import se.inera.intyg.intygstyper.ts_diabetes.model.internal.IntygAvserKategori;
import se.inera.intyg.intygstyper.ts_diabetes.model.internal.Syn;
import se.inera.intyg.intygstyper.ts_diabetes.model.internal.Utlatande;
import se.inera.intyg.intygstyper.ts_diabetes.model.internal.Vardkontakt;
import se.inera.intyg.common.schemas.Constants;
import se.inera.intygstjanster.ts.services.RegisterTSDiabetesResponder.v1.RegisterTSDiabetesType;
import se.inera.intygstjanster.ts.services.types.v1.II;
import se.inera.intygstjanster.ts.services.v1.BedomningTypDiabetes;
import se.inera.intygstjanster.ts.services.v1.Diabetes;
import se.inera.intygstjanster.ts.services.v1.DiabetesTypVarden;
import se.inera.intygstjanster.ts.services.v1.GrundData;
import se.inera.intygstjanster.ts.services.v1.Hypoglykemier;
import se.inera.intygstjanster.ts.services.v1.IdentifieringsVarden;
import se.inera.intygstjanster.ts.services.v1.IdentitetStyrkt;
import se.inera.intygstjanster.ts.services.v1.IntygsAvserTypDiabetes;
import se.inera.intygstjanster.ts.services.v1.Korkortsbehorighet;
import se.inera.intygstjanster.ts.services.v1.KorkortsbehorighetTsDiabetes;
import se.inera.intygstjanster.ts.services.v1.Patient;
import se.inera.intygstjanster.ts.services.v1.SkapadAv;
import se.inera.intygstjanster.ts.services.v1.SynfunktionDiabetes;
import se.inera.intygstjanster.ts.services.v1.SynskarpaMedKorrektion;
import se.inera.intygstjanster.ts.services.v1.SynskarpaUtanKorrektion;
import se.inera.intygstjanster.ts.services.v1.TSDiabetesIntyg;
import se.inera.intygstjanster.ts.services.v1.Vardenhet;
import se.inera.intygstjanster.ts.services.v1.Vardgivare;

public final class InternalToTransportConverter {

    private static final String SIGNERINGS_TIDSTAMPEL_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    public static final Map<String, DiabetesTypVarden> TYP_VARDEN_MAP;

    private InternalToTransportConverter() {
    }

    static {
        Map<String, DiabetesTypVarden> tempMap = new HashMap<>();
        tempMap.put("DIABETES_TYP_2", DiabetesTypVarden.TYP_2);
        tempMap.put("DIABETES_TYP_1", DiabetesTypVarden.TYP_1);

        TYP_VARDEN_MAP = Collections.unmodifiableMap(tempMap);
    }

    public static RegisterTSDiabetesType convert(Utlatande utlatande) {
        RegisterTSDiabetesType registerTsDiabetes = new RegisterTSDiabetesType();
        TSDiabetesIntyg result = new TSDiabetesIntyg();

        result.setBedomning(readBedomning(utlatande.getBedomning()));
        result.setDiabetes(readDiabetes(utlatande.getDiabetes()));
        result.setGrundData(readGrundData(utlatande.getGrundData()));
        result.setHypoglykemier(readHypoglykemier(utlatande.getHypoglykemier()));
        result.setIdentitetStyrkt(readIdentitetStyrkt(utlatande.getVardkontakt()));
        result.setIntygAvser(readIntygAvser(utlatande.getIntygAvser()));
        result.setIntygsId(utlatande.getId());
        result.setIntygsTyp(UtlatandeKod.getCurrentVersion().getCode());
        result.setSeparatOgonLakarintygKommerSkickas(utlatande.getSyn().getSeparatOgonlakarintyg());
        result.setOvrigKommentar(utlatande.getKommentar());
        if (utlatande.getSyn().getSeparatOgonlakarintyg() != null && !utlatande.getSyn().getSeparatOgonlakarintyg()) {
            result.setSynfunktion(readSynfunktionDiabetes(utlatande.getSyn()));
        }
        result.setUtgava(UtlatandeKod.getCurrentVersion().getTsUtgava());
        result.setVersion(UtlatandeKod.getCurrentVersion().getTsVersion());
        registerTsDiabetes.setIntyg(result);
        return registerTsDiabetes;
    }

    private static IdentitetStyrkt readIdentitetStyrkt(Vardkontakt vardkontakt) {
        IdentitetStyrkt result = new IdentitetStyrkt();
        result.setIdkontroll(IdentifieringsVarden.fromValue(IdKontrollKod.valueOf(vardkontakt.getIdkontroll()).getCode()));
        return result;
    }

    private static SynfunktionDiabetes readSynfunktionDiabetes(Syn syn) {
        SynfunktionDiabetes result = new SynfunktionDiabetes();
        result.setHarDiplopi(syn.getDiplopi() != null && syn.getDiplopi());
        result.setSynskarpaMedKorrektion(readMedKorrektion(syn));
        result.setSynskarpaUtanKorrektion(readUtanKorrektion(syn));
        result.setFinnsSynfaltsprovning(syn.getSynfaltsprovning() != null && syn.getSynfaltsprovning());
        result.setSynfaltsprovningUtanAnmarkning(syn.getSynfaltsprovningUtanAnmarkning() != null && syn.getSynfaltsprovningUtanAnmarkning());
        result.setFinnsProvningOgatsRorlighet(syn.getProvningOgatsRorlighet() != null && syn.getProvningOgatsRorlighet());
        return result;
    }

    private static SynskarpaUtanKorrektion readUtanKorrektion(Syn syn) {
        SynskarpaUtanKorrektion result = new SynskarpaUtanKorrektion();
        result.setBinokulart(syn.getBinokulart() != null ? syn.getBinokulart().getUtanKorrektion() : null);
        result.setHogerOga(syn.getHoger() != null ? syn.getHoger().getUtanKorrektion() : null);
        result.setVansterOga(syn.getVanster() != null ? syn.getVanster().getUtanKorrektion() : null);
        return result;
    }

    private static SynskarpaMedKorrektion readMedKorrektion(Syn syn) {
        SynskarpaMedKorrektion result = new SynskarpaMedKorrektion();
        result.setBinokulart(syn.getBinokulart() != null ? syn.getBinokulart().getMedKorrektion() : null);
        result.setHogerOga(syn.getHoger() != null ? syn.getHoger().getMedKorrektion() : null);
        result.setVansterOga(syn.getVanster() != null ? syn.getVanster().getMedKorrektion() : null);
        return result;
    }

    private static IntygsAvserTypDiabetes readIntygAvser(IntygAvser intygAvser) {
        IntygsAvserTypDiabetes result = new IntygsAvserTypDiabetes();

        for (IntygAvserKategori kat : intygAvser.getKorkortstyp()) {
            KorkortsbehorighetTsDiabetes bh = KorkortsbehorighetTsDiabetes.fromValue(Korkortsbehorighet.fromValue(kat.name()));
            result.getKorkortstyp().add(bh);
        }

        return result;
    }

    private static Hypoglykemier readHypoglykemier(se.inera.intyg.intygstyper.ts_diabetes.model.internal.Hypoglykemier hypoglykemier) {
        Hypoglykemier result = new Hypoglykemier();
        result.setHarKunskapOmAtgarder(hypoglykemier.getKunskapOmAtgarder() != null && hypoglykemier.getKunskapOmAtgarder());
        result.setHarTeckenNedsattHjarnfunktion(hypoglykemier.getTeckenNedsattHjarnfunktion() != null
                && hypoglykemier.getTeckenNedsattHjarnfunktion());

        if (hypoglykemier.getSaknarFormagaKannaVarningstecken() != null) {
            result.setSaknarFormagaKannaVarningstecken(hypoglykemier.getSaknarFormagaKannaVarningstecken());
        }

        if (hypoglykemier.getAllvarligForekomst() != null) {
            result.setHarAllvarligForekomst(hypoglykemier.getAllvarligForekomst());
            result.setAllvarligForekomstBeskrivning(hypoglykemier.getAllvarligForekomstBeskrivning());
        }

        if (hypoglykemier.getAllvarligForekomstTrafiken() != null) {
            result.setHarAllvarligForekomstTrafiken(hypoglykemier.getAllvarligForekomstTrafiken());
            result.setAllvarligForekomstTrafikBeskrivning(hypoglykemier.getAllvarligForekomstTrafikBeskrivning());
        }

        if (hypoglykemier.getEgenkontrollBlodsocker() != null) {
            result.setGenomforEgenkontrollBlodsocker(hypoglykemier.getEgenkontrollBlodsocker());
        }

        if (hypoglykemier.getAllvarligForekomstVakenTid() != null) {
            result.setHarAllvarligForekomstVakenTid(hypoglykemier.getAllvarligForekomstVakenTid());
            result.setAllvarligForekomstVakenTidAr(hypoglykemier.getAllvarligForekomstVakenTidObservationstid() != null ? hypoglykemier
                    .getAllvarligForekomstVakenTidObservationstid().getDate() : null);
        }
        return result;
    }

    private static GrundData readGrundData(se.inera.intyg.common.support.model.common.internal.GrundData grundData) {
        GrundData result = new GrundData();
        result.setPatient(readPatient(grundData.getPatient()));
        result.setSigneringsTidstampel(grundData.getSigneringsdatum() != null ? grundData.getSigneringsdatum().toString(SIGNERINGS_TIDSTAMPEL_FORMAT)
                : null);
        result.setSkapadAv(readSkapadAv(grundData.getSkapadAv()));
        return result;
    }

    private static SkapadAv readSkapadAv(HoSPersonal skapadAv) {
        SkapadAv result = new SkapadAv();

        II ii = new II();
        ii.setRoot(Constants.HSA_ID_OID);
        ii.setExtension(skapadAv.getPersonId());

        result.setPersonId(ii);
        result.setFullstandigtNamn(skapadAv.getFullstandigtNamn());
        result.setVardenhet(readVardenhet(skapadAv.getVardenhet()));
        return result;
    }

    private static Vardenhet readVardenhet(se.inera.intyg.common.support.model.common.internal.Vardenhet vardenhet) {
        Vardenhet result = new Vardenhet();
        II ii = new II();
        ii.setRoot(Constants.HSA_ID_OID);
        ii.setExtension(vardenhet.getEnhetsid());

        result.setEnhetsId(ii);
        result.setEnhetsnamn(vardenhet.getEnhetsnamn());
        result.setPostadress(vardenhet.getPostadress());
        result.setPostnummer(vardenhet.getPostnummer());
        result.setPostort(vardenhet.getPostort());
        result.setTelefonnummer(vardenhet.getTelefonnummer());
        result.setVardgivare(readVardgivare(vardenhet.getVardgivare()));
        return result;
    }

    private static Vardgivare readVardgivare(se.inera.intyg.common.support.model.common.internal.Vardgivare vardgivare) {
        Vardgivare result = new Vardgivare();
        II ii = new II();
        ii.setRoot(Constants.HSA_ID_OID);
        ii.setExtension(vardgivare.getVardgivarid());

        result.setVardgivarid(ii);
        result.setVardgivarnamn(vardgivare.getVardgivarnamn());
        return result;
    }

    private static Patient readPatient(
            se.inera.intyg.common.support.model.common.internal.Patient patient) {
        Patient result = new Patient();
        result.setEfternamn(patient.getEfternamn());
        result.setFornamn(patient.getFornamn());
        result.setFullstandigtNamn(patient.getFullstandigtNamn());

        II iid = new II();
        iid.setRoot(Constants.PERSON_ID_OID);
        iid.setExtension(patient.getPersonId() != null ? patient.getPersonId().getPersonnummer() : null);
        result.setPersonId(iid);

        result.setPostadress(patient.getPostadress());
        result.setPostnummer(patient.getPostnummer());
        result.setPostort(patient.getPostort());
        return result;
    }

    private static Diabetes readDiabetes(se.inera.intyg.intygstyper.ts_diabetes.model.internal.Diabetes diabetes) {
        Diabetes result = new Diabetes();
        result.setAnnanBehandlingBeskrivning(diabetes.getAnnanBehandlingBeskrivning());
        result.setDebutArDiabetes(diabetes.getObservationsperiod());
        result.setHarBehandlingInsulin(diabetes.getInsulin());
        result.setHarBehandlingKost(diabetes.getEndastKost());
        result.setHarBehandlingTabletter(diabetes.getTabletter());
        result.setInsulinBehandlingSedanAr(diabetes.getInsulinBehandlingsperiod());

        result.getDiabetesTyp().add(TYP_VARDEN_MAP.get(diabetes.getDiabetestyp()));
        return result;
    }

    private static BedomningTypDiabetes readBedomning(Bedomning bedomning) {
        BedomningTypDiabetes result = new BedomningTypDiabetes();
        result.setBehovAvLakareSpecialistKompetens(bedomning.getLakareSpecialKompetens());
        result.setKanInteTaStallning(bedomning.getKanInteTaStallning());
        if (bedomning.getLamplighetInnehaBehorighet() != null) {
            result.setLamplighetInnehaBehorighetSpecial(bedomning.getLamplighetInnehaBehorighet());
        }
        result.setOvrigKommentar(bedomning.getKommentarer());

        for (BedomningKorkortstyp typ : bedomning.getKorkortstyp()) {
            result.getKorkortstyp().add(KorkortsbehorighetTsDiabetes.fromValue(Korkortsbehorighet.fromValue(typ.name())));
        }

        return result;
    }
}
