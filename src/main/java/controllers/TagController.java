package controllers;

import api.CreateTagRequest;
import api.ReceiptResponse;
import api.TagResponse;
import dao.TagDao;
import generated.tables.records.ReceiptsRecord;
import generated.tables.records.TagsRecord;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Path("/tags")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TagController {
    final TagDao tags;

    public TagController(TagDao tags) {
        this.tags = tags;
    }

    @GET /* for testing purposes */
    public List<TagResponse> getTags() {
        List<TagsRecord> tagRecords = tags.getAllTags();
        return tagRecords.stream().map(TagResponse::new).collect(toList());
    }

    @PUT
    @Path("/{tag}")
    public void toggleTag(@PathParam("tag") String tagName, CreateTagRequest tag) {
        tags.put(tagName, tag.receiptId);
    }

    @GET
    @Path("/{tag}")
    public List<ReceiptResponse> getReceiptsWithTag(@PathParam("tag") String tagName) {
        List<ReceiptsRecord> receiptRecords = tags.receiptsWithTag(tagName);
        return receiptRecords.stream().map(ReceiptResponse::new).collect(toList());
    }
}
