package controllers;

import dao.TagDao;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class TagControllerTest {
    @Mock private TagDao tagDao;
    @Rule public MockitoRule rule = MockitoJUnit.rule();

    @Test
    public void testToggleTag() {
        // test that toggleTag() calls put() in TagDao
        MockitoAnnotations.initMocks(this);
        TagController tagController = new TagController(tagDao);
        tagController.toggleTag("test",1);
        Mockito.verify(tagDao).put("test", 1);
    }

    @Test
    public void testGetReceiptsWithTag() {
        // test that getReceiptsWithTag() calls receiptsWithTag() in TagDao
        MockitoAnnotations.initMocks(this);
        TagController tagController = new TagController(tagDao);
        tagController.getReceiptsWithTag("test");
        Mockito.verify(tagDao).receiptsWithTag("test");
    }
}

// Reference: https://examples.javacodegeeks.com/core-java/mockito/mockito-mock-database-connection-example/