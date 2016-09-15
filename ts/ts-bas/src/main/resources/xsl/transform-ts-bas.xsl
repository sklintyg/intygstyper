<?xml version="1.0" encoding="UTF-8"?>
<!--
  ~ Copyright (C) 2016 Inera AB (http://www.inera.se)
  ~
  ~ This file is part of sklintyg (https://github.com/sklintyg).
  ~
  ~ sklintyg is free software: you can redistribute it and/or modify
  ~ it under the terms of the GNU General Public License as published by
  ~ the Free Software Foundation, either version 3 of the License, or
  ~ (at your option) any later version.
  ~
  ~ sklintyg is distributed in the hope that it will be useful,
  ~ but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~ MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
  ~ GNU General Public License for more details.
  ~
  ~ You should have received a copy of the GNU General Public License
  ~ along with this program.  If not, see <http://www.gnu.org/licenses/>.
  -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0"
    xmlns:ns1="urn:local:se:intygstjanster:services:1"
    xmlns:ns2="urn:local:se:intygstjanster:services:RegisterTSBasResponder:1"
    xmlns:ns3="urn:local:se:intygstjanster:services:types:1"
    xmlns:p="urn:riv:clinicalprocess:healthcond:certificate:1"
    xmlns:p2="urn:riv:clinicalprocess:healthcond:certificate:ts-bas:1"
    xmlns:reg="urn:riv:clinicalprocess:healthcond:certificate:RegisterCertificateResponder:1">

  <xsl:output method="xml" indent="yes"/>

  <xsl:include href="xsl/transform-ts-common.xsl"/>

  <xsl:variable name="ts-bas-ns" select="'urn:riv:clinicalprocess:healthcond:certificate:ts-bas:1'"/>
  <xsl:variable name="ts-bas-prefix" select="'p2'"/>

  <xsl:template match="ns2:intyg">
    <reg:RegisterCertificate>
      <reg:utlatande>

        <xsl:call-template name="utlatandeHeader">
          <xsl:with-param name="displayName" select="'TSTRK1007'"/>
        </xsl:call-template>

        <xsl:call-template name="grundData">
          <xsl:with-param name="ns-namespace" select="$ts-bas-ns"/>
          <xsl:with-param name="ns-prefix" select="$ts-bas-prefix"/>
        </xsl:call-template>

        <xsl:call-template name="vardKontakt"/>

        <!-- Synfältsprövning (Donders konfrontationsmetod) -->
        <xsl:call-template name="ogatsSynfaltAktivitet">
          <xsl:with-param name="ns-namespace" select="$ts-bas-ns"/>
          <xsl:with-param name="ns-prefix" select="$ts-bas-prefix"/>
        </xsl:call-template>

        <xsl:call-template name="ogatsRorlighetAktivitet"/>

        <!-- Undersökning med 8+ dioptrier -->
        <xsl:if test="ns1:synfunktion/ns1:harGlasStyrkaOver8Dioptrier = 'true' or ns1:synfunktion/ns1:harGlasStyrkaOver8Dioptrier = '1'">
          <p:aktivitet>
            <p:aktivitetskod code="AKT17" codeSystem="{$id_kv_aktiviteter_intyg}" codeSystemName="kv_aktiviteter_intyg"/>
            <p:forekomst>true</p:forekomst>
          </p:aktivitet>
        </xsl:if>

        <!-- Vårdinsats för missbruk eller beroende av alkohol, narkotika eller läkemedel (11.2) -->
        <p:aktivitet>
          <p:aktivitetskod code="AKT15" codeSystem="{$id_kv_aktiviteter_intyg}" codeSystemName="kv_aktiviteter_intyg"/>
          <p:forekomst>
            <xsl:value-of select="ns1:alkoholNarkotikaLakemedel/ns1:harVardinsats"/>
          </p:forekomst>
        </p:aktivitet>

        <!-- Provtagning avseende narkotika eller alkohol -->
        <xsl:if test="ns1:alkoholNarkotikaLakemedel/ns1:harVardinsatsProvtagningBehov">
          <p:aktivitet>
            <p:aktivitetskod code="AKT14" codeSystem="{$id_kv_aktiviteter_intyg}" codeSystemName="kv_aktiviteter_intyg"/>
            <p:forekomst>
              <xsl:value-of select="ns1:alkoholNarkotikaLakemedel/ns1:harVardinsatsProvtagningBehov"/>
            </p:forekomst>
          </p:aktivitet>
        </xsl:if>

        <!-- Vård på sjukhus (16.1) -->
        <xsl:if test="ns1:sjukhusvard">
          <p:aktivitet>
            <p:aktivitetskod code="AKT19" codeSystem="{$id_kv_aktiviteter_intyg}" codeSystemName="kv_aktiviteter_intyg"/>
            <xsl:if test="ns1:sjukhusvard/ns1:sjukhusvardEllerLakarkontaktAnledning">
              <p:beskrivning>
                <xsl:value-of select="ns1:sjukhusvard/ns1:sjukhusvardEllerLakarkontaktAnledning"/>
              </p:beskrivning>
            </xsl:if>
            <p:forekomst>
              <xsl:value-of select="ns1:sjukhusvard/ns1:harSjukhusvardEllerLakarkontakt"/>
            </p:forekomst>
            <xsl:if test="ns1:sjukhusvard/ns1:sjukhusvardEllerLakarkontaktVardinrattning">
              <p2:plats>
                <xsl:value-of select="ns1:sjukhusvard/ns1:sjukhusvardEllerLakarkontaktVardinrattning"/>
              </p2:plats>
            </xsl:if>
            <xsl:if test="ns1:sjukhusvard/ns1:sjukhusvardEllerLakarkontaktDatum">
              <p2:ostruktureradtid>
                <xsl:value-of select="ns1:sjukhusvard/ns1:sjukhusvardEllerLakarkontaktDatum"/>
              </p2:ostruktureradtid>
            </xsl:if>
          </p:aktivitet>
        </xsl:if>

        <p:rekommendation>
          <p:rekommendationskod code="REK8" codeSystem="{$id_kv_rekommendation_intyg}" codeSystemName="kv_rekommendation_intyg"/>
          <xsl:choose>
            <xsl:when test="ns1:bedomning/ns1:kanInteTaStallning = 'true' or ns1:bedomning/ns1:kanInteTaStallning = '1'">
              <p2:varde code="VAR11" codeSystem="{$id_kv_korkortsbehorighet}" codeSystemName="kv_körkortsbehörighet"/>
            </xsl:when>
            <xsl:otherwise>
              <xsl:for-each select="ns1:bedomning/ns1:korkortstyp">
                <p2:varde codeSystem="{$id_kv_korkortsbehorighet}" codeSystemName="kv_körkortsbehörighet" code="{$korkortsTyp/mapping[@key = current()]/@value}"/>
              </xsl:for-each>
            </xsl:otherwise>
          </xsl:choose>
        </p:rekommendation>

        <xsl:if test="ns1:bedomning/ns1:behovAvLakareSpecialistKompetens">
          <p:rekommendation>
            <p:rekommendationskod code="REK9" codeSystem="{$id_kv_rekommendation_intyg}" codeSystemName="kv_rekommendation_intyg"/>
            <p:beskrivning>
              <xsl:value-of select="ns1:bedomning/ns1:behovAvLakareSpecialistKompetens"/>
            </p:beskrivning>
          </p:rekommendation>
        </xsl:if>

        <!-- Synfältsprövning -->
        <p:observation>
          <p:observations-id root="1.2.752.129.2.1.2.1" extension="{$observations-id1}"/>
          <p:observationskod code="H53.4" codeSystem="1.2.752.116.1.1.1.1.1" codeSystemName="ICD-10"/>
          <p:forekomst>
            <xsl:value-of select="ns1:synfunktion/ns1:harSynfaltsdefekt"/>
          </p:forekomst>
        </p:observation>

        <!-- Begränsning av seende vid nedsatt belysning -->
        <p:observation>
          <p:observationskod code="H53.6" codeSystem="{$id_icd-10}" codeSystemName="ICD-10"/>
          <p:forekomst>
            <xsl:value-of select="ns1:synfunktion/ns1:harNattblindhet"/>
          </p:forekomst>
        </p:observation>

        <!-- Progressiv ögonsjukdom -->
        <p:observation>
          <p:observationskod code="OBS1" codeSystem="{$id_kv_observationer_intyg}" codeSystemName="kv_observationer_intyg"/>
          <p:forekomst>
            <xsl:value-of select="ns1:synfunktion/ns1:harProgressivOgonsjukdom"/>
          </p:forekomst>
        </p:observation>

        <xsl:call-template name="ogatsRorlighetObservation"/>

        <!-- Nystagmus -->
        <p:observation>
          <p:observationskod code="H55.9" codeSystem="{$id_icd-10}" codeSystemName="ICD-10"/>
          <p:forekomst>
            <xsl:value-of select="ns1:synfunktion/ns1:harNystagmus"/>
          </p:forekomst>
        </p:observation>

        <xsl:call-template name="synfunktionObservation">
          <xsl:with-param name="ns-namespace" select="$ts-bas-ns"/>
          <xsl:with-param name="ns-prefix" select="$ts-bas-prefix"/>
        </xsl:call-template>

        <!-- Kontaktlinser -->
        <xsl:if test="ns1:synfunktion/ns1:synskarpaMedKorrektion/ns1:harKontaktlinsHogerOga">
          <p:observation>
            <p:observationskod code="285049007" codeSystem="{$id_snomed-ct}" codeSystemName="SNOMED-CT"/>
            <p:forekomst>
              <xsl:value-of select="ns1:synfunktion/ns1:synskarpaMedKorrektion/ns1:harKontaktlinsHogerOga"/>
            </p:forekomst>
            <p2:lateralitet code="24028007" codeSystem="{$id_snomed-ct}" codeSystemName="SNOMED-CT"/>
          </p:observation>
        </xsl:if>

        <xsl:if test="ns1:synfunktion/ns1:synskarpaMedKorrektion/ns1:harKontaktlinsVansterOga">
          <p:observation>
            <p:observationskod code="285049007" codeSystem="{$id_snomed-ct}" codeSystemName="SNOMED-CT"/>
            <p:forekomst>
              <xsl:value-of select="ns1:synfunktion/ns1:synskarpaMedKorrektion/ns1:harKontaktlinsVansterOga"/>
            </p:forekomst>
            <p2:lateralitet code="7771000" codeSystem="{$id_snomed-ct}" codeSystemName="SNOMED-CT"/>
          </p:observation>
        </xsl:if>

        <!-- Anfall av balansrubbningar eller yrsel -->
        <p:observation>
          <p:observationskod code="OBS2" codeSystem="{$id_kv_observationer_intyg}" codeSystemName="kv_observationer_intyg"/>
          <p:forekomst>
            <xsl:value-of select="ns1:horselBalanssinne/ns1:harBalansrubbningYrsel"/>
          </p:forekomst>
        </p:observation>

        <!-- Uppfatta samtal 4 meter -->
        <xsl:if test="ns1:horselBalanssinne/ns1:harSvartUppfattaSamtal4Meter">
          <p:observation>
            <p:observationskod code="OBS3" codeSystem="{$id_kv_observationer_intyg}" codeSystemName="kv_observationer_intyg"/>
            <p:forekomst>
              <xsl:value-of select="ns1:horselBalanssinne/ns1:harSvartUppfattaSamtal4Meter"/>
            </p:forekomst>
          </p:observation>
        </xsl:if>

        <!-- Rörelseorganens funktioner -->
        <p:observation>
          <p:observationskod code="OBS4" codeSystem="{$id_kv_observationer_intyg}" codeSystemName="kv_observationer_intyg"/>
          <xsl:if test="ns1:rorelseorganensFunktioner/ns1:rorelsebegransningBeskrivning">
            <p:beskrivning>
              <xsl:value-of select="ns1:rorelseorganensFunktioner/ns1:rorelsebegransningBeskrivning"/>
            </p:beskrivning>
          </xsl:if>
          <p:forekomst>
            <xsl:value-of select="ns1:rorelseorganensFunktioner/ns1:harRorelsebegransning"/>
          </p:forekomst>
        </p:observation>

        <!-- Oförmåga hjälpa passagerare -->
        <xsl:if test="ns1:rorelseorganensFunktioner/ns1:harOtillrackligRorelseformagaPassagerare">
          <p:observation>
            <p:observationskod code="OBS5" codeSystem="{$id_kv_observationer_intyg}" codeSystemName="kv_observationer_intyg"/>
            <p:forekomst>
              <xsl:value-of select="ns1:rorelseorganensFunktioner/ns1:harOtillrackligRorelseformagaPassagerare"/>
            </p:forekomst>
          </p:observation>
        </xsl:if>

        <!-- Hjärt och kärlsjukdomar (6.1 - 6.3) -->

        <!-- Hjärt- och kärlsjukdom som innebär en trafiksäkerhetsrisk -->
        <p:observation>
          <p:observationskod code="OBS6" codeSystem="{$id_kv_observationer_intyg}" codeSystemName="kv_observationer_intyg"/>
          <p:forekomst>
            <xsl:value-of select="ns1:hjartKarlSjukdomar/ns1:harRiskForsamradHjarnFunktion"/>
          </p:forekomst>
        </p:observation>

        <p:observation>
          <p:observationskod code="OBS8" codeSystem="{$id_kv_observationer_intyg}" codeSystemName="kv_observationer_intyg"/>
          <p:forekomst>
            <xsl:value-of select="ns1:hjartKarlSjukdomar/ns1:harHjarnskadaICNS"/>
          </p:forekomst>
        </p:observation>

        <p:observation>
          <p:observationskod code="OBS7" codeSystem="{$id_kv_observationer_intyg}" codeSystemName="kv_observationer_intyg"/>
          <xsl:if test="ns1:hjartKarlSjukdomar/ns1:riskfaktorerStrokeBeskrivning">
            <p:beskrivning>
              <xsl:value-of select="ns1:hjartKarlSjukdomar/ns1:riskfaktorerStrokeBeskrivning"/>
            </p:beskrivning>
          </xsl:if>
          <p:forekomst>
            <xsl:value-of select="ns1:hjartKarlSjukdomar/ns1:harRiskfaktorerStroke"/>
          </p:forekomst>
        </p:observation>

        <!-- Diabetes (7.2) -->
        <p:observation>
          <p:observationskod code="73211009" codeSystem="{$id_snomed-ct}" codeSystemName="SNOMED-CT"/>
          <p:forekomst>
            <xsl:value-of select="ns1:diabetes/ns1:harDiabetes"/>
          </p:forekomst>
        </p:observation>

        <xsl:if test="ns1:diabetes/ns1:harDiabetes = 'true' or ns1:diabetes/ns1:harDiabetes = '1'">
          <xsl:choose>

            <xsl:when test="ns1:diabetes/ns1:diabetesTyp = 'TYP1'">
              <!-- typ1 -->
              <p:observation>
                <p:observationskod code="E10" codeSystem="{$id_icd-10}" codeSystemName="ICD-10"/>
                <p:forekomst>true</p:forekomst>
              </p:observation>
            </xsl:when>

            <xsl:otherwise>
              <!-- typ2 -->
              <p:observation>
                <p:observationskod code="E11" codeSystem="{$id_icd-10}" codeSystemName="ICD-10"/>
                <p:forekomst>true</p:forekomst>
              </p:observation>

              <!-- Kostbehandling -->
              <xsl:if test="ns1:diabetes/ns1:harBehandlingKost = 'true' or ns1:diabetes/ns1:harBehandlingKost = '1'">
                <p:observation>
                  <p:observationskod code="OBS9" codeSystem="{$id_kv_observationer_intyg}" codeSystemName="kv_observationer_intyg"/>
                  <p:forekomst>true</p:forekomst>
                </p:observation>
              </xsl:if>

              <!-- Tablettbehandling -->
              <xsl:if test="ns1:diabetes/ns1:harBehandlingTabletter = 'true' or ns1:diabetes/ns1:harBehandlingTabletter = '1'">
                <p:observation>
                  <p:observationskod code="170746002" codeSystem="{$id_snomed-ct}" codeSystemName="SNOMED-CT"/>
                  <p:forekomst>true</p:forekomst>
                </p:observation>
              </xsl:if>

              <!-- Insulinbehandling -->
              <xsl:if test="ns1:diabetes/ns1:harBehandlingInsulin = 'true' or ns1:diabetes/ns1:harBehandlingInsulin = '1'">
                <p:observation>
                  <p:observationskod code="170747006" codeSystem="{$id_snomed-ct}" codeSystemName="SNOMED-CT"/>
                  <p:forekomst>true</p:forekomst>
                </p:observation>
              </xsl:if>
            </xsl:otherwise>

          </xsl:choose>
        </xsl:if>

        <!-- Neurologiska sjukdomar (8) -->
        <p:observation>
          <p:observationskod code="407624006" codeSystem="{$id_snomed-ct}" codeSystemName="SNOMED-CT"/>
          <p:forekomst>
            <xsl:value-of select="ns1:neurologiskaSjukdomar"/>
          </p:forekomst>
        </p:observation>

        <!-- Epilepsi etc (9.1) -->
        <p:observation>
          <p:observationskod code="G40.9" codeSystem="{$id_icd-10}" codeSystemName="ICD-10"/>
          <xsl:if test="ns1:medvetandestorning/ns1:medvetandestorningBeskrivning">
            <p:beskrivning>
              <xsl:value-of select="ns1:medvetandestorning/ns1:medvetandestorningBeskrivning"/>
            </p:beskrivning>
          </xsl:if>
          <p:forekomst>
            <xsl:value-of select="ns1:medvetandestorning/ns1:harMedvetandestorning"/>
          </p:forekomst>
        </p:observation>

        <!-- Njursjukdomar -->
        <p:observation>
          <p:observationskod code="OBS11" codeSystem="{$id_kv_observationer_intyg}" codeSystemName="kv_observationer_intyg"/>
          <p:forekomst>
            <xsl:value-of select="ns1:harNjurSjukdom"/>
          </p:forekomst>
        </p:observation>

        <!-- Demens och kognitiva störningar -->
        <p:observation>
          <p:observationskod code="OBS12" codeSystem="{$id_kv_observationer_intyg}" codeSystemName="kv_observationer_intyg"/>
          <p:forekomst>
            <xsl:value-of select="ns1:harKognitivStorning"/>
          </p:forekomst>
        </p:observation>

        <!-- Sömn- och vakenhetsstörningar -->
        <p:observation>
          <p:observationskod code="OBS13" codeSystem="{$id_kv_observationer_intyg}" codeSystemName="kv_observationer_intyg"/>
          <p:forekomst>
            <xsl:value-of select="ns1:harSomnVakenhetStorning"/>
          </p:forekomst>
        </p:observation>

        <!-- Alkohol, narkotika och läkemedel (13.1 - 13.2) -->
        <p:observation>
          <p:observationskod code="OBS14" codeSystem="{$id_kv_observationer_intyg}" codeSystemName="kv_observationer_intyg"/>
          <p:forekomst>
            <xsl:value-of select="ns1:alkoholNarkotikaLakemedel/ns1:harTeckenMissbruk"/>
          </p:forekomst>
        </p:observation>

        <!-- Regelbundet läkarordinerat bruk av läkemedel som kan innebära en trafiksäkerhetsrisk -->
        <p:observation>
          <p:observationskod code="OBS15" codeSystem="{$id_kv_observationer_intyg}" codeSystemName="kv_observationer_intyg"/>
          <xsl:if test="ns1:alkoholNarkotikaLakemedel/ns1:lakarordineratLakemedelOchDos">
            <p:beskrivning>
              <xsl:value-of select="ns1:alkoholNarkotikaLakemedel/ns1:lakarordineratLakemedelOchDos"/>
            </p:beskrivning>
          </xsl:if>
          <p:forekomst>
            <xsl:value-of select="ns1:alkoholNarkotikaLakemedel/ns1:harLakarordineratLakemedelsbruk"/>
          </p:forekomst>
        </p:observation>

        <!-- Psykisk sjukdom eller störning (14) -->
        <p:observation>
          <p:observationskod code="OBS16" codeSystem="{$id_kv_observationer_intyg}" codeSystemName="kv_observationer_intyg"/>
          <p:forekomst>
            <xsl:value-of select="ns1:harPsykiskStorning"/>
          </p:forekomst>
        </p:observation>

        <!-- Psykisk utvecklingsstörning (15.1) -->
        <p:observation>
          <p:observationskod code="129104009" codeSystem="{$id_snomed-ct}" codeSystemName="SNOMED-CT"/>
          <p:forekomst>
            <xsl:value-of select="ns1:utvecklingsstorning/ns1:harPsykiskUtvecklingsstorning"/>
          </p:forekomst>
        </p:observation>

        <!-- ADHD DAMP (15.2) -->
        <p:observation>
          <p:observationskod code="OBS17" codeSystem="{$id_kv_observationer_intyg}" codeSystemName="kv_observationer_intyg"/>
          <p:forekomst>
            <xsl:value-of select="ns1:utvecklingsstorning/ns1:harAndrayndrom"/>
          </p:forekomst>
        </p:observation>

        <!-- Stadigvarande medicinering (17.1) -->
        <p:observation>
          <p:observationskod code="OBS18" codeSystem="{$id_kv_observationer_intyg}" codeSystemName="kv_observationer_intyg"/>
          <xsl:if test="ns1:ovrigMedicinering/ns1:stadigvarandeMedicineringBeskrivning">
            <p:beskrivning>
              <xsl:value-of select="ns1:ovrigMedicinering/ns1:stadigvarandeMedicineringBeskrivning"/>
            </p:beskrivning>
          </xsl:if>
          <p:forekomst>
            <xsl:value-of select="ns1:ovrigMedicinering/ns1:harStadigvarandeMedicinering"/>
          </p:forekomst>
        </p:observation>

        <p2:observationAktivitetRelation>
          <p2:observationsid root="1.2.752.129.2.1.2.1" extension="{$observations-id1}"/>
          <p2:aktivitetsid root="1.2.752.129.2.1.2.1" extension="{$aktivitets-id1}"/>
        </p2:observationAktivitetRelation>

        <p2:observationAktivitetRelation>
          <p2:observationsid root="1.2.752.129.2.1.2.1" extension="{$observations-id2}"/>
          <p2:aktivitetsid root="1.2.752.129.2.1.2.1" extension="{$aktivitets-id2}"/>
        </p2:observationAktivitetRelation>

        <xsl:for-each select="ns1:intygAvser/ns1:korkortstyp">
          <p2:intygAvser codeSystem="{$id_kv_intyget_avser}" codeSystemName="kv_intyget_avser" code="{$intygAvser/mapping[@key = current()]/@value}"/>
        </xsl:for-each>

        <p2:utgava>
          <xsl:value-of select="ns1:utgava"/>
        </p2:utgava>
        <p2:version>
          <xsl:value-of select="ns1:version"/>
        </p2:version>

      </reg:utlatande>
    </reg:RegisterCertificate>
  </xsl:template>

  <!-- Dont output text nodes we dont transform  -->
  <xsl:template match="ns1:grundData/ns1:intygsTyp"/>

</xsl:stylesheet>
