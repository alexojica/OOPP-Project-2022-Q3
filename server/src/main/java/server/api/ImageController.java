package server.api;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/api/images")
public class ImageController {
    /**
     * Api endpoint which returns a byte array of the image at the specified locaion.
     * The attribute produces in GetMapping refers to the 'content-type' http header
     * @param path of the resource image
     * @return
     * @throws IOException
     */
    @GetMapping(
            value = "/getImageByActivityId",
            produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
    @ResponseBody
    public byte[] getImageByActivityId(@RequestParam String path) throws IOException {
        System.out.println("Image path is " + path);
        InputStream imageStream = getClass().getResourceAsStream("/activitiesImages/" + path);

        byte[] imageData = imageStream.readAllBytes();
        return imageData;
    }
}
