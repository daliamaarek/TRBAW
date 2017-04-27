app.factory('Dates',['$http', function($http)

{

        return $http.get("http://localhost:8080/Dates")
        .success(function(data){
            return data;
        })
        .error(function(data, status, headers, config){
            return data;
        });

}]);

