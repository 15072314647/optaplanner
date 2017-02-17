/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
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

package org.optaplanner.core.api.score.buildin.hardmediumsoftlong;

import org.kie.api.runtime.rule.RuleContext;
import org.optaplanner.core.api.score.Score;
import org.optaplanner.core.api.score.buildin.hardmediumsoft.HardMediumSoftScore;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.holder.AbstractScoreHolder;

/**
 * @see HardMediumSoftScore
 */
public class HardMediumSoftLongScoreHolder extends AbstractScoreHolder {

    protected long hardScore;
    protected long mediumScore;
    protected long softScore;

    public HardMediumSoftLongScoreHolder(boolean constraintMatchEnabled) {
        super(constraintMatchEnabled, HardMediumSoftLongScore.ZERO);
    }

    public long getHardScore() {
        return hardScore;
    }

    public long getMediumScore() {
        return mediumScore;
    }

    public long getSoftScore() {
        return softScore;
    }

    // ************************************************************************
    // Worker methods
    // ************************************************************************

    /**
     * @param kcontext never null, the magic variable in DRL
     * @param weight higher is better, negative for a penalty, positive for a reward
     */
    public void addHardConstraintMatch(RuleContext kcontext, final long weight) {
        hardScore += weight;
        registerConstraintMatch(kcontext,
                () -> hardScore -= weight,
                () -> HardMediumSoftLongScore.valueOf(weight, 0L, 0L));
    }

    /**
     * @param kcontext never null, the magic variable in DRL
     * @param weight higher is better, negative for a penalty, positive for a reward
     */
    public void addMediumConstraintMatch(RuleContext kcontext, final long weight) {
        mediumScore += weight;
        registerConstraintMatch(kcontext,
                () -> mediumScore -= weight,
                () -> HardMediumSoftLongScore.valueOf(0L, weight, 0L));
    }

    /**
     * @param kcontext never null, the magic variable in DRL
     * @param weight higher is better, negative for a penalty, positive for a reward
     */
    public void addSoftConstraintMatch(RuleContext kcontext, final long weight) {
        softScore += weight;
        registerConstraintMatch(kcontext,
                () -> softScore -= weight,
                () -> HardMediumSoftLongScore.valueOf(0L, 0L, weight));
    }

    @Override
    public Score extractScore(int initScore) {
        return HardMediumSoftLongScore.valueOfUninitialized(initScore, hardScore, mediumScore, softScore);
    }

}
