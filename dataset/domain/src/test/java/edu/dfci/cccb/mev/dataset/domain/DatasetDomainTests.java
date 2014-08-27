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

package edu.dfci.cccb.mev.dataset.domain;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import edu.dfci.cccb.mev.dataset.domain.guice.DatasetModuleTest;
import edu.dfci.cccb.mev.dataset.domain.prototype.AnalysisAdapterTest;
import edu.dfci.cccb.mev.dataset.domain.prototype.DatasetAdapterTest;
import edu.dfci.cccb.mev.dataset.domain.prototype.DimensionAdapterTest;
import edu.dfci.cccb.mev.dataset.domain.prototype.ValuesTest;
import edu.dfci.cccb.mev.dataset.domain.support.FlatFileStoreValuesAdapterTest;
import edu.dfci.cccb.mev.dataset.domain.support.json.DatasetJsonSerializerTest;
import edu.dfci.cccb.mev.dataset.domain.support.tsv.DatasetTsvDeserializerTest;
import edu.dfci.cccb.mev.dataset.domain.support.tsv.DatasetTsvSerializerTest;

@RunWith (Suite.class)
@SuiteClasses ({
                ValuesTest.class, DatasetJsonSerializerTest.class, DimensionAdapterTest.class,
                DatasetAdapterTest.class, AnalysisAdapterTest.class, DatasetModuleTest.class,
                FlatFileStoreValuesAdapterTest.class, DatasetTsvDeserializerTest.class,
                DatasetTsvSerializerTest.class })
public class DatasetDomainTests {}
