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

package edu.dfci.cccb.mev.common.domain.jobs.r;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

/**
 * @author levk
 * @since CRYSTAL
 */
@RequiredArgsConstructor
public class ByAnnotationValue implements Serializer<Object> {

  private final Class<? extends Annotation> type;
  private final String member;

  public ByAnnotationValue (Class<? extends Annotation> type) {
    this (type, "value");
  }

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.common.domain.jobs.r.Serializer#serialize(java.lang.
   * Object) */
  @Override
  @SneakyThrows ({ NoSuchMethodException.class, InvocationTargetException.class, IllegalAccessException.class })
  public String serialize (Object object) {
    if (object == null)
      return "NULL";
    else {
      Annotation annotation = object.getClass ().getAnnotation (type);
      return annotation.annotationType ().getMethod (member).invoke (annotation).toString ();
    }
  }
}
