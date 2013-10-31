<html>
  <body>
    <table border="1">
      <thead>
        <tr>
          <th>methods</th><th>urls</th><th>parameters</th><th>handlers</th>
        </tr>
      </thead>
      <#list descriptors as descriptor><tr>
        <td><#list descriptor.methods as method>${method}<br/></#list></td><td><#list descriptor.urls as url>${url}<br/></#list></td><td><#list descriptor.parameters as parameter>${parameter.type.simpleName} ${parameter.parameter}<br/></#list></td><td><#list descriptor.handlers as handler>${handler.declaringClass.simpleName}.${handler.name}<br/></#list></td>
      </tr></#list>
    </table>
  </body>
</html>