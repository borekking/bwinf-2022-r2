package de.flo.zzz;

import de.flo.zzz.bruteforceAlgorithm.BruteforceZZZSolver;
import de.flo.zzz.solutionFromVectorSpace.ValidSolutionBruteforcer;
import de.flo.zzz.util.JLinAlgUtils;

import org.jlinalg.LinSysSolver;
import org.jlinalg.Matrix;
import org.jlinalg.Vector;
import org.jlinalg.f2.F2;

public class ZZZSolver extends AbstractZZZSolver {

    private final static int maxDimension = 3;

    private final static int maxK = 4;

    private enum SolvingType {
        BRUTEFORCE_BS_COMBINATIONS,
        BRUTEFORCE_GAUSS_SOLUTION,
        UNKNOWN;
    }

    /*
     * Procedure:
     *  - Create matrix and solve homogenous system (poly)
     *  - If amount of free variables smaller 3, do
     *    bruteforce of possible solutions (fake poly)
     *  - Else if n * n^(k/2) * c smaller ..., do
     *    bruteforce over all possible BS combination
     *  - Else ... ???
     *
     */

    private final Vector<F2>[] solutionSpace;

    private final SolvingType solvingType;

    public ZZZSolver(ZZZProblem problem) {
        super(problem);

        this.solutionSpace = this.solveHomogenousSystem(this.linearSystem);
        this.solvingType = this.getSolvingType();
    }

    @Override
    public int[] getResult() {
        switch (this.solvingType) {
            case BRUTEFORCE_BS_COMBINATIONS:
                return new BruteforceZZZSolver(this.problem).getResult();
            case BRUTEFORCE_GAUSS_SOLUTION:
                Vector<F2> solution = new ValidSolutionBruteforcer(this.solutionSpace, this.problem.n,
                        this.problem.size).getResult();
                return this.convertVectorIndexes(solution);
            case UNKNOWN:
            default:
                return new int[0];
        }
    }

    private int[] convertVectorIndexes(Vector<F2> solution) {
        int[] result = new int[this.problem.size];
        int i = 0;

        for (int j = 1; j <= this.problem.n; j++) {
            if (solution.getEntry(j) == F2.ONE) {
                result[i] = j - 1;
                i++;
            }
        }

        return result;
    }

    private Vector<F2>[] solveHomogenousSystem(Matrix<F2> matrix) {
        // Create zero vector
        Vector<F2> bVec = JLinAlgUtils.getZeroVector(matrix.getRows());

        // Solve Ax = 0 for solution space of x
        return LinSysSolver.solutionSpace(matrix, bVec).getGeneratingSystem();
    }

    private SolvingType getSolvingType() {
        int dimension = this.solutionSpace.length;
        if (dimension <= maxDimension) return SolvingType.BRUTEFORCE_GAUSS_SOLUTION;

        if (this.problem.k <= maxK) return SolvingType.BRUTEFORCE_BS_COMBINATIONS;

        return SolvingType.UNKNOWN;
    }
}
