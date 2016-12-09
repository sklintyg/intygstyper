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

package se.inera.intyg.intygstyper.ts_diabetes.model.converter;

import static se.inera.intyg.intygstyper.ts_parent.codes.RespConstants.VARDKONTAKT_TYP;
import static se.inera.intyg.intygstyper.ts_parent.model.converter.TransportToInternalUtil.getTextVersion;

import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.util.integration.schema.adapter.InternalDateAdapter;
import se.inera.intyg.intygstyper.ts_diabetes.model.internal.*;
import se.inera.intyg.intygstyper.ts_diabetes.model.internal.Diabetes;
import se.inera.intyg.intygstyper.ts_diabetes.model.internal.Hypoglykemier;
import se.inera.intyg.intygstyper.ts_diabetes.support.TsDiabetesEntryPoint;
import se.inera.intyg.intygstyper.ts_parent.codes.IdKontrollKod;
import se.inera.intyg.intygstyper.ts_parent.model.converter.TransportToInternalUtil;
import se.inera.intygstjanster.ts.services.v1.*;

public final class TransportToInternalConverter {

    private TransportToInternalConverter() {
    }

    public static Utlatande convert(TSDiabetesIntyg transport) throws ConverterException {
        if (transport == null) {
            throw new ConverterException("Source utlatande was null, cannot convert");
        }

        Utlatande result = new Utlatande();

        result.setId(transport.getIntygsId());
        result.setTyp(TsDiabetesEntryPoint.MODULE_ID);
        result.setGrundData(TransportToInternalUtil.buildGrundData(transport.getGrundData()));
        readDiabetes(result.getDiabetes(), transport.getDiabetes());
        readHypoglykemier(result.getHypoglykemier(), transport.getHypoglykemier());
        result.getSyn().setSeparatOgonlakarintyg(transport.isSeparatOgonLakarintygKommerSkickas());
        readSyn(result.getSyn(), transport.getSynfunktion());
        readBedomning(result.getBedomning(), transport.getBedomning());
        readIntygAvser(result.getIntygAvser(), transport.getIntygAvser());
        result.setVardkontakt(readVardkontakt(transport.getIdentitetStyrkt()));
        result.setKommentar(transport.getOvrigKommentar());
        result.setTextVersion(getTextVersion(transport.getVersion(), transport.getUtgava()));
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
        if (diabetes2.getDiabetesTyp().get(0) != null) {
            diabetes.setDiabetestyp(TransportToInternalUtil.convertDiabetesTyp(diabetes2.getDiabetesTyp().get(0)).name());
        }
        diabetes.setEndastKost(diabetes2.isHarBehandlingKost());
        diabetes.setInsulin(diabetes2.isHarBehandlingInsulin());
        diabetes.setInsulinBehandlingsperiod(diabetes2.getInsulinBehandlingSedanAr());
        diabetes.setObservationsperiod(diabetes2.getDebutArDiabetes());
        diabetes.setTabletter(diabetes2.isHarBehandlingTabletter());
    }
}
