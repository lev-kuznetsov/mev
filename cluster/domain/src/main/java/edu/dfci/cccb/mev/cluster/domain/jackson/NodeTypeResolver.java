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

package edu.dfci.cccb.mev.cluster.domain.jackson;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.CUSTOM;
import static com.fasterxml.jackson.databind.type.SimpleType.construct;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;

import edu.dfci.cccb.mev.cluster.domain.Node;
import edu.dfci.cccb.mev.cluster.domain.Node.Branch;
import edu.dfci.cccb.mev.cluster.domain.Node.Leaf;

/**
 * @author levk
 */
public class NodeTypeResolver extends TypeIdResolverBase {

  private static final JavaType LEAF = construct (Leaf.class);
  private static final JavaType BRANCH = construct (Branch.class);

  /* (non-Javadoc)
   * @see
   * com.fasterxml.jackson.databind.jsontype.TypeIdResolver#idFromValue(java
   * .lang.Object) */
  @Override
  public String idFromValue (Object value) {
    return idFromValueAndType (value, null);
  }

  /* (non-Javadoc)
   * @see
   * com.fasterxml.jackson.databind.jsontype.TypeIdResolver#idFromValueAndType
   * (java.lang.Object, java.lang.Class) */
  @Override
  public String idFromValueAndType (Object value, Class<?> suggestedType) {
    Class<?> type = value == null ? suggestedType : value.getClass ();
    if (!Node.class.isAssignableFrom (type))
      throw new IllegalArgumentException ("Unsupported type to NodeTypeResolver " + type.getName ());
    else if (Branch.class.isAssignableFrom (type))
      return "branch";
    else if (Leaf.class.isAssignableFrom (type))
      return "leaf";
    else
      throw new IllegalArgumentException ("Unrecognized node type " + type.getName ());
  }

  /* (non-Javadoc)
   * @see com.fasterxml.jackson.databind.jsontype.TypeIdResolver#getMechanism() */
  @Override
  public Id getMechanism () {
    return CUSTOM;
  }

  /* (non-Javadoc)
   * @see
   * com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase#typeFromId
   * (java.lang.String) */
  @Override
  public JavaType typeFromId (String id) {
    return typeFromId (null, id);
  }

  /* (non-Javadoc)
   * @see
   * com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase#typeFromId
   * (com.fasterxml.jackson.databind.DatabindContext, java.lang.String) */
  @Override
  public JavaType typeFromId (DatabindContext context, String id) {
    if ("branch".equals (id))
      return BRANCH;
    else if ("leaf".equals (id))
      return LEAF;
    else
      throw new IllegalArgumentException ("Unrecognized id " + id);
  }
}
