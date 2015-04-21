<?xml version="1.0" encoding="UTF-8"?>
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

  <xsl:template match="ns1:diabetesIntyg | ns2:intyg">
    <reg:RegisterCertificate>
      <reg:utlatande>

        <xsl:call-template name="utlatandeHeader">
          <xsl:with-param name="displayName" select="'ts-diabetes'"/>
        </xsl:call-template>

        <xsl:call-template name="grundData">
          <xsl:with-param name="ns-namespace" select="'urn:riv:clinicalprocess:healthcond:certificate:ts-diabetes:1'"/>
          <xsl:with-param name="ns-prefix" select="'p2'"/>
        </xsl:call-template>

        <xsl:call-template name="vardKontakt"/>

        <!-- AKTIVITETER begin --><!-- Egenkontroll av blodsocker -->
        <xsl:if test="ns1:hypoglykemier/ns1:genomforEgenkontrollBlodsocker = 'true' or ns1:hypoglykemier/ns1:genomforEgenkontrollBlodsocker = '1'">
          <p:aktivitet>
            <p:aktivitetskod code="308113006" codeSystem="1.2.752.116.2.1.1.1" codeSystemName="SNOMED-CT"/>
            <p:forekomst>true</p:forekomst>
          </p:aktivitet>
        </xsl:if>

        <xsl:if test="ns1:synfunktion/ns1:finnsSeparatOgonlakarintyg = 'false'">
          <!-- Synfältsprövning (Donders konfrontationsmetod) -->
          <p:aktivitet>
            <p:aktivitets-id root="1.2.752.129.2.1.2.1">
              <xsl:attribute name="extension" select="$synfaltsprovning-aktivitets-id"/>
            </p:aktivitets-id>
            <p:aktivitetskod code="86944008" codeSystem="1.2.752.116.2.1.1.1" codeSystemName="SNOMED-CT"/>
            <p2:metod code="MET1" codeSystem="b0c078c6-512a-42a5-ab42-a3380f369ac3" codeSystemName="kv_metod"/>
          </p:aktivitet>

          <!-- Prövning av ögats rörlighet -->
          <xsl:call-template name="ogatsRorlighetAktivitet"/>
        </xsl:if>

        <!-- AKTIVITETER end -->

        <!-- REKOMMENDATIONER begin --><!-- Patienten uppfyller kraven.. -->
        <p:rekommendation>
          <p:rekommendationskod code="REK8"
              codeSystem="5a931b99-bda8-4f1e-8a6d-6f8a3f40a459" codeSystemName="kv_rekommendation_intyg"/>
          <xsl:for-each select="ns1:bedomning/ns1:korkortstyp">
            <p2:varde codeSystem="5a931b99-bda8-4f1e-8a6d-6f8a3f40a459" codeSystemName="kv_rekommendation_intyg">
              <xsl:attribute name="code" select="$korkortsTyp/mapping[@key = current()]/@value"/>
            </p2:varde>
          </xsl:for-each>
        </p:rekommendation>

        <!-- Specialistkompetens inom.. -->
        <xsl:if test="ns1:bedomning/ns1:behovAvLakareSpecialistKompetens">
          <p:rekommendation>
            <p:rekommendationskod code="REK9"
                codeSystem="5a931b99-bda8-4f1e-8a6d-6f8a3f40a459" codeSystemName="kv_rekommendation_intyg"/>
            <p:beskrivning>
              <xsl:value-of select="ns1:bedomning/ns1:behovAvLakareSpecialistKompetens"/>
            </p:beskrivning>
          </p:rekommendation>
        </xsl:if>

        <!-- Lämplighet att inneha... -->
        <xsl:if test="ns1:bedomning/ns1:lamplighetInnehaBehorighetSpecial">
          <p:rekommendation>
            <p:rekommendationskod code="REK10"
                codeSystem="5a931b99-bda8-4f1e-8a6d-6f8a3f40a459" codeSystemName="kv_rekommendation_intyg"/>
            <p2:varde>
              <xsl:value-of select="ns1:bedomning/ns1:lamplighetInnehaBehorighetSpecial"/>
            </p2:varde>
          </p:rekommendation>
        </xsl:if>
        <!-- REKOMMENDATIONER end -->

        <!-- OBSERVATIONER begin -->

        <!-- Diabetes typ1 or typ2 -->
        <p:observation>
          <p:observationskod codeSystem="1.2.752.116.1.1.1.1.3" codeSystemName="ICD-10">
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
        <xsl:if test="ns1:diabetes/ns1:harBehandlingTabletter = 'true'">
          <p:observation>
            <p:observationskod code="170746002" codeSystem="1.2.752.116.2.1.1.1"
                codeSystemName="SNOMED-CT"/>
            <p:forekomst>true</p:forekomst>
          </p:observation>
        </xsl:if>

        <xsl:if test="ns1:diabetes/ns1:harBehandlingKost = 'true'">
          <p:observation>
            <p:observationskod code="170745003" codeSystem="1.2.752.116.2.1.1.1"
                codeSystemName="SNOMED-CT"/>
            <p:forekomst>true</p:forekomst>
          </p:observation>
        </xsl:if>

        <xsl:if test="ns1:diabetes/ns1:harBehandlingInsulin = 'true'">
          <p:observation>
            <p:observationskod code="170747006" codeSystem="1.2.752.116.2.1.1.1"
                codeSystemName="SNOMED-CT"/>
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
            <p:observationskod code="OBS10" codeSystem="335d4bed-7e1d-4f81-ae7d-b39b266ef1a3"
                codeSystemName="kv_observationer_intyg"/>
            <p:beskrivning>
              <xsl:value-of select="ns1:diabetes/ns1:annanBehandlingBeskrivning"/>
            </p:beskrivning>
            <p:forekomst>true</p:forekomst>
          </p:observation>
        </xsl:if>

        <!-- Hypoglykemi --><!-- Har kunskap om åtgärder -->
        <p:observation>
          <p:observationskod code="OBS19" codeSystem="335d4bed-7e1d-4f81-ae7d-b39b266ef1a3"
              codeSystemName="kv_observationer_intyg"/>
          <p:forekomst>
            <xsl:value-of select="ns1:hypoglykemier/ns1:harKunskapOmAtgarder"/>
          </p:forekomst>
        </p:observation>

        <!-- Tecken på nedsatt hjärnfunktion -->
        <p:observation>
          <p:observationskod code="OBS20" codeSystem="335d4bed-7e1d-4f81-ae7d-b39b266ef1a3"
              codeSystemName="kv_observationer_intyg"/>
          <p:forekomst>
            <xsl:value-of select="ns1:hypoglykemier/ns1:harTeckenNedsattHjarnfunktion"/>
          </p:forekomst>
        </p:observation>

        <!-- if  harTeckenNedsattHjarnfunktion = 'true' -->
        <xsl:if test="ns1:hypoglykemier/ns1:harTeckenNedsattHjarnfunktion = 'true'">
          <p:observation>
            <p:observationskod code="OBS21" codeSystem="335d4bed-7e1d-4f81-ae7d-b39b266ef1a3"
                codeSystemName="kv_observationer_intyg"/>
            <p:forekomst>
              <xsl:value-of select="ns1:hypoglykemier/ns1:saknarFormagaKannaVarningstecken"/>
            </p:forekomst>
          </p:observation>

          <p:observation>
            <p:observationskod code="OBS22" codeSystem="335d4bed-7e1d-4f81-ae7d-b39b266ef1a3"
                codeSystemName="kv_observationer_intyg"/>
            <p:beskrivning>
              <xsl:value-of select="ns1:hypoglykemier/ns1:allvarligForekomstBeskrivning"/>
            </p:beskrivning>
            <p:forekomst>
              <xsl:value-of select="ns1:hypoglykemier/ns1:harAllvarligForekomst"/>
            </p:forekomst>
          </p:observation>

          <p:observation>
            <p:observationskod code="OBS23" codeSystem="335d4bed-7e1d-4f81-ae7d-b39b266ef1a3"
                codeSystemName="kv_observationer_intyg"/>
            <p:beskrivning>
              <xsl:value-of select="ns1:hypoglykemier/ns1:allvarligForekomstTrafikBeskrivning"/>
            </p:beskrivning>
            <p:forekomst>
              <xsl:value-of select="ns1:hypoglykemier/ns1:harAllvarligForekomstTrafiken"/>
            </p:forekomst>
          </p:observation>
        </xsl:if>
        <!-- end if -->

        <xsl:if test="ns1:hypoglykemier/ns1:harAllvarligForekomstVakenTid = 'true'">
          <p:observation>
            <p:observationskod code="OBS24" codeSystem="335d4bed-7e1d-4f81-ae7d-b39b266ef1a3"
                codeSystemName="kv_observationer_intyg"/>
            <p:observationstid>
              <xsl:value-of select="ns1:hypoglykemier/ns1:allvarligForekomstVakenTidAr"/>
            </p:observationstid>
            <p:forekomst>true</p:forekomst>
          </p:observation>
        </xsl:if>

        <!-- Syn -->

        <xsl:call-template name="synfaltsObservation"/>

        <xsl:call-template name="ogatsRorlighetObservation"/>

        <xsl:if test="ns1:synfunktion/ns1:finnsSeparatOgonlakarintyg = 'false'">
          <p:observation>
            <p:observations-id root="1.2.752.129.2.1.2.1"
                extension="3"/>
            <p:observationskod code="OBS25" codeSystem="335d4bed-7e1d-4f81-ae7d-b39b266ef1a3"
                codeSystemName="kv_observationer_intyg"/>
            <p:forekomst>
              <xsl:value-of select="ns1:synfunktion/ns1:synfaltsprovningUtanAnmarkning"/>
            </p:forekomst>
          </p:observation>

          <!-- Utan korrektion --><!-- Höger öga -->
          <p:observation>
            <p:observationskod code="420050001" codeSystem="1.2.752.116.2.1.1.1"
                codeSystemName="SNOMED-CT"/>
            <p:varde>
              <xsl:attribute name="value" select="ns1:synfunktion/ns1:synskarpaUtanKorrektion/ns1:hogerOga"/>
            </p:varde>
            <p2:lateralitet code="24028007" codeSystem="1.2.752.116.2.1.1.1"
                codeSystemName="SNOMED-CT"/>
          </p:observation>

          <!-- Vänster öga -->
          <p:observation>
            <p:observationskod code="420050001" codeSystem="1.2.752.116.2.1.1.1"
                codeSystemName="SNOMED-CT"/>
            <p:varde>
              <xsl:attribute name="value" select="ns1:synfunktion/ns1:synskarpaUtanKorrektion/ns1:vansterOga"/>
            </p:varde>
            <p2:lateralitet code="7771000" codeSystem="1.2.752.116.2.1.1.1"
                codeSystemName="SNOMED-CT"/>
          </p:observation>

          <!-- Binokulärt -->
          <p:observation>
            <p:observationskod code="420050001" codeSystem="1.2.752.116.2.1.1.1"
                codeSystemName="SNOMED-CT"/>
            <p:varde>
              <xsl:attribute name="value" select="ns1:synfunktion/ns1:synskarpaUtanKorrektion/ns1:binokulart"/>
            </p:varde>
            <p2:lateralitet code="51440002" codeSystem="1.2.752.116.2.1.1.1"
                codeSystemName="SNOMED-CT"/>
          </p:observation>

          <!-- Med korrektion --><!-- Höger öga -->
          <p:observation>
            <p:observationskod code="397535007" codeSystem="1.2.752.116.2.1.1.1"
                codeSystemName="SNOMED-CT"/>
            <p:varde>
              <xsl:attribute name="value" select="ns1:synfunktion/ns1:synskarpaMedKorrektion/ns1:hogerOga"/>
            </p:varde>
            <p2:lateralitet code="24028007" codeSystem="1.2.752.116.2.1.1.1"
                codeSystemName="SNOMED-CT"/>
          </p:observation>

          <!-- Vänster öga -->
          <p:observation>
            <p:observationskod code="397535007" codeSystem="1.2.752.116.2.1.1.1"
                codeSystemName="SNOMED-CT"/>
            <p:varde>
              <xsl:attribute name="value" select="ns1:synfunktion/ns1:synskarpaMedKorrektion/ns1:vansterOga"/>
            </p:varde>
            <p2:lateralitet code="7771000" codeSystem="1.2.752.116.2.1.1.1"
                codeSystemName="SNOMED-CT"/>
          </p:observation>

          <!-- Binokulärt -->
          <p:observation>
            <p:observationskod code="397535007" codeSystem="1.2.752.116.2.1.1.1"
                codeSystemName="SNOMED-CT"/>
            <p:varde>
              <xsl:attribute name="value" select="ns1:synfunktion/ns1:synskarpaMedKorrektion/ns1:binokulart"/>
            </p:varde>
            <p2:lateralitet code="51440002" codeSystem="1.2.752.116.2.1.1.1"
                codeSystemName="SNOMED-CT"/>
          </p:observation>

          <!-- Dubbelseende -->
          <p:observation>
            <p:observations-id root="1.2.752.129.2.1.2.1"
                extension="4"/>
            <p:observationskod code="H53.2" codeSystem="1.2.752.116.1.1.1.1.3"
                codeSystemName="ICD-10"/>
            <p:forekomst>
              <xsl:value-of select="ns1:synfunktion/ns1:harDiplopi"/>
            </p:forekomst>
          </p:observation>
        </xsl:if>
        <!-- OBSERVATIONER end -->

        <!-- OBSERVATIONAKTIVITETRELATION begin-->
        <p2:observationAktivitetRelation>
          <p2:observationsid root="1.2.752.129.2.1.2.1">
            <xsl:attribute name="extension" select="$synfaltsprovning-observations-id"/>
          </p2:observationsid>
          <p2:aktivitetsid root="1.2.752.129.2.1.2.1">
            <xsl:attribute name="extension" select="$synfaltsprovning-aktivitets-id"/>
          </p2:aktivitetsid>
        </p2:observationAktivitetRelation>

        <p2:observationAktivitetRelation>
          <p2:observationsid root="1.2.752.129.2.1.2.1">
            <xsl:attribute name="extension" select="$ogats-rorlighet-observations-id"/>
          </p2:observationsid>
          <p2:aktivitetsid root="1.2.752.129.2.1.2.1">
            <xsl:attribute name="extension" select="$ogats-rorlighet-aktivitets-id"/>
          </p2:aktivitetsid>
        </p2:observationAktivitetRelation>
        <!-- OBSERVATIONAKTIVITETRELATION end -->

        <xsl:for-each select="ns1:intygAvser/ns1:korkortstyp">
          <p2:intygAvser codeSystem="24c41b8d-258a-46bf-a08a-b90738b28770" codeSystemName="kv_intyget_avser">
            <xsl:attribute name="code" select="$intygAvser/mapping[@key = current()]/@value"/>
          </p2:intygAvser>
        </xsl:for-each>

        <p2:bilaga>
          <p2:bilagetyp code="BIL1" codeSystem="80595600-7477-4a6c-baeb-d2439e86b8bc" codeSystemName="kv_bilaga"/>
          <p2:forekomst>
            <xsl:value-of select="ns1:synfunktion/ns1:finnsSeparatOgonlakarintyg"/>
          </p2:forekomst>
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
