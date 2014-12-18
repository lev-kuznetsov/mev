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

package edu.dfci.cccb.mev.hclust.domain;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.PROPERTY;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.CUSTOM;
import static javax.xml.bind.annotation.XmlAccessType.NONE;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;

import edu.dfci.cccb.mev.common.domain.jobs.r.annotation.toR;
import edu.dfci.cccb.mev.hclust.domain.jackson.AgglomerationTypeResolver;
import edu.dfci.cccb.mev.hclust.domain.r.AgglomerationRSerializer;

/**
 * @author levk
 * @since CRYSTAL
 */
@XmlRootElement
@XmlAccessorType (NONE)
@JsonTypeInfo (use = CUSTOM, property = "type", include = PROPERTY)
@JsonTypeIdResolver (AgglomerationTypeResolver.class)
@toR (AgglomerationRSerializer.class)
public interface Agglomeration {
  // Inner classes implementing Agglomeration are automatically registered by
  // the AgglomerationTypeResolver

  @EqualsAndHashCode
  @ToString
  @edu.dfci.cccb.mev.hclust.domain.annotation.Agglomeration ("ward")
  public static final class Ward implements Agglomeration {}

  @EqualsAndHashCode
  @ToString
  @edu.dfci.cccb.mev.hclust.domain.annotation.Agglomeration ("single")
  public static final class Single implements Agglomeration {}

  @EqualsAndHashCode
  @ToString
  @edu.dfci.cccb.mev.hclust.domain.annotation.Agglomeration ("complete")
  public static final class Complete implements Agglomeration {}

  @EqualsAndHashCode
  @ToString
  @edu.dfci.cccb.mev.hclust.domain.annotation.Agglomeration ("average")
  public static final class Average implements Agglomeration {}

  @EqualsAndHashCode
  @ToString
  @edu.dfci.cccb.mev.hclust.domain.annotation.Agglomeration ("mcquitty")
  public static final class McQuitty implements Agglomeration {}

  @EqualsAndHashCode
  @ToString
  @edu.dfci.cccb.mev.hclust.domain.annotation.Agglomeration ("median")
  public static final class Median implements Agglomeration {}

  @EqualsAndHashCode
  @ToString
  @edu.dfci.cccb.mev.hclust.domain.annotation.Agglomeration ("centroid")
  public static final class Centroid implements Agglomeration {}
}
