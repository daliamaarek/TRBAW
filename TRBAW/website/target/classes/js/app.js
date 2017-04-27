angular.module('routingApp',['ngRoute'])
.config(function($routeProvider)
{
    $routeProvider.when('/tweets', {
        controller: "tweetCtrl",
        templateUrl: "resources/templates/tweet.html"
    });
});
