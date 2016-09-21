<?xml version="1.0" encoding="utf-8"?>
<iso:schema xmlns="http://purl.oclc.org/dsdl/schematron" xmlns:iso="http://purl.oclc.org/dsdl/schematron"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://purl.oclc.org/dsdl/schematron"
    queryBinding='xslt2' schemaVersion='ISO19757-3'>

  <iso:title>Schematron file for "Läkarutlåtande för aktivitetsersättning vid nedsatt arbetsförmåga".</iso:title>

  <iso:ns prefix="xs" uri="http://www.w3.org/2001/XMLSchema"/>
  <iso:ns prefix="rg" uri="urn:riv:clinicalprocess:healthcond:certificate:RegisterCertificateResponder:2"/>
  <iso:ns prefix="gn" uri="urn:riv:clinicalprocess:healthcond:certificate:2"/>
  <iso:ns prefix="tp" uri="urn:riv:clinicalprocess:healthcond:certificate:types:2"/>

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
      <iso:assert test="count(gn:svar[@id='5']) = 1">
        Ett 'MU' måste ha ett 'Sjukdomsförlopp för aktuella sjukdomar av betydelse'
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='6']) ge 1 and count(gn:svar[@id='6']) le 3">
        Ett 'MU' måste ha mellan 1 och 3 'Typ av diagnos'
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='7']) = 1">
        Ett 'MU' måste ha en 'Diagnosgrund'
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='45']) = 1">
        Ett 'MU' måste ha en 'Behov av ny diagnos'
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='8']) le 1">
        Ett 'MU' får ha högst ett 'Funktionsnedsättning för intellektuell funktion'
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='9']) le 1">
        Ett 'MU' får ha högst ett 'Funktionsnedsättning för kommunikation och social interaktion'
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='10']) le 1">
        Ett 'MU' får ha högst ett 'Funktionsnedsättning för uppmärksamhet och koncentration'
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='11']) le 1">
        Ett 'MU' får ha högst ett 'Funktionsnedsättning för annan psykisk funktion'
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='12']) le 1">
        Ett 'MU' får ha högst ett 'Funktionsnedsättning för syn, hörsel och tal'
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='13']) le 1">
        Ett 'MU' får ha högst ett 'Funktionsnedsättning för balans/koordination, motorik och smärta'
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='14']) le 1">
        Ett 'MU' får ha högst ett 'Funktionsnedsättning för annan kroppslig funktion'
      </iso:assert>
      <iso:assert test="count(gn:svar[matches(@id, '^([89]|1[0-4])$')]) ge 1">
        Ett 'MU' måste ha minst ett 'Funktionsnedsättning'
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='17']) = 1">
        Ett 'MU' måste ha ett 'Aktivitetsbegränsningar'
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='18']) le 1">
        Ett 'MU' får ha högst ett 'Avslutade medicinska behandlingar'
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='19']) le 1">
        Ett 'MU' får ha högst ett 'Pågående medicinska behandlingar'
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='20']) le 1">
        Ett 'MU' får ha högst ett 'Planerade medicinska behandlingar'
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='21']) le 1">
        Ett 'MU' får ha högst ett 'Substansintag (ord. läkemedel alkohol, tobak o övriga substansintag)'
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='22']) = 1">
        Ett 'MU' måste ha ett 'Medicinska förutsättningar för arbete'
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='23']) le 1">
        Ett 'MU' får ha högst ett 'Förmåga trots begränsningar'
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='24']) le 1">
        Ett 'MU' får ha högst ett 'Förslag på åtgärder'
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='25']) le 1">
        Ett 'MU' får ha högst ett 'Övrigt'
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='26']) le 1">
        Ett 'MU' får ha högst ett 'Kontakt önskas'
      </iso:assert>
      <iso:let name="svarsIdExpr" value="'^([123456789]|1[01234789]|2[0123456]|45|9[0-9]{3})$'"/>
      <iso:assert test="count(gn:svar[not(matches(@id, $svarsIdExpr))]) = 0">
        Oväntat svars-id. Svars-id:n måste matcha "<value-of select="$svarsIdExpr"/>".
      </iso:assert>
      <!-- Meddelandet kan innehålla ett valfritt antal tilläggsfrågor -->
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q1">
    <iso:rule context="//gn:svar[@id='1']">
      <iso:assert test="count(gn:instans) = 1">
        'Grund för medicinskt underlag (MU)' måste ha ett instansnummer.
      </iso:assert>
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
      <iso:assert test="matches(normalize-space(tp:cv/tp:code), '^(UNDERSOKNING|JOURNALUPPGIFTER|ANHORIG|ANNAT)$')">
        'Typ av grund för MU' kan ha ett av värdena UNDERSOKNING, JOURNALUPPGIFTER, ANHORING eller ANNAT.
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
      <iso:assert test="count(../gn:delsvar[@id='1.1']/tp:cv/tp:code[normalize-space(.) != 'ANNAT']) = 0">
        Om 'Typ av grund för MU' inte är 'Annat' så får 'Vilken annan grund finns för MU' inte anges.
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q1.1-1.3">
    <iso:rule context="//gn:delsvar[@id='1.1']/tp:cv/tp:code[normalize-space(.) = 'ANNAT']">
      <iso:assert test="../../../gn:delsvar[@id='1.3']">
        Om 'Typ av grund för MU' är 'Annat' så måste 'Vilken annan grund finns för MU' anges.
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q1.1-1.2-2.1">
    <iso:rule context="//gn:delsvar[@id='1.1']/tp:cv/tp:code[matches(normalize-space(.), '^(UNDERSOKNING|ANHORIG)$')]">
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
      <iso:assert test="count(gn:instans) = 1">
        'Andra medicinska utredningar eller underlag' måste ha ett instansnummer.
      </iso:assert>
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
      <iso:assert test="matches(normalize-space(tp:cv/tp:code), '^(NEUROPSYKIATRISKT|HABILITERING|ARBETSTERAPEUT|FYSIOTERAPEUT|LOGOPED|PSYKOLOG|FORETAGSHALSOVARD|SKOLHALSOVARD|SPECIALISTKLINIK|VARD_UTOMLANDS|OVRIGT_UTLATANDE)$')">
        'Utredning eller underlagstyp?' kan ha ett av värdena NEUROPSYKIATRISKT, HABILITERING, ARBETSTERAPEUT, FYSIOTERAPEUT, LOGOPED, PSYKOLOG, FORETAGSHALSOVARD, SKOLHALSOVARD, SPECIALISTKLINIK, VARD_UTOMLANDS, OVRIGT_UTLATANDE.
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

  <iso:pattern id="q5">
    <iso:rule context="//gn:svar[@id='5']">
      <iso:assert test="count(gn:delsvar[@id='5.1']) = 1">
        'Sjukdomsförlopp för aktuella sjukdomar av betydelse' måste ha ett 'Beskrivning av sjukdomsförlopp'.
      </iso:assert>
      <iso:let name="delsvarsIdExpr" value="'^5\.[1]$'"/>
      <iso:assert test="count(gn:delsvar[not(matches(@id, $delsvarsIdExpr))]) = 0">
        Oväntat delsvars-id i delsvar till svar "<value-of select="@id"/>". Delsvars-id:n måste matcha "<value-of select="$delsvarsIdExpr"/>".
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q5.1">
    <iso:rule context="//gn:delsvar[@id='5.1']">
      <iso:extends rule="non-empty-string"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q6">
    <iso:rule context="//gn:svar[@id='6']">
      <iso:assert test="count(gn:instans) = 1">
        'Typ av diagnos' måste ha ett instansnummer.
      </iso:assert>
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
      <iso:assert test="matches(normalize-space(tp:cv/tp:codeSystem), '(1.2.752.116.1.1.1.1.3|1.2.752.116.1.3.1.4.1)')">
        Diagnoskodsystem måste vara OID för ICD-10-SE eller KSH97-P.
      </iso:assert>
      <iso:assert test="matches(normalize-space(tp:cv/tp:code),'^([A-EG-Ya-eg-y][0-9]{2}[A-Za-z0-9-]*|[Zz][0-689][0-9][A-Za-z0-9-]*|[Zz]7[0-24-9][A-Za-z0-9-]*|[Zz]73[A-Za-z0-9-]+|[Ff][0-9]{2}[A-Za-z0-9-]+)$')">
        Diagnoskod måste anges som bokstav följt av två siffror följt av noll eller flera bokstäver, siffror eller bindestreck, d.v.s. minst tre positioner måste anges.
        Om diagnoskoden börjar med F eller Z73 måste bokstav och två siffor följas av minst en bokstav, siffra eller bindestreck, d.v.s. minst fyra positioner måste anges.
          </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q7">
    <iso:rule context="//gn:svar[@id='7']">
      <iso:assert test="count(gn:delsvar[@id='7.1']) = 1">
        'Ursprungsdiagnos' måste ha ett 'Ursprungsdiagnos'.
      </iso:assert>
      <iso:let name="delsvarsIdExpr" value="'^7\.[1]$'"/>
      <iso:assert test="count(gn:delsvar[not(matches(@id, $delsvarsIdExpr))]) = 0">
        Oväntat delsvars-id i delsvar till svar "<value-of select="@id"/>". Delsvars-id:n måste matcha "<value-of select="$delsvarsIdExpr"/>".
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q7.1">
    <iso:rule context="//gn:delsvar[@id='7.1']">
      <iso:extends rule="non-empty-string"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q45">
    <iso:rule context="//gn:svar[@id='45']">
      <iso:assert test="count(gn:delsvar[@id='45.1']) = 1">
        'Behov av ny diagnos' måste ha ett 'Diagnosuppdatering?'.
      </iso:assert>
      <iso:let name="delsvarsIdExpr" value="'^45\.[12]$'"/>
      <iso:assert test="count(gn:delsvar[not(matches(@id, $delsvarsIdExpr))]) = 0">
        Oväntat delsvars-id i delsvar till svar "<value-of select="@id"/>". Delsvars-id:n måste matcha "<value-of select="$delsvarsIdExpr"/>".
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q45.1">
    <iso:rule context="//gn:delsvar[@id='45.1']">
      <iso:extends rule="boolean"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q45.2">
    <iso:rule context="//gn:delsvar[@id='45.2']">
      <iso:extends rule="non-empty-string"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q45.1-45.2">
    <iso:rule context="gn:delsvar[@id='45.1' and (normalize-space(.)='1' or normalize-space(.)='true')]">
      <iso:assert test="count(../gn:delsvar[@id='45.2']) = 1">
        'Diagnos för ny uppdatering' måste ifyllas om 'Diagnosuppdatering?' besvarats med 'JA'.
      </iso:assert>
    </iso:rule>
    <iso:rule context="gn:delsvar[@id='45.1' and (normalize-space(.)='0' or normalize-space(.)='false')]">
      <iso:assert test="count(../gn:delsvar[@id='45.2']) = 0">
        'Diagnos för ny uppdatering' får inte fyllas i om 'Diagnosuppdatering?' besvarats med 'NEJ'.
      </iso:assert>
    </iso:rule>
  </iso:pattern>p

  <iso:pattern id="q8">
    <iso:rule context="//gn:svar[@id='8']">
      <iso:assert test="count(gn:delsvar[@id='8.1']) = 1">
        'Funktionsnedsättning för intellektuell funktion' måste ha ett 'Beskrivning'.
      </iso:assert>
      <iso:let name="delsvarsIdExpr" value="'^8\.[1]$'"/>
      <iso:assert test="count(gn:delsvar[not(matches(@id, $delsvarsIdExpr))]) = 0">
        Oväntat delsvars-id i delsvar till svar "<value-of select="@id"/>". Delsvars-id:n måste matcha "<value-of select="$delsvarsIdExpr"/>".
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q8.1">
    <iso:rule context="//gn:delsvar[@id='8.1']">
      <iso:extends rule="non-empty-string"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q9">
    <iso:rule context="//gn:svar[@id='9']">
      <iso:assert test="count(gn:delsvar[@id='9.1']) = 1">
        'Funktionsnedsättning för kommunikation och social interaktion' måste ha ett 'Beskrivning'.
      </iso:assert>
      <iso:let name="delsvarsIdExpr" value="'^9\.[1]$'"/>
      <iso:assert test="count(gn:delsvar[not(matches(@id, $delsvarsIdExpr))]) = 0">
        Oväntat delsvars-id i delsvar till svar "<value-of select="@id"/>". Delsvars-id:n måste matcha "<value-of select="$delsvarsIdExpr"/>".
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q9.1">
    <iso:rule context="//gn:delsvar[@id='9.1']">
      <iso:extends rule="non-empty-string"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q10">
    <iso:rule context="//gn:svar[@id='10']">
      <iso:assert test="count(gn:delsvar[@id='10.1']) = 1">
        'Funktionsnedsättning för uppmärksamhet och koncentration' måste ha ett 'Beskrivning'.
      </iso:assert>
      <iso:let name="delsvarsIdExpr" value="'^10\.[1]$'"/>
      <iso:assert test="count(gn:delsvar[not(matches(@id, $delsvarsIdExpr))]) = 0">
        Oväntat delsvars-id i delsvar till svar "<value-of select="@id"/>". Delsvars-id:n måste matcha "<value-of select="$delsvarsIdExpr"/>".
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q10.1">
    <iso:rule context="//gn:delsvar[@id='10.1']">
      <iso:extends rule="non-empty-string"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q11">
    <iso:rule context="//gn:svar[@id='11']">
      <iso:assert test="count(gn:delsvar[@id='11.1']) = 1">
        'Funktionsnedsättning för annan psykisk funktion' måste ha ett 'Beskrivning'.
      </iso:assert>
      <iso:let name="delsvarsIdExpr" value="'^11\.[1]$'"/>
      <iso:assert test="count(gn:delsvar[not(matches(@id, $delsvarsIdExpr))]) = 0">
        Oväntat delsvars-id i delsvar till svar "<value-of select="@id"/>". Delsvars-id:n måste matcha "<value-of select="$delsvarsIdExpr"/>".
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q11.1">
    <iso:rule context="//gn:delsvar[@id='11.1']">
      <iso:extends rule="non-empty-string"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q12">
    <iso:rule context="//gn:svar[@id='12']">
      <iso:assert test="count(gn:delsvar[@id='12.1']) = 1">
        'Funktionsnedsättning för syn, hörsel och tal' måste ha ett 'Beskrivning'.
      </iso:assert>
      <iso:let name="delsvarsIdExpr" value="'^12\.[1]$'"/>
      <iso:assert test="count(gn:delsvar[not(matches(@id, $delsvarsIdExpr))]) = 0">
        Oväntat delsvars-id i delsvar till svar "<value-of select="@id"/>". Delsvars-id:n måste matcha "<value-of select="$delsvarsIdExpr"/>".
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q12.1">
    <iso:rule context="//gn:delsvar[@id='12.1']">
      <iso:extends rule="non-empty-string"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q13">
    <iso:rule context="//gn:svar[@id='13']">
      <iso:assert test="count(gn:delsvar[@id='13.1']) = 1">
        'Funktionsnedsättning för balans/koordination, motorik och smärta' måste ha ett 'Beskrivning'.
      </iso:assert>
      <iso:let name="delsvarsIdExpr" value="'^13\.[1]$'"/>
      <iso:assert test="count(gn:delsvar[not(matches(@id, $delsvarsIdExpr))]) = 0">
        Oväntat delsvars-id i delsvar till svar "<value-of select="@id"/>". Delsvars-id:n måste matcha "<value-of select="$delsvarsIdExpr"/>".
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q13.1">
    <iso:rule context="//gn:delsvar[@id='13.1']">
      <iso:extends rule="non-empty-string"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q14">
    <iso:rule context="//gn:svar[@id='14']">
      <iso:assert test="count(gn:delsvar[@id='14.1']) = 1">
        'Funktionsnedsättning för annan kroppslig funktion' måste ha ett 'Beskrivning'.
      </iso:assert>
      <iso:let name="delsvarsIdExpr" value="'^14\.[1]$'"/>
      <iso:assert test="count(gn:delsvar[not(matches(@id, $delsvarsIdExpr))]) = 0">
        Oväntat delsvars-id i delsvar till svar "<value-of select="@id"/>". Delsvars-id:n måste matcha "<value-of select="$delsvarsIdExpr"/>".
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q14.1">
    <iso:rule context="//gn:delsvar[@id='14.1']">
      <iso:extends rule="non-empty-string"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q17">
    <iso:rule context="//gn:svar[@id='17']">
      <iso:assert test="count(gn:delsvar[@id='17.1']) = 1">
        'Aktivitetsbegränsningar' måste ha ett 'Beskrivning'.
      </iso:assert>
      <iso:let name="delsvarsIdExpr" value="'^17\.[1]$'"/>
      <iso:assert test="count(gn:delsvar[not(matches(@id, $delsvarsIdExpr))]) = 0">
        Oväntat delsvars-id i delsvar till svar "<value-of select="@id"/>". Delsvars-id:n måste matcha "<value-of select="$delsvarsIdExpr"/>".
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q17.1">
    <iso:rule context="//gn:delsvar[@id='17.1']">
      <iso:extends rule="non-empty-string"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q18">
    <iso:rule context="//gn:svar[@id='18']">
      <iso:assert test="count(gn:delsvar[@id='18.1']) = 1">
        'Avslutade medicinska behandlingar' måste ha ett 'Typ av avslutade med. behandlingar, period och resultat'.
      </iso:assert>
      <iso:let name="delsvarsIdExpr" value="'^18\.[1]$'"/>
      <iso:assert test="count(gn:delsvar[not(matches(@id, $delsvarsIdExpr))]) = 0">
        Oväntat delsvars-id i delsvar till svar "<value-of select="@id"/>". Delsvars-id:n måste matcha "<value-of select="$delsvarsIdExpr"/>".
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q18.1">
    <iso:rule context="//gn:delsvar[@id='18.1']">
      <iso:extends rule="non-empty-string"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q19">
    <iso:rule context="//gn:svar[@id='19']">
      <iso:assert test="count(gn:delsvar[@id='19.1']) = 1">
        'Pågående medicinska behandlingar' måste ha ett 'Typ av pågående behandlingar och ansvarig vårdenhet'.
      </iso:assert>
      <iso:let name="delsvarsIdExpr" value="'^19\.[1]$'"/>
      <iso:assert test="count(gn:delsvar[not(matches(@id, $delsvarsIdExpr))]) = 0">
        Oväntat delsvars-id i delsvar till svar "<value-of select="@id"/>". Delsvars-id:n måste matcha "<value-of select="$delsvarsIdExpr"/>".
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q19.1">
    <iso:rule context="//gn:delsvar[@id='19.1']">
      <iso:extends rule="non-empty-string"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q20">
    <iso:rule context="//gn:svar[@id='20']">
      <iso:assert test="count(gn:delsvar[@id='20.1']) = 1">
        'Planerade medicinska behandlingar' måste ha ett 'Typ av planerade behandlingar och ansvarig vårdenhet'.
      </iso:assert>
      <iso:let name="delsvarsIdExpr" value="'^20\.[1]$'"/>
      <iso:assert test="count(gn:delsvar[not(matches(@id, $delsvarsIdExpr))]) = 0">
        Oväntat delsvars-id i delsvar till svar "<value-of select="@id"/>". Delsvars-id:n måste matcha "<value-of select="$delsvarsIdExpr"/>".
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q20.1">
    <iso:rule context="//gn:delsvar[@id='20.1']">
      <iso:extends rule="non-empty-string"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q21">
    <iso:rule context="//gn:svar[@id='21']">
      <iso:assert test="count(gn:delsvar[@id='21.1']) = 1">
        'Substansintag (ord. läkemedel alkohol, tobak o övriga substansintag)' måste ha ett 'Typ av substansintag'.
      </iso:assert>
      <iso:let name="delsvarsIdExpr" value="'^21\.[1]$'"/>
      <iso:assert test="count(gn:delsvar[not(matches(@id, $delsvarsIdExpr))]) = 0">
        Oväntat delsvars-id i delsvar till svar "<value-of select="@id"/>". Delsvars-id:n måste matcha "<value-of select="$delsvarsIdExpr"/>".
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q21.1">
    <iso:rule context="//gn:delsvar[@id='21.1']">
      <iso:extends rule="non-empty-string"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q22">
    <iso:rule context="//gn:svar[@id='22']">
      <iso:assert test="count(gn:delsvar[@id='22.1']) = 1">
        'Medicinska förutsättningar för arbete' måste ha ett 'Beskrivning'.
      </iso:assert>
      <iso:let name="delsvarsIdExpr" value="'^22\.[1]$'"/>
      <iso:assert test="count(gn:delsvar[not(matches(@id, $delsvarsIdExpr))]) = 0">
        Oväntat delsvars-id i delsvar till svar "<value-of select="@id"/>". Delsvars-id:n måste matcha "<value-of select="$delsvarsIdExpr"/>".
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q22.1">
    <iso:rule context="//gn:delsvar[@id='22.1']">
      <iso:extends rule="non-empty-string"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q23">
    <iso:rule context="//gn:svar[@id='23']">
      <iso:assert test="count(gn:delsvar[@id='23.1']) = 1">
        'Förmåga trots begränsningar' måste ha ett 'Beskrivning'.
      </iso:assert>
      <iso:let name="delsvarsIdExpr" value="'^23\.[1]$'"/>
      <iso:assert test="count(gn:delsvar[not(matches(@id, $delsvarsIdExpr))]) = 0">
        Oväntat delsvars-id i delsvar till svar "<value-of select="@id"/>". Delsvars-id:n måste matcha "<value-of select="$delsvarsIdExpr"/>".
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q23.1">
    <iso:rule context="//gn:delsvar[@id='23.1']">
      <iso:extends rule="non-empty-string"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q24">
    <iso:rule context="//gn:svar[@id='24']">
      <iso:assert test="count(gn:delsvar[@id='24.1']) = 1">
        'Förslag till åtgärder' måste ha ett 'Beskrivning'.
      </iso:assert>
      <iso:let name="delsvarsIdExpr" value="'^24\.[1]$'"/>
      <iso:assert test="count(gn:delsvar[not(matches(@id, $delsvarsIdExpr))]) = 0">
        Oväntat delsvars-id i delsvar till svar "<value-of select="@id"/>". Delsvars-id:n måste matcha "<value-of select="$delsvarsIdExpr"/>".
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q24.1">
    <iso:rule context="//gn:delsvar[@id='24.1']">
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

  <iso:pattern id="instans-pattern">
    <iso:rule context="//gn:instans">
      <iso:assert test="number(.) ge 1">
        'Instans' måste vara större än 0.
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="non-empty-string-pattern">
    <iso:rule id="non-empty-string" abstract="true">
      <iso:assert test="count(*) = 0">Värdet får inte vara inbäddat i något element.</iso:assert>
      <iso:assert test="string-length(normalize-space(text())) > 0">Sträng kan inte vara tom.</iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="boolean-pattern">
    <iso:rule id="boolean" abstract="true">
      <iso:assert test="count(*) = 0">Booleskt värde får inte vara inbäddat i något element.</iso:assert>
      <iso:assert test=". castable as xs:boolean">Kan bara vara 'true/1' eller 'false/0'</iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="cv-pattern">
    <iso:rule id="cv" abstract="true">
      <iso:assert test="count(tp:cv) = 1">Ett värde av typen CV måste ha ett cv-element</iso:assert>
      <iso:assert test="count(tp:cv/tp:codeSystem) = 1">codeSystem är obligatoriskt</iso:assert>
      <iso:assert test="tp:cv/tp:codeSystem/count(*) = 0">'codeSystem' får inte vara inbäddat i något element.</iso:assert>
      <iso:assert test="count(tp:cv/tp:code) = 1">code är obligatoriskt</iso:assert>
      <iso:assert test="tp:cv/tp:code/count(*) = 0">'code' får inte vara inbäddat i något element.</iso:assert>
      <iso:assert test="count(tp:cv/tp:displayName) le 1">högst ett displayName kan anges</iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="date-pattern">
    <iso:rule id="date" abstract="true">
      <iso:assert test="count(*) = 0">Datum får inte vara inbäddat i något element.</iso:assert>
      <iso:assert test=". castable as xs:date">Värdet måste vara ett giltigt datum.</iso:assert>
      <iso:assert test="matches(., '^\d{4}-\d\d-\d\d')">Datumet måste uttryckas som YYYY-MM-DD.</iso:assert>
    </iso:rule>
  </iso:pattern>

</iso:schema>
