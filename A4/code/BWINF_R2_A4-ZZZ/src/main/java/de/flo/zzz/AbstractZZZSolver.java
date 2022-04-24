package de.flo.zzz;

import de.flo.zzz.bitSequence.BitSequence;

import org.jlinalg.Matrix;
import org.jlinalg.f2.F2;

import java.util.List;

public abstract class AbstractZZZSolver {

    protected final ZZZProblem problem;

    protected final Matrix<F2> linearSystem;

    protected final List<BitSequence> bitSequences;

    public AbstractZZZSolver(ZZZProblem problem) {
        this.problem = problem;
        this.linearSystem = this.problem.createLinearSystem();
        this.bitSequences = this.problem.getBitSequences();
    }

    public abstract int[] getResult();
}
