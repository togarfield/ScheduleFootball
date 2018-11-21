package com.example.togar.schedulefootball.api

import org.junit.Test
import org.mockito.Mockito

class ApiRepositoryTest {
    @Test
    fun testDoRequest() {
        val apiRepository = Mockito.mock(ApiRepository::class.java)
        val url = "https://www.thesportsdb.com/api/v1/json/1/lookupteam.php?id=133604"
        apiRepository.doRequest(url)
        Mockito.verify(apiRepository).doRequest(url)
    }
}