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
package edu.dfci.cccb.mev.tools.jackson.rserve;

import static com.fasterxml.jackson.core.JsonToken.END_ARRAY;
import static com.fasterxml.jackson.core.JsonToken.END_OBJECT;
import static com.fasterxml.jackson.core.JsonToken.FIELD_NAME;
import static com.fasterxml.jackson.core.JsonToken.START_ARRAY;
import static com.fasterxml.jackson.core.JsonToken.START_OBJECT;
import static com.fasterxml.jackson.core.JsonToken.VALUE_FALSE;
import static com.fasterxml.jackson.core.JsonToken.VALUE_NULL;
import static com.fasterxml.jackson.core.JsonToken.VALUE_NUMBER_FLOAT;
import static com.fasterxml.jackson.core.JsonToken.VALUE_NUMBER_INT;
import static com.fasterxml.jackson.core.JsonToken.VALUE_STRING;
import static com.fasterxml.jackson.core.JsonToken.VALUE_TRUE;
import static com.fasterxml.jackson.core.Version.unknownVersion;
import static java.util.Arrays.asList;
import static org.rosuda.REngine.REXPLogical.FALSE;
import static org.rosuda.REngine.REXPLogical.TRUE;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPDouble;
import org.rosuda.REngine.REXPInteger;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.RList;
import org.rosuda.REngine.Rserve.protocol.REXPFactory;

import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.base.ParserMinimalBase;

/**
 * Rserve parser
 * 
 * @author levk
 */
public class RserveParser extends ParserMinimalBase {
  /**
   * IO error
   */
  private final IOException ioError;
  /**
   * Runtime error
   */
  private final RuntimeException rtError;
  /**
   * Tokens
   */
  private final Iterator <Token <?>> content;
  /**
   * Current token
   */
  private Token <?> current;
  /**
   * Current name
   */
  private String currentName;
  /**
   * Context
   */
  private JsonStreamContext context = new JsonStreamContext () {

    @Override
    public JsonStreamContext getParent () {
      return null;
    }

    @Override
    public String getCurrentName () {
      return currentName;
    }
  };
  private final ObjectCodec oc;

  /**
   * @param content
   *          tokens
   */
  private RserveParser (Iterable <Token <?>> content) {
    oc = null;
    ioError = null;
    rtError = null;
    this.content = content.iterator ();
  }

  /**
   * Null
   */
  public static final RserveParser NULL = new RserveParser (asList (new Token <> (VALUE_NULL, null)));

  /**
   * @param ct
   *          content
   * @param offset
   *          starting offset
   */
  public RserveParser (byte[] ct, int offset, ObjectCodec oc) {
    this.oc = oc;
    IOException ioError = null;
    RuntimeException rtError = null;
    List <Token <?>> content = null;
    if (ct.length > 0) {
      try {
        REXPFactory factory = new REXPFactory ();
        factory.parseREXP (ct, offset);
        System.out.println (factory.getREXP ());
        content = parse (factory.getREXP ());
      } catch (REXPMismatchException e) {
        ioError = new IOException (e);
      } catch (IOException e) {
        ioError = e;
      } catch (RuntimeException e) {
        rtError = e;
      }
    } else content = asList (new Token <Void> (VALUE_NULL, null));
    this.content = content == null ? null : content.iterator ();
    this.ioError = ioError;
    this.rtError = rtError;
  }

  /**
   * Token
   * 
   * @author levk
   *
   * @param <T>
   */
  private static class Token <T> {
    final JsonToken token;
    final T body;

    Token (JsonToken token, T body) {
      if (token == null) throw new IllegalArgumentException ("Token cannot be null with " + body);
      this.token = token;
      this.body = body;
    }
  }

  /**
   * @param r
   *          expression
   * @return tokens
   * @throws IOException
   * @throws REXPMismatchException
   */
  @SuppressWarnings ({ "unchecked", "rawtypes" })
  private static List <Token <?>> parse (REXP r) throws IOException, REXPMismatchException {
    Collector <Token <?>, List <Token <?>>, List <Token <?>>> collector;
    collector = new Collector <Token <?>, List <Token <?>>, List <Token <?>>> () {
      @Override
      public Supplier <List <Token <?>>> supplier () {
        return ArrayList::new;
      }

      @Override
      public BiConsumer <List <Token <?>>, Token <?>> accumulator () {
        return List::add;
      }

      @Override
      public BinaryOperator <List <Token <?>>> combiner () {
        return (l, rr) -> {
          l.addAll (rr);
          return rr;
        };
      }

      @Override
      public Function <List <Token <?>>, List <Token <?>>> finisher () {
        return l -> {
          if (l.size () > 1) {
            l.add (0, new Token <Void> (START_ARRAY, null));
            l.add (new Token <Void> (END_ARRAY, null));
          }
          return l;
        };
      }

      @Override
      public Set <Characteristics> characteristics () {
        return Collections.emptySet ();
      }
    };

    if (r == null || r.isNull ()) return asList (new Token (VALUE_NULL, null));
    else if (r.isNumeric ()) {
      if (r.isInteger ()) return IntStream.of (r.asIntegers ()).mapToObj (i -> {
        return i == REXPInteger.NA ? new Token <Void> (VALUE_NULL, null) : new Token <Integer> (VALUE_NUMBER_INT, i);
      }).collect (collector);
      else return DoubleStream.of (r.asDoubles ()).mapToObj (d -> {
        return d == REXPDouble.NA ? new Token <Void> (VALUE_NULL, null) : new Token <Double> (VALUE_NUMBER_FLOAT, d);
      }).collect (collector);
    } else if (r.isString ()) {
      return Stream.of (r.asStrings ()).map (s -> new Token <String> (VALUE_STRING, s)).collect (collector);
    } else if (r.isLogical ()) {
      return IntStream.of (r.asIntegers ()).mapToObj (i -> {
        return new Token <Boolean> (i == TRUE ? VALUE_TRUE : i == FALSE ? VALUE_FALSE : VALUE_NULL, null);
      }).collect (collector);
    } else if (r.isList ()) {
      RList list = r.asList ();
      List <Token <?>> tokens = new ArrayList <> ();
      if (list.isNamed ()) {
        tokens.add (new Token (START_OBJECT, null));
        for (Iterator names = list.names.iterator (), values = list.iterator (); names.hasNext ();) {
          tokens.add (new Token (FIELD_NAME, (String) names.next ()));
          tokens.addAll (parse ((REXP) values.next ()));
        }
        tokens.add (new Token (END_OBJECT, null));
      } else {
        tokens.add (new Token (START_ARRAY, null));
        for (REXP item : (Iterable <REXP>) list)
          tokens.addAll (parse (item));
        tokens.add (new Token (END_ARRAY, null));
      }
      return tokens;
    } else throw new IOException ("Unexpected expression type " + r.toDebugString ());
  }

  @Override
  public JsonToken nextToken () throws IOException {
    if (ioError != null) throw ioError;
    else if (rtError != null) throw rtError;
    else {
      JsonToken result = _currToken = (current = content.next ()).token;
      if (result == FIELD_NAME) currentName = (String) current.body;
      return result;
    }
  }

  @Override
  public String getText () throws IOException {
    return current.body.toString ();
  }

  @Override
  public char[] getTextCharacters () throws IOException {
    return getText ().toCharArray ();
  }

  @Override
  public int getTextLength () throws IOException {
    return getTextCharacters ().length;
  }

  @Override
  public int getTextOffset () throws IOException {
    return 0;
  }

  @Override
  public byte[] getBinaryValue (Base64Variant b64variant) throws IOException {
    throw new UnsupportedOperationException ();
  }

  @Override
  public ObjectCodec getCodec () {
    return oc;
  }

  @Override
  public void setCodec (ObjectCodec c) {
    throw new UnsupportedOperationException ();
  }

  @Override
  public boolean hasTextCharacters () {
    return current.body instanceof String;
  }

  @Override
  public Number getNumberValue () throws IOException {
    return (Number) current.body;
  }

  @Override
  public NumberType getNumberType () throws IOException {
    if (current.body instanceof Integer) return NumberType.INT;
    else if (current.body instanceof Double) return NumberType.DOUBLE;
    else throw new IOException ("Unrecognized number type " + current.body.getClass ());
  }

  @Override
  public int getIntValue () throws IOException {
    return (Integer) current.body;
  }

  @Override
  public long getLongValue () throws IOException {
    return getIntValue ();
  }

  @Override
  public BigInteger getBigIntegerValue () throws IOException {
    return BigInteger.valueOf (getIntValue ());
  }

  @Override
  public float getFloatValue () throws IOException {
    return (float) getDoubleValue ();
  }

  @Override
  public double getDoubleValue () throws IOException {
    return (Double) current.body;
  }

  @Override
  public BigDecimal getDecimalValue () throws IOException {
    return BigDecimal.valueOf (getDoubleValue ());
  }

  @Override
  protected void _handleEOF () throws JsonParseException {
    throw new UnsupportedOperationException ();
  }

  @Override
  public String getCurrentName () throws IOException {
    return currentName;
  }

  @Override
  public void close () throws IOException {}

  @Override
  public boolean isClosed () {
    return false;
  }

  @Override
  public JsonStreamContext getParsingContext () {
    return context;
  }

  @Override
  public void overrideCurrentName (String name) {
    currentName = name;
  }

  @Override
  public Version version () {
    return unknownVersion ();
  }

  @Override
  public JsonLocation getTokenLocation () {
    throw new UnsupportedOperationException ();
  }

  @Override
  public JsonLocation getCurrentLocation () {
    return new JsonLocation (null, 0, 0, 0);
  }

  @Override
  public Object getEmbeddedObject () throws IOException {
    throw new UnsupportedOperationException ();
  }
}
