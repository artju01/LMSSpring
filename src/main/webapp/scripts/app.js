
angular.module('d3', [])
  .factory('d3Service', ['$document', '$q', '$rootScope',
    function($document, $q, $rootScope) {
      var d = $q.defer();
      function onScriptLoad() {
        // Load client in the browser
        $rootScope.$apply(function() { d.resolve(window.d3); });
      }
      // Create a script tag with d3 as the source
      // and call our onScriptLoad callback when it
      // has been loaded
      var scriptTag = $document[0].createElement('script');
      scriptTag.type = 'text/javascript'; 
      scriptTag.async = true;
      scriptTag.src = 'http://d3js.org/d3.v3.min.js';
      scriptTag.onreadystatechange = function () {
        if (this.readyState == 'complete') onScriptLoad();
      }
      scriptTag.onload = onScriptLoad;

      var s = $document[0].getElementsByTagName('body')[0];
      s.appendChild(scriptTag);

      return {
        d3: function() { return d.promise; }
      };
}]);

var lmsModule = angular.module('lmsApp', ['ngRoute', 'ui.bootstrap','d3']);

lmsModule.filter('format', function () {
	   return function (input) {
	      return input+"10";
	   };
	});


var currentPage = 1;

lmsModule.directive('barChart', ['d3Service',"$window","$http", function(d3Service, $window, $http) {
	return {
		restrict: 'EA',
		scope: {},
		link: function(scope, element, attrs) {
			d3Service.d3().then(function(d3) {
				
				var margin = {top: 20, right: 20, bottom: 30, left: 40},
			    width = 960 - margin.left - margin.right,
			    height = 500 - margin.top - margin.bottom;
				
				var svg = d3.select(element[0])
				.append("svg")
				.attr("width", width + margin.left + margin.right)
				.attr("height", height + margin.top + margin.bottom)
				.append("g")
				.attr("transform", "translate(" + margin.left + "," + margin.top + ")")
				.style('width', '100%');

				// Browser onresize event
				
		          window.onresize = function() {
		            scope.$apply();
		          };

		         
		          // Watch for resize event
		          scope.$watch(function() {
		        	  return angular.element($window)[0].innerWidth;
		          }, function() {
		        	  var empty = [];
		        	  scope.render(empty);	  
		        	  $http.post("totalBookForAllBranch").
			          success(function(data) {
			        	  dataset = data;
			        	
			        	 scope.render(data);
			          });
		          });

		          
		      
		          	          		      
		          
		          scope.render = function(data) {
		        	  svg.selectAll('*').remove();
		        	  /*var dataset = [
		        	                 {
		        	                	 name:"Art",
		        	                	 noOfCopies:"80"
		        	                 },
		        	                 {
		        	                	 name:"Js",
		        	                	 noOfCopies:"10"
		        	                 },
		        	                 {
		        	                	 name:"wdJs",
		        	                	 noOfCopies:"30"
		        	                 }			        	 
		        	  ];*/
		        			  
		        	  
		 
		        	  barPadding = 10;
		        	 

		        	  var x = d3.scale.ordinal()
		        	    .rangeRoundBands([0, width], .1);

		        	  var y = d3.scale.linear()
		        	    .range([height, 0]);

		        	  var xAxis = d3.svg.axis()
		        	    .scale(x)
		        	    .orient("bottom");
		        	  
		        	  var yAxis = d3.svg.axis()
		        	    .scale(y)
		        	    .orient("left");
		        	   // .ticks(10, "%");
		        	  
		        	  x.domain(data.map(function(d) { return d.branch.name; }));
		        	  y.domain([0, d3.max(data, function(d) { return d.noOfCopies; })]);
		        	  
		        	  svg.append("g")
		              .attr("class", "x axis")
		              .attr("transform", "translate(0," + height + ")")
		              .call(xAxis)
		              
		        	  
		              svg.append("g")
		              .attr("class", "y axis")		             
		              .call(yAxis)
		              .append("text")
		              .attr("transform", "rotate(-90)")
		              .attr("y", 6)
		              .attr("dy", ".71em") 
		              .style("text-anchor", "end")
		              .text("Total Books");

		        	  svg.selectAll("bar")
		        	  .data(data)		        	   
		        	  .enter()	    		        	  
		        	  .append("rect")			        	 
		        	  .attr("class", "bar")
		        	  .attr("x", function(d, i) {
		        		  return i * (width / data.length);  
		        	  })
		        	  .attr("y", function(d) {		        		  
		        		  return height - (d.noOfCopies*5);          
		        	  })		 		        	 
		        	  .attr("height", function(d) {
		        		  return d.noOfCopies*5;
		        	  })		
		        	  .transition()
		        	  .duration(1000)
		        	  .attr("width", width / data.length - barPadding)		        	 
		        	  .attr("fill", function(d) {
		        		    return "rgb("+(d.noOfCopies*4)+","+(d.noOfCopies*2)+", " + (d.noOfCopies*4) + ")";
		        		});
		        	  
		        	  /*
		        	  svg.selectAll("text")
		        	  .data(data)
		        	  .enter()
		        	  .append("text")
		        	  .attr("transform", "translate(" + margin.left + "," + margin.top + ")")
		        	  .text(function(d) {
		        		  return d;
		        	  })
		        	  .attr("x", function(d, i) {
		        		  return i * (width / data.length) + (width / data.length - barPadding) / 2;
		        	  })
		        	  .attr("y", function(d) {
		        		  return height - (d.noOfCopies * 5) + 14;  //15 is now 14
		        	  })
		        	  .attr("font-family", "sans-serif")
		        	  .attr("font-size", "11px")
		        	  .attr("fill", "black")
		        	  .attr("text-anchor", "middle");*/

		          }
			});
		}};
}]);
	

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
	}).when("/listBookLoans", {
		templateUrl : "listBookLoans.html"
	}).when("/listLibrarianBranches", {
		templateUrl : "listLibrarianBranches.html"
	}).when("/checkOutBook", {
		templateUrl : "checkOutBook.html"
	}).when("/returnBook", {
		templateUrl : "returnBook.html"
	}).when("/visualization", {
		templateUrl : "visualization.html"
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

///////////////////////////////////////////////////////////////////////////////////
///////////////////////////BookLoan////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
lmsModule.controller('listBookLoansCtrl', ["$scope", "$http", "$modal", "$rootScope", function($scope, $http, $modal, $rootScope) {
	$http.post("countBookloans").
	success(function(data) {
		var range = [];
		var maxPage = Math.ceil(data/10);
		for(var i=1;(i<=maxPage);i++) {
			range.push(i);
		}
		$scope.range = range;

		$http.post("listBookLoans/1").
		success(function(data) {
			$scope.bookLoans = data;				
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
		$http.post("listBookLoans/"+currentPage).
		success(function(data) {
			$scope.borrowers = data;
		});	
	}

	$scope.editBookLoan = function(bl) {
		$rootScope.bookLoan = bl;
		editModalWindow = $modal.open({
			templateUrl: "editBookLoansModal.html",
			controller: "editBookLoanCtrl"
		});

	}

}]);


lmsModule.controller('editBookLoanCtrl', ["$scope", "$http", "$modal","$rootScope", 
                                           function($scope, $http, $modal, $rootScope) {
	$scope.dt = $rootScope.bookLoan.dueDate;
	
	$scope.toggleMin = function() {
		$scope.minDate = $scope.minDate ? null : new Date();
	};
	$scope.toggleMin();

	$scope.maxDate = new Date(2020, 5, 22);
	
   	$scope.cancel = function() {
   		editModalWindow.close('close');
   	};
   	
   	
   	$scope.save = function () {
   		$rootScope.bookLoan.dueDate = $scope.dt;
   		$http.post("editBookLoan",$rootScope.bookLoan).
   		success(function(data) {
   			alert("Success");	
   			editModalWindow.close('close');
   		});
   	}
   	
}]);


///////////////////////////////////////////////////////////////////////////////////
///////////////////////////LIBRARIAN BRANCHES//////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
lmsModule.controller('listLibrarianBranchesCtrl', ["$scope", "$http", "$modal", "$rootScope",
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

	$scope.editBranch = function(branch) {
		$rootScope.branch = branch;
		editModalWindow = $modal.open({
			templateUrl: "editBranchModal.html",
			controller: "editBranchCtrl"
		});

	}
	
	$scope.manageBranch = function(branch) {
		$rootScope.branch = branch;
		editModalWindow = $modal.open({
			templateUrl: "editBookCopiesModal.html",
			controller: "editBookCopiesCtrl"
		});

	}
	
}]);


lmsModule.controller('editBookCopiesCtrl', ["$scope", "$http", "$modal","$rootScope", 
                                        function($scope, $http, $modal, $rootScope) {
	$http.post("listBookCopies/"+$rootScope.branch.branchId).
	success(function(data) {
		$scope.bookCopies = data;				
	});
	
	$scope.reloadData = function() {
		$http.post("listBookCopies/"+$rootScope.branch.branchId).
		success(function(data) {
			$scope.bookCopies = data;				
		});
	}
	
	$http.post("listBooks/-1").
	success(function(data) {
		$scope.books = data;				
	});
	
	$scope.addBookCopy = function() {
		var checker = false;
		angular.forEach($scope.bookCopies, function(value, key){
			if ($scope.selectedBook == value.book.bookId) {
				alert("Already have this record !!")
				checker = true;
			} 
		});
		
		if (checker == false) {
			var newBookCopy = {
					branch:$rootScope.branch,
					book:{
						bookId:$scope.selectedBook
					},
					noOfCopies:$scope.newNoOfCopies
			}
			$http.post("addBookCopy",newBookCopy).
	   		success(function(data) {
	   			alert("Success");	
	   			$scope.reloadData();
	   		});
		}
		
	}
	
	$scope.updateBookCopy = function(copy) {
		$http.post("updateBookCopy", copy).
   		success(function(data) {
   			alert("Success");	
   			editModalWindow.close('close');
   		});
	};
	
	$scope.cancel = function() {
		editModalWindow.close('close');
	};
}]);

///////////////////////////////////////////////////////////////////////////////////
///////////////////////////Borrower////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
lmsModule.controller('checkOutCtrl', ["$scope", "$http", "$modal", "$rootScope",
                                      function($scope, $http, $modal, $rootScope) {
	$http.post("listBorrowers/-1").
	success(function(data) {
		$scope.borrowers = data;
	});

	$http.post("listBranches/-1").
	success(function(data) {
		$scope.branches = data;
	});

	$scope.changedValue = function(branchId){
		$http.post("listBookCopies/"+branchId).
		success(function(data) {
			$scope.bookCopies = data;				
		});
	}       

	$scope.checkOut = function(copy){
		if (copy.noOfCopies <= 0) {
			alert("Book Not Available !!");
		}
		else {
			var borrower = {
					cardNo: $scope.selectedBorrower
			};

			$http.post("listBookLoansForBorrower",borrower).
			success(function(loanData) {
				var checker  = false;
				angular.forEach(loanData, function(value, key){
					if(value.book.bookId == copy.book.bookId) {
						if (value.dateIn == null) {
							checker = true;
							alert("You already check out this book !!");
						}
					}			
				});

				if (checker == false) {
					//save 
					var today = new Date();
					var dueDate = new Date(today.getTime() + 14 * 24 * 60 * 60 * 1000);
					var bookloanData = {
							borrower: {
								cardNo:$scope.selectedBorrower
							},
							book:{
								bookId:copy.book.bookId
							},
							branch:{
								branchId:$scope.selectedBranch
							},
							dateOut:today,
							dueDate:dueDate
					}
					$http.post("addBookLoan",bookloanData).
					success(function(data) {
						alert("success");	
						$scope.changedValue($scope.selectedBranch);
					});
				}
			});
		}
	}
}]);

lmsModule.controller('returnCtrl', ["$scope", "$http", "$modal", "$rootScope",
                                    function($scope, $http, $modal, $rootScope) {
	$http.post("listBorrowers/-1").
	success(function(data) {
		$scope.borrowers = data;
	});


	$scope.changedValue = function(cardNo){
		var borrower = {
				cardNo:cardNo
		}
		$http.post("listBookLoansNotReturnForBorrower/",borrower).
		success(function(data) {
			console.log(data);
			$scope.bookLoans = data;				
		});
	}       

	$scope.returnBookLoan = function(loan){
		$http.post("returnBookLoan/",loan).
		success(function(data) {
			$scope.changedValue(loan.borrower.cardNo);				
		});
	}
}]);

///////////////////////////////////////////////////////////////////////////////////
///////////////////////////Visualization////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////

lmsModule.controller('visualizationCtrl', ["$scope", "$http", "$modal", function($scope, $http, $modal) {
	
}]);