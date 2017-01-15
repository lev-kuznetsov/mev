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

import static com.fasterxml.jackson.core.format.MatchStrength.INCONCLUSIVE;
import static edu.dfci.cccb.mev.tools.jackson.rserve.RserveParser.NULL;
import static java.nio.ByteOrder.LITTLE_ENDIAN;
import static org.rosuda.REngine.Rserve.protocol.RTalk.DT_LARGE;
import static org.rosuda.REngine.Rserve.protocol.RTalk.DT_SEXP;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.nio.ByteBuffer;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.Rserve.RserveException;
import org.rosuda.REngine.Rserve.protocol.RPacket;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.format.InputAccessor;
import com.fasterxml.jackson.core.format.MatchStrength;

/**
 * @author levk
 */
public class RserveFactory extends JsonFactory {

  private static final long serialVersionUID = 1L;

  /**
   * 
   */
  public RserveFactory () {}

  /**
   * @param oc
   */
  public RserveFactory (ObjectCodec oc) {
    super (oc);
  }

  /**
   * @param src
   * @param codec
   */
  public RserveFactory (RserveFactory src, ObjectCodec codec) {
    super (src, codec);
  }

  @Override
  public boolean canUseCharArrays () {
    return false;
  }

  @Override
  public String getFormatName () {
    return "Rserve";
  }

  @Override
  public MatchStrength hasFormat (InputAccessor acc) throws IOException {
    return INCONCLUSIVE;
  }

  @Override
  public RserveParser createParser (byte[] data) throws IOException, JsonParseException {
    return createParser (data, 0, data.length);
  }

  @Override
  public RserveParser createParser (byte[] data, int offset, int len) throws IOException, JsonParseException {
    return new RserveParser (data, offset, _objectCodec);
  }

  @Override
  public RserveParser createParser (char[] content) throws IOException {
    return createParser (content, 0, content.length);
  }

  @Override
  public RserveParser createParser (char[] content, int offset, int len) throws IOException {
    return createParser (new String (content, offset, len));
  }

  @Override
  public RserveParser createParser (String content) throws IOException, JsonParseException {
    return createParser (content.getBytes ());
  }

  /**
   * Note, the stream will assume pickup at the {@link RPacket} header point
   * which describes the length of the packet to read
   */
  @Override
  public RserveParser createParser (File f) throws IOException, JsonParseException {
    return createParser (new FileInputStream (f));
  }

  /**
   * Note, the stream will assume pickup at the {@link RPacket} header point
   * which describes the length of the packet to read
   * 
   * The implementation is currently not streaming, the entire packet will be
   * buffered and passed to the factory for parsing. Streaming parser will
   * require a different {@link REXP} parsing implementation
   */
  @Override
  public RserveParser createParser (InputStream in) throws IOException, JsonParseException {
    try {
      byte[] array = new byte[16];
      ByteBuffer buffer = ByteBuffer.wrap (array);
      buffer.order (LITTLE_ENDIAN);
      in.read (array);
      int rc = buffer.getInt ();
      if ((rc & 0x0f) == 1) {
        int contentLength = buffer.getInt ();
        if (contentLength == 0) return NULL;
        byte[] content = new byte[contentLength];
        in.read (content);
        int rxo = content[0] == (DT_SEXP | DT_LARGE) ? 8 : 4;
        if (content.length > rxo) return createParser (content, rxo, content.length - rxo);
        else throw new IOException ("Malformed response packet");
      } else if ((rc & 0x0f) == 2)
        throw new IOException ("Error response packet", new RserveException (null, "Command failed", (rc >> 24) & 127));
      else throw new IOException ("Malformed response packet");
    } finally {
      in.close ();
    }
  }

  /**
   * Note, the stream will assume pickup at the {@link RPacket} header point
   * which describes the length of the packet to read
   */
  @Override
  public RserveParser createParser (URL url) throws IOException, JsonParseException {
    return createParser (url.openStream ());
  }

  @Override
  public RserveParser createParser (Reader r) throws IOException, JsonParseException {
    throw new UnsupportedOperationException ();
  }

  @Override
  public RserveGenerator createGenerator (File f, JsonEncoding enc) throws IOException {
    return createGenerator (new FileOutputStream (f));
  }

  @Override
  public RserveGenerator createGenerator (OutputStream out) throws IOException {
    return new RserveGenerator (0, null, out);
  }

  @Override
  public RserveGenerator createGenerator (OutputStream out, JsonEncoding enc) throws IOException {
    return createGenerator (out);
  }

  @Override
  public JsonGenerator createGenerator (Writer w) throws IOException {
    throw new UnsupportedOperationException ();
  }
}
