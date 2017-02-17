/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.optaplanner.core.api.score.buildin.hardsoftbigdecimal;

import java.math.BigDecimal;

import org.kie.api.runtime.rule.RuleContext;
import org.optaplanner.core.api.score.Score;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.holder.AbstractScoreHolder;

/**
 * @see HardSoftBigDecimalScore
 */
public class HardSoftBigDecimalScoreHolder extends AbstractScoreHolder {

    protected BigDecimal hardScore = null;
    protected BigDecimal softScore = null;

    public HardSoftBigDecimalScoreHolder(boolean constraintMatchEnabled) {
        super(constraintMatchEnabled, HardSoftBigDecimalScore.ZERO);
    }

    public BigDecimal getHardScore() {
        return hardScore;
    }

    public BigDecimal getSoftScore() {
        return softScore;
    }

    // ************************************************************************
    // Worker methods
    // ************************************************************************

    /**
     * @param kcontext never null, the magic variable in DRL
     * @param weight higher is better, negative for a penalty, positive for a reward
     */
    public void addHardConstraintMatch(RuleContext kcontext, final BigDecimal weight) {
        hardScore = (hardScore == null) ? weight : hardScore.add(weight);
        registerConstraintMatch(kcontext,
                () -> hardScore = hardScore.subtract(weight),
                () -> HardSoftBigDecimalScore.valueOf(weight, BigDecimal.ZERO));
    }

    /**
     * @param kcontext never null, the magic variable in DRL
     * @param weight higher is better, negative for a penalty, positive for a reward
     */
    public void addSoftConstraintMatch(RuleContext kcontext, final BigDecimal weight) {
        softScore = (softScore == null) ? weight : softScore.add(weight);
        registerConstraintMatch(kcontext,
                () -> softScore = softScore.subtract(weight),
                () -> HardSoftBigDecimalScore.valueOf(BigDecimal.ZERO, weight));
    }

    @Override
    public Score extractScore(int initScore) {
        return HardSoftBigDecimalScore.valueOfUninitialized(initScore,
                hardScore == null ? BigDecimal.ZERO : hardScore,
                softScore == null ? BigDecimal.ZERO : softScore);
    }

}
