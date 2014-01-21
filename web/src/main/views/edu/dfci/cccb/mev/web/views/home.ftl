<html>
  <head>
    <script type="text/javascript" src="/library/webjars/log4javascript/1.4.5/log4javascript_uncompressed.js"></script>
    <script type="text/javascript">
      var log = log4javascript.getDefaultLogger ();
    </script>
    <script type="text/javascript" src="/library/webjars/requirejs/2.1.8/require.js"></script>
    <#list injectors as injector><script src="${injector}"></script></#list>
  </head>
  <body>
   
   <ng-view></ng-view>
  </body>
</html>
