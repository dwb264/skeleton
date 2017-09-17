package controllers;

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

    @PUT
    @Path("/{tag}")
    public void toggleTag(@PathParam("tag") String tagName, Integer receiptId) {
        tags.put(tagName, receiptId);
    }

    @GET
    @Path("/{tag}")
    public List<ReceiptResponse> getReceiptsWithTag(@PathParam("tag") String tagName) {
        List<ReceiptsRecord> receiptRecords = tags.receiptsWithTag(tagName);
        return receiptRecords.stream().map(ReceiptResponse::new).collect(toList());
    }

    @GET
    public List<TagResponse> getReceipts() {
        List<TagsRecord> tagsRecords = tags.getAllTags();
        return tagsRecords.stream().map(TagResponse::new).collect(toList());
    }
}
