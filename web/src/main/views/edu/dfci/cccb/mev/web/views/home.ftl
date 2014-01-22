<html>
  <head>
    <title>MeV</title>

    <script type="text/javascript" src="/library/webjars/log4javascript/1.4.5/log4javascript_uncompressed.js"></script>
    <script type="text/javascript">
      var log = log4javascript.getDefaultLogger ();
    </script>

    <link rel="apple-touch-icon" sizes="57x57" href="/container/images/favicon/apple-touch-icon-57x57.png">
    <link rel="apple-touch-icon" sizes="114x114" href="/container/images/favicon/apple-touch-icon-114x114.png">
    <link rel="apple-touch-icon" sizes="72x72" href="/container/images/favicon/apple-touch-icon-72x72.png">
    <link rel="apple-touch-icon" sizes="144x144" href="/container/images/favicon/apple-touch-icon-144x144.png">
    <link rel="apple-touch-icon" sizes="60x60" href="/container/images/favicon/apple-touch-icon-60x60.png">
    <link rel="apple-touch-icon" sizes="120x120" href="/container/images/favicon/apple-touch-icon-120x120.png">
    <link rel="apple-touch-icon" sizes="76x76" href="/container/images/favicon/apple-touch-icon-76x76.png">
    <link rel="apple-touch-icon" sizes="152x152" href="/container/images/favicon/apple-touch-icon-152x152.png">
    <link rel="icon" type="image/png" href="/container/images/favicon/favicon-196x196.png" sizes="196x196">
    <link rel="icon" type="image/png" href="/container/images/favicon/favicon-160x160.png" sizes="160x160">
    <link rel="icon" type="image/png" href="/container/images/favicon/favicon-96x96.png" sizes="96x96">
    <link rel="icon" type="image/png" href="/container/images/favicon/favicon-32x32.png" sizes="32x32">
    <link rel="icon" type="image/png" href="/container/images/favicon/favicon-16x16.png" sizes="16x16">
    <meta name="msapplication-TileColor" content="#ffffff">
    <meta name="msapplication-TileImage" content="/container/images/mstile-144x144.png">

    <script type="text/javascript" src="/library/webjars/requirejs/2.1.8/require.js"></script>
    <#list injectors as injector><script src="${injector}"></script></#list>
  </head>
  <body>
   
   <ng-view></ng-view>
  </body>
</html>
