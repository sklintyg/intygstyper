angular.module('fk7263').run(['$templateCache', function($templateCache) {
  'use strict';

  $templateCache.put('/web/webjars/fk7263/webcert/js/directives/qaPanel.html',
    "<div>\r" +
    "\n" +
    "  <div ng-if=\"qa.proxyMessage\">\r" +
    "\n" +
    "    <div alert type=\"info\" close=\"dismissProxy(qa)\">\r" +
    "\n" +
    "      <span message key=\"{{qa.proxyMessage}}\"></span>\r" +
    "\n" +
    "    </div>\r" +
    "\n" +
    "  </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "  <div ng-class=\"{'panel-container': true, 'revoked-certificate' : certProperties.isRevoked}\">\r" +
    "\n" +
    "\r" +
    "\n" +
    "    <div class=\"qa-panel\" id=\"qa{{panelId}}-{{qa.internReferens}}\" ng-show=\"!qa.proxyMessage\">\r" +
    "\n" +
    "\r" +
    "\n" +
    "      <div ng-if=\"handledPanel\">\r" +
    "\n" +
    "        <h3 class=\"inline-block\">\r" +
    "\n" +
    "          Ämne: <strong><span message key=\"qa.amne.{{qa.amne | lowercase}}\"></span></strong><span\r" +
    "\n" +
    "            id=\"fkMeddelandeRubrik-{{qa.internReferens}}\" ng-show=\"qa.meddelandeRubrik\"> - {{qa.meddelandeRubrik}}</span>\r" +
    "\n" +
    "        </h3>\r" +
    "\n" +
    "      </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "      <div ng-if=\"!handledPanel\" class=\"row\">\r" +
    "\n" +
    "        <div class=\"col-xs-9\">\r" +
    "\n" +
    "          <div class=\"status-header\">\r" +
    "\n" +
    "            <h3 class=\"inline-block\">\r" +
    "\n" +
    "              Ämne: <strong><span message key=\"qa.amne.{{qa.amne | lowercase}}\"></span></strong><span\r" +
    "\n" +
    "                id=\"fkMeddelandeRubrik-{{qa.internReferens}}\" ng-show=\"qa.meddelandeRubrik\"> - {{qa.meddelandeRubrik}}</span>\r" +
    "\n" +
    "            </h3>\r" +
    "\n" +
    "            <p>\r" +
    "\n" +
    "              Åtgärd: <strong><span message key=\"qa.measure.{{qa.measureResKey}}\"></span></strong>\r" +
    "\n" +
    "            </p>\r" +
    "\n" +
    "            <div wc-feature-not-active feature=\"franJournalsystem\" class=\"form-inline\"\r" +
    "\n" +
    "                 ng-show=\"!certProperties.isRevoked\">\r" +
    "\n" +
    "              <label for=\"{{panelId}}-mark-as-forwarded-{{qa.internReferens}}\" class=\"checkbox-inline\">\r" +
    "\n" +
    "                <input id=\"{{panelId}}-mark-as-forwarded-{{qa.internReferens}}\" type=\"checkbox\"\r" +
    "\n" +
    "                       ng-disabled=\"qa.forwardInProgress\" ng-model=\"qa.vidarebefordrad\"\r" +
    "\n" +
    "                       ng-change=\"onVidareBefordradChange(qa)\" /> Vidarebefordrad</label> <span\r" +
    "\n" +
    "                ng-if=\"qa.forwardInProgress\"> <img src=\"/img/ajax-loader-kit-16x16.gif\"></span>\r" +
    "\n" +
    "            </div>\r" +
    "\n" +
    "          </div>\r" +
    "\n" +
    "        </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "        <div wc-feature-not-active feature=\"franJournalsystem\" class=\"col-xs-3\">\r" +
    "\n" +
    "          <!--  status header -->\r" +
    "\n" +
    "          <button id=\"{{panelId}}-vidarebefordraEjHanterad\" class=\"btn pull-right\"\r" +
    "\n" +
    "                  ng-class=\"{'btn-info': !qa.vidarebefordrad, 'btn-default btn-secondary' : qa.vidarebefordrad}\"\r" +
    "\n" +
    "                  title=\"Skicka mejl med en länk till intyget för att informera den som ska hantera frågan-svaret.\"\r" +
    "\n" +
    "                  ng-click=\"openMailDialog(qa)\" ng-show=\"!certProperties.isRevoked\">\r" +
    "\n" +
    "            <img ng-if=\"!qa.vidarebefordrad\" src=\"/img/mail.png\"> <img ng-if=\"qa.vidarebefordrad\"\r" +
    "\n" +
    "                                                                       src=\"/img/mail_dark.png\">\r" +
    "\n" +
    "          </button>\r" +
    "\n" +
    "        </div>\r" +
    "\n" +
    "      </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "      <!-- Frågan -->\r" +
    "\n" +
    "      <div class=\"panel-container\">\r" +
    "\n" +
    "        <div class=\"qa-block\"\r" +
    "\n" +
    "             ng-class=\"{'qa-block-handled' : !handledPanel && qa.status == 'ANSWERED', 'qa-block-fk' : (qa.frageStallare | lowercase) == 'fk', 'qa-block-wc' : (qa.frageStallare | lowercase) == 'wc'}\">\r" +
    "\n" +
    "          <div class=\"qa-block-head clearfix\">\r" +
    "\n" +
    "            <div class=\"pull-left\" ng-switch=\"qa.frageStallare | lowercase\">\r" +
    "\n" +
    "              Från: <span class=\"qa-sender\" ng-switch-when=\"wc\"\r" +
    "\n" +
    "                          id=\"{{panelId}}-question-fraga-vard-aktor-namn-{{qa.internReferens}}\">{{qa.vardAktorNamn}}</span>\r" +
    "\n" +
    "              <span ng-switch-default message key=\"qa.fragestallare.{{qa.frageStallare | lowercase}}\"></span>\r" +
    "\n" +
    "              <div id=\"{{panelId}}-fkKontakter-{{qa.internReferens}}\" ng-repeat=\"kontakt in qa.externaKontakter\">\r" +
    "\n" +
    "                {{kontakt}}<br />\r" +
    "\n" +
    "              </div>\r" +
    "\n" +
    "            </div>\r" +
    "\n" +
    "            <!-- Fraga fran fk eller wc? -->\r" +
    "\n" +
    "            <div class=\"pull-right\" ng-switch=\"qa.frageStallare | lowercase\">\r" +
    "\n" +
    "              <span ng-switch-when=\"wc\">Skickat: </span> <span ng-switch-default>Mottaget: </span> <span\r" +
    "\n" +
    "                class=\"qa-date\" id=\"{{panelId}}-qa-skickaddatum-{{qa.internReferens}}\">{{qa.frageSkickadDatum | date:'short'}}</span>\r" +
    "\n" +
    "            </div>\r" +
    "\n" +
    "          </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "          <div class=\"qa-block-body\" id=\"{{panelId}}-qa-fragetext-{{qa.internReferens}}\">{{qa.frageText}}</div>\r" +
    "\n" +
    "          <div class=\"qa-block-body komplettera-block\" id=\"{{panelId}}-fkKompletteringar-{{qa.internReferens}}\"\r" +
    "\n" +
    "               ng-repeat=\"komplettering in qa.kompletteringar\">\r" +
    "\n" +
    "            <strong><span>{{komplettering.falt}}</span></strong>\r" +
    "\n" +
    "            <div>\r" +
    "\n" +
    "              <span>{{komplettering.text}}</span>\r" +
    "\n" +
    "            </div>\r" +
    "\n" +
    "          </div>\r" +
    "\n" +
    "        </div>\r" +
    "\n" +
    "      </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "      <!-- Svarsdelen -->\r" +
    "\n" +
    "\r" +
    "\n" +
    "      <div class=\"panel-container\">\r" +
    "\n" +
    "        <div class=\"qa-block\"\r" +
    "\n" +
    "             ng-class=\"{'qa-block-handled': handledPanel, 'qa-block-fk' : (qa.frageStallare | lowercase) == 'wc', 'qa-block-wc' : (qa.frageStallare | lowercase) == 'fk'}\"\r" +
    "\n" +
    "             ng-if=\"(handledPanel && qa.svarsText) || (!handledPanel && qa.status == 'ANSWERED')\">\r" +
    "\n" +
    "\r" +
    "\n" +
    "          <!-- UNHANDLEDTYPE: frågan är redan BESVARAD -->\r" +
    "\n" +
    "          <!-- HANDLEDTYPE: frågan har en svarstext -->\r" +
    "\n" +
    "          <div class=\"qa-block-head clearfix\">\r" +
    "\n" +
    "            <div class=\"pull-left\" ng-switch=\"qa.frageStallare | lowercase\">\r" +
    "\n" +
    "              <!--  Frågaställaren var Vårdenheten - svaret måste kommit från FK -->\r" +
    "\n" +
    "              Från: <span ng-switch-when=\"wc\">\r" +
    "\n" +
    "                          <span class=\"qa-sender\" message key=\"qa.fragestallare.fk\"></span>\r" +
    "\n" +
    "                        </span>\r" +
    "\n" +
    "              <!--  Frågaställaren var inte Vårdenheten - svaret måste alltså kommit från Vårdenheten -->\r" +
    "\n" +
    "              <span ng-switch-default id=\"{{panelId}}-answer-svar-vard-aktor-namn-{{qa.internReferens}}\" class=\"qa-sender\">{{qa.vardAktorNamn}}</span>\r" +
    "\n" +
    "            </div>\r" +
    "\n" +
    "            <div class=\"pull-right\" ng-switch=\"qa.frageStallare | lowercase\">\r" +
    "\n" +
    "              <!-- motsatt villkor mot frågan -->\r" +
    "\n" +
    "              <span ng-switch-when=\"wc\">Mottaget: </span> <span ng-switch-default>Skickat: </span> <span\r" +
    "\n" +
    "                class=\"qa-date\">{{qa.svarSkickadDatum | date:'short'}}</span>\r" +
    "\n" +
    "            </div>\r" +
    "\n" +
    "          </div>\r" +
    "\n" +
    "          <div id=\"{{panelId}}-{{qa.internReferens}}-svarsText\" class=\"qa-block-body\">{{qa.svarsText}}</div>\r" +
    "\n" +
    "        </div>\r" +
    "\n" +
    "      </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "      <!-- KNAPPAR HANTERADE FRÅGOR -->\r" +
    "\n" +
    "      <div ng-if=\"handledPanel\">\r" +
    "\n" +
    "        <!-- error message occuring when inteacting with server for this FragaSvar-->\r" +
    "\n" +
    "        <div id=\"{{panelId}}-{{qa.internReferens}}-svarsText-load-error\" ng-show=\"qa.activeErrorMessageKey\" class=\"alert alert-danger\">\r" +
    "\n" +
    "          <span message key=\"fk7263.error.{{qa.activeErrorMessageKey}}\"></span>\r" +
    "\n" +
    "        </div>\r" +
    "\n" +
    "        <div class=\"form-group\">\r" +
    "\n" +
    "          <!--  Oavsett hur frågan stänged skall man kunna 'öppna' den igen -->\r" +
    "\n" +
    "          <div class=\"controls\" ng-hide=\"qa.frageStallare!='WC' && qa.svarsText\">\r" +
    "\n" +
    "            <button class=\"btn btn-default btn-secondary\" ng-click=\"updateAsUnHandled(qa)\"\r" +
    "\n" +
    "                    wc-check-vardenhet vardenhet=\"{{cert.grundData.skapadAv.vardenhet.enhetsid}}\"\r" +
    "\n" +
    "                    id=\"markAsUnhandledBtn-{{qa.internReferens}}\" ng-disabled=\"qa.updateHandledStateInProgress\">\r" +
    "\n" +
    "              <img src=\"/img/loader-small.gif\" ng-show=\"qa.updateHandledStateInProgress\"> Markera som ej hanterad\r" +
    "\n" +
    "            </button>\r" +
    "\n" +
    "          </div>\r" +
    "\n" +
    "        </div>\r" +
    "\n" +
    "      </div>\r" +
    "\n" +
    "      <!-- KNAPPAR EJ HANTERADE FRÅGOR -->\r" +
    "\n" +
    "      <div ng-if=\"!handledPanel\">\r" +
    "\n" +
    "        <div class=\"form-group\" ng-if=\"qa.status == 'PENDING_INTERNAL_ACTION'\">\r" +
    "\n" +
    "          <!--  VÄNTAR på svar från Vårdenheten, men skall kunna klarmarkeras ändå?-->\r" +
    "\n" +
    "          <label class=\"control-label\" ng-show=\"!qa.answerDisabled && !certProperties.isRevoked\">Svar</label>\r" +
    "\n" +
    "          <div class=\"controls\">\r" +
    "\n" +
    "            <div class=\"alert alert-info\" ng-show=\"qa.answerDisabled && qa.answerDisabledReason\">\r" +
    "\n" +
    "              {{qa.answerDisabledReason}}\r" +
    "\n" +
    "            </div>\r" +
    "\n" +
    "            <div class=\"form-group\">\r" +
    "\n" +
    "              <textarea rows=\"5\" class=\"form-control col-md-9 qa-block-wc\" ng-model=\"qa.svarsText\"\r" +
    "\n" +
    "                        id=\"answerText-{{qa.internReferens}}\"\r" +
    "\n" +
    "                        ng-show=\"!qa.answerDisabled && !certProperties.isRevoked\"></textarea>\r" +
    "\n" +
    "            </div>\r" +
    "\n" +
    "            <div class=\"webcert-top-padding-section\">\r" +
    "\n" +
    "              <!-- error message occuring when inteacting with server for this FragaSvar-->\r" +
    "\n" +
    "              <div id=\"internal-{{qa.internReferens}}-svarsText-load-error\" ng-show=\"qa.activeErrorMessageKey\"\r" +
    "\n" +
    "                   class=\"alert alert-danger\">\r" +
    "\n" +
    "                <span message key=\"fk7263.error.{{qa.activeErrorMessageKey}}\"></span>\r" +
    "\n" +
    "              </div>\r" +
    "\n" +
    "              <button class=\"btn btn-success\" ng-click=\"sendAnswer(qa)\"\r" +
    "\n" +
    "                      ng-disabled=\"!qa.svarsText || qa.updateInProgress\"\r" +
    "\n" +
    "                      ng-show=\"!qa.answerDisabled && !certProperties.isRevoked\" wc-check-vardenhet\r" +
    "\n" +
    "                      vardenhet=\"{{cert.grundData.skapadAv.vardenhet.enhetsid}}\"\r" +
    "\n" +
    "                      id=\"sendAnswerBtn-{{qa.internReferens}}\">\r" +
    "\n" +
    "                <img src=\"/img/loader-small-green.gif\" ng-show=\"qa.updateInProgress\"> Skicka svar\r" +
    "\n" +
    "              </button>\r" +
    "\n" +
    "              <button class=\"btn btn-default btn-secondary\" ng-click=\"updateAsHandled(qa)\"\r" +
    "\n" +
    "                      id=\"markAsHandledFkOriginBtn-{{qa.internReferens}}\"\r" +
    "\n" +
    "                      ng-disabled=\"qa.updateHandledStateInProgress\" wc-check-vardenhet\r" +
    "\n" +
    "                      vardenhet=\"{{cert.grundData.skapadAv.vardenhet.enhetsid}}\"\r" +
    "\n" +
    "                      title=\"När frågan-svaret markeras som hanterad kommer den att anses som avslutad. Frågan-svaret flyttas till ’Hanterade frågor och svar’ och visas inte längre i vårdenhetens översikt över ej hanterade frågor och svar.\">\r" +
    "\n" +
    "                <img src=\"/img/loader-small.gif\" ng-show=\"qa.updateHandledStateInProgress\"> Markera som hanterad\r" +
    "\n" +
    "              </button>\r" +
    "\n" +
    "            </div>\r" +
    "\n" +
    "          </div>\r" +
    "\n" +
    "        </div>\r" +
    "\n" +
    "        <div class=\"form-group\" ng-if=\"qa.status == 'PENDING_EXTERNAL_ACTION' || qa.status == 'ANSWERED'\">\r" +
    "\n" +
    "          <!--  fått svar eller väntar på svar från FK, skall kunna klarmarkera manuellt ändå -->\r" +
    "\n" +
    "          <!-- error message occuring when inteacting with server for this FragaSvar-->\r" +
    "\n" +
    "          <div id=\"external-{{qa.internReferens}}-svarsText-load-error\" ng-show=\"qa.activeErrorMessageKey\"\r" +
    "\n" +
    "               class=\"alert alert-danger\">\r" +
    "\n" +
    "            <span message key=\"fk7263.error.{{qa.activeErrorMessageKey}}\"></span>\r" +
    "\n" +
    "          </div>\r" +
    "\n" +
    "          <div class=\"controls\" wc-check-vardenhet vardenhet=\"{{cert.grundData.skapadAv.vardenhet.enhetsid}}\">\r" +
    "\n" +
    "            <button class=\"btn btn-default btn-secondary\" ng-click=\"updateAsHandled(qa)\"\r" +
    "\n" +
    "                    id=\"markAsHandledWcOriginBtn-{{qa.internReferens}}\"\r" +
    "\n" +
    "                    ng-disabled=\"qa.updateHandledStateInProgress\"\r" +
    "\n" +
    "                    title=\"När frågan-svaret markeras som hanterad kommer den att anses som avslutad. Frågan-svaret flyttas till ’Hanterade frågor och svar’ och visas inte längre i vårdenhetens översikt över ej hanterade frågor och svar.\">\r" +
    "\n" +
    "              <img src=\"/img/loader-small.gif\" ng-show=\"qa.updateHandledStateInProgress\"> Markera som hanterad\r" +
    "\n" +
    "            </button>\r" +
    "\n" +
    "          </div>\r" +
    "\n" +
    "        </div>\r" +
    "\n" +
    "      </div>\r" +
    "\n" +
    "    </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "  </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "</div>\r" +
    "\n"
  );


  $templateCache.put('/web/webjars/fk7263/webcert/js/intyg/edit/form/form1.html',
    "<div wc-field-single field-number=\"1\" field-help-text=\"fk7263.helptext.smittskydd\">\r" +
    "\n" +
    "  <label for=\"smittskydd\" class=\"control-label title-single\">\r" +
    "\n" +
    "    <input id=\"smittskydd\" name=\"smittskydd\" type=\"checkbox\" ng-model=\"model.avstangningSmittskydd\"\r" +
    "\n" +
    "           wc-focus-on=\"firstInput\" ng-change=\"onSmittskyddChange()\">&nbsp;<span message key=\"fk7263.label.smittskydd\"></span>\r" +
    "\n" +
    "  </label>\r" +
    "\n" +
    "</div>"
  );


  $templateCache.put('/web/webjars/fk7263/webcert/js/intyg/edit/form/form10.html',
    "<div wc-field field-label=\"fk7263.label.prognos\" field-number=\"10\"\r" +
    "\n" +
    "     field-help-text=\"fk7263.helptext.arbetsformaga.prognos\" id=\"prognosForm\">\r" +
    "\n" +
    "\r" +
    "\n" +
    "  <div class=\"alert alert-danger\" ng-if=\"viewState.common.validationMessagesGrouped.prognos\" id=\"validationMessages_prognos\">\r" +
    "\n" +
    "    <ul>\r" +
    "\n" +
    "      <li ng-repeat=\"message in viewState.common.validationMessagesGrouped.prognos\"><span message\r" +
    "\n" +
    "                                                                         key=\"{{message.message}}\"></span>\r" +
    "\n" +
    "      </li>\r" +
    "\n" +
    "    </ul>\r" +
    "\n" +
    "  </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "  <a class=\"soclink\" href=\"http://www.socialstyrelsen.se/riktlinjer/forsakringsmedicinsktbeslutsstod\"\r" +
    "\n" +
    "     target=\"_blank\" title=\"Öppnar Socialstyrelsens beslutstöd för angiven huvuddiagnos\">Socialstyrelsens\r" +
    "\n" +
    "    försäkringsmedicinska beslutstöd</a>\r" +
    "\n" +
    "  <div class=\"form-group\">\r" +
    "\n" +
    "    <label class=\"radio-inline\"><input type=\"radio\" name=\"capacityForWorkForecast\" value=\"YES\"\r" +
    "\n" +
    "                                       ng-model=\"radioGroups.prognos\" ng-change=\"onPrognosChange()\"> Ja</label>\r" +
    "\n" +
    "  </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "  <div class=\"form-group\">\r" +
    "\n" +
    "    <label class=\"radio-inline\"><input type=\"radio\" id=\"capacityForWork2\"\r" +
    "\n" +
    "                                       name=\"capacityForWorkForecast\" value=\"PARTLY\"\r" +
    "\n" +
    "                                       ng-model=\"radioGroups.prognos\" ng-change=\"onPrognosChange()\"> Ja, delvis</label>\r" +
    "\n" +
    "  </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "  <div class=\"form-group\">\r" +
    "\n" +
    "    <label class=\"radio-inline\"><input type=\"radio\" id=\"capacityForWork3\"\r" +
    "\n" +
    "                                       name=\"capacityForWorkForecast\" value=\"NO\"\r" +
    "\n" +
    "                                       ng-model=\"radioGroups.prognos\" ng-change=\"onPrognosChange()\"> Nej</label>\r" +
    "\n" +
    "  </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "  <div class=\"form-group\">\r" +
    "\n" +
    "    <label class=\"radio-inline\"><input type=\"radio\" id=\"capacityForWork4\"\r" +
    "\n" +
    "                                       name=\"capacityForWorkForecast\" value=\"UNKNOWN\"\r" +
    "\n" +
    "                                       ng-model=\"radioGroups.prognos\" ng-change=\"onPrognosChange()\"> Går ej att bedöma</label>\r" +
    "\n" +
    "    <div class=\"indent form-inline\" ng-show=\"showInteAttBedoma()\">\r" +
    "\n" +
    "      Förtydligande: <input type=\"text\"\r" +
    "\n" +
    "                            ng-model=\"model.arbetsformagaPrognosGarInteAttBedomaBeskrivning\"\r" +
    "\n" +
    "                            id=\"capacityForWorkForecastText\" name=\"capacityForWork.forecastText\"\r" +
    "\n" +
    "                            value=\"\" class='form-control otherInformation'\r" +
    "\n" +
    "                            ng-change=\"viewState.limitOtherField('arbetsformagaPrognosGarInteAttBedomaBeskrivning')\"> <span\r" +
    "\n" +
    "        id=\"prognosis-counter\">Tecken kvar:\r" +
    "\n" +
    "                        {{viewState.inputLimits.ovrigt - viewState.getTotalOvrigtLength()}}</span>\r" +
    "\n" +
    "      <div class=\"info-transfer\">Informationen överförs till \"Övriga upplysningar\" vid PDF-utskrift av\r" +
    "\n" +
    "        blanketten.\r" +
    "\n" +
    "      </div>\r" +
    "\n" +
    "    </div>\r" +
    "\n" +
    "  </div>\r" +
    "\n" +
    "</div>"
  );


  $templateCache.put('/web/webjars/fk7263/webcert/js/intyg/edit/form/form12.html',
    "<div wc-field field-label=\"fk7263.label.fk-kontakt\" field-number=\"12\"\r" +
    "\n" +
    "     field-help-text=\"fk7263.helptext.kontakt\">\r" +
    "\n" +
    "  <label><input type=\"checkbox\" id=\"kontaktFk\" name=\"contactWithFkRequested\"\r" +
    "\n" +
    "                ng-model=\"model.kontaktMedFk\"> Kontakt önskas med\r" +
    "\n" +
    "    Försäkringskassan</label>\r" +
    "\n" +
    "</div>"
  );


  $templateCache.put('/web/webjars/fk7263/webcert/js/intyg/edit/form/form13.html',
    "<div wc-field field-label=\"fk7263.label.ovrigt\" field-number=\"13\"\r" +
    "\n" +
    "     field-help-text=\"fk7263.helptext.ovrigt\" class=\"print-pagebreak-after\">\r" +
    "\n" +
    "                    <textarea id=\"otherInformation\" name=\"otherInformation\" rows='10'\r" +
    "\n" +
    "                              class='form-control otherInformation' ng-model=\"model.kommentar\"\r" +
    "\n" +
    "                              ng-change=\"viewState.limitOtherField('kommentar')\"></textarea>\r" +
    "\n" +
    "  <div class=\"counter\">Tecken kvar: {{teckenKvar()}}</div>\r" +
    "\n" +
    "</div>"
  );


  $templateCache.put('/web/webjars/fk7263/webcert/js/intyg/edit/form/form15.html',
    "<div wc-field field-label=\"fk7263.label.vardenhet.adress\" field-number=\"15\"\r" +
    "\n" +
    "     field-help-text=\"fk7263.helptext.adress\" id=\"vardenhetForm\">\r" +
    "\n" +
    "  <div class=\"alert alert-danger\" ng-if=\"viewState.common.validationMessagesGrouped.vardperson\" id=\"validationMessages_vardperson\">\r" +
    "\n" +
    "    <ul>\r" +
    "\n" +
    "      <li ng-repeat=\"message in viewState.common.validationMessagesGrouped.vardperson\"><span message\r" +
    "\n" +
    "                                                                            key=\"{{message.message}}\"></span>\r" +
    "\n" +
    "      </li>\r" +
    "\n" +
    "    </ul>\r" +
    "\n" +
    "  </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "  <label class=\"control-label\">{{model.grundData.skapadAv.vardenhet.enhetsnamn}}\r" +
    "\n" +
    "    ({{model.grundData.skapadAv.vardenhet.enhetsid}}),\r" +
    "\n" +
    "    {{model.grundData.skapadAv.vardenhet.vardgivare.vardgivarnamn}}</label>\r" +
    "\n" +
    "\r" +
    "\n" +
    "  <div class=\"alert alert-info\" ng-show=\"widgetState.hasInfoMissing\">\r" +
    "\n" +
    "    Information om vårdenheten saknas i HSA-katalogen. Be den på vårdenheten som är ansvarig för\r" +
    "\n" +
    "    frågor om HSA att uppdatera informationen.\r" +
    "\n" +
    "  </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "  <div class=\"form-horizontal\">\r" +
    "\n" +
    "    <div class=\"form-group\">\r" +
    "\n" +
    "      <label class=\"col-xs-3 control-label\">Postadress</label>\r" +
    "\n" +
    "      <div class=\"col-xs-9\">\r" +
    "\n" +
    "        <input type=\"text\" class=\"form-control input-full\" id=\"clinicInfoPostalAddress\"\r" +
    "\n" +
    "               name=\"clinicInfo.postalAddress\"\r" +
    "\n" +
    "               ng-model=\"model.grundData.skapadAv.vardenhet.postadress\">\r" +
    "\n" +
    "      </div>\r" +
    "\n" +
    "    </div>\r" +
    "\n" +
    "    <div class=\"form-group\">\r" +
    "\n" +
    "      <label class=\"col-xs-3 control-label\">Postnummer</label>\r" +
    "\n" +
    "      <div class=\"col-xs-9\">\r" +
    "\n" +
    "        <input type=\"text\" class=\"form-control input-5\" id=\"clinicInfoPostalCode\"\r" +
    "\n" +
    "               name=\"clinicInfo.postalCode\" ng-model=\"model.grundData.skapadAv.vardenhet.postnummer\">\r" +
    "\n" +
    "      </div>\r" +
    "\n" +
    "    </div>\r" +
    "\n" +
    "    <div class=\"form-group\">\r" +
    "\n" +
    "      <label class=\"col-xs-3 control-label\">Postort</label>\r" +
    "\n" +
    "      <div class=\"col-xs-9\">\r" +
    "\n" +
    "        <input type=\"text\" class=\"form-control\" id=\"clinicInfoPostalCity\" name=\"clinicInfo.postalCity\"\r" +
    "\n" +
    "               ng-model=\"model.grundData.skapadAv.vardenhet.postort\">\r" +
    "\n" +
    "      </div>\r" +
    "\n" +
    "    </div>\r" +
    "\n" +
    "    <div class=\"form-group\">\r" +
    "\n" +
    "      <label class=\"col-xs-3 control-label\">Telefonnummer</label>\r" +
    "\n" +
    "      <div class=\"col-xs-9\">\r" +
    "\n" +
    "        <input type=\"text\" class=\"form-control\" id=\"clinicInfoPhone\" name=\"clinicInfo.phone\"\r" +
    "\n" +
    "               ng-model=\"model.grundData.skapadAv.vardenhet.telefonnummer\" >\r" +
    "\n" +
    "      </div>\r" +
    "\n" +
    "    </div>\r" +
    "\n" +
    "  </div>\r" +
    "\n" +
    "</div>"
  );


  $templateCache.put('/web/webjars/fk7263/webcert/js/intyg/edit/form/form2.html',
    "<div ng-form=\"form2\">\r" +
    "\n" +
    "<div wc-field field-label=\"fk7263.label.diagnos\" field-number=\"2\"\r" +
    "\n" +
    "     field-help-text=\"fk7263.helptext.diagnos\" ng-hide=\"viewState.avstangningSmittskydd()\"\r" +
    "\n" +
    "     ng-animate=\"'fade'\" class=\"print-pagebreak-after\" id=\"diagnoseForm\">\r" +
    "\n" +
    "\r" +
    "\n" +
    "  <div class=\"alert alert-danger\" ng-if=\"viewState.common.validationMessagesGrouped.diagnos\" id=\"validationMessages_diagnos\">\r" +
    "\n" +
    "    <ul>\r" +
    "\n" +
    "      <li ng-repeat=\"message in viewState.common.validationMessagesGrouped.diagnos\"><span message\r" +
    "\n" +
    "                                                                         key=\"{{message.message}}\"></span>\r" +
    "\n" +
    "      </li>\r" +
    "\n" +
    "    </ul>\r" +
    "\n" +
    "  </div>\r" +
    "\n" +
    "  <div class=\"row bottom-padding-small\">\r" +
    "\n" +
    "    <div class='col-xs-12'>\r" +
    "\n" +
    "      <div class='divider'>\r" +
    "\n" +
    "        <span message key=\"fk7263.label.valjkodverk\"></span>\r" +
    "\n" +
    "        <label class=\"radio-inline\">\r" +
    "\n" +
    "          <input type=\"radio\" id=\"diagnoseKodverk_ICD_10_SE\" name=\"diagnosKodverk\" value=\"ICD_10_SE\"\r" +
    "\n" +
    "                 ng-model=\"model.diagnosKodverk\" checked=\"checked\" ng-change=\"onChangeKodverk()\"><span message key=\"fk7263.label.diagnoskodverk.icd_10_se\"></span>\r" +
    "\n" +
    "        </label>\r" +
    "\n" +
    "        <label class=\"radio-inline\">\r" +
    "\n" +
    "          <input type=\"radio\" id=\"diagnoseKodverk_KSH_97_P\" name=\"diagnosKodverk\" value=\"KSH_97_P\"\r" +
    "\n" +
    "                 ng-model=\"model.diagnosKodverk\" ng-change=\"onChangeKodverk()\"><span message key=\"fk7263.label.diagnoskodverk.ksh_97_p\"></span>\r" +
    "\n" +
    "        </label>\r" +
    "\n" +
    "      </div>\r" +
    "\n" +
    "    </div>\r" +
    "\n" +
    "  </div>\r" +
    "\n" +
    "  <div class=\"row bottom-padding-small\">\r" +
    "\n" +
    "    <div class='col-xs-3'>\r" +
    "\n" +
    "      <label class=\"control-label unbreakable\">ICD-10</label>\r" +
    "\n" +
    "      <input type=\"text\" class=\"form-control\" id=\"diagnoseCode\" name=\"diagnose.code\" value=\"\"\r" +
    "\n" +
    "             class='diagnose-code col-xs-11 form-control' ng-model=\"model.diagnosKod\"\r" +
    "\n" +
    "             typeahead=\"d.value as d.label for d in getDiagnoseCodes(model.diagnosKodverk, $viewValue) | filter:$viewValue | limitTo:10\"\r" +
    "\n" +
    "             typeahead-min-length=\"3\" typeahead-on-select=\"onDiagnoseCode1Select($item)\"\r" +
    "\n" +
    "             wc-to-uppercase\r" +
    "\n" +
    "          >\r" +
    "\n" +
    "    </div>\r" +
    "\n" +
    "    <div class='col-xs-9'>\r" +
    "\n" +
    "      <label class=\"control-label\">Diagnos</label>\r" +
    "\n" +
    "      <input type=\"text\" class=\"form-control maxwidth\" id=\"diagnoseDescription\"\r" +
    "\n" +
    "             class=\"col-xs-12 form-control\" name=\"diagnose.description\" value=\"\"\r" +
    "\n" +
    "             ng-model=\"model.diagnosBeskrivning1\"\r" +
    "\n" +
    "             typeahead=\"d.value as d.label for d in searchDiagnoseByDescription(model.diagnosKodverk, $viewValue) | filter:$viewValue \"\r" +
    "\n" +
    "             typeahead-on-select=\"onDiagnoseDescription1Select($item)\"\r" +
    "\n" +
    "             typeahead-wait-ms=\"100\" ng-change=\"limitDiagnosBeskrivningField('diagnosBeskrivning1')\">\r" +
    "\n" +
    "      <span class=\"counter\">Tecken kvar: {{viewState.inputLimits.diagnosBeskrivning - getTotalDiagnosBeskrivningLength()}}</span>\r" +
    "\n" +
    "    </div>\r" +
    "\n" +
    "  </div>\r" +
    "\n" +
    "  <div class=\"row bottom-padding-tiny\">\r" +
    "\n" +
    "    <div class='col-xs-3'>\r" +
    "\n" +
    "      <label class=\"control-label unbreakable\">ICD-10</label>\r" +
    "\n" +
    "      <div class=\"controls\">\r" +
    "\n" +
    "        <div class=\"bottom-padding-small clearfix\">\r" +
    "\n" +
    "          <input type=\"text\" id=\"diagnoseCodeOpt1\" name=\"diagnose.codeOpt1\" value=\"\"\r" +
    "\n" +
    "                 ng-model=\"model.diagnosKod2\" class=\"col-xs-11 form-control\"\r" +
    "\n" +
    "                 typeahead=\"d.value as d.label for d in getDiagnoseCodes(model.diagnosKodverk, $viewValue) | filter:$viewValue | limitTo:10\"\r" +
    "\n" +
    "                 typeahead-min-length=\"3\" typeahead-on-select=\"onDiagnoseCode2Select($item)\"\r" +
    "\n" +
    "                 ng-change=\"limitDiagnosBeskrivningField('diagnosKod2')\" wc-to-uppercase>\r" +
    "\n" +
    "        </div>\r" +
    "\n" +
    "      </div>\r" +
    "\n" +
    "    </div>\r" +
    "\n" +
    "    <div class='col-xs-9'>\r" +
    "\n" +
    "      <label class=\"control-label inline-block\"><span message\r" +
    "\n" +
    "                                                      key=\"fk7263.label.ytterligarediagnoser\"></span>\r" +
    "\n" +
    "        (frivilligt)</label><span wc-enable-tooltip class=\"absolute\"\r" +
    "\n" +
    "                                  field-help-text=\"fk7263.helptext.diagnos.ytterligare\"></span>\r" +
    "\n" +
    "      <div class=\"bottom-padding-small clearfix\">\r" +
    "\n" +
    "        <input type=\"text\" id=\"diagnoseDescriptionOpt1\" name=\"diagnose.descriptionOpt1\" value=\"\"\r" +
    "\n" +
    "               ng-model=\"model.diagnosBeskrivning2\" class=\"block col-xs-12 form-control maxwidth\"\r" +
    "\n" +
    "               typeahead=\"d.value as d.label for d in searchDiagnoseByDescription(model.diagnosKodverk, $viewValue) | filter:$viewValue \"\r" +
    "\n" +
    "               typeahead-on-select=\"onDiagnoseDescription2Select($item)\"\r" +
    "\n" +
    "               typeahead-wait-ms=\"100\" ng-change=\"limitDiagnosBeskrivningField('diagnosBeskrivning2')\">\r" +
    "\n" +
    "        <span class=\"counter\">Tecken kvar: {{viewState.inputLimits.diagnosBeskrivning - getTotalDiagnosBeskrivningLength()}}</span>\r" +
    "\n" +
    "      </div>\r" +
    "\n" +
    "    </div>\r" +
    "\n" +
    "  </div>\r" +
    "\n" +
    "  <div class=\"row bottom-padding-small\">\r" +
    "\n" +
    "    <div class='col-xs-3'>\r" +
    "\n" +
    "      <input type=\"text\" id=\"diagnoseCodeOpt2\" name=\"diagnose.codeOpt2\" value=\"\"\r" +
    "\n" +
    "             ng-model=\"model.diagnosKod3\" class=\"col-xs-11 form-control\"\r" +
    "\n" +
    "             typeahead=\"d.value as d.label for d in getDiagnoseCodes(model.diagnosKodverk, $viewValue) | filter:$viewValue | limitTo:10\"\r" +
    "\n" +
    "             typeahead-min-length=\"3\" typeahead-on-select=\"onDiagnoseCode3Select($item)\"\r" +
    "\n" +
    "             ng-change=\"limitDiagnosBeskrivningField('diagnosKod3')\" wc-to-uppercase>\r" +
    "\n" +
    "    </div>\r" +
    "\n" +
    "    <div class='col-xs-9'>\r" +
    "\n" +
    "      <div class=\"controls\">\r" +
    "\n" +
    "        <input type=\"text\" id=\"diagnoseDescriptionOpt2\" name=\"diagnose.descriptionOpt2\" value=\"\"\r" +
    "\n" +
    "               ng-model=\"model.diagnosBeskrivning3\" class=\"block col-xs-12 form-control maxwidth\"\r" +
    "\n" +
    "               typeahead=\"d.value as d.label for d in searchDiagnoseByDescription(model.diagnosKodverk, $viewValue) | filter:$viewValue \"\r" +
    "\n" +
    "               typeahead-on-select=\"onDiagnoseDescription3Select($item)\"\r" +
    "\n" +
    "               typeahead-wait-ms=\"100\"\r" +
    "\n" +
    "               ng-change=\"limitDiagnosBeskrivningField('diagnosBeskrivning3')\">\r" +
    "\n" +
    "        <span class=\"counter\">Tecken kvar: {{viewState.inputLimits.diagnosBeskrivning - getTotalDiagnosBeskrivningLength()}}</span>\r" +
    "\n" +
    "      </div>\r" +
    "\n" +
    "    </div>\r" +
    "\n" +
    "  </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "  <div class=\"form-group\">\r" +
    "\n" +
    "    <div>\r" +
    "\n" +
    "      <label class=\"control-label\"><span message key=\"fk7263.label.diagnosfortydligande\"></span></label>\r" +
    "\n" +
    "      <span wc-enable-tooltip field-help-text=\"fk7263.helptext.diagnos.fortydligande\"></span>\r" +
    "\n" +
    "    </div>\r" +
    "\n" +
    "                      <textarea id=\"diagnoseClarification\" class=\"form-control\" name=\"diagnose.clarification\" rows='5'\r" +
    "\n" +
    "                                ng-model=\"model.diagnosBeskrivning\" ng-trim=\"false\"\r" +
    "\n" +
    "                                ng-change=\"limitDiagnosBeskrivningField('diagnosBeskrivning')\"></textarea>\r" +
    "\n" +
    "    <span class=\"counter\">Tecken kvar: {{viewState.inputLimits.diagnosBeskrivning - getTotalDiagnosBeskrivningLength()}}</span>\r" +
    "\n" +
    "  </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "  <div class=\"form-group form-inline\">\r" +
    "\n" +
    "    <label class=\"checkbox\">\r" +
    "\n" +
    "      <input type=\"checkbox\" id=\"diagnoseMultipleDiagnoses\" name=\"diagnose.multipleDiagnoses\"\r" +
    "\n" +
    "             ng-model=\"model.samsjuklighet\"\r" +
    "\n" +
    "             ng-change=\"limitDiagnosBeskrivningField('samsjuklighet')\">\r" +
    "\n" +
    "      Samsjuklighet föreligger</label><span wc-enable-tooltip\r" +
    "\n" +
    "                                            field-help-text=\"fk7263.helptext.diagnos.samsjuklighet\"></span>\r" +
    "\n" +
    "  </div>\r" +
    "\n" +
    "</div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "</div>"
  );


  $templateCache.put('/web/webjars/fk7263/webcert/js/intyg/edit/form/form3.html',
    "<div wc-field field-label=\"fk7263.label.aktuellt-sjukdomsforlopp\" field-number=\"3\"\r" +
    "\n" +
    "     field-help-text=\"fk7263.helptext.aktuellt-sjukdomsforlopp\" ng-hide=\"viewState.avstangningSmittskydd()\"\r" +
    "\n" +
    "     ng-animate=\"fade\">\r" +
    "\n" +
    "                    <textarea id=\"diseaseCause\" class=\"form-control\" name=\"diseaseCause\" rows='5'\r" +
    "\n" +
    "                              ng-model=\"model.sjukdomsforlopp\"\r" +
    "\n" +
    "                              wc-maxlength ng-trim=\"false\"\r" +
    "\n" +
    "                              maxlength=\"{{viewState.inputLimits.sjukdomsforlopp}}\"></textarea>\r" +
    "\n" +
    "\r" +
    "\n" +
    "</div>"
  );


  $templateCache.put('/web/webjars/fk7263/webcert/js/intyg/edit/form/form4.html',
    "<div wc-field field-label=\"fk7263.label.funktionsnedsattningar\" field-number=\"4\"\r" +
    "\n" +
    "     field-help-text=\"fk7263.helptext.funktionsnedsattning\" ng-hide=\"viewState.avstangningSmittskydd()\"\r" +
    "\n" +
    "     ng-animate=\"'fade'\">\r" +
    "\n" +
    "  <div class=\"alert alert-danger\" ng-if=\"viewState.common.validationMessagesGrouped.funktionsnedsattning\" id=\"validationMessages_funktionsnedsattning\">\r" +
    "\n" +
    "    <ul>\r" +
    "\n" +
    "      <li ng-repeat=\"message in viewState.common.validationMessagesGrouped.funktionsnedsattning\"><span message\r" +
    "\n" +
    "                                                                                      key=\"{{message.message}}\"></span>\r" +
    "\n" +
    "      </li>\r" +
    "\n" +
    "    </ul>\r" +
    "\n" +
    "  </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "  <a class=\"soclink\" href=\"http://www.socialstyrelsen.se/riktlinjer/forsakringsmedicinsktbeslutsstod\"\r" +
    "\n" +
    "     target=\"_blank\" title=\"Öppnar Socialstyrelsens beslutstöd för angiven huvuddiagnos\">Socialstyrelsens\r" +
    "\n" +
    "    försäkringsmedicinska beslutstöd</a>\r" +
    "\n" +
    "                    <textarea id=\"disabilities\" class=\"form-control\" name=\"disabilities\" rows='6'\r" +
    "\n" +
    "                              ng-model=\"model.funktionsnedsattning\" wc-maxlength ng-trim=\"false\"\r" +
    "\n" +
    "                              maxlength=\"{{viewState.inputLimits.funktionsnedsattning}}\"></textarea>\r" +
    "\n" +
    "</div>"
  );


  $templateCache.put('/web/webjars/fk7263/webcert/js/intyg/edit/form/form4b.html',
    "<div ng-form=\"form4b\">\r" +
    "\n" +
    "<div id=\"intygetbaseraspa\" wc-field field-label=\"fk7263.label.intyg-baserat-pa\" field-number=\"4b\"\r" +
    "\n" +
    "                       field-help-text=\"fk7263.helptext.intyg-baserat-pa\" ng-hide=\"viewState.avstangningSmittskydd()\"\r" +
    "\n" +
    "                       ng-animate=\"'fade'\">\r" +
    "\n" +
    "  <div class=\"alert alert-danger\" ng-if=\"viewState.common.validationMessagesGrouped.intygbaseratpa\" id=\"validationMessages_intygbaseratpa\">\r" +
    "\n" +
    "    <ul>\r" +
    "\n" +
    "      <li ng-repeat=\"message in viewState.common.validationMessagesGrouped.intygbaseratpa\"><span message\r" +
    "\n" +
    "                                                                                key=\"{{message.message}}\"></span>\r" +
    "\n" +
    "      </li>\r" +
    "\n" +
    "    </ul>\r" +
    "\n" +
    "  </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "  <!-- Undersökning -->\r" +
    "\n" +
    "  <div class=\"form-group form-inline\">\r" +
    "\n" +
    "    <label class=\"checkbox inline-block\" style=\"min-width: 250px\"><input type=\"checkbox\"\r" +
    "\n" +
    "                                                                         id=\"basedOnExamination\"\r" +
    "\n" +
    "                                                                         name=\"informationBasedOn.examination\"\r" +
    "\n" +
    "                                                                         class=\"checkbox-inline\"\r" +
    "\n" +
    "                                                                         ng-model=\"basedOnState.check.undersokningAvPatienten\"\r" +
    "\n" +
    "                                                                         ng-change=\"toggleBaseradPaDate('undersokningAvPatienten')\"\r" +
    "\n" +
    "        value=\"true\">\r" +
    "\n" +
    "      Min undersökning av patienten</label>\r" +
    "\n" +
    "    <span wc-date-picker-field target-model=\"dates.undersokningAvPatienten\"\r" +
    "\n" +
    "          dom-id=\"undersokningAvPatientenDate\"></span>\r" +
    "\n" +
    "  </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "  <!-- Telefonkontakt -->\r" +
    "\n" +
    "  <div class=\"form-group form-inline\">\r" +
    "\n" +
    "    <label class=\"checkbox inline-block\" style=\"min-width: 250px\"><input type=\"checkbox\"\r" +
    "\n" +
    "                                                                         id=\"basedOnPhoneContact\"\r" +
    "\n" +
    "                                                                         name=\"informationBasedOn.phoneContact\"\r" +
    "\n" +
    "                                                                         ng-model=\"basedOnState.check.telefonkontaktMedPatienten\"\r" +
    "\n" +
    "                                                                         ng-change=\"toggleBaseradPaDate('telefonkontaktMedPatienten')\">\r" +
    "\n" +
    "      Min telefonkontakt med patienten</label>\r" +
    "\n" +
    "    <span wc-date-picker-field target-model=\"dates.telefonkontaktMedPatienten\"\r" +
    "\n" +
    "          dom-id=\"telefonkontaktMedPatientenDate\"></span>\r" +
    "\n" +
    "  </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "  <!-- Journaluppgifter -->\r" +
    "\n" +
    "  <div class=\"form-group form-inline\">\r" +
    "\n" +
    "    <label class=\"checkbox inline-block\" style=\"min-width: 250px\"><input type=\"checkbox\"\r" +
    "\n" +
    "                                                                         id=\"basedOnJournal\"\r" +
    "\n" +
    "                                                                         name=\"informationBasedOn.journal\"\r" +
    "\n" +
    "                                                                         ng-model=\"basedOnState.check.journaluppgifter\"\r" +
    "\n" +
    "                                                                         ng-change=\"toggleBaseradPaDate('journaluppgifter')\">\r" +
    "\n" +
    "      Journaluppgifter</label>\r" +
    "\n" +
    "    <span wc-date-picker-field target-model=\"dates.journaluppgifter\"\r" +
    "\n" +
    "          dom-id=\"journaluppgifterDate\"></span>\r" +
    "\n" +
    "  </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "  <!-- Annat -->\r" +
    "\n" +
    "  <div class=\"form-group form-inline\">\r" +
    "\n" +
    "    <label class=\"checkbox inline-block\" style=\"min-width: 250px\"><input type=\"checkbox\"\r" +
    "\n" +
    "                                                                         id=\"basedOnOther\"\r" +
    "\n" +
    "                                                                         name=\"informationBasedOn.other\"\r" +
    "\n" +
    "                                                                         ng-model=\"basedOnState.check.annanReferens\"\r" +
    "\n" +
    "                                                                         ng-change=\"toggleBaseradPaDate('annanReferens')\">\r" +
    "\n" +
    "      Annat<span wc-enable-tooltip field-help-text=\"fk7263.helptext.intyg-baserat-pa.annat\"></span>\r" +
    "\n" +
    "    </label>\r" +
    "\n" +
    "    <span wc-date-picker-field target-model=\"dates.annanReferens\" dom-id=\"annanReferensDate\"></span>\r" +
    "\n" +
    "  </div>\r" +
    "\n" +
    "  <div class=\"otherText indent\" ng-if=\"basedOnState.check.annanReferens\">\r" +
    "\n" +
    "    <span>Ange vad:</span><br>\r" +
    "\n" +
    "    <textarea id=\"informationBasedOnOtherText\" name=\"informationBasedOn.otherText\" rows='2'\r" +
    "\n" +
    "              class='form-control otherInformation' ng-model=\"model.annanReferensBeskrivning\"\r" +
    "\n" +
    "              ng-change=\"viewState.limitOtherField('annanReferensBeskrivning')\"></textarea>\r" +
    "\n" +
    "    <div class=\"counter\">\r" +
    "\n" +
    "      Tecken kvar: {{viewState.inputLimits.ovrigt - viewState.getTotalOvrigtLength()}}\r" +
    "\n" +
    "    </div>\r" +
    "\n" +
    "    <div class=\"info-transfer\">Informationen överförs till \"Övriga upplysningar\" vid PDF-utskrift av\r" +
    "\n" +
    "      blanketten.\r" +
    "\n" +
    "    </div>\r" +
    "\n" +
    "  </div>\r" +
    "\n" +
    "</div>\r" +
    "\n" +
    "</div>"
  );


  $templateCache.put('/web/webjars/fk7263/webcert/js/intyg/edit/form/form5.html',
    "<div wc-field field-label=\"fk7263.label.aktivitetsbegransning\" field-number=\"5\"\r" +
    "\n" +
    "     field-help-text=\"fk7263.helptext.aktivitetsbegransning\" ng-hide=\"viewState.avstangningSmittskydd()\"\r" +
    "\n" +
    "     ng-animate=\"'fade'\" class=\"print-pagebreak-after\">\r" +
    "\n" +
    "\r" +
    "\n" +
    "  <div class=\"alert alert-danger\" ng-if=\"viewState.common.validationMessagesGrouped.aktivitetsbegransning\" id=\"validationMessages_aktivitetsbegransning\">\r" +
    "\n" +
    "    <ul>\r" +
    "\n" +
    "      <li ng-repeat=\"message in viewState.common.validationMessagesGrouped.aktivitetsbegransning\">\r" +
    "\n" +
    "        <span message key=\"{{message.message}}\"></span>\r" +
    "\n" +
    "      </li>\r" +
    "\n" +
    "    </ul>\r" +
    "\n" +
    "  </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "  <a class=\"soclink\" href=\"http://www.socialstyrelsen.se/riktlinjer/forsakringsmedicinsktbeslutsstod\"\r" +
    "\n" +
    "     target=\"_blank\" title=\"Öppnar Socialstyrelsens beslutstöd för angiven huvuddiagnos\">Socialstyrelsens\r" +
    "\n" +
    "    försäkringsmedicinska beslutstöd</a>\r" +
    "\n" +
    "                    <textarea id=\"activityLimitation\" class=\"form-control\" name=\"activityLimitation\" rows='10'\r" +
    "\n" +
    "                              ng-model=\"model.aktivitetsbegransning\" wc-maxlength ng-trim=\"false\"\r" +
    "\n" +
    "                              maxlength=\"{{viewState.inputLimits.aktivitetsbegransning}}\"></textarea>\r" +
    "\n" +
    "</div>"
  );


  $templateCache.put('/web/webjars/fk7263/webcert/js/intyg/edit/form/form6a-7-11.html',
    "  <div wc-field field-label=\"fk7263.label.rekommendationer\" id=\"rekommendationerForm\" field-number=\"6a, 7, 11\"\r" +
    "\n" +
    "       field-help-text=\"fk7263.helptext.rekommendationer\">\r" +
    "\n" +
    "\r" +
    "\n" +
    "    <div class=\"alert alert-danger\" ng-if=\"viewState.common.validationMessagesGrouped.rekommendationer\"\r" +
    "\n" +
    "         id=\"validationMessages_rekommendationer\">\r" +
    "\n" +
    "      <ul>\r" +
    "\n" +
    "        <li ng-repeat=\"message in viewState.common.validationMessagesGrouped.rekommendationer\"><span message\r" +
    "\n" +
    "                                                                                                               key=\"{{message.message}}\"></span>\r" +
    "\n" +
    "        </li>\r" +
    "\n" +
    "      </ul>\r" +
    "\n" +
    "    </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "    <!-- 11 -->\r" +
    "\n" +
    "    <div>Resor till och från arbetet med annat färdmedel än normalt kan göra det möjligt för patienten\r" +
    "\n" +
    "      att återgå i arbete</label> <span wc-enable-tooltip\r" +
    "\n" +
    "                                        field-help-text=\"fk7263.helptext.rekommendationer.resor\"></span>\r" +
    "\n" +
    "    </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "    <div class=\"form-group form-inline\" id=\"rekommendationRessattRadioGroup\">\r" +
    "\n" +
    "      <label class=\"radio-inline\">\r" +
    "\n" +
    "        <input type=\"radio\" id=\"rekommendationRessatt\" name=\"recommendationsToFkTravel\" value=\"JA\"\r" +
    "\n" +
    "               ng-model=\"radioGroups.ressattTillArbete\" ng-change=\"onTravelChange()\"> Ja</label>\r" +
    "\n" +
    "      <label class=\"radio-inline\">\r" +
    "\n" +
    "        <input type=\"radio\" id=\"rekommendationRessattEj\" name=\"recommendationsToFkTravel\" value=\"NEJ\"\r" +
    "\n" +
    "               ng-model=\"radioGroups.ressattTillArbete\" ng-change=\"onTravelChange()\"> Nej</label>\r" +
    "\n" +
    "    </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "    <!-- 6a, 7 -->\r" +
    "\n" +
    "    <div ng-hide=\"viewState.avstangningSmittskydd()\">\r" +
    "\n" +
    "\r" +
    "\n" +
    "      <!-- 6a -->\r" +
    "\n" +
    "      <div class=\"form-group form-inline\">\r" +
    "\n" +
    "        <label class=\"checkbox\"><input type=\"checkbox\" id=\"rekommendationKontaktAf\" name=\"recommendationsToFk.contactAf\"\r" +
    "\n" +
    "                                       ng-model=\"model.rekommendationKontaktArbetsformedlingen\">\r" +
    "\n" +
    "          Patienten\r" +
    "\n" +
    "          bör komma i kontakt med arbetsförmedlingen</label> <span wc-enable-tooltip\r" +
    "\n" +
    "                                                                   field-help-text=\"fk7263.helptext.rekommendationer.arbetsformedlingen\"></span>\r" +
    "\n" +
    "      </div>\r" +
    "\n" +
    "      <div class=\"form-group form-inline\">\r" +
    "\n" +
    "        <label class=\"checkbox\"><input type=\"checkbox\" id=\"rekommendationKontaktForetagshalsovard\"\r" +
    "\n" +
    "                                       name=\"recommendationsToFk.contactCompanyCare\"\r" +
    "\n" +
    "                                       ng-model=\"model.rekommendationKontaktForetagshalsovarden\">\r" +
    "\n" +
    "          Patienten\r" +
    "\n" +
    "          bör komma i kontakt med företagshälsovården</label> <span wc-enable-tooltip\r" +
    "\n" +
    "                                                                    field-help-text=\"fk7263.helptext.rekommendationer.foretagshalsovarden\"></span>\r" +
    "\n" +
    "      </div>\r" +
    "\n" +
    "      <div class=\"form-group form-inline\">\r" +
    "\n" +
    "        <label class=\"checkbox\"><input type=\"checkbox\" id=\"rekommendationOvrigt\" name=\"recommendationsToFk.other\"\r" +
    "\n" +
    "                                       ng-model=\"model.rekommendationOvrigtCheck\"> Övrigt (ange\r" +
    "\n" +
    "          vad)</label><span wc-enable-tooltip field-help-text=\"fk7263.helptext.rekommendationer.ovrigt\"></span>\r" +
    "\n" +
    "        <div class=\"indent\" ng-if=\"model.rekommendationOvrigtCheck\">\r" +
    "\n" +
    "          <input type=\"text\" id=\"rekommendationOvrigtBeskrivning\" name=\"recommendationsToFk.otherText\" value=\"\"\r" +
    "\n" +
    "                 class='form-control maxwidth' ng-model=\"model.rekommendationOvrigt\" maxlength=\"80\" wc-maxlength>\r" +
    "\n" +
    "\r" +
    "\n" +
    "        </div>\r" +
    "\n" +
    "      </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "      <!-- 7 -->\r" +
    "\n" +
    "    <span id=\"recommendationsToFkReabInQuestionRadioGroup\">\r" +
    "\n" +
    "      <h4 class=\"inline-block\">Är arbetslivsinriktad rehabilitering aktuell?</h4><span wc-enable-tooltip\r" +
    "\n" +
    "                                                                                       field-help-text=\"fk7263.helptext.rekommendationer.arbetslivsinriktad-rehabilitering\"></span>\r" +
    "\n" +
    "\r" +
    "\n" +
    "      <!-- Rehab -->\r" +
    "\n" +
    "      <!-- YES -->\r" +
    "\n" +
    "      <div class=\"form-group\">\r" +
    "\n" +
    "        <label class=\"radio-inline\"><input type=\"radio\" name=\"recommendationsToFkReabInQuestion\"\r" +
    "\n" +
    "                                           value=\"JA\"\r" +
    "\n" +
    "                                           ng-model=\"radioGroups.rehab\" id=\"rehabYes\" ng-change=\"onRehabChange()\"> Ja</label>\r" +
    "\n" +
    "      </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "      <!-- NO -->\r" +
    "\n" +
    "      <div class=\"form-group\">\r" +
    "\n" +
    "        <label class=\"radio-inline\"><input type=\"radio\" name=\"recommendationsToFkReabInQuestion\" group=\"group3\"\r" +
    "\n" +
    "                                           value=\"NEJ\" ng-model=\"radioGroups.rehab\" id=\"rehabNo\" ng-change=\"onRehabChange()\">\r" +
    "\n" +
    "          Nej</label>\r" +
    "\n" +
    "      </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "      <!-- GÅR EJ ATT BEDÖMA -->\r" +
    "\n" +
    "      <div class=\"form-group\">\r" +
    "\n" +
    "        <label class=\"radio-inline\"><input type=\"radio\" name=\"recommendationsToFkReabInQuestion\" group=\"group3\"\r" +
    "\n" +
    "                                           value=\"GAREJ\" ng-model=\"radioGroups.rehab\" id=\"garej\" ng-change=\"onRehabChange()\"> Går inte\r" +
    "\n" +
    "          att bedöma</label>\r" +
    "\n" +
    "      </div>\r" +
    "\n" +
    "    </span>\r" +
    "\n" +
    "    </div>\r" +
    "\n" +
    "  </div>"
  );


  $templateCache.put('/web/webjars/fk7263/webcert/js/intyg/edit/form/form6b.html',
    "<div wc-field field-label=\"fk7263.label.atgarder\" field-number=\"6b\"\r" +
    "\n" +
    "     ng-hide=\"viewState.avstangningSmittskydd()\" field-help-text=\"fk7263.helptext.atgarder\"\r" +
    "\n" +
    "     class=\"print-pagebreak-after\">\r" +
    "\n" +
    "  <div class=\"form-group\">\r" +
    "\n" +
    "    <label class=\"control-label\">Planerad eller pågående behandling eller åtgärd inom sjukvården (ange\r" +
    "\n" +
    "      vilken)</label>\r" +
    "\n" +
    "                      <textarea id=\"measuresCurrent\" class=\"form-control\" name=\"measures.current\" rows='2'\r" +
    "\n" +
    "                                ng-model=\"model.atgardInomSjukvarden\" wc-maxlength ng-trim=\"false\"\r" +
    "\n" +
    "                                maxlength=\"{{viewState.inputLimits.atgardInomSjukvarden}}\"></textarea>\r" +
    "\n" +
    "  </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "  <div class=\"form-group\">\r" +
    "\n" +
    "    <label class=\"control-label\">Annan åtgärd (ange vilken)</label>\r" +
    "\n" +
    "                      <textarea id=\"measuresOther\" class=\"form-control\" name=\"measures.other\" rows='2'\r" +
    "\n" +
    "                                ng-model=\"model.annanAtgard\" wc-maxlength ng-trim=\"false\"\r" +
    "\n" +
    "                                maxlength=\"{{viewState.inputLimits.annanAtgard}}\"></textarea>\r" +
    "\n" +
    "  </div>\r" +
    "\n" +
    "</div>"
  );


  $templateCache.put('/web/webjars/fk7263/webcert/js/intyg/edit/form/form8a.html',
    "<div wc-field field-label=\"fk7263.label.arbete\" field-number=\"8a\"\r" +
    "\n" +
    "     field-help-text=\"fk7263.helptext.sysselsattning\" ng-hide=\"viewState.avstangningSmittskydd()\"\r" +
    "\n" +
    "     ng-animate=\"'fade'\" id=\"arbeteForm\">\r" +
    "\n" +
    "  <div class=\"alert alert-danger\" ng-if=\"viewState.common.validationMessagesGrouped.sysselsattning\" id=\"validationMessages_sysselsattning\">\r" +
    "\n" +
    "    <ul>\r" +
    "\n" +
    "      <li ng-repeat=\"message in viewState.common.validationMessagesGrouped.sysselsattning\"><span message\r" +
    "\n" +
    "                                                                                key=\"{{message.message}}\"></span>\r" +
    "\n" +
    "      </li>\r" +
    "\n" +
    "    </ul>\r" +
    "\n" +
    "  </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "  <div class=\"form-group form-inline\">\r" +
    "\n" +
    "    <label class=\"control-label\"><span message key=\"fk7263.label.arbetsformaga\"></span></label>\r" +
    "\n" +
    "    <div class=\"controls form-inline\">\r" +
    "\n" +
    "      <label class=\"checkbox\"><input type=\"checkbox\" id=\"arbeteNuvarande\"\r" +
    "\n" +
    "                                     ng-model=\"model.nuvarandeArbete\"> Nuvarande\r" +
    "\n" +
    "        arbete</label><span wc-enable-tooltip\r" +
    "\n" +
    "                            field-help-text=\"fk7263.helptext.sysselsattning.nuvarande\"></span>\r" +
    "\n" +
    "      <div class=\"indent\" ng-if=\"model.nuvarandeArbete\">\r" +
    "\n" +
    "        <label for=\"currentWork\">Ange aktuella arbetsuppgifter</label>\r" +
    "\n" +
    "        <div class=\"controls\">\r" +
    "\n" +
    "                            <textarea id=\"currentWork\" class=\"\" name=\"currentWork\" rows='2'\r" +
    "\n" +
    "                                      ng-model=\"model.nuvarandeArbetsuppgifter\" wc-maxlength ng-trim=\"false\"\r" +
    "\n" +
    "                                      maxlength=\"{{viewState.inputLimits.nuvarandeArbetsuppgifter}}\"></textarea>\r" +
    "\n" +
    "        </div>\r" +
    "\n" +
    "      </div>\r" +
    "\n" +
    "    </div>\r" +
    "\n" +
    "  </div>\r" +
    "\n" +
    "  <div class=\"form-group form-inline\">\r" +
    "\n" +
    "    <label class=\"checkbox\"><input type=\"checkbox\" id=\"arbeteArbetslos\" ng-model=\"model.arbetsloshet\">\r" +
    "\n" +
    "      Arbetslöshet - att utföra arbete på den\r" +
    "\n" +
    "      reguljära arbetsmarknaden</label><span wc-enable-tooltip\r" +
    "\n" +
    "                                             field-help-text=\"fk7263.helptext.sysselsattning.arbetsloshet\"></span>\r" +
    "\n" +
    "  </div>\r" +
    "\n" +
    "  <div class=\"form-group form-inline\">\r" +
    "\n" +
    "    <label class=\"checkbox\"><input type=\"checkbox\" id=\"arbeteForaldraledig\"\r" +
    "\n" +
    "                                   ng-model=\"model.foraldrarledighet\">\r" +
    "\n" +
    "      Föräldraledighet med föräldrapenning -\r" +
    "\n" +
    "      att vårda sitt barn</label><span wc-enable-tooltip\r" +
    "\n" +
    "                                       field-help-text=\"fk7263.helptext.sysselsattning.foraldrarledighet\"></span>\r" +
    "\n" +
    "  </div>\r" +
    "\n" +
    "</div>"
  );


  $templateCache.put('/web/webjars/fk7263/webcert/js/intyg/edit/form/form8b.html',
    "<div ng-form=\"form8b\">\r" +
    "\n" +
    "  <div wc-field field-label=\"fk7263.label.nedsattning\" field-number=\"8b\" field-help-text=\"fk7263.helptext.arbetsformaga\"\r" +
    "\n" +
    "       class=\"arbetsformaga print-pagebreak-after\" id=\"arbetsformagaForm\">\r" +
    "\n" +
    "    <div class=\"alert alert-danger\" ng-if=\"viewState.common.validationMessagesGrouped.arbetsformaga\"\r" +
    "\n" +
    "         id=\"validationMessages_arbetsformaga\">\r" +
    "\n" +
    "      <ul>\r" +
    "\n" +
    "        <li ng-repeat=\"message in viewState.common.validationMessagesGrouped.arbetsformaga\"><span message\r" +
    "\n" +
    "                                                                                                            key=\"{{message.message}}\"></span>\r" +
    "\n" +
    "        </li>\r" +
    "\n" +
    "      </ul>\r" +
    "\n" +
    "    </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "    <div class=\"form-group form-inline\">\r" +
    "\n" +
    "      Faktisk tjänstgöringstid <input style=\"width: 3em;\" type=\"text\" wc-decimal-number\r" +
    "\n" +
    "                                      id=\"capacityForWorkActualWorkingHoursPerWeek\"\r" +
    "\n" +
    "                                      name=\"capacityForWork.actualWorkingHoursPerWeek\" value=\"\"\r" +
    "\n" +
    "                                      class='short control-element form-control' maxlength=\"5\"\r" +
    "\n" +
    "                                      ng-model=\"model.tjanstgoringstid\"> <span>timmar/vecka</span> <span\r" +
    "\n" +
    "        wc-enable-tooltip field-help-text=\"fk7263.helptext.arbetsformaga.faktisk-tjanstgoringstid\"></span>\r" +
    "\n" +
    "    </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "    <!-- 25% -->\r" +
    "\n" +
    "    <div class=\"form-group form-inline\">\r" +
    "\n" +
    "      <label class=\"checkbox\" style=\"min-width: 250px\"><input type=\"checkbox\" id=\"nedsattMed25\"\r" +
    "\n" +
    "                                                              name=\"capacityForWork.work25\"\r" +
    "\n" +
    "                                                              ng-change=\"field8b.onChangeWorkStateCheck('nedsattMed25')\"\r" +
    "\n" +
    "                                                              ng-model=\"field8b.nedsattMed25.workState\">\r" +
    "\n" +
    "        25%\r" +
    "\n" +
    "        nedsatt arbetsförmåga från</label>\r" +
    "\n" +
    "                      <span wc-date-picker-field target-model=\"model.nedsattMed25.from\"\r" +
    "\n" +
    "                            on-change=\"onChangeNedsattMed('nedsattMed25', 'from')\" dom-id=\"nedsattMed25from\"\r" +
    "\n" +
    "                            invalid=\"field8b.nedsattMed25.nedsattInvalidFrom\"></span>\r" +
    "\n" +
    "      <span>till</span>\r" +
    "\n" +
    "                      <span wc-date-picker-field target-model=\"model.nedsattMed25.tom\"\r" +
    "\n" +
    "                            on-change=\"onChangeNedsattMed('nedsattMed25', 'tom')\" dom-id=\"nedsattMed25tom\"\r" +
    "\n" +
    "                            invalid=\"field8b.nedsattMed25.nedsattInvalidTom\"></span>\r" +
    "\n" +
    "      <div class=\"indent pad10\" ng-if=\"field8b.nedsattMed25.workState\">\r" +
    "\n" +
    "        <div class=\"workHoursPerWeek\" ng-show=\"model.tjanstgoringstid != '' && model.tjanstgoringstid > 0\">\r" +
    "\n" +
    "          Arbetstid timmar/vecka: <strong id=\"arbetstid25\">{{model.tjanstgoringstid * 0.75 |\r" +
    "\n" +
    "          number}}</strong>\r" +
    "\n" +
    "        </div>\r" +
    "\n" +
    "        Arbetstidsförläggning (frivilligt) <input type=\"text\" ng-model=\"model.nedsattMed25Beskrivning\"\r" +
    "\n" +
    "                                                  id=\"nedsattMed25beskrivning\"\r" +
    "\n" +
    "                                                  id=\"capacityForWork.rec25LongerWorkingHours\"\r" +
    "\n" +
    "                                                  name=\"capacityForWork.rec25LongerWorkingHours\" value=\"\"\r" +
    "\n" +
    "                                                  class='form-control otherInformation'\r" +
    "\n" +
    "                                                  ng-change=\"viewState.limitOtherField('nedsattMed25Beskrivning')\"\r" +
    "\n" +
    "                                                  wc-disable-key-down>\r" +
    "\n" +
    "        <span class=\"counter\">Tecken kvar:  {{viewState.inputLimits.ovrigt - viewState.getTotalOvrigtLength()}}</span>\r" +
    "\n" +
    "        <div class=\"info-transfer\">Informationen överförs till \"Övriga upplysningar\" vid PDF-utskrift av\r" +
    "\n" +
    "          blanketten.\r" +
    "\n" +
    "        </div>\r" +
    "\n" +
    "      </div>\r" +
    "\n" +
    "    </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "    <!-- 50% -->\r" +
    "\n" +
    "    <div class=\"form-group form-inline\">\r" +
    "\n" +
    "      <label class=\"checkbox\" style=\"min-width: 250px\"><input type=\"checkbox\" id=\"nedsattMed50\"\r" +
    "\n" +
    "                                                              name=\"capacityForWork.work50\"\r" +
    "\n" +
    "                                                              ng-change=\"field8b.onChangeWorkStateCheck('nedsattMed50')\"\r" +
    "\n" +
    "                                                              ng-model=\"field8b.nedsattMed50.workState\"> 50%\r" +
    "\n" +
    "        nedsatt arbetsförmåga från</label>\r" +
    "\n" +
    "                      <span wc-date-picker-field target-model=\"model.nedsattMed50.from\"\r" +
    "\n" +
    "                            on-change=\"onChangeNedsattMed('nedsattMed50', 'from')\" dom-id=\"nedsattMed50from\"\r" +
    "\n" +
    "                            invalid=\"field8b.nedsattMed50.nedsattInvalidFrom\"></span>\r" +
    "\n" +
    "      <span>till</span>\r" +
    "\n" +
    "                      <span wc-date-picker-field target-model=\"model.nedsattMed50.tom\"\r" +
    "\n" +
    "                            on-change=\"onChangeNedsattMed('nedsattMed50', 'tom')\" dom-id=\"nedsattMed50tom\"\r" +
    "\n" +
    "                            invalid=\"field8b.nedsattMed50.nedsattInvalidTom\"></span>\r" +
    "\n" +
    "      <div class=\"indent pad10\" ng-if=\"field8b.nedsattMed50.workState\">\r" +
    "\n" +
    "        <div class=\"workHoursPerWeek\" ng-show=\"model.tjanstgoringstid != '' && model.tjanstgoringstid > 0\">\r" +
    "\n" +
    "          Arbetstid timmar/vecka: <strong id=\"arbetstid50\">{{model.tjanstgoringstid * 0.5 |\r" +
    "\n" +
    "          number}}</strong>\r" +
    "\n" +
    "        </div>\r" +
    "\n" +
    "        Arbetstidsförläggning (frivilligt) <input type=\"text\" ng-model=\"model.nedsattMed50Beskrivning\"\r" +
    "\n" +
    "                                                  id=\"nedsattMed50beskrivning\"\r" +
    "\n" +
    "                                                  name=\"capacityForWork.rec50LongerWorkingHours\" value=\"\"\r" +
    "\n" +
    "                                                  class='form-control otherInformation'\r" +
    "\n" +
    "                                                  ng-change=\"viewState.limitOtherField('nedsattMed50Beskrivning')\"\r" +
    "\n" +
    "                                                  wc-disable-key-down>\r" +
    "\n" +
    "        <span class=\"counter\">Tecken kvar:  {{viewState.inputLimits.ovrigt - viewState.getTotalOvrigtLength()}}</span>\r" +
    "\n" +
    "        <div class=\"info-transfer\">Informationen överförs till \"Övriga upplysningar\" vid PDF-utskrift av\r" +
    "\n" +
    "          blanketten.\r" +
    "\n" +
    "        </div>\r" +
    "\n" +
    "      </div>\r" +
    "\n" +
    "    </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "    <!-- 75% -->\r" +
    "\n" +
    "    <div class=\"form-group form-inline\">\r" +
    "\n" +
    "      <label class=\"checkbox\" style=\"min-width: 250px\"><input type=\"checkbox\" id=\"nedsattMed75\"\r" +
    "\n" +
    "                                                              name=\"capacityForWork.work75\"\r" +
    "\n" +
    "                                                              ng-change=\"field8b.onChangeWorkStateCheck('nedsattMed75')\"\r" +
    "\n" +
    "                                                              ng-model=\"field8b.nedsattMed75.workState\"> 75%\r" +
    "\n" +
    "        nedsatt arbetsförmåga från</label>\r" +
    "\n" +
    "                      <span wc-date-picker-field target-model=\"model.nedsattMed75.from\"\r" +
    "\n" +
    "                            on-change=\"onChangeNedsattMed('nedsattMed75', 'from')\" dom-id=\"nedsattMed75from\"\r" +
    "\n" +
    "                            invalid=\"field8b.nedsattMed75.nedsattInvalidFrom\"></span>\r" +
    "\n" +
    "      <span>till</span>\r" +
    "\n" +
    "                      <span wc-date-picker-field target-model=\"model.nedsattMed75.tom\"\r" +
    "\n" +
    "                            on-change=\"onChangeNedsattMed('nedsattMed75', 'tom')\" dom-id=\"nedsattMed75tom\"\r" +
    "\n" +
    "                            invalid=\"field8b.nedsattMed75.nedsattInvalidTom\"></span>\r" +
    "\n" +
    "      <div class=\"indent pad10\" ng-if=\"field8b.nedsattMed75.workState\">\r" +
    "\n" +
    "        <div class=\"workHoursPerWeek\" ng-show=\"model.tjanstgoringstid != '' && model.tjanstgoringstid > 0\">\r" +
    "\n" +
    "          Arbetstid timmar/vecka: <strong id=\"arbetstid75\">{{model.tjanstgoringstid * 0.25 |\r" +
    "\n" +
    "          number}}</strong>\r" +
    "\n" +
    "        </div>\r" +
    "\n" +
    "        Arbetstidsförläggning (frivilligt) <input type=\"text\" ng-model=\"model.ovrigt.nedsattMed75Beskrivning\"\r" +
    "\n" +
    "                                                  id=\"nedsattMed75beskrivning\"\r" +
    "\n" +
    "                                                  name=\"capacityForWork.rec75LongerWorkingHours\" value=\"\"\r" +
    "\n" +
    "                                                  class='form-control otherInformation'\r" +
    "\n" +
    "                                                  ng-change=\"viewState.limitOtherField('nedsattMed75Beskrivning')\"\r" +
    "\n" +
    "                                                  wc-disable-key-down>\r" +
    "\n" +
    "        <span class=\"counter\">Tecken kvar:  {{viewState.inputLimits.ovrigt - viewState.getTotalOvrigtLength()}}</span>\r" +
    "\n" +
    "        <div class=\"info-transfer\">Informationen överförs till \"Övriga upplysningar\" vid PDF-utskrift av\r" +
    "\n" +
    "          blanketten.\r" +
    "\n" +
    "        </div>\r" +
    "\n" +
    "      </div>\r" +
    "\n" +
    "    </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "    <!-- 100% -->\r" +
    "\n" +
    "    <div class=\"form-group form-inline\">\r" +
    "\n" +
    "      <label class=\"checkbox\" style=\"min-width: 250px\"><input type=\"checkbox\" id=\"nedsattMed100\"\r" +
    "\n" +
    "                                                              name=\"capacityForWork.workFull\"\r" +
    "\n" +
    "                                                              ng-change=\"field8b.onChangeWorkStateCheck('nedsattMed100')\"\r" +
    "\n" +
    "                                                              ng-model=\"field8b.nedsattMed100.workState\"> Helt\r" +
    "\n" +
    "        nedsatt arbetsförmåga från</label>\r" +
    "\n" +
    "                      <span wc-date-picker-field target-model=\"model.nedsattMed100.from\"\r" +
    "\n" +
    "                            on-change=\"onChangeNedsattMed('nedsattMed100', 'from')\" dom-id=\"nedsattMed100from\"\r" +
    "\n" +
    "                            invalid=\"field8b.nedsattMed100.nedsattInvalidFrom\"></span>\r" +
    "\n" +
    "      <span>till</span>\r" +
    "\n" +
    "                      <span wc-date-picker-field target-model=\"model.nedsattMed100.tom\"\r" +
    "\n" +
    "                            on-change=\"onChangeNedsattMed('nedsattMed100', 'tom')\" dom-id=\"nedsattMed100tom\"\r" +
    "\n" +
    "                            invalid=\"field8b.nedsattMed100.nedsattInvalidTom\"></span>\r" +
    "\n" +
    "    </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "    <div ng-show=\"totalCertDays\">\r" +
    "\n" +
    "      <p>Detta intyg motsvarar en period på: <span id=\"totalCertDays\">{{totalCertDays}}</span> <span\r" +
    "\n" +
    "          ng-show=\"totalCertDays > 1\">dagar</span><span ng-show=\"totalCertDays == 1\">dag</span></p>\r" +
    "\n" +
    "    </div>\r" +
    "\n" +
    "    <div class=\"alert alert-warning\" ng-show=\"datesOutOfRange\">\r" +
    "\n" +
    "      De datum du angett är <strong>mer än en vecka före dagens datum eller mer än 6 månader framåt i\r" +
    "\n" +
    "      tiden</strong>. Du bör kontrollera att tidsperioderna är korrekta.\r" +
    "\n" +
    "    </div>\r" +
    "\n" +
    "    <div class=\"alert alert-warning\" ng-show=\"datesPeriodTooLong\">\r" +
    "\n" +
    "      De datum du angett innebär <strong>en period på mer än 6 månader</strong>. Du bör kontrollera att\r" +
    "\n" +
    "      tidsperioderna är korrekta.\r" +
    "\n" +
    "    </div>\r" +
    "\n" +
    "  </div>\r" +
    "\n" +
    "</div>"
  );


  $templateCache.put('/web/webjars/fk7263/webcert/js/intyg/edit/form/form9.html',
    "<div wc-field field-label=\"fk7263.label.arbetsformaga.bedomning\" field-number=\"9\"\r" +
    "\n" +
    "     field-help-text=\"fk7263.helptext.arbetsformaga.bedoms-langre\">\r" +
    "\n" +
    "  <a class=\"soclink\" href=\"http://www.socialstyrelsen.se/riktlinjer/forsakringsmedicinsktbeslutsstod\"\r" +
    "\n" +
    "     target=\"_blank\" title=\"Öppnar Socialstyrelsens beslutstöd för angiven huvuddiagnos\">Socialstyrelsens\r" +
    "\n" +
    "    försäkringsmedicinska beslutstöd</a>\r" +
    "\n" +
    "                    <textarea id=\"capacityForWorkText\" class=\"form-control\" name=\"capacityForWork.text\" rows='10'\r" +
    "\n" +
    "                              ng-model=\"model.arbetsformagaPrognos\" wc-maxlength ng-trim=\"false\"\r" +
    "\n" +
    "                              maxlength=\"{{viewState.inputLimits.arbetsformagaPrognos}}\"></textarea>\r" +
    "\n" +
    "</div>"
  );


  $templateCache.put('/web/webjars/fk7263/webcert/js/intyg/edit/intyg-edit.html',
    "<a id=\"top\"></a>\r" +
    "\n" +
    "<div class=\"container-fluid\" id=\"edit-fk7263\">\r" +
    "\n" +
    "  <div class=\"row\">\r" +
    "\n" +
    "    <div class=\"col-md-12 webcert-col webcert-col-single\">\r" +
    "\n" +
    "\r" +
    "\n" +
    "      <div ui-view=\"header\"></div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "      <!-- Integration deleted message -->\r" +
    "\n" +
    "      <div id=\"integration-deleted\" ng-if=\"viewState.common.deleted\">\r" +
    "\n" +
    "        <div class=\"alert alert-info banner-margin\">\r" +
    "\n" +
    "          Utkastet är raderat och borttaget från Webcert.\r" +
    "\n" +
    "        </div>\r" +
    "\n" +
    "      </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "      <!-- FK 7263 -->\r" +
    "\n" +
    "      <div class=\"edit-form\" ng-class=\"{'collapsed' : viewState.common.collapsedHeader}\">\r" +
    "\n" +
    "\r" +
    "\n" +
    "        <form name=\"certForm\" novalidate autocomplete=\"off\" wc-auto-save=\"save(true)\">\r" +
    "\n" +
    "          <div ng-hide=\"viewState.common.error.activeErrorMessageKey || viewState.common.deleted\">\r" +
    "\n" +
    "\r" +
    "\n" +
    "            <div wc-spinner label=\"info.loadingdata\" show-spinner=\"!viewState.common.doneLoading\"\r" +
    "\n" +
    "                 show-content=\"viewState.common.doneLoading\">\r" +
    "\n" +
    "\r" +
    "\n" +
    "              <div class=\"alert alert-danger\" ng-show=\"viewState.common.showComplete && !viewState.common.intyg.isComplete\"\r" +
    "\n" +
    "                   style=\"margin-right: 20px\">\r" +
    "\n" +
    "                <h3><span message key=\"draft.saknar-uppgifter\"></span></h3>\r" +
    "\n" +
    "                <ul>\r" +
    "\n" +
    "                  <li ng-repeat=\"message in viewState.common.validationMessages\">\r" +
    "\n" +
    "                    <span message key=\"{{message.message}}\"></span></li>\r" +
    "\n" +
    "                </ul>\r" +
    "\n" +
    "              </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "              <div id=\"certificate\" class=\"row certificate\">\r" +
    "\n" +
    "                <div class=\"col-md-12 cert-body\">\r" +
    "\n" +
    "\r" +
    "\n" +
    "                  <!-- 1. Smittskydd -->\r" +
    "\n" +
    "                  <div ui-view=\"form1\"/>\r" +
    "\n" +
    "\r" +
    "\n" +
    "                  <!-- 4b. Intyger baseras på -->\r" +
    "\n" +
    "                  <div ui-view=\"form4b\"/>\r" +
    "\n" +
    "\r" +
    "\n" +
    "                  <!-- 2. Diagnosis -->\r" +
    "\n" +
    "                  <div ui-view=\"form2\"/>\r" +
    "\n" +
    "\r" +
    "\n" +
    "                  <!-- 3. Sjukdomsförlopp-->\r" +
    "\n" +
    "                  <div ui-view=\"form3\"/>\r" +
    "\n" +
    "\r" +
    "\n" +
    "                  <!-- 4. Funktionsnedsättning-->\r" +
    "\n" +
    "                  <div ui-view=\"form4\"/>\r" +
    "\n" +
    "\r" +
    "\n" +
    "                  <!-- 5. Aktivitetsbegränsning -->\r" +
    "\n" +
    "                  <div ui-view=\"form5\"/>\r" +
    "\n" +
    "\r" +
    "\n" +
    "                  <!-- 8a. Arbete -->\r" +
    "\n" +
    "                  <div ui-view=\"form8a\"/>\r" +
    "\n" +
    "\r" +
    "\n" +
    "                  <!-- 8b. Arbetsförmåga -->\r" +
    "\n" +
    "                  <div ui-view=\"form8b\"/>\r" +
    "\n" +
    "\r" +
    "\n" +
    "                  <!-- 9. Arbetsförmåga -->\r" +
    "\n" +
    "                  <div ui-view=\"form9\"/>\r" +
    "\n" +
    "\r" +
    "\n" +
    "                  <!-- 10. Prognos -->\r" +
    "\n" +
    "                  <div ui-view=\"form10\"/>\r" +
    "\n" +
    "\r" +
    "\n" +
    "                  <!-- 6b. Åtgärder -->\r" +
    "\n" +
    "                  <div ui-view=\"form6b\"/>\r" +
    "\n" +
    "\r" +
    "\n" +
    "                  <!-- 6a, 7, 11. Rekommendationer -->\r" +
    "\n" +
    "                  <div ui-view=\"form6a-7-11\"/>\r" +
    "\n" +
    "\r" +
    "\n" +
    "                  <!-- 12. Kontakt -->\r" +
    "\n" +
    "                  <div ui-view=\"form12\"/>\r" +
    "\n" +
    "\r" +
    "\n" +
    "                  <!-- 13. upplysningar -->\r" +
    "\n" +
    "                  <div ui-view=\"form13\"/>\r" +
    "\n" +
    "\r" +
    "\n" +
    "                  <!-- 15. upplysningar -->\r" +
    "\n" +
    "                  <div ui-view=\"form15\"/>\r" +
    "\n" +
    "\r" +
    "\n" +
    "                  <div wc-field-signing-doctor></div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "                </div>\r" +
    "\n" +
    "                <!-- cert body -->\r" +
    "\n" +
    "\r" +
    "\n" +
    "              </div>\r" +
    "\n" +
    "              <!-- certificate -->\r" +
    "\n" +
    "\r" +
    "\n" +
    "              <div ng-if=\"!isSigned\" class=\"print-hide\">\r" +
    "\n" +
    "\r" +
    "\n" +
    "                <h3><span message key=\"common.sign.intyg\"></span></h3>\r" +
    "\n" +
    "\r" +
    "\n" +
    "                <div ng-if=\"!user.userContext.lakare\">\r" +
    "\n" +
    "                  <div id=\"sign-requires-doctor-message-text\" class=\"alert alert-info\">\r" +
    "\n" +
    "                    <span message key=\"draft.onlydoctorscansign\"></span>\r" +
    "\n" +
    "                  </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "                  <div wc-feature-not-active feature=\"franJournalsystem\">\r" +
    "\n" +
    "                    <p>\r" +
    "\n" +
    "                      <span message key=\"draft.notifydoctor\"></span>\r" +
    "\n" +
    "                    </p>\r" +
    "\n" +
    "                    <div class=\"form-group\">\r" +
    "\n" +
    "                      <button id=\"vidarebefordraEjHanterad\" class=\"btn\"\r" +
    "\n" +
    "                              ng-class=\"{'btn-info': !certMeta.vidarebefordrad, 'btn-default btn-secondary' : certMeta.vidarebefordrad}\"\r" +
    "\n" +
    "                              title=\"Skicka mejl med en länk till utkastet för att informera den läkare som ska signera det.\"\r" +
    "\n" +
    "                              ng-click=\"openMailDialog()\">\r" +
    "\n" +
    "                        <img ng-if=\"!certMeta.vidarebefordrad\" src=\"/img/mail.png\"> <img ng-if=\"certMeta.vidarebefordrad\"\r" +
    "\n" +
    "                                                                                         src=\"/img/mail_dark.png\">\r" +
    "\n" +
    "                      </button>\r" +
    "\n" +
    "                    </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "                    <div class=\"body-row\">\r" +
    "\n" +
    "                      <label for=\"mark-as-vidarebefordrad\">\r" +
    "\n" +
    "                        <input id=\"mark-as-vidarebefordrad\" type=\"checkbox\"\r" +
    "\n" +
    "                               ng-disabled=\"viewState.common.vidarebefordraInProgress\" ng-model=\"certMeta.vidarebefordrad\"\r" +
    "\n" +
    "                               ng-change=\"onVidarebefordradChange()\" /> Vidarebefordrad\r" +
    "\n" +
    "                      </label> <span ng-if=\"viewState.common.vidarebefordraInProgress\"> <img\r" +
    "\n" +
    "                        src=\"/img/ajax-loader-kit-16x16.gif\"></span>\r" +
    "\n" +
    "                    </div>\r" +
    "\n" +
    "                  </div>\r" +
    "\n" +
    "                </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "                <button id=\"signera-utkast-button\" class=\"btn btn-success\" ng-disabled=\"!viewState.common.intyg.isComplete || certForm.$dirty || signingWithSITHSInProgress\"\r" +
    "\n" +
    "                        ng-click=\"sign()\" ng-show=\"user.userContext.lakare\"><img ng-show=\"signingWithSITHSInProgress\" ng-src=\"/img/ajax-loader-small-green.gif\"/> <span message key=\"common.sign\"></span>\r" +
    "\n" +
    "                </button>\r" +
    "\n" +
    "\r" +
    "\n" +
    "              </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "            </div>\r" +
    "\n" +
    "          </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "          <wc-disable-key-down wc-disable-key-down-elements=\":text,:checkbox,:radio\" wc-disable-key-down-done-loading=\"viewState.common.doneLoading\" />\r" +
    "\n" +
    "\r" +
    "\n" +
    "        </form>\r" +
    "\n" +
    "      </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "    </div>\r" +
    "\n" +
    "    <!-- span12 webcert-col-single -->\r" +
    "\n" +
    "  </div>\r" +
    "\n" +
    "  <!-- row -->\r" +
    "\n" +
    "</div> <!-- container -->\r" +
    "\n"
  );


  $templateCache.put('/web/webjars/fk7263/webcert/js/intyg/view/intyg-view.html',
    "<div class=\"weak-hidden\" ng-class=\"{show: widgetState.showTemplate}\" ng-controller=\"fk7263.ViewCertCtrl\">\r" +
    "\n" +
    "  <div wc-spinner label=\"fk7263.info.loadingcertificate\" show-spinner=\"!widgetState.doneLoading\"\r" +
    "\n" +
    "       show-content=\"widgetState.doneLoading\">\r" +
    "\n" +
    "\r" +
    "\n" +
    "    <div ui-view=\"header\"></div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "    <div ng-show=\"!widgetState.activeErrorMessageKey\">\r" +
    "\n" +
    "      <div id=\"certificate\" ng-class=\"{'certificate': true, 'revoked-certificate' : certProperties.isRevoked}\">\r" +
    "\n" +
    "        <div class=\"row\">\r" +
    "\n" +
    "          <div class=\"col-md-12\">\r" +
    "\n" +
    "            <div class=\"cert-body\">\r" +
    "\n" +
    "\r" +
    "\n" +
    "              <div wc-field field-number=\"1\" field-label=\"fk7263.label.smittskydd\" filled=\"{{true}}\">\r" +
    "\n" +
    "                <span ng-show=\"cert.avstangningSmittskydd\" id=\"smittskydd\"> <span message key=\"fk7263.label.yes\"></span></span>\r" +
    "\n" +
    "                <span ng-show=\"!cert.avstangningSmittskydd\"> <span message key=\"fk7263.label.no\"></span></span>\r" +
    "\n" +
    "              </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "              <div wc-field field-number=\"2\" field-label=\"fk7263.label.diagnos\"\r" +
    "\n" +
    "                   filled=\"{{cert.diagnosKod!=null}}\" id=\"field2\">\r" +
    "\n" +
    "                <div class=\"form-group\">\r" +
    "\n" +
    "                  <span message key=\"fk7263.label.diagnoskod.icd\"></span>\r" +
    "\n" +
    "                  <span id=\"diagnosKod\">{{cert.diagnosKod}}</span> <span id=\"diagnosBeskrivning1\">{{cert.diagnosBeskrivning1}}</span>\r" +
    "\n" +
    "                </div>\r" +
    "\n" +
    "                <div class=\"form-group\">\r" +
    "\n" +
    "                  <label class=\"control-label bold\"\r" +
    "\n" +
    "                         ng-show=\"cert.diagnosKod2 || cert.diagnosBeskrivning2 || cert.diagnosKod3 || cert.diagnosBeskrivning3\">\r" +
    "\n" +
    "                    <span message key=\"fk7263.label.ytterligarediagnoser\"></span>\r" +
    "\n" +
    "                  </label>\r" +
    "\n" +
    "                  <div class=\"controls\">\r" +
    "\n" +
    "                    <p ng-show=\"cert.diagnosKod2 || cert.diagnosBeskrivning2 \"><span id=\"diagnosKod2\">{{cert.diagnosKod2}}</span>\r" +
    "\n" +
    "                      <span id=\"diagnosBeskrivning2\">{{cert.diagnosBeskrivning2}}</span></p>\r" +
    "\n" +
    "                    <p ng-show=\"cert.diagnosKod3 || cert.diagnosBeskrivning3\"><span id=\"diagnosKod3\">{{cert.diagnosKod3}}</span>\r" +
    "\n" +
    "                      <span id=\"diagnosBeskrivning3\">{{cert.diagnosBeskrivning3}}</span></p>\r" +
    "\n" +
    "                    <div id=\"samsjuklighet\" ng-show=\"cert.samsjuklighet\" class=\"bold\"><span message\r" +
    "\n" +
    "                                                                         key=\"fk7263.label.samsjuklighet\"></span></div>\r" +
    "\n" +
    "                  </div>\r" +
    "\n" +
    "                </div>\r" +
    "\n" +
    "                <div class=\"form-group\" ng-show=\"cert.diagnosBeskrivning\">\r" +
    "\n" +
    "                  <label class=\"control-label bold\">\r" +
    "\n" +
    "                    <span message key=\"fk7263.label.diagnosfortydligande\"></span>\r" +
    "\n" +
    "                  </label>\r" +
    "\n" +
    "                  <div class=\"controls\">\r" +
    "\n" +
    "                    <div id=\"diagnosBeskrivning\" class=\"multiline\">{{cert.diagnosBeskrivning}}</div>\r" +
    "\n" +
    "                  </div>\r" +
    "\n" +
    "                </div>\r" +
    "\n" +
    "              </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "              <div wc-field field-number=\"3\" field-label=\"fk7263.label.aktuellt-sjukdomsforlopp\"\r" +
    "\n" +
    "                   filled=\"{{cert.sjukdomsforlopp!=null}}\"><div id=\"sjukdomsforlopp\" class=\"multiline\">{{cert.sjukdomsforlopp}}</div>\r" +
    "\n" +
    "              </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "              <div wc-field field-number=\"4\" field-label=\"fk7263.label.funktionsnedsattningar\"\r" +
    "\n" +
    "                   filled=\"{{cert.funktionsnedsattning!=null}}\"><div id=\"funktionsnedsattning\" class=\"multiline\">{{cert.funktionsnedsattning}}</div>\r" +
    "\n" +
    "              </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "              <div wc-field field-number=\"4b\" field-label=\"fk7263.label.intyg-baserat-pa\" id=\"field4b\"\r" +
    "\n" +
    "                   filled=\"{{cert.undersokningAvPatienten!=null || cert.telefonkontaktMedPatienten!=null || cert.journaluppgifter!=null || cert.annanReferens!=null}}\">\r" +
    "\n" +
    "                <ul class=\"padded-list\" id=\"baseratPaList\">\r" +
    "\n" +
    "                  <li ng-show=\"cert.undersokningAvPatienten!=null\"><span message\r" +
    "\n" +
    "                                                                         key=\"fk7263.vardkontakt.undersokning\"></span><span id=\"undersokningAvPatienten\">{{cert.undersokningAvPatienten\r" +
    "\n" +
    "                    | date:'longDate' }}</span>\r" +
    "\n" +
    "                  </li>\r" +
    "\n" +
    "                  <li ng-show=\"cert.telefonkontaktMedPatienten!=null\"><span message\r" +
    "\n" +
    "                                                                            key=\"fk7263.vardkontakt.telefonkontakt\"></span><span id=\"telefonkontaktMedPatienten\">{{cert.telefonkontaktMedPatienten\r" +
    "\n" +
    "                    | date:'longDate' }}</span>\r" +
    "\n" +
    "                  </li>\r" +
    "\n" +
    "                  <li ng-show=\"cert.journaluppgifter!=null\"><span message key=\"fk7263.referens.journal\"></span><span id=\"journaluppgifter\">{{cert.journaluppgifter\r" +
    "\n" +
    "                    | date:'longDate' }}</span>\r" +
    "\n" +
    "                  </li>\r" +
    "\n" +
    "\r" +
    "\n" +
    "                  <li ng-show=\"cert.annanReferens!=null\">\r" +
    "\n" +
    "                    <!-- Annat -->\r" +
    "\n" +
    "                    <span message key=\"fk7263.referens.annat\"></span> <span id=\"annanReferens\">{{cert.annanReferens | date:'longDate'}}</span>:\r" +
    "\n" +
    "                    <div id=\"annanReferensBeskrivning\" class=\"multiline\">{{cert.annanReferensBeskrivning}}</div>\r" +
    "\n" +
    "                  </li>\r" +
    "\n" +
    "                </ul>\r" +
    "\n" +
    "              </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "              <div wc-field field-number=\"5\" field-label=\"fk7263.label.aktivitetsbegransning\"\r" +
    "\n" +
    "                   filled=\"{{cert.aktivitetsbegransning!=null}}\"><div id=\"aktivitetsbegransning\" class=\"multiline\">{{cert.aktivitetsbegransning}}</div>\r" +
    "\n" +
    "              </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "              <div wc-field field-number=\"6a\" field-label=\"fk7263.label.rekommendationer\" id=\"field6a\"\r" +
    "\n" +
    "                   filled=\"{{cert.rekommendationKontaktArbetsformedlingen || cert.rekommendationKontaktForetagshalsovarden || cert.rekommendationOvrigt!=null}}\">\r" +
    "\n" +
    "                <ul class=\"padded-list\">\r" +
    "\n" +
    "                  <li id=\"rekommendationKontaktArbetsformedlingen\" ng-show=\"cert.rekommendationKontaktArbetsformedlingen\"><span message\r" +
    "\n" +
    "                                                                                   key=\"fk7263.label.rekommendationer.kontakt.arbetsformedlingen\"></span>\r" +
    "\n" +
    "                  </li>\r" +
    "\n" +
    "                  <li id=\"rekommendationKontaktForetagshalsovarden\" ng-show=\"cert.rekommendationKontaktForetagshalsovarden\"><span message\r" +
    "\n" +
    "                                                                                    key=\"fk7263.label.rekommendationer.kontakt.foretagshalsovarden\"></span>\r" +
    "\n" +
    "                  </li>\r" +
    "\n" +
    "                  <li ng-show=\"cert.rekommendationOvrigt\"><span message\r" +
    "\n" +
    "                                                                key=\"fk7263.label.rekommendationer.kontakt.ovrigt\"></span>\r" +
    "\n" +
    "                    <span id=\"rekommendationOvrigt\">{{cert.rekommendationOvrigt}}</span>\r" +
    "\n" +
    "                  </li>\r" +
    "\n" +
    "                </ul>\r" +
    "\n" +
    "              </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "              <div wc-field field-number=\"6b\" field-label=\"fk7263.label.planerad-atgard\"\r" +
    "\n" +
    "                   filled=\"{{cert.atgardInomSjukvarden!=null||cert.annanAtgard!=null}}\" id=\"field6b\">\r" +
    "\n" +
    "                <ul class=\"padded-list\">\r" +
    "\n" +
    "                  <li ng-show=\"cert.atgardInomSjukvarden != null\">\r" +
    "\n" +
    "                    <span message key=\"fk7263.label.planerad-atgard.sjukvarden\"></span>\r" +
    "\n" +
    "                    <div id=\"atgardInomSjukvarden\" class=\"multiline\">{{cert.atgardInomSjukvarden}}</div>\r" +
    "\n" +
    "                  </li>\r" +
    "\n" +
    "                  <li ng-show=\"cert.annanAtgard != null\">\r" +
    "\n" +
    "                    <span message key=\"fk7263.label.planerad-atgard.annat\"></span>\r" +
    "\n" +
    "                    <div id=\"annanAtgard\" class=\"multiline\">{{cert.annanAtgard}}</div>\r" +
    "\n" +
    "                  </li>\r" +
    "\n" +
    "                </ul>\r" +
    "\n" +
    "              </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "              <div wc-field field-number=\"7\" field-label=\"fk7263.label.rehabilitering-aktuell\"\r" +
    "\n" +
    "                   filled=\"{{cert.rehabilitering!=null}}\"\r" +
    "\n" +
    "                   id=\"field7\" class=\"print-pagebreak-after\">\r" +
    "\n" +
    "                <span id=\"rehabiliteringAktuell\" ng-show=\"cert.rehabilitering == 'rehabiliteringAktuell'\"><span message key=\"fk7263.label.yes\"></span></span>\r" +
    "\n" +
    "                <span id=\"rehabiliteringEjAktuell\" ng-show=\"cert.rehabilitering == 'rehabiliteringEjAktuell'\"><span message key=\"fk7263.label.no\"></span></span>\r" +
    "\n" +
    "                <span id=\"rehabiliteringGarInteAttBedoma\" ng-show=\"cert.rehabilitering == 'rehabiliteringGarInteAttBedoma'\"><span message key=\"fk7263.label.gar-ej-att-bedomma\"></span></span>\r" +
    "\n" +
    "              </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "              <div wc-field field-number=\"8a\" field-label=\"fk7263.label.arbetsformaga\"\r" +
    "\n" +
    "                   filled=\"{{cert.nuvarandeArbetsuppgifter||cert.arbetsloshet||cert.foraldrarledighet}}\" id=\"field8\">\r" +
    "\n" +
    "                <ul class=\"padded-list\">\r" +
    "\n" +
    "                  <li ng-show=\"cert.nuvarandeArbetsuppgifter\"><span message\r" +
    "\n" +
    "                                                                    key=\"fk7263.label.arbetsformaga.nuvarande-arbete\"></span>\r" +
    "\n" +
    "                    <div id=\"nuvarandeArbetsuppgifter\" class=\"multiline\">{{cert.nuvarandeArbetsuppgifter}}</div>\r" +
    "\n" +
    "                  </li>\r" +
    "\n" +
    "                  <li id=\"arbetsloshet\" ng-show=\"cert.arbetsloshet\"><span message key=\"fk7263.label.arbetsformaga.arbetslos\"></span></li>\r" +
    "\n" +
    "                  <li id=\"foraldrarledighet\" ng-show=\"cert.foraldrarledighet\"><span message\r" +
    "\n" +
    "                                                             key=\"fk7263.label.arbetsformaga.foraldrarledighet\"></span>\r" +
    "\n" +
    "                  </li>\r" +
    "\n" +
    "                </ul>\r" +
    "\n" +
    "              </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "              <div wc-field field-number=\"8b\" field-label=\"fk7263.label.nedsattning\"\r" +
    "\n" +
    "                   filled=\"{{cert.nedsattMed25!=null||cert.nedsattMed50!=null||cert.nedsattMed75!=null||cert.nedsattMed100!=null}}\"\r" +
    "\n" +
    "                   id=\"field8b\">\r" +
    "\n" +
    "\r" +
    "\n" +
    "                <div ng-show=\"cert.nedsattMed25 != null\">\r" +
    "\n" +
    "                  <strong><span message key=\"fk7263.nedsattningsgrad.nedsatt_med_1_4\"></span></strong><br> Från och med\r" +
    "\n" +
    "                  <span id=\"nedsattMed25from\">{{cert.nedsattMed25.from | date:'longDate'}}</span> &ndash; längst till och med\r" +
    "\n" +
    "                  <span id=\"nedsattMed25tom\">{{cert.nedsattMed25.tom | date:'longDate'}}</span>\r" +
    "\n" +
    "                  <div id=\"nedsattMed25Beskrivning\" ng-show=\"cert.nedsattMed25Beskrivning\" class=\"multiline\">Arbetstidsförläggning: {{cert.nedsattMed25Beskrivning}}\r" +
    "\n" +
    "                  </div>\r" +
    "\n" +
    "                </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "                <div ng-show=\"cert.nedsattMed50 != null\">\r" +
    "\n" +
    "                  <strong><span message key=\"fk7263.nedsattningsgrad.nedsatt_med_1_2\"></span></strong><br> Från och med\r" +
    "\n" +
    "                  <span id=\"nedsattMed50from\">{{cert.nedsattMed50.from | date:'longDate'}}</span> &ndash; längst till och med\r" +
    "\n" +
    "                  <span id=\"nedsattMed50tom\">{{cert.nedsattMed50.tom | date:'longDate'}}</span>\r" +
    "\n" +
    "                  <div id=\"nedsattMed50Beskrivning\" ng-show=\"cert.nedsattMed50Beskrivning\" class=\"multiline\">Arbetstidsförläggning: {{cert.nedsattMed50Beskrivning}}\r" +
    "\n" +
    "                  </div>\r" +
    "\n" +
    "                </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "                <div ng-show=\"cert.nedsattMed75 != null\">\r" +
    "\n" +
    "                  <strong><span message key=\"fk7263.nedsattningsgrad.nedsatt_med_3_4\"></span></strong><br> Från och med\r" +
    "\n" +
    "                  <span id=\"nedsattMed75from\">{{cert.nedsattMed75.from | date:'longDate'}}</span> &ndash; längst till och med\r" +
    "\n" +
    "                  <span id=\"nedsattMed75tom\">{{cert.nedsattMed75.tom | date:'longDate'}}</span>\r" +
    "\n" +
    "                  <div id=\"nedsattMed75Beskrivning\" ng-show=\"cert.nedsattMed75Beskrivning\" class=\"multiline\">Arbetstidsförläggning: {{cert.nedsattMed75Beskrivning}}\r" +
    "\n" +
    "                  </div>\r" +
    "\n" +
    "                </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "                <div ng-show=\"cert.nedsattMed100 != null\">\r" +
    "\n" +
    "                  <strong><span message key=\"fk7263.nedsattningsgrad.helt_nedsatt\"></span></strong><br> Från och med\r" +
    "\n" +
    "                  <span id=\"nedsattMed100from\">{{cert.nedsattMed100.from | date:'longDate'}}</span> &ndash; längst till och med\r" +
    "\n" +
    "                  <span id=\"nedsattMed100tom\">{{cert.nedsattMed100.tom | date:'longDate'}}</span>\r" +
    "\n" +
    "                </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "              </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "              <div wc-field field-number=\"9\" field-label=\"fk7263.label.arbetsformaga.bedomning\"\r" +
    "\n" +
    "                   filled=\"{{cert.arbetsformagaPrognos!=null}}\"><div id=\"arbetsformagaPrognos\" class=\"multiline\">{{cert.arbetsformagaPrognos}}</div>\r" +
    "\n" +
    "              </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "              <div wc-field field-number=\"10\" field-label=\"fk7263.label.prognos\"\r" +
    "\n" +
    "                   filled=\"{{cert.prognosBedomning!=null}}\"\r" +
    "\n" +
    "                   id=\"field10\">\r" +
    "\n" +
    "\r" +
    "\n" +
    "                <span id=\"arbetsformataPrognosJa\" ng-show=\"cert.prognosBedomning == 'arbetsformagaPrognosJa'\"><span message key=\"fk7263.label.yes\"></span></span>\r" +
    "\n" +
    "                <span id=\"arbetsformataPrognosJaDelvis\" ng-show=\"cert.prognosBedomning == 'arbetsformagaPrognosJaDelvis'\"><span message key=\"fk7263.label.delvis\"></span></span>\r" +
    "\n" +
    "                <span id=\"arbetsformataPrognosNej\" ng-show=\"cert.prognosBedomning == 'arbetsformagaPrognosNej'\"><span message key=\"fk7263.label.no\"></span></span>\r" +
    "\n" +
    "                <span id=\"arbetsformataPrognosGarInteAttBedoma\" ng-show=\"cert.prognosBedomning == 'arbetsformagaPrognosGarInteAttBedoma'\">\r" +
    "\n" +
    "                  <span message key=\"fk7263.label.gar-ej-att-bedomma\"></span>\r" +
    "\n" +
    "                  <div ng-show=\"cert.arbetsformagaPrognosGarInteAttBedomaBeskrivning\">\r" +
    "\n" +
    "                    Förtydligande: <div id=\"arbetsformagaPrognosGarInteAttBedomaBeskrivning\" class=\"multiline\">{{cert.arbetsformagaPrognosGarInteAttBedomaBeskrivning}}</div>\r" +
    "\n" +
    "                  </div>\r" +
    "\n" +
    "                </span>\r" +
    "\n" +
    "              </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "              <div wc-field field-number=\"11\" field-label=\"fk7263.label.resor-till-arbetet\"\r" +
    "\n" +
    "                   filled=\"{{cert.ressattTillArbeteAktuellt || cert.ressattTillArbeteEjAktuellt}}\" id=\"field11\">\r" +
    "\n" +
    "                <span id=\"ressattTillArbeteAktuellt\" ng-show=\"cert.ressattTillArbeteAktuellt\"> <span message key=\"fk7263.label.yes\"></span></span>\r" +
    "\n" +
    "                <span id=\"ressattTillArbeteEjAktuellt\" ng-show=\"cert.ressattTillArbeteEjAktuellt\"> <span message key=\"fk7263.label.no\"></span></span>\r" +
    "\n" +
    "              </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "              <div wc-field field-number=\"12\" field-label=\"fk7263.label.kontakt-med-fk\" filled=\"{{true}}\" id=\"field12\">\r" +
    "\n" +
    "                <span id=\"kontaktMedFk\" ng-show=\"cert.kontaktMedFk\"> <span message key=\"fk7263.label.yes\"></span></span>\r" +
    "\n" +
    "                <span ng-show=\"!cert.kontaktMedFk\"> <span message key=\"fk7263.label.no\"></span></span>\r" +
    "\n" +
    "              </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "              <div wc-field field-number=\"13\" field-label=\"fk7263.label.ovrigt\" filled=\"{{cert.kommentar!=null&&cert.kommentar!=''&&cert.kommentar!=undefined}}\"\r" +
    "\n" +
    "                   id=\"field13\">\r" +
    "\n" +
    "                <div id=\"kommentar\" class=\"multiline\">{{cert.kommentar}}</div>\r" +
    "\n" +
    "              </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "              <div wc-field field-label=\"fk7263.label.arbetsplatskod\" field-number=\"17\"\r" +
    "\n" +
    "                   filled=\"{{cert.forskrivarkodOchArbetsplatskod!=null}}\" id=\"field17\">\r" +
    "\n" +
    "                <!--  leave  as div tag for IE8 magic bug. using span crashes angular.. -->\r" +
    "\n" +
    "                <div id=\"forskrivarkodOchArbetsplatskod\">{{cert.forskrivarkodOchArbetsplatskod}}</div>\r" +
    "\n" +
    "              </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "              <div class=\"infobox\">\r" +
    "\n" +
    "                <h3>\r" +
    "\n" +
    "                  <span message key=\"common.label.ovanstaende-har-bekraftats\"></span>\r" +
    "\n" +
    "                </h3>\r" +
    "\n" +
    "                <h4>\r" +
    "\n" +
    "                  <span message key=\"fk7263.label.datum\"></span>\r" +
    "\n" +
    "                </h4>\r" +
    "\n" +
    "                <p id=\"signeringsdatum\">{{cert.grundData.signeringsdatum | date:'longDate'}}</p>\r" +
    "\n" +
    "                <h4>\r" +
    "\n" +
    "                  <span message key=\"fk7263.label.kontakt-info\"></span>\r" +
    "\n" +
    "                </h4>\r" +
    "\n" +
    "                <p>\r" +
    "\n" +
    "                  <span class=\"text\" id=\"vardperson_namn\">{{cert.grundData.skapadAv.fullstandigtNamn}}</span><br>\r" +
    "\n" +
    "                  <span class=\"text\" id=\"vardperson_enhetsnamn\">{{cert.grundData.skapadAv.vardenhet.enhetsnamn}}</span><br>\r" +
    "\n" +
    "                  <span class=\"text\" id=\"vardperson_postadress\">{{cert.grundData.skapadAv.vardenhet.postadress}}</span><br>\r" +
    "\n" +
    "                  <span class=\"text\"><span id=\"vardperson_postnummer\">{{cert.grundData.skapadAv.vardenhet.postnummer}}</span> <span id=\"vardperson_postort\">{{cert.grundData.skapadAv.vardenhet.postort}}</span></span><br>\r" +
    "\n" +
    "                  <span class=\"text\" id=\"vardperson_telefonnummer\">{{cert.grundData.skapadAv.vardenhet.telefonnummer}}</span><br>\r" +
    "\n" +
    "                </p>\r" +
    "\n" +
    "              </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "            </div><!-- cert body -->\r" +
    "\n" +
    "          </div>\r" +
    "\n" +
    "        </div>\r" +
    "\n" +
    "      </div>\r" +
    "\n" +
    "    </div><!-- entire cert -->\r" +
    "\n" +
    "  </div><!--  spinner end -->\r" +
    "\n" +
    "</div>\r" +
    "\n"
  );


  $templateCache.put('/web/webjars/fk7263/webcert/views/fragasvar.html',
    "<div class=\"row weak-hidden\" ng-class=\"{show: widgetState.showTemplate}\" ng-controller=\"fk7263.QACtrl\">\r" +
    "\n" +
    "  <div class=\"col-md-12 qa-column\">\r" +
    "\n" +
    "\r" +
    "\n" +
    "    <div wc-spinner label=\"fk7263.info.loading.existing.qa\" show-spinner=\"!widgetState.doneLoading\"\r" +
    "\n" +
    "         show-content=\"widgetState.doneLoading\">\r" +
    "\n" +
    "\r" +
    "\n" +
    "      <div class=\"webcert-bottom-padding-section\" ng-show=\"certProperties.isLoaded\">\r" +
    "\n" +
    "\r" +
    "\n" +
    "        <!-- Question button -->\r" +
    "\n" +
    "        <div\r" +
    "\n" +
    "            ng-show=\"!certProperties.isRevoked && !widgetState.newQuestionOpen && (certProperties.isSent || qaList.length>0)\"\r" +
    "\n" +
    "            wc-check-vardenhet vardenhet=\"{{cert.grundData.skapadAv.vardenhet.enhetsid}}\">\r" +
    "\n" +
    "          <div class=\"form-group\">\r" +
    "\n" +
    "            <button class=\"btn btn-success\" ng-click=\"toggleQuestionForm()\" id=\"askQuestionBtn\">Ny fråga till\r" +
    "\n" +
    "              Försäkringskassan\r" +
    "\n" +
    "            </button>\r" +
    "\n" +
    "          </div>\r" +
    "\n" +
    "        </div>\r" +
    "\n" +
    "        <div ng-if=\"certProperties.isSent === false && (qaList.length<1)\" wc-check-vardenhet\r" +
    "\n" +
    "             vardenhet=\"{{cert.grundData.skapadAv.vardenhet.enhetsid}}\" class=\"alert alert-info\"\r" +
    "\n" +
    "             id=\"certificate-is-not-sent-to-fk-message-text\">\r" +
    "\n" +
    "          <strong>Intyget är inte skickat till Försäkringskassan.</strong><br> Det går därför inte att ställa frågor om\r" +
    "\n" +
    "          intyget.\r" +
    "\n" +
    "        </div>\r" +
    "\n" +
    "        <div ng-if=\"certProperties.isSent === undefined && (qaList.length<1)\" wc-check-vardenhet\r" +
    "\n" +
    "             vardenhet=\"{{cert.grundData.skapadAv.vardenhet.enhetsid}}\" class=\"alert alert-info\"\r" +
    "\n" +
    "             id=\"certificate-is-not-available\">\r" +
    "\n" +
    "          <strong>Det finns inga frågor och svar.</strong>\r" +
    "\n" +
    "        </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "        <!-- New question form -->\r" +
    "\n" +
    "        <div ng-show=\"widgetState.newQuestionOpen\">\r" +
    "\n" +
    "          <div class=\"form-group\">\r" +
    "\n" +
    "            <label class=\"control-label\">Fråga</label>\r" +
    "\n" +
    "            <textarea wc-maxlength ng-trim=\"false\" maxlength=\"2000\" rows=\"5\" class=\"form-control\"\r" +
    "\n" +
    "                      ng-model=\"newQuestion.frageText\" wc-focus-me=\"widgetState.focusQuestion\"\r" +
    "\n" +
    "                      id=\"newQuestionText\"></textarea>\r" +
    "\n" +
    "          </div>\r" +
    "\n" +
    "          <div class=\"form-group\" id=\"newQuestionForm\">\r" +
    "\n" +
    "            <label class=\"control-label\">Ämne</label>\r" +
    "\n" +
    "            <div class=\"controls\">\r" +
    "\n" +
    "              <select id=\"new-question-topic\" class=\"form-control\" ng-model=\"newQuestion.chosenTopic\"\r" +
    "\n" +
    "                      ng-options=\"c.label for c in newQuestion.topics\"></select>\r" +
    "\n" +
    "            </div>\r" +
    "\n" +
    "          </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "          <!-- error message occuring when inteacting with server for this FragaSvar-->\r" +
    "\n" +
    "          <div id=\"newQuestion-load-error\" ng-show=\"newQuestion.activeErrorMessageKey\" class=\"alert alert-danger\">\r" +
    "\n" +
    "            <span message key=\"fk7263.error.{{newQuestion.activeErrorMessageKey}}\"></span>\r" +
    "\n" +
    "          </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "          <!-- Form button group -->\r" +
    "\n" +
    "          <div class=\"form-group\">\r" +
    "\n" +
    "            <button class=\"btn btn-success\" ng-click=\"sendQuestion(newQuestion)\"\r" +
    "\n" +
    "                    ng-disabled=\"!questionValidForSubmit(newQuestion)\" title=\"{{newQuestion.sendButtonToolTip}}\"\r" +
    "\n" +
    "                    id=\"sendQuestionBtn\">\r" +
    "\n" +
    "              <img src=\"/img/loader-small-green.gif\" ng-show=\"newQuestion.updateInProgress\"> Skicka till\r" +
    "\n" +
    "              Försäkringskassan\r" +
    "\n" +
    "            </button>\r" +
    "\n" +
    "            <button class=\"btn btn-default btn-secondary\" ng-click=\"toggleQuestionForm()\" id=\"cancelQuestionBtn\">Avbryt\r" +
    "\n" +
    "              fråga\r" +
    "\n" +
    "            </button>\r" +
    "\n" +
    "          </div>\r" +
    "\n" +
    "        </div>\r" +
    "\n" +
    "        <!--  Fragasvar formular end -->\r" +
    "\n" +
    "\r" +
    "\n" +
    "        <!-- question sent message -->\r" +
    "\n" +
    "        <div ng-if=\"widgetState.sentMessage\">\r" +
    "\n" +
    "          <div alert type=\"info\" close=\"dismissProxy()\" id=\"question-is-sent-to-fk-message-text\">\r" +
    "\n" +
    "            <strong>Frågan är skickad till Försäkringskassan</strong><br><span wc-feature-not-active\r" +
    "\n" +
    "                                                                               feature=\"franJournalsystem\"> Vårdenheten kontaktas via mejl när svar har inkommit.</span>\r" +
    "\n" +
    "          </div>\r" +
    "\n" +
    "        </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "        <!-- error message -->\r" +
    "\n" +
    "        <div id=\"qa-list-load-error\" ng-show=\"widgetState.doneLoading && widgetState.activeErrorMessageKey\"\r" +
    "\n" +
    "             class=\"alert alert-danger\">\r" +
    "\n" +
    "          <span message key=\"{{widgetState.activeErrorMessageKey}}\"></span>\r" +
    "\n" +
    "        </div>\r" +
    "\n" +
    "      </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "      <div class=\"webcert-col-section qa-list-section\" ng-show=\"(qaList | filter:openIssuesFilter).length > 0\" id=\"unhandledQACol\">\r" +
    "\n" +
    "\r" +
    "\n" +
    "        <h2 class=\"col-head\">Ej hanterade frågor och svar</h2>\r" +
    "\n" +
    "        <div ng-repeat=\"qa in qaList | filter:openIssuesFilter | orderBy:'senasteHandelseDatum':true\" class=\"qa-item\">\r" +
    "\n" +
    "\r" +
    "\n" +
    "          <div qa-panel panel-id=\"unhandled\" qa=\"qa\" qa-list=\"qaList\" cert=\"cert\" cert-properties=\"certProperties\"></div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "        </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "      </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "    </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "    <!-- ------------------------- HANTERADE FRÅGOR --------------------------------------------- -->\r" +
    "\n" +
    "\r" +
    "\n" +
    "    <div class=\"webcert-col-section qa-list-section\" ng-show=\"(qaList | filter:closedIssuesFilter).length > 0\">\r" +
    "\n" +
    "\r" +
    "\n" +
    "      <h2 class=\"col-head\">Hanterade frågor och svar</h2>\r" +
    "\n" +
    "      <div ng-repeat=\"qa in qaList | filter:closedIssuesFilter | orderBy:'senasteHandelseDatum':true\" class=\"qa-item\">\r" +
    "\n" +
    "\r" +
    "\n" +
    "        <div qa-panel panel-id=\"handled\" type=\"handled\" qa=\"qa\" qa-list=\"qaList\" cert=\"cert\" cert-properties=\"certProperties\"></div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "      </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "    </div>\r" +
    "\n" +
    "    <!--  end ng-repeat -->\r" +
    "\n" +
    "\r" +
    "\n" +
    "  </div>\r" +
    "\n" +
    "  <!--  end spinner -->\r" +
    "\n" +
    "\r" +
    "\n" +
    "</div>\r" +
    "\n"
  );

}]);
