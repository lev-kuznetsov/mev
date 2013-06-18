<!doctype html>
<html lang="en" ng-app>
<head>
  <meta charset="utf-8">
  <title>My HTML File</title>
  <!--link rel="stylesheet" href="css/app.css">
  <link rel="stylesheet" href="css/bootstrap.css"-->
  <script src="/resources/webjars/angularjs/1.1.4/angular.js"></script>
  <script type="text/javascript">
  function PhoneListCtrl($scope) {
  $scope.phones = [
    {"name": "Nexus S",
     "snippet": "Fast just got faster with Nexus S."},
    {"name": "Motorola XOOM™ with Wi-Fi",
     "snippet": "The Next, Next Generation tablet."},
    {"name": "MOTOROLA XOOM™",
     "snippet": "The Next, Next Generation tablet."}
  ];
}
  </script>
</head>
<body>
 
  <p>Nothing here {{'yet' + '!'}}</p>
  <p>1 + 2 = {{ 1 + 2 }}</p>
 
</body>
</html>