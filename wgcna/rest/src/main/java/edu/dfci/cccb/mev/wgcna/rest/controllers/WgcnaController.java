package edu.dfci.cccb.mev.wgcna.rest.controllers;

import edu.dfci.cccb.mev.dataset.domain.contract.Analysis;
import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetException;
import edu.dfci.cccb.mev.dataset.rest.resolvers.DatasetPathVariableMethodArgumentResolver;
import edu.dfci.cccb.mev.wgcna.domain.Wgcna;
import edu.dfci.cccb.mev.wgcna.domain.WgcnaBuilder;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.inject.Provider;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;
import static org.springframework.web.context.WebApplicationContext.SCOPE_REQUEST;

/**
 * Created by antony on 5/27/16.
 */

@RestController
@RequestMapping ("/dataset/" + DatasetPathVariableMethodArgumentResolver.DATASET_URL_ELEMENT)
@Scope(SCOPE_REQUEST)
public class WgcnaController {

    private @Inject Dataset dataset;
    private @Inject Provider<WgcnaBuilder> builder;

    @RequestMapping (value = "/analyze/wgcna/{name}", method = PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    public Analysis put (final @PathVariable("name") String name, @RequestBody Wgcna.Parameters dto) throws DatasetException {
        return builder.get ().dataset (dataset).parameters(dto) .buildAsync ();
    }
}
