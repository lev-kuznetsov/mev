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

package edu.dfci.cccb.mev.dataset.domain.guice;

import static ch.lambdaj.Lambda.convert;
import static com.google.inject.multibindings.Multibinder.newSetBinder;
import static com.google.inject.name.Names.named;
import static edu.dfci.cccb.mev.dataset.domain.messages.DatasetTsvMessageHandler.COLUMN;
import static edu.dfci.cccb.mev.dataset.domain.messages.DatasetTsvMessageHandler.COMMENT_EXPRESSION;
import static edu.dfci.cccb.mev.dataset.domain.messages.DatasetTsvMessageHandler.DELIMITER_CHARACTER;
import static edu.dfci.cccb.mev.dataset.domain.messages.DatasetTsvMessageHandler.END_OF_LINE_SYMBOL;
import static edu.dfci.cccb.mev.dataset.domain.messages.DatasetTsvMessageHandler.QUOTE_CHARACTER;
import static edu.dfci.cccb.mev.dataset.domain.messages.DatasetTsvMessageHandler.ROW;
import static java.lang.String.valueOf;
import static java.util.regex.Pattern.compile;

import java.util.Collection;
import java.util.Set;
import java.util.regex.Pattern;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.inject.Named;
import javax.inject.Singleton;

import org.supercsv.comment.CommentMatcher;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.prefs.CsvPreference.Builder;

import ch.lambdaj.function.convert.Converter;

import com.google.inject.Binder;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;

import edu.dfci.cccb.mev.common.domain.guice.MevModule;
import edu.dfci.cccb.mev.common.domain.guice.SingletonModule;
import edu.dfci.cccb.mev.common.domain.guice.jaxrs.JaxrsModule;
import edu.dfci.cccb.mev.common.domain.guice.jaxrs.MessageReaderBinder;
import edu.dfci.cccb.mev.common.domain.guice.jaxrs.MessageWriterBinder;
import edu.dfci.cccb.mev.dataset.domain.Analysis;
import edu.dfci.cccb.mev.dataset.domain.annotation.Type;
import edu.dfci.cccb.mev.dataset.domain.jackson.AnalysisTypeResolver.RegisteredAnalyses;
import edu.dfci.cccb.mev.dataset.domain.messages.DatasetTsvMessageHandler;

/**
 * @author levk
 * @since CRYSTAL
 */
public class DatasetModule extends MevModule {

  /* (non-Javadoc)
   * @see com.google.inject.Module#configure(com.google.inject.Binder) */
  @Override
  @OverridingMethodsMustInvokeSuper
  public void configure (final Binder binder) {
    super.configure (binder);

    configure (new AnalysisTypeRegistrar () {
      Multibinder<Class<? extends Analysis>> types = newSetBinder (binder,
                                                                   new TypeLiteral<Class<? extends Analysis>> () {},
                                                                   RegisteredAnalyses.class);

      @Override
      public void register (Class<? extends Analysis> type) {
        if (type.getAnnotation (Type.class) == null)
          throw new IllegalArgumentException ("Failed registering analysis type "
                                              + type.getSimpleName ()
                                              + " because it is missing required " + Type.class.getSimpleName ()
                                              + " annotation");
        types.addBinding ().toInstance (type);
      }
    });

    binder.install (new SingletonModule () {

      // Dataset construction

      @Provides
      @Singleton
      @Named (ROW)
      public String row () {
        return "row";
      }

      @Provides
      @Singleton
      @Named (COLUMN)
      public String column () {
        return "column";
      }

      // CSV parser configuration

      public void configureCommentExpressions (Binder binder) {
        Multibinder<String> commentExpressions = newSetBinder (binder, String.class, named (COMMENT_EXPRESSION));
        commentExpressions.addBinding ().toInstance ("[\t ]*#[^$]*");
      }

      public void configureEndOfLineCharacters (Binder binder) {
        Multibinder<Character> endOfLineCharacters = newSetBinder (binder, Character.class, named (END_OF_LINE_SYMBOL));
        endOfLineCharacters.addBinding ().toInstance ('\r');
        endOfLineCharacters.addBinding ().toInstance ('\n');
      }

      @Provides
      @Singleton
      @Named (DELIMITER_CHARACTER)
      public Character delimiterCharacter () {
        return '\t';
      }

      @Provides
      @Singleton
      @Named (QUOTE_CHARACTER)
      public Character quoteCharacter () {
        return '"';
      }

      @Provides
      @Singleton
      public CsvPreference preference (@Named (COMMENT_EXPRESSION) final Set<String> commentExpressions,
                                       @Named (END_OF_LINE_SYMBOL) Set<Character> endOfLineSymbols,
                                       @Named (DELIMITER_CHARACTER) Character delimiterCharacter,
                                       @Named (QUOTE_CHARACTER) Character quoteCharacter) {
        char[] endOfLineSymbolsArray = new char[endOfLineSymbols.size ()];
        int i = 0;
        for (char c : endOfLineSymbols)
          endOfLineSymbolsArray[i++] = c;

        return new Builder (quoteCharacter,
                            delimiterCharacter,
                            valueOf (endOfLineSymbolsArray)).skipComments (new CommentMatcher () {
          private final Collection<Pattern> comments;

          {
            comments = convert (commentExpressions, new Converter<String, Pattern> () {
              @Override
              public Pattern convert (String from) {
                return compile (from);
              }
            });
          }

          @Override
          public boolean isComment (String line) {
            for (Pattern comment : comments)
              if (comment.matcher (line).matches ())
                return true;
            return false;
          }
        }).build ();
      }

      @Override
      public void configure (Binder binder) {
        configureCommentExpressions (binder);
        configureEndOfLineCharacters (binder);

        binder.install (new JaxrsModule () {
          private final DatasetTsvMessageHandler handler = new DatasetTsvMessageHandler ();

          @Override
          public void configure (MessageReaderBinder binder) {
            binder.useInstance (handler);
          }

          @Override
          public void configure (MessageWriterBinder binder) {
            binder.useInstance (handler);
          }
        });
      }
    });
  }

  /**
   * Register analysis types
   */
  public void configure (AnalysisTypeRegistrar registrar) {}
}
