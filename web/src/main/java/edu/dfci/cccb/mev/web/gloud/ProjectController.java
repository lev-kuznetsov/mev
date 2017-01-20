package edu.dfci.cccb.mev.web.gloud;

import static com.google.cloud.storage.Storage.BucketField.ACL;
import static com.google.cloud.storage.Storage.BucketField.ID;
import static com.google.cloud.storage.Storage.BucketField.NAME;
import static com.google.cloud.storage.Storage.BucketListOption.fields;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Provider;

import org.springframework.context.annotation.Scope;
import org.springframework.social.google.api.Google;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.Page;
import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Acl.Entity;
import com.google.cloud.storage.Acl.Entity.Type;
import com.google.cloud.storage.Acl.User;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;

import edu.dfci.cccb.mev.dataset.domain.contract.DatasetBuilder;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetException;
import edu.dfci.cccb.mev.dataset.domain.contract.RawInput;
import edu.dfci.cccb.mev.dataset.domain.contract.Workspace;

@RestController
public class ProjectController {

  private @Inject Storage storage;
  private @Inject ObjectMapper mapper;
  private @Inject Provider<Google> google;
  private @Inject Workspace workspace;
  private @Inject DatasetBuilder dsb;

  @RequestMapping (value = "/cccb-projects", method = GET)
  public List<Project> projects () throws JsonParseException, JsonMappingException, IOException {
    List<Project> projects = new ArrayList<> ();
    Set<String> emails = google.get ().plusOperations ().getGoogleProfile ().getEmailAddresses ();

    for (Page<Bucket> p = storage.list (fields (ID, ACL, NAME)).getNextPage (); p != null;)
      for (Bucket b : p.getValues ())
        for (Acl a : b.getAcl ()) {
          Entity e = a.getEntity ();
          if (e.getType () == Type.USER)
            if (emails.contains (((User) e).getEmail ()))
              projects.add (mapper.readValue (b.get ("mev.json").getContent (), Project.class)
                                  .bucket (b.getName ()));
        }

    return projects;
  }

  @RequestMapping (value = "/cccb-projects", method = POST)
  public void load (final @RequestBody List<Project> projects) throws DatasetException, IOException {
    List<String> eligible = new ArrayList<> ();
    for (Project mine : projects ())
      eligible.add (mine.bucket ());
    for (final Project project : projects)
      if (eligible.contains (project.bucket ()))
        for (final File file : project.files ())
          workspace.put (dsb.build (new RawInput () {
            private final long sz;
            private final InputStream ct;
            private String name = file.description ();

            {
              byte[] bt = storage.get (project.bucket (), file.path ()).getContent ();
              sz = bt.length;
              ct = new ByteArrayInputStream (bt);
            }

            public long size () {
              return sz;
            }

            @Override
            public RawInput name (String name) {
              this.name = name;
              return this;
            }

            @Override
            public String name () {
              return name;
            }

            @Override
            public InputStream input () throws IOException {
              return ct;
            }

            @Override
            public String contentType () {
              return null;
            }
          }));
  }
}
