/*
 * SonarQube
 * Copyright (C) 2009-2016 SonarSource SA
 * mailto:contact AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package org.sonar.server.favorite;

import org.sonar.db.DbClient;
import org.sonar.db.DbSession;
import org.sonar.db.property.PropertyDto;
import org.sonar.server.user.UserSession;

public class FavoriteUpdater {
  private static final String PROP_FAVORITE_KEY = "favourite";

  private final DbClient dbClient;
  private final UserSession userSession;

  public FavoriteUpdater(DbClient dbClient, UserSession userSession) {
    this.dbClient = dbClient;
    this.userSession = userSession;
  }

  /**
   * Set favorite to the logged in user. If no user is logged, no action is done
   */
  public void add(DbSession dbSession, long componentDbId) {
    if (!userSession.isLoggedIn()) {
      return;
    }

    dbClient.propertiesDao().saveProperty(dbSession, new PropertyDto()
      .setKey(PROP_FAVORITE_KEY)
      .setResourceId(componentDbId)
      .setUserId(Long.valueOf(userSession.getUserId())));
  }
}
