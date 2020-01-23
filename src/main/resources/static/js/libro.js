'use strict';

app.controller('LibroController', ['$scope', '$http', function($scope, $http) {
    
	$scope.libro={};
	$scope.libros=[];
    
	listLibro();
  
    function listLibro(){
    	$http.get('/bibliotk/api/libros').success(function(libros) {
        	$scope.libros = libros._embedded.libros;
        }).error(function(error) {
	           console.log(error);
    	});
    }
    
    $scope.addLibro = function(formLibro) {
    	
    	if (formLibro.id == null) {
    		
    		formLibro.alta = false;
    		$http.post('/bibliotk/api/libros', formLibro).success(function(libro) {
	    		listLibro();
	    	}).error(function(error) {
	           console.log(error);
	    	});
    	} else {
    		console.log(formLibro);
	    	$http.put('/bibliotk/api/libros/'+formLibro.id, formLibro).success(function(libro) {
	    		listLibro();
	    	}).error(function(error) {
	           console.log(error);
	    	});
    	}
    	
    }
    
    
    $scope.editLibro = function(libro){
    	console.log(libro);
    	$scope.libro = libro;
    }
    
    $scope.deleteLibro = function(libro){
    	$http.delete('/bibliotk/api/libros/'+libro.id).success(function() {
    		listLibro();
        });
    }
    
    $scope.publishLibro = function(libro){
    	libro.alta = true;
    	$http.put('/bibliotk/api/libros/'+libro.id, libro).success(function() {
    		listLibro();
        });
    }
    $scope.hideLibro = function(libro){
    	libro.alta = false;
    	$http.put('/bibliotk/api/libros/'+libro.id, libro).success(function() {
    		listLibro();
        });
    }
    
}]);
