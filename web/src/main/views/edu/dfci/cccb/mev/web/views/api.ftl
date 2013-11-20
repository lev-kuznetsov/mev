<html>
  <body>
    <table border="1">
      <thead>
        <tr>
          <th>method</th>
          <th>url</th>
          <th>parameters</th>
          <th>handler</th>
          <th>returns</th>
          <th>throws</th>
        </tr>
      </thead>
      <#list reflection.services() as service><tr>
        <td><#list service.methods() as method>${method}<br/></#list></td>
        <td><#list service.urls() as url>${url}<br/></#list></td>
        <td><#list service.parameters() as parameter>${parameter.type().simpleName} ${parameter.name()}<br/></#list></td>
        <td>${service.handler().declaringClass.simpleName}.${service.handler().name}</td>
        <td>${service.handler().returnType.simpleName}</td>
        <td><#list service.throwing() as throwing>${throwing.simpleName}<br/></#list></td>
      </tr></#list>
    </table>
  </body>
</html>