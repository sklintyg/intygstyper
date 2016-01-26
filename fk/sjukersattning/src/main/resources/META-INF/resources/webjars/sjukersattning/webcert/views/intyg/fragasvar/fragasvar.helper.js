angular.module('luse').service('sjukersattning.QACtrl.Helper',
    ['$log', '$timeout', 'sjukersattning.fragaSvarService', 'common.fragaSvarCommonService', '$window', 'common.statService',
        function( $log, $timeout, fragaSvarService, fragaSvarCommonService, $window, statService) {
        'use strict';

        function delayFindMessageAndAct(timeout, qaList, message, onFound) {
            $timeout(function() {
                var i;
                for(i = 0; i < qaList.length; i++){
                    if(qaList[i].id === message.id && qaList[i].proxyMessage !== undefined) {
                        onFound(i);
                        break;
                    }
                }
            }, timeout);

            $log.debug('Message not found:' + message.id);
        }

        function addListMessage(qaList, qa, messageId) {
            var messageProxy = {}; // = angular.copy(qa);
            messageProxy.proxyMessage = messageId;
            messageProxy.id = qa.id;
            messageProxy.senasteHandelseDatum = qa.senasteHandelseDatum;
            messageProxy.messageStatus = qa.status;
            qaList.push(messageProxy);

            delayFindMessageAndAct(5000, qaList, messageProxy, function(index) {
                qaList[index].messageStatus = 'HIDDEN';
                delayFindMessageAndAct(2000, qaList, messageProxy, function(index) {
                    qaList.splice(index, 1);
                });
            });
        }

        this.updateAnsweredAsHandled = function(deferred, unhandledQas, fromHandledDialog){
            if(unhandledQas === undefined || unhandledQas.length === 0 ){
                return;
            }
            fragaSvarService.closeAllAsHandled(unhandledQas,
                function(qas){
                    if(qas) {
                        angular.forEach(qas, function(qa) { //unused parameter , key
                            fragaSvarCommonService.decorateSingleItem(qa);
                            if(fromHandledDialog) {
                                qa.proxyMessage = 'sjukersattning.fragasvar.marked.as.hanterad';
                            } else {
                                addListMessage(qas, qa, 'sjukersattning.fragasvar.marked.as.hanterad'); // TODOOOOOOOO TEST !!!!!!!!!!
                            }
                        });
                        statService.refreshStat();
                    }
                    $window.doneLoading = true;
                    if(deferred) {
                        deferred.resolve();
                    }
                },function() { // unused parameter: errorData
                    // show error view
                    $window.doneLoading = true;
                    if(deferred) {
                        deferred.resolve();
                    }
                });
        };


    }]);