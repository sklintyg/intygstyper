angular.module('sjukersattning').run(['$templateCache', function($templateCache) {
  'use strict';

  $templateCache.put('/web/webjars/fk/sjukersattning/webcert/views/intyg/fragasvar/fragasvar.html',
    "<div class=\"row weak-hidden\" ng-class=\"{show: widgetState.showTemplate}\" ng-controller=\"sjukersattning.QACtrl\">\r" +
    "\n" +
    "  <div class=\"col-md-12 qa-column\">\r" +
    "\n" +
    "\r" +
    "\n" +
    "    <div wc-spinner label=\"sjukersattning.info.loading.existing.qa\" show-spinner=\"!widgetState.doneLoading\"\r" +
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
    "            ng-show=\"!certProperties.isRevoked && !widgetState.newQuestionOpen && (viewState.isIntygOnSendQueue || certProperties.isSent || qaList.length>0)\"\r" +
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
    "        <div ng-if=\"viewState.isIntygOnSendQueue === false && certProperties.isSent === false && (qaList.length<1)\" wc-check-vardenhet\r" +
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
    "            <textarea wc-maxlength ng-trim=\"false\" maxlength=\"2000\" rows=\"13\" class=\"form-control\"\r" +
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
    "            <span message key=\"sjukersattning.error.{{newQuestion.activeErrorMessageKey}}\"></span>\r" +
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
    "          <div alert type=\"info\" close=\"dismissSentMessage()\" id=\"question-is-sent-to-fk-message-text\">\r" +
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
    "          <span message key=\"sjukersattning.error.{{widgetState.activeErrorMessageKey}}\"></span>\r" +
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


  $templateCache.put('/web/webjars/fk/sjukersattning/webcert/views/intyg/fragasvar/fragasvarPanel.directive.html',
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
    "              <label for=\"{{panelId}}-mark-as-notified-{{qa.internReferens}}\" class=\"checkbox-inline\">\r" +
    "\n" +
    "                <input id=\"{{panelId}}-mark-as-notified-{{qa.internReferens}}\" type=\"checkbox\"\r" +
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
    "          <span message key=\"sjukersattning.error.{{qa.activeErrorMessageKey}}\"></span>\r" +
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
    "                    wc-check-vardenhet vardenhet=\"{{viewState.intygModel.grundData.skapadAv.vardenhet.enhetsid}}\"\r" +
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
    "            <div class=\"form-group\" ng-show=\"!qa.answerDisabled && !certProperties.isRevoked\">\r" +
    "\n" +
    "              <textarea wc-maxlength ng-trim=\"false\" rows=\"13\" maxlength=\"5000\" class=\"form-control col-md-9 qa-block-wc\" ng-model=\"qa.svarsText\"\r" +
    "\n" +
    "                        id=\"answerText-{{qa.internReferens}}\"></textarea>\r" +
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
    "                <span message key=\"sjukersattning.error.{{qa.activeErrorMessageKey}}\"></span>\r" +
    "\n" +
    "              </div>\r" +
    "\n" +
    "              <button class=\"btn btn-success\" ng-click=\"sendAnswer(qa)\"\r" +
    "\n" +
    "                      ng-disabled=\"!qa.svarsText || qa.updateInProgress\"\r" +
    "\n" +
    "                      ng-show=\"!qa.answerDisabled && !certProperties.isRevoked\" wc-check-vardenhet\r" +
    "\n" +
    "                      vardenhet=\"{{viewState.intygModel.grundData.skapadAv.vardenhet.enhetsid}}\"\r" +
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
    "                      vardenhet=\"{{viewState.intygModel.grundData.skapadAv.vardenhet.enhetsid}}\"\r" +
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
    "            <span message key=\"sjukersattning.error.{{qa.activeErrorMessageKey}}\"></span>\r" +
    "\n" +
    "          </div>\r" +
    "\n" +
    "          <div class=\"controls\" wc-check-vardenhet vardenhet=\"{{viewState.intygModel.grundData.skapadAv.vardenhet.enhetsid}}\">\r" +
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


  $templateCache.put('/web/webjars/fk/sjukersattning/webcert/views/intyg/intyg.html',
    "<div class=\"weak-hidden\" ng-class=\"{show: viewState.common.showTemplate}\" ng-controller=\"sjukersattning.ViewCertCtrl\">\r" +
    "\n" +
    "  <div wc-spinner label=\"sjukersattning.info.loadingcertificate\" show-spinner=\"!viewState.common.doneLoading\"\r" +
    "\n" +
    "       show-content=\"viewState.common.doneLoading\">\r" +
    "\n" +
    "\r" +
    "\n" +
    "    <div ui-view=\"header\"></div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "    <div ng-show=\"!viewState.common.activeErrorMessageKey\">\r" +
    "\n" +
    "      <div id=\"certificate\" ng-class=\"{'certificate': true, 'revoked-certificate' : $scope.viewState.common.intyg.isRevoked}\">\r" +
    "\n" +
    "        <div class=\"row\">\r" +
    "\n" +
    "          <div class=\"col-md-12\">\r" +
    "\n" +
    "            <div class=\"cert-body\">\r" +
    "\n" +
    "\r" +
    "\n" +
    "              <!--div wc-field field-number=\"1\" field-label=\"sjukersattning.label.smittskydd\" filled=\"{{true}}\">\r" +
    "\n" +
    "                <span ng-show=\"viewState.intygModel.avstangningSmittskydd\" id=\"smittskydd\"> <span message key=\"sjukersattning.label.yes\"></span></span>\r" +
    "\n" +
    "                <span ng-show=\"!viewState.intygModel.avstangningSmittskydd\"> <span message key=\"sjukersattning.label.no\"></span></span>\r" +
    "\n" +
    "              </div-->\r" +
    "\n" +
    "\r" +
    "\n" +
    "              <div wc-field field-label=\"sjukersattning.label.intygbaseratpa\" id=\"field2\"\r" +
    "\n" +
    "                   filled=\"{{viewState.intygModel.undersokningAvPatienten!=null || viewState.intygModel.journaluppgifter!=null || viewState.intygModel.telefonkontaktMedPatienten !=null || viewState.intygModel.kannedomOmPatient !=null }}\">\r" +
    "\n" +
    "                <ul class=\"padded-list\" id=\"baseratPaList\">\r" +
    "\n" +
    "                  <li ng-show=\"viewState.intygModel.undersokningAvPatienten!=null\">\r" +
    "\n" +
    "                    <span message key=\"sjukersattning.vardkontakt.undersokning\"></span><span id=\"undersokningAvPatienten\">{{viewState.intygModel.undersokningAvPatienten | date: 'longDate' }}</span>\r" +
    "\n" +
    "                  </li>\r" +
    "\n" +
    "                  <li ng-show=\"viewState.intygModel.journaluppgifter!=null\">\r" +
    "\n" +
    "                    <span message key=\"sjukersattning.referens.journal\"></span><span id=\"journaluppgifter\">{{viewState.intygModel.journaluppgifter | date: 'longDate' }}</span>\r" +
    "\n" +
    "                  </li>\r" +
    "\n" +
    "                  <li ng-show=\"viewState.intygModel.telefonkontaktMedPatienten!=null\">\r" +
    "\n" +
    "                    <span message key=\"sjukersattning.referens.telefonkontaktmedpatienten\"></span><span id=\"telefonKontaktBeskrivning\">{{viewState.intygModel.telefonkontaktMedPatienten | date: 'longDate' }}</span>\r" +
    "\n" +
    "                  </li>\r" +
    "\n" +
    "                  <li ng-show=\"viewState.intygModel.kannedomOmPatient!=null\">\r" +
    "\n" +
    "                    <span message key=\"sjukersattning.referens.kannedomompatient\"></span><span id=\"kannedomOmPatient\">{{viewState.intygModel.kannedomOmPatient | date: 'longDate' }}</span>\r" +
    "\n" +
    "                  </li>\r" +
    "\n" +
    "                  <li ng-repeat=\"underlag in viewState.intygModel.underlag\" ng-if=\"viewState.intygModel.underlag != null\">\r" +
    "\n" +
    "                      <span message key=\"sjukersattning.referens.underlag{{ $index }}\"></span>\r" +
    "\n" +
    "                      <span id=\"underlagText\"> {{ underlag.datum | date: 'longDate' }}<br/>\r" +
    "\n" +
    "                        <i>{{ underlag.bilaga ? 'ytterligare information bifogas ' : ''}}</i>\r" +
    "\n" +
    "                      </span>\r" +
    "\n" +
    "                  </li>\r" +
    "\n" +
    "                </ul>\r" +
    "\n" +
    "              </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "              <div wc-field field-label=\"sjukersattning.label.diagnos\" filled=\"{{viewState.intygModel.diagnoser!=null}}\" id=\"field4\">\r" +
    "\n" +
    "                <div class=\"form-group\">\r" +
    "\n" +
    "                  <span> Baserat på diagnoskodsystem: {{ viewState.intygModel.diagnoser[0].diagnosKodSystem === 'ICD-10-SE' ? 'ICD-10-SE' : 'KSH-97-P' }}<br/></span>\r" +
    "\n" +
    "                  <span id=\"diagnosKod\">Diagnoskod: {{viewState.intygModel.diagnoser[0].diagnosKod}} <br/></span>\r" +
    "\n" +
    "                  <span id=\"diagnosBeskrivning1\">Diagnosbeskrivning: {{ viewState.intygModel.diagnoser[0].diagnosBeskrivning }}</span>\r" +
    "\n" +
    "                </div>\r" +
    "\n" +
    "                <div class=\"form-group\">\r" +
    "\n" +
    "                  <label class=\"control-label bold\"\r" +
    "\n" +
    "                         ng-show=\"viewState.intygModel.diagnoser[1].diagnosKod || viewState.intygModel.diagnoser[1].diagnosBeskrivning ||\r" +
    "\n" +
    "                         viewState.intygModel.diagnoser[2].intygModel.diagnosKod || viewState.intygModel.diagnoser[2].diagnosBeskrivning\">\r" +
    "\n" +
    "                    <span message key=\"sjukersattning.label.ytterligarediagnoser\"></span>\r" +
    "\n" +
    "                  </label>\r" +
    "\n" +
    "                  <div class=\"controls\">\r" +
    "\n" +
    "                    <p ng-show=\"viewState.intygModel.diagnoser[1].diagnosKod || viewState.intygModel.diagnoser[1].diagnosBeskrivning\">\r" +
    "\n" +
    "                      <span id=\"diagnosKod2\">{{ viewState.intygModel.diagnoser[1].diagnosKod }} :</span>\r" +
    "\n" +
    "                      <span id=\"diagnosBeskrivning2\">{{viewState.intygModel.diagnoser[1].diagnosBeskrivning}}</span>\r" +
    "\n" +
    "                    </p>\r" +
    "\n" +
    "                    <p ng-show=\"viewState.intygModel.diagnoser[2].diagnosKod || viewState.intygModel.diagnoser[2].diagnosBeskrivning\">\r" +
    "\n" +
    "                      <span id=\"diagnosKod3\">{{viewState.intygModel.diagnoser[2].diagnosKod}} :</span>\r" +
    "\n" +
    "                      <span id=\"diagnosBeskrivning3\">{{viewState.intygModel.diagnoser[2].diagnosBeskrivning}}</span>\r" +
    "\n" +
    "                    </p>\r" +
    "\n" +
    "                  </div>\r" +
    "\n" +
    "                </div>\r" +
    "\n" +
    "                <!--div class=\"form-group\" ng-show=\"viewState.intygModel.diagnosBeskrivning\">\r" +
    "\n" +
    "                  <label class=\"control-label bold\">\r" +
    "\n" +
    "                    <span message key=\"sjukersattning.label.diagnosfortydligande\"></span>\r" +
    "\n" +
    "                  </label>\r" +
    "\n" +
    "                  <div class=\"controls\">\r" +
    "\n" +
    "                    <div id=\"diagnosBeskrivning\" class=\"multiline\">{{viewState.intygModel.diagnosBeskrivning}}</div>\r" +
    "\n" +
    "                  </div-->\r" +
    "\n" +
    "                </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "\r" +
    "\n" +
    "            <div wc-field field-label=\"sjukersattning.label.diagnostisering\" filled=\"{{true}}\" id=\"4b\">\r" +
    "\n" +
    "              <div class=\"form-group\">\r" +
    "\n" +
    "                  <p ng-show=\"viewState.intygModel.diagnostisering!=null\">\r" +
    "\n" +
    "                     <div id=\"diagnosticering\" class=\"multiline\">{{ viewState.intygModel.diagnostisering }}</div>\r" +
    "\n" +
    "                  </p>\r" +
    "\n" +
    "                  <p ng-show=\"viewState.intygModel.nyBedomningDiagnos\">\r" +
    "\n" +
    "                    <span message key=\"sjukersattning.label.diagnostisering.nybedomning\"></span>\r" +
    "\n" +
    "                    <span id=\"nyBedomningDiagnos\">{{ viewState.intygModel.nyBedomningDiagnos = true ? 'JA' : 'NEJ' }}</span>\r" +
    "\n" +
    "                  </p>\r" +
    "\n" +
    "              </div>\r" +
    "\n" +
    "            </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "              <div wc-field field-label=\"sjukersattning.label.funktionsnedsattningar\" id=\"5\"\r" +
    "\n" +
    "                   filled=\"{{ viewState.intygModel.funktionsnedsattningar!=null }}\">\r" +
    "\n" +
    "                <ul class=\"padded-list\">\r" +
    "\n" +
    "                     <li id=\"funktionsnedsattning\" ng-repeat=\"nedsattning in viewState.intygModel.funktionsnedsattningar\">\r" +
    "\n" +
    "                        <span message key=\"sjukersattning.label.intyg.nedsattningar{{ nedsattning.funktionsomrade }}\"></span>\r" +
    "\n" +
    "                        <div id=\"nedsattning-{{ nedsattning.funktionsomrade }}\" class=\"multiline\">{{ nedsattning.beskrivning }}</div>\r" +
    "\n" +
    "                     </li>\r" +
    "\n" +
    "                </ul>\r" +
    "\n" +
    "              </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "              <div wc-field field-label=\"sjukersattning.label.aktivitetsbegransning\"\r" +
    "\n" +
    "                   filled=\"{{viewState.intygModel.aktivitetsbegransning!=null}}\">\r" +
    "\n" +
    "                   <div id=\"aktivitetsbegransning\" class=\"multiline\">{{viewState.intygModel.aktivitetsbegransning}}</div>\r" +
    "\n" +
    "              </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "              <div wc-field field-label=\"sjukersattning.label.planerad-atgard\" id=\"field7\"\r" +
    "\n" +
    "                   filled=\"{{viewState.intygModel.pagaendeBehandling!=null|| viewState.intygModel.planeradBehandling!=null|| viewState.intygModel.avslutadBehandling!=null }}\">\r" +
    "\n" +
    "                <ul class=\"padded-list\">\r" +
    "\n" +
    "                  <li ng-show=\"viewState.intygModel.avslutadBehandling != null\">\r" +
    "\n" +
    "                    <span message key=\"sjukersattning.label.planerad-atgard.avslutadBehandling\"></span>\r" +
    "\n" +
    "                    <div id=\"avslutadBehandling\" class=\"multiline\">{{viewState.intygModel.avslutadBehandling}}</div>\r" +
    "\n" +
    "                  </li>\r" +
    "\n" +
    "                  <li ng-show=\"viewState.intygModel.pagaendeBehandling != null\">\r" +
    "\n" +
    "                    <span message key=\"sjukersattning.label.planerad-atgard.pagaendeBehandling\"></span>\r" +
    "\n" +
    "                    <div id=\"pagaendeBehandling\" class=\"multiline\">{{viewState.intygModel.pagaendeBehandling}}</div>\r" +
    "\n" +
    "                  </li>\r" +
    "\n" +
    "                  <li ng-show=\"viewState.intygModel.planeradBehandling != null\">\r" +
    "\n" +
    "                    <span message key=\"sjukersattning.label.planerad-atgard.planeradBehandling\"></span>\r" +
    "\n" +
    "                    <div id=\"planeradBehandling\" class=\"multiline\">{{viewState.intygModel.planeradBehandling}}</div>\r" +
    "\n" +
    "                  </li>\r" +
    "\n" +
    "                </ul>\r" +
    "\n" +
    "              </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "              <div wc-field field-label=\"sjukersattning.label.medicinskaforutsattningar\" id=\"field9\"\r" +
    "\n" +
    "                   filled=\"{{ viewState.intygModel.prognos!=null || viewState.intygModel.aktivitetsFormaga!=null }}\">\r" +
    "\n" +
    "                 <ul class=\"padded-list\">\r" +
    "\n" +
    "                   <li ng-show=\"viewState.intygModel.prognos != null\">\r" +
    "\n" +
    "                     <span message key=\"sjukersattning.label.medicinskaforutsattningar.bedomning\"></span>\r" +
    "\n" +
    "                     <div id=\"prognos\" class=\"multiline\">{{viewState.intygModel.prognos}}</div>\r" +
    "\n" +
    "                   </li>\r" +
    "\n" +
    "                   <li ng-show=\"viewState.intygModel.aktivitetsFormaga != null\">\r" +
    "\n" +
    "                     <span message key=\"sjukersattning.label.medicinskaforutsattningar.mojligheter\"></span>\r" +
    "\n" +
    "                     <div id=\"aktivitetsFormaga\" class=\"multiline\">{{viewState.intygModel.aktivitetsFormaga }}</div>\r" +
    "\n" +
    "                   </li>\r" +
    "\n" +
    "                 </ul>\r" +
    "\n" +
    "              </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "              <div wc-field field-label=\"sjukersattning.label.ovrigt\" id=\"field10\" filled=\"{{ viewState.intygModel.ovrigt!=null }}\"\r" +
    "\n" +
    "                   id=\"field10\">\r" +
    "\n" +
    "                 <div id=\"kommentar\" class=\"multiline\">{{viewState.intygModel.ovrigt}}</div>\r" +
    "\n" +
    "              </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "              <div wc-field field-label=\"sjukersattning.label.kontakt-med-fk\"\r" +
    "\n" +
    "                   filled=\"{{true}}\" id=\"field11\">\r" +
    "\n" +
    "                <span id=\"kontaktMedFk\" ng-show=\"viewState.intygModel.kontaktMedFk\">\r" +
    "\n" +
    "                  <span message key=\"sjukersattning.label.yes\"></span>\r" +
    "\n" +
    "                </span>\r" +
    "\n" +
    "                <span ng-show=\"!viewState.intygModel.kontaktMedFk\">\r" +
    "\n" +
    "                  <span message key=\"sjukersattning.label.no\"></span>\r" +
    "\n" +
    "                </span>\r" +
    "\n" +
    "                <div id=\"anledningTillKontakt\" class=\"multiline\" ng-show=\"!viewState.intygModel.anledningTillKontakt != null\">\r" +
    "\n" +
    "                  <span message key=\"sjukersattning.label.anledningTillKontakt\"></span>\r" +
    "\n" +
    "                  {{ viewState.intygModel.anledningTillKontakt }}\r" +
    "\n" +
    "                </div>\r" +
    "\n" +
    "\r" +
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
    "                  <span message key=\"sjukersattning.label.datum\"></span>\r" +
    "\n" +
    "                </h4>\r" +
    "\n" +
    "                <p id=\"signeringsdatum\">{{viewState.intygModel.grundData.signeringsdatum | date: 'longDate'}}</p>\r" +
    "\n" +
    "                <h4>\r" +
    "\n" +
    "                  <span message key=\"sjukersattning.label.kontakt-info\"></span>\r" +
    "\n" +
    "                </h4>\r" +
    "\n" +
    "                <p>\r" +
    "\n" +
    "                  <span class=\"text\" id=\"vardperson_namn\">{{viewState.intygModel.grundData.skapadAv.fullstandigtNamn}}</span><br>\r" +
    "\n" +
    "                  <span class=\"text\" id=\"vardperson_enhetsnamn\">{{viewState.intygModel.grundData.skapadAv.vardenhet.enhetsnamn}}</span><br>\r" +
    "\n" +
    "                  <span class=\"text\" id=\"vardperson_postadress\">{{viewState.intygModel.grundData.skapadAv.vardenhet.postadress}}</span><br>\r" +
    "\n" +
    "                  <span class=\"text\"><span id=\"vardperson_postnummer\">{{viewState.intygModel.grundData.skapadAv.vardenhet.postnummer}}</span> <span id=\"vardperson_postort\">{{viewState.intygModel.grundData.skapadAv.vardenhet.postort}}</span></span><br>\r" +
    "\n" +
    "                  <span class=\"text\" id=\"vardperson_telefonnummer\">{{viewState.intygModel.grundData.skapadAv.vardenhet.telefonnummer}}</span><br>\r" +
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


  $templateCache.put('/web/webjars/fk/sjukersattning/webcert/views/utkast/form/form10.html',
    "<div wc-field field-label=\"sjukersattning.label.ovrigt\"\r" +
    "\n" +
    "     field-help-text=\"sjukersattning.helptext.ovrigt\" class=\"print-pagebreak-after\">\r" +
    "\n" +
    "                    <textarea id=\"otherInformation\" name=\"otherInformation\" rows='6'\r" +
    "\n" +
    "                              class='form-control otherInformation' ng-model=\"model.ovrigt\"/>\r" +
    "\n" +
    "</div>\r" +
    "\n"
  );


  $templateCache.put('/web/webjars/fk/sjukersattning/webcert/views/utkast/form/form11.html',
    "<div wc-field field-label=\"sjukersattning.label.fk-kontakt\"\r" +
    "\n" +
    "     field-help-text=\"sjukersattning.helptext.kontakt\" class=\"print-pagebreak-after\">\r" +
    "\n" +
    "\r" +
    "\n" +
    "    <div class=\"form-group form-inline\">\r" +
    "\n" +
    "        <label class=\"radio-inline\">\r" +
    "\n" +
    "            <input id=\"kontaktFkJa\" name=\"kontkatFk\" type=\"radio\" ng-value=\"true\"\r" +
    "\n" +
    "                   ng-model=\"model.kontaktMedFk\">Ja\r" +
    "\n" +
    "        </label>\r" +
    "\n" +
    "        <label class=\"radio-inline\">\r" +
    "\n" +
    "            <input id=\"kontaktFkNej\" name=\"andraMedicinskaUtredningar\" type=\"radio\" ng-value=\"false\" checked=\"checked\"\r" +
    "\n" +
    "                   ng-model=\"model.kontaktMedFk\">Nej\r" +
    "\n" +
    "        </label>\r" +
    "\n" +
    "    </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "    <div class=\"form-group\" ng-show=\"model.kontaktMedFk === true\">\r" +
    "\n" +
    "        <label class=\"control-label bold\"><span message key=\"sjukersattning.label.kontaktMedFk.anledningTillKontakt\"/></label>\r" +
    "\n" +
    "\r" +
    "\n" +
    "            <textarea id=\"anledningTillKontakt\" name=\"otherInformation\" rows=\"6\"\r" +
    "\n" +
    "                      class=\"form-control\" ng-model=\"model.anledningTillKontakt\" style=\"min-width: 600px\"/>\r" +
    "\n" +
    "    </div>\r" +
    "\n" +
    "</div>\r" +
    "\n"
  );


  $templateCache.put('/web/webjars/fk/sjukersattning/webcert/views/utkast/form/form15.html',
    "<a id=\"anchor.vardenhet\" name=\"anchor.vardenhet\"></a>\r" +
    "\n" +
    "<div wc-field field-label=\"sjukersattning.label.vardenhet\"\r" +
    "\n" +
    "     field-help-text=\"sjukersattning.helptext.adress\" id=\"vardenhetForm\" field-has-errors=\"viewState.common.validationMessagesGrouped.vardenhet\">\r" +
    "\n" +
    "  <div class=\"alert alert-danger\" ng-if=\"viewState.common.validationMessagesGrouped.vardenhet\" id=\"validationMessages_vardenhet\">\r" +
    "\n" +
    "\r" +
    "\n" +
    "    <ul>\r" +
    "\n" +
    "      <li ng-repeat=\"message in viewState.common.validationMessagesGrouped.vardenhet\"><span message\r" +
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
    "  <div class=\"alert alert-info\" ng-show=\"viewState.common.hsaInfoMissing\">\r" +
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
    "      <label class=\"col-xs-3 control-label bold\">Postadress</label>\r" +
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
    "      <label class=\"col-xs-3 control-label bold\">Postnummer</label>\r" +
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
    "      <label class=\"col-xs-3 control-label bold\"></label>\r" +
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
    "      <label class=\"col-xs-3 control-label bold\">Telefonnummer</label>\r" +
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


  $templateCache.put('/web/webjars/fk/sjukersattning/webcert/views/utkast/form/form2.html',
    "\r" +
    "\n" +
    "<div ng-form=\"form2\">\r" +
    "\n" +
    "  <a id=\"anchor.intygbaseratpa\" name=\"anchor.intygbaseratpa\"></a>\r" +
    "\n" +
    "    <h3 class=\"title\">Läkarutlåtande för sjukersättning</h3>\r" +
    "\n" +
    "  <div id=\"intygetbaseraspa\" wc-field field-dynamic-label=\"KAT_2.RBK\"\r" +
    "\n" +
    "                                      field-dynamic-help-text=\"KAT_2.HLP\"\r" +
    "\n" +
    "       field-has-errors=\"viewState.common.validationMessagesGrouped.intygbaseratpa\" class=\"field-fade\">\r" +
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
    "  <!-- Test dynamic label\r" +
    "\n" +
    "      <div class=\"form-group form-inline\">\r" +
    "\n" +
    "          <!-- test rubrik goes here -->\r" +
    "\n" +
    "\r" +
    "\n" +
    "          <!--p><b>with  correct id (RBK + HLP):</b></p>\r" +
    "\n" +
    "          <p><span dynamic-label key=\"KAT_2.RBK\"></span></p>\r" +
    "\n" +
    "          <p><span dynamic-label key=\"KAT_2.HLP\"></span></p>\r" +
    "\n" +
    "\r" +
    "\n" +
    "          <p><b>with  incorrect id (RBK + HLP):</b></p>\r" +
    "\n" +
    "          <p><span dynamic-label key=\"KAT_2.RBKX\"></span></p>\r" +
    "\n" +
    "          <p><span dynamic-label key=\"KAT_2HaLP\"></span></p>\r" +
    "\n" +
    "\r" +
    "\n" +
    "          <p><b>with id no text (RBK + HLP):</b></p>\r" +
    "\n" +
    "          <p><span dynamic-label key=\"DFR_3.1.RBK\"></span></p>\r" +
    "\n" +
    "\r" +
    "\n" +
    "      </div-->\r" +
    "\n" +
    "\r" +
    "\n" +
    "\r" +
    "\n" +
    "  <!-- Undersökning -->\r" +
    "\n" +
    "  <div class=\"form-group form-inline\">\r" +
    "\n" +
    "    <label class=\"checkbox inline-block\" style=\"min-width: 300px\"><input  type=\"checkbox\"\r" +
    "\n" +
    "                                                             id=\"basedOnExamination\"\r" +
    "\n" +
    "                                                             name=\"informationBasedOn.examination\"\r" +
    "\n" +
    "                                                             class=\"checkbox-inline\"\r" +
    "\n" +
    "                                                             ng-model=\"basedOnState.check.undersokningAvPatienten\"\r" +
    "\n" +
    "                                                             ng-change=\"toggleBaseradPaDate('undersokningAvPatienten')\"\r" +
    "\n" +
    "                                                             value=\"true\">\r" +
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
    "  <!-- Journaluppgifter -->\r" +
    "\n" +
    "  <div class=\"form-group form-inline\"><label class=\"checkbox inline-block\" style=\"min-width: 300px\"><input type=\"checkbox\"\r" +
    "\n" +
    "                                                                                                id=\"basedOnJournal\"\r" +
    "\n" +
    "                                                                                                name=\"informationBasedOn.journal\"\r" +
    "\n" +
    "                                                                                                ng-model=\"basedOnState.check.journaluppgifter\"\r" +
    "\n" +
    "                                                                                                ng-change=\"toggleBaseradPaDate('journaluppgifter')\">\r" +
    "\n" +
    "              Journaluppgifter från den</label>\r" +
    "\n" +
    "    <span wc-date-picker-field target-model=\"dates.journaluppgifter\"\r" +
    "\n" +
    "          dom-id=\"journaluppgifterDate\"></span>\r" +
    "\n" +
    "  </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "  <div class=\"form-group form-inline\">\r" +
    "\n" +
    "      <label class=\"checkbox inline-block\" style=\"min-width: 300px\"><input type=\"checkbox\"\r" +
    "\n" +
    "                                                                           id=\"basedOnTelefon\"\r" +
    "\n" +
    "                                                                           name=\"informationBasedOn.telefon\"\r" +
    "\n" +
    "                                                                           ng-model=\"basedOnState.check.telefonkontaktMedPatienten\"\r" +
    "\n" +
    "                                                                           ng-change=\"toggleBaseradPaDate('telefonkontaktMedPatienten')\">\r" +
    "\n" +
    "          Telefonkontakt med patienten</label>\r" +
    "\n" +
    "    <span wc-date-picker-field target-model=\"dates.telefonkontaktMedPatienten\"\r" +
    "\n" +
    "          dom-id=\"telefonkontaktMedPatientenDate\"></span>\r" +
    "\n" +
    "  </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "      <!--  Jag har känt patienten sedan den -->\r" +
    "\n" +
    "\r" +
    "\n" +
    "      <div class=\"alert alert-danger\" ng-if=\"viewState.common.validationMessagesGrouped.kannedom\"\r" +
    "\n" +
    "           id=\"validationMessages_kannedom\">\r" +
    "\n" +
    "          <ul>\r" +
    "\n" +
    "              <li ng-repeat=\"message in viewState.common.validationMessagesGrouped.kannedom\">\r" +
    "\n" +
    "                  <span message key=\"{{message.message}}\"></span>\r" +
    "\n" +
    "              </li>\r" +
    "\n" +
    "          </ul>\r" +
    "\n" +
    "      </div>\r" +
    "\n" +
    "  <div class=\"form-group form-inline\">\r" +
    "\n" +
    "      <label class=\"checkbox inline-block\" style=\"min-width: 300px; \"><input type=\"checkbox\"\r" +
    "\n" +
    "                                                                           id=\"basedOnKannedom\"\r" +
    "\n" +
    "                                                                           name=\"informationBasedOn.kannedom\"\r" +
    "\n" +
    "                                                                           ng-model=\"basedOnState.check.kannedomOmPatient\"\r" +
    "\n" +
    "                                                                           ng-change=\"toggleBaseradPaDate('kannedomOmPatient')\" />\r" +
    "\n" +
    "          <span>Jag har känt patienten sedan den</span></label>\r" +
    "\n" +
    "      <span wc-date-picker-field target-model=\"dates.kannedomOmPatient\" dom-id=\"kannedomOmPatientDate\"></span>\r" +
    "\n" +
    "\r" +
    "\n" +
    "\r" +
    "\n" +
    "  </div>\r" +
    "\n" +
    "      <p>\r" +
    "\n" +
    "\r" +
    "\n" +
    "      <!--  Finns det andra medicinska... -->\r" +
    "\n" +
    "      <div class=\"form-group form-inline\">\r" +
    "\n" +
    "          <label class=\"inline-block bold\">Finns det andra medicinska utredningar eller underlag som är relevanta för bedömningen?</label>\r" +
    "\n" +
    "      </div>\r" +
    "\n" +
    "      <div class=\"form-group form-inline\">\r" +
    "\n" +
    "          <label class=\"radio-inline\">\r" +
    "\n" +
    "                <input id=\"andraMedicinskaUtredningarYes\" name=\"andraMedicinskaUtredningar\" type=\"radio\" ng-value=\"true\"\r" +
    "\n" +
    "                        ng-model=\"viewModel.radioMedicalChecked\" />Ja\r" +
    "\n" +
    "          </label>\r" +
    "\n" +
    "          <label class=\"radio-inline\">\r" +
    "\n" +
    "              <input id=\"andraMedicinskaUtredningarNo\" name=\"andraMedicinskaUtredningar\" type=\"radio\" ng-value=\"false\"\r" +
    "\n" +
    "                     ng-model=\"viewModel.radioMedicalChecked\" checked=\"checked\" />Nej\r" +
    "\n" +
    "          </label>\r" +
    "\n" +
    "      </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "     <div class=\"form-group form-inline bottom-padding-small\" ng-show=\"viewModel.radioMedicalChecked\"  style=\"padding-bottom: 40px;\">\r" +
    "\n" +
    "         <div class=\"underlagItem\" style=\"padding-bottom: 10px;\" ng-repeat=\"underlag in viewModel.initialUnderlag\">\r" +
    "\n" +
    "              <span class=\"glyphicon glyphicon-remove-sign deleteUnderlag\" ng-click=\"removeUnderlag(underlag.typ, $index)\"></span>\r" +
    "\n" +
    "              <select class=\"form-control\" name=\"andraUnderlag\"\r" +
    "\n" +
    "                    data-ng-model=\"underlag.typ\" ng-change=\"onUnderlagChange(underlag, $index)\"\r" +
    "\n" +
    "                    data-ng-options=\"item.id as item.label for item in $parent.underlagSelect\"></select>\r" +
    "\n" +
    "\r" +
    "\n" +
    "              <span wc-date-picker-field target-model=\"underlag.datum\" dom-id=\"{{ underlag.typ }}-Date\"\r" +
    "\n" +
    "                    ng-change=\"onUnderlagChange(underlag, $index)\" ng-model=\"underlag.datum\"></span>\r" +
    "\n" +
    "\r" +
    "\n" +
    "              <div class=\"willSupplyWrapper pull-right\">\r" +
    "\n" +
    "              <label class=\"control-label inline-block bold\" style=\"padding-right: 10px;\">Skickas separat</label>\r" +
    "\n" +
    "              <label class=\"radio-inline\">\r" +
    "\n" +
    "                  <input id=\"willSupplyAttachmentYES-{{ $index }}\" name=\"willSupplyAttachmentRadioYes{{ $index }}\"\r" +
    "\n" +
    "                         type=\"radio\" ng-value=\"true\" ng-change=\"onUnderlagChange(underlag, $index)\" ng-model=\"underlag.bilaga\" />Ja\r" +
    "\n" +
    "              </label>\r" +
    "\n" +
    "              <label class=\"radio-inline\">\r" +
    "\n" +
    "                  <input id=\"willSupplyAttachmentNO-{{ $index }}\" name=\"willSupplyAttachmentRadioNo{{ $index }}\"\r" +
    "\n" +
    "                         type=\"radio\" ng-value=\"false\" ng-change=\"onUnderlagChange(underlag, $index)\" ng-model=\"underlag.bilaga\" />Nej\r" +
    "\n" +
    "              </label>\r" +
    "\n" +
    "          </div>\r" +
    "\n" +
    "             <br/>\r" +
    "\n" +
    "         </div>\r" +
    "\n" +
    "         <div class=\"col-xs-12\" style=\"padding: 5px 0px;background-color: #e5e5e5; border-bottom-left-radius: 4px; border-bottom-right-radius: 4px;\"\r" +
    "\n" +
    "              ng-show=\"model.underlag.length < 9\">\r" +
    "\n" +
    "             <a href ng-click=\"createUnderlag()\" class=\"btn btn-link\"\r" +
    "\n" +
    "                ng-class=\"{ 'disabled': viewModel.initialUnderlag[viewModel.initialUnderlag.length - 1].typ === 0 ||\r" +
    "\n" +
    "                                        viewModel.initialUnderlag[viewModel.initialUnderlag.length - 1].datum === null }\">\r" +
    "\n" +
    "                 Lägg till ytterligare underlag</a>\r" +
    "\n" +
    "         </div>\r" +
    "\n" +
    "         <span ng-if=\"model.underlag.length === 9\" message key=\"sjukersattning.validation.underlag.max-extra-underlag\"></span><!-- kursivera -->\r" +
    "\n" +
    "     </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "\r" +
    "\n" +
    "\r" +
    "\n" +
    "  </div>\r" +
    "\n" +
    "</div>"
  );


  $templateCache.put('/web/webjars/fk/sjukersattning/webcert/views/utkast/form/form4.diagnoseTemplate.html',
    "<span style=\"font-style:italic\" ng-if=\"match.model.moreResults\" message key=\"sjukersattning.label.diagnoses.more_results\"></span>\r" +
    "\n" +
    "<a tabindex=\"-1\" ng-bind-html=\"match.label | uibTypeaheadHighlight:query\" ></a>"
  );


  $templateCache.put('/web/webjars/fk/sjukersattning/webcert/views/utkast/form/form4.html',
    "<a id=\"anchor.diagnos\" name=\"anchor.diagnos\"></a>\r" +
    "\n" +
    "<div ng-form=\"form4\">\r" +
    "\n" +
    "      <div wc-field field-label=\"sjukersattning.label.diagnos\"\r" +
    "\n" +
    "         field-help-text=\"sjukersattning.helptext.diagnos\"\r" +
    "\n" +
    "         class=\"print-pagebreak-after field-fade\" field-has-errors=\"viewState.common.validationMessagesGrouped.diagnos\" id=\"diagnoseForm\">\r" +
    "\n" +
    "\r" +
    "\n" +
    "          <div class=\"alert alert-danger\" ng-if=\"viewState.common.validationMessagesGrouped.diagnos\" id=\"validationMessages_diagnos\">\r" +
    "\n" +
    "            <ul>\r" +
    "\n" +
    "              <li ng-repeat=\"message in viewState.common.validationMessagesGrouped.diagnos\"><span message\r" +
    "\n" +
    "                                                                                 key=\"{{message.message}}\"></span>\r" +
    "\n" +
    "              </li>\r" +
    "\n" +
    "            </ul>\r" +
    "\n" +
    "          </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "          <div class=\"row bottom-padding-small\">\r" +
    "\n" +
    "              <div class='col-xs-12'>\r" +
    "\n" +
    "                  <div class='divider'>\r" +
    "\n" +
    "                      <span message key=\"sjukersattning.label.valjkodverk\"></span>\r" +
    "\n" +
    "                      <label class=\"radio-inline\">\r" +
    "\n" +
    "                          <input type=\"radio\" id=\"diagnoseKodverk_ICD_10_SE\" name=\"diagnosKodSystem\" value=\"ICD_10_SE\"\r" +
    "\n" +
    "                                 ng-model=\"viewModel.diagnosKodSystem\" checked=\"checked\"\r" +
    "\n" +
    "                                 ng-change=\"onChangeKodverk()\"><span message\r" +
    "\n" +
    "                                                                     key=\"sjukersattning.label.diagnoskodverk.icd_10_se\"></span>\r" +
    "\n" +
    "                      </label>\r" +
    "\n" +
    "                      <label class=\"radio-inline\">\r" +
    "\n" +
    "                          <input type=\"radio\" id=\"diagnoseKodverk_KSH_97_P\" name=\"diagnosKodSystem\" value=\"KSH_97_P\"\r" +
    "\n" +
    "                                 ng-model=\"viewModel.diagnosKodSystem\" ng-change=\"onChangeKodverk()\"><span message\r" +
    "\n" +
    "                                                                                                         key=\"sjukersattning.label.diagnoskodverk.ksh_97_p\"></span>\r" +
    "\n" +
    "                      </label>\r" +
    "\n" +
    "                  </div>\r" +
    "\n" +
    "              </div>\r" +
    "\n" +
    "          </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "        <!-- ROW 1 -->\r" +
    "\n" +
    "      <div class=\"row bottom-padding-small\">\r" +
    "\n" +
    "        <div class='col-xs-3'>\r" +
    "\n" +
    "\r" +
    "\n" +
    "          <input type=\"hidden\" name=\"diagnosKodSystem\" value=\"ICD_10_SE\" ng-model=\"model.diagnoser[0].diagnosKod\" />\r" +
    "\n" +
    "          <label class=\"control-label unbreakable\">ICD-10-SE</label>\r" +
    "\n" +
    "          <input type=\"text\" class=\"form-control\" id=\"diagnoseCode\" name=\"diagnose.code\" value=\"\"\r" +
    "\n" +
    "                 class='diagnose-code col-xs-11 form-control' ng-model=\"model.diagnoser[0].diagnosKod\"\r" +
    "\n" +
    "                 uib-typeahead=\"d.value as d.label for d in getDiagnoseCodes(viewModel.diagnosKodSystem, $viewValue) | limitTo:10\"\r" +
    "\n" +
    "                 typeahead-min-length=\"3\" typeahead-on-select=\"onDiagnoseCode1Select($item)\"\r" +
    "\n" +
    "                 typeahead-template-url=\"/web/webjars/sjukersattning/webcert/views/utkast/form/form4.diagnoseTemplate.html\"\r" +
    "\n" +
    "                 wc-to-uppercase>\r" +
    "\n" +
    "        </div>\r" +
    "\n" +
    "        <div class='col-xs-9'>\r" +
    "\n" +
    "          <label class=\"control-label\">Diagnos</label>\r" +
    "\n" +
    "          <input type=\"text\" class=\"form-control maxwidth\" id=\"diagnoseDescription\"\r" +
    "\n" +
    "                 class=\"col-xs-12 form-control\" name=\"diagnose.description\" value=\"\"\r" +
    "\n" +
    "                 ng-model= \"model.diagnoser[0].diagnosBeskrivning\"\r" +
    "\n" +
    "                 uib-typeahead=\"d.value as d.label for d in searchDiagnoseByDescription(viewModel.diagnosKodSystem, $viewValue)\"\r" +
    "\n" +
    "                 typeahead-on-select=\"onDiagnoseDescription1Select($item)\"\r" +
    "\n" +
    "                 typeahead-template-url=\"/web/webjars/sjukersattning/webcert/views/utkast/form/form4.diagnoseTemplate.html\"\r" +
    "\n" +
    "                 typeahead-wait-ms=\"100\">\r" +
    "\n" +
    "        </div>\r" +
    "\n" +
    "      </div>\r" +
    "\n" +
    "      <div class=\"row bottom-padding-small\" ng-show=\"!viewModel.diagnosKodRow2.visible\">\r" +
    "\n" +
    "          <div class=\"col-xs-12\">\r" +
    "\n" +
    "              <a href ng-click=\"viewModel.diagnosKodRow2.visible = true; viewModel.diagnosKodRow2.type = 'diagnos'\">\r" +
    "\n" +
    "                  Lägg till övriga diagnoser </a>|\r" +
    "\n" +
    "              <a href ng-click=\"viewModel.diagnosKodRow2.visible = true; viewModel.diagnosKodRow2.type = 'behandling'\">\r" +
    "\n" +
    "                  Lägg till behandlingsåtgärder som orsakat nedsatt arbetsförmåga</a>\r" +
    "\n" +
    "          </div>\r" +
    "\n" +
    "          <div class=\"col-xs-12\">\r" +
    "\n" +
    "\r" +
    "\n" +
    "          </div>\r" +
    "\n" +
    "      </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "      <!-- ROW 2 -->\r" +
    "\n" +
    "      <div class=\"row bottom-padding-tiny\" ng-show=\"viewModel.diagnosKodRow2.visible\">\r" +
    "\n" +
    "        <div class='col-xs-3'>\r" +
    "\n" +
    "          <label class=\"control-label unbreakable\">{{ viewModel.diagnosKodRow2.type !== null &&\r" +
    "\n" +
    "              viewModel.diagnosKodRow2.type === 'diagnos' ? 'ICD-10-SE' : 'KVÅ' }}\r" +
    "\n" +
    "              </label>\r" +
    "\n" +
    "          <div class=\"controls\">\r" +
    "\n" +
    "            <div class=\"bottom-padding-small clearfix\">\r" +
    "\n" +
    "              <input type=\"text\" id=\"diagnoseCodeOpt1\" name=\"diagnose.codeOpt1\" value=\"\"\r" +
    "\n" +
    "                     ng-model=\"model.diagnoser[1].diagnosKod\" class=\"col-xs-11 form-control\"\r" +
    "\n" +
    "                     uib-typeahead=\"d.value as d.label for d in getDiagnoseCodes(viewModel.diagnosKodSystem, $viewValue) | limitTo:10\"\r" +
    "\n" +
    "                     typeahead-min-length=\"3\" typeahead-on-select=\"onDiagnoseCode2Select($item)\"\r" +
    "\n" +
    "                     typeahead-template-url=\"/web/webjars/sjukersattning/webcert/views/utkast/form/form4.diagnoseTemplate.html\"\r" +
    "\n" +
    "                      wc-to-uppercase>\r" +
    "\n" +
    "            </div>\r" +
    "\n" +
    "          </div>\r" +
    "\n" +
    "        </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "\r" +
    "\n" +
    "        <div class='col-xs-9'>\r" +
    "\n" +
    "          <label class=\"control-label inline-block\">\r" +
    "\n" +
    "              <span message key=\"{{ viewModel.diagnosKodRow2.type !== null && viewModel.diagnosKodRow2.type === 'diagnos' ?\r" +
    "\n" +
    "                                    'sjukersattning.label.ytterligarediagnoser' : 'sjukersattning.label.ytterligarebehandling' }}\"></span>\r" +
    "\n" +
    "            </label>\r" +
    "\n" +
    "          <div class=\"bottom-padding-small clearfix\">\r" +
    "\n" +
    "            <input type=\"text\" id=\"diagnoseDescriptionOpt1\" name=\"diagnose.descriptionOpt1\" value=\"\"\r" +
    "\n" +
    "                   ng-model=\"model.diagnoser[1].diagnosBeskrivning\" class=\"block col-xs-12 form-control maxwidth\"\r" +
    "\n" +
    "                   uib-typeahead=\"d.value as d.label for d in searchDiagnoseByDescription(viewModel.diagnosKodSystem, $viewValue)\"\r" +
    "\n" +
    "                   typeahead-on-select=\"onDiagnoseDescription2Select($item)\"\r" +
    "\n" +
    "                   typeahead-template-url=\"/web/webjars/sjukersattning/webcert/views/utkast/form/form4.diagnoseTemplate.html\"\r" +
    "\n" +
    "                   typeahead-wait-ms=\"100\">\r" +
    "\n" +
    "          </div>\r" +
    "\n" +
    "        </div>\r" +
    "\n" +
    "      </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "      <!-- ROW 3 -->\r" +
    "\n" +
    "      <div class=\"row bottom-padding-small\" ng-show=\"viewModel.diagnosKodRow2.visible && !viewModel.diagnosKodRow3.visible\">\r" +
    "\n" +
    "          <div class=\"col-xs-12\">\r" +
    "\n" +
    "              <a href ng-click=\"viewModel.diagnosKodRow3.visible = true; viewModel.diagnosKodRow3.type = 'diagnos'\">\r" +
    "\n" +
    "                  Lägg till övriga diagnoser </a>|\r" +
    "\n" +
    "              <a href ng-click=\"viewModel.diagnosKodRow3.visible = true; viewModel.diagnosKodRow3.type = 'behandling'\">\r" +
    "\n" +
    "                  Lägg till behandlingsåtgärder som orsakat nedsatt arbetsförmåga</a> </div>\r" +
    "\n" +
    "      </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "      <div class=\"row bottom-padding-small\" ng-show=\"viewModel.diagnosKodRow3.visible\">\r" +
    "\n" +
    "\r" +
    "\n" +
    "        <div class='col-xs-3'>\r" +
    "\n" +
    "            <label class=\"control-label unbreakable\">{{ viewModel.diagnosKodRow3.type !== null &&\r" +
    "\n" +
    "                viewModel.diagnosKodRow3.type === 'diagnos' ? 'ICD-10-SE' : 'KVÅ' }}\r" +
    "\n" +
    "            </label>\r" +
    "\n" +
    "          <input type=\"text\" id=\"diagnoseCodeOpt2\" name=\"diagnose.codeOpt2\" value=\"\"\r" +
    "\n" +
    "                 ng-model=\"model.diagnoser[2].diagnosKod\" class=\"col-xs-11 form-control\"\r" +
    "\n" +
    "                 uib-typeahead=\"d.value as d.label for d in getDiagnoseCodes(viewModel.diagnosKodSystem, $viewValue) | limitTo:10\"\r" +
    "\n" +
    "                 typeahead-min-length=\"3\" typeahead-on-select=\"onDiagnoseCode3Select($item)\"\r" +
    "\n" +
    "                 typeahead-template-url=\"/web/webjars/sjukersattning/webcert/views/utkast/form/form4.diagnoseTemplate.html\"\r" +
    "\n" +
    "                wc-to-uppercase>\r" +
    "\n" +
    "        </div>\r" +
    "\n" +
    "        <div class='col-xs-9'>\r" +
    "\n" +
    "            <label class=\"control-label inline-block\">\r" +
    "\n" +
    "                <span message key=\"{{ viewModel.diagnosKodRow3.type !== null && viewModel.diagnosKodRow3.type === 'diagnos' ?\r" +
    "\n" +
    "                            'sjukersattning.label.ytterligarediagnoser' : 'sjukersattning.label.ytterligarebehandling' }}\"></span>\r" +
    "\n" +
    "            </label>\r" +
    "\n" +
    "          <div class=\"controls\">\r" +
    "\n" +
    "            <input type=\"text\" id=\"diagnoseDescriptionOpt2\" name=\"diagnose.descriptionOpt2\" value=\"\"\r" +
    "\n" +
    "                   ng-model=\"model.diagnoser[2].diagnosBeskrivning\" class=\"block col-xs-12 form-control maxwidth\"\r" +
    "\n" +
    "                   uib-typeahead=\"d.value as d.label for d in searchDiagnoseByDescription(viewModel.diagnosKodSystem, $viewValue)\"\r" +
    "\n" +
    "                   typeahead-on-select=\"onDiagnoseDescription3Select($item)\"\r" +
    "\n" +
    "                   typeahead-template-url=\"/web/webjars/sjukersattning/webcert/views/utkast/form/form4.diagnoseTemplate.html\"\r" +
    "\n" +
    "                   typeahead-wait-ms=\"100\">\r" +
    "\n" +
    "          </div>\r" +
    "\n" +
    "        </div>\r" +
    "\n" +
    "          <span>{{ $scope.model.diagnoser }}</span>\r" +
    "\n" +
    "      </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "\r" +
    "\n" +
    "\r" +
    "\n" +
    "\r" +
    "\n" +
    "    </div>\r" +
    "\n" +
    "</div>\r" +
    "\n"
  );


  $templateCache.put('/web/webjars/fk/sjukersattning/webcert/views/utkast/form/form4b.html',
    "<a id=\"anchor.diagnosticering\" name=\"anchor.diagnosticering\"></a>\r" +
    "\n" +
    "<div ng-form=\"form4b\">\r" +
    "\n" +
    "    <div wc-field field-label=\"sjukersattning.label.diagnostisering\"\r" +
    "\n" +
    "         field-has-errors=\"sjukersattning.helptext.diagnostisering\" class=\"print-pagebreak-after field-fade\">\r" +
    "\n" +
    "\r" +
    "\n" +
    "        <div class=\"alert alert-danger\" ng-if=\"viewState.common.validationMessagesGrouped.diagnostisering\" id=\"validationMessages_diagnostisering\">\r" +
    "\n" +
    "            <ul>\r" +
    "\n" +
    "                <li ng-repeat=\"message in viewState.common.validationMessagesGrouped.diagnostisering\">\r" +
    "\n" +
    "                    <span message key=\"{{message.message}}\"></span>\r" +
    "\n" +
    "                </li>\r" +
    "\n" +
    "            </ul>\r" +
    "\n" +
    "        </div>\r" +
    "\n" +
    "        <div class=\"form-group\">\r" +
    "\n" +
    "              <textarea id=\"diagnostisering\" class=\"form-control\" name=\"diagnostisation\" rows='6'\r" +
    "\n" +
    "                        ng-model=\"model.diagnostisering\" ng-trim=\"false\" />\r" +
    "\n" +
    "        </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "        <div class=\"form-group form-inline\">\r" +
    "\n" +
    "            <label class=\"control-label inline-block bold\">\r" +
    "\n" +
    "                <span message key=\"sjukersattning.label.diagnostisering.nybedomning\"></span>\r" +
    "\n" +
    "\r" +
    "\n" +
    "                <span wc-enable-tooltip field-help-text=\"sjukersattning.label.diagnostisering.nybedomning.help\"></span>\r" +
    "\n" +
    "            </label>\r" +
    "\n" +
    "        </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "        <div class=\"form-group form-inline\">\r" +
    "\n" +
    "            <label class=\"radio-inline\">\r" +
    "\n" +
    "                <input type=\"radio\" id=\"nyBedomning_Yes\" name=\"nyBedomning\" ng-value=\"true\"\r" +
    "\n" +
    "                       ng-model=\"viewModel.nyBedomningDiagnos\" ng-change=\"onChangeNyBedomning()\" checked=\"checked\">Ja\r" +
    "\n" +
    "            </label>\r" +
    "\n" +
    "            <label class=\"radio-inline\">\r" +
    "\n" +
    "                <input type=\"radio\" id=\"nyBedomning_No\" name=\"nyBedomning\" ng-value=\"false\"\r" +
    "\n" +
    "                       ng-model=\"viewModel.nyBedomningDiagnos\" ng-change=\"onChangeNyBedomning()\">Nej\r" +
    "\n" +
    "            </label>\r" +
    "\n" +
    "\r" +
    "\n" +
    "        </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "    </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "\r" +
    "\n" +
    "</div>"
  );


  $templateCache.put('/web/webjars/fk/sjukersattning/webcert/views/utkast/form/form5.html',
    "<a id=\"anchor.funktionsnedsattning\" name=\"anchor.funktionsnedsattning\"></a>\r" +
    "\n" +
    "<h2 class=\"title\">4. Funktionsnedsättning och Aktivitetsbegränsning</h2>\r" +
    "\n" +
    "\r" +
    "\n" +
    "    <div wc-field class=\"print-pagebreak-after field-fade\">\r" +
    "\n" +
    "\r" +
    "\n" +
    "        <div class=\"alert alert-danger\" ng-if=\"viewState.common.validationMessagesGrouped.funktionsnedsattning\" id=\"validationMessages_funktionsnedsattning\">\r" +
    "\n" +
    "            <ul>\r" +
    "\n" +
    "                <li ng-repeat=\"message in viewState.common.validationMessagesGrouped.funktionsnedsattning\">\r" +
    "\n" +
    "                    <span message key=\"{{message.message}}\"></span>\r" +
    "\n" +
    "                </li>\r" +
    "\n" +
    "            </ul>\r" +
    "\n" +
    "        </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "        <div class=\"form-group form-inline\" ng-repeat=\"nedsattning in funktionsnedsattningOptions\">\r" +
    "\n" +
    "\r" +
    "\n" +
    "            <input type=\"checkbox\"\r" +
    "\n" +
    "                   id=\"nedsattning.id\"\r" +
    "\n" +
    "                   name=\"{{nedsattning.id}}-funktionsnedsattningarCheck.intellektuell\"\r" +
    "\n" +
    "                   class=\"checkbox-inline\"\r" +
    "\n" +
    "                   ng-model=\"nedsattning.selected\" ng-value=\"true\" ng-change=\"$parent.setFocus('{{ nedsattning.id }}', nedsattning.selected, nedsattning.text)\">\r" +
    "\n" +
    "            <label class=\"label-inline\"><h3 class=\"title\" ng-if=\"nedsattning.label != null\"\r" +
    "\n" +
    "                                            ng-click=\"nedsattning.selected=!nedsattning.selected\">\r" +
    "\n" +
    "                {{ nedsattning.label }}\r" +
    "\n" +
    "            </h3></label>\r" +
    "\n" +
    "\r" +
    "\n" +
    "            <div class=\"form-nedsattning-wrapper\" ng-class=\"{ 'dark-grey-bg': nedsattning.selected }\">\r" +
    "\n" +
    "\r" +
    "\n" +
    "            <textarea id=\"{{nedsattning.id}}\" class=\"form-control block grey-border\"  name=\"disabilities{{ nedsattning.id }}\" rows='6'\r" +
    "\n" +
    "                        ng-model=\"nedsattning.text\" ng-trim=\"false\" ng-change=\"updateViewModelNedsattningar( nedsattning.id, nedsattning.text )\" ng-focus=\"nedsattning.selected = true\" ></textarea>\r" +
    "\n" +
    "        </div>\r" +
    "\n" +
    "    </div>\r" +
    "\n" +
    "</div>\r" +
    "\n" +
    "\r" +
    "\n"
  );


  $templateCache.put('/web/webjars/fk/sjukersattning/webcert/views/utkast/form/form6.html',
    "<a id=\"anchor.aktivitetsbegransning\" name=\"anchor.aktivitetsbegransning\"></a>\r" +
    "\n" +
    "<div wc-field field-label=\"sjukersattning.label.aktivitetsbegransning\"\r" +
    "\n" +
    "     field-help-text=\"sjukersattning.helptext.aktivitetsbegransning\"\r" +
    "\n" +
    "     field-has-errors=\"viewState.common.validationMessagesGrouped.aktivitetsbegransning\" class=\"print-pagebreak-after field-fade\">\r" +
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
    "    <div class=\"form-aktivitetsbegransning-wrapper\">\r" +
    "\n" +
    "      <textarea id=\"activityLimitation\" class=\"form-control\" name=\"activityLimitation\" rows='6'\r" +
    "\n" +
    "                ng-model=\"model.aktivitetsbegransning\" ng-trim=\"false\"\r" +
    "\n" +
    "                maxlength=\"{{viewState.inputLimits.aktivitetsbegransning}}\" />\r" +
    "\n" +
    "    </div>\r" +
    "\n" +
    "</div>"
  );


  $templateCache.put('/web/webjars/fk/sjukersattning/webcert/views/utkast/form/form7.html',
    "<div wc-field field-label=\"sjukersattning.label.atgarder\"\r" +
    "\n" +
    "     field-help-text=\"sjukersattning.helptext.atgarder\"\r" +
    "\n" +
    "     class=\"print-pagebreak-after field-fade\">\r" +
    "\n" +
    "\r" +
    "\n" +
    "  <div class=\"form-group\">\r" +
    "\n" +
    "    <label class=\"control-label bold\"><span message key=\"sjukersattning.label.behandling.avslutad\"/></label>\r" +
    "\n" +
    "    <textarea id=\"measuresFinished\" class=\"form-control\" name=\"measures.finished\" rows='6'\r" +
    "\n" +
    "              ng-model=\"model.avslutadBehandling\" ng-trim=\"false\"\r" +
    "\n" +
    "              />\r" +
    "\n" +
    "    </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "  <div class=\"form-group\">\r" +
    "\n" +
    "    <label class=\"control-label bold\"><span message key=\"sjukersattning.label.behandling.pagaende\"/></label>\r" +
    "\n" +
    "    <textarea id=\"measuresCurrent\" class=\"form-control\" name=\"measures.current\" rows='6'\r" +
    "\n" +
    "              ng-model=\"model.pagaendeBehandling\" ng-trim=\"false\"\r" +
    "\n" +
    "              />\r" +
    "\n" +
    "  </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "  <div class=\"form-group\">\r" +
    "\n" +
    "    <label class=\"control-label bold\"><span message key=\"sjukersattning.label.behandling.planerad\"/></label>\r" +
    "\n" +
    "    <textarea id=\"measuresOther\" class=\"form-control\" name=\"measures.other\" rows='6'\r" +
    "\n" +
    "              ng-model=\"model.planeradBehandling\" ng-trim=\"false\"\r" +
    "\n" +
    "             />\r" +
    "\n" +
    "  </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "\r" +
    "\n" +
    "\r" +
    "\n" +
    "</div>\r" +
    "\n"
  );


  $templateCache.put('/web/webjars/fk/sjukersattning/webcert/views/utkast/form/form9.html',
    "<a id=\"anchor.medicalpossibilities\" name=\"anchor.medicalpossibilities\"></a>\r" +
    "\n" +
    "<div ng-form=\"form9\">\r" +
    "\n" +
    "  <div wc-field field-label=\"sjukersattning.label.medicinskaforutsattningar\"\r" +
    "\n" +
    "       field-has-errors=\"sjukersattning.label.medicinskaforutsattningar\" class=\"print-pagebreak-after field-fade\">\r" +
    "\n" +
    "\r" +
    "\n" +
    "    <div class=\"alert alert-danger\" ng-if=\"viewState.common.validationMessagesGrouped.medicinskaforutsattningar\" id=\"validationMessages_medicinskaforutsattningar\">\r" +
    "\n" +
    "      <ul>\r" +
    "\n" +
    "        <li ng-repeat=\"message in viewState.common.validationMessagesGrouped.medicinskaforutsattningar\">\r" +
    "\n" +
    "          <span message key=\"{{message.message}}\"></span>\r" +
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
    "      <label class=\"control-label inline-block bold\">\r" +
    "\n" +
    "        <span message key=\"sjukersattning.label.medicinskaforutsattningar.bedomning\"></span>\r" +
    "\n" +
    "      </label>\r" +
    "\n" +
    "    </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "    <div class=\"form-group\">\r" +
    "\n" +
    "              <textarea id=\"medicinskaForutsattningar\" class=\"form-control\" name=\"medicinskaForutsattningar\" rows='6'\r" +
    "\n" +
    "                        ng-model=\"model.prognos\" ng-trim=\"false\" />\r" +
    "\n" +
    "    </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "    <div class=\"form-group form-inline\">\r" +
    "\n" +
    "      <label class=\"control-label inline-block bold\">\r" +
    "\n" +
    "          <span message key=\"sjukersattning.label.prognos\"></span>\r" +
    "\n" +
    "      </label>\r" +
    "\n" +
    "    </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "    <div class=\"form-group\">\r" +
    "\n" +
    "              <textarea id=\"medicinskamojligheter\" class=\"form-control\" name=\"vadPatientenKanGora\" rows='6'\r" +
    "\n" +
    "                        ng-model=\"model.aktivitetsFormaga\" ng-trim=\"false\" />\r" +
    "\n" +
    "    </div>\r" +
    "\n" +
    "  </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "  </div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "\r" +
    "\n" +
    "</div>"
  );


  $templateCache.put('/web/webjars/fk/sjukersattning/webcert/views/utkast/utkast.html',
    "<a id=\"top\"></a>\r" +
    "\n" +
    "<div class=\"container-fluid\" id=\"edit-sjukersattning\">\r" +
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
    "      <div class=\"edit-form\" ng-class=\"{'collapsed' : viewState.common.collapsedHeader}\" wc-size-target>\r" +
    "\n" +
    "\r" +
    "\n" +
    "        <form name=\"certForm\" novalidate autocomplete=\"off\" wc-auto-save>\r" +
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
    "              <div wc-utkast-error-summary view-state=\"viewState\"></div>\r" +
    "\n" +
    "\r" +
    "\n" +
    "              <div id=\"certificate\" class=\"row certificate\">\r" +
    "\n" +
    "                <div class=\"col-md-12 cert-body\">\r" +
    "\n" +
    "\r" +
    "\n" +
    "                  <div ui-view=\"form2\"/>\r" +
    "\n" +
    "\r" +
    "\n" +
    "                  <!-- 2. Diagnos. SAMMA SOM INNAN -->\r" +
    "\n" +
    "                  <div ui-view=\"form4\"/>\r" +
    "\n" +
    "\r" +
    "\n" +
    "                  <!-- 3. Förtydligande diagnos -->\r" +
    "\n" +
    "                  <div ui-view=\"form4b\"/>\r" +
    "\n" +
    "\r" +
    "\n" +
    "                  <!-- 4A. Funktionsnedsättning. - SAMMA SOM INNNA FAST SAMMAFOGAD MED AKTIVITETSBEGRÄNSNING -->\r" +
    "\n" +
    "                  <div ui-view=\"form5\" class=\"form5\"/>\r" +
    "\n" +
    "                  <!-- 4B. aktivitetsbegränsning. -->\r" +
    "\n" +
    "                  <div ui-view=\"form6\" class=\"form6\"/>\r" +
    "\n" +
    "\r" +
    "\n" +
    "                  <!-- 4. MEDICINSKA BEHANDLING. NY -->\r" +
    "\n" +
    "                  <div ui-view=\"form7\"/>\r" +
    "\n" +
    "\r" +
    "\n" +
    "                  <!-- 5. Medicinska förutsättningar för arbete -->\r" +
    "\n" +
    "                  <div ui-view=\"form9\"/>\r" +
    "\n" +
    "\r" +
    "\n" +
    "                  <!-- 6. Övrigt -->\r" +
    "\n" +
    "                  <div ui-view=\"form10\"/>\r" +
    "\n" +
    "\r" +
    "\n" +
    "                  <!-- 7. Kontakt -->\r" +
    "\n" +
    "                  <div ui-view=\"form11\"/>\r" +
    "\n" +
    "\r" +
    "\n" +
    "                  <!-- Additional questions -->\r" +
    "\n" +
    "                  <div ui-view=\"formAdditionalQuestions\"/>\r" +
    "\n" +
    "\r" +
    "\n" +
    "                  <!-- 8. Upplysningar om utfärdare -->\r" +
    "\n" +
    "                  <div ui-view=\"form15\"/>\r" +
    "\n" +
    "\r" +
    "\n" +
    "                  <!-- 9. Sign doktor -->\r" +
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
    "              <div ui-view=\"footer\"></div>\r" +
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

}]);
