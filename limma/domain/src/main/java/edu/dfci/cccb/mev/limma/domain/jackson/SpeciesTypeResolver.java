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

package edu.dfci.cccb.mev.limma.domain.jackson;

import static com.fasterxml.jackson.databind.type.SimpleType.construct;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;

import edu.dfci.cccb.mev.limma.domain.Species;

/**
 * @author levk
 */
public class SpeciesTypeResolver extends TypeIdResolverBase {

  private static final JavaType TYPE = construct (Species.class);

  private Map<String, Class<? extends Species>> map = new HashMap<> ();

  {
    Set<Class<? extends Species>> types = new HashSet<> ();
    for (Class<?> candidate : Species.class.getDeclaredClasses ()) {
      if (Species.class.isAssignableFrom (candidate)) {
        @SuppressWarnings ("unchecked") Class<? extends Species> type = (Class<? extends Species>) candidate;
        types.add (type);
      }
    }
  }

  @Inject
  private void addSpeciesTypes (Set<Class<? extends Species>> types) {
    for (Class<? extends Species> type : types)
      map.put (type.getAnnotation (edu.dfci.cccb.mev.limma.domain.annotation.Species.class).value (), type);
  }

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
    return (value == null ? suggestedType : value.getClass ()).getAnnotation (edu.dfci.cccb.mev.limma.domain.annotation.Species.class)
                                                              .value ();
  }

  /* (non-Javadoc)
   * @see com.fasterxml.jackson.databind.jsontype.TypeIdResolver#getMechanism() */
  @Override
  public Id getMechanism () {
    return Species.class.getAnnotation (JsonTypeInfo.class).use ();
  }

  /* (non-Javadoc)
   * @see
   * com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase#typeFromId
   * (java.lang.String) */
  @Override
  @Deprecated
  public JavaType typeFromId (String id) {
    Class<? extends Species> type = map.get (id);
    if (type == null)
      throw new IllegalArgumentException ("Unrecognized species type id: " + id);
    return construct (type);
  }

  /* (non-Javadoc)
   * @see
   * com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase#typeFromId
   * (com.fasterxml.jackson.databind.DatabindContext, java.lang.String) */
  @Override
  public JavaType typeFromId (DatabindContext context, String id) {
    Class<? extends Species> type = map.get (id);
    if (type == null)
      throw new IllegalArgumentException ("Unrecognized species type id " + id);
    return context.constructSpecializedType (TYPE, type);
  }
}
