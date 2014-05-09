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
<%@ page language="java" isErrorPage="true" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
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

<link rel="icon" href="<c:url value="/favicon.ico" />" type="image/vnd.microsoft.icon" />

<%-- Dependencies to common static resources components loaded from Mina Intyg "module host" web app running at "/" context --%>
<link rel="stylesheet" href="/css/bootstrap/2.3.2/bootstrap.css" />
<link rel="stylesheet" href="/css/inera.css" />
<link rel="stylesheet" href="/css/inera-certificate.css" />
</head>


<body>
  <div class="container">
    <div id="page-header-container">
      <div id="page-header">
        <div id="page-header-left"></div>
        <div id="page-header-right"></div>
        <img id="logo" src="/img/logo_mina_intyg.png"/>
      </div>
    </div>
    <div id="content-container">
      <div class="content">
        <div class="row-fluid">
          <div id="content-body" class="span12">

            <h1>
              <spring:message code="error.generictechproblem.title" />
            </h1>
            <div id="noAuth" class="alert alert-error">
              <spring:message code="error.generictechproblem.text" />
            </div>

          </div>
        </div>
      </div>

    </div>
  </div>
  <!-- 
  Error:
   <c:out value="${pageContext.errorData.throwable.message}" />, 
   
   Stacktrace:
  <c:forEach items="${pageContext.errorData.throwable.stackTrace}" var="element">
    <c:out value="${element}" />, 
    </c:forEach>
-->
</body>
</html>