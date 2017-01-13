/*
 * The MIT License (MIT)
 * Copyright (c) 2017 Dana-Farber Cancer Institute
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package edu.dfci.cccb.mev.tools.jackson;

import static java.util.stream.StreamSupport.stream;
import static javax.enterprise.inject.spi.CDI.current;

import java.lang.annotation.Annotation;

import javax.inject.Qualifier;

import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.InjectableValues;

/**
 * CDI {@link InjectableValues}
 * 
 * @author levk
 */
public class CdiInjectionHandler extends InjectableValues {

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.fasterxml.jackson.databind.InjectableValues#findInjectableValue(java.
   * lang.Object, com.fasterxml.jackson.databind.DeserializationContext,
   * com.fasterxml.jackson.databind.BeanProperty, java.lang.Object)
   */
  @Override
  public Object findInjectableValue (Object valueId, DeserializationContext ctxt, BeanProperty forProperty,
                                     Object beanInstance) {
    return current ().select (forProperty.getMember ().getRawType (),
                              stream (forProperty.getMember ().annotations ().spliterator (), true).filter (a -> {
                                return a.annotationType ().isAnnotationPresent (Qualifier.class);
                              }).toArray (Annotation[]::new)).get ();
  }
}
