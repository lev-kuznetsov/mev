/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */

package edu.dfci.cccb.mev.googleplus.domain.guice;

import static com.google.inject.Guice.createInjector;
import static com.google.inject.Key.get;
import static com.google.inject.name.Names.named;
import static com.google.inject.util.Modules.override;
import static edu.dfci.cccb.mev.googleplus.domain.guice.GoogleDomainModule.API_KEY;
import static edu.dfci.cccb.mev.googleplus.domain.guice.GoogleDomainModule.API_SECRET;
import static edu.dfci.cccb.mev.googleplus.domain.guice.GoogleDomainModule.APPLICATION_NAME;
import static edu.dfci.cccb.mev.googleplus.domain.guice.GoogleDomainModule.AUTHORIZATION_URL;
import static edu.dfci.cccb.mev.googleplus.domain.guice.GoogleDomainModule.REDIRECT_URI;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URL;

import lombok.SneakyThrows;

import org.junit.Before;
import org.junit.Test;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Module;

public class GoogleDomainModuleTest {

  public HttpTransport transport () {
    return mock (HttpTransport.class, RETURNS_DEEP_STUBS);
  }

  public GoogleCredential credential () {
    return mock (GoogleCredential.class, RETURNS_DEEP_STUBS);
  }

  public File file (String title) {
    File result = mock (File.class, RETURNS_DEEP_STUBS);
    when (result.getTitle ()).thenReturn (title);
    return result;
  }

  public FileList fileList (String next, File... files) {
    FileList list = mock (FileList.class, RETURNS_DEEP_STUBS);
    when (list.getNextPageToken ()).thenReturn (next);
    when (list.getItems ()).thenReturn (asList (files));
    return list;
  }

  @SneakyThrows
  public Files files (File... files) {
    Files f = mock (Files.class, RETURNS_DEEP_STUBS);
    when (f.list ().setPageToken (anyString ()).execute ()).thenReturn (fileList ("next", files));
    when (f.list ().setMaxResults (anyInt ()).execute ()).thenReturn (fileList (null, files));
    return f;
  }

  public Drive drive (Files files) {
    Drive drive = mock (Drive.class, RETURNS_DEEP_STUBS);
    when (drive.files ()).thenReturn (files);
    return drive;
  }

  public Module mockInjectedModule () {
    return override (new GoogleDomainModule ()).with (new AbstractModule () {
      protected void configure () {
        bind (HttpTransport.class).toInstance (transport ());
        bind (GoogleCredential.class).toInstance (credential ());
      }
    });
  }

  private Injector injector;

  @Before
  public void setUp () {
    injector = createInjector (mockInjectedModule ());
  }

  @Test
  public void apiKey () {
    assertThat (injector.getInstance (get (String.class, named (API_KEY))), is (anyString ()));
  }

  @Test
  public void apiSecret () {
    assertThat (injector.getInstance (get (String.class, named (API_SECRET))), is (anyString ()));
  }

  @Test
  public void redirectUri () {
    assertThat (injector.getInstance (get (String.class, named (REDIRECT_URI))), is (anyString ()));
  }

  @Test
  public void authUrl () {
    assertThat (injector.getInstance (get (URL.class, named (AUTHORIZATION_URL))), is (any (URL.class)));
  }

  @Test
  public void appName () {
    assertThat (injector.getInstance (get (String.class, named (APPLICATION_NAME))), is (anyString ()));
  }

  @Test
  public void serializers () {
    Reflections reflections = new Reflections ("edu.dfci.cccb.mev.googleplus.domain.support.jackson",
                                               new SubTypesScanner (true));
    for (Class<?> serializer : reflections.getSubTypesOf (JsonSerializer.class))
      injector.getInstance (serializer);
  }
}
