'use strict';

angular.module('btkApp', [ 'ui.bootstrap' ]).controller('LoginCtrl', function($scope, $modal) {

    modal_open();
    
    function modal_open() {
    	
		var modalInstance = $modal.open({
		    templateUrl : '/fragments/loginmodal',
		    controller : 'LoginModalCtrl',
		    size : 'sm'
		});
	
		modalInstance.result.then(function() {
		    modal_open();
		},
	
		function() {
		    modal_open();
		});

    }

}).controller('LoginModalCtrl', function($scope, $modalInstance) {

});
