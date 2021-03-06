package controllers;

import api.ReceiptSuggestionResponse;
import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;

import java.math.BigDecimal;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import org.hibernate.validator.constraints.NotEmpty;

@Path("/images")
@Consumes(MediaType.TEXT_PLAIN)
@Produces(MediaType.APPLICATION_JSON)
public class ReceiptImageController {
    private final AnnotateImageRequest.Builder requestBuilder;

    public ReceiptImageController() {
        // DOCUMENT_TEXT_DETECTION is not the best or only OCR method available
        Feature ocrFeature = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build();
        this.requestBuilder = AnnotateImageRequest.newBuilder().addFeatures(ocrFeature);

    }

    /**
     * This borrows heavily from the Google Vision API Docs.  See:
     * https://cloud.google.com/vision/docs/detecting-fulltext
     *
     * YOU SHOULD MODIFY THIS METHOD TO RETURN A ReceiptSuggestionResponse:
     *
     * public class ReceiptSuggestionResponse {
     *     String merchantName;
     *     String amount;
     * }
     */
    @POST
    public ReceiptSuggestionResponse parseReceipt(@NotEmpty String base64EncodedImage) throws Exception {

        Image img = Image.newBuilder().setContent(ByteString.copyFrom(Base64.getDecoder().decode(base64EncodedImage))).build();
        AnnotateImageRequest request = this.requestBuilder.setImage(img).build();

        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse responses = client.batchAnnotateImages(Collections.singletonList(request));
            AnnotateImageResponse res = responses.getResponses(0);

            String merchantName = null;
            BigDecimal amount = null;

            List<EntityAnnotation> annotationsList = res.getTextAnnotationsList();

            // Top-most non-decimal text is the merchant
            for (EntityAnnotation annotation : annotationsList) {

                if (res.getTextAnnotationsList().indexOf(annotation) == 0) continue; // ignore the 0th item

                String description = annotation.getDescription();

                try {
                    BigDecimal bd = new BigDecimal(description);
                    // Double.parseDouble(description);
                } catch (NumberFormatException e) {
                    // description is NOT a bigdecimal
                    merchantName = description;
                    break;
                }
            }

            // bottom-most decimal text is the total amount
            for (int i = res.getTextAnnotationsCount() - 1; i > 0; i--) {
                try {
                    // Check if it is a BigDecimal
                    // Remove $ sign to account for dollar amounts
                    String desc = annotationsList.get(i).getDescription().replace("$", "");
                    BigDecimal bd = new BigDecimal(desc);
                    // description is a bigdecimal
                    amount = bd;
                    break;
                } catch(NumberFormatException e) {
                    // Not a BigDecimal
                    continue;
                }
            }

            return new ReceiptSuggestionResponse(merchantName, amount);
        }
    }

}