package com.ericsson.oss.services.scriptengine.rest.resources;

import static com.ericsson.oss.services.scriptengine.rest.resources.CommandResponseCache.MAX_RESPONSE_CACHE_LINES;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.cache.Cache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.ericsson.oss.services.scriptengine.spi.dtos.AbstractDto;
import com.google.common.collect.ObjectArrays;

@RunWith(MockitoJUnitRunner.class)
public class CommandResponseCacheTest {

    private static final CachedOutput EMPTY_CACHED_OUTPUT = new CachedOutput(new ArrayList<AbstractDto>(0), new ArrayList<AbstractDto>(0));
    public static final String CACHE_KEY = "key";

    @InjectMocks
    CommandResponseCache objectUnderTest;

    @Mock
    Cache<String, CachedOutput> cacheMock;

    @Mock
    AbstractDto a, b, c, d, e;

    @Test
    public void simpleAdd() {

        when(cacheMock.get(CACHE_KEY)).thenReturn(EMPTY_CACHED_OUTPUT);

        objectUnderTest.appendCachedOutput(CACHE_KEY, getDtoListForValues(new AbstractDto[]{a, b}));

        verify(cacheMock).replace(CACHE_KEY, new CachedOutput(getDtoListForValues(new AbstractDto[]{a, b})));
    }


    @Test
    public void concatenate() {
        List<AbstractDto> cachedData = getDtoListForValues(new AbstractDto[]{a, b});
        when(cacheMock.get(CACHE_KEY)).thenReturn(new CachedOutput(cachedData));

        objectUnderTest.appendCachedOutput(CACHE_KEY, getDtoListForValues(new AbstractDto[]{c, d, e}));

        List<AbstractDto> replacementList = getDtoListForValues(new AbstractDto[]{c, d, e, a, b});
        verify(cacheMock).replace(CACHE_KEY, new CachedOutput(replacementList));
    }

    @Test
    public void get() {
        List<AbstractDto> cachedData = getDtoListForValues(new AbstractDto[]{a, b, c, d, e});
        when(cacheMock.get(CACHE_KEY)).thenReturn(new CachedOutput(cachedData));

        final List<AbstractDto> list = objectUnderTest.getMaintainedElements(CACHE_KEY, 2, 5);

        assertEquals(getDtoListForValues(new AbstractDto[]{c, d, e}), list);
    }

    @Test
    public void canAddWhereAdditionalElementsAreLargerThanMaxSize() {
        when(cacheMock.get(CACHE_KEY)).thenReturn(EMPTY_CACHED_OUTPUT);
        final List<AbstractDto> largeListToBeCached = getDtoListForValues(generateLargeArrayOfMocks("LargeList", MAX_RESPONSE_CACHE_LINES * 2));
        objectUnderTest.appendCachedOutput(CACHE_KEY, largeListToBeCached);

        final List<AbstractDto> reducedList = largeListToBeCached.subList(0, MAX_RESPONSE_CACHE_LINES);
        verify(cacheMock).replace(CACHE_KEY,new CachedOutput(reducedList));
    }

    @Test
    public void canAddWhereTotalSizeIsLargerThanMaxSizeElementsAddedBeingTheLargest() {
        final AbstractDto[] preExistingCache = generateLargeArrayOfMocks("pre-existing", 5000);
        when(cacheMock.get(CACHE_KEY)).thenReturn(new CachedOutput(getDtoListForValues(preExistingCache)));

        final AbstractDto[] additionalDtos = generateLargeArrayOfMocks("adding", MAX_RESPONSE_CACHE_LINES - 3000);
        objectUnderTest.appendCachedOutput(CACHE_KEY, getDtoListForValues(additionalDtos));

        verifyCacheAfterDtoAdditionResultingInMaxLength(preExistingCache, additionalDtos);
    }

    @Test
    public void canAddWhereTotalSizeIsLargerThanMaxSizeElementsAlreadyInCacheBeingTheLargest() {
        final AbstractDto[] preExistingCache = generateLargeArrayOfMocks("pre-existing", MAX_RESPONSE_CACHE_LINES - 3000);
        when(cacheMock.get(CACHE_KEY)).thenReturn(new CachedOutput(getDtoListForValues(preExistingCache)));

        final AbstractDto[] additionalDtos = generateLargeArrayOfMocks("adding", 5000);
        objectUnderTest.appendCachedOutput(CACHE_KEY, getDtoListForValues(additionalDtos));

        verifyCacheAfterDtoAdditionResultingInMaxLength(preExistingCache, additionalDtos);
    }

    @Test
    public void willGetEmptyListWhenGetReadOnceElementsWhichHaveNotBeenSet(){
        List<AbstractDto> existingDtos = getDtoListForValues(new AbstractDto[]{a, b});
        when(cacheMock.get(CACHE_KEY)).thenReturn(new CachedOutput(new ArrayList<AbstractDto>(0), existingDtos));
        List<AbstractDto> readOnceDtos = objectUnderTest.getReadOnceElements(CACHE_KEY);
        assertEquals(existingDtos, readOnceDtos);
    }

    @Test
    public void newCachedOutputObjectWillBeCreatedCachedIfNoneExistWhenAppendingCache(){
        when(cacheMock.get(CACHE_KEY)).thenReturn(null);

        final AbstractDto[] additionalDtos = generateLargeArrayOfMocks("adding", 5000);
        objectUnderTest.appendCachedOutput(CACHE_KEY, getDtoListForValues(additionalDtos));
        verify(cacheMock).put(CACHE_KEY,new CachedOutput(new ArrayList<AbstractDto>(0)));
        verify(cacheMock).replace(CACHE_KEY,new CachedOutput(Arrays.asList(additionalDtos)));
    }

    private List<AbstractDto> getDtoListForValues(AbstractDto[] abstractDtos) {
        ArrayList<AbstractDto> abstractDtoList = new ArrayList<>();
        for(AbstractDto dto: abstractDtos){
            abstractDtoList.add(dto);
        }
        return abstractDtoList;
    }

    private void verifyCacheAfterDtoAdditionResultingInMaxLength(final AbstractDto[] preExistingCache, final AbstractDto[] additionalDtos) {
        final int numberOfElementsToRemove = (additionalDtos.length + preExistingCache.length) - MAX_RESPONSE_CACHE_LINES;
        final AbstractDto[] reducedCacheArray = Arrays.copyOfRange(preExistingCache, 0, preExistingCache.length - numberOfElementsToRemove);
        final AbstractDto[] newCache = ObjectArrays.concat(additionalDtos, reducedCacheArray, AbstractDto.class);
        assertEquals(MAX_RESPONSE_CACHE_LINES, newCache.length);
        verify(cacheMock).replace(CACHE_KEY, new CachedOutput(Arrays.asList(newCache)));
    }

    private AbstractDto[] generateLargeArrayOfMocks(final String mockNames, final int numOfElms) {
        final AbstractDto[] abstractDtos = new AbstractDto[numOfElms];
        int cnt = 0;
        do {
            abstractDtos[cnt] = Mockito.mock(AbstractDto.class, mockNames + " " + cnt);
            ++cnt;
        } while (cnt < numOfElms);

        return abstractDtos;
    }
}
