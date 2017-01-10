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

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Stack;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPDouble;
import org.rosuda.REngine.REXPInteger;
import org.rosuda.REngine.REXPList;
import org.rosuda.REngine.REXPLogical;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REXPNull;
import org.rosuda.REngine.REXPString;
import org.rosuda.REngine.RList;
import org.rosuda.REngine.Rserve.protocol.REXPFactory;

import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.json.JsonWriteContext;

/**
 * Rserve generator
 * 
 * @author levk
 */
public class RserveGenerator extends GeneratorBase {

  /**
   * Frame
   * 
   * @author levk
   *
   */
  private static class Frame {
    final ArrayList <String> names = new ArrayList <> ();
    final ArrayList <REXP> values = new ArrayList <> ();
  }

  /**
   * Stack
   */
  private Stack <Frame> stack = new Stack <> ();
  /**
   * Output
   */
  private OutputStream out;

  /**
   * @param features
   * @param codec
   * @param out
   */
  public RserveGenerator (int features, ObjectCodec codec, OutputStream out) {
    super (features, codec);
    this.out = out;
  }

  /**
   * @param features
   * @param codec
   * @param ctxt
   * @param out
   */
  public RserveGenerator (int features, ObjectCodec codec, JsonWriteContext ctxt, OutputStream out) {
    super (features, codec, ctxt);
    this.out = out;
  }

  @Override
  public void flush () throws IOException {
    out.flush ();
  }

  @Override
  protected void _releaseBuffers () {
    stack.clear ();
  }

  @Override
  protected void _verifyValueWrite (String typeMsg) throws IOException {}

  /**
   * @param r
   *          expression
   * @throws IOException
   */
  private void writeRexp (REXP r) throws IOException {
    if (!stack.isEmpty ()) {
      stack.peek ().values.add (r);
    } else try {
      REXPFactory factory = new REXPFactory (r);
      byte[] buffer = new byte[factory.getBinaryLength ()];
      factory.getBinaryRepresentation (buffer, 0);
      out.write (buffer);
    } catch (REXPMismatchException e) {
      throw new IOException ("Unable to write " + r.toDebugString ());
    }
  }

  @Override
  public void writeStartArray () throws IOException {
    stack.push (new Frame ());
  }

  @Override
  public void writeEndArray () throws IOException {
    writeRexp (new REXPList (new RList (stack.pop ().values)));
  }

  @Override
  public void writeStartObject () throws IOException {
    stack.push (new Frame ());
  }

  @Override
  public void writeEndObject () throws IOException {
    Frame f = stack.pop ();
    writeRexp (new REXPList (new RList (f.values.toArray (new REXP[f.values.size ()]),
                                        f.names.toArray (new String[f.names.size ()]))));
  }

  @Override
  public void writeFieldName (String name) throws IOException {
    stack.peek ().names.add (name);
  }

  @Override
  public void writeString (String text) throws IOException {
    writeRexp (new REXPString (text));
  }

  @Override
  public void writeString (char[] text, int offset, int len) throws IOException {
    writeString (new String (text, offset, len));
  }

  @Override
  public void writeRawUTF8String (byte[] text, int offset, int length) throws IOException {
    throw new UnsupportedOperationException ();
  }

  @Override
  public void writeUTF8String (byte[] text, int offset, int length) throws IOException {
    throw new UnsupportedOperationException ();
  }

  @Override
  public void writeRaw (String text) throws IOException {
    throw new UnsupportedOperationException ();
  }

  @Override
  public void writeRaw (String text, int offset, int len) throws IOException {
    throw new UnsupportedOperationException ();
  }

  @Override
  public void writeRaw (char[] text, int offset, int len) throws IOException {
    throw new UnsupportedOperationException ();
  }

  @Override
  public void writeRaw (char c) throws IOException {
    throw new UnsupportedOperationException ();
  }

  @Override
  public void writeBinary (Base64Variant bv, byte[] data, int offset, int len) throws IOException {
    throw new UnsupportedOperationException ();
  }

  @Override
  public void writeNumber (int v) throws IOException {
    writeRexp (new REXPInteger (v));
  }

  @Override
  public void writeNumber (long v) throws IOException {
    throw new UnsupportedOperationException ();
  }

  @Override
  public void writeNumber (BigInteger v) throws IOException {
    throw new UnsupportedOperationException ();
  }

  @Override
  public void writeNumber (double v) throws IOException {
    writeRexp (new REXPDouble (v));
  }

  @Override
  public void writeNumber (float v) throws IOException {
    writeRexp (new REXPDouble (v));
  }

  @Override
  public void writeNumber (BigDecimal v) throws IOException {
    throw new UnsupportedOperationException ();
  }

  @Override
  public void writeNumber (String encodedValue) throws IOException {
    writeRexp (new REXPDouble (Double.valueOf (encodedValue)));
  }

  @Override
  public void writeBoolean (boolean state) throws IOException {
    writeRexp (new REXPLogical (state));
  }

  @Override
  public void writeNull () throws IOException {
    writeRexp (new REXPNull ());
  }
}
