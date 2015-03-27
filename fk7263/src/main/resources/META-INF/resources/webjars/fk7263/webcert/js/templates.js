angular.module('fk7263').run(['$templateCache', function($templateCache) {
  'use strict';

  $templateCache.put('/web/webjars/fk7263/webcert/js/directives/qaPanel.html',
    "<div>\n" +
    "  <div ng-if=\"qa.proxyMessage\">\n" +
    "    <div alert type=\"info\" close=\"dismissProxy(qa)\">\n" +
    "      <span message key=\"{{qa.proxyMessage}}\"></span>\n" +
    "    </div>\n" +
    "  </div>\n" +
    "\n" +
    "  <div ng-class=\"{'panel-container': true, 'revoked-certificate' : certProperties.isRevoked}\">\n" +
    "\n" +
    "    <div class=\"qa-panel\" id=\"qa{{panelId}}-{{qa.internReferens}}\" ng-show=\"!qa.proxyMessage\">\n" +
    "\n" +
    "      <div ng-if=\"handledPanel\">\n" +
    "        <h3 class=\"inline-block\">\n" +
    "          Ämne: <strong><span message key=\"qa.amne.{{qa.amne | lowercase}}\"></span></strong><span\n" +
    "            id=\"fkMeddelandeRubrik-{{qa.internReferens}}\" ng-show=\"qa.meddelandeRubrik\"> - {{qa.meddelandeRubrik}}</span>\n" +
    "        </h3>\n" +
    "      </div>\n" +
    "\n" +
    "      <div ng-if=\"!handledPanel\" class=\"row\">\n" +
    "        <div class=\"col-xs-9\">\n" +
    "          <div class=\"status-header\">\n" +
    "            <h3 class=\"inline-block\">\n" +
    "              Ämne: <strong><span message key=\"qa.amne.{{qa.amne | lowercase}}\"></span></strong><span\n" +
    "                id=\"fkMeddelandeRubrik-{{qa.internReferens}}\" ng-show=\"qa.meddelandeRubrik\"> - {{qa.meddelandeRubrik}}</span>\n" +
    "            </h3>\n" +
    "            <p>\n" +
    "              Åtgärd: <strong><span message key=\"qa.measure.{{qa.measureResKey}}\"></span></strong>\n" +
    "            </p>\n" +
    "            <div wc-feature-not-active feature=\"franJournalsystem\" class=\"form-inline\"\n" +
    "                 ng-show=\"!certProperties.isRevoked\">\n" +
    "              <label for=\"{{panelId}}-mark-as-forwarded-{{qa.internReferens}}\" class=\"checkbox-inline\">\n" +
    "                <input id=\"{{panelId}}-mark-as-forwarded-{{qa.internReferens}}\" type=\"checkbox\"\n" +
    "                       ng-disabled=\"qa.forwardInProgress\" ng-model=\"qa.vidarebefordrad\"\n" +
    "                       ng-change=\"onVidareBefordradChange(qa)\" /> Vidarebefordrad</label> <span\n" +
    "                ng-if=\"qa.forwardInProgress\"> <img src=\"/img/ajax-loader-kit-16x16.gif\"></span>\n" +
    "            </div>\n" +
    "          </div>\n" +
    "        </div>\n" +
    "\n" +
    "        <div wc-feature-not-active feature=\"franJournalsystem\" class=\"col-xs-3\">\n" +
    "          <!--  status header -->\n" +
    "          <button id=\"{{panelId}}-vidarebefordraEjHanterad\" class=\"btn pull-right\"\n" +
    "                  ng-class=\"{'btn-info': !qa.vidarebefordrad, 'btn-default btn-secondary' : qa.vidarebefordrad}\"\n" +
    "                  title=\"Skicka mejl med en länk till intyget för att informera den som ska hantera frågan-svaret.\"\n" +
    "                  ng-click=\"openMailDialog(qa)\" ng-show=\"!certProperties.isRevoked\">\n" +
    "            <img ng-if=\"!qa.vidarebefordrad\" src=\"/img/mail.png\"> <img ng-if=\"qa.vidarebefordrad\"\n" +
    "                                                                       src=\"/img/mail_dark.png\">\n" +
    "          </button>\n" +
    "        </div>\n" +
    "      </div>\n" +
    "\n" +
    "      <!-- Frågan -->\n" +
    "      <div class=\"panel-container\">\n" +
    "        <div class=\"qa-block\"\n" +
    "             ng-class=\"{'qa-block-handled' : !handledPanel && qa.status == 'ANSWERED', 'qa-block-fk' : (qa.frageStallare | lowercase) == 'fk', 'qa-block-wc' : (qa.frageStallare | lowercase) == 'wc'}\">\n" +
    "          <div class=\"qa-block-head clearfix\">\n" +
    "            <div class=\"pull-left\" ng-switch=\"qa.frageStallare | lowercase\">\n" +
    "              Från: <span class=\"qa-sender\" ng-switch-when=\"wc\"\n" +
    "                          id=\"{{panelId}}-question-fraga-vard-aktor-namn-{{qa.internReferens}}\">{{qa.vardAktorNamn}}</span>\n" +
    "              <span ng-switch-default message key=\"qa.fragestallare.{{qa.frageStallare | lowercase}}\"></span>\n" +
    "              <div id=\"{{panelId}}-fkKontakter-{{qa.internReferens}}\" ng-repeat=\"kontakt in qa.externaKontakter\">\n" +
    "                {{kontakt}}<br />\n" +
    "              </div>\n" +
    "            </div>\n" +
    "            <!-- Fraga fran fk eller wc? -->\n" +
    "            <div class=\"pull-right\" ng-switch=\"qa.frageStallare | lowercase\">\n" +
    "              <span ng-switch-when=\"wc\">Skickat: </span> <span ng-switch-default>Mottaget: </span> <span\n" +
    "                class=\"qa-date\" id=\"{{panelId}}-qa-skickaddatum-{{qa.internReferens}}\">{{qa.frageSkickadDatum | date:'short'}}</span>\n" +
    "            </div>\n" +
    "          </div>\n" +
    "\n" +
    "          <div class=\"qa-block-body\" id=\"{{panelId}}-qa-fragetext-{{qa.internReferens}}\">{{qa.frageText}}</div>\n" +
    "          <div class=\"qa-block-body komplettera-block\" id=\"{{panelId}}-fkKompletteringar-{{qa.internReferens}}\"\n" +
    "               ng-repeat=\"komplettering in qa.kompletteringar\">\n" +
    "            <strong><span>{{komplettering.falt}}</span></strong>\n" +
    "            <div>\n" +
    "              <span>{{komplettering.text}}</span>\n" +
    "            </div>\n" +
    "          </div>\n" +
    "        </div>\n" +
    "      </div>\n" +
    "\n" +
    "      <!-- Svarsdelen -->\n" +
    "\n" +
    "      <div class=\"panel-container\">\n" +
    "        <div class=\"qa-block\"\n" +
    "             ng-class=\"{'qa-block-handled': handledPanel, 'qa-block-fk' : (qa.frageStallare | lowercase) == 'wc', 'qa-block-wc' : (qa.frageStallare | lowercase) == 'fk'}\"\n" +
    "             ng-if=\"(handledPanel && qa.svarsText) || (!handledPanel && qa.status == 'ANSWERED')\">\n" +
    "\n" +
    "          <!-- UNHANDLEDTYPE: frågan är redan BESVARAD -->\n" +
    "          <!-- HANDLEDTYPE: frågan har en svarstext -->\n" +
    "          <div class=\"qa-block-head clearfix\">\n" +
    "            <div class=\"pull-left\" ng-switch=\"qa.frageStallare | lowercase\">\n" +
    "              <!--  Frågaställaren var Vårdenheten - svaret måste kommit från FK -->\n" +
    "              Från: <span ng-switch-when=\"wc\">\n" +
    "                          <span class=\"qa-sender\" message key=\"qa.fragestallare.fk\"></span>\n" +
    "                        </span>\n" +
    "              <!--  Frågaställaren var inte Vårdenheten - svaret måste alltså kommit från Vårdenheten -->\n" +
    "              <span ng-switch-default id=\"{{panelId}}-answer-svar-vard-aktor-namn-{{qa.internReferens}}\" class=\"qa-sender\">{{qa.vardAktorNamn}}</span>\n" +
    "            </div>\n" +
    "            <div class=\"pull-right\" ng-switch=\"qa.frageStallare | lowercase\">\n" +
    "              <!-- motsatt villkor mot frågan -->\n" +
    "              <span ng-switch-when=\"wc\">Mottaget: </span> <span ng-switch-default>Skickat: </span> <span\n" +
    "                class=\"qa-date\">{{qa.svarSkickadDatum | date:'short'}}</span>\n" +
    "            </div>\n" +
    "          </div>\n" +
    "          <div id=\"{{panelId}}-{{qa.internReferens}}-svarsText\" class=\"qa-block-body\">{{qa.svarsText}}</div>\n" +
    "        </div>\n" +
    "      </div>\n" +
    "\n" +
    "      <!-- KNAPPAR HANTERADE FRÅGOR -->\n" +
    "      <div ng-if=\"handledPanel\">\n" +
    "        <!-- error message occuring when inteacting with server for this FragaSvar-->\n" +
    "        <div id=\"{{panelId}}-{{qa.internReferens}}-svarsText-load-error\" ng-show=\"qa.activeErrorMessageKey\" class=\"alert alert-danger\">\n" +
    "          <span message key=\"fk7263.error.{{qa.activeErrorMessageKey}}\"></span>\n" +
    "        </div>\n" +
    "        <div class=\"form-group\">\n" +
    "          <!--  Oavsett hur frågan stänged skall man kunna 'öppna' den igen -->\n" +
    "          <div class=\"controls\" ng-hide=\"qa.frageStallare!='WC' && qa.svarsText\">\n" +
    "            <button class=\"btn btn-default btn-secondary\" ng-click=\"updateAsUnHandled(qa)\"\n" +
    "                    wc-check-vardenhet vardenhet=\"{{cert.grundData.skapadAv.vardenhet.enhetsid}}\"\n" +
    "                    id=\"markAsUnhandledBtn-{{qa.internReferens}}\" ng-disabled=\"qa.updateHandledStateInProgress\">\n" +
    "              <img src=\"/img/loader-small.gif\" ng-show=\"qa.updateHandledStateInProgress\"> Markera som ej hanterad\n" +
    "            </button>\n" +
    "          </div>\n" +
    "        </div>\n" +
    "      </div>\n" +
    "      <!-- KNAPPAR EJ HANTERADE FRÅGOR -->\n" +
    "      <div ng-if=\"!handledPanel\">\n" +
    "        <div class=\"form-group\" ng-if=\"qa.status == 'PENDING_INTERNAL_ACTION'\">\n" +
    "          <!--  VÄNTAR på svar från Vårdenheten, men skall kunna klarmarkeras ändå?-->\n" +
    "          <label class=\"control-label\" ng-show=\"!qa.answerDisabled && !certProperties.isRevoked\">Svar</label>\n" +
    "          <div class=\"controls\">\n" +
    "            <div class=\"alert alert-info\" ng-show=\"qa.answerDisabled && qa.answerDisabledReason\">\n" +
    "              {{qa.answerDisabledReason}}\n" +
    "            </div>\n" +
    "            <div class=\"form-group\">\n" +
    "              <textarea rows=\"5\" class=\"form-control col-md-9 qa-block-wc\" ng-model=\"qa.svarsText\"\n" +
    "                        id=\"answerText-{{qa.internReferens}}\"\n" +
    "                        ng-show=\"!qa.answerDisabled && !certProperties.isRevoked\"></textarea>\n" +
    "            </div>\n" +
    "            <div class=\"webcert-top-padding-section\">\n" +
    "              <!-- error message occuring when inteacting with server for this FragaSvar-->\n" +
    "              <div id=\"internal-{{qa.internReferens}}-svarsText-load-error\" ng-show=\"qa.activeErrorMessageKey\"\n" +
    "                   class=\"alert alert-danger\">\n" +
    "                <span message key=\"fk7263.error.{{qa.activeErrorMessageKey}}\"></span>\n" +
    "              </div>\n" +
    "              <button class=\"btn btn-success\" ng-click=\"sendAnswer(qa)\"\n" +
    "                      ng-disabled=\"!qa.svarsText || qa.updateInProgress\"\n" +
    "                      ng-show=\"!qa.answerDisabled && !certProperties.isRevoked\" wc-check-vardenhet\n" +
    "                      vardenhet=\"{{cert.grundData.skapadAv.vardenhet.enhetsid}}\"\n" +
    "                      id=\"sendAnswerBtn-{{qa.internReferens}}\">\n" +
    "                <img src=\"/img/loader-small-green.gif\" ng-show=\"qa.updateInProgress\"> Skicka svar\n" +
    "              </button>\n" +
    "              <button class=\"btn btn-default btn-secondary\" ng-click=\"updateAsHandled(qa)\"\n" +
    "                      id=\"markAsHandledFkOriginBtn-{{qa.internReferens}}\"\n" +
    "                      ng-disabled=\"qa.updateHandledStateInProgress\" wc-check-vardenhet\n" +
    "                      vardenhet=\"{{cert.grundData.skapadAv.vardenhet.enhetsid}}\"\n" +
    "                      title=\"När frågan-svaret markeras som hanterad kommer den att anses som avslutad. Frågan-svaret flyttas till ’Hanterade frågor och svar’ och visas inte längre i vårdenhetens översikt över ej hanterade frågor och svar.\">\n" +
    "                <img src=\"/img/loader-small.gif\" ng-show=\"qa.updateHandledStateInProgress\"> Markera som hanterad\n" +
    "              </button>\n" +
    "            </div>\n" +
    "          </div>\n" +
    "        </div>\n" +
    "        <div class=\"form-group\" ng-if=\"qa.status == 'PENDING_EXTERNAL_ACTION' || qa.status == 'ANSWERED'\">\n" +
    "          <!--  fått svar eller väntar på svar från FK, skall kunna klarmarkera manuellt ändå -->\n" +
    "          <!-- error message occuring when inteacting with server for this FragaSvar-->\n" +
    "          <div id=\"external-{{qa.internReferens}}-svarsText-load-error\" ng-show=\"qa.activeErrorMessageKey\"\n" +
    "               class=\"alert alert-danger\">\n" +
    "            <span message key=\"fk7263.error.{{qa.activeErrorMessageKey}}\"></span>\n" +
    "          </div>\n" +
    "          <div class=\"controls\" wc-check-vardenhet vardenhet=\"{{cert.grundData.skapadAv.vardenhet.enhetsid}}\">\n" +
    "            <button class=\"btn btn-default btn-secondary\" ng-click=\"updateAsHandled(qa)\"\n" +
    "                    id=\"markAsHandledWcOriginBtn-{{qa.internReferens}}\"\n" +
    "                    ng-disabled=\"qa.updateHandledStateInProgress\"\n" +
    "                    title=\"När frågan-svaret markeras som hanterad kommer den att anses som avslutad. Frågan-svaret flyttas till ’Hanterade frågor och svar’ och visas inte längre i vårdenhetens översikt över ej hanterade frågor och svar.\">\n" +
    "              <img src=\"/img/loader-small.gif\" ng-show=\"qa.updateHandledStateInProgress\"> Markera som hanterad\n" +
    "            </button>\n" +
    "          </div>\n" +
    "        </div>\n" +
    "      </div>\n" +
    "    </div>\n" +
    "\n" +
    "  </div>\n" +
    "\n" +
    "</div>\n"
  );


  $templateCache.put('/web/webjars/fk7263/webcert/js/intyg/edit/form/form1.html',
    "<div wc-field-single field-number=\"1\" field-help-text=\"fk7263.helptext.smittskydd\">\n" +
    "  <label for=\"smittskydd\" class=\"control-label title-single\">\n" +
    "    <input id=\"smittskydd\" name=\"smittskydd\" type=\"checkbox\" ng-model=\"model.avstangningSmittskydd\"\n" +
    "           wc-focus-on=\"firstInput\" ng-change=\"onSmittskyddChange()\">&nbsp;<span message key=\"fk7263.label.smittskydd\"></span>\n" +
    "  </label>\n" +
    "</div>"
  );


  $templateCache.put('/web/webjars/fk7263/webcert/js/intyg/edit/form/form10.html',
    "<div wc-field field-label=\"fk7263.label.prognos\" field-number=\"10\"\n" +
    "     field-help-text=\"fk7263.helptext.arbetsformaga.prognos\" id=\"prognosForm\">\n" +
    "\n" +
    "  <div class=\"alert alert-danger\" ng-if=\"viewState.common.validationMessagesGrouped.prognos\" id=\"validationMessages_prognos\">\n" +
    "    <ul>\n" +
    "      <li ng-repeat=\"message in viewState.common.validationMessagesGrouped.prognos\"><span message\n" +
    "                                                                         key=\"{{message.message}}\"></span>\n" +
    "      </li>\n" +
    "    </ul>\n" +
    "  </div>\n" +
    "\n" +
    "  <a class=\"soclink\" href=\"http://www.socialstyrelsen.se/riktlinjer/forsakringsmedicinsktbeslutsstod\"\n" +
    "     target=\"_blank\" title=\"Öppnar Socialstyrelsens beslutstöd för angiven huvuddiagnos\">Socialstyrelsens\n" +
    "    försäkringsmedicinska beslutstöd</a>\n" +
    "  <div class=\"form-group\">\n" +
    "    <label class=\"radio-inline\"><input type=\"radio\" name=\"capacityForWorkForecast\" value=\"YES\"\n" +
    "                                       ng-model=\"radioGroups.prognos\" ng-change=\"onPrognosChange()\"> Ja</label>\n" +
    "  </div>\n" +
    "\n" +
    "  <div class=\"form-group\">\n" +
    "    <label class=\"radio-inline\"><input type=\"radio\" id=\"capacityForWork2\"\n" +
    "                                       name=\"capacityForWorkForecast\" value=\"PARTLY\"\n" +
    "                                       ng-model=\"radioGroups.prognos\" ng-change=\"onPrognosChange()\"> Ja, delvis</label>\n" +
    "  </div>\n" +
    "\n" +
    "  <div class=\"form-group\">\n" +
    "    <label class=\"radio-inline\"><input type=\"radio\" id=\"capacityForWork3\"\n" +
    "                                       name=\"capacityForWorkForecast\" value=\"NO\"\n" +
    "                                       ng-model=\"radioGroups.prognos\" ng-change=\"onPrognosChange()\"> Nej</label>\n" +
    "  </div>\n" +
    "\n" +
    "  <div class=\"form-group\">\n" +
    "    <label class=\"radio-inline\"><input type=\"radio\" id=\"capacityForWork4\"\n" +
    "                                       name=\"capacityForWorkForecast\" value=\"UNKNOWN\"\n" +
    "                                       ng-model=\"radioGroups.prognos\" ng-change=\"onPrognosChange()\"> Går ej att bedöma</label>\n" +
    "    <div class=\"indent form-inline\" ng-show=\"showInteAttBedoma()\">\n" +
    "      Förtydligande: <input type=\"text\"\n" +
    "                            ng-model=\"model.arbetsformagaPrognosGarInteAttBedomaBeskrivning\"\n" +
    "                            id=\"capacityForWorkForecastText\" name=\"capacityForWork.forecastText\"\n" +
    "                            value=\"\" class='form-control otherInformation'\n" +
    "                            ng-change=\"viewState.limitOtherField('arbetsformagaPrognosGarInteAttBedomaBeskrivning')\"> <span\n" +
    "        id=\"prognosis-counter\">Tecken kvar:\n" +
    "                        {{viewState.inputLimits.ovrigt - viewState.getTotalOvrigtLength()}}</span>\n" +
    "      <div class=\"info-transfer\">Informationen överförs till \"Övriga upplysningar\" vid PDF-utskrift av\n" +
    "        blanketten.\n" +
    "      </div>\n" +
    "    </div>\n" +
    "  </div>\n" +
    "</div>"
  );


  $templateCache.put('/web/webjars/fk7263/webcert/js/intyg/edit/form/form12.html',
    "<div wc-field field-label=\"fk7263.label.fk-kontakt\" field-number=\"12\"\n" +
    "     field-help-text=\"fk7263.helptext.kontakt\">\n" +
    "  <label><input type=\"checkbox\" id=\"kontaktFk\" name=\"contactWithFkRequested\"\n" +
    "                ng-model=\"model.kontaktMedFk\"> Kontakt önskas med\n" +
    "    Försäkringskassan</label>\n" +
    "</div>"
  );


  $templateCache.put('/web/webjars/fk7263/webcert/js/intyg/edit/form/form13.html',
    "<div wc-field field-label=\"fk7263.label.ovrigt\" field-number=\"13\"\n" +
    "     field-help-text=\"fk7263.helptext.ovrigt\" class=\"print-pagebreak-after\">\n" +
    "                    <textarea id=\"otherInformation\" name=\"otherInformation\" rows='10'\n" +
    "                              class='form-control otherInformation' ng-model=\"model.kommentar\"\n" +
    "                              ng-change=\"viewState.limitOtherField('kommentar')\"></textarea>\n" +
    "  <div class=\"counter\">Tecken kvar: {{teckenKvar()}}</div>\n" +
    "</div>"
  );


  $templateCache.put('/web/webjars/fk7263/webcert/js/intyg/edit/form/form15.html',
    "<div wc-field field-label=\"fk7263.label.vardenhet.adress\" field-number=\"15\"\n" +
    "     field-help-text=\"fk7263.helptext.adress\" id=\"vardenhetForm\">\n" +
    "  <div class=\"alert alert-danger\" ng-if=\"viewState.common.validationMessagesGrouped.vardperson\" id=\"validationMessages_vardperson\">\n" +
    "    <ul>\n" +
    "      <li ng-repeat=\"message in viewState.common.validationMessagesGrouped.vardperson\"><span message\n" +
    "                                                                            key=\"{{message.message}}\"></span>\n" +
    "      </li>\n" +
    "    </ul>\n" +
    "  </div>\n" +
    "\n" +
    "  <label class=\"control-label\">{{model.grundData.skapadAv.vardenhet.enhetsnamn}}\n" +
    "    ({{model.grundData.skapadAv.vardenhet.enhetsid}}),\n" +
    "    {{model.grundData.skapadAv.vardenhet.vardgivare.vardgivarnamn}}</label>\n" +
    "\n" +
    "  <div class=\"alert alert-info\" ng-show=\"widgetState.hasInfoMissing\">\n" +
    "    Information om vårdenheten saknas i HSA-katalogen. Be den på vårdenheten som är ansvarig för\n" +
    "    frågor om HSA att uppdatera informationen.\n" +
    "  </div>\n" +
    "\n" +
    "  <div class=\"form-horizontal\">\n" +
    "    <div class=\"form-group\">\n" +
    "      <label class=\"col-xs-3 control-label\">Postadress</label>\n" +
    "      <div class=\"col-xs-9\">\n" +
    "        <input type=\"text\" class=\"form-control input-full\" id=\"clinicInfoPostalAddress\"\n" +
    "               name=\"clinicInfo.postalAddress\"\n" +
    "               ng-model=\"model.grundData.skapadAv.vardenhet.postadress\">\n" +
    "      </div>\n" +
    "    </div>\n" +
    "    <div class=\"form-group\">\n" +
    "      <label class=\"col-xs-3 control-label\">Postnummer</label>\n" +
    "      <div class=\"col-xs-9\">\n" +
    "        <input type=\"text\" class=\"form-control input-5\" id=\"clinicInfoPostalCode\"\n" +
    "               name=\"clinicInfo.postalCode\" ng-model=\"model.grundData.skapadAv.vardenhet.postnummer\">\n" +
    "      </div>\n" +
    "    </div>\n" +
    "    <div class=\"form-group\">\n" +
    "      <label class=\"col-xs-3 control-label\">Postort</label>\n" +
    "      <div class=\"col-xs-9\">\n" +
    "        <input type=\"text\" class=\"form-control\" id=\"clinicInfoPostalCity\" name=\"clinicInfo.postalCity\"\n" +
    "               ng-model=\"model.grundData.skapadAv.vardenhet.postort\">\n" +
    "      </div>\n" +
    "    </div>\n" +
    "    <div class=\"form-group\">\n" +
    "      <label class=\"col-xs-3 control-label\">Telefonnummer</label>\n" +
    "      <div class=\"col-xs-9\">\n" +
    "        <input type=\"text\" class=\"form-control\" id=\"clinicInfoPhone\" name=\"clinicInfo.phone\"\n" +
    "               ng-model=\"model.grundData.skapadAv.vardenhet.telefonnummer\" >\n" +
    "      </div>\n" +
    "    </div>\n" +
    "  </div>\n" +
    "</div>"
  );


  $templateCache.put('/web/webjars/fk7263/webcert/js/intyg/edit/form/form2.html',
    "<div ng-form=\"form2\">\n" +
    "<div wc-field field-label=\"fk7263.label.diagnos\" field-number=\"2\"\n" +
    "     field-help-text=\"fk7263.helptext.diagnos\" ng-hide=\"viewState.avstangningSmittskydd()\"\n" +
    "     ng-animate=\"'fade'\" class=\"print-pagebreak-after\" id=\"diagnoseForm\">\n" +
    "\n" +
    "  <div class=\"alert alert-danger\" ng-if=\"viewState.common.validationMessagesGrouped.diagnos\" id=\"validationMessages_diagnos\">\n" +
    "    <ul>\n" +
    "      <li ng-repeat=\"message in viewState.common.validationMessagesGrouped.diagnos\"><span message\n" +
    "                                                                         key=\"{{message.message}}\"></span>\n" +
    "      </li>\n" +
    "    </ul>\n" +
    "  </div>\n" +
    "  <div class=\"row bottom-padding-small\">\n" +
    "    <div class='col-xs-12'>\n" +
    "      <div class='divider'>\n" +
    "        <span message key=\"fk7263.label.valjkodverk\"></span>\n" +
    "        <label class=\"radio-inline\">\n" +
    "          <input type=\"radio\" id=\"diagnoseKodverk_ICD_10_SE\" name=\"diagnosKodverk\" value=\"ICD_10_SE\"\n" +
    "                 ng-model=\"model.diagnosKodverk\" checked=\"checked\" ng-change=\"onChangeKodverk()\"><span message key=\"fk7263.label.diagnoskodverk.icd_10_se\"></span>\n" +
    "        </label>\n" +
    "        <label class=\"radio-inline\">\n" +
    "          <input type=\"radio\" id=\"diagnoseKodverk_KSH_97_P\" name=\"diagnosKodverk\" value=\"KSH_97_P\"\n" +
    "                 ng-model=\"model.diagnosKodverk\" ng-change=\"onChangeKodverk()\"><span message key=\"fk7263.label.diagnoskodverk.ksh_97_p\"></span>\n" +
    "        </label>\n" +
    "      </div>\n" +
    "    </div>\n" +
    "  </div>\n" +
    "  <div class=\"row bottom-padding-small\">\n" +
    "    <div class='col-xs-3'>\n" +
    "      <label class=\"control-label unbreakable\">ICD-10</label>\n" +
    "      <input type=\"text\" class=\"form-control\" id=\"diagnoseCode\" name=\"diagnose.code\" value=\"\"\n" +
    "             class='diagnose-code col-xs-11 form-control' ng-model=\"model.diagnosKod\"\n" +
    "             typeahead=\"d.value as d.label for d in getDiagnoseCodes(model.diagnosKodverk, $viewValue) | filter:$viewValue | limitTo:10\"\n" +
    "             typeahead-min-length=\"3\" typeahead-on-select=\"onDiagnoseCode1Select($item)\"\n" +
    "             wc-to-uppercase\n" +
    "          >\n" +
    "    </div>\n" +
    "    <div class='col-xs-9'>\n" +
    "      <label class=\"control-label\">Diagnos</label>\n" +
    "      <input type=\"text\" class=\"form-control maxwidth\" id=\"diagnoseDescription\"\n" +
    "             class=\"col-xs-12 form-control\" name=\"diagnose.description\" value=\"\"\n" +
    "             ng-model=\"model.diagnosBeskrivning1\"\n" +
    "             typeahead=\"d.value as d.label for d in searchDiagnoseByDescription(model.diagnosKodverk, $viewValue) | filter:$viewValue \"\n" +
    "             typeahead-on-select=\"onDiagnoseDescription1Select($item)\"\n" +
    "             typeahead-wait-ms=\"100\" ng-change=\"limitDiagnosBeskrivningField('diagnosBeskrivning1')\">\n" +
    "      <span class=\"counter\">Tecken kvar: {{viewState.inputLimits.diagnosBeskrivning - getTotalDiagnosBeskrivningLength()}}</span>\n" +
    "    </div>\n" +
    "  </div>\n" +
    "  <div class=\"row bottom-padding-tiny\">\n" +
    "    <div class='col-xs-3'>\n" +
    "      <label class=\"control-label unbreakable\">ICD-10</label>\n" +
    "      <div class=\"controls\">\n" +
    "        <div class=\"bottom-padding-small clearfix\">\n" +
    "          <input type=\"text\" id=\"diagnoseCodeOpt1\" name=\"diagnose.codeOpt1\" value=\"\"\n" +
    "                 ng-model=\"model.diagnosKod2\" class=\"col-xs-11 form-control\"\n" +
    "                 typeahead=\"d.value as d.label for d in getDiagnoseCodes(model.diagnosKodverk, $viewValue) | filter:$viewValue | limitTo:10\"\n" +
    "                 typeahead-min-length=\"3\" typeahead-on-select=\"onDiagnoseCode2Select($item)\"\n" +
    "                 ng-change=\"limitDiagnosBeskrivningField('diagnosKod2')\" wc-to-uppercase>\n" +
    "        </div>\n" +
    "      </div>\n" +
    "    </div>\n" +
    "    <div class='col-xs-9'>\n" +
    "      <label class=\"control-label inline-block\"><span message\n" +
    "                                                      key=\"fk7263.label.ytterligarediagnoser\"></span>\n" +
    "        (frivilligt)</label><span wc-enable-tooltip class=\"absolute\"\n" +
    "                                  field-help-text=\"fk7263.helptext.diagnos.ytterligare\"></span>\n" +
    "      <div class=\"bottom-padding-small clearfix\">\n" +
    "        <input type=\"text\" id=\"diagnoseDescriptionOpt1\" name=\"diagnose.descriptionOpt1\" value=\"\"\n" +
    "               ng-model=\"model.diagnosBeskrivning2\" class=\"block col-xs-12 form-control maxwidth\"\n" +
    "               typeahead=\"d.value as d.label for d in searchDiagnoseByDescription(model.diagnosKodverk, $viewValue) | filter:$viewValue \"\n" +
    "               typeahead-on-select=\"onDiagnoseDescription2Select($item)\"\n" +
    "               typeahead-wait-ms=\"100\" ng-change=\"limitDiagnosBeskrivningField('diagnosBeskrivning2')\">\n" +
    "        <span class=\"counter\">Tecken kvar: {{viewState.inputLimits.diagnosBeskrivning - getTotalDiagnosBeskrivningLength()}}</span>\n" +
    "      </div>\n" +
    "    </div>\n" +
    "  </div>\n" +
    "  <div class=\"row bottom-padding-small\">\n" +
    "    <div class='col-xs-3'>\n" +
    "      <input type=\"text\" id=\"diagnoseCodeOpt2\" name=\"diagnose.codeOpt2\" value=\"\"\n" +
    "             ng-model=\"model.diagnosKod3\" class=\"col-xs-11 form-control\"\n" +
    "             typeahead=\"d.value as d.label for d in getDiagnoseCodes(model.diagnosKodverk, $viewValue) | filter:$viewValue | limitTo:10\"\n" +
    "             typeahead-min-length=\"3\" typeahead-on-select=\"onDiagnoseCode3Select($item)\"\n" +
    "             ng-change=\"limitDiagnosBeskrivningField('diagnosKod3')\" wc-to-uppercase>\n" +
    "    </div>\n" +
    "    <div class='col-xs-9'>\n" +
    "      <div class=\"controls\">\n" +
    "        <input type=\"text\" id=\"diagnoseDescriptionOpt2\" name=\"diagnose.descriptionOpt2\" value=\"\"\n" +
    "               ng-model=\"model.diagnosBeskrivning3\" class=\"block col-xs-12 form-control maxwidth\"\n" +
    "               typeahead=\"d.value as d.label for d in searchDiagnoseByDescription(model.diagnosKodverk, $viewValue) | filter:$viewValue \"\n" +
    "               typeahead-on-select=\"onDiagnoseDescription3Select($item)\"\n" +
    "               typeahead-wait-ms=\"100\"\n" +
    "               ng-change=\"limitDiagnosBeskrivningField('diagnosBeskrivning3')\">\n" +
    "        <span class=\"counter\">Tecken kvar: {{viewState.inputLimits.diagnosBeskrivning - getTotalDiagnosBeskrivningLength()}}</span>\n" +
    "      </div>\n" +
    "    </div>\n" +
    "  </div>\n" +
    "\n" +
    "  <div class=\"form-group\">\n" +
    "    <div>\n" +
    "      <label class=\"control-label\"><span message key=\"fk7263.label.diagnosfortydligande\"></span></label>\n" +
    "      <span wc-enable-tooltip field-help-text=\"fk7263.helptext.diagnos.fortydligande\"></span>\n" +
    "    </div>\n" +
    "                      <textarea id=\"diagnoseClarification\" class=\"form-control\" name=\"diagnose.clarification\" rows='5'\n" +
    "                                ng-model=\"model.diagnosBeskrivning\" ng-trim=\"false\"\n" +
    "                                ng-change=\"limitDiagnosBeskrivningField('diagnosBeskrivning')\"></textarea>\n" +
    "    <span class=\"counter\">Tecken kvar: {{viewState.inputLimits.diagnosBeskrivning - getTotalDiagnosBeskrivningLength()}}</span>\n" +
    "  </div>\n" +
    "\n" +
    "  <div class=\"form-group form-inline\">\n" +
    "    <label class=\"checkbox\">\n" +
    "      <input type=\"checkbox\" id=\"diagnoseMultipleDiagnoses\" name=\"diagnose.multipleDiagnoses\"\n" +
    "             ng-model=\"model.samsjuklighet\"\n" +
    "             ng-change=\"limitDiagnosBeskrivningField('samsjuklighet')\">\n" +
    "      Samsjuklighet föreligger</label><span wc-enable-tooltip\n" +
    "                                            field-help-text=\"fk7263.helptext.diagnos.samsjuklighet\"></span>\n" +
    "  </div>\n" +
    "</div>\n" +
    "\n" +
    "</div>"
  );


  $templateCache.put('/web/webjars/fk7263/webcert/js/intyg/edit/form/form3.html',
    "<div wc-field field-label=\"fk7263.label.aktuellt-sjukdomsforlopp\" field-number=\"3\"\n" +
    "     field-help-text=\"fk7263.helptext.aktuellt-sjukdomsforlopp\" ng-hide=\"viewState.avstangningSmittskydd()\"\n" +
    "     ng-animate=\"fade\">\n" +
    "                    <textarea id=\"diseaseCause\" class=\"form-control\" name=\"diseaseCause\" rows='5'\n" +
    "                              ng-model=\"model.sjukdomsforlopp\"\n" +
    "                              wc-maxlength ng-trim=\"false\"\n" +
    "                              maxlength=\"{{viewState.inputLimits.sjukdomsforlopp}}\"></textarea>\n" +
    "\n" +
    "</div>"
  );


  $templateCache.put('/web/webjars/fk7263/webcert/js/intyg/edit/form/form4.html',
    "<div wc-field field-label=\"fk7263.label.funktionsnedsattningar\" field-number=\"4\"\n" +
    "     field-help-text=\"fk7263.helptext.funktionsnedsattning\" ng-hide=\"viewState.avstangningSmittskydd()\"\n" +
    "     ng-animate=\"'fade'\">\n" +
    "  <div class=\"alert alert-danger\" ng-if=\"viewState.common.validationMessagesGrouped.funktionsnedsattning\" id=\"validationMessages_funktionsnedsattning\">\n" +
    "    <ul>\n" +
    "      <li ng-repeat=\"message in viewState.common.validationMessagesGrouped.funktionsnedsattning\"><span message\n" +
    "                                                                                      key=\"{{message.message}}\"></span>\n" +
    "      </li>\n" +
    "    </ul>\n" +
    "  </div>\n" +
    "\n" +
    "  <a class=\"soclink\" href=\"http://www.socialstyrelsen.se/riktlinjer/forsakringsmedicinsktbeslutsstod\"\n" +
    "     target=\"_blank\" title=\"Öppnar Socialstyrelsens beslutstöd för angiven huvuddiagnos\">Socialstyrelsens\n" +
    "    försäkringsmedicinska beslutstöd</a>\n" +
    "                    <textarea id=\"disabilities\" class=\"form-control\" name=\"disabilities\" rows='6'\n" +
    "                              ng-model=\"model.funktionsnedsattning\" wc-maxlength ng-trim=\"false\"\n" +
    "                              maxlength=\"{{viewState.inputLimits.funktionsnedsattning}}\"></textarea>\n" +
    "</div>"
  );


  $templateCache.put('/web/webjars/fk7263/webcert/js/intyg/edit/form/form4b.html',
    "<div ng-form=\"form4b\">\n" +
    "<div id=\"intygetbaseraspa\" wc-field field-label=\"fk7263.label.intyg-baserat-pa\" field-number=\"4b\"\n" +
    "                       field-help-text=\"fk7263.helptext.intyg-baserat-pa\" ng-hide=\"viewState.avstangningSmittskydd()\"\n" +
    "                       ng-animate=\"'fade'\">\n" +
    "  <div class=\"alert alert-danger\" ng-if=\"viewState.common.validationMessagesGrouped.intygbaseratpa\" id=\"validationMessages_intygbaseratpa\">\n" +
    "    <ul>\n" +
    "      <li ng-repeat=\"message in viewState.common.validationMessagesGrouped.intygbaseratpa\"><span message\n" +
    "                                                                                key=\"{{message.message}}\"></span>\n" +
    "      </li>\n" +
    "    </ul>\n" +
    "  </div>\n" +
    "\n" +
    "  <!-- Undersökning -->\n" +
    "  <div class=\"form-group form-inline\">\n" +
    "    <label class=\"checkbox inline-block\" style=\"min-width: 250px\"><input type=\"checkbox\"\n" +
    "                                                                         id=\"basedOnExamination\"\n" +
    "                                                                         name=\"informationBasedOn.examination\"\n" +
    "                                                                         class=\"checkbox-inline\"\n" +
    "                                                                         ng-model=\"basedOnState.check.undersokningAvPatienten\"\n" +
    "                                                                         ng-change=\"toggleBaseradPaDate('undersokningAvPatienten')\"\n" +
    "        value=\"true\">\n" +
    "      Min undersökning av patienten</label>\n" +
    "    <span wc-date-picker-field target-model=\"dates.undersokningAvPatienten\"\n" +
    "          dom-id=\"undersokningAvPatientenDate\"></span>\n" +
    "  </div>\n" +
    "\n" +
    "  <!-- Telefonkontakt -->\n" +
    "  <div class=\"form-group form-inline\">\n" +
    "    <label class=\"checkbox inline-block\" style=\"min-width: 250px\"><input type=\"checkbox\"\n" +
    "                                                                         id=\"basedOnPhoneContact\"\n" +
    "                                                                         name=\"informationBasedOn.phoneContact\"\n" +
    "                                                                         ng-model=\"basedOnState.check.telefonkontaktMedPatienten\"\n" +
    "                                                                         ng-change=\"toggleBaseradPaDate('telefonkontaktMedPatienten')\">\n" +
    "      Min telefonkontakt med patienten</label>\n" +
    "    <span wc-date-picker-field target-model=\"dates.telefonkontaktMedPatienten\"\n" +
    "          dom-id=\"telefonkontaktMedPatientenDate\"></span>\n" +
    "  </div>\n" +
    "\n" +
    "  <!-- Journaluppgifter -->\n" +
    "  <div class=\"form-group form-inline\">\n" +
    "    <label class=\"checkbox inline-block\" style=\"min-width: 250px\"><input type=\"checkbox\"\n" +
    "                                                                         id=\"basedOnJournal\"\n" +
    "                                                                         name=\"informationBasedOn.journal\"\n" +
    "                                                                         ng-model=\"basedOnState.check.journaluppgifter\"\n" +
    "                                                                         ng-change=\"toggleBaseradPaDate('journaluppgifter')\">\n" +
    "      Journaluppgifter</label>\n" +
    "    <span wc-date-picker-field target-model=\"dates.journaluppgifter\"\n" +
    "          dom-id=\"journaluppgifterDate\"></span>\n" +
    "  </div>\n" +
    "\n" +
    "  <!-- Annat -->\n" +
    "  <div class=\"form-group form-inline\">\n" +
    "    <label class=\"checkbox inline-block\" style=\"min-width: 250px\"><input type=\"checkbox\"\n" +
    "                                                                         id=\"basedOnOther\"\n" +
    "                                                                         name=\"informationBasedOn.other\"\n" +
    "                                                                         ng-model=\"basedOnState.check.annanReferens\"\n" +
    "                                                                         ng-change=\"toggleBaseradPaDate('annanReferens')\">\n" +
    "      Annat<span wc-enable-tooltip field-help-text=\"fk7263.helptext.intyg-baserat-pa.annat\"></span>\n" +
    "    </label>\n" +
    "    <span wc-date-picker-field target-model=\"dates.annanReferens\" dom-id=\"annanReferensDate\"></span>\n" +
    "  </div>\n" +
    "  <div class=\"otherText indent\" ng-if=\"basedOnState.check.annanReferens\">\n" +
    "    <span>Ange vad:</span><br>\n" +
    "    <textarea id=\"informationBasedOnOtherText\" name=\"informationBasedOn.otherText\" rows='2'\n" +
    "              class='form-control otherInformation' ng-model=\"model.annanReferensBeskrivning\"\n" +
    "              ng-change=\"viewState.limitOtherField('annanReferensBeskrivning')\"></textarea>\n" +
    "    <div class=\"counter\">\n" +
    "      Tecken kvar: {{viewState.inputLimits.ovrigt - viewState.getTotalOvrigtLength()}}\n" +
    "    </div>\n" +
    "    <div class=\"info-transfer\">Informationen överförs till \"Övriga upplysningar\" vid PDF-utskrift av\n" +
    "      blanketten.\n" +
    "    </div>\n" +
    "  </div>\n" +
    "</div>\n" +
    "</div>"
  );


  $templateCache.put('/web/webjars/fk7263/webcert/js/intyg/edit/form/form5.html',
    "<div wc-field field-label=\"fk7263.label.aktivitetsbegransning\" field-number=\"5\"\n" +
    "     field-help-text=\"fk7263.helptext.aktivitetsbegransning\" ng-hide=\"viewState.avstangningSmittskydd()\"\n" +
    "     ng-animate=\"'fade'\" class=\"print-pagebreak-after\">\n" +
    "\n" +
    "  <div class=\"alert alert-danger\" ng-if=\"viewState.common.validationMessagesGrouped.aktivitetsbegransning\" id=\"validationMessages_aktivitetsbegransning\">\n" +
    "    <ul>\n" +
    "      <li ng-repeat=\"message in viewState.common.validationMessagesGrouped.aktivitetsbegransning\">\n" +
    "        <span message key=\"{{message.message}}\"></span>\n" +
    "      </li>\n" +
    "    </ul>\n" +
    "  </div>\n" +
    "\n" +
    "  <a class=\"soclink\" href=\"http://www.socialstyrelsen.se/riktlinjer/forsakringsmedicinsktbeslutsstod\"\n" +
    "     target=\"_blank\" title=\"Öppnar Socialstyrelsens beslutstöd för angiven huvuddiagnos\">Socialstyrelsens\n" +
    "    försäkringsmedicinska beslutstöd</a>\n" +
    "                    <textarea id=\"activityLimitation\" class=\"form-control\" name=\"activityLimitation\" rows='10'\n" +
    "                              ng-model=\"model.aktivitetsbegransning\" wc-maxlength ng-trim=\"false\"\n" +
    "                              maxlength=\"{{viewState.inputLimits.aktivitetsbegransning}}\"></textarea>\n" +
    "</div>"
  );


  $templateCache.put('/web/webjars/fk7263/webcert/js/intyg/edit/form/form6a-7-11.html',
    "  <div wc-field field-label=\"fk7263.label.rekommendationer\" id=\"rekommendationerForm\" field-number=\"6a, 7, 11\"\n" +
    "       field-help-text=\"fk7263.helptext.rekommendationer\">\n" +
    "\n" +
    "    <div class=\"alert alert-danger\" ng-if=\"viewState.common.validationMessagesGrouped.rekommendationer\"\n" +
    "         id=\"validationMessages_rekommendationer\">\n" +
    "      <ul>\n" +
    "        <li ng-repeat=\"message in viewState.common.validationMessagesGrouped.rekommendationer\"><span message\n" +
    "                                                                                                               key=\"{{message.message}}\"></span>\n" +
    "        </li>\n" +
    "      </ul>\n" +
    "    </div>\n" +
    "\n" +
    "    <!-- 11 -->\n" +
    "    <div>Resor till och från arbetet med annat färdmedel än normalt kan göra det möjligt för patienten\n" +
    "      att återgå i arbete</label> <span wc-enable-tooltip\n" +
    "                                        field-help-text=\"fk7263.helptext.rekommendationer.resor\"></span>\n" +
    "    </div>\n" +
    "\n" +
    "    <div class=\"form-group form-inline\" id=\"rekommendationRessattRadioGroup\">\n" +
    "      <label class=\"radio-inline\">\n" +
    "        <input type=\"radio\" id=\"rekommendationRessatt\" name=\"recommendationsToFkTravel\" value=\"JA\"\n" +
    "               ng-model=\"radioGroups.ressattTillArbete\" ng-change=\"onTravelChange()\"> Ja</label>\n" +
    "      <label class=\"radio-inline\">\n" +
    "        <input type=\"radio\" id=\"rekommendationRessattEj\" name=\"recommendationsToFkTravel\" value=\"NEJ\"\n" +
    "               ng-model=\"radioGroups.ressattTillArbete\" ng-change=\"onTravelChange()\"> Nej</label>\n" +
    "    </div>\n" +
    "\n" +
    "    <!-- 6a, 7 -->\n" +
    "    <div ng-hide=\"viewState.avstangningSmittskydd()\">\n" +
    "\n" +
    "      <!-- 6a -->\n" +
    "      <div class=\"form-group form-inline\">\n" +
    "        <label class=\"checkbox\"><input type=\"checkbox\" id=\"rekommendationKontaktAf\" name=\"recommendationsToFk.contactAf\"\n" +
    "                                       ng-model=\"model.rekommendationKontaktArbetsformedlingen\">\n" +
    "          Patienten\n" +
    "          bör komma i kontakt med arbetsförmedlingen</label> <span wc-enable-tooltip\n" +
    "                                                                   field-help-text=\"fk7263.helptext.rekommendationer.arbetsformedlingen\"></span>\n" +
    "      </div>\n" +
    "      <div class=\"form-group form-inline\">\n" +
    "        <label class=\"checkbox\"><input type=\"checkbox\" id=\"rekommendationKontaktForetagshalsovard\"\n" +
    "                                       name=\"recommendationsToFk.contactCompanyCare\"\n" +
    "                                       ng-model=\"model.rekommendationKontaktForetagshalsovarden\">\n" +
    "          Patienten\n" +
    "          bör komma i kontakt med företagshälsovården</label> <span wc-enable-tooltip\n" +
    "                                                                    field-help-text=\"fk7263.helptext.rekommendationer.foretagshalsovarden\"></span>\n" +
    "      </div>\n" +
    "      <div class=\"form-group form-inline\">\n" +
    "        <label class=\"checkbox\"><input type=\"checkbox\" id=\"rekommendationOvrigt\" name=\"recommendationsToFk.other\"\n" +
    "                                       ng-model=\"model.rekommendationOvrigtCheck\"> Övrigt (ange\n" +
    "          vad)</label><span wc-enable-tooltip field-help-text=\"fk7263.helptext.rekommendationer.ovrigt\"></span>\n" +
    "        <div class=\"indent\" ng-if=\"model.rekommendationOvrigtCheck\">\n" +
    "          <input type=\"text\" id=\"rekommendationOvrigtBeskrivning\" name=\"recommendationsToFk.otherText\" value=\"\"\n" +
    "                 class='form-control maxwidth' ng-model=\"model.rekommendationOvrigt\" maxlength=\"80\" wc-maxlength>\n" +
    "\n" +
    "        </div>\n" +
    "      </div>\n" +
    "\n" +
    "      <!-- 7 -->\n" +
    "    <span id=\"recommendationsToFkReabInQuestionRadioGroup\">\n" +
    "      <h4 class=\"inline-block\">Är arbetslivsinriktad rehabilitering aktuell?</h4><span wc-enable-tooltip\n" +
    "                                                                                       field-help-text=\"fk7263.helptext.rekommendationer.arbetslivsinriktad-rehabilitering\"></span>\n" +
    "\n" +
    "      <!-- Rehab -->\n" +
    "      <!-- YES -->\n" +
    "      <div class=\"form-group\">\n" +
    "        <label class=\"radio-inline\"><input type=\"radio\" name=\"recommendationsToFkReabInQuestion\"\n" +
    "                                           value=\"JA\"\n" +
    "                                           ng-model=\"radioGroups.rehab\" id=\"rehabYes\" ng-change=\"onRehabChange()\"> Ja</label>\n" +
    "      </div>\n" +
    "\n" +
    "      <!-- NO -->\n" +
    "      <div class=\"form-group\">\n" +
    "        <label class=\"radio-inline\"><input type=\"radio\" name=\"recommendationsToFkReabInQuestion\" group=\"group3\"\n" +
    "                                           value=\"NEJ\" ng-model=\"radioGroups.rehab\" id=\"rehabNo\" ng-change=\"onRehabChange()\">\n" +
    "          Nej</label>\n" +
    "      </div>\n" +
    "\n" +
    "      <!-- GÅR EJ ATT BEDÖMA -->\n" +
    "      <div class=\"form-group\">\n" +
    "        <label class=\"radio-inline\"><input type=\"radio\" name=\"recommendationsToFkReabInQuestion\" group=\"group3\"\n" +
    "                                           value=\"GAREJ\" ng-model=\"radioGroups.rehab\" id=\"garej\" ng-change=\"onRehabChange()\"> Går inte\n" +
    "          att bedöma</label>\n" +
    "      </div>\n" +
    "    </span>\n" +
    "    </div>\n" +
    "  </div>"
  );


  $templateCache.put('/web/webjars/fk7263/webcert/js/intyg/edit/form/form6b.html',
    "<div wc-field field-label=\"fk7263.label.atgarder\" field-number=\"6b\"\n" +
    "     ng-hide=\"viewState.avstangningSmittskydd()\" field-help-text=\"fk7263.helptext.atgarder\"\n" +
    "     class=\"print-pagebreak-after\">\n" +
    "  <div class=\"form-group\">\n" +
    "    <label class=\"control-label\">Planerad eller pågående behandling eller åtgärd inom sjukvården (ange\n" +
    "      vilken)</label>\n" +
    "                      <textarea id=\"measuresCurrent\" class=\"form-control\" name=\"measures.current\" rows='2'\n" +
    "                                ng-model=\"model.atgardInomSjukvarden\" wc-maxlength ng-trim=\"false\"\n" +
    "                                maxlength=\"{{viewState.inputLimits.atgardInomSjukvarden}}\"></textarea>\n" +
    "  </div>\n" +
    "\n" +
    "  <div class=\"form-group\">\n" +
    "    <label class=\"control-label\">Annan åtgärd (ange vilken)</label>\n" +
    "                      <textarea id=\"measuresOther\" class=\"form-control\" name=\"measures.other\" rows='2'\n" +
    "                                ng-model=\"model.annanAtgard\" wc-maxlength ng-trim=\"false\"\n" +
    "                                maxlength=\"{{viewState.inputLimits.annanAtgard}}\"></textarea>\n" +
    "  </div>\n" +
    "</div>"
  );


  $templateCache.put('/web/webjars/fk7263/webcert/js/intyg/edit/form/form8a.html',
    "<div wc-field field-label=\"fk7263.label.arbete\" field-number=\"8a\"\n" +
    "     field-help-text=\"fk7263.helptext.sysselsattning\" ng-hide=\"viewState.avstangningSmittskydd()\"\n" +
    "     ng-animate=\"'fade'\" id=\"arbeteForm\">\n" +
    "  <div class=\"alert alert-danger\" ng-if=\"viewState.common.validationMessagesGrouped.sysselsattning\" id=\"validationMessages_sysselsattning\">\n" +
    "    <ul>\n" +
    "      <li ng-repeat=\"message in viewState.common.validationMessagesGrouped.sysselsattning\"><span message\n" +
    "                                                                                key=\"{{message.message}}\"></span>\n" +
    "      </li>\n" +
    "    </ul>\n" +
    "  </div>\n" +
    "\n" +
    "  <div class=\"form-group form-inline\">\n" +
    "    <label class=\"control-label\"><span message key=\"fk7263.label.arbetsformaga\"></span></label>\n" +
    "    <div class=\"controls form-inline\">\n" +
    "      <label class=\"checkbox\"><input type=\"checkbox\" id=\"arbeteNuvarande\"\n" +
    "                                     ng-model=\"model.nuvarandeArbete\"> Nuvarande\n" +
    "        arbete</label><span wc-enable-tooltip\n" +
    "                            field-help-text=\"fk7263.helptext.sysselsattning.nuvarande\"></span>\n" +
    "      <div class=\"indent\" ng-if=\"model.nuvarandeArbete\">\n" +
    "        <label for=\"currentWork\">Ange aktuella arbetsuppgifter</label>\n" +
    "        <div class=\"controls\">\n" +
    "                            <textarea id=\"currentWork\" class=\"\" name=\"currentWork\" rows='2'\n" +
    "                                      ng-model=\"model.nuvarandeArbetsuppgifter\" wc-maxlength ng-trim=\"false\"\n" +
    "                                      maxlength=\"{{viewState.inputLimits.nuvarandeArbetsuppgifter}}\"></textarea>\n" +
    "        </div>\n" +
    "      </div>\n" +
    "    </div>\n" +
    "  </div>\n" +
    "  <div class=\"form-group form-inline\">\n" +
    "    <label class=\"checkbox\"><input type=\"checkbox\" id=\"arbeteArbetslos\" ng-model=\"model.arbetsloshet\">\n" +
    "      Arbetslöshet - att utföra arbete på den\n" +
    "      reguljära arbetsmarknaden</label><span wc-enable-tooltip\n" +
    "                                             field-help-text=\"fk7263.helptext.sysselsattning.arbetsloshet\"></span>\n" +
    "  </div>\n" +
    "  <div class=\"form-group form-inline\">\n" +
    "    <label class=\"checkbox\"><input type=\"checkbox\" id=\"arbeteForaldraledig\"\n" +
    "                                   ng-model=\"model.foraldrarledighet\">\n" +
    "      Föräldraledighet med föräldrapenning -\n" +
    "      att vårda sitt barn</label><span wc-enable-tooltip\n" +
    "                                       field-help-text=\"fk7263.helptext.sysselsattning.foraldrarledighet\"></span>\n" +
    "  </div>\n" +
    "</div>"
  );


  $templateCache.put('/web/webjars/fk7263/webcert/js/intyg/edit/form/form8b.html',
    "<div ng-form=\"form8b\">\n" +
    "  <div wc-field field-label=\"fk7263.label.nedsattning\" field-number=\"8b\" field-help-text=\"fk7263.helptext.arbetsformaga\"\n" +
    "       class=\"arbetsformaga print-pagebreak-after\" id=\"arbetsformagaForm\">\n" +
    "    <div class=\"alert alert-danger\" ng-if=\"viewState.common.validationMessagesGrouped.arbetsformaga\"\n" +
    "         id=\"validationMessages_arbetsformaga\">\n" +
    "      <ul>\n" +
    "        <li ng-repeat=\"message in viewState.common.validationMessagesGrouped.arbetsformaga\"><span message\n" +
    "                                                                                                            key=\"{{message.message}}\"></span>\n" +
    "        </li>\n" +
    "      </ul>\n" +
    "    </div>\n" +
    "\n" +
    "    <div class=\"form-group form-inline\">\n" +
    "      Faktisk tjänstgöringstid <input style=\"width: 3em;\" type=\"text\" wc-decimal-number\n" +
    "                                      id=\"capacityForWorkActualWorkingHoursPerWeek\"\n" +
    "                                      name=\"capacityForWork.actualWorkingHoursPerWeek\" value=\"\"\n" +
    "                                      class='short control-element form-control' maxlength=\"5\"\n" +
    "                                      ng-model=\"model.tjanstgoringstid\"> <span>timmar/vecka</span> <span\n" +
    "        wc-enable-tooltip field-help-text=\"fk7263.helptext.arbetsformaga.faktisk-tjanstgoringstid\"></span>\n" +
    "    </div>\n" +
    "\n" +
    "    <!-- 25% -->\n" +
    "    <div class=\"form-group form-inline\">\n" +
    "      <label class=\"checkbox\" style=\"min-width: 250px\"><input type=\"checkbox\" id=\"nedsattMed25\"\n" +
    "                                                              name=\"capacityForWork.work25\"\n" +
    "                                                              ng-change=\"field8b.onChangeWorkStateCheck('nedsattMed25')\"\n" +
    "                                                              ng-model=\"field8b.nedsattMed25.workState\">\n" +
    "        25%\n" +
    "        nedsatt arbetsförmåga från</label>\n" +
    "                      <span wc-date-picker-field target-model=\"model.nedsattMed25.from\"\n" +
    "                            on-change=\"onChangeNedsattMed('nedsattMed25', 'from')\" dom-id=\"nedsattMed25from\"\n" +
    "                            invalid=\"field8b.nedsattMed25.nedsattInvalidFrom\"></span>\n" +
    "      <span>till</span>\n" +
    "                      <span wc-date-picker-field target-model=\"model.nedsattMed25.tom\"\n" +
    "                            on-change=\"onChangeNedsattMed('nedsattMed25', 'tom')\" dom-id=\"nedsattMed25tom\"\n" +
    "                            invalid=\"field8b.nedsattMed25.nedsattInvalidTom\"></span>\n" +
    "      <div class=\"indent pad10\" ng-if=\"field8b.nedsattMed25.workState\">\n" +
    "        <div class=\"workHoursPerWeek\" ng-show=\"model.tjanstgoringstid != '' && model.tjanstgoringstid > 0\">\n" +
    "          Arbetstid timmar/vecka: <strong id=\"arbetstid25\">{{model.tjanstgoringstid * 0.75 |\n" +
    "          number}}</strong>\n" +
    "        </div>\n" +
    "        Arbetstidsförläggning (frivilligt) <input type=\"text\" ng-model=\"model.nedsattMed25Beskrivning\"\n" +
    "                                                  id=\"nedsattMed25beskrivning\"\n" +
    "                                                  id=\"capacityForWork.rec25LongerWorkingHours\"\n" +
    "                                                  name=\"capacityForWork.rec25LongerWorkingHours\" value=\"\"\n" +
    "                                                  class='form-control otherInformation'\n" +
    "                                                  ng-change=\"viewState.limitOtherField('nedsattMed25Beskrivning')\"\n" +
    "                                                  wc-disable-key-down>\n" +
    "        <span class=\"counter\">Tecken kvar:  {{viewState.inputLimits.ovrigt - viewState.getTotalOvrigtLength()}}</span>\n" +
    "        <div class=\"info-transfer\">Informationen överförs till \"Övriga upplysningar\" vid PDF-utskrift av\n" +
    "          blanketten.\n" +
    "        </div>\n" +
    "      </div>\n" +
    "    </div>\n" +
    "\n" +
    "    <!-- 50% -->\n" +
    "    <div class=\"form-group form-inline\">\n" +
    "      <label class=\"checkbox\" style=\"min-width: 250px\"><input type=\"checkbox\" id=\"nedsattMed50\"\n" +
    "                                                              name=\"capacityForWork.work50\"\n" +
    "                                                              ng-change=\"field8b.onChangeWorkStateCheck('nedsattMed50')\"\n" +
    "                                                              ng-model=\"field8b.nedsattMed50.workState\"> 50%\n" +
    "        nedsatt arbetsförmåga från</label>\n" +
    "                      <span wc-date-picker-field target-model=\"model.nedsattMed50.from\"\n" +
    "                            on-change=\"onChangeNedsattMed('nedsattMed50', 'from')\" dom-id=\"nedsattMed50from\"\n" +
    "                            invalid=\"field8b.nedsattMed50.nedsattInvalidFrom\"></span>\n" +
    "      <span>till</span>\n" +
    "                      <span wc-date-picker-field target-model=\"model.nedsattMed50.tom\"\n" +
    "                            on-change=\"onChangeNedsattMed('nedsattMed50', 'tom')\" dom-id=\"nedsattMed50tom\"\n" +
    "                            invalid=\"field8b.nedsattMed50.nedsattInvalidTom\"></span>\n" +
    "      <div class=\"indent pad10\" ng-if=\"field8b.nedsattMed50.workState\">\n" +
    "        <div class=\"workHoursPerWeek\" ng-show=\"model.tjanstgoringstid != '' && model.tjanstgoringstid > 0\">\n" +
    "          Arbetstid timmar/vecka: <strong id=\"arbetstid50\">{{model.tjanstgoringstid * 0.5 |\n" +
    "          number}}</strong>\n" +
    "        </div>\n" +
    "        Arbetstidsförläggning (frivilligt) <input type=\"text\" ng-model=\"model.nedsattMed50Beskrivning\"\n" +
    "                                                  id=\"nedsattMed50beskrivning\"\n" +
    "                                                  name=\"capacityForWork.rec50LongerWorkingHours\" value=\"\"\n" +
    "                                                  class='form-control otherInformation'\n" +
    "                                                  ng-change=\"viewState.limitOtherField('nedsattMed50Beskrivning')\"\n" +
    "                                                  wc-disable-key-down>\n" +
    "        <span class=\"counter\">Tecken kvar:  {{viewState.inputLimits.ovrigt - viewState.getTotalOvrigtLength()}}</span>\n" +
    "        <div class=\"info-transfer\">Informationen överförs till \"Övriga upplysningar\" vid PDF-utskrift av\n" +
    "          blanketten.\n" +
    "        </div>\n" +
    "      </div>\n" +
    "    </div>\n" +
    "\n" +
    "    <!-- 75% -->\n" +
    "    <div class=\"form-group form-inline\">\n" +
    "      <label class=\"checkbox\" style=\"min-width: 250px\"><input type=\"checkbox\" id=\"nedsattMed75\"\n" +
    "                                                              name=\"capacityForWork.work75\"\n" +
    "                                                              ng-change=\"field8b.onChangeWorkStateCheck('nedsattMed75')\"\n" +
    "                                                              ng-model=\"field8b.nedsattMed75.workState\"> 75%\n" +
    "        nedsatt arbetsförmåga från</label>\n" +
    "                      <span wc-date-picker-field target-model=\"model.nedsattMed75.from\"\n" +
    "                            on-change=\"onChangeNedsattMed('nedsattMed75', 'from')\" dom-id=\"nedsattMed75from\"\n" +
    "                            invalid=\"field8b.nedsattMed75.nedsattInvalidFrom\"></span>\n" +
    "      <span>till</span>\n" +
    "                      <span wc-date-picker-field target-model=\"model.nedsattMed75.tom\"\n" +
    "                            on-change=\"onChangeNedsattMed('nedsattMed75', 'tom')\" dom-id=\"nedsattMed75tom\"\n" +
    "                            invalid=\"field8b.nedsattMed75.nedsattInvalidTom\"></span>\n" +
    "      <div class=\"indent pad10\" ng-if=\"field8b.nedsattMed75.workState\">\n" +
    "        <div class=\"workHoursPerWeek\" ng-show=\"model.tjanstgoringstid != '' && model.tjanstgoringstid > 0\">\n" +
    "          Arbetstid timmar/vecka: <strong id=\"arbetstid75\">{{model.tjanstgoringstid * 0.25 |\n" +
    "          number}}</strong>\n" +
    "        </div>\n" +
    "        Arbetstidsförläggning (frivilligt) <input type=\"text\" ng-model=\"model.ovrigt.nedsattMed75Beskrivning\"\n" +
    "                                                  id=\"nedsattMed75beskrivning\"\n" +
    "                                                  name=\"capacityForWork.rec75LongerWorkingHours\" value=\"\"\n" +
    "                                                  class='form-control otherInformation'\n" +
    "                                                  ng-change=\"viewState.limitOtherField('nedsattMed75Beskrivning')\"\n" +
    "                                                  wc-disable-key-down>\n" +
    "        <span class=\"counter\">Tecken kvar:  {{viewState.inputLimits.ovrigt - viewState.getTotalOvrigtLength()}}</span>\n" +
    "        <div class=\"info-transfer\">Informationen överförs till \"Övriga upplysningar\" vid PDF-utskrift av\n" +
    "          blanketten.\n" +
    "        </div>\n" +
    "      </div>\n" +
    "    </div>\n" +
    "\n" +
    "    <!-- 100% -->\n" +
    "    <div class=\"form-group form-inline\">\n" +
    "      <label class=\"checkbox\" style=\"min-width: 250px\"><input type=\"checkbox\" id=\"nedsattMed100\"\n" +
    "                                                              name=\"capacityForWork.workFull\"\n" +
    "                                                              ng-change=\"field8b.onChangeWorkStateCheck('nedsattMed100')\"\n" +
    "                                                              ng-model=\"field8b.nedsattMed100.workState\"> Helt\n" +
    "        nedsatt arbetsförmåga från</label>\n" +
    "                      <span wc-date-picker-field target-model=\"model.nedsattMed100.from\"\n" +
    "                            on-change=\"onChangeNedsattMed('nedsattMed100', 'from')\" dom-id=\"nedsattMed100from\"\n" +
    "                            invalid=\"field8b.nedsattMed100.nedsattInvalidFrom\"></span>\n" +
    "      <span>till</span>\n" +
    "                      <span wc-date-picker-field target-model=\"model.nedsattMed100.tom\"\n" +
    "                            on-change=\"onChangeNedsattMed('nedsattMed100', 'tom')\" dom-id=\"nedsattMed100tom\"\n" +
    "                            invalid=\"field8b.nedsattMed100.nedsattInvalidTom\"></span>\n" +
    "    </div>\n" +
    "\n" +
    "    <div ng-show=\"totalCertDays\">\n" +
    "      <p>Detta intyg motsvarar en period på: <span id=\"totalCertDays\">{{totalCertDays}}</span> <span\n" +
    "          ng-show=\"totalCertDays > 1\">dagar</span><span ng-show=\"totalCertDays == 1\">dag</span></p>\n" +
    "    </div>\n" +
    "    <div class=\"alert alert-warning\" ng-show=\"datesOutOfRange\">\n" +
    "      De datum du angett är <strong>mer än en vecka före dagens datum eller mer än 6 månader framåt i\n" +
    "      tiden</strong>. Du bör kontrollera att tidsperioderna är korrekta.\n" +
    "    </div>\n" +
    "    <div class=\"alert alert-warning\" ng-show=\"datesPeriodTooLong\">\n" +
    "      De datum du angett innebär <strong>en period på mer än 6 månader</strong>. Du bör kontrollera att\n" +
    "      tidsperioderna är korrekta.\n" +
    "    </div>\n" +
    "  </div>\n" +
    "</div>"
  );


  $templateCache.put('/web/webjars/fk7263/webcert/js/intyg/edit/form/form9.html',
    "<div wc-field field-label=\"fk7263.label.arbetsformaga.bedomning\" field-number=\"9\"\n" +
    "     field-help-text=\"fk7263.helptext.arbetsformaga.bedoms-langre\">\n" +
    "  <a class=\"soclink\" href=\"http://www.socialstyrelsen.se/riktlinjer/forsakringsmedicinsktbeslutsstod\"\n" +
    "     target=\"_blank\" title=\"Öppnar Socialstyrelsens beslutstöd för angiven huvuddiagnos\">Socialstyrelsens\n" +
    "    försäkringsmedicinska beslutstöd</a>\n" +
    "                    <textarea id=\"capacityForWorkText\" class=\"form-control\" name=\"capacityForWork.text\" rows='10'\n" +
    "                              ng-model=\"model.arbetsformagaPrognos\" wc-maxlength ng-trim=\"false\"\n" +
    "                              maxlength=\"{{viewState.inputLimits.arbetsformagaPrognos}}\"></textarea>\n" +
    "</div>"
  );


  $templateCache.put('/web/webjars/fk7263/webcert/js/intyg/edit/intyg-edit.html',
    "<a id=\"top\"></a>\n" +
    "<div class=\"container-fluid\" id=\"edit-fk7263\">\n" +
    "  <div class=\"row\">\n" +
    "    <div class=\"col-md-12 webcert-col webcert-col-single\">\n" +
    "\n" +
    "      <div ui-view=\"header\"></div>\n" +
    "\n" +
    "      <!-- Integration deleted message -->\n" +
    "      <div id=\"integration-deleted\" ng-if=\"viewState.common.deleted\">\n" +
    "        <div class=\"alert alert-info banner-margin\">\n" +
    "          Utkastet är raderat och borttaget från Webcert.\n" +
    "        </div>\n" +
    "      </div>\n" +
    "\n" +
    "      <!-- FK 7263 -->\n" +
    "      <div class=\"edit-form\" ng-class=\"{'collapsed' : viewState.common.collapsedHeader}\">\n" +
    "\n" +
    "        <form name=\"certForm\" novalidate autocomplete=\"off\" wc-auto-save=\"save(true)\">\n" +
    "          <div ng-hide=\"viewState.common.error.activeErrorMessageKey || viewState.common.deleted\">\n" +
    "\n" +
    "            <div wc-spinner label=\"info.loadingdata\" show-spinner=\"!viewState.common.doneLoading\"\n" +
    "                 show-content=\"viewState.common.doneLoading\">\n" +
    "\n" +
    "              <div class=\"alert alert-danger\" ng-show=\"viewState.common.showComplete && !viewState.common.intyg.isComplete\"\n" +
    "                   style=\"margin-right: 20px\">\n" +
    "                <h3><span message key=\"draft.saknar-uppgifter\"></span></h3>\n" +
    "                <ul>\n" +
    "                  <li ng-repeat=\"message in viewState.common.validationMessages\">\n" +
    "                    <span message key=\"{{message.message}}\"></span></li>\n" +
    "                </ul>\n" +
    "              </div>\n" +
    "\n" +
    "              <div id=\"certificate\" class=\"row certificate\">\n" +
    "                <div class=\"col-md-12 cert-body\">\n" +
    "\n" +
    "                  <!-- 1. Smittskydd -->\n" +
    "                  <div ui-view=\"form1\"/>\n" +
    "\n" +
    "                  <!-- 4b. Intyger baseras på -->\n" +
    "                  <div ui-view=\"form4b\"/>\n" +
    "\n" +
    "                  <!-- 2. Diagnosis -->\n" +
    "                  <div ui-view=\"form2\"/>\n" +
    "\n" +
    "                  <!-- 3. Sjukdomsförlopp-->\n" +
    "                  <div ui-view=\"form3\"/>\n" +
    "\n" +
    "                  <!-- 4. Funktionsnedsättning-->\n" +
    "                  <div ui-view=\"form4\"/>\n" +
    "\n" +
    "                  <!-- 5. Aktivitetsbegränsning -->\n" +
    "                  <div ui-view=\"form5\"/>\n" +
    "\n" +
    "                  <!-- 8a. Arbete -->\n" +
    "                  <div ui-view=\"form8a\"/>\n" +
    "\n" +
    "                  <!-- 8b. Arbetsförmåga -->\n" +
    "                  <div ui-view=\"form8b\"/>\n" +
    "\n" +
    "                  <!-- 9. Arbetsförmåga -->\n" +
    "                  <div ui-view=\"form9\"/>\n" +
    "\n" +
    "                  <!-- 10. Prognos -->\n" +
    "                  <div ui-view=\"form10\"/>\n" +
    "\n" +
    "                  <!-- 6b. Åtgärder -->\n" +
    "                  <div ui-view=\"form6b\"/>\n" +
    "\n" +
    "                  <!-- 6a, 7, 11. Rekommendationer -->\n" +
    "                  <div ui-view=\"form6a-7-11\"/>\n" +
    "\n" +
    "                  <!-- 12. Kontakt -->\n" +
    "                  <div ui-view=\"form12\"/>\n" +
    "\n" +
    "                  <!-- 13. upplysningar -->\n" +
    "                  <div ui-view=\"form13\"/>\n" +
    "\n" +
    "                  <!-- 15. upplysningar -->\n" +
    "                  <div ui-view=\"form15\"/>\n" +
    "\n" +
    "                  <div wc-field-signing-doctor></div>\n" +
    "\n" +
    "                </div>\n" +
    "                <!-- cert body -->\n" +
    "\n" +
    "              </div>\n" +
    "              <!-- certificate -->\n" +
    "\n" +
    "              <div ng-if=\"!isSigned\" class=\"print-hide\">\n" +
    "\n" +
    "                <h3><span message key=\"common.sign.intyg\"></span></h3>\n" +
    "\n" +
    "                <div ng-if=\"!user.userContext.lakare\">\n" +
    "                  <div id=\"sign-requires-doctor-message-text\" class=\"alert alert-info\">\n" +
    "                    <span message key=\"draft.onlydoctorscansign\"></span>\n" +
    "                  </div>\n" +
    "\n" +
    "                  <div wc-feature-not-active feature=\"franJournalsystem\">\n" +
    "                    <p>\n" +
    "                      <span message key=\"draft.notifydoctor\"></span>\n" +
    "                    </p>\n" +
    "                    <div class=\"form-group\">\n" +
    "                      <button id=\"vidarebefordraEjHanterad\" class=\"btn\"\n" +
    "                              ng-class=\"{'btn-info': !certMeta.vidarebefordrad, 'btn-default btn-secondary' : certMeta.vidarebefordrad}\"\n" +
    "                              title=\"Skicka mejl med en länk till utkastet för att informera den läkare som ska signera det.\"\n" +
    "                              ng-click=\"openMailDialog()\">\n" +
    "                        <img ng-if=\"!certMeta.vidarebefordrad\" src=\"/img/mail.png\"> <img ng-if=\"certMeta.vidarebefordrad\"\n" +
    "                                                                                         src=\"/img/mail_dark.png\">\n" +
    "                      </button>\n" +
    "                    </div>\n" +
    "\n" +
    "                    <div class=\"body-row\">\n" +
    "                      <label for=\"mark-as-vidarebefordrad\">\n" +
    "                        <input id=\"mark-as-vidarebefordrad\" type=\"checkbox\"\n" +
    "                               ng-disabled=\"viewState.common.vidarebefordraInProgress\" ng-model=\"certMeta.vidarebefordrad\"\n" +
    "                               ng-change=\"onVidarebefordradChange()\" /> Vidarebefordrad\n" +
    "                      </label> <span ng-if=\"viewState.common.vidarebefordraInProgress\"> <img\n" +
    "                        src=\"/img/ajax-loader-kit-16x16.gif\"></span>\n" +
    "                    </div>\n" +
    "                  </div>\n" +
    "                </div>\n" +
    "\n" +
    "                <button id=\"signera-utkast-button\" class=\"btn btn-success\" ng-disabled=\"!viewState.common.intyg.isComplete || certForm.$dirty || signingWithSITHSInProgress\"\n" +
    "                        ng-click=\"sign()\" ng-show=\"user.userContext.lakare\"><img ng-show=\"signingWithSITHSInProgress\" ng-src=\"/img/ajax-loader-small-green.gif\"/> <span message key=\"common.sign\"></span>\n" +
    "                </button>\n" +
    "\n" +
    "              </div>\n" +
    "\n" +
    "            </div>\n" +
    "          </div>\n" +
    "\n" +
    "          <wc-disable-key-down wc-disable-key-down-elements=\":text,:checkbox,:radio\" wc-disable-key-down-done-loading=\"viewState.common.doneLoading\" />\n" +
    "\n" +
    "        </form>\n" +
    "      </div>\n" +
    "\n" +
    "    </div>\n" +
    "    <!-- span12 webcert-col-single -->\n" +
    "  </div>\n" +
    "  <!-- row -->\n" +
    "</div> <!-- container -->\n"
  );


  $templateCache.put('/web/webjars/fk7263/webcert/js/intyg/view/intyg-view.html',
    "<div class=\"weak-hidden\" ng-class=\"{show: widgetState.showTemplate}\" ng-controller=\"fk7263.ViewCertCtrl\">\n" +
    "  <div wc-spinner label=\"fk7263.info.loadingcertificate\" show-spinner=\"!widgetState.doneLoading\"\n" +
    "       show-content=\"widgetState.doneLoading\">\n" +
    "\n" +
    "    <div ui-view=\"header\"></div>\n" +
    "\n" +
    "    <div ng-show=\"!widgetState.activeErrorMessageKey\">\n" +
    "      <div id=\"certificate\" ng-class=\"{'certificate': true, 'revoked-certificate' : certProperties.isRevoked}\">\n" +
    "        <div class=\"row\">\n" +
    "          <div class=\"col-md-12\">\n" +
    "            <div class=\"cert-body\">\n" +
    "\n" +
    "              <div wc-field field-number=\"1\" field-label=\"fk7263.label.smittskydd\" filled=\"{{true}}\">\n" +
    "                <span ng-show=\"cert.avstangningSmittskydd\" id=\"smittskydd\"> <span message key=\"fk7263.label.yes\"></span></span>\n" +
    "                <span ng-show=\"!cert.avstangningSmittskydd\"> <span message key=\"fk7263.label.no\"></span></span>\n" +
    "              </div>\n" +
    "\n" +
    "              <div wc-field field-number=\"2\" field-label=\"fk7263.label.diagnos\"\n" +
    "                   filled=\"{{cert.diagnosKod!=null}}\" id=\"field2\">\n" +
    "                <div class=\"form-group\">\n" +
    "                  <span message key=\"fk7263.label.diagnoskod.icd\"></span>\n" +
    "                  <span id=\"diagnosKod\">{{cert.diagnosKod}}</span> <span id=\"diagnosBeskrivning1\">{{cert.diagnosBeskrivning1}}</span>\n" +
    "                </div>\n" +
    "                <div class=\"form-group\">\n" +
    "                  <label class=\"control-label bold\"\n" +
    "                         ng-show=\"cert.diagnosKod2 || cert.diagnosBeskrivning2 || cert.diagnosKod3 || cert.diagnosBeskrivning3\">\n" +
    "                    <span message key=\"fk7263.label.ytterligarediagnoser\"></span>\n" +
    "                  </label>\n" +
    "                  <div class=\"controls\">\n" +
    "                    <p ng-show=\"cert.diagnosKod2 || cert.diagnosBeskrivning2 \"><span id=\"diagnosKod2\">{{cert.diagnosKod2}}</span>\n" +
    "                      <span id=\"diagnosBeskrivning2\">{{cert.diagnosBeskrivning2}}</span></p>\n" +
    "                    <p ng-show=\"cert.diagnosKod3 || cert.diagnosBeskrivning3\"><span id=\"diagnosKod3\">{{cert.diagnosKod3}}</span>\n" +
    "                      <span id=\"diagnosBeskrivning3\">{{cert.diagnosBeskrivning3}}</span></p>\n" +
    "                    <div id=\"samsjuklighet\" ng-show=\"cert.samsjuklighet\" class=\"bold\"><span message\n" +
    "                                                                         key=\"fk7263.label.samsjuklighet\"></span></div>\n" +
    "                  </div>\n" +
    "                </div>\n" +
    "                <div class=\"form-group\" ng-show=\"cert.diagnosBeskrivning\">\n" +
    "                  <label class=\"control-label bold\">\n" +
    "                    <span message key=\"fk7263.label.diagnosfortydligande\"></span>\n" +
    "                  </label>\n" +
    "                  <div class=\"controls\">\n" +
    "                    <div id=\"diagnosBeskrivning\" class=\"multiline\">{{cert.diagnosBeskrivning}}</div>\n" +
    "                  </div>\n" +
    "                </div>\n" +
    "              </div>\n" +
    "\n" +
    "              <div wc-field field-number=\"3\" field-label=\"fk7263.label.aktuellt-sjukdomsforlopp\"\n" +
    "                   filled=\"{{cert.sjukdomsforlopp!=null}}\"><div id=\"sjukdomsforlopp\" class=\"multiline\">{{cert.sjukdomsforlopp}}</div>\n" +
    "              </div>\n" +
    "\n" +
    "              <div wc-field field-number=\"4\" field-label=\"fk7263.label.funktionsnedsattningar\"\n" +
    "                   filled=\"{{cert.funktionsnedsattning!=null}}\"><div id=\"funktionsnedsattning\" class=\"multiline\">{{cert.funktionsnedsattning}}</div>\n" +
    "              </div>\n" +
    "\n" +
    "              <div wc-field field-number=\"4b\" field-label=\"fk7263.label.intyg-baserat-pa\" id=\"field4b\"\n" +
    "                   filled=\"{{cert.undersokningAvPatienten!=null || cert.telefonkontaktMedPatienten!=null || cert.journaluppgifter!=null || cert.annanReferens!=null}}\">\n" +
    "                <ul class=\"padded-list\" id=\"baseratPaList\">\n" +
    "                  <li ng-show=\"cert.undersokningAvPatienten!=null\"><span message\n" +
    "                                                                         key=\"fk7263.vardkontakt.undersokning\"></span><span id=\"undersokningAvPatienten\">{{cert.undersokningAvPatienten\n" +
    "                    | date:'longDate' }}</span>\n" +
    "                  </li>\n" +
    "                  <li ng-show=\"cert.telefonkontaktMedPatienten!=null\"><span message\n" +
    "                                                                            key=\"fk7263.vardkontakt.telefonkontakt\"></span><span id=\"telefonkontaktMedPatienten\">{{cert.telefonkontaktMedPatienten\n" +
    "                    | date:'longDate' }}</span>\n" +
    "                  </li>\n" +
    "                  <li ng-show=\"cert.journaluppgifter!=null\"><span message key=\"fk7263.referens.journal\"></span><span id=\"journaluppgifter\">{{cert.journaluppgifter\n" +
    "                    | date:'longDate' }}</span>\n" +
    "                  </li>\n" +
    "\n" +
    "                  <li ng-show=\"cert.annanReferens!=null\">\n" +
    "                    <!-- Annat -->\n" +
    "                    <span message key=\"fk7263.referens.annat\"></span> <span id=\"annanReferens\">{{cert.annanReferens | date:'longDate'}}</span>:\n" +
    "                    <div id=\"annanReferensBeskrivning\" class=\"multiline\">{{cert.annanReferensBeskrivning}}</div>\n" +
    "                  </li>\n" +
    "                </ul>\n" +
    "              </div>\n" +
    "\n" +
    "              <div wc-field field-number=\"5\" field-label=\"fk7263.label.aktivitetsbegransning\"\n" +
    "                   filled=\"{{cert.aktivitetsbegransning!=null}}\"><div id=\"aktivitetsbegransning\" class=\"multiline\">{{cert.aktivitetsbegransning}}</div>\n" +
    "              </div>\n" +
    "\n" +
    "              <div wc-field field-number=\"6a\" field-label=\"fk7263.label.rekommendationer\" id=\"field6a\"\n" +
    "                   filled=\"{{cert.rekommendationKontaktArbetsformedlingen || cert.rekommendationKontaktForetagshalsovarden || cert.rekommendationOvrigt!=null}}\">\n" +
    "                <ul class=\"padded-list\">\n" +
    "                  <li id=\"rekommendationKontaktArbetsformedlingen\" ng-show=\"cert.rekommendationKontaktArbetsformedlingen\"><span message\n" +
    "                                                                                   key=\"fk7263.label.rekommendationer.kontakt.arbetsformedlingen\"></span>\n" +
    "                  </li>\n" +
    "                  <li id=\"rekommendationKontaktForetagshalsovarden\" ng-show=\"cert.rekommendationKontaktForetagshalsovarden\"><span message\n" +
    "                                                                                    key=\"fk7263.label.rekommendationer.kontakt.foretagshalsovarden\"></span>\n" +
    "                  </li>\n" +
    "                  <li ng-show=\"cert.rekommendationOvrigt\"><span message\n" +
    "                                                                key=\"fk7263.label.rekommendationer.kontakt.ovrigt\"></span>\n" +
    "                    <span id=\"rekommendationOvrigt\">{{cert.rekommendationOvrigt}}</span>\n" +
    "                  </li>\n" +
    "                </ul>\n" +
    "              </div>\n" +
    "\n" +
    "              <div wc-field field-number=\"6b\" field-label=\"fk7263.label.planerad-atgard\"\n" +
    "                   filled=\"{{cert.atgardInomSjukvarden!=null||cert.annanAtgard!=null}}\" id=\"field6b\">\n" +
    "                <ul class=\"padded-list\">\n" +
    "                  <li ng-show=\"cert.atgardInomSjukvarden != null\">\n" +
    "                    <span message key=\"fk7263.label.planerad-atgard.sjukvarden\"></span>\n" +
    "                    <div id=\"atgardInomSjukvarden\" class=\"multiline\">{{cert.atgardInomSjukvarden}}</div>\n" +
    "                  </li>\n" +
    "                  <li ng-show=\"cert.annanAtgard != null\">\n" +
    "                    <span message key=\"fk7263.label.planerad-atgard.annat\"></span>\n" +
    "                    <div id=\"annanAtgard\" class=\"multiline\">{{cert.annanAtgard}}</div>\n" +
    "                  </li>\n" +
    "                </ul>\n" +
    "              </div>\n" +
    "\n" +
    "              <div wc-field field-number=\"7\" field-label=\"fk7263.label.rehabilitering-aktuell\"\n" +
    "                   filled=\"{{cert.rehabilitering!=null}}\"\n" +
    "                   id=\"field7\" class=\"print-pagebreak-after\">\n" +
    "                <span id=\"rehabiliteringAktuell\" ng-show=\"cert.rehabilitering == 'rehabiliteringAktuell'\"><span message key=\"fk7263.label.yes\"></span></span>\n" +
    "                <span id=\"rehabiliteringEjAktuell\" ng-show=\"cert.rehabilitering == 'rehabiliteringEjAktuell'\"><span message key=\"fk7263.label.no\"></span></span>\n" +
    "                <span id=\"rehabiliteringGarInteAttBedoma\" ng-show=\"cert.rehabilitering == 'rehabiliteringGarInteAttBedoma'\"><span message key=\"fk7263.label.gar-ej-att-bedomma\"></span></span>\n" +
    "              </div>\n" +
    "\n" +
    "              <div wc-field field-number=\"8a\" field-label=\"fk7263.label.arbetsformaga\"\n" +
    "                   filled=\"{{cert.nuvarandeArbetsuppgifter||cert.arbetsloshet||cert.foraldrarledighet}}\" id=\"field8\">\n" +
    "                <ul class=\"padded-list\">\n" +
    "                  <li ng-show=\"cert.nuvarandeArbetsuppgifter\"><span message\n" +
    "                                                                    key=\"fk7263.label.arbetsformaga.nuvarande-arbete\"></span>\n" +
    "                    <div id=\"nuvarandeArbetsuppgifter\" class=\"multiline\">{{cert.nuvarandeArbetsuppgifter}}</div>\n" +
    "                  </li>\n" +
    "                  <li id=\"arbetsloshet\" ng-show=\"cert.arbetsloshet\"><span message key=\"fk7263.label.arbetsformaga.arbetslos\"></span></li>\n" +
    "                  <li id=\"foraldrarledighet\" ng-show=\"cert.foraldrarledighet\"><span message\n" +
    "                                                             key=\"fk7263.label.arbetsformaga.foraldrarledighet\"></span>\n" +
    "                  </li>\n" +
    "                </ul>\n" +
    "              </div>\n" +
    "\n" +
    "              <div wc-field field-number=\"8b\" field-label=\"fk7263.label.nedsattning\"\n" +
    "                   filled=\"{{cert.nedsattMed25!=null||cert.nedsattMed50!=null||cert.nedsattMed75!=null||cert.nedsattMed100!=null}}\"\n" +
    "                   id=\"field8b\">\n" +
    "\n" +
    "                <div ng-show=\"cert.nedsattMed25 != null\">\n" +
    "                  <strong><span message key=\"fk7263.nedsattningsgrad.nedsatt_med_1_4\"></span></strong><br> Från och med\n" +
    "                  <span id=\"nedsattMed25from\">{{cert.nedsattMed25.from | date:'longDate'}}</span> &ndash; längst till och med\n" +
    "                  <span id=\"nedsattMed25tom\">{{cert.nedsattMed25.tom | date:'longDate'}}</span>\n" +
    "                  <div id=\"nedsattMed25Beskrivning\" ng-show=\"cert.nedsattMed25Beskrivning\" class=\"multiline\">Arbetstidsförläggning: {{cert.nedsattMed25Beskrivning}}\n" +
    "                  </div>\n" +
    "                </div>\n" +
    "\n" +
    "                <div ng-show=\"cert.nedsattMed50 != null\">\n" +
    "                  <strong><span message key=\"fk7263.nedsattningsgrad.nedsatt_med_1_2\"></span></strong><br> Från och med\n" +
    "                  <span id=\"nedsattMed50from\">{{cert.nedsattMed50.from | date:'longDate'}}</span> &ndash; längst till och med\n" +
    "                  <span id=\"nedsattMed50tom\">{{cert.nedsattMed50.tom | date:'longDate'}}</span>\n" +
    "                  <div id=\"nedsattMed50Beskrivning\" ng-show=\"cert.nedsattMed50Beskrivning\" class=\"multiline\">Arbetstidsförläggning: {{cert.nedsattMed50Beskrivning}}\n" +
    "                  </div>\n" +
    "                </div>\n" +
    "\n" +
    "                <div ng-show=\"cert.nedsattMed75 != null\">\n" +
    "                  <strong><span message key=\"fk7263.nedsattningsgrad.nedsatt_med_3_4\"></span></strong><br> Från och med\n" +
    "                  <span id=\"nedsattMed75from\">{{cert.nedsattMed75.from | date:'longDate'}}</span> &ndash; längst till och med\n" +
    "                  <span id=\"nedsattMed75tom\">{{cert.nedsattMed75.tom | date:'longDate'}}</span>\n" +
    "                  <div id=\"nedsattMed75Beskrivning\" ng-show=\"cert.nedsattMed75Beskrivning\" class=\"multiline\">Arbetstidsförläggning: {{cert.nedsattMed75Beskrivning}}\n" +
    "                  </div>\n" +
    "                </div>\n" +
    "\n" +
    "                <div ng-show=\"cert.nedsattMed100 != null\">\n" +
    "                  <strong><span message key=\"fk7263.nedsattningsgrad.helt_nedsatt\"></span></strong><br> Från och med\n" +
    "                  <span id=\"nedsattMed100from\">{{cert.nedsattMed100.from | date:'longDate'}}</span> &ndash; längst till och med\n" +
    "                  <span id=\"nedsattMed100tom\">{{cert.nedsattMed100.tom | date:'longDate'}}</span>\n" +
    "                </div>\n" +
    "\n" +
    "              </div>\n" +
    "\n" +
    "              <div wc-field field-number=\"9\" field-label=\"fk7263.label.arbetsformaga.bedomning\"\n" +
    "                   filled=\"{{cert.arbetsformagaPrognos!=null}}\"><div id=\"arbetsformagaPrognos\" class=\"multiline\">{{cert.arbetsformagaPrognos}}</div>\n" +
    "              </div>\n" +
    "\n" +
    "              <div wc-field field-number=\"10\" field-label=\"fk7263.label.prognos\"\n" +
    "                   filled=\"{{cert.prognosBedomning!=null}}\"\n" +
    "                   id=\"field10\">\n" +
    "\n" +
    "                <span id=\"arbetsformataPrognosJa\" ng-show=\"cert.prognosBedomning == 'arbetsformagaPrognosJa'\"><span message key=\"fk7263.label.yes\"></span></span>\n" +
    "                <span id=\"arbetsformataPrognosJaDelvis\" ng-show=\"cert.prognosBedomning == 'arbetsformagaPrognosJaDelvis'\"><span message key=\"fk7263.label.delvis\"></span></span>\n" +
    "                <span id=\"arbetsformataPrognosNej\" ng-show=\"cert.prognosBedomning == 'arbetsformagaPrognosNej'\"><span message key=\"fk7263.label.no\"></span></span>\n" +
    "                <span id=\"arbetsformataPrognosGarInteAttBedoma\" ng-show=\"cert.prognosBedomning == 'arbetsformagaPrognosGarInteAttBedoma'\">\n" +
    "                  <span message key=\"fk7263.label.gar-ej-att-bedomma\"></span>\n" +
    "                  <div ng-show=\"cert.arbetsformagaPrognosGarInteAttBedomaBeskrivning\">\n" +
    "                    Förtydligande: <div id=\"arbetsformagaPrognosGarInteAttBedomaBeskrivning\" class=\"multiline\">{{cert.arbetsformagaPrognosGarInteAttBedomaBeskrivning}}</div>\n" +
    "                  </div>\n" +
    "                </span>\n" +
    "              </div>\n" +
    "\n" +
    "              <div wc-field field-number=\"11\" field-label=\"fk7263.label.resor-till-arbetet\"\n" +
    "                   filled=\"{{cert.ressattTillArbeteAktuellt || cert.ressattTillArbeteEjAktuellt}}\" id=\"field11\">\n" +
    "                <span id=\"ressattTillArbeteAktuellt\" ng-show=\"cert.ressattTillArbeteAktuellt\"> <span message key=\"fk7263.label.yes\"></span></span>\n" +
    "                <span id=\"ressattTillArbeteEjAktuellt\" ng-show=\"cert.ressattTillArbeteEjAktuellt\"> <span message key=\"fk7263.label.no\"></span></span>\n" +
    "              </div>\n" +
    "\n" +
    "              <div wc-field field-number=\"12\" field-label=\"fk7263.label.kontakt-med-fk\" filled=\"{{true}}\" id=\"field12\">\n" +
    "                <span id=\"kontaktMedFk\" ng-show=\"cert.kontaktMedFk\"> <span message key=\"fk7263.label.yes\"></span></span>\n" +
    "                <span ng-show=\"!cert.kontaktMedFk\"> <span message key=\"fk7263.label.no\"></span></span>\n" +
    "              </div>\n" +
    "\n" +
    "              <div wc-field field-number=\"13\" field-label=\"fk7263.label.ovrigt\" filled=\"{{cert.kommentar!=null&&cert.kommentar!=''&&cert.kommentar!=undefined}}\"\n" +
    "                   id=\"field13\">\n" +
    "                <div id=\"kommentar\" class=\"multiline\">{{cert.kommentar}}</div>\n" +
    "              </div>\n" +
    "\n" +
    "              <div wc-field field-label=\"fk7263.label.arbetsplatskod\" field-number=\"17\"\n" +
    "                   filled=\"{{cert.forskrivarkodOchArbetsplatskod!=null}}\" id=\"field17\">\n" +
    "                <!--  leave  as div tag for IE8 magic bug. using span crashes angular.. -->\n" +
    "                <div id=\"forskrivarkodOchArbetsplatskod\">{{cert.forskrivarkodOchArbetsplatskod}}</div>\n" +
    "              </div>\n" +
    "\n" +
    "              <div class=\"infobox\">\n" +
    "                <h3>\n" +
    "                  <span message key=\"common.label.ovanstaende-har-bekraftats\"></span>\n" +
    "                </h3>\n" +
    "                <h4>\n" +
    "                  <span message key=\"fk7263.label.datum\"></span>\n" +
    "                </h4>\n" +
    "                <p id=\"signeringsdatum\">{{cert.grundData.signeringsdatum | date:'longDate'}}</p>\n" +
    "                <h4>\n" +
    "                  <span message key=\"fk7263.label.kontakt-info\"></span>\n" +
    "                </h4>\n" +
    "                <p>\n" +
    "                  <span class=\"text\" id=\"vardperson_namn\">{{cert.grundData.skapadAv.fullstandigtNamn}}</span><br>\n" +
    "                  <span class=\"text\" id=\"vardperson_enhetsnamn\">{{cert.grundData.skapadAv.vardenhet.enhetsnamn}}</span><br>\n" +
    "                  <span class=\"text\" id=\"vardperson_postadress\">{{cert.grundData.skapadAv.vardenhet.postadress}}</span><br>\n" +
    "                  <span class=\"text\"><span id=\"vardperson_postnummer\">{{cert.grundData.skapadAv.vardenhet.postnummer}}</span> <span id=\"vardperson_postort\">{{cert.grundData.skapadAv.vardenhet.postort}}</span></span><br>\n" +
    "                  <span class=\"text\" id=\"vardperson_telefonnummer\">{{cert.grundData.skapadAv.vardenhet.telefonnummer}}</span><br>\n" +
    "                </p>\n" +
    "              </div>\n" +
    "\n" +
    "            </div><!-- cert body -->\n" +
    "          </div>\n" +
    "        </div>\n" +
    "      </div>\n" +
    "    </div><!-- entire cert -->\n" +
    "  </div><!--  spinner end -->\n" +
    "</div>\n"
  );


  $templateCache.put('/web/webjars/fk7263/webcert/views/diagnoseTemplate.html',
    "<span style=\"font-style:italic\" ng-if=\"match.model.moreResults\" message key=\"fk7263.label.diagnoses.more_results\"></span>\n" +
    "<a tabindex=\"-1\" bind-html-unsafe=\"match.label | typeaheadHighlight:query\" ></a>"
  );


  $templateCache.put('/web/webjars/fk7263/webcert/views/edit-cert.html',
    "<a id=\"top\"></a>\n" +
    "<div class=\"container-fluid\" id=\"edit-fk7263\">\n" +
    "  <div class=\"row\">\n" +
    "    <div class=\"col-md-12 webcert-col webcert-col-single\">\n" +
    "\n" +
    "      <div wc-print-header title-id=\"fk7263.label.certtitle\" print-message-id=\"print.label.draft\"\n" +
    "           intygs-id=\"cert.id\"></div>\n" +
    "\n" +
    "      <div class=\"header-fix-top print-hide\">\n" +
    "        <div class=\"header-wrapper menucollapse\" ng-hide=\"widgetState.collapsedHeader\">\n" +
    "          <div id=\"wcHeader\" wc-header default-active=\"index\"></div>\n" +
    "        </div>\n" +
    "\n" +
    "        <div ng-hide=\"widgetState.activeErrorMessageKey\">\n" +
    "          <div wc-spinner label=\"info.loadingdata\" show-spinner=\"!widgetState.doneLoading\"\n" +
    "               show-content=\"widgetState.doneLoading\">\n" +
    "\n" +
    "            <div class=\"header-fix-panel\">\n" +
    "              <div class=\"header-fix-top-expand\">\n" +
    "                <div class=\"header-fix-top-expand-wrapper\">\n" +
    "                  <button class=\"btn btn-link\" ng-click=\"toggleHeader()\"><span\n" +
    "                      ng-if=\"widgetState.collapsedHeader\">Visa</span><span\n" +
    "                      ng-if=\"!widgetState.collapsedHeader\">Dölj</span> meny\n" +
    "                  </button>\n" +
    "                </div>\n" +
    "              </div>\n" +
    "              <a id=\"tillbakaButton\" wc-feature-not-active feature=\"franJournalsystem\" class=\"backlink-icon\"\n" +
    "                 href wc-back title=\"Tillbaka till Skriv intyg\"></a>\n" +
    "              <h1 style=\"display: inline-block; margin-right: 20px;\"><span message key=\"fk7263.label.certtitle\"></span>\n" +
    "              </h1>\n" +
    "              <div class=\"inline-block\">\n" +
    "                <label class=\"control-label\" id=\"patientNamnPersonnummer\">{{cert.grundData.patient.fullstandigtNamn}} -\n" +
    "                  {{cert.grundData.patient.personId}}</label>\n" +
    "              </div>\n" +
    "\n" +
    "              <div wc-new-person-id-message></div>\n" +
    "\n" +
    "              <div class=\"form-group buttonbar\">\n" +
    "                <button id=\"spara-utkast\" class=\"btn btn-info\" ng-click=\"save()\" ng-if=\"!isSigned\"\n" +
    "                        ng-disabled=\"!certForm.$dirty\" title=\"Sparar intyget som ett utkast.\">Spara\n" +
    "                </button>\n" +
    "                <button id=\"ta-bort-utkast\" class=\"btn btn-default btn-secondary\" ng-if=\"!isSigned\" ng-click=\"discard()\"\n" +
    "                        title=\"Klicka på papperskorgen för att radera utkastet.\"><i\n" +
    "                    class=\"glyphicon glyphicon-trash\"></i>\n" +
    "                </button>\n" +
    "                <button id=\"skriv-ut-utkast\" class=\"btn btn-default btn-secondary\" ng-if=\"!isSigned\" ng-click=\"print()\"\n" +
    "                        title=\"Skriver ut utkastet.\">\n" +
    "                  Skriv ut\n" +
    "                </button>\n" +
    "                <button class=\"btn btn-default btn-secondary\" ng-if=\"!widgetState.showComplete\" ng-disabled=\"isComplete\"\n" +
    "                        ng-click=\"toggleShowComplete()\" id=\"showCompleteButton\"\n" +
    "                        title=\"Visar vilka obligatoriska fält i utkastet som inte är ifyllda. Alla obligatoriska fält måste vara ifyllda för att utkastet ska kunna signeras.\">\n" +
    "                  Visa vad som saknas\n" +
    "                </button>\n" +
    "                <button class=\"btn btn-default btn-secondary\" ng-if=\"widgetState.showComplete\" ng-disabled=\"isComplete\"\n" +
    "                        ng-click=\"toggleShowComplete()\" id=\"hideCompleteButton\"\n" +
    "                        title=\"Visar vilka obligatoriska fält i utkastet som inte är ifyllda. Alla obligatoriska fält måste vara ifyllda för att utkastet ska kunna signeras.\">\n" +
    "                  Dölj vad som saknas\n" +
    "                </button>\n" +
    "              </div>\n" +
    "\n" +
    "              <!-- Show save errors here to spare user from retyping everything for intermittent errors -->\n" +
    "              <div class=\"row\">\n" +
    "                <div ng-show=\"widgetState.saveErrorMessageKey != null\" class=\"col-md-7\">\n" +
    "                  <div class=\"alert alert-danger\">\n" +
    "                    <span message key=\"{{widgetState.saveErrorMessageKey}}\"></span>\n" +
    "                  </div>\n" +
    "                </div>\n" +
    "              </div>\n" +
    "\n" +
    "              <!-- Just in case -->\n" +
    "              <div id=\"certificate-is-sent-to-it-message-text\" ng-show=\"isSigned\"><span message\n" +
    "                                                                                        key=\"draft.status.signed\"></span>\n" +
    "              </div>\n" +
    "              <!-- complete -->\n" +
    "              <div ng-show=\"certForm.$pristine && isComplete && !isSigned\"><span id=\"intyget-sparat-meddelande\" message\n" +
    "                                                                                 key=\"draft.status.draft_complete\"></span>\n" +
    "              </div>\n" +
    "              <!-- incomplete -->\n" +
    "              <div ng-show=\"certForm.$pristine && !isComplete\"><span id=\"intyget-ej-komplett-meddelande\" message\n" +
    "                                                                     key=\"draft.status.draft_incomplete\"></span>\n" +
    "              </div>\n" +
    "              <!-- form changed -->\n" +
    "              <div ng-show=\"certForm.$dirty\"><span message key=\"draft.status.changed\"></span></div>\n" +
    "            </div>\n" +
    "          </div>\n" +
    "        </div>\n" +
    "      </div>\n" +
    "      <!-- top end -->\n" +
    "\n" +
    "      <!-- Total fail error messages -->\n" +
    "      <div id=\"error-panel\" class=\"cert-error-panel\"\n" +
    "           ng-show=\"widgetState.doneLoading && widgetState.activeErrorMessageKey && !widgetState.deleted\">\n" +
    "        <div class=\"alert alert-danger\">\n" +
    "          <span message key=\"{{widgetState.activeErrorMessageKey}}\"></span>\n" +
    "        </div>\n" +
    "      </div>\n" +
    "\n" +
    "      <!-- Integration deleted message -->\n" +
    "      <div id=\"integration-deleted\" ng-if=\"widgetState.deleted\">\n" +
    "        <div class=\"alert alert-info banner-margin\">\n" +
    "          Utkastet är raderat och borttaget från Webcert.\n" +
    "        </div>\n" +
    "      </div>\n" +
    "\n" +
    "      <!-- FK 7263 -->\n" +
    "      <div class=\"edit-form\" ng-class=\"{'collapsed' : widgetState.collapsedHeader}\">\n" +
    "\n" +
    "        <form name=\"certForm\" novalidate autocomplete=\"off\" wc-auto-save=\"save(true)\">\n" +
    "          <div ng-hide=\"widgetState.activeErrorMessageKey || widgetState.deleted\">\n" +
    "\n" +
    "            <div wc-spinner label=\"info.loadingdata\" show-spinner=\"!widgetState.doneLoading\"\n" +
    "                 show-content=\"widgetState.doneLoading\">\n" +
    "\n" +
    "              <div class=\"alert alert-danger\" ng-show=\"widgetState.showComplete && !isComplete\"\n" +
    "                   style=\"margin-right: 20px\">\n" +
    "                <h3><span message key=\"draft.saknar-uppgifter\"></span></h3>\n" +
    "                <ul>\n" +
    "                  <li ng-repeat=\"message in validationMessages\">\n" +
    "                    <span message key=\"{{message.message}}\"></span></li>\n" +
    "                </ul>\n" +
    "              </div>\n" +
    "\n" +
    "              <div id=\"certificate\" class=\"row certificate\">\n" +
    "                <div class=\"col-md-12 cert-body\">\n" +
    "\n" +
    "                  <!-- 1. Smittskydd -->\n" +
    "                  <div wc-field-single field-number=\"1\" field-help-text=\"fk7263.helptext.smittskydd\">\n" +
    "                    <label for=\"smittskydd\" class=\"control-label title-single\">\n" +
    "                      <input id=\"smittskydd\" name=\"smittskydd\" type=\"checkbox\" ng-model=\"cert.avstangningSmittskydd\"\n" +
    "                             ng-change=\"onChangeSmittskydd()\" wc-focus-on=\"firstInput\">&nbsp;<span message\n" +
    "                                                                                                   key=\"fk7263.label.smittskydd\"></span>\n" +
    "                    </label>\n" +
    "                  </div>\n" +
    "\n" +
    "                  <!-- 4b. Intyger baseras på -->\n" +
    "                  <div id=\"intygetbaseraspa\" wc-field field-label=\"fk7263.label.intyg-baserat-pa\" field-number=\"4b\"\n" +
    "                       field-help-text=\"fk7263.helptext.intyg-baserat-pa\" ng-hide=\"cert.avstangningSmittskydd\" class=\"field-fade\">\n" +
    "                    <div class=\"alert alert-danger\" ng-if=\"validationMessagesGrouped.intygbaseratpa\" id=\"validationMessages_intygbaseratpa\">\n" +
    "                      <ul>\n" +
    "                        <li ng-repeat=\"message in validationMessagesGrouped.intygbaseratpa\"><span message\n" +
    "                                                                                                  key=\"{{message.message}}\"></span>\n" +
    "                        </li>\n" +
    "                      </ul>\n" +
    "                    </div>\n" +
    "\n" +
    "                    <!-- Undersökning -->\n" +
    "                    <div class=\"form-group form-inline\">\n" +
    "                      <label class=\"checkbox inline-block\" style=\"min-width: 250px\"><input type=\"checkbox\"\n" +
    "                                                                                           id=\"basedOnExamination\"\n" +
    "                                                                                           name=\"informationBasedOn.examination\"\n" +
    "                                                                                           class=\"checkbox-inline\"\n" +
    "                                                                                           ng-model=\"basedOnState.check.undersokningAvPatienten\"\n" +
    "                                                                                           ng-change=\"toggleBaseradPaDate('undersokningAvPatienten')\">\n" +
    "                        Min undersökning av patienten</label>\n" +
    "                      <span wc-date-picker-field target-model=\"cert.undersokningAvPatienten\"\n" +
    "                            dom-id=\"undersokningAvPatientenDate\"></span>\n" +
    "                    </div>\n" +
    "\n" +
    "                    <!-- Telefonkontakt -->\n" +
    "                    <div class=\"form-group form-inline\">\n" +
    "                      <label class=\"checkbox inline-block\" style=\"min-width: 250px\"><input type=\"checkbox\"\n" +
    "                                                                                           id=\"basedOnPhoneContact\"\n" +
    "                                                                                           name=\"informationBasedOn.phoneContact\"\n" +
    "                                                                                           ng-model=\"basedOnState.check.telefonkontaktMedPatienten\"\n" +
    "                                                                                           ng-change=\"toggleBaseradPaDate('telefonkontaktMedPatienten')\">\n" +
    "                        Min telefonkontakt med patienten</label>\n" +
    "                      <span wc-date-picker-field target-model=\"cert.telefonkontaktMedPatienten\"\n" +
    "                            dom-id=\"telefonkontaktMedPatientenDate\"></span>\n" +
    "                    </div>\n" +
    "\n" +
    "                    <!-- Journaluppgifter -->\n" +
    "                    <div class=\"form-group form-inline\">\n" +
    "                      <label class=\"checkbox inline-block\" style=\"min-width: 250px\"><input type=\"checkbox\"\n" +
    "                                                                                           id=\"basedOnJournal\"\n" +
    "                                                                                           name=\"informationBasedOn.journal\"\n" +
    "                                                                                           ng-model=\"basedOnState.check.journaluppgifter\"\n" +
    "                                                                                           ng-change=\"toggleBaseradPaDate('journaluppgifter')\">\n" +
    "                        Journaluppgifter</label>\n" +
    "                      <span wc-date-picker-field target-model=\"cert.journaluppgifter\"\n" +
    "                            dom-id=\"journaluppgifterDate\"></span>\n" +
    "                    </div>\n" +
    "\n" +
    "                    <!-- Annat -->\n" +
    "                    <div class=\"form-group form-inline\">\n" +
    "                      <label class=\"checkbox inline-block\" style=\"min-width: 250px\"><input type=\"checkbox\"\n" +
    "                                                                                           id=\"basedOnOther\"\n" +
    "                                                                                           name=\"informationBasedOn.other\"\n" +
    "                                                                                           ng-model=\"basedOnState.check.annanReferens\"\n" +
    "                                                                                           ng-change=\"toggleBaseradPaDate('annanReferens')\">\n" +
    "                        Annat<span wc-enable-tooltip field-help-text=\"fk7263.helptext.intyg-baserat-pa.annat\"></span>\n" +
    "                      </label>\n" +
    "                      <span wc-date-picker-field target-model=\"cert.annanReferens\" dom-id=\"annanReferensDate\"></span>\n" +
    "                    </div>\n" +
    "                    <div class=\"otherText indent\" ng-if=\"basedOnState.check.annanReferens\">\n" +
    "                      <span>Ange vad:</span><br>\n" +
    "                      <textarea id=\"informationBasedOnOtherText\" name=\"informationBasedOn.otherText\" rows='2'\n" +
    "                                class='form-control otherInformation' ng-model=\"form.ovrigt.annanReferensBeskrivning\"\n" +
    "                                ng-change=\"limitOtherField('annanReferensBeskrivning')\"></textarea>\n" +
    "                      <div class=\"counter\">\n" +
    "                        Tecken kvar: {{inputLimits.ovrigt - getTotalOvrigtLength()}}\n" +
    "                      </div>\n" +
    "                      <div class=\"info-transfer\">Informationen överförs till \"Övriga upplysningar\" vid PDF-utskrift av\n" +
    "                        blanketten.\n" +
    "                      </div>\n" +
    "                    </div>\n" +
    "                  </div>\n" +
    "\n" +
    "                  <div wc-field field-label=\"fk7263.label.diagnos\" field-number=\"2\"\n" +
    "                       field-help-text=\"fk7263.helptext.diagnos\" ng-hide=\"cert.avstangningSmittskydd\"\n" +
    "                       class=\"print-pagebreak-after field-fade\" id=\"diagnoseForm\">\n" +
    "                    <div class=\"alert alert-danger\" ng-if=\"validationMessagesGrouped.diagnos\" id=\"validationMessages_diagnos\">\n" +
    "                      <ul>\n" +
    "                        <li ng-repeat=\"message in validationMessagesGrouped.diagnos\"><span message\n" +
    "                                                                                           key=\"{{message.message}}\"></span>\n" +
    "                        </li>\n" +
    "                      </ul>\n" +
    "                    </div>\n" +
    "                    <div class=\"row bottom-padding-small\">\n" +
    "                      <div class='col-xs-12'>\n" +
    "                        <div class='divider'>\n" +
    "                          <span message key=\"fk7263.label.valjkodverk\"></span>\n" +
    "                          <label class=\"radio-inline\">\n" +
    "                            <input type=\"radio\" id=\"diagnoseKodverk_ICD_10_SE\" name=\"diagnosKodverk\" value=\"ICD_10_SE\"\n" +
    "                                   ng-model=\"form.diagnosKodverk\" checked=\"checked\" ng-change=\"onChangeKodverk()\"><span message key=\"fk7263.label.diagnoskodverk.icd_10_se\"></span>\n" +
    "                          </label>\n" +
    "                          <label class=\"radio-inline\">\n" +
    "                            <input type=\"radio\" id=\"diagnoseKodverk_KSH_97_P\" name=\"diagnosKodverk\" value=\"KSH_97_P\"\n" +
    "                                   ng-model=\"form.diagnosKodverk\" ng-change=\"onChangeKodverk()\"><span message key=\"fk7263.label.diagnoskodverk.ksh_97_p\"></span>\n" +
    "                          </label>\n" +
    "                        </div>\n" +
    "                      </div>\n" +
    "                    </div>\n" +
    "                    <div class=\"row bottom-padding-small\">\n" +
    "                      <div class='col-xs-3'>\n" +
    "                        <label class=\"control-label unbreakable\">ICD-10</label>\n" +
    "                        <input type=\"text\" class=\"form-control\" id=\"diagnoseCode\" name=\"diagnose.code\" value=\"\"\n" +
    "                               class='diagnose-code col-xs-11 form-control' ng-model=\"cert.diagnosKod\"\n" +
    "                               typeahead=\"d.value as d.label for d in getDiagnoseCodes(form.diagnosKodverk, $viewValue) | filter:$viewValue | limitTo:10\"\n" +
    "                               typeahead-min-length=\"3\" typeahead-on-select=\"onDiagnoseCode1Select($item)\"\n" +
    "                               wc-to-uppercase\n" +
    "                            >\n" +
    "                      </div>\n" +
    "                      <div class='col-xs-9'>\n" +
    "                        <label class=\"control-label\">Diagnos</label>\n" +
    "                        <input type=\"text\" class=\"form-control maxwidth\" id=\"diagnoseDescription\"\n" +
    "                               class=\"col-xs-12 form-control\" name=\"diagnose.description\" value=\"\"\n" +
    "                               ng-model=\"cert.diagnosBeskrivning1\"\n" +
    "                               typeahead=\"d.value as d.label for d in searchDiagnoseByDescription(form.diagnosKodverk, $viewValue) | filter:$viewValue \"\n" +
    "                               typeahead-on-select=\"onDiagnoseDescription1Select($item)\"\n" +
    "                               typeahead-wait-ms=\"100\" ng-change=\"limitDiagnosBeskrivningField('diagnosBeskrivning1')\">\n" +
    "                        <span class=\"counter\">Tecken kvar: {{inputLimits.diagnosBeskrivning - getTotalDiagnosBeskrivningLength()}}</span>\n" +
    "                      </div>\n" +
    "                    </div>\n" +
    "                    <div class=\"row bottom-padding-tiny\">\n" +
    "                      <div class='col-xs-3'>\n" +
    "                        <label class=\"control-label unbreakable\">ICD-10</label>\n" +
    "                        <div class=\"controls\">\n" +
    "                          <div class=\"bottom-padding-small clearfix\">\n" +
    "                            <input type=\"text\" id=\"diagnoseCodeOpt1\" name=\"diagnose.codeOpt1\" value=\"\"\n" +
    "                                   ng-model=\"cert.diagnosKod2\" class=\"col-xs-11 form-control\"\n" +
    "                                   typeahead=\"d.value as d.label for d in getDiagnoseCodes(form.diagnosKodverk, $viewValue) | filter:$viewValue | limitTo:10\"\n" +
    "                                   typeahead-min-length=\"3\" typeahead-on-select=\"onDiagnoseCode2Select($item)\"\n" +
    "                                   ng-change=\"limitDiagnosBeskrivningField('diagnosKod2')\"\n" +
    "                                   wc-to-uppercase>\n" +
    "                          </div>\n" +
    "                        </div>\n" +
    "                      </div>\n" +
    "                      <div class='col-xs-9'>\n" +
    "                        <label class=\"control-label inline-block\"><span message\n" +
    "                                                                        key=\"fk7263.label.ytterligarediagnoser\"></span>\n" +
    "                          (frivilligt)</label><span wc-enable-tooltip class=\"absolute\"\n" +
    "                                                    field-help-text=\"fk7263.helptext.diagnos.ytterligare\"></span>\n" +
    "                        <div class=\"bottom-padding-small clearfix\">\n" +
    "                          <input type=\"text\" id=\"diagnoseDescriptionOpt1\" name=\"diagnose.descriptionOpt1\" value=\"\"\n" +
    "                                 ng-model=\"cert.diagnosBeskrivning2\" class=\"block col-xs-12 form-control maxwidth\"\n" +
    "                                 typeahead=\"d.value as d.label for d in searchDiagnoseByDescription(form.diagnosKodverk, $viewValue) | filter:$viewValue \"\n" +
    "                                 typeahead-on-select=\"onDiagnoseDescription2Select($item)\"\n" +
    "                                 typeahead-wait-ms=\"100\" ng-change=\"limitDiagnosBeskrivningField('diagnosBeskrivning2')\">\n" +
    "                          <span class=\"counter\">Tecken kvar: {{inputLimits.diagnosBeskrivning - getTotalDiagnosBeskrivningLength()}}</span>\n" +
    "                        </div>\n" +
    "                      </div>\n" +
    "                    </div>\n" +
    "                    <div class=\"row bottom-padding-small\">\n" +
    "                      <div class='col-xs-3'>\n" +
    "                        <input type=\"text\" id=\"diagnoseCodeOpt2\" name=\"diagnose.codeOpt2\" value=\"\"\n" +
    "                               ng-model=\"cert.diagnosKod3\" class=\"col-xs-11 form-control\"\n" +
    "                               typeahead=\"d.value as d.label for d in getDiagnoseCodes(form.diagnosKodverk, $viewValue) | filter:$viewValue | limitTo:10\"\n" +
    "                               typeahead-min-length=\"3\" typeahead-on-select=\"onDiagnoseCode3Select($item)\"\n" +
    "                               ng-change=\"limitDiagnosBeskrivningField('diagnosKod3')\"\n" +
    "                               wc-to-uppercase>\n" +
    "                      </div>\n" +
    "                      <div class='col-xs-9'>\n" +
    "                        <div class=\"controls\">\n" +
    "                          <input type=\"text\" id=\"diagnoseDescriptionOpt2\" name=\"diagnose.descriptionOpt2\" value=\"\"\n" +
    "                                 ng-model=\"cert.diagnosBeskrivning3\" class=\"block col-xs-12 form-control maxwidth\"\n" +
    "                                 typeahead=\"d.value as d.label for d in searchDiagnoseByDescription(form.diagnosKodverk, $viewValue) | filter:$viewValue \"\n" +
    "                                 typeahead-on-select=\"onDiagnoseDescription3Select($item)\"\n" +
    "                                 typeahead-wait-ms=\"100\"\n" +
    "                                 ng-change=\"limitDiagnosBeskrivningField('diagnosBeskrivning3')\">\n" +
    "                          <span class=\"counter\">Tecken kvar: {{inputLimits.diagnosBeskrivning - getTotalDiagnosBeskrivningLength()}}</span>\n" +
    "                        </div>\n" +
    "                      </div>\n" +
    "                    </div>\n" +
    "\n" +
    "                    <div class=\"form-group\">\n" +
    "                      <div>\n" +
    "                        <label class=\"control-label\"><span message key=\"fk7263.label.diagnosfortydligande\"></span></label>\n" +
    "                        <span wc-enable-tooltip field-help-text=\"fk7263.helptext.diagnos.fortydligande\"></span>\n" +
    "                      </div>\n" +
    "                      <textarea id=\"diagnoseClarification\" class=\"form-control\" name=\"diagnose.clarification\" rows='5'\n" +
    "                                ng-model=\"cert.diagnosBeskrivning\" ng-trim=\"false\"\n" +
    "                                ng-change=\"limitDiagnosBeskrivningField('diagnosBeskrivning')\"></textarea>\n" +
    "                      <span class=\"counter\">Tecken kvar: {{inputLimits.diagnosBeskrivning - getTotalDiagnosBeskrivningLength()}}</span>\n" +
    "                    </div>\n" +
    "\n" +
    "                    <div class=\"form-group form-inline\">\n" +
    "                      <label class=\"checkbox\">\n" +
    "                        <input type=\"checkbox\" id=\"diagnoseMultipleDiagnoses\" name=\"diagnose.multipleDiagnoses\"\n" +
    "                               ng-model=\"cert.samsjuklighet\"\n" +
    "                               ng-change=\"limitDiagnosBeskrivningField('samsjuklighet')\">\n" +
    "                        Samsjuklighet föreligger</label><span wc-enable-tooltip\n" +
    "                                                              field-help-text=\"fk7263.helptext.diagnos.samsjuklighet\"></span>\n" +
    "                    </div>\n" +
    "                  </div>\n" +
    "\n" +
    "                  <div wc-field field-label=\"fk7263.label.aktuellt-sjukdomsforlopp\" field-number=\"3\"\n" +
    "                       field-help-text=\"fk7263.helptext.aktuellt-sjukdomsforlopp\" ng-hide=\"cert.avstangningSmittskydd\" class=\"field-fade\">\n" +
    "                    <textarea id=\"diseaseCause\" class=\"form-control\" name=\"diseaseCause\" rows='5'\n" +
    "                              ng-model=\"cert.sjukdomsforlopp\" wc-maxlength ng-trim=\"false\"\n" +
    "                              maxlength=\"{{inputLimits.sjukdomsforlopp}}\"></textarea>\n" +
    "                  </div>\n" +
    "\n" +
    "                  <div wc-field field-label=\"fk7263.label.funktionsnedsattningar\" field-number=\"4\"\n" +
    "                       field-help-text=\"fk7263.helptext.funktionsnedsattning\" ng-hide=\"cert.avstangningSmittskydd\" class=\"field-fade\">\n" +
    "                    <div class=\"alert alert-danger\" ng-if=\"validationMessagesGrouped.funktionsnedsattning\" id=\"validationMessages_funktionsnedsattning\">\n" +
    "                      <ul>\n" +
    "                        <li ng-repeat=\"message in validationMessagesGrouped.funktionsnedsattning\"><span message\n" +
    "                                                                                                        key=\"{{message.message}}\"></span>\n" +
    "                        </li>\n" +
    "                      </ul>\n" +
    "                    </div>\n" +
    "\n" +
    "                    <a class=\"soclink\" href=\"http://www.socialstyrelsen.se/riktlinjer/forsakringsmedicinsktbeslutsstod\"\n" +
    "                       target=\"_blank\" title=\"Öppnar Socialstyrelsens beslutstöd för angiven huvuddiagnos\">Socialstyrelsens\n" +
    "                      försäkringsmedicinska beslutstöd</a>\n" +
    "                    <textarea id=\"disabilities\" class=\"form-control\" name=\"disabilities\" rows='6'\n" +
    "                              ng-model=\"cert.funktionsnedsattning\" wc-maxlength ng-trim=\"false\"\n" +
    "                              maxlength=\"{{inputLimits.funktionsnedsattning}}\"></textarea>\n" +
    "                  </div>\n" +
    "\n" +
    "                  <div wc-field field-label=\"fk7263.label.aktivitetsbegransning\" field-number=\"5\"\n" +
    "                       field-help-text=\"fk7263.helptext.aktivitetsbegransning\" ng-hide=\"cert.avstangningSmittskydd\" class=\"print-pagebreak-after field-fade\">\n" +
    "\n" +
    "                    <div class=\"alert alert-danger\" ng-if=\"validationMessagesGrouped.aktivitetsbegransning\" id=\"validationMessages_aktivitetsbegransning\">\n" +
    "                      <ul>\n" +
    "                        <li ng-repeat=\"message in validationMessagesGrouped.aktivitetsbegransning\">\n" +
    "                          <span message key=\"{{message.message}}\"></span>\n" +
    "                        </li>\n" +
    "                      </ul>\n" +
    "                    </div>\n" +
    "\n" +
    "                    <a class=\"soclink\" href=\"http://www.socialstyrelsen.se/riktlinjer/forsakringsmedicinsktbeslutsstod\"\n" +
    "                       target=\"_blank\" title=\"Öppnar Socialstyrelsens beslutstöd för angiven huvuddiagnos\">Socialstyrelsens\n" +
    "                      försäkringsmedicinska beslutstöd</a>\n" +
    "                    <textarea id=\"activityLimitation\" class=\"form-control\" name=\"activityLimitation\" rows='10'\n" +
    "                              ng-model=\"cert.aktivitetsbegransning\" wc-maxlength ng-trim=\"false\"\n" +
    "                              maxlength=\"{{inputLimits.aktivitetsbegransning}}\"></textarea>\n" +
    "                  </div>\n" +
    "\n" +
    "                  <!-- Arbete -->\n" +
    "                  <div wc-field field-label=\"fk7263.label.arbete\" field-number=\"8a\"\n" +
    "                       field-help-text=\"fk7263.helptext.sysselsattning\" ng-hide=\"cert.avstangningSmittskydd\" id=\"arbeteForm\" class=\"field-fade\">\n" +
    "                    <div class=\"alert alert-danger\" ng-if=\"validationMessagesGrouped.sysselsattning\" id=\"validationMessages_sysselsattning\">\n" +
    "                      <ul>\n" +
    "                        <li ng-repeat=\"message in validationMessagesGrouped.sysselsattning\"><span message\n" +
    "                                                                                                  key=\"{{message.message}}\"></span>\n" +
    "                        </li>\n" +
    "                      </ul>\n" +
    "                    </div>\n" +
    "\n" +
    "                    <div class=\"form-group form-inline\">\n" +
    "                      <label class=\"control-label\"><span message key=\"fk7263.label.arbetsformaga\"></span></label>\n" +
    "                      <div class=\"controls form-inline\">\n" +
    "                        <label class=\"checkbox\"><input type=\"checkbox\" id=\"arbeteNuvarande\"\n" +
    "                                                       ng-model=\"cert.nuvarandeArbete\"> Nuvarande\n" +
    "                          arbete</label><span wc-enable-tooltip\n" +
    "                                              field-help-text=\"fk7263.helptext.sysselsattning.nuvarande\"></span>\n" +
    "                        <div class=\"indent\" ng-if=\"cert.nuvarandeArbete\">\n" +
    "                          <label for=\"currentWork\">Ange aktuella arbetsuppgifter</label>\n" +
    "                          <div class=\"controls\">\n" +
    "                            <textarea id=\"currentWork\" class=\"\" name=\"currentWork\" rows='2'\n" +
    "                                      ng-model=\"form.nuvarandeArbetsuppgifter\" wc-maxlength ng-trim=\"false\"\n" +
    "                                      maxlength=\"{{inputLimits.nuvarandeArbetsuppgifter}}\"></textarea>\n" +
    "                          </div>\n" +
    "                        </div>\n" +
    "                      </div>\n" +
    "                    </div>\n" +
    "                    <div class=\"form-group form-inline\">\n" +
    "                      <label class=\"checkbox\"><input type=\"checkbox\" id=\"arbeteArbetslos\" ng-model=\"cert.arbetsloshet\">\n" +
    "                        Arbetslöshet - att utföra arbete på den\n" +
    "                        reguljära arbetsmarknaden</label><span wc-enable-tooltip\n" +
    "                                                               field-help-text=\"fk7263.helptext.sysselsattning.arbetsloshet\"></span>\n" +
    "                    </div>\n" +
    "                    <div class=\"form-group form-inline\">\n" +
    "                      <label class=\"checkbox\"><input type=\"checkbox\" id=\"arbeteForaldraledig\"\n" +
    "                                                     ng-model=\"cert.foraldrarledighet\">\n" +
    "                        Föräldraledighet med föräldrapenning -\n" +
    "                        att vårda sitt barn</label><span wc-enable-tooltip\n" +
    "                                                         field-help-text=\"fk7263.helptext.sysselsattning.foraldrarledighet\"></span>\n" +
    "                    </div>\n" +
    "                  </div>\n" +
    "\n" +
    "                  <!-- Arbetsförmåga -->\n" +
    "                  <div wc-field field-label=\"fk7263.label.nedsattning\" field-number=\"8b\"\n" +
    "                       field-help-text=\"fk7263.helptext.arbetsformaga\" class=\"arbetsformaga print-pagebreak-after\"\n" +
    "                       id=\"arbetsformagaForm\">\n" +
    "                    <div class=\"alert alert-danger\" ng-if=\"validationMessagesGrouped.arbetsformaga\" id=\"validationMessages_arbetsformaga\">\n" +
    "                      <ul>\n" +
    "                        <li ng-repeat=\"message in validationMessagesGrouped.arbetsformaga\"><span message\n" +
    "                                                                                                 key=\"{{message.message}}\"></span>\n" +
    "                        </li>\n" +
    "                      </ul>\n" +
    "                    </div>\n" +
    "\n" +
    "                    <div class=\"form-group form-inline\">\n" +
    "                      Faktisk tjänstgöringstid <input style=\"width: 3em;\" type=\"text\" wc-decimal-number\n" +
    "                                                      id=\"capacityForWorkActualWorkingHoursPerWeek\"\n" +
    "                                                      name=\"capacityForWork.actualWorkingHoursPerWeek\" value=\"\"\n" +
    "                                                      class='short control-element form-control' maxlength=\"5\"\n" +
    "                                                      ng-model=\"cert.tjanstgoringstid\"> <span>timmar/vecka</span> <span\n" +
    "                        wc-enable-tooltip\n" +
    "                        field-help-text=\"fk7263.helptext.arbetsformaga.faktisk-tjanstgoringstid\"></span>\n" +
    "                    </div>\n" +
    "\n" +
    "                    <!-- 25% -->\n" +
    "                    <div class=\"form-group form-inline\">\n" +
    "                      <label class=\"checkbox\" style=\"min-width: 250px\"><input type=\"checkbox\" id=\"nedsattMed25\"\n" +
    "                                                                              name=\"capacityForWork.work25\"\n" +
    "                                                                              ng-change=\"field8b.onChangeWorkStateCheck('nedsattMed25')\"\n" +
    "                                                                              ng-model=\"field8b.nedsattMed25.workState\">\n" +
    "                        25%\n" +
    "                        nedsatt arbetsförmåga från {{field8b.nedsattMed25.nedsattInvalidFrom}}</label>\n" +
    "                      <span wc-date-picker-field target-model=\"cert.nedsattMed25.from\"\n" +
    "                            on-change=\"onChangeNedsattMed('nedsattMed25', 'from')\" dom-id=\"nedsattMed25from\"\n" +
    "                            invalid=\"field8b.nedsattMed25.nedsattInvalidFrom\"></span>\n" +
    "                      <span>till</span>\n" +
    "                      <span wc-date-picker-field target-model=\"cert.nedsattMed25.tom\"\n" +
    "                            on-change=\"onChangeNedsattMed('nedsattMed25', 'tom')\" dom-id=\"nedsattMed25tom\"\n" +
    "                            invalid=\"field8b.nedsattMed25.nedsattInvalidTom\"></span>\n" +
    "                      <div class=\"indent pad10\" ng-if=\"field8b.nedsattMed25.workState\">\n" +
    "                        <div class=\"workHoursPerWeek\"\n" +
    "                             ng-show=\"cert.tjanstgoringstid != '' && cert.tjanstgoringstid > 0\">\n" +
    "                          Arbetstid timmar/vecka: <strong id=\"arbetstid25\">{{cert.tjanstgoringstid * 0.75 |\n" +
    "                          number}}</strong>\n" +
    "                        </div>\n" +
    "                        Arbetstidsförläggning (frivilligt) <input type=\"text\"\n" +
    "                                                                  ng-model=\"form.ovrigt.nedsattMed25Beskrivning\"\n" +
    "                                                                  id=\"nedsattMed25beskrivning\"\n" +
    "                                                                  id=\"capacityForWork.rec25LongerWorkingHours\"\n" +
    "                                                                  name=\"capacityForWork.rec25LongerWorkingHours\"\n" +
    "                                                                  value=\"\" class='form-control otherInformation'\n" +
    "                                                                  ng-change=\"limitOtherField('nedsattMed25Beskrivning')\"\n" +
    "                                                                  wc-disable-key-down>\n" +
    "                        <span class=\"counter\">Tecken kvar:  {{inputLimits.ovrigt - getTotalOvrigtLength()}}</span>\n" +
    "                        <div class=\"info-transfer\">Informationen överförs till \"Övriga upplysningar\" vid PDF-utskrift av\n" +
    "                          blanketten.\n" +
    "                        </div>\n" +
    "                      </div>\n" +
    "                    </div>\n" +
    "\n" +
    "                    <!-- 50% -->\n" +
    "                    <div class=\"form-group form-inline\">\n" +
    "                      <label class=\"checkbox\" style=\"min-width: 250px\"><input type=\"checkbox\" id=\"nedsattMed50\"\n" +
    "                                                                              name=\"capacityForWork.work50\"\n" +
    "                                                                              ng-change=\"field8b.onChangeWorkStateCheck('nedsattMed50')\"\n" +
    "                                                                              ng-model=\"field8b.nedsattMed50.workState\"> 50%\n" +
    "                        nedsatt arbetsförmåga från</label>\n" +
    "                      <span wc-date-picker-field target-model=\"cert.nedsattMed50.from\"\n" +
    "                            on-change=\"onChangeNedsattMed('nedsattMed50', 'from')\" dom-id=\"nedsattMed50from\"\n" +
    "                            invalid=\"field8b.nedsattMed50.nedsattInvalidFrom\"></span>\n" +
    "                      <span>till</span>\n" +
    "                      <span wc-date-picker-field target-model=\"cert.nedsattMed50.tom\"\n" +
    "                            on-change=\"onChangeNedsattMed('nedsattMed50', 'tom')\" dom-id=\"nedsattMed50tom\"\n" +
    "                            invalid=\"field8b.nedsattMed50.nedsattInvalidTom\"></span>\n" +
    "                      <div class=\"indent pad10\" ng-if=\"field8b.nedsattMed50.workState\">\n" +
    "                        <div class=\"workHoursPerWeek\"\n" +
    "                             ng-show=\"cert.tjanstgoringstid != '' && cert.tjanstgoringstid > 0\">\n" +
    "                          Arbetstid timmar/vecka: <strong id=\"arbetstid50\">{{cert.tjanstgoringstid * 0.5 |\n" +
    "                          number}}</strong>\n" +
    "                        </div>\n" +
    "                        Arbetstidsförläggning (frivilligt) <input type=\"text\"\n" +
    "                                                                  ng-model=\"form.ovrigt.nedsattMed50Beskrivning\"\n" +
    "                                                                  id=\"nedsattMed50beskrivning\"\n" +
    "                                                                  name=\"capacityForWork.rec50LongerWorkingHours\"\n" +
    "                                                                  value=\"\" class='form-control otherInformation'\n" +
    "                                                                  ng-change=\"limitOtherField('nedsattMed50Beskrivning')\"\n" +
    "                                                                  wc-disable-key-down>\n" +
    "                        <span class=\"counter\">Tecken kvar:  {{inputLimits.ovrigt - getTotalOvrigtLength()}}</span>\n" +
    "                        <div class=\"info-transfer\">Informationen överförs till \"Övriga upplysningar\" vid PDF-utskrift av\n" +
    "                          blanketten.\n" +
    "                        </div>\n" +
    "                      </div>\n" +
    "                    </div>\n" +
    "\n" +
    "                    <!-- 75% -->\n" +
    "                    <div class=\"form-group form-inline\">\n" +
    "                      <label class=\"checkbox\" style=\"min-width: 250px\"><input type=\"checkbox\" id=\"nedsattMed75\"\n" +
    "                                                                              name=\"capacityForWork.work75\"\n" +
    "                                                                              ng-change=\"field8b.onChangeWorkStateCheck('nedsattMed75')\"\n" +
    "                                                                              ng-model=\"field8b.nedsattMed75.workState\"> 75%\n" +
    "                        nedsatt arbetsförmåga från</label>\n" +
    "                      <span wc-date-picker-field target-model=\"cert.nedsattMed75.from\"\n" +
    "                            on-change=\"onChangeNedsattMed('nedsattMed75', 'from')\" dom-id=\"nedsattMed75from\"\n" +
    "                            invalid=\"field8b.nedsattMed75.nedsattInvalidFrom\"></span>\n" +
    "                      <span>till</span>\n" +
    "                      <span wc-date-picker-field target-model=\"cert.nedsattMed75.tom\"\n" +
    "                            on-change=\"onChangeNedsattMed('nedsattMed75', 'tom')\" dom-id=\"nedsattMed75tom\"\n" +
    "                            invalid=\"field8b.nedsattMed75.nedsattInvalidTom\"></span>\n" +
    "                      <div class=\"indent pad10\" ng-if=\"field8b.nedsattMed75.workState\">\n" +
    "                        <div class=\"workHoursPerWeek\"\n" +
    "                             ng-show=\"cert.tjanstgoringstid != '' && cert.tjanstgoringstid > 0\">\n" +
    "                          Arbetstid timmar/vecka: <strong id=\"arbetstid75\">{{cert.tjanstgoringstid * 0.25 |\n" +
    "                          number}}</strong>\n" +
    "                        </div>\n" +
    "                        Arbetstidsförläggning (frivilligt) <input type=\"text\"\n" +
    "                                                                  ng-model=\"form.ovrigt.nedsattMed75Beskrivning\"\n" +
    "                                                                  id=\"nedsattMed75beskrivning\"\n" +
    "                                                                  name=\"capacityForWork.rec75LongerWorkingHours\"\n" +
    "                                                                  value=\"\" class='form-control otherInformation'\n" +
    "                                                                  ng-change=\"limitOtherField('nedsattMed75Beskrivning')\"\n" +
    "                                                                  wc-disable-key-down>\n" +
    "                        <span class=\"counter\">Tecken kvar:  {{inputLimits.ovrigt - getTotalOvrigtLength()}}</span>\n" +
    "                        <div class=\"info-transfer\">Informationen överförs till \"Övriga upplysningar\" vid PDF-utskrift av\n" +
    "                          blanketten.\n" +
    "                        </div>\n" +
    "                      </div>\n" +
    "                    </div>\n" +
    "\n" +
    "                    <!-- 100% -->\n" +
    "                    <div class=\"form-group form-inline\">\n" +
    "                      <label class=\"checkbox\" style=\"min-width: 250px\"><input type=\"checkbox\" id=\"nedsattMed100\"\n" +
    "                                                                              name=\"capacityForWork.workFull\"\n" +
    "                                                                              ng-change=\"field8b.onChangeWorkStateCheck('nedsattMed100')\"\n" +
    "                                                                              ng-model=\"field8b.nedsattMed100.workState\"> Helt\n" +
    "                        nedsatt arbetsförmåga från</label>\n" +
    "                      <span wc-date-picker-field target-model=\"cert.nedsattMed100.from\"\n" +
    "                            on-change=\"onChangeNedsattMed('nedsattMed100', 'from')\" dom-id=\"nedsattMed100from\"\n" +
    "                            invalid=\"field8b.nedsattMed100.nedsattInvalidFrom\"></span>\n" +
    "                      <span>till</span>\n" +
    "                      <span wc-date-picker-field target-model=\"cert.nedsattMed100.tom\"\n" +
    "                            on-change=\"onChangeNedsattMed('nedsattMed100', 'tom')\" dom-id=\"nedsattMed100tom\"\n" +
    "                            invalid=\"field8b.nedsattMed100.nedsattInvalidTom\"></span>\n" +
    "                    </div>\n" +
    "\n" +
    "                    <div ng-show=\"totalCertDays\">\n" +
    "                      <p>Detta intyg motsvarar en period på: <span id=\"totalCertDays\">{{totalCertDays}}</span> <span\n" +
    "                          ng-show=\"totalCertDays > 1\">dagar</span><span ng-show=\"totalCertDays == 1\">dag</span></p>\n" +
    "                    </div>\n" +
    "                    <div class=\"alert alert-warning\" ng-show=\"datesOutOfRange\">\n" +
    "                      De datum du angett är <strong>mer än en vecka före dagens datum eller mer än 6 månader framåt i\n" +
    "                      tiden</strong>. Du bör kontrollera att tidsperioderna är korrekta.\n" +
    "                    </div>\n" +
    "                    <div class=\"alert alert-warning\" ng-show=\"datesPeriodTooLong\">\n" +
    "                      De datum du angett innebär <strong>en period på mer än 6 månader</strong>. Du bör kontrollera att\n" +
    "                      tidsperioderna är korrekta.\n" +
    "                    </div>\n" +
    "                  </div>\n" +
    "\n" +
    "                  <div wc-field field-label=\"fk7263.label.arbetsformaga.bedomning\" field-number=\"9\"\n" +
    "                       field-help-text=\"fk7263.helptext.arbetsformaga.bedoms-langre\">\n" +
    "                    <a class=\"soclink\" href=\"http://www.socialstyrelsen.se/riktlinjer/forsakringsmedicinsktbeslutsstod\"\n" +
    "                       target=\"_blank\" title=\"Öppnar Socialstyrelsens beslutstöd för angiven huvuddiagnos\">Socialstyrelsens\n" +
    "                      försäkringsmedicinska beslutstöd</a>\n" +
    "                    <textarea id=\"capacityForWorkText\" class=\"form-control\" name=\"capacityForWork.text\" rows='10'\n" +
    "                              ng-model=\"cert.arbetsformagaPrognos\" wc-maxlength ng-trim=\"false\"\n" +
    "                              maxlength=\"{{inputLimits.arbetsformagaPrognos}}\"></textarea>\n" +
    "                  </div>\n" +
    "\n" +
    "                  <!-- Prognos -->\n" +
    "                  <div wc-field field-label=\"fk7263.label.prognos\" field-number=\"10\"\n" +
    "                       field-help-text=\"fk7263.helptext.arbetsformaga.prognos\" id=\"prognosForm\">\n" +
    "\n" +
    "                    <div class=\"alert alert-danger\" ng-if=\"validationMessagesGrouped.prognos\" id=\"validationMessages_prognos\">\n" +
    "                      <ul>\n" +
    "                        <li ng-repeat=\"message in validationMessagesGrouped.prognos\"><span message\n" +
    "                                                                                           key=\"{{message.message}}\"></span>\n" +
    "                        </li>\n" +
    "                      </ul>\n" +
    "                    </div>\n" +
    "\n" +
    "                    <a class=\"soclink\" href=\"http://www.socialstyrelsen.se/riktlinjer/forsakringsmedicinsktbeslutsstod\"\n" +
    "                       target=\"_blank\" title=\"Öppnar Socialstyrelsens beslutstöd för angiven huvuddiagnos\">Socialstyrelsens\n" +
    "                      försäkringsmedicinska beslutstöd</a>\n" +
    "                    <div class=\"form-group\">\n" +
    "                      <label class=\"radio-inline\"><input type=\"radio\" name=\"capacityForWorkForecast\" value=\"YES\"\n" +
    "                                                         ng-model=\"form.prognos\" checked=\"checked\"> Ja</label>\n" +
    "                    </div>\n" +
    "\n" +
    "                    <div class=\"form-group\">\n" +
    "                      <label class=\"radio-inline\"><input type=\"radio\" id=\"capacityForWork2\"\n" +
    "                                                         name=\"capacityForWorkForecast\" value=\"PARTLY\"\n" +
    "                                                         ng-model=\"form.prognos\"> Ja, delvis</label>\n" +
    "                    </div>\n" +
    "\n" +
    "                    <div class=\"form-group\">\n" +
    "                      <label class=\"radio-inline\"><input type=\"radio\" id=\"capacityForWork3\"\n" +
    "                                                         name=\"capacityForWorkForecast\" value=\"NO\"\n" +
    "                                                         ng-model=\"form.prognos\"> Nej</label>\n" +
    "                    </div>\n" +
    "\n" +
    "                    <div class=\"form-group\">\n" +
    "                      <label class=\"radio-inline\"><input type=\"radio\" id=\"capacityForWork4\"\n" +
    "                                                         name=\"capacityForWorkForecast\" value=\"UNKNOWN\"\n" +
    "                                                         ng-model=\"form.prognos\"> Går ej att bedöma</label>\n" +
    "                      <div class=\"indent form-inline\" ng-show=\"form.prognos == 'UNKNOWN'\">\n" +
    "                        Förtydligande: <input type=\"text\"\n" +
    "                                              ng-model=\"form.ovrigt.arbetsformagaPrognosGarInteAttBedomaBeskrivning\"\n" +
    "                                              id=\"capacityForWorkForecastText\" name=\"capacityForWork.forecastText\"\n" +
    "                                              value=\"\" class='form-control otherInformation'\n" +
    "                                              ng-change=\"limitOtherField('arbetsformagaPrognosGarInteAttBedomaBeskrivning')\"> <span\n" +
    "                          id=\"prognosis-counter\">Tecken kvar:\n" +
    "                        {{inputLimits.ovrigt - getTotalOvrigtLength()}}</span>\n" +
    "                        <div class=\"info-transfer\">Informationen överförs till \"Övriga upplysningar\" vid PDF-utskrift av\n" +
    "                          blanketten.\n" +
    "                        </div>\n" +
    "                      </div>\n" +
    "                    </div>\n" +
    "                  </div>\n" +
    "\n" +
    "                  <!-- Åtgärder -->\n" +
    "                  <div wc-field field-label=\"fk7263.label.atgarder\" field-number=\"6b\"\n" +
    "                       ng-hide=\"cert.avstangningSmittskydd\" field-help-text=\"fk7263.helptext.atgarder\"\n" +
    "                       class=\"print-pagebreak-after\">\n" +
    "                    <div class=\"form-group\">\n" +
    "                      <label class=\"control-label\">Planerad eller pågående behandling eller åtgärd inom sjukvården (ange\n" +
    "                        vilken)</label>\n" +
    "                      <textarea id=\"measuresCurrent\" class=\"form-control\" name=\"measures.current\" rows='2'\n" +
    "                                ng-model=\"cert.atgardInomSjukvarden\" wc-maxlength ng-trim=\"false\"\n" +
    "                                maxlength=\"{{inputLimits.atgardInomSjukvarden}}\"></textarea>\n" +
    "                    </div>\n" +
    "\n" +
    "                    <div class=\"form-group\">\n" +
    "                      <label class=\"control-label\">Annan åtgärd (ange vilken)</label>\n" +
    "                      <textarea id=\"measuresOther\" class=\"form-control\" name=\"measures.other\" rows='2'\n" +
    "                                ng-model=\"cert.annanAtgard\" wc-maxlength ng-trim=\"false\"\n" +
    "                                maxlength=\"{{inputLimits.annanAtgard}}\"></textarea>\n" +
    "                    </div>\n" +
    "                  </div>\n" +
    "\n" +
    "                  <!-- Rekommendationer -->\n" +
    "                  <div wc-field field-label=\"fk7263.label.rekommendationer\" id=\"rekommendationerForm\"\n" +
    "                       field-number=\"6a, 7, 11\" field-help-text=\"fk7263.helptext.rekommendationer\">\n" +
    "\n" +
    "                    <div class=\"alert alert-danger\" ng-if=\"validationMessagesGrouped.rekommendationer\" id=\"validationMessages_rekommendationer\">\n" +
    "                      <ul>\n" +
    "                        <li ng-repeat=\"message in validationMessagesGrouped.rekommendationer\"><span message\n" +
    "                                                                                                    key=\"{{message.message}}\"></span>\n" +
    "                        </li>\n" +
    "                      </ul>\n" +
    "                    </div>\n" +
    "\n" +
    "                    <!-- 11 -->\n" +
    "                    <div>Resor till och från arbetet med annat färdmedel än normalt kan göra det möjligt för patienten\n" +
    "                      att återgå i arbete</label> <span wc-enable-tooltip\n" +
    "                                                        field-help-text=\"fk7263.helptext.rekommendationer.resor\"></span>\n" +
    "                    </div>\n" +
    "\n" +
    "                    <div class=\"form-group form-inline\" id=\"rekommendationRessattRadioGroup\">\n" +
    "                      <label class=\"radio-inline\">\n" +
    "                        <input type=\"radio\" id=\"rekommendationRessatt\" name=\"recommendationsToFkTravel\" value=\"JA\"\n" +
    "                               ng-model=\"form.ressattTillArbeteAktuellt\" > Ja</label>\n" +
    "                      <label class=\"radio-inline\">\n" +
    "                        <input type=\"radio\" id=\"rekommendationRessattEj\" name=\"recommendationsToFkTravel\" value=\"NEJ\"\n" +
    "                               ng-model=\"form.ressattTillArbeteAktuellt\" checked=\"checked\"> Nej</label>\n" +
    "                    </div>\n" +
    "\n" +
    "                    <!-- 6a, 7 -->\n" +
    "                    <div ng-hide=\"cert.avstangningSmittskydd\">\n" +
    "\n" +
    "                      <!-- 6a -->\n" +
    "                      <div class=\"form-group form-inline\">\n" +
    "                        <label class=\"checkbox\"><input type=\"checkbox\" id=\"rekommendationKontaktAf\"\n" +
    "                                                       name=\"recommendationsToFk.contactAf\"\n" +
    "                                                       ng-model=\"cert.rekommendationKontaktArbetsformedlingen\">\n" +
    "                          Patienten\n" +
    "                          bör komma i kontakt med arbetsförmedlingen</label> <span wc-enable-tooltip\n" +
    "                                                                                   field-help-text=\"fk7263.helptext.rekommendationer.arbetsformedlingen\"></span>\n" +
    "                      </div>\n" +
    "                      <div class=\"form-group form-inline\">\n" +
    "                        <label class=\"checkbox\"><input type=\"checkbox\" id=\"rekommendationKontaktForetagshalsovard\"\n" +
    "                                                       name=\"recommendationsToFk.contactCompanyCare\"\n" +
    "                                                       ng-model=\"cert.rekommendationKontaktForetagshalsovarden\">\n" +
    "                          Patienten\n" +
    "                          bör komma i kontakt med företagshälsovården</label> <span wc-enable-tooltip\n" +
    "                                                                                    field-help-text=\"fk7263.helptext.rekommendationer.foretagshalsovarden\"></span>\n" +
    "                      </div>\n" +
    "                      <div class=\"form-group form-inline\">\n" +
    "                        <label class=\"checkbox\"><input type=\"checkbox\" id=\"rekommendationOvrigt\"\n" +
    "                                                       name=\"recommendationsToFk.other\"\n" +
    "                                                       ng-model=\"cert.rekommendationOvrigtCheck\"> Övrigt (ange\n" +
    "                          vad)</label><span wc-enable-tooltip\n" +
    "                                            field-help-text=\"fk7263.helptext.rekommendationer.ovrigt\"></span>\n" +
    "                        <div class=\"indent\" ng-if=\"cert.rekommendationOvrigtCheck\">\n" +
    "                          <input type=\"text\" id=\"rekommendationOvrigtBeskrivning\" name=\"recommendationsToFk.otherText\"\n" +
    "                                 value=\"\" class='form-control maxwidth' ng-model=\"form.rekommendationOvrigt\"\n" +
    "                                 maxlength=\"80\" wc-maxlength>\n" +
    "\n" +
    "                        </div>\n" +
    "                      </div>\n" +
    "\n" +
    "                      <!-- 7 -->\n" +
    "                      <span id=\"recommendationsToFkReabInQuestionRadioGroup\">\n" +
    "                      <h4 class=\"inline-block\">Är arbetslivsinriktad rehabilitering aktuell?</h4><span wc-enable-tooltip\n" +
    "                                                                                                       field-help-text=\"fk7263.helptext.rekommendationer.arbetslivsinriktad-rehabilitering\"></span>\n" +
    "\n" +
    "                      <!-- Rehab -->\n" +
    "                      <!-- YES -->\n" +
    "                      <div class=\"form-group\">\n" +
    "                        <label class=\"radio-inline\"><input type=\"radio\" name=\"recommendationsToFkReabInQuestion\"\n" +
    "                                                           value=\"JA\" ng-model=\"form.rehab\" id=\"rehabYes\"> Ja</label>\n" +
    "                      </div>\n" +
    "\n" +
    "                      <!-- NO -->\n" +
    "                      <div class=\"form-group\">\n" +
    "                        <label class=\"radio-inline\"><input type=\"radio\" name=\"recommendationsToFkReabInQuestion\"\n" +
    "                                                           group=\"group3\" value=\"NEJ\" ng-model=\"form.rehab\"\n" +
    "                                                           checked=\"checked\" id=\"rehabNo\"> Nej</label>\n" +
    "                      </div>\n" +
    "\n" +
    "                      <!-- GÅR EJ ATT BEDÖMA -->\n" +
    "                      <div class=\"form-group\">\n" +
    "                        <label class=\"radio-inline\"><input type=\"radio\" name=\"recommendationsToFkReabInQuestion\"\n" +
    "                                                           group=\"group3\" value=\"GAREJ\" ng-model=\"form.rehab\" id=\"garej\"> Går inte\n" +
    "                          att bedöma</label>\n" +
    "                      </div>\n" +
    "                      </span>\n" +
    "                    </div>\n" +
    "                  </div>\n" +
    "\n" +
    "                  <div wc-field field-label=\"fk7263.label.fk-kontakt\" field-number=\"12\"\n" +
    "                       field-help-text=\"fk7263.helptext.kontakt\">\n" +
    "                    <label><input type=\"checkbox\" id=\"kontaktFk\" name=\"contactWithFkRequested\"\n" +
    "                                  ng-model=\"cert.kontaktMedFk\"> Kontakt önskas med\n" +
    "                      Försäkringskassan</label>\n" +
    "                  </div>\n" +
    "\n" +
    "                  <div wc-field field-label=\"fk7263.label.ovrigt\" field-number=\"13\"\n" +
    "                       field-help-text=\"fk7263.helptext.ovrigt\" class=\"print-pagebreak-after\">\n" +
    "                    <textarea id=\"otherInformation\" name=\"otherInformation\" rows='10'\n" +
    "                              class='form-control otherInformation' ng-model=\"cert.kommentar\"\n" +
    "                              ng-change=\"limitOtherField('kommentar')\"></textarea>\n" +
    "                    <div class=\"counter\">Tecken kvar: {{inputLimits.ovrigt - getTotalOvrigtLength()}}</div>\n" +
    "                  </div>\n" +
    "\n" +
    "                  <div wc-field field-label=\"fk7263.label.vardenhet.adress\" field-number=\"15\"\n" +
    "                       field-help-text=\"fk7263.helptext.adress\" id=\"vardenhetForm\">\n" +
    "                    <div class=\"alert alert-danger\" ng-if=\"validationMessagesGrouped.vardperson\" id=\"validationMessages_vardperson\">\n" +
    "                      <ul>\n" +
    "                        <li ng-repeat=\"message in validationMessagesGrouped.vardperson\"><span message\n" +
    "                                                                                              key=\"{{message.message}}\"></span>\n" +
    "                        </li>\n" +
    "                      </ul>\n" +
    "                    </div>\n" +
    "\n" +
    "                    <label class=\"control-label\">{{cert.grundData.skapadAv.vardenhet.enhetsnamn}}\n" +
    "                      ({{cert.grundData.skapadAv.vardenhet.enhetsid}}),\n" +
    "                      {{cert.grundData.skapadAv.vardenhet.vardgivare.vardgivarnamn}}</label>\n" +
    "\n" +
    "                    <div class=\"alert alert-info\" ng-show=\"widgetState.hasInfoMissing\">\n" +
    "                      Information om vårdenheten saknas i HSA-katalogen. Be den på vårdenheten som är ansvarig för\n" +
    "                      frågor om HSA att uppdatera informationen.\n" +
    "                    </div>\n" +
    "\n" +
    "                    <div class=\"form-horizontal\">\n" +
    "                      <div class=\"form-group\">\n" +
    "                        <label class=\"col-xs-3 control-label\">Postadress</label>\n" +
    "                        <div class=\"col-xs-9\">\n" +
    "                          <input type=\"text\" class=\"form-control input-full\" id=\"clinicInfoPostalAddress\"\n" +
    "                                 name=\"clinicInfo.postalAddress\"\n" +
    "                                 ng-model=\"cert.grundData.skapadAv.vardenhet.postadress\">\n" +
    "                        </div>\n" +
    "                      </div>\n" +
    "                      <div class=\"form-group\">\n" +
    "                        <label class=\"col-xs-3 control-label\">Postnummer</label>\n" +
    "                        <div class=\"col-xs-9\">\n" +
    "                          <input type=\"text\" class=\"form-control input-5\" id=\"clinicInfoPostalCode\"\n" +
    "                                 name=\"clinicInfo.postalCode\" ng-model=\"cert.grundData.skapadAv.vardenhet.postnummer\">\n" +
    "                        </div>\n" +
    "                      </div>\n" +
    "                      <div class=\"form-group\">\n" +
    "                        <label class=\"col-xs-3 control-label\">Postort</label>\n" +
    "                        <div class=\"col-xs-9\">\n" +
    "                          <input type=\"text\" class=\"form-control\" id=\"clinicInfoPostalCity\" name=\"clinicInfo.postalCity\"\n" +
    "                                 ng-model=\"cert.grundData.skapadAv.vardenhet.postort\">\n" +
    "                        </div>\n" +
    "                      </div>\n" +
    "                      <div class=\"form-group\">\n" +
    "                        <label class=\"col-xs-3 control-label\">Telefonnummer</label>\n" +
    "                        <div class=\"col-xs-9\">\n" +
    "                          <input type=\"text\" class=\"form-control\" id=\"clinicInfoPhone\" name=\"clinicInfo.phone\"\n" +
    "                                 ng-model=\"cert.grundData.skapadAv.vardenhet.telefonnummer\" >\n" +
    "                        </div>\n" +
    "                      </div>\n" +
    "                    </div>\n" +
    "                  </div>\n" +
    "\n" +
    "                  <div wc-field-signing-doctor></div>\n" +
    "\n" +
    "                </div>\n" +
    "                <!-- cert body -->\n" +
    "\n" +
    "              </div>\n" +
    "              <!-- certificate -->\n" +
    "\n" +
    "              <div ng-if=\"!isSigned\" class=\"print-hide\">\n" +
    "\n" +
    "                <h3><span message key=\"common.sign.intyg\"></span></h3>\n" +
    "\n" +
    "                <div ng-if=\"!user.userContext.lakare\">\n" +
    "                  <div id=\"sign-requires-doctor-message-text\" class=\"alert alert-info\">\n" +
    "                    <span message key=\"draft.onlydoctorscansign\"></span>\n" +
    "                  </div>\n" +
    "\n" +
    "                  <div wc-feature-not-active feature=\"franJournalsystem\">\n" +
    "                    <p>\n" +
    "                      <span message key=\"draft.notifydoctor\"></span>\n" +
    "                    </p>\n" +
    "                    <div class=\"form-group\">\n" +
    "                      <button id=\"vidarebefordraEjHanterad\" class=\"btn\"\n" +
    "                              ng-class=\"{'btn-info': !certMeta.vidarebefordrad, 'btn-default btn-secondary' : certMeta.vidarebefordrad}\"\n" +
    "                              title=\"Skicka mejl med en länk till utkastet för att informera den läkare som ska signera det.\"\n" +
    "                              ng-click=\"openMailDialog()\">\n" +
    "                        <img ng-if=\"!certMeta.vidarebefordrad\" src=\"/img/mail.png\"> <img ng-if=\"certMeta.vidarebefordrad\"\n" +
    "                                                                                         src=\"/img/mail_dark.png\">\n" +
    "                      </button>\n" +
    "                    </div>\n" +
    "\n" +
    "                    <div class=\"body-row\">\n" +
    "                      <label for=\"mark-as-vidarebefordrad\">\n" +
    "                        <input id=\"mark-as-vidarebefordrad\" type=\"checkbox\"\n" +
    "                               ng-disabled=\"widgetState.vidarebefordraInProgress\" ng-model=\"certMeta.vidarebefordrad\"\n" +
    "                               ng-change=\"onVidarebefordradChange()\" /> Vidarebefordrad\n" +
    "                      </label> <span ng-if=\"widgetState.vidarebefordraInProgress\"> <img\n" +
    "                        src=\"/img/ajax-loader-kit-16x16.gif\"></span>\n" +
    "                    </div>\n" +
    "                  </div>\n" +
    "                </div>\n" +
    "\n" +
    "                <button id=\"signera-utkast-button\" class=\"btn btn-success\" ng-disabled=\"!isComplete || certForm.$dirty || signingWithSITHSInProgress\"\n" +
    "                        ng-click=\"sign()\" ng-show=\"user.userContext.lakare\"><img ng-show=\"signingWithSITHSInProgress\" ng-src=\"/img/ajax-loader-small-green.gif\"/> <span message key=\"common.sign\"></span>\n" +
    "                </button>\n" +
    "\n" +
    "              </div>\n" +
    "\n" +
    "            </div>\n" +
    "          </div>\n" +
    "\n" +
    "          <wc-disable-key-down wc-disable-key-down-elements=\":text,:checkbox,:radio\" wc-disable-key-down-done-loading=\"widgetState.doneLoading\" />\n" +
    "\n" +
    "        </form>\n" +
    "      </div>\n" +
    "\n" +
    "    </div>\n" +
    "    <!-- span12 webcert-col-single -->\n" +
    "  </div>\n" +
    "  <!-- row -->\n" +
    "</div> <!-- container -->\n"
  );


  $templateCache.put('/web/webjars/fk7263/webcert/views/fragasvar.html',
    "<div class=\"row weak-hidden\" ng-class=\"{show: widgetState.showTemplate}\" ng-controller=\"fk7263.QACtrl\">\n" +
    "  <div class=\"col-md-12 qa-column\">\n" +
    "\n" +
    "    <div wc-spinner label=\"fk7263.info.loading.existing.qa\" show-spinner=\"!widgetState.doneLoading\"\n" +
    "         show-content=\"widgetState.doneLoading\">\n" +
    "\n" +
    "      <div class=\"webcert-bottom-padding-section\" ng-show=\"certProperties.isLoaded\">\n" +
    "\n" +
    "        <!-- Question button -->\n" +
    "        <div\n" +
    "            ng-show=\"!certProperties.isRevoked && !widgetState.newQuestionOpen && (certProperties.isSent || qaList.length>0)\"\n" +
    "            wc-check-vardenhet vardenhet=\"{{cert.grundData.skapadAv.vardenhet.enhetsid}}\">\n" +
    "          <div class=\"form-group\">\n" +
    "            <button class=\"btn btn-success\" ng-click=\"toggleQuestionForm()\" id=\"askQuestionBtn\">Ny fråga till\n" +
    "              Försäkringskassan\n" +
    "            </button>\n" +
    "          </div>\n" +
    "        </div>\n" +
    "        <div ng-if=\"certProperties.isSent === false && (qaList.length<1)\" wc-check-vardenhet\n" +
    "             vardenhet=\"{{cert.grundData.skapadAv.vardenhet.enhetsid}}\" class=\"alert alert-info\"\n" +
    "             id=\"certificate-is-not-sent-to-fk-message-text\">\n" +
    "          <strong>Intyget är inte skickat till Försäkringskassan.</strong><br> Det går därför inte att ställa frågor om\n" +
    "          intyget.\n" +
    "        </div>\n" +
    "        <div ng-if=\"certProperties.isSent === undefined && (qaList.length<1)\" wc-check-vardenhet\n" +
    "             vardenhet=\"{{cert.grundData.skapadAv.vardenhet.enhetsid}}\" class=\"alert alert-info\"\n" +
    "             id=\"certificate-is-not-available\">\n" +
    "          <strong>Det finns inga frågor och svar.</strong>\n" +
    "        </div>\n" +
    "\n" +
    "        <!-- New question form -->\n" +
    "        <div ng-show=\"widgetState.newQuestionOpen\">\n" +
    "          <div class=\"form-group\">\n" +
    "            <label class=\"control-label\">Fråga</label>\n" +
    "            <textarea wc-maxlength ng-trim=\"false\" maxlength=\"2000\" rows=\"5\" class=\"form-control\"\n" +
    "                      ng-model=\"newQuestion.frageText\" wc-focus-me=\"widgetState.focusQuestion\"\n" +
    "                      id=\"newQuestionText\"></textarea>\n" +
    "          </div>\n" +
    "          <div class=\"form-group\" id=\"newQuestionForm\">\n" +
    "            <label class=\"control-label\">Ämne</label>\n" +
    "            <div class=\"controls\">\n" +
    "              <select id=\"new-question-topic\" class=\"form-control\" ng-model=\"newQuestion.chosenTopic\"\n" +
    "                      ng-options=\"c.label for c in newQuestion.topics\"></select>\n" +
    "            </div>\n" +
    "          </div>\n" +
    "\n" +
    "          <!-- error message occuring when inteacting with server for this FragaSvar-->\n" +
    "          <div id=\"newQuestion-load-error\" ng-show=\"newQuestion.activeErrorMessageKey\" class=\"alert alert-danger\">\n" +
    "            <span message key=\"fk7263.error.{{newQuestion.activeErrorMessageKey}}\"></span>\n" +
    "          </div>\n" +
    "\n" +
    "          <!-- Form button group -->\n" +
    "          <div class=\"form-group\">\n" +
    "            <button class=\"btn btn-success\" ng-click=\"sendQuestion(newQuestion)\"\n" +
    "                    ng-disabled=\"!questionValidForSubmit(newQuestion)\" title=\"{{newQuestion.sendButtonToolTip}}\"\n" +
    "                    id=\"sendQuestionBtn\">\n" +
    "              <img src=\"/img/loader-small-green.gif\" ng-show=\"newQuestion.updateInProgress\"> Skicka till\n" +
    "              Försäkringskassan\n" +
    "            </button>\n" +
    "            <button class=\"btn btn-default btn-secondary\" ng-click=\"toggleQuestionForm()\" id=\"cancelQuestionBtn\">Avbryt\n" +
    "              fråga\n" +
    "            </button>\n" +
    "          </div>\n" +
    "        </div>\n" +
    "        <!--  Fragasvar formular end -->\n" +
    "\n" +
    "        <!-- question sent message -->\n" +
    "        <div ng-if=\"widgetState.sentMessage\">\n" +
    "          <div alert type=\"info\" close=\"dismissProxy()\" id=\"question-is-sent-to-fk-message-text\">\n" +
    "            <strong>Frågan är skickad till Försäkringskassan</strong><br><span wc-feature-not-active\n" +
    "                                                                               feature=\"franJournalsystem\"> Vårdenheten kontaktas via mejl när svar har inkommit.</span>\n" +
    "          </div>\n" +
    "        </div>\n" +
    "\n" +
    "        <!-- error message -->\n" +
    "        <div id=\"qa-list-load-error\" ng-show=\"widgetState.doneLoading && widgetState.activeErrorMessageKey\"\n" +
    "             class=\"alert alert-danger\">\n" +
    "          <span message key=\"{{widgetState.activeErrorMessageKey}}\"></span>\n" +
    "        </div>\n" +
    "      </div>\n" +
    "\n" +
    "      <div class=\"webcert-col-section qa-list-section\" ng-show=\"(qaList | filter:openIssuesFilter).length > 0\" id=\"unhandledQACol\">\n" +
    "\n" +
    "        <h2 class=\"col-head\">Ej hanterade frågor och svar</h2>\n" +
    "        <div ng-repeat=\"qa in qaList | filter:openIssuesFilter | orderBy:'senasteHandelseDatum':true\" class=\"qa-item\">\n" +
    "\n" +
    "          <div qa-panel panel-id=\"unhandled\" qa=\"qa\" qa-list=\"qaList\" cert=\"cert\" cert-properties=\"certProperties\"></div>\n" +
    "\n" +
    "        </div>\n" +
    "\n" +
    "      </div>\n" +
    "\n" +
    "    </div>\n" +
    "\n" +
    "    <!-- ------------------------- HANTERADE FRÅGOR --------------------------------------------- -->\n" +
    "\n" +
    "    <div class=\"webcert-col-section qa-list-section\" ng-show=\"(qaList | filter:closedIssuesFilter).length > 0\">\n" +
    "\n" +
    "      <h2 class=\"col-head\">Hanterade frågor och svar</h2>\n" +
    "      <div ng-repeat=\"qa in qaList | filter:closedIssuesFilter | orderBy:'senasteHandelseDatum':true\" class=\"qa-item\">\n" +
    "\n" +
    "        <div qa-panel panel-id=\"handled\" type=\"handled\" qa=\"qa\" qa-list=\"qaList\" cert=\"cert\" cert-properties=\"certProperties\"></div>\n" +
    "\n" +
    "      </div>\n" +
    "\n" +
    "    </div>\n" +
    "    <!--  end ng-repeat -->\n" +
    "\n" +
    "  </div>\n" +
    "  <!--  end spinner -->\n" +
    "\n" +
    "</div>\n"
  );

}]);
