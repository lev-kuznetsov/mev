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

package edu.dfci.cccb.mev.googleplus.domain;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import edu.dfci.cccb.mev.googleplus.domain.support.MixinsTest;
import edu.dfci.cccb.mev.googleplus.domain.support.jackson.CircleFeedSerializerTest;
import edu.dfci.cccb.mev.googleplus.domain.support.jackson.CircleSerializerTest;
import edu.dfci.cccb.mev.googleplus.domain.support.jackson.CommentListSerializerTest;
import edu.dfci.cccb.mev.googleplus.domain.support.jackson.CommentReplySerializerTest;
import edu.dfci.cccb.mev.googleplus.domain.support.jackson.CommentSerializerTest;
import edu.dfci.cccb.mev.googleplus.domain.support.jackson.CoverInfoSerializerTest;
import edu.dfci.cccb.mev.googleplus.domain.support.jackson.CoverPhotoSerializerTest;
import edu.dfci.cccb.mev.googleplus.domain.support.jackson.CoverSerializerTest;
import edu.dfci.cccb.mev.googleplus.domain.support.jackson.DateTimeSerializerTest;
import edu.dfci.cccb.mev.googleplus.domain.support.jackson.EmailsSerializerTest;
import edu.dfci.cccb.mev.googleplus.domain.support.jackson.FileListSerializerTest;
import edu.dfci.cccb.mev.googleplus.domain.support.jackson.FileSerializerTest;
import edu.dfci.cccb.mev.googleplus.domain.support.jackson.ImageSerializerTest;
import edu.dfci.cccb.mev.googleplus.domain.support.jackson.LabelsSerializerTest;
import edu.dfci.cccb.mev.googleplus.domain.support.jackson.OrganizationsSerializerTest;
import edu.dfci.cccb.mev.googleplus.domain.support.jackson.PeopleFeedSerializerTest;
import edu.dfci.cccb.mev.googleplus.domain.support.jackson.PeopleSerializerTest;
import edu.dfci.cccb.mev.googleplus.domain.support.jackson.PersonSerializerTest;
import edu.dfci.cccb.mev.googleplus.domain.support.jackson.PlacesLivedSerializerTest;
import edu.dfci.cccb.mev.googleplus.domain.support.jackson.PropertySerializerTest;
import edu.dfci.cccb.mev.googleplus.domain.support.jackson.UrlsSerializerTest;
import edu.dfci.cccb.mev.googleplus.domain.support.jackson.UserSerializerTest;

/**
 * @author levk
 */
@RunWith (Suite.class)
@SuiteClasses ({ MixinsTest.class,
                CircleFeedSerializerTest.class,
                CircleSerializerTest.class,
                CommentListSerializerTest.class,
                CommentReplySerializerTest.class,
                CommentSerializerTest.class,
                CoverInfoSerializerTest.class,
                CoverPhotoSerializerTest.class,
                CoverSerializerTest.class,
                DateTimeSerializerTest.class,
                EmailsSerializerTest.class,
                FileListSerializerTest.class,
                FileSerializerTest.class,
                ImageSerializerTest.class,
                LabelsSerializerTest.class,
                OrganizationsSerializerTest.class,
                PeopleFeedSerializerTest.class,
                PeopleSerializerTest.class,
                PersonSerializerTest.class,
                PlacesLivedSerializerTest.class,
                PropertySerializerTest.class,
                UrlsSerializerTest.class,
                UserSerializerTest.class })
public class GooglePlusDomainTests {}
