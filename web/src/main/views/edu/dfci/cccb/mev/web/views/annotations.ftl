<b>hello cruel world </b>
<table>
HEY!444

RoundingMode: ${RoundingMode.UP}<br>
enums["java.math.RoundingMode"].UP: ${enums["java.math.RoundingMode"].UP}<br>
DimensionType: ${Dimension$Type.COLUMN}<br>
enums["edu.dfci.cccb.mev.dataset.domain.contract.Dimension$Type"].COLUMN: ${enums["edu.dfci.cccb.mev.dataset.domain.contract.Dimension$Type"].COLUMN}<br>

<#list workspace.list() as heatmap_name>
	<#assign heatmap=workspace.get(heatmap_name) >
		
	<#assign columnDimension=heatmap.dimension(enums["edu.dfci.cccb.mev.dataset.domain.contract.Dimension$Type"].COLUMN) >
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
