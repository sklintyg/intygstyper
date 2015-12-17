<?xml version="1.0" encoding="utf-8"?>
<iso:schema xmlns="http://purl.oclc.org/dsdl/schematron" xmlns:iso="http://purl.oclc.org/dsdl/schematron"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://purl.oclc.org/dsdl/schematron"
    queryBinding='xslt2' schemaVersion='ISO19757-3'>

  <iso:title>Schematron file for sjukpenning (fördjupat).</iso:title>

  <iso:ns prefix="xs" uri="http://www.w3.org/2001/XMLSchema"/>
  <iso:ns prefix="rg" uri="urn:riv:clinicalprocess:healthcond:certificate:RegisterCertificateResponder:2"/>
  <iso:ns prefix="gn" uri="urn:riv:clinicalprocess:healthcond:certificate:2"/>
  <iso:ns prefix="tp" uri="urn:riv:clinicalprocess:healthcond:certificate:types:2"/>

  <iso:include href='types.sch#non-empty-string-pattern'/>
  <iso:include href='types.sch#boolean-pattern'/>
  <iso:include href='types.sch#cv-pattern'/>
  <iso:include href='types.sch#date-pattern'/>
  <iso:include href='types.sch#period-pattern'/>

  <!-- TODO: tilläggsfrågor -->
  <!-- TODO: arbetstidsfördelning endast vid icke-100% à la Emmys mail -->
  <!-- TODO: inga dubletter av åtgärds-id:n i enlighet med Marcus fråga -->
  <!-- TODO: begränsa värden i alla kodverk från FK -->

  <iso:pattern>
    <iso:rule context="//rg:intyg">
      <iso:assert test="count(gn:svar[@id='1']) = 1">
        Ett 'intyg' måste ha en 'sysselsättning'
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='3']) = 1">
        Ett 'intyg' måste ha en 'huvudsaklig orsak'
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='4']) le 2">
        Ett 'intyg' får inte ha fler än 2 bidiagnoser/åtgärder
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='5']) = 1">
        Ett 'intyg' måste ha en 'aktivitetsbegränsning'
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='6']) ge 1 and count(gn:svar[@id='6']) le 4">
        Ett 'intyg' måste ha mellan en och fyra 'sjukskrivning'
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='7']) = 1">
        Ett 'intyg' måste ha ett 'förändrat resesätt'
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='8']) = 1">
        Ett 'intyg' måste ha ett 'finns skäl till förändrad arbetstidförläggning'
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='10']) ge 1 and count(gn:svar[@id='10']) le 3">
        Ett 'intyg' måste ha minst ett och högst tre 'referens'
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='11']) = 1">
        Ett 'intyg' måste ha en 'funktionsnedsättning'
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='12']) le 1">
        Ett 'intyg' får inte ha mer än en pågående behandling
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='13']) le 1">
        Ett 'intyg' får inte ha mer än en planerad behandling
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='14']) = 1">
        Ett 'intyg' måste ha ett 'Överskrider FMB'
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='16']) = 1">
        Ett 'intyg' måste ha ett 'aktivitetsförmåga'
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='17']) = 1">
        Ett 'intyg' måste ha ett 'prognos'
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='18']) ge 1 and count(gn:svar[@id='18']) le 10">
        Ett 'intyg' måste ha mellan en och tio 'arbetslivsinriktad åtgärd'
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='21']) = 1">
        Ett 'intyg' måste ha ett 'önskar kontakt med FK'
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='22']) le 1">
        Ett 'intyg' får ha högst ett 'övrigt'
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <iso:rule context="//gn:svar[@id='1']">
      <iso:assert test="count(gn:delsvar[@id='1.1']) ge 1 and count(gn:delsvar[@id='1.1']) le 5"/>
      <iso:assert test="count(gn:delsvar[@id='1.2']) le 1"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <iso:rule context="//gn:delsvar[@id='1.1']">
      <iso:extends rule="cv"/>
      <iso:assert test="tp:cv/tp:codeSystem = 'KV_FKMU_0002'"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <!-- TODO: check that there is NO description of work if code is NOT 1 -->
    <iso:rule context="//gn:delsvar[@id='1.1']/tp:code[.='1']">
      <iso:assert test="//gn:delsvar[@id='1.2']"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <iso:rule context="//gn:delsvar[@id='1.2']">
      <iso:extends rule="non-empty-string"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <iso:rule context="//gn:svar[@id='3']">
      <iso:assert test="count(gn:delsvar[@id='3.1']) = 1">'Huvudsakling orsak' måste ha en 'huvuddiagnos'.</iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <iso:rule context="//gn:delsvar[@id='3.1']">
      <!-- TODO: rätt kodverk -->
      <iso:extends rule="cv"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <iso:rule context="//gn:svar[@id='4']">
      <iso:assert test="count(gn:delsvar[@id='4.1']) = 1">'Ytterligare orsak' måste ha ANTINGEN 'bidiagnos' ELLER 'åtgärd'.</iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <iso:rule context="//gn:delsvar[@id='4.1']">
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
    <iso:rule context="//gn:svar[@id='6']">
      <iso:assert test="count(gn:delsvar[@id='6.1']) = 1">'Sjukskrivning' måste ha en 'grad'.</iso:assert>
      <iso:assert test="count(gn:delsvar[@id='6.2']) = 1">'Sjukskrivning' måste ha en 'period'.</iso:assert>
      <iso:assert test="not(preceding-sibling::gn:svar[@id='6']/gn:delsvar[@id='6.1']/tp:code = gn:delsvar[@id='6.1']/tp:code)">
        Samma 'grad' kan inte användas flera gånger i samma 'sjukskrivning'.
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <iso:rule context="//gn:delsvar[@id='6.1']">
      <iso:extends rule="cv"/>
      <iso:assert test="tp:cv/tp:codeSystem = 'KV_FKMU_0003'">'grad' kan bara ha ett av fyra värden: 100%, 75%, 50%, 25%.</iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <iso:rule context="//gn:delsvar[@id='6.2']">
      <iso:extends rule="period"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <iso:rule context="//gn:svar[@id='7']">
      <iso:assert test="count(gn:delsvar[@id='7.1']) = 1">'Förändrat ressätt' måste ha ett delsvar.</iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <iso:rule context="//gn:delsvar[@id='7.1']">
      <iso:extends rule="boolean"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <iso:rule context="//gn:svar[@id='8']">
      <iso:assert test="count(gn:delsvar[@id='8.1']) = 1">'Förändrad arbetstidsförläggning' måste ha ett delsvar.</iso:assert>
      <iso:assert test="count(gn:delsvar[@id='8.2']) le 1"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <iso:rule context="//gn:delsvar[@id='8.1']">
      <iso:extends rule="boolean"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <!-- TODO: check that there is NO description of arbetstidsförläggning if arbetstidsförläggning is NOT true/1 -->
    <iso:rule context="//gn:delsvar[@id='8.1' and (.='1' or .='true')]">
      <iso:assert test="//gn:delsvar[@id='8.2']"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <iso:rule context="//gn:delsvar[@id='8.2']">
      <iso:extends rule="non-empty-string"/>
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
    <iso:rule context="//gn:svar[@id='11']">
      <iso:assert test="count(gn:delsvar[@id='11.1']) = 1">'Funktionsnedsättning' måste ha ett delsvar.</iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <iso:rule context="//gn:delsvar[@id='11.1']">
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
    <iso:rule context="//gn:svar[@id='14']">
      <iso:assert test="count(gn:delsvar[@id='14.1']) = 1">'Överskrider FMB' måste ha ett delsvar.</iso:assert>
      <iso:assert test="count(gn:delsvar[@id='14.2']) le 1"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <iso:rule context="//gn:delsvar[@id='14.1']">
      <iso:extends rule="boolean"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <!-- TODO: check that there is NO description of överskriden FMB if överskriden FMB is NOT true/1 -->
    <iso:rule context="//gn:delsvar[@id='14.1' and (.='1' or .='true')]">
      <iso:assert test="//gn:delsvar[@id='14.2']"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <iso:rule context="//gn:delsvar[@id='14.2']">
      <iso:extends rule="non-empty-string"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <iso:rule context="//gn:svar[@id='16']">
      <iso:assert test="count(gn:delsvar[@id='16.1']) = 1">'Aktivitetsförmåga' måste ha ett delsvar.</iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <iso:rule context="//gn:delsvar[@id='16.1']">
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
      <iso:assert test="count(gn:delsvar[@id='18.1']) = 1">'Arbetslivsinriktad åtgärd' måste ha ett delsvar.</iso:assert>
      <iso:assert test="count(gn:delsvar[@id='18.2']) le 1"/>
      <iso:assert test="count(gn:delsvar[@id='18.3']) le 1"/>
      <iso:assert test="count(gn:delsvar[@id='18.2']) + count(gn:delsvar[@id='18.3']) = 1"/>
      <iso:assert test="not(preceding-sibling::gn:svar[@id='18']/gn:delsvar[@id='18.1']/tp:code[.!='1'] and gn:delsvar[@id='18.1']/tp:code[.='1'])">
        'Inte aktuellt' kan inte kombineras med andra svar
      </iso:assert>
      <iso:assert test="not(preceding-sibling::gn:svar[@id='18']/gn:delsvar[@id='18.1']/tp:code[.='1'] and gn:delsvar[@id='18.1']/tp:code[.!='1'])">
        'Inte aktuellt' kan inte kombineras med andra svar
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <iso:rule context="//gn:delsvar[@id='18.1']">
      <iso:extends rule="cv"/>
      <iso:assert test="tp:cv/tp:codeSystem = 'KV_FKMU_0004'"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <iso:rule context="//gn:delsvar[@id='18.1']/tp:code[.='1']">
      <iso:assert test="//gn:delsvar[@id='18.2']"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <iso:rule context="//gn:delsvar[@id='18.1']/tp:code[.!='1']">
      <iso:assert test="//gn:delsvar[@id='18.3']"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <iso:rule context="//gn:delsvar[@id='18.2']">
      <iso:extends rule="non-empty-string"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <iso:rule context="//gn:delsvar[@id='18.3']">
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
    <iso:rule context="//gn:svar[@id='22']">
      <iso:assert test="count(gn:delsvar[@id='22.1']) = 1"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern>
    <iso:rule context="//gn:delsvar[@id='22.1']">
      <iso:extends rule="non-empty-string"/>
    </iso:rule>
  </iso:pattern>

</iso:schema>
