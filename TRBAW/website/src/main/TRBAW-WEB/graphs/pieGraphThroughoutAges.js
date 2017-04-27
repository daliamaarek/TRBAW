// <script src="https://www.amcharts.com/lib/3/amcharts.js"></script>
// <script src="https://www.amcharts.com/lib/3/pie.js"></script>
// <script src="https://www.amcharts.com/lib/3/plugins/animate/animate.min.js"></script>
// <script src="https://www.amcharts.com/lib/3/themes/light.js"></script>
// <div id="chartdiv"></div>
/**
 * Define data for each year
 */
app.controller('pieGraphThroughoutAges', ['$scope', '$window', 'Years' , '$localStorage', function pieGraphThroughoutAges($scope, $window, Years, $localStorage){

$scope.currentYear = 2015;
$scope.chart = AmCharts.makeChart( "chartdiv", {
  "type": "pie",
  "theme": "light",
  "dataProvider": [],
  "valueField": "size",
  "titleField": "sector",
  "startDuration": 0,
  "innerRadius": 80,
  "pullOutRadius": 20,
  "marginTop": 30,
  "titles": [{
    "text": "Tweet Classification Throughout the Years"
  }],
  "allLabels": [{
    "y": "54%",
    "align": "center",
    "size": 25,
    "bold": true,
    "text": "2000",
    "color": "#555"
  }, {
    "y": "49%",
    "align": "center",
    "size": 15,
    "text": "Year",
    "color": "#555"
  }],
  "listeners": [ {
    "event": "init",
    "method": function( e ) {
      $scope.chart = e.chart;
      
      function getCurrentData() {
        $scope.data = $localStorage.pieData[$scope.currentYear];
        $scope.currentYear++;
        if ($scope.currentYear > 2016)
          $scope.currentYear = 2015;
        return $scope.data;
      }
      
      function loop() {
        $scope.chart.allLabels[0].text = $scope.currentYear;
        $scope.data = getCurrentData();
        $scope.chart.animateData( $scope.data, {
          duration: 1000,
          complete: function() {
            setTimeout( loop, 3000 );
          }
        } );
      }

      loop();
    }
  } ],
   "export": {
   "enabled": true
  }
});
  

}]);