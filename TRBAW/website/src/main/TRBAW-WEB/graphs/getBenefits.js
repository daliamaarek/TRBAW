app.factory('getBenefits',['$http', function($http)

{

        return $http.get("http://localhost:8080/benefits")
        .success(function(benefitData){
            return benefitData;
        })
        .error(function(data, status, headers, config){
            return data;
        });

}]);

