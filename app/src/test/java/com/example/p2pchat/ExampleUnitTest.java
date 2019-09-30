package com.example.p2pchat;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;

import static com.google.common.truth.Truth.assertThat;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertThat(4).isEqualTo(4 + 2);
    }
    @Test
    public void verify_uuid_unique()
    {
        UUID[] uuids = new UUID[500];
        for(int i = 0; i < 500; i++) {
            uuids[i] = UUID.randomUUID();
        }
        HashSet<UUID> isUnique = new HashSet<UUID>(Arrays.asList(uuids));
        assertThat(uuids.length).isEqualTo(isUnique.size());
    }
}