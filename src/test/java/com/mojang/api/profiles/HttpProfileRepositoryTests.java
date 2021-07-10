package com.mojang.api.profiles;

import com.google.gson.Gson;
import com.mojang.api.http.HttpBody;
import com.mojang.api.http.HttpClient;
import com.mojang.api.http.HttpHeader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.io.IOException;
import java.net.URL;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HttpProfileRepositoryTests {

    private HttpClient client;
    private Gson gson = new Gson();

    @Test
    public void findProfilesByCriteria_someProfileNames_returnsExpectedProfiles() throws Exception{
        client = mock(HttpClient.class);
        String someAgent = "someAgent";

        Profile someProfile = getProfile("someName");
        Profile someOtherProfile = getProfile("someOtherName");
        Profile[] profiles = {someProfile, someOtherProfile};

        setProfilesForUrl(client, new URL("https://api.mojang.com/profiles/" + someAgent), profiles);
        ProfileRepository repository = new HttpProfileRepository(someAgent, client);

        Profile[] actual = repository.findProfilesByNames("someName", "someOtherName");

        Assertions.assertEquals(actual.length, 2);
        Assertions.assertEquals(actual[0].getName(), "someName");
        Assertions.assertEquals(actual[1].getName(), "someOtherName");
    }

    private void setProfilesForUrl(HttpClient mock, URL url, Profile[] profiles) throws IOException {
        String jsonString = gson.toJson(profiles);
        when(mock.post(eq(url), any(HttpBody.class), ArgumentMatchers.<HttpHeader>anyList())).thenReturn(jsonString);
    }

    private static Profile getProfile(String name) {
        Profile profile = new Profile();
        profile.setName(name);
        return profile;
    }

}
