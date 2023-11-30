/*
 * Album Store API
 * CS6650 Fall 2023
 *
 * OpenAPI spec version: 1.0.0
 * Contact: i.gorton@northeasern.edu
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package io.swagger.client.api;

import io.swagger.client.model.AlbumInfo;
import io.swagger.client.model.AlbumsProfile;
import io.swagger.client.model.ErrorMsg;
import java.io.File;
import io.swagger.client.model.ImageMetaData;
import org.junit.Test;
import org.junit.Ignore;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * API tests for DefaultApi
 */
@Ignore
public class DefaultApiTest {

    private final DefaultApi api = new DefaultApi();

    /**
     * get album by key
     *
     * 
     *
     * @throws Exception
     *          if the Api call fails
     */
    @Test
    public void getAlbumByKeyTest() throws Exception {
        String albumID = null;
//        AlbumInfo response = api.getAlbumByKey(albumID);

        // TODO: test validations
    }
    /**
     * Returns the new key and size of an image in bytes.
     *
     * 
     *
     * @throws Exception
     *          if the Api call fails
     */
    @Test
    public void newAlbumTest() throws Exception {
        File image = null;
        AlbumsProfile profile = null;
//        ImageMetaData response = api.newAlbum(image, profile);

        // TODO: test validations
    }
}
