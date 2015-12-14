<iso:pattern id="q27">
  <iso:rule context="//gn:svar[@id='27']">
    <iso:assert test="count(gn:delsvar[@id='27.1']) = 1">
      'Smittbärarpenning' måste ha ett 'Om smittbärarpenning'.
    </iso:assert>
  </iso:rule>
</iso:pattern>

<iso:pattern id="q27">
<iso:rule context="//gn:delsvar[@id='27.1']">
  <iso:extends rule="non-empty-string"/>
</iso:rule>
</iso:pattern>

<iso:assert test="count(gn:svar[@id='27']) le 1">
Ett 'MU' får ha högst ett 'Smittbärarpenning'
</iso:assert>
