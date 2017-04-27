app.factory('getNeutral',['$http', function($http)

{  
        return $http.get("http://localhost:8080/neutral")
        .success(function(neutralData){
            return neutralData;
        })
        .error(function(data, status, headers, config){
            return data;
        });

}]);
