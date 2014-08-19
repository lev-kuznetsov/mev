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

package edu.dfci.cccb.mev.googleplus.domain.support.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.api.services.plus.model.Person;

/**
 * @author levk
 * @since CRYSTAL
 */
public class PersonSerializer extends JsonSerializer<Person> {

  /* (non-Javadoc)
   * @see com.fasterxml.jackson.databind.JsonSerializer#handledType() */
  @Override
  public Class<Person> handledType () {
    return Person.class;
  }

  /* (non-Javadoc)
   * @see
   * com.fasterxml.jackson.databind.JsonSerializer#serialize(java.lang.Object,
   * com.fasterxml.jackson.core.JsonGenerator,
   * com.fasterxml.jackson.databind.SerializerProvider) */
  @Override
  public void serialize (Person value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
                                                                                       JsonProcessingException {
    jgen.writeStartObject ();
    jgen.writeStringField ("about", value.getAboutMe ());
    jgen.writeStringField ("birthday", value.getBirthday ());
    jgen.writeStringField ("bragging-rights", value.getBraggingRights ());
    jgen.writeStringField ("location", value.getCurrentLocation ());
    jgen.writeStringField ("display", value.getDisplayName ());
    jgen.writeStringField ("domain", value.getDomain ());
    jgen.writeStringField ("gender", value.getGender ());
    jgen.writeStringField ("id", value.getId ());
    jgen.writeStringField ("language", value.getLanguage ());
    jgen.writeStringField ("nickname", value.getNickname ());
    jgen.writeStringField ("type", value.getObjectType ());
    jgen.writeStringField ("occupation", value.getOccupation ());
    jgen.writeStringField ("relationship", value.getRelationshipStatus ());
    jgen.writeStringField ("skills", value.getSkills ());
    jgen.writeStringField ("tagline", value.getTagline ());
    jgen.writeStringField ("url", value.getUrl ());
    jgen.writeNumberField ("circled-by", value.getCircledByCount ());
    jgen.writeNumberField ("plus-one-count", value.getPlusOneCount ());
    jgen.writeBooleanField ("verified", value.getVerified ());
    jgen.writeBooleanField ("is-plus-user", value.getIsPlusUser ());
    provider.defaultSerializeField ("cover", value.getCover (), jgen);
    provider.defaultSerializeField ("emails", value.getEmails (), jgen);
    provider.defaultSerializeField ("image", value.getImage (), jgen);
    provider.defaultSerializeField ("organizations", value.getOrganizations (), jgen);
    provider.defaultSerializeField ("places-lived", value.getPlacesLived (), jgen);
    provider.defaultSerializeField ("urls", value.getUrls (), jgen);
    jgen.writeEndObject ();
  }
}
