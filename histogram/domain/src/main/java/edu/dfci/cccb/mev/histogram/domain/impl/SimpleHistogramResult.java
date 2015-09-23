package edu.dfci.cccb.mev.histogram.domain.impl;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.dfci.cccb.mev.histogram.domain.contract.HistogramResult;

//sample result
/*
{
  "breaks":[0,20,40,60,80,100,120,140,160,180,200,220,240,260,280,300,320,340,360,380,400,420,440,460],
  "counts":[47125,19715,19516,19575,19410,21335,23067,19363,19165,20184,19827,19418,20799,19650,19510,19669,19995,19568,19651,18641,12735,3143,55],
  "density":[0.0053,0.0022,0.0022,0.0022,0.0022,0.0024,0.0026,0.0022,0.0022,0.0023,0.0022,0.0022,0.0024,0.0022,0.0022,0.0022,0.0023,0.0022,0.0022,0.0021,0.0014,0.0004,6.2342e-06],
  "mids":[10,30,50,70,90,110,130,150,170,190,210,230,250,270,290,310,330,350,370,390,410,430,450]
} 
*/

@ToString
@NoArgsConstructor
@Accessors(fluent=true)
public class SimpleHistogramResult implements HistogramResult {
  
  private @Getter @JsonProperty  List<Integer> breaks;
  private @Getter @JsonProperty  List<Integer> counts;
  private @Getter @JsonProperty  List<Double> density;
  private @Getter @JsonProperty  List<Double> mids;  
}
