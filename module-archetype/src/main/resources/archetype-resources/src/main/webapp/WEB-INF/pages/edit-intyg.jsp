#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
<%--

    Copyright (C) 2013 Inera AB (http://www.inera.se)

    This file is part of Inera Certificate Modules (http://code.google.com/p/inera-certificate-modules).

    Inera Certificate Modules is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    Inera Certificate Modules is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>


<!DOCTYPE html>
<html lang="sv">
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=Edge"/>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="ROBOTS" content="nofollow, noindex" />
	
	<title><spring:message code="application.name" /></title>
	
	<link rel="icon" href="/favicon.ico" type="image/vnd.microsoft.icon" />
	
	<%-- Dependencies to common static resources components loaded from Mina Intyg "module host" web app running at "/" context --%>
	<link rel="stylesheet" href="/css/bootstrap/2.3.2/bootstrap.css" />
	<link rel="stylesheet" href="/css/inera.css" />
	<link rel="stylesheet" href="/css/inera-certificate.css" />

	<script type="text/javascript">
	    /**
	     Global JS config/constants for this app, to be used by scripts
	     **/
	    var MODULE_CONFIG = {
	        MI_COMMON_API_CONTEXT_PATH : '/moduleapi/certificate/',
	        MODULE_CONTEXT_PATH : '<c:out value="${symbol_dollar}{pageContext.request.contextPath}"/>',
	        CERT_ID_PARAMETER : '<c:out value="${symbol_dollar}{id}"/>',
	        PRINCIPAL_NAME : '<%=request.getHeader("X-Username")%>', // How do we get the username? cookie?
	        PROXY_PREFIX : '/m/${artifactId}' //maybe from serverside config?
	    }
	</script>
	
</head>

<body ng-app="RLIEditCertApp">

  <div class="container">
    <div id="content-container">
      <div class="content" style="padding-top: 0px;">
        <div class="row-fluid">
          <div id="content-body" class="span12" style="padding-top: 25px;">
            <div ng-view></div>
          </div>
        </div>
      </div>
    </div>
  </div>

	<%-- Dependencies to common (3rd party) components loaded from MI web app running at "/" context--%>
	<script type="text/javascript" src="/js/vendor/angular/1.1.5/angular.js"></script>
	<script type="text/javascript" src="/js/vendor/angular/1.1.5/i18n/angular-locale_sv-se.js"></script>
	<%--<script type="text/javascript" src="/js/vendor/ui-bootstrap/0.3.0/ui-bootstrap-tpls-0.3.0.js"></script>--%>
	
	<%-- Dependencies to common components loaded from MI web app running at "/" context--%>
	<script type="text/javascript" src="/js/modules/message-module.js"></script>
	<script type="text/javascript" src="/js/modules/mi-header-directive.js"></script>
	<script type="text/javascript" src="/js/modules/common-message-resources.js"></script>
	
	<%-- Dependencies to module specific components loaded from this modules web app running at "?" context--%>
	<script type="text/javascript" src="<c:url value="/webcert/js/${artifactId}-app.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/webcert/js/directives.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/webcert/js/controllers.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/webcert/js/messages.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/webcert-mock/js/modules/webcert-service.js"/>"></script>


</body>
</html>
