/*
 * Copyright 2010 Red Hat, Inc. and/or its affiliates.
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

package org.optaplanner.core.api.score.buildin.hardsoft;

import org.kie.api.runtime.rule.RuleContext;
import org.optaplanner.core.api.score.Score;
import org.optaplanner.core.api.score.holder.AbstractScoreHolder;

/**
 * @see HardSoftScore
 */
public class HardSoftScoreHolder extends AbstractScoreHolder {

    protected int hardScore;
    protected int softScore;

    public HardSoftScoreHolder(boolean constraintMatchEnabled) {
        super(constraintMatchEnabled, HardSoftScore.ZERO);
    }

    public int getHardScore() {
        return hardScore;
    }

    public int getSoftScore() {
        return softScore;
    }

    // ************************************************************************
    // Worker methods
    // ************************************************************************

    /**
     * @param kcontext never null, the magic variable in DRL
     * @param weight higher is better, negative for a penalty, positive for a reward
     */
    public void addHardConstraintMatch(RuleContext kcontext, final int weight) {
        hardScore += weight;
        registerConstraintMatch(kcontext,
                () -> hardScore -= weight,
                () -> HardSoftScore.valueOf(weight, 0));
    }

    /**
     * @param kcontext never null, the magic variable in DRL
     * @param weight higher is better, negative for a penalty, positive for a reward
     */
    public void addSoftConstraintMatch(RuleContext kcontext, final int weight) {
        softScore += weight;
        registerConstraintMatch(kcontext,
                () -> softScore -= weight,
                () -> HardSoftScore.valueOf(0, weight));
    }

    @Override
    public Score extractScore(int initScore) {
        return HardSoftScore.valueOfUninitialized(initScore, hardScore, softScore);
    }

}
