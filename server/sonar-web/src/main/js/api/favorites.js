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
/* @flow */
import { post, requestDelete, getJSON } from '../helpers/request';

export const getFavorites = (): Promise<Object> => (
    getJSON('/api/favourites')
);

export function addFavorite (componentKey: string) {
  const url = '/api/favorites/add';
  const data = { component: componentKey };
  return post(url, data);
}

export function removeFavorite (componentKey: string) {
  const url = '/api/favorites/remove';
  const data = { component: componentKey };
  return post(url, data);
}
