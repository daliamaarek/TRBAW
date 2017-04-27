app.controller('LoadEverything', ['$scope', '$window', 'Dates' , '$localStorage', 'Years', 
	'getBenefits', 'getRisks', 'getNeutral','getTweets', 'getNumberOfTweets',
	// 'classify',

	function LoadEverything($scope, $window, Dates, $localStorage, Years, getBenefits, getRisks, getNeutral, getTweets, getNumberOfTweets
		// , classify
		){
		

		if($localStorage.dates == null){
			Dates.success(function(data){
				$localStorage.dates = data;
				loadUsers();		
				// classify();
			});
		}
		else{
			// classify();
			loadUsers();
		}
		// function classify(){
		// 	// classify.success(function(data){
		// 	// 		loadUsers();
		// 	// 	});
		// 	loadUsers();
		// }
		function loadUsers(){
			if($localStorage.users==null){
				getTweets.success(function(userdata){
					$localStorage.users = userdata;
					for (var i =  $localStorage.users.length - 1; i >= 0; i--) {
						$localStorage.users[i].borderColor="black";
						$localStorage.users[i].borderWidth=2;
					}
					loadPie();
				});
			}
			else{
				loadPie();
			}
		}
		function loadPie(){
			if($localStorage.pieData == null){
				Years.success(function(data){
					$localStorage.pieData = data;
					loadBenefits();
				});
			}
			else{
				loadBenefits();
			}
		}
		function loadBenefits(){
			if($localStorage.benefits==null){
				getBenefits.success(function(benefitData){
					$localStorage.benefits = benefitData;
					loadRisks();
				});
			}
			else{
				loadRisks();
			}
		}
		function loadRisks(){
			if($localStorage.risks==null){
				getRisks.success(function(riskData){
					$localStorage.risks= riskData;
					loadNeutrals();
				});
			}
			else{
				loadNeutrals();
			}
		}
		function loadNeutrals(){
			if($localStorage.neutrals==null){
				getNeutral.success(function(neutralData){
					$localStorage.neutrals = neutralData;
					loadNumbers();
				});
			}
			else{
				loadNumbers();
			}
		}
		function loadNumbers(){
			if($localStorage.numbers==null){
				getNumberOfTweets.success(function(numbers){
					$localStorage.numbers = numbers;
					$window.location.href = '../TRBAW-WEB/time.html'
				});
			}
			else{
				redirect();
			}
		}
		function redirect(){
			$window.setTimeout(function(){ 
			$window.location.href = '../TRBAW-WEB/time.html';
		},3000);
		}
	}]);
