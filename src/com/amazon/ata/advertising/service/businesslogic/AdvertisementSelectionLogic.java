package com.amazon.ata.advertising.service.businesslogic;

import com.amazon.ata.advertising.service.dao.ReadableDao;
import com.amazon.ata.advertising.service.model.*;
import com.amazon.ata.advertising.service.targeting.TargetingEvaluator;
import com.amazon.ata.advertising.service.targeting.TargetingGroup;

import com.amazon.ata.advertising.service.targeting.TargetingGroupComparator;
import com.amazon.ata.advertising.service.targeting.predicate.TargetingPredicate;
import com.amazon.ata.advertising.service.targeting.predicate.TargetingPredicateResult;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.inject.Inject;

/**
 * This class is responsible for picking the advertisement to be rendered.
 */
public class AdvertisementSelectionLogic {

    private static final Logger LOG = LogManager.getLogger(AdvertisementSelectionLogic.class);

    private final ReadableDao<String, List<AdvertisementContent>> contentDao;
    private final ReadableDao<String, List<TargetingGroup>> targetingGroupDao;
    private Random random = new Random();

    /**
     * Constructor for AdvertisementSelectionLogic.
     * @param contentDao Source of advertising content.
     * @param targetingGroupDao Source of targeting groups for each advertising content.
     */
    @Inject
    public AdvertisementSelectionLogic(ReadableDao<String, List<AdvertisementContent>> contentDao,
                                       ReadableDao<String, List<TargetingGroup>> targetingGroupDao) {
        this.contentDao = contentDao;
        this.targetingGroupDao = targetingGroupDao;
    }

    /**
     * Setter for Random class.
     * @param random generates random number used to select advertisements.
     */
    public void setRandom(Random random) {
        this.random = random;
    }

    /**
     * Gets all of the content and metadata for the marketplace and determines which content can be shown.  Returns the
     * eligible content with the highest click through rate.  If no advertisement is available or eligible, returns an
     * EmptyGeneratedAdvertisement.
     *
     * @param customerId - the customer to generate a custom advertisement for
     * @param marketplaceId - the id of the marketplace the advertisement will be rendered on
     * @return an advertisement customized for the customer id provided, or an empty advertisement if one could
     *     not be generated.
     */
    public GeneratedAdvertisement selectAdvertisement(String customerId, String marketplaceId) {
        GeneratedAdvertisement generatedAdvertisement = new EmptyGeneratedAdvertisement();
        if (StringUtils.isEmpty(marketplaceId)) {
            LOG.warn("MarketplaceId cannot be null or empty. Returning empty ad.");
        } else {
            // Gets list of AdvertisementContent for the marketplaceId
            final List<AdvertisementContent> contents = contentDao.get(marketplaceId);

//            TreeMap<TargetingGroup, String> adMap = new TreeMap<TargetingGroup, String>(new TargetingGroupComparator());
//            for (AdvertisementContent ad : contents) {
//                // Gets list of TargetingGroup associated with the AdvertisementContent's contentId
//                List<TargetingGroup> targetingGroups = targetingGroupDao.get(ad.getContentId());
//                for (TargetingGroup group : targetingGroups) {
//                    // Puts the TargetingGroup's into the Sorted TreeMap by their clickthrough rate
//                    adMap.put(group, group.getContentId());
//                }
//            }
//
//            for (Map.Entry<TargetingGroup, String> entry : adMap.entrySet()) {
//                TargetingEvaluator targetingEvaluator = new TargetingEvaluator(new RequestContext(customerId, marketplaceId));
//                TargetingPredicateResult result = null;
//                try {
//                   result = targetingEvaluator.evaluate(entry.getKey());
//                } catch (ExecutionException e) {
//                    e.printStackTrace();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                if (result.isTrue()) {
//
//            }



//                    .anyMatch(targetingGroup -> {
//                                TargetingEvaluator targetingEvaluator = new TargetingEvaluator(new RequestContext(customerId, marketplaceId));
//                                TargetingPredicateResult result = null;
//                                try {
//                                    result = targetingEvaluator.evaluate(targetingGroup);
//                                } catch (ExecutionException e) {
//                                    e.printStackTrace();
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
//                                return result.isTrue();
//                            })
//                    .collect(Collectors.toList());


            if (CollectionUtils.isNotEmpty(contents)) {
                LisAdvertisementContent> advertisementContents = contents.stream()
                        .filter(advertisementContent -> targetingGroupDao.get(advertisementContent.getContentId()).stream()
                                .anyMatch(targetingGroup -> {
                                    TargetingEvaluator targetingEvaluator = new TargetingEvaluator(new RequestContext(customerId, marketplaceId));
                                    TargetingPredicateResult result = null;
                                    try {
                                        result = targetingEvaluator.evaluate(targetingGroup);
                                    } catch (ExecutionException e) {
                                        e.printStackTrace();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    return result.isTrue();
                                }))
                        .collect(Collectors.toList());

                AdvertisementContent randomAdvertisementContent = .get(0);
                generatedAdvertisement = new GeneratedAdvertisement(randomAdvertisementContent);
            }

        return generatedAdvertisement;
    }
}
