'use strict';

angular.module('myApp').controller('LibroController', ['$scope', '$http', function($scope, $http) {
    
	$scope.libro={};
	$scope.libros=[];
    
	listLibro();
    
    function listLibro(){
    	$http.get('/api/libros').success(function(libros) {
        	$scope.libros = libros._embedded.libros;
        }).error(function(error) {
	           console.log(error);
    	});
    }
    
    $scope.addLibro = function(formLibro) {
    	console.log(formLibro);
    	
    	if ( formLibro.id == null) {
    		$http.post('/api/libros', formLibro).success(function(libro) {
	    		listLibro();
	    	}).error(function(error) {
	           console.log(error);
	    	});
    	} else {
	    	$http.put('/api/libros/'+formLibro.id, formLibro).success(function(libro) {
	    		listLibro();
	    	}).error(function(error) {
	           console.log(error);
	    	});
    	}
    }
    
    $scope.editLibro = function(libro){
    	$scope.libro = libro;
    }
    $scope.deleteLibro = function(libro){
    	console.log(libro);
    	$http.delete('/api/libros/'+libro.id).success(function() {
    		listLibro();
        });
    }
    
}]);
