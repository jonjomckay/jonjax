package com.jonjax.example.controllers;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/")
@Singleton
public class HelloController {

    @GET
    @Path("/hello")
    public String hello() {
        return "hello!";
    }
}
