<?xml version="1.0" encoding="utf-8"?>
<iso:schema xmlns="http://purl.oclc.org/dsdl/schematron" xmlns:iso="http://purl.oclc.org/dsdl/schematron"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://purl.oclc.org/dsdl/schematron"
    queryBinding='xslt2' schemaVersion='ISO19757-3'>

  <iso:title>Schematron file for sjukersättning.</iso:title>

  <iso:ns prefix="xs" uri="http://www.w3.org/2001/XMLSchema"/>
  <iso:ns prefix="rg" uri="urn:riv:clinicalprocess:healthcond:certificate:RegisterCertificateResponder:2"/>
  <iso:ns prefix="gn" uri="urn:riv:clinicalprocess:healthcond:certificate:2"/>
  <iso:ns prefix="tp" uri="urn:riv:clinicalprocess:healthcond:certificate:types:2"/>

  <!-- TODO: tilläggsfrågor -->
  <!-- TODO: begränsa värden i alla kodverk från FK -->

  <iso:pattern>
    <iso:rule context="//rg:intyg">
      <iso:assert test="count(gn:svar[@id='10']) ge 1 and count(gn:svar[@id='10']) le 3">
        Ett 'intyg' måste ha minst en men inte fler än tre 'referenser'
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='27']) = 1">
        Ett 'intyg' måste ha en 'kännedom om patient'
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='28']) le 10">
        Ett 'intyg' får ha högst tio 'annat underlag för bedömning'
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='3']) = 1">
        Ett 'intyg' måste ha en 'huvudsaklig orsak'
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='4']) le 2">
        Ett 'intyg' får inte ha fler än 2 bidiagnoser/åtgärder
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='23']) = 1">
        Ett 'intyg' måste ha en 'diagnostisering'
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='24']) = 1">
        Ett 'intyg' måste ha en 'ny bedömning av diagnos'
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='11']) ge 1 and count(gn:svar[@id='11']) le 3">
        Ett 'intyg' måste ha minst en och högst tre 'funktionsnedsättning'
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='5']) = 1">
        Ett 'intyg' måste ha en 'aktivitetsbegränsning'
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='12']) le 1">
        Ett 'intyg' får inte ha mer än en 'pågående behandling'
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='25']) le 1">
        Ett 'intyg' får inte ha mer än en 'avslutad behandling'
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='13']) le 1">
        Ett 'intyg' får inte ha mer än en 'planerad behandling'
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='17']) = 1">
        Ett 'intyg' måste ha ett 'prognos'
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='16']) = 1">
        Ett 'intyg' måste ha ett 'aktivitetsförmåga'
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='22']) le 1">
        Ett 'intyg' får ha högst ett 'övrigt'
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='21']) = 1">
        Ett 'intyg' måste ha ett 'önskar kontakt med FK'
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <iso:rule context="//gn:svar[@id='10']">
      <iso:assert test="count(gn:delsvar[@id='10.1']) = 1">'Referens' måste ha en 'referenstyp'.</iso:assert>
      <iso:assert test="count(gn:delsvar[@id='10.2']) = 1">'Referens' måste ha ett 'referensdatum'.</iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <iso:rule context="//gn:delsvar[@id='10.1']">
      <iso:extends rule="cv"/>
      <iso:assert test="tp:cv/tp:codeSystem = 'KV_FKMU_0001'"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <iso:rule context="//gn:delsvar[@id='10.2']">
      <iso:extends rule="date"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <iso:rule context="//gn:svar[@id='27']">
      <iso:assert test="count(gn:delsvar[@id='27.1']) = 1">'Kännedom om patient' måste ha ett delsvar.</iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <iso:rule context="//gn:delsvar[@id='27.1']">
      <iso:extends rule="date"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <iso:rule context="//gn:svar[@id='28']">
      <iso:assert test="count(gn:delsvar[@id='28.1']) = 1">'Annat underlag för bedömning' måste ha ett 'typ av underlag'.</iso:assert>
      <iso:assert test="count(gn:delsvar[@id='28.2']) = 1">'Annat underlag för bedömning' måste ha ett 'underlagsdatum'.</iso:assert>
      <iso:assert test="count(gn:delsvar[@id='28.3']) = 1">'Annat underlag för bedömning' måste ha ett 'underlag bifogas'.</iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <iso:rule context="//gn:delsvar[@id='28.1']">
      <!-- TODO: kodverk -->
      <iso:extends rule="cv"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <iso:rule context="//gn:delsvar[@id='28.2']">
      <iso:extends rule="date"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <iso:rule context="//gn:delsvar[@id='28.3']">
      <iso:extends rule="boolean"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <iso:rule context="//gn:svar[@id='3']">
      <iso:assert test="count(gn:delsvar[@id='3.1']) = 1">'Huvudsaklig orsak' måste ha en 'huvuddiagnos'.</iso:assert>
      <iso:assert test="count(gn:delsvar[@id='3.2']) = 1">'Huvudsaklig orsak' måste ha en 'beskrivning'.</iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <iso:rule context="//gn:delsvar[@id='3.1']"> 
      <!-- TODO: rätt kodverk -->
      <iso:extends rule="cv"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <iso:rule context="//gn:delsvar[@id='3.2']">
      <iso:extends rule="non-empty-string"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <iso:rule context="//gn:svar[@id='4']">
      <iso:assert test="count(gn:delsvar[@id='4.1']) = 1">'Ytterligare orsak' måste ha ANTINGEN 'bidiagnos' ELLER 'behandlingsåtgärd'.</iso:assert>
      <iso:assert test="count(gn:delsvar[@id='4.2']) = 1">'Ytterligare orsak' måste ha en 'beskrivning'.</iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <iso:rule context="//gn:delsvar[@id='4.1']">
      <!-- TODO: rätt kodverk -->
      <iso:extends rule="cv"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <iso:rule context="//gn:delsvar[@id='4.2']">
      <iso:extends rule="non-empty-string"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <iso:rule context="//gn:svar[@id='23']">
      <iso:assert test="count(gn:delsvar[@id='23.1']) = 1">'Diagnosticering' måste ha ett delsvar.</iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <iso:rule context="//gn:delsvar[@id='23.1']">
      <iso:extends rule="non-empty-string"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <iso:rule context="//gn:svar[@id='24']">
      <iso:assert test="count(gn:delsvar[@id='24.1']) = 1">'Ny bedömning av diagnos' måste ha ett delsvar.</iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <iso:rule context="//gn:delsvar[@id='24.1']">
      <iso:extends rule="boolean"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <iso:rule context="//gn:svar[@id='11']">
      <iso:assert test="count(gn:delsvar[@id='11.1']) = 1">'Funktionsnedsättning' måste ha en 'beskrivning'.</iso:assert>
      <iso:assert test="count(gn:delsvar[@id='11.2']) = 1">'Funktionsnedsättning' måste ha ett 'funktionsområde'.</iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <iso:rule context="//gn:delsvar[@id='11.1']">
      <iso:extends rule="non-empty-string"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <iso:rule context="//gn:delsvar[@id='11.2']">
      <!-- TODO: rätt kodverk -->
      <iso:extends rule="cv"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <iso:rule context="//gn:svar[@id='5']">
      <iso:assert test="count(gn:delsvar[@id='5.1']) = 1">'Aktivitetsbegränsning' måste ha ett delsvar.</iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <iso:rule context="//gn:delsvar[@id='5.1']">
      <iso:extends rule="non-empty-string"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <iso:rule context="//gn:svar[@id='12']">
      <iso:assert test="count(gn:delsvar[@id='12.1']) = 1">'Pågående behandling' måste ha ett delsvar.</iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <iso:rule context="//gn:delsvar[@id='12.1']">
      <iso:extends rule="non-empty-string"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <iso:rule context="//gn:svar[@id='25']">
      <iso:assert test="count(gn:delsvar[@id='25.1']) = 1">'Avslutad behandling' måste ha ett delsvar.</iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <iso:rule context="//gn:delsvar[@id='25.1']">
      <iso:extends rule="non-empty-string"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <iso:rule context="//gn:svar[@id='13']">
      <iso:assert test="count(gn:delsvar[@id='13.1']) = 1">'Planerad behandling' måste ha ett delsvar.</iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <iso:rule context="//gn:delsvar[@id='13.1']">
      <iso:extends rule="non-empty-string"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <iso:rule context="//gn:svar[@id='17']">
      <iso:assert test="count(gn:delsvar[@id='17.1']) = 1">'Prognos' måste ha ett delsvar.</iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <iso:rule context="//gn:delsvar[@id='17.1']">
      <iso:extends rule="non-empty-string"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <iso:rule context="//gn:svar[@id='18']">
      <iso:assert test="count(gn:delsvar[@id='18.1']) = 1">'Aktivitetsförmåga' måste ha ett delsvar.</iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <iso:rule context="//gn:delsvar[@id='18.1']">
      <iso:extends rule="non-empty-string"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <iso:rule context="//gn:svar[@id='22']">
      <iso:assert test="count(gn:delsvar[@id='22.1']) = 1">'Övrigt' måste ha ett delsvar.</iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <iso:rule context="//gn:delsvar[@id='22.1']">
      <iso:extends rule="non-empty-string"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <iso:rule context="//gn:svar[@id='21']">
      <iso:assert test="count(gn:delsvar[@id='21.1']) = 1">'Önskar kontakt med FK' måste ha ett delsvar.</iso:assert>
      <iso:assert test="count(gn:delsvar[@id='21.2']) le 1"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <iso:rule context="//gn:delsvar[@id='21.1']">
      <iso:extends rule="boolean"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <!-- TODO: check that there is NO description of önskar kontakt if önskar kontakt is NOT true/1 -->
    <iso:rule context="//gn:delsvar[@id='21.1' and (.='1' or .='true')]">
      <iso:assert test="//gn:delsvar[@id='21.2']"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <iso:rule context="//gn:delsvar[@id='21.2']">
      <iso:extends rule="non-empty-string"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <iso:rule id="cv" abstract="true">
      <iso:assert test="count(tp:cv) = 1">Ett värde av typen CV måste ha ett cv-element</iso:assert>
      <iso:assert test="count(tp:cv/tp:codeSystem) = 1">codeSystem saknas</iso:assert>
      <iso:assert test="count(tp:cv/tp:code) = 1">code saknas</iso:assert>
      <iso:assert test="count(tp:cv/tp:displayName) le 1">för många displayName</iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <iso:rule id="date" abstract="true">
      <iso:assert test=". castable as xs:date">värdet måste vara ett giltigt datum.</iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <iso:rule id="period" abstract="true">
      <iso:assert test="tp:datePeriod">En period måste inneslutas av ett 'datePeriod'-element</iso:assert>
      <iso:assert test="tp:datePeriod/tp:start castable as xs:date">'from' måste vara ett giltigt datum.</iso:assert>
      <iso:assert test="tp:datePeriod/tp:end castable as xs:date">'tom' måste vara ett giltigt datum.</iso:assert>
      <iso:assert test="tp:datePeriod/tp:start le tp:datePeriod/tp:end">'from' måste vara mindre än eller lika med 'to'</iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <iso:rule id="non-empty-string" abstract="true">
      <iso:assert test="string-length(normalize-space(text())) > 0">Sträng kan inte vara tom.</iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <iso:rule id="boolean" abstract="true">
      <iso:assert test=". castable as xs:boolean">Kan bara vara 'true/1' eller 'false/0'</iso:assert>
    </iso:rule>
  </iso:pattern>

</iso:schema>
