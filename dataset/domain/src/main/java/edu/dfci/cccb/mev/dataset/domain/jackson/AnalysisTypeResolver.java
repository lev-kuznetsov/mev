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

package edu.dfci.cccb.mev.dataset.domain.jackson;

import static com.fasterxml.jackson.databind.type.SimpleType.construct;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Qualifier;

import lombok.extern.log4j.Log4j;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;

import edu.dfci.cccb.mev.dataset.domain.Analysis;
import edu.dfci.cccb.mev.dataset.domain.annotation.Type;

/**
 * Analysis sub-type resolver
 * 
 * @author levk
 * @since CRYSTAL
 */
@Log4j
public class AnalysisTypeResolver extends TypeIdResolverBase {

  private static final JavaType ANALYSIS_BASE_TYPE = construct (Analysis.class);

  @Retention (RUNTIME)
  @Target ({ PARAMETER, METHOD, FIELD })
  @Qualifier
  public static @interface RegisteredAnalyses {}

  private Map<String, Class<? extends Analysis>> map = new HashMap<> ();

  @Inject
  private void analysisTypes (@RegisteredAnalyses Provider<Set<Class<? extends Analysis>>> analysisTypesProvider) {
    Set<Class<? extends Analysis>> analysisTypes = analysisTypesProvider.get ();
    if (analysisTypes != null)
      for (Class<? extends Analysis> type : analysisTypes)
        map.put (idFromValueAndType (null, type), type);

    log.info ("Registered analysis types: " + map);
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
    return (value == null ? suggestedType : value.getClass ()).getAnnotation (Type.class)
                                                              .value ();
  }

  /* (non-Javadoc)
   * @see com.fasterxml.jackson.databind.jsontype.TypeIdResolver#getMechanism() */
  @Override
  public Id getMechanism () {
    return Analysis.class.getAnnotation (JsonTypeInfo.class).use ();
  }

  /* (non-Javadoc)
   * @see
   * com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase#typeFromId
   * (java.lang.String) */
  @Override
  @Deprecated
  public JavaType typeFromId (String id) {
    Class<? extends Analysis> type = map.get (id);
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
    Class<? extends Analysis> type = map.get (id);
    if (type == null)
      throw new IllegalArgumentException ("Unrecognized analysis id " + id);
    return context.constructSpecializedType (ANALYSIS_BASE_TYPE, type);
  }
}
