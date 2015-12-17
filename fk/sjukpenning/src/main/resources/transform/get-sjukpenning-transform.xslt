<?xml version="1.0"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:rmc="urn:local:se:intygstjanster:services:fk:GetSjukpenningResponder:1">

  <xsl:include href="transform/general-transform.xslt"/>

  <xsl:template name="response">
     <rmc:GetSjukpenningResponse>
       <rmc:resultat>
         <xsl:call-template name="result"/>
       </rmc:resultat>
     </rmc:GetSjukpenningResponse>
   </xsl:template>

</xsl:stylesheet>
