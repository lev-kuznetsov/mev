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

package edu.dfci.cccb.mev.cluster.domain;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.PROPERTY;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.CUSTOM;
import static javax.xml.bind.annotation.XmlAccessType.NONE;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;

import edu.dfci.cccb.mev.cluster.domain.jackson.LinkageTypeResolver;

/**
 * @author levk
 * @since CRYSTAL
 */
@XmlRootElement
@XmlAccessorType (NONE)
@JsonTypeInfo (use = CUSTOM, property = "type", include = PROPERTY)
@JsonTypeIdResolver (LinkageTypeResolver.class)
public interface Linkage {

  @EqualsAndHashCode
  @ToString
  @edu.dfci.cccb.mev.cluster.domain.annotation.Linkage ("average")
  public static class Average implements Linkage {}

  @EqualsAndHashCode
  @ToString
  @edu.dfci.cccb.mev.cluster.domain.annotation.Linkage ("centroid")
  public static class Centroid implements Linkage {}

  @EqualsAndHashCode
  @ToString
  @edu.dfci.cccb.mev.cluster.domain.annotation.Linkage ("complete")
  public static class Complete implements Linkage {}

  @EqualsAndHashCode
  @ToString
  @edu.dfci.cccb.mev.cluster.domain.annotation.Linkage ("mcquitty")
  public static class McQuitty implements Linkage {}

  @EqualsAndHashCode
  @ToString
  @edu.dfci.cccb.mev.cluster.domain.annotation.Linkage ("ward")
  public static class Ward implements Linkage {}

  public static final Linkage DEFAULT = new Average ();
}
