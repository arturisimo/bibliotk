'use strict';

app.controller('jobController', ['$scope', '$http', function($scope, $http) {
    
	$scope.job={};
	$scope.msg = {};
	$scope.jobs=[];
    
	
	$http.get('/bibliotk/admin/job/list').success(function(jobs) {
    	$scope.jobs = jobs;
    }).error(function(error) {
        console.log(error);
	});
    
    $scope.fireJob = function(job){
    	$http.get('/bibliotk/admin/fire/'+job.jobName).success(function(msg) {
    		console.log(msg);
    		$scope.msg = msg;
        	
        }).error(function(error) {
        	$scope.msg = error;
    	});
    }
    
}]);
