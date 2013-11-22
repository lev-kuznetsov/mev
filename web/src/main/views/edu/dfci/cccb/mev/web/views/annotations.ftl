<b>hello cruel world </b>
<table>
HEY!
<#list workspace.list() as heatmap_name>
	<#assign heatmap=workspace.get(heatmap_name) >
		
	<#assign columnDimension=heatmap.columns() >
	<tr>
		<td>${heatmap.name()}</td>
		<td><a href="${heatmap.name()}/annotation/0/">column annotations</a></td>
		<#list columnDimension.keys() as columnKey>
			<td>${columnKey}</td>	
		</#list>
	</tr>
		
	<#list columnDimension.selections().list() as selection_name>		
		<#assign selection=columnDimension.selections().get(selection_name) >
		<tr>
		<td>${selection_name}:${selection.name()}</td>
		<td>
		<#list selection.keys() as columnInSet>
			${columnInSet},
		</#list>
		</td>
		</tr>
	</#list>
</#list>

</table>
