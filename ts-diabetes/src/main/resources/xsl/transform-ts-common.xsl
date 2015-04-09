<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0"
    xmlns:ns1="urn:local:se:intygstjanster:services:1"
    xmlns:p="urn:riv:clinicalprocess:healthcond:certificate:1">

  <xsl:output method="xml" indent="yes"/>

  <xsl:variable name="intygAvser">
    <mapping key="C1" value="IAV1"/>
    <mapping key="C1E" value="IAV2"/>
    <mapping key="C" value="IAV3"/>
    <mapping key="CE" value="IAV4"/>
    <mapping key="D1" value="IAV5"/>
    <mapping key="D1E" value="IAV6"/>
    <mapping key="D" value="IAV7"/>
    <mapping key="DE" value="IAV8"/>
    <mapping key="TAXI" value="IAV9"/>
    <mapping key="ANNAT" value="IAV10"/>
    <mapping key="AM" value="IAV11"/>
    <mapping key="A1" value="IAV12"/>
    <mapping key="A2" value="IAV13"/>
    <mapping key="A" value="IAV14"/>
    <mapping key="B" value="IAV15"/>
    <mapping key="BE" value="IAV16"/>
    <mapping key="TRAKTOR" value="IAV17"/>
  </xsl:variable>

  <xsl:variable name="korkortsTyp">
    <mapping key="C1" value="VAR1"/>
    <mapping key="C1E" value="VAR2"/>
    <mapping key="C" value="VAR3"/>
    <mapping key="CE" value="VAR4"/>
    <mapping key="D1" value="VAR5"/>
    <mapping key="D1E" value="VAR6"/>
    <mapping key="D" value="VAR7"/>
    <mapping key="DE" value="VAR8"/>
    <mapping key="TAXI" value="VAR9"/>
    <mapping key="ANNAT" value="VAR10"/>
    <mapping key="AM" value="VAR12"/>
    <mapping key="A1" value="VAR13"/>
    <mapping key="A2" value="VAR14"/>
    <mapping key="A" value="VAR15"/>
    <mapping key="B" value="VAR16"/>
    <mapping key="BE" value="VAR17"/>
    <mapping key="TRAKTOR" value="VAR18"/>
  </xsl:variable>

  <xsl:variable name="synfaltsprovning-aktivitets-id">
    <xsl:value-of select="'1'"/>
  </xsl:variable>
  <xsl:variable name="synfaltsprovning-observations-id">
    <xsl:value-of select="'3'"/>
  </xsl:variable>

  <xsl:variable name="ogats-rorlighet-aktivitets-id">
    <xsl:value-of select="'2'"/>
  </xsl:variable>
  <xsl:variable name="ogats-rorlighet-observations-id">
    <xsl:value-of select="'4'"/>
  </xsl:variable>

  <xsl:template name="utlatandeHeader">
    <p:utlatande-id>
      <xsl:attribute name="root">1.2.752.129.2.1.2.1</xsl:attribute>
      <xsl:attribute name="extension" select="ns1:intygsId"/>
    </p:utlatande-id>
    <p:typAvUtlatande code="ts-bas" codeSystem="f6fb361a-e31d-48b8-8657-99b63912dd9b"
        codeSystemName="kv_utlåtandetyp_intyg" displayName="ts-bas"/>
    <xsl:if test="ns1:ovrigKommentar">
      <p:kommentar>
        <xsl:value-of select="ns1:ovrigKommentar"/>
      </p:kommentar>
    </xsl:if>
  </xsl:template>

  <xsl:template name="vardKontakt">
    <p:vardkontakt>
      <p:vardkontakttyp code="5880005" codeSystem="1.2.752.116.2.1.1.1" codeSystemName="SNOMED-CT"/>
      <p:idKontroll>
        <xsl:attribute name="code" select="ns1:identitetStyrkt/ns1:idkontroll"/>
        <xsl:attribute name="codeSystem">e7cc8f30-a353-4c42-b17a-a189b6876647</xsl:attribute>
        <xsl:attribute name="codeSystemName">kv_id_kontroll</xsl:attribute>
      </p:idKontroll>
    </p:vardkontakt>
  </xsl:template>

  <xsl:template name="ogatsRorlighetAktivitet">
    <p:aktivitet>
      <p:aktivitets-id root="1.2.752.129.2.1.2.1">
        <xsl:attribute name="extension" select="$ogats-rorlighet-aktivitets-id"/>
      </p:aktivitets-id>
      <p:aktivitetskod code="AKT18" codeSystem="8040b4d1-67dc-42e1-a938-de5374e9526a" codeSystemName="kv_aktiviteter_intyg"/>
    </p:aktivitet>
  </xsl:template>

  <xsl:template name="synfaltsObservation">
    <p:observation>
      <p:observations-id root="1.2.752.129.2.1.2.1">
        <xsl:attribute name="extension" select="$synfaltsprovning-observations-id"/>
      </p:observations-id>
      <p:observationskod code="H53.4" codeSystem="1.2.752.116.1.1.1.1.1" codeSystemName="ICD-10"/>
      <p:forekomst>
        <xsl:value-of select="ns1:synfunktion/ns1:harSynfaltsdefekt"/>
      </p:forekomst>
    </p:observation>
  </xsl:template>

  <xsl:template name="ogatsRorlighetObservation">
    <p:observation>
      <p:observations-id root="1.2.752.129.2.1.2.1">
        <xsl:attribute name="extension" select="$ogats-rorlighet-observations-id"/>
      </p:observations-id>
      <p:observationskod code="H53.2" codeSystem="1.2.752.116.1.1.1.1.1" codeSystemName="ICD-10"/>
      <p:forekomst>
        <xsl:value-of select="ns1:synfunktion/ns1:harDiplopi"/>
      </p:forekomst>
    </p:observation>
  </xsl:template>

</xsl:stylesheet>
