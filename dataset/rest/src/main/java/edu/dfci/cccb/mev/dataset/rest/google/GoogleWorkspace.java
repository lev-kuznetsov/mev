/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.dfci.cccb.mev.dataset.rest.google;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.social.google.api.Google;
import org.springframework.social.google.api.drive.DriveFile;
import org.springframework.social.google.api.drive.UploadParameters;

import edu.dfci.cccb.mev.dataset.domain.contract.Analyses;
import edu.dfci.cccb.mev.dataset.domain.contract.Analysis;
import edu.dfci.cccb.mev.dataset.domain.contract.AnalysisNotFoundException;
import edu.dfci.cccb.mev.dataset.domain.contract.ComposerFactory;
import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetBuilder;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetBuilderException;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetComposingException;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetNotFoundException;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension.Type;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidCoordinateException;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDatasetNameException;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDimensionTypeException;
import edu.dfci.cccb.mev.dataset.domain.contract.SelectionNotFoundException;
import edu.dfci.cccb.mev.dataset.domain.contract.Values;
import edu.dfci.cccb.mev.dataset.domain.contract.Workspace;
import edu.dfci.cccb.mev.dataset.domain.mock.MockTsvInput;
import edu.dfci.cccb.mev.dataset.domain.simple.ArrayListWorkspace;
import edu.dfci.cccb.mev.io.implementation.TemporaryFile;

/**
 * @author levk
 * 
 */
@Log4j
public class GoogleWorkspace implements Workspace {
  private final Workspace workspaceDelegate = new ArrayListWorkspace ();

  private static HashMap<String, HashMap<String, HashMap<String, Class<? extends Analysis>>>> SESSIONS =
                                                                                                         new HashMap<> ();

  private @Inject Provider<DatasetBuilder> builder;
  private @Inject ComposerFactory composer;

  private Google google;

  @Inject
  private void setUp (Provider<Google> google) throws IOException {
    this.google = google.get ();
    pull ();
  }

  private HashMap<String, HashMap<String, Class<? extends Analysis>>> session;

  @SneakyThrows ({ DatasetBuilderException.class, InvalidDatasetNameException.class })
  private void pull () throws IOException {
    String email = google.plusOperations ().getGoogleProfile ().getAccountEmail ();
    session = SESSIONS.get (email);
    if (session == null)
      SESSIONS.put (email, session = new HashMap<> ());
    for (String id : session.keySet ())
      load (id);
  }

  @SneakyThrows ({
                  InvalidDimensionTypeException.class, InvocationTargetException.class, IllegalAccessException.class,
                  NoSuchMethodException.class })
  public void load (final String id) throws IOException, InvalidDatasetNameException, DatasetBuilderException {
    try (TemporaryFile file = new TemporaryFile ()) {
      try (FileOutputStream copy = new FileOutputStream (file)) {
        IOUtils.copy (google.driveOperations ().downloadFile (id).getInputStream (), copy);
      } catch (Exception e) {
        log.warn ("Failure downloading file", e);
        return;
      }
      workspaceDelegate.put (new Dataset () {
        private final Dataset delegate;
        private final Analyses analyses;

        {
          delegate = builder.get ().build (new MockTsvInput (google.driveOperations ()
                                                                   .getFile (id)
                                                                   .getTitle (), file));
          final Analyses analysesDelegate = this.delegate.analyses ();
          if (!session.containsKey (id)) {
            session.put (id, new HashMap<String, Class<? extends Analysis>> ());
          } else {
            for (Map.Entry<String, Class<? extends Analysis>> analysisId : session.get (id).entrySet ())
              analysesDelegate.put ((Analysis) analysisId.getValue ()
                                                         .getMethod ("from", InputStream.class)
                                                         .invoke (null,
                                                                  google.driveOperations ()
                                                                        .downloadFile (analysisId.getKey ())
                                                                        .getInputStream ()));
          }

          analyses = new Analyses () {

            @Override
            public void remove (String name) throws AnalysisNotFoundException {
              analysesDelegate.remove (name);
            }

            @Override
            @SneakyThrows ({
                            FileNotFoundException.class, IllegalAccessException.class, IOException.class,
                            InvocationTargetException.class })
            public void put (Analysis analysis) {
              File tmp = File.createTempFile ("mev-", ".analysis");
              try (OutputStream out = new BufferedOutputStream (new FileOutputStream (tmp))) {
                analysis.getClass ().getMethod ("to", OutputStream.class).invoke (analysis, out);
                session.get (id).put (google.driveOperations ()
                                            .upload (new FileSystemResource (tmp),
                                                     DriveFile.builder ().setTitle (analysis.name ()).build (),
                                                     new UploadParameters ())
                                            .getId (),
                                      analysis.getClass ());
              } catch (NoSuchMethodException e) {
                log.warn ("Unable to save analysis" + analysis, e);
              } finally {
                tmp.delete ();
              }
              analysesDelegate.put (analysis);
            }

            @Override
            public List<String> list () {
              return analysesDelegate.list ();
            }

            @Override
            public Analysis get (String name) throws AnalysisNotFoundException {
              return analysesDelegate.get (name);
            }
          };
        }

        @Override
        public String name () {
          return delegate.name ();
        }

        @Override
        public Values values () {
          return delegate.values ();
        }

        @Override
        public Dimension dimension (Type type) throws InvalidDimensionTypeException {
          return delegate.dimension (type);
        }

        @Override
        public void set (Dimension dimension) throws InvalidDimensionTypeException {
          delegate.set (dimension);
        }

        @Override
        public Analyses analyses () {
          return analyses;
        }

        @Override
        public void exportSelection (String name,
                                     Type dimension,
                                     String selection,
                                     Workspace workspace,
                                     DatasetBuilder datasetBuilder) throws InvalidDimensionTypeException,
                                                                   SelectionNotFoundException,
                                                                   InvalidCoordinateException,
                                                                   IOException,
                                                                   DatasetBuilderException,
                                                                   InvalidDatasetNameException {
          delegate.exportSelection (name, dimension, selection, workspace, datasetBuilder);
        }
      });
    }
  }

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.dataset.domain.contract.Workspace#put(edu.dfci.cccb.
   * mev.dataset.domain.contract.Dataset) */
  @Override
  @SneakyThrows ({ IOException.class, DatasetComposingException.class })
  public void put (Dataset dataset) {
    workspaceDelegate.put (dataset);
    try (TemporaryFile ds = new TemporaryFile ()) {
      try (OutputStream out = new BufferedOutputStream (new FileOutputStream (ds))) {
        composer.compose (dataset).write (out);
      }
      String id = google.driveOperations ()
                        .upload (new FileSystemResource (ds),
                                 DriveFile.builder ().setTitle (dataset.name ()).build (),
                                 new UploadParameters ())
                        .getId ();
      session.put (id,
                   new HashMap<String, Class<? extends Analysis>> ());
    }
  }

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.dataset.domain.contract.Workspace#get(java.lang.String) */
  @Override
  public Dataset get (String name) throws DatasetNotFoundException {
    return workspaceDelegate.get (name);
  }

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.dataset.domain.contract.Workspace#remove(java.lang.String
   * ) */
  @Override
  public void remove (String name) throws DatasetNotFoundException {
    workspaceDelegate.remove (name);
  }

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.dataset.domain.contract.Workspace#list() */
  @Override
  public List<String> list () {
    return workspaceDelegate.list ();
  }
}
