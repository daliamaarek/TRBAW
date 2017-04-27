app.factory('Years',['$http', function($http)

{
        return $http.get("http://localhost:8080/Year")
        .success(function(yeardata){
            return yeardata;
        })
        .error(function(data, status, headers, config){
            return data;
        });

}]);

