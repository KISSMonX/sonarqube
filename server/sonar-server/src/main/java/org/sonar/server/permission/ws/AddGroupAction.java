/*
 * SonarQube, open source software quality management tool.
 * Copyright (C) 2008-2014 SonarSource
 * mailto:contact AT sonarsource DOT com
 *
 * SonarQube is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * SonarQube is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package org.sonar.server.permission.ws;

import org.sonar.api.server.ws.Request;
import org.sonar.api.server.ws.Response;
import org.sonar.api.server.ws.WebService;
import org.sonar.core.permission.GlobalPermissions;
import org.sonar.db.DbClient;
import org.sonar.db.DbSession;
import org.sonar.server.permission.PermissionChange;
import org.sonar.server.permission.PermissionUpdater;

import static org.sonar.server.permission.ws.PermissionWsCommons.PARAM_GROUP_ID;
import static org.sonar.server.permission.ws.PermissionWsCommons.PARAM_GROUP_NAME;
import static org.sonar.server.permission.ws.PermissionWsCommons.PARAM_PERMISSION;
import static org.sonar.server.permission.ws.PermissionWsCommons.PARAM_PROJECT_UUID;
import static org.sonar.server.permission.ws.PermissionWsCommons.PARAM_PROJECT_KEY;

public class AddGroupAction implements PermissionsWsAction {

  public static final String ACTION = "add_group";

  private final DbClient dbClient;
  private final PermissionWsCommons permissionWsCommons;
  private final PermissionUpdater permissionUpdater;

  public AddGroupAction(DbClient dbClient, PermissionWsCommons permissionWsCommons, PermissionUpdater permissionUpdater) {
    this.permissionWsCommons = permissionWsCommons;
    this.permissionUpdater = permissionUpdater;
    this.dbClient = dbClient;
  }

  @Override
  public void define(WebService.NewController context) {
    WebService.NewAction action = context.createAction(ACTION)
      .setDescription("Add permission to a group.<br /> " +
        "If the project id is provided, a project permission is created.<br />" +
        "The group name or group id must be provided. <br />" +
        "Requires 'Administer System' permission.")
      .setSince("5.2")
      .setPost(true)
      .setHandler(this);

    action.createParam(PARAM_PERMISSION)
      .setDescription("Permission")
      .setRequired(true)
      .setPossibleValues(GlobalPermissions.ALL);

    action.createParam(PARAM_GROUP_NAME)
      .setDescription("Group name or 'anyone' (whatever the case)")
      .setExampleValue("sonar-administrators");

    action.createParam(PARAM_GROUP_ID)
      .setDescription("Group id")
      .setExampleValue("42");

    action.createParam(PARAM_PROJECT_UUID)
      .setDescription("Project id")
      .setExampleValue("ce4c03d6-430f-40a9-b777-ad877c00aa4d");

    action.createParam(PARAM_PROJECT_KEY)
      .setDescription("Project key")
      .setExampleValue("org.apache.hbas:hbase");
  }

  @Override
  public void handle(Request request, Response response) throws Exception {
    DbSession dbSession = dbClient.openSession(false);
    try {
      PermissionChange permissionChange = permissionWsCommons.buildGroupPermissionChange(dbSession, request);
      permissionUpdater.addPermission(permissionChange);
    } finally {
      dbClient.closeSession(dbSession);
    }

    response.noContent();
  }
}