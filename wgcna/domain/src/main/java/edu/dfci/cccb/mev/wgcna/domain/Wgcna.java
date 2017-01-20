package edu.dfci.cccb.mev.wgcna.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractAnalysis;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Parameter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Created by antony on 5/27/16.
 */
@Accessors(fluent = true)
@NoArgsConstructor
public class Wgcna extends AbstractAnalysis{

    @NoArgsConstructor
    public static class Edge {
        //{"from":"A1BG","to":"ABCA5","direction":0,"method":"M0039","weight":1}
        private @JsonProperty @Getter String from;
        private @JsonProperty @Getter String to;
        private @JsonProperty @Getter Integer direction;
        private @JsonProperty @Getter String method;
        private @JsonProperty @Getter Double weight;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    public static class Result {
        private @JsonProperty @Getter List<Edge> edges;
    }

    @Accessors(fluent = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Parameters {
        private @JsonProperty @Getter String name;
        private @JsonProperty(required = false) @Getter List<String> sampleList;
        private @JsonProperty(required = false) @Getter List<String> geneList;
        private @JsonProperty @Getter String distMethod;
        private @JsonProperty @Getter Double WeightFilter;
        private @JsonProperty @Getter Integer sizeLimit = 1000;
        private @JsonProperty @Getter Boolean log = true;
    }

    private @JsonProperty @Getter Parameters params;
    private @JsonProperty @Getter Result result;
    public Wgcna(Parameters params, Result result){
        this.name(params.name());
        this.type("wgcna");
        this.params = params;
        this.result = result;
    }
}
