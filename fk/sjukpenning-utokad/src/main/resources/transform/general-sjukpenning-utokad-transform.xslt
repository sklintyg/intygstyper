<?xml version="1.0"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
                xmlns:cr="urn:local:se:intygstjanster:services:fk:1">

  <!-- Copy all XML nodes, if no more specific template matches. -->
  <xsl:template match="@*|node()">
    <xsl:copy>
      <xsl:apply-templates select="@*|node()"/>
    </xsl:copy>
  </xsl:template>

  <!-- Transform <soap:Fault> element into a proper WS response. -->
  <xsl:template match="soap:Fault">
    <!--
      Here we call the 'abstract' template named response. Has to be provided by
      all XSLTs which include this one.
      -->
    <xsl:call-template name="response"/>
  </xsl:template>

  <!-- Transform <faultcode> and <faultstring> elements to <resultCode>, <errorId> and <errorText> -->
  <xsl:template name="result">
    <cr:resultCode>ERROR</cr:resultCode>

    <xsl:choose>
      <xsl:when test="contains(faultstring/text(), 'Unmarshalling Error')">
        <!-- Schema validation errors are transformed to VALIDATION_ERROR -->
        <cr:errorId>VALIDATION_ERROR</cr:errorId>
      </xsl:when>
      <xsl:when test="contains(faultcode/text(), 'soap:Client')">
        <!-- 'soap:Client' is transformed to VALIDATION_ERROR -->
        <cr:errorId>VALIDATION_ERROR</cr:errorId>
      </xsl:when>
      <xsl:otherwise>
        <!-- 'soap:Server' is transformed to APPLICATION_ERROR -->
        <cr:errorId>APPLICATION_ERROR</cr:errorId>
      </xsl:otherwise>
    </xsl:choose>

    <cr:resultText>
      <xsl:value-of select="faultstring/text()"/>
    </cr:resultText>
  </xsl:template>

</xsl:stylesheet>
