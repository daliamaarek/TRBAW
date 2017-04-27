app.controller('classifyTweets', ['$scope', '$window', 'Dates' , '$localStorage', 'Years', 
	'getBenefits', 'getRisks', 'getNeutral','getTweets', 'getNumberOfTweets',
	'classify',

	function classifyTweets($scope, $window, Dates, $localStorage, Years, getBenefits, getRisks, getNeutral, getTweets, getNumberOfTweets
		, classify
		){
		$scope.message = "This may take several minutes, depending on your internet connection";
	
		classify.success(function(data){
			loadUsers();
			
		});
		classify.error(function(data, status, headers, config){
			$scope.message = "Internet Connection Interruped, please restart server";
		})
			function loadUsers(){
			Dates.success(function(data){
				$localStorage.dates = data;
				// classify();
				loadUsers();
			});
		}
		function loadUsers(){
				getTweets.success(function(userdata){
					$localStorage.users = userdata;
					for (var i =  $localStorage.users.length - 1; i >= 0; i--) {
						$localStorage.users[i].borderColor="black";
						$localStorage.users[i].borderWidth=2;
					}
					loadPie();
				});
			}
		function loadPie(){
				Years.success(function(data){
					$localStorage.pieData = data;
					loadBenefits();
				});
			}
			
		function loadBenefits(){
				getBenefits.success(function(benefitData){
					$localStorage.benefits = benefitData;
					loadRisks();
				});
			}

		function loadRisks(){
				getRisks.success(function(riskData){
					$localStorage.risks= riskData;
					loadNeutrals();
				});
			}

		function loadNeutrals(){
				getNeutral.success(function(neutralData){
					$localStorage.neutrals = neutralData;
					loadNumbers();
				});
			}

		function loadNumbers(){
				getNumberOfTweets.success(function(numbers){
					$localStorage.numbers = numbers;
					$window.location.href = '../TRBAW-WEB/time.html'
				});
			}

	}]);
