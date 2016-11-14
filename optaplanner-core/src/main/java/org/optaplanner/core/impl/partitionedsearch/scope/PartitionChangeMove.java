/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
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

package org.optaplanner.core.impl.partitionedsearch.scope;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.impl.domain.entity.descriptor.EntityDescriptor;
import org.optaplanner.core.impl.domain.solution.descriptor.SolutionDescriptor;
import org.optaplanner.core.impl.domain.variable.descriptor.GenuineVariableDescriptor;
import org.optaplanner.core.impl.heuristic.move.AbstractMove;
import org.optaplanner.core.impl.heuristic.move.Move;
import org.optaplanner.core.impl.score.director.InnerScoreDirector;
import org.optaplanner.core.impl.score.director.ScoreDirector;

/**
 * @param <Solution_> the solution type, the class with the {@link PlanningSolution} annotation
 */
public final class PartitionChangeMove<Solution_> extends AbstractMove {

    public static <Solution_> PartitionChangeMove<Solution_> createMove(InnerScoreDirector<Solution_> scoreDirector) {
        SolutionDescriptor<Solution_> solutionDescriptor = scoreDirector.getSolutionDescriptor();
        Solution_ workingSolution = scoreDirector.getWorkingSolution();

        int entityCount = solutionDescriptor.getEntityCount(workingSolution);
        Map<GenuineVariableDescriptor<Solution_>, List<Pair<Object, Object>>> changeMap
                = new LinkedHashMap<>(solutionDescriptor.getEntityDescriptors().size() * 3);
        for (EntityDescriptor<Solution_> entityDescriptor : solutionDescriptor.getEntityDescriptors()) {
            for (GenuineVariableDescriptor<Solution_> variableDescriptor
                    : entityDescriptor.getDeclaredGenuineVariableDescriptors()) {
                changeMap.put(variableDescriptor, new ArrayList<>(entityCount));
            }
        }
        for (Iterator<Object> it = solutionDescriptor.extractAllEntitiesIterator(workingSolution); it.hasNext();) {
            Object entity = it.next();
            EntityDescriptor<Solution_> entityDescriptor = solutionDescriptor.findEntityDescriptorOrFail(
                    entity.getClass());
            if (entityDescriptor.isMovable(scoreDirector, entity)) {
                for (GenuineVariableDescriptor<Solution_> variableDescriptor
                        : entityDescriptor.getGenuineVariableDescriptors()) {
                    Object value = variableDescriptor.getValue(entity);
                    changeMap.get(variableDescriptor).add(Pair.of(entity, value));
                }
            }
        }
        return new PartitionChangeMove<>(changeMap);
    }

    private final Map<GenuineVariableDescriptor<Solution_>, List<Pair<Object, Object>>> changeMap;

    public PartitionChangeMove(Map<GenuineVariableDescriptor<Solution_>, List<Pair<Object, Object>>> changeMap) {
        this.changeMap = changeMap;
    }

    @Override
    protected void doMoveOnGenuineVariables(ScoreDirector scoreDirector) {
        for (Map.Entry<GenuineVariableDescriptor<Solution_>, List<Pair<Object, Object>>> entry : changeMap.entrySet()) {
            GenuineVariableDescriptor<Solution_> variableDescriptor = entry.getKey();
            for (Pair<Object, Object> pair : entry.getValue()) {
                Object entity = pair.getKey();
                Object value = pair.getValue();
                scoreDirector.changeVariableFacade(variableDescriptor, entity, value);
            }
        }
    }

    @Override
    public boolean isMoveDoable(ScoreDirector scoreDirector) {
        return true;
    }

    @Override
    public Move createUndoMove(ScoreDirector scoreDirector) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<? extends Object> getPlanningEntities() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<? extends Object> getPlanningValues() {
        throw new UnsupportedOperationException();
    }

    public PartitionChangeMove<Solution_> relocate(InnerScoreDirector<Solution_> destinationScoreDirector) {
        Map<GenuineVariableDescriptor<Solution_>, List<Pair<Object, Object>>> destinationChangeMap
                = new LinkedHashMap<>(changeMap.size());
        for (Map.Entry<GenuineVariableDescriptor<Solution_>, List<Pair<Object, Object>>> entry : changeMap.entrySet()) {
            GenuineVariableDescriptor<Solution_> variableDescriptor = entry.getKey();
            List<Pair<Object, Object>> originPairList = entry.getValue();
            List<Pair<Object, Object>> destinationPairList = new ArrayList<>(originPairList.size());
            for (Pair<Object, Object> pair : originPairList) {
                Object originEntity = pair.getKey();
                Object originValue = pair.getValue();
                Object destinationEntity = destinationScoreDirector.locateWorkingObject(originEntity);
                Object destinationValue = destinationScoreDirector.locateWorkingObject(originValue);
                destinationPairList.add(Pair.of(destinationEntity, destinationValue));
            }
            destinationChangeMap.put(variableDescriptor, destinationPairList);
        }
        return new PartitionChangeMove<>(destinationChangeMap);
    }

}
