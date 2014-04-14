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

package edu.dfci.cccb.mev.common.domain;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import edu.dfci.cccb.mev.common.domain.c3p0.PooledDataSourceProviderTest;
import edu.dfci.cccb.mev.common.domain.guice.JacksonModuleTest;
import edu.dfci.cccb.mev.common.domain.guice.MevDomainModuleTest;
import edu.dfci.cccb.mev.common.domain.guice.ModulesTest;
import edu.dfci.cccb.mev.common.domain.guice.SingletonModuleTest;
import edu.dfci.cccb.mev.common.domain.jooq.StoreTest;
import edu.dfci.cccb.mev.common.domain.support.MevExceptionTest;

@RunWith (Suite.class)
@SuiteClasses ({
                PooledDataSourceProviderTest.class, JacksonModuleTest.class, MevDomainModuleTest.class,
                ModulesTest.class, SingletonModuleTest.class, StoreTest.class, MevExceptionTest.class })
public class CommonDomainTests {}
