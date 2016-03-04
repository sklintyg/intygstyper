<?xml version="1.0" encoding="UTF-8"?>
<!-- ~ Copyright (C) 2015 Inera AB (http://www.inera.se) ~ ~ This file is 
	part of sklintyg (https://github.com/sklintyg). ~ ~ sklintyg is free software: 
	you can redistribute it and/or modify ~ it under the terms of the GNU General 
	Public License as published by ~ the Free Software Foundation, either version 
	3 of the License, or ~ (at your option) any later version. ~ ~ sklintyg is 
	distributed in the hope that it will be useful, ~ but WITHOUT ANY WARRANTY; 
	without even the implied warranty of ~ MERCHANTABILITY or FITNESS FOR A PARTICULAR 
	PURPOSE. See the ~ GNU General Public License for more details. ~ ~ You should 
	have received a copy of the GNU General Public License ~ along with this 
	program. If not, see <http://www.gnu.org/licenses/>. -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	version="2.0" xmlns:ns3="urn:riv:insuranceprocess:healthreporting:2"
	xmlns:ns0="urn:riv:insuranceprocess:healthreporting:RegisterMedicalCertificateResponder:3"
	xmlns:ns5="urn:riv:insuranceprocess:healthreporting:mu7263:3" xmlns:p2="urn:riv:clinicalprocess:healthcond:certificate:2"
	xmlns:p1="urn:riv:clinicalprocess:healthcond:certificate:RegisterCertificateResponder:2"
	xmlns:p3="urn:riv:clinicalprocess:healthcond:certificate:types:2">

	<xsl:output method="xml" indent="yes" />

	<xsl:template match="ns0:RegisterMedicalCertificate">
		<p1:RegisterCertificate>
			<p1:intyg>
				<p2:intygs-id>
					<p3:root>
						<xsl:value-of select="ns0:lakarutlatande/ns5:lakarutlatande-id" />
					</p3:root>
					<p3:extension>
						<xsl:value-of select="ns0:lakarutlatande/ns5:lakarutlatande-id" />
					</p3:extension>
				</p2:intygs-id>
				<p2:typ>
					<p3:code>
						LISU
					</p3:code>
					<p3:codeSystem>
						f6fb361a-e31d-48b8-8657-99b63912dd9b
					</p3:codeSystem>
					<p3:displayName>
						<xsl:value-of select="ns0:lakarutlatande/ns5:typAvUtlatande" />
					</p3:displayName>
				</p2:typ>
				<p2:version>1</p2:version>
				<p2:signeringstidpunkt>
					<xsl:value-of select="ns0:lakarutlatande/ns5:signeringsdatum" />
				</p2:signeringstidpunkt>
				<p2:skickatTidpunkt>
					<xsl:value-of select="ns0:lakarutlatande/ns5:skickatDatum" />
				</p2:skickatTidpunkt>
				<p2:patient>
					<p2:person-id>
						<p3:root>
							<xsl:value-of
								select="ns0:lakarutlatande/ns5:patient/ns3:person-id/@root" />
						</p3:root>
						<p3:extension>
							<xsl:value-of
								select="translate(ns0:lakarutlatande/ns5:patient/ns3:person-id/@extension, '-', '')" />
						</p3:extension>
					</p2:person-id>
					<p2:fornamn>
						<xsl:value-of
							select="substring-before(ns0:lakarutlatande/ns5:patient/ns3:fullstandigtNamn,' ')" />
					</p2:fornamn>
					<p2:efternamn>
						<xsl:value-of
							select="substring-after(ns0:lakarutlatande/ns5:patient/ns3:fullstandigtNamn,' ')" />
					</p2:efternamn>
                    <p2:postadress>.</p2:postadress>
                    <p2:postnummer>.</p2:postnummer>
                    <p2:postort>.</p2:postort>
				</p2:patient>
				<p2:skapadAv>
					<p2:personal-id>
						<p3:root>
							<xsl:value-of
								select="ns0:lakarutlatande/ns5:skapadAvHosPersonal/ns3:personal-id/@root" />
						</p3:root>
						<p3:extension>
							<xsl:value-of
								select="ns0:lakarutlatande/ns5:skapadAvHosPersonal/ns3:personal-id/@extension" />
						</p3:extension>
					</p2:personal-id>
					<p2:fullstandigtNamn>
						<xsl:value-of
							select="ns0:lakarutlatande/ns5:skapadAvHosPersonal/ns3:fullstandigtNamn" />
					</p2:fullstandigtNamn>
					<p2:forskrivarkod>
						<xsl:value-of
							select="ns0:lakarutlatande/ns5:skapadAvHosPersonal/ns3:forskrivarkod" />
					</p2:forskrivarkod>

					<p2:enhet>
						<p2:enhets-id>
							<p3:root>
								<xsl:value-of
									select="ns0:lakarutlatande/ns5:skapadAvHosPersonal/ns3:enhet/ns3:enhets-id/@root" />
							</p3:root>
							<p3:extension>
								<xsl:value-of
									select="ns0:lakarutlatande/ns5:skapadAvHosPersonal/ns3:enhet/ns3:enhets-id/@extension" />
							</p3:extension>
						</p2:enhets-id>
						<p2:arbetsplatskod>
							<p3:root>
								<xsl:value-of
									select="ns0:lakarutlatande/ns5:skapadAvHosPersonal/ns3:enhet/ns3:arbetsplatskod/@root" />
							</p3:root>
							<p3:extension>
								<xsl:value-of
									select="ns0:lakarutlatande/ns5:skapadAvHosPersonal/ns3:enhet/ns3:arbetsplatskod/@extension" />
							</p3:extension>
						</p2:arbetsplatskod>
						<p2:enhetsnamn>
							<xsl:value-of
								select="ns0:lakarutlatande/ns5:skapadAvHosPersonal/ns3:enhet/ns3:enhetsnamn" />
						</p2:enhetsnamn>
						<p2:postadress>
							<xsl:value-of
								select="ns0:lakarutlatande/ns5:skapadAvHosPersonal/ns3:enhet/ns3:postadress" />
						</p2:postadress>
						<p2:postnummer>
							<xsl:value-of
								select="ns0:lakarutlatande/ns5:skapadAvHosPersonal/ns3:enhet/ns3:postnummer" />
						</p2:postnummer>
						<p2:postort>
							<xsl:value-of
								select="ns0:lakarutlatande/ns5:skapadAvHosPersonal/ns3:enhet/ns3:postort" />
						</p2:postort>
						<p2:telefonnummer>
							<xsl:value-of
								select="ns0:lakarutlatande/ns5:skapadAvHosPersonal/ns3:enhet/ns3:telefonnummer" />
						</p2:telefonnummer>
						<p2:vardgivare>
							<p2:vardgivare-id>
								<p3:root>
									<xsl:value-of
										select="ns0:lakarutlatande/ns5:skapadAvHosPersonal/ns3:enhet/ns3:vardgivare/ns3:vardgivare-id/@root" />
								</p3:root>
								<p3:extension>12345678</p3:extension>
							</p2:vardgivare-id>
							<p2:vardgivarnamn>
								<xsl:value-of
									select="ns0:lakarutlatande/ns5:skapadAvHosPersonal/ns3:enhet/ns3:vardgivare/ns3:vardgivarnamn" />
							</p2:vardgivarnamn>
						</p2:vardgivare>
					</p2:enhet>
				</p2:skapadAv>

				<xsl:for-each select="ns0:lakarutlatande/ns5:vardkontakt">
					<p2:svar id="1">
						<p2:delsvar id="1.1">
							<xsl:call-template name="beslutsunderlag">
								<xsl:with-param name="list" select="./ns5:vardkontakttyp" />
							</xsl:call-template>
						</p2:delsvar>
						<p2:delsvar id="1.2">
							<xsl:value-of select="./ns5:vardkontaktstid" />
						</p2:delsvar>
					</p2:svar>
				</xsl:for-each>
				<xsl:if test="not(ns0:lakarutlatande/ns5:vardkontakt)">
					<p2:svar id="1">
						<p2:delsvar id="1.1">
							<p3:cv>
								<p3:code>5</p3:code>
								<p3:codeSystem>KV_FKMU_0001</p3:codeSystem>
							</p3:cv>
						</p2:delsvar>
						<p2:delsvar id="1.2">
							<xsl:value-of
								select="substring-before(ns0:lakarutlatande/ns5:skickatDatum, 'T')" />
						</p2:delsvar>
						<p2:delsvar id="1.3">
							Information om vårdkontakt saknas.
						</p2:delsvar>
					</p2:svar>
				</xsl:if>

	<xsl:choose>
		<xsl:when
			test="not(ns0:lakarutlatande/ns5:medicinsktTillstand/ns5:beskrivning)">
			<xsl:call-template name="diagnostext">
				<xsl:with-param name="beskrivning" select="'Information om medicinskt tillstånd saknas'" />
				<xsl:with-param name="code" select="'U99'" />
				<xsl:with-param name="codeSystem" select="'1.2.752.116.1.1.1.1.3'" />
			</xsl:call-template>
		</xsl:when>
		<xsl:otherwise>
		<xsl:call-template name="diagnostext">
				<xsl:with-param name="beskrivning" select="ns0:lakarutlatande/ns5:medicinsktTillstand/ns5:beskrivning" />
				<xsl:with-param name="code" select="ns0:lakarutlatande/ns5:medicinsktTillstand/ns5:tillstandskod/@code" />
				<xsl:with-param name="codeSystem" select="'1.2.752.116.1.1.1.1.3'" />
			</xsl:call-template>

		</xsl:otherwise>
	</xsl:choose>
	
				<xsl:choose>
				<xsl:when test="not(ns0:lakarutlatande/ns5:funktionstillstand/ns5:typAvFunktionstillstand[.='Aktivitet'])">
				<xsl:call-template name="aktivitetsbegransningar">
				<xsl:with-param name="beskrivning" select="'Uppgift om aktivitetsbegränsningar saknas.'" />
			</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
					<xsl:call-template name="aktivitetsbegransningar">
				<xsl:with-param name="beskrivning" select="ns0:lakarutlatande/ns5:funktionstillstand/ns5:beskrivning" />
			</xsl:call-template>
					</xsl:otherwise>
				</xsl:choose>

	<xsl:choose>
		<xsl:when
			test="not(ns0:lakarutlatande/ns5:funktionstillstand/ns5:arbetsformaga/ns5:sysselsattning/ns5:typAvSysselsattning)">
			<p2:svar id="28">
				<p2:delsvar id="28.1">
					<p3:cv>
						<p3:code>2</p3:code>
						<p3:codeSystem>KV_FKMU_0002</p3:codeSystem>
					</p3:cv>
				</p2:delsvar>
			</p2:svar>
		</xsl:when>
		<xsl:otherwise>
			<xsl:variable name="sysselsattningar"
				select="ns0:lakarutlatande/ns5:funktionstillstand/ns5:arbetsformaga/ns5:sysselsattning/ns5:typAvSysselsattning" />
			<p2:svar id="28">
				<p2:delsvar id="28.1">
					<xsl:call-template name="typavarbete">
						<xsl:with-param name="param"
							select="$sysselsattningar" />
					</xsl:call-template>
				</p2:delsvar>
			</p2:svar>
			<xsl:if
				test="count(ns0:lakarutlatande/ns5:funktionstillstand/ns5:arbetsformaga/ns5:arbetsuppgift/ns5:typAvArbetsuppgift) != 0 and $sysselsattningar[1]='Nuvarande_arbete'">
				<p2:svar id="29">
					<p2:delsvar id="29.1">
						<xsl:value-of
							select="ns0:lakarutlatande/ns5:funktionstillstand/ns5:arbetsformaga/ns5:arbetsuppgift/ns5:typAvArbetsuppgift" />
					</p2:delsvar>
				</p2:svar>
			</xsl:if>
		</xsl:otherwise>
	</xsl:choose>

				

				<xsl:choose>
				<xsl:when test="not(ns0:lakarutlatande/ns5:funktionstillstand/ns5:arbetsformaga/ns5:arbetsformagaNedsattning)">
						<p2:svar id="32">
							<p2:delsvar id="32.1">
								<p3:cv>
									<p3:code>1</p3:code>
									<p3:codeSystem>KV_FKMU_0003</p3:codeSystem>
								</p3:cv>
							</p2:delsvar>
							<p2:delsvar id="32.2">
								<p3:datePeriod>
									<p3:start>
										<xsl:value-of
											select="substring-before(ns0:lakarutlatande/ns5:skickatDatum, 'T')" />
									</p3:start>
									<p3:end>
										<xsl:value-of
											select="substring-before(ns0:lakarutlatande/ns5:skickatDatum, 'T')" />
									</p3:end>
								</p3:datePeriod>
							</p2:delsvar>
						</p2:svar>
					</xsl:when>
					<xsl:otherwise>
						<xsl:for-each
							select="ns0:lakarutlatande/ns5:funktionstillstand/ns5:arbetsformaga/ns5:arbetsformagaNedsattning">
							<p2:svar id="32">
								<p2:delsvar id="32.1">
									<p3:cv>
										<xsl:call-template name="sjukskrivningskod">
											<xsl:with-param name="list" select="./ns5:nedsattningsgrad" />
										</xsl:call-template>
									</p3:cv>
								</p2:delsvar>
								<p2:delsvar id="32.2">
									<p3:datePeriod>
										<p3:start>
											<xsl:value-of select="./ns5:varaktighetFrom" />
										</p3:start>
										<p3:end>
											<xsl:value-of select="./ns5:varaktighetTom" />
										</p3:end>
									</p3:datePeriod>
								</p2:delsvar>
							</p2:svar>
						</xsl:for-each>
					</xsl:otherwise>
				</xsl:choose>

				<xsl:if
					test="ns0:lakarutlatande/ns5:funktionstillstand/ns5:arbetsformaga/ns5:arbetsformagaNedsattning/ns5:nedsattningsgrad != 'Helt_nedsatt'">
					<p2:svar id="33">
						<p2:delsvar id="33.1">
							<xsl:value-of select="boolean('false')" />
						</p2:delsvar>
						<p2:delsvar id="33.2">
							En motivering
						</p2:delsvar>

					</p2:svar>
				</xsl:if>

				<xsl:choose>
                  <xsl:when test="not(ns0:lakarutlatande/ns5:bedomtTillstand)">
					<p2:svar id="35">
								<p2:delsvar id="35.1">
									Bedömt tillstånd saknas. 
								</p2:delsvar>
							</p2:svar>
					</xsl:when>
					<xsl:otherwise>
						<xsl:for-each select="ns0:lakarutlatande/ns5:bedomtTillstand">
							<p2:svar id="35">
								<p2:delsvar id="35.1">
									<xsl:value-of select="./ns5:beskrivning" />
								</p2:delsvar>
							</p2:svar>
						</xsl:for-each>
					</xsl:otherwise>
				</xsl:choose>

				<xsl:choose>
					<xsl:when
						test="ns0:lakarutlatande/ns5:funktionstillstand/ns5:arbetsformaga/ns5:prognosangivelse">
						<p2:svar id="39">
							<p2:delsvar id="39.1">
								<xsl:call-template name="prognos">
									<xsl:with-param name="param"
										select="ns0:lakarutlatande/ns5:funktionstillstand/ns5:arbetsformaga/ns5:prognosangivelse" />
								</xsl:call-template>
							</p2:delsvar>

							<xsl:if
								test="ns0:lakarutlatande/ns5:funktionstillstand/ns5:arbetsformaga/ns5:prognosangivelse = 'Det_gar_inte_att_bedomma'">
								<p2:delsvar id="39.2">
									<xsl:value-of
										select="ns0:lakarutlatande/ns5:funktionstillstand/ns5:arbetsformaga/ns5:motivering" />
								</p2:delsvar>
							</xsl:if>
						</p2:svar>
					</xsl:when>
					<xsl:otherwise>
						<p2:svar id="39">
							<p2:delsvar id="39.1">
								<p3:cv>
									<p3:code>4</p3:code>
									<p3:codeSystem>KV_FKMU_0006</p3:codeSystem>
								</p3:cv>
							</p2:delsvar>
							<p2:delsvar id="39.2">
								Prognosangivelse saknas.
							</p2:delsvar>
						</p2:svar>
					</xsl:otherwise>
				</xsl:choose>

				<xsl:choose>
				 <xsl:when test="not(ns0:lakarutlatande/ns5:aktivitet)">
						<p2:svar id="40">
							<p2:delsvar id="40.1">
								<p3:cv>
									<p3:code>9</p3:code>
									<p3:codeSystem>KV_FKMU_0004</p3:codeSystem>
								</p3:cv>
							</p2:delsvar>
							<p2:delsvar id="40.2">
								Information om arbetslivsinriktade
								åtgärder saknas.
							</p2:delsvar>
						</p2:svar>
					</xsl:when>
					<xsl:otherwise>
						<xsl:for-each select="ns0:lakarutlatande/ns5:aktivitet">
							<p2:svar id="40">
								<p2:delsvar id="40.1">
									<xsl:call-template name="arbetslivsinriktadeatgarder">
										<xsl:with-param name="param" select="./ns5:aktivitetskod" />
									</xsl:call-template>
								</p2:delsvar>
								<p2:delsvar id="40.2">
									<xsl:value-of select="./ns5:beskrivning" />
								</p2:delsvar>
							</p2:svar>
						</xsl:for-each>
					</xsl:otherwise>
				</xsl:choose>
			</p1:intyg>
		</p1:RegisterCertificate>
	</xsl:template>

	<xsl:template name="beslutsunderlag">
		<xsl:param name="list" />
		<xsl:choose>
			<xsl:when test="contains($list, 'Min_undersokning_av_patienten')">
				<p3:cv>
					<p3:code>1</p3:code>
					<p3:codeSystem>KV_FKMU_0001</p3:codeSystem>
				</p3:cv>
			</xsl:when>
			<xsl:when test="contains($list, 'Min_telefonkontakt_med_patienten')">
				<p3:cv>
					<p3:code>2</p3:code>
					<p3:codeSystem>KV_FKMU_0001</p3:codeSystem>
				</p3:cv>
			</xsl:when>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="typavarbete">
		<xsl:param name="param" />
		<xsl:choose>
			<xsl:when test="contains($param[1], 'Nuvarande_arbete')">
				<p3:cv>
					<p3:code>1</p3:code>
					<p3:codeSystem>KV_FKMU_0002</p3:codeSystem>
				</p3:cv>
			</xsl:when>
			<xsl:when test="contains($param[1], 'Arbetsloshet')">
				<p3:cv>
					<p3:code>2</p3:code>
					<p3:codeSystem>KV_FKMU_0002</p3:codeSystem>
				</p3:cv>
			</xsl:when>
			<xsl:when test="contains($param[1], 'Foraldraledighet')">
				<p3:cv>
					<p3:code>3</p3:code>
					<p3:codeSystem>KV_FKMU_0002</p3:codeSystem>
				</p3:cv>
			</xsl:when>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="sjukskrivningskod">
		<xsl:param name="list" />
		<xsl:choose>
			<xsl:when test="contains($list, 'Helt_nedsatt')">
				<p3:code>1</p3:code>
				<p3:codeSystem>KV_FKMU_0003</p3:codeSystem>
			</xsl:when>
			<xsl:when test="contains($list, 'Nedsatt_med_1/4')">
				<p3:code>4</p3:code>
				<p3:codeSystem>KV_FKMU_0003</p3:codeSystem>
			</xsl:when>
			<xsl:when test="contains($list, 'Nedsatt_med_1/2')">
				<p3:code>3</p3:code>
				<p3:codeSystem>KV_FKMU_0003</p3:codeSystem>
			</xsl:when>
			<xsl:when test="contains($list, 'Nedsatt_med_3/4')">
				<p3:code>2</p3:code>
				<p3:codeSystem>KV_FKMU_0003</p3:codeSystem>
			</xsl:when>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="arbetslivsinriktadeatgarder">
		<xsl:param name="param" />
		<xsl:choose>
			<xsl:when
				test="contains($param, 'Arbetslivsinriktad_rehabilitering_ar_aktuell')">
				<p3:cv>
					<p3:code>2</p3:code>
					<p3:codeSystem>KV_FKMU_0004</p3:codeSystem>
				</p3:cv>
			</xsl:when>
			<xsl:when
				test="contains($param, 'Arbetslivsinriktad_rehabilitering_ar_ej_aktuell')">
				<p3:cv>
					<p3:code>1</p3:code>
					<p3:codeSystem>KV_FKMU_0004</p3:codeSystem>
				</p3:cv>
			</xsl:when>
			<xsl:when
				test="contains($param, 'Gar_ej_att_bedomma_om_arbetslivsinriktad_rehabilitering_ar_aktuell')">
				<p3:cv>
					<p3:code>11</p3:code>
					<p3:codeSystem>KV_FKMU_0004</p3:codeSystem>
				</p3:cv>
			</xsl:when>
			<xsl:when
				test="contains($param, 'Forandrat_ressatt_till_arbetsplatsen_ar_aktuellt')">
				<p3:cv>
					<p3:code>3</p3:code>
					<p3:codeSystem>KV_FKMU_0004</p3:codeSystem>
				</p3:cv>
			</xsl:when>

			<xsl:when
				test="contains($param, 'Forandrat_ressatt_till_arbetsplatsen_ar_ej_aktuellt')">
				<p3:cv>
					<p3:code>1</p3:code>
					<p3:codeSystem>KV_FKMU_0004</p3:codeSystem>
				</p3:cv>
			</xsl:when>
			<xsl:when
				test="contains($param, 'Planerad_eller_pagaende_behandling_eller_atgard_inom_sjukvarden')">
				<p3:cv>
					<p3:code>y</p3:code>
					<p3:codeSystem>KV_FKMU_0004</p3:codeSystem>
				</p3:cv>
			</xsl:when>
			<xsl:when test="contains($param, 'Planerad_eller_pagaende_annan_atgard')">
				<p3:cv>
					<p3:code>11</p3:code>
					<p3:codeSystem>KV_FKMU_0004</p3:codeSystem>
				</p3:cv>
			</xsl:when>
			<xsl:when
				test="contains($param, 'Kontakt_med_Forsakringskassan_ar_aktuell')">
				<p3:cv>
					<p3:code>11</p3:code>
					<p3:codeSystem>KV_FKMU_0004</p3:codeSystem>
				</p3:cv>
			</xsl:when>
			<xsl:when
				test="contains($param, 'Patienten_behover_fa_kontakt_med_foretagshalsovarden')">
				<p3:cv>
					<p3:code>9</p3:code>
					<p3:codeSystem>KV_FKMU_0004</p3:codeSystem>
				</p3:cv>
			</xsl:when>
			<xsl:when test="contains($param, 'Avstangning_enligt_SmL_pga_smitta')">
				<p3:cv>
					<p3:code>11</p3:code>
					<p3:codeSystem>KV_FKMU_0004</p3:codeSystem>
				</p3:cv>
			</xsl:when>
			<xsl:when test="contains($param, 'Ovrigt')">
				<p3:cv>
					<p3:code>11</p3:code>
					<p3:codeSystem>KV_FKMU_0004</p3:codeSystem>
				</p3:cv>
			</xsl:when>
			<xsl:when
				test="contains($param, 'Patienten_behover_fa_kontakt_med_Arbetsformedlingen')">
				<p3:cv>
					<p3:code>4</p3:code>
					<p3:codeSystem>KV_FKMU_0004</p3:codeSystem>
				</p3:cv>
			</xsl:when>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="prognos">
		<xsl:param name="param" />
		<xsl:choose>
			<xsl:when test="contains($param, 'Aterstallas_helt')">
				<p3:cv>
					<p3:code>1</p3:code>
					<p3:codeSystem>KV_FKMU_0006</p3:codeSystem>
				</p3:cv>
			</xsl:when>
			<xsl:when test="contains($param, 'Aterstallas_delvis')">
				<p3:cv>
					<p3:code>2</p3:code>
					<p3:codeSystem>KV_FKMU_0006</p3:codeSystem>
				</p3:cv>
			</xsl:when>
			<xsl:when test="contains($param, 'Inte_aterstallas')">
				<p3:cv>
					<p3:code>3</p3:code>
					<p3:codeSystem>KV_FKMU_0006</p3:codeSystem>
				</p3:cv>
			</xsl:when>
			<xsl:when test="contains($param, 'Det_gar_inte_att_bedomma')">
				<p3:cv>
					<p3:code>4</p3:code>
					<p3:codeSystem>KV_FKMU_0006</p3:codeSystem>
				</p3:cv>
			</xsl:when>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="diagnostext">
		<xsl:param name="beskrivning" />
		<xsl:param name="code" />
		<xsl:param name="codeSystem" />
		<p2:svar id="6">
			<p2:delsvar id="6.1">
				<xsl:value-of
					select="$beskrivning" />

			</p2:delsvar>
			<p2:delsvar id="6.2">
				<p3:cv>
					<p3:code><xsl:value-of
					select="$code"/></p3:code>
					<p3:codeSystem><xsl:value-of
					select="$codeSystem"/></p3:codeSystem>
				</p3:cv>
			</p2:delsvar>
		</p2:svar>
	</xsl:template>

	<xsl:template name="aktivitetsbegransningar">
		<xsl:param name="beskrivning" />
		<p2:svar id="17">
			<p2:delsvar id="17.1">
				<xsl:value-of select="$beskrivning"/>
			</p2:delsvar>
		</p2:svar>
	</xsl:template>

</xsl:stylesheet>

