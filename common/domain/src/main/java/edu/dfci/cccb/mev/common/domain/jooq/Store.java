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

package edu.dfci.cccb.mev.common.domain.jooq;

import static com.mycila.inject.internal.guava.collect.ObjectArrays.concat;
import static java.util.UUID.randomUUID;
import static lombok.AccessLevel.PROTECTED;
import static org.jooq.impl.DSL.tableByName;
import static org.jooq.impl.DSL.using;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.sql.DataSource;

import lombok.Getter;
import lombok.Synchronized;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.QueryPart;
import org.jooq.Table;

/**
 * @author levk
 * @since CRYSTAL
 */
@Log4j
@Accessors (fluent = true)
public abstract class Store implements AutoCloseable {

  private @Getter (PROTECTED) DSLContext context;
  private final boolean temporary;
  private @Getter (PROTECTED) final Table<?> table;
  private final Field<?>[] fields;
  private final String[][] indices;

  protected Store (Field<?>[] fields, String[]... indices) {
    this (tableByName (randomUUID ().toString ()), true, fields, indices);
  }

  protected Store (String name, Field<?>[] fields, String[]... indices) {
    this (tableByName (name), false, fields, indices);
  }

  protected Store (Table<?> table, boolean temporary, Field<?>[] fields, String[]... indices) {
    if (fields.length < 1)
      throw new IllegalArgumentException ("New table must have fields");
    this.table = table;
    this.fields = fields;
    this.indices = indices;
    this.temporary = temporary;
  }

  protected Store (DataSource dataSource, Field<?>[] fields, String[]... indices) throws SQLException {
    this (dataSource, tableByName (randomUUID ().toString ()), true, fields, indices);
  }

  protected Store (DataSource dataSource, String name, Field<?>[] fields, String[]... indices) throws SQLException {
    this (dataSource, tableByName (name), false, fields, indices);
  }

  protected Store (DataSource dataSource, Table<?> table, boolean temporary, Field<?>[] fields, String[]... indices) throws SQLException {
    this (table, temporary, fields, indices);
    connect (dataSource);
  }

  @Inject
  @Synchronized
  private void connect (DataSource dataSource) throws SQLException {
    if (context != null)
      throw new IllegalStateException ("DataSource already set");

    context = using (dataSource.getConnection ());

    StringBuilder statement = new StringBuilder ();
    statement.append ("CREATE ");
    if (temporary)
      statement.append ("TEMPORARY ");
    statement.append ("TABLE IF NOT EXISTS {0}(");
    for (int i = 0; i < fields.length; i++) {
      statement.append ("{" + (i + 1) + "} ");
      statement.append (Type.from (fields[i].getType ()));
      statement.append (i < fields.length - 1 ? ", " : ") ");
    }

    if (log.isDebugEnabled ())
      log.debug ("Executing statement " + statement.toString ());
    context.query (statement.toString (), concat (new QueryPart[] { table }, fields, QueryPart.class)).execute ();

    for (int i = 0; i < indices.length; i++) {
      statement = new StringBuilder ();
      List<QueryPart> fields = new ArrayList<> ();
      statement.append ("CREATE UNIQUE INDEX IF NOT EXISTS \"" + randomUUID () + "\" ON {0}(");
      field: for (int j = 0; j < indices[i].length; j++) {
        for (int k = this.fields.length; --k >= 0;)
          if (this.fields[k].getName ().equals (indices[i][j])) {
            fields.add (this.fields[k]);
            continue field;
          }
        throw new IllegalArgumentException ("Cannot index on non existing field " + indices[i][j]);
      }
      for (int j = 0; j < fields.size (); j++)
        statement.append ("{" + (j + 1) + "}").append (j < fields.size () - 1 ? ", " : ")");

      if (log.isDebugEnabled ())
        log.debug ("Executing statement " + statement.toString ());
      context.query (statement.toString (),
                     concat (new QueryPart[] { table }, fields.toArray (new QueryPart[0]), QueryPart.class))
             .execute ();
    }
  }

  private enum Type {
    VARCHAR ("VARCHAR(255)", String.class),
    INT ("INT", Integer.class),
    DOUBLE ("DOUBLE", Double.class);

    private final String sql;
    private final Class<?>[] types;

    private Type (String sql, Class<?>... types) {
      this.sql = sql;
      this.types = types;
    }

    public static String from (Class<?> of) {
      for (Type type : values ())
        for (Class<?> clazz : type.types)
          if (clazz.equals (of))
            return type.sql;
      throw new IllegalArgumentException ("No corresponding SQL type found for " + of);
    }
  }

  /* (non-Javadoc)
   * @see java.lang.AutoCloseable#close() */
  @Override
  public void close () throws Exception {
    if (temporary)
      context.query ("DROP TABLE IF EXISTS {0}", table);
  }

  @SuppressWarnings ("unchecked")
  protected <T> Field<T> field (String name) {
    for (Field<?> field : fields)
      if (field.getName ().equals (name))
        return (Field<T>) field;
    return null;
  }
}
