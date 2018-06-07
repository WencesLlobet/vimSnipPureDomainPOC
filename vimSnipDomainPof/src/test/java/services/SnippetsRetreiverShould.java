package services;

import domain.Snippet;
import domain.SnippetTitle;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import services.SnippetRetriever;
import services.SnippetsProviderService;

import java.util.NoSuchElementException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class SnippetsRetreiverShould {


    private SnippetRetriever vimsnip;

    @Mock
    private SnippetsProviderService snippetService;

    private String noVersionTitle = "title";
    private String version1Title = "title 1";
    private String version2Title = "title 2";


    @Before
    public void setUp() {
        vimsnip = new SnippetRetriever(snippetService);
    }

    @Test(expected = NoSuchElementException.class)
    public void
    not_allow_getting_a_nonexistent_snippet() {
        snippetServiceHasNotSnippetWithTitle(version1Title);

        vimsnip.get(version1Title);
    }

    @Test(expected = NoSuchElementException.class)
    public void
    not_allow_getting_newest_version_having_no_versions_at_all() {
        snippetServiceHasNotSnippetWithTitle(version1Title);

        vimsnip.get(version1Title);
    }

    @Test
    public void
    provide_newest_snippet_version_if_it_is_not_specified() {
        snippetServiceHasSnippetWithTitle(version1Title);
        snippetServiceHasSnippetWithTitle(version2Title);
        given(snippetService.get(new SnippetTitle(version2Title))).willReturn(new Snippet(version2Title));

        Snippet retrivedSnippet = vimsnip.get(noVersionTitle);

        assertThat(retrivedSnippet,is(new Snippet(version2Title)));
    }


    private void snippetServiceHasNotSnippetWithTitle(String title2) {
        given(snippetService.hasSnippetWith(new SnippetTitle(title2))).willReturn(false);
    }

    private void snippetServiceHasSnippetWithTitle(String title1) {
        given(snippetService.hasSnippetWith(new SnippetTitle(title1))).willReturn(true);
    }
}