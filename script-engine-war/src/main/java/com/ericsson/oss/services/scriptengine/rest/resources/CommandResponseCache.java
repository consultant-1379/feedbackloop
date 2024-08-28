package com.ericsson.oss.services.scriptengine.rest.resources;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.cache.Cache;
import javax.enterprise.context.ApplicationScoped;

import com.ericsson.oss.itpf.modeling.annotation.cache.CacheMode;
import com.ericsson.oss.itpf.modeling.annotation.cache.CacheVisibilityType;
import com.ericsson.oss.itpf.modeling.annotation.cache.EvictionStrategy;
import com.ericsson.oss.itpf.sdk.cache.classic.CacheProviderBean;
import com.ericsson.oss.itpf.sdk.cache.classic.PersistentCacheStoreBean;
import com.ericsson.oss.services.scriptengine.spi.dtos.AbstractDto;

@ApplicationScoped
public class CommandResponseCache implements Serializable {
    public static final long serialVersionUID = 1L;

    static final int MAX_RESPONSE_CACHE_LINES = 10000;
    static final String CACHE_NAME = "CommandResponseCache";
    static final int TIME_TO_LIVE = Integer.MAX_VALUE;
    static final int NUMBER_OF_DISTRIBUTED_OWNERS = 3;
    static final String MAX_ENTRIES = "199";
    static final PersistentCacheStoreBean PERSISTENT_CACHE_STORE_BEAN = null;
    static final boolean TRANSACTIONAL = true;

    private Cache<String, CachedOutput> cache;

    @PostConstruct
    public void createCache() {
        // TODO emulleo Use injection for below bean 
        final CacheProviderBean cacheProviderBean = new CacheProviderBean();
        cache = cacheProviderBean.createOrGetCache(CACHE_NAME, TIME_TO_LIVE, MAX_ENTRIES, EvictionStrategy.LRU, TRANSACTIONAL,
                CacheMode.REPLICATED_SYNC, NUMBER_OF_DISTRIBUTED_OWNERS, CacheVisibilityType.CLUSTER, PERSISTENT_CACHE_STORE_BEAN);
    }

    public void appendCachedOutput(final String key, final List<AbstractDto> newElements) {
    	appendCachedOutput(key, newElements, new ArrayList<AbstractDto>(0));

    }
    public void appendCachedOutput(final String key, final List<AbstractDto> elementsToAppend, final List<AbstractDto> readOnceDtos) {
        List<AbstractDto> replacementList = new ArrayList<>();
        final List<AbstractDto> cachedArray = getCachedOutput(key).getMaintainedElements();

        if (elementsToAppend.size() >= MAX_RESPONSE_CACHE_LINES) {
            replacementList = elementsToAppend.subList(0,MAX_RESPONSE_CACHE_LINES);
        } else if (elementsToAppend.size() + cachedArray.size()> MAX_RESPONSE_CACHE_LINES) {
            final int elementsToRemove = Math.abs(elementsToAppend.size() + cachedArray.size() - MAX_RESPONSE_CACHE_LINES);
            replacementList.addAll(elementsToAppend);
            replacementList.addAll(cachedArray.subList(0, cachedArray.size() - elementsToRemove));
        } else {
            replacementList.addAll(elementsToAppend);
            replacementList.addAll(cachedArray);
        }
        cache.replace(key, new CachedOutput(replacementList, readOnceDtos));
    }

    public List<AbstractDto> getMaintainedElements(final String key, final int index, final int offset) {
        final CachedOutput cachedOutput = getCachedOutput(key);
        final List<AbstractDto>maintainedElements = cachedOutput.getMaintainedElements();
        final int toIndex = Math.min(index + offset, maintainedElements.size());
        final int fromIndex = Math.min(index, toIndex);
        return maintainedElements.subList(fromIndex, toIndex);
    }

    public List<AbstractDto> getReadOnceElements(final String key){
        return getCachedOutput(key).removeReadOnceElements();
    }

    private CachedOutput getCachedOutput(final String key) {
        final CachedOutput cachedOutput =  cache.get(key);
        if(cachedOutput == null){
            final CachedOutput cachedOutputInit = new CachedOutput(new ArrayList<AbstractDto>(0));
            //NOTE: a value must first be put in for the key in order to use the replace method later
            cache.put(key,cachedOutputInit);
            return cachedOutputInit;
        }
        return cachedOutput;
    }

}
