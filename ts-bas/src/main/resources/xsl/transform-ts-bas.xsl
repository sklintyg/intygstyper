<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0"
    xmlns:ns1="urn:local:se:intygstjanster:services:1"
    xmlns:ns2="urn:local:se:intygstjanster:services:RegisterTSBasResponder:1"
    xmlns:ns3="urn:local:se:intygstjanster:services:types:1"
    xmlns:p="urn:riv:clinicalprocess:healthcond:certificate:1"
    xmlns:p2="urn:riv:clinicalprocess:healthcond:certificate:ts-bas:1"
    xmlns:reg="urn:riv:clinicalprocess:healthcond:certificate:RegisterCertificateResponder:1">

  <xsl:output method="xml" indent="yes"/>
  <xsl:include href="xsl/transform-ts-common.xsl"/>

  <xsl:template match="ns1:basIntyg | ns2:intyg">
  <reg:RegisterCertificate>
    <reg:utlatande>

       <xsl:call-template name="utlatandeHeader"/>

      <xsl:apply-templates select="ns1:grundData"/>

      <xsl:call-template name="vardKontakt"/>

      <!-- Synfältsprövning (Donders konfrontationsmetod) -->
      <p:aktivitet>
        <p:aktivitets-id root="1.2.752.129.2.1.2.1">
          <xsl:attribute name="extension" select="$synfaltsprovning-aktivitets-id"/>
        </p:aktivitets-id>
        <p:aktivitetskod code="86944008" codeSystem="1.2.752.116.2.1.1.1" codeSystemName="SNOMED-CT"/>
        <p2:metod code="MET1" codeSystem="b0c078c6-512a-42a5-ab42-a3380f369ac3" codeSystemName="kv_metod"/>
      </p:aktivitet>

      <xsl:call-template name="ogatsRorlighetAktivitet"/>

      <!-- Undersökning med 8+ dioptrier -->
      <xsl:if test="ns1:synfunktion/ns1:harGlasStyrkaOver8Dioptrier = 'true' or ns1:synfunktion/ns1:harGlasStyrkaOver8Dioptrier = '1'">
        <p:aktivitet>
          <p:aktivitetskod code="AKT17" codeSystem="8040b4d1-67dc-42e1-a938-de5374e9526a" codeSystemName="kv_aktiviteter_intyg"/>
          <p:forekomst>true</p:forekomst>
        </p:aktivitet>
      </xsl:if>

      <!-- Vårdinsats för missbruk eller beroende av alkohol, narkotika eller läkemedel (11.2) -->
      <p:aktivitet>
        <p:aktivitetskod code="AKT15" codeSystem="8040b4d1-67dc-42e1-a938-de5374e9526a" codeSystemName="kv_aktiviteter_intyg"/>
        <p:forekomst>
          <xsl:value-of select="ns1:alkoholNarkotikaLakemedel/ns1:harVardinsats"/>
        </p:forekomst>
      </p:aktivitet>

      <!-- Provtagning avseende narkotika eller alkohol -->
      <xsl:if test="ns1:alkoholNarkotikaLakemedel/ns1:harVardinsatsProvtagningBehov">
        <p:aktivitet>
          <p:aktivitetskod code="AKT14" codeSystem="8040b4d1-67dc-42e1-a938-de5374e9526a" codeSystemName="kv_aktiviteter_intyg"/>
          <p:forekomst>
            <xsl:value-of select="ns1:alkoholNarkotikaLakemedel/ns1:harVardinsatsProvtagningBehov"/>
          </p:forekomst>
        </p:aktivitet>
      </xsl:if>

      <!-- Vård på sjukhus (16.1) -->
      <xsl:if test="ns1:sjukhusvard">
        <p:aktivitet>
          <p:aktivitetskod code="AKT19" codeSystem="8040b4d1-67dc-42e1-a938-de5374e9526a" codeSystemName="kv_aktiviteter_intyg"/>
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

      <xsl:if test="not(ns1:bedomning/ns1:kanInteTaStallning = 'true' or ns1:bedomning/ns1:kanInteTaStallning = '1')">
        <p:rekommendation>
          <p:rekommendationskod code="REK8" codeSystem="5a931b99-bda8-4f1e-8a6d-6f8a3f40a459" codeSystemName="kv_rekommendation_intyg"/>
          <xsl:for-each select="ns1:bedomning/ns1:korkortstyp">
            <p2:varde codeSystem="e889fa20-1dee-4f79-8b37-03853e75a9f8" codeSystemName="kv_värde">
              <xsl:attribute name="code" select="$korkortsTyp/mapping[@key = current()]/@value"/>
            </p2:varde>
          </xsl:for-each>
        </p:rekommendation>
      </xsl:if>

      <xsl:if test="ns1:bedomning/ns1:behovAvLakareSpecialistKompetens">
        <p:rekommendation>
          <p:rekommendationskod code="REK9" codeSystem="5a931b99-bda8-4f1e-8a6d-6f8a3f40a459" codeSystemName="kv_rekommendation_intyg"/>
          <p:beskrivning>
            <xsl:value-of select="ns1:bedomning/ns1:behovAvLakareSpecialistKompetens"/>
          </p:beskrivning>
        </p:rekommendation>
      </xsl:if>

      <xsl:call-template name="synfaltsObservation"/>

      <!-- Begränsning av seende vid nedsatt belysning -->
      <p:observation>
        <p:observationskod code="H53.6" codeSystem="1.2.752.116.1.1.1.1.1" codeSystemName="ICD-10"/>
        <p:forekomst>
          <xsl:value-of select="ns1:synfunktion/ns1:harNattblindhet"/>
        </p:forekomst>
      </p:observation>

      <!-- Progressiv ögonsjukdom -->
      <p:observation>
        <p:observationskod code="OBS1" codeSystem="335d4bed-7e1d-4f81-ae7d-b39b266ef1a3" codeSystemName="kv_observationer_intyg"/>
        <p:forekomst>
          <xsl:value-of select="ns1:synfunktion/ns1:harProgressivOgonsjukdom"/>
        </p:forekomst>
      </p:observation>

      <xsl:call-template name="ogatsRorlighetObservation" />

      <!-- Nystagmus -->
      <p:observation>
        <p:observationskod code="H55.9" codeSystem="1.2.752.116.1.1.1.1.1" codeSystemName="ICD-10"/>
        <p:forekomst>
          <xsl:value-of select="ns1:synfunktion/ns1:harNystagmus"/>
        </p:forekomst>
      </p:observation>

      <!-- Synfunktion utan korrektion (3.5.1 - 3.5.3) -->
      <p:observation>
        <p:observationskod code="420050001" codeSystem="1.2.752.116.2.1.1.1" codeSystemName="SNOMED-CT"/>
        <p:varde>
          <xsl:attribute name="value" select="ns1:synfunktion/ns1:synskarpaUtanKorrektion/ns1:hogerOga"/>
        </p:varde>
        <p2:lateralitet code="24028007" codeSystem="1.2.752.116.2.1.1.1" codeSystemName="SNOMED-CT"/>
      </p:observation>

      <p:observation>
        <p:observationskod code="420050001" codeSystem="1.2.752.116.2.1.1.1" codeSystemName="SNOMED-CT"/>
        <p:varde>
          <xsl:attribute name="value" select="ns1:synfunktion/ns1:synskarpaUtanKorrektion/ns1:vansterOga"/>
        </p:varde>
        <p2:lateralitet code="7771000" codeSystem="1.2.752.116.2.1.1.1" codeSystemName="SNOMED-CT"/>
      </p:observation>

      <p:observation>
        <p:observationskod code="420050001" codeSystem="1.2.752.116.2.1.1.1" codeSystemName="SNOMED-CT"/>
        <p:varde>
          <xsl:attribute name="value" select="ns1:synfunktion/ns1:synskarpaUtanKorrektion/ns1:binokulart"/>
        </p:varde>
        <p2:lateralitet code="51440002" codeSystem="1.2.752.116.2.1.1.1" codeSystemName="SNOMED-CT"/>
      </p:observation>

      <!-- Synskärpa med korrektion -->
      <xsl:if test="ns1:synfunktion/ns1:synskarpaMedKorrektion/ns1:hogerOga">
        <p:observation>
          <p:observationskod code="397535007" codeSystem="1.2.752.116.2.1.1.1" codeSystemName="SNOMED-CT"/>
          <p:varde>
            <xsl:attribute name="value" select="ns1:synfunktion/ns1:synskarpaMedKorrektion/ns1:hogerOga"/>
          </p:varde>
          <p2:lateralitet code="24028007" codeSystem="1.2.752.116.2.1.1.1" codeSystemName="SNOMED-CT"/>
        </p:observation>
      </xsl:if>

      <xsl:if test="ns1:synfunktion/ns1:synskarpaMedKorrektion/ns1:vansterOga">
        <p:observation>
          <p:observationskod code="397535007" codeSystem="1.2.752.116.2.1.1.1" codeSystemName="SNOMED-CT"/>
          <p:varde>
            <xsl:attribute name="value" select="ns1:synfunktion/ns1:synskarpaMedKorrektion/ns1:vansterOga"/>
          </p:varde>
          <p2:lateralitet code="7771000" codeSystem="1.2.752.116.2.1.1.1" codeSystemName="SNOMED-CT"/>
        </p:observation>
      </xsl:if>

      <xsl:if test="ns1:synfunktion/ns1:synskarpaMedKorrektion/ns1:binokulart">
        <p:observation>
          <p:observationskod code="397535007" codeSystem="1.2.752.116.2.1.1.1" codeSystemName="SNOMED-CT"/>
          <p:varde>
            <xsl:attribute name="value" select="ns1:synfunktion/ns1:synskarpaMedKorrektion/ns1:binokulart"/>
          </p:varde>
          <p2:lateralitet code="51440002" codeSystem="1.2.752.116.2.1.1.1" codeSystemName="SNOMED-CT"/>
        </p:observation>
      </xsl:if>

      <!-- Kontaktlinser -->
      <xsl:if test="ns1:synfunktion/ns1:synskarpaMedKorrektion/ns1:harKontaktlinsHogerOga">
        <p:observation>
          <p:observationskod code="285049007" codeSystem="1.2.752.116.2.1.1.1" codeSystemName="SNOMED-CT"/>
          <p:forekomst>
            <xsl:value-of select="ns1:synfunktion/ns1:synskarpaMedKorrektion/ns1:harKontaktlinsHogerOga"/>
          </p:forekomst>
          <p2:lateralitet code="24028007" codeSystem="1.2.752.116.2.1.1.1" codeSystemName="SNOMED-CT"/>
        </p:observation>
      </xsl:if>

      <xsl:if test="ns1:synfunktion/ns1:synskarpaMedKorrektion/ns1:harKontaktlinsVansterOga">
        <p:observation>
          <p:observationskod code="285049007" codeSystem="1.2.752.116.2.1.1.1" codeSystemName="SNOMED-CT"/>
          <p:forekomst>
            <xsl:value-of select="ns1:synfunktion/ns1:synskarpaMedKorrektion/ns1:harKontaktlinsVansterOga"/>
          </p:forekomst>
          <p2:lateralitet code="7771000" codeSystem="1.2.752.116.2.1.1.1" codeSystemName="SNOMED-CT"/>
        </p:observation>
      </xsl:if>

      <!-- Anfall av balansrubbningar eller yrsel -->
      <p:observation>
        <p:observationskod code="OBS2" codeSystem="335d4bed-7e1d-4f81-ae7d-b39b266ef1a3" codeSystemName="kv_observationer_intyg"/>
        <p:forekomst>
          <xsl:value-of select="ns1:horselBalanssinne/ns1:harBalansrubbningYrsel"/>
        </p:forekomst>
      </p:observation>

      <!-- Uppfatta samtal 4 meter -->
      <xsl:if test="ns1:horselBalanssinne/ns1:harSvartUppfattaSamtal4Meter">
        <p:observation>
          <p:observationskod code="OBS3" codeSystem="335d4bed-7e1d-4f81-ae7d-b39b266ef1a3" codeSystemName="kv_observationer_intyg"/>
          <p:forekomst>
            <xsl:value-of select="ns1:horselBalanssinne/ns1:harSvartUppfattaSamtal4Meter"/>
          </p:forekomst>
        </p:observation>
      </xsl:if>

      <!-- Rörelseorganens funktioner -->
      <p:observation>
        <p:observationskod code="OBS4" codeSystem="335d4bed-7e1d-4f81-ae7d-b39b266ef1a3" codeSystemName="kv_observationer_intyg"/>
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
          <p:observationskod code="OBS5" codeSystem="335d4bed-7e1d-4f81-ae7d-b39b266ef1a3" codeSystemName="kv_observationer_intyg"/>
          <p:forekomst>
            <xsl:value-of select="ns1:rorelseorganensFunktioner/ns1:harOtillrackligRorelseformagaPassagerare"/>
          </p:forekomst>
        </p:observation>
      </xsl:if>

      <!-- Hjärt och kärlsjukdomar (6.1 - 6.3) -->

      <!-- Hjärt- och kärlsjukdom som innebär en trafiksäkerhetsrisk -->
      <p:observation>
        <p:observationskod code="OBS6" codeSystem="335d4bed-7e1d-4f81-ae7d-b39b266ef1a3" codeSystemName="kv_observationer_intyg"/>
        <p:forekomst>
          <xsl:value-of select="ns1:hjartKarlSjukdomar/ns1:harRiskForsamradHjarnFunktion"/>
        </p:forekomst>
      </p:observation>

      <p:observation>
        <p:observationskod code="OBS8" codeSystem="335d4bed-7e1d-4f81-ae7d-b39b266ef1a3" codeSystemName="kv_observationer_intyg"/>
        <p:forekomst>
          <xsl:value-of select="ns1:hjartKarlSjukdomar/ns1:harHjarnskadaICNS"/>
        </p:forekomst>
      </p:observation>

      <p:observation>
        <p:observationskod code="OBS7" codeSystem="335d4bed-7e1d-4f81-ae7d-b39b266ef1a3" codeSystemName="kv_observationer_intyg"/>
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
        <p:observationskod code="73211009" codeSystem="1.2.752.116.2.1.1.1" codeSystemName="SNOMED-CT"/>
        <p:forekomst>
          <xsl:value-of select="ns1:diabetes/ns1:harDiabetes"/>
        </p:forekomst>
      </p:observation>

      <xsl:if test="not(ns1:diabetes/ns1:harDiabetes = 'true' or ns1:diabetes/ns1:harDiabetes = '1')">
        <xsl:choose>

          <xsl:when test="ns1:diabetes/ns1:diabetesTyp = 'TYP1'">
            <!-- typ1 -->
            <p:observation>
              <p:observationskod code="E10" codeSystem="1.2.752.116.1.1.1.1.1" codeSystemName="ICD-10"/>
              <p:forekomst>true</p:forekomst>
            </p:observation>
          </xsl:when>

          <xsl:otherwise>
            <!-- typ2 -->
            <p:observation>
              <p:observationskod code="E11" codeSystem="1.2.752.116.1.1.1.1.1" codeSystemName="ICD-10"/>
              <p:forekomst>true</p:forekomst>
            </p:observation>

            <!-- Kostbehandling -->
            <xsl:if test="not(ns1:diabetes/ns1:harBehandlingKost = 'true' or ns1:diabetes/ns1:harBehandlingKost = '1')">
              <p:observation>
                <p:observationskod code="OBS9" codeSystem="335d4bed-7e1d-4f81-ae7d-b39b266ef1a3" codeSystemName="kv_observationer_intyg"/>
                <p:forekomst>true</p:forekomst>
              </p:observation>
            </xsl:if>

            <!-- Tabettbehandling -->
            <xsl:if test="not(ns1:diabetes/ns1:harBehandlingTabletter = 'true' or ns1:diabetes/ns1:harBehandlingTabletter = '1')">
              <p:observation>
                <p:observationskod code="170746002" codeSystem="1.2.752.116.2.1.1.1" codeSystemName="SNOMED-CT"/>
                <p:forekomst>true</p:forekomst>
              </p:observation>
            </xsl:if>

            <!-- Insulinbehandling -->
            <xsl:if test="not(ns1:diabetes/ns1:harBehandlingInsulin = 'true' or ns1:diabetes/ns1:harBehandlingInsulin = '1')">
              <p:observation>
                <p:observationskod code="170747006" codeSystem="1.2.752.116.2.1.1.1" codeSystemName="SNOMED-CT"/>
                <p:forekomst>true</p:forekomst>
              </p:observation>
            </xsl:if>
          </xsl:otherwise>

        </xsl:choose>
      </xsl:if>

      <!-- Neurologiska sjukdomar (8) -->
      <p:observation>
        <p:observationskod code="407624006" codeSystem="1.2.752.116.2.1.1.1" codeSystemName="SNOMED-CT"/>
        <p:forekomst>
          <xsl:value-of select="ns1:neurologiskaSjukdomar"/>
        </p:forekomst>
      </p:observation>

      <!-- Epilepsi etc (9.1) -->
      <p:observation>
        <p:observationskod code="G40.9" codeSystem="1.2.752.116.1.1.1.1.1" codeSystemName="ICD-10"/>
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
        <p:observationskod code="OBS11" codeSystem="335d4bed-7e1d-4f81-ae7d-b39b266ef1a3" codeSystemName="kv_observationer_intyg"/>
        <p:forekomst>
          <xsl:value-of select="ns1:harNjurSjukdom"/>
        </p:forekomst>
      </p:observation>

      <!-- Demens och kognitiva störningar -->
      <p:observation>
        <p:observationskod code="OBS12" codeSystem="335d4bed-7e1d-4f81-ae7d-b39b266ef1a3" codeSystemName="kv_observationer_intyg"/>
        <p:forekomst>
          <xsl:value-of select="ns1:harKognitivStorning"/>
        </p:forekomst>
      </p:observation>

      <!-- Sömn- och vakenhetsstörningar -->
      <p:observation>
        <p:observationskod code="OBS13" codeSystem="335d4bed-7e1d-4f81-ae7d-b39b266ef1a3" codeSystemName="kv_observationer_intyg"/>
        <p:forekomst>
          <xsl:value-of select="ns1:harSomnVakenhetStorning"/>
        </p:forekomst>
      </p:observation>

      <!-- Alkohol, narkotika och läkemedel (13.1 - 13.2) -->
      <p:observation>
        <p:observationskod code="OBS14" codeSystem="335d4bed-7e1d-4f81-ae7d-b39b266ef1a3" codeSystemName="kv_observationer_intyg"/>
        <p:forekomst>
          <xsl:value-of select="ns1:alkoholNarkotikaLakemedel/ns1:harTeckenMissbruk"/>
        </p:forekomst>
      </p:observation>

      <!-- Regelbundet läkarordinerat bruk av läkemedel som kan innebära en trafiksäkerhetsrisk -->
      <p:observation>
        <p:observationskod code="OBS15" codeSystem="335d4bed-7e1d-4f81-ae7d-b39b266ef1a3" codeSystemName="kv_observationer_intyg"/>
        <xsl:if test="ns1:alkoholNarkotikaLakemedel/ns1:lakarordineratLakemedelOchDos">
          <p:beskrivning>
            <xsl:value-of select="ns1:alkoholNarkotikaLakemedel/ns1:lakarordineratLakemedelOchDos"/>
          </p:beskrivning>
        </xsl:if>
        <p:forekomst>
          <xsl:value-of select="ns1:alkoholNarkotikaLakemedel/ns1:harVardinsats"/>
        </p:forekomst>
      </p:observation>

      <!-- Psykisk sjukdom eller störning (14) -->
      <p:observation>
        <p:observationskod code="OBS16" codeSystem="335d4bed-7e1d-4f81-ae7d-b39b266ef1a3" codeSystemName="kv_observationer_intyg"/>
        <p:forekomst>
          <xsl:value-of select="ns1:harPsykiskStorning"/>
        </p:forekomst>
      </p:observation>

      <!-- Psykisk utvecklingsstörning (15.1) -->
      <p:observation>
        <p:observationskod code="129104009" codeSystem="1.2.752.116.2.1.1.1" codeSystemName="SNOMED-CT"/>
        <p:forekomst>
          <xsl:value-of select="ns1:utvecklingsstorning/ns1:harPsykiskUtvecklingsstorning"/>
        </p:forekomst>
      </p:observation>

      <!-- ADHD DAMP (15.2) -->
      <p:observation>
        <p:observationskod code="OBS17" codeSystem="335d4bed-7e1d-4f81-ae7d-b39b266ef1a3" codeSystemName="kv_observationer_intyg"/>
        <p:forekomst>
          <xsl:value-of select="ns1:utvecklingsstorning/ns1:harAndrayndrom"/>
        </p:forekomst>
      </p:observation>

      <!-- Stadigvarande medicinering (17.1) -->
      <p:observation>
        <p:observationskod code="OBS18" codeSystem="335d4bed-7e1d-4f81-ae7d-b39b266ef1a3" codeSystemName="kv_observationer_intyg"/>
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

      <xsl:for-each select="ns1:intygAvser/ns1:korkortstyp">
        <p2:intygAvser codeSystem="24c41b8d-258a-46bf-a08a-b90738b28770" codeSystemName="kv_intyget_avser">
          <xsl:attribute name="code" select="$intygAvser/mapping[@key = current()]/@value"/>
        </p2:intygAvser>
      </xsl:for-each>

        <p2:utgava>
          <xsl:value-of select="//ns1:utgava"/>
        </p2:utgava>
        <p2:version>
          <xsl:value-of select="//ns1:version"/>
        </p2:version>

    </reg:utlatande>
  </reg:RegisterCertificate>
  </xsl:template>

  <xsl:template match="ns1:grundData">
    <p:signeringsdatum>
      <xsl:value-of select="ns1:signeringsTidstampel"/>
    </p:signeringsdatum>
    <p:patient>
      <p:person-id>
        <xsl:attribute name="root">1.2.752.129.2.1.3.1</xsl:attribute>
        <xsl:attribute name="extension">
          <xsl:value-of select="ns1:patient/ns1:personId/ns3:extension"/>
        </xsl:attribute>
      </p:person-id>
      <p:fornamn>
        <xsl:value-of select="ns1:patient/ns1:fornamn"/>
      </p:fornamn>
      <p:efternamn>
        <xsl:value-of select="ns1:patient/ns1:efternamn"/>
      </p:efternamn>
      <p:postadress>
        <xsl:value-of select="ns1:patient/ns1:postadress"/>
      </p:postadress>
      <p:postnummer>
        <xsl:value-of select="ns1:patient/ns1:postnummer"/>
      </p:postnummer>
      <p:postort>
        <xsl:value-of select="ns1:patient/ns1:postort"/>
      </p:postort>
    </p:patient>
    <p:skapadAv>
      <p:personal-id>
        <xsl:attribute name="root">1.2.752.129.2.1.4.1</xsl:attribute>
        <xsl:attribute name="extension">
          <xsl:value-of select="ns1:skapadAv/ns1:personId/ns3:extension"/>
        </xsl:attribute>
      </p:personal-id>
      <p:fullstandigtNamn>
        <xsl:value-of select="ns1:skapadAv/ns1:fullstandigtNamn"/>
      </p:fullstandigtNamn>
      <xsl:for-each select="ns1:skapadAv/ns1:befattningar">
        <p:befattning>
          <xsl:value-of select="."/>
        </p:befattning>
      </xsl:for-each>
      <p:enhet>
        <p:enhets-id>
          <xsl:attribute name="root">1.2.752.129.2.1.4.1</xsl:attribute>
          <xsl:attribute name="extension">
            <xsl:value-of select="ns1:skapadAv/ns1:vardenhet/ns1:enhetsId/ns3:extension"/>
          </xsl:attribute>
        </p:enhets-id>
        <p:enhetsnamn>
          <xsl:value-of select="ns1:skapadAv/ns1:vardenhet/ns1:enhetsnamn"/>
        </p:enhetsnamn>
        <p:postadress>
          <xsl:value-of select="ns1:skapadAv/ns1:vardenhet/ns1:postadress"/>
        </p:postadress>
        <p:postnummer>
          <xsl:value-of select="ns1:skapadAv/ns1:vardenhet/ns1:postnummer"/>
        </p:postnummer>
        <p:postort>
          <xsl:value-of select="ns1:skapadAv/ns1:vardenhet/ns1:postort"/>
        </p:postort>
        <p:telefonnummer>
          <xsl:value-of select="ns1:skapadAv/ns1:vardenhet/ns1:telefonnummer"/>
        </p:telefonnummer>
        <p:vardgivare>
          <p:vardgivare-id>
            <xsl:attribute name="root">1.2.752.129.2.1.4.1</xsl:attribute>
            <xsl:attribute name="extension">
              <xsl:value-of select="ns1:skapadAv/ns1:vardenhet/ns1:vardgivare/ns1:vardgivarid/ns3:extension"/>
            </xsl:attribute>
          </p:vardgivare-id>
          <p:vardgivarnamn>
            <xsl:value-of select="ns1:skapadAv/ns1:vardenhet/ns1:vardgivare/ns1:vardgivarnamn"/>
          </p:vardgivarnamn>
        </p:vardgivare>
      </p:enhet>
      <xsl:for-each select="ns1:skapadAv/ns1:specialiteter">
        <p2:specialitet code="SPEC" codeSystem="coming_soon" codeSystemName="kv_intyg_specialitet"/>
      </xsl:for-each>
    </p:skapadAv>
  </xsl:template>

  <!-- Dont output text nodes we dont transform  -->
  <xsl:template match="ns1:intygsTyp"/>

</xsl:stylesheet>
