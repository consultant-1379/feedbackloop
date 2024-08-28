package com.ericsson.oss.services.cm.scriptengine.ejb.service.alias;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.ericsson.oss.itpf.sdk.core.annotation.EServiceRef;
import com.ericsson.oss.services.cli.alias.model.CliAlias;
import com.ericsson.oss.services.cm.cmreader.api.CmReaderService;
import com.ericsson.oss.services.cm.cmshared.dto.AttributeSpecification;
import com.ericsson.oss.services.cm.cmshared.dto.AttributeSpecificationContainer;
import com.ericsson.oss.services.cm.cmshared.dto.CmObject;
import com.ericsson.oss.services.cm.cmshared.dto.CmObjectSpecification;
import com.ericsson.oss.services.cm.cmshared.dto.CmResponse;
import com.ericsson.oss.services.cm.cmshared.dto.StringifiedAttributeSpecifications;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmMatchCondition;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmSearchCriteria;
import com.ericsson.oss.services.cm.cmwriter.api.CmWriterService;
import com.ericsson.oss.services.cm.scriptengine.ejb.service.alias.exceptions.CannotFindAliasException;
import com.ericsson.oss.services.cm.scriptengine.ejb.service.alias.exceptions.CannotPersistAliasException;

public class AliasPersistence {
    private static final String EXCEPTION_DURING_ALIAS_PERSISTENCE = "Exception during alias persistence";
    private static final String EXCEPTION_DURING_ALIAS_SEARCH = "Exception during alias search";

    private static final String ALIAS_MODEL_NAMESPACE = "OSS_CLI";
    private static final String ALIAS_MODEL_NAMESPACE_VERSION = "1.0.0";
    private static final String ALIAS_MODEL_TYPE = "CliAlias";
    private static final String ALIAS_NAME_FIELD = "name";
    private static final String LIVE_CONFIGURATION = "Live";

    @EServiceRef
    CmWriterService cmWriterService;

    @EServiceRef
    CmReaderService cmReaderService;

    public void saveAlias(final CliAlias cliAlias) {
        final CmResponse cmResponse = cmWriterService.createPersistenceObject(ALIAS_MODEL_NAMESPACE, ALIAS_MODEL_TYPE, ALIAS_MODEL_NAMESPACE_VERSION,
                getCliAliasAttributesForDpsPersistence(cliAlias), LIVE_CONFIGURATION);
        if (cmResponse.getStatusCode() < 0) {
            //run time exception
            throw new CannotPersistAliasException(EXCEPTION_DURING_ALIAS_PERSISTENCE);
        }
    }

    public CliAlias findAlias(final String aliasName) {
        final CmResponse cmResponse = cmReaderService.search(getCliAliasCmSearchCriteria(aliasName), LIVE_CONFIGURATION);
        if (cmResponse.getStatusCode() < 0) {
            //run time exception
            throw new CannotFindAliasException(EXCEPTION_DURING_ALIAS_SEARCH);
        }
        return getCliAliasFromCmResponse(cmResponse);
    }

    public boolean isDefinedAlias(final String aliasName) {
        final CmResponse cmResponse = cmReaderService.search(getCliAliasCmSearchCriteria(aliasName), LIVE_CONFIGURATION);
        return cmResponse.getCmObjects().size() > 0;
    }

    /*
     * P R I V A T E - M E T H O D S
     */
    private AttributeSpecificationContainer getCliAliasAttributeSpecificationContainer(final String aliasName) {
        final AttributeSpecificationContainer cliAliasAttributeSpecificationContainer = new StringifiedAttributeSpecifications();
        cliAliasAttributeSpecificationContainer.addAttributeSpecification(getAttributeSpecification(ALIAS_NAME_FIELD, CmMatchCondition.EQUALS,
                aliasName));
        for (final Field field : CliAlias.class.getFields()) {
            if (!field.getName().equals(ALIAS_NAME_FIELD)) {
                cliAliasAttributeSpecificationContainer.addAttributeSpecification(getAttributeSpecification(field.getName(),
                        CmMatchCondition.NO_MATCH_REQUIRED, null));
            }
        }
        return cliAliasAttributeSpecificationContainer;
    }

    private AttributeSpecification getAttributeSpecification(final String attributeName, final CmMatchCondition cmMatchCondition,
            final Object attributeValue) {
        final AttributeSpecification attributeSpecification = new AttributeSpecification();
        attributeSpecification.setName(attributeName);
        attributeSpecification.setCmMatchCondition(cmMatchCondition);
        attributeSpecification.setValue(attributeValue);
        return attributeSpecification;
    }

    private CmSearchCriteria getCliAliasCmSearchCriteria(final String aliasName) {
        final CmObjectSpecification cliAliasCmObjectSpecification = new CmObjectSpecification();
        cliAliasCmObjectSpecification.setType(ALIAS_MODEL_TYPE);
        cliAliasCmObjectSpecification.setNamespace(ALIAS_MODEL_NAMESPACE);
        // TODO EEITSIK Think about (not) passing in version once we have new model version and backward compatibility...
        cliAliasCmObjectSpecification.setNamespaceVersion(ALIAS_MODEL_NAMESPACE_VERSION);
        cliAliasCmObjectSpecification.setAttributeSpecificationContainer(getCliAliasAttributeSpecificationContainer(aliasName));
        final CmSearchCriteria cmSearchCriteria = new CmSearchCriteria();
        cmSearchCriteria.setSingleCmObjectSpecification(cliAliasCmObjectSpecification);
        return cmSearchCriteria;
    }

    private CliAlias getCliAliasFromCmResponse(final CmResponse cmResponse) {
        final CliAlias cliAlias = new CliAlias();
        final CmObject cmObject = cmResponse.getCmObjects().iterator().next();
        final Map<String, Object> attributes = cmObject.getAttributes();
        try {
            for (final Field field : CliAlias.class.getFields()) {
                field.set(cliAlias, attributes.get(field.getName()));
            }
        } catch (final IllegalArgumentException e) {
            // Cannot happen because we are iterating over its own fields
        } catch (final IllegalAccessException e) {
            // Cannot happen because we are iterating over its own fields
        }
        return cliAlias;
    }

    private Map<String, Object> getCliAliasAttributesForDpsPersistence(final CliAlias cliAlias) {
        final Map<String, Object> attributes = new HashMap<>();
        try {
            for (final Field field : CliAlias.class.getFields()) {
                attributes.put(field.getName(), field.get(cliAlias));
            }
        } catch (final IllegalArgumentException e) {
            // Cannot happen because we are iterating over its own fields
        } catch (final IllegalAccessException e) {
            // Cannot happen because we are iterating over its own fields
        }
        return attributes;
    }
}