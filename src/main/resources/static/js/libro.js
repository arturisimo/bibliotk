'use strict';

app.controller('LibroController', ['$scope', '$http', function($scope, $http) {
    
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
    	
    	if (formLibro.id == null) {
    		
    		formLibro.alta = 'N'
    		$http.post('/api/libros', formLibro).success(function(libro) {
	    		listLibro();
	    	}).error(function(error) {
	           console.log(error);
	    	});
    	} else {
    		console.log(formLibro);
	    	$http.put('/api/libros/'+formLibro.id, formLibro).success(function(libro) {
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
    	$http.delete('/api/libros/'+libro.id).success(function() {
    		listLibro();
        });
    }
    
    $scope.publishLibro = function(libro){
    	libro.alta = 'S'
    	$http.put('/api/libros/'+libro.id, libro).success(function() {
    		listLibro();
        });
    }
    $scope.hideLibro = function(libro){
    	libro.alta = 'N'
    	$http.put('/api/libros/'+libro.id, libro).success(function() {
    		listLibro();
        });
    }
    
}]);
