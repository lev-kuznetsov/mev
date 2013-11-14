<b>hello cruel world </b>

<table>

<#list workspace.list() as heatmap_name>
	<#assign heatmap=workspace.get(heatmap_name) >
	<#assign columnHeader=heatmap.columnHeader() >

	<tr>
		<td>${heatmap.name()}</td>
		<td><a href="${heatmap.name()}/annotation/0/">column annotations</a></td>
		<#list columnHeader.annotation.keys as columnKey>
		<td>${columnKey}</td>	
		</#list>
	</tr>
	<#list columnHeader.keysets?keys as key>	
	<tr>
	<td>${key}</td>
	<td>
	<#assign dimensionSet=columnHeader.keysets[key] > 
	<#list dimensionSet as columnInSet>
		${columnInSet},
	</#list>
	</td>
	</tr>
	</#list>
</#list>

</table>
