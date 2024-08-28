package com.ericsson.oss.services.cm.script.engine.test.dummyhandler;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import com.ericsson.oss.itpf.sdk.core.annotation.EService;
import com.ericsson.oss.services.cm.cmreader.api.CmReaderService;
import com.ericsson.oss.services.cm.cmshared.dto.ActionSpecification;
import com.ericsson.oss.services.cm.cmshared.dto.AttributeSpecificationContainer;
import com.ericsson.oss.services.cm.cmshared.dto.AttributeSpecificationContainerPerMoType;
import com.ericsson.oss.services.cm.cmshared.dto.CmDescriptionResponse;
import com.ericsson.oss.services.cm.cmshared.dto.CmObject;
import com.ericsson.oss.services.cm.cmshared.dto.CmObjectSpecification;
import com.ericsson.oss.services.cm.cmshared.dto.CmOutputSpecification;
import com.ericsson.oss.services.cm.cmshared.dto.CmResponse;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmSearchCriteria;
import com.ericsson.oss.services.cm.cmwriter.api.CmWriterService;

@EService
@Stateless
public class DummyReaderWriter implements CmReaderService, CmWriterService {

    @Override
    public CmResponse createManagedObject(String arg0, CmObjectSpecification arg1, String arg2) {
        return new CmResponse();
    }

    @Override
    public CmResponse createMibRoot(String arg0, CmObjectSpecification arg1, String arg2) {
        return new CmResponse();
    }

    @Override
    public CmResponse createNodeRootMib(CmObjectSpecification arg0, String arg1) {
        return new CmResponse();
    }

    @Override
    public CmResponse createPersistenceObject(String arg0, String arg1, String arg2, Map<String, Object> arg3, String arg4) {
        return new CmResponse();
    }

    @Override
    public CmResponse deleteBatchManagedObjects(Collection<CmObject> arg0, String arg1) {
        return new CmResponse();
    }

    @Override
    public CmResponse deleteBatchManagedObjectsWithDescendants(Collection<CmObject> arg0, String arg1) {
        return new CmResponse();
    }

    @Override
    public CmResponse deleteManagedObject(String arg0, String arg1) {
        return new CmResponse();
    }

    @Override
    public CmResponse deleteManagedObjectWithDescendants(String arg0, String arg1) {
        return new CmResponse();
    }

    @Override
    public CmResponse deletePersistenceObjects(CmSearchCriteria arg0, String arg1) {
        return new CmResponse();
    }

    @Override
    public CmResponse deletePersistenceObjectsWithDescendants(CmSearchCriteria arg0, String arg1) {
        return new CmResponse();
    }

    @Override
    public CmResponse performAction(String arg0, ActionSpecification arg1) {
        return new CmResponse();
    }

    @Override
    public CmResponse performAction(CmSearchCriteria arg0, ActionSpecification arg1) {
        return new CmResponse();
    }

    @Override
    public CmResponse performBatchActions(Collection<CmObject> arg0, ActionSpecification arg1) {
        return new CmResponse();
    }

    @Override
    public CmResponse setManagedObjectAttributes(String arg0, AttributeSpecificationContainer arg1, String arg2) {
        return new CmResponse();
    }

    @Override
    public CmResponse setManagedObjectsAttributesBatch(Collection<CmObject> arg0, Map<String, Map<String, Object>> arg1, String arg2) {
        return new CmResponse();
    }

    @Override
    public CmResponse setManagedObjectsAttributesInBatches(CmSearchCriteria arg0, AttributeSpecificationContainerPerMoType arg1, String arg2) {
        return new CmResponse();
    }

    @Override
    public CmResponse setManagedObjectsAttributesInOneTx(CmSearchCriteria arg0, AttributeSpecificationContainerPerMoType arg1, String arg2) {
        return new CmResponse();
    }

    @Override
    public CmDescriptionResponse getDescriptions(List<CmObjectSpecification> arg0) {
        return new CmDescriptionResponse();
    }

    @Override
    public CmDescriptionResponse getDescriptions(CmObjectSpecification arg0) {
        return new CmDescriptionResponse();
    }

    @Override
    public CmResponse getMoByFdn(String arg0, String arg1) {
        return new CmResponse();
    }

    @Override
    public CmResponse getPosByPoIds(List<Long> arg0, String arg1) {
        return new CmResponse();
    }

    @Override
    public CmResponse search(CmSearchCriteria arg0, String arg1) {
        return new CmResponse();
    }

    @Override
    public CmResponse search(CmSearchCriteria arg0, List<CmOutputSpecification> arg1, String arg2) {
        return new CmResponse();
    }

    @Override
    public CmResponse search(CmSearchCriteria arg0, CmOutputSpecification arg1, String arg2) {
        return new CmResponse();
    }

}
