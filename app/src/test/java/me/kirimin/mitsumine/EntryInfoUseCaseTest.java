//package me.kirimin.mitsumine;
//
//import android.content.Context;
//
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import me.kirimin.mitsumine.common.domain.model.Bookmark;
//import me.kirimin.mitsumine.common.domain.model.EntryInfo;
//import me.kirimin.mitsumine.entryinfo.EntryInfoRepository;
//import rx.Observable;
//import rx.observers.TestSubscriber;
//
//import static org.mockito.Mockito.*;
//
//public class EntryInfoUseCaseTest {
//
//    EntryInfoRepository dataMock;
//
//    @Before
//    public void setup() {
//        dataMock = mock(EntryInfoRepository.class);
//    }
//
//    @Test
//    public void getHasCommentBookmarkTest() {
//        List<Bookmark> bookmarks = new ArrayList<>();
//        bookmarks.add(new Bookmark("test1", new ArrayList<String>(), "", "comment", ""));
//        bookmarks.add(new Bookmark("test2", new ArrayList<String>(), "", "", ""));
//        bookmarks.add(new Bookmark("test3", new ArrayList<String>(), "", "comment", ""));
//        bookmarks.add(new Bookmark("test4", new ArrayList<String>(), "", "comment", ""));
//        EntryInfoUseCase useCase = new EntryInfoUseCase(dataMock);
//        List<Bookmark> results = useCase.getHasCommentBookmarks(bookmarks);
//        Assert.assertEquals(results.size(), 3);
//        Assert.assertEquals(results.get(0).getUser(), "test1");
//        Assert.assertEquals(results.get(1).getUser(), "test3");
//        Assert.assertEquals(results.get(2).getUser(), "test4");
//    }
//
//    @Test
//    public void requestTest() throws InterruptedException {
//        List<EntryInfo> entryInfos = new ArrayList<>();
//        entryInfos.add(new EntryInfo("test1", 0, "url", "thumUrl", new ArrayList<Bookmark>(), new ArrayList<String>()));
//        entryInfos.add(new EntryInfo("test2", 0, "url", "thumUrl", new ArrayList<Bookmark>(), new ArrayList<String>()));
//        entryInfos.add(new EntryInfo("empty", 0, "url", "thumUrl", new ArrayList<Bookmark>(), new ArrayList<String>()));
//        entryInfos.add(new EntryInfo("test4", 0, "url", "thumUrl", new ArrayList<Bookmark>(), new ArrayList<String>()));
//        when(dataMock.requestEntryInfoApi(any(Context.class), anyString())).thenReturn(Observable.from(entryInfos));
//
//        EntryInfoUseCase useCase = new EntryInfoUseCase(dataMock);
//        TestSubscriber<EntryInfo> testSubscriber = new TestSubscriber<>();
//        useCase.requestEntryInfo("test", mock(Context.class), testSubscriber);
//
//        testSubscriber.awaitTerminalEvent();
//        testSubscriber.assertNoErrors();
//        Assert.assertEquals(testSubscriber.getOnNextEvents().get(0).getTitle(), "test1");
//        Assert.assertEquals(testSubscriber.getOnNextEvents().get(1).getTitle(), "test2");
//        Assert.assertEquals(testSubscriber.getOnNextEvents().get(2).getTitle(), "test4");
//    }
//}
