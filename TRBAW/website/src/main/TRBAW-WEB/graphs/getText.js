app.controller('getText', ['$scope','$localStorage', 'getBenefits', 'getRisks',   'getNeutral', 
    function getText($scope, $localStorage, getNeutral, getRisks, getBenefits){
        $scope.localStorage = $localStorage;
//         if($localStorage.benefits==null){
//             getBenefits.success(function(benefitData){
//                 $localStorage.benefits = benefitData;
//                 // configBenefits();
//             });

//         }
//         else{
//             // configBenefits();
//         }
//         if($localStorage.risks==null){
//             getRisks.success(function(riskData){
//                 $localStorage.risks= riskData;
//                 // configRisks();
//             });
//         }
//         // else{
//             // configRisks();}
//         if($localStorage.neutrals==null){
//             getNeutral.success(function(neutralData){
//               $localStorage.neutrals = neutralData;
//               // configNeutrals();
//           });
//         }
//         // else {configNeutrals();}

        
// //         function configBenefits(){
// //         $scope.textBenefits ="";    
// //         for (var i = $localStorage.benefits.length - 1; i >= 0; i--) {
// //             $scope.textBenefits = $scope.textBenefits + '<font color="green"><b>' + 
// //             $localStorage.benefits[i].username + " </b></font> " +$localStorage.benefits[i].content + "<br><hr>";
// //         }
// // }
// // function configRisks(){
// // for (var i = $localStorage.risks.length - 1; i >= 0; i--) {
// //             $scope.textRisks =  $scope.textRisks + "<font color='red'><b>"+ $localStorage.risks[i].username + "</b></font>" + 
// //             + " " +$localStorage.risks[i].content;
// //         }
// //     }
// //     function configNeutrals(){
// // for (var i = $localStorage.neutrals.length - 1; i >= 0; i--) {
// //             $scope.textNeutrals = $scope.textNeutrals + "<font color='blue'><b>" + $localStorage.neutrals[i].username +"</b></font>" + 
// //              " " +$localStorage.neutrals[i].content;
// //         }
// //     }
// //      $scope.renderHtml = function(data) {
// //     return $sce.trustAsHtml(data);
// //   };

    }]);
