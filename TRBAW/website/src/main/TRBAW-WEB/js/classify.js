app.factory('classify',['$http', function($http)

{	
        return $http.get("http://localhost:8080/class")
        .success(function(data){
            return data;
        })
        .error(function(data, status, headers, config){
            data = "Internet Connection has been interrupted";
            return data;
        });

}]);