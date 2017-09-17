package dao;

import generated.tables.records.ReceiptsRecord;
import generated.tables.records.TagsRecord;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.util.List;

import static com.google.common.base.Preconditions.checkState;
import static generated.Tables.RECEIPTS;
import static generated.Tables.TAGS;

public class TagDao {
    DSLContext dsl;

    public TagDao(Configuration jooqConfig) {
        this.dsl = DSL.using(jooqConfig);
    }

    public void put(String tagName, Integer receiptId) {

        TagsRecord tagsRecord = dsl.selectFrom(TAGS)
                .where(TAGS.TAG.equal(tagName), TAGS.RECEIPTID.equal(receiptId))
                .fetchOne();

        if (tagsRecord == null) {
            // add tag
            dsl.insertInto(TAGS, TAGS.TAG, TAGS.RECEIPTID)
                    .values(tagName, receiptId)
                    .execute();

            // check insert was successful
            TagsRecord tagCheck = dsl.selectFrom(TAGS)
                    .where(TAGS.TAG.equal(tagName), TAGS.RECEIPTID.equal(receiptId))
                    .fetchOne();
            checkState(tagCheck != null, "Failed to add tag");

        } else {
            // delete tag
            dsl.deleteFrom(TAGS)
                    .where(TAGS.TAG.equal(tagName), TAGS.RECEIPTID.equal(receiptId))
                    .execute();
            TagsRecord tagCheck = dsl.selectFrom(TAGS)
                    .where(TAGS.TAG.equal(tagName), TAGS.RECEIPTID.equal(receiptId))
                    .fetchOne();
            checkState(tagCheck == null, "Failed to remove tag");
        }

    }

    public List<ReceiptsRecord> receiptsWithTag(String tagName) {
        // Get all receiptIds from tag table that correspond to a given tag
        List<Integer> receiptIds = dsl
                .select(TAGS.RECEIPTID)
                .from(TAGS)
                .where(TAGS.TAG.eq(tagName))
                .fetch(TAGS.RECEIPTID);

        // Get all receipts from receipt table with those ids
        return dsl.selectFrom(RECEIPTS).where(RECEIPTS.ID.in(receiptIds)).fetch();
    }


    public List<TagsRecord> getAllTags() {
        return dsl.selectFrom(TAGS).fetch();
    }
}
