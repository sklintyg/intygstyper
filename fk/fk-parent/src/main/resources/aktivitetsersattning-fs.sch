<?xml version="1.0" encoding="utf-8"?>
<iso:schema xmlns="http://purl.oclc.org/dsdl/schematron" xmlns:iso="http://purl.oclc.org/dsdl/schematron"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://purl.oclc.org/dsdl/schematron"
    queryBinding='xslt2' schemaVersion='ISO19757-3'>

  <iso:title>Schematron file for "Läkarutlåtande för aktivitetsersättning vid förlängd skolgång".</iso:title>

  <iso:ns prefix="xs" uri="http://www.w3.org/2001/XMLSchema"/>
  <iso:ns prefix="rg" uri="urn:riv:clinicalprocess:healthcond:certificate:RegisterCertificateResponder:2"/>
  <iso:ns prefix="gn" uri="urn:riv:clinicalprocess:healthcond:certificate:2"/>
  <iso:ns prefix="tp" uri="urn:riv:clinicalprocess:healthcond:certificate:types:2"/>

  <iso:include href='types.sch#non-empty-string-pattern'/>
  <iso:include href='types.sch#boolean-pattern'/>
  <iso:include href='types.sch#cv-pattern'/>
  <iso:include href='types.sch#date-pattern'/>

  <iso:pattern id="intyg">
    <iso:rule context="//rg:intyg">
      <iso:assert test="count(gn:svar[@id='1']) ge 1 and count(gn:svar[@id='1']) le 4">
        Ett 'MU' måste ha mellan 1 och 4 'Grund för medicinskt underlag'
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='2']) = 1">
        Ett 'MU' måste ha ett 'Kännedom om patienten'
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='3']) = 1">
        Ett 'MU' måste ha ett 'Finns det andra medicinska utredningar eller underlag'
      </iso:assert>
      <!-- Fråga 4 kan besvaras ett valfritt antal gånger -->
      <iso:assert test="count(gn:svar[@id='6']) ge 1 and count(gn:svar[@id='6']) le 3">
        Ett 'MU' måste ha mellan 1 och 3 'Typ av diagnos'
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='15']) = 1">
        Ett 'MU' måste ha ett 'Funktionsnedsättningens debut, utveckling och visar sig nu'
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='16']) = 1">
        Ett 'MU' måste ha ett 'Funktionsnedsättningens påverkan på skolgången?'
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='25']) le 1">
        Ett 'MU' får ha högst ett 'Övrigt'
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='26']) le 1">
        Ett 'MU' får ha högst ett 'Kontakt önskas'
      </iso:assert>
      <iso:let name="svarsIdExpr" value="'^([12346]|1[56]|2[56]|9[0-9]{3})$'"/>
      <iso:assert test="count(gn:svar[not(matches(@id, $svarsIdExpr))]) = 0">
        Oväntat svars-id. Svars-id:n måste matcha "<value-of select="$svarsIdExpr"/>".
      </iso:assert>
      <!-- Meddelandet kan innehålla ett valfritt antal tilläggsfrågor -->
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q1">
    <iso:rule context="//gn:svar[@id='1']">
      <iso:assert test="count(gn:delsvar[@id='1.1']) = 1">
        'Grund för medicinskt underlag (MU)' måste ha ett 'Typ av grund för MU'.
      </iso:assert>
      <iso:assert test="count(gn:delsvar[@id='1.2']) = 1">
        'Grund för medicinskt underlag (MU)' måste ha ett 'Datum som grund för MU'.
      </iso:assert>
      <iso:assert test="count(gn:delsvar[@id='1.3']) le 1">
        'Grund för medicinskt underlag (MU)' får ha högst ett 'Vilken annan grund finns för MU'.
      </iso:assert>
      <iso:assert test="not(preceding-sibling::gn:svar[@id='1']/gn:delsvar[@id='1.1']/tp:cv/tp:code/normalize-space() = normalize-space(gn:delsvar[@id='1.1']/tp:cv/tp:code))">
        Samma 'Typ av grund för MU' kan inte användas flera gånger i samma 'MU'.
      </iso:assert>
      <iso:let name="delsvarsIdExpr" value="'^1\.[123]$'"/>
      <iso:assert test="count(gn:delsvar[not(matches(@id, $delsvarsIdExpr))]) = 0">
        Oväntat delsvars-id i delsvar till svar "<value-of select="@id"/>". Delsvars-id:n måste matcha "<value-of select="$delsvarsIdExpr"/>".
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q1.1">
    <iso:rule context="//gn:delsvar[@id='1.1']">
      <iso:extends rule="cv"/>
      <iso:assert test="tp:cv/tp:codeSystem = 'KV_FKMU_0001'">'codeSystem' måste vara 'KV_FKMU_0001'.</iso:assert>
      <iso:assert test="matches(normalize-space(tp:cv/tp:code), '^[1345]$')">
        'Typ av grund för MU' kan ha ett av värdena 1, 3, 4 eller 5.
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q1.2">
    <iso:rule context="//gn:delsvar[@id='1.2']">
      <iso:extends rule="date"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q1.3">
    <iso:rule context="//gn:delsvar[@id='1.3']">
      <iso:extends rule="non-empty-string"/>
      <iso:assert test="count(../gn:delsvar[@id='1.1']/tp:cv/tp:code[normalize-space(.) != '5']) = 0">
        Om 'Typ av grund för MU' inte är 'Annat' så får 'Vilken annan grund finns för MU' inte anges.
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q1.1-1.3">
    <iso:rule context="//gn:delsvar[@id='1.1']/tp:cv/tp:code[normalize-space(.) = '5']">
      <iso:assert test="../../../gn:delsvar[@id='1.3']">
        Om 'Typ av grund för MU' är 'Annat' så måste 'Vilken annan grund finns för MU' anges.
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q1.1-1.2-2.1">
    <iso:rule context="//gn:delsvar[@id='1.1']/tp:cv/tp:code[matches(normalize-space(.), '^[14]$')]">
      <iso:assert test="normalize-space(../../../../gn:svar[@id='2']/gn:delsvar[@id='2.1']) le normalize-space(../../../gn:delsvar[@id='1.2'])">
        'Kännedom om patienten' får inte vara senare än datum för 'Min undersökning av patienten' eller 'Anhörigs beskrivning av patienten'.
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q2">
    <iso:rule context="//gn:svar[@id='2']">
      <iso:assert test="count(gn:delsvar[@id='2.1']) = 1">
        'Kännedom om patienten' måste ha ett 'Datum för kännedom om patienten'.
      </iso:assert>
      <iso:let name="delsvarsIdExpr" value="'^2\.[1]$'"/>
      <iso:assert test="count(gn:delsvar[not(matches(@id, $delsvarsIdExpr))]) = 0">
        Oväntat delsvars-id i delsvar till svar "<value-of select="@id"/>". Delsvars-id:n måste matcha "<value-of select="$delsvarsIdExpr"/>".
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q2.1">
    <iso:rule context="//gn:delsvar[@id='2.1']">
      <iso:extends rule="date"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q3">
    <iso:rule context="//gn:svar[@id='3']">
      <iso:assert test="count(gn:delsvar[@id='3.1']) = 1">
        'Finns det andra medicinska utredningar eller underlag' måste ha ett 'Finns andra underlag?'.
      </iso:assert>
      <iso:let name="delsvarsIdExpr" value="'^3\.[1]$'"/>
      <iso:assert test="count(gn:delsvar[not(matches(@id, $delsvarsIdExpr))]) = 0">
        Oväntat delsvars-id i delsvar till svar "<value-of select="@id"/>". Delsvars-id:n måste matcha "<value-of select="$delsvarsIdExpr"/>".
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q3.1">
    <iso:rule context="//gn:delsvar[@id='3.1']">
      <iso:extends rule="boolean"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q3.1-4">
    <iso:rule context="//gn:delsvar[@id='3.1' and (normalize-space(.)='1' or normalize-space(.)='true')]">
      <iso:assert test="count(../../gn:svar[@id='4']) ge 1">
        Om 'Finns andra underlag?' besvarats med sant måste minst en 'Andra medicinska utredningar eller underlag' anges.
      </iso:assert>
    </iso:rule>
    <iso:rule context="//gn:delsvar[@id='3.1' and (normalize-space(.)='0' or normalize-space(.)='false')]">
      <iso:assert test="count(../../gn:svar[@id='4']) = 0">
        Om 'Finns andra underlag?' besvarats med falskt får 'Andra medicinska utredningar eller underlag' inte anges.
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q4">
    <iso:rule context="//gn:svar[@id='4']">
      <iso:assert test="count(gn:delsvar[@id='4.1']) = 1">
        'Andra medicinska utredningar eller underlag' måste ha ett 'Utredning eller underlagstyp?'.
      </iso:assert>
      <iso:assert test="count(gn:delsvar[@id='4.2']) = 1">
        'Andra medicinska utredningar eller underlag' måste ha ett 'Datum för utredning eller underlag'.
      </iso:assert>
      <iso:assert test="count(gn:delsvar[@id='4.3']) = 1">
        'Andra medicinska utredningar eller underlag' måste ha ett 'Var utredningen kan hämtas in?'.
      </iso:assert>
      <iso:let name="delsvarsIdExpr" value="'^4\.[123]$'"/>
      <iso:assert test="count(gn:delsvar[not(matches(@id, $delsvarsIdExpr))]) = 0">
        Oväntat delsvars-id i delsvar till svar "<value-of select="@id"/>". Delsvars-id:n måste matcha "<value-of select="$delsvarsIdExpr"/>".
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q4.1">
    <iso:rule context="//gn:delsvar[@id='4.1']">
      <iso:extends rule="cv"/>
      <iso:assert test="tp:cv/tp:codeSystem = 'KV_FKMU_0005'">'codeSystem' måste vara 'KV_FKMU_0005'.</iso:assert>
      <iso:assert test="matches(normalize-space(tp:cv/tp:code), '^([12345689]|1[01])$')">
        'Utredning eller underlagstyp?' kan ha ett av värdena 1, 2, 3, 4, 5, 6, 8, 9, 10 eller 11.
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q4.2">
    <iso:rule context="//gn:delsvar[@id='4.2']">
      <iso:extends rule="date"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q4.3">
    <iso:rule context="//gn:delsvar[@id='4.3']">
      <iso:extends rule="non-empty-string"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q6">
    <iso:rule context="//gn:svar[@id='6']">
      <iso:assert test="count(gn:delsvar[@id='6.1']) = 1">
        'Typ av diagnos' måste ha en 'Diagnostext'.
      </iso:assert>
      <iso:assert test="count(gn:delsvar[@id='6.2']) = 1">
        'Typ av diagnos' måste ha en 'Diagnoskod ICD-10'.
      </iso:assert>
      <iso:let name="delsvarsIdExpr" value="'^6\.[12]$'"/>
      <iso:assert test="count(gn:delsvar[not(matches(@id, $delsvarsIdExpr))]) = 0">
        Oväntat delsvars-id i delsvar till svar "<value-of select="@id"/>". Delsvars-id:n måste matcha "<value-of select="$delsvarsIdExpr"/>".
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q6.1">
    <iso:rule context="//gn:delsvar[@id='6.1']">
      <iso:extends rule="non-empty-string"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q6.2">
    <iso:rule context="//gn:delsvar[@id='6.2']">
      <iso:extends rule="cv"/>
      <iso:assert test="matches(normalize-space(tp:cv/tp:codeSystem), '(1.2.752.116.1.1.1.1.3|1.2.752.116.1.3.1.1.2)')">
        Diagnoskodsystem måste vara OID för ICD-10-SE eller KSH97-P.
      </iso:assert>
      <iso:assert test="matches(normalize-space(tp:cv/tp:code),'^([A-EG-Ya-eg-y][0-9]{2}[A-Za-z0-9-]*|[Zz][0-689][0-9][A-Za-z0-9-]*|[Zz]7[0-24-9][A-Za-z0-9-]*|[Zz]73[A-Za-z0-9-]+|[Ff][0-9]{2}[A-Za-z0-9-]+)$')">
        Diagnoskod måste anges som bokstav följt av två siffror följt av noll eller flera bokstäver, siffror eller bindestreck, d.v.s. minst tre positioner
        måste anges. Om diagnoskoden börjar med F eller Z73 måste bokstav och två siffor följas av minst en bokstav, siffra eller bindestreck, d.v.s. minst fyra
        positioner måste anges.
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q15">
    <iso:rule context="//gn:svar[@id='15']">
      <iso:assert test="count(gn:delsvar[@id='15.1']) = 1">
        'Funktionsnedsättningens debut, utveckling och visar sig nu' måste ha ett 'Beskriv funktionsnedsättningens debut och utveckling'.
      </iso:assert>
      <iso:let name="delsvarsIdExpr" value="'^15\.[1]$'"/>
      <iso:assert test="count(gn:delsvar[not(matches(@id, $delsvarsIdExpr))]) = 0">
        Oväntat delsvars-id i delsvar till svar "<value-of select="@id"/>". Delsvars-id:n måste matcha "<value-of select="$delsvarsIdExpr"/>".
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q15.1">
    <iso:rule context="//gn:delsvar[@id='15.1']">
      <iso:extends rule="non-empty-string"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q16">
    <iso:rule context="//gn:svar[@id='16']">
      <iso:assert test="count(gn:delsvar[@id='16.1']) = 1">
        'Funktionsnedsättningens påverkan på skolgången?' måste ha ett 'På vilket sätt har skolgången påverkats?'.
      </iso:assert>
      <iso:let name="delsvarsIdExpr" value="'^16\.[1]$'"/>
      <iso:assert test="count(gn:delsvar[not(matches(@id, $delsvarsIdExpr))]) = 0">
        Oväntat delsvars-id i delsvar till svar "<value-of select="@id"/>". Delsvars-id:n måste matcha "<value-of select="$delsvarsIdExpr"/>".
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q16.1">
    <iso:rule context="//gn:delsvar[@id='16.1']">
      <iso:extends rule="non-empty-string"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q25">
    <iso:rule context="//gn:svar[@id='25']">
      <iso:assert test="count(gn:delsvar[@id='25.1']) = 1">
        'Övrigt' måste ha ett 'Typ av övriga upplysningar'.
      </iso:assert>
      <iso:let name="delsvarsIdExpr" value="'^25\.[1]$'"/>
      <iso:assert test="count(gn:delsvar[not(matches(@id, $delsvarsIdExpr))]) = 0">
        Oväntat delsvars-id i delsvar till svar "<value-of select="@id"/>". Delsvars-id:n måste matcha "<value-of select="$delsvarsIdExpr"/>".
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q25.1">
    <iso:rule context="//gn:delsvar[@id='25.1']">
      <iso:extends rule="non-empty-string"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q26">
    <iso:rule context="//gn:svar[@id='26']">
      <iso:assert test="count(gn:delsvar[@id='26.1']) = 1">
        'Kontakt önskas' måste ha ett 'Kontakt önskas'.
      </iso:assert>
      <iso:assert test="count(gn:delsvar[@id='26.2']) le 1">
        'Kontakt önskas' får ha högst ett 'Motivering av kontakt önskas'.
      </iso:assert>
      <iso:let name="delsvarsIdExpr" value="'^26\.[12]$'"/>
      <iso:assert test="count(gn:delsvar[not(matches(@id, $delsvarsIdExpr))]) = 0">
        Oväntat delsvars-id i delsvar till svar "<value-of select="@id"/>". Delsvars-id:n måste matcha "<value-of select="$delsvarsIdExpr"/>".
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q26.1">
    <iso:rule context="//gn:delsvar[@id='26.1']">
      <iso:extends rule="boolean"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q26.2">
    <iso:rule context="//gn:delsvar[@id='26.2']">
      <iso:extends rule="non-empty-string"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q26.1-26.2">
    <iso:rule context="//gn:delsvar[@id='26.1' and (normalize-space(.)='0' or normalize-space(.)='false')]">
      <iso:assert test="count(../gn:delsvar[@id='26.2']) = 0">
        Om 'Kontakt önskas' besvarats med nej kan 'Motivering av kontakt önskas' inte fyllas i.
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q9000">
    <iso:rule context="//gn:svar[number(@id) ge 9001]">
      <iso:assert test="count(gn:delsvar[@id = string-join(@id, '.1')]) = 1">
        En tilläggsfråga måste ha ett delsvar med delfråge-id som består av frågans id plus '.1'.
      </iso:assert>
      <iso:assert test="not(preceding-sibling::*/@id = @id)">
        Två tilläggsfrågor kan inte ha samma id.
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q9000.1">
    <iso:rule context="//gn:svar[number(@id) ge 9001]/gn:delsvar">
      <iso:extends rule="non-empty-string"/>
    </iso:rule>
  </iso:pattern>

</iso:schema>
