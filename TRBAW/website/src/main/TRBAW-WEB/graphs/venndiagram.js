  app.controller('venndiagram', ['$scope','$window', 'getTweets', '$localStorage'
, function venndiagram($scope, $window, getTweets, $localStorage){

  $scope.users = $localStorage.users;
  $scope.myConfig =  
  {

    "type": "venn",
    "title": {
      "text": "Users"
    },
    "subtitle": {
      "text": "Classified according to their tweets"
    },
    "legend": {
    "align": "right", //"left", "center", or "right"
    "vertical-align": "top", //"top", "middle", "bottom"
    "marker": {
      "type": "circle",
      "border-color": "black"
    }
  },
  "plot": {
    "value-box": {
      "text": "",
      "font-family": "Georgia",
    }
  },
  "series": $scope.users
}

$window.
zingchart.render({
  id: 'myChart',
  data: $scope.myConfig,
  height: 500,
  width: "100%"
});



}]);
