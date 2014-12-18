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

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableSet;
import static javax.xml.bind.annotation.XmlAccessType.NONE;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import edu.dfci.cccb.mev.common.domain.jobs.r.annotation.toJava;
import edu.dfci.cccb.mev.hclust.domain.r.NewickRDeserializer;

/**
 * Node in a dendrogram
 * 
 * @author levk
 * @since CRYSTAL
 */
@XmlRootElement
@XmlAccessorType (NONE)
@Accessors (fluent = true)
@toJava (NewickRDeserializer.class)
public interface Node <K> {

  @RequiredArgsConstructor
  public static final class Leaf <K> implements Node<K> {
    private final @XmlElement @Getter K key;
  }

  public static final class Branch <K> implements Node<K> {
    private final @XmlElement @Getter Set<Node<K>> children;

    public Branch (Node<K> child1, Node<K> child2) {
      children = unmodifiableSet (new HashSet<> (asList (child1, child2)));
    }
  }
}
