<?xml version="1.0"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:rmc="urn:local:se:intygstjanster:services:fk:RegisterSjukpenningResponder:1">

  <xsl:include href="transform/general-sjukpenning-transform.xslt"/>

  <xsl:template name="response">
     <rmc:RegisterSjukpenningResponse>
       <rmc:resultat>
         <xsl:call-template name="result"/>
       </rmc:resultat>
     </rmc:RegisterSjukpenningResponse>
   </xsl:template>

</xsl:stylesheet>
