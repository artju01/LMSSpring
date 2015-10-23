var addAuthorModal,listAuthorsModal; 

var lmsModule = angular.module('lmsApp', ['ngRoute', 'ui.bootstrap']);

lmsModule.filter('format', function () {
	   return function (input) {
	      return input+"10";
	   };
	});


var currentPage = 1;

lmsModule.directive('ngLmsSearchBox', ['$http', function($http) {
	  return {
	      restrict: 'E',
	      replace: 'true',
	      scope: {
	    	  searchtype: '@searchtype',
	    	  ngModel: '=',
	    	  authors: '@authors'
	        },
	      templateUrl: 'searchBox.html',
	      bindToController: true,
	      link: function (scope, element, attrs) {
	    	  	if(scope.searchtype == "Authors") {	 
		            element.bind('keyup', function () {	
		            	if (!scope.ngModel) {
		            		$http.post("listAuthors/"+currentPage).
		    				success(function(data) {
		    					scope.$emit('notification', data);
		    				});
		            	}
		            	else {
		            		$http({
					  			  method: "POST",
								  url: "searchAuthorsWithPage/"+currentPage+"/"+scope.ngModel
				        		}).success(function(data) {
				        			scope.$emit('notification', data);	        	
				    			});
		            	}		        				        
		            });
	    	  	} 
	    	  	else if(scope.searchtype == "AuthorsAll") {		    	
		            element.bind('keyup', function () {			           
		            	if (!scope.ngModel) {
		            		$http.post("listAuthors/-1").
		    				success(function(data) {
		    					scope.$emit('notificationAuthor', data);
		    				});
		            	}
		            	else {
		            		$http({
					  			  method: "POST",
								  url: "searchAuthorsWithPage/-1/"+scope.ngModel
				        		}).success(function(data) {			        	
				        			scope.$emit('notificationAuthor', data);	        	
				    			});
		            	}		        				        
		            });
	    	  	} 
	    	  	else if (scope.searchtype == "Branches") {
	    	  		element.bind('keyup', function () {	
		            	if (!scope.ngModel) {
		            		$http.post("listBranches/"+currentPage).
		    				success(function(data) {
		    					scope.$emit('notification', data);
		    				});
		            	}
		            	else {
		            		$http({
					  			  method: "POST",
								  url: "searchBranchesWithPage/"+currentPage+"/"+scope.ngModel
				        		}).success(function(data) {
				        			scope.$emit('notification', data);	        	
				    			});
		            	}		        				        
		            });
	    	  	} 
	    	  	else if (scope.searchtype == "Publishers") {
	    	  		element.bind('keyup', function () {	
		            	if (!scope.ngModel) {
		            		$http.post("listPublishers/"+currentPage).
		    				success(function(data) {
		    					scope.$emit('notification', data);
		    				});
		            	}
		            	else {
		            		$http({
					  			  method: "POST",
								  url: "searchPublishersWithPage/"+currentPage+"/"+scope.ngModel
				        		}).success(function(data) {
				        			scope.$emit('notification', data);	        	
				    			});
		            	}		        				        
		            });
	    	  	} 
	    	  	else if(scope.searchtype == "PublishersAll") {		    	
		            element.bind('keyup', function () {			           
		            	if (!scope.ngModel) {
		            		$http.post("listPublishers/-1").
		    				success(function(data) {
		    					scope.$emit('notificationPublisher', data);
		    				});
		            	}
		            	else {
		            		$http({
					  			  method: "POST",
								  url: "searchPublishers/"+scope.ngModel
				        		}).success(function(data) {			        	
				        			scope.$emit('notificationPublisher', data);	        	
				    			});
		            	}		        				        
		            });
	    	  	} 
	    	  	else if (scope.searchtype == "Borrowers") {
	    	  		element.bind('keyup', function () {	
		            	if (!scope.ngModel) {
		            		$http.post("listBorrowers/"+currentPage).
		    				success(function(data) {
		    					scope.$emit('notification', data);
		    				});
		            	}
		            	else {
		            		$http({
					  			  method: "POST",
								  url: "searchBorrowersWithPage/"+currentPage+"/"+scope.ngModel
				        		}).success(function(data) {
				        			scope.$emit('notification', data);	        	
				    			});
		            	}		        				        
		            });
	    	  	} 
	    	  	else if (scope.searchtype == "Books") {
	    	  		element.bind('keyup', function () {	
		            	if (!scope.ngModel) {
		            		$http.post("listBooks/"+currentPage).
		    				success(function(data) {
		    					scope.$emit('notification', data);
		    				});
		            	}
		            	else {
		            		$http({
					  			  method: "POST",
								  url: "searchBooksWithPage/"+currentPage+"/"+scope.ngModel
				        		}).success(function(data) {
				        			scope.$emit('notification', data);	        	
				    			});
		            	}		        				        
		            });
	    	  	}
	        }
	  };
	}]);


lmsModule.service('sharedData', function(){
	var author;
	//var branch;
	
	return {
		getAuthor: function () {
			return author;
		},
		setAuthor: function (value) {
			author = value;
		}
	}
})

lmsModule.config([ "$routeProvider", function($routeProvider) {
	return $routeProvider.when("/", {
		redirectTo : "/index"
	}).when("/home", {
		templateUrl : "index.html"
	}).when("/listAuthors", {
		templateUrl : "listAuthors.html"
	}).when("/listBooks", {
		templateUrl : "listBooks.html"
	}).when("/listPublishers", {
		templateUrl : "listPublishers.html"
	}).when("/listBranches", {
		templateUrl : "listBranches.html"
	}).when("/listBorrowers", {
		templateUrl : "listBorrowers.html"
	})
} ]);


lmsModule.controller('homeCtrl', ["$scope", "$http", "$modal", function($scope, $http, $modal) {
	$scope.listAuthors = function() {
		listAuthorsModal = $modal.open({
            templateUrl: "listAuthors.html",
            controller: "listAuthorsCtrl"
        });
	};
}]);

lmsModule.controller('listAuthorsCtrl', ["$scope", "$http", "$modal", "$rootScope" ,"sharedData",      
                                         function($scope, $http, $modal, sharedData, $rootScope) {
	$http.post("countAuthors").
		success(function(data) {
			var range = [];
			var maxPage = Math.ceil(data/10);
			for(var i=1;(i<=maxPage);i++) {
			  range.push(i);
			}
			$scope.range = range;

			currentPage = 1;
			$scope.reloadData();
		});

	$scope.reloadData = function () {
		$http.post("listAuthors/"+currentPage).
		success(function(data) {
			$scope.authors = data;
		});	
	}
	
	$scope.$on('notification', function (evt, data) {
        $scope.authors = data;
    });
	
	$scope.pageAuthor = function(pgNo) {
		currentPage = pgNo;
		$scope.reloadData();
	}
	
	$scope.editAuthor = function(author) {
		//sharedData.setAuthor(author)
		$rootScope.author = author;
		editModalWindow = $modal.open({
			templateUrl: "editAuthorModal.html",
			controller: "editAuthorCtrl"
		});
		
	}
	
	$scope.deleteAuthor = function(author) {
		$rootScope.author = author;
		console.log($rootScope);
		deleteModalWindow = $modal.open({
			templateUrl: "deleteAuthorModal.html",
			controller: "deleteAuthorCtrl"
		});
		
		deleteModalWindow.result.then(function () {
			$http.post("countAuthors").
			success(function(data) {
				var range = [];
				var maxPage = Math.ceil(data/10);
				for(var i=1;(i<=maxPage);i++) {
				  range.push(i);
				}
				
				if (currentPage > maxPage) {
					currentPage = maxPage
				}
				
				$scope.range = range;
				$scope.reloadData();
			});
		});
	}
	
	$scope.addAuthor = function(author) {
		modalWindow = $modal.open({
			templateUrl: "addAuthorModal.html",
			controller: "addAuthorCtrl"
		});
		
		modalWindow.result.then(function () {
			$http.post("countAuthors").
			success(function(data) {
				var range = [];
				var maxPage = Math.ceil(data/10);
				currentPage = maxPage;
				for(var i=1;(i<=maxPage);i++) {
				  range.push(i);
				}
				$scope.range = range;
				$scope.reloadData();
			});
		});
	}
	
	
}]);

//we can use either service(sharedData) or $rootScope  
//if we use service, we need to create services for each data.
//for rootScope, we don't need to write service. (reduce line of code)

lmsModule.controller('editAuthorCtrl', ["$scope", "$http", "$modal","$rootScope", "sharedData", 
                                        function($scope, $http, $modal,sharedData, $rootScope) {
	console.log($rootScope);
	$scope.authorName = $rootScope.author.authorName;
	$scope.cancel = function() {
		editModalWindow.close('close');
	};
	
	$scope.updateAuthor = function() {
		$rootScope.author.authorName = $scope.authorName;
		$http.post("editAuthor",$rootScope.author).
		success(function(data) {
			alert("Success");	
			editModalWindow.close('close');
		});
	}
	
}]);

lmsModule.controller('deleteAuthorCtrl', ["$scope", "$http", "$modal","$rootScope", "sharedData", 
                                          function($scope, $http, $modal,sharedData, $rootScope) {
  	console.log($rootScope);
  	$scope.cancel = function() {
  		deleteModalWindow.close('close');
  	};
  	
  	$scope.deleteAuthor = function() {
  		$http.post("deleteAuthor",$rootScope.author).
  		success(function(data) {
  			alert("Success");	
  			deleteModalWindow.close('close');
  		});
  	}
  	
  }]);

lmsModule.controller('addAuthorCtrl', ["$scope", "$http", 
                                       function($scope, $http) {
        	
        	$scope.cancel = function() {
        		modalWindow.close('close');
        	};
        	
        	$scope.addAuthor = function() {
        		var author = {
        				authorName:$scope.authorName
        		};
        		$http.post("addAuthor",author).
        		success(function(data) {
        			alert("Success");	
        			modalWindow.close('close');
        		});
        	}
        	
}]);


///////////////////////////////////////////////////////////////////////////////////
///////////////////////////BRANCHES////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
lmsModule.controller('listBranchesCtrl', ["$scope", "$http", "$modal", "$rootScope",
                                          function($scope, $http, $modal, $rootScope) {
	$http.post("countBranches").
		success(function(data) {
			var range = [];
			var maxPage = Math.ceil(data/10);
			for(var i=1;(i<=maxPage);i++) {
			  range.push(i);
			}
			$scope.range = range;

			$http.post("listBranches/1").
				success(function(data) {
					$scope.branches = data;				
				});
		});
	
	$scope.reloadData = function () {
		$http.post("listBranches/"+currentPage).
		success(function(data) {
			$scope.branches = data;
		});	
	}
	
	$scope.$on('notification', function (evt, data) {
        $scope.branches = data;
    });
	
	$scope.pageBranches = function(pgNo) {
		currentPage = pgNo;
		$scope.reloadData();
	}
	
	$scope.addBranch = function() {
		modalWindow = $modal.open({
			templateUrl: "addBranchModal.html",
			controller: "addBranchCtrl"
		});
		
		modalWindow.result.then(function () {
			$http.post("countBranches").
			success(function(data) {
				var range = [];
				var maxPage = Math.ceil(data/10);
				currentPage = maxPage;
				for(var i=1;(i<=maxPage);i++) {
				  range.push(i);
				}
				$scope.range = range;
				$scope.reloadData();
			});
		});
	}
	
	$scope.editBranch = function(branch) {
		$rootScope.branch = branch;
		editModalWindow = $modal.open({
			templateUrl: "editBranchModal.html",
			controller: "editBranchCtrl"
		});
		
	}
	
	$scope.deleteBranch = function(branch) {
		$rootScope.branch = branch;
		deleteModalWindow = $modal.open({
			templateUrl: "deleteBranchModal.html",
			controller: "deleteBranchCtrl"
		});
		
		deleteModalWindow.result.then(function () {
			$http.post("countBranches").
			success(function(data) {
				var range = [];
				var maxPage = Math.ceil(data/10);
				for(var i=1;(i<=maxPage);i++) {
				  range.push(i);
				}
				
				if (currentPage > maxPage) {
					currentPage = maxPage
				}
				
				$scope.range = range;
				$scope.reloadData();
			});
		});
	}
}]);

lmsModule.controller('addBranchCtrl', ["$scope", "$http", 
                                       function($scope, $http) {    
        	$scope.cancel = function() {
        		modalWindow.close('close');
        	};
        	
        	$scope.addBranch = function() {
        		var branch = {
        				name:$scope.branchName,
        				address:$scope.branchAddress
        		};
        		$http.post("addBranch",branch).
        		success(function(data) {
        			alert("Success");	
        			modalWindow.close('close');
        		});
        	}
        	
}]);

lmsModule.controller('editBranchCtrl', ["$scope", "$http", "$modal","$rootScope", 
                                        function($scope, $http, $modal, $rootScope) {
	$scope.branchName = $rootScope.branch.name;
	$scope.branchAddress = $rootScope.branch.address;
	$scope.cancel = function() {
		editModalWindow.close('close');
	};
	
	$scope.updateBranch = function() {
		$rootScope.branch.name = $scope.branchName;
		$rootScope.branch.address =  $scope.branchAddress;
		$http.post("editBranch",$rootScope.branch).
		success(function(data) {
			alert("Success");	
			editModalWindow.close('close');
		});
	}
	
	
}]);

lmsModule.controller('deleteBranchCtrl', ["$scope", "$http", "$modal","$rootScope", 
                                          function($scope, $http, $modal, $rootScope) {
  	$scope.cancel = function() {
  		deleteModalWindow.close('close');
  	};
  	
  	$scope.deleteBranch = function() {
  		$http.post("deleteBranch",$rootScope.branch).
  		success(function(data) {
  			alert("Success");	
  			deleteModalWindow.close('close');
  		});
  	}
  	
  }]);

///////////////////////////////////////////////////////////////////////////////////
///////////////////////////Publisher///////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////

lmsModule.controller('listPublishersCtrl', ["$scope", "$http", "$modal", "$rootScope", function($scope, $http, $modal, $rootScope) {
	$http.post("countPublishers").
		success(function(data) {
			var range = [];
			var maxPage = Math.ceil(data/10);
			for(var i=1;(i<=maxPage);i++) {
			  range.push(i);
			}
			$scope.range = range;

			$http.post("listPublishers/1").
				success(function(data) {
					$scope.publishers = data;				
				});
		});
	
	$scope.pagePublisher = function(pgNo) {
		currentPage = pgNo;
		$scope.reloadData();
	}
	
	$scope.$on('notification', function (evt, data) {
        $scope.publishers = data;
    });
	
	$scope.reloadData = function () {
		$http.post("listPublishers/"+currentPage).
		success(function(data) {
			$scope.publishers = data;
		});	
	}
	
	$scope.addPublisher = function() {
		modalWindow = $modal.open({
			templateUrl: "addPublisherModal.html",
			controller: "addPublisherCtrl"
		});
		
		modalWindow.result.then(function () {
			$http.post("countPublishers").
			success(function(data) {
				var range = [];
				var maxPage = Math.ceil(data/10);
				currentPage = maxPage;
				for(var i=1;(i<=maxPage);i++) {
				  range.push(i);
				}
				$scope.range = range;
				$scope.reloadData();
			});
		});
	}
	
	$scope.editPublisher = function(pub) {
		$rootScope.publisher = pub;
		editModalWindow = $modal.open({
			templateUrl: "editPublisherModal.html",
			controller: "editPublisherCtrl"
		});
		
	}
	
	$scope.deletePublisher = function(pub) {
		$rootScope.publisher = pub;
		deleteModalWindow = $modal.open({
			templateUrl: "deletePublisherModal.html",
			controller: "deletePublisherCtrl"
		});
		
		deleteModalWindow.result.then(function () {
			$http.post("countPublishers").
			success(function(data) {
				var range = [];
				var maxPage = Math.ceil(data/10);
				for(var i=1;(i<=maxPage);i++) {
				  range.push(i);
				}
				
				if (currentPage > maxPage) {
					currentPage = maxPage
				}
				
				$scope.range = range;
				$scope.reloadData();
			});
		});
	}
	
}]);

lmsModule.controller('addPublisherCtrl', ["$scope", "$http", 
                                       function($scope, $http) {
        	
        	$scope.cancel = function() {
        		modalWindow.close('close');
        	};
        	
        	$scope.addPublisher = function() {
        		var publisher = {
        				publisherName:$scope.publisherName,
        				address:$scope.address,
        				phone:$scope.phone
        		};
        		$http.post("addPublisher",publisher).
        		success(function(data) {
        			alert("Success");	
        			modalWindow.close('close');
        		});
        	}
        	
}]);

lmsModule.controller('editPublisherCtrl', ["$scope", "$http", "$modal","$rootScope", 
                                        function($scope, $http, $modal, $rootScope) {
	$scope.publisherName = $rootScope.publisher.publisherName;
	$scope.address = $rootScope.publisher.address;
	$scope.phone = $rootScope.publisher.phone;
	$scope.cancel = function() {
		editModalWindow.close('close');
	};
	
	$scope.updatePublisher = function() {
		$rootScope.publisher.publisherName = $scope.publisherName;
		$rootScope.publisher.address =  $scope.address;
		$rootScope.publisher.phone =  $scope.phone;
		$http.post("editPublisher",$rootScope.publisher).
		success(function(data) {
			alert("Success");	
			editModalWindow.close('close');
		});
	}
	
	
}]);

lmsModule.controller('deletePublisherCtrl', ["$scope", "$http", "$modal","$rootScope", 
                                          function($scope, $http, $modal, $rootScope) {
  	$scope.cancel = function() {
  		deleteModalWindow.close('close');
  	};
  	
  	$scope.deletePublisher = function() {
  		$http.post("deletePublisher",$rootScope.publisher).
  		success(function(data) {
  			alert("Success");	
  			deleteModalWindow.close('close');
  		});
  	}
  	
  }]);


///////////////////////////////////////////////////////////////////////////////////
///////////////////////////Borrower///////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////

lmsModule.controller('listBorrowersCtrl', ["$scope", "$http", "$modal", "$rootScope", function($scope, $http, $modal, $rootScope) {
	$http.post("countBorrowers").
	success(function(data) {
		var range = [];
		var maxPage = Math.ceil(data/10);
		for(var i=1;(i<=maxPage);i++) {
			range.push(i);
		}
		$scope.range = range;

		$http.post("listBorrowers/1").
		success(function(data) {
			$scope.borrowers = data;				
		});
	});

	$scope.pageBorrowers = function(pgNo) {
		currentPage = pgNo;
		$scope.reloadData();
	}

	$scope.$on('notification', function (evt, data) {
		$scope.borrowers = data;
	});

	$scope.reloadData = function () {
		$http.post("listBorrowers/"+currentPage).
		success(function(data) {
			$scope.borrowers = data;
		});	
	}

	$scope.addBorrower = function() {
		modalWindow = $modal.open({
			templateUrl: "addBorrowerModal.html",
			controller: "addBorrowerCtrl"
		});

		modalWindow.result.then(function () {
			$http.post("countBorrowers").
			success(function(data) {
				var range = [];
				var maxPage = Math.ceil(data/10);
				currentPage = maxPage;
				for(var i=1;(i<=maxPage);i++) {
					range.push(i);
				}
				$scope.range = range;
				$scope.reloadData();
			});
		});
	}

	$scope.editBorrower = function(borrower) {
		$rootScope.borrower = borrower;
		editModalWindow = $modal.open({
			templateUrl: "editBorrowerModal.html",
			controller: "editBorrowerCtrl"
		});

	}

	$scope.deleteBorrower = function(borrower) {
		$rootScope.borrower = borrower;
		deleteModalWindow = $modal.open({
			templateUrl: "deleteBorrowerModal.html",
			controller: "deleteBorrowerCtrl"
		});

		deleteModalWindow.result.then(function () {
			$http.post("countBorrowers").
			success(function(data) {
				var range = [];
				var maxPage = Math.ceil(data/10);
				for(var i=1;(i<=maxPage);i++) {
					range.push(i);
				}

				if (currentPage > maxPage) {
					currentPage = maxPage
				}

				$scope.range = range;
				$scope.reloadData();
			});
		});
	}

}]);

lmsModule.controller('addBorrowerCtrl', ["$scope", "$http", 
                                         function($scope, $http) {

	$scope.cancel = function() {
		modalWindow.close('close');
	};

	$scope.addBorrower = function() {
		var borrower = {
				name:$scope.name,
				address:$scope.address,
				phone:$scope.phone
		};
		$http.post("addBorrower",borrower).
		success(function(data) {
			alert("Success");	
			modalWindow.close('close');
		});
	}

}]);

lmsModule.controller('editBorrowerCtrl', ["$scope", "$http", "$modal","$rootScope", 
                                           function($scope, $http, $modal, $rootScope) {
   	$scope.name = $rootScope.borrower.name;
   	$scope.address = $rootScope.borrower.address;
   	$scope.phone = $rootScope.borrower.phone;
   	$scope.cancel = function() {
   		editModalWindow.close('close');
   	};
   	
   	$scope.updateBorrower = function() {
   		$rootScope.borrower.name = $scope.name;
   		$rootScope.borrower.address =  $scope.address;
   		$rootScope.borrower.phone =  $scope.phone;
   		$http.post("editBorrower",$rootScope.borrower).
   		success(function(data) {
   			alert("Success");	
   			editModalWindow.close('close');
   		});
   	}
}]);

lmsModule.controller('deleteBorrowerCtrl', ["$scope", "$http", "$modal","$rootScope", 
                                             function($scope, $http, $modal, $rootScope) {
     	$scope.cancel = function() {
     		deleteModalWindow.close('close');
     	};
     	
     	$scope.deleteBorrower = function() {
     		$http.post("deleteBorrower",$rootScope.borrower).
     		success(function(data) {
     			alert("Success");	
     			deleteModalWindow.close('close');
     		});
     	}
     	
}]);

///////////////////////////////////////////////////////////////////////////////////
///////////////////////////Book////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////

lmsModule.controller('listBooksCtrl', ["$scope", "$http", "$modal", "$rootScope", function($scope, $http, $modal, $rootScope) {
	$http.post("countBooks").
	success(function(data) {
		var range = [];
		var max = Math.ceil(data/10)
		for(var i=1;(i<=max);i++) {
			range.push(i);
		}
		$scope.range = range;

		$http.post("listBooks/1").
		success(function(data) {
			$scope.books = data;					
		});
	});

	$scope.$on('notification', function (evt, data) {
        $scope.books = data;
    });
	
	$scope.pageBook = function(pgNo) {
		currentPage = pgNo;
		$scope.reloadData();
	}
	
	$scope.reloadData = function () {
		$http.post("listBooks/"+currentPage).
		success(function(data) {
			$scope.books = data;
		});	
	}
	
	$scope.addBook = function() {
		modalWindow = $modal.open({
			templateUrl: "addBookModal.html",
			controller: "addBookCtrl"
		});

		modalWindow.result.then(function () {
			$http.post("countBooks").
			success(function(data) {
				var range = [];
				var maxPage = Math.ceil(data/10);
				currentPage = maxPage;
				for(var i=1;(i<=maxPage);i++) {
					range.push(i);
				}
				$scope.range = range;
				$scope.reloadData();
			});
		});
	}

	$scope.editBook = function(book) {
		$rootScope.book = book;
		editModalWindow = $modal.open({
			templateUrl: "editBookModal.html",
			controller: "editBookCtrl"
		});

	}

	$scope.deleteBook = function(book) {
		$rootScope.book = book;
		deleteModalWindow = $modal.open({
			templateUrl: "deleteBookModal.html",
			controller: "deleteBookCtrl"
		});

		deleteModalWindow.result.then(function () {
			$http.post("countBooks").
			success(function(data) {
				var range = [];
				var maxPage = Math.ceil(data/10);
				for(var i=1;(i<=maxPage);i++) {
					range.push(i);
				}

				if (currentPage > maxPage) {
					currentPage = maxPage
				}

				$scope.range = range;
				$scope.reloadData();
			});
		});
	}

}]);

lmsModule.controller('addBookCtrl', ["$scope", "$http", 
                                     function($scope, $http) {
	
	$http.post("listGenres").
	success(function(data) {
		$scope.genres = data;	
		$scope.selectedGenres = [];
	});
	
	$http.post("listPublishers/-1").
	success(function(data) {
		$scope.publishers = data;	
	});
	
	$http.post("listAuthors/-1").
	success(function(data) {
		$scope.authors = data;
		$scope.selectedAuthors = [];
	});
	
	$scope.$on('notificationAuthor', function (evt, data) {
        $scope.authors = data;
    });
	
	$scope.selectMoveGenre = function (select1, select2) {
		var gen;
		for (var i=select1.length-1; i>=0; i--) {
			angular.forEach($scope.genres, function(value, key){
				if (value.genreId == select1[i]) {
					gen = value;
					var isDuplicate = false;
					angular.forEach($scope.selectedGenres, function(value2, key2){
						if (value2.genreId == gen.genreId) {
							isDuplicate = true;
						}
					});
					
					if (isDuplicate == false) {
						$scope.selectedGenres.push(gen);
					}
				}
			});
		}
	}
	
	$scope.removeGenre = function (selectBox) {
		for (var i=selectBox.length-1; i>=0; i--) {
			var count=0;
			var removeIndex =-1;
			angular.forEach($scope.selectedGenres, function(value, key){
				if (value.genreId == selectBox[i]) {
					removeIndex = count;
				}
				count++;
			});
			
			if(removeIndex != -1) {
				$scope.selectedGenres.splice(removeIndex,1);
			}
		}
	}
	
	$scope.selectMoveAuthor = function (select1, select2) {
		var auth;
		for (var i=select1.length-1; i>=0; i--) {
			angular.forEach($scope.authors, function(value, key){
				if (value.authorId == select1[i]) {
					auth = value;
					var isDuplicate = false;
					angular.forEach($scope.selectedAuthors, function(value2, key2){
						if (value2.authorId == auth.authorId) {
							isDuplicate = true;
						}
					});
					
					if (isDuplicate == false) {
						$scope.selectedAuthors.push(auth);
					}
				}
			});
		}
	}
	
	$scope.removeAuthor = function (selectBox) {
		for (var i=selectBox.length-1; i>=0; i--) {
			var count=0;
			var removeIndex =-1;
			angular.forEach($scope.selectedAuthors, function(value, key){
				if (value.authorId == selectBox[i]) {
					removeIndex = count;
				}
				count++;
			});
			
			if(removeIndex != -1) {
				$scope.selectedAuthors.splice(removeIndex,1);
			}
		}
	}
	
	$scope.cancel = function() {
		modalWindow.close('close');
	}

	$scope.addBook = function() {
		console.log($scope.selectedPublisher);
		pub = {
				publisherId:$scope.selectedPublisher,
				publisherName:" "
		};
		
		var book = {
				title:$scope.bookName,
				publisher:pub,
				authors:$scope.selectedAuthors,
				genres:$scope.selectedGenres
		};
		
		console.log(book);
		
		if ($scope.bookName == null) {
			alert("Please enter book name");
			if ($scope.selectedPublisher == null) {
				alert("Please select pulisher");	
				if ($scope.selectedAuthors.length == 0) {
					alert("Please select author");	
				}
			}
		}
		else {
			if ($scope.selectedPublisher != null) {
				if ($scope.selectedAuthors.length > 0) {
					$http.post("addBook",book).
					success(function(data) {
						alert("Success");	
						modalWindow.close('close');
					});
				}
			}
		}
		
		
		
	}

}]);

lmsModule.controller('editBookCtrl', ["$scope", "$http", "$rootScope", 
                                     function($scope, $http, $rootScope) {
	
	
	$scope.bookName = $rootScope.book.title;
   	//$scope.address = $rootScope.borrower.address;
   	//$scope.phone = $rootScope.borrower.phone;
	
	$http.post("listGenres").
	success(function(data) {
		$scope.genres = data;	
		$scope.selectedGenres = $rootScope.book.genres;
	});
	
	$http.post("listPublishers/-1").
	success(function(data) {
		$scope.publishers = data;	
	});
	
	$http.post("listAuthors/-1").
	success(function(data) {
		$scope.authors = data;
		$scope.selectedAuthors = $rootScope.book.authors;
	});
	
	$scope.$on('notificationAuthor', function (evt, data) {
        $scope.authors = data;
    });
	
	$scope.selectMoveGenre = function (select1, select2) {
		var gen;
		for (var i=select1.length-1; i>=0; i--) {
			angular.forEach($scope.genres, function(value, key){
				if (value.genreId == select1[i]) {
					gen = value;
					var isDuplicate = false;
					angular.forEach($scope.selectedGenres, function(value2, key2){
						if (value2.genreId == gen.genreId) {
							isDuplicate = true;
						}
					});
					
					if (isDuplicate == false) {
						$scope.selectedGenres.push(gen);
					}
				}
			});
		}
	}
	
	$scope.removeGenre = function (selectBox) {
		for (var i=selectBox.length-1; i>=0; i--) {
			var count=0;
			var removeIndex =-1;
			angular.forEach($scope.selectedGenres, function(value, key){
				if (value.genreId == selectBox[i]) {
					removeIndex = count;
				}
				count++;
			});
			
			if(removeIndex != -1) {
				$scope.selectedGenres.splice(removeIndex,1);
			}
		}
	}
	
	$scope.selectMoveAuthor = function (select1, select2) {
		var auth;
		for (var i=select1.length-1; i>=0; i--) {
			angular.forEach($scope.authors, function(value, key){
				if (value.authorId == select1[i]) {
					auth = value;
					var isDuplicate = false;
					angular.forEach($scope.selectedAuthors, function(value2, key2){
						if (value2.authorId == auth.authorId) {
							isDuplicate = true;
						}
					});
					
					if (isDuplicate == false) {
						$scope.selectedAuthors.push(auth);
					}
				}
			});
		}
	}
	
	$scope.removeAuthor = function (selectBox) {
		for (var i=selectBox.length-1; i>=0; i--) {
			var count=0;
			var removeIndex =-1;
			angular.forEach($scope.selectedAuthors, function(value, key){
				if (value.authorId == selectBox[i]) {
					removeIndex = count;
				}
				count++;
			});
			
			if(removeIndex != -1) {
				$scope.selectedAuthors.splice(removeIndex,1);
			}
		}
	}
	
	$scope.cancel = function() {
		editModalWindow.close('close');
	}

	$scope.updateBook = function() {
		pub = {
				publisherId:$scope.selectedPublisher,
				publisherName:" "
		};
		
		var book = {
				title:$scope.bookName,
				bookId:$scope.book.bookId,
				publisher:pub,
				authors:$scope.selectedAuthors,
				genres:$scope.selectedGenres
		};
		
		console.log(book);
		
		if ($scope.bookName == null) {
			alert("Please enter book name");
			if ($scope.selectedPublisher == null) {
				alert("Please select pulisher");	
				if ($scope.selectedAuthors.length == 0) {
					alert("Please select author");	
				}
			}
		}
		else {
			if ($scope.selectedPublisher != null) {
				if ($scope.selectedAuthors.length > 0) {
					$http.post("editBook",book).
					success(function(data) {
						alert("Success");	
						editModalWindow.close('close');
					});
				}
			}
		}
		
		
		
	}

}]);


lmsModule.controller('deleteBookCtrl', ["$scope", "$http", "$modal","$rootScope", 
                                            function($scope, $http, $modal, $rootScope) {
    	$scope.cancel = function() {
    		deleteModalWindow.close('close');
    	};
    	
    	$scope.deleteBook = function() {
    		$http.post("deleteBook",$rootScope.book).
    		success(function(data) {
    			alert("Success");	
    			deleteModalWindow.close('close');
    		});
    	}
    	
}]);

