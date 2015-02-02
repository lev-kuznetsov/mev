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

package edu.dfci.cccb.mev.limma.domain;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.PROPERTY;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.CUSTOM;
import static javax.xml.bind.annotation.XmlAccessType.NONE;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;

import edu.dfci.cccb.mev.limma.domain.jackson.SpeciesTypeResolver;

/**
 * @author levk
 * @since CRYSTAL
 */
@XmlRootElement
@XmlAccessorType (NONE)
@JsonTypeInfo (use = CUSTOM, property = "type", include = PROPERTY)
@JsonTypeIdResolver (SpeciesTypeResolver.class)
public interface Species {
  @EqualsAndHashCode
  @ToString
  @edu.dfci.cccb.mev.limma.domain.annotation.Species ("human")
  public static class Human implements Species {}

  @EqualsAndHashCode
  @ToString
  @edu.dfci.cccb.mev.limma.domain.annotation.Species ("rat")
  public static class Rat implements Species {}

  @EqualsAndHashCode
  @ToString
  @edu.dfci.cccb.mev.limma.domain.annotation.Species ("mouse")
  public static class Mouse implements Species {}
}
