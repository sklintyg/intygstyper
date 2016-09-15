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
    xmlns:ns2="urn:local:se:intygstjanster:services:RegisterTSDiabetesResponder:1"
    xmlns:ns3="urn:local:se:intygstjanster:services:types:1"
    xmlns:p="urn:riv:clinicalprocess:healthcond:certificate:1"
    xmlns:p2="urn:riv:clinicalprocess:healthcond:certificate:ts-diabetes:1"
    xmlns:p3="urn:riv:clinicalprocess:healthcond:certificate:types:1"
    xmlns:reg="urn:riv:clinicalprocess:healthcond:certificate:RegisterCertificateResponder:1">

  <xsl:output method="xml" indent="yes"/>

  <xsl:include href="xsl/transform-ts-common.xsl"/>

  <xsl:variable name="ts-diabetes-ns" select="'urn:riv:clinicalprocess:healthcond:certificate:ts-diabetes:1'"/>
  <xsl:variable name="ts-diabetes-prefix" select="'p2'"/>

  <xsl:template match="ns2:intyg">
    <reg:RegisterCertificate>
      <reg:utlatande>

        <xsl:call-template name="utlatandeHeader">
          <xsl:with-param name="displayName" select="'TSTRK1031'"/>
        </xsl:call-template>

        <xsl:call-template name="grundData">
          <xsl:with-param name="ns-namespace" select="$ts-diabetes-ns"/>
          <xsl:with-param name="ns-prefix" select="$ts-diabetes-prefix"/>
        </xsl:call-template>

        <xsl:call-template name="vardKontakt"/>

        <!-- Egenkontroll av blodsocker -->
        <xsl:if test="ns1:hypoglykemier/ns1:genomforEgenkontrollBlodsocker">
          <p:aktivitet>
            <p:aktivitetskod code="308113006" codeSystem="{$id_snomed-ct}" codeSystemName="SNOMED-CT"/>
            <p:forekomst>
              <xsl:value-of select="ns1:hypoglykemier/ns1:genomforEgenkontrollBlodsocker"/>
            </p:forekomst>
          </p:aktivitet>
        </xsl:if>

        <xsl:if test="ns1:separatOgonLakarintygKommerSkickas = 'false' or ns1:separatOgonLakarintygKommerSkickas = '0'">
          <!-- Synfältsprövning (Donders konfrontationsmetod) -->
          <xsl:call-template name="ogatsSynfaltAktivitet">
            <xsl:with-param name="ns-namespace" select="$ts-diabetes-ns"/>
            <xsl:with-param name="ns-prefix" select="$ts-diabetes-prefix"/>
          </xsl:call-template>

          <!-- Prövning av ögats rörlighet -->
          <xsl:call-template name="ogatsRorlighetAktivitet"/>
        </xsl:if>

        <!-- Patienten uppfyller kraven -->
        <p:rekommendation>
          <p:rekommendationskod code="REK8" codeSystem="{$id_kv_rekommendation_intyg}" codeSystemName="kv_rekommendation_intyg"/>
          <xsl:choose>
            <xsl:when test="ns1:bedomning/ns1:kanInteTaStallning = 'true'">
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

        <xsl:if test="ns1:bedomning/ns1:lamplighetInnehaBehorighetSpecial">
          <p:rekommendation>
            <p:rekommendationskod code="REK10" codeSystem="{$id_kv_rekommendation_intyg}" codeSystemName="kv_rekommendation_intyg"/>
            <p2:varde>
              <xsl:value-of select="ns1:bedomning/ns1:lamplighetInnehaBehorighetSpecial"/>
            </p2:varde>
          </p:rekommendation>
        </xsl:if>

        <!-- Diabetes typ1 or typ2 -->
        <p:observation>
          <p:observationskod codeSystem="{$id_icd-10}" codeSystemName="ICD-10">
            <xsl:attribute name="code">
              <xsl:choose>
                <xsl:when test="ns1:diabetes/ns1:diabetesTyp = 'TYP1'">E10</xsl:when>
                <xsl:otherwise>E11</xsl:otherwise>
              </xsl:choose>
            </xsl:attribute>
          </p:observationskod>
          <p:observationsperiod>
            <p3:from>
              <xsl:value-of select="ns1:diabetes/ns1:debutArDiabetes"/>
            </p3:from>
          </p:observationsperiod>
          <p:forekomst>true</p:forekomst>
        </p:observation>

        <!-- Behandlingar -->
        <xsl:if test="ns1:diabetes/ns1:harBehandlingKost = 'true' or ns1:diabetes/ns1:harBehandlingKost = '1'">
          <p:observation>
            <p:observationskod code="170745003" codeSystem="{$id_snomed-ct}" codeSystemName="SNOMED-CT"/>
            <p:forekomst>true</p:forekomst>
          </p:observation>
        </xsl:if>

        <xsl:if test="ns1:diabetes/ns1:harBehandlingTabletter = 'true' or ns1:diabetes/ns1:harBehandlingTabletter = '1'">
          <p:observation>
            <p:observationskod code="170746002" codeSystem="{$id_snomed-ct}" codeSystemName="SNOMED-CT"/>
            <p:forekomst>true</p:forekomst>
          </p:observation>
        </xsl:if>

        <xsl:if test="ns1:diabetes/ns1:harBehandlingInsulin = 'true' or ns1:diabetes/ns1:harBehandlingInsulin = '1'">
          <p:observation>
            <p:observationskod code="170747006" codeSystem="{$id_snomed-ct}" codeSystemName="SNOMED-CT"/>
            <p:observationsperiod>
              <p3:from>
                <xsl:value-of select="ns1:diabetes/ns1:insulinBehandlingSedanAr"/>
              </p3:from>
            </p:observationsperiod>
            <p:forekomst>true</p:forekomst>
          </p:observation>
        </xsl:if>

        <xsl:if test="ns1:diabetes/ns1:annanBehandlingBeskrivning">
          <p:observation>
            <p:observationskod code="OBS10" codeSystem="{$id_kv_observationer_intyg}"
                codeSystemName="kv_observationer_intyg"/>
            <p:beskrivning>
              <xsl:value-of select="ns1:diabetes/ns1:annanBehandlingBeskrivning"/>
            </p:beskrivning>
            <p:forekomst>true</p:forekomst>
          </p:observation>
        </xsl:if>

        <!-- Hypoglykemi -->
        <!-- Har kunskap om åtgärder -->
        <p:observation>
          <p:observationskod code="OBS19" codeSystem="{$id_kv_observationer_intyg}"
              codeSystemName="kv_observationer_intyg"/>
          <p:forekomst>
            <xsl:value-of select="ns1:hypoglykemier/ns1:harKunskapOmAtgarder"/>
          </p:forekomst>
        </p:observation>

        <!-- Tecken på nedsatt hjärnfunktion -->
        <p:observation>
          <p:observationskod code="OBS20" codeSystem="{$id_kv_observationer_intyg}"
              codeSystemName="kv_observationer_intyg"/>
          <p:forekomst>
            <xsl:value-of select="ns1:hypoglykemier/ns1:harTeckenNedsattHjarnfunktion"/>
          </p:forekomst>
        </p:observation>

        <xsl:if test="ns1:hypoglykemier/ns1:harTeckenNedsattHjarnfunktion = 'true' or ns1:hypoglykemier/ns1:harTeckenNedsattHjarnfunktion = '1'">
          <p:observation>
            <p:observationskod code="OBS21" codeSystem="{$id_kv_observationer_intyg}"
                codeSystemName="kv_observationer_intyg"/>
            <p:forekomst>
              <xsl:value-of select="ns1:hypoglykemier/ns1:saknarFormagaKannaVarningstecken"/>
            </p:forekomst>
          </p:observation>

          <p:observation>
            <p:observationskod code="OBS22" codeSystem="{$id_kv_observationer_intyg}"
                codeSystemName="kv_observationer_intyg"/>
            <p:beskrivning>
              <xsl:value-of select="ns1:hypoglykemier/ns1:allvarligForekomstBeskrivning"/>
            </p:beskrivning>
            <p:forekomst>
              <xsl:value-of select="ns1:hypoglykemier/ns1:harAllvarligForekomst"/>
            </p:forekomst>
          </p:observation>

          <!-- Tecken på nedsatt hjärnfunktion -->
          <p:observation>
            <p:observationskod code="OBS23" codeSystem="{$id_kv_observationer_intyg}"
                codeSystemName="kv_observationer_intyg"/>
            <p:beskrivning>
              <xsl:value-of select="ns1:hypoglykemier/ns1:allvarligForekomstTrafikBeskrivning"/>
            </p:beskrivning>
            <p:forekomst>
              <xsl:value-of select="ns1:hypoglykemier/ns1:harAllvarligForekomstTrafiken"/>
            </p:forekomst>
          </p:observation>
        </xsl:if>

        <p:observation>
          <p:observationskod code="OBS24" codeSystem="{$id_kv_observationer_intyg}"
              codeSystemName="kv_observationer_intyg"/>
          <xsl:if test="ns1:hypoglykemier/ns1:harAllvarligForekomstVakenTid = 'true' or ns1:hypoglykemier/ns1:harAllvarligForekomstVakenTid = '1'">
            <p:observationstid>
              <xsl:value-of select="ns1:hypoglykemier/ns1:allvarligForekomstVakenTidAr"/>
            </p:observationstid>
          </xsl:if>
          <p:forekomst>
            <xsl:value-of select="ns1:hypoglykemier/ns1:harAllvarligForekomstVakenTid"/>
          </p:forekomst>
        </p:observation>

        <!-- Syn -->
        <xsl:if test="ns1:separatOgonLakarintygKommerSkickas = 'false' or ns1:separatOgonLakarintygKommerSkickas = '0'">
          <p:observation>
            <p:observations-id root="1.2.752.129.2.1.2.1" extension="{$observations-id1}"/>
            <p:observationskod code="OBS25" codeSystem="{$id_kv_observationer_intyg}"
                codeSystemName="kv_observationer_intyg"/>
            <p:forekomst>
              <xsl:value-of select="ns1:synfunktion/ns1:synfaltsprovningUtanAnmarkning"/>
            </p:forekomst>
          </p:observation>

          <xsl:call-template name="synfunktionObservation">
            <xsl:with-param name="ns-namespace" select="$ts-diabetes-ns"/>
            <xsl:with-param name="ns-prefix" select="$ts-diabetes-prefix"/>
          </xsl:call-template>

          <!-- Dubbelseende -->
          <xsl:call-template name="ogatsRorlighetObservation"/>

          <p2:observationAktivitetRelation>
            <p2:observationsid root="1.2.752.129.2.1.2.1" extension="{$observations-id1}"/>
            <p2:aktivitetsid root="1.2.752.129.2.1.2.1" extension="{$aktivitets-id1}"/>
          </p2:observationAktivitetRelation>

          <p2:observationAktivitetRelation>
            <p2:observationsid root="1.2.752.129.2.1.2.1" extension="{$observations-id2}"/>
            <p2:aktivitetsid root="1.2.752.129.2.1.2.1" extension="{$aktivitets-id2}"/>
          </p2:observationAktivitetRelation>
        </xsl:if>

        <xsl:for-each select="ns1:intygAvser/ns1:korkortstyp">
          <p2:intygAvser codeSystem="{$id_kv_intyget_avser}" codeSystemName="kv_intyget_avser" code="{$intygAvser/mapping[@key = current()]/@value}"/>
        </xsl:for-each>

        <p2:bilaga>
          <p2:bilagetyp code="BIL1" codeSystem="80595600-7477-4a6c-baeb-d2439e86b8bc" codeSystemName="kv_bilaga"/>
          <p:forekomst>
            <xsl:value-of select="ns1:separatOgonLakarintygKommerSkickas"/>
          </p:forekomst>
        </p2:bilaga>

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
  <xsl:template match="ns1:intygsTyp"/>

</xsl:stylesheet>
