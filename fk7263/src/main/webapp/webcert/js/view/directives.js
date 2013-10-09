/**
 * Directives used only in module app pages. 
 */
angular.module('wc.common.directives', []);

angular.module('wc.common.directives').directive("wcCertField", ['$rootScope', function($rootScope) {
  return {
      restrict : "A",
      transclude : true,
      replace : true,
      scope : {
        fieldLabel: "@",
        fieldNumber: "@",
        filled: "=?"
      },
      template :
      		'<div class="body-row clearfix">'
      			+'<h4 class="cert-field-number"><span message key="view.label.field"></span> {{fieldNumber}}</h4>'
      			+'<h3 class="title" ng-class="{ \'unfilled\' : !filled}"><span message key="{{ fieldLabel }}"></span> <span class="cert-field-blank" ng-hide="filled"><span message key="view.label.blank"></span></span></h3>'
      			+'<span class="text" ng-show="filled">'
      			+'<span ng-transclude></span>'
      			+'</span>'
         +'</div>'


/*
	//TODO: fix this to use default value for filled if it is omitted (it is already optional but default value is undefined =?)
	compile: function(element,attrs)
    {
	    var filled = attrs.filled || true;
	    var htmlText = '<div class="body-row">'
	               +'   <h3 class="title" ng-class="{ \'unfilled\' : !'+filled+'}"><span message key="{{ title }}"></span> <span ng-hide="'+filled+'"><span message key="view.label.blank"></span></span></h3>'
	               +'   <span class="text" ng-show="'+filled+'">'
	               +'       <span ng-transclude></span>'
	               +'   </span>'
	               +'</div>';
	    element.replaceWith(htmlText);
    }*/
  }
} ]);