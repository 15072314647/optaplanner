/*
 * Copyright 2019 Red Hat, Inc. and/or its affiliates.
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

package org.optaplanner.core.impl.score.stream.bavet.bi;

import org.optaplanner.core.impl.score.stream.bavet.session.BavetAbstractNode;
import org.optaplanner.core.impl.score.stream.bavet.session.BavetConstraintSession;
import org.optaplanner.core.impl.score.stream.bavet.uni.BavetAbstractUniTuple;
import org.optaplanner.core.impl.score.stream.bavet.uni.BavetJoinLeftBridgeUniTuple;

public abstract class BavetAbstractBiNode<A, B> extends BavetAbstractNode {

    public BavetAbstractBiNode(BavetConstraintSession session, int nodeOrder) {
        super(session, nodeOrder);
    }

    public abstract BavetAbstractBiTuple<A, B> createTuple(BavetAbstractBiTuple<A, B> previousTuple);

    // ************************************************************************
    // Getters/setters
    // ************************************************************************

}
