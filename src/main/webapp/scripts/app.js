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
	    	  	} else if (scope.searchtype == "Branches") {
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
			for(var i=1;(i<=data/10);i++) {
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
///////////////////////////Book////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////

lmsModule.controller('listBooksCtrl', ["$scope", "$http", "$modal", function($scope, $http, $modal) {
	$http.post("countBooks").
		success(function(data) {
			var range = [];
			for(var i=1;(i<=data/10);i++) {
			  range.push(i);
			}
			$scope.range = range;

			$http.post("listBooks/1").
				success(function(data) {
					$scope.books = data;					
				});
		});
	
	$scope.pageBook = function(pgNo) {
		$http({
		    method: "POST",
		    url: "listBooks/"+pgNo
		}).success(function(data) {
				$scope.books = data;
			});
	}
	
}]);

lmsModule.controller('listPublishersCtrl', ["$scope", "$http", "$modal", function($scope, $http, $modal) {
	$http.post("countPublishers").
		success(function(data) {
			var range = [];
			for(var i=1;(i<=data/10);i++) {
			  range.push(i);
			}
			$scope.range = range;

			$http.post("listPublishers/1").
				success(function(data) {
					$scope.publishers = data;				
				});
		});
	
	$scope.pageBook = function(pgNo) {
		$http({
		    method: "POST",
		    url: "listPublishers/"+pgNo
		}).success(function(data) {
				$scope.publishers = data;
			});
	}
	
}]);


