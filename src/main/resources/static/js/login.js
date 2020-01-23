'use strict';

app.controller('LoginController', function($scope, $modal) {
	
	$scope.login = function(){
		$modal.open({
		    templateUrl : '/bibliotk/fragments/loginmodal',
		    size : 'sm'
		});
	};
	    
});


