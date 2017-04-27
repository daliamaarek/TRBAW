app.factory('getRisks',['$http', function($http)

{   
        return $http.get("http://localhost:8080/risks")
        .success(function(riskData){
            return riskData;
        })
        .error(function(data, status, headers, config){
            return data;
        });
}]);
