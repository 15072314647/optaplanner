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

package org.optaplanner.core.impl.partitionedsearch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.config.heuristic.policy.HeuristicConfigPolicy;
import org.optaplanner.core.config.phase.PhaseConfig;
import org.optaplanner.core.config.solver.recaller.BestSolutionRecallerConfig;
import org.optaplanner.core.impl.partitionedsearch.partitioner.SolutionPartitioner;
import org.optaplanner.core.impl.phase.AbstractPhase;
import org.optaplanner.core.impl.phase.Phase;
import org.optaplanner.core.impl.solver.ChildThreadType;
import org.optaplanner.core.impl.solver.recaller.BestSolutionRecaller;
import org.optaplanner.core.impl.solver.scope.DefaultSolverScope;
import org.optaplanner.core.impl.solver.termination.Termination;

/**
 * Default implementation of {@link PartitionedSearchPhase}.
 * @param <Solution_> the solution type, the class with the {@link PlanningSolution} annotation
 */
public class DefaultPartitionedSearchPhase<Solution_> extends AbstractPhase<Solution_>
        implements PartitionedSearchPhase<Solution_> {

    protected ExecutorService executorService;
    protected SolutionPartitioner<Solution_> solutionPartitioner;
    protected List<PhaseConfig> phaseConfigList;
    protected HeuristicConfigPolicy configPolicy;

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public void setSolutionPartitioner(SolutionPartitioner<Solution_> solutionPartitioner) {
        this.solutionPartitioner = solutionPartitioner;
    }

    public void setPhaseConfigList(List<PhaseConfig> phaseConfigList) {
        this.phaseConfigList = phaseConfigList;
    }

    public void setConfigPolicy(HeuristicConfigPolicy configPolicy) {
        this.configPolicy = configPolicy;
    }

    @Override
    public String getPhaseTypeString() {
        return "Partitioned Search";
    }

    // ************************************************************************
    // Worker methods
    // ************************************************************************

    @Override
    public void solve(DefaultSolverScope<Solution_> solverScope) {
//        PartitionedSearchPhaseScope<Solution_> phaseScope = new PartitionedSearchPhaseScope<>(solverScope);
//        phaseStarted(phaseScope);
        List<Solution_> partList = solutionPartitioner.splitWorkingSolution(solverScope.getScoreDirector());
        List<Future> futureList = new ArrayList<>(partList.size());
        for (Solution_ part : partList) {
            PartitionSolver<Solution_> partitionSolver = buildPartitionSolver(solverScope);
            Future<?> future = executorService.submit(() -> partitionSolver.solve(part));
            futureList.add(future);
        }
        for (Future future : futureList) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace(); // TODO remove
            }
        }
//        phaseEnded(phaseScope);

//
//        while (!termination.isPhaseTerminated(phaseScope)) {
//            PartitionedSearchStepScope<Solution_> stepScope = new PartitionedSearchStepScope<>(phaseScope);
//            stepScope.setTimeGradient(termination.calculatePhaseTimeGradient(phaseScope));
//            stepStarted(stepScope);
//            decider.decideNextStep(stepScope);
//            if (stepScope.getStep() == null) {
//                if (termination.isPhaseTerminated(phaseScope)) {
//                    logger.trace("    Step index ({}), time spent ({}) terminated without picking a nextStep.",
//                            stepScope.getStepIndex(),
//                            stepScope.getPhaseScope().calculateSolverTimeMillisSpentUpToNow());
//                } else if (stepScope.getSelectedMoveCount() == 0L) {
//                    logger.warn("    No doable selected move at step index ({}), time spent ({})."
//                            + " Terminating phase early.",
//                            stepScope.getStepIndex(),
//                            stepScope.getPhaseScope().calculateSolverTimeMillisSpentUpToNow());
//                } else {
//                    throw new IllegalStateException("The step index (" + stepScope.getStepIndex()
//                            + ") has accepted/selected move count (" + stepScope.getAcceptedMoveCount() + "/"
//                            + stepScope.getSelectedMoveCount()
//                            + ") but failed to pick a nextStep (" + stepScope.getStep() + ").");
//                }
//                // Although stepStarted has been called, stepEnded is not called for this step
//                break;
//            }
//            doStep(stepScope);
//            stepEnded(stepScope);
//            phaseScope.setLastCompletedStepScope(stepScope);
//        }
    }

    public PartitionSolver<Solution_> buildPartitionSolver(DefaultSolverScope<Solution_> solverScope) {
        Termination partTermination = termination.createChildThreadTermination(solverScope, ChildThreadType.PART_THREAD);
        BestSolutionRecaller<Solution_> bestSolutionRecaller = new BestSolutionRecallerConfig()
                .buildBestSolutionRecaller(configPolicy.getEnvironmentMode());
        List<Phase<Solution_>> phaseList = new ArrayList<>(phaseConfigList.size());
        int partPhaseIndex = 0;
        for (PhaseConfig phaseConfig : phaseConfigList) {
            phaseList.add(phaseConfig.buildPhase(partPhaseIndex, configPolicy, bestSolutionRecaller, partTermination));
            partPhaseIndex++;
        }
        // TODO create PartitionSolverScope alternative to deal with 3 layer terminations
        DefaultSolverScope<Solution_> partSolverScope = solverScope.createChildThreadSolverScope(ChildThreadType.PART_THREAD);
        return new PartitionSolver<>(partTermination, bestSolutionRecaller, phaseList, partSolverScope);
    }

    //    private void doStep(PartitionedSearchStepScope<Solution_> stepScope) {
//        Move nextStep = stepScope.getStep();
//        nextStep.doMove(stepScope.getScoreDirector());
//        predictWorkingStepScore(stepScope, nextStep);
//        bestSolutionRecaller.processWorkingSolutionDuringStep(stepScope);
//    }
//
//    @Override
//    public void solvingStarted(DefaultSolverScope<Solution_> solverScope) {
//        super.solvingStarted(solverScope);
//        decider.solvingStarted(solverScope);
//    }
//
//    @Override
//    public void phaseStarted(PartitionedSearchPhaseScope<Solution_> phaseScope) {
//        super.phaseStarted(phaseScope);
//        decider.phaseStarted(phaseScope);
//        // TODO maybe this restriction should be lifted to allow PartitionedSearch to initialize a solution too?
//        assertWorkingSolutionInitialized(phaseScope);
//    }
//
//    @Override
//    public void stepStarted(PartitionedSearchStepScope<Solution_> stepScope) {
//        super.stepStarted(stepScope);
//        decider.stepStarted(stepScope);
//    }
//
//    @Override
//    public void stepEnded(PartitionedSearchStepScope<Solution_> stepScope) {
//        super.stepEnded(stepScope);
//        decider.stepEnded(stepScope);
//        PartitionedSearchPhaseScope phaseScope = stepScope.getPhaseScope();
//        if (logger.isDebugEnabled()) {
//            logger.debug("    LS step ({}), time spent ({}), score ({}), {} best score ({})," +
//                    " accepted/selected move count ({}/{}), picked move ({}).",
//                    stepScope.getStepIndex(),
//                    phaseScope.calculateSolverTimeMillisSpentUpToNow(),
//                    stepScope.getScore(),
//                    (stepScope.getBestScoreImproved() ? "new" : "   "), phaseScope.getBestScore(),
//                    stepScope.getAcceptedMoveCount(),
//                    stepScope.getSelectedMoveCount(),
//                    stepScope.getStepString());
//        }
//    }
//
//    @Override
//    public void phaseEnded(PartitionedSearchPhaseScope<Solution_> phaseScope) {
//        super.phaseEnded(phaseScope);
//        decider.phaseEnded(phaseScope);
//        phaseScope.endingNow();
//        logger.info("Local Search phase ({}) ended: time spent ({}), best score ({}),"
//                        + " score calculation speed ({}/sec), step total ({}).",
//                phaseIndex,
//                phaseScope.calculateSolverTimeMillisSpentUpToNow(),
//                phaseScope.getBestScore(),
//                phaseScope.getPhaseScoreCalculationSpeed(),
//                phaseScope.getNextStepIndex());
//    }
//
//    @Override
//    public void solvingEnded(DefaultSolverScope<Solution_> solverScope) {
//        super.solvingEnded(solverScope);
//        decider.solvingEnded(solverScope);
//    }

}
