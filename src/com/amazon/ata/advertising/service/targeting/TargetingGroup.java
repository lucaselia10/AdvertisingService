package com.amazon.ata.advertising.service.targeting;

import com.amazon.ata.advertising.service.targeting.predicate.TargetingPredicate;

import com.amazon.ata.advertising.service.targeting.predicate.TargetingPredicateTypeConverter;

import com.amazonaws.services.dynamodbv2.datamodeling.*;

import java.util.List;

/**
 * A targeting group for an advertisement, required to show if this advertisement should be rendered.
 */
@DynamoDBTable(tableName = "TargetingGroups")
public class TargetingGroup {
    public static final String CONTENT_ID_INDEX = "ContentIdIndex";

    @DynamoDBHashKey(attributeName = "TargetingGroupId")
    private String targetingGroupId;

    @DynamoDBIndexHashKey(globalSecondaryIndexName = CONTENT_ID_INDEX, attributeName = "ContentId")
    private String contentId;

    @DynamoDBAttribute(attributeName = "ClickThroughRate")
    private double clickThroughRate;

    @DynamoDBAttribute(attributeName = "TargetingPredicates")
    @DynamoDBTypeConverted(converter = TargetingPredicateTypeConverter.class)
    private List<TargetingPredicate> targetingPredicates;

    /**
     * Creates a TargetingGroup.
     * @param targetingGroupId The ID specifically for this targeting group
     * @param contentId The ID of the content this metadata is tied to.
     * @param clickThroughRate The probability a customer will click on this advertisement.
     * @param targetingPredicates All of the targeting predicates that must be TRUE to show this advertisement.
     */
    public TargetingGroup(String targetingGroupId,
                          String contentId,
                          double clickThroughRate,
                          List<TargetingPredicate> targetingPredicates) {

        this.targetingGroupId = targetingGroupId;
        this.contentId = contentId;
        this.clickThroughRate = clickThroughRate;
        this.targetingPredicates = targetingPredicates;
    }

    /**
     * Creates an empty TargetingGroup.
     */
    public TargetingGroup() {}

    public String getTargetingGroupId() {
        return targetingGroupId;
    }

    public void setTargetingGroupId(String targetingGroupId) {
        this.targetingGroupId = targetingGroupId;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public double getClickThroughRate() {
        return clickThroughRate;
    }

    public void setClickThroughRate(double clickThroughRate) {
        this.clickThroughRate = clickThroughRate;
    }

    public List<TargetingPredicate> getTargetingPredicates() {
        return targetingPredicates;
    }

    public void setTargetingPredicates(List<TargetingPredicate> targetingPredicates) {
        this.targetingPredicates = targetingPredicates;
    }
    public TargetingGroup(Builder builder) {
        this.contentId = builder.contentId;
        this.targetingPredicates = builder.targetingPredicates;
        this.targetingGroupId = builder.targetingGroupId;
        this.clickThroughRate = builder.clickThroughRate;
    }

    public static Builder builder() {return new Builder();}

    public static final class Builder {
        private String targetingGroupId;
        private String contentId;
        private double clickThroughRate;
        private List<TargetingPredicate> targetingPredicates;

        private Builder() {

        }

        public Builder withTargetingGroupId(String targetingGroupIdToUse) {
            this.targetingGroupId = targetingGroupIdToUse;
            return this;
        }

        public Builder withContentId(String contentIdToUse) {
            this.contentId = contentIdToUse;
            return this;
        }

        public Builder withClickThroughRate(double clickThroughRateToUse) {
            this.clickThroughRate = clickThroughRateToUse;
            return this;
        }

        public Builder withTargetingPredicates(List<TargetingPredicate> targetingPredicatesToUse) {
            this.targetingPredicates = targetingPredicatesToUse;
            return this;
        }

        public TargetingGroup build() { return new TargetingGroup(this); }
    }
}
