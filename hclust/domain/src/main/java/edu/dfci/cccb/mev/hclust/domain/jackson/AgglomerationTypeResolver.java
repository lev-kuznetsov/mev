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

package edu.dfci.cccb.mev.hclust.domain.jackson;

import static com.fasterxml.jackson.databind.type.SimpleType.construct;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Provider;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;

import edu.dfci.cccb.mev.hclust.domain.Agglomeration;

/**
 * @author levk
 * @since CRYSTAL
 */
@SuppressWarnings ("unchecked")
public class AgglomerationTypeResolver extends TypeIdResolverBase {

  private static final JavaType BASE_TYPE = construct (Agglomeration.class);

  private final Map<String, Class<? extends Agglomeration>> map;

  {
    map = new HashMap<> ();
    addAgglomerationTypes (new Provider<Set<Class<? extends Agglomeration>>> () {
      @Override
      public Set<Class<? extends Agglomeration>> get () {
        Set<Class<? extends Agglomeration>> result = new HashSet<> ();
        for (Class<?> inner : Agglomeration.class.getClasses ())
          if (Agglomeration.class.isAssignableFrom (inner))
            result.add ((Class<? extends Agglomeration>) inner);
        return result.size () < 1 ? null : result;
      }
    });
  }

  @Inject
  private void addAgglomerationTypes (Provider<Set<Class<? extends Agglomeration>>> agglomerationTypesProvider) {
    Set<Class<? extends Agglomeration>> types = agglomerationTypesProvider.get ();
    for (Class<? extends Agglomeration> type : types)
      map.put (type.getAnnotation (edu.dfci.cccb.mev.hclust.domain.annotation.Agglomeration.class).value (),
               (Class<? extends Agglomeration>) type);
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
    return (value == null ? suggestedType : value.getClass ()).getAnnotation (edu.dfci.cccb.mev.hclust.domain.annotation.Agglomeration.class)
                                                              .value ();
  }

  /* (non-Javadoc)
   * @see com.fasterxml.jackson.databind.jsontype.TypeIdResolver#getMechanism() */
  @Override
  public Id getMechanism () {
    return Agglomeration.class.getAnnotation (JsonTypeInfo.class).use ();
  }

  /* (non-Javadoc)
   * @see
   * com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase#typeFromId
   * (java.lang.String) */
  @Override
  @Deprecated
  public JavaType typeFromId (String id) {
    Class<? extends Agglomeration> type = map.get (id);
    if (type == null)
      throw new IllegalArgumentException ("Unrecognized analysis id " + id);
    return construct (type);
  }

  /* (non-Javadoc)
   * @see
   * com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase#typeFromId
   * (com.fasterxml.jackson.databind.DatabindContext, java.lang.String) */
  @Override
  public JavaType typeFromId (DatabindContext context, String id) {
    Class<? extends Agglomeration> type = map.get (id);
    if (type == null)
      throw new IllegalArgumentException ("Unrecognized analysis id " + id);
    return context.constructSpecializedType (BASE_TYPE, type);
  }
}
