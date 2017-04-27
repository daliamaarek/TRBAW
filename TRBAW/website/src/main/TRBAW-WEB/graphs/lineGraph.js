app.controller('lineGraph', ['$scope', '$window', 'Dates' , '$localStorage',

  function lineGraph($scope, $window, Dates, $localStorage){


    AmCharts.makeChart("chartdivLine",
    {
      type: "serial",
      categoryField: "date",
      dataDateFormat: "YYYY-MM-DD",
      handDrawScatter: 1,
      categoryAxis: {
        parseDates: true
      },
      chartCursor: {
        enabled: true
      },
      chartScrollbar: {
        enabled: true
      },
      trendLines: [],
      graphs: [
      {
        bullet: "round",
        id: "AmGraph-1",
        title: "Risks",
        valueField: "risks",
        lineColor: "#DE5656"
      },
      {
        bullet: "circle",
        id: "AmGraph-2",
        title: "Benefits",
        valueField: "benefits",
        lineColor: "#9DBE60"
      },
      {
        bullet: "circle",
        id: "AmGraph-3",
        title: "Neutral",
        valueField: "neutral",
        lineColor:"#5EB9D9"
      }
      ],
      guides: [],
      valueAxes: [
      {
        id: "ValueAxis-1",
        title: "Number of Tweets"
      }
      ],
      allLabels: [],
      balloon: {},
      legend: {
        enabled: true,
        useGraphSettings: true
      },
      titles: [
      {
        id: "Title-1",
        size: 15,
        text: "Number of Tweets per Day (Classified)"
      }
      ],
      dataProvider : $localStorage.dates
                  });
    
  }]);