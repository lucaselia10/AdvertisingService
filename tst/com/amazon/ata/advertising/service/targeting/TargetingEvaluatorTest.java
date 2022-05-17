package com.amazon.ata.advertising.service.targeting;

import com.amazon.ata.advertising.service.model.RequestContext;
import com.amazon.ata.advertising.service.targeting.predicate.TargetingPredicate;
import com.amazon.ata.advertising.service.targeting.predicate.TargetingPredicateResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class TargetingEvaluatorTest {
    private List<TargetingPredicate> targetingPredicates;
    TargetingGroup targetingGroup;

    @Mock
    private TargetingPredicate predicate1;

    @Mock
    private TargetingPredicate predicate2;

    @Mock
    private RequestContext requestContext;

    @InjectMocks
    private TargetingEvaluator targetingEvaluator;

    @BeforeEach
    public void setup() {
        initMocks(this);
        targetingPredicates = new ArrayList<>();
        targetingGroup = new TargetingGroup(UUID.randomUUID().toString(), UUID.randomUUID().toString(), 0, targetingPredicates);
    }

    @Test
    public void evaluateNoPredicates() throws ExecutionException, InterruptedException {
        TargetingPredicateResult result = targetingEvaluator.evaluate(targetingGroup);
        assertEquals(TargetingPredicateResult.TRUE, result);
    }

    @Test
    public void evaluateTruePredicate() throws ExecutionException, InterruptedException {
        when(predicate1.evaluate(requestContext)).thenReturn(TargetingPredicateResult.TRUE);
        targetingPredicates.add(predicate1);

        TargetingPredicateResult result = targetingEvaluator.evaluate(targetingGroup);
        assertEquals(TargetingPredicateResult.TRUE, result);
    }

    @Test
    public void evaluateFalsePredicate() throws ExecutionException, InterruptedException {
        when(predicate1.evaluate(requestContext)).thenReturn(TargetingPredicateResult.FALSE);
        targetingPredicates.add(predicate1);

        TargetingPredicateResult result = targetingEvaluator.evaluate(targetingGroup);
        assertEquals(TargetingPredicateResult.FALSE, result);
    }

    @Test
    public void evaluateIndeterminatePredicate() throws ExecutionException, InterruptedException {
        when(predicate1.evaluate(requestContext)).thenReturn(TargetingPredicateResult.INDETERMINATE);
        targetingPredicates.add(predicate1);

        TargetingPredicateResult result = targetingEvaluator.evaluate(targetingGroup);
        assertEquals(TargetingPredicateResult.FALSE, result);
    }

    @Test
    public void evaluateTrueAndFalsePredicate() throws ExecutionException, InterruptedException {
        when(predicate1.evaluate(requestContext)).thenReturn(TargetingPredicateResult.TRUE);
        when(predicate2.evaluate(requestContext)).thenReturn(TargetingPredicateResult.FALSE);
        targetingPredicates.add(predicate1);
        targetingPredicates.add(predicate2);

        TargetingPredicateResult result = targetingEvaluator.evaluate(targetingGroup);
        assertEquals(TargetingPredicateResult.FALSE, result);
    }

    @Test
    public void evaluateTrueAndIndeterminatePredicate() throws ExecutionException, InterruptedException {
        when(predicate1.evaluate(requestContext)).thenReturn(TargetingPredicateResult.TRUE);
        when(predicate2.evaluate(requestContext)).thenReturn(TargetingPredicateResult.INDETERMINATE);
        targetingPredicates.add(predicate1);
        targetingPredicates.add(predicate2);

        TargetingPredicateResult result = targetingEvaluator.evaluate(targetingGroup);
        assertEquals(TargetingPredicateResult.FALSE, result);
    }

    @Test
    public void evaluateFalseAndIndeterminatePredicate() throws ExecutionException, InterruptedException {
        when(predicate1.evaluate(requestContext)).thenReturn(TargetingPredicateResult.INDETERMINATE);
        when(predicate2.evaluate(requestContext)).thenReturn(TargetingPredicateResult.FALSE);
        targetingPredicates.add(predicate1);
        targetingPredicates.add(predicate2);

        TargetingPredicateResult result = targetingEvaluator.evaluate(targetingGroup);
        assertEquals(TargetingPredicateResult.FALSE, result);
    }

    @Test
    public void evaluateMultipleTruePredicates() throws ExecutionException, InterruptedException {
        when(predicate1.evaluate(requestContext)).thenReturn(TargetingPredicateResult.TRUE);
        when(predicate2.evaluate(requestContext)).thenReturn(TargetingPredicateResult.TRUE);
        targetingPredicates.add(predicate1);
        targetingPredicates.add(predicate2);

        TargetingPredicateResult result = targetingEvaluator.evaluate(targetingGroup);
        assertEquals(TargetingPredicateResult.TRUE, result);
    }

}
