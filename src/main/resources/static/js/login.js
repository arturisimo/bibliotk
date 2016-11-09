'use strict';

app.controller('LoginController', function($scope, $modal) {
	
	$scope.login = function(){
		$modal.open({
		    templateUrl : '/fragments/loginmodal',
		    size : 'sm'
		});
	};
	    
});


