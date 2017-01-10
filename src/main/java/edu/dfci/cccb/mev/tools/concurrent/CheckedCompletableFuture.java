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
package edu.dfci.cccb.mev.tools.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

/**
 * {@link CompletableFuture} supporting checked exceptions
 * 
 * @author levk
 */
public class CheckedCompletableFuture <T> extends CompletableFuture <T> {

  /**
   * @param callable
   * @return checked completion stage
   */
  public <R> CheckedCompletableFuture <R> thenCall (Callable <R> callable) {
    CheckedCompletableFuture <R> f = new CheckedCompletableFuture <> ();

    f.thenRun ( () -> {
      try {
        f.complete (callable.call ());
      } catch (Exception e) {
        f.completeExceptionally (e);
      }
    });

    return f;
  }

  /**
   * @param callable
   * @param executor
   * @return checked completion stage
   */
  public static <T> CheckedCompletableFuture <T> callAsync (Callable <T> callable, ExecutorService executor) {
    CheckedCompletableFuture <T> f = new CheckedCompletableFuture <> ();

    executor.execute ( () -> {
      try {
        f.complete (callable.call ());
      } catch (Exception e) {
        f.completeExceptionally (e);
      }
    });

    return f;
  }
}
