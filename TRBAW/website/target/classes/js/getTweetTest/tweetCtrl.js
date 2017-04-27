/**
 * Created by daliamaarek on 07/05/16.
 */
function tweetCtrl($scope,$http){
    $scope.tweet= "noooo :("
    $http.get("http://localhost:8080/").
        success(function(data){
            $scope.tweet = data;
        })
        .error(function(data, status, headers, config){
            $scope.tweet= status + " "+ data;
        });

}