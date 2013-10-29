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
package edu.dfci.cccb.mev.web.context;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableCollection;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import edu.dfci.cccb.mev.api.client.annotation.Client;
import edu.dfci.cccb.mev.api.client.annotation.Client.Javascript;
import edu.dfci.cccb.mev.api.client.annotation.Client.Static;
import edu.dfci.cccb.mev.api.client.annotation.Client.View;
import edu.dfci.cccb.mev.api.client.annotation.ClientContext;
import edu.dfci.cccb.mev.api.server.annotation.Server;
import edu.dfci.cccb.mev.api.server.annotation.ServerContext;

@Accessors (fluent = true, chain = false)
@Log4j
public class MevContext implements ClientContext, ServerContext {

  private @Getter final Collection<Class<?>> configurations;
  private @Getter final Collection<Static> resources;
  private @Getter final Collection<Javascript> injectors;
  private @Getter final Collection<View> views;

  {
    Collection<Static> resources = new HashSet<> ();
    Collection<Javascript> injectors = new HashSet<> ();
    Collection<View> views = new HashSet<> ();
    Collection<Class<?>> configurations = new HashSet<> ();

    for (BeanDefinition definition : new ClassPathScanningCandidateComponentProvider (false) {
      {
        addIncludeFilter (new AnnotationTypeFilter (Client.class));
        addIncludeFilter (new AnnotationTypeFilter (Server.class));
      }

      /* (non-Javadoc)
       * @see org.springframework.context.annotation.
       * ClassPathScanningCandidateComponentProvider
       * #isCandidateComponent(org.springframework
       * .core.type.classreading.MetadataReader) */
      @Override
      protected boolean isCandidateComponent (MetadataReader metadataReader) throws IOException {
        try {
          return super.isCandidateComponent (metadataReader);
        } catch (IOException e) {
          return false;
        }
      }

      /* (non-Javadoc)
       * @see org.springframework.context.annotation.
       * ClassPathScanningCandidateComponentProvider
       * #isCandidateComponent(org.springframework
       * .beans.factory.annotation.AnnotatedBeanDefinition) */
      @Override
      protected boolean isCandidateComponent (AnnotatedBeanDefinition beanDefinition) {
        return true;
      }

      /* (non-Javadoc)
       * @see org.springframework.context.annotation.
       * ClassPathScanningCandidateComponentProvider
       * #findCandidateComponents(java.lang.String) */
      @Override
      public Set<BeanDefinition> findCandidateComponents (String basePackage) {
        if (null == basePackage || "".equals (basePackage)) {
          Set<BeanDefinition> result = new HashSet<> ();
          for (String domain : ("ac:ad:ae:aero:af:ag:ai:al:am:an:ao:aq:ar:arpa:as" +
                                ":asia:at:au:aw:ax:az:ba:bb:bd:be:bf:bg:bh:bi:biz" +
                                ":bj:bm:bn:bo:br:bs:bt:bv:bw:by:bz:ca:cat:cc:cd:cf" +
                                ":cg:ch:ci:ck:cl:cm:cn:co:com:coop:cr:cu:cv:cw:cx" +
                                ":cy:cz:de:dj:dk:dm:do:dz:ec:edu:ee:eg:er:es:et:eu" +
                                ":fi:fj:fk:fm:fo:fr:ga:gb:gd:ge:gf:gg:gh:gi:gl:gm" +
                                ":gn:gov:gp:gq:gr:gs:gt:gu:gw:gy:hk:hm:hn:hr:ht:hu" +
                                ":id:ie:il:im:in:info:int:io:iq:ir:is:it:je:jm:jo" +
                                ":jobs:jp:ke:kg:kh:ki:km:kn:kp:kr:kw:ky:kz:la:lb" +
                                ":lc:li:lk:lr:ls:lt:lu:lv:ly:ma:mc:md:me:mg:mh:mil" +
                                ":mk:ml:mm:mn:mo:mobi:mp:mq:mr:ms:mt:mu:museum:mv" +
                                ":mw:mx:my:mz:na:name:nc:ne:net:nf:ng:ni:nl:no:np" +
                                ":nr:nu:nz:om:org:pa:pe:pf:pg:ph:pk:pl:pm:pn:post" +
                                ":pr:pro:ps:pt:pw:py:qa:re:ro:rs:ru:rw:sa:sb:sc:sd" +
                                ":se:sg:sh:si:sj:sk:sl:sm:sn:so:sr:st:su:sv:sx:sy" +
                                ":sz:tc:td:tel:tf:tg:th:tj:tk:tl:tm:tn:to:tp:tr" +
                                ":travel:tt:tv:tw:tz:ua:ug:uk:us:uy:uz:va:vc:ve" +
                                ":vg:vi:vn:vu:wf:ws:xxx:ye:yt:za:zm:zw").split (":"))
            result.addAll (super.findCandidateComponents (domain));
          return result;
        } else
          return super.findCandidateComponents (basePackage);
      }
    }.findCandidateComponents (""))
      try {
        Class<?> clazz = Class.forName (definition.getBeanClassName ());
        if (clazz != null) {
          Package packege = clazz.getPackage ();
          if (packege != null) {
            Client client = packege.getAnnotation (Client.class);
            if (client != null) {
              log.info ("Found " + client + " in " + packege);
              resources.addAll (asList (client.resources ()));
              injectors.addAll (asList (client.javascript ()));
              views.addAll (asList (client.views ()));
            }

            Server server = packege.getAnnotation (Server.class);
            if (server != null) {
              log.info ("Found " + server + " in " + packege);
              configurations.addAll (asList (server.configurations ()));
            }
          }
        }
      } catch (ClassNotFoundException e) {
        log.error ("Failure on candidate scan", e);
      }

    this.configurations = unmodifiableCollection (configurations);
    this.injectors = unmodifiableCollection (injectors);
    this.resources = unmodifiableCollection (resources);
    this.views = unmodifiableCollection (views);
  }
}