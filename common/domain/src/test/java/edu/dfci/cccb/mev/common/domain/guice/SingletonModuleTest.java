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

package edu.dfci.cccb.mev.common.domain.guice;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.concurrent.atomic.AtomicInteger;

import lombok.RequiredArgsConstructor;

import org.junit.Test;

import com.google.inject.Binder;
import com.google.inject.Guice;

public class SingletonModuleTest {

  @RequiredArgsConstructor
  private static class TestSingletonModule extends SingletonModule {
    private final AtomicInteger called;

    public void configure (Binder binder) {
      called.incrementAndGet ();
    }
  }

  @Test
  public void once () throws Exception {
    AtomicInteger called = new AtomicInteger (0);
    TestSingletonModule module = new TestSingletonModule (called);

    Guice.createInjector (module);

    assertThat (called.get (), is (1));
  }

  @Test
  public void twice () throws Exception {
    AtomicInteger called = new AtomicInteger (0);
    TestSingletonModule module = new TestSingletonModule (called);

    Guice.createInjector (module, module);

    assertThat (called.get (), is (1));
  }

  @Test
  public void twoInstances () throws Exception {
    AtomicInteger called = new AtomicInteger (0);
    TestSingletonModule module1 = new TestSingletonModule (called);
    TestSingletonModule module2 = new TestSingletonModule (called);

    Guice.createInjector (module1, module2);

    assertThat (called.get (), is (1));
  }
}