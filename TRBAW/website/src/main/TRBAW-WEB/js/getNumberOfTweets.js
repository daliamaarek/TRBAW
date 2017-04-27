app.factory('getNumberOfTweets',['$http', function($http)

{

        return $http.get("http://localhost:8080/Numbers")
        .success(function(data){
            return data;
        })
        .error(function(data, status, headers, config){
            return data;
        });

}]);