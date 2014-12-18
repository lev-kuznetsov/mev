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

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;

import edu.dfci.cccb.mev.common.domain.jobs.r.annotation.toR;
import edu.dfci.cccb.mev.hclust.domain.jackson.MetricTypeResolver;
import edu.dfci.cccb.mev.hclust.domain.r.MetricRSerializer;

/**
 * @author levk
 * @since CRYSTAL
 */
@XmlRootElement
@XmlAccessorType (NONE)
@JsonTypeInfo (use = CUSTOM, property = "type", include = PROPERTY)
@JsonTypeIdResolver (MetricTypeResolver.class)
@toR (MetricRSerializer.class)
public interface Metric {
  // Inner classes implementing Metric are automatically registered by the
  // MetricTypeResolver

  @EqualsAndHashCode
  @ToString
  @edu.dfci.cccb.mev.hclust.domain.annotation.Metric ("euclidean")
  public static final class Euclidean implements Metric {}

  @EqualsAndHashCode
  @ToString
  @edu.dfci.cccb.mev.hclust.domain.annotation.Metric ("maximum")
  public static final class Maximum implements Metric {}

  @EqualsAndHashCode
  @ToString
  @edu.dfci.cccb.mev.hclust.domain.annotation.Metric ("manhattan")
  public static final class Manhattan implements Metric {}

  @EqualsAndHashCode
  @ToString
  @edu.dfci.cccb.mev.hclust.domain.annotation.Metric ("canberra")
  public static final class Canberra implements Metric {}

  @EqualsAndHashCode
  @ToString
  @edu.dfci.cccb.mev.hclust.domain.annotation.Metric ("binary")
  public static final class Binary implements Metric {}

  @Accessors (fluent = true)
  @EqualsAndHashCode
  @ToString
  @edu.dfci.cccb.mev.hclust.domain.annotation.Metric ("binary")
  public static final class Minkowski implements Metric {
    private final @Getter String type = "minkowski";

    private @XmlElement int power = 2;

    @PUT
    @Path ("/power")
    public void set (int power) {
      this.power = power;
    }

    @GET
    @Path ("/power")
    public int power () {
      return power;
    }
  }
}
