/**
 * Created by daliamaarek on 07/05/16.
 */
//angular.module('routingApp')
//    .controller ('tweetCtrl', function($scope, $http){

function tweetCtrl($scope, $http) {
    $scope.tweet = "noooo :(";

    $http({
        url:"http://localhost:8080/",
        method: "GET"
        }).
        success(function(data){
            $scope.tweet = data;
        })
        .error(function(data, status, headers, config){
            $scope.tweet=  ":'(";
        });

//});
}