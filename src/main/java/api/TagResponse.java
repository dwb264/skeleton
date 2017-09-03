package api;

import com.fasterxml.jackson.annotation.JsonProperty;
import generated.tables.records.TagsRecord;

public class TagResponse {
    @JsonProperty
    String tag;

    @JsonProperty
    Integer receiptId;

    public TagResponse(TagsRecord dbRecord) {
        this.tag = dbRecord.getTag();
        this.receiptId = dbRecord.getReceiptid();
    }
}
