<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0"
    xmlns:ns1="urn:local:se:intygstjanster:services:1"
    xmlns:ns3="urn:local:se:intygstjanster:services:types:1"
    xmlns:p="urn:riv:clinicalprocess:healthcond:certificate:1"
    xmlns:p2="urn:riv:clinicalprocess:healthcond:certificate:ts-diabetes:1">

  <xsl:output method="xml" indent="yes"/>

  <xsl:include href="xsl/transform-ts-common.xsl"/>

  <xsl:template match="ns1:diabetesIntyg">
    <p:utlatande>

      <xsl:call-template name="utlatandeHeader"/>

      <xsl:apply-templates select="ns1:grundData"/>

      <xsl:call-template name="vardKontakt"/>

      <!-- Egenkontroll av blodsocker -->
      <xsl:if test="ns1:hypoglykemier/ns1:genomforEgenkontrollBlodsocker = 'true' or ns1:hypoglykemier/ns1:genomforEgenkontrollBlodsocker = '1'">
        <p:aktivitet>
          <p:aktivitetskod code="308113006" codeSystem="1.2.752.116.2.1.1.1" codeSystemName="SNOMED-CT"/>
          <p:forekomst>true</p:forekomst>
        </p:aktivitet>
      </xsl:if>

      <!-- TODO aktiviteterna synfältsprövnnig och ögats rörlighet ska bara skapas om bilaga är NEJ -->

      <!-- Synfältsprövning (Donders konfrontationsmetod) -->
      <p:aktivitet>
        <p:aktivitets-id root="1.2.752.129.2.1.2.1">
          <xsl:attribute name="extension" select="$synfaltsprovning-aktivitets-id"/>
        </p:aktivitets-id>
        <p:aktivitetskod code="86944008" codeSystem="1.2.752.116.2.1.1.1" codeSystemName="SNOMED-CT"/>
        <p2:metod code="MET1" codeSystem="b0c078c6-512a-42a5-ab42-a3380f369ac3" codeSystemName="kv_metod"/>
      </p:aktivitet>

      <xsl:call-template name="ogatsRorlighetAktivitet"/>

      <xsl:call-template name="synfaltsObservation"/>

      <xsl:call-template name="ogatsRorlighetObservation" />

      <!-- TODO aktiviteterna synfältsprövnnig och ögats rörlighet ska bara skapas om bilaga är NEJ -->

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
        <xsl:value-of select="ns1:utgava"/>
      </p2:utgava>
      <p2:version>
        <xsl:value-of select="ns1:version"/>
      </p2:version>

    </p:utlatande>
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
