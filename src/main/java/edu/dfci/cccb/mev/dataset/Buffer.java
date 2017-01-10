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
package edu.dfci.cccb.mev.dataset;

import static java.nio.ByteBuffer.wrap;
import static javax.persistence.GenerationType.IDENTITY;

import java.nio.ByteBuffer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

/**
 * Binary buffer
 * 
 * @author levk
 */
@Entity
public class Buffer {

  /**
   * Identifier
   */
  private @Id @GeneratedValue (strategy = IDENTITY) long id;
  /**
   * Buffer
   */
  private @Column @Lob byte[] data;

  /**
   * @return buffer
   */
  public ByteBuffer buffer () {
    return wrap (data);
  }

  /**
   * @param data
   * @return buffer
   */
  public static Buffer buffer (byte[] data) {
    Buffer b = new Buffer ();
    b.data = data;
    return b;
  }
}
