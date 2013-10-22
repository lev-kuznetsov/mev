/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.dfci.cccb.mev.heatmap.support;

import static edu.dfci.cccb.mev.heatmap.domain.Axis.COLUMN;
import static edu.dfci.cccb.mev.heatmap.domain.Axis.ROW;
import static java.lang.Integer.parseInt;
import static java.util.Arrays.asList;
import static java.util.regex.Pattern.matches;

import java.util.List;

import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j;

import org.javatuples.Pair;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.PathVariableMethodArgumentResolver;

import us.levk.util.runtime.support.Methods;
import edu.dfci.cccb.mev.heatmap.domain.Area;
import edu.dfci.cccb.mev.heatmap.domain.Axis;
import edu.dfci.cccb.mev.heatmap.domain.Interval;
import edu.dfci.cccb.mev.heatmap.domain.InvalidDataRequestException;

/**
 * @author levk
 * 
 */
@Log4j
@Accessors (fluent = true)
public class AreaMethodArgumentResolver extends PathVariableMethodArgumentResolver {

  public static final String AREA_STRING_REPRESENTATION_PATTERN_EXPRESSION = "\\[[0-9]+:[0-9]+,[0-9]+:[0-9]+\\]";

  /* (non-Javadoc)
   * @see org.springframework.web.servlet.mvc.method.annotation.
   * PathVariableMethodArgumentResolver
   * #supportsParameter(org.springframework.core.MethodParameter) */
  @Override
  public boolean supportsParameter (MethodParameter parameter) {
    boolean result = Area.class.equals (parameter.getParameterType ())
                     && super.supportsParameter (parameter);
    if (log.isDebugEnabled ())
      log.debug ("Method parameter " + parameter.getParameterIndex () + " for "
                 + Methods.brief (parameter.getMethod ()) + " is "
                 + (result ? "" : "not ") + "supported");
    return result;
  }

  /* (non-Javadoc)
   * @see org.springframework.web.servlet.mvc.method.annotation.
   * PathVariableMethodArgumentResolver#resolveName(java.lang.String,
   * org.springframework.core.MethodParameter,
   * org.springframework.web.context.request.NativeWebRequest) */
  @Override
  protected Object resolveName (final String name, MethodParameter parameter, NativeWebRequest request) throws Exception {
    Object value = null, result = null;
    try {
      value = super.resolveName (name, parameter, request);
      if (!(value instanceof String) || !matches (AREA_STRING_REPRESENTATION_PATTERN_EXPRESSION, value.toString ()))
        throw new InvalidDataRequestException ().arguments (value == null ? null : value.toString ());
      String[] split = value.toString ().replace ("[", "").replace ("]", "").split (",");
      String[] rows = split[0].split (":");
      String[] columns = split[1].split (":");
      final int startRow = parseInt (rows[0]);
      final int endRow = parseInt (rows[1]);
      final int startColumn = parseInt (columns[0]);
      final int endColumn = parseInt (columns[1]);
      if (startRow >= endRow || startColumn >= endColumn)
        throw new InvalidDataRequestException ().arguments (value.toString ());
      result = new Area () {
        {
          intervals = asList (new Pair<> (ROW, (Interval) new Interval () {

            @Override
            public int start () {
              return startRow;
            }

            @Override
            public int end () {
              return endRow;
            }
          }), new Pair<> (COLUMN, (Interval) new Interval () {

            @Override
            public int start () {
              return startColumn;
            }

            @Override
            public int end () {
              return endColumn;
            }
          }));
        }

        private @Getter final List<Pair<Axis, Interval>> intervals;
      };
      return result;
    } finally {
      if (log.isDebugEnabled ())
        log.debug ("Resolving " + name + " value " + value + " to area " + result);
    }
  }
}
