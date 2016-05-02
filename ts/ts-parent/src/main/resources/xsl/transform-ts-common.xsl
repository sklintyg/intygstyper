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
    xmlns:ns3="urn:local:se:intygstjanster:services:types:1"
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

  <xsl:variable name="aktivitets-id1" select="'1'"/>
  <xsl:variable name="observations-id1" select="'3'"/>

  <xsl:variable name="aktivitets-id2" select="'2'"/>
  <xsl:variable name="observations-id2" select="'4'"/>

  <xsl:variable name="id_kv_metod" select="'b0c078c6-512a-42a5-ab42-a3380f369ac3'"/>
  <xsl:variable name="id_kv_aktiviteter_intyg" select="'8040b4d1-67dc-42e1-a938-de5374e9526a'"/>
  <xsl:variable name="id_kv_observationer_intyg" select="'335d4bed-7e1d-4f81-ae7d-b39b266ef1a3'"/>
  <xsl:variable name="id_kv_rekommendation_intyg" select="'5a931b99-bda8-4f1e-8a6d-6f8a3f40a459'"/>
  <xsl:variable name="id_kv_intyget_avser" select="'24c41b8d-258a-46bf-a08a-b90738b28770'"/>
  <xsl:variable name="id_kv_korkortsbehorighet" select="'e889fa20-1dee-4f79-8b37-03853e75a9f8'"/>
  <xsl:variable name="id_snomed-ct" select="'1.2.752.116.2.1.1.1'"/>
  <xsl:variable name="id_icd-10" select="'1.2.752.116.1.1.1.1.1'"/>

  <xsl:template name="utlatandeHeader">
    <xsl:param name="displayName"/>
    <p:utlatande-id root="1.2.752.129.2.1.2.1">
      <xsl:attribute name="extension" select="ns1:intygsId"/>
    </p:utlatande-id>
    <p:typAvUtlatande codeSystem="f6fb361a-e31d-48b8-8657-99b63912dd9b"
        codeSystemName="kv_utlåtandetyp_intyg" code="{$displayName}" displayName="{$displayName}"/>
    <xsl:if test="ns1:ovrigKommentar">
      <p:kommentar>
        <xsl:value-of select="ns1:ovrigKommentar"/>
      </p:kommentar>
    </xsl:if>
  </xsl:template>

  <xsl:template name="grundData">
    <xsl:param name="ns-namespace"/>
    <xsl:param name="ns-prefix"/>
    <p:signeringsdatum>
      <xsl:value-of select="ns1:grundData/ns1:signeringsTidstampel"/>
    </p:signeringsdatum>
    <p:patient>
      <p:person-id root="1.2.752.129.2.1.3.1">
        <xsl:attribute name="extension">
          <xsl:value-of select="ns1:grundData/ns1:patient/ns1:personId/ns3:extension"/>
        </xsl:attribute>
      </p:person-id>
      <p:fornamn>
        <xsl:value-of select="ns1:grundData/ns1:patient/ns1:fornamn"/>
      </p:fornamn>
      <p:efternamn>
        <xsl:value-of select="ns1:grundData/ns1:patient/ns1:efternamn"/>
      </p:efternamn>
      <p:postadress>
        <xsl:value-of select="ns1:grundData/ns1:patient/ns1:postadress"/>
      </p:postadress>
      <p:postnummer>
        <xsl:value-of select="ns1:grundData/ns1:patient/ns1:postnummer"/>
      </p:postnummer>
      <p:postort>
        <xsl:value-of select="ns1:grundData/ns1:patient/ns1:postort"/>
      </p:postort>
    </p:patient>
    <p:skapadAv>
      <p:personal-id root="1.2.752.129.2.1.4.1">
        <xsl:attribute name="extension">
          <xsl:value-of select="ns1:grundData/ns1:skapadAv/ns1:personId/ns3:extension"/>
        </xsl:attribute>
      </p:personal-id>
      <p:fullstandigtNamn>
        <xsl:value-of select="ns1:grundData/ns1:skapadAv/ns1:fullstandigtNamn"/>
      </p:fullstandigtNamn>
      <xsl:for-each select="ns1:grundData/ns1:skapadAv/ns1:befattningar">
        <p:befattning>
          <xsl:value-of select="."/>
        </p:befattning>
      </xsl:for-each>
      <p:enhet>
        <p:enhets-id root="1.2.752.129.2.1.4.1">
          <xsl:attribute name="extension">
            <xsl:value-of select="ns1:grundData/ns1:skapadAv/ns1:vardenhet/ns1:enhetsId/ns3:extension"/>
          </xsl:attribute>
        </p:enhets-id>
        <p:enhetsnamn>
          <xsl:value-of select="ns1:grundData/ns1:skapadAv/ns1:vardenhet/ns1:enhetsnamn"/>
        </p:enhetsnamn>
        <p:postadress>
          <xsl:value-of select="ns1:grundData/ns1:skapadAv/ns1:vardenhet/ns1:postadress"/>
        </p:postadress>
        <p:postnummer>
          <xsl:value-of select="ns1:grundData/ns1:skapadAv/ns1:vardenhet/ns1:postnummer"/>
        </p:postnummer>
        <p:postort>
          <xsl:value-of select="ns1:grundData/ns1:skapadAv/ns1:vardenhet/ns1:postort"/>
        </p:postort>
        <p:telefonnummer>
          <xsl:value-of select="ns1:grundData/ns1:skapadAv/ns1:vardenhet/ns1:telefonnummer"/>
        </p:telefonnummer>
        <p:vardgivare>
          <p:vardgivare-id root="1.2.752.129.2.1.4.1">
            <xsl:attribute name="extension">
              <xsl:value-of select="ns1:grundData/ns1:skapadAv/ns1:vardenhet/ns1:vardgivare/ns1:vardgivarid/ns3:extension"/>
            </xsl:attribute>
          </p:vardgivare-id>
          <p:vardgivarnamn>
            <xsl:value-of select="ns1:grundData/ns1:skapadAv/ns1:vardenhet/ns1:vardgivare/ns1:vardgivarnamn"/>
          </p:vardgivarnamn>
        </p:vardgivare>
      </p:enhet>
      <xsl:for-each select="ns1:grundData/ns1:skapadAv/ns1:specialiteter">
        <xsl:element name="{$ns-prefix}:specialitet" namespace="{$ns-namespace}">
          <xsl:attribute name="code" select="." />
          <xsl:attribute name="codeSystem" select="'coming_soon'" />
          <xsl:attribute name="codeSystemName" select="'kv_intyg_specialitet'" />
        </xsl:element>
      </xsl:for-each>
    </p:skapadAv>
  </xsl:template>

  <xsl:template name="vardKontakt">
    <p:vardkontakt>
      <p:vardkontakttyp code="5880005" codeSystem="1.2.752.116.2.1.1.1" codeSystemName="SNOMED-CT"/>
      <p:idKontroll codeSystem="e7cc8f30-a353-4c42-b17a-a189b6876647" codeSystemName="kv_id_kontroll">
        <xsl:attribute name="code" select="ns1:identitetStyrkt/ns1:idkontroll"/>
      </p:idKontroll>
    </p:vardkontakt>
  </xsl:template>

  <xsl:template name="ogatsSynfaltAktivitet">
    <xsl:param name="ns-namespace"/>
    <xsl:param name="ns-prefix"/>
    <p:aktivitet>
      <p:aktivitets-id root="1.2.752.129.2.1.2.1" extension="{$aktivitets-id1}"/>
      <p:aktivitetskod code="86944008" codeSystem="{$id_snomed-ct}" codeSystemName="SNOMED-CT"/>
      <xsl:element name="{$ns-prefix}:metod" namespace="{$ns-namespace}">
        <xsl:attribute name="code" select="'MET1'"/>
        <xsl:attribute name="codeSystem" select="$id_kv_metod"/>
        <xsl:attribute name="codeSystemName" select="'kv_metod'"/>
      </xsl:element>
    </p:aktivitet>
  </xsl:template>

  <xsl:template name="ogatsRorlighetAktivitet">
    <p:aktivitet>
      <p:aktivitets-id root="1.2.752.129.2.1.2.1" extension="{$aktivitets-id2}"/>
      <p:aktivitetskod code="AKT18" codeSystem="8040b4d1-67dc-42e1-a938-de5374e9526a" codeSystemName="kv_aktiviteter_intyg"/>
    </p:aktivitet>
  </xsl:template>

  <xsl:template name="ogatsRorlighetObservation">
    <p:observation>
      <p:observations-id root="1.2.752.129.2.1.2.1" extension="{$observations-id2}"/>
      <p:observationskod code="H53.2" codeSystem="1.2.752.116.1.1.1.1.1" codeSystemName="ICD-10"/>
      <p:forekomst>
        <xsl:value-of select="ns1:synfunktion/ns1:harDiplopi"/>
      </p:forekomst>
    </p:observation>
  </xsl:template>

  <xsl:template name="synfunktionObservation">
    <xsl:param name="ns-namespace"/>
    <xsl:param name="ns-prefix"/>

    <!-- Utan korrektion -->
    <!-- Höger öga -->
    <p:observation>
      <p:observationskod code="420050001" codeSystem="{$id_snomed-ct}" codeSystemName="SNOMED-CT"/>
      <p:varde>
        <xsl:attribute name="value" select="ns1:synfunktion/ns1:synskarpaUtanKorrektion/ns1:hogerOga"/>
      </p:varde>
      <xsl:element name="{$ns-prefix}:lateralitet" namespace="{$ns-namespace}">
        <xsl:attribute name="code" select="'24028007'"/>
        <xsl:attribute name="codeSystem" select="$id_snomed-ct"/>
        <xsl:attribute name="codeSystemName" select="'SNOMED-CT'"/>
      </xsl:element>
    </p:observation>

    <!-- Vänster öga -->
    <p:observation>
      <p:observationskod code="420050001" codeSystem="{$id_snomed-ct}" codeSystemName="SNOMED-CT"/>
      <p:varde>
        <xsl:attribute name="value" select="ns1:synfunktion/ns1:synskarpaUtanKorrektion/ns1:vansterOga"/>
      </p:varde>
      <xsl:element name="{$ns-prefix}:lateralitet" namespace="{$ns-namespace}">
        <xsl:attribute name="code" select="'7771000'"/>
        <xsl:attribute name="codeSystem" select="$id_snomed-ct"/>
        <xsl:attribute name="codeSystemName" select="'SNOMED-CT'"/>
      </xsl:element>
    </p:observation>

    <!-- Binokulärt -->
    <p:observation>
      <p:observationskod code="420050001" codeSystem="{$id_snomed-ct}" codeSystemName="SNOMED-CT"/>
      <p:varde>
        <xsl:attribute name="value" select="ns1:synfunktion/ns1:synskarpaUtanKorrektion/ns1:binokulart"/>
      </p:varde>
      <xsl:element name="{$ns-prefix}:lateralitet" namespace="{$ns-namespace}">
        <xsl:attribute name="code" select="'51440002'"/>
        <xsl:attribute name="codeSystem" select="$id_snomed-ct"/>
        <xsl:attribute name="codeSystemName" select="'SNOMED-CT'"/>
      </xsl:element>
    </p:observation>

    <!-- Med korrektion -->
    <!-- Höger öga -->
    <p:observation>
      <p:observationskod code="397535007" codeSystem="{$id_snomed-ct}" codeSystemName="SNOMED-CT"/>
      <p:varde>
        <xsl:attribute name="value" select="ns1:synfunktion/ns1:synskarpaMedKorrektion/ns1:hogerOga"/>
      </p:varde>
      <xsl:element name="{$ns-prefix}:lateralitet" namespace="{$ns-namespace}">
        <xsl:attribute name="code" select="'24028007'"/>
        <xsl:attribute name="codeSystem" select="$id_snomed-ct"/>
        <xsl:attribute name="codeSystemName" select="'SNOMED-CT'"/>
      </xsl:element>
    </p:observation>

    <!-- Vänster öga -->
    <p:observation>
      <p:observationskod code="397535007" codeSystem="{$id_snomed-ct}" codeSystemName="SNOMED-CT"/>
      <p:varde>
        <xsl:attribute name="value" select="ns1:synfunktion/ns1:synskarpaMedKorrektion/ns1:vansterOga"/>
      </p:varde>
      <xsl:element name="{$ns-prefix}:lateralitet" namespace="{$ns-namespace}">
        <xsl:attribute name="code" select="'7771000'"/>
        <xsl:attribute name="codeSystem" select="$id_snomed-ct"/>
        <xsl:attribute name="codeSystemName" select="'SNOMED-CT'"/>
      </xsl:element>
    </p:observation>

    <!-- Binokulärt -->
    <p:observation>
      <p:observationskod code="397535007" codeSystem="{$id_snomed-ct}" codeSystemName="SNOMED-CT"/>
      <p:varde>
        <xsl:attribute name="value" select="ns1:synfunktion/ns1:synskarpaMedKorrektion/ns1:binokulart"/>
      </p:varde>
      <xsl:element name="{$ns-prefix}:lateralitet" namespace="{$ns-namespace}">
        <xsl:attribute name="code" select="'51440002'"/>
        <xsl:attribute name="codeSystem" select="$id_snomed-ct"/>
        <xsl:attribute name="codeSystemName" select="'SNOMED-CT'"/>
      </xsl:element>
    </p:observation>

  </xsl:template>

</xsl:stylesheet>
