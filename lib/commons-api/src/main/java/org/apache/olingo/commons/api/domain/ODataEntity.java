/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.olingo.commons.api.domain;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 * OData entity.
 */
public class ODataEntity extends AbstractODataPayload implements ODataInvokeResult {

  private static final long serialVersionUID = 8360640095932811034L;

  /**
   * Entity reference.
   */
  private String reference;

  /**
   * ETag.
   */
  private String eTag;

  /**
   * Media entity flag.
   */
  private boolean mediaEntity = false;

  /**
   * In case of media entity, media content type.
   */
  private String mediaContentType;

  /**
   * In case of media entity, media content source.
   */
  private String mediaContentSource;

  /**
   * Edit link.
   */
  protected URI editLink;

  /**
   * Navigation links (might contain in-line entities or feeds).
   */
  protected final List<ODataLink> navigationLinks = new ArrayList<ODataLink>();

  /**
   * Association links.
   */
  protected final List<ODataLink> associationLinks = new ArrayList<ODataLink>();

  /**
   * Media edit links.
   */
  protected final List<ODataLink> editMediaLinks = new ArrayList<ODataLink>();

  /**
   * Operations (legacy, functions, actions).
   */
  protected final List<ODataOperation> operations = new ArrayList<ODataOperation>();

  /**
   * Entity properties.
   */
  protected final List<ODataProperty> properties = new ArrayList<ODataProperty>();

  /**
   * Constructor.
   *
   * @param name OData entity name.
   */
  public ODataEntity(final String name) {
    super(name);
  }

  /**
   * To request entity references in place of the actual entities, the client issues a GET request with /$ref appended
   * to the resource path.
   * <br />
   * If the resource path does not identify an entity or a collection of entities, the service returns 404 Not Found.
   * <br />
   * If the resource path terminates on a collection, the response MUST be the format-specific representation of a
   * collection of entity references pointing to the related entities. If no entities are related, the response is the
   * format-specific representation of an empty collection.
   * <br />
   * If the resource path terminates on a single entity, the response MUST be the format-specific representation of an
   * entity reference pointing to the related single entity. If the resource path terminates on a single entity and no
   * such entity exists, the service returns 404 Not Found.
   *
   * @return entity reference.
   */
  public String getReference() {
    return reference;
  }

  public void setReference(final String reference) {
    this.reference = reference;
  }

  /**
   * Gets ETag.
   *
   * @return ETag.
   */
  public String getETag() {
    return eTag;
  }

  /**
   * Sets ETag.
   *
   * @param eTag ETag.
   */
  public void setETag(final String eTag) {
    this.eTag = eTag;
  }

  /**
   * Searches for operation with given title.
   *
   * @param title operation to look for
   * @return operation if found with given title, <tt>null</tt> otherwise
   */
  public ODataOperation getOperation(final String title) {
    ODataOperation result = null;
    for (ODataOperation operation : operations) {
      if (title.equals(operation.getTitle())) {
        result = operation;
      }
    }

    return result;
  }

  /**
   * Gets operations.
   *
   * @return operations.
   */
  public List<ODataOperation> getOperations() {
    return this.operations;
  }

  /**
   * Searches for property with given name.
   *
   * @param name property to look for
   * @return property if found with given name, <tt>null</tt> otherwise
   */
  public ODataProperty getProperty(final String name) {
    ODataProperty result = null;

    if (StringUtils.isNotBlank(name)) {
      for (ODataProperty property : properties) {
        if (name.equals(property.getName())) {
          result = property;
        }
      }
    }

    return result;
  }

  /**
   * Returns OData entity properties.
   *
   * @return OData entity properties.
   */
  public List<ODataProperty> getProperties() {
    return properties;
  }

  /**
   * Puts the given link into one of available lists, based on its type.
   *
   * @param link to be added
   * @return <tt>true</tt> if the given link was added in one of available lists
   */
  public boolean addLink(final ODataLink link) {
    boolean result = false;

    switch (link.getType()) {
      case ASSOCIATION:
        result = associationLinks.contains(link) ? false : associationLinks.add(link);
        break;

      case ENTITY_NAVIGATION:
      case ENTITY_SET_NAVIGATION:
        result = navigationLinks.contains(link) ? false : navigationLinks.add(link);
        break;

      case MEDIA_EDIT:
        result = editMediaLinks.contains(link) ? false : editMediaLinks.add(link);
        break;

      default:
    }

    return result;
  }

  /**
   * Removes the given link from any list (association, navigation, edit-media).
   *
   * @param link to be removed
   * @return <tt>true</tt> if the given link was contained in one of available lists
   */
  public boolean removeLink(final ODataLink link) {
    return associationLinks.remove(link) || navigationLinks.remove(link) || editMediaLinks.remove(link);
  }

  /**
   * Returns all entity navigation links (including inline entities / feeds).
   *
   * @return OData entity links.
   */
  public List<ODataLink> getNavigationLinks() {
    return navigationLinks;
  }

  /**
   * Returns all entity association links.
   *
   * @return OData entity links.
   */
  public List<ODataLink> getAssociationLinks() {
    return associationLinks;
  }

  /**
   * Returns all entity media edit links.
   *
   * @return OData entity links.
   */
  public List<ODataLink> getEditMediaLinks() {
    return editMediaLinks;
  }

  /**
   * Returns OData entity edit link.
   *
   * @return entity edit link.
   */
  public URI getEditLink() {
    return editLink;
  }

  /**
   * Sets OData entity edit link.
   *
   * @param editLink edit link.
   */
  public void setEditLink(final URI editLink) {
    this.editLink = editLink;
  }

  @Override
  public URI getLink() {
    return super.getLink() == null ? getEditLink() : super.getLink();
  }

  /**
   * TRUE if read-only entity.
   *
   * @return TRUE if read-only; FALSE otherwise.
   */
  public boolean isReadOnly() {
    return super.getLink() != null;
  }

  /**
   * Checks if the current entity is a media entity.
   *
   * @return 'TRUE' if media entity; 'FALSE' otherwise.
   */
  public boolean isMediaEntity() {
    return mediaEntity;
  }

  /**
   * Sets media entity flag.
   *
   * @param isMediaEntity media entity flag value.
   */
  public void setMediaEntity(final boolean isMediaEntity) {
    this.mediaEntity = isMediaEntity;
  }

  /**
   * Gets media content type.
   *
   * @return media content type.
   */
  public String getMediaContentType() {
    return mediaContentType;
  }

  /**
   * Sets media content type.
   *
   * @param mediaContentType media content type.
   */
  public void setMediaContentType(final String mediaContentType) {
    this.mediaContentType = mediaContentType;
  }

  /**
   * Gets media content source.
   *
   * @return media content source.
   */
  public String getMediaContentSource() {
    return mediaContentSource;
  }

  /**
   * Sets media content source.
   *
   * @param mediaContentSource media content source.
   */
  public void setMediaContentSource(final String mediaContentSource) {
    this.mediaContentSource = mediaContentSource;
  }
}