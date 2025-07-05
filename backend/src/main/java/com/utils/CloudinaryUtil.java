package com.utils;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.servlet.annotation.MultipartConfig;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
@MultipartConfig
public class CloudinaryUtil {

    private static final Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "YOUR_CLOUD_NAME",
            "api_key", "YOUR_API_KEY",
            "api_secret", "YOUR_API_SECRET"
    ));

    public static Map uploadImage(InputStream imageStream, String fileName){
        System.out.println("uploading image");
        try {

//           Convert InputStream to byte[]
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] data = new byte[1024];
            int nRead;
            while((nRead = imageStream.read(data,0, data.length)) != -1){
                buffer.write(data,0, nRead);
            }

            buffer.flush();
            Map uploadResult = cloudinary.uploader().upload(buffer.toByteArray(), ObjectUtils.asMap(
                    "public_id", "school_system/admin/" + fileName,
                    "overwrite", true,
                    "resource_type", "image"
            ));

            System.out.println("image uploaded : " + uploadResult.toString());

//            return (String) uploadResult.get("secure_url");
            return uploadResult;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("image upload failed : " + e.getMessage());
        }
    }

    public static boolean deleteImage(String public_id){
        try{
            Map result = cloudinary.uploader().destroy(public_id, ObjectUtils.emptyMap());
            return "ok".equals(result.get("result"));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
/*
    getting this error:

    ❗️Cannot find Cloudinary platform adapter
        [com.cloudinary.android.UploaderStrategy, com.cloudinary.http5.UploaderStrategy]

    WHY => because we are missing a required HTTP client library adapter in our dependencies. The Cloudinary Java SDK needs one of the following libraries to send HTTP requests.
*/
