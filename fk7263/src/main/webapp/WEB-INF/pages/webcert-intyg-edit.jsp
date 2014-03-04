<%--

    Copyright (C) 2013 Inera AB (http://www.inera.se)

    This file is part of Inera Certificate Web (http://code.google.com/p/inera-certificate-web).

    Inera Certificate Web is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    Inera Certificate Web is distributed in the hope that it will be useful,
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

<title><spring:message code="wc.application.name" /></title>

<link rel="icon" href="/favicon.ico" type="image/vnd.microsoft.icon" />

<%-- Dependencies to common static resources components loaded from Webcert "module host" web app running at "/" context --%>
<link rel="stylesheet" href="/css/bootstrap/2.3.2/bootstrap.css">
<link rel="stylesheet" href="/css/bootstrap-responsive-modified.css">
<link rel="stylesheet" href="/css/inera-webcert.css">
<link rel="stylesheet" href="/css/inera-certificate.css">
<link rel="stylesheet" href="<c:url context="/m/fk7263" value="/webcert/css/fk7263.css"/>">

<script type="text/javascript" src="/usercontext.jsp"></script>

<script type="text/javascript">
    /**
     Global JS config/constants for this app, to be used by scripts
     **/
    var MODULE_CONFIG = {
        MI_COMMON_API_CONTEXT_PATH : '/moduleapi/certificate/',
        MODULE_CONTEXT_PATH : '/m/fk7263',
        CERT_ID_PARAMETER : '<c:out value="${id}"/>'
    }
</script>
</head>

<body ng-app="FK7263ViewCertApp">

  <a id="top"></a>
  <div class="container-fluid">

    <div class="row-fluid">
      <div class="span12 webcert-col webcert-col-single" ng-controller="EditCertCtrl">

			  <noscript>
			    <h1>
			      <span><spring:message code="error.noscript.title" /></span>
			    </h1>

			    <div class="alert alert-error">
			      <spring:message code="error.noscript.text" />
			    </div>
			  </noscript>

    	  <%-- ng-view that holds dynamic content managed by angular app --%>
			  <div id="view" ng-view></div>
	    </div>
	  </div>
	</div>

  <%-- Dependencies to common (3rd party) components loaded from web app running at "/" context--%>
  <script type="text/javascript" src="<c:url context="/m/fk7263" value="/js/vendor/angular/1.2.13/angular.js"/>"></script>
  <script type="text/javascript" src="<c:url context="/m/fk7263" value="/js/vendor/angular/1.2.13/angular-cookies.min.js"/>"></script>
  <script type="text/javascript" src="<c:url context="/m/fk7263" value="/js/vendor/angular/1.2.13/angular-route.min.js"/>"></script>
  <script type="text/javascript" src="<c:url context="/m/fk7263" value="/js/vendor/angular/1.2.13/angular-sanitize.min.js"/>"></script>
  <script type="text/javascript" src="<c:url context="/m/fk7263" value="/js/vendor/angular/1.2.13/i18n/angular-locale_sv-se.js"/>"></script>
  <script type="text/javascript" src="<c:url context="/m/fk7263" value="/js/vendor/ui-bootstrap/0.7.0/ui-bootstrap-tpls-0.7.0.js"/>"></script>

  <%-- Dependencies to common components loaded from web app running at "/" context--%>
  <script type="text/javascript" src="/js/common/wc-message-module.js"></script>
  <script type="text/javascript" src="/js/common/wc-utils.js"></script>
  <script type="text/javascript" src="/js/common/wc-common.js"></script>
  <script type="text/javascript" src="/js/common/wc-common-fragasvar-module.js"></script>
  <script type="text/javascript" src="/js/common/wc-common-message-resources.js"></script>

  <%-- Dependencies to module specific components loaded from this modules web app running at "?" context--%>
  <script type="text/javascript" src="<c:url context="/m/fk7263" value="/webcert/js/view/fk7263-app.js"/>"></script>
  <script type="text/javascript" src="<c:url context="/m/fk7263" value="/webcert/js/view/controllers.js"/>"></script>
  <script type="text/javascript" src="<c:url context="/m/fk7263" value="/webcert/js/view/directives.js"/>"></script>
  <script type="text/javascript" src="<c:url context="/m/fk7263" value="/webcert/js/view/messages.js"/>"></script>
  <script type="text/javascript" src="<c:url context="/m/fk7263" value="/webcert/js/view/services.js"/>"></script>
  <script type="text/javascript" src="<c:url context="/m/fk7263" value="/webcert/js/view/fragasvar-module.js"/>"></script>

</body>
</html>
